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
<title>${tabName}</title>
<style type="text/css">
	*{
		box-sizing: border-box;
	}
</style>
<script type="text/javascript">
	var filenames = '${info.attachName}';
	var fileurls = '${info.attach}';
	var fileServersURL = '${fileServersURL}';
	function GetRequest() {
		var url = location.search; //获取url中"?"符后的字串  
		var theRequest = new Object();
		if(url.indexOf("?") != -1) {
			var str = url.substr(1);
			strs = str.split("&");
			for(var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = decodeURI(decodeURI(strs[i].split("=")[1]));
			}
		}
		return theRequest;
	}
	
	var tabName =  GetRequest()["tabName"]
	var patName =  GetRequest()["tabName"]
	var tabID =  GetRequest()["tabID"]
	var isShowCollect = '${isShowCollect}';
	
	$(function() {
		if (filenames != null && filenames != "") {
			var filename = filenames.split("#");
			var fileurl = fileurls.split("#");
			for (var i = 0; i < fileurl.length; i++) {
				var html = '<a href="'+fileServersURL+fileurl[i]+'" download="'+filename[i]+'">'
						+ filename[i] + '</a></br>';
				$('#attach').append(html);
			}
		}
		if(isShowCollect == 0){
			$('#collectionid').hide();
		}
	});
	function collection(infoid){
		$.ajax({
			url : '<%=basePath%>/oa/repositoryInfo/collectionInfo.action?infoid='+infoid,
			success : function(data){
				$.messager.alert("提示",data.resMsg);
			},error: function(){
				$.messager.alert("提示",'未知错误,请联系管理员...');
			}
		});
	}
	/***
	 * 取消操作
	 */
	function funCancel(){
		var $window;
		upPagecallback (tabName,patName,function(self){
			$window = self
		});
		setTimeout(function(){
			clearNav(tabID,tabName);
		},100)

	}
</script>
</head>
<body style="overflow:hidden; padding: 10px 50px 130px 50px; background: #ffffff;">

    <div  style="text-align: center; ">
		<h1 style="line-height: 50px">${info.name }</h1>
		<span style="line-height: 30px" align="center">发布时间:${info.createTime }&nbsp;&nbsp;发布人:${info.createUser }&nbsp;&nbsp;阅读量：${info.views}</span>
	</div>
	<div  class="borderColor" style="background:#F0F5FB;overflow-x: auto; overflow-y: auto;border-top: 3px solid;width: 100%;height: 100%">
		<div style="padding: 20px;">${info.contentHtml }</div>
		<div style="padding-left: 20px" id="attach"></div>
	</div>
	<div align="center">
		<button id = "collectionid" type="button" class="btn btn-success" onclick="collection('${info.id}')">收藏</button>
		<button type="button" class="btn btn-default" onclick="funCancel()">关闭</button>
	</div>
</body>
</html>