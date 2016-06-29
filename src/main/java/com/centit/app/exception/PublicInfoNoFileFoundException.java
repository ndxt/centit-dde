package com.centit.app.exception;

/**
 * 公共文件信息-未找到文件异常
 *
 * @author zk
 * @create 2013-1-7
 */
public class PublicInfoNoFileFoundException extends PublicInfoException {

    private static final long serialVersionUID = 1L;

    public PublicInfoNoFileFoundException(String msg) {
        super(msg);
    }
}
