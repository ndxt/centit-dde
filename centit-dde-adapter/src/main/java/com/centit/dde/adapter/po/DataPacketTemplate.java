package com.centit.dde.adapter.po;

import com.alibaba.fastjson2.JSONObject;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@Entity
@Table(name = "q_data_packet_template")
public class DataPacketTemplate  implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(value = "数据处理ID", hidden = true)
    @Id
    @Column(name = "id")
    @NotBlank(message = "字段不能为空")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String id;

    @Column(name = "template_type")
    @ApiModelProperty(value = "模板(操作)类型 1：新建 2：修改 3：删除 4：查询 5：查看 6：创建流程 7：提交流程 8：http调用")
    private Integer templateType;

    @Column(name = "os_id")
    @ApiModelProperty(value = "应用ID")
    private String osId;

    @Column(name = "opt_id")
    @ApiModelProperty(value = "业务模块id")
    private String optId;

    @Column(name = "content")
    @ApiModelProperty(value = "模板内容")
    private JSONObject content;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateTime;

    @Column(name = "create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @Column(name = "is_valid")
    @ApiModelProperty(value = "是否可用")
    private Integer isValid;

    @Column(name = "top_unit")
    @ApiModelProperty(value = "租户code")
    private String topUnit;

    @Column(name = "template_name")
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @Column(name = "packet_template_name")
    @ApiModelProperty(value = "API网关名称")
    private String packetTemplateName;

    @Column(name = "opt_method_template_name")
    @ApiModelProperty(value = "OPT_NAME（f_optdef表字段）")
    private String optMethodTemplateName;

    @Column(name = "task_type")
    @ApiModelProperty(value = "任务类型：1：GET请求，2：表示定时任务,3：POST请求,4：表示消息触发，5：PUT请求,6：DELETE请求")
    private String taskType;

    @Column(name = "schema_props")
    @ApiModelProperty(value = "模式属性")
    private JSONObject schemaProps;
}
