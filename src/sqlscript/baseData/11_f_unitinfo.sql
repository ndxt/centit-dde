prompt PL/SQL Developer import file
prompt Created on 2012年8月23日 by sx
set feedback off
set define off
prompt Disabling triggers for F_UNITINFO...
alter table F_UNITINFO disable all triggers;
prompt Deleting F_UNITINFO...
delete from F_UNITINFO;
commit;
prompt Loading F_UNITINFO...
insert into F_UNITINFO (unitcode, parentunit, unittype, isvalid, unitname, unitdesc, addrbookid)
values ('d00001', null, 'A', 'T', '机构', null, null);
commit;
prompt 1 records loaded
prompt Enabling triggers for F_UNITINFO...
alter table F_UNITINFO enable all triggers;
set feedback on
set define on
prompt Done.
