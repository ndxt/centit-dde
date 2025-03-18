package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.dde.adapter.po.DataPacketParam;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OptMethod;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketDraftServiceImpl implements DataPacketDraftService {
    private final static String OPTMETHOD_OPTTYPE_API = "A";
    @Autowired
    private DataPacketDraftDao dataPacketDraftDao;

    @Autowired
    private DataPacketDao dataPacketDao;

    @Autowired(required = false)
    private PlatformEnvironment platformEnvironment;

    public DataPacketDraftServiceImpl() {

    }

    @Override
    public void createDataPacket(DataPacketDraft dataPacketCopy) {
        if(StringUtils.isBlank(dataPacketCopy.getPacketId())) {
            dataPacketCopy.setPacketId(UuidOpt.getUuidAsString32());
        }
        //dataPacketCopy.setOptCode(creatApiOptMethod(dataPacketCopy));
        dataPacketDraftDao.saveNewObject(dataPacketCopy);
        dataPacketDraftDao.saveObjectReferences(dataPacketCopy);
    }

    private String mergeApiOptMethod(DataPacketDraft dataPacket) {
        OptMethod result = assemblyOptMethodGet(dataPacket);
        if (platformEnvironment != null && result != null) {
            OptMethod iOptMethod = platformEnvironment.mergeOptMethod(result);
            return iOptMethod==null?null:iOptMethod.getOptCode();
        }
        return null;
    }

    private OptMethod assemblyOptMethodGet(DataPacketDraft dataPacket) {
        OptMethod result = new OptMethod();
        result.setOptCode(dataPacket.getPacketId());
        result.setOptId(dataPacket.getOptId());
        result.setOptName(dataPacket.getPacketName());
        result.setApiId(dataPacket.getPacketId());
        result.setOptMethod("api");
        result.setOptUrl("/dde/run/" + dataPacket.getPacketId());
        String taskType = dataPacket.getTaskType();
        //任务类型1：GET请求，2：表示定时任务,3：POST请求,4：消息触发 5：PUT请求 6：DELETE请求 7：子模块
        switch (taskType){
            case "1":
                taskType="R";
                break;
            case "2":
            case "4":
            case "7":
                platformEnvironment.deleteOptMethod(dataPacket.getPacketId());
                return null;
            case "3":
                taskType="C";
                break;
            case "5":
                taskType="U";
                break;
            case "6":
                taskType="D";
                break;
            default:
                taskType="未知类型";
        }
        result.setOptReq(taskType);
        result.setOptDesc("请求api网关接口");
        result.setOptType(OPTMETHOD_OPTTYPE_API);
        return result;
    }


    @Override
    public void updateDataPacket(String topUnit, DataPacketDraft dataPacketCopy) {
        Date updateTime = DatetimeOpt.truncateToSecond(DatetimeOpt.currentUtilDate());
        dataPacketCopy.setUpdateDate(updateTime);
        //校验url是否冲突
        if(StringUtils.isNotBlank(dataPacketCopy.getRouteUrl())){
            String packetId = dataPacketDraftDao.getPacketIdByUrl(topUnit, dataPacketCopy.getRouteUrl());
            if(StringUtils.isNotBlank(packetId) && !StringUtils.equals(packetId, dataPacketCopy.getPacketId())){
                throw new ObjectException("路由地址已经存在，冲突的接口ID为：" + packetId);
            }
        }
        dataPacketDraftDao.updateObject(dataPacketCopy);
        dataPacketDraftDao.saveObjectReferences(dataPacketCopy);
        // update error level
        DataPacket dataPacket = dataPacketDao.getObjectById(dataPacketCopy.getPacketId());
        if(dataPacket != null && !Objects.equals(dataPacket.getLogLevel(), dataPacketCopy.getLogLevel())) {

            dataPacketDao.updatePublishPackedStatus(dataPacketCopy.getLogLevel(),
                dataPacketCopy.getIsValid(), dataPacketCopy.getIsDisable(),
                dataPacketCopy.getPacketId());
        }
    }

    @Override
    public void publishDataPacket(DataPacketDraft dataPacketCopy) {
        String optCode = mergeApiOptMethod(dataPacketCopy);
        Date publishTime = DatetimeOpt.truncateToSecond(DatetimeOpt.currentUtilDate());
        dataPacketCopy.setPublishDate(publishTime);
        dataPacketDraftDao.publishDataPacket(optCode, dataPacketCopy);

        DataPacket dataPacket = new DataPacket();
        //复制属性
        BeanUtils.copyProperties(dataPacketCopy, dataPacket);
        dataPacket.setOptCode(dataPacketCopy.getPacketId());
        List<DataPacketParam> dataPacketParamList = new ArrayList<>();
        if (dataPacketCopy.getPacketParams()!=null){
           dataPacketCopy.getPacketParams().forEach(dataPacketParamDraft -> {
               DataPacketParam  dataPacketParam = new DataPacketParam();
               BeanUtils.copyProperties(dataPacketParamDraft, dataPacketParam);
               dataPacketParamList.add(dataPacketParam);
           });
        }
        dataPacket.setPacketParams(dataPacketParamList);
        dataPacket.setPublishDate(publishTime);
        dataPacketDao.publishDataPacket(dataPacket);
    }

    @Override
    public void batchPublishByOsId(String osId){
        List<DataPacketDraft> dataPacketDraftList = dataPacketDraftDao.listNeedPublishDataPacket(osId);
        if(dataPacketDraftList==null || dataPacketDraftList.isEmpty()) return;
        for(DataPacketDraft dataPacketDraft : dataPacketDraftList){
            publishDataPacket(dataPacketDraft);
        }
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
          for(String apiId:apiIds){
              DataPacketDraft dataPacketDraft=getDataPacket(apiId);
              if(dataPacketDraft.getPublishDate()!=null){
                  dataPacketDraft.setOptId(optId);
                  mergeApiOptMethod(dataPacketDraft);
              }
          }
          return dataPacketDraftDao.batchUpdateOptIdByApiId(optId, apiIds);
    }

    @Override
    public void updateDisableStatus(String packetId, String disable) {
       dataPacketDraftDao.updateDisableStatus(packetId, disable);
    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {
        dataPacketDraftDao.batchDeleteByPacketIds(packetIds);
    }

    @Override
    public int clearTrashStand(String osId) {
        return  dataPacketDraftDao.clearTrashStand(osId);
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        dataPacketDraftDao.updateDataPacketOptJson(packetId, dataPacketOptJson);
    }


    @Override
    public void deleteDataPacket(String packetId) {
        DataPacketDraft dataPacketCopy = dataPacketDraftDao.getObjectWithReferences(packetId);
        if(dataPacketCopy!=null) {
            dataPacketDraftDao.deleteObjectReferences(dataPacketCopy);
            dataPacketDraftDao.deleteObjectById(packetId);
        }
    }

   /* @Override
    public List<DataPacketDraft> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketDraftDao.listObjectsByProperties(params, pageDesc);
    }*/

    @Override
    public JSONArray listDataPacketForList(Map<String, Object> params, PageDesc pageDesc){
        return dataPacketDraftDao.listDataPacketDraft(params, pageDesc);
    }

    @Override
    public DataPacketDraft getDataPacket(String packetId) {
        return dataPacketDraftDao.getObjectWithReferences(packetId);
    }

}
