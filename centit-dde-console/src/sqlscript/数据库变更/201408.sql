
create table D_IMPORT_TRIGGER  (
   trigger_id           number(10)                      not null,
   IMPORT_id            number(12)                      not null,
   trigger_sql          varchar2(2000),
   trigger_desc         varchar2(500),
   trigger_type         char,
   trigger_time         char,
   tigger_order         number(4),
   ISPROCEDURE          char,
   constraint PK_D_IMPORT_TRIGGER primary key (trigger_id, IMPORT_id)
);

comment on column D_IMPORT_TRIGGER.trigger_type is
'L 、行触发器（迁移时对每一条数据执行一遍）
T 、表触发器（数据迁移前或者 完成时执行，一个对应关系只执行一次）';

comment on column D_IMPORT_TRIGGER.trigger_time is
'B 交换前，A 交换后 ，E  交换失败后';

comment on column D_IMPORT_TRIGGER.tigger_order is
'执行顺序是指同一类型中的处罚器执行顺序
执行类别：1、表迁移前源、2、表迁移前目标、 3、行迁移前目标 、4、行迁移后目标、5、行迁移后源，6、迁移失败后目标、7、迁移失败后源、8、表迁移后目标、9、表迁移后源';

alter table D_IMPORT_TRIGGER
   add constraint FK_D_IMPORT_REFERENCE_D_IMPORT foreign key (IMPORT_id)
      references D_IMPORT_OPT (IMPORT_id);

      
---- 8:22更新，添加导出触发器表
      
create table D_EXPORT_TRIGGER  (
   trigger_id           number(10)                      not null,
   Export_id            number(10)                      not null,
   trigger_sql          varchar2(2000),
   trigger_desc         varchar2(500),
   trigger_type         char,
   trigger_time         char,
   tigger_order         number(4),
   ISPROCEDURE          char                           default '0',
   constraint PK_D_EXPORT_TRIGGER primary key (trigger_id, Export_id)
);

comment on column D_EXPORT_TRIGGER.trigger_type is
'L 、行触发器（迁移时对每一条数据执行一遍）
T 、表触发器（数据迁移前或者 完成时执行，一个对应关系只执行一次）';

comment on column D_EXPORT_TRIGGER.trigger_time is
'B 交换前，A 交换后 ，E  交换失败后';

comment on column D_EXPORT_TRIGGER.tigger_order is
'执行顺序是指同一类型中的处罚器执行顺序
执行类别：1、表迁移前源、2、表迁移前目标、 3、行迁移前目标 、4、行迁移后目标、5、行迁移后源，6、迁移失败后目标、7、迁移失败后源、8、表迁移后目标、9、表迁移后源';

alter table D_EXPORT_TRIGGER
   add constraint FK_D_EXPORT_REFERENCE_D_EXPORT foreign key (Export_id)
      references D_Export_SQL (Export_id);
