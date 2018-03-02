<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;	
		}
	</style>
<body>
	<div style="padding:10px" id="panelEast" >
		<form id="editForm" enctype="multipart/form-data" method="post">
			<input type="hidden" id="id" name="mobileBlackList.id" value="${mobileBlackList.id}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">手机号：</td>
					<td class="honry-info">
					<input class="easyui-numberbox" id="mobileNum" name="mobileBlackList.mobileNum" value="${mobileBlackList.mobileNum }" data-options="required:true" style="width:200px" missingMessage="请输入手机号码"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">类型：</td>
					<td class="honry-info">
						<input id="types" class="easyui-combobox" name="mobileBlackList.type" value="${mobileBlackList.type}" data-options="required:true" missingMessage="请选择类型"  style="width:200px"> 
					</td>
				</tr>
				<tr>
	    			<td class="honry-lable">备注：</td>
	    			<td class="honry-info"><input class="easyui-textbox" type="text" id="mobileRemark" name="mobileBlackList.mobileRemark" value="${mobileBlackList.mobileRemark}" style="width:200px" /></td>
	    		</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${mobileBlackList.id==null }">
					<a href="javascript:submits(1);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:submits(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
		$(function(){
			//类型
			$('#types').combobox({
				valueField : 'id',	
				textField : 'value',
				editable : false,
				data : [{id : 1, value : '业务审批'},{id : 2, value : '日程安排'},{id : 3, value : '文章管理'}]
			});
		})
 		
			//根据分类和账号校验信息是否存在
		function submits(flg){
			var mobileNum=$('#mobileNum').numberbox('getValue');
			var type=$("#types").combobox('getValue');
			var typeName=$("#types").combobox('getText');
			var check='0';
			if(mobileNum!=null&&mobileNum!=""&&type!=null&&type!=""){
				var id = '${mobileBlackList.id }'
				if(id!=null&&id!=""){
					var oldMobileNum = '${mobileBlackList.mobileNum }'
					var oldType = '${mobileBlackList.type }'
					if(oldMobileNum==mobileNum&&oldType==type){
						check='1';
						submit(flg);
					}
				}
				if('0'==check){
					$.ajax({
						url:"<%=basePath%>mosys/cellPhoneBlackList/checkExist.action",
						async:false,
						cache:false,
						data:{'mobileNum':mobileNum,type:type},
						type:"POST",
						success:function(data){
							if(data.resCode=='1'){
								$.messager.alert('提示','该手机号在'+typeName+'类型下已经存在！');	
		  						close_alert();
		  						return;
							}else{
								submit(flg);
							}
						}
					});
				}
				
	    	}else{
	    		$.messager.alert('提示信息','验证没有通过,不能提交表单!');
				close_alert();
				return false ;
	    	}
		}
	
	
	
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(flag){
			$('#editForm').form('submit', {
				url : "<%=basePath%>mosys/cellPhoneBlackList/saveCellPhoneBlack.action",
				onSubmit : function() {
					if(!$('#editForm').form('validate')){
						$.messager.alert('提示信息','验证没有通过,不能提交表单!');
						close_alert();
						return false ;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success:function(data){ 
					$.messager.progress('close');
					var res = eval("(" + data + ")");
					if (res.resCode == "0") {
						if(flag == 1){
							clear();
							$('#list').datagrid('reload');
						}else if(flag == 2){
							closeLayout('edit');
							$.messager.alert('提示',res.resMsg);
							close_alert();
						}
					}else {
						$.messager.alert('提示',res.resMsg);
					}
				},
				error : function(data) {
					$.messager.progress('close');
				}
			}); 
		}
		//清除所填信息
		function clear(){
			var id = '${mobileBlackList.id }';
			$('#editForm').form('reset');
			if(id){
				$('#id').val(id);
			}
		}
		
		/* 
		* 关闭界面
		*/
		function closeLayout(flag){
			$('#divLayout').layout('remove','east');
			if(flag == 'edit'){
				$('#list').datagrid('reload');
			}
		}
		
	</script>
	</body>
</html>
