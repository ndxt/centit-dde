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
@Table(name = "q_data_packet_draft_copy1")
public class DataPacketDraft implements Serializable, DataPacketInterface {
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
    @ApiModelProperty(value = "缓存时间", required = true)
    private Integer bufferFreshPeriod;

    @Column(name = "buffer_fresh_period_type")
    @ApiModelProperty(value = "缓存单位, 1:分 2:时 3:日 -1:不缓存")
    private Integer bufferFreshPeriodType;

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
    //@ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateDate;

    @ApiModelProperty(value = "业务模块代码", required = true)
    @Column(name = "os_id")
    @NotBlank(message = "osId字段不能为空")
    private String osId;

    @ApiModelProperty(value = "所属模块", required = true)
    @Column(name = "OPT_ID")
    @NotBlank(message = "optId字段不能为空")
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

    @Column(name = "publish_date")
    @ApiModelProperty(value = "发布时间")
    private Date publishDate;

    @Column(name = "EXT_PROPS")
    @ApiModelProperty(value = "队列配置扩展信息，独有配置，公共的配置在资源管理页面添加")
    private JSONObject extProps;

    @Column(name = "opt_code")
    @ApiModelProperty(value = "f_optdef表主键")
    private String optCode;

    @Column(name = "template_type")
    @ApiModelProperty(value = "模板(操作)类型：1：新建 2：修改 3：删除 4：查询 5：查看 6：创建流程 7：提交流程 8：http调用")
    private Integer templateType;
    /**
     * template_type  metadata_table_id  这2个字段都是给前端做判断使用的，dde本身不需要这2个字段
     */
    @Column(name = "metadata_table_id")
    @ApiModelProperty(value = "通过元数据模板生成接口时会选择一个表，这个存选择的表ID")
    private String metadataTableId;

    @OneToMany(targetEntity = DataPacketParamDraft.class)
    @JoinColumn(name = "packetId", referencedColumnName = "packetId")
    private List<DataPacketParamDraft> packetParams;

    @ApiModelProperty(hidden = true)
    private Object optMethod;

    public List<DataPacketParamDraft> getPacketParams() {
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
        for (DataPacketParamDraft packetParam : packetParams) {
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
