<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<body>
	<div id="chequePay" class="easyui-window" style="width:800px;height:500px;" data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false">
    	<form id="chequePayForm" action="savePwdAccount.action" method="post"> 
    		<input type="hidden" id="upaccoutId" name="account.id">
			<table title="支票支付信息">
					<tr>
		    			<td>开户单位:</td>
		    			<td><input class="easyui-textbox" type="text" id="unit"  data-options="required:true" style="width:290px" missingMessage="开户单位"/></td>
		    		</tr>
					<tr>
		    			<td>开户银行:</td>
		    			<td><input class="easyui-textbox" type="text" id="oldPassword"  data-options="required:true" style="width:290px" missingMessage="开户银行"/></td>
		    		</tr>
		    		<tr>
		    			<td>开户账号:</td>
		    			<td><input class="easyui-textbox" type="text" id="oldPassword"  data-options="required:true" style="width:290px" missingMessage="开户账号"/></td>
		    		</tr>
		    		<tr>
		    			<td>小票号:</td>
		    			<td><input class="easyui-textbox" type="text" id="oldPassword" name=""  data-options="required:true" style="width:290px" missingMessage="小票号"/></td>
		    		</tr>
					<tr>
						<td colspan="4" align="center">
							<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeWindow('#chequePay')">关闭</a>
		   					<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton" onclick="clearForm()">清除</a>
		   					<a href="javascript:void(0)" data-options="iconCls:'icon-add'" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	   					</td>
					</tr>
			</table>	
		</form>
		</div>
	</body>
<script type="text/javascript">
	//提交验证
	function submitForm(){
		$("#upaccoutId").val($("#accountId").val());
		var accountId = $("#accountId").val();
		var bool = $("#modifyPwdForm").form('validate');
		var oldPassword=$("#oldPassword").val();
		$.ajax({ 
	        type: "post", 
	        url: 'updatePwdAccount.action?oldPwd='+oldPassword+'&accountId='+accountId, 
	        async:false, 
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
		return;
	}
	//清除所填信息
	function clearForm(){
		$('#modifyPwdForm').form('clear');
	}
	//关闭窗口
	function closeWindow(winId){
		$(winId).window('close');  // close a window 
	}
</script>
</html>
