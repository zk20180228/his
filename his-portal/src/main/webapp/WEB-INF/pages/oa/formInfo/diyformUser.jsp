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
				width: 160px;
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
				border-right: 1px solid #ccc;
			}
			
			.slelctMain {
				width: 275px;
				height: 450px;
				float: left;
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
				width: 260px;
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
			
			.honryicon {
				font-size: 22px;
			}
			
			.btnBox button {
				padding: 1px 6px;
			}
			
			#tDt {
				width: 100%;
				height: 420px;
			}
		</style>
	</head>

	<body>

		<div class="contentnav">
			<!-- 人员 -->
			<div class="topContent clearfix">
				<div class="treePanel panel panel-default ">
					<div class="panel-heading">科室信息</div>
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
									<button id="add1" onclick="add1()" type="button" class="btn btn-success"><span>添&nbsp;&nbsp;加 <i class="honryicon icon-angle_right"></i></span></button>
									<button id="del1" onclick="del1()" type="button" class="btn btn-danger"><span><i class="honryicon icon-angle_left"></i> 移&nbsp;&nbsp;除</span></button>
									<button id="delAll" onclick="delAll(1)" type="button" class="btn btn-danger"><span><i class="honryicon icon-double_angle_left"></i> 移除全部</span></button>
								</div>
							</div>
							<div class="selectBox">
								<select multiple="multiple" id="selectAll1" style="width:150px;height:100%;"></select>
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

		$('#tDt').tree({
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
		$(function() {
			bindEnterEvent('searchDept', searchTreeUser, 'easyui');
			var str = ""
			var dataString = window.parent.tempData
			if(dataString) {
				if(linkItem && linkItem !="undefined"){
					var data = dataString
						str += '<option username="'+ data["6"] +'" value ="' + JSON.stringify(data) + '">' + data["12"] + '</option>'
					$("#selectAll1").html(str)
					window.parent.tempData = null
				}else{
					var data = $.parseJSON(dataString)
					for(var key in data) {
						str += '<option value ="' + key + '">' + data[key] + '</option>'
					}
					$("#selectAll1").html(str)
					window.parent.tempData = null
				}
			}
			if(isChoice == "true") {
				$("#add1").css("display", "none")
				$("#del1").css("display", "none").parent().append("<span >请双击选择!</span>")
			}
		});

		function searchTreeNodes() {
			var searchText = $('#searchTreeInpId').textbox('getValue');
			$("#tDt1").tree("search", searchText);
		}

		function searchTreeUser() {
			var searchText = $('#searchDept').textbox('getValue');
			$("#tDt").tree("search", searchText);
		}
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
		//关闭
		function closeWin() {
			window.parent.$("#dialogWindows").modal('hide')
		}

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
		var urlObj = GetRequest()
		//保存
		var isChoice = urlObj.isChoice
		//联动标识
		var linkage = urlObj.linkage
		// 分组标识
		var linkItem = urlObj.link
		function save() {
			var opction = $("#selectAll1 option")
			if(isChoice == "true" && opction.length > 1) {
				$.messager.alert("提示", "该选框只能选择一位人员!")
				return false
			}
			var obj = {}
			var str = ""
			var id = GetRequest().id
			if(linkItem && linkItem !="undefined") {
				window.parent.setIinkage(id, JSON.parse(opction.attr("value")),linkItem)
			} else {
				opction.each(function(i, v) {
					obj[$(v).attr("username")] = v.innerHTML
					str += v.innerHTML + ","
				})
				if(str.length > 0) {
					str = str.substr(0, str.length - 1)
				}
				window.parent.$("#" + id).val(str)
				window.parent.$("#" + id + "Data").attr("value", JSON.stringify(obj))
				if(str) {
					window.parent.$("#" + id + "Data").removeClass("requi")
					window.parent.$("#" + id).removeClass("requi")
				}
			}
			closeWin()
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

		var sexObj = {
			"1": "男",
			"2": "女",
			"3": "未知"
		}

		//人员
		$('#list').datagrid({
			url: "<%=basePath%>meeting/meetingApply/queryEmployeeExtend.action",
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
					checkbox: isChoice == "true" ? false : true
				}, {
					field: 'name',
					title: '姓名',
					width: 100
				}, {
					field: 'post',
					title: '职务',
					width: 100,
					//formatter: dutiesFamater
				}, {
					field: 'title',
					title: '职称',
					width: 115,
					//formatter: titleFamater
				}, {
					field: 'mobile',
					title: '电话',
					hidden: true
				}, {
					field: 'family',
					title: '名族',
					hidden: true
				}, {
					field: 'jobNo',
					title: '工号',
					hidden: true
				}, {
					field: 'idEntityCard',
					title: '身份证',
					hidden: true
				}, {
					field: 'deptCode',
					title: '科室信息',
					hidden: true,
					formatter: function(value, row, index) {
						return {
							value: row.deptName
						}
					}
				}, {
					field: 'birthday',
					title: '年龄',
					hidden: true,
				}, {
					field: 'sex',
					title: '性别',
					hidden: true,
				}, {
					field: 'type',
					title: '员工类型',
					hidden: true,
				}]
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
				if(isChoice == "true") {
					$("#selectAll1").html("<option userName = '" + rowData.jobNo + "' value='" + setTableData(rowData) + "'>" + rowData.name + "</option>")
				} else {
					var tmp = true;
					$('#selectAll1 option').each(function(index, v) {
						if(v.getAttribute("userName") == rowData.jobNo) {
							tmp = false
							return false;
						}
					});
					if(tmp) {
						$("#selectAll1").append("<option userName = '" + rowData.jobNo + "' value='" + setTableData(rowData) + "'>" + rowData.name + "</option>")
					}
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
			if(isChoice == "true" && data.length > 1) {
				$.messager.alert("提示", "该选框只能选择一位人员!")
				return false
			}
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

		function delAll() {
			$('#list').datagrid('uncheckAll');
			$("#selectAll1").html(null)
		}

		/**
		 * 查询
		 */
		function searchFrom() {
			var node = $('#tDt').tree('getSelected')
			var queryName = $.trim($('#queryName').textbox('getValue'));
			var deptid = $('#deptid').val();
			$('#list').datagrid('load', {
				ename: queryName,
				dname: deptid,
				dtype: node.attributes.hasson
			});
		}
		// 列表查询重置
		function searchReload() {
			$('#queryName').textbox('setValue', '');
			searchFrom();
		}

		function setTableData(data) {
			var dept = {}
			dept[(data["deptCode"])] = data["deptName"]
			var user = {}
			user[(data["jobNo"])] = data["name"]
			return JSON.stringify({
				"1": data.mobile, //电话
				"2": dept, //科室
				"3": data.idEntityCard, //身份证
				"4": sexObj[data.sex] || "未知", //性别
				"5": jsGetAge(data.birthday), //年龄
				"6": data.jobNo, //员工号
				"7": data.post, //职务
				"8": data.title, //职称
				"9": user, //人员
				"10": data.type, //员工类型
				"11": data.birthday, //生日
				"12": data.name,  //人员输入
				"13":data.deptName//科室输入
			})
		}

		function jsGetAge(strBirthday) {
			if(strBirthday) {
				var returnAge;
				var strBirthdayArr = strBirthday.split("-");
				var birthYear = strBirthdayArr[0];
				var birthMonth = strBirthdayArr[1];
				var birthDay = strBirthdayArr[2];
				var d = new Date();
				var nowYear = d.getFullYear();
				var nowMonth = d.getMonth() + 1;
				var nowDay = d.getDate();
				if(nowYear == birthYear) {
					returnAge = 0; //同年 则为0岁  
				} else {
					var ageDiff = nowYear - birthYear; //年之差  
					if(ageDiff > 0) {
						if(nowMonth == birthMonth) {
							var dayDiff = nowDay - birthDay; //日之差  
							if(dayDiff < 0) {
								returnAge = ageDiff - 1;
							} else {
								returnAge = ageDiff;
							}
						} else {
							var monthDiff = nowMonth - birthMonth; //月之差  
							if(monthDiff < 0) {
								returnAge = ageDiff - 1;
							} else {
								returnAge = ageDiff;
							}
						}
					} else {
						returnAge = -1; //返回-1 表示出生日期输入错误 晚于今天  
					}
				}
				return returnAge +"岁" ; //返回周岁年龄  
			} else {
				return ""
			}
		}
	</script>

</html>