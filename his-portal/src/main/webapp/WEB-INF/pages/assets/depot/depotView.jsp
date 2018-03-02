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
						仓库编码:
					</td>
					<td class="honry-view">
						${depot.depotCode}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						仓库名称:
					</td>
					<td class="honry-view">
						${depot.depotName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						仓库地点:
					</td>
					<td class="honry-view">
						${depot.address }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						仓库管理员工号:
					</td>
					<td class="honry-view">
						${depot.manageAcc }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						仓库管理员姓名:
					</td>
					<td>
					${depot.manageName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系电话:
					</td>
					<td>
						${depot.phone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						停用标志:
					</td>
					<td class="honry-view">
						<c:choose>
						<c:when test="${advdrugnature.stop_flg eq '1'}">
							停用
						</c:when>
						<c:otherwise>
							启用
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${depot.createTime }&nbsp;
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