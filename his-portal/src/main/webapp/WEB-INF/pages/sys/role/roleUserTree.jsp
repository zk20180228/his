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
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north'" style="width: 100%; height:50px;border: 0px;margin-top: 10px;">
				<div style="margin-left: 10px;margin-top: 8px;">
					<table>
						<tr>
							<td>查询条件：</td>
							<td>
								<input type="text" id="searchParam" class="easyui-textbox" data-options="prompt:'用户名'" style="width: 150px;"/>
							</td>
							<td>
								<a id="search" href="javascript:void(0)" onClick="getTreeNode()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-top: 2px;">搜索</a>
							</td>
							<td>
								<a id="add" href="javascript:void(0)" onClick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="margin-top: 2px;">添加</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div data-options="region:'center'" style="width: 100%;border: 0px;margin-top: 5px;">
				<div style="padding-top: 10px;padding-bottom: 10px;">
					<ul id="tDtRole"></ul>
				</div>
			</div>
		</div>
<script type="text/javascript">
	var map = new Map();
	var deptId = "";
	var roleId= "${param.roleId }";
	var flag="${falgs}";
	/**
	 * 加载部门树
	 * @author  huangbiao
	 * @date 2016-03-30
	 * @version 1.0
	 */
	$(function(){
		$('#tDtRole').tree({   
			onlyLeafCheck:false,
			checkbox:true,
			url:"<%=basePath%>sys/treeUserRole.action",
			queryParams:{treeType:"C,I,F,PI,P,D,T,L,N,S,U,OP,O"},
			onDblClick:function(node){
				if(node.attributes.type=="0"){
		 			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				}else if(node.attributes.type=="1"){
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				}else if(node.attributes.type=="2"){
					if(node.checked){
						$(this).tree('uncheck',node.target);
					}else{
						$(this).tree('check',node.target);
					}
				}
			},
			onCheck:function(node, checked){
				var parentNode = $('#tDtRole').tree('getParent',node.target);
				if(parentNode != null){
					deptId = parentNode.id;
				}
			},
			onSelect:function(node){
				$(this).tree('check', node.target);
			}
		});
	});
		
	setTimeout(function(){
		bindEnterEvent('searchParam',getTreeNode,'easyui');
	},100);
	
	//展开树
	function expandAll(){
		$('#tDtRole').tree('expandAll');
	}
	//关闭树
	function collapseAll(){
		$('#tDtRole').tree('collapseAll');
	}
	    
	    function getTreeNode(){
	    	var searchParam = $('#searchParam').textbox('getValue');
	    	collapseAll();
	    	$.post("<%=basePath%>sys/getTreeParam.action",{userName:searchParam},function(data){
	    		if(data!=null&&data.length>0){
	    			for(var i=0;i<data.length;i++){
	    				//展开一级节点
	    				var type = data[i].deptType+"_"+data[i].deptType;
	    				var node1 = $('#tDtRole').tree('find',type);
	    				$('#tDtRole').tree('expand',node1.target);
	    			}
	    			setTimeout(function(){
	    				for(var i=0;i<data.length;i++){
		    				var deptId = data[i].deptId;
	    					var node2 = $('#tDtRole').tree('find',deptId);
		    				$('#tDtRole').tree('expand',node2.target);
		    			}
	    			},500);
	    		}
	    	});
	    }
	    
	    function add(){
	    	var selectObj = $('#tDtRole').tree('getChecked');
			if(selectObj != null && selectObj.length>0){
				var types = "";
				var depts = "";
				var ids = "";
				if(flag=="1"){
					if(selectObj.length!=1){
						$.messager.alert('提示','该功能只能选择一个，请重选择组套医师');
						close_alert();
						return ;
					}
					$.messager.confirm('确认提示','确定添加组套医师吗？',function(r){
						if(r){
							$('#hiddenDoc').val(selectObj[0].attributes.jobNo);
							$('#showDoc').val(selectObj[0].text);
							closeDialogUser('selectDoctor');
						}
					});
				}else{
					for(var i=0;i < selectObj.length;i++){
						if(selectObj[i].attributes.type == '0'){
							types+= selectObj[i].id+",";
						}
						if(selectObj[i].attributes.type == '1'){
							depts+= selectObj[i].id+",";
						}
						if(selectObj[i].attributes.type == '2'){
							ids+= selectObj[i].id+",";
						}
					}
					$.messager.confirm('确认提示','确定添加用户吗？',function(r){
						if(r){
							$.post("<c:url value='/sys/saveRoleUserTree.action'/>",{"roleId":roleId,"userId":ids,"deptId":depts,"typeIds":types},function(result){
								if(result=="success"){
									$('#roleaddUserdiv').dialog('destroy');
									queryLayout();
								}else{
									$.messager.alert("提示","操作失败!","info");
								}
							});
						}
					});
				}
				
				
			}else{
				$.messager.alert('提示','请选择需要添加的信息！','info');
				close_alert();
			}
	    }
		</script>
		
	</body>
</html>