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
			
			.lableBox {
				float: left;
			    margin-left: 30px;
			}
			
			.contentBox {
				width: 100%;
				height: 100%;
				position: relative;
				top: 0;
				left: 0;
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
				width: 121px;
				margin: 0 auto;
			}
			
			.header {
				width: 100%;
				height: 52px;
				position: absolute;
				top: 0;
				left: 0;
				z-index: 2;
			}
			
			.Allposon {
				margin: 20px;
			}
			
			.treeMain {
				width: 250px;
				float: left;
			}
			
			.treeBox {
				width: 100%;
				/*height: 100%;*/
				height: 428px;
				overflow-y: auto;
				overflow-X: hidden;
				border: 1px solid #CCCCCC;
			}
			
			.tableMain {
				width: 400px;
				height: 450px;
				float: left;
				position: relative;
			}
			
			.slelctMain {
				width: 275px;
				height: 450px;
				float: left;
				margin-left: 30px;
			}
			
			.slelctMain .btnBox {
				width: 120px;
				float: left;
				position: absolute;
				top: 50%;
				margin-top: -100px;
				text-align: center;
			}
			
			.slelctMain .btnBox button {
				display: block;
				margin: 15px auto;
			}
			
			.btnAllBox {
				position: relative;
				width: 120px;
				height: 100%;
				float: left;
			}
			
			.selectBox {
				width: 150px;
				height: 100%;
				/*margin-left: 120px;*/
				float: left;
			}
			
			.treePanel {
				width: 100%;
				height: 500px;
				float: left;
			}
			
			.tablePanel {
				margin-left: 20px;
				height: 500px;
				float: left;
			}
			
			.panel-body {
				padding: 0 15px;
			}
			.honryicon  {
				font-size: 22px;
			}
			.btnBox button {
				padding: 1px 6px;
			}
			#tDt{
				width: 100%;
				height: 420px;
			}
		</style>
	</head>

	<body>
		<div style="width: 100%;height: 100%;border: 1px solid #CCCCCC;padding: 15px;">
			<div class="clearfix" style="margin-bottom: 10px;">
				<div class="treeMain">
					<div>
						<input id="searchTreeInpId" class="easyui-textbox" style="width: 180px; " data-options="prompt: '拼音,五笔,自定义,编码,名称' " />
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a> 
					</div>
					<div class="treeBox">
						<ul id="tDt1"></ul>
					</div>
				</div>
				<div class="slelctMain ">
				<div class="btnAllBox">
					<div class="btnBox">
						<button id= "add3" onclick="add3()" type="button" class="btn btn-success"><span>添&nbsp;&nbsp;加 <i class="honryicon icon-angle_right"></i></span></button>
						<button id = "del1" onclick="del3()" type="button" class="btn btn-danger"><span><i class="honryicon icon-angle_left"></i> 移&nbsp;&nbsp;除</span></button>
						<button id= "delAll" onclick="delAll(3)" type="button" class="btn btn-danger"><span><i class="honryicon icon-double_angle_left"></i> 移除全部</span></button>
					</div>
				</div>
				<div class="selectBox">
					<select multiple="multiple" id="selectAll3" style="width:150px;height:100%;margin-left: 30px"></select>
				</div>
			</div>
			</div>
			<div class="footerContent">
				<div class="navBtnBox">
					<button type="submit" onclick="save()" class="btn btn-theme">保存</button>
					<button type="button" onclick="closeWin()" class="btn btn-default">关闭</button>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">
		var  isChoice = GetRequest().isChoice
		$(function(){
			bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
			var str = ""
			var dataString = window.parent.tempData
			if(dataString){
				var data =  $.parseJSON(dataString)
				for (var key in data ) {
					str += '<option value ="'+ key +'">'+ data[key] +'</option>'
				}
				$("#selectAll3").html(str)
				window.parent.tempData = null
			}
			if(isChoice == "true"){
				$("#add3").css("display","none")
				$("#del1").css("display","none").parent().append("<span >请双击选择!</span>")
			}
		});
		function searchTreeNodes(){
			var searchText = $('#searchTreeInpId').textbox('getValue');
	        $("#tDt1").tree("search", searchText);
	    }
		
		$('#tDt1').tree({
			url: "<%=basePath%>/baseinfo/department/treeDepartmen.action",
			method: 'get',
			animate: true,
			lines: true,
			checkbox : isChoice=="true"?false:true,
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
				var n = $('#tDt1').tree('find', 1);
// 				$('#tDt1').tree('select', n.target);
				if(data.length > 0) { //节点收缩
					$('#tDt1').tree('collapseAll');
				}
			},
			onClick: function(node) { //点击节点
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			},
			onDblClick : function(node){
				if(node.children.length>0){//当不是根节点时，直接返回
					return;
				}
				$('#tDt1').tree('check',node.target);
				if( isChoice == "true"){
					$("#selectAll3").html("<option value='" + node.id + "'>" + node.text + "</option>")
				}else{
					var tmp = true;
					$('#selectAll3 option').each(function(index, v) {
						if(v.getAttribute("value") == node.id) {
							tmp = false
							return false;
						}
					});
					if(tmp){
						$("#selectAll3").append("<option value='" + node.id + "'>" + node.text + "</option>")
					}
				}
			},
			onBeforeCollapse: function(node) {
				if(node.id == "1") {
					return false;
				}
			}
		});

		function del3() {
			var selectoption = $('#selectAll3 option:selected');
			for(var i=0;i<selectoption.length;i++){
				var va = selectoption[i].value;
				var node = $('#tDt1').tree('find', va);
				$('#tDt1').tree('uncheck', node.target);

			}
			$('#selectAll3 option:selected').remove();
		}
		function add3(){
			var treelist = $('#tDt1').tree('getChecked');
			if( isChoice == "true" && treelist.length>1){
				$.messager.alert("提示","该选框只能选择一位人员!")
				return false
			}
			var tmp, str = "";
			for(var i=0;i<treelist.length;i++){
				tmp = false
				if(!treelist[i]["children"].length>0){
					tmp = true;
				}
				$('#selectAll3 option').each(function(index, v) {
					if(v.getAttribute("value") == treelist[i]["id"]) {
						tmp = false
						return false;
					}
				});
				if(tmp) {
					str += "<option value='" + treelist[i]["id"] + "'>" + treelist[i]["text"] + "</option>"
				}
			}
			if(str != "") {
				$('#selectAll3').append(str)
			}
		}

		function delAll(value) {
			var treelist = $('#tDt1').tree('getChecked');
			for(var i=0;i<treelist.length;i++){
				$('#tDt1').tree('uncheck', treelist[i].target);
			}
			$("#selectAll"+value).html(null)
		}
		function GetRequest() {
			var url = decodeURIComponent(location.search) ; //获取url中"?"符后的字串  
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
		function closeWin (){
			window.parent.$("#dialogWindows").modal('hide')
		}
		//保存
		
		function save (){
			var opction = $("#selectAll3 option")
			var obj = {}
			var str = ""
			opction.each(function(i,v){
				obj[v.value] = v.innerHTML
				str += v.innerHTML+","
			})
			if(str.length>0){
				str = str.substr(0,str.length-1)
			}
			var id = GetRequest().id
			window.parent.$("#"+id).val(str)
			window.parent.$("#"+id+"Data").attr("value",JSON.stringify(obj))
			if(str){
				window.parent.$("#"+id+"Data").removeClass("requi")
				window.parent.$("#"+id).removeClass("requi")
			}
			closeWin()
		}
	</script>
</html>