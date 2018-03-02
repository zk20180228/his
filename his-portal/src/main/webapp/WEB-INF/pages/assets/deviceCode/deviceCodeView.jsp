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
						办公用途编码:
					</td>
					<td class="honry-view">
						${deviceCode.officeCode}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						办公用途名称:
					</td>
					<td class="honry-view">
						${deviceCode.officeName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备分类编码:
					</td>
					<td class="honry-view">
						${deviceCode.classCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备分类名称:
					</td>
					<td class="honry-view">
						${deviceCode.className }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备条码号:
					</td>
					<td>
					${deviceCode.deviceNo }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备代码:
					</td>
					<td>
						${deviceCode.deviceCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备名称:
					</td>
					<td>
						${deviceCode.deviceName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						计量单位:
					</td>
					<td>
						${deviceCode.meterUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						采购单价(元):
					</td>
					<td>
						${deviceCode.purchPrice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库时间:
					</td>
					<td>
						${deviceCode.deviceDate }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						状态:
					</td>
					<td class="honry-view">
						<c:choose>
						<c:when test="${deviceCode.state eq '0'}">
							未打印
						</c:when>
						<c:otherwise>
							已打印
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${deviceCode.createTime }&nbsp;
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