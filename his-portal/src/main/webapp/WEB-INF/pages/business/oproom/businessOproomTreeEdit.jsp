<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
      	<div align="center" class="easyui-panel" style="padding:10px">
		<form id="treeEditForm" method="post" >
			<input type="hidden" id="id" name="businessOproom.id" value="${businessOproom.id }">
			<input type="hidden" id="createUser" name="businessOproom.createUser" value="${businessOproom.createUser }">
			<input type="hidden" id="createDept" name="businessOproom.createDept" value="${businessOproom.createDept }">
			<input type="hidden" id="createTime" name="businessOproom.createTime" value="${businessOproom.createTime }">
			<input type="hidden" id="stop_flg" name="businessOproom.stop_flg" value="${businessOproom.stop_flg }">
			<input type="hidden" id="del_flg" name="businessOproom.del_flg" value="${businessOproom.del_flg }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
	    			<td>房间代码:</td>
	    			<td><input class="easyui-textbox" type="roomId" id="name" name="businessOproom.roomId" value="${businessOproom.roomId }" data-options="required:true"/></td>
	    		</tr>
	    		<tr>
	    			<td>房间名称:</td>
	    			<td><input class="easyui-textbox" type="text" id="roomName" name="businessOproom.roomName" value="${businessOproom.roomName }" data-options="required:true"/></td>
	    		</tr>
	    		<tr>
	    			<td>助记码:</td>
	    			<td><input class="easyui-textbox" type="text" id="inputCode" name="businessOproom.inputCode" value="${businessOproom.inputCode }" /></td>
	    		</tr>
	    		<tr>
	    			<td>是否有效:</td>
	    			<td><input type="text" id=validFlag name="businessOproom.validFlag" value="${businessOproom.validFlag }" data-options="required:true" editable="false"/></td>
	    		</tr>
	    		<tr>
	    			<td>所属科室:</td>
	    			<td><input class="easyui-combobox"id="deptCode" name="businessOproom.deptCode" value="${businessOproom.deptCode }" data-options="required:true" editable="false"/></td>
	    		</tr>
				<tr>
					<td colspan="2" align="center">
   					<shiro:hasPermission name="${menuAlias}:function:save"> 
   					<a href="javascript:void(0)" data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitTreeForm()">保存</a>
   					</shiro:hasPermission>
   					<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton" onclick="clearTreeForm()">清除</a>
   					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
   					</td>
				</tr>
			</table>	
		</form>
	</div>
	<script type="text/javascript">
		$(function(){
			//是否有效状态
			$('#validFlag').combobox({    
				url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=operatTableEffective',
				valueField:'encode',    
				textField:'name'
			});
			
			if($('#tDt').tree('getSelected').attributes.isdept=='1'){
				//所属科室
				$('#deptCode').combobox({    
					url : '<%=basePath %>operation/oproom/queryOperationDept.action',
					valueField:'id',
					textField:'deptName'
					
				});
			}else if($('#tDt').tree('getSelected').attributes.isdept=='Y'){
				$('#deptCode').combobox({    
					url : '<%=basePath %>operation/oproom/queryOperationDept.action',
					valueField:'id',
					textField:'deptName'
				});
			}else{
				$('#deptCode').combobox({    
					data:[{id:$('#tDt').tree('getSelected').attributes.pid,value:$('#tDt').tree('getSelected').attributes.pname}],
					valueField:'id',
					textField:'value'
				});
			}
			
		});
	
	//提交验证
	function submitTreeForm(){
					$('#treeEditForm').form('submit', {
						url : '<%=basePath %>operation/oproom/businessOproomTreeInfo.action',
						data:$('#treeEditForm').serialize(),
				        dataType:'json',
						onSubmit : function() {
							if(!$('#treeEditForm').form('validate')){
								$.messager.show({  
							         title:'提示信息' ,   
							         msg:'验证没有通过,不能提交表单!'  
							    }); 
							       return false ;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});
						},
						success : function(data) {
							if(data=="save"){
								$.messager.alert("提示","房间已存在，添加失败!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else if(data=="upd"){
								$.messager.alert("提示","房间已存在，修改失败!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
							$.messager.progress('close');
							closeDialog();
							refresh();
							$("#list").datagrid("reload");
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert("提示","操作失败！");	
						}
					}); 
		
	}
	//清除所填信息
	function clearTreeForm(){
		$('#treeEditForm').form('reset');
	}
	
</script>	
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</body>

</html>