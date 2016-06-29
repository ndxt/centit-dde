package com.centit.dde.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://dde.centit.com/ws/")
public interface UploadData {
    /**
     * 上传数据接口，写入数据库之前需要进行用户身份验证
     *
     * @param userName  用户名
     * @param userPin   密码，加密形式
     * @param tableDate 数据格式参见离线文件数据格式，为了控制大小可以将大字段单独上传，也可以一起上传
     *                  <!ELEMENT table (row*)>
     *                  <!ATTLIST table name CDATA #REQUIRED>
     *                  <!ATTLIST table dataoptid CDATA #REQUIRED>
     *                  <!ATTLIST table sourceosid CDATA #IMPLIED>
     *                  <!ATTLIST table sourcedatabase CDATA #IMPLIED>
     *                  <!ATTLIST table exportdesc CDATA #IMPLIED>
     *                  <!ELEMENT row (item+)>
     *                  <!ELEMENT item (#PCDATA)>
     *                  <!ATTLIST item name CDATA #REQUIRED>
     *                  <!ATTLIST item type (text|date|datetime|number|blob|clob) "text">
     *                  <!-- type 为date是 这个format为日期的格式，默认为"YYYY-DD-MM"； type为blob时这个格式为 plain 或则base64，clob默认为 plain， blob默认为base64 -->
     *                  <!ATTLIST item format CDATA #IMPLIED>
     * @return
     */
    @WebMethod
    String uploadTableAsXml(
            @WebParam(name = "userName") String userName,
            @WebParam(name = "userPin") String userPin,
            @WebParam(name = "xmlData") String tableData);

    /**
     * 上传数据接口，写入数据库之前需要进行用户身份验证
     *
     * @param userName  用户名
     * @param userPin   密码，加密形式
     * @param tableData 数据格式参见离线文件数据格式，为了控制大小可以将大字段单独上传，也可以一起上传
     * @return
     */
    @WebMethod
    String uploadTableAsByteArray(
            @WebParam(name = "userName") String userName,
            @WebParam(name = "userPin") String userPin,
            @WebParam(name = "xmlData") byte[] tableData);

    /**
     * 单独上传大字段接口
     *
     * @param userName   用户名
     * @param userPin    密码，加密形式
     * @param database   数据库名称 D_DataBase_Info 的databasename
     * @param tableName  表名
     * @param columnName lob字段名
     * @param keyDesc    主键描述可以是复合主键，形式为 key=value and key2=value2 ...
     * @param isBase64   是否为 base64编码，一般Clob不需要，Blob需要编码
     * @param lobData    大字段字符串
     * @return
     */
    @WebMethod
    String uploadLobAsString(
            @WebParam(name = "userName") String userName,
            @WebParam(name = "userPin") String userPin,
            @WebParam(name = "database") String database,
            @WebParam(name = "tableName") String tableName,
            @WebParam(name = "columnName") String columnName,
            @WebParam(name = "keyDesc") String keyDesc,
            @WebParam(name = "isBase64") boolean isBase64,
            @WebParam(name = "lobData") String lobData);

    /**
     * 单独上传大字段接口
     *
     * @param userName   用户名
     * @param userPin    密码，加密形式
     * @param database   数据库名称 D_DataBase_Info 的databasename
     * @param tableName  表名
     * @param columnName lob字段名
     * @param keyDesc    主键描述可以是复合主键，形式为 key=value and key2=value2 ...
     * @param lobData    大字段数据
     * @return
     */
    @WebMethod
    String uploadLobAsByteArray(
            @WebParam(name = "userName") String userName,
            @WebParam(name = "userPin") String userPin,
            @WebParam(name = "database") String database,
            @WebParam(name = "tableName") String tableName,
            @WebParam(name = "columnName") String columnName,
            @WebParam(name = "keyDesc") String keyDesc,
            @WebParam(name = "lobData") byte[] lobData);

}
