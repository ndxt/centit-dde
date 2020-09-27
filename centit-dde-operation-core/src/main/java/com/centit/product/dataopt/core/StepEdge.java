package com.centit.product.dataopt.core;

//边类 有向图
class StepEdge {
    StepVertex start;
    StepVertex end;

    public StepEdge(StepVertex start, StepVertex end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "(" + start.bizModel.getModelName() + "," + end.bizModel.getModelName() + ")";
    }
}
