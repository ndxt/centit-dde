function runOpt(eventRuntime, entity) {
  eventRuntime.databaseRunTime.execute("0000000042","delete from d_Task_Log where  run_begin_time<sysdate-1");
  eventRuntime.databaseRunTime.execute("0000000042","delete from d_task_detail_log where run_begin_time<sysdate-1");
  return 0;
}
