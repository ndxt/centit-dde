package com.centit.app.service;

import java.util.List;
import java.util.Map;

import com.centit.app.exception.PublicInfoException;
import com.centit.app.exception.PublicInfoNoFileFoundException;
import com.centit.app.po.Fileinfo;
import com.centit.app.po.Publicinfo;
import com.centit.core.service.BaseEntityManager;
import com.centit.sys.security.FUserDetail;

public interface PublicinfoManager extends BaseEntityManager<Publicinfo> {

    /**
     * 获取用户公共文件夹根目录
     *
     * @param unitcode
     * @return
     */
    Publicinfo getPublicRootDirectory(String unitcode) throws PublicInfoNoFileFoundException;

    /**
     * 获取公共文件所有子文件
     *
     * @param code
     * @return
     */
    List<Publicinfo> queryAllPublicFileChilds(List<String> codes, FUserDetail user);

    /**
     * 获取公共文件夹下的所有文件
     *
     * @param infocode
     * @param user
     * @return
     */
    List<Publicinfo> queryPublicFiles(String infocode, FUserDetail user) throws PublicInfoException;

    /**
     * 列出文件路径
     *
     * @param infocode
     * @return
     */
    List<Map<String, String>> queryFolderParents(String infocode);

    /**
     * 保存文件
     *
     * @param file
     * @param root
     * @return
     */
    Publicinfo saveFile(Fileinfo file, Publicinfo root, FUserDetail user);

    /**
     * 删除文件
     *
     * @param info
     * @throws PublicInfoException
     */
    void deleteFile(Publicinfo info) throws PublicInfoException;

    /**
     * 重命名文件
     *
     * @param infocode
     * @param name
     * @param user
     * @return
     * @throws PublicInfoException
     */
    Publicinfo updateFilename(Publicinfo file, String name) throws PublicInfoException;

    /**
     * 获取单个文件权限
     *
     * @param file
     * @param user
     * @return
     */
    int getAuthority(Publicinfo file, FUserDetail user);

    /**
     * 鉴权上传文件
     *
     * @param path
     * @param user
     * @throws PublicInfoException
     */
    Publicinfo authenticate4UploadFile(String path, FUserDetail user) throws PublicInfoException;

    /**
     * 鉴权进入文件夹
     *
     * @param infocode
     * @param user
     * @return
     * @throws PublicInfoException
     */
    Publicinfo authenticate4EntryPublicDirectory(Publicinfo info, FUserDetail user) throws PublicInfoException;

    /**
     * 鉴权创建文件夹
     *
     * @param filename
     * @param infocode
     * @param user
     * @return
     * @throws PublicInfoException
     */
    Publicinfo authenticate4AddFolder(String filename, String infocode, FUserDetail user) throws PublicInfoException;


    /**
     * 鉴权删除文件
     *
     * @param infocodes
     * @param user
     * @return
     */
    List<Publicinfo> authenticate4DeleteFiles(String infocodes, FUserDetail user);

    /**
     * 鉴权重命名文件
     *
     * @param infocode
     * @param root
     * @param name
     * @param user
     * @return
     * @throws PublicInfoException
     */
    Publicinfo authenticate4RenameFiles(String infocode, String root, String name, FUserDetail user) throws PublicInfoException;

}
