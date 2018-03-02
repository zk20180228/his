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
		<div class="easyui-panel" id="panelEast" data-options="title:'栏目资源编码查看',iconCls:'icon-form',fit:true" style="width:580px;">
			<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${menuResource.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" style="margin:10px auto 0;">
				<tr>
					<td class="honry-lable">
						资源名称:
					</td>
					<td class="honry-view">
						${menuResource.mrcName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						资源别名:
					</td>
					<td class="honry-view">
						${menuResource.mrcAlias}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						资源类型:
					</td>
					<td class="honry-view">
						${menuResource.mrcType}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						资源排序:
					</td>
					<td class="honry-view">
						${menuResource.mrcOrder}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						资源说明:
					</td>
					<td class="honry-view">
						${menuResource.mrcDescription}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view"><fmt:formatDate value="${menuResource.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
				</tr>	
						
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		    <script type="text/javascript">
		    function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		    
		    </script>
		    
	</body>
</html>