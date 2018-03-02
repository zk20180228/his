<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div class="easyui-layout" style="width:100%;height:100%;" fit=true>  
		<div id="addData-window" style="position:relative;">  
			<div style="margin:28px 30px;position:absolute;">
			<form id="editForm" method="post">			
				<table style="width: 100%;" cellSpacing=10px cellPadding=10px border=0>
					<tr style="padding: 5px 5px 5px 5px;">
						 <td style="border:none;">执行单名称：</td>
						 <td style="border:none;"><input id="billName" name="billName" class="easyui-textbox"/></td>
					</tr>
					<tr style="padding: 5px 5px 5px 5px;">
						 <td style="border:none;">备注：</td>
						 <td style="border:none;"><input id="mark" name="mark" class="easyui-textbox"/></td>
					</tr>
					<tr style="padding: 5px 5px 5px 5px;">
						 <td style="border:none;">项目执行单：</td>
						 <td style="border:none;"><input id="itemFlag" name="itemFlag" type='checkbox' class="easyui-checkbox"></td>
					</tr>
				</table>			
				<div style="margin:27px 70px;position:absolute;width:70%;" >		    
			    	<a id="save" href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;&nbsp;
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			    </div>
			</form>						
			</div>
		</div> 		

</div>
<script type="text/javascript">
		

		//表单提交
		function submit(){ 
			var billName=$("#billName").val();		
			var mark=$("#mark").val();
			var itemFlag=0;
			if($("#itemFlag").is(":checked")){
				itemFlag=1;				
			}
			if(billName==null||billName==""){
				$.messager.alert('提示',"执行单名称不能为空！");
				return;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
			 $.ajax({ 
		    	url: '<%=basePath%>inpatient/doctorAdvice/queryDocAdvExe.action',	
		    	data:'billName='+billName,
		    	type:'post',
		        success:function(data){ 
		        	$.messager.progress('close');	// 如果提交成功则隐藏进度条
		        	if(data=='success'){
			        	 $.ajax({							
								url:'<%=basePath%>inpatient/doctorAdvice/saveDocAdvExe.action',
								data:'billName='+billName+'&mark='+mark+'&itemFlag='+itemFlag,
								type:'post',
								success: function(data) {						
									if(data=='success'){
										$.messager.alert('提示',"添加成功！");
										window.location="<%=basePath%>inpatient/doctorAdvice/docAdvExeInfos.action?menuAlias=${menuAlias}";
									}
									else{
										$.messager.alert('提示',"添加失败！");
									}
								}
							});
		        	}else{
		        		$.messager.alert('提示',"执行单名称不能重复！");
		        	}
		        }										         
		    }); 
		}	
</script>
</body>
</html>