alter table q_data_packet add  column  return_type char(1);
alter table q_data_packet add  column  return_result varchar(2000);
alter table q_data_packet drop  column extend_opt_js;