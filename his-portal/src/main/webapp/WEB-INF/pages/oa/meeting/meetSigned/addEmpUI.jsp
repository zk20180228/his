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

			$(function(){
				$("#selectDep").hide();
				$("#selecRank").attr("checked", true);
				$("#content4").css({
						"opacity": "1",
						"zIndex": "1"
				});
				bindEnterEvent('searchDept',searchTreeUser,'easyui');
				bindEnterEvent('queryName',searchFrom,'easyui');

			});
			function searchTreeUser(){
				var searchText = $('#searchDept').textbox('getValue');
		        $("#tDt").tree("search", searchText);
		    }
		</script>
	</head>

	<body>
		<div class="contentBox">
			<div id="content4" class="contentnav">
				<!-- 人员 -->
				<div class="topContent">
					<div class="treePanel panel panel-default ">
						<div class="panel-heading">科室</div>
						<div class="panel-body">
							<div class="treeMain">
								<input id="searchDept" class="easyui-textbox" style="width: 180px; " data-options="prompt: '拼音,五笔,自定义,编码,名称' " />
								<div class="treeBox">
									<ul id="tDt"></ul>
								</div>
							</div>
						</div>
					</div>
					<div class="tablePanel panel panel-default">
						<div class="panel-heading">人员</div>
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
		var dutiesMap, titleMap;//titleMap职称集合，dutiesMap职务集合
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


		$('#tDt').tree({
			//url: "http://localhost:8080/his-portal/baseinfo/department/departmentCombobox.action",
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


		//职位的
		function dutiesFamater(value) {
			return dutiesMap[value];
		}
		//显示职称格式化
		function titleFamater(value) {
			return titleMap[value];
		}


		function del1() {
			$('#selectAll1 option:selected').remove()
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
			if(value==1){
				$('#list').datagrid('uncheckAll');
			}
			$("#selectAll"+value).html(null)
		}
		
		//得到员工号
		function submitSelect(){
			var employee_jobon = "";//员工号
			$('#selectAll1 option').each(function(index, v){
				if(employee_jobon!=""){
					employee_jobon += ",";
				}
				employee_jobon+= v.getAttribute("value");
			});			
			//通过ajax把员工号传递到后台，向会议组员表中插入数据，一个员工号一条数据，异步，成功之后，关闭当前页面，刷新父页面
			$.ajax({
				url:"${pageContext.request.contextPath}/meeting/emGroup/addEmp.action",
				data:"id=${id}&text=${text}&employee_jobon="+employee_jobon,
				type:"post",
				success:function(backData){
					if("true"==backData){
						//刷新父页面的datagrid组件
						window.opener.$("#list").datagrid("load");
                        window.opener.$('#cc').combobox('reload');
                        //关闭当前窗口
						self.close();
					}else{
						$.messager.alert('提示','添加失败,请重新添加！');
					}
				}
			});
		}
		
		
		function closeWin(){
			self.close();//关闭当前窗口
		}
		/**
		 * 查询
		 */
		function searchFrom() {
			var node = $('#tDt').tree('getSelected')
			var queryName = $.trim($('#queryName').textbox('getValue'));
			var deptid = $('#deptid').val();
			$('#list').datagrid('load', {
				ename :queryName,
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