package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.domain.Task;
import com.centit.dde.entity.DynamicCodeParam;
import com.centit.dde.java.JarService;
import com.centit.dde.java.JavaRunner;
import com.centit.dde.util.StaticValue;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicCodeBizOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        DynamicCodeParam dynamicCodeParam = JSON.toJavaObject(bizOptJson, DynamicCodeParam.class);
        Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "key", "value");
        JSONObject jsonObject = new JSONObject();
        if (mapString != null) {
            for (Map.Entry<String, String> map : mapString.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    jsonObject.put(map.getKey(),  VariableFormula.calculate(map.getValue(),new BizModelJSONTransform(bizModel)));
                }
            }
            dynamicCodeParam.setMethodParams(jsonObject);
        }
        Object result = assembleCode(dynamicCodeParam);
        bizModel.putDataSet(dynamicCodeParam.getId(),new SimpleDataSet(result));
        return ResponseSingleData.makeResponseData(bizModel.getDataSet(dynamicCodeParam.getId()).getSize());
    }

    /**
     * 组装代码
     */
    private Object assembleCode(DynamicCodeParam dynamicCodeParam) throws Exception {
        String javaCode;
        //加载第三方依赖包
        if (StringUtils.isNotBlank(dynamicCodeParam.getMavenDeps())){
            JarService.savePom(StaticValue.mavenDeps(dynamicCodeParam.getMavenDeps()));
        }
        if (StringUtils.isNotBlank(dynamicCodeParam.getImportClass())){
            StringBuilder builderCode = new StringBuilder();
            builderCode.append("package com.centit.dde;\r\n");
            builderCode.append("\r\n");
            if (StringUtils.isNotBlank(dynamicCodeParam.getImportClass())){
                builderCode.append(dynamicCodeParam.getImportClass()+"\r\n");
                builderCode.append("\r\n");
            }
            builderCode.append("\r\n");
            builderCode.append("public class DynamicCodeTempClzz {\r\n");
            builderCode.append("\r\n");
            builderCode.append(dynamicCodeParam.getJavaCode());
            builderCode.append("}");
            javaCode=builderCode.toString();
        }else {
            javaCode=dynamicCodeParam.getJavaCode();

        }
        Task task = new Task();
        task.setId(dynamicCodeParam.getId());
        task.setName(dynamicCodeParam.getId());
        task.setStatus(1);
        task.setCode(javaCode);
        return run(task,dynamicCodeParam.getMethodParams(),dynamicCodeParam.getMethodName());
    }

    /**
     * 编译并执行代码
     * @param task
     * @throws Exception
     * @return
     */
    private Object run(Task task,JSONObject params,String methodName) throws Exception {
        String home = StaticValue.HOME;
        StaticValue.makeFiles(home);
        // 初始化Jar环境
        JarService.init();
        JavaRunner javaRunner = new JavaRunner(task);
        if(javaRunner.check()) {
            return javaRunner.compile().instance().execute(methodName, params);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        DynamicCodeParam dynamicCodeParam = new DynamicCodeParam();
        dynamicCodeParam.setJavaCode(" public Object test(String name , String address, Integer age, List<String> list){\n" +
            "        for (String str : list) {\n" +
            "            System.out.println(str+\"_\"+name+\"_\"+address+\"_\"+age);\n" +
            "        }\n" +
            "        return name+\"_\"+address+\"_\"+age;\n" +
            "    }"+"\r\n"+" public Object test1(String name , String address, Integer age, List<String> list){\n" +
            "            System.out.println(\"_\"+name+\"_\"+address+\"_\"+age);\n" +
            "        return name+\"_\"+address+\"_\"+age;\n" +
            "    }");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("age",12);
        jsonObject.put("name","测试");
        jsonObject.put("address","南京");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        jsonObject.put("list",list);
        dynamicCodeParam.setImportClass("import java.util.ArrayList;\n" +
            "import java.util.List;");
        dynamicCodeParam.setMethodParams(jsonObject);
        //dynamicCodeParam.setMethodName("test1");
        System.out.println(new DynamicCodeBizOperation().assembleCode(dynamicCodeParam));
    }
}
