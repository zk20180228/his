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
		<form id="editForm" method="post">
			<input type="hidden" id="id" name="blackList.id" value="${blackList.id}">
			<input type="hidden" id="accunt" value="0" >
			<input type="hidden" id="userAccuntHidden" value="${blackList.user_account }" >
			<input type="hidden" id="machineCodeHidden" value="${blackList.machine_code }" >
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">用户账户：</td>
					<td class="honry-info">
					<input  id="userAccunt" name="blackList.user_account" class="easyui-textbox"  value="${blackList.user_account }"  data-options="required:true" style="width:200px" missingMessage="请输入用户账户"/></td>
				</tr>
				<tr>
					<td class="honry-lable">设备码：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="machineCode" name="blackList.machine_code" value="${blackList.machine_code }" data-options="required:true"  style="width:200px" missingMessage="请输入设备码,多个请用逗号隔开"/></td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${blackList.id==null }">
					<a href="javascript:submits(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:submits(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
		$(function(){
		}); 
		
		//根据分类和账号校验信息是否存在
		function submits(flg){
			var userAccunt=$('#userAccunt').textbox('getValue');
			var machineCode=$("#machineCode").textbox('getValue');
			if(userAccunt!=null&&userAccunt!=""&&machineCode!=null&&machineCode!=""){
				var  id=$('#id').val();
				var type=0;
				if(id!=null&&id!=""){
					var oldUserAccunt=$('#userAccuntHidden').val();
					var oldMachineCode=$('#machineCodeHidden').val();
					if(oldUserAccunt==userAccunt&&oldMachineCode==machineCode){
						type=1;
						submit(flg);
					}
				}
				if(type==0){
					$.ajax({
						url:"<%=basePath%>mosys/whiteList/findDataByUserAccunt.action",
						data:{'userAccunt':userAccunt,'machineCode':machineCode,'id':id,'flg':'black'},
						type:"POST",
						success:function(data){
							if(data.resCode!='0'){
								if(data.resCode=='3'){
									$.messager.confirm('提示','该设备已经在白名单中，您确定要移至黑名单吗？',function(r){    
							  		    if (!r){  
							  		    	$("#accunt").val("0");
							  		    	return false ;
							  		    }else{
							  		    	$("#accunt").val("1");
							  		    	submit(flg);
							  		    }
									})
								}else{
									$.messager.alert('提示',data.resMsg);
									close_alert();
									$("#accunt").val("0");
									return false ;
								}
							}else{
								$("#accunt").val("0");
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
			var accunt=$("#accunt").val();
			$('#editForm').form('submit', {
				url : "<%=basePath%>mosys/blackList/saveBlackList.action",
				onSubmit : function(param) {
					param.flg=accunt;
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
			var id = '${blackList.id}';
			var userAccuntHidden = '${blackList.user_account }';
			var machineCodeHidden = '${blackList.machine_code }';
			$('#editForm').form('reset');
			$("#accunt").val("0");
			if(id){
				$('#id').val(id);
				$('#userAccuntHidden').val(userAccuntHidden);
				$('#machineCodeHidden').val(machineCodeHidden);
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
