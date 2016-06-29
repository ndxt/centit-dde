prompt PL/SQL Developer import file
prompt Created on 2013年1月21日 by sx
set feedback off
set define off
prompt Dropping C_PURVIEW...
drop table C_PURVIEW cascade constraints;
prompt Creating C_PURVIEW...
create table C_PURVIEW
(
  purviewcode  VARCHAR2(20) not null,
  purviewname  VARCHAR2(60),
  pureviewdesc VARCHAR2(512)
)
tablespace BSDFW_TBS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
alter table C_PURVIEW
  add constraint PK_C_PURVIEW primary key (PURVIEWCODE)
  using index 
  tablespace BSDFW_TBS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

prompt Disabling triggers for C_PURVIEW...
alter table C_PURVIEW disable all triggers;
prompt Loading C_PURVIEW...
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('MP', '管理项目', '允许在JIRA中管理项目。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('VV', '查看版本控制', '允许查看问题的版本控制信息');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('BP', '浏览项目', '允许浏览项目和项目所属的问题。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('VW', '查看工作流', '拥有这个权限的用户可以查看工作流。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('CQ', '创建问题', '允许创建问题');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('EQ', '编辑问题', '允许编辑问题');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('PQ', '规划问题日程', '允许设置问题的到期日。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('MQ', '移动问题', '允许在不同项目之间或同一个项目不同工作流之间移动问题。请注意，用户必须具有目标项目的创建问题权限才能将问题移动到目标项目中。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('AQ', '分配问题', '允许分配问题给其他用户');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('WA', '被分配', '允许其他用户把问题分配给这个权限的用户。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('SQ', '解决问题', '允许解决和重新打开问题。包括可以设置''解决版本''。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('CLQ', '关闭问题', '允许关闭问题。通常是开发人员解决问题，质检部门负责关闭。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('MR', '修改报告人', '允许在创建和编辑问题时修改报告人。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DQ', '删除问题', '允许删除问题');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('LQ', '链接问题', '允许将多个问题建立联系。只有当链接问题功能打开后才能使用。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('SSL', '设置安全级别', '允许设置一个问题的安全级别，来决定哪些用户可以浏览这个问题。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('VVC', '查看投票人与关注人', '允许查看一个问题的投票人和关注人列表。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('MW', '管理关注列表', '允许管理问题的关注者列表。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('AR', '添加备注', '允许为问题添加备注');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('EAR', '编辑所有备注', '允许编辑所有备注。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('EMR', '编辑自己的备注', '允许编辑自己的备注。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DAR', '删除所有备注', '允许删除所有备注。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DMR', '删除自己的备注', '允许删除自己的备注');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('AA', '添加附件', '这个权限中的用户可以为问题添加附件。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DAA', '删除所有附件', '拥有这个权限的用户可以删除所有附件。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DMA', '删除自己的附件', '拥有这个权限的用户可以删除自己的附件。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('AWL', '添加工作日志', '允许为问题记录工作日志。只有当时间追踪功能打开后才能使用。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('EMWL', '编辑自己的工作日志', '允许编辑自己的工作日志记录。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('EAWL', '编辑所有工作日志', '允许编辑所有人的工作日志记录。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DMWL', '删除自己的工作日志', '允许删除自己的工作日志记录。');
insert into C_PURVIEW (purviewcode, purviewname, pureviewdesc)
values ('DAWL', '删除所有工作日志', '允许删除所有人的工作日志记录。');
commit;
prompt 31 records loaded
prompt Enabling triggers for C_PURVIEW...
alter table C_PURVIEW enable all triggers;
set feedback on
set define on
prompt Done.
