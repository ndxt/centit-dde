<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script type="text/javascript">

     function dojson (data)
     {
    	// alert(data.name);
    	 $("#fileform").append('<p>'+data.name+'<p>');
    	
     };
  
     $("#loading")
     .ajaxStart(function(){
         $(this).show();
     })//开始上传文件时显示一个图片
     .ajaxComplete(function(){
         $(this).hide();
     });
    
</script>

<div>
<p>
<label>文件一：</label>
<input type="file" name="upload" id="upload">
<img id="loading"  src="<c:url value='/themes/css/images/frame/loading.gif' ></c:url>">
<input type="button" action="<c:url value='/app/fileinfo!uploadfile.do' ></c:url>"
 onclick="uploadFile(this,'upload',dojson);" value="上传">
 </p>

<div id="fileform">
	<ul class="uploadList">
		<li>20120315122321.jpg<span>加载</span></li>
		<li>20120315122321.jpg<span>加载</span></li>
	</ul>

</div>		

</div>
