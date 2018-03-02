<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
var typeMap = new Map();
	$.ajax({
		url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
		success: function(data) {
			var deptType = data;
			for(var i=0;i<deptType.length;i++){
				typeMap.put(deptType[i].encode,deptType[i].name);
			}
		}
	});
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center',fit:true"  id="content" style="width: 100%">
					<div style="padding: 5px 5px 0px 5px;height: 5%;margin-bottom: 5px;" data-options="fit:true">
						<form id="searchForm" method="post" data-options="fit:true">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 30%" nowrap="nowrap">
										部门名称：<input type="text" id="deptName" name="deptName"  value="${deptName }" />
									</td>
									<td style="width: 30%" nowrap="nowrap">
										员工类型：<input type="text" id="employeeType"   name="employeeType"/>
									</td>
									<td style="width: 30%;margin-left: 5px;" nowrap="nowrap">
										关键字： <input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" />
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" id="searchBtn">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div style="padding: 0px 5px 5px 5px;height: 90%">
					<table id="list" style="width:100%"></table>
				</div>
	</div>
</div>
</body>
	<script type="text/javascript">
	var textId= "${param.textId}";//需要传递子窗口commbo的ID
	var textName= "${param.textName}";
	var employeeType= "${param.employeeType}";//员工类型  多个用,分隔  例:1,2,3  
	var deptIds= "${param.deptIds}";//部门ID  多个用,分隔  例:1,2,3  
	var workState= "${param.workState}";//员工工作状态 多个用,分隔 例:1,2,3
	var isExpert= "${param.isExpert}";//是否是专家 1是 0否
	var titles= "${param.titles}";//员工的职称 可以传递多个用,号分割
	var deptTypes= "${param.deptTypes}";//员工所在部门的类型 可以传递多个用,号分割
	var schedual= "${param.schedual}";//区分挂号排班
	var empType = new Array();
	var empMap=new Map();
	var deptName=$("#deptName").val();
	$("#employeeType").val(employeeType);
	//渲染员工
	$.ajax({
			url: "<%=basePath%>inpatient/admission/getEmpList.action", 
			type:'post',
			success: function(data) {
				if(data!=null&&data!=""){
					empMap=data;
				}		
			}
		});
	var popWinEmployeeListPage = {
		easyuiComponent:{},
		initEasyui:{
			/*---初始化页面尺寸---开始--*/
			initPageSize:function(){
				var winH=$("body").height();
				$('#list').height(winH-78-30-27-22);
			},
			/*---初始化页面尺寸---结束--*/
			
			/*---初始化查询按钮---开始--*/
			initSearchBtn:function(){
				var searchBtn = $("#searchBtn").linkbutton({    
				    iconCls: 'icon-search'   
				});
				popWinEmployeeListPage.easyuiComponent["searchBtn"] = searchBtn;
			},
			/*---初始化查询按钮---结束--*/
			
			/*---初始化查询输入框---开始--*/
			initTextBox:function(){
				var queryName = $("#queryName").textbox();
				var deptName = $("#deptName").textbox();
				popWinEmployeeListPage.easyuiComponent["queryName"] = queryName;
				popWinEmployeeListPage.easyuiComponent["deptName"] = deptName;
			},
			/*---初始化查询输入框---结束--*/
			
			
			/*---初始化人员类型下拉框---开始--*/
			initCombobox:function(){
				var employeeTypeCombobox = $('#employeeType').combobox({    
				    url:'<%=basePath%>popWin/popWinEmployee/queryEmployeeTypes.action?employeeType='+employeeType,    
				    valueField:'encode',    
				    textField:'name',
				});
				popWinEmployeeListPage.easyuiComponent["employeeTypeCombobox"] = employeeTypeCombobox;
			},
			/*---初始化人员类型下拉框---结束--*/
			
			/*---初始化人员列表---开始--*/
			initDataGrid:function(){
				//添加操作按钮
				var employeeDatagrid = $('#list').datagrid({
					url:'<%=basePath%>popWin/popWinEmployee/queryPopWinEmployee.action',
					method:'post',
					rownumbers:true,
					idField: 'id',
					fit:true,
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					pagination:true,
					pageSize:20,
					columns:[[
					  		{field:'jobNo',title:'工号',width:'11%'},
					  		{field:'name',title:'姓名',width:'12%'},
					  		{field:'title',title:'职称',width:'12%'},
					  		{field:'type',title:'员工类型',width:'12%',
					  			formatter:function(data){
					  				if(data!=null){
					  					for(var i=0;i<empType.length;i++){
					  						if(data==empType[i].encode){
					  							return empType[i].name;
					  						}
					  					}	
					  				}
					  				}
					  			},
					  		{field:'deptName',title:'所在科室',width:'12%'},
					  		{field:'workState',title:'工作状态',width:'12%',
					  			formatter:function(value,row,index){
					  				return typeMap.get(value);
					  		}},
					  		{field:'pinyin',title:'拼音码',width:'10%'},
					  		{field:'wb',title:'五笔码',width:'10%'},
					  		{field:'inputCode',title:'自定义码',width:'10%'}
					]],
					queryParams: {
						employeeType: employeeType,
						workState: workState,
						isExpert: isExpert,
						deptIds:deptIds,
						titles:titles,
						deptTypes:deptTypes,
						schedual:schedual,
						deptName:deptName
					},
		             onDblClickRow: function (rowIndex, rowData) {//双击查看
				    		var tmpId ="#"+textId;
				    		var tmpName = "#"+textName;
				    		if(window.opener.$(tmpId).attr("class")&&/combotree/ig.test(window.opener.$(tmpId).attr("class"))){
				    			window.opener.$(tmpId).combotree('setValue',rowData.jobNo);
				    		}else if(window.opener.$(tmpId).attr("class")&&/combogrid/ig.test(window.opener.$(tmpId).attr("class"))){
				    			window.opener.$(tmpId).combogrid('setValue',empMap[rowData.jobNo]);//可触发原有js的onSelect事件 
					    		window.opener.$(tmpId+"1").val(rowData.jobNo);
				    		}else if(window.opener.$(tmpId).attr("class")&&/combobox/ig.test(window.opener.$(tmpId).attr("class"))){
				    			window.opener.$(tmpId).combobox('select',rowData.jobNo);
				    		}else{
								if('function' === typeof window.opener.popWinEmpCallBackFn){
									window.opener.popWinEmpCallBackFn(rowData);
								}else{
									window.opener.$(tmpId).val(rowData.jobNo);
									window.opener.$(tmpName).textbox("setValue",rowData.name);
									window.opener.$(tmpId).change();
								}
							}
				    		window.close();
					}    
				});
				popWinEmployeeListPage.easyuiComponent["employeeDatagrid"] = employeeDatagrid;
			}
			/*---初始化人员列表---结束--*/
			
		},
		
		/*--初始化组件事件---开始*/
		ininEvent:function(){
			popWinEmployeeListPage.easyuiComponent["searchBtn"].click(popWinEmployeeListPage.operation.searchEmployee);
			popWinEmployeeListPage.easyuiComponent["employeeTypeCombobox"].combobox({
				onSelect:popWinEmployeeListPage.operation.searchEmployee
			});
			bindEnterEvent('queryName',popWinEmployeeListPage.operation.searchEmployee,'easyui');
			bindEnterEvent('deptName',popWinEmployeeListPage.operation.searchEmployee,'easyui');
		},
		/*--初始化组件事件---结束*/
		
		/*--页面的逻辑---开始*/
		operation:{
			/*
			 *员工条件查询 
			 */
			searchEmployee:function(){
		   		var queryName = popWinEmployeeListPage.easyuiComponent["queryName"].textbox('getValue');
		   		var deptName = popWinEmployeeListPage.easyuiComponent["deptName"].textbox('getValue');
		   		var employeeTypeCurr = popWinEmployeeListPage.easyuiComponent["employeeTypeCombobox"].combobox('getValue');
		   		popWinEmployeeListPage.easyuiComponent["employeeDatagrid"].datagrid('load',{
		   			queryName:queryName,
					employeeType: employeeTypeCurr,
					workState: workState,
					isExpert: isExpert,
					deptIds:deptIds,
					titles:titles,
					deptName:deptName,
					deptTypes:deptTypes,
					schedual:schedual
				});	
			}
		}
		/*--页面的逻辑---结束*/
	};
	//加载页面
		$(function(){
			$.ajax({
				url: "<%=basePath%>popWin/popWinEmployee/queryEmployeeTypes.action", 
				type:'post',
				success: function(deptData){
					empType = deptData;
				}
			});
			popWinEmployeeListPage.initEasyui.initCombobox();
			popWinEmployeeListPage.initEasyui.initSearchBtn();
			popWinEmployeeListPage.initEasyui.initTextBox();
			popWinEmployeeListPage.initEasyui.initPageSize();
			popWinEmployeeListPage.initEasyui.initDataGrid();
			popWinEmployeeListPage.ininEvent();
			
		});
	
	</script>
</html>
