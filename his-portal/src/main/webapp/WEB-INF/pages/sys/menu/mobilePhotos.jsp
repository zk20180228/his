<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
</head>
<body style="padding:10px">
    <div id="imggrid">
    </div>
    <script>
        $('#imggrid').imgGrid({
            title: '图片',
            type: 'POST',
            url:'<%=basePath%>mosys/menuIcon/findMenuIconList.action',
            params: { type: 0 },//url参数
            img: { width: '20%',height:"200px" },//图片宽度，及动画效果
            onClick: function (obj, index, item) {//点击图片事件
           	  $('#icon').textbox('setText',item.picPath);
           	  $('#icon').textbox('setValue',item.picPath);
           	  $('#menuphoto').dialog('close');
           	
            },
            render: function (item, index) {//自定义显示图片
                var str = '<img src="' + item.picShow + '"><p>'+ item.picName +'</p>';
                return str;
            }
        });
    </script>
</body>


</html>