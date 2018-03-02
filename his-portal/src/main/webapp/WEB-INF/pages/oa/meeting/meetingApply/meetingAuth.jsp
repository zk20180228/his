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
<!-- 		<meta name="viewport" content="width=device-width, initial-scale=1.0" /> -->
<!-- 		<meta http-equiv="X-UA-Compatible" content="ie=edge" /> -->
<!-- 		<link rel="stylesheet" type="text/css" href="js/honry-frame.css" /> -->
<!-- 		<script src="js/honry-frame.js" type="text/javascript" charset="utf-8"></script> -->
<!-- 		<script src="js/jquery.easyui.min.js" type="text/javascript" charset="utf-8"></script> -->
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
				width: 90px;
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
			var whichfile = '${whichfile}';
			var findType = '${findType}';
			$(function(){
				//获取需要回显的数据
				var hadSelect = "";
				if('1'==findType){
					$("#selectDep").hide();
					$("#selecRank").attr("checked", true);
					$("#content4").css({
						"opacity": "1",
						"zIndex": "1"
					});
					$("#content1").css({
						"opacity": "0",
						"zIndex": "0"
					});
					hadSelect = parent.$('#inSidePersonCode').val();
				}else{
					$("#selectPerson").attr("checked", true);
					hadSelect = parent.$('#meetingDeptCode').val();
				}
				
				
				bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
				bindEnterEvent('searchDept',searchTreeUser,'easyui');
				bindEnterEvent('queryName',searchFrom,'easyui');
				
				if(''!=hadSelect){
					showtoselect(hadSelect);
				}
			});
			function searchTreeNodes(){
				var searchText = $('#searchTreeInpId').textbox('getValue');
		        $("#tDt1").tree("search", searchText);
		    }
			function searchTreeUser(){
				var searchText = $('#searchDept').textbox('getValue');
		        $("#tDt").tree("search", searchText);
		    }
			//选择回显
			//0:查看范围
	 		//1:内部出席人员
			function showtoselect(hadSelect){
					var type = 1;
				if("1"==findType){
					var code = hadSelect;
					var cdna = code.split(",");
					for(var j=0;j<cdna.length;j++){
						var node = cdna[j].split(':');
						if(typeof(code)!="undefined"&&code!=null&&code!=""){
							$("#selectAll"+type).append("<option value='" + node[0] + "'>" + node[1] + "</option>");
						}
					}
				}else if("0"==findType){
					var checkRange = "";
					var number = "";
					if(hadSelect.indexOf("&&")!=-1){
						number = hadSelect.split("&&");
					}else{
						checkRange = hadSelect.replace("查看范围（人员）：","");
						number = checkRange.split("查看范围（部门）：");
					}
					for(var n = 0 ;n<number.length;n++){
						var code = number[n];
						var cdna = code.split(",");
						for(var j=0;j<cdna.length;j++){
							var node = cdna[j].split(':');
							if(typeof(code)!="undefined"&&code!=null&&code!=""){
								$("#selectAll"+type).append("<option value='" + node[0] + "'>" + node[1] + "</option>");
							}
						}		
						type = type + 2;
					}
				}
// 				console.log(hadSelect);
// 				var selectV = hadSelect.split('#');
// 				for(var i=0;i<selectV.length;i++){
// 					var tAc = selectV[i];
// 					var tc = tAc.split('_');
// 					if(tc.length>1){//0全部，1部门，2职位，3级别，4人员
// 						if(tc[0]=="0"){
// 							$('All').attr('checked',true);
// 						}else{
// 							var type = tc[0];
// 							var code = tc[1];
// 							var cdna = code.split(",");
// 							for(var j=0;j<cdna.length;j++){
// 								var node = cdna[j].split(':');
// 								if(typeof(code)!="undefined"&&code!=null&&code!=""){
// 									$("#selectAll"+type).append("<option value='" + node[0] + "'>" + node[1] + "</option>");
// 								}
// 							}
// 						}
// 					}
// 				}
			}
		</script>
	</head>

	<body>
		<div class="header">
			<div class="nav radio radio-theme">
				<div class="lableBox">
					范围类型：
				</div>
<!-- 				<div class="lableBox"> -->
<!-- 					<input type="radio" value="0" id="Allperson" checked="checked" name="selectCon"> -->
<!-- 					<label for="Allperson">全部人员</label> -->
<!-- 				</div> -->
				<div class="lableBox"  id="selectDep">
					<input type="radio" value="1" id="selectPerson" name="selectCon">
					<label for="selectPerson">选部门</label>
				</div>
<!-- 				<div class="lableBox"> -->
<!-- 					<input type="radio" value="2" id="selectSection" name="selectCon"> -->
<!-- 					<label for="selectSection">选职位</label> -->
<!-- 				</div> -->
<!-- 				<div class="lableBox"> -->
<!-- 					<input type="radio" value="3" id="selectPost" name="selectCon"> -->
<!-- 					<label for="selectPost">选级别</label> -->
<!-- 				</div> -->
				<div class="lableBox">
					<input type="radio" value="4" id="selecRank" name="selectCon">
					<label for="selecRank">选人员</label>
				</div>
				<c:if test="${findType!='0'}"><!--当是范围窗口时，不显示  -->
					<c:if test="${flag=='YZBGS'}"><!--为了提高加载速度  -->
						<div class="lableBox">
							<input type="radio" value="5" id="selecGroup" name="selectCon">
							<label for="selecGroup">选组员</label>
						</div>
					</c:if>
				</c:if>
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
<!-- 								 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a> -->
								<div class="treeBox">
									<ul id="tDt1"></ul>
								</div>
							</div>
							<div class="slelctMain" style="width: 482px;float: right;">
								<div class="btnAllBox">
									<div class="btnBox">
										<button onclick="add3()" type="button" class="btn btn-success"><span>添&nbsp;&nbsp;加 <i class="honryicon icon-angle_right"></i></span></button>
										<button onclick="del3()" type="button" class="btn btn-info"><span><i class="honryicon icon-angle_left"></i> 移&nbsp;&nbsp;除</span></button>
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
			<div id="content4" class="contentnav">
				<!-- 人员 -->
				<div class="topContent">
					<div class="treePanel panel panel-default ">
						<div class="panel-heading">科室信息</div>
						<div class="panel-body">
							<div class="treeMain">
								<input id="searchDept" class="easyui-textbox" style="width: 180px; " data-options="prompt: '拼音,五笔,自定义,编码,名称' " />
<!-- 								 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeUser()" style="margin-top: 2px;">搜索</a> -->
								<div class="treeBox">
									<ul id="tDt"></ul>
								</div>
							</div>
						</div>
					</div>
					<div class="tablePanel panel panel-default">
						<div class="panel-heading">人员信息</div>
						<div class="panel-body">
							<div class="tableMain">
								<form id="search" method="post">
									<table style="width: 100%; border: 0px; padding: 5px 5px 0px 5px;">
										<tr>
											<td style="width: 100%;">查询条件：
												<input class="easyui-textbox" id="queryName" name="queryName" data-options="prompt:'姓名,拼音,五笔,自定义,工作号'" onkeydown="KeyDown(0)" style="width: 150px;" />
												<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
												<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
												<input id="deptid" type="hidden" />
											</td>
										</tr>
									</table>
								</form>
								<table id="list"></table>
							</div>
							<div class="slelctMain">
								<div class="btnAllBox">
									<div class="btnBox">
										<button onclick="add1()" type="button" class="btn btn-success"><span>添&nbsp;&nbsp;加 <i class="honryicon icon-angle_right"></i></span></button>
										<button onclick="del1()" type="button" class="btn btn-info"><span><i class="honryicon icon-angle_left"></i> 移&nbsp;&nbsp;除</span></button>
										<button onclick="delAll(1)" type="button" class="btn btn-danger"><span><i class="honryicon icon-double_angle_left"></i> 移除全部</span></button>
									</div>
								</div>
								<div class="selectBox">
									<select multiple="multiple" id="selectAll1" style="width:150px;height:100%;"></select>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
				<div id="content5" class="contentnav">
				<!-- 人员 -->
				<div class="topContent">
					<div class="treePanel panel panel-default ">
						<div class="panel-heading">会议组</div>
						<div class="panel-body">
							<div class="treeMain">
								<input id="searchGroup" class="easyui-textbox" style="width: 180px; " data-options="prompt: '拼音,五笔,自定义,编码,名称' " />
<!-- 								 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeUser()" style="margin-top: 2px;">搜索</a> -->
								<div class="treeBox">
									<ul id="tDt_group"></ul>
								</div>
							</div>
						</div>
					</div>
					<div class="tablePanel panel panel-default">
						<div class="panel-heading">组员</div>
						<div class="panel-body">
							<div class="tableMain">
								<form id="searchGroupEmp" method="post">
									<table style="width: 100%; border: 0px; padding: 5px 5px 0px 5px;">
										<tr>
											<td style="width: 100%;">查询条件：
												<input class="easyui-textbox" id="queryEmp" name="queryEmp" data-options="prompt:'姓名,科室,员工号'" onkeydown="KeyDown(0)" style="width: 150px;" />
												<a href="javascript:void(0)" onclick="queryEmp()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
												<a href="javascript:void(0)" onclick="resetEmp()" class="easyui-linkbutton" iconCls="reset">重置</a>
												<input id="empId" type="hidden" />
											</td>
										</tr>
									</table>
								</form>
								<table id="list_group"></table>
							</div>
							<div class="slelctMain">
								<div class="btnAllBox">
									<div class="btnBox">
										<button onclick="add_emp()" type="button" class="btn btn-success"><span>添&nbsp;&nbsp;加 <i class="honryicon icon-angle_right"></i></span></button>
										<button onclick="del_emp()" type="button" class="btn btn-info"><span><i class="honryicon icon-angle_left"></i> 移&nbsp;&nbsp;除</span></button>
										<button onclick="delAll_emp(1)" type="button" class="btn btn-danger"><span><i class="honryicon icon-double_angle_left"></i> 移除全部</span></button>
									</div>
								</div>
								<div class="selectBox">
									<select multiple="multiple" id="selectAll_emp" style="width:150px;height:100%;"></select>
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
		var dutiesMap, titleMap;
		$.ajax({
			type: "get",
			url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action',
			async: false,
			data: {
				type: "duties"
			},
			success: function(data) {
				dutiesMap = data
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
				titleMap = data
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
		
/*********************************************************************************院长办公室开始*************************************************************************************/
	
			$(function(){
				//加载组tree树
				$('#tDt_group').tree({    
					    url:'${pageContext.request.contextPath}/meeting/emGroup/loadGroup.action'
					});  
				
				function searchTree(){
					 var text= $("#searchGroup").textbox("getText");
		        	   $('#tDt_group').tree({
								queryParams : {
									text : text
								}
		            });
				}
				
				//回车加载tree树
				bindEnterAndBlackEvent('searchGroup', searchTree,'easyui');
				
				//加载组员列表
				$('#list_group').datagrid({
							url : '${pageContext.request.contextPath}/meeting/emGroup/groupList.action',
							fitColumns : true,
							idField : "employee_jobon",
							pagination : true,
							striped : true,
							fit : true,
							border:false,
							pageList : [ 10, 20, 30, 40, 50 ],
							pageSize : 20,
							//rownumbers : true,
							checkOnSelect : false,
							columns : [ [
									{
										field : 'employee_jobon',
										checkbox : true,
										width : 100
									},
									{
										field : 'employee_name',
										title : '姓名',
										width : 100,
										align : 'center'
									},
									{
										field : 'dept_name',
										title : '科室',
										width : 150,
										align : 'center'
									},
									{
										field : 'duties_name',
										title : '职务',
										width : 100,
										align : 'center'
									},
									{
										field : 'title_name',
										title : '职称',
										width : 100,
										align : 'center'
									} ] ],
						        	   onLoadSuccess: function(data){
											//分页工具栏作用提示
											var pager = $(this).datagrid('getPager');
											var aArr = $(pager).find('a');
											var iArr = $(pager).find('input');
											$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
											for(var i=0;i<aArr.length;i++){
												$(aArr[i]).tooltip({
													content:toolArr[i],
													hideDelay:1
												});
												$(aArr[i]).tooltip('hide');
											}
						        	   }
						});
				
				
					//点击tree树节点，加载对应节点下的成员列表
					$('#tDt_group').tree({
							onClick: function(node){
								//加载员工列表
								loadEmpList(node.id);
							}
					});
				
			});
			
			
			//加载指定组的成员列表
			function loadEmpList(id) {
				var searchField = $('#queryEmp').textbox('getValue');
				$('#list_group').datagrid("load", {
					'id' : id,
					'employee_name' : searchField,
					'employee_jobon' : '',
					'dept_name' : ''
				});
			}
			
			//根据条件查询指定节点id下的成员
			function queryEmp() {
				var node = $('#tDt_group').tree('getSelected');
				if (node != null && node.id != null && node.id != null) {
					loadEmpList(node.id);
				}
			}
	
	
			//重置
			function resetEmp() {
				$('#queryEmp').textbox('setValue', '');
				queryEmp();
			}
			
			//添加到右边的菜单栏
			function add_emp() {
				var data = $('#list_group').datagrid('getChecked');
				var tmp, str = "";
				for(var i = 0; i < data.length; i++) {
					tmp = true
					$('#selectAll_emp option').each(function(index, v) {
						if(v.getAttribute("value") == data[i]["employee_jobon"]) {
							tmp = false
							return false;
						}
					})
					if(tmp) {
						str += "<option value='" + data[i]["employee_jobon"] + "'>" + data[i]["employee_name"] + "</option>"
					}
				}
				if(str != "") {
					$('#selectAll_emp').append(str)
				}
			}
			
			//从右边的菜单栏移除
			function del_emp() {
				$('#selectAll_emp option:selected').remove()
			}
			
			
			function delAll_emp(){
		
				$('#list_group').datagrid('uncheckAll');
				$("#selectAll_emp").html(null)
			}

			
			
		
	/*********************************************************************************院长办公室开始结束*************************************************************************************/
	
		
		//部门
		$('#tDt1').tree({
			//			url: "http://localhost:8080/his-portal/baseinfo/department/departmentCombobox.action",
<%-- 			url: "<%=basePath%>oa/information/getDeptAuth.action", --%>
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
		
		//人员
		$('#list').datagrid({
<%-- 			url: "<%=basePath%>oa/information/getUserAuth.action", --%>
			url: "<%=basePath%>baseinfo/employee/queryEmployee.action",
			method: 'post',
			rownumbers: true,
			idField: 'id',
			striped: true,
			border: false,
			checkOnSelect: true,
			selectOnCheck: false,
			singleSelect: true,
			fitColumns: false,
			toolbar: '#toolbarId',
			fit: true,
			pagination: true,
			pageSize: 20,
			pageList: [10, 20, 30, 40, 50],
			columns: [
				[{
						field: 'ck',
						checkbox: true
					},
					{
						field: 'name',
						title: '姓名',
						width: 100
					},
					{
						field: 'post',
						title: '职务',
						width: 100,
						formatter: dutiesFamater
					},
					{
						field: 'title',
						title: '职称',
						width: 115,
						formatter: titleFamater
					}
				]
			],
			onBeforeLoad: function(param) {
				//GH 2017年2月17日 翻页时清空前页的选中项
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onLoadSuccess: function(data) { //默认选中
				//分页工具栏作用提示
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({
					content: '回车跳转',
					showEvent: 'focus',
					hideEvent: 'blur',
					hideDelay: 1
				});
				for(var i = 0; i < aArr.length; i++) {
					$(aArr[i]).tooltip({
						content: toolArr[i],
						hideDelay: 1
					});
					$(aArr[i]).tooltip('hide');
				}
// 				var rowData = data.rows;//回显选中使用
// 				$.each(rowData, function(index, value) {
// 					if(value.id == id) {
// 						$("#list").datagrid("checkRow", index);
// 					}
// 				});
			},
			onDblClickRow: function(rowIndex, rowData) { //双击查看
				var tmp = true;
				$('#selectAll1 option').each(function(index, v) {
					if(v.getAttribute("value") == rowData.jobNo) {
						tmp = false
						return false;
					}
				});
				if(tmp){
					$("#selectAll1").append("<option value='" + rowData.jobNo + "'>" + rowData.name + "</option>")
				}
			}
		});
		//职位
		$('#postlist').datagrid({
<%-- 			url: "<%=basePath%>oa/information/getDutyAuth.action", --%>
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=duties",
			method: 'post',
			rownumbers: true,
			idField: 'id',
			striped: true,
			border: false,
			checkOnSelect: true,
			selectOnCheck: false,
			singleSelect: true,
			fitColumns: false,
			toolbar: '#toolbarId',
			fit: true,
			columns:[[
					  {field: 'ck',checkbox: true},
			          {field:'encode',title:'编码',hidden:true,width:10},    
			          {field:'name',title:'名称',width:'80%'},    
			      ]],
			onDblClickRow: function(rowIndex, rowData) { //双击查看
				var tmp = true;
				$('#selectAll4 option').each(function(index, v) {
					if(v.getAttribute("value") == rowData.encode) {
						tmp = false
						return false;
					}
				});
				if(tmp){
					$("#selectAll4").append("<option value='" + rowData.encode + "'>" + rowData.name + "</option>")
				}
			}
		});
		//职位
		$('#gradelist').datagrid({
<%-- 			url: "<%=basePath%>oa/information/getDutyAuth.action", --%>
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=level",
			method: 'post',
			rownumbers: true,
			idField: 'id',
			striped: true,
			border: false,
			checkOnSelect: true,
			selectOnCheck: false,
			singleSelect: true,
			fitColumns: false,
			toolbar: '#toolbarId',
			fit: true,
			columns:[[
					  {field: 'ck',checkbox: true},
			          {field:'encode',title:'编码',hidden:true,width:10},    
			          {field:'name',title:'名称',width:'80%'},    
			      ]],
			onDblClickRow: function(rowIndex, rowData) { //双击查看
				var tmp = true;
				$('#selectAll2 option').each(function(index, v) {
					if(v.getAttribute("value") == rowData.encode) {
						tmp = false
						return false;
					}
				});
				if(tmp){
					$("#selectAll2").append("<option value='" + rowData.encode + "'>" + rowData.name + "</option>")
				}
			}
		});
		//职位的
		function dutiesFamater(value) {
			return dutiesMap[value];
		}
		//显示职称格式化
		function titleFamater(value) {
			return titleMap[value];
		}

		function del3() {
			var selectoption = $('#selectAll3 option:selected');
			for(var i=0;i<selectoption.length;i++){
				var va = selectoption[i].value;
				var node = $('#tDt1').tree('find', va);
				$('#tDt1').tree('uncheck', node.target);

			}
			$('#selectAll3 option:selected').remove();
		}
		function del2() {
			$('#selectAll2 option:selected').remove()
		}
		function del1() {
			$('#selectAll1 option:selected').remove()
		}
		function del4() {
			$('#selectAll4 option:selected').remove()
		}
		function add3(){
			var treelist = $('#tDt1').tree('getChecked');
			var tmp, str = "";
			for(var i=0;i<treelist.length;i++){
				tmp = true
				$('#selectAll3 option').each(function(index, v) {
					if(v.getAttribute("value") == treelist[i]["id"]) {
						tmp = false
						return false;
					}
				});
				if(treelist[i]["children"].length>0){
					tmp = false;
					return false;
				}
				if(tmp) {
					str += "<option value='" + treelist[i]["id"] + "'>" + treelist[i]["text"] + "</option>"
				}
			}
			if(str != "") {
				$('#selectAll3').append(str)
			}
		}
		function add4(){
			var postlist = $('#postlist').datagrid('getChecked');
			var tmp,str = "";
			for(var i=0;i<postlist.length;i++){
				tmp = true
				$('#selectAll4 option').each(function(index, v) {
					if(v.getAttribute("value") == postlist[i]["encode"]) {
						tmp = false
						return false;
					}
				})
				if(tmp) {
					str += "<option value='" + postlist[i]["encode"] + "'>" + postlist[i]["name"] + "</option>"
				}
			}
			if(str != "") {
				$('#selectAll4').append(str)
			}
		}
		function add2(){
			var gradelist = $('#gradelist').datagrid('getChecked');
			var tmp,str = ""; 
			for(var i=0;i<gradelist.length;i++){
				tmp = true
				$('#selectAll2 option').each(function(index, v) {
					if(v.getAttribute("value") == gradelist[i]["encode"]) {
						tmp = false
						return false;
					}
				})
				if(tmp) {
					str += "<option value='" + gradelist[i]["encode"] + "'>" + gradelist[i]["name"] + "</option>"
				}
			}
			if(str != "") {
				$('#selectAll2').append(str)
			}
		}
		function add1() {
			var data = $('#list').datagrid('getChecked');
			var tmp, str = "";
			for(var i = 0; i < data.length; i++) {
				tmp = true
				$('#selectAll1 option').each(function(index, v) {
					if(v.getAttribute("value") == data[i]["jobNo"]) {
						tmp = false
						return false;
					}
				})
				if(tmp) {
					str += "<option value='" + data[i]["jobNo"] + "'>" + data[i]["name"] + "</option>"
				}
			}
			if(str != "") {
				$('#selectAll1').append(str)
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
			var value = "";//所有人员
			var valueName = "";//所有人员name
			var value1 = "";//选中的部门
			var value1Name = "";//选中的部门
			var value2 = "";//选中的职位
			var value2Name = "";//选中的职位
			var value3 = "";//选中的级别
			var value3Name = "";//选中的级别
			var value4 = "";//选中的人员
			var value4Name = "";//选中的人员
			
			var value_emp = "";//选中的人员
			var value_empName = "";//选中的人员的名字
			if($('#All').is(':checked')){
				value = 'all';
				valueName = "所有人员";
			}
			$('#selectAll1 option').each(function(index, v){
				if(value4!=""){
					value4 += ",";
					value4Name += '、';
				}
				value4 += v.getAttribute("value")+":"+v.text;
				value4Name += v.text;
			});
			
			$('#selectAll_emp option').each(function(index, v){
					if(value_emp!=""){
						value_emp += ",";
						value_empName += '、';
					}
					value_emp += v.getAttribute("value")+":"+v.text;
					value_empName += v.text;
			});
		
			$('#selectAll2 option').each(function(index, v){
				if(value3!=""){
					value3 += ",";
					value3Name += "、";
				}
				value3 += v.getAttribute("value")+":"+v.text;
				value3Name += v.text;
			});
			$('#selectAll3 option').each(function(index, v){
				if(value1!=""){
					value1 += ",";
					value1Name += "、";
				}
				value1 += v.getAttribute("value")+":"+v.text;
				value1Name += v.text;
			});
			$('#selectAll4 option').each(function(index, v){
				if(value2!=""){
					value2 += ",";
					value2Name += "、";
				}
				value2 += v.getAttribute("value")+":"+v.text;
				value2Name += v.text;
			});
			var showbackname = "";
			if(valueName!=""){
				showbackname = valueName;
			}else{
				if(value1Name!=""){
					showbackname += "部门:"+value1Name+";";
				}
				if(value2Name!=""){
					showbackname += "职位:"+value2Name+";";
				}
				if(value3Name!=""){
					showbackname += "级别:"+value3Name+";";
				}
				if(value4Name!=""){
					showbackname += "人员:"+value4Name+";";
				}
			}
			if('0'==findType){
				var result = '';
				var resultCode = '';
				if(""!=value4Name){
					result += "查看范围（人员）："+value4Name+"\n";
// 					resultCode += "查看范围（人员）："+value4+"\n";
// 					result += value4Name;
					resultCode += value4;
				}
				resultCode = resultCode+"&&";
				if(""!=value1Name){
					result += "查看范围（部门）："+value1Name;
// 					resultCode += "查看范围（部门）："+value1;
// 					result += "&&"+value1Name;
					resultCode += value1;
				}
				parent.$('#meetingDept').val(result);
				parent.$('#meetingDeptCode').val(resultCode);
			}else{
				
				if("YZBGS"=="${flag}"){//如果是院长办公室，那么走院长办公室流程
					
					var gf=$("#selecGroup:checked").val();  
					if(gf){//组员单选框被选中
						parent.$('#inSidePerson').val(value_empName);
						parent.$('#inSidePersonCode').val(value_emp);
					}
					var p=$("#selecRank:checked").val();					
					if(p){//人员单选框被选中
						parent.$('#inSidePerson').val(value4Name);
						parent.$('#inSidePersonCode').val(value4);
					}
					
				}else{
					parent.$('#inSidePerson').val(value4Name);
					parent.$('#inSidePersonCode').val(value4);
				}
			}
// 			parent.showback(whichfile,showbackname);
			closeWin();
		}
		function closeWin(){
			parent.$('#winidSet').window('close');
		}
		/**
		 * 查询
		 */
		function searchFrom() {
			var node = $('#tDt').tree('getSelected')
			var queryName = $.trim($('#queryName').textbox('getValue'));
			var deptid = $('#deptid').val();
			$('#list').datagrid('load', {
				ename : queryName,
				dname: deptid,
				dtype:node.attributes.hasson
			});
		}
		// 列表查询重置
		function searchReload() {
			$('#queryName').textbox('setValue','');
			searchFrom();
		}
		
		
		
	</script>

</html>