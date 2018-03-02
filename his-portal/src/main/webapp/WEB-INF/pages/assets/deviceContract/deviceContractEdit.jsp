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
	<div class="easyui-panel" id = "panelEast" data-options="title:'合同上传',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="baseUploadForm" method="post" enctype="multipart/form-data" >
				<input type="hidden" name="id" value="${deviceContractId }" >
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td>
							<input id="fileBaseName" name="fileBase"class="easyui-filebox" style="width:200px"></input>
							<a href="javascript:void(0)" name="filePhoto" onclick="upload()" class="easyui-linkbutton" iconCls="icon-disk_upload">上传合同</a>
						</td>
					</tr>
				</table>
			    	<table id="baseFile" class="easyui-datagrid"  data-options="method:'post',idField: 'id',striped:true,border:false,fitColumns:true,singleSelect:true,selectOnCheck:true,fit:true">
						<thead>
							<tr>
								<th data-options="field:'foreignCode',hidden:true"></th>
								<th data-options="field:'fileKind',hidden:true"></th>
								<th data-options="field:'loadStatus',hidden:true"></th>
								<th data-options="field:'fileName', width : '40%'">文件名</th>
								<th data-options="field:'memo',editor:{type:'textbox'}, width : '40%'">备注</th>
								<th data-options="field:'filePath',hidden:true">路径</th>
							</tr>
						</thead>
					</table>
				</form>
				<div style="text-align:center;padding:50px">
<!-- 			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a> -->
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script type="text/javascript">
 $(function(){
	$('#fileBaseName').filebox({
		buttonText: '选择文件',
	});
});


 	// 清除页面填写信息
	function clear(){
		$('#editForm').form('clear');
	}
   //关闭页面
	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
	//设备合同文件上传
	function upload(){
		var fileBaseName= $('#fileBaseName').filebox('getValue');
		if(fileBaseName!=''){
			$('#baseUploadForm').form('submit', {
				url : "<%=basePath %>assets/deviceContract/devConUpload.action",
				queryParams:{filePath:$('#fileBaseName').filebox('getValue')},
				success : function(data) {
					if(data!=""&&data!=null){
						var dataMap = eval("("+data+")");
						var fullFileName = dataMap.fileServerURL+dataMap.fileName;
						$("#baseFile").datagrid("appendRow",{
							fileKind:2,
							fileName:fileBaseName,
							filePath:fullFileName,
							loadStatus:'true',
						});
						editIndex = $("#baseFile").datagrid('getRows').length-1;
				 	 	$("#baseFile").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
					}else{
						$.messager.alert('提示',"上传的文件格式不对！");
					}
				},
				error : function(data) {
					$.messager.alert('提示',"上传失败！");
				}
			 });
		}else{
			$.messager.alert('提示',"请选择要上传的文件！");
		}
	}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>