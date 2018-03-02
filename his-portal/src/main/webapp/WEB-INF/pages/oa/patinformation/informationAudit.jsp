<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>${info.infoTitle}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
	*{
		padding: 0;
		margin: 0;
		box-sizing: border-box;
	}
	.MainBox{
		width: 100%;
		height: 100%;
		background-color: #fff;
	}
	.BtnBox {
		margin-top: 10px;
		padding-left: 35px;
	}
	.FileBox::after {
		content: "";
		display: block;
		height: 1px;
		width: 100%;
		position: absolute;
		top: -20px;
		left: 0;
		background-color: #ccc;
	}
	.FileBox {
		padding-left: 35px;
		margin-top: 20px;
		position: relative;
	}
	
</style>
<script type="text/javascript">
var filenames = '${filenames}';
var fileurls = '${fileurls}';
var infoid = '${infoid}';
$(function(){
	if(filenames!=null&&filenames!=""){
		var filename = filenames.split("#");
		var fileurl = fileurls.split("#");
		for(var i=0;i<fileurl.length;i++){
			var html = '<a href="'+fileurl[i]+'" download="'+filename[i]+'">'+filename[i]+'</a></br>';
			$('#attach').append(html);
		}
	}
});
function submit(value){
	$.messager.progress({text:'保存中，请稍后...',modal:true});
	$.ajax({
		url : '<%=basePath%>oa/patInformation/audite.action',
		data : {infoid:infoid,type:value},
		success : function(re){
			$.messager.progress('close');
			if(re.resCode=="success"){
				$.messager.alert('提示',re.resMsg,'info',function(){
					var $window;
					upPagecallback ('文章审核','信息管理',function(self){
						$window = self
					});
					setTimeout(function(){
						$window.$("#dg").datagrid('reload');
						clearNav('informationAuditid','文章审核'); 
					},100)
				});
			}else{
				$.messager.progress('close');
				$.messager.alert('提示','未知错误!','info');
			}
		},
		error : function(re){
			$.messager.progress('close');
			$.messager.alert('提示','网络繁忙,请稍后重试...','info');
		}
	});
}
function cancelPage(){
	clearNav('informationAuditid','文章审核');
}
</script>
</head>
<body style="overflow:hidden; padding: 10px 50px 130px 50px; background: #ffffff;">
	 <div  style="text-align: center; ">
		<h1 style="line-height: 50px">${info.infoTitle }</h1>
	    <span style="line-height: 30px" align="center">发布时间:${info.time }&nbsp;&nbsp;发布人:${info.pubuserName }</span>
 	 </div>
 	 
 	 <div  class="borderColor" style="background:#F0F5FB;overflow-x: auto; overflow-y: auto;border-top: 3px solid;width: 100%;height: 100%">
		<div style="padding: 20px 20px 0 20px;">${content }</div>
		<div id="attach" style="padding:0 20px 20px 20px"></div>
	</div>
 	 <div class = "BtnBox">
		<button onclick="submit(1)" type="button" class="btn btn-success">同意</button>
		<button onclick="submit(0)" type="button" class="btn btn-warning">退回</button>
		<button onclick="cancelPage(0)" type="button" class="btn btn-inverse">关闭</button>
	</div>
 	 
<!-- <div class = "MainBox"> -->
<!-- 	<div style="padding: 20px 30px;"> -->
<%-- 	${content } --%>
<!-- 	</div> -->
<!-- 	<div class = "FileBox" id="attach"> -->
		
<!-- 	</div> -->
	
<!-- </div> -->
</body>
</html>