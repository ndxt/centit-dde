/*
  交通厅先期部署的项目中表中无数据
*/
insert into D_OS_INFO (os_id, os_name, has_interface, interface_url, created, last_update_time, create_time, os_user, os_password)
values ('osid', 'osname', 'T', 'http://sxpc.centit.com:8090/dde/ws/uploadData?wsdl', 'u0000000', to_date('07-11-2014 09:48:25', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2012 14:02:47', 'dd-mm-yyyy hh24:mi:ss'), 'osname', 'ospassword');
commit;
update D_DATABASE_INFO t set t.source_os_id = 'osid';
commit;

/**
  *end
 */



insert into F_DATACATALOG (catalogcode, catalogname, catalogstyle, catalogtype, catalogdesc, fielddesc, lastmodifydate, createdate)
values ('DATA_TYPE', '数据库数据类型匹配', 'S', 'L', null, null, to_date('12-12-2014 09:43:38', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:40:17', 'dd-mm-yyyy hh24:mi:ss'));
commit;

insert into F_DATADICTIONARY (catalogcode, datacode, extracode, extracode2, datatag, datavalue, datastyle, datadesc, lastmodifydate, createdate)
values ('DATA_TYPE', 'date', null, null, 'T', 'date,datetime,timestamp', 'U', null, to_date('12-12-2014 09:42:19', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:42:19', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_DATADICTIONARY (catalogcode, datacode, extracode, extracode2, datatag, datavalue, datastyle, datadesc, lastmodifydate, createdate)
values ('DATA_TYPE', 'clob', null, null, 'T', 'clob,text', 'U', null, to_date('12-12-2014 09:42:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:42:45', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_DATADICTIONARY (catalogcode, datacode, extracode, extracode2, datatag, datavalue, datastyle, datadesc, lastmodifydate, createdate)
values ('DATA_TYPE', 'string', null, null, 'T', 'char,varchar,varchar2', 'U', null, to_date('12-12-2014 09:40:49', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:40:49', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_DATADICTIONARY (catalogcode, datacode, extracode, extracode2, datatag, datavalue, datastyle, datadesc, lastmodifydate, createdate)
values ('DATA_TYPE', 'number', null, null, 'T', 'number,int,decimal', 'U', null, to_date('12-12-2014 09:41:44', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:41:44', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_DATADICTIONARY (catalogcode, datacode, extracode, extracode2, datatag, datavalue, datastyle, datadesc, lastmodifydate, createdate)
values ('DATA_TYPE', 'blob', null, null, 'T', 'blob,img', 'U', null, to_date('12-12-2014 09:43:13', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:43:13', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_DATADICTIONARY (catalogcode, datacode, extracode, extracode2, datatag, datavalue, datastyle, datadesc, lastmodifydate, createdate)
values ('DATA_TYPE', 'unknown', null, null, 'T', null, 'U', null, to_date('12-12-2014 09:43:38', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-12-2014 09:43:38', 'dd-mm-yyyy hh24:mi:ss'));
commit;

/*添加nvarchar,ntext类型*/
update F_DATADICTIONARY t set t.datavalue = 'char,varchar,varchar2,nvarchar' where  t.catalogcode = 'DATA_TYPE' and t.datacode = 'string';
update F_DATADICTIONARY t set t.datavalue = 'clob,text,ntext' where  t.catalogcode = 'DATA_TYPE' and t.datacode = 'clob';
update f_datadictionary t set t.datastyle = 'U' where t.catalogcode = 'SYSPARAM' and t.datacode = 'SysName';
commit;