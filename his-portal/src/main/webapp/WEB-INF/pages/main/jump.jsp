<%@ page contentType="text/html; charset=utf-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<script language="javascript">
<!--
if(window!=window.top)
{
	window.alert('提示：登录时间过长，请重新登录 ！');
	top.location.href="<%=basePath%>";
}else
{
	window.alert('提示：登录时间过长，请重新登录 ！');
	window.location.href="<%=basePath%>";
}
//-->
</script>