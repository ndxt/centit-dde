package com.centit.dde.services.impl;

import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.dde.adapter.po.DataPacketParam;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.IOptMethod;
import com.centit.framework.system.po.OptMethod;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(StringBaseOpt.isNvl(dataPacketCopy.getPacketId())) {
            dataPacketCopy.setPacketId(UuidOpt.getUuidAsString32());
        }
        //dataPacketCopy.setOptCode(creatApiOptMethod(dataPacketCopy));
        dataPacketDraftDao.saveNewObject(dataPacketCopy);
        dataPacketDraftDao.saveObjectReferences(dataPacketCopy);
    }

    private String mergeApiOptMethod(DataPacketDraft dataPacket) {
        OptMethod result = assemblyOptMethodGet(dataPacket);
        if (platformEnvironment != null && result != null) {
            IOptMethod iOptMethod = platformEnvironment.mergeOptMethod(result);
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
    public void updateDataPacket(DataPacketDraft dataPacketCopy) {
        dataPacketDraftDao.updateObject(dataPacketCopy);
        dataPacketDraftDao.saveObjectReferences(dataPacketCopy);
    }


    @Override
    public void publishDataPacket(DataPacketDraft dataPacketCopy) {
        String optCode = mergeApiOptMethod(dataPacketCopy);
        dataPacketDraftDao.publishDataPacket(optCode, dataPacketCopy);

        DataPacket dataPacket = new DataPacket();
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
        dataPacketDao.publishDataPacket(dataPacket);
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId,List<String> apiIds) {
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
        dataPacketDraftDao.deleteObjectById(packetId);
        dataPacketDraftDao.deleteObjectReferences(dataPacketCopy);
    }

    @Override
    public List<DataPacketDraft> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketDraftDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public DataPacketDraft getDataPacket(String packetId) {
        return dataPacketDraftDao.getObjectWithReferences(packetId);
    }

}
