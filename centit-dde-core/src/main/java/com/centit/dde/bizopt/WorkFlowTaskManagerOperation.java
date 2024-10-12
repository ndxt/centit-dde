package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.po.RoleRelegate;
import com.centit.workflow.service.FlowManager;

import java.util.Date;
import java.util.List;

public class WorkFlowTaskManagerOperation implements BizOperation {

    private FlowManager flowManager;

    public WorkFlowTaskManagerOperation(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    /**
     * 任务转移，
     * 任务委托（增加 、删除、 查）
     */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String taskType = bizOptJson.getString("taskType");
        String fromUserCode, toUserCode;
        int result;
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
        switch (taskType) {
            //根据节点实例转移任务
            case "moveTask":
                Object nodeInstIds = JSONTransformer.transformer(bizOptJson.getString("nodeInstIds"), transform);
                fromUserCode = StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("fromUserCode"), transform));
                toUserCode = StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("toUserCode"), transform));
                result = flowManager.moveUserTaskTo(StringBaseOpt.objectToStringList(nodeInstIds), fromUserCode, toUserCode, dataOptContext.getCurrentUserDetail(), "");
                bizModel.putDataSet(id, new DataSet(result));
                break;
            //根据应用转移任务
            case "moveTaskByOs":
                fromUserCode = StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("fromUserCode"), transform));
                toUserCode = StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("toUserCode"), transform));
                result = flowManager.moveUserTaskTo(dataOptContext.getOsId(), fromUserCode, toUserCode, dataOptContext.getCurrentUserDetail(), "");
                bizModel.putDataSet(id, new DataSet(result));
                break;
            //创建任务委托
            case "createRelegate":
                RoleRelegate roleRelegate = new RoleRelegate();
                roleRelegate.setExpireTime(DatetimeOpt.castObjectToDate(
                    JSONTransformer.transformer(bizOptJson.getString("expireTime"), transform)
                ));
                //原处理人，委托人
                roleRelegate.setGrantee(StringBaseOpt.castObjectToString(
                    JSONTransformer.transformer(bizOptJson.getString("grantee"), transform)
                ));
                //现处理人，受委托人
                roleRelegate.setGrantor(StringBaseOpt.castObjectToString(
                    JSONTransformer.transformer(bizOptJson.getString("grantor"), transform)
                ));
                roleRelegate.setOptId(StringBaseOpt.castObjectToString(
                    JSONTransformer.transformer(bizOptJson.getString("grantee"), transform)
                ));
                roleRelegate.setUnitCode(StringBaseOpt.castObjectToString(
                    JSONTransformer.transformer(bizOptJson.getString("unitCode"), transform)
                ));
                roleRelegate.setRoleCode(StringBaseOpt.castObjectToString(
                    JSONTransformer.transformer(bizOptJson.getString("roleCode"), transform)
                ));
                roleRelegate.setTopUnit(dataOptContext.getTopUnit());
                roleRelegate.setRecordDate(new Date());
                flowManager.saveRoleRelegate(roleRelegate);
                break;
            //获取委托人委托列表
            case "listGrantee":
                List<RoleRelegate> granteeRelegateList = flowManager.listRoleRelegateByUser(
                    StringBaseOpt.castObjectToString(
                        JSONTransformer.transformer(bizOptJson.getString("grantee"), transform)
                    )
                );
                bizModel.putDataSet(id, new DataSet(granteeRelegateList));
                break;
            //获取受委托人委托列表
            case "listGrantor":
                List<RoleRelegate> grantorRelegateList = flowManager.listRoleRelegateByGrantor(
                    StringBaseOpt.castObjectToString(
                        JSONTransformer.transformer(bizOptJson.getString("grantor"), transform)
                    )
                );
                bizModel.putDataSet(id, new DataSet(grantorRelegateList));
                break;
            case "deleteRelegate":
                flowManager.deleteRoleRelegateByUserCode(
                    StringBaseOpt.castObjectToString(
                        JSONTransformer.transformer(bizOptJson.getString("grantor"), transform)
                    ),
                    StringBaseOpt.castObjectToString(
                        JSONTransformer.transformer(bizOptJson.getString("grantee"), transform)
                    )
                );
                break;
            default:
                break;
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
