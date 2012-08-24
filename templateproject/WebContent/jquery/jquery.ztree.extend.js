//zTree Setting and function
var showedTreeId;
var setting = {
		check: { enable: true, chkboxType: {"Y":"s", "N":"s"} },
		view: { dblClickExpand: false },
		data: { simpleData: { enable: true } },
		callback: { beforeClick: treeBefClick, onCheck: treeOncheck }
	};
//设置树中被选中的节点
function selectNodes(treeObj,ids){
	//设置勾选的节点
	if(treeObj){
		treeObj.checkAllNodes(false);
		if(ids){
			var idArr=ids.split(",");
			for(var i in idArr){
				var node = treeObj.getNodeByParam("id", idArr[i], null);
				if(node!=null){
					treeObj.checkNode(node, true, true);
					var nodes=[];
					nodes.push(node);
					while(node.getParentNode()!=null){
						node=node.getParentNode();
						nodes.push(node);
					}
					for(var j=nodes.length-1;j>=0;j--)
						treeObj.expandNode(nodes[j], true);
				}
			}
		}
	}
}

/*显示树形菜单 */
function showTreeMenu(treeId){
	var paramName = "#"+treeId;
	var obj = $(paramName+"Names");
	var offset = obj.offset();
	var v=$(paramName+"Ids").attr("value");
	var treeObj = $.fn.zTree.getZTreeObj(treeId+"Tree");
	selectNodes(treeObj,v);
	$(paramName+"MenuContent").css({left:offset.left + "px", top:offset.top + obj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", hidenTree);
	showedTreeId = treeId;
}
/*隐藏树形菜单 */
function hidenTree(event){
	var paramName = showedTreeId;
	if (!(event.target.id == (paramName+"Names") || event.target.id == (paramName+"MenuContent") || $(event.target).parents("#"+paramName+"MenuContent").length>0)) {
		$("#"+paramName+"MenuContent").fadeOut("fast");
		$("body").unbind("mousedown", hidenTree);
	}
}
/*判断是否可以点击*/
function treeBefClick(treeId,treeNode){
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
/*选中节点时更新参数*/
function treeOncheck(e,treeId,treeNode){
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	nodes = zTree.getCheckedNodes(true);
	var values="";
	var names = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		var id=nodes[i].id+"";
		if(id.indexOf(":")!=-1){
			var ids=id.split(":");
			if(ids[0]=="type" || ids[1]=="root")
				continue;
		}
		names += nodes[i].name + ",";
		values += nodes[i].id + ",";
	}
	var paramName = treeId.replace("Tree","");
	if (names.length > 0 ) 
		names = names.substring(0, names.length-1);
	$("#"+paramName+"Names").attr("value", names);
	if (values.length > 0 ) 
		values = values.substring(0, values.length-1);
	$("#"+paramName+"Ids").attr("value", values);
}
