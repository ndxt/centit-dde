<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="accountInfo">
	<p>
		<span>数据服务接口说明 </span>
	</p>
	<p>
		<span>数据服务接口包括两种方式：1、web servcies 2、RESTful接口。 </span>
	</p>
</div>

<div class="pageCentent">

	<div class="panel collapse" defH="130">
		<h1>
			一、web servcies 接口<small> 通过web servcies获取数据字段信息，返回xml格式数据信息 </small>
		</h1>
		<div>
			<table width="100%" cellspacing="1" cellpadding="1" border="1"
				class="list">
				<tbody>
					<tr>
						<td><span class="porttypename">SOAP方法</span>
							<ul>
								<li>datacatalog</li> 参数：datacatacode 数据字典类别代码
								<br /> datacataname数据字典类别名称
								<br /> 任意录入一个即可
								<li>datacatalogchange</li> 参数：datacatacode 数据字典类别代码
								<br />lastModifyDate最后一次修改时间
								<br />
							</ul></td>
						<td><!-- <span class="field">Endpoint address:</span> --> <!-- <span
							class="value">http://../cxf/SOAP</span><br> --> <span
							class="field">WSDL :</span> <a target="_blank" rel="soapwsdl"
							title="soap?wsdl"
							href="${pageContext.request.contextPath}/cxf/SOAP?wsdl">{http://cxf.dde.centit.com/}SOAPImplService</a><br>
							<!-- <span class="field">Target namespace:</span> <span class="value">http://cxf.dde.centit.com/</span></td> -->
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="panel collapse" defH="200">
		<h1>
			二、RESTful接口<small> 通过RESTful获取数据字段信息，返回json格式数据信息</small>
		</h1>
		<div>
			1、返回所有数据字典类别的 json: <a
				href="${pageContext.request.contextPath}/data/datadictionary/list"
				target="dialog" rel="soapwsdl" title="所有数据字典类别的 json">http://../data/datadictionary/list</a>
			<br />
			<br /> 2、 返回一定时间之后更改的所有数据字典类别的
			json：http://../data/datadictionary/list/年月日。 <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如：
			<a
				href="${pageContext.request.contextPath}/data/datadictionary/list/20121212"
				target="dialog" rel="soapwsdl" title="2012-12-12之后更改的所有数据字典类别的 json">http://../data/datadictionary/list/20121212</a>
			返回2012-12-12之后更改的所有数据字典类别的 json <br />
			<br /> 3、返回特定数据类别的所有数据字典类别的 json：http://../data/datadictionary/数据类别。
			<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如： <a
				href="${pageContext.request.contextPath}/data/datadictionary/UnitType"
				target="dialog" rel="soapwsdl" title="所有机构类别数据字典的所有明细 json">http://../data/datadictionary/UnitType</a>
			返回所有机构类别数据字典的所有明细 json<br /> <br /> 4、 返回特定数据类别的中对应数据代码的
			字典明细：http://../data/datadictionary/数据类别/数据代码 。 <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如：
			<a
				href="${pageContext.request.contextPath}/data/datadictionary/UnitType/O"
				target="dialog" rel="soapwsdl" title="机构类别数据字典中对应'O'这个代码的 字典明细">
				>http://../data/datadictionary/UnitType/O</a> 返回机构类别数据字典中对应'O'这个代码的 字典明细<br />
			<br /> 5、返回特定数据类别的中对应数据代码的 字典明细的数值或者描述等属性。 <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如：
			<a
				href="${pageContext.request.contextPath}/data/datadictionary/UnitType/O/datavalue"
				target="dialog" rel="soapwsdl" title="机构类别数据字典中对应'O'这个代码的数值">http://../data/datadictionary/UnitType/O/datavalue</a>
			返回机构类别数据字典中对应'O'这个代码的数值<br />
		</div>
	</div>
</div>
 