<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>
<body>
	<div class="easyui-panel" id="panelEast">
		<form id="editForm" method="post">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr style="display: none">
					<td class="honry-lable">menuAlias：</td>
	    			<td class="honry-info">
	    				<input type="text" name="menuAlias" value="${menuAlias}"/></td>
				</tr>
				<tr style="display: none">
					<td class="honry-lable">就诊卡号：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" id="a_idcardNo" name="idcardNo" data-options="readonly:'true' "
	    					style="width: 200"/></td>
				</tr>
				<tr>
	    			<td class="honry-lable">原密码：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" type='password' id="e_pwd" name="oldpwd" 
	    					required="true" style="width: 200"/></td>
	    		</tr>
				<tr>
	    			<td class="honry-lable">新密码：</td>
	    			<td class="honry-info">
	    				 <input id="password" name="nowpwd" type="password" class="easyui-textbox" 
	    				 	required="true"  style="width: 200"/></td>
    				
	    		</tr>
				<tr>
	    			<td class="honry-lable">密码确认：</td>
	    			<td class="honry-info">
	    				 <input name="reNowpwd" type="password" class="easyui-textbox" required="true"  
	    				 	invalidMessage="两次输入密码不匹配"  
							validType="eqPassword['#editForm input[name=nowpwd]']"
							style="width: 200"/></td>
	    		</tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:closeDialog();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
		</form>	
	</div>
<script type="text/javascript">
//密码二次验证
$.extend($.fn.validatebox.defaults.rules, { 
	eqPassword : { 
		validator : function(value, param) {
		return value == $(param[0]).val(); 
		}, 
		message : '密码不一致！' 
	} 
});

$(function(){
	var cardno=$('#idcardNo').textbox('getValue');
	$('#a_idcardNo').val(cardno);
	
});



//表单提交
function submit(){ 
	
	$('#editForm').form('submit',{  
		url:'<%=basePath%>finance/outAccount/uppwdAccount.action',
        onSubmit:function(){
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			var pwd=$('#password').val(); 
        	if(pwd.length!=6){
        		$.messager.alert('友情提示','新密码必须为6位!');	
        		setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
        		return false;
        	}
        	var oldpwd=$('#e_pwd').val(); //原密码
        	if(pwd==oldpwd){
        		$.messager.alert('友情提示','原密码与新密码相同,请重新输入新密码!');	
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        		return false;
        	}
        },  
        success:function(data){  
        	if(data=='error'){
        		$.messager.alert('提示','原密码不匹配，修改失败!','warning');	
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        	}else{
        		$.messager.alert('提示','修改成功!');	
				window.location.reload();
        	}
        },
		error : function(data) {
			$.messager.alert('提示','添加失败!');	
		}							         
    }); 
}	    
</script>
</body>
</html>