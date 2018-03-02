<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<body>
	<div id="modifyPwd" class="easyui-window" style="width:800px;height:500px;" data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false">
    	<form id="modifyPwdForm" action="${pageContext.request.contextPath}/patient/account/savePwdAccount.action" method="post"> 
    		<input type="hidden" id="upaccoutId" name="account.id">
			<table title="修改密码">
					<tr>
		    			<td>原密码:</td>
		    			<td><input class="easyui-textbox" type="password" id="oldPassword"  data-options="required:true" style="width:290px" missingMessage="原密码"/></td>
		    		</tr>
					<tr>
						<td>新密码:</td>
		    			<td><input class="easyui-validatebox" type="password" id="newPassword" name="account.accountPassword"  data-options="required:true"  style="width:290px" missingMessage="请输入新密码"/></td>
					</tr>
					<tr>
						<td>确认新密码:</td>
		    			<td><input class="easyui-validatebox" type="password" id="confirmPassword"  validType="eqPassword['#modifyPwdForm input[id=newPassword]']"  data-options="required:true" style="width:290px" missingMessage="请输入新密码"/></td>
					</tr>
					<tr>
						<td colspan="4" align="center">
						<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeWindow()">关闭</a>
	   					<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton" onclick="clearForm()">清除</a>
	   					<shiro:hasPermission name="${menuAlias}:function:add">
	   						<a href="javascript:void(0)" data-options="iconCls:'icon-add'" class="easyui-linkbutton" onclick="submitForm()">提交</a></td>
						</shiro:hasPermission>
					</tr>
			</table>	
		</form>
		</div>
<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {   
	    eqPassword : { validator : function(value, param) {  
		    return value == $(param[0]).val();   
		    },   
		    message : '密码不一致！'   
	    }   
    });
	//提交验证
	function submitForm(){
		$("#upaccoutId").val($("#accountId").val());
		var accountId = $("#accountId").val();
		var bool = $("#modifyPwdForm").form('validate');
		var oldPassword=$("#oldPassword").val();
		$.ajax({ 
			url: "<c:url value='/patient/account/updatePwdAccount.action'/>",
			data:{"oldPwd" : oldPassword,"accountId" : accountId},
	        type: "post", 
	        success: function(data){
	        	var retVal = eval("("+data+")");
	        	if(bool){
	    			if(retVal=="yes"){
	    				//alert("可以修改密码了！");
	    				$('#modifyPwdForm').submit();
	    			}else{
	    				$.messager.alert('提示',"输入的原密码不对！");
	    			}
	    			
	    		}
	        } 
		});
	}
	//清除所填信息
	function clearForm(){
		$('#modifyPwdForm').form('clear');
	}
	//关闭弹出窗口
	function closeWindow(){
		$('#modifyPwd').window('close');
	}
</script>
</body>
