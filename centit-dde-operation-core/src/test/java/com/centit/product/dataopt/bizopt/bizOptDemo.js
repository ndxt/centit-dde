function doBiz01(bizModel) {
  var item = {};
  item.cust = bizModel.bizData.ds1.data.length
  item.city = bizModel.bizData.ds2.data.length

  return creataSingleDataSetModel('default', [item,item] ,bizModel)
}
