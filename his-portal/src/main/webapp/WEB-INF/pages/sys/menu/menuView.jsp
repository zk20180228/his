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
	<div class="easyui-panel" id="panelEast" data-options="title:'栏目查看',iconCls:'icon-form',fit:true" style="border:0">
		<div>
			<input type="hidden" id="id" name="id" value="${menu.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
				<tr>
					<td class="honry-lable">上级栏目:</td>
					<td class="honry-view" id="parentId">${menu.parent }</td>
				</tr>
				<tr>
					<td class="honry-lable">栏目名称:</td>
					<td class="honry-view">${menu.name }</td>
				</tr>
				<tr>
					<td class="honry-lable">栏目别名:</td>
					<td class="honry-view">${menu.alias }</td>
				</tr>
				<tr>
					<td class="honry-lable">栏目类型:</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${menu.type eq '1'}">
							系统栏目
							</c:when>
							<c:when test="${menu.type eq '2'}">
							一般栏目
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">栏目所属:</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${menu.belong == 1}">
								平台
							</c:when>
							<c:when test="${menu.belong == 2}">
								移动端
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">栏目功能:</td>
					<td class="honry-view">${menu.menufunction.mfName }</td>
				</tr>
				<tr>
					<td class="honry-lable">子菜单标志:</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${menu.haveson eq '1'}">
							是
							</c:when>
							<c:otherwise>
							否
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">链接:</td>
					<td class="honry-view">${menu.url }</td>
				</tr>
				<tr>
					<td class="honry-lable">参数:</td>
					<td class="honry-view">${menu.parameter }</td>
				</tr>
				<tr>
					<td class="honry-lable">图标:</td>
					<td class="honry-view">${menu.icon }</td>
				</tr>
				<tr>
					<td class="honry-lable">说明:</td>
					<td class="honry-view">${menu.description }</td>
				</tr>
				<tr>
					<td class="honry-lable">打开方式:</td>
					<td class="honry-view">${menu.openmode }</td>
				</tr>
				<tr>
					<td class="honry-lable">栏目排序:</td>
					<td class="honry-view">${menu.levelOrder }</td>
				</tr>
				<tr>
					<td class="honry-lable">审核标志:</td>
					<td class="honry-view">${menu.needcheck }</td>
				</tr>
				<tr>
					<td class="honry-lable">
						停用标志:
					</td>	
					<td class="honry-view">
						<c:choose>
							<c:when test="${menu.stop_flg eq '1'}">
							是
							</c:when>
							<c:otherwise>
							否
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${menu.haveson eq '1'}">
				<tr>
					<td class="honry-lable">栏目资源:</td>
			    	<td>已拥有的资源:<br>
						<select multiple="multiple" id="selectBut" name="butName" style="width:90%;height:160px;">
							<c:forEach var="list" items="${mbList }">
								<option  value="${list.alias }" disabled="disabled">${list.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				</c:if>
				<tr>
				<td class="honry-lable">
					创建时间:
				</td>
				<td class="honry-view"><fmt:formatDate value="${menu.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
			</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	<script>
	$(function(){
		$.ajax({
			url: "<c:url value='/sys/getMenuById.action'/>?id=${menu.parent }&menuAlias=${menuAlias}",
			type:'post',
			success: function(parent) {
				$('#parentId').text(parent.name);
			}
		});
	});
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
	</script>
</body>
</html>