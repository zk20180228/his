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
<script type="text/javascript">
		//加载页面
		var lvlList =new Array;
		var rentabList = "";
		$(function() {
			bindEnterEvent('name',searchFrom,'easyui');//绑定回车事件
			bindEnterEvent('code',searchFrom,'easyui');//绑定回车事件
			var id = '${id}'; //存储数据ID
			//添加datagrid事件及分页
			$('#list').datagrid(
					{
						pagination : true,
						pageSize : 20,
						pageList : [ 20, 30, 50, 80, 100 ],
						onBeforeLoad : function(param) {//加载数据
							//GH 2017年2月17日 翻页时清空前页的选中项
							$('#list').datagrid('clearChecked');
							$('#list').datagrid('clearSelections');
						},
						onLoadSuccess : function(data) {//默认选中
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
							var rowData = data.rows;
						var data= new Array(rowData.length); ;
						for(var i=0;i<rowData.length;i++){
							data[i]=rowData[i].code
						}
						$("#codes").val(data);
							$.each(rowData, function(index, value) {
								if (value.id == id) {
									$('#list').datagrid('checkRow', index);

								}
							});
						},
						onDblClickRow : function(rowIndex, rowData) {//双击查看
							if (getIdUtil('#list').length != 0) {
								AddOrShowEast('EditForm','<%=basePath%>baseinfo/hospital/viewHospital.action?hospital.id='+ getIdUtil('#list'));
							}
						}
					});
		});
		
		$.ajax({
			url : "<%=basePath%>baseinfo/hospital/hospitalLevelCombobox.action",
			type : 'post',
			success : function(lvlData) {
				lvlList = lvlData;
			}
		});
		function add() {
			var code=$("#codes").val();
			 AddOrShowEast('EditForm','<%=basePath%>baseinfo/hospital/addHospital.action','post'
					   ,{"codes":code});
		}

		function edit() {
			if (getIdUtil("#list") != null) {
				AddOrShowEast('EditForm', '<%=basePath%>baseinfo/hospital/editHospital.action?hospital.id='+ getIdUtil("#list"));
			}
		}

		function del() {
			//选中要删除的行
			var rows = $('#list').datagrid('getChecked');
			if (rows.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res) {
						var ids = '';
						for (var i = 0; i < rows.length; i++) {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].id;
						}
						$.ajax({
							url : "<%=basePath%>baseinfo/hospital/delHospital.action?ids=" + ids,
							type : 'post',
							success : function(data) {
								$.messager.progress('close');
								$.messager.alert('提示',data.resMsg);
								if(data.resCode=='success'){
									$('#list').datagrid('reload');
								}
							}
						});
					}
				});
			}else{
		    	 $.messager.alert('提示信息','请选择要删除的信息！');
		    	 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		     }
		}

		function reload() {
			//实现刷新栏目中的数据
			$('#list').datagrid('reload');
		}

		//获得选中id	
		function getId(parameter) {
			var row = $("#list").datagrid("getSelections");
			var i = 0;
			if (parameter == 'single') {//获得单个id
				if (row.length < 1) {
					$.messager.alert('提示','请选择一条记录！');
					close_alert();
					return null;
				} else if (row.length > 1) {
					$.messager.alert('提示','只能选择一条记录！');
					close_alert();
					return null;
				} else {
					var id = "";
					for (i; i < row.length; i++) {
						id += row[i].id;
						return id;
					}
				}
			} else if (parameter == 'plurality') {//获得多个id
				if (row.length < 1) {
					$.messager.alert('提示','请至少选择一条记录！');
					close_alert();
					return null;
				} else {
					var ids = "";
					for (i; i < row.length; i++) {
						ids += row[i].id + ",";
					}
					return id;
				}
			} else if (parameter == 'notNull') {//至少获得一个id
				var id = "";
				if (row.length < 1) {//如果没有选择数据，默认选中第一行数据
					$('#list').datagrid('selectRow', 0);
					var row = $("#list").datagrid("getSelections");
				}
				id += row[0].id;
				return id;
			} else {
				$.messager.alert('提示','参数无效！');
				close_alert();
				return null;
			}
		}

		//查询
		function searchFrom() {
			var code = $.trim($('#code').val());
			var name = $.trim($('#name').val());
			$('#list').datagrid('load', {
				"hospital.name" : name,
				"hospital.code" : code
			});
		}
		/**
		 * 动态添加LayOut
		 * @author  liujl
		 * @param title 标签名称
		 * @param url 跳转路径
		 * @date 2015-05-21
		 * @modifiedTime 2015-6-18
		 * @modifier liujl
		 * @version 1.0
		 */
		function AddOrShowEast(title, url,method,params) {
			if(!method){
				method="get";
			}
			if(!params){
				params={};
			}
			var eastpanel = $('#panelEast'); //获取右侧收缩面板
			if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
				//重新装载右侧面板
				$('#divLayout').layout('panel', 'east').panel({
					href : url,
					method:method,
					queryParams:params,
				});
			} else {//打开新面板
				$('#divLayout').layout('add', {
					region : 'east',
					width : '30%',
					split : true,
					href : url,
					method:method,
					queryParams:params,
					closable : true
				});
			}
		}
		//按回车键提交表单！
		$('#searchTab').find('input').on('keyup', function(event) {
			if (event.keyCode == 13) {
				searchFrom();
			}
		});
		//等级显示
		function formatLevel(value, row, index) {
			for (var i = 0; i < lvlList.length; i++) {
				if (value == lvlList[i].encode) {
					return lvlList[i].name;
				}
			}
		}
		//显示盈利性
		function formatRentab(value, row, index) {
			if(value == "1"){
				return "盈利性"
			}
			if(value == "2"){
				return "非盈利性"
			}
		}
		//显示性质
		function formatProperty(value, row, index) {
			if(value == "1"){
				return "公立"
			}
			if(value == "2"){
				return "私营"
			}
		}
		//列表查询重置
		function searchReload() {
			$('#name').textbox('setValue','');
			$('#code').textbox('setValue','');
			searchFrom();
		}
	</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="width:100%;height: 40px">	
			<table style="width:100%;border:none;padding:5px 0 0 0" id="searchTab">
				<tr >
					<td >医院名称：
					<input class="easyui-textbox" ID="name"/>
					系统编号：
					<input class="easyui-textbox"  ID="code"/>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',border:false" style="width:100%;">	
		<input type="hidden" id="codes">
			<table id="list" data-options="url:'${pageContext.request.contextPath}/baseinfo/hospital/queryHospital.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',title:'医院列表',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'name',width:'12%'">医院名称</th>
						<th data-options="field:'code',width:'7%'">系统编号</th>
						<th data-options="field:'brev',width:'7%'">简称</th>
						<th data-options="field:'district',width:'7%'">所在省市县</th>
						<th data-options="field:'createTime',width:'7%'">创建时间</th>
						<th data-options="field:'description',width:'7%'">描述</th>
						<th data-options="field:'trafficRoutes',width:'7%'">交通路线</th>
						<th data-options="field:'address',width:'7%'">详细地址</th>
						<th data-options="field:'level',width:'7%',formatter:formatLevel">等级</th>
						<th data-options="field:'rentability',width:'7%',formatter:formatRentab">盈利性</th>
						<th data-options="field:'property',width:'7%',formatter:formatProperty">性质</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>


	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" onclick="del()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:reload">
			<a href="javascript:void(0)" onclick="reload()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</shiro:hasPermission>
	</div>



	
</body>
</html>