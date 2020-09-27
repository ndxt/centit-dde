package com.centit.product.dataopt.core;

import com.centit.product.dataopt.bizopt.SimpleBizSupplier;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务流
 */
public class BizOptFlow {

    public BizOptFlow(){

    }
    /**
     * 业务数据 生产者
     */
    private BizSupplier supplier;
    /**
     * 业务数据的 处理流程
     */
    private List<BizOperation> operations;

    private BizModel lastResult;

    /**
     * @return 返回真正运行的次数, 如果小于 0 表示報錯
     */
    public int run(){
        int n = 0;
        do{
            BizModel tempBM = supplier.get();
            if(tempBM == null){
                break;
            }
            lastResult = tempBM;
            n ++;
            if(this.operations != null) {
                for (BizOperation opt : operations) {
                    lastResult = opt.apply(lastResult);
                }
            }
        }while(supplier.isBatchWise());
        return n;
    }

    public BizSupplier getSupplier() {
        return supplier;
    }

    public BizOptFlow setSupplier(BizSupplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public List<BizOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<BizOperation> operations) {
        this.operations = operations;
    }

    public BizOptFlow addOperation(BizOperation operation) {
        if(this.operations == null){
            this.operations = new ArrayList<>(5);
        }
        this.operations.add(operation);
        return this;
    }

    public static BizOptFlow makeSingleOptFlow(BizOperation operation){
        BizOptFlow optFlow = new BizOptFlow();
        return optFlow.setSupplier(SimpleBizSupplier.DUMMY_BIZ_SUPPLIER)
                      .addOperation(operation);
    }

    public BizModel getLastResult() {
        return lastResult;
    }
}
