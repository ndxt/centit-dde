package com.centit.dde.utils;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.algorithm.ZipCompressor;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileType;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public abstract class FileDataSetOptUtil {

    public static FileDataSet mapDataToFile(Map<String, Object> objectMap,
                                            String fileNameDesc, String fileContentDesc){
        if(objectMap==null)
            return null;

        String fileName = StringUtils.isNotBlank(fileNameDesc)?
            StringBaseOpt.castObjectToString(objectMap.get(fileNameDesc)) : null;

        if (StringUtils.isBlank(fileName)) {
            fileName = StringBaseOpt.castObjectToString(objectMap.get(ConstantValue.FILE_NAME));
        }

        Object fileData =null;
        if(StringUtils.isNotBlank(fileContentDesc)) {
            fileData = objectMap.get(fileContentDesc);
        }
        if (fileData == null) {
            fileData = objectMap.get(ConstantValue.FILE_CONTENT);
        }

        if(fileData == null)
            return null;

        HashMap<String, Object> fileInfo = new HashMap<>();
        for(Map.Entry<String, Object> entry : objectMap.entrySet()){
            if(! StringUtils.equalsAny(entry.getKey(),
                ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT, fileNameDesc, fileContentDesc)){
                fileInfo.put(entry.getKey(), entry.getValue());
            }
        }
        FileDataSet fileDataset =  new FileDataSet();
        fileDataset.setFileInfo(fileInfo);

        fileDataset.setFileContent(fileName,
            NumberBaseOpt.castObjectToLong(objectMap.get(ConstantValue.FILE_SIZE), -1l),
            fileData);
        return fileDataset;
    }
    public static FileDataSet castToFileDataSet(DataSet dataSet){
        if(dataSet==null){
            return null;
        }
        if(dataSet instanceof FileDataSet)
            return (FileDataSet) dataSet;
        return mapDataToFile(dataSet.getFirstRow(), null, null);
    }

    public static InputStream getInputStreamFormFile(Map<String, Object> fileInfo){
        Object data = fileInfo.get(ConstantValue.FILE_CONTENT);
        return FileIOOpt.castObjectToInputStream(data);
    }

    public static InputStream getInputStreamFormDataSet(DataSet dataSet){
        if(dataSet instanceof FileDataSet){
            return ((FileDataSet)dataSet).getFileInputStream();
        }
        return getInputStreamFormFile(dataSet.getFirstRow());
    }

    public static FileDataSet zipFileDatasetList(String fileName, List<FileDataSet> files){
        FileDataSet fileDataset = new FileDataSet();
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        try(ZipOutputStream out = ZipCompressor.convertToZipOutputStream(outBuf)) {
            Map<String, Integer> fileNameMap = new HashMap<>(files.size() + 4);
            for (FileDataSet ds : files) {
                InputStream inputStream = ds.getFileInputStream();
                String fn = ds.getFileName();
                while (fileNameMap.containsKey(fn)) {
                    int copies = fileNameMap.get(fn) + 1;
                    fileNameMap.put(fn, copies);
                    fn = FileType.truncateFileExtNameWithPath(fn) + "(" + copies + ")." + FileType.getFileExtName(fn);
                }
                fileNameMap.put(fn, 1);
                ZipCompressor.compressFile(inputStream, fn, out, "");
            }
        } catch (IOException e) {
            throw new ObjectException(ObjectException.DATA_NOT_INTEGRATED, "压缩多个文件时报错："+ e.getMessage(), e);
        }
        fileDataset.setFileContent(fileName, outBuf.size(), outBuf);
        return fileDataset;
    }

    public static List<FileDataSet> unzipFileDatasetList(String tempPath, FileDataSet zipFileDataset) throws IOException{
        String fileName = tempPath + File.separatorChar + UuidOpt.getUuidAsString32()+".zip";
        FileIOOpt.writeInputStreamToFile(zipFileDataset.getFileInputStream(), fileName);
        List<FileDataSet> files = new ArrayList<>();
        try (ZipFile zip = new ZipFile(fileName)) {
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                if(entry.isDirectory()) continue;
                try (InputStream in = zip.getInputStream(entry);
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buf1 = new byte[1024];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                    FileDataSet fileDataSet = new FileDataSet(zipEntryName, out.size(), out);
                    files.add(fileDataSet);
                }
            }
        }
        return files;
    }

    public static FileDataSet attainFileDataset(BizModel bizModel, DataSet dataSet, JSONObject jsonStep, boolean singleFile){

        String fileNameDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_NAME, "");
        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel, dataSet.getData());
        String fileName = null;

        if(StringUtils.isNotBlank(fileNameDesc)){
            fileName = StringBaseOpt.objectToString(JSONTransformer.transformer(fileNameDesc, transformer));
        }

        if(dataSet instanceof FileDataSet){
            FileDataSet fileDataSet = (FileDataSet) dataSet;
            String currentFileName =  fileDataSet.getFileName();
            if(StringUtils.isNotBlank(fileName) && (StringUtils.isBlank(currentFileName) || StringUtils.equals(
                FileType.getFileExtName(currentFileName), FileType.getFileExtName(fileName)))){
                fileDataSet.setFileName(fileName);
            }
            return fileDataSet;
        }

        String fileContentDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_CONTENT, "");

        if(singleFile || dataSet.getSize()==1) {
            Map<String, Object> mapFirstRow = dataSet.getFirstRow();
            FileDataSet fileDataset = mapDataToFile(mapFirstRow, fileNameDesc, fileContentDesc);
            if(fileDataset==null){
                Object fileData = JSONTransformer.transformer(fileContentDesc, transformer);
                if(fileData==null){
                    throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "文件数据获取失败");
                }
                fileDataset = new FileDataSet(fileName,
                    NumberBaseOpt.castObjectToLong(jsonStep.get(ConstantValue.FILE_SIZE), -1l),
                    fileData);
            } else if (StringUtils.isNotBlank(fileName)) {
                fileDataset.setFileName(fileName);
            }
            return fileDataset;
        }

        List<FileDataSet> files = new ArrayList<>();
        for(Map<String, Object> objectMap : dataSet.getDataAsList()){
            FileDataSet fileDataSet = mapDataToFile(objectMap, fileNameDesc, fileContentDesc);
            if(fileDataSet != null){
                files.add(fileDataSet);
            }
        }
        if(files.isEmpty()){
            throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "文件数据获取失败");
        }

        if(files.size() == 1){
            FileDataSet ds = files.get(0);
            String currentFileName = ds.getFileName();
            if(StringUtils.isNotBlank(fileName) && (StringUtils.isBlank(currentFileName) || StringUtils.equals(
                FileType.getFileExtName(currentFileName), FileType.getFileExtName(fileName))) ){
                ds.setFileName(fileName);
            }
            return ds;
        }

        if(StringUtils.isBlank(fileName)){
            fileName ="download.zip";
        } else if(!fileName.toLowerCase().endsWith(".zip")) {
            fileName = fileName + ".zip";
        }
        return zipFileDatasetList(fileName, files);
    }

}
