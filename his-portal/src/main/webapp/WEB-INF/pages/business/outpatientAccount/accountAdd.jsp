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
		<form id="addForm" method="post">
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
					<td class="honry-lable">账户名称：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" id="a_name" name="account.accountName"  
	    					style="width: 200"  required="true"  validType="length[0,10]" invalidMessage="不能超过10个字符！"/></td>
    			</tr>
				<tr>
	    			<td class="honry-lable">账户密码：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" type='password' id="a_pwd" name="account.accountPassword" 
	    					style="width: 200"/></td>
	    		</tr>
				<tr>
	    			<td class="honry-lable">账户类型：</td>
    				<td class="honry-info">
	    				<input class="easyui-combobox" id="a_type" name="account.accountType" data-options="editable:false" 
	    					style="width: 200"/></td>
	    		</tr>
				<tr>
					<td class="honry-lable">门诊单日消费限额：</td>
	    			<td class="honry-info">
	    				<input class="easyui-numberbox" id="a_daylimit" name="account.accountDaylimit"  data-options="precision:2" 
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
$(function(){
	var cardno=$('#idcardNo').textbox('getValue');
	$('#a_idcardNo').val(cardno);
	
	//初始化打印类型
	$('#a_type').combobox({    
		data:[{id:'0',value:'普通账户'},{id:'1',value:'记账账户'}],
		valueField:'id',    
		textField:'value'
	});
});

//表单提交
function submit(){ 

	$('#addForm').form('submit',{  
		url:'<%=basePath%>finance/outAccount/addAccount.action',
        onSubmit:function(){
			
			if (!$('#addForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			var pwd=$('#a_pwd').val(); 
			if(pwd!=null&&pwd!=""){
				if(pwd.length!=6){
					$.messager.alert('友情提示','密码必须为6位!');	
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
			}
			
			$.messager.progress({text:'保存中，请稍后...',modal:true});
        },  
        success:function(data){  
        	$.messager.progress('close');
        	if(data=='success'){
        		closeDialog();
        		$.messager.alert('友情提示','创建成功!');	
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        		reader();
        		$('#create').linkbutton('disable');
        	}
        },
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','添加失败!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}							         
    }); 
}	    


</script>
</body>
</html>