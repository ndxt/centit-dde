truncate table F_ROLEINFO;
insert into F_ROLEINFO (ROLECODE, ROLENAME, ISVALID, ROLEDESC)
values ('G-SYSADMIN', '系统管理员', 'T', '所有系统配置功能');
insert into F_ROLEINFO (ROLECODE, ROLENAME, ISVALID, ROLEDESC)
values ('G-anonymous', '匿名角色', 'T', '匿名用户角色');
