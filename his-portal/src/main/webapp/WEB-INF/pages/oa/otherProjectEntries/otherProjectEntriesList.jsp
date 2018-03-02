<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<head>
		<title>其他项目入口</title>
		<style type="text/css">
			#entries li a {
				width: 100%;
				height: 100%;
				display: block;
			}
		</style>
	</head>

	<body>
		<div class="rolemain">
			<div class="role chose">
				<h2>选择系统</h2>
				<div class="roleList choseList">
					<a href="javascript:void(0)"  class="prevR"></a>
					<a href="javascript:void(0)" class="nextR"></a>
					<div class="roleListMid choseListMid">
						<ul id="entries">
						</ul>
					</div>
				</div>
			</div>
		</div>
		<script src="http://192.168.0.82:9000/hias/javascript/js/alertwindow.js" type="text/javascript"></script>
		<script type="text/javascript">
			
			function getData (data){
				return [
					{
						name:"${projectName1}",
						action:"${projectUrl1}"+"?ssoToken="+"${ssoToken}"+"&currentUserAccount="+"${currentUserAccount}"
					},
					{
						name:"1",
						action:"http://www.baidu.com"
					}
				]
			}
			$(function(){
				$('.prevR').css('visibility', 'hidden');
				$('.nextR').css('visibility', 'hidden');
				$('.prevD').css('visibility', 'hidden');
				$('.nextD').css('visibility', 'hidden');
				var data = getData()
				var str = ""
				for (var i = 0 ;i<data.length;i++) {
						str += 	'<li  style="cursor:pointer;" onclick = goto("'+data[i].action+'","'+data[i].name+'")>'
								'<div class="role-M"></div>'+ data[i].name+'</li>'
				}
				$("#entries").html(str)
			})
				
			function goto (url,name){
				window.open(url,name)
			}
				
		</script>
	</body>

</html>