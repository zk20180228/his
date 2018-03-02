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
			<div style="width: 95%; height:50px;border: 1px solid #95b8e7;margin-left: 10px;margin-top: 10px;">
				<div style="margin-top: 8px;">
					<table>
						<tr>
							<td>查询条件：</td>
							<td>
								<input type="text" id="searchParam" class="easyui-textbox" data-options="prompt:'用户名'" style="width: 150px;"/>
							</td>
							<td>
								<a id="search" href="javascript:void(0)" onClick="getTreeNode()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="height:25px;margin-top: 2px;">搜索</a>
							</td>
							<td>
								 <a id="add" href="javascript:void(0)" onClick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="height:25px;margin-top: 2px;">添加</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div style="width: 95%;border: 1px solid #95b8e7;margin-left: 10px;margin-top: 5px;">
				<div style="padding-top: 10px;padding-bottom: 10px;">
					<ul id="tDtRole"></ul>
				</div>
			</div>
		
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>    
		<script type="text/javascript">
		var map = new Map();
		var deptId = "";
		var isLeaf =false;
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
		   		queryParams: {
		   			treeType: 'F'
		   		},
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
		   		onLoadSuccess:function(node, data){
	   				expandAll();
		   		},
		   		onCheck:function(node, checked){
		   			var parentNode = $('#tDtRole').tree('getParent',node.target);
		   			if(parentNode!=null){
			   			deptId = parentNode.id;
		   			}
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
			$("#tDtRole").tree("search", [searchParam,true]);
	    }
	    
	    function add(){
	    	var selectObj = $('#tDtRole').tree('getChecked');
	    	if(selectObj[0].text=='财务'){
	    		for(var i=0; i<selectObj.length; i++){
	    			var children = $('#tDtRole').tree('getChildren',selectObj[i].target);
	    			var leaf = $('#tDtRole').tree('getChildren',children.target);
	    			if($('#tDtRole').tree('isLeaf',leaf.target)){
	    				isLeaf = true;
	    				break ;
	    			}
	    		}
	    		if(isLeaf==false){
	    			$.messager.alert('提示','请至少选择一名具体的员工！');
	    			setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    		return;
	    		}
	    	}
	    	
			if(selectObj != null&&selectObj.length>0){
				$.messager.confirm('确认提示','确定添加用户吗？',function(r){
					if(r){
						var name = '';//列表中已存在的员工
						var secondRoots = true;//第二级节点
						for(var i=0;i<selectObj.length;i++){
							if(selectObj[i].id=='F_F'){
								continue;
							}
							var parentNode = $('#tDtRole').tree('getParent',selectObj[i].target);
							if(parentNode.text=='财务'){
								continue;
							} 
							secondRoots = false;
							var rows = $('#yxygList').datagrid('getRows');							
							var id = 0;
							for(var j=0;j<rows.length;j++){
								if(selectObj[i].attributes.employeeId==rows[j].id){
									id=1;
									if(name!=''){
										name=name+'、'+selectObj[i].text;
									}else{
										name=selectObj[i].text;
									}									
								}
							}
							if(id==1){//列表中已存在该员工，避免重复添加
								continue;
							}
							$('#yxygList').datagrid('appendRow',{
								jobNo:selectObj[i].attributes.jobNo,
								deptName:parentNode.text,
								name:selectObj[i].text,
								id:selectObj[i].attributes.employeeId,
								flag:1
							});													
						}
						if(secondRoots == true){
							$.messager.alert('提示','请至少选择一名具体的员工！');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
				    		return;
						}
						$('#roleaddUserdiv').window('close');
						if(name != ''){
							$.messager.alert('提示','员工'+name+'在列表中已存在！');
						}	
					}
				});
			}else{
				$.messager.alert('提示','请选择需要添加的信息！','info');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
	    }
		</script>
		
	</body>
</html>