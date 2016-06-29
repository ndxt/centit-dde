package com.centit.app.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.centit.app.po.Fileinfo;
import com.centit.app.service.FileinfoManager;
import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.util.SysParametersUtils;

public class FileinfoAction extends BaseEntityDwzAction<Fileinfo> {

    private static final long serialVersionUID = 1L;
    private FileinfoManager fileinfoMag;
    private Map<String, Object> dataMap = new HashMap<String, Object>();
    private InputStream inputStream;
    private String optID;
    private String fullname;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public String getOptID() {
        return optID;
    }

    public void setOptID(String optID) {
        this.optID = optID;
    }

    public void setFileinfoMag(FileinfoManager mgr) {
        fileinfoMag = mgr;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String list() {
        return "upload";
    }

    /**
     * 在下载文件之前做必要校验，防止下载出现错误
     *
     * @return
     */
    public String toDownloadfile() {
        dataMap.clear();

        Fileinfo info = fileinfoMag.getObjectById(object.getFilecode());

        if (null == info) {
            dataMap.put("result", "1");
            dataMap.put("description", "文件不存在");

            return "files";
        }

        String path = FilenameUtils.normalize(SysParametersUtils.getUploadHome() + info.getPath());
        File file = new File(path);

        if (!file.exists()) {
            dataMap.put("result", "1");
            dataMap.put("description", "文件不存在");
        } else if (!file.canRead()) {
            dataMap.put("result", "1");
            dataMap.put("description", "文件可能正在被其他进程占用，无法读取");
        } else {
            dataMap.put("result", "0");
            dataMap.put("filecode", info.getFilecode());
        }

        return "files";
    }

    /**
     * 下载文件
     *
     * @return
     */
    public String downloadfile() {
        dataMap.clear();

        Fileinfo info = fileinfoMag.getObjectById(object.getFilecode());

        if (null == info) {

            return "download";
        }

        try {
            inputStream = fileinfoMag.downloadFileinfo(info);
            fullname = StringUtils.isBlank(info.getFileext()) ? info.getFilename() : info.getFilename() + "." + info.getFileext();
            fullname = new String(fullname.getBytes("gb2312"), "iso-8859-1");

        } catch (IOException e) {
            e.getMessage();
        }

        return "download";
    }

    /**
     * 上传文件
     *
     * @return
     */
    public String uploadfile() {
        dataMap.clear();

        FUserDetail uinfo = ((FUserDetail) getLoginUser());

        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        File[] items = wrapper.getFiles("Filedata");
        String[] names = wrapper.getFileNames("Filedata");

        try {
            Fileinfo file = null;

            // 如果是应用插件上传，request里只会有一个文件
            for (int i = 0; i < items.length; i++) {
                file = processUploadedFile(items[i], names[i], optID, uinfo);
            }

            dataMap.put("result", 0);
            dataMap.put("file", file);

        } catch (IOException e) {
            dataMap.put("result", "9");
            dataMap.put("description", e.getMessage());
        }

        return "files";
    }

    public String deletefile() {
        dataMap.clear();

        Fileinfo info = fileinfoMag.getObjectById(object.getFilecode());

        if (null == info) {
            dataMap.put("result", "9");
            dataMap.put("description", "文件不存在");

            return "files";
        }

        fileinfoMag.deleteFileinfo(info);
        dataMap.put("result", "0");
        dataMap.put("description", "删除文件成功");

        return "files";
    }

    private Fileinfo processUploadedFile(File file, String filename, String optCode, FUserDetail user) throws IOException {
        Fileinfo info = new Fileinfo();
        info.setFilename(FilenameUtils.getBaseName(filename));
        info.setFileext(FilenameUtils.getExtension(filename));
        info.setFilesize(file.length());
        info.setRecorder(user.getUsercode());
        info.setRecorderDate(new Date());
        info.setOptcode(optCode);
        info.setInstid(0L);
        info.setIndb("0");
        info.setIsindex("0");

        fileinfoMag.saveFileinfo(info, new FileInputStream(file), optCode);

        return info;
    }

}
