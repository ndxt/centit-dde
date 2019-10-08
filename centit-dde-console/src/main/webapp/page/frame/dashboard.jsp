<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="utf-8"%>

<head>
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

</head>
<body>
	<div>
		<div class="dashLeft">
			<div class="pagra p1">
				<div id="sq1" class="stylewords">
					<div class="map" style="float: left;">
						<h1 style="position: absolute; top: 88px; left: 106px;">
							<a href="#">徐州</a>
						</h1>
						<h1 style="position: absolute; top: 51px; left: 230px;">
							<a href="#">连云港</a>
						</h1>
						<h1 style="position: absolute; top: 128px; left: 180px;">
							<a href="#">宿迁</a>
						</h1>
						<h1 style="position: absolute; top: 154px; left: 236px;">
							<a href="#">淮安</a>
						</h1>
						<h1 style="position: absolute; top: 174px; left: 330px;">
							<a href="#">盐城</a>
						</h1>
						<h1 style="position: absolute; top: 270px; left: 277px;">
							<a href="#">扬州</a>
						</h1>
						<h1 style="position: absolute; top: 219px; left: 311px;">
							<a href="#">泰州</a>
						</h1>
						<h1 style="position: absolute; top: 310px; left: 400px;">
							<a href="#">南通</a>
						</h1>
						<h1 style="position: absolute; top: 331px; left: 200px;">
							<a
								style="BACKGROUND: url(${pageContext.request.contextPath}/images/maph1.gif) no-repeat;BACKGROUND-POSITION: left bottom;"
								href="${pageContext.request.contextPath}/zfbzjc/zfbzjcInfoBuilds!build.do?ctiycode=320100&cityName=南京市"
								target="navTab" external='true'>南京</a>
						</h1>
						<h1 style="position: absolute; top: 319px; left: 260px;">
							<a href="#">镇江</a>
						</h1>
						<h1 style="position: absolute; top: 366px; left: 290px;">
							<a href="#">常州</a>
						</h1>
						<h1 style="position: absolute; top: 368px; left: 359px;">
							<a href="#">无锡</a>
						</h1>
						<h1 style="position: absolute; top: 409px; left: 380px;">
							<a href="#">苏州</a>
						</h1>
					</div>


                     1-6月份，全省新开工各类保障性住房<fmt:formatNumber value="${zib.zib6_1/10000}" pattern="#.##" minFractionDigits="2" />
                                                      万套（户），完成年度目标任务<fmt:formatNumber value="${zib.zib6_1_1 }" type="percent" />；
                                                      竣工各类保障性住房<fmt:formatNumber value="${zib.zib6_2/10000}" pattern="#.##" minFractionDigits="2" />万套，
                                                      完成年度目标任务的<fmt:formatNumber value="${zib.zib6_2_1 }" type="percent" />。
                                                      发放廉租住房租赁补贴<fmt:formatNumber value="${zib.zib5_1/10000}" pattern="#.##" minFractionDigits="2" />万户，
                                                      完成年度目标任务<fmt:formatNumber value="${zib.zib5_1_1 }" type="percent" />。
					 <a onClick="change('sq1')" href="#"
						class="stylebutton"><img
						src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
				</div>


				<span class="bottom-left"> <span class="bottom-right"></span>
				</span>
			</div>
		</div>


		<div class="dashLeft">
			<div class="pagra p2">
				<div id="sq2" class="stylewords2">
					<table class="tableView" cellspacing="0" cellpadding="0">

						<tr>
							<th>地区名</th>
							<th>计划开工数（套）</th>
							<th>实际开工数（套）</th>
							<th>完成率</th>
						</tr>


						<c:forEach var="obj" items="${objects }">
							<tr>
								<td align="center" style="white-space: nowrap;">${obj.d_dataname}
								</td>
								<td align="center" style="white-space: nowrap;">
									<fmt:formatNumber value="${obj.zib6_1_0}" pattern="#"
										minFractionDigits="0" />
								</td>
								<td align="center" style="white-space: nowrap;">${obj.zib6_1
									}
								</td>
								<td align="center" style="white-space: nowrap;">
								<fmt:formatNumber value="${obj.zib6_1_1 }" type="percent" />
								</td>
							</tr>
						</c:forEach>

					</table>
					<a  href="${pageContext.request.contextPath}/zfbzjc/zfbzjcInfoBuilds!build.do" title="房屋建设"
						target="navTab" rel="external_FWJS" external="true" class="stylebutton">
						<img src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif">
					</a>
				</div>



				<span class="bottom-left"> <span class="bottom-right"></span>
				</span>
			</div>

			<div class="pagra p3">
				<div id="sq3" class="stylewords2">
					<table class="tableView" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th>地区名</th>
							<th>计划竣工数（套）</th>
							<th>实际竣工数（套）</th>
							<th>完成率</th>
						</tr>
						<c:forEach var="obj" items="${objects }">
							<tr>
								<td align="center" style="white-space: nowrap;">${obj.d_dataname}
								</td>
								<td align="center" style="white-space: nowrap;">
								<fmt:formatNumber value="${obj.zib6_2_0}" pattern="#"
										minFractionDigits="0" />
								</td>
								<td align="center" style="white-space: nowrap;">${obj.zib6_2}</td>
								<td align="center" style="white-space: nowrap;">
								<fmt:formatNumber value="${obj.zib6_2_1 }" type="percent" />
								</td>
							</tr>
						</c:forEach>

					</table>
					<a onClick="change3('sq3')" href="#" class="stylebutton"><img
						src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
				</div>

				<span class="bottom-left"> <span class="bottom-right"></span>
				</span>
			</div>
		</div>

		<div class="dashLeft">
			<div class="pagra p4">
				<div id="sq4" class="stylewords2">
					<table class="tableView" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th>地区名</th>
							<th>省下达任务数（套）</th>
							<th>已分配套数（套）</th>
							<th>分配率</th>
						</tr>
						<c:forEach var="appcant" items="${infoAps}">
							<tr>
								<td align="center" style="white-space: nowrap;">${appcant.dd_dataname}</td>
								<td align="center" style="white-space: nowrap;">
								<fmt:formatNumber value="${appcant.zia_number_0}" pattern="#"
										minFractionDigits="0" />
								</td>
								<td align="center" style="white-space: nowrap;">${appcant.zia_number}</td>
								<td align="center" style="white-space: nowrap;">
								<fmt:formatNumber value="${appcant.zia_number_1 }" type="percent" /></td>
							</tr>
						</c:forEach>
					</table>
					<a onClick="change4('sq4')" href="#" class="stylebutton"><img
						src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
				</div>

				<span class="bottom-left"> <span class="bottom-right"></span>
				</span>
			</div>

			<div class="pagra p5">
				<div id="sq5" class="stylewords2">
					<table class="tableView" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th>地区名</th>
							<th>预警数</th>
							<th>报警数</th>
						</tr>
						<tr>
							<td align="center">江苏省</td>
							<td align="center">30</td>
							<td align="center">7</td>

						</tr>
						<tr>
							<td align="center">南京市</td>
							<td align="center">4</td>
							<td align="center">2</td>

						</tr>
						<tr>
							<td align="center">无锡市</td>
							<td align="center">2</td>
							<td align="center">1</td>

						</tr>
						<tr>
							<td align="center">徐州市</td>
							<td align="center">4</td>
							<td align="center">2</td>

						</tr>
						<tr>
							<td align="center">常州市</td>
							<td align="center">0</td>
							<td align="center">0</td>

						</tr>
						<tr>
							<td align="center">苏州市</td>
							<td align="center">4</td>
							<td align="center">1</td>

						</tr>
						<tr>
							<td align="center">南通市</td>
							<td align="center">4</td>
							<td align="center">1</td>

						</tr>
						<tr>
							<td align="center">连云港市</td>
							<td align="center">0</td>
							<td align="center">0</td>

						</tr>
						<tr>
							<td align="center">淮安市</td>
							<td align="center">0</td>
							<td align="center">0</td>

						</tr>
						<tr>
							<td align="center">盐城市</td>
							<td align="center">0</td>
							<td align="center">0</td>

						</tr>
						<tr>
							<td align="center">扬州市</td>
							<td align="center">4</td>
							<td align="center">2</td>

						</tr>
						<tr>
							<td align="center">镇江市</td>
							<td align="center">4</td>
							<td align="center">0</td>

						</tr>
						<tr>
							<td align="center">泰州市</td>
							<td align="center">0</td>
							<td align="center">0</td>

						</tr>
						<tr>
							<td align="center">宿迁市</td>
							<td align="center">4</td>
							<td align="center">2</td>

						</tr>
					</table>
					<a onClick="change5('sq5')" href="#" class="stylebutton"><img
						src="${pageContext.request.contextPath}/themes/css/images/frame/Q1.gif"></a>
				</div>

				<span class="bottom-left"> <span class="bottom-right"></span>
				</span>
			</div>
		</div>
	</div>
</body>
