package com.centit.sys.util;

public interface CommonCodeUtil {
    /**
     * 公共信息文件-文件夹
     */
    public static final String PUBLICINFO_T_FOLDER = "1";

    /**
     * 公共信息文件-文件
     */
    public static final String PUBLICINFO_T_NOT_FOLDER = "0";

    /**
     * 公共信息文件-类型：公共文件夹，用户自己创建
     */
    public static final String PUBLICINFO_T_PUBLIC_CUSTOM = "0";

    /**
     * 公共信息文件-类型：公共文件夹，系统创建
     */
    public static final String PUBLICINFO_T_PUBLIC_DEFAULT = "1";

    /**
     * 公共信息文件-类型：个人文件夹，用户自己创建
     */
    public static final String PUBLICINFO_T_PERSONAL_CUSTOM = "2";

    /**
     * 公共信息文件-类型：个人文件夹，系统创建
     */
    public static final String PUBLICINFO_T_PERSONAL_DEFAULT = "3";

    /**
     * 公共信息文件-状态：正常
     */
    public static final String PUBLICINFO_T_NORMAL = "0";

    /**
     * 公共信息文件-状态：锁定
     */
    public static final String PUBLICINFO_T_LOCKED = "1";

    /**
     * 公共信息文件-状态：已删除
     */
    public static final String PUBLICINFO_T_DELETED = "2";

    public static final int PUBLICINFO_AUTHORITY_MODIFY = 2;

    public static final int PUBLICINFO_AUTHORITY_ADD = 1;

    public static final int PUBLICINFO_AUTHORITY_VIEW = 0;

    public static final String PUBLICINFO_OPTCODE = "UPLOADFI";

    /**
     * 有权限
     */
    public static final int HAS_AUTHORITY = 1;

    /**
     * 无权限
     */
    public static final int NO_AUTHORITY = 0;

    /**
     * 添加索引的文件
     */
    public static final String INDEX_FILE = "1";
}
