<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<table id="kgqkintable" class="list">
	<thead align="center">
		<tr>
			<th>${cityname }</th>
			<th>任务</th>
			<th>完成</th>
			<th>比率</th>
		</tr>
	</thead>
	<tbody align="right">
		<c:forEach var="obj" items="${datas }">
			<tr>
				<td align="center">${obj.type }<br />
				</td>
				<td>${obj.plan }</td>
				<td>${obj.reality }</td>
				<td>${obj.scale }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
