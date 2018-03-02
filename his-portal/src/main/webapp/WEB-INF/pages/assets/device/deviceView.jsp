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
						办公用途:
					</td>
					<td class="honry-view">
						${device.officeName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备分类:
					</td>
					<td class="honry-view">
						${device.className }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备名称:
					</td>
					<td>
						${device.deviceName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						计量单位:
					</td>
					<td>
						${device.meterUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						所属仓库:
					</td>
					<td class="honry-view">
						${device.depotName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						采购单价(元):
					</td>
					<td>
						${device.purchPrice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库数量:
					</td>
					<td>
						${device.deviceNum }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						采购总价(元):
					</td>
					<td>
						${device.purchTotal }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						折旧年限:
					</td>
					<td>
						${device.depreciation }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库时间:
					</td>
					<td>
						${device.deviceDate }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						申报人工号:
					</td>
					<td>
						${device.applAcc }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						申报人姓名:
					</td>
					<td>
						${device.applName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						申报时间:
					</td>
					<td>
						${device.applDate }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库状态:
					</td>
					<td class="honry-view">
						<c:choose>
						<c:when test="${device.deviceState eq '0'}">
							草稿
						</c:when>
						<c:when test="${device.deviceState eq '1'}">
							申请
						</c:when>
						<c:when test="${device.deviceState eq '2'}">
							未批准
						</c:when>
						<c:otherwise>
							已入库
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${device.createTime }&nbsp;
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