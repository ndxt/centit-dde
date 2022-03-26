package com.centit.dde.utils;

/**
 * @author zhf
 */
public class ConstantValue {
    //缓存单位  1:分 2:时 3:日  -1:不缓存
    public static final int MINUS_ONE=-1;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    public static final String DATA_COPY ="1";

    static final String MODEL_TAG = "modelTag";
    static final String MODEL_NAME = "modelName";
    static final String BACKSLASH = "/";
    static final String SPOT = ".";
    static final String DOUBLE_SPOT = "..";
    public static final String ELSE = "else";
    public static final String FALSE = "F";
    public static final String TRUE = "T";
    public static final String CONFIG = "config";
    public static final String FILE_CONTENT = "fileContent";
    public static final String FILE_NAME = "fileName";

    public static final String CURRENT_USER = "currentUser";
    public static final String DATASET_SIZE = "dataSetSize";
    public static final String RESPONSE_DATA_CODE="responseDataCode";
    public static final String RUN_TYPE_NORMAL = "N";
    public static final String RUN_TYPE_COPY = "D";
    public static final String CYCLE_TYPE_RANGE = "range";
    public static final String CYCLE = "cycle";
    public static final String CYCLE_JUMP_OUT = "jumpCycle";

    public static final String CYCLE_JUMP_BREAK = "break";
    public static final String CYCLE_FINISH = "finishCycle";
    public static final String RESULTS = "results";
    public static final String SCHEDULER = "schedule";
    public static final String BRANCH = "branch";
    //生成json文件组件
    public static final String GENERATE_JSON = "generateJson";
    //生成CSV文件组件
    public static final String GENERATE_CSV= "generateCsv";
    //生成EXCEL文件组件
    public static final String GENERAT_EXCEL= "generateExcel";
    //文件上传组件
    public static final String FILE_UPLOAD = "fileUpload";
    //文件下载组件
    public static final String FILE_DOWNLOAD = "fileDownload";
    //定义JSON数据组件
    public static final String DEFINE_JSON_DATA = "defineJsonData";
    //元数据操作组件
    public static final String METADATA_OPERATION ="metadata";
    //赋值组件
    public static final String ASSIGNMENT ="assignment";
    //创建工作流组件
    public static final String CREATE_WORKFLOW = "createWorkflow";
    //提交工作流组件
    public static final String SUBMIT_WORKFLOW ="submitWorkflow";
    //删除工作流组件
    public static final String DELETE_WORKFLOW ="deleteWorkflow";
    //
    public static final String COMPARE_SOURCE="compareSource";
    //获取session数据
    public static final String SESSION_DATA="sessionData";

    public static final String ELASTICSEARCH_QUERY = "esQuery";
    public static final String ELASTICSEARCH_WRITE = "esWrite";

    //dde手动提交事物
    public static final String  COMMIT_TRANSACTION="commitTransaction";

    public static final String FINAL_TWO="2";

    public static final String TASK_TYPE_GET="1";
    public static final String TASK_TYPE_POST="3";
    public static final String TASK_TYPE_PUT="5";
    public static final String TASK_TYPE_DELETE="6";

    public static final String DATASET_JOIN_TYPE_INNER = "innerjoin";
    public static final String DATASET_JOIN_TYPE_LEFT = "leftjoin";
    public static final String DATASET_JOIN_TYPE_RIGHT = "rightjoin";
    public static final String DATASET_JOIN_TYPE_ALL = "alljoin";


}
