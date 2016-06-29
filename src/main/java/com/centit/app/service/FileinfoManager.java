package com.centit.app.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.centit.app.po.Fileinfo;
import com.centit.core.service.BaseEntityManager;

public interface FileinfoManager extends BaseEntityManager<Fileinfo> {
    public String getNextKey();

    /**
     * 上传保存文件
     *
     * @param file
     * @param is
     * @param optID
     * @return
     * @throws IOException
     */
    public Fileinfo saveFileinfo(Fileinfo file, InputStream is, String optID) throws IOException;

    /**
     * 保存文件对象，与上传保存文件不同的是，这个方法不会再复制文件，直接根据原文件信息保存对象
     *
     * @param info 传入时只要有业务逻辑字段如：上传者、OPTCODE等，其他会根据FILE信息自动生成
     * @param file
     * @return
     */
    public Fileinfo saveFileinfo(Fileinfo info, File file);

    /**
     * 复制单个文件
     *
     * @param file
     * @param path
     * @return
     */
    public Fileinfo updateFileinfoCopy(Fileinfo file, String path);


    /**
     * 剪切单个文件
     *
     * @param file
     * @param path
     * @return
     */
    public Fileinfo updateFileinfoCut(Fileinfo file, String path);

    /**
     * 二次确认上传文件
     *
     * @param info
     * @return
     */
    public Fileinfo update4ConfirmFileinfo(Fileinfo info);

    /**
     * 二次确认上传文件
     *
     * @param infos
     */
    public void update4ConfirmFileinfos(Collection<Fileinfo> infos);

    /**
     * 删除文件
     * <p/>
     * 若文件对象已与其他业务对象做级联删除，强烈不建议使用这2个方法单独删除。
     * 而是应该直接删除父对象时级联删除文件对象，再调用接口直接删除磁盘上的文件。
     *
     * @param file 待删除文件对象
     */
    @Deprecated
    public boolean deleteFileinfo(Fileinfo file);

    /**
     * 直接删除磁盘上文件
     *
     * @param file 待删除文件对象
     * @return
     */
    public boolean deleteFileOnDisk(Fileinfo file);

    /**
     * 直接删除磁盘上文件
     *
     * @param path 文件路径
     * @return
     */
    public boolean deleteFileOnDisk(String path);

    /**
     * 下载文件
     *
     * @param file
     * @return
     */
    public InputStream downloadFileinfo(Fileinfo file) throws IOException;

}
