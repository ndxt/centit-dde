package com.centit.dde.services.impl;

import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zc
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketServiceImpl implements DataPacketService {
    private final static String OPTINFO_FORMCODE_COMMON = "C";
    @Autowired
    private DataPacketDao dataPacketDao;

    @Override
    public void createDataPacket(DataPacket dataPacket) {
        dataPacketDao.saveNewObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public void updateDataPacket(DataPacket dataPacket) {
        dataPacketDao.mergeObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(dataPacketDao, DataPacket.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson),
            CollectionsOpt.createHashMap("packetId", packetId)
        );
    }


    @Override
    public void deleteDataPacket(String packetId) {
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        dataPacketDao.deleteObjectById(packetId);
        dataPacketDao.deleteObjectReferences(dataPacket);
    }

    @Override
    public List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public List<Map<String, String>> listDataPacket(String optId) {
        optId = getOptIdWithCommon(optId);
        List<DataPacket> dataPacketList = dataPacketDao.listObjects(CollectionsOpt.createHashMap("optids", optId));
        List<Map<String, String>> mapList = new ArrayList<>();
        for (DataPacket dataPacket : dataPacketList) {
            Map<String, String> map = new HashMap<>(1);
            map.put(dataPacket.getPacketId(), dataPacket.getPacketName());
            mapList.add(map);
        }
        return mapList;
    }

    private String getOptIdWithCommon(String optId) {
        String topUnit = WebOptUtils.getCurrentTopUnit(RequestThreadLocal.getLocalThreadWrapperRequest());
        IOptInfo commonOptInfo = CodeRepositoryUtil.getCommonOptId(topUnit, optId);
        if (commonOptInfo != null) {
            String commonOptId = commonOptInfo.getOptId();
            return StringBaseOpt.concat(optId, ",", commonOptId);
        }
        return optId;
    }

    @Override
    public DataPacket getDataPacket(String packetId) {
        return dataPacketDao.getObjectWithReferences(packetId);
    }


    @Override
    public void publishDataPacket(DataPacket dataPacket) {
        dataPacketDao.mergeObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        String sql = "UPDATE q_data_packet SET OPT_ID=? WHERE PACKET_ID = ?";
        int[] dataPacket = dataPacketDao.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, optId);
                ps.setString(2, apiIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return apiIds.size();
            }
        });
        return dataPacket;
    }


}
