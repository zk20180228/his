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
	<table>
		<tr>
			<td>开始时间：</td>
			<td>
			<input id="beganTime" class="Wdate" type="text" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;">
			</input>
			</td>
		</tr>
		<tr>
			<td>结束时间：</td>
			<td>
			<input id="endTime" class="Wdate" type="text" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"></input></td>
		</tr>
		<tr>
			<td><a href="javascript:funsubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a></td>
			<td><a href="javascript:closeDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a></td>
		</tr>
	</table>
	
<script type="text/javascript">

	function funsubmit(){
		var beganTime=$('#beganTime').val();
		var endTime=$('#endTime').val();
		$('#tDt').tree({
			url : '<%=basePath %>material/wzadjustPrice/wzadjustPriceTree.action?beganTime='+beganTime+'&endTime='+endTime,
		});
		closeDialog();
	}
</script>
</body>
</html>