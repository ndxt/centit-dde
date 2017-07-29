<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_exchange_trigger_form" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input id="hid_exchange_trigger_form_triggerId" type="hidden" name="triggerId" value="${param.columnNo}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>执行语句：</label> 
				<textarea name="triggerSql" rows="6" cols="60" ></textarea>

			</div>
			
			<div class="unit">
				<label>执行语句说明：</label> <textarea name="triggerDesc" rows="2" cols="60"></textarea>

			</div>

			<div class="unit">
				<label>执行对象：</label> 
				
				<input name="triggerDatabase" type="radio"  value="S" />数据源
				<input name="triggerDatabase" type="radio"  value="D" />数据目标

			</div>

			<div class="unit">
				<label>触发类别：</label> 
				
				<d title="行触发器（迁移时对每一条数据执行一遍）"><input name="triggerType" type="radio"  value="L" />行触发器</d>
				<d title="表触发器（数据迁移前或者 完成时执行，一个对应关系只执行一次）"><input name="triggerType" type="radio"  value="T" />表触发器</d>

			</div>
			
			<div class="unit">
				<label>触发时机：</label> 
				<input name="triggerTime" type="radio"  value="B" />交换前
				<input name="triggerTime" type="radio"  value="A" />交换后
				<input name="triggerTime" type="radio"  value="E" />交换失败后

			</div>

			<!--<div class="unit">
				<label>触发器顺序：</label> 
				<input id="chk_isNull" name="isNull" type="checkbox" size="40"  value="1" />

			</div> -->
			
			<div class="unit">
				<label>无参数存储过程：</label> 
				<input name="isprocedure" type="checkbox" value="T" />

			</div>

            <div class="unit">
                <fieldset style="line-height: 20px; ">
                    <legend>触发器使用规则</legend>
                    1、行触发器在数据迁移时对每一条记录都执行一遍，执行顺序为：迁移前左、迁移前右、迁移后右、迁移后左（左代表数据源，右代表数据目标）。<br />
                    2、行触发器为一条DML语句（insert、update、delete）或者一个存储过程（如果需要执行多条语句用就用存储过程），对数据的引用名为 ':'+数据源字段名,或者 TODAY当前时间 SQL_ERROR_MSG 数据库运行异常信息。
                    迁移触发器在数据迁移完成时执行，其行为同样是一条DML语句（insert、update、delete）或者一个存储过程（如果需要执行多条语句用就用存储过程）。<br />
                    3、对数据的引用名为 ':'+数据源字段名，或者是下列之一：<br />
                    <div style="border: 1px solid red; ">
                        TODAY 当前时间，SYNC_DATA_PIECES 迁移条数，<br />
                        SUCCEED_PIECES 成功条数,FAULT_PIECES 失败条数，<br/>
                        SYNC_BEGIN_TIME 迁移开始时间 SYNC_END_TIME 迁移结束时间<br />

                    </div>
                </fieldset>
            </div>
            <div class="unit" >

                <fieldset style="height: 106px; overflow: auto;">
                    <legend>可引用的数据源字段名</legend>
                    <div id="exchange_trigger_refer" style="width: 600px; float: left; height: 50px; word-break: break-all; ">

                    </div>
                </fieldset>
            </div>

		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_exchange_trigger" type="button">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button id="btn_exchange_trigger_close" type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>

	</s:form>
</div>

<script src="${pageContext.request.contextPath }/scripts/module/dde/exchange/exchange.js"></script>
<script src="${pageContext.request.contextPath }/scripts/module/dde/public.js"></script>
<script>
	$(function(){
		var exportsql = new ExportSql();
		exportsql.pubfuns.exportTriggerInit('${param.columnNo}');
		exportsql.bind('bindTrigger');
	});
</script>

