<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<script type="text/javascript">
	function change2(uid) {
		var u = document.getElementById(uid);
		var sq_hi = document.getElementById(uid + "_hi").value;
		if (sq_hi == '' || sq_hi == '0') {
			document.getElementById(uid + "_hi").value = document
					.getElementById(uid).innerHTML;
			document.getElementById(uid).innerHTML = "<img alt='qwe' src='images/bzzy.png'   height='350px'  width='350px' >";
		} else {
			document.getElementById(uid).innerHTML = document
					.getElementById(uid + "_hi").value;
			document.getElementById(uid + "_hi").value = '0';
		}
	}
	function change3(uid) {
		var u = document.getElementById(uid);
		var sq_hi = document.getElementById(uid + "_hi").value;
		if (sq_hi == '' || sq_hi == '0') {
			document.getElementById(uid + "_hi").value = document
					.getElementById(uid).innerHTML;
			document.getElementById(uid).innerHTML = "<img alt='qwe' src='images/bzdx.png'   height='350px'  width='350px'>";
		} else {
			document.getElementById(uid).innerHTML = document
					.getElementById(uid + "_hi").value;
			document.getElementById(uid + "_hi").value = '0';
		}
	}
	function change4(uid) {
		var u = document.getElementById(uid);
		var sq_hi = document.getElementById(uid + "_hi").value;
		if (sq_hi == '' || sq_hi == '0') {
			document.getElementById(uid + "_hi").value = document
					.getElementById(uid).innerHTML;
			document.getElementById(uid).innerHTML = "<img alt='qwe' src='images/bzfjs.png'   height='350px'  width='350px'>";
		} else {
			document.getElementById(uid).innerHTML = document
					.getElementById(uid + "_hi").value;
			document.getElementById(uid + "_hi").value = '0';
		}
	}
</script>
<div>
	<div class="dashLeft">
		<div class="pagra p1">
			<div id="sq1" class="stylewords">
				省政府任务要求新开工保障房31.5万套，其中公租房10.5万套，经济适用房5.3万套，限价房6.5万套，改造9.2万套。租赁补贴4.5万户，总计已完成30万套，占任务的95%。
					竣工指标全年完成13万套，已完成12万套，占任务的92%。<img class="bzroom"
					src="${pageContext.request.contextPath}/themes/css/images/frame/bzroom.jpg">
			</div>
			<a onClick="change('sq1')" href="#" class="stylebutton"><img
				src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
		</div>
		<div class="pagra p2">
			<div id="sq2" class="stylewords2">
				<table class="tableView" border="0" cellspacing="0" cellpadding="0">
											<tr>
							<th></th>
							<th>需要（套）</th>
							<th>已完成（套）</th>
						</tr>
						<tr>
							<td align="center">全省</td>
							<td align="center">47</td>
							<td align="center">32</td>
						</tr>
						<tr>
							<td align="center">南京</td>
							<td align="center">20</td>
							<td align="center">19</td>
						</tr>
						<tr>
							<td align="center">无锡</td>
							<td align="center">5</td>
							<td align="center">2</td>
						</tr>
						<tr>
							<td align="center">苏州</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">南通</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">泰州</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">徐州</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">淮安</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">宿迁</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">连云港</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">扬州</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">常州</td>
							<td align="center">2</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">镇江</td>
							<td align="center">1</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">盐城</td>
							<td align="center">3</td>
							<td align="center">1</td>
						</tr>
				</table>
			</div>
			<a onClick="change2('sq2')" href="#" class="stylebutton"><img
				src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
		</div>
	</div>
	<div class="dashLeft">
		<div class="pagra p3">
			<div id="sq3" class="stylewords2">
				<table class="tableView" border="0" cellspacing="0" cellpadding="0">
					<tr>
							<th></th>
							<th>需要保障（人）</th>
							<th>已获得（人）</th>
						</tr>
						<tr>
							<td align="center">全省</td>
							<td align="center">105</td>
							<td align="center">85</td>
						</tr>
						<tr>
							<td align="center">南京</td>
							<td align="center">29</td>
							<td align="center">23</td>
						</tr>
						<tr>
							<td align="center">无锡</td>
							<td align="center">6</td>
							<td align="center">6</td>
						</tr>
						<tr>
							<td align="center">苏州</td>
							<td align="center">7</td>
							<td align="center">4</td>
						</tr>
						<tr>
							<td align="center">南通</td>
							<td align="center">6</td>
							<td align="center">6</td>
						</tr>
						<tr>
							<td align="center">泰州</td>
							<td align="center">7</td>
							<td align="center">7</td>
						</tr>
						<tr>
							<td align="center">徐州</td>
							<td align="center">1</td>
							<td align="center">1</td>
						</tr>
						<tr>
							<td align="center">淮安</td>
							<td align="center">6</td>
							<td align="center">5</td>
						</tr>
						<tr>
							<td align="center">宿迁</td>
							<td align="center">6</td>
							<td align="center">6</td>
						</tr>
						<tr>
							<td align="center">连云港</td>
							<td align="center">7</td>
							<td align="center">6</td>
						</tr>
						<tr>
							<td align="center">扬州</td>
							<td align="center">9</td>
							<td align="center">7</td>
						</tr>
						<tr>
							<td align="center">常州</td>
							<td align="center">8</td>
							<td align="center">7</td>
						</tr>
						<tr>
							<td align="center">镇江</td>
							<td align="center">5</td>
							<td align="center">5</td>
						</tr>
						<tr>
							<td align="center">盐城</td>
							<td align="center">8</td>
							<td align="center">2</td>
						</tr>
				</table>
			</div>
			<a onClick="change3('sq3')" href="#" class="stylebutton"><img
				src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
		</div>
		<div class="pagra p4">
			<div id="sq4" class="stylewords2">
				<table class="tableView" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<th></th>
						<th>申请保障（人）</th>
						<th>已获得（人）</th>
					</tr>
					<tr>
						<td align="center">全省</td>
						<td align="center">94</td>
						<td align="center">85</td>
					</tr>
					<tr>
						<td align="center">南京</td>
						<td align="center">26</td>
						<td align="center">23</td>
					</tr>
					<tr>
						<td align="center">无锡</td>
						<td align="center">6</td>
						<td align="center">6</td>
					</tr>
					<tr>
						<td align="center">苏州</td>
						<td align="center">6</td>
						<td align="center">4</td>
					</tr>
					<tr>
						<td align="center">南通</td>
						<td align="center">6</td>
						<td align="center">6</td>
					</tr>
					<tr>
						<td align="center">泰州</td>
						<td align="center">7</td>
						<td align="center">7</td>
					</tr>
					<tr>
						<td align="center">徐州</td>
						<td align="center">1</td>
						<td align="center">1</td>
					</tr>
					<tr>
						<td align="center">淮安</td>
						<td align="center">6</td>
						<td align="center">5</td>
					</tr>
					<tr>
						<td align="center">宿迁</td>
						<td align="center">6</td>
						<td align="center">6</td>
					</tr>
					<tr>
						<td align="center">连云港</td>
						<td align="center">6</td>
						<td align="center">6</td>
					</tr>
					<tr>
						<td align="center">扬州</td>
						<td align="center">7</td>
						<td align="center">7</td>
					</tr>
					<tr>
						<td align="center">常州</td>
						<td align="center">8</td>
						<td align="center">7</td>
					</tr>
					<tr>
						<td align="center">镇江</td>
						<td align="center">6</td>
						<td align="center">5</td>
					</tr>
					<tr>
						<td align="center">盐城</td>
						<td align="center">3</td>
						<td align="center">2</td>
					</tr>
				</table>
			</div>
			<a onClick="change4('sq4')" href="#" class="stylebutton"><img
				src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
		</div>
	</div>
</div>