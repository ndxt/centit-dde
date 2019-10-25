DROP TABLE IF EXISTS d_exchange_task;
CREATE TABLE IF NOT EXISTS d_exchange_task (
  Task_ID varchar(32) NOT NULL,
  PACKET_ID varchar(32) DEFAULT NULL,
  task_name varchar(100) NOT NULL,
  task_type char(1) NOT NULL DEFAULT '1' COMMENT '1: 直接交换 2 :导出离线文件 3：监控文件夹导入文件 4：调用接口 5:接口事件',
  task_Cron varchar(100) DEFAULT NULL,
  task_desc varchar(500) DEFAULT NULL,
  exchange_desc_json longtext,
  last_run_time datetime DEFAULT NULL,
  next_run_time datetime DEFAULT NULL,
  is_valid varchar(1) DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  created varchar(8) DEFAULT NULL,
  last_update_time datetime DEFAULT NULL,
  PRIMARY KEY (Task_ID)
);
DROP TABLE IF EXISTS d_task_detail_log;
CREATE TABLE IF NOT EXISTS d_task_detail_log (
  log_detail_id varchar(32) NOT NULL,
  Task_ID varchar(32) DEFAULT NULL,
  log_id varchar(32) DEFAULT NULL,
  log_type varchar(200) DEFAULT NULL COMMENT '运行步骤',
  run_begin_time datetime DEFAULT NULL,
  run_end_time datetime DEFAULT NULL,
  log_info varchar(2000) DEFAULT NULL COMMENT 'opttype=1,3,4',
  success_pieces decimal(12,0) DEFAULT NULL,
  error_pieces decimal(12,0) DEFAULT NULL,
  PRIMARY KEY (log_detail_id)
);
DROP TABLE IF EXISTS d_task_log;
CREATE TABLE IF NOT EXISTS d_task_log (
  log_id varchar(32) NOT NULL,
  Task_ID varchar(32) DEFAULT NULL,
  run_begin_time datetime DEFAULT NULL,
  run_end_time datetime DEFAULT NULL,
  run_type varchar(100) DEFAULT NULL COMMENT '运行名字',
  runner varchar(8) DEFAULT NULL,
  other_message varchar(500) DEFAULT NULL COMMENT '比如数据库连接失败',
  ERROR_PIECES varchar(100) DEFAULT NULL,
  SUCCESS_PIECES varchar(100) DEFAULT NULL,
  PRIMARY KEY (log_id)
);