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
      	<div align="center" class="easyui-panel" style="padding:10px">
		<form id="treeEditForm" method="post" >
			<input type="hidden" id="id" name="mobileTypeManage.id" value="${mobileTypeManage.id }">
			<input type="hidden" id="accunt" value="0" >
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
	    			<td class="honry-lable">手机类别:</td>
	    			<td><input class="easyui-textbox" type="text" id="mobileCategory" name="mobileTypeManage.mobileCategory" value="${mobileTypeManage.mobileCategory}" data-options="required:true"/></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">备注:</td>
	    			<td><input class="easyui-textbox" type="text" id="mobileRemark" name="mobileTypeManage.mobileRemark" value="${mobileTypeManage.mobileRemark}" /></td>
	    		</tr>
				<tr>
					<td colspan="2" align="center">
   					<shiro:hasPermission name="${menuAlias}:function:save"> 
   					<a href="javascript:void(0)" data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitForm()">保存</a>
   					</shiro:hasPermission>
   					<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton" onclick="clearForm()">清除</a>
   					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
   					</td>
				</tr>
			</table>	
		</form>
	</div>
	<script type="text/javascript">
		$(function(){
		
			
		});
		//提交验证
		function submitForm(){
			var mobileCategory=$('#mobileCategory').textbox('getValue');
			if(mobileCategory!=null&&mobileCategory!=""){
				var id = '${mobileTypeManage.id }'
				var check='0';
				if(id!=null&&id!=""){
					var oldMobileCategory="${mobileTypeManage.mobileCategory}"
					if(oldMobileCategory==mobileCategory){
						var check='1';
						submit();
					}
				}
				if('0'==check){
					$.ajax({
						url:"<%=basePath%>mosys/whiteListManage/checkExist.action",
						async:false,
						cache:false,
						data:{'mobileCategory':mobileCategory},
						type:"POST",
						success:function(data){
							if(data.resCode=='1'){
								$('#mobileCategory').textbox('setValue','');
								$.messager.alert('提示','该手机类型已经存在，请重新填写！');	
		  						close_alert();
		  						return;
							}else if(data.resCode=='2'){
								$.messager.confirm('提示','该手机类型已经在黑名单中，您确定要移至白名单吗？',function(r){    
						  		    if (!r){  
						  		    	$("#accunt").val("0");
						  		    	return false ;
						  		    }else{
						  		    	$("#accunt").val("1");
						  		    	submit();
						  		    }
								})
							}else{
								$("#accunt").val("0");
								submit();
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
		
	//提交验证
	function submit(){
		var accunt=$("#accunt").val();
		$('#treeEditForm').form('submit', {
			url : '<%=basePath %>mosys/whiteListManage/saveWhiteListData.action',
			onSubmit : function(param) {
				param.flg=accunt;
				if(!$('#treeEditForm').form('validate')){
					$.messager.show({  
				         title:'提示信息' ,   
				         msg:'验证没有通过,不能提交表单!'  
				    }); 
				       return false ;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},
			success:function(data){ 
				 $.messager.progress('close');
				var res = eval("(" + data + ")");
				$.messager.alert("提示",res.resMsg);
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				closeDialog();
				reload();
			}
		}); 
		
	}
	//清除所填信息
	function clearForm(){
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