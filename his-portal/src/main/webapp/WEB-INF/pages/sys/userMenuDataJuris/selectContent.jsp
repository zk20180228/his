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
		<meta charset="UTF-8" />
		<style type="text/css">
			* {
				box-sizing: border-box;
			}
			
			body,
			html {
				width: 100%;
				height: 100%;
			}
			
			
			.contentnav {
				opacity: 0;
				transition: all 0.3s;
				position: absolute;
				left: 0;
				top: 0;
				width: 100%;
				height: 100%;
			}
			
			.contentnav:nth-child(1) {
				opacity: 1;
				z-index: 1;
			}
			
			.footerContent {
				width: 100%;
				height: 40px;
				z-index: 2;
			}
			
			.navBtnBox {
				width: 160px;
				margin: 0 auto;
			}
			
			
			
			.treeMain {
				width: 100%;
				height: 100%;
				float: left;
			}
			
			.treeBox {
				width: 100%;
				/*height: 100%;*/
				height: 428px;
				overflow-y: auto;
				overflow-X: hidden;
			}
			
			
			.treePanel {
				width: 430px;
				height: 500px;
				float: left;
			}
			
			.panel-body {
				padding: 0 15px;
			}
			
			.honryicon {
				font-size: 22px;
			}
			#tDt {
				width: 100%;
				height: 420px;
			}
		</style>
	</head>

	<body>

		<div class="contentnav">
			<!-- 选择区域 -->
			<div class="topContent clearfix">
				<div class="treePanel panel panel-default ">
					<div id="titleTip" class="panel-heading"></div>
					<div class="panel-body">
						<div class="treeMain">
							<input id="searchDept" class="easyui-textbox" style="width: 160px; " data-options="prompt: '拼音,五笔,自定义,编码,名称' " />
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeUser()" style="margin-top: 2px;">搜索</a>
							<div class="treeBox">
								<ul id="tDt"></ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="footerContent">
				<div class="navBtnBox">
					<button type="submit" onclick="save()" class="btn btn-theme">保存</button>
					<button type="button" onclick="closeWin()" class="btn btn-default">关闭</button>
				</div>
			</div>
	</body>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">
		var jurisSign=true;
		function GetRequest() {
			var url = decodeURIComponent(location.search); //获取url中"?"符后的字串  
			var theRequest = new Object();
			if(url.indexOf("?") != -1) {
				var str = url.substr(1);
				strs = str.split("&");
				for(var i = 0; i < strs.length; i++) {
					theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
				}
			}
			return theRequest;
		}
		var urlObj = GetRequest();
		var sign=urlObj['sign'];
		var dataMap=null;
		if(sign=='dept'){
			$('#titleTip').html('科室信息');
			dataMap=window.parent.deptInfo;//加载科室
			jurisSign=false;
		}else{
			$('#titleTip').html('栏目信息');
			dataMap=window.parent.menuInfo;//加载栏目数据
		}
		$('#tDt').tree({
			method: 'get',
			animate: true,
			lines: true,
			multiple:true,
			checkbox:true,
			cascadeCheck:true,
			onlyLeafCheck:false,
			data:dataMap,
			formatter: function(node) { //统计节点总数
				var s = node.text;
				if(node.children.length > 0) {
					if(node.children) {
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},
			onLoadSuccess: function(node, data) {
				var n = $('#tDt').tree('find', 1);
				$('#tDt').tree('select', n.target);
				if(data.length > 0) { //节点收缩
					$('#tDt').tree('collapseAll');
				}
			},
			onClick: function(node) { //点击节点
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			},
			onBeforeCollapse: function(node) {
				if(node.id == "1") {
					return false;
				}
			},
			onSelect:function(index,row){
				//回显
				
			}
		});
		$(function() {
			bindEnterEvent('searchDept', searchTreeUser, 'easyui');
			var str = ""
			var dataString = window.parent.userData
			if(dataString) {
				var data = dataString
				for(var key in data) {
					str += '<option userName ="' + key + '">' + data[key] + '</option>'
				}
				$("#selectAll1").html(str)
				window.parent.tempData = null
			}
		});

		function searchTreeUser() {
			var searchText = $('#searchDept').textbox('getValue');
			$("#tDt").tree("search", searchText);
		}
		function expandAll() { //展开树
			$('#tDt').tree('expandAll');
		}
		function collapseAll() { //关闭树
			$('#tDt').tree('collapseAll');
		}
		function getSelected() { //获得选中节点
			var node = $('#tDt').tree('getSelected');
			if(node) {
				var id = node.id;
				return id;
			}
		}
		//关闭
		function closeWin() {
			window.parent.$("#dialogWindows").modal('hide')
		}
		function save() {
			var obj = {}
			var str = ""
			var id = GetRequest().id
			var jurisCodeNodes =$("#tDt").tree('getChecked');
			for(var i=0;i<jurisCodeNodes.length;i++){
				if(jurisSign){//拼接栏目
					str += '<span code="'+ jurisCodeNodes[i].id +'" alias="'+jurisCodeNodes[i].attributes.alias+'" haveson="'+jurisCodeNodes[i].attributes.haveson+'" class="selectSpanUser">'+ jurisCodeNodes[i].text +'</span>';
				}else{//拼接科室
					if(jurisCodeNodes[i].attributes!=null&&jurisCodeNodes[i].attributes.hasson=='2'){
						str += '<span code="'+ jurisCodeNodes[i].id +'" class="selectSpanUser">'+ jurisCodeNodes[i].text +'</span>';
					}
				}
			}
			str != "" && (window.parent.$("#" + id).parent().find(".isclear").show())
			window.parent.$("#" + id).html(str);
			closeWin();
		}
	</script>

</html>