<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<style type="text/css">
/**
	文件容器
*/
.file-container input.newfolder {vertical-align: middle; border: 1px solid #D2D2D2; background: white; height: 18px; width:200px;}
.file-container .public-window {width:100%;}
.file-container .public-window a {text-decoration: none;}
.file-container .line,.file-container .header  {padding:0 0 0 10px; height:36px; font:12px/1.5 tahoma,arial,宋体b8b\4f53;}
.file-container .line.navigation {height:48px; line-height:48px; background: #F7F7F7 url(${pageContext.request.contextPath}/themes/default/images/publicinfo/outer_bn2.gif) left -53px repeat-x;} 

.file-container .line.path {height:28px; line-height:28px; border-top:1px solid #eee; padding:0 25px;} 
.file-container .line.path .info {height:28px; line-height:28px;}
.file-container .line.path .st {width: 130px;}
.file-container .line.path .ed {width:65%;}
.file-container .line.path .th {width:auto; max-width: 150px; text-align: center;}
.file-container .line.path a {color:#06c; cursor:pointer;}
.file-container .line.path a.text {color:#999; cursor:default; text-decoration: none;}
.file-container .line.path a.path {margin-left:5px; font-size: 13px;}
.file-container .line.path .status {float:right; height:28px; line-height:28px; display:inline-block; text-align: center;}

.file-container .line .info,.file-container .header .info { border-top:1px solid #eee;}
.file-container .line.last .info{border-bottom:1px solid #eee;}

.file-container .selected .info{background:#F2F7FF}
.file-container .hover .info,.file-container .line:hover .info{background:#F0F8FD}
.file-container .header .info.hover,.file-container .header .info:hover{background:#F0F8FD} 
.file-container .header .info {cursor:pointer;}

.file-container .line .info,.file-container .header .info  {float:left; line-height:36px;}
.file-container .line .operation,.file-container .header .operation {width:99%; background:#F2F7FF}
.file-container .line .name,.file-container .header .name {width:50%}
.file-container .line .size,.file-container .header .size {width:16%}
.file-container .line .owner,.file-container .header .owner{width:16%}
.file-container .line .time,.file-container .header .time {width:17%}

/**
	下拉菜单
*/
.file-container .pull-down-menu {width:64px; position:absolute; padding:0; line-heigth:24px; text-align:center; background:#fff; border:1px solid #cfcfcf; box-shadow:0 2px 3px #cfcfcf; cursor:default; list-style:none; z-index: 99;}
.file-container .pull-down-menu li {display: block; height: 24px!important;}
.file-container .pull-down-menu a {color:#666; display:block; height:24px; line-height:24px; padding:0 5px; text-decoration: none;}
.file-container .pull-down-menu a:hover {background:#F0F8FD;}
.file-container .pull-down-menu a.disable:hover {background:#fff;}
.file-container .pull-down-menu a.disable {color:#ddd;}
/**
	图标
*/
.file-container .icon {display:inline-block; position:relative; z-index:5; margin-right:10px;}

.file-container .cst1 {cursor:pointer; width:26px; height:22px; line-height:22px; background:url(${pageContext.request.contextPath}/themes/default/images/publicinfo/sprite_list_icon.gif) no-repeat -416px -80px;}
.file-container .icon.folder {background-position:0 0;}
.file-container .icon.img {background-position:0 -80px;}
.file-container .icon.visio {background-position:-32px -80px;}
.file-container .icon.pdf {background-position:-64px -80px;}
.file-container .icon.word {background-position:-96px -80px;}
.file-container .icon.excel {background-position:-128px -80px;}
.file-container .icon.txt {background-position:-160px -80px;}
.file-container .icon.music {background-position:-192px -80px;}
.file-container .icon.movie {background-position:-224px -80px;}
.file-container .icon.ppt {background-position:-256px -80px;}
.file-container .icon.apple {background-position:-288px -80px;}
.file-container .icon.exe {background-position:-320px -80px;}
.file-container .icon.zip {background-position:-352px -80px;}
.file-container .icon.apk {background-position:-384px -80px;}

.file-container .check {width:14px; height:14px; cursor:pointer; margin:0 10px 0 10px; background:url(${pageContext.request.contextPath}/themes/default/images/publicinfo/btn_icon.gif) no-repeat -113px 0;}
.file-container .selected .icon.check {background-position:-113px -19px;}

.file-container .sort {cursor:pointer; width:8px; height:4px; margin:0 0 0 5px; display:none;background:url(${pageContext.request.contextPath}/themes/default/images/publicinfo/btn_icon.gif) no-repeat}
.file-container .sort.asc {display:inline-block; background-position:-46px -86px;}
.file-container .sort.desc {display:inline-block; background-position:-46px -101px;}



/**
	按钮
*/
.file-container .btn{vertical-align: middle; position:relative; margin:0 0 0 10px; padding:0 0 0 10px; cursor:pointer; text-decoration:none; display:inline-block; background:url(${pageContext.request.contextPath}/themes/default/images/publicinfo/btn_sprit.gif) no-repeat;}
.file-container .btn .icon {margin:0 5px 0 0; background: url(${pageContext.request.contextPath}/themes/default/images/publicinfo/btn_icon.gif?@=-1) no-repeat;}
.file-container .btn .text {padding:0 10px 0 0; font-weight:100; display:inline-block; font:12px/1.5 tahoma,arial,宋体b8b\4f53; background:url(${pageContext.request.contextPath}/themes/default/images/publicinfo/btn_sprit.gif) no-repeat;}

.file-container .bst1 {height:30px; line-height:30px; background-position:0 -600px;}
.file-container .bst1 .text {height:30px; line-height:30px; background-position:right -600px; color:#fff;}
.file-container .bst1 .icon {width:20px; height:20px; top:5px;}
.file-container .btn.upload .icon {background-position:-183px -281px;} 

.file-container .bst2 {height:28px; line-height:28px; background-position:0 -144px;}
.file-container .bst2 .text {height:28px; line-height:28px; background-position:right -144px; color:#666;}
.file-container .bst2 .icon {width:18px; height:18px; top:4px;}
.file-container .btn.download .icon {background-position:-159px -39px;} 
.file-container .btn.download:hover .icon {background-position:-182px -39px;}
.file-container .btn.delete .icon {background-position:-159px -131px;} 
.file-container .btn.delete:hover .icon {background-position:-182px -131px;}
.file-container .btn.add .icon {background-position:-160px -20px;} 
.file-container .btn.add:hover .icon {background-position:-183px -20px;}

.file-container .bst3 .text {padding:0 25px 0 0;}
.file-container .btn.more .icon {background-position:-45px -113px; left:40px; position:absolute;} 

.file-container .bst4 {text-indent:-999px; margin:0 0 0 2px; padding:0; width:22px; height:22px; background: url(${pageContext.request.contextPath}/themes/default/images/publicinfo/btn_icon.gif?@=-1) no-repeat;}
.file-container .btn.sure {background-position:0 -512px;}
.file-container .btn.cancel {background-position: -27px -512px;}
 
</style>

<div class="file-container" id="file-container">
	<form id="uploadForm" target="uploadFrame" enctype="multipart/form-data" action="${pageContext.request.contextPath}/app/publicinfo!upload.do" method="post">
		<div class="line navigation">
			
<!-- 			<a class="btn upload bst1">
				<em class="icon"></em>
				<b class="text">上传文件</b>
			</a> -->
			
			<input type="file" id="publicinfo-upload" optID="UPLOADFI" dynamicFormData="uploadFormData"
				uploadCallback="uploadCallback" uploader="${pageContext.request.contextPath}/app/publicinfo!upload.do"/>
			
			<a class="btn add bst2" >
				<em class="icon"></em>
				<b class="text">新建文件夹</b>
			</a>
			
		</div>
	</form>
	
	
	<div class="line path">
		<div class="info st">
			<a class="uplevel" type="dir">返回上一级</a> | <a class="home" type="dir">部门主页</a>
		</div>
		<div class="info ed">
		
		</div>
		<div class="info th">
			<span class="status ready">已全部加载，共<i></i>个</span>
			<span class="status refresh" style="display:none;">正在加载中...</span>
		</div>
	</div>
	
	<div class="line operation" style="display:none;">
		<div class="info operation">
			<span class="check icon"></span>
			已选中<i></i>个文件/文件夹
			
			<a class="btn download bst2" href="javascript:;">
				<dfn class="icon"></dfn>
				<b class="text">下载</b>
			</a>
			
			<a class="btn delete bst2">
				<em class="icon"></em>
				<b class="text">删除</b>
			</a>
			
			<a class="btn more bst2 bst3">
				<em class="icon"></em>
				<b class="text">更多</b>
			</a>
		</div>
		
		<ul class="pull-down-menu header-menu" style="display:none;">
			<li>
				<a href="javascript:;" class="rename">重命名</a>
			</li>
			<li>
				<a href="javascript:;" class="copyto">复制到</a>
			</li>
			<li>
				<a href="javascript:;" class="moveto">移动到</a>
			</li>
		</ul>
	</div>
	
	<div class="header line">
		<div class="name info">
			<span class="check icon"></span>
			文件名
			<span class="sort icon"></span>
		</div>
		<div class="size info">大小<span class="sort icon"></span></div>
		<div class="owner info">上传者<span class="sort icon"></span></div>
		<div class="time info">上传时间<span class="sort icon"></span></div>
	</div>
	
	<span id="rename-container" style="display:none;">
		<input type="text" name="filename" value="新建文件夹" class="newfolder" />
		<a title="确定" class="btn sure bst4" href="javascript:;">确定</a>
		<a title="取消" class="btn cancel bst4" href="javascript:;">取消</a>
	</span>
	
	<div class="public-window" layoutH="152">
	</div> 
	<div class="personal-window" style="display:none;"></div>
</div>

<script type="text/javascript">

// status	0:正常 1:新增文件夹
var FILE_OPTION = {
	public_url: '${pageContext.request.contextPath}/app/publicinfo!dirPublicFolder.do',	
	personal_url: '${pageContext.request.contextPath}/app/publicinfo!dirPersonalFolder.do',
	view_url: '${pageContext.request.contextPath}/app/publicinfo!view.do',
	add_url: '${pageContext.request.contextPath}/app/publicinfo!addFolder.do',
	download_url: '${pageContext.request.contextPath}/app/publicinfo!download.do',
	download_file_url: '${pageContext.request.contextPath}/app/fileinfo!downloadfile.do',
	delete_url:'${pageContext.request.contextPath}/app/publicinfo!delete.do',
	rename_url:'${pageContext.request.contextPath}/app/publicinfo!rename.do',
	copy_url:'${pageContext.request.contextPath}/app/publicinfo!copy.do',
	move_url:'${pageContext.request.contextPath}/app/publicinfo!rename.do',
	upload_url:'${pageContext.request.contextPath}/app/publicinfo!upload.do',
	status:0,
	AUTH_VIEW:0,
	AUTH_ADD:1,
	AUTH_MODIFY:2,
	TYPE_PUBLIC_CUSTOM:0,
	TYPE_PUBLIC_DEFAULT:1
};

var FILE_TYPES = {
		'bmp':'img',
		'gif':'img',
		'jpeg':'img',
		'jpg':'img',
		'png':'img',
		'vsd':'visio',
		'pdf':'pdf',
		'doc':'word',
		'docx':'word',
		'xls':'excel',
		'txt':'txt',
		'wav':'music',
		'mp3':'music',
		'mov':'movie',
		'rm':'movie',
		'rmvb':'movie',
		'avi':'movie',
		'mkv':'movie',
		'ppt':'ppt',
		'app':'apple',
		'exe':'exe',
		'zip':'zip',
		'rar':'zip',
		'7z':'zip',
		'apk':'apk'
};

var CAN_VIEW = {
		'img':1,'pdf':1,'word':1,'txt':1,'ppt':1,'excel':1
};

var FILES = [];
var FOLDERS = [];
var SELECTED_FILES = [];
var popMenuHandle;


// 加载公共文件夹
function dirPublicFolder() {
	var path = FILE_OPTION.infocode;
	
	if (path) {
		$.getJSON(FILE_OPTION.public_url, {path:path}, dirPublicFolderCallback);
	}
	else {
		$.getJSON(FILE_OPTION.public_url, dirPublicFolderCallback);
	}
}

function getFileType(file) {
	if (!file) return "";
	
	// 文件夹
	if (file.isfolder=='1') return "folder";
	
	if (FILE_TYPES[file.fileextension.toLowerCase()]) return FILE_TYPES[file.fileextension.toLowerCase()];
	
	return "";
}

function getFileURL(file) {
	// 文件夹
	if (file.isfolder=='1') {
		return FILE_OPTION.public_url+'?path='+file.infocode;
	}
	else if (CAN_VIEW[getFileType(file)]) {
		return FILE_OPTION.view_url+'?infocode='+file.infocode+'&type='+getFileType(file);
	}
	// 查看文件
	else {
		return 'javascript:;';
	}
}

function getLinkType(file) {
	// 文件夹
	if (file.isfolder=='1') {
		return 'dir';
	}
	else if (CAN_VIEW[getFileType(file)]) {
		return 'view';
	}
	// 查看文件
	else {
		return 'none';
	}
}

function getFileSize(file) {
	// 文件夹
	if (file.isfolder=='1') {
		return '-';
	}
	// 查看文件
	else {
		return (parseFloat(file.filesize) / (1024*1024)).toFixed(2) + ' MB';
	}
}

function getFullFilename(file) {
	return file.fileextension ? file.filename+'.'+file.fileextension : file.filename;
}

function getFileUploadtime(file) {
	return file.uploadtime.replaceAll('T', ' ');
}

function addFileSingle(file) {
	
	var html = DWZ.frag["PUBLIC_FILE_LINE"]
	
	.replaceAll('{infocode}', file.infocode)
	// 替换类型
	.replaceAll('{type}', getFileType(file))
	.replaceAll('{fullFilename}', getFullFilename(file))
	// 替换文件名
	.replaceAll('{filename}', file.filename)
	// 替换类型
	.replaceAll('{atype}', getLinkType(file))
	// 替换URL
	.replaceAll('{url}', getFileURL(file))
	// 替换文件大小
	.replaceAll('{size}', getFileSize(file))
	// 替换上传者
	.replaceAll('{ownercode}', file.uploader)
	// 替换上传时间
	.replaceAll('{uploadtime}', getFileUploadtime(file));
	
	var line = FILES[0] || FOLDERS[0];
	var fileLine;
	
	if (line) {
		line.before(html);
		fileLine = line.closest('.public-window').find('div[_data_infocode='+file.infocode+']');
	}
	else {
		var container = $('.public-window');
		fileLine = $(html).appendTo(container).addClass('last');
	}
	
	saveData(fileLine, file);
	
	if (file.isfolder=='1') {
		FOLDERS.push(fileLine);
	}
	else {
		FILES.push(fileLine);
	}
	
}

function addFile(container, file, last) {
	var html = DWZ.frag["PUBLIC_FILE_LINE"]
	
	.replaceAll('{infocode}', file.infocode)
	// 替换类型
	.replaceAll('{type}', getFileType(file))
	.replaceAll('{fullFilename}', getFullFilename(file))
	// 替换文件名
	.replaceAll('{filename}', file.filename)
	// 替换类型
	.replaceAll('{atype}', getLinkType(file))
	// 替换URL
	.replaceAll('{url}', getFileURL(file))
	// 替换文件大小
	.replaceAll('{size}', getFileSize(file))
	// 替换上传者
	.replaceAll('{ownercode}', file.uploader)
	// 替换上传时间
	.replaceAll('{uploadtime}', getFileUploadtime(file));
	
	var line = $(html).appendTo(container);
	
	if (last) {
		line.addClass('last');
	}
	
	if (file.isfolder=='1') {
		FOLDERS.push(line);
	}
	else {
		FILES.push(line);
	}
	
	saveData(line, file);
}

function saveData(line, file) {
	line.data('authority', file.authority)
		.data('dounloadcount', file.downloadcount)
		.data('filedescription', file.filedescription)
		.data('fileextension', file.fileextension)
		.data('filename', file.filename)
		.data('filesize', file.filesize)
		.data('infocode', file.infocode)
		.data('isfolder', file.isfolder)
		.data('modifytime', file.modifytime)
		.data('ownercode', file.ownercode)
		.data('uploader', file.uploader)
		.data('readcount', file.readcount)
		.data('status', file.status)
		.data('type', file.type)
		.data('unitcode', file.unitcode)
		.data('uploadtime', file.uploadtime);
		
}

// 创建路径
function createPath(data) {
	$('.line.path span.ready').show();
	$('.line.path span.refresh').hide();
	
	var line = $('.file-container .line.path');
	var unitroot = data.unitroot;
	var parentcode = data.parentcode;
	var infocode = data.infocode;
	
	// 返回上一级
	if (parentcode && parentcode != '0') {
		$('a.uplevel', line).removeClass('text').attr('href', FILE_OPTION.public_url+'?path='+parentcode);
	}
	else {
		$('a.uplevel', line).addClass('text').attr('href', '');
	}
	
	// 部门主页
	if (infocode == unitroot) {
		$('a.home', line).addClass('text');
	}
	else {
		$('a.home', line).removeClass('text').attr('href', FILE_OPTION.public_url+'?path='+unitroot);
	}
	
	// 路径
	$('.ed', line).html('');
	
	if (data.path) {
		var path = data.path;
		var length = data.path.length;
		
		for (var i=0; i<length; i++) {
			if (i == length - 1) {
				$('.ed', line).append('&gt;&nbsp;&nbsp;<a class="patha text">'+ path[i].FILENAME +'</a>&nbsp;&nbsp;');
			}
			else {
				$('.ed', line).append('&gt;&nbsp;&nbsp;<a class="patha" type="dir" href="' + FILE_OPTION.public_url+'?path='+path[i].INFOCODE+'">'+ path[i].FILENAME +'</a>&nbsp;&nbsp;');
			}
		}
	}
	
	// 加载总数
	$('i', line).html(data.files.length);
	
}

function dirPublicFolderCallback(data){
	
	if (!data || '0' != data.result) {
		alertMsg.warn(data.description);
		return false;
	};
	
	FILE_OPTION.username = data.username;
	FILE_OPTION.usercode = data.usercode;
	FILE_OPTION.userunit = data.userunit;
	FILE_OPTION.fileunit = data.fileunit;
	FILE_OPTION.infocode = data.infocode;
	FILE_OPTION.authority = data.authority;
	
	createPath(data);
	
	var files = data.files;
	var container = $('.public-window');
	
	// 清空
	var count = 0;
	FOLDERS = [];
	FILES = [];
	SELECTED_FILES = [];
	
	$('.public-window .line').remove();
	
	for (var i=0; i<files.length; i++) {
		
		if (i != files.length-1) {
			addFile(container, files[i], false);
		}
		else {
			addFile(container, files[i], true);
		}
		
	}
	
	selectHead();
}

/**
 * 打开新建文件夹
 */
function toAddFolder() {
	if (!authorityAdd()) {
		alertMsg.warn('没有权限在非本机构创建文件夹、上传文件。');
		
		return false;
	}
	
	var html = DWZ.frag["PUBLIC_NEW_FOLDER"]
	// 替换上传者
	.replaceAll('{username}', FILE_OPTION.username);
	
	var container = $('.public-window');
	var firstChild = container.children().eq(0);
	
	// 插入
	if (firstChild[0]) {
		firstChild.before(html);
	}else {
		container.append(html);
	}
	
	var div = $('.line.new', container);
	var input = $('input', div).select();
	
	FILE_OPTION.status = 1;
}

/**
 * 关闭新建文件夹
 */
function toCancelAddFolder() {
	var container = $('.public-window');
	var div = $('.line.new', container).remove();
	FILE_OPTION.status = 0;
}

function addFolder() {
	var container = $('.public-window');
	var div = $('.line.new', container);
	var input = $('input', div).select();
	
	if (!input.val()) {
 		alertMsg.warn('请填写文件名。', {okCall:function() {
			input.focus();
		}}); 
		

		return false;
	}
	
	$.post(FILE_OPTION.add_url, {filename:input.val(), root:FILE_OPTION.infocode}, addFolderCallback, 'json');
}

function addFolderCallback(data) {
	if (!data || '0' != data.result) {
		alertMsg.warn(data.description);
		return false;
	};
	
	toCancelAddFolder();
	alertMsg.correct('新建文件夹成功。');
	
	dirPublicFolder();
}

function selectFile(e, line) {
	// 新建文件夹中不可选择文件
	if (FILE_OPTION.status == 1) {
		return false;
	}
	
	$(line).toggleClass('selected');
	var container = $('.public-window');
	
	SELECTED_FILES.length = 0;
	$('div.line.selected', container).each(function(i, line) {
		SELECTED_FILES.push($(line));
	});
	
	selectHead();
}

function selectAllFile(e, button) {
	// 新建文件夹中不可选择文件
	if (FILE_OPTION.status == 1) {
		return false;
	}
	
	var $this = $(button).closest('.line');
	$this.toggleClass('selected');
	
	var container = $('.public-window');
	if ($this.hasClass('selected')) {
		
		$('div.line', container).addClass('selected');
		$('.line.operation').addClass('selected');
		$('.header').addClass('selected');
		
		SELECTED_FILES.length = 0;
		$('div.line.selected', container).each(function(i, line) {
			SELECTED_FILES.push($(line));
		});
		
	}else {
		
		$('div.line', container).removeClass('selected');
		$('.line.operation').removeClass('selected');
		$('.header').removeClass('selected');
		SELECTED_FILES.length = 0;
	}
	
	selectHead();
}

function selectHead() {
	var container = $('.file-container');
	
	if (SELECTED_FILES.length == 0) {
		$('.line.operation', container).hide();
		$('.header', container).show().removeClass('selected');
	}
	else {
		$('.line.operation', container).show().find('i').html(SELECTED_FILES.length);
		$('.header', container).hide();
	}
}

function downloadFile() {
	var files = [];
	
	for (var i=0; i<SELECTED_FILES.length; i++) {
		var line = SELECTED_FILES[i];
		files.push(line.data('infocode'));
	}
	
	$.post(FILE_OPTION.download_url, {infocode: files.join(',')}, downloadFilesCallback, 'json');
}

function downloadFilesCallback(data) {
	if (!data || '0' != data.result) {
		alertMsg.warn(data.description);
		return false;
	};
	
	// 下载文件
	var downloadForm = $('#downloadForm');
	
	if (!downloadForm[0]) {
		var form = $("<form>");   //定义一个form表单
		form.attr('id', 'downloadForm');
		form.attr('style','display:none');   //在form表单中添加查询参数
		form.attr('target','');
		form.attr('method','post');
		form.attr('action',FILE_OPTION.download_file_url);
		
		var input = $('<input>'); 
		input.attr('type','hidden'); 
		input.attr('name','filecode'); 
		
		$('body').append(form);  //将表单放置在web中
		form.append(input);   //将查询参数控件提交到表单上
		downloadForm = form;
	}
	
	downloadForm.find('input[name=filecode]').val(data.filecode);
	downloadForm.submit(); 
}

function deleteFiles() {
	var files = [];
	
	for (var i=0; i<SELECTED_FILES.length; i++) {
		var line = SELECTED_FILES[i];
		var cancelFlag = false;
		var notMyFile = false;
		
		if (line.data('status') == '1' || line.data('type') == FILE_OPTION.TYPE_PUBLIC_DEFAULT) {
			cancelFlag = true;
		}
		else if (line.data('ownercode') != FILE_OPTION.usercode) {
			notMyFile = true;
		}
		else {
			files.push(line.data('infocode'));
		}
		
	}
	
	if (cancelFlag && files.length == 0) {
		alertMsg.warn('所选文件为系统默认或状态为锁定，无法删除。');
		return false;
	}
	else if (cancelFlag && files.length != 0) {
		alertMsg.confirm('所选文件有部分为系统默认或状态为锁定，这些文件将不会被删除。剩下的文件将被删除不可恢复，是否继续？', {
			okCall: function() {
				$.post(FILE_OPTION.delete_url, {infocode: files.join(',')}, deleteFilesCallback, 'json');
			}
		});
	}
	else if (notMyFile && files.length == 0) {
		alertMsg.warn('所选文件不是本人上传，无法删除。');
		return false;
	}
	else if (notMyFile && files.length != 0) {
		alertMsg.confirm('所选文件有部分不是本人上传，这些文件将不会被删除。剩下的文件将被删除不可恢复，是否继续？', {
			okCall: function() {
				$.post(FILE_OPTION.delete_url, {infocode: files.join(',')}, deleteFilesCallback, 'json');
			}
		});
	}
	else {
		alertMsg.confirm('文件将被删除不可恢复，是否继续？', {
			okCall: function() {
				$.post(FILE_OPTION.delete_url, {infocode: files.join(',')}, deleteFilesCallback, 'json');
			}
		});
	}
	
}

function deleteFilesCallback(data) {
	if (!data || '0' != data.result) {
		alertMsg.warn(data.description);
		return false;
	};
	
	alertMsg.correct('删除文件成功。');
	
	dirPublicFolder();
}

function getSort(info) {
	var btn = info.find('span.sort');
	
	// 正序
	var sort = 1;
	
	// 倒序
	if (btn.hasClass('asc')) {
		sort = 0;
	}
	
	return sort;
}

function sortFileIcon(info, sort) {
	var container = $('.public-window');
	
	// 去除原来的排序图标
	info.closest('div.line').find('span.sort').removeClass('asc').removeClass('desc');
	
	if (sort) {
		$('span.sort', info).addClass('asc');
	}
	else {
		$('span.sort', info).addClass('desc');
	}
	
	// 去除原来最后.last样式
	$('div.last.line', container).removeClass('last');
	
	if (FILES.length != 0) {
		FILES[FILES.length -1].addClass('last');
	}
	else if (FOLDERS.length != 0) {
		FOLDERS[FOLDERS.length -1].addClass('last');
	}
	
	container.hide();
	container.detach('.line');
	
	for (var i=0; i<FOLDERS.length; i++) {
		container.append(FOLDERS[i]);
	}	
	for (var i=0; i<FILES.length; i++) {
		container.append(FILES[i]);
	}	
	
	container.show();
}

/**
 * 按名称排序
 */
function sortByName(info) {
	info = $(info);
	var sort = getSort(info);
	
	if (sort) {
		FOLDERS.sort(ArraysSortByNameAsc);
		FILES.sort(ArraysSortByNameAsc);
	}
	else {
		FOLDERS.sort(ArraysSortByNameDesc);
		FILES.sort(ArraysSortByNameDesc);
	}
	
	sortFileIcon(info, sort);
}

/**
 * 按大小排序
 */
function sortBySize(info) {
	info = $(info);
	var sort = getSort(info);
	
	if (sort) {
		FOLDERS.sort(ArraysSortBySizeAsc);
		FILES.sort(ArraysSortBySizeAsc);
	}
	else {
		FOLDERS.sort(ArraysSortBySizeDesc);
		FILES.sort(ArraysSortBySizeDesc);
	}
	
	sortFileIcon(info, sort);
}

/**
 * 按上传者排序
 */
function sortByUploader(info) {
	info = $(info);
	var sort = getSort(info);
	
	if (sort) {
		FOLDERS.sort(ArraysSortByUploaderAsc);
		FILES.sort(ArraysSortByUploaderAsc);
	}
	else {
		FOLDERS.sort(ArraysSortByUploaderDesc);
		FILES.sort(ArraysSortByUploaderDesc);
	}
	
	sortFileIcon(info, sort);
}

/**
 * 按上传时间排序
 */
function sortByUploadTime(info) {
	info = $(info);
	var sort = getSort(info);
	
	if (sort) {
		FOLDERS.sort(ArraysSortByUploadTimeAsc);
		FILES.sort(ArraysSortByUploadTimeAsc);
	}
	else {
		FOLDERS.sort(ArraysSortByUploadTimeDesc);
		FILES.sort(ArraysSortByUploadTimeDesc);
	}
	
	sortFileIcon(info, sort);
}

function ArraysSortByNameAsc(file1, file2) {
	var name1 = file1.data('filename');
	var name2 = file2.data('filename');
	
	if (name1 > name2) return 1;
	if (name2 > name1) return -1;
	
	return 0;
}

function ArraysSortByNameDesc(file1, file2) {
	var name1 = file1.data('filename');
	var name2 = file2.data('filename');
	
	if (name1 > name2) return -1;
	if (name2 > name1) return 1;
	
	return 0;
}

function ArraysSortBySizeAsc(file1, file2) {
	var size1 = file1.data('filesize');
	var size2 = file2.data('filesize');
	
	if (size1 > size2) return 1;
	if (size2 > size1) return -1;
	
	return 0;
}

function ArraysSortBySizeDesc(file1, file2) {
	var size1 = file1.data('filesize');
	var size2 = file2.data('filesize');
	
	if (size1 > size2) return -1;
	if (size2 > size1) return 1;
	
	return 0;
}

function ArraysSortByUploaderAsc(file1, file2) {
	var uploader1 = file1.data('uploader');
	var uploader2 = file2.data('uploader');
	
	if (uploader1 > uploader2) return 1;
	if (uploader2 > uploader1) return -1;
	
	return 0;
}

function ArraysSortByUploaderDesc(file1, file2) {
	var uploader1 = file1.data('uploader');
	var uploader2 = file2.data('uploader');
	
	if (uploader1 > uploader2) return -1;
	if (uploader2 > uploader1) return 1;
	
	return 0;
}

function ArraysSortByUploadTimeAsc(file1, file2) {
	var uploadtime1 = file1.data('uploadtime');
	var uploadtime2 = file2.data('uploadtime');
	
	if (uploadtime1 > uploadtime2) return 1;
	if (uploadtime2 > uploadtime1) return -1;
	
	return 0;
}

function ArraysSortByUploadTimeDesc(file1, file2) {
	var uploadtime1 = file1.data('uploadtime');
	var uploadtime2 = file2.data('uploadtime');
	
	if (uploadtime1 > uploadtime2) return -1;
	if (uploadtime2 > uploadtime1) return 1;
	
	return 0;
}

function openPopMenu(btn) {
	var container = $('.file-container');
	var menu = $('.pull-down-menu.header-menu', container);
	clearTimeout(popMenuHandle);
	
	btn = $(btn);
	if (btn.hasClass('pull-down-menu')) return;
	
	var left = btn.position().left+10;
	var top = btn.position().top + btn.height();
	
	if (SELECTED_FILES.length != 1) {
		$('a.rename', menu).addClass('disable');
	}
	else {
		$('a.rename', menu).removeClass('disable');
	}
	
	menu.css({
		left:left,
		top:top
	}).show();
}

function closePopMenu() {
	var container = $('.file-container');
	var menu = $('.pull-down-menu.header-menu', container);
	
	menu.hide();
}

function toRename() {
	FILE_OPTION.status = 1;
	
	$('.pull-down-menu.header-menu').hide();
	
	if (SELECTED_FILES.length != 1) {
		FILE_OPTION.status = 0;
		return false;
	}
	
	var line = SELECTED_FILES[0];
	
	if (FILE_OPTION.TYPE_PUBLIC_DEFAULT == line.data('type')) {
		alertMsg.warn('不能重命名机构默认文件夹。');
		
		cancelRename();
		return false;
	}
	
	if (!authroityRename()) {
		alertMsg.warn('不能重命名非本人上传文件。');
		
		cancelRename();
		return false;
	}
	
	var info = line.find('div.info.name');
	info.find('a').hide();
	
	var rename = FILE_OPTION.RENAME_CONTAINER.show();
	rename.find('input').val(line.data('filename')).select();
	
	info.append(rename);
}

function cancelRename() {
	var line = SELECTED_FILES[0];
	var info = line.find('div.info.name');
	info.find('a').show();
	FILE_OPTION.status = 0;
	
	FILE_OPTION.RENAME_CONTAINER.detach();
}

function rename() {
	if (SELECTED_FILES.length != 1) return false;
	
	var file = SELECTED_FILES[0];
	var rename = $('#rename-container');
	var info = file.find('div.info.name');
	
	var newName = rename.find('input').val();

	if (/\.|\*|\?|\%|\_/.test(newName)) {
		alertMsg.info('文件名不能包含.*?%_等特殊字符，请重新输入。');
		
		rename.find('input').select();
		return false;
	}
	
	$.post(FILE_OPTION.rename_url, {name:newName, infocode:file.data('infocode'), root:FILE_OPTION.infocode}, renameCallback, 'json');
}

function renameCallback(data) {
	// 权限错误没有必要再修改了
	if (data.result == '1' || data.result == '2') {
		alertMsg.warn(data.description);
		cancelRename();
		return false;
	};
	
	if (data.result != '0') {
		alertMsg.warn(data.description);
		return false;
	};
	
	cancelRename();
	
	var file = data.file;
	var infocode = file.infocode;
	var filename = file.filename;
	
	var line = $('div.line[_data_infocode='+infocode+']');
	line.data('filename', filename);
	line.find('div.info.name').find('a').first().html(filename);
	
	alertMsg.correct('重命名文件成功。');
}

function toUploadFile() {
	
	if (!authorityAdd()) {
		alertMsg.warn('非本机构文件夹不能上传文件。');
		return false;
	}
	
	$('#publicinfo-upload').click();
}

function uploadFile(btn) {
	btn = $(btn);
	
	if (!btn.val()) {
		return;
	}
	
	//$('#uploadForm').submit();
	
 	$.ajaxFileUpload({
		url:FILE_OPTION.upload_url,
		secureuri:false,
		fileElementId:'publicinfo-upload',
		data:{path:FILE_OPTION.infocode},
		dataType:'json',
		success: uploadCallback,
		error:uploadError
	}); 
}

function uploadCallback(data) {
	if (data.result != '0') {
		alertMsg.warn(data.description);
		return false;
	};
	
	addFileSingle(data.file);
}

function uploadFormData() {
	return {
		path:FILE_OPTION.infocode
	};
}

function uploadError(data, status, e) {
	alertMsg.info('文件上传失败。');
	dirPublicFolder();
}

/**
 * 附加事件
 */
function attachEvent() {
	
	$('a[type=dir]').die('click');
	// 进入目录
	$('a[type=dir]').live('click', function(event) {
		$('.line.path span.ready').hide();
		$('.line.path span.refresh').show();
		
		$.getJSON(this.href, dirPublicFolderCallback);
		event.preventDefault();
	} );
	
	$('a[type=none]').die('click');
	// 进入目录
	$('a[type=none]').live('click', function(event) {
		event.preventDefault();
		return false;
	} );
	
	// 新建文件夹
	$('.line.navigation a.add').click(function(event) {
		toAddFolder();
		event.preventDefault();
	});
	
	var container = $('.public-window');
	$('.line.new a.sure', container).die('click');
	$('.line.new a.sure', container).live('click', function(event) {
		addFolder();
		event.preventDefault();
	});
	$('.line.new a.cancel', container).die('click');
	$('.line.new a.cancel', container).live('click', function(event) {
		toCancelAddFolder();
		event.preventDefault();
	});
	
	// 选择文件
	$('.line', container).die('click');
	$('.line', container).live('click', function(event){
		selectFile(event, this);
	});
	
	// 全选
	$('.line.operation span.check').add('.header span.check').click(function(event) {
		selectAllFile(event, this);
	});
	
	// 下载
	$('.line.operation a.download').click(function(event) {
		downloadFile();
	});
	
	// 删除
	$('.line.operation a.delete').click(function(event) {
		deleteFiles();
	});
	
	// 排序
	$('.line.header div.name').click(function (event) {
		sortByName(this);
	});
	
	$('.line.header div.size').click(function (event) {
		sortBySize(this);
	});
	
	$('.line.header div.owner').click(function (event) {
		sortByUploader(this);
	});
	
	$('.line.header div.time').click(function (event) {
		sortByUploadTime(this);
	});
	
	// 更多按钮
	$('.line.operation a.more').add('.pull-down-menu.header-menu').hover(function(event) {
		openPopMenu(this);
	}, function(event) {
		popMenuHandle = setTimeout("closePopMenu()", 50);
	});
	
	// 重命名
	$('.pull-down-menu.header-menu a.rename').click(function(event) {
		toRename();
	});
	
	$('#rename-container a.sure').click(function (event) {
		rename();
	});
	
	$('#rename-container a.cancel').click(function (event) {
		cancelRename();
	});
}

function authority(auth, pos) {
	return ((auth >> pos) % 2) == 1;
}

/**
 * 鉴权新增文件夹、上传文件
 */
function authorityAdd() {
	if (FILE_OPTION.userunit != FILE_OPTION.fileunit) {
		return false;
	}
	
	return authority(FILE_OPTION.authority, FILE_OPTION.AUTH_ADD);
}

function authroityDelete() {
	
}

/**
 * 鉴权重命名
 */
function authroityRename() {
	var file = SELECTED_FILES[0];
	
	if (FILE_OPTION.usercode != file.data('ownercode')) {
		return false;
	}
	
	return authority(file.data('authority'), FILE_OPTION.AUTH_MODIFY);
}

$(function () {
	FILE_OPTION.RENAME_CONTAINER = $('#rename-container');
	
	dirPublicFolder();
	attachEvent();
});
</script>