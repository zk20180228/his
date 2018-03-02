<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script>
function submit(){
	$('#editForm').form('submit',{
		url:"<%=basePath%>technical/essentialsItem/saveEditTecDeptItem.action",
		success:function(data){
			$.messager.alert('操作提示', '保存成功！');
			setTimeout(function(){
				closeDialog();
			},2000);
			$('#list2').datagrid('reload');
		},error:function(){
			$.messager.alert('操作提示', '保存失败！');
			setTimeout(function(){
				closeDialog();
			},2000);
		}
	});
}
//关闭
function closeDialog(){
// 	$('#newwindow').dialog('close');
	window.close();
}
if($('#tmpbookDate').val()){
	var tmpVal = $('#tmpbookDate').val()
	$('#bookDate').val(tmpVal.substring(0,19))
}
if($('#tmpreportDate').val()){
	var tmpVal = $('#tmpreportDate').val()
	$('#reportDate').val(tmpVal.substring(0,19))
}
</script>
</head>
<body>
	<div class="easyui-panel" id="panelEast" style="width:100%" >
		<div style="width:100%">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="id" name="item.id" value="${item.id}">
				<input type="hidden" id="deptCode" name="item.deptCode" value="${item.deptCode}">
				<input type="hidden" id="itemCode" name="item.itemCode" value="${item.itemCode}">
				<input type="hidden" id="unitFlag" name="item.unitFlag" value="${item.unitFlag}">
				<input type="hidden" id="classCode" name="item.classCode" value="${item.classCode}">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px" >
					<tr>
						<td class="honry-lable">预约地</td>
						<td>
							<input id="bookLocate" name="item.bookLocate" class="easyui-textbox" value="${item.bookLocate }" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">预约固定时间</td>
						<td>
							<input id="bookDate" class="Wdate" type="text" name="item.bookDate" value="${item.bookDate }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<input type="hidden" id="tmpbookDate" value="${item.bookDate }" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">执行地点</td>
						<td>
							<input id="executeLocate" name="item.executeLocate" class="easyui-textbox" value="${item.executeLocate}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">取报告时间</td>
						<td>
							<input id="reportDate" class="Wdate" type="text" name="item.reportDate" value="${item.reportDate }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<input type="hidden" id="tmpreportDate" value="${item.reportDate }" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">有创无创</td>
						<td>
							<input id="hurtFlag" name="item.hurtFlag" class="easyui-combobox"  value="${item.hurtFlag}" data-options="valueField:'id',textField:'text',data:[{'id':1,'text':'无效'},{'id':2,'text':'有效'}]" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">是否科内预约</td>
						<td>
							<input id="selfbookFlag" name="item.selfbookFlag" class="easyui-combobox"  value="${item.selfbookFlag}" data-options="valueField:'id',textField:'text',	data:[{'id':'1','text':'是'},{'id':'0','text':'否'}]" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">知情同意书</td>
						<td>
							<input id="reasonableFlag" name="item.reasonableFlag" class="easyui-combobox"  value="${item.reasonableFlag}" data-options="valueField:'id',textField:'text',data:[{'id':'1','text':'是'},{'id':'0','text':'否'}]" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">临床意义</td>
						<td>
							<input id="clinicMeaning" name="item.clinicMeaning" class="easyui-textbox" value="${item.clinicMeaning}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">标本</td>
						<td>
							<input id="sampleKind" name="item.sampleKind" class="easyui-textbox" value="${item.sampleKind}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">采样方法</td>
						<td>
							<input id="sampleWay" name="item.sampleWay" class="easyui-textbox" value="${item.sampleWay}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">标本单位</td>
						<td>
							<input id="sampleUnit" name="item.sampleUnit" class="easyui-combobox"  value="${item.sampleUnit}" data-options="valueField : 'encode',textField : 'name',url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=laboratorysample'" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">标本量</td>
						<td>
							<input id="sampleQty" name="item.sampleQty" class="easyui-textbox"  value="${item.sampleQty}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">容量</td>
						<td>
							<input id="sampleContainer" name="item.sampleContainer" class="easyui-textbox"  value="${item.sampleContainer}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">正常值范围</td>
						<td>
							<input id="scope" name="item.scope" class="easyui-textbox"  value="${item.scope}" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">设备类型</td>
						<td>
							<input id="machineType" name="item.machineType" class="easyui-combobox"  value="${item.machineType}" data-options="valueField : 'encode',textField : 'name',url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=sbtype'" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable">备注</td>
						<td>
							<input id="remark" name="item.remark" class="easyui-textbox"  value="${item.remark}" >
						</td>
					</tr>
				</table>
				<div style="text-align:center;padding:5px">
			<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:closeDialog();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
			</form>
		</div>
	</div>
</body>
</html>