package com.centit.dde.core;

class StepVertex {
    BizModel bizModel;
    //标记该点是否被查看-广度优先专用
    boolean visited = false;
    //标记该点是否被查看-深度优先专用
    boolean visited2 = false;

    public StepVertex(BizModel bizModel) {
        this.bizModel = bizModel;
    }

    @Override
    public String toString() {
        return "[" + bizModel.getModelName() + "]";
    }
}
