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
						操作账号:
					</td>
					<td class="honry-view">
						${sysOperateLog.log_account}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作栏目:
					</td>
					<td class="honry-view">
						${sysOperateLog.log_action}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作栏目:
					</td>
					<td class="honry-view">
						${sysOperateLog.log_menu_id}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操做SQL:
					</td>
					<td	class="honry-view" 	>
					${sysOperateLog.log_sql}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作表:
					</td>
					<td  class="honry-view">
					${sysOperateLog.log_table}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						目标编号:
					</td>
					<td  class="honry-view">
						${sysOperateLog.log_target_id}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作时间:
					</td>
					<td  class="honry-view">
						${sysOperateLog.log_time}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作备注:
					</td>
					<td  class="honry-view">
						${sysOperateLog.log_description}&nbsp;
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