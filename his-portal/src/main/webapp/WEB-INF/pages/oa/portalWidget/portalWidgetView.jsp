<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>组件查看</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body style="margin: 0px;padding: 0px">
		<div class="easyui-panel"  id="panelEast" data-options="title:'查看',iconCls:'icon-form',fit:true,border:false">
			<div style="padding: 5px">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
					<tr>
						<td class="honry-lable">组件编码：</td>
						<td class="honry-view">${portalWidget.id }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">组件名称：</td>
						<td class="honry-view">${portalWidget.name}&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">组件地址：</td>
						<td class="honry-view">${portalWidget.url }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">查看地址：</td>
						<td class="honry-view">${portalWidget.viewUrl }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">列表地址：</td>
						<td class="honry-view">${portalWidget.moreUrl }&nbsp;</td>
					</tr>
					<tr>
					<td class="honry-lable">组件状态：</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${portalWidget.status eq '1'}">
							<span>禁用</span>
							</c:when>
							<c:otherwise>
							<span>正常</span>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
					<tr>
						<td class="honry-lable">创建时间：</td>
						<td class="honry-view"><fmt:formatDate value="${portalWidget.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
					</tr>
				</table>
			</div>
			<div style="text-align:center;padding:5px">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
		</div>
			<script>
			/**
			 * 关闭查看窗口
			 * @author  liujinliang
			 * @date 2015-5-22 10:53
			 * @version 1.0
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			
		</script>
	</body>
</html>