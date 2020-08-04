package com.centit.dde.datamoving.utils;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.datamoving.dataopt.DatabaseBizOperation;
import com.centit.dde.services.DBPacketBizSupplier;
import com.centit.product.dataopt.bizopt.BuiltInOperation;
import com.centit.product.dataopt.bizopt.DataLoadSupplier;
import com.centit.product.dataopt.bizopt.PersistenceOperation;
import com.centit.product.dataopt.core.BizOperation;
import com.centit.product.dataopt.core.BizOptFlow;
import com.centit.product.dataopt.core.BizSupplier;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhf
 */
public abstract class BizOptFlowUtil {

    public static int runDataExchange(
        DataLoadSupplier loadData, PersistenceOperation saveData)
    {
        BizOptFlow bof = new BizOptFlow().setSupplier(loadData).addOperation(saveData);
        return bof.run();
    }
    public static int runDataExchange(
        DBPacketBizSupplier loadData, DatabaseBizOperation saveData)
    {
        BizOptFlow bof = new BizOptFlow().setSupplier(loadData).addOperation(saveData);
        return bof.run();
    }

    public static int runDataExchange(
        DataLoadSupplier loadData, BizOperation dataTrans, PersistenceOperation saveData)
    {
        BizOptFlow bof = new BizOptFlow().setSupplier(loadData)
            .addOperation(dataTrans)
            .addOperation(saveData);
        return bof.run();
    }

    public static BizOptFlow createOptFlow(BizSupplier bizSupplier, String optDescJson){
        if(StringUtils.isBlank(optDescJson)){
            return new BizOptFlow().setSupplier(bizSupplier);
        }
        return createOptFlow(bizSupplier, JSONObject.parseObject(optDescJson));
    }

    public static BizOptFlow createOptFlow(BizSupplier bizSupplier, JSONObject optJson){
        return new BizOptFlow().setSupplier(bizSupplier)
            .addOperation(new BuiltInOperation(optJson));
    }
}
