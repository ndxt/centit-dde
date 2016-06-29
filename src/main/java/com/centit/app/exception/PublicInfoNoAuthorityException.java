package com.centit.app.exception;

/**
 * 公共文件信息-没有权限异常
 *
 * @author zk
 * @create 2013-1-7
 */
public class PublicInfoNoAuthorityException extends PublicInfoException {

    private static final long serialVersionUID = 1L;

    public PublicInfoNoAuthorityException(String msg) {
        super(msg);
    }
}
