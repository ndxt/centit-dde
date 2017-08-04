package com.centit.dde.po;

/**
 * 一次交换对应多条数据对应交换关系
 * 一次数据对应关系中交换返回结果集
 */
public class TransferResult {
    /**
     * 返回结果
     */
    private int res;

    /**
     * 成功条数
     */
    private long succ;

    /**
     * 失败条数
     */
    private long error;

    /**
     * 源数据有无数据
     */
    private boolean sourceHasData;


    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public long getSucc() {
        return succ;
    }

    public void setSucc(long succ) {
        this.succ = succ;
    }

    public long getError() {
        return error;
    }

    public void setError(long error) {
        this.error = error;
    }

    public boolean isSourceHasData() {
        return sourceHasData;
    }

    public void setSourceHasData(boolean sourceHasData) {
        this.sourceHasData = sourceHasData;
    }
}
