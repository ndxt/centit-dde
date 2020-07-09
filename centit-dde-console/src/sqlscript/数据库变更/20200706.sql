--mysql
alter table q_data_packet add  column task_type char(1) NOT NULL DEFAULT '1' COMMENT '1表示普通任务，2表示定时任务';
alter table q_data_packet add  column task_Cron varchar(100);
alter table q_data_packet add  column  last_run_time datetime(0);
alter table q_data_packet add  column  next_run_time datetime(0);
alter table q_data_packet add  column  is_valid varchar(1) default 'T';
alter table q_data_packet add  column  interface_name varchar(64);
alter table q_data_packet add  column  extend_opt_js text;
--postgress
alter table q_data_packet add  column task_type char(1) NOT NULL DEFAULT '1';
alter table q_data_packet add  column task_Cron varchar(100);
alter table q_data_packet add  column  last_run_time timestamp;
alter table q_data_packet add  column  next_run_time timestamp;
alter table q_data_packet add  column  is_valid varchar(1) default 'T';
alter table q_data_packet add  column  interface_name varchar(64);
alter table q_data_packet add  column  extend_opt_js text;