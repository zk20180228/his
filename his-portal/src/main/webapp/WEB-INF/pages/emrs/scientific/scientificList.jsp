<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>科研病历辅助系统</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
</head>
<body>
<!-- 电子病历常用词常用词维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;">
		 <div data-options="region:'west'" style="width: 500px;border-top:0">
				<form id="search" method="post" style="padding: 5px">
					<table style="border-bottom:0px;padding: 5px" >
						<tr>
							<td>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;
								<a href="javascript:void(0)" onclick="resetFrom()" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
							</td>
						</tr>
					</table>
					<table style="border-bottom:0px;padding: 5px" id="serarchTB" >
						<tr style="height: 30px">
							<td>
								病历名：&nbsp;
								<input  type="text" ID="emrName" name="emrName"/>
								<a href="javascript:delSelectedData('emrName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr style="height: 30px">
							<td>
								患者：&nbsp;
								<input  type="text" ID="emrPatient" name="emrPatient"/>
								<a href="javascript:delSelectedData('emrPatient');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="width: 95%;border-top:0">
			<table id="list" fit="true" data-options="url:'<%=basePath%>emrs/emrScientific/queryEmrList.action',method:'post',rownumbers:true,idField: 'emrId',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'emrId',hidden:true">病历id</th>
						<th data-options="field:'emrName',width:'20%'">病历名</th>
						<th data-options="field:'emrPatientName',width:'10%'">患者姓名</th>
						<th data-options="field:'emrPatid',width:'10%'">病历号</th>
						<th data-options="field:'emrSex',width:'10%'">性别</th>
						<th data-options="field:'emrAge',width:'10%'">年龄</th>
						<th data-options="field:'emrType',width:'10%'">病历类型</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	<script type="text/javascript">
			var list = ${queryString};
			if(list.length > 0){
				for(var i = 0; i < list.length; i++){
					var tr = '';
					var option = list[i];
					tr = '<tr style="height: 30px"><td>' + option.name + '：';
					if(option.type == 1 || option.type == 2 || option.type == 3){
						var options = option.options.split('_');
						options.forEach(function(value,index,array){
							tr = tr + '&nbsp;<input type="radio" name="' + option.code + '_' + option.type + '"value="' + value + '">' + value;
						});
						tr = tr + '&nbsp;<a href="javascript:removeCheck(' + "'" + option.code + '_' + option.type + "');" + '"' + " class='easyui-linkbutton' data-options=" + '"' + "iconCls:'icon-opera_clear',plain:true" + '"</tr></td>';
					}else if(option.type == 6){
						tr = tr + '&nbsp;<input type="text" class="easyui-numberbox"  data-options="min:1,max:150" name="' + option.code + '_' + option.type +'"/></tr></td>';
					}else if(option.type == 7){
						tr = tr + '&nbsp;<input type="text" class="Wdate" onFocus="WdatePicker({dateFmt:' + "'" +"yyyy/MM/dd',maxDate:" + "'" + "2020/01/01'})" + '" name="' + option.code + '_' + option.type +'"/></tr></td>';
					}
					$('#serarchTB').append(tr);
				}
			}
		//加载页面
			$(function(){
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					method:'post',
					onBeforeLoad:function(){
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					},
					onDblClickRow: function (rowIndex, rowData) {//双击查看
						window.open('<%=basePath%>emrs/emrScientific/toScientificView.action?emrId=' + rowData.emrId,'mywin','menubar=no,toolbar=no,status=no,resizable=no,left='+(screen.availWidth-756)/2+',top=0,scrollbars=1,width=756,height=1086');
						},
					onLoadSuccess:function(row, data){
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
							}}
				});
				
				//电子病历名下拉
				$('#emrName').combobox({
					url : '<%=basePath%>emrs/emrScientific/emrNameComb.action',
					valueField : 'code',
					textField : 'name',
					width : 180,
					onHidePanel:function(none){
						var data = $(this).combobox('getData');
						var val = $(this).combobox('getValue');
						var result = true;
						for (var i = 0; i < data.length; i++) {
							if (val == data[i].code) {
							result = false;
							}
						}
						if (result) {
							$(this).combobox("clear");
						}else{
							$(this).combobox('unselect',val);
							$(this).combobox('select',val);
						}
					},
					filter: function(q, row){
						var keys = new Array();
						keys[keys.length] = 'code';
						keys[keys.length] = 'name';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						keys[keys.length] = 'inputCode';
						return filterLocalCombobox(q, row, keys);
					}
				});
				//患者下拉
				$('#emrPatient').combobox({
					url : '<%=basePath%>emrs/emrScientific/emrPatientComb.action',
					valueField : 'code',
					textField : 'name',
					width : 180,
					onHidePanel:function(none){
						var data = $(this).combobox('getData');
						var val = $(this).combobox('getValue');
						var result = true;
						for (var i = 0; i < data.length; i++) {
							if (val == data[i].code) {
							result = false;
							}
						}
						if (result) {
							$(this).combobox("clear");
						}else{
							$(this).combobox('unselect',val);
							$(this).combobox('select',val);
						}
					},
					filter: function(q, row){
						var keys = new Array();
						keys[keys.length] = 'code';
						keys[keys.length] = 'name';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						keys[keys.length] = 'inputCode';
						return filterLocalCombobox(q, row, keys);
					}
				});
			});
			function removeCheck(name){
				$("input[name='"+name+"']").removeAttr('checked');
			}
			function resetFrom(){
				$('#search').form('clear');
			}
			function searchFrom(){
				var a = $('#search').serialize();
				$('#list').datagrid('load',{queryParems:a});
			}
	</script>
</body>

