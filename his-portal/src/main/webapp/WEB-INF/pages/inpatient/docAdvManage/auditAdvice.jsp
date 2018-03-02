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
			<div style="margin:18px 30px;position:absolute;">
			<form id="editForm" method="post">			
				<table style="width: 100%;" cellSpacing=10px cellPadding=10px border=0>					
					<tr style="padding: 5px 5px 5px 5px;">
						<input type="hidden" id="auditHidden" value="0"/>
						 <td style="border:none;"><span style="margin-left:40px;">通过：<input type="radio" id="passAudit" onclick="javascript:onclickBoxgzA()"  name="passAudit" /></span></td>					
						 <td style="border:none;"><span>不通过：<input type="radio" id="unPassAudit" onclick="javascript:onclickBoxgzB()"  name="unPassAudit" /></span></td>
					</tr>
				</table>
				<div><textarea id="adExplain" name="adExplain" cols="36" rows="4"></textarea></div>			
				<div style="margin:27px 97px;position:absolute;width:70%;" >		    
			    	<a id="save" href="javascript:submitAudit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>&nbsp;&nbsp;&nbsp;&nbsp;
			    	<a href="javascript:closeAuditLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			    </div>
			</form>						
			</div>
		</div> 		

</div>
<script type="text/javascript">
		//单选按钮赋值
		$("#passAudit").attr('checked', true);
		$("#adExplain").attr('disabled',true); 
		function onclickBoxgzA(){			
			$("#unPassAudit").attr('checked', false);
			$("#unPassAudit").attr('checked', false);
			$("#auditHidden").val(0);
			$("#adExplain").attr('disabled',true); 
			$("#adExplain").val('');		
		}
		function onclickBoxgzB(){			
			$("#passAudit").attr('checked', false);		
			$("#passAudit").attr('checked', false);
			$("#auditHidden").val(-3);				
			$("#adExplain").attr('disabled',false); 
		}

		//表单提交
		function submitAudit(){ 						
			var auditValue = $("#auditHidden").val(); 
			var adExplain = $("#adExplain").val();
			$.messager.confirm('确认','确认之后结果将无法更改，您确定选择吗?',function(r){    
			    if (r){    
					 var qq = $('#ttta').tabs('getSelected');				
					 var tab = qq.panel('options');
					 if(tab.title=='长期医嘱'){
						 var rowA = $('#infolistA').datagrid('getSelected');
						 $.ajax({ 
						    	url: '<%=basePath%>inpatient/docAdvManage/auditAdvice.action',	
						    	data:'auditValue='+auditValue+'&id='+rowA.id+'&adExplain='+adExplain,
						    	type:'post',
						        success:function(data){ 
						        	if(data=='success'){
						        		$('#adAuditData-window').window('close');
						        		$.messager.alert('提示',"审核成功！");
						        		setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										$('#infolistA').datagrid('reload');
									}
									else{
										$('#adAuditData-window').window('close');
										$.messager.alert('提示',"审核失败！");
									}
						        }										         
						  });
					 }else if(tab.title=='临时医嘱'){
						 var row = $('#infolistB').datagrid('getSelected');
						 $.ajax({ 
						    	url: '<%=basePath%>inpatient/docAdvManage/auditAdvice.action',	
						    	data:'auditValue='+auditValue+'&id='+row.id+'&adExplain='+adExplain,
						    	type:'post',
						        success:function(data){ 
						        	if(data=='success'){
						        		$('#adAuditData-window').window('close');
						        		$.messager.alert('提示',"审核成功！");
						        		setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										$('#infolistB').datagrid('reload');
									}
									else{
										$('#adAuditData-window').window('close');
										$.messager.alert('提示',"审核失败！");
									}
						        }										         
						  });
					 }					  
				}
			});
		}	
</script>
</body>
</html>