<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;
		}
	</style>
</head>
	<body>
		<div  id="panelEast"  style="padding:10px">
			<table class="honry-table removeBorders"  cellpadding="0" cellspacing="0" border="0" style="width:100%">
				<tr>
					<td class="honry-lable">
						图片名称:
					</td>
					<td	class="honry-view">
							${menuIcon.picName}&nbsp;
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						图片:
					</td>
					<td class="honry-view">
						<img style="height:50px" src="${menuIcon.picShow }">&nbsp;
					</td>
				</tr>
				<tr>
				<tr>
					<td class="honry-lable">
						图片路径：
					</td>
					<td class="honry-view">
						${menuIcon.picPath }&nbsp;
					</td>					
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td	class="honry-view">
						<fmt:formatDate value="${menuIcon.createTime}" pattern="yyyy-MM-dd" />&nbsp;
					</td>
				</tr>
				
			</table>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('')">关闭</a>
			</div>
			</div>
			<script type="text/javascript">
			$(function(){
			});
			
		    function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			</script>
			
	</body>
</html>