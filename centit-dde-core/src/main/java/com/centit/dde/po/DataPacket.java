package com.centit.dde.po;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * @author zhf
 */
@ApiModel
@Data
@Entity
@Table(name = "Q_DATA_PACKET")
public class DataPacket implements Serializable,DataPacketInterface {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "数据处理ID", hidden = true)
    @Id
    @Column(name = "PACKET_ID")
    @NotBlank(message = "字段不能为空")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String packetId;

    @ApiModelProperty(value = "数据处理名称")
    @Column(name = "PACKET_NAME")
    @NotBlank(message = "字段不能为空")
    private String packetName;

    /**
     * 详细描述
     */
    @ApiModelProperty(value = "详细描述")
    @Column(name = "PACKET_DESC")
    private String packetDesc;

    @Column(name = "OWNER_TYPE")
    @ApiModelProperty(value = "属主类别（D:部门；U:用户）")
    private String ownerType;

    @Column(name = "OWNER_CODE")
    @ApiModelProperty(value = "属主代码")
    private String ownerCode;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DATA_OPT_DESC_JSON")
    @ApiModelProperty(value = "数据预处理描述 json格式的数据预处理说明", required = true)
    private JSONObject dataOptDescJson;

    @Column(name = "BUFFER_FRESH_PERIOD")
    @ApiModelProperty(value = "数据缓存有效期，-1：不缓存（默认值） 0 永不失效 1 一日，2 按周（注意不是一周） 3 按月 4 按年， >=60 代表时间单位为秒", required = true)
    private Integer bufferFreshPeriod;

    @Column(name = "RECORDER")
    @ApiModelProperty(value = "创建人", hidden = true)
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @Column(name = "RECORD_DATE")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date recordDate;

    @Column(name = "UPDATE_DATE")
    @ApiModelProperty(value = "修改时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateDate;

    @ApiModelProperty(value = "业务模块代码")
    @Column(name = "os_id")
    private String osId;

    @ApiModelProperty(value = "所属业务")
    @Column(name = "OPT_ID")
    private String optId;

    @Column(name = "TASK_TYPE")
    @ApiModelProperty(value = "任务类型,1表示普通任务，2表示定时任务", required = true)
    @NotBlank
    private String taskType;

    @Column(name = "TASK_CRON")
    @ApiModelProperty(value = "任务执行定时器")
    private String taskCron;

    @Column(name = "LAST_RUN_TIME")
    @ApiModelProperty(value = "上次执行时间")
    private Date lastRunTime;

    @Column(name = "NEXT_RUN_TIME")
    @ApiModelProperty(value = "下次执行时间")
    private Date nextRunTime;

    @Column(name = "IS_VALID")
    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean isValid;

    @Column(name = "NEED_ROLLBACK")
    @ApiModelProperty(value = "是否回滚并结束")
    private String needRollback;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "RETURN_RESULT")
    @ApiModelProperty(value = "桩数据", required = true)
    private JSONObject returnResult;

    @Column(name = "publish_date")
    @ApiModelProperty(value = "发布时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private String publishDate;

    @Column(name = "EXT_PROPS")
    @ApiModelProperty(value = "队列配置扩展信息，独有配置，公共的配置在资源管理页面添加")
    private JSONObject extProps;

    @OneToMany(targetEntity = DataPacketParam.class)
    @JoinColumn(name = "packetId", referencedColumnName = "packetId")
    private List<DataPacketParam> packetParams;

    public List<DataPacketParam> getPacketParams() {
        if (packetParams == null) {
            packetParams = new ArrayList<>(2);
        }
        return packetParams;
    }

    @Override










    @JSONField(serialize = false)
    public Map<String, Object> getPacketParamsValue() {
        Map<String, Object> params = new HashMap<>(10);
        if (packetParams == null) {
            return params;
        }
        for (DataPacketParam packetParam : packetParams) {
            if (packetParam.getParamType() != null) {
                switch (packetParam.getParamType()) {
                    case "N":
                        params.put(packetParam.getParamName(), NumberBaseOpt.castObjectToLong(packetParam.getParamDefaultValue()));
                        break;
                    case "T":
                        params.put(packetParam.getParamName(), DatetimeOpt.castObjectToSqlDate(packetParam.getParamDefaultValue()));
                        break;
                    default:
                        params.put(packetParam.getParamName(), packetParam.getParamDefaultValue());
                        break;
                }
            }
        }
        return params;
    }

}
