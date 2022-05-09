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
    //元数据操作查询组件
    public static final String METADATA_OPERATION ="metadata";
    //元数据操作查询组件
    public static final String METADATA_OPERATION_QUERY ="metadataQuery";
    //元数据操作查询组件
    public static final String METADATA_OPERATION_UPDATE ="metadataUpdate";
    //创建工作流组件
    public static final String CREATE_WORKFLOW = "createWorkflow";
    //提交工作流组件
    public static final String SUBMIT_WORKFLOW ="submitWorkflow";
    //删除工作流组件
    public static final String DELETE_WORKFLOW ="deleteWorkflow";
    //获取流程待办组件
    public static final String USER_TASK_WORKFLOW ="userTaskWorkflow";
    public static final String INST_NODES_WORKFLOW ="instNodesWorkFlow";

    //赋值组件
    public static final String ASSIGNMENT = "assignment";

    //
    public static final String COMPARE_SOURCE="compareSource";
    //获取session数据
    public static final String SESSION_DATA="sessionData";

    public static final String ELASTICSEARCH_QUERY = "esQuery";
    public static final String ELASTICSEARCH_WRITE = "esWrite";

    //dde手动提交事物
    public static final String  COMMIT_TRANSACTION="commitTransaction";
    //交集
    public static final String  INTERSECT_DATASET="intersect";
    //差集
    public static final String  MINUS_DATASET="minus";

    public static final String FINAL_TWO="2";

    //日志记录级别
    public static final int LOGLEVEL_TYPE_ERROR= 1;//ERROR
    public static final int LOGLEVEL_TYPE_INFO = 3;//INFO
    public static final int LOGLEVEL_TYPE_DEBUG= 7;//DEBUG
    // 1 1 1
    public static final int LOGLEVEL_CHECK_ERROR= 1;//ERROR
    public static final int LOGLEVEL_CHECK_INFO = 2; //INFO
    public static final int LOGLEVEL_CHECK_DEBUG= 4;//DEBUG

    //API任务类型
    public static final String TASK_TYPE_GET="1";
    public static final String TASK_TYPE_POST="3";
    public static final String TASK_TYPE_PUT="5";
    public static final String TASK_TYPE_DELETE="6";
    //消息触发
    public static final String TASK_TYPE_MSG="4";
    //定时任务
    public static final String TASK_TYPE_TIME="2";

    public static final String DATASET_JOIN_TYPE_INNER = "innerjoin";
    public static final String DATASET_JOIN_TYPE_LEFT = "leftjoin";
    public static final String DATASET_JOIN_TYPE_RIGHT = "rightjoin";
    public static final String DATASET_JOIN_TYPE_ALL = "alljoin";

    //可以2个都选，但是必须选择其中一个
    public static final String CHECK_RULE_RESULT_TAG = "__check_rule_result";
    public static final String CHECK_RULE_RESULT_MSG_TAG = "__check_rule_result_msg";

    public static final String REQUEST_BODY_TAG = "__request_body";
    public static final String REQUEST_FILE_TAG = "__request_file";
    public static final String REQUEST_PARAMS_TAG = "__request_params";
    public static final String SESSION_DATA_TAG = "__session_data";
    public static final String MESSAGE_QUEUE_TAG = "__queue_message";
    public static final String MODULE_CALL_TAG = "__module_params";
    public static final String LAST_ERROR_TAG = "__last_error";



}
