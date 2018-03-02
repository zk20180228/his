<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历查看</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false"  fit=true align="center">
			<form id="saveForm" method="post" style="border: 1px solid #95b8e7;width: 600px;height: 200px;border-top:0 !important;" class="changeskin">
				<table>
					<tr >
						<td align="center">
							提交一般纸质病历时间距出院日期
							<input type="text" class="easyui-numberbox" style="width: 90px" name="simple" id="simple" value="${simple}" data-options="required:true,min:0"></input>
							天算晚回病历
						</td>
					</tr>
					<tr >
						<td align="center">
							提交死亡纸质病历时间距出院日期
							<input type="text" class="easyui-numberbox" style="width: 90px" name="death" id="death" value="${death}" data-options="required:true,min:0"></input>
							天算晚回病历
						</td>
					</tr>
					<tr >
						<td align="center">
							<a id="btnSave" href="javascript:save();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
/**
* 保存
* @version 1.0
*/
function save(){
	$.messager.confirm('确认', '是否保存?', function(res) {//提示是否删除
		if(res){
			$('#saveForm').form('submit',{
				url : '<%=basePath%>emrs/lateEmrs/saveLateEmrs.action',
				onSubmit : function() {
					var isValid = $(this).form('validate');
					if (!isValid){
						$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
						$.messager.alert('提示',"请核对信息!");
						setTimeout(function(){$(".messager-body").window('close');},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
				},
				success : function(data) {
					$.messager.progress('close');
// 					window.location.reload();
					if(data == 'success'){
						$.messager.alert('提示',"保存成功!");
					}
				}
			});
		}
	});
}
</script>
</html>