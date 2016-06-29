package com.centit.app.service.impl;

import com.centit.app.dao.PublicinfoDao;
import com.centit.app.exception.PublicInfoException;
import com.centit.app.exception.PublicInfoNoAuthorityException;
import com.centit.app.exception.PublicInfoNoFileFoundException;
import com.centit.app.po.Fileinfo;
import com.centit.app.po.Publicinfo;
import com.centit.app.service.PublicinfoManager;
import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.util.CommonCodeUtil;
import com.centit.sys.util.SysParametersUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO Class description should be added
 *
 * @author zk
 * @create 2013-2-25
 */
public class PublicinfoManagerImpl extends BaseEntityManagerImpl<Publicinfo> implements PublicinfoManager {
    private static final long serialVersionUID = 1L;

    /**
     * 文件名校验规则
     */
    private static final Pattern FILENAME_PATTERN = Pattern.compile("\\.|\\*|\\?|\\%|\\_");

    public static final Log log = LogFactory.getLog(PublicinfoManager.class);

    private PublicinfoDao publicinfoDao;

    public void setPublicinfoDao(PublicinfoDao baseDao) {
        this.publicinfoDao = baseDao;
        setBaseDao(this.publicinfoDao);
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#getPublicRootDirectory(java.lang.String)
     */
    @Override
    public Publicinfo getPublicRootDirectory(String unitcode)
            throws PublicInfoNoFileFoundException {

        Publicinfo root = publicinfoDao.getUnitRootDirectory(unitcode);

        if (null == root) {
            throw new PublicInfoNoFileFoundException("机构[" + unitcode + "]没有根目录，请联系管理员创建。");
        }

        return root;
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#queryAllPublicFileChilds(java.util.List)
     */
    @Override
    public List<Publicinfo> queryAllPublicFileChilds(List<String> codes, FUserDetail user) {
        List<Publicinfo> childrenFiles = new ArrayList<Publicinfo>();

        List<Publicinfo> files = publicinfoDao.listPublicinfos(codes);
        setFilesAuthority(files, user);

        for (Publicinfo file : files) {
            childrenFiles.addAll(queryAllChildPublicinfos(file, file.getFilename()));
        }

        return childrenFiles;
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#queryPublicFiles(java.lang.String, com.centit.sys.security.FUserDetail)
     */
    @Override
    public List<Publicinfo> queryPublicFiles(String infocode, FUserDetail user) throws PublicInfoException {
        Publicinfo file = publicinfoDao.getObjectById(infocode);

        // 查找目录下所有文件/文件夹
        List<Publicinfo> files = publicinfoDao.listAllPublicinfos(file.getInfocode());
        setFilesAuthority(files, user);

        return files;
    }

    /**
     * 查询机构直接父级机构
     *
     * @param unitcode
     * @return
     */
    @Override
    public List<Map<String, String>> queryFolderParents(String infocode) {

        List<Map<String, String>> list = publicinfoDao.listFolderParents(infocode);
        Collections.reverse(list);

        return list;

    }

    /**
     * 获取用户单个文件权限
     *
     * @param file
     * @param user
     * @return
     */
    @Override
    public int getAuthority(Publicinfo file, FUserDetail user) {
        Set<String> units = queryUnitParents(user.getPrimaryUnit());
        return getFileAuthority(file, user, units);
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#saveFile(com.centit.app.po.Fileinfo, com.centit.app.po.Publicinfo)
     */
    @Override
    public Publicinfo saveFile(Fileinfo file, Publicinfo root, FUserDetail user) {
        Publicinfo newFolder = createFile(file, root, user);
        renameDuplicationNameFile(newFolder, root.getInfocode());
        publicinfoDao.saveObject(newFolder);
        setFileAuthority(newFolder, user);
        return newFolder;
    }


    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#deleteFiles(java.lang.String[], com.centit.sys.security.FUserDetail)
     */
    @Override
    public void deleteFile(Publicinfo info) throws PublicInfoException {
        if (null == info) {
            return;
        }

        String path = null;
        if (null != info.getFileinfo()) {
            path = getFullFilePath(info.getFileinfo().getPath());
        }

        publicinfoDao.deleteObject(info);

        if (!StringUtils.isBlank(path)) {
            File file = new File(path);

            if (file.exists()) {
                file.delete();
            }
        }
    }


    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#updateFilename(java.lang.String, java.lang.String, com.centit.sys.security.FUserDetail)
     */
    @Override
    public Publicinfo updateFilename(Publicinfo info, String name) throws PublicInfoException {

        info.setFilename(name);

        if (null != info.getFileinfo()) {
            info.getFileinfo().setFilename(name);
        }

        info.setModifytime(new Date());

        publicinfoDao.saveObject(info);

        return info;
    }

    /**
     * 新建文件夹
     *
     * @param filename
     * @param user
     * @return
     */
    private Publicinfo createFolder(String filename, Publicinfo root, FUserDetail user) {
        Publicinfo file = new Publicinfo();

        file.setInfocode(publicinfoDao.getNextValueOfSequence("s_fileno"));
        file.setFileinfo(null);
        file.setDownloadcount(0L);
        file.setFilename(filename);
        file.setIsfolder(CommonCodeUtil.PUBLICINFO_T_FOLDER);
        file.setModifytime(new Date());
        file.setOwnercode(user.getUsercode());
        file.setReadcount(0L);
        file.setStatus(CommonCodeUtil.PUBLICINFO_T_NORMAL);
        file.setType(CommonCodeUtil.PUBLICINFO_T_PUBLIC_CUSTOM);
        file.setUnitcode(user.getPrimaryUnit());
        file.setUploadtime(new Date());
        file.setParentinfocode(root.getInfocode());

        return file;
    }

    /**
     * 新建文件
     *
     * @param file
     * @param root
     * @param user
     * @return
     */
    private Publicinfo createFile(Fileinfo info, Publicinfo root, FUserDetail user) {
        Publicinfo file = new Publicinfo();

        file.setInfocode(publicinfoDao.getNextValueOfSequence("s_fileno"));
        file.setDownloadcount(0L);
        file.setFilename(info.getFilename());
        file.setFileextension(info.getFileext());
        file.setFileinfo(info);
        file.setIsfolder("0");
        file.setModifytime(new Date());
        file.setOwnercode(user.getUsercode());
        file.setReadcount(0L);
        file.setFilesize(info.getFilesize());
        file.setStatus(CommonCodeUtil.PUBLICINFO_T_NORMAL);
        file.setType(CommonCodeUtil.PUBLICINFO_T_PUBLIC_CUSTOM);
        file.setUnitcode(user.getPrimaryUnit());
        file.setUploadtime(new Date());
        file.setParentinfocode(root.getInfocode());

        return file;
    }

    /**
     * 查找目录下所有子文件
     *
     * @param file
     * @return
     */
    private List<Publicinfo> queryAllChildPublicinfos(Publicinfo file, String path) {
        List<Publicinfo> files = new ArrayList<Publicinfo>();

        // 如果是文件直接返回
        if (!StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_FOLDER, file.getIsfolder())) {
            if (StringUtils.isBlank(file.getFileextension())) {
                file.setZipPath(path);
            } else {
                file.setZipPath(path + "." + file.getFileextension());
            }

            files.add(file);
            return files;
        }
        file.setZipPath(path);

        List<Publicinfo> childs = publicinfoDao.listAllPublicinfos(file.getInfocode());

        for (Publicinfo child : childs) {
            files.addAll(queryAllChildPublicinfos(child, path + File.separator + child.getFilename()));
        }

        files.add(file);
        return files;
    }

    /**
     * 获取权限
     *
     * @param authority
     * @param pos
     * @return
     */
    private static boolean getAuthority(int authority, int pos) {
        return ((authority >> pos) % 2) != CommonCodeUtil.NO_AUTHORITY;
    }

    private void setFileAuthority(Publicinfo file, FUserDetail user) {
        Set<String> units = queryUnitParents(user.getPrimaryUnit());
        setFileAuthority(file, user, units);
        file.setUploader(CodeRepositoryUtil.getValue("usercode", file.getOwnercode()));
    }

    /**
     * 获取用户文件权限
     *
     * @param file
     * @param user
     * @param units
     * @return
     */
    private static int getFileAuthority(Publicinfo file, FUserDetail user, Set<String> units) {
        int authorityModify = 0;
        int authorityAdd = 0;
        int authorityView = 0;

        int authority;

        // 查看权限：本级文件、直接上级的文件和自己的文件
        if (units.contains(file.getUnitcode())) {
            authorityView = 1;
        } else {
            authorityView = 0;
        }

        // 新增权限：本级文件夹或本人
        if (file.getUnitcode().equals(user.getPrimaryUnit())) {
            authorityAdd = 1;
        } else {
            authorityAdd = 0;
        }

        // 文件锁定
        if (CommonCodeUtil.PUBLICINFO_T_LOCKED.equals(file.getStatus())) {
            authorityAdd = 0;
            authorityView = 0;
        }

        // 修改权限：限本人
        if (file.getOwnercode().equals(user.getUsercode())) {
            authorityModify = 1;
            authorityAdd = 1;
            authorityView = 1;
        } else {
            authorityModify = 0;
        }

        authority = (authorityModify << CommonCodeUtil.PUBLICINFO_AUTHORITY_MODIFY)
                + (authorityAdd << CommonCodeUtil.PUBLICINFO_AUTHORITY_ADD)
                + authorityView;

        return authority;
    }

    @SuppressWarnings("static-method")
    private void setFileAuthority(Publicinfo file, FUserDetail user, Set<String> units) {

        file.setAuthority(getFileAuthority(file, user, units) + "");
    }

    /**
     * 获取用户权限
     *
     * @param files
     */
    private void setFilesAuthority(Collection<Publicinfo> files, FUserDetail user) {

        Set<String> units = queryUnitParents(user.getPrimaryUnit());

        for (Publicinfo file : files) {
            setFileAuthority(file, user, units);
            file.setUploader(CodeRepositoryUtil.getValue("usercode", file.getOwnercode()));
        }
    }

    /**
     * 鉴权重命名文件
     *
     * @param file
     * @param name
     * @param user
     */
    @Override
    public Publicinfo authenticate4RenameFiles(String infocode, String root, String name, FUserDetail user) throws PublicInfoException {
        Publicinfo info = publicinfoDao.getObjectById(infocode);

        if (null == info) {
            throw new PublicInfoNoFileFoundException("文件不存在。");
        }

        if (StringUtils.isBlank(name)) {
            throw new PublicInfoException("文件名不能为空，请重新输入。");
        }

        Matcher m = FILENAME_PATTERN.matcher(name);
        if (m.matches()) {
            throw new PublicInfoException("文件名不能含有. * ? % _等特殊字符，请重新输入。");
        }

        if (checkDuplicationNameFolder(info, root)) {
            throw new PublicInfoException("重复的文件名，请重新输入。");
        }

        if (!StringUtils.equals(user.getUsercode(), info.getOwnercode())) {
            throw new PublicInfoNoAuthorityException("非本人上传文件不能重命名。");
        }

        if (StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_PUBLIC_DEFAULT, info.getType())) {
            throw new PublicInfoNoAuthorityException("机构默认文件夹不能重命名。");
        }

        if (StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_LOCKED, info.getStatus())) {
            throw new PublicInfoNoAuthorityException("文件被锁定不能重命名。");
        }

        if (!getAuthority(getAuthority(info, user), CommonCodeUtil.PUBLICINFO_AUTHORITY_MODIFY)) {
            throw new PublicInfoNoAuthorityException("非本人上传文件不能重命名。");
        }

        return info;
    }

    /**
     * 鉴权删除文件
     *
     * @param files
     * @param user
     * @return
     */
    @Override
    public List<Publicinfo> authenticate4DeleteFiles(String infocodes, FUserDetail user) {
        List<Publicinfo> toDeleteFiles = new ArrayList<Publicinfo>();

        if (StringUtils.isBlank(infocodes)) {
            return toDeleteFiles;
        }

        List<Publicinfo> files = publicinfoDao.listPublicinfos(Arrays.asList(infocodes.split(",")));
        setFilesAuthority(files, user);

        for (Publicinfo file : files) {
            // 非本人上传文件不可删除
            if (!StringUtils.equals(file.getOwnercode(), user.getUsercode())) {
                continue;
            }

            // 机构默认文件夹不可删除
            if (StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_PUBLIC_DEFAULT, file.getType())) {
                continue;
            }

            // 状态为锁定的文件不可删除
            if (StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_LOCKED, file.getStatus())) {
                continue;
            }

            toDeleteFiles.addAll(queryAllChildPublicinfos(file, file.getFilename()));
        }

        return toDeleteFiles;
    }


    public Publicinfo authenticate4AddFolder(String filename, String infocode, FUserDetail user) throws PublicInfoException {

        Publicinfo file = publicinfoDao.getObjectById(infocode);

        if (StringUtils.isBlank(filename)) {
            throw new PublicInfoException("文件夹名称不能为空。");
        }

        if (null == file) {
            throw new PublicInfoNoFileFoundException("根目录不存在，无法新建文件夹。");
        }

        if (!CommonCodeUtil.PUBLICINFO_T_FOLDER.equals(file.getIsfolder())) {
            throw new PublicInfoNoFileFoundException("根目录类型不正确，无法新建文件夹。");
        }

        if (!CommonCodeUtil.PUBLICINFO_T_NORMAL.equals(file.getStatus())) {
            throw new PublicInfoNoFileFoundException("根目录状态不正确，无法新建文件夹。");
        }

        if (!StringUtils.equals(file.getUnitcode(), user.getPrimaryUnit())) {
            throw new PublicInfoNoAuthorityException("没有权限在非本机构创建文件件。");
        }

        int authority = getAuthority(file, user);
        if (!getAuthority(authority, CommonCodeUtil.PUBLICINFO_AUTHORITY_ADD)) {
            throw new PublicInfoNoAuthorityException("没有权限在非本机构创建文件件。");
        }

        Publicinfo newFolder = createFolder(filename, file, user);

        renameDuplicationNameFile(newFolder, infocode);

        return newFolder;
    }

    /**
     * 鉴权进入公共文件夹目录
     *
     * @param file         要进入的文件夹
     * @param userUnitcode 用户机构编码
     * @throws PublicInfoException
     */
    @Override
    public Publicinfo authenticate4EntryPublicDirectory(Publicinfo file, FUserDetail user) throws PublicInfoException {

        if (null == user) {
            throw new PublicInfoNoAuthorityException("没有权限。");
        }

        // 为空直接抛异常
        if (null == file) {
            throw new PublicInfoNoFileFoundException("文件夹不存在。");
        }

        // 不是文件夹并且也不是公共文件夹，也抛出异常
        if (!CommonCodeUtil.PUBLICINFO_T_FOLDER.equals(file.getIsfolder())) {
            throw new PublicInfoException(file.getFilename() + "[" + file.getInfocode() + "]类型错误。");
        }

        // 用户只能查看本机构和直接上级机构的文件夹，子机构和兄弟机构的文件夹无权访问
        Set<String> units = queryUnitParents(user.getPrimaryUnit());
        if (!units.contains(file.getUnitcode()) && !user.getUsercode().equals(file.getOwnercode())) {
            throw new PublicInfoNoAuthorityException("用户（机构[" + user.getPrimaryUnit() + "]）无权访问"
                    + file.getFilename() + "（机构[" + file.getUnitcode() + "]）");
        }

        // 文件夹锁定，不允许查看
        if (CommonCodeUtil.PUBLICINFO_T_LOCKED.equals(file.getStatus()) && !user.getUsercode().equals(file.getOwnercode())) {
            throw new PublicInfoNoAuthorityException(file.getFilename() + "已被用户锁定，无法访问。");
        }

        return file;
    }

    /* (non-Javadoc)
     * @see com.centit.app.service.PublicinfoManager#authenticate4UploadFile(java.lang.String, com.centit.sys.security.FUserDetail)
     */
    @Override
    public Publicinfo authenticate4UploadFile(String path, FUserDetail user) throws PublicInfoException {

        Publicinfo file = publicinfoDao.getObjectById(path);
        if (null == file) {
            throw new PublicInfoNoFileFoundException("目录不存在无法上传。");
        }

        if (!StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_FOLDER, file.getIsfolder())) {
            throw new PublicInfoException("目录类型不正确无法上传。");
        }

        if (!StringUtils.equals(file.getUnitcode(), user.getPrimaryUnit())) {
            throw new PublicInfoNoAuthorityException("非本机构目录无法上传。");
        }


        if (!getAuthority(getAuthority(file, user), CommonCodeUtil.PUBLICINFO_AUTHORITY_ADD)) {
            throw new PublicInfoNoAuthorityException("没有权限无法上传。");
        }

        return file;
    }


    /**
     * 查询机构直接父级机构
     *
     * @param unitcode
     * @return
     */
    private Set<String> queryUnitParents(String unitcode) {
        List<String> units = publicinfoDao.listUnitDirectParents(unitcode);

        Set<String> unitSet = new HashSet<String>();
        unitSet.addAll(units);

        return unitSet;
    }

    /**
     * 判断是否有重名文件夹
     *
     * @param name
     * @param infocode
     * @return true有重名
     *         false无重名
     */
    private boolean checkDuplicationNameFolder(Publicinfo file, String infocode) {
        String name = file.getFilename();
        String ext = file.getFileextension();

        if (StringUtils.equals(CommonCodeUtil.PUBLICINFO_T_FOLDER, file.getIsfolder())) {
            return null != publicinfoDao.getPublicFolderByName(file.getFilename(), infocode);
        }

        if (StringUtils.isBlank(ext)) {
            return null != publicinfoDao.getPublicFileByName(name, infocode);
        }

        return null != publicinfoDao.getPublicFileByName(name, ext, infocode);
    }

    /**
     * 判断是否有重名，并重命名
     *
     * @param file
     * @param infocode
     * @return
     */
    private String renameDuplicationNameFile(Publicinfo file, String infocode) {
        String name = file.getFilename();
        int index = 1;

        while (checkDuplicationNameFolder(file, infocode)) {
            file.setFilename(name + "(" + (index++) + ")");
        }

        return file.getFilename();
    }

    private static String getFullFilePath(String path) {
        return SysParametersUtils.getUploadHome() + path;
    }
}

