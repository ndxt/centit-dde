<link href="${pageContext.request.contextPath}/themes/css/core.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/themes/css/core-custom.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/themes/default/style-custom.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/scripts/centitui/jquery-1.7.1.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/highcharts.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/sys/ui/centit.chart.js" type="text/javascript"></script> 

<script type="text/javascript">
function showSy(){
	window.open('qlyxSy!list.do?djid=${djnodeid}&nodeid=${nodeInstId}&flowintanceid=${flowInstId}');
}
function check(){
	  if(event.keyCode==13&&(event.srcElement.type=="text"||event.srcElement.type=="select-one"))
	  {
	     event.keyCode=9;
	  }
	 }
	 document.onkeydown=check;
</script>