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
						最新版本号:
					</td>
					<td class="honry-view">
						${mIosApkVersion.apkNewestVnum}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						版本名称:
					</td>
					<td	class="honry-view">
							${mIosApkVersion.apkVersionName}&nbsp;
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否强制更新:
					</td>
					<td id="stopFlgTd"	class="honry-view">
						<input type="hidden" id="stopFlg" value="${mIosApkVersion.forceUpdateFlg }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否清理缓存:
					</td>
					<td id="apkClearCacheTd"	class="honry-view">
						<input type="hidden" id="apkClearCache" value="${mIosApkVersion.apkClearCache }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						APPSTORE下载地址:
					</td>
					<td class="honry-view">
						${mIosApkVersion.apkDownloadAddr }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						二维码:
					</td>
					<td class="honry-view">
						${mIosApkVersion.apkDownloadQRAddr }&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('')">关闭</a>
			</div>
			</div>
			<script type="text/javascript">
			$(function(){
				 if($('#stopFlg').val()==2){
					$('#stopFlgTd').text("是");
				}else{
					$('#stopFlgTd').text("否");
				}
				 if($('#apkClearCache').val()==2){
					$('#apkClearCacheTd').text("是");
				}else{
					$('#apkClearCacheTd').text("否");
				}
			});
			
		    function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			</script>
			
	</body>
</html>