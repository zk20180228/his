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
	<div style="padding: 10px" id="panelEast" >
		<input type="hidden" id="id" name="id" value="${role.id }">
		<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
		
			<tr>
				<td class="honry-lable">
					父级名称:
				</td>
				<td class="honry-view" id="parentRoleId">
					
				</td>
			<tr>
				<td class="honry-lable">
					角色名称:
				</td>
				<td class="honry-view">
					${role.name }
				</td>
			</tr>
			<tr>
				<td class="honry-lable">
					角色别名:
				</td>
				<td class="honry-view">
					${role.alias }
				</td>
			</tr>
			<tr>
				<td class="honry-lable">
					角色描述:
				</td>
				<td class="honry-view">
					${role.description }
				</td>
			</tr>
			<tr>
				<td class="honry-lable">
					是否停用:
				</td>
				<td class="honry-view">
					<c:choose>
						<c:when test="${role.stop_flg eq '1'}">
						是
						</c:when>
						<c:otherwise>
						否
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td class="honry-lable">
					创建时间:
				</td>
				<td class="honry-view"><fmt:formatDate value="${role.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
			</tr>
		</table>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		</div>
	</div>
	<script>
	$(function(){
		$.ajax({
			url: "<c:url value='/sys/getRole.action'/>?id="+"${role.parentRoleId }",
			type:'post',
			success: function(dataObj) {
				$('#parentRoleId').text(dataObj.description);
			}
		});
	});
	function closeLayout() {
		$('#divLayout').layout('remove','east');
	}
	</script>
</body>
</html>