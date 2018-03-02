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
	<div class="easyui-layout" style="padding:10px;background-color: white;" id="panelEast" >
		<form id="editForm"  method="post">
			<input type="hidden" id="id" name="personalAddress.id" value="${personalAddress.id }">
			<input type="hidden" id="parentCode" name="personalAddress.parentCode" value="${personalAddress.parentCode}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">所属分组：</td>
					<td class="honry-info">
					<input  id="belongGroupName" class="easyui-textbox"  name="personalAddress.belongGroupName" value="${personalAddress.belongGroupName}"  readonly="readonly" data-options="required:true" style="width:200px" missingMessage="请输入用户账户"/></td>
				</tr>
				<tr>
					<td class="honry-lable">姓名：</td>
					<td class="honry-info">
					<input  id="perName" class="easyui-textbox"  name="personalAddress.perName" value="${personalAddress.perName}"  data-options="required:true" style="width:200px" missingMessage="请输入用户账户"/></td>
				</tr>
				<tr>
					<td class="honry-lable">性别：</td>
					<td class="honry-info">
					<input class="easyui-combobox" id="perSex" name="personalAddress.perSex" value="${personalAddress.perSex }" data-options="required:true"  style="width:200px" /></td>
				</tr>
				<tr>
					<td class="honry-lable">生日：</td>
					<td class="honry-info">
					<input  id="perBirthday" name="personalAddress.perBirthday" value="<fmt:formatDate value='${personalAddress.perBirthday}' type='date' pattern="yyyy-MM-dd"/>"  class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">自定义码：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="perInputCode" name="personalAddress.perInputCode" value="${personalAddress.perInputCode }"  style="width:200px" missingMessage="请输入自定义码"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">移动电话：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="mobilePhone" name="personalAddress.mobilePhone" value="${personalAddress.mobilePhone}" data-options="required:true" style="width:200px" missingMessage="请输入移动电话"/></td>					
				</tr>
				
				<tr>
					<td class="honry-lable">办公电话：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="workPhone" name="personalAddress.workPhone" value="${personalAddress.workPhone}"  style="width:200px" missingMessage="请输入办公电话"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">电子邮箱：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="perEmail" name="personalAddress.perEmail" value="${personalAddress.perEmail}"  style="width:200px" missingMessage="请输入电子邮箱"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">家庭住址：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="perAddress" name="personalAddress.perAddress" value="${personalAddress.perAddress}"  style="width:300px" missingMessage="请输入家庭住址"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">备注：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="perRemark" name="personalAddress.perRemark" value="${personalAddress.perRemark }"  style="width:300px" missingMessage="请输入备注"/></td>					
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${personalAddress.id==null }">
					<a href="javascript:checkMes(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:checkMes(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
		$(function(){
			//性别下拉框渲染
			$('#perSex').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
				valueField : 'encode',
				textField : 'name',
				multiple : false
			});
		}); 
		
		
		
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(flag){
			$('#editForm').form('submit', {
				url : "<%=basePath%>oa/personalAddressList/savePersonalAddress.action",
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
					if(flag == 1){
						clear();
						$('#list').datagrid('reload');
					}else if(flag == 2){
						closeLayout('edit');
						$.messager.alert('提示',res.resMsg);
						close_alert();
					}
				}
			}); 
		}
		//清除所填信息
		function clear(){
			var id = '${personalAddress.id}';
			var parentCode = '${personalAddress.parentCode}';
			$('#editForm').form('reset');
			if(id){
				$('#id').val(id);
				$('#parentCode').val(id);
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
		
		//校验信息
		function checkMes(flag){
			if($("#workPhone").textbox('getValue')!="" && $("#workPhone").textbox('getValue')!=null && !isTelphoneNum($("#workPhone").textbox('getValue'))&&!isMobilephoneNum($("#workPhone").textbox('getValue'))){
				 $.messager.progress('close');	
				$.messager.alert('提示',"办公电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				return false;
			}
			if($("#mobilePhone").textbox('getValue')!="" && $("#mobilePhone").textbox('getValue')!=null && !isTelphoneNum($("#mobilePhone").textbox('getValue'))&&!isMobilephoneNum($("#mobilePhone").textbox('getValue'))){
				 $.messager.progress('close');	
				$.messager.alert('提示',"移动电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				return false;
			}
			
			var reg=/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
			if($("#perEmail").textbox('getValue')!="" && $("#perEmail").textbox('getValue')!=null ){
				 var flg=reg.test($("#perEmail").textbox('getValue')); 
				 if(!flg){
					 $.messager.alert('提示',"电子邮箱格式不正确！");
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					 return false;
				 }
			}
			submit(flag);
		}
	</script>
	</body>
</html>
