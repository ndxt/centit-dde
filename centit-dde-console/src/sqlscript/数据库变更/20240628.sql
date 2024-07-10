
INSERT INTO f_datadictionary
(CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER, AUTO_DELEGATE)
VALUES('databaseType', 'ClickHouse', '', '', 'T', 'ClickHouse', 'S', 'ClickHouse（焦点）', '2024-05-28 14:49:24', '2024-05-28 14:47:46', 6, NULL);

INSERT INTO f_datacatalog
(CATALOG_CODE, CATALOG_NAME, CATALOG_STYLE, CATALOG_TYPE, CATALOG_DESC, FIELD_DESC, UPDATE_DATE, CREATE_DATE, OPT_ID, NEED_CACHE, CREATOR, UPDATOR, TOP_UNIT, OS_ID, source_id)
VALUES('ClickHouse', 'ClickHouses数据库参数', 'F', 'L', 'ClickHouses 数据库 连接池配置参数',
       '{"dataCode":{"isUse":"T","value":"编码"},"dataOrder":{"isUse":"T","value":"排序"},"dataValue":{"isUse":"T","value":"名称"},"dataTag":{"isUse":"T","value":"启用"},"dataDesc":{"isUse":"T","value":"描述"},"extraCode":{"isUse":"F","value":"扩展编码"},"extraCode2":{"isUse":"F","value":"扩展编码2"}}',
       '2024-05-28 15:00:04', NULL, 'platform', '1', NULL, NULL, 'system', NULL, NULL);

INSERT INTO f_datadictionary
(CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER, AUTO_DELEGATE)
VALUES('ClickHouse', 'defaultPort', '', '', 'T', '8123', 'S', '默认端口', '2024-05-28 15:00:04', '2024-05-28 14:56:25', NULL, NULL);
INSERT INTO locodedata.f_datadictionary
(CATALOG_CODE, DATA_CODE, EXTRA_CODE, EXTRA_CODE2, DATA_TAG, DATA_VALUE, DATA_STYLE, DATA_DESC, LAST_MODIFY_DATE, CREATE_DATE, DATA_ORDER, AUTO_DELEGATE)
VALUES('ClickHouse', 'pattern', '', '', 'T', 'jdbc:clickhouse://%s:%s/%s', 'S', '链接串模版', '2024-05-28 15:00:04', '2024-05-28 14:56:25', NULL, NULL);