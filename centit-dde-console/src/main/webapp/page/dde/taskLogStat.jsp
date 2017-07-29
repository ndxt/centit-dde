<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/dde/taskLog!listStat.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li style="width: auto;">
				<label>年份：</label>
				<input type="text" name="s_year" value="${s_year}" />
				</li>
				<li>
							<label>按月查询：</label>
							<input type="checkbox" name="s_isMonth" value="0" <c:if test="${'0' eq param['s_isMonth'] }">checked="checked" </c:if> /> 
							<select id="month" name="s_month" class="combox">
								<option value="1" <c:if test="${s_month=='1' }">selected="selected"</c:if> >1月</option>
								<option value="2" <c:if test="${s_month=='2' }">selected="selected"</c:if>>2月</option>
								<option value="3" <c:if test="${s_month=='3' }">selected="selected"</c:if>>3月</option>
								<option value="4" <c:if test="${s_month=='4' }">selected="selected"</c:if>>4月</option>
								<option value="5" <c:if test="${s_month=='5' }">selected="selected"</c:if>>5月</option>
								<option value="6" <c:if test="${s_month=='6' }">selected="selected"</c:if>>6月</option>
								<option value="7" <c:if test="${s_month=='7' }">selected="selected"</c:if>>7月</option>
								<option value="8" <c:if test="${s_month=='8' }">selected="selected"</c:if>>8月</option>
								<option value="9" <c:if test="${s_month=='9' }">selected="selected"</c:if>>9月</option>
								<option value="10" <c:if test="${s_month=='10' }">selected="selected"</c:if>>10月</option>
								<option value="11" <c:if test="${s_month=='11' }">selected="selected"</c:if>>11月</option>
								<option value="12" <c:if test="${s_month=='12' }">selected="selected"</c:if>>12月</option>
							</select>
						</li>
				
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div></li>
					
				</ul>
			</div>
			
		</div>
	</form>
</div>

<div class="pageContent">
	<table class="list" width="100%" layoutH=".pageHeader 1">
		
		
			<thead align="center">
				
					<tr>
					<th>
						日期
					</th>
					<th>
						任务数
					</th>
					<th>
						对应关系数
					</th>
					<th>
						执行数
					</th>
					<th>
						成功条数
					</th>
					<th>
						失败条数
						</th>
					</tr>
					
			</thead>
		
		
		
			<tbody align="center">
				<c:forEach var="line" items="${taskLogStat}">
					<tr>
						<td align="center" title="${line[0]}" >
						<c:if test="${fn:length(line[0])!=7}">
						<a href="${contextPath }/dde/taskLog!listall.do?s_runBeginTime=${line[0]}&s_runBeginTime2=${line[0]}" 
						rel="ErrorData" target='navTab' width="850" height="500" title="任务明细日志"> 
						</c:if>
						<c:if test="${fn:length(line[0])==7}">
						<a href="${contextPath }/dde/taskLog!listall.do?s_runBeginTime=${line[0]}-01&s_runBeginTime2=${line[0]}-31" 
						rel="ErrorData" target='navTab' width="850" height="500" title="任务明细日志"> 
						</c:if>
						${line[0]}</a>
						</td>
						<td align="center" title="${line[0]}">${line[1]}</td>
						<td align="center" title="${line[0]}">${line[2]}</td>
						<td align="center" title="${line[0]}">${line[3]}</td>
						<td align="center" title="${line[0]}">${line[4]}</td>
						<td align="center" title="${line[0]}">${line[5]}</td>
					</tr>
				</c:forEach>
			</tbody>
	</table>
</div>
	
<script>
  	$(function() {
		$('a', navTab.getCurrentPanel()).each(function() {
			$this = $(this);
			
			var href = $this.attr('href');
			href=encodeURI(encodeURI(href));
			$this.attr('href', href);
		});
	}); 
</script>