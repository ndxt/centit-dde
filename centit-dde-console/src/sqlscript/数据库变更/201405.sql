---2014-5-8
--为表D_EXCHANGE_TASK添加数据类别字段，用于区分该数据是交换任务数据还是导出任务数据：0 交换任务 1 导出任务
alter table DDE.D_EXCHANGE_TASK add TASK_TYPE VARCHAR2(20);
update DDE.F_OPTINFO t set t.opturl='/dde/exchangeTask!list.do?s_taskType=exchange' where t.optid='JHRW';
insert into f_optinfo t (t.optid,t.optname,t.preoptid,t.opturl,t.opttype,t.isintoolbar,t.orderind,t.pagetype) values ('DCRW','导出任务','SJJH','/dde/exchangeTask!list.do?s_taskType=export','M','Y','5','D');
commit;
---2014-5-28
alter table D_Task_Detail_Log add other_message varchar2(500);