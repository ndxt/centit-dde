$(function() {
	if (!$.fn.uploadify) {
		alert('没有引用Uploadify库，请联系系统管理员');//请到http://www.highcharts.com/官网下载
		return false;
	}
	
	$.myUpload = {
			_run_flag: false,
			
			SELECTORS : {
				UNITBOX:	'div.unitBox',					// 系统页面容器
				DIALOG:		'div.dialog',					// 系统对话框
				
				FILE_INFO:	'.file-info',					// 上传完成后显示内容
				TITLE_NUM:	'.info-num',					// 标题文件个数
				TITLE_SPEED:'.info-speed',					// 标题速度	
				UPLOAD_DAILOG: '.upload-dialog',			// 上传文件窗口
				FILE_PROGRESS: '.title .inline-mask',		// 文件上传进度条
				CONTENT: 	'.content',						// 上传文件内容
				FILE_CONTENT:	'.upload-content',			// 上传文件内容
				BTN_CLOSE: 	'a.close',						// 关闭按钮
				BTN_MIN:	'a.min',						// 最小化按钮
				BTN_RESTORE:'a.restore'						// 恢复按钮
			},
			
			init: function() {
				var _run_flag = true;
				
				
				
				$(this).each(function() {
					
					var $this = $(this);
					
					var setOptionAttribute = function(name, obj, option) {
						var value = obj.attr(name);
						
						if (value) {
							if (value.toLowerCase() == 'true' || value.toLowerCase() == 'false') {
								option[name] = eval(value.toLowerCase());
								return;
							}
							
							if (parseInt(value)) {
								option[name] = parseInt(value);
								return;
							}
							
							option[name] = obj.attr(name);
						}
					};
					
					var setOptionCallback = function(name, obj, option) {
						var value = obj.attr(name);
						
						if (! $.isFunction(value)) value = eval('(' + value + ')');
						
						option[name] = value;
					};
					
					var myOptions = {
							optID: $this.attr('optID')			// 上传文件业务代码
					};
					
					setOptionAttribute('inputID', $this, myOptions);
					setOptionAttribute('queueContainer', $this, myOptions);
					setOptionAttribute('uploader', $this, myOptions);
					setOptionAttribute('buttonText', $this, myOptions);
					setOptionAttribute('fileTypeDesc', $this, myOptions);
					setOptionAttribute('fileTypeExts', $this, myOptions);
					setOptionAttribute('queueSizeLimit', $this, myOptions);
					setOptionAttribute('uploadLimit', $this, myOptions);
					setOptionAttribute('multi', $this, myOptions);
					
					setOptionCallback('uploadCallback', $this, myOptions);
					setOptionCallback('dynamicFormData', $this, myOptions);
					
					// 设置选项
					var settings = $.extend($.myUpload.options, myOptions);
					settings.formData = {optID:settings.optID};
					settings.swf = settings.swf+'?var='+(new Date().getTime());
					
					// 绘制窗口
					var dialog = $.myUpload.drawUploadDialog(settings);
					$.myUpload.attachEvent(dialog);
					
					$this.uploadify(settings);
				});
			},
			
			drawUploadDialog: function(settings) {
				var container = navTab.getCurrentPanel();
				if (settings.queueContainer) {
					container = $('#'+settings.queueContainer,navTab.getCurrentPanel());
				}
				
				var dialog = $(DWZ.frag["PUBLIC_UPLOAD_CONTAINER"]).appendTo(container);
				settings.dialog = dialog;
				
				return dialog;
			},
			
			// 附加事件
			attachEvent: function(dialog) {
				$('.upload-dialog .close').die('click');
				$('.upload-dialog .close').live('click', function (event) {
					$.myUpload.closeUploadDialog(this);
				});
				
				$('.upload-dialog .min').die('click');
				$('.upload-dialog .min').live('click', function (event) {
					$.myUpload.minUploadDialog(this);
				});
				
				$('.upload-dialog .restore').die('click');
				$('.upload-dialog .restore').live('click', function (event) {
					$.myUpload.restoreUploadDialog(this);
				});
				
				$('.file-info a.delete').die('click');
				$('.file-info a.delete').live('click', $.myUpload.deleteFileValue);
			},
			
			// 打开文件上传窗口
			openUploadDialog: function(dialog) {
				dialog.show();
				
				this.restoreUploadDialog(dialog);
			},
			
			// 关闭文件上传窗口
			closeUploadDialog: function(obj) {
				var dialog = $(obj);
				
				// 点击按钮事件触发
				if ($(obj).is('a')) {
					dialog = $(obj).closest(this.SELECTORS.UPLOAD_DAILOG);
				}
				
				// TODO 清空上传文件队列
				
				
				var content = dialog.find(this.SELECTORS.FILE_CONTENT);
				
				// 清空
				content.html('');
				dialog.hide();
			},
			
			// 最小化文件上传窗口
			minUploadDialog: function(obj) {
				var dialog = obj;
				var btn = obj;
				
				// 点击按钮事件触发
				if ($(obj).is('a')) {
					dialog = $(btn).closest(this.SELECTORS.UPLOAD_DAILOG);
				}
				// 传入对象为上传文件对话框
				else {
					btn = dialog.find(this.SELECTORS.BTN_MIN);
				}
				
				$(btn).removeClass('min').addClass('restore').attr('title', '还原');
				var content = dialog.find(this.SELECTORS.CONTENT);
				content.slideUp();
			},
			
			// 恢复文件上传窗口
			restoreUploadDialog: function(obj) {
				var dialog = obj;
				var btn = obj;
				
				// 点击按钮事件触发
				if ($(obj).is('a')) {
					dialog = $(btn).closest(this.SELECTORS.UPLOAD_DAILOG);
				}
				// 传入对象为上传文件对话框
				else {
					btn = dialog.find(this.SELECTORS.BTN_RESTORE);
				}
				
				// 进度条清空
				this.setUploadFileProgress(dialog, 0);
				
				// 速度清空
				this.setTitleSpeed(dialog, '');
				
				$(btn).removeClass('restore').addClass('min').attr('title', '最小化');
				var content = dialog.find(this.SELECTORS.CONTENT);
				content.slideDown();
			},
			
			// 文件成功上传后将返回的文件ID写入到INPUT值中
			afterUploadSuccess: function(data) {
				
				if (data.result != '0') return;
				
				var input = $('#'+this.settings.inputID);
				var parent = $('#'+this.original[0].id).parent();
				
				if (!parent.find($.myUpload.SELECTORS.FILE_INFO)[0]) {
					parent.append('<table class="file-info"></table>');
				}
				var container = parent.find($.myUpload.SELECTORS.FILE_INFO);
				
				var file = data.file;
				var filename = file.fileext ? file.filename + '.' + file.fileext : file.filename;
				var filesize = (parseFloat(file.filesize) / (1024*1024)).toFixed(2) + ' MB';
				var filecode = file.filecode;
				
				container.append('<tr id="file_'+filecode+'" > <td>'+filename+'</td> <td>'+filesize+'</td> <td><a href="'+DWZ.contextPath+'/app/fileinfo!deletefile.do?filecode='+filecode+'" class="delete" inputID="'+this.settings.inputID+'" filecode="'+filecode+'">删除</a></td><tr>');
			
				$.myUpload.addFileValue(input, filecode);
			},
			
			addFileValue: function(input, filecode) {
				var values = input.val() ? input.val().split(',') : [];
				values.push(filecode);
				
				input.val(values.join(','));
			},
			
			deleteFileValue: function(event) {
				var $this = $(this);
				var input = $('#'+$this.attr('inputID'));
				var filecode = $this.attr('filecode');
				
				var values = input.val() ? input.val().split(',') : [];
				
				for (var i=0; i<values.length; i++) {
					if (values[i] == filecode) {
						values.splice(i, 1);
					} 
				}
				event.preventDefault();
				
				// 发送ajax删除文件
				
				$.getJSON($this.attr('href'), function(data) {
					if (data.result != '0') {
						alertMsg.warn(data.description);
						return false;
					}
					
					$this.closest('tr').fadeOut(function() {
						$(this).remove();
						input.val(values.join(','));
					});
				});
				
				return false;
			},
			
			// 设置文件上传进度条百分比，progress百分比整数
			setUploadFileProgress: function(dialog, progress) {
				dialog.find(this.SELECTORS.FILE_PROGRESS).css({
					width: progress+'%'
				});
			},
			
			// 设置标题
			setTitle: function(dialog, value) {
				var info = dialog.find(this.SELECTORS.TITLE_NUM);
				info.html(value);
			},
			
			// 设置文件个数标题
			setTitleNumber: function(dialog, value) {
				var info = dialog.find(this.SELECTORS.TITLE_NUM);
				
				if (value) {
					info.html('文件个数：' + value);
				}
				else {
					info.html('');
				}
			},
			
			// 设置速度标题
			setTitleSpeed: function(dialog, value) {
				var info = dialog.find(this.SELECTORS.TITLE_SPEED);
				
				if (value) {
					info.html('速度：' + value);
				}
				else {
					info.html('');
				}
			},
			
			// 获取已经上传文件个数
			getUploadDialogFileSize: function(files) {
				var length = 0;
				for (var n in files) {
					if (typeof files[n] === 'object') length++;
				}
				
				return length;
			},
			
			// 初始化函数
			onInit : function(instance) {
				var dialog = instance.settings.dialog;
				
        		$(instance).data('_data', {
        			all_num:0,		// 总共上传文件数
        			now_num:0		// 正在上传的文件序号
        		}).data('dialog', dialog);
			},
			
			// 选择文件后事件
			onSelect: function(file) {
				var dialog = $(this).data('dialog');
				
				$.myUpload.openUploadDialog(dialog);
			},
			
			// 开始上传文件事件
			onUploadStart: function(file) {
				var callback = this.settings.dynamicFormData;
				if (callback) {
					$('#'+this.original[0].id).uploadify("settings", "formData", callback());
				}
				
				var data = $(this).data('_data');
				var dialog = $(this).data('dialog');
				
	        	var all_num = $.myUpload.getUploadDialogFileSize(this.queueData.files);
	        	var now_num = data.now_num+1;
	        	
	        	data.all_num = all_num;
	        	data.now_num = now_num;
	        	
	        	// 重新设置上传文件个数
	        	$.myUpload.setTitleNumber(dialog, now_num + '/' + all_num);
			},
			
			// 正在上传文件事件
			onUploadProgress :function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
				var data = $(this).data('_data');
				var dialog = $(this).data('dialog');
				
				var all_num = $.myUpload.getUploadDialogFileSize(this.queueData.files);
	        	var now_num = data.now_num;
	        	
	        	data.all_num = all_num;
	        	
	        	// 重新设置上传文件个数
	        	$.myUpload.setTitleNumber(dialog, now_num + '/' + all_num);
	        	
	        	// 上传窗口最小化时才显示速度和进度
	        	if (!dialog.find($.myUpload.SELECTORS.CONTENT).is(':visible')) {
	        		$.myUpload.setTitleSpeed(dialog, this.queueData.averageSpeed + this.queueData.suffix);
	        		$.myUpload.setUploadFileProgress(dialog, this.queueData.percentage);
	        	}
			},
			
			// 文件成功上传后事件
			onUploadSuccess: function (file, data, response) {
				var callback = this.settings.uploadCallback;
				
				if (callback) {
					callback.call(this, $.parseJSON(data));
				}
				else {
					$.myUpload.afterUploadSuccess.call(this, $.parseJSON(data));
				}
				
			},
			
			// 全部文件上传完成
			onQueueComplete: function(queueData) {
				var dialog = $(this).data('dialog');
				
				
				$.myUpload.setTitle(dialog, '上传完成');
				
				// 进度、速度清空
				$.myUpload.setTitleSpeed(dialog, '');
        		$.myUpload.setUploadFileProgress(dialog, 0);
        		
        		// 最小化窗口
        		setTimeout(function() {
        			$.myUpload.minUploadDialog(dialog);
        		}, 1000);
			}
	};
	
	$.myUpload.options = {
		// 可选选项
		buttonText: '上传文件',
		removeCompleted: false,
		queueID:'upload-content',
		
		onInit: $.myUpload.onInit,
		onSelect: $.myUpload.onSelect,
		onUploadStart: $.myUpload.onUploadStart,
		onUploadProgress: $.myUpload.onUploadProgress,
		onUploadSuccess: $.myUpload.onUploadSuccess,
		onQueueComplete: $.myUpload.onQueueComplete,
		
		// 必选设置
		swf: DWZ.contextPath+'/scripts/plugin/uploadify-v3.1/uploadify.swf',
		uploader:DWZ.contextPath+'/app/fileinfo!uploadfile.do'
	};
});