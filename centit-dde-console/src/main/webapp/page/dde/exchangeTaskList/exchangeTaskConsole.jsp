<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<style type="text/css">
    div.console {
        word-wrap:break-word;
        margin-bottom: 3px;
    }
    div.left {
        float: left;
        border: solid 1px cadetblue;
    }
    div.break{
        word-break: break-all;
        line-height: 20px;
    }
    span.word {
        margin: 0 2px 0 2px;
    }
</style>

<div class="pageContent">

        <div class="left" style="width: 72%">
            <h5>正在进行数据处理......</h5>

            <div id="div_task_console" ></div>

            <div id="spinner" start="0"  style="display: none;"><img alt="" src="${pageContext.request.contextPath}/images/spinner.gif"></div>

            <div style="margin-bottom: 30px;"></div>
        </div>
        <div id="div_task_info" class="left" style="width: 290px; ">
            <table>
                <tr>
                    <td>任务编号</td>
                    <td><span id="task_id" class="word"></span></td>
                </tr>
                <tr>
                    <td>正在执行交换对应关系名称</td>
                    <td><span id="span_exchangeName" class="word"></span></td>
                </tr>
            </table>


            数据信息：
            <div style="position: fixed; top: 160px; right: 150px; border: 1px solid cadetblue; line-height: 20px;">
                已处理总<span id="span_all" class="word">0</span>行数据 <br />
                成功<span id="span_success" class="word" style="color: green;">0</span>行数据 <br />
                失败<span id="span_error" class="word" style="color: red;">0</span>行数据
            </div>



            <div style="position: fixed; top: 250px; width: 290px; right: 30px; border: 1px solid cadetblue; line-height: 20px;">
                <div>已执行完的交换对应关系:</div>
                <div id="div_already_process"></div>
            </div>
        </div>

</div>



