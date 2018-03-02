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
						用户账户:
					</td>
					<td class="honry-view">
						${machineManage.user_account}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						设备码:
					</td>
					<td class="honry-view">
						${machineManage.machine_code}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						手机号:
					</td>
					<td class="honry-view">
						${machineManage.machine_mobile}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否挂失:
					</td>
					<td	class="honry-view" id="isLost"	>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否白名单:
					</td>
					<td id="isWhites" class="honry-view">
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否黑名单:
					</td>
					<td id="isBlacks" class="honry-view">
					</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('')">关闭</a>
			</div>
			</div>
			<script type="text/javascript">
			$(function(){
				var isWhite="${machineManage.is_white}";
				var isBlack="${machineManage.is_black}";
				var isLost="${machineManage.is_lost}";
				$("#isLost").text(formatFlg(isLost));
				$("#isWhites").text(formatFlg(isWhite));
				$("#isBlacks").text(formatFlg(isBlack));
			});
			
			/**
			 * 格式化
			*/
			function formatFlg(value){
				if(value==2){
					return '√';
				}else if(value==1){
					return '×';
				}else{
					return '';
				}
			}
		    function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			</script>
			
	</body>
</html>