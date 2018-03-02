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
				padding: 52px 0 40px 0;
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
				padding: 52px 20px 40px 20px;
			}
			
			.contentnav:nth-child(1) {
				opacity: 1;
				z-index: 1;
			}
			
			.footerContent {
				position: absolute;
				width: 100%;
				height: 40px;
				bottom: 0;
				left: 0;
				border-top: 1px solid #CCCCCC;
				z-index: 2;
			}
			
			.navBtnBox {
				width: 120px;
				margin: 0 auto;
			}
			
			.header {
				width: 100%;
				height: 52px;
				position: absolute;
				top: 0;
				left: 0;
				border-bottom: 1px solid #CCCCCC;
				z-index: 2;
			}
			
			.Allposon {
				margin: 20px;
			}
			
			.topContent {
				height: 100%;
				width: 100%;
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
			
			.tableMain {
				width: 400px;
				height: 450px;
				float: left;
				position: relative;
			}
			
			.slelctMain {
				width: 300px;
				height: 450px;
				float: left;
			}
			
			.slelctMain .btnBox {
				width: 120px;
				float: left;
				position: absolute;
				top: 50%;
				margin-top: -100px;
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
				width: 20%;
				height: 100%;
				float: left;
			}
			
			.tablePanel {
				margin-left: 2%;
				width: 78%;
				height: 100%;
				float: left;
			}
			
			.panel-body {
				width: 100%;
				height: 100%;
			}
		</style>
		<script type="text/javascript">
			$(function(){
				bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
				$("#selectPerson").attr("checked", true);
				//回显
				var hadSelect = parent.$('#deptName').textbox('getValue');
				var hadSelectCode = parent.$('#deptCode1').val();
				showtoselect(hadSelect,hadSelectCode);
			});
			function searchTreeNodes(){
				var searchText = $('#searchTreeInpId').textbox('getValue');
		        $("#tDt1").tree("search", searchText);
		    }
			function showtoselect(hadSelect,hadSelectCode){
				var selectV = hadSelect.split(',');
				var selectVCode = hadSelectCode.split(',');
				for(var i=0;i<selectV.length;i++){
					$("#selectAll3").append("<option value='"+selectVCode[i]+ "'>" + selectV[i] + "</option>");
				}
			}
		</script>
	</head>

	<body>
	<div class="header">
			<div class="nav radio radio-theme">
			<div class="lableBox"  id="selectDep">
					<input type="radio" value="checked" id="selectPerson" name="checked">
					<label for="selectPerson">选择科室</label>
				</div>
			</div>
		</div>
		<div class="contentBox">
			<div id="content1" class="contentnav">
				<!-- 部门 -->
				<div class="topContent">
					<div style="width: 100%" class="treePanel panel panel-default ">
						<div class="panel-body" style="padding-top: 20px">
							<div class="treeMain" style="width: 220px;float: left;margin-left: 100px">
								<input id="searchTreeInpId" class="easyui-textbox" style="width: 180px; " data-options="prompt: '拼音,五笔,自定义,编码,名称' " />
								<div class="treeBox">
									<ul id="tDt1"></ul>
								</div>
							</div>
							<div class="slelctMain" style="width: 482px;float: right;">
								<div class="btnAllBox">
									<div class="btnBox">
										<button onclick="add3()" type="button" class="btn btn-success"><span>添&nbsp;&nbsp;加 <i class="honryicon icon-angle_right"></i></span></button>
										<button onclick="del3()" type="button" class="btn btn-danger"><span><i class="honryicon icon-angle_left"></i> 移&nbsp;&nbsp;除</span></button>
										<button onclick="delAll(3)" type="button" class="btn btn-danger"><span><i class="honryicon icon-double_angle_left"></i> 移除全部</span></button>
									</div>
								</div>
								<div class="selectBox">
									<select multiple="multiple" id="selectAll3" style="width:150px;height:100%;margin-left: 100px"></select>
								</div>
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
		<div class="footerContent">
			<div class="navBtnBox">
				<button type="submit" onclick="submitSelect()" class="btn btn-success">保存</button>
				<button type="button" onclick="closeWin()" class="btn btn-default">关闭</button>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">
	var dutiesList, titleList;
	$.ajax({
		type: "get",
		url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action',
		async: false,
		data: {
			type: "duties"
		},
		success: function(data) {
			dutiesList = data
		}
	});
	$.ajax({
		type: "get",
		url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action',
		async: false,
		data: {
			type: "title"
		},
		success: function(data) {
			titleList = data
		}
	});

	$('.lableBox input:radio[name="selectCon"]').change(function() {
		$(".contentBox .contentnav").css({
			"opacity": "0",
			"zIndex": "0"
		})
		$("#content" + this.value).css({
			"opacity": "1",
			"zIndex": "1"
		})
	})

	$('#tDt').tree({
		//			url: "http://localhost:8080/his-portal/baseinfo/department/departmentCombobox.action",
		url: "<%=basePath%>/baseinfo/department/treeDepartmen.action",
		method: 'get',
		animate: true,
		lines: true,
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
			deptid = node.id;
			$('#deptid').val(deptid);
			$('#list').datagrid('load', {
				dname: node.id,
				dtype: node.attributes.hasson
			});
		},
		onBeforeCollapse: function(node) {
			if(node.id == "1") {
				return false;
			}
		}
	});
	//部门
	$('#tDt1').tree({
		url: "<%=basePath%>/baseinfo/department/treeDepartmen.action",
		method: 'get',
		animate: true,
		lines: true,
		checkbox : true,
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
//				$('#tDt1').tree('select', n.target);
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
			var tmp = true;
			$('#selectAll3 option').each(function(index, v) {
				if(v.text == node.text) {
					tmp = false
					return false;
				}
			});
			if(tmp){
				$("#selectAll3").append("<option value='" + node.text+":"+node.id + "'>" + node.text + "</option>")
			}
		},
		onBeforeCollapse: function(node) {
			if(node.id == "1") {
				return false;
			}
		}
	});
	
	//科室部门树操作
	function refresh() { //刷新树
		$('#deptid').val('');
		$('#tDt').tree('options').url = "<c:url value='/baseinfo/department/treeDepartmen.action'/>";
		$('#tDt').tree('reload');
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

	/**
	 * 树查询的方法
	 * @author  zpty
	 * @param 
	 * @date 2015-06-03
	 * @version 1.0
	 */
	function searchTree() { //刷新树
		$.ajax({
			url: "<c:url value='/baseinfo/employee/searchTree.action'/>?searcht=" + encodeURI(encodeURI($('#searchTree').val())), //通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
			type: 'post',
			success: function(data) {
				$('#tDt').tree('collapseAll'); //单独展开一个节点,先收缩树
				var node = $('#tDt').tree('find', data);
				$('#tDt').tree('expandTo', node.target).tree('select', node.target); //展开指定id的节点
				$("#list").datagrid("uncheckAll");
				$('#list').datagrid('load', {
					deptName: data
				});
			}
		});
	}
	function del3() {
		var selectoption = $('#selectAll3 option:selected');
		for(var i=0;i<selectoption.length;i++){
			var va = selectoption[i].value;
			var node = $('#tDt1').tree('find', va);
			if(node != null){
			$('#tDt1').tree('uncheck', node.target);
			}

		}
		$('#selectAll3 option:selected').remove();
	}
	function add3(){
		var treelist = $('#tDt1').tree('getChecked');
		var tmp, str = "";
		for(var i=0;i<treelist.length;i++){
			tmp = false
			if(!treelist[i]["children"].length>0){
				tmp = true;
			}
			$('#selectAll3 option').each(function(index, v) {
				if(v.text == treelist[i]["text"]) {
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
		if(value==3){
			var treelist = $('#tDt1').tree('getChecked');
			for(var i=0;i<treelist.length;i++){
				$('#tDt1').tree('uncheck', treelist[i].target);
			}
		}
		if(value==4){
			$('#postlist').datagrid('uncheckAll');
		}
		if(value==2){
			$('#gradelist').datagrid('uncheckAll');
		}
		if(value==1){
			$('#list').datagrid('uncheckAll');
		}
		$("#selectAll"+value).html(null)
	}
	
	function submitSelect(){
		var value1 = "";//选中的部门
		var value11 ="";
		$('#selectAll3 option').each(function(index, v){
			if(value1!=""){
				value1 += ",";
				value11 += ",";
			}
			value1 += v.getAttribute("value");
			value11 += v.text;
		});
		
		var s = "";
		var s1 = "";
		if(value1 != ""){
			s +=value11;
		}
		parent.$('#deptName').textbox('setValue',s);
		parent.$('#deptCode1').val(value1);
		closeWin();
	}
	function closeWin(){
		parent.$('#mview-window').window('close');
	}
	</script>

</html>