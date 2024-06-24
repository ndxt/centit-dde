function runOpt(eventRuntime, entity) {
  eventRuntime.databaseRunTime.execute("0000000042","delete from d_Task_Log where  run_begin_time<sysdate-1");
  eventRuntime.databaseRunTime.execute("0000000042","delete from d_task_detail_log where run_begin_time<sysdate-1");
  return 0;
}
//传入requestbody保存到表
function runOpt(eventRuntime, bizmodel) {
  eventRuntime.metaObjectService.saveObject('oe27nPnuQ5qJ4CdlXQ7UDQ',bizmodel.fetchDataSetByName('requestBody').getFirstRow());
  return 0;
}
function runOpt(eventRuntime, bizmodel) {
  var map = new java.util.HashMap();
  var size1=bizmodel.fetchDataSetByName('databaseA024a0222').size();
  map.put('size',size1);
  return map;
}
