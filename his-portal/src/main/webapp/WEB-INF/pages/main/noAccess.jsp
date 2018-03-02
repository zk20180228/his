<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<head>
	    <title>商户管理</title>
	    <link href="<%=basePath%>themes/system/css/edocss/edo-all.css" rel="stylesheet" type="text/css" />
	    <script src="<%=basePath%>javascript/edojs/edo.js" type="text/javascript"></script>		
	</head>
<body>
</body>
</html>
<script type="text/javascript">
	Edo.create(
		{
	        id: "app", type: "app", render: "#body", horizontalAlign: "center", verticalAlign: "middle",
	        children : [
	            {
	                type: "ct", id: "ct1", width: "150", height: "150",
	                children : [																																																
	                    {
	                        type: "div", id: "div1", width: "100%", height: "100%", html: "<img src='<%=basePath%>themes/system/images/msg/noaccess.png'>"
	                    }
	                ]
	            },
	            {
					id: 'progress',
					type: 'progress',
					onprogresschange: function(e){
						progress.set({
							text: '请稍后......'
	            		});
	            		if(e.progress==100){
	            		 	if(window.top!=null)
	            		 		window.top.location="mainAction!getMenu";//跳转页面
	            		 	else
	            		 		window.parent.location="mainAction!getMenu";//跳转页面
	                	}
	            	},
	            	render: document.body
	            }
	        ]
		}
	);
	var i=0;
	var t=setInterval(function(){
		i+=20;
		if(i>100){
			clearInterval(t);
			return;
		}
		progress.set('progress',i);
	},1000);
</script>