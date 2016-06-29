package com.centit.app.service.impl;

import com.centit.app.dao.FileinfoDao;
import com.centit.app.po.Fileinfo;
import com.centit.app.service.FileinfoManager;
import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.util.SysParametersUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * TODO Class description should be added
 *
 * @author zk
 * @create 2013-3-6
 */
public class FileinfoManagerImpl extends BaseEntityManagerImpl<Fileinfo>
        implements FileinfoManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(FileinfoManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private FileinfoDao fileinfoDao;

    public void setFileinfoDao(FileinfoDao baseDao) {
        this.fileinfoDao = baseDao;
        setBaseDao(this.fileinfoDao);
    }

    static private final  String pathDF =  "yyyyMMdd";
    static private final  String nameDF ="yyyyMMddHHmmss";

    public String getNextKey() {
        /*String sKey = "00000000000"+
                fileinfoDao.getNextValueOfSequence("S_FILENO");
        return df.format(new Date()) + sKey.substring(sKey.length()-10);*/

        return fileinfoDao.getNextValueOfSequence("S_FILENO");
    }


    /* (non-Javadoc)
     * @see com.centit.app.service.FileinfoManager#saveFileinfo(com.centit.app.po.Fileinfo, java.io.InputStream, java.lang.String)
     */
    @Override
    public Fileinfo saveFileinfo(Fileinfo info, InputStream is, String optID) throws IOException {
        String path = File.separator + optID;
        path = FileinfoManagerImpl.getFilePath(path,                
                new SimpleDateFormat(pathDF).format(DatetimeOpt.currentUtilDate()));

        // 获取文件路径
        String fullPath = getFullFilePath(path);

        File root = new File(fullPath);
        if (!root.exists()) {
            FileUtils.forceMkdir(root);
        }

        String filename = new SimpleDateFormat(nameDF).format(DatetimeOpt.currentUtilDate()) +
                "_" + FileinfoManagerImpl.getFileName(info.getFilename(), 
                            info.getFileext());
        File file = new File(getFilePath(fullPath, filename));

        // 复制文件
        FileUtils.copyInputStreamToFile(is, file);

        // 保存文件对象
        info.setFilesize(file.length());
        info.setFilecode(getNextKey());
        info.setPath(getFilePath(path, filename));
        fileinfoDao.saveObject(info);

        return info;
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.FileinfoManager#saveFileinfo(com.centit.app.po.Fileinfo, java.io.File)
     */
    @Override
    public Fileinfo saveFileinfo(Fileinfo info, File file) {

        String filename = file.getName();

        info.setFilename(FilenameUtils.getBaseName(filename));
        info.setFileext(FilenameUtils.getExtension(filename));

        // 重写路径，去除上传文件路径前缀
        info.setPath(StringUtils.replace(FilenameUtils.normalize(file.getPath()), SysParametersUtils.getUploadHome(), ""));
        info.setFilesize(file.length());
        info.setFilecode(getNextKey());

        info.setIndb("0");
        info.setInstid(0L);
        info.setIsindex("0");
        info.setRecorderDate(new Date());

        fileinfoDao.saveObject(info);
        return info;
    }


    /* (non-Javadoc)
     * @see com.centit.app.service.FileinfoManager#update4ConfirmFileinfo(com.centit.app.po.Fileinfo)
     */
    @Override
    public Fileinfo update4ConfirmFileinfo(Fileinfo info) {

        info.setInstid(1L);
        fileinfoDao.saveObject(info);

        return info;
    }

    @Override
    public void update4ConfirmFileinfos(Collection<Fileinfo> infos) {
        for (Fileinfo info : infos) {
            update4ConfirmFileinfo(info);
        }
    }

    @Override
    public Fileinfo updateFileinfoCopy(Fileinfo file, String path) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Fileinfo updateFileinfoCut(Fileinfo file, String path) {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see com.centit.app.service.FileinfoManager#deleteFileinfo(com.centit.app.po.Fileinfo)
     */
    @Override
    public boolean deleteFileinfo(Fileinfo info) {
        if (null == info) {
            return true;
        }

        String path = getFullFilePath(info.getPath());
        File file = new File(path);

        try {
            fileinfoDao.deleteObject(info);
            return FileUtils.deleteQuietly(file);
        } catch (Exception e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.FileinfoManager#deleteFileOnDisk(com.centit.app.po.Fileinfo)
     */
    @Override
    public boolean deleteFileOnDisk(Fileinfo info) {
        if (null == info) {
            return true;
        }

        String path = getFullFilePath(info.getPath());
        File file = new File(path);

        // 删除文件
        return FileUtils.deleteQuietly(file);
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.FileinfoManager#deleteFileOnDisk(java.lang.String)
     */
    @Override
    public boolean deleteFileOnDisk(String path) {
        String fullPath = getFullFilePath(path);
        File file = new File(fullPath);

        // 若文件不存在，则无需删除
        if (!file.exists()) return true;

        // 删除文件
        return FileUtils.deleteQuietly(file);
    }


    @Override
    public InputStream downloadFileinfo(Fileinfo info) throws IOException {
        File file = new File(getFullFilePath(info.getPath()));

        InputStream is = FileUtils.openInputStream(file);

        return is;
    }

    private static String getFileName(String filename, String fileext) {
        return StringUtils.isBlank(fileext) ? filename : (filename + "." + fileext);
    }

    private static String getFilePath(String path, String... filename) {
        String filePath = path;
        for (String name : filename) {
            filePath += File.separator + name;
        }

        return filePath;
    }

    private static String getFullFilePath(String path) {
        return SysParametersUtils.getUploadHome() + path;
    }
}

