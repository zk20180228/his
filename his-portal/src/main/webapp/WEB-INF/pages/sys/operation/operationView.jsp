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
	<div class="easyui-panel" id="panelEast" data-options="title:'操作日志查看',iconCls:'icon-form',border:false,fit:true">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						操作用户:
					</td>
					<td class="honry-view">
						${operation.user.name }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作行为:
					</td>
					<td class="honry-view">
						${operation.action }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作部门:
					</td>
					<td class="honry-view">
						${operation.deptId }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作栏目:
					</td>
					<td class="honry-view">
						${operation.menuId }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作sql:
					</td>
					<td class="honry-view">
						${operation.sql }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作表:
					</td>
					<td class="honry-view">
						${operation.table }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						目标编号:
					</td>
					<td class="honry-view">
						<c:forTokens items="${operation.targetId }" delims="," var="target">
						   <c:out value="${target}"/>,
						</c:forTokens>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作时间:
					</td>
					<td class="honry-view">
						${operation.time }
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	<script>
		function closeLayout() {
			$('#divLayout').layout('remove', 'east');
		}
	</script>
</body>
</html>