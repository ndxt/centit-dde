var UnitUser = function(param) {
	var $this = this;
	var tree = param.treeObj;
	var setting = {
		data : {
			simpleData : {
				enable : true
			}
		}
	};

	var settingCheckbox = {
		data : {
			simpleData : {
				enable : true
			}
		},
		check : {
			enable : true
		}
	};
	this.e = {
		events : function() {
			// 绑定 this.e 下所有事件，统一调用
			$.each(this, function(index, fn) {
				if ('events' != fn && $.isFunction(fn)) {
					fn();
				}
			});
		},
		bindSave : function() {
			$('#' + param.btnAdd).click(function() {
				var $treeObj = $this.funs.getTree();

				var units = $this.funs.getCheckedName($treeObj);
				var userCodes = $this.funs.getCheckUserCode($treeObj);

				$('#' + param.username).text('');
				$('#' + param.username).text(units.join(';'));
				$('#' + param.usercode).val('');
				$('#' + param.usercode).val(userCodes.join(';'));

				$.pdialog.closeCurrent();
			});
		},

		// 已选择人员，再次点击选择后，覆写状态
		bindChooseInit : function() {
			var usercodes = $.trim($('#' + param.usercode).val());
			if ('' == usercodes) {
				return;
			}

			var ucodeArr = usercodes.split(';');

			var $tree = $this.funs.getTree();

			$.each(ucodeArr, function(index, code) {
				var node = $tree.getNodesByParam('id', code);
				$tree.checkNode(node[0], true, true);
			});

		}

	};

	this.funs = {
		init : function(zNodes) {
			$.fn.zTree.init($tree, setting, zNodes);
		},

		initCheckbox : function(zNodes) {
			$.fn.zTree.init($('#' + tree), settingCheckbox, zNodes);

			$this.e.events();
		},

		getTree : function() {
			return $.fn.zTree.getZTreeObj(tree);
		},

		/**
		 * 获取选中中文名
		 * 
		 * @param $tree
		 * @returns {Array}
		 */
		getCheckedName : function($tree) {
			var $checkNodes = $tree.getCheckedNodes();
			var $units = [];
			$.each($checkNodes, function(index, node) {
				var pNode = node.getParentNode();
				if (null == pNode) {
					var obj = (node.getCheckStatus());
					if (obj.checked && !(obj.half)) {
						$units.push(node.name);
					}
				} else {
					var obj = (pNode.getCheckStatus());
					var o = node.getCheckStatus();
					// 父节点选中，选择父节点。
					// 自己选中，父半选，选择自己
					if (obj.checked && (obj.half) && o.checked && !(o.half)) {
						$units.push(node.name);
					}
				}
			});

			return $units;
		},

		getCheckUserCode : function($tree) {
			var $checkNodes = $tree.getCheckedNodes();

			var userCodes = [];
			$.each($checkNodes, function(index, node) {
				// 不是父节点，并且不是root节点
				if (!(node.isParent) && 'false' == node.p) {
					userCodes.push(node.id);
				}
			});

			return userCodes;
		}
	};
};

function E(z) {
	console.error(z);
}
