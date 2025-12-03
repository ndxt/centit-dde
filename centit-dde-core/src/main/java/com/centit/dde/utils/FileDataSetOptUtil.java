package com.centit.dde.utils;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileSystemOpt;
import com.centit.support.file.FileType;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

import java.nio.file.Files;
import java.util.*;


public abstract class FileDataSetOptUtil {

    public static FileDataSet mapDataToFile(Map<String, Object> objectMap,
                                            String fileNameDesc, String fileContentDesc) {
        if (objectMap == null)
            return null;

        String fileName = null;
        if (StringUtils.isNotBlank(fileNameDesc)) {
            fileName = StringBaseOpt.castObjectToString(objectMap.get(fileNameDesc));
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = StringBaseOpt.castObjectToString(objectMap.get(ConstantValue.FILE_NAME));
        }

        Object fileData = null;
        if (StringUtils.isNotBlank(fileContentDesc)) {
            fileData = objectMap.get(fileContentDesc);
        }
        if (fileData == null) {
            fileData = objectMap.get(ConstantValue.FILE_CONTENT);
        }

        if (fileData == null)
            return null;

        HashMap<String, Object> fileInfo = new HashMap<>();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            if (!StringUtils.equalsAny(entry.getKey(),
                ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT, fileNameDesc, fileContentDesc)) {
                fileInfo.put(entry.getKey(), entry.getValue());
            }
        }
        FileDataSet fileDataset = new FileDataSet();
        fileDataset.setFileInfo(fileInfo);

        fileDataset.setFileContent(fileName,
            NumberBaseOpt.castObjectToLong(objectMap.get(ConstantValue.FILE_SIZE), -1L),
            fileData);
        return fileDataset;
    }

    public static FileDataSet castToFileDataSet(DataSet dataSet) {
        if (dataSet == null) {
            return null;
        }
        if (dataSet instanceof FileDataSet)
            return (FileDataSet) dataSet;
        return mapDataToFile(dataSet.getFirstRow(), null, null);
    }

    public static InputStream getInputStreamFormFile(Map<String, Object> fileInfo) {
        Object data = fileInfo.get(ConstantValue.FILE_CONTENT);
        return FileIOOpt.castObjectToInputStream(data);
    }

    public static InputStream getInputStreamFormDataSet(DataSet dataSet) {
        if (dataSet instanceof FileDataSet) {
            return ((FileDataSet) dataSet).getFileInputStream();
        }
        return getInputStreamFormFile(dataSet.getFirstRow());
    }

    public static FileDataSet zipFileDatasetList(String fileName, List<FileDataSet> files) {
        long totalSize = 0;
        // 计算总文件大小并检查限制
        for (FileDataSet ds : files) {
            long fileSize = ds.getFileSize();
            if (fileSize > 0) {
                totalSize += fileSize;
            }
            // 单个文件大小检查可在此添加
        }
        // 检查可用内存
        Runtime runtime = Runtime.getRuntime();
        long availableMemory = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
        if (totalSize > availableMemory * 0.8) { // 使用不超过80%可用内存
            throw new ObjectException(ObjectException.DATA_NOT_INTEGRATED,
                "可用内存不足，无法完成压缩操作");
        }
        FileDataSet fileDataset = new FileDataSet();
        // 使用临时文件进行流式处理，避免内存溢出
        File tempZipFile = null;
        try {
            tempZipFile = File.createTempFile("zip_", ".tmp");
            try (FileOutputStream fos = new FileOutputStream(tempZipFile);
                 ZipArchiveOutputStream out = new ZipArchiveOutputStream(fos)) {

                out.setEncoding("UTF-8");
                out.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
                Map<String, Integer> fileNameMap = new HashMap<>(files.size() + 4);

                // 缓冲区复用
                byte[] buffer = new byte[8192];

                for (FileDataSet ds : files) {
                    try (InputStream inputStream = ds.getFileInputStream()) {
                        if (inputStream == null) {
                            continue;
                        }

                        String fn = ds.getFileName();
                        while (fileNameMap.containsKey(fn)) {
                            int copies = fileNameMap.get(fn) + 1;
                            fileNameMap.put(fn, copies);
                            fn = FileType.truncateFileExtNameWithPath(fn) + "(" + copies + ")." +
                                FileType.getFileExtName(fn);
                        }
                        fileNameMap.put(fn, 1);

                        ZipArchiveEntry zipEntry = new ZipArchiveEntry(fn);
                        out.putArchiveEntry(zipEntry);

                        int len;
                        while ((len = inputStream.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        out.closeArchiveEntry();
                    } catch (Exception e) {
                        throw new ObjectException(ObjectException.DATA_NOT_INTEGRATED,
                            "压缩文件报错：" + e.getMessage(), e);
                    }
                }
            } catch (IOException e) {
                throw new ObjectException(ObjectException.DATA_NOT_INTEGRATED,
                    "压缩多个文件时报错：" + e.getMessage(), e);
            }
            // 将临时文件内容读取到内存中返回
            try (FileInputStream fis = new FileInputStream(tempZipFile);
                 ByteArrayOutputStream outBuf = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    outBuf.write(buffer, 0, len);
                }
                fileDataset.setFileContent(fileName, outBuf.size(), outBuf);
            }

        } catch (IOException e) {
            throw new ObjectException(ObjectException.DATA_NOT_INTEGRATED,
                "创建临时文件失败：" + e.getMessage(), e);
        } finally {
            // 确保临时文件被删除
            if (tempZipFile != null && tempZipFile.exists()) {
                try {
                    Files.delete(tempZipFile.toPath());
                } catch (IOException e) {
                    // 记录日志但不中断流程
                }
            }
        }
        return fileDataset;
    }


    public static List<FileDataSet> unzipFileDatasetList(String tempPath, FileDataSet zipFileDataset) throws IOException {
        String fileName = tempPath + File.separatorChar + UuidOpt.getUuidAsString32() + ".zip";
        FileIOOpt.writeInputStreamToFile(zipFileDataset.getFileInputStream(), fileName);
        List<FileDataSet> files = new ArrayList<>();

        // 使用 Apache Commons Compress，自动处理编码问题
        try (ZipFile zip =
                 new ZipFile(new File(fileName))) {

            // 使用 Enumeration 的传统遍历方式
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                if (entry.isDirectory()) continue;

                try (InputStream in = zip.getInputStream(entry);
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    byte[] buf1 = new byte[1024];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                    FileDataSet fileDataSet = new FileDataSet(zipEntryName, out.size(), out);
                    files.add(fileDataSet);
                } catch (Exception e) {
                    // 处理单个文件异常，继续处理其他文件
                    continue;
                }
            }
        }

        // 删除临时文件
        FileSystemOpt.deleteFile(fileName);
        return files;
    }


    public static List<FileDataSet> fetchFiles(DataSet dataSet, JSONObject jsonStep) {
        List<FileDataSet> files = new ArrayList<>();
        if (dataSet instanceof FileDataSet) {
            files.add((FileDataSet) dataSet);
            return files;
        }

        String fileNameDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_NAME, "");
        String fileContentDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_CONTENT, "");
        for (Map<String, Object> objectMap : dataSet.getDataAsList()) {
            FileDataSet fileDataSet = mapDataToFile(objectMap, fileNameDesc, fileContentDesc);
            if (fileDataSet != null) {
                files.add(fileDataSet);
            }
        }
        return files;
    }

    public static FileDataSet attainFileDataset(BizModel bizModel, DataSet dataSet, JSONObject jsonStep, boolean singleFile) {
        String fileNameDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_NAME, "");
        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel, dataSet.getData());
        String fileName = null;
        if (StringUtils.isNotBlank(fileNameDesc)) {
            fileName = StringBaseOpt.objectToString(JSONTransformer.transformer(fileNameDesc, transformer));
        }

        List<FileDataSet> files = fetchFiles(dataSet, jsonStep);
        if (files.isEmpty()) {
            throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "文件数据获取失败");
        }

        if (singleFile || files.size() == 1) {
            FileDataSet ds = files.get(0);
            String currentFileName = ds.getFileName();
            if (StringUtils.isNotBlank(fileName) && (StringUtils.isBlank(currentFileName) || StringUtils.equals(
                FileType.getFileExtName(currentFileName), FileType.getFileExtName(fileName)))) {
                ds.setFileName(fileName);
            }
            return ds;
        }

        if (StringUtils.isBlank(fileName)) {
            fileName = "download.zip";
        } else if (!fileName.toLowerCase().endsWith(".zip")) {
            fileName = fileName + ".zip";
        }
        return zipFileDatasetList(fileName, files);
    }

}
