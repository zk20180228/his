<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div>
		<div data-options="region:'center',tools:'#toolSMId'" style="width:100%;">
			<div id="depttreeDiv" style="width: 100%; height: 90%; overflow-y: auto;">
				<ul id="depttDt">数据加载中...</ul>
			</div>
			<div>
				<a href="javascript:OK()" class="easyui-linkbutton">确定</a>
				<a href="javascript:closeDialog()" class="easyui-linkbutton">取消</a>
			</div>
	    </div>   
	</div>

<script type="text/javascript">
	$(function(){
		$('#depttDt').tree({
			url : '<%=basePath %>material/matInoutDept/treeList.action',
			method:'get',
			lines : true,
			animate : true,
			//checked:true, 该节点是否被选中
			checkbox:true,
			cascadeCheck:false,
			onlyLeafCheck: false,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children) {
					s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
				}
				return s;
			},onClick:function(node) {//点击节点
				$('#depttDt').tree('check',node.target);
			},onBeforeCheck:function(node,checked){  //在勾选复选框之前触发
				/*if(node.id.length<32){
					return false;
				}  加入判断后,复选框无法选择           */
			}
		});
	});
	
	function OK(){
		tab = $('#tt').tabs('getSelected');
		index = $('#tt').tabs('getTabIndex',tab);
		var nodes = $('#depttDt').tree('getChecked');
		if(nodes.length == 0){
			$.messager.alert('警告','请选择具体科室!!');
			return false;
		}
			if(index==0){
				var obj=$('#list1').datagrid('getData');
				if(obj.rows.length>0){
					var onlyName='';
					for(var i = 0;i < nodes.length;i ++){
						for(var j = 0;j < obj.rows.length;j ++){
							if(nodes[i].text == obj.rows[j].objectDeptName){
								$.messager.alert('警告','科室'+nodes[i].text+'已存在');
								return false;
							}
						}
					}
				}
				$.each(nodes,function(i,n){
				$('#list1').edatagrid('appendRow',{
					objectDeptName:n.text,
					objectDeptType:n.attributes.deptType,
					objectDeptCode:n.attributes.deptCode
					});
				});
				closeDialog();
				}else{
					var obj=$('#list2').datagrid('getData');
					if(obj.rows.length>0){
						var onlyName='';
						for(var i = 0;i < nodes.length;i ++){
							for(var j = 0;j < obj.rows.length;j ++){
								if(nodes[i].text == obj.rows[j].objectDeptName){
									$.messager.alert('警告','科室'+nodes[i].text+'已存在');
									return false;
								}
							}
						}
					}
					$.each(nodes,function(i,n){
						$('#list2').edatagrid('appendRow',{
							objectDeptName:n.text,
							objectDeptType:n.attributes.deptType,
							objectDeptCode:n.attributes.deptCode
							});
						});
					closeDialog();
					}
			}
</script>
</body>
</html>