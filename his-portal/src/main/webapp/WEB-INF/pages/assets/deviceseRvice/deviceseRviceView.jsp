<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<jsp:include page="/javascript/default.jsp"></jsp:include>

<body>
	<div id="panelEast" class="easyui-panel" data-options="title:'详细信息',iconCls:'icon-book',fit:true" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						办公用途名称:
					</td>
					<td class="honry-view">
						${deviceseRvice.officeName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备分类名称:
					</td>
					<td class="honry-view">
						${deviceseRvice.className }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备代码:
					</td>
					<td>
						${deviceseRvice.deviceCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备名称:
					</td>
					<td>
						${deviceseRvice.deviceName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						计量单位:
					</td>
					<td>
						${deviceseRvice.meterUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${deviceseRvice.createTime }&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
</body>
</html>