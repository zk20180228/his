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
<title>医疗组变更</title>
</head>
<body style="margin: 0px; padding: 0px;">
			<div id="cc" class="easyui-layout" data-options="fit:true" >   
			    <div data-options="region:'center',border:false" style="padding-top: 20px" align="center">
			    	<form method="post" id="editFrom">
			    		<table>
				    		<tr>
				    			<td>
				    				变更原因：<input id="querydrug" name="medicalgroupChangeRecord.changeCause" class="easyui-textbox" value="${changeCause }">
				    				<input type="hidden" name="medicalgroupChangeRecord.id" value="${id }">
				    			</td>
				    		</tr>
				    		<tr style="height: 20px;"></tr>
				    		<tr>
				    			<td style="padding-left: 70px;">
				    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()" iconCls="icon-save">保存</a>
				    			</td>
				    		</tr>
				    	</table>
			    	</form>
			    </div>   
			</div>
			<script type="text/javascript">
				function save(){
					$('#editFrom').form('submit', {    
						url:'<%=basePath%>baseinfo/medicalgroupChange/updatemedicalgroupChange.action',
						success:function(data){
							if(data="success"){
								$.messager.alert('提示','修改成功！');
								$('#addMatKindinfo').dialog('close');
								$('#billSearchHzList').datagrid('reload');
							}
						}
					});
				}
			</script> 
</body>
</html>