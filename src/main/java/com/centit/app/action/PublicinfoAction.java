package com.centit.app.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.centit.app.exception.PublicInfoException;
import com.centit.app.exception.PublicInfoNoAuthorityException;
import com.centit.app.exception.PublicInfoNoFileFoundException;
import com.centit.app.po.Fileinfo;
import com.centit.app.po.Publicinfo;
import com.centit.app.service.FileinfoManager;
import com.centit.app.service.PublicinfoManager;
import com.centit.app.util.ZipUtils;
import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.util.CommonCodeUtil;
import com.centit.sys.util.SysParametersUtils;

public class PublicinfoAction extends BaseEntityDwzAction<Publicinfo> {
    private PublicinfoManager publicinfoManager;
    private FileinfoManager fileinfoManager;
    private String path = null;
    private Map<String, Object> dataMap = new HashMap<String, Object>();

    private static final Log log = LogFactory.getLog(PublicinfoAction.class);

    private static final long serialVersionUID = 1L;

    /**
     * 处理异常
     *
     * @param e
     */
    private void doWithException(PublicInfoException e) {

        if (e instanceof PublicInfoNoFileFoundException) {
            dataMap.put("result", "1");
            dataMap.put("description", e.getMessage());
        } else if (e instanceof PublicInfoNoAuthorityException) {
            dataMap.put("result", "2");
            dataMap.put("description", e.getMessage());

        } else {
            dataMap.put("result", "9");
            dataMap.put("description", e.getMessage());

        }
    }

    public String list() {
        return "list";
    }

    public String download() {
        String infocode = request.getParameter("infocode");
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        List<String> codes = new ArrayList<String>();
        dataMap.clear();

        if (!StringUtils.isBlank(infocode)) {
            codes.addAll(Arrays.asList(infocode.split(",")));
        }

        List<Publicinfo> infos = publicinfoManager.queryAllPublicFileChilds(codes, uinfo);
        Collections.reverse(infos);

        if (infos.isEmpty()) {
            dataMap.put("result", "1");
            dataMap.put("description", "没有可以下载的文件");

            return "files";
        }

        Publicinfo info = infos.get(0);

        // 单个文件直接下载返回
        if (1 == infos.size() && !StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_FOLDER, info.getIsfolder())) {
            dataMap.put("result", "0");
            dataMap.put("filecode", info.getFileinfo().getFilecode());

            return "files";
        }

        try {
            // 对于文件夹或者多个文件，采取先压缩后下载的方法
            File zipFile = createZipFile(info.getFilename() + "等【批量下载】.zip", uinfo);

            // 压缩
            ZipUtils.zipPublicinfo(infos, zipFile);

            Fileinfo zipFileinfo = new Fileinfo();
            zipFileinfo.setOptcode("TEMP");
            zipFileinfo.setRecorder(uinfo.getUsercode());

            // 保存至FILEINFO
            fileinfoManager.saveFileinfo(zipFileinfo, zipFile);

            dataMap.put("result", "0");
            dataMap.put("filecode", zipFileinfo.getFilecode());
        } catch (Exception e) {
            dataMap.put("result", "9");
            dataMap.put("description", "下载时发生错误\n" + e.getMessage());

        }

        return "files";
    }

    /**
     * 查询公共文件夹目录
     *
     * @return
     */
    public String dirPublicFolder() {
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        dataMap.clear();

        List<Publicinfo> files = null;
        Publicinfo file = null;
        Publicinfo root = null;

        try {
            root = publicinfoManager.getPublicRootDirectory(uinfo.getPrimaryUnit());

            // 第一次进入页面时获取用户根目录
            if (StringUtils.isBlank(path)) {
                file = root;

            } else {
                file = publicinfoManager.getObjectById(path);
            }

            // 鉴权是否能够进入文件夹
            publicinfoManager.authenticate4EntryPublicDirectory(file, uinfo);
            files = publicinfoManager.queryPublicFiles(file.getInfocode(), uinfo);

            dataMap.put("path", publicinfoManager.queryFolderParents(file.getInfocode()));
            dataMap.put("authority", publicinfoManager.getAuthority(file, uinfo));

            // 查询成功
            dataMap.put("result", "0");
            dataMap.put("files", files);

        } catch (PublicInfoException e) {
            doWithException(e);
        }

        // 用户信息
        dataMap.put("usercode", uinfo.getUsercode());
        dataMap.put("username", CodeRepositoryUtil.getValue("usercode", uinfo.getUsercode()));
        dataMap.put("userunit", uinfo.getPrimaryUnit());

        // 当前文件夹信息
        if (null != file) {
            dataMap.put("infocode", file.getInfocode());
            dataMap.put("fileunit", file.getUnitcode());
            dataMap.put("parentcode", file.getParentinfocode());
        }

        // 部门文件夹信息
        if(root!=null)
            dataMap.put("unitroot", root.getInfocode());

        return "files";
    }

    /**
     * 新建文件夹
     *
     * @return
     */
    public String addFolder() {
        String filename = request.getParameter("filename");
        String root = request.getParameter("root");
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        dataMap.clear();

        try {
            Publicinfo folder = publicinfoManager.authenticate4AddFolder(filename, root, uinfo);
            publicinfoManager.saveObject(folder);

            dataMap.put("result", "0");
            dataMap.put("file", folder);

        } catch (PublicInfoException e) {
            doWithException(e);
        }

        return "files";
    }

    /**
     * 删除文件
     *
     * @return
     */
    public String delete() {
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        dataMap.clear();

        try {
            List<Publicinfo> toDeleteFiles = publicinfoManager.authenticate4DeleteFiles(request.getParameter("infocode"), uinfo);

            for (Publicinfo info : toDeleteFiles) {
                publicinfoManager.deleteFile(info);
            }

            dataMap.put("result", "0");

        } catch (PublicInfoException e) {
            doWithException(e);
        }

        return "files";
    }

    /**
     * 重命名文件
     *
     * @return
     */
    public String rename() {
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        dataMap.clear();

        String infocode = request.getParameter("infocode");
        String name = request.getParameter("name");
        String root = request.getParameter("root");

        try {
            // 鉴权重命名文件
            Publicinfo info = publicinfoManager.authenticate4RenameFiles(infocode, root, name, uinfo);

            // 重命名数据库记录
            info = publicinfoManager.updateFilename(info, name);

            dataMap.put("result", "0");
            dataMap.put("file", info);

        } catch (PublicInfoException e) {
            doWithException(e);
        }

        return "files";
    }

    /**
     * 上传文件
     *
     * @return
     */
    public String upload() {
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        dataMap.clear();

        try {
            // 鉴权上传文件
            Publicinfo root = publicinfoManager.authenticate4UploadFile(path, uinfo);
            Publicinfo info = null;

            MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
            File[] items = wrapper.getFiles("Filedata");
            String[] names = wrapper.getFileNames("Filedata");

            for (int i = 0; i < items.length; i++) {
                Fileinfo temp = processUploadedFile(items[i], names[i]);
                info = publicinfoManager.saveFile(temp, root, uinfo);
            }

            dataMap.put("result", "0");
            dataMap.put("description", "上传文件成功。");
            dataMap.put("file", info);

        } catch (PublicInfoException e) {
            doWithException(e);
        } catch (IOException e) {
            dataMap.put("result", "3");
            dataMap.put("description", e.getMessage());
        }

        return "files";
    }

    public String dirPersonalFolder() {

        return null;
    }

    /**
     * 处理上传文件
     *
     * @param item
     * @param path
     * @return
     * @throws IOException
     */
    private Fileinfo processUploadedFile(File file, String filename) throws IOException {

        Fileinfo info = new Fileinfo();
        info.setFilename(FilenameUtils.getBaseName(filename));
        info.setFileext(FilenameUtils.getExtension(filename));
        info.setFilesize(file.length());
        info.setRecorderDate(new Date());
        info.setOptcode(CommonCodeUtil.PUBLICINFO_OPTCODE);
        info.setInstid(1L);
        info.setIndb("0");
        info.setIsindex("1");

        fileinfoManager.saveFileinfo(info, new FileInputStream(file), CommonCodeUtil.PUBLICINFO_OPTCODE);

        return info;
    }

    private static File createZipFile(String name, FUserDetail user) throws IOException {
        String path = SysParametersUtils.getUploadTempHome() + File.separator + user.getUsercode();

        // 检查文件夹是否存在
        File root = new File(path);
        if (!root.exists()) {
            FileUtils.forceMkdir(root);
        }

        File file = new File(path + File.separator + name);

        return file;
    }

    public void setPublicinfoManager(PublicinfoManager publicinfoManager) {
        this.publicinfoManager = publicinfoManager;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public void setFileinfoManager(FileinfoManager fileinfoManager) {
        this.fileinfoManager = fileinfoManager;
    }

}
