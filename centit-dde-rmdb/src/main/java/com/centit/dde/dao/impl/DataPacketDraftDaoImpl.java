package com.centit.dde.dao.impl;

import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketDraftDao")
public class DataPacketDraftDaoImpl extends BaseDaoImpl<DataPacketDraft, String> implements DataPacketDraftDao{
    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("(like)packetName", "(packet_name like :packetName or packet_id like :packetName)");
        filterField.put("osId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optId",CodeBook.EQUAL_HQL_ID);
        filterField.put("isDisable",CodeBook.EQUAL_HQL_ID);
        filterField.put("taskType",CodeBook.EQUAL_HQL_ID);
        filterField.put("(like)packetId",CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Override
    public void publishDataPacket(String optCode, DataPacketDraft dataPacketCopy) {
        String sql = "update q_data_packet_draft SET publish_date=? ,opt_code=?  WHERE  PACKET_ID=? ";
        this.getJdbcTemplate().update(sql, new Object[]{dataPacketCopy.getPublishDate(), optCode, dataPacketCopy.getPacketId()});
    }

        @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        String sql="UPDATE q_data_packet_draft SET OPT_ID=? ,IS_DISABLE='F' WHERE PACKET_ID = ? ";
        int[] dataPacket = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
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

    @Override
    public void updateDisableStatus(String packetId, String disable) {
        String sql ="UPDATE q_data_packet_draft SET IS_DISABLE=? WHERE PACKET_ID = ? ";
        this.getJdbcTemplate().update(sql, new Object[]{disable,packetId});
    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {
        String delSql ="DELETE FROM q_data_packet_draft WHERE PACKET_ID = ? ";
        this.getJdbcTemplate().batchUpdate(delSql,new BatchPreparedStatementSetter(){
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setString(1, packetIds[i]);
            }
            public int getBatchSize() {
                return packetIds.length;
            }
        });
    }

    @Override
    public int clearTrashStand(String osId) {
        String delSql ="DELETE FROM q_data_packet_draft WHERE IS_DISABLE = 'T' AND  OS_ID=? ";
        int delCount = DatabaseOptUtils.doExecuteSql(this, delSql, new Object[]{osId});
        return  delCount;
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(this, DataPacketDraft.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson),
            CollectionsOpt.createHashMap("packetId", packetId)
        );
    }

}
