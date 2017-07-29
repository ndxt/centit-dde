function D(obj) {
	console.log(obj);
}

function PT(options) {
	var $this = this;
	this.events = {
		e : function() {
			for ( var i in this) {
				if (this.e != this[i] && $.isFunction(this[i])) {
					this[i]();
				}
			}
		},

		spanClick : function() {
			// alert(1);
			var $spans = options.content.find('span.edit');

			$spans.each(function() {
				var $sp = $(this);

				$sp.bind('click', function() {
					// 是否正在编辑标识
					if ('1' == $sp.attr('isEdit')) {
						return;
					}
					$sp.attr('isEdit', '1');

					var $span = $sp.find('span');
					$span.addClass('hidden');

					var obj = {};
					obj[$sp.attr('param')] = $sp.attr('paramValue');

					if ($sp.hasClass('select')) {
						obj = $.extend(obj, {
							'value' : $sp.attr('optionValue'),
							'text' : $sp.attr('optionText'),
							'url' : '/pm/projectTasks!getOptionValue.do',
							'param' : $sp.attr('param'),
							'paramValue' : $sp.attr('paramValue'),
							'module' : $sp.attr('module')

						});
						$this.funs.appendOptions($sp, obj);
					} else if ($sp.hasClass('textarea')) {
						obj.value = $.trim($sp.attr('paramValue'));
						obj.param = $sp.attr('param');

						$this.funs.appendTextarea($sp, obj);
					}
				});
			});
		},

		textBlur : function() {
			var $text = options.content.find('span>.text');
			$text.die('blur');
			$text.live('blur', function() {
				$this.funs.ajaxSave($(this), {
					'type' : 'text',
					'param' : $(this).attr('name'),
					'paramValue' : $(this).val()
				});
			});
		},

		buttonClick : function() {
			var $button = options.content.find('span :button');
			$button.die('click');
			$button.live('click', function() {
				var $options = $(this).parents('div.buttonActive').prevAll('select');

				if ($(this).hasClass('cancel')) {
					$this.funs.removeOptions($options, true);
				} else {
					$this.funs.ajaxSave($options, {
						'type' : 'select',
						'param' : $options.attr('name'),
						'paramValue' : $options.val()
					});
				}

			});
		},

		attentionClick : function() {
			$('#p_task_view_attention').click(function() {
				var $_this = $(this);
				var on = $_this.attr('on');
				
				$.post(options.contextPath + $(this).parent().attr('url'), {
					's_on' : on,
					'projectTasks.taskid' : options.taskid
				}, function(result) {
					if ('success' == $.trim(result)) {
						var $span = $_this.next('span');
						var size = parseInt($span.text());
						if ('1' == on) {
							$span.text(size + 1);
							$_this.attr('on', '0');
							$_this.text('正在关注');
						} else if ('0' == on) {
							$span.text(size - 1);
							$_this.attr('on', '1');
							$_this.text('关注');
						}
					}
				}, "text");
			});
		}
	};
	this.funs = {
		removeOptions : function($options, cancel) {
			var $span = $options.prev();
			if (!cancel) {
				$span.html($options.find(":selected").text());
				$span.parent('span').attr('value', $options.find(":selected").val());
			}

			$span.removeClass('hidden');

			$options.nextAll('div.buttonActive').die('click').remove();
			$options.remove();

			$span.parent('span').attr('isEdit', '0');

		},

		removeText : function($text, obj) {
			$text.parent('span').attr('isEdit', '0');
			$text.parent('span').attr('paramValue', obj.paramValue);

			$text.prev('span').removeClass('hidden');
			$text.prev('span').html(obj.paramValue);

			$text.die('blur').remove();
		},

		appendOptions : function($sp, obj) {
			$this.funs
					.ajaxOptionValue(
							obj,
							function(jsonResult) {
								var select = [];
								select.push("<select name = '" + $sp.attr('key') + "' class='combox'>");
								for ( var op in jsonResult) {
									select.push("<option value = '" + jsonResult[op][obj.value] + "' clazz='"
											+ obj.clazz + "'");

									if (jsonResult[op][obj.value] == $sp.attr('value')) {
										select.push("selected='selected' >");
									} else {
										select.push(">");
									}

									select.push(jsonResult[op][obj.text] + "</option>");
								}
								select.push("</select>");

								$sp.append(select.join(''));
								$sp
										.append('<div class="buttonActive"><div class="buttonContent"><button class="submit">提交</button></div></div>');
								$sp
										.append('<div class="buttonActive"><div class="buttonContent"><button class="cancel">取消</button></div></div>');

							});
		},

		appendTextarea : function($sp, obj) {
			$sp.append('<textarea rows="6" cols="90" name="' + obj.param + '" class="text editor">' + obj.value
					+ '</textarea>');
		},

		ajaxOptionValue : function(obj, fn) {
			$.post(options.contextPath + obj.url, obj, fn, 'json');
		},

		ajaxSave : function($o, obj) {
			$.post(options.contextPath + '/pm/projectTasks!saveAjax.do', {
				'taskid' : options.taskid,
				'param' : obj.param,
				'paramValue' : obj.paramValue
			}, function(json) {
				if ('success' == $.trim(json)) {
					if ('select' == obj.type) {
						$this.funs.removeOptions($o);
					} else if ('text' == obj.type) {
						$this.funs.removeText($o, obj);
					}
				}
			}, 'text');
		}
	};
}