package com.centit.dde.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("this")
public class DataPacketDaoImpl extends BaseDaoImpl<DataPacket, String> implements DataPacketDao {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>(1);
        filterField.put("(splitforin)optids", "opt_id in (:optids)");
        return filterField;
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(this, DataPacket.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson),
            CollectionsOpt.createHashMap("packetId", packetId)
        );
    }

    @Override
    public void publishDataPacket(DataPacket dataPacket) {
        this.mergeObject(dataPacket);
        this.saveObjectReferences(dataPacket);
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        String sql = "UPDATE q_data_packet SET OPT_ID=?, PUBLISH_DATE =?, IS_DISABLE='F' WHERE PACKET_ID = ?";
        int[] dataPacket = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, optId);
                ps.setTimestamp(2, DatetimeOpt.currentSqlTimeStamp());
                ps.setString(3, apiIds.get(i));
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
        String sql ="UPDATE q_data_packet SET is_disable= ? WHERE PACKET_ID = ? ";
        this.getJdbcTemplate().update(sql, new Object[]{disable, packetId});
    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {
        String delSql ="DELETE FROM q_data_packet WHERE PACKET_ID = ? ";
        this.getJdbcTemplate().batchUpdate(delSql, new BatchPreparedStatementSetter(){
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
    public void updatePublishPackedStatus(int logLevel, Boolean isValid, Boolean isDisable, String  packetId){
        String sql ="UPDATE q_data_packet SET log_level= :logLevel, is_valid = :isValid, is_disable =:isDisable, " +
            " publish_date = :toDay WHERE PACKET_ID = :api ";
        DatabaseOptUtils.doExecuteNamedSql(this, sql, CollectionsOpt.createHashMap(
            "logLevel", logLevel,
            "isValid", isValid==null || isValid ?  BooleanBaseOpt.ONE_CHAR_TRUE : BooleanBaseOpt.ONE_CHAR_FALSE,
            "isDisable", isDisable!=null && isDisable ? BooleanBaseOpt.ONE_CHAR_TRUE : BooleanBaseOpt.ONE_CHAR_FALSE,
            "toDay", DatetimeOpt.currentUtilDate(),
            "api", packetId
        ));
    }

    /**
     * @Column(name = "log_level")
     *     @ApiModelProperty(value = "日志记录级别，1=ERROR,3=INFO,7=DEBUG")
     * @param logLevel
     * @param packetIds
     */
    @Override
    public void updatePackedLogLevel(int logLevel, List<String>  packetIds){
        String sql ="UPDATE q_data_packet SET log_level = :logLevel, publish_date = :toDay WHERE PACKET_ID in (:apis) ";
        DatabaseOptUtils.doExecuteNamedSql(this, sql, CollectionsOpt.createHashMap(
            "logLevel", logLevel,
            "toDay", DatetimeOpt.currentUtilDate(),
            "apis", packetIds
        ));

        sql ="UPDATE q_data_packet_draft SET log_level= :logLevel WHERE PACKET_ID in (:apis) ";
        DatabaseOptUtils.doExecuteNamedSql(this, sql, CollectionsOpt.createHashMap(
            "logLevel", logLevel,
            "apis", packetIds
        ));
    }

    @Override
    public void updateApplicationLogLevel(int logLevel, String osId){
        String sql = "UPDATE q_data_packet SET log_level = ?, publish_date = ? WHERE os_id = ?";
        DatabaseOptUtils.doExecuteSql(this, sql, new Object[]
            {logLevel,DatetimeOpt.currentUtilDate(), osId});

        sql = "UPDATE q_data_packet_draft SET log_level = ? WHERE os_id = ?";
        DatabaseOptUtils.doExecuteSql(this, sql, new Object[]{logLevel, osId});
    }

    @Override
    public JSONArray listApiWithRoute(String topUnit){
        return DatabaseOptUtils.listObjectsBySqlAsJson(this,
            "select a.packet_id, a.route_url, a.task_type " +
                "from q_data_packet a join f_os_info b on a.os_id = b.os_id " +
                "where b.top_unit = ? and a.route_url is not null", new Object[]{topUnit});
    }

    @Override
    public int clearTrashStand(String osId) {
        String delSql ="DELETE FROM q_data_packet WHERE IS_DISABLE = 'T' AND OS_ID=? ";
        int delCount = DatabaseOptUtils.doExecuteSql(this, delSql, new Object[]{osId});
        return  delCount;
    }

    @Override
    public Map<String, Object> statApplicationInfo(String applicationId) {
        Map<String, Object> map = new HashMap<>(8);

        int apiCount = NumberBaseOpt.castObjectToInteger( DatabaseOptUtils.getScalarObjectQuery(this,
            "select count(*) as dataSum from q_data_packet where os_id = ?", new Object[]{applicationId}), 0);
        int pageCount = NumberBaseOpt.castObjectToInteger( DatabaseOptUtils.getScalarObjectQuery(this,
            "select count(*) as dataSum from m_meta_form_model where os_id = ?", new Object[]{applicationId}), 0);
        int flowCount = NumberBaseOpt.castObjectToInteger( DatabaseOptUtils.getScalarObjectQuery(this,
            "select count(distinct flow_code) as dataSum from wf_flow_define where os_id = ?", new Object[]{applicationId}), 0);
        int moduleCount = NumberBaseOpt.castObjectToInteger( DatabaseOptUtils.getScalarObjectQuery(this,
            "select count(*) as dataSum from f_optinfo where os_id = ?", new Object[]{applicationId}), 0);
        map.put("apiCount", apiCount);
        map.put("pageCount", pageCount);
        map.put("flowCount", flowCount);
        map.put("moduleCount", moduleCount);
        return map;
    }

    @Override
    public Map<String, String> mapDataPacketName(List<String> packetIds) {
        if(packetIds == null || packetIds.isEmpty()) return Collections.emptyMap();
        JSONArray jsonArray = DatabaseOptUtils.listObjectsByNamedSqlAsJson(this,
            "select a.packet_id, a.packet_name " +
                "from q_data_packet a " +
                "where a.packet_id in (:ids) ", CollectionsOpt.createHashMap("ids", packetIds));
        if(jsonArray==null) return Collections.emptyMap();
        Map<String, String> map = new HashMap<>(jsonArray.size());
        for (Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            map.put(jsonObject.getString("packetId"), jsonObject.getString("packetName"));
        }
        return map;
    }
}
