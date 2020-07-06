package com.centit.dde.po;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/*Name	Code	Comment	Data Type	Length	Precision	Primary	Foreign Key	Mandatory
包ID	RESOURCE_ID		VARCHAR2(64)	64		TRUE	TRUE	TRUE
参数名称	PARAM_Name		VARCHAR2(64)	64		TRUE	FALSE	TRUE
参数提示（中文名）	PARAM_Label		VARCHAR2(200)	200		FALSE	FALSE	TRUE
参数显示样式	PARAM_Display_Style	N:普通 nomal H 隐藏 hide R 只读 readonly	CHAR			FALSE	FALSE	TRUE
参数类型	param_Type	S:文本 N数字  D：日期 T：时间戳（datetime)  	VARCHAR2(64)	64		FALSE	FALSE	FALSE
参数引用类型	param_Reference_Type	0：没有：1： 数据字典 2：JSON表达式 3：sql语句  Y：年份 M：月份	CHAR			FALSE	FALSE	FALSE
参数引用数据	param_Reference_Data	根据paramReferenceType类型（1,2,3）填写对应值	VARCHAR2(1000)	1,000		FALSE	FALSE	FALSE
参数约束表达式	param_Validate_Regex	regex表达式	VARCHAR2(200)	200		FALSE	FALSE	FALSE
参数约束提示	param_Validate_Info	约束不通过提示信息	VARCHAR2(200)	200		FALSE	FALSE	FALSE
参数默认值	param_Default_Value	参数默认值	VARCHAR2(200)	200		FALSE	FALSE	FALSE
条件序号	PARAM_Order		number(2)	2		FALSE	FALSE	FALSE*/
@Data
@Entity
@Table(name = "Q_DATA_PACKET_PARAM")
@ApiModel(value = "数据包参数")
public class DataPacketParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "数据包ID", hidden = true)
    @Column(name = "PACKET_ID")
    @NotBlank(message = "字段不能为空")
    @JSONField(serialize = false)
    private String packetId;

    @Id
    @Column(name = "PARAM_NAME")
    @ApiModelProperty(value = "参数名")
    private String paramName;

    @Column(name = "PARAM_LABEL")
    @ApiModelProperty(value = "参数中文名")
    private String paramLabel;

    @Column(name = "PARAM_Display_Style")
    @ApiModelProperty(value = "参数展示样式（N:普通 normal; H 隐藏 hide; R 只读 readonly）")
    private String paramDisplayStyle;

    @Column(name = "PARAM_TYPE")
    @ApiModelProperty(value = "参数类型（S:文本 N:数字  D：日期 T：时间戳（datetime)）")
    private String paramType;

    @Column(name = "PARAM_REFERENCE_TYPE")
    @ApiModelProperty(value = "参数引用类型（0：没有：1： 数据字典 2：JSON表达式 3：sql语句  Y：年份 M：月份）")
    private String paramReferenceType;

    @Column(name = "PARAM_REFERENCE_DATA")
    @ApiModelProperty(value = "参数引用数据（根据paramReferenceType类型（1,2,3）填写对应值）")
    private String paramReferenceData;

    @Column(name = "PARAM_VALIDATE_REGEX")
    @ApiModelProperty(value = "参数约束表达式（regex表达式）")
    private String paramValidateRegex;

    @Column(name = "PARAM_VALIDATE_INFO")
    @ApiModelProperty(value = "参数约束提示（约束不通过提示信息）")
    private String paramValidateInfo;

    @Column(name = "PARAM_DEFAULT_VALUE")
    @ApiModelProperty(value = "参数默认值")
    private String paramDefaultValue;

    @Column(name = "PARAM_ORDER")
    @ApiModelProperty(value = "条件序号")
    private int paramOrder;
}
