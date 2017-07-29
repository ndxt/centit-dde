---2014-6-11
alter table D_DataBase_Info add SOURCE_OS_ID varchar2(20);

alter table D_Exchange_Task add task_type char(1) default '1'  not null 
                            add last_update_time     date 
   							add store_isolation      char(1)
   							add monitor_folder       varchar2(200);
   							
comment on column D_Exchange_Task.task_type is
'1: 直接交换 2 :导出离线文件 3：监控文件夹导入文件 4：调用接口 5:接口事件';

comment on column D_Exchange_Task.store_isolation is
'task_type=2 有效';

comment on column D_Exchange_Task.monitor_folder is
'task_type=3 有效';

alter table D_Task_Log add task_type char(1) default '1'  not null;

comment on column D_Task_Log.task_type is
'1: 直接交换 2 :导出离线文件 3：监控文件夹导入文件 4：调用接口 5: 接口事件';


--- 数据量类型和PDM中的表结构一直

alter table D_EXCHANGE_MAPINFO modify mapinfo_id number(12);

alter table D_MAPINFO_DETAIL modify mapinfo_id number(12);

alter table D_MAPINFO_TRIGGER modify mapinfo_id number(12);

alter table D_TASK_ERROR_DATA modify log_detail_id number(12);

create sequence D_TASK_ID minvalue 10000;

alter table D_EXCHANGE_TASK add n_task_id number(12);
alter table D_Task_Log add n_task_id number(12) add n_Log_id number(12);

alter table D_EXCHANGE_TASKDETAIL add n_MAPINFO_ID number(12) add n_task_id number(12);

alter table D_TASK_DETAIL_LOG add n_LOG_DETAIL_ID number(12) add n_Log_id number(12)  modify mapinfo_id number(12);

update D_EXCHANGE_TASK set n_task_id=D_TASK_ID.nextVal;
update  D_Task_Log set n_Log_id = to_number(log_id);
update  D_Task_Log b set b.n_task_id = (select a.n_task_id from D_EXCHANGE_TASK a where a.task_id=b.task_id);

update  D_EXCHANGE_TASKDETAIL set n_MAPINFO_ID = to_number(MAPINFO_ID);
update  D_EXCHANGE_TASKDETAIL b set b.n_task_id = (select a.n_task_id from D_EXCHANGE_TASK a where a.task_id=b.task_id);

update  D_TASK_DETAIL_LOG set n_Log_id = to_number(log_id) , n_LOG_DETAIL_ID = to_number(LOG_DETAIL_ID) ;

commit;

alter table D_EXCHANGE_TASK drop column task_id;
alter table D_EXCHANGE_TASK rename column n_task_id to task_id;
alter table D_EXCHANGE_TASK add constraint pk_D_EXCHANGE_TASK primary key (task_id);

alter table D_TASK_DETAIL_LOG drop constraint FK_D_TASK_D_REFERENCE_D_TASK_L;

alter table D_Task_Log drop column task_id;
alter table D_Task_Log drop column log_id ;
alter table D_Task_Log rename column n_task_id to task_id;
alter table D_Task_Log rename column n_log_id to log_id;
alter table D_Task_Log add constraint pk_D_Task_Log primary key (log_id);


alter table D_EXCHANGE_TASKDETAIL drop constraint PK_D_EXCHANGE_TASKDETAIL;
alter table D_EXCHANGE_TASKDETAIL drop column task_id;
alter table D_EXCHANGE_TASKDETAIL drop column MAPINFO_ID ;
alter table D_EXCHANGE_TASKDETAIL rename column n_task_id to task_id;
alter table D_EXCHANGE_TASKDETAIL rename column n_MAPINFO_ID to MAPINFO_ID;
alter table D_EXCHANGE_TASKDETAIL add constraint pk_D_EXCHANGE_TASKDETAIL primary key (task_id,MAPINFO_ID);


alter table D_TASK_DETAIL_LOG drop column LOG_DETAIL_ID;
alter table D_TASK_DETAIL_LOG drop column log_id ;
alter table D_TASK_DETAIL_LOG rename column n_LOG_DETAIL_ID to LOG_DETAIL_ID;
alter table D_TASK_DETAIL_LOG rename column n_log_id to log_id;
alter table D_TASK_DETAIL_LOG add constraint pk_D_TASK_DETAIL_LOG primary key (LOG_DETAIL_ID);


alter table D_TASK_DETAIL_LOG  add   opt_type             CHAR(1)
   			add OS_ID                varchar2(20);
 

comment on column D_Task_Detail_Log.mapinfo_id is
'opttype=1,3,4';

comment on column D_Task_Detail_Log.opt_type is
'1, 交换 3 导出 4 导入 2 调用WS';

comment on column D_Task_Detail_Log.OS_ID is
'opt_type=2';
   			
-----------------------------------------------------
--2014-6-24 创建导入导出相关信息表
create table D_Export_SQL  (
   Export_id            number(10)                      not null,
   Source_Database_name varchar2(100),
   SOURCE_OS_ID         varchar2(20),
   Export_name          varchar2(100)                   not null,
   Query_sql            varchar2(4000),
   created              varchar2(8),
   after_sql_block      varchar2(4000),
   export_desc          varchar2(500),
   last_update_time     date,
   create_time          date,
   before_sql_block     varchar2(4000),
   Data_Opt_ID           varchar2(20),
   constraint PK_D_EXPORT_SQL primary key (Export_id)
);

comment on table D_Export_SQL is
'主键 通过 D_MAPINFOID 序列生成';


create table D_Export_FIELD  (
   Export_id            number(10)                      not null,
   column_no            number(4)                       not null,
   field_name           varchar2(100)                   not null,
   field_sentence       varchar2(200)                   not null,
   field_type           varchar2(100),
   field_format         varchar2(100),
   field_store_type     char(1),
   is_pk                char(1),
   constraint PK_D_EXPORT_FIELD primary key (Export_id, column_no)
);

comment on column D_Export_FIELD.field_store_type is
'infile|embedded ';

comment on column D_Export_FIELD.is_pk is
'1、是  0、否';


create table D_OS_INFO  (
   OS_ID                varchar2(20)                    not null,
   OS_NAME              varchar2(200)                   not null,
   has_interface        CHAR                            not null,
   interface_url        varchar2(200),
   created              varchar2(8),
   last_update_time     date,
   create_time          date,
   os_user              varchar2(20),
   os_password          varchar2(100),
   constraint PK_D_OS_INFO primary key (OS_ID)
);

comment on column D_OS_INFO.os_password is
'加密';

create table D_IMPORT_OPT  (
   IMPORT_id            number(12)                      not null,
   Dest_Database_name   varchar2(100),
   SOURCE_OS_ID         varchar2(20),
   Import_name          varchar2(100)                   not null,
   table_name           varchar2(100),
   created              varchar2(8),
   after_import_block   varchar2(4000),
   before_import_block  varchar2(4000),
   export_desc          varchar2(500),
   last_update_time     date,
   create_time          date,
   constraint PK_D_IMPORT_OPT primary key (IMPORT_id)
);


create table D_IMPORT_FIELD  (
   IMPORT_id            number(12)                      not null,
   column_no            number(4)                       not null,
   source_field_name    varchar2(100)                   not null,
   dest_field_name      varchar2(100)                   not null,
   dest_field_type      varchar2(100),
   is_pk                char(1),
   dest_field_default   varchar2(200),
   is_null              char(1),
   constraint PK_D_IMPORT_FIELD primary key (column_no, IMPORT_id)
);

comment on column D_IMPORT_FIELD.source_field_name is
'来自导入文件 或者 XML';

comment on column D_IMPORT_FIELD.is_pk is
'1、是  0、否';

comment on column D_IMPORT_FIELD.is_null is
'1、是  0、否';

alter table D_IMPORT_FIELD
   add constraint FK_D_IMPORT_REFERENCE_D_IMPORT foreign key (IMPORT_id)
      references D_IMPORT_OPT (IMPORT_id);


create table D_DATA_OPT_INFO  (
   Data_Opt_ID          varchar2(20)                    not null,
   opt_desc             varchar2(500),
   created              varchar2(8),
   last_update_time     date,
   create_time          date,
   constraint PK_D_DATA_OPT_INFO primary key (Data_Opt_ID)
);

comment on column D_DATA_OPT_INFO.Data_Opt_ID is
'mapinfoID';

create table D_DATA_OPT_STEP  (
   Data_Opt_ID          varchar2(20),
   Opt_STEP_ID          number(12)                      not null,
   opt_type             CHAR                            not null,
   IMPORT_id            number(12),
   OS_ID                varchar2(20),
   mapinfo_order        number(4),
   constraint PK_D_DATA_OPT_STEP primary key (Opt_STEP_ID)
);

comment on column D_DATA_OPT_STEP.opt_type is
'1 : 导入 2：调用接口';

comment on column D_DATA_OPT_STEP.IMPORT_id is
'opt_type=1';

comment on column D_DATA_OPT_STEP.OS_ID is
'opt_type=2';

alter table D_DATA_OPT_STEP
   add constraint FK_D_DATA_O_REFERENCE_D_DATA_O foreign key (Data_Opt_ID)
      references D_DATA_OPT_INFO (Data_Opt_ID);

alter table D_DATA_OPT_STEP
   add constraint FK_D_DATA_O_REFERENCE_D_OS_INF foreign key (OS_ID)
      references D_OS_INFO (OS_ID);

alter table D_DATA_OPT_STEP
   add constraint FK_D_DATA_O_REFERENCE_D_IMPORT foreign key (IMPORT_id)
      references D_IMPORT_OPT (IMPORT_id);

      
alter table D_IMPORT_OPT 
   add record_operate       char(1) default '3';
   
comment on column D_IMPORT_OPT.record_operate is
'1、插入（insert）
2、更新（update）
3、合并（merge）';