function createSimpleBizMode(){
  var SimpleBizModelClass = Java.type('com.centit.support.dataopt.core.SimpleBizModel');
  var obj = new SimpleBizModelClass();
  return obj;
}

function createSimpleDataSet(){
  var SimpleDataSetClass = Java.type('com.centit.support.dataopt.core.SimpleDataSet');
  var obj = new SimpleDataSetClass();
  return obj;
}

function creataSingleDataSetModel(dsName, dataList) {
  var ds = createSimpleDataSet();
  ds.dataSetName = dsName;
  ds.data = dataList;
  var bizModel = createSimpleBizMode();
  bizModel.modelName = dsName;
  bizModel.addDataSet(ds);
  return bizModel;
}
