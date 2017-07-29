jQueryCheckExt = function() {
	var $this = this;
	var $roleTree = {};
	this.makeTreeTable = function(roleTree, index, imgpath) {
		$roleTree = roleTree;
		var options = {
			openImg : imgpath + "/tv-collapsable.gif",
			shutImg : imgpath + "/tv-expandable.gif",
			leafImg : imgpath + "/tv-item.gif",
			lastOpenImg : imgpath + "/tv-collapsable-last.gif",
			lastShutImg : imgpath + "/tv-expandable-last.gif",
			lastLeafImg : imgpath + "/tv-item-last.gif",
			vertLineImg : imgpath + "/vertline.gif",
			blankImg : imgpath + "/blank.gif",
			// collapse : false,
			// column : 0,
			striped : true,
			highlight : true,
			state : false
		};
		$roleTree.jqTreeTable(index, options);
	};

	this.makeCkeckBoxTreeTable = function(roleTree, index, imgpath) {
		$this.makeTreeTable(roleTree, index, imgpath);
		$this.initParentClick(index);

		$this.pcCheckBoxClickEvent();
		$this.ccCheckBoxClickEvent();

	};
	// 初始化所有的 左边的click选项
	this.initParentClick = function(index) {
		$.each($roleTree.find("tr"), function(ind, tr) {
			$(tr).attr("sortId", index[ind]);

			var parentClicked = false;
			$.each($(tr).find('input:checkbox.cc'), function(ind, ccBox) {
				var sc = $(ccBox).attr("checked");
				if (sc == 'checked' || sc == true) {
					parentClicked = true;
				}
			});
			if (parentClicked == true)
				$this.setParentClick(tr);
		});
	};

	// 父节点click事件
	this.pcCheckBoxClickEvent = function() {

		$roleTree.find('input:checkbox.pc').click(function() {

			var pc = this;
			var tr = $(pc).closest('tr');
			// alert($(tr).index() );
			$this.clickAllCcCheckBox(tr, pc.checked);
		});
	};

	// 设置所有子节点中click标志
	this.clickAllCcCheckBox = function(tr, ck) {
		var $children = $(tr).find('input:checkbox');
		$.each($children, function(index, c) {
			$(c).attr("checked", ck);
		});

		if (ck == true)
			$this.setParentClick(tr);
		else
			$this.clearParentClick(tr);

		var trInd = $(tr).index() + 1;

		$.each($roleTree.find('tr[sortId=' + trInd + ']'), function(ind, subtr) {
			$this.clickAllCcCheckBox(subtr, ck);
		});
	};

	// 右边checkbox 的 click事件
	this.ccCheckBoxClickEvent = function() {

		$roleTree.find('input:checkbox.cc').click(function() {

			var cc = this;
			var td = $(cc).closest('td');
			var allclear = true;
			$.each($(td).find('input:checkbox.cc'), function(ind, ccCheck) {
				var sc = $(ccCheck).attr("checked");
				if (sc == 'checked' || sc == true) {
					allclear = false;
				}
			});

			var tr = $(cc).closest('tr');
			if (allclear == true)
				$this.clearParentClick(tr);
			else
				$this.setParentClick(tr);
		});
	};
	// 给所有的父节点设置 click标记
	this.setParentClick = function(tr) {

		$(tr).find('input:checkbox.pc').eq(0).attr("checked", true);
		var sortId = $(tr).attr("sortId");
		var parentTr = tr;

		while (sortId > 0) {
			parentTr = $(parentTr).closest('tbody').find("tr").eq(sortId - 1);
			$(parentTr).find('input:checkbox.pc').eq(0).attr("checked", true);
			sortId = $(parentTr).attr("sortId");
		}
	};
	// 查看兄弟节点 如果所有的兄弟节点都已经清楚，则清除上级节点的click 标记
	this.clearParentClick = function(tr) {

		$(tr).find('input:checkbox.pc').eq(0).attr("checked", false);

		var sortId = $(tr).attr("sortId");
		var parentTr = tr;
		while (sortId > 0) {
			var clearClick = true;
			$.each($(parentTr).closest('tbody').find('tr[sortId=' + sortId + ']'), function(ind, subtr) {
				var sc = $(subtr).find('input:checkbox.pc').eq(0).attr("checked");
				if (sc == 'checked' || sc == true) {
					clearClick = false;
				}
			});

			if (clearClick == true) {
				parentTr = $(parentTr).closest('tbody').find("tr").eq(sortId - 1);
				$(parentTr).find('input:checkbox.pc').eq(0).attr("checked", false);
				sortId = $(parentTr).attr("sortId");
			} else
				break;
		}
	};

};
