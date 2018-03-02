<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div class="easyui-panel" id="panelEast" data-options="title:'业务变更查看',iconCls:'icon-form'">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						主体名称:
					</td>
					<td class="honry-view">
						${change.subjectName }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						表名:
					</td>
					<td class="honry-view">
						${change.tableName }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						变更属性:
					</td>
					<td class="honry-view">
						${change.column }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						变更属性名:
					</td>
					<td class="honry-view">
						${change.columnName }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						旧值:
					</td>
					<td class="honry-view">
						${change.oldValue }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						新值:
					</td>
					<td class="honry-view">
						${change.newValue }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						会话ID:
					</td>
					<td class="honry-view">
						${change.SessionID}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						IP:
					</td>
					<td class="honry-view">
						${change.IP }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建人:
					</td>
					<td class="honry-view">
						${change.createUser }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${change.createTime }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建部门:
					</td>
					<td class="honry-view">
						${change.createDept }
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