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
   <script type="text/javascript">
			   
	</script>
</head>
	<body>
		<div class="easyui-panel" id="panelEast" data-options="title:'',iconCls:'icon-form',border:false,fit:true" style="width:580px">
			<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${userLogin.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						登录编号:
					</td>
					<td class="honry-view">
						${userLogin.id}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						登录用户:
					</td>
					<td class="honry-view">
						${userLogin.userId}&nbsp;
					</td>
				</tr>
			    <tr>
					<td class="honry-lable">
						登录ip:
					</td>
					<td class="honry-view">
						${userLogin.ip}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						登录客户端类型:
					</td>
					<td class="honry-view">
						${userLogin.http}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						登录会话:
					</td>
					<td class="honry-view">
						${userLogin.sessionId}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						登录时间:
					</td>
					<td class="honry-view">
						${userLogin.loginTime}&nbsp;
					</td>
				</tr>
			</table>
			</div>
			</div>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayoutLogin()">关闭</a>
		    </div>
	</body>
</html>