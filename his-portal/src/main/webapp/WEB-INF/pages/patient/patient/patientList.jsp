<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
		<div data-options="region:'center',split:false,title:'病床列表',iconCls:'icon-book'" style="padding: 5px;">
			<div class="easyui-panel" data-options="title:'信息查询',iconCls:'icon-search'">
				<table id="searchTab" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 5px 15px;">
							患者姓名：
						</td>
						<td>
							<input type="text" ID="encode" name="patientName" onkeydown="KeyDown()" />
						</td>
						<td style="padding: 5px 15px;">
							医保手册号：
						</td>
						<td>
							<input type="text" ID="state" name="patientHandbook" onkeydown="KeyDown()" />
						</td>
						<td>
						<shiro:hasPermission name="HZJBXX:function:query">
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<input type="hidden" value="${id }" id="id"></input>
			<table id="list" data-options="url:'listPatient.action',method:'post',selectOnCheck:false,rownumbers:true,idField: 'Id',striped:true,border:true,singleSelect:true,checkOnSelect:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],showRefresh:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
						<!--<th data-options="field:'hospitalId.name', width : 100">
							医院编号
						</th>-->
						<th data-options="field:'patientName', width : 100">
							患者姓名
						</th>
						<!--<th data-options="field:'patientPinyin', width : 100">
							拼音码
						</th>
						<th data-options="field:'patientWb', width : 100">
							五笔码
						</th>
						<th data-options="field:'patientInputcode', width : 100">
							自定义码
						</th>
						-->
						<th data-options="field:'patientHandbook', width : 100">
							医保手册号
						</th>
						<th data-options="field:'patientSex', width : 100">
							性别
						</th>
						<th data-options="field:'patientBirthday', width : 100">
							出生日期
						</th>
						<th data-options="field:'patientAddress', width : 100">
							家庭地址
						</th>
						<th data-options="field:'patientDoorno', width : 100">
							门牌号
						</th>
						<th data-options="field:'patientPhone', width : 100">
							电话
						</th>
						<th data-options="field:'patientCertificatestype', width : 100">
							证件类型
						</th>
						<th data-options="field:'patientCertificatesno', width : 100">
							证件号码
						</th>
						<th data-options="field:'patientBirthplace', width : 100">
							出生地
						</th>
						<th data-options="field:'patientNativeplace', width : 100">
							籍贯
						</th>
						<th data-options="field:'patientNationality', width : 100">
							国籍
						</th>
						<th data-options="field:'patientNation', width : 100">
							民族
						</th>
						<th data-options="field:'patientWorkunit', width : 100">
							工作单位
						</th>
						<th data-options="field:'patientWorkphone', width : 100">
							单位电话
						</th>
						<th data-options="field:'patientWarriage', width : 100">
							婚姻状况
						</th>
						<th data-options="field:'patientWarriage', width : 100">
							职业
						</th>
						<th data-options="field:'bedOrder', width : 100">
							电子邮箱
						</th>
						<th data-options="field:'patientEmail', width : 100">
							母亲姓名
						</th>
						<th data-options="field:'patientMother', width : 100">
							联系人
						</th>
						<th data-options="field:'patientLinkrelation', width : 100">
							联系人关系
						</th>
						<th data-options="field:'patientLinkaddress', width : 100">
							联系人地址
						</th>
						<th data-options="field:'patientLinkdoorno', width : 100">
							联系人门牌号
						</th>
						<th data-options="field:'patientLinkphone', width : 100">
							联系电话
						</th>
					</tr>
				</thead>
			</table>
			<!--<div class="easyui-panel">
				<div class="easyui-pagination"
					data-options="total:114,pageSize:20,pageList:[20,30,50,80,100],showRefresh:false"></div>
			</div>
		-->
		</div>
	</div>
	<div id="toolbarId">
	<shiro:hasPermission name="HZJBXX:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="HZJBXX:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>	
	</shiro:hasPermission>
	<shiro:hasPermission name="HZJBXX:function:delete">	
		<a href="javascript:void(0)" onclick="del1()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				if (getbachIdUtil('#list', 0, 0).length != 0) {
						addAndEdit = 1;
						closeLayout();
						AddOrShowEast('EditForm', 'viewPatient.action?Id=' + getbachIdUtil('#list', 1, 0));
					}
			}
		});
		
		//右键触发事件
		$('#hospitalTree').bind('contextmenu', function(e) {
			e.preventDefault();
			if (getSelected(1) == undefined) {
				$.messager.show({
					title : '提示信息',
					msg : '请选择节点进行操作!'
				});
			} else {
				$('#mm').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}

		});
	});
	
		function add(){
			addAndEdit = 0;
			closeLayout();
			AddOrShowEast('EditForm', 'savePatientURL.action');
		}
		
		function edit(){
			if (getbachIdUtil('#list', 0, 0).length != 0) {
				addAndEdit = 1;
				closeLayout();
				AddOrShowEast('EditForm', 'editPatientUrl.action?Id=' + getbachIdUtil('#list', 1, 0));
			}
		}
		
		function del1(){
			$.messager.confirm('确认对话框', '您想要删除改天记录吗？', function(r) {
				if (r) {
					$.post('deleteHospitalbed.action?id=' + getbachIdUtil('#list', 0, 1));
					del();
					$.messager.show({
						title : '提示信息',
						msg : '删除成功!'
					});
				}
			});
		}
		
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('reload');
		}
	//增加病房信息-提交form表单
	function bedward() {
		var u;
		if (addAndEdit == 2) {
			u = 'addBedward.action';
		}
		if (addAndEdit == 3) {
			u = 'editBedward.action?id=' + getSelected(1);
		}
		$('#bedwardForm').form('submit', {
			url : u,
			data : $('#bedwardForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#hospitalForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				if (addAndEdit == 2) {
					$.messager.show({
						title : '提示信息',
						msg : '添加病房成功!'
					});
				}
				if (addAndEdit == 3) {
					$.messager.show({
						title : '提示信息',
						msg : '修改病房成功!'
					});
				}

			},
			error : function(data) {
				$.messager.alert(data);
			}
		});
		$('#addBedwardWin').panel('close');
		//window.location.href='queryBedward.action';
	}
	//添加病房弹出窗口
	function addBedward() {
		if (getSelected(0) != 1) {
			$.messager.show({
				title : '提示信息',
				msg : '该节点下不允许添加子节点!'
			});
		} else {
			addAndEdit = 2;
			$('#bedwardForm').form('clear');
			$('#department').combobox('reload', 'departmentFindAll.action?id=' + getSelected(1));
			$('#addBedwardWin').panel('open');
			$('#hospitalTree').tree('reload');
		}
	};
	//删除病房信息
	function deleteBedward() {
		window.location.href = 'deleteBedward.action?id=' + getSelected(1);
	}
	//修改病房信息
	function editBedward() {
		if (getSelected(0) != 0) {
			$.messager.show({
				title : '提示信息',
				msg : '只支持病房修改操作!'
			});
		} else {
			addAndEdit = 3;
			$('#bedwardForm').form('clear');
			$('#department').combobox('reload', 'departmentFindAll.action?id=' + getSelected(2));
			$('#bedwardForm').form('load', 'viewBedward.action?id=' + getSelected(1));
			$('#addBedwardWin').panel('open');
			$('#hospitalTree').tree('reload');
		}
	}
	//查看信息
	function viewBedward() {
		if (getSelected(0) != 0) {
			$.messager.show({
				title : '提示信息',
				msg : '只支持病房查看操作!'
			});
		} else {
			$('#bedwardForm').form('clear');
			$('#department').combobox('reload', 'departmentFindAll.action?id=' + getSelected(2));
			$('#bedwardForm').form('load', 'viewBedward.action?id=' + getSelected(1));
			$('#vv').attr('disabled', 'disabled');
			$('#addBedwardWin').panel('open');
		}
	}
	//查询
	function searchFrom() {
		var encode = $('#encode').val();
		var state = $('#state').val();
		$('#list').datagrid('load', {
			patientName : encode,	
			patientHandbook : state
		});
	}
	//获取数据表格选中行的ID checked=0否则是获取勾选行的ID ，获取多个带有拼接''的ID str=0，否则不带有''，
	function getbachIdUtil(tableID, str, checked) {
		var row;
		if (checked == 0) {
			row = $(tableID).datagrid("getSelections");
		} else {
			row = $(tableID).datagrid("getChecked");
		}
		var dgID = "";
		if (row.length < 1) {
			$.messager.alert("操作提示", "请选择一条记录！", "warning");
		}
		var i = 0;
		for (i; i < row.length; i++) {
			if (str == 0) {
				dgID += "\'" + row[i].Id + "\'";
			} else {
				dgID += row[i].Id;
			}
			if (i < row.length - 1) {
				dgID += ',';
			} else {
				break;
			}
		}
		return dgID;
	}
	//删除选中table row 
	function del() {
		var rows = $('#list').datagrid("getChecked");
		var copyRows = [];
		for ( var j = 0; j < rows.length; j++) {
			copyRows.push(rows[j]);
		}
		for ( var i = 0; i < copyRows.length; i++) {
			var index = $('#list').datagrid('getRowIndex', copyRows[i]);
			$('#list').datagrid('deleteRow', index);
		}
	}
	//获得选中节点 tag=1获取ID tag=0获取nodetype  tag = 2 父节点ID 
	//tag=3 判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
	function getSelected(tag) {
		var node = $('#hospitalTree').tree('getSelected');
		if (node != null) {
			var Pnode = $('#hospitalTree').tree('getParent', node.target);
			if (node) {
				if (tag == 0) {
					var nodeType = node.nodeType;
					return nodeType;
				}
				if (tag == 1) {
					var id = node.id;
					return id;
				}
				if (tag == 2) {
					var pid = Pnode.id;
					return pid;
				}
				if (tag == 3) {
					if ($('#hospitalTree').tree('isLeaf', node.target)) {
						var id = node.id;
						return id;
					} else {
						return 1;
					}
				}
			}
		} else {
			return 1;
		}
	}
	//按回车键提交表单！
	$('#searchTab').find('input').on('keyup', function(event) {
		if (event.keyCode == 13) {
			searchFrom();
		}
	});
	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
</script>
</body>
</html>