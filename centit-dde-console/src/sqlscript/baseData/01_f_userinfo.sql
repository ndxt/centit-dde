truncate table F_USERINFO;
insert into F_USERINFO (USERCODE, USERPIN, ISVALID, LOGINNAME, USERNAME, USERDESC, LOGINTIMES, ACTIVETIME, LOGINIP, ADDRBOOKID, REGEMAIL, USERPWD)
values ('noname', '67b74fe1423796dfe8db34b959b81fbd', 'T', 'noname', '匿名用户', '匿名用户', null, null, null, null, null, null);
insert into F_USERINFO (USERCODE, USERPIN, ISVALID, LOGINNAME, USERNAME, USERDESC, LOGINTIMES, ACTIVETIME, LOGINIP, ADDRBOOKID, REGEMAIL, USERPWD)
values ('99999999', '739d8080c7b12fb05d3f11830c3f2655', 'T', 'admin', '系统管理员', null, null, null, null, null, null, null);