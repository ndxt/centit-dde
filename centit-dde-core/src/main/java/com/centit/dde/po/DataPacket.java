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
public class DataPacket implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "数据包ID", hidden = true)
    @Id
    @Column(name = "PACKET_ID")
    @NotBlank(message = "字段不能为空")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String packetId;

    @ApiModelProperty(value = "数据包名称")
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

    @Column(name = "HAS_DATA_OPT")
    @ApiModelProperty(value = "是否有数据预处理", required = true)
    private String hasDataOpt;


    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DATA_OPT_DESC_JSON")
    @ApiModelProperty(value = "数据预处理描述 json格式的数据预处理说明", required = true)
    private JSONObject dataOptDescJson;

    @Column(name = "BUFFER_FRESH_PERIOD")
    @ApiModelProperty(value = "数据缓存有效期，-1：不缓存（默认值） 0 永不失效 1 一日，2 按周（注意不是一周） 3 按月 4 按年， >=60 代表时间单位为秒", required = true)
    private Integer bufferFreshPeriod;

    @Column(name = "RECORDER")
    @ApiModelProperty(value = "修改人", hidden = true)
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @Column(name = "RECORD_DATE")
    @ApiModelProperty(value = "修改时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    @JSONField(serialize = false)
    private Date recordDate;

    @ApiModelProperty(value = "业务模块代码")
    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "TASK_TYPE")
    @ApiModelProperty(value = "任务类型,1表示普通任务，2表示定时任务", required = true)
    @NotBlank
    private String taskType;

    @Column(name = "TASK_CRON")
    @ApiModelProperty(value = "任务执行定时器")
    private String taskCron;
    @Column(name = "INTERFACE_NAME")
    @ApiModelProperty(value = "接口名称(英文不含特殊字符,唯一性)")
    private String interfaceName;
    @Column(name = "LAST_RUN_TIME")
    @ApiModelProperty(value = "上次执行时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date lastRunTime;

    @Column(name = "NEXT_RUN_TIME")
    @ApiModelProperty(value = "下次执行时间")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date nextRunTime;

    @Column(name = "IS_VALID")
    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean isValid;
    @Column(name = "IS_WHILE")
    @ApiModelProperty(value = "是否循环", required = true)
    private Boolean isWhile;

    /**
     * 添加返回数据：
     * S: 仅仅返回状态 ; code=0 成功 code = ERROR_CODE 错误
     * D: 返回dataset ;返回所有处理过程中没有使用过的dataset，并接受参数可以之返回部分dataset
     * A: 返回指定的一个或多个dataset
     * J: 返回json，通过 json 模板指定
     */
    @ApiModelProperty(value = "业务特殊处理脚本")
    @Column(name = "EXTEND_OPT_JS")
    @Basic(fetch = FetchType.LAZY)
    private String extendOptJs;

    @OneToMany(targetEntity = DataPacketParam.class)
    @JoinColumn(name = "packetId", referencedColumnName = "packetId")
    private List<DataPacketParam> packetParams;

    @OneToMany(targetEntity = DataSetDefine.class)
    @JoinColumn(name = "packetId", referencedColumnName = "packetId")
    private List<DataSetDefine> dataSetDefines;

    public DataPacket() {
        bufferFreshPeriod = -1;
    }

    public List<DataSetDefine> getDataSetDefines() {
        return dataSetDefines;
    }

    public List<DataPacketParam> getPacketParams() {
        if (packetParams == null) {
            packetParams = new ArrayList<>(2);
        }
        return packetParams;
    }

    @JSONField(serialize = false)
    public Map<String, Object> getPacketParamsValue() {
        Map<String, Object> params = new HashMap<>(10);
        if (packetParams == null) {
            return params;
        }
        for (DataPacketParam packetParam : packetParams) {
            if(packetParam.getParamType()!=null) {
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
