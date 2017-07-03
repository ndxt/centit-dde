insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('OSINFO', '业务系统管理', 'SJJH', '/dde/osInfo!list.do', null, 'N', null, null, 'Y', null, null, -1, null, 'D', null, to_date('28-07-2014 09:38:04', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('JLJHDYGX1', '建立交换对应关系', 'SJJH', '/dde/exchangeMapinfoNew!list.do', null, 'N', null, null, 'Y', null, null, 2, null, 'D', null, to_date('28-08-2014 10:24:33', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('JHRW', '交换任务', 'SJJH', '/dde/exchangeTask!list.do?s_taskType=1', null, 'M', null, null, 'Y', null, null, 4, null, 'D', null, to_date('08-05-2013 11:13:13', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('EXPORTSJJH', '导出文件交换任务', 'SJJH', '/dde/exchangeTask!list.do?s_taskType=2', null, 'N', null, null, 'Y', null, null, 8, null, 'D', null, to_date('15-07-2014 09:57:30', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('DRJHRW', '导入文件交换任务', 'SJJH', '/dde/exchangeTask!list.do?s_taskType=3', null, 'N', null, null, 'Y', null, null, 11, null, 'D', null, to_date('01-08-2014 16:43:11', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('DATAOPTINFO', '数据处理方式管理', 'SJJH', '/dde/dataOptInfo!list.do', null, 'N', null, null, 'Y', null, null, 10, null, 'D', null, to_date('29-07-2014 15:16:16', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('EXPORTSQL', '导出文件配置管理', 'SJJH', '/dde/exportSql!list.do', null, 'N', null, null, 'Y', null, null, 7, null, 'D', null, to_date('25-06-2014 13:36:29', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('IMPORTOPT', '数据导入配置管理', 'SJJH', '/dde/importOpt!list.do', null, 'N', null, null, 'Y', null, null, 9, null, 'D', null, to_date('28-07-2014 14:12:32', 'dd-mm-yyyy hh24:mi:ss'));
insert into F_OPTINFO (OPTID, OPTNAME, PREOPTID, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, WFCODE, PAGETYPE, LASTMODIFYDATE, CREATEDATE)
values ('WSJHRW', 'WS接口交换任务', 'SJJH', '/dde/exchangeTask!list.do?s_taskType=4', null, 'N', null, null, 'Y', null, null, 8, null, 'D', null, to_date('28-08-2014 10:04:03', 'dd-mm-yyyy hh24:mi:ss'));
commit;


insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'OSINFO', 'OSINFO', 'OSINFO', 'OSINFO', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'JLJHDYGX1', 'JLJHDYGX1', 'JLJHDYGX1', 'JLJHDYGX1', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'JHRW', 'JHRW', 'JHRW', 'JHRW', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'EXPORTSJJH', 'EXPORTSJJH', 'EXPORTSJJH', 'EXPORTSJJH', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'DRJHRW', 'DRJHRW', 'DRJHRW', 'DRJHRW', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'DATAOPTINFO', 'DATAOPTINFO', 'DATAOPTINFO', 'DATAOPTINFO', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'EXPORTSQL', 'EXPORTSQL', 'EXPORTSQL', 'EXPORTSQL', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'IMPORTOPT', 'IMPORTOPT', 'IMPORTOPT', 'IMPORTOPT', '', null, null);
insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, LASTMODIFYDATE, CREATEDATE)
values ((select max(t.optcode) from f_optdef t)  + 1 , 'WSJHRW', 'WSJHRW', 'WSJHRW', 'WSJHRW', '', null, null);
commit;

update f_optinfo t set t.opturl = '/dde/exchangeMapinfoNew!list.do' where t.opturl = 
'/dde/exchangeMapInfo!list.do';

update f_optinfo t set t.opturl = '/dde/exchangeTask!list.do?s_taskType=1' where t.opturl = 
'/dde/exchangeTask!list.do';

update D_EXCHANGE_TASK t set t.task_type = '1';

commit;