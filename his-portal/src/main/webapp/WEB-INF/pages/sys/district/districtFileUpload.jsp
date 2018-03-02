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
<div class="easyui-panel" id="panelEast" data-options="title:'行政区编码编辑',iconCls:'icon-form',border: true,fit:true">
	<div style="padding:10px">
		<form name="form"  id="editForm" method="post"   enctype="multipart/form-data" target="hidden_frame">
		<input type="hidden" id="id" name="id" value="${district.id  }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;">
				<tr style="height: 40px;display: none;" id="msgTrId" >
					<td align="center" colspan="7">&nbsp;
					&nbsp;&nbsp;&nbsp;<span id="msg"></span>	
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>请选择Excel文件：</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;" >
						<input type="file" name="fileEsign" id="fileFileName">
					</td>
				</tr>
			</table>
			<iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
			<div style="text-align:center;padding:5px">
				<a id="save" href="javascript:returnSource();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">导入</a>
				<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
/**
 * 导入功能
 */
function returnSource(){
	document.getElementById("msg").innerHTML="";
	document.getElementById("msgTrId").style.display="none";
	$.messager.progress({text:'正在导入数据，请稍后...',modal:true});
	$('#editForm').form('submit',{ 
		url : "<%=basePath %>baseinfo/district/districtFileUpload.action",
		success:function(data){ 
			
			if(data=="error"){
				$.messager.progress("close");
				$.messager.alert('提示','导入失败');
			}else{
				
				$.messager.progress("close");
				$.messager.alert('提示','导入成功');
				//实现刷新
				$('#list').treegrid('reload');
				$('#divLayout').layout('remove', 'east');
			}
			
		},error : function(result) {
			$.messager.progress("close");
			$.messager.alert('提示','保存失败！');
		}
	}); 
}
//关闭编辑窗口
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
</script>
</body>
</html>