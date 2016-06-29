package com.centit.sys.util;

import java.util.List;

/**
 * 即时消息发送接口
 *
 * @author sx
 * @create 2012-12-12
 */
public interface IInstantMsg {
    /**
     * 服务端向前端推送消息
     *
     * @param script 前端执行的Js脚本名称，Js函数全称，如有命名空间，需要带上
     * @param params Js函数的参数
     */
    void pushAll(String script, String... params);

    /**
     * 服务端向指定用户的前端推送消息
     *
     * @param userCodes 接收消息的用户代码集合
     * @param script    前端执行的Js脚本名称，Js函数全称，如有命名空间，需要带上
     * @param params    Js函数的参数
     */
    void push(List<String> userCodes, String script, String... params);

}
