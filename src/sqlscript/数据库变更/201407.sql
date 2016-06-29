alter table D_Export_SQL add table_store_type char default '0';

comment on column D_Export_SQL.table_store_type is
'1、 infile  0 、embedded ';


alter table D_DATA_OPT_INFO add OPT_NAME varchar2(100);