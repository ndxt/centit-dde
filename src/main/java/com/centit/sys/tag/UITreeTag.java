package com.centit.sys.tag;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.cxf.common.util.StringUtils;


/**
 * 展示树状结构的弹出窗口
 *
 * @author zk
 * @create 2012-7-13
 */
public class UITreeTag extends TagSupport {
    private String id;

    /**
     * navtab dialog
     */
    private String type;

    private String items;

    private String idKey;

    private String parentKey;

    private String valueKey;

    private String inputValue;

    private String showValue;

    private String name;

    private String basePath;

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getShowValue() {
        return showValue;
    }

    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        if (!StringUtils.isEmpty(type)) {
            return type;
        }

        return "navtab";
    }

    public void setType(String type) {
        this.type = type;
    }

    @SuppressWarnings("static-access")
    @Override
    public int doStartTag() throws JspException {
        if (null == items) {
            return super.doStartTag();
        }
        JspWriter out = this.pageContext.getOut();

        String varId = "tree_" + id;

        try {
            // 两个INPUT分别用来存储提交后台的值和显示值
            out.println("<input type='hidden' name='" + name + "' value='" + inputValue + "' />");

            out.println("<input type='text' id='input_" + id + "' readonly='readonly' hide='no' onclick='$.ui.showUnitSearch(this)' value='" + showValue + "' style='cursor:hand' title='单击查看区域' />");

            // 存放树的容器
            out.println("<div id='" + id + "' class='treebox'></div>");

            // 执行脚本
            out.println("<script type='text/javascript'>");

            if ("navtab".equals(getType())) {
                out.println("var input = $('#input_" + id + "', navTab.getCurrentPanel());");
                out.println("var box = $('#" + id + "', navTab.getCurrentPanel());");
            } else {
                out.println("var input = $('#input_" + id + "', $.pdialog.getCurrent());");
                out.println("var box = $('#" + id + "', $.pdialog.getCurrent());");
            }

            out.println("var " + varId + " = new MzTreeView('" + varId + "', box, input);");
            out.println(varId + ".setIconPath('" + basePath + "/scripts/Mztreeview1.0/TreeView/');");

            items = items.replaceAll("\"", "\\\"");
            JSONArray array = new JSONArray().fromObject(items);
            JSONObject obj = null;
            String idkey = null;
            String parent = null;
            String value = null;


            Set<String> unitcodes = new HashSet<String>();

            for (int i = 0; i < array.size(); i++) {
                obj = array.getJSONObject(i);
                unitcodes.add(obj.getString(idKey));
            }

            for (int i = 0; i < array.size(); i++) {
                obj = array.getJSONObject(i);
                idkey = obj.getString(idKey);
                parent = obj.getString(parentKey);
                value = obj.getString(valueKey);

                if (null == parent || parent.equals("") || !unitcodes.contains(parent)) {
                    parent = "-1";
                }

                out.println(varId + ".nodes['" + parent + "_" + idkey + "']='text:" + value + ";method:$.ui.choseUnit(" + varId + ")';");
            }

//            out.println("debugger;");
            out.println("box[0].innerHTML = " + varId + ".toString();");
            out.println("input.data('tree', " + varId + ");");
            out.println("input.data('box', box);");

            out.println("</script>");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.doStartTag();
    }
}
