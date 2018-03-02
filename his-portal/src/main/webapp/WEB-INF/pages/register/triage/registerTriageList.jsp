<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>门诊分诊</title>
		<%@ include file="/common/metas.jsp" %>
	
<style type="text/css">
.easyui-dialog .panel-header .panel-tool a{
    background-color: red;	
}
.panel-header{
	border-top:0;
}
</style>
<script type="text/javascript">
	//部门
	var dept;
//医生
	var emp;
//午别
	var noonCode = '';
	var noonList = '';
	var typeList = '';
	var levelList = '';
	var flag;
		$(function(){
			
			var loginFlag = '${loginFlag}';
			if(loginFlag != '2'){
				$('#msg').show();
				if(loginFlag == '0'){
					$('#msg').html("当前科室不是分诊台，请选择分诊台进行登录！");
				}
				if(loginFlag == '1'){
					$('#msg').html("当前分诊台尚未分配分诊科室，请联系管理员进行添加！");
				}
				comb(0);
				$('#que').linkbutton('disable');
				$('#readbtn').linkbutton('disable');
				$('#btnAddUser').linkbutton('disable');
				$('#btnCleUser').linkbutton('disable');
				$('#querybtn').linkbutton('disable');
				$('#queryNo').textbox('disable');
			}else{
				$('#msg').hide();
				$('#msg').html("");
				comb(1);
				$('#que').linkbutton('enable');
				$('#readbtn').linkbutton('enable');
				$('#btnAddUser').linkbutton('disable');
				$('#btnCleUser').linkbutton('enable');
				$('#querybtn').linkbutton('enable');
				$('#queryNo').textbox('enable');
			}
			var flag = 0;
			$.ajax({
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=triage", 
				success: function(data) {
					typeList = data;
				}
			});
			$.ajax({
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=triagelevel", 
				success: function(data) {
					levelList = data;
				}
			});
			$.ajax({
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday", 
				success: function(data) {
					noonList = data;
				}
			});
			bindEnterEvent('queryNo', searchReg, 'easyui');
			//1 系统参数患者是否先登记后分诊 0-不需要登记即可分诊，1-需要登记才可分诊 1列表默认显示已分诊挂号，0显示当天所有挂号信息 2登记查询时使用，查询未分诊的患者 2登记查询时使用，查询未分诊的患者
			flag = $("#flag").val();
			//加载部门树
			$('#tDt').tree({
				url:"<%=basePath%>outpatient/triage/deptTreeTriage.action",
			    method:'get',
			    animate:false,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					if(loginFlag != '2'){
						$('#msg').show();
						if(loginFlag == '0'){
							$('#msg').html("当前科室不是分诊台，请选择分诊台进行登录！");
						}
						if(loginFlag == '1'){
							$('#msg').html("当前分诊台尚未分配分诊科室，请联系管理员进行添加！");
						}
						return ;
					}else{
						$('#msg').hide();
					}
					var type = node.attributes.type;//各层节点的意义
					if(type=="1"){
						emp = null;
						$('#list').datagrid('load', {
							flag : flag
						});
					}else if(type=="2"){
						dept = node.id;
						emp = null;
						$('#list').datagrid('load', {
							flag : flag,
							dep : dept
						});
					}else{
						dept = node.attributes.pid;
						emp = node.id.split("_")[0];
						noonCode = node.attributes.midday;
						$('#list').datagrid('load', {
							flag : flag,
							dep : dept,
							emp : emp,
							midday :noonCode
						});
					}
				}
			}); 
			//初始化看诊患者表格
			$('#list').edatagrid({
				url:"<%=basePath%>outpatient/triage/queryTriagePatient.action?menuAlias=${menuAlias}",
				queryParams : {
					flag : $("#flag").val()
				},
				pagination:true,
                pageSize: 20,
                pageList: [20,30,40,50,100],
				onDblClickRow : function(rowIndex, rowData) {//双击查看
					$('#btnAddUser').linkbutton('enable');
					toTable(rowData.clinicCode);
				},onLoadSuccess:function(row, data){
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
		});
		/**
		 * 加载下拉框
		 */
		function comb(i){
			var req = true;
			var dis = false;
			if(i == 0){
				req = false;
				dis = true;
			}
			$('#seeOptimize').combobox({
                url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=triagelevel",
				valueField: 'encode',
                textField: 'name',
                editable:false,
                disabled:dis,
                required:req
			});
			$('#triageType').combobox({
                url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=triage",
				valueField: 'encode',
                textField: 'name',
                editable:false,
                disabled:dis,
                required:req
			});
		}
		
		/**
		 * 根据门诊号填充患者信息
		 */
		function toTable(clinicCode){
			getNode()
			$.ajax({
                url : "<%=basePath%>outpatient/triage/queryTriagePatientByClinicCode.action?menuAlias=${menuAlias}",
                data : {
                    queryNo : clinicCode
                },
                type:'post',
                success: function(data) {
                	$('#idd').val(data.id);
                	$('#patientName').textbox('setValue',data.patientName);
                	$('#clinicCode').textbox('setValue',data.clinicCode);
                	$('#cardId').textbox('setValue',data.cardNo);
                	$('#midicalrecordId').textbox('setValue',data.midicalrecordId);
                	$('#doctName').textbox('setValue',data.doctName);
                	$('#gradeName').textbox('setValue',data.reglevlName);
                	$('#deptName').textbox('setValue',data.deptName);
                	$('#clinicName').textbox('setValue',data.deptName);
                	$('#orderNo').textbox('setValue',data.orderNo);
                	$('#noonCode').textbox('setValue',noonCodeFamater(data.noonCode));
                	$('#seeOptimize').combobox('setValue', data.seeOptimize);
                	$('#triageType').combobox('setValue', data.triageType);
                }
			});
		}
		
		/**
		 * 获得左侧树节点信息
		 */
		function getNode() {
			var node = $('#tDt').tree('getSelected');
			dep = null;
			emp = null;
			midday = null;
			if(node){
				var type = node.attributes.type;//各层节点的意义
				if(type=="2"){
					dept = node.id;
				}else{
					dept = node.attributes.pid;
					emp = node.id.split("_");
					noonCode = node.attributes.midday;
				}
			}
			
		}
		/**
		 * 通过输入的病历号或者住院号或者门诊号查询列表
		 */
		function searchReg() {
			flag = $("#flag").val();
			var queryNo = $.trim($('#queryNo').textbox('getValue'));	
			if(queryNo == null || queryNo == ""){
				$.messager.alert("操作提示", "请输入患者病历号/门诊号！", "warning");
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return false;
			}
			getNode();
			$('#list').datagrid('load', {
				flag : flag,
				dep : dept,
				emp : emp,
				midday : noonCode,
				queryNo : queryNo
			});
			
		}
		/**
		 * 通过输入的病历号或者住院号或者门诊号查询为分诊患者为其分诊
		 */
		function searchFrom() {
			var queryNo = $.trim($('#queryNo').textbox('getValue'));
			if(queryNo != null && queryNo != ""){
				getNode();
				$.ajax({
     				url : "<%=basePath%>outpatient/triage/queryTriagePatient.action?menuAlias=${menuAlias}",
     				data : {
     					flag : 2,
     					dep : dept,
    					emp : emp,
    					midday : noonCode,
     					queryNo : queryNo
     				},
     				type:'post',
     				success: function(data) {
     					if(data.total>0){
     						$("#diaRegister").window('open');
     						$("#registerInfoDatagrid").datagrid({
     							url : "<%=basePath%>outpatient/triage/queryTriagePatient.action?menuAlias=${menuAlias}",
     							queryParams : {
     								flag : 2,
     		     					queryNo : queryNo
     		     				},
     		     				pagination : true,
     		   					pageSize : 10,
     		   					pageList : [ 10, 20, 30],
     		     				striped : true,
     							checkOnSelect : true,
     							selectOnCheck : true,
     							singleSelect : true,
     							fitColumns : false,
     							onClickRow : function(rowIndex, rowData) {
     					        	$("#registerInfoDatagrid").datagrid("beginEdit", rowIndex);
     							},onLoadSuccess:function(row, data){
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
     								}},
     						    columns:[[
     						        {
     						            field : 'ck',
     						            checkbox : true
     						        },{
										field : 'id',
										title : 'id',
										width : '30%',
										hidden : true
									},{
										field : 'cardId',
										title : '就诊卡号',
										width : '15%',
										align : 'center'
									},{
										field : 'midicalrecordId',
										title : '病历号',
										width : '15%',
										align : 'center'
									},{
                                        field : 'clinicCode',
                                        title : '门诊号',
                                        width : '15%',
                                        align : 'center'
                                    },{
										field : 'patientName',
										title : '姓名',width:'10%',
										align : 'center'
									},{
										field : 'seeOptimize',
										title : '优先级别',width:'10%',
										align : 'center',
										editor: {
											type: 'combobox',
											options: {
												valueField: 'encode',
												textField: 'name',
												url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=triagelevel",
												editable: false
											}},
										formatter:seeOptimizeFamater
									},{
										field : 'triageType',
										title : '分诊类型',width:'10%',
										align : 'center',
										editor: {
											type: 'combobox',
											options: {
												valueField: 'encode',
												textField: 'name',
												url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=triage",
												editable: false
											}},
										formatter:triageFamater
									},{
										field : 'doctName',
										title : '挂号专家',
										width : '10%',
										align : 'center'
									},{
										field:'noonCode',
										title : '挂号午别',
										width : '10%',
										align : 'center',
										formatter: noonCodeFamater
									},{
											field:'deptCode',
											title : '科室id',
											hidden: true
									},{
											field:'doctCode',
											title : '专家id',
											hidden:true
									}
									]]
     						});
     					
     				}else{
     					$.messager.alert("操作提示", "没有找到该待登记患者信息！", "warning");
     					setTimeout(function(){$(".messager-body").window('close')},3500);
     				}
     				}
     			});
					
			}else{
				$.messager.alert("操作提示", "请输入患者病历号/门诊号！", "warning");
				setTimeout(function(){$(".messager-body").window('close')},3500);
			}
			
		}
		/**
		 * 显示挂号午别格式化 
		 */
		function noonCodeFamater(value,row,index){
			for(var i = 0; i < noonList.length; i++){
				if(noonList[i].encode == value){
					return noonList[i].name;
				}
			}
		}
		/**
		 * 显示优先级别格式化 
		 */
		function seeOptimizeFamater(value,row,index){
			for(var i = 0; i < levelList.length; i++){
				if(levelList[i].encode == value){
					return levelList[i].name;
				}
			}
		}
		/**
		 * 分诊类型格式化 
		 */
		function triageFamater(value,row,index){
			for(var i = 0; i < typeList.length; i++){
				if(value == typeList[i].encode){
					return typeList[i].name;
				}
			}
		}
		/**
		 * 批量提交列表信息（页面修改）
		 */
		function save() {
			if (!$('#saveForm').form('validate')) {
                $.messager.progress('close');
                $.messager.alert('提示信息','请补全信息后保存!');
                setTimeout(function(){$(".messager-body").window('close')},3500);
                return false;
            }
			$('#saveForm').form('submit',{
				url :"<%=basePath%>outpatient/triage/saveTriagePatient.action?flag=1",
				onSubmit:function(){
					
				},
				success : function(data) {
					if(data == 'success'){
						$('#list').datagrid('clearSelections');
						$('#list').datagrid('reload');
						$('#saveForm').form('clear');
						$.messager.alert("操作提示", "保存成功！");
					}else{
						$.messager.alert("操作提示", "保存失败！");
					}
				}
			});
		} 
		/**
		 * 批量提交列表信息（弹窗新添加）
		 */
		function cle() {
			$('#saveForm').form('clear');
		}
		/**
		 * 批量提交列表信息（弹窗新添加）
		 */
		function register() {
			var rows = $('#registerInfoDatagrid').edatagrid('getSelections');
			if (rows.length > 0) {//选中几行的话触发事件
				$('#registerForm').form('submit',{
					url :"<%=basePath%>outpatient/triage/saveTriagePatient.action?flag=0",
					onSubmit:function(){
						$.each(rows,function(){
							var index=$("#registerInfoDatagrid").datagrid("getRowIndex", this);
							$('#registerInfoDatagrid').edatagrid('endEdit',index);
						});
						$('#registerInfoJson').val(JSON.stringify($('#registerInfoDatagrid').edatagrid("getSelections")));
					},
					success : function(data) {
						if(data == 'success'){
							var rows = $('#registerInfoDatagrid').edatagrid('getSelections');
							$('#list').datagrid('reload'); 
							$.messager.alert("操作提示", "登记成功！");
							toTable(rows[0].clinicCode);
							$('#btnAddUser').linkbutton('enable');
							$("#diaRegister").window('close');
						}else{
							$.messager.alert("操作提示", "登记失败！");
						}
					}
				});
			} else {
				$.messager.alert("操作提示", "请选要登记的患者！", "warning");
				setTimeout(function(){$(".messager-body").window('close')},1500);
			}
		}
		function refresh() {//刷新树
			$('#tDt').tree('reload');
		}
		function expandAll(){//展开树
			$('#tDt').tree('expandAll');
		}
		function collapseAll(){//关闭树
			var root = $('#tDt').tree('getRoots');
			var nodes = $('#tDt').tree('getChildren',root.target);
			for (var i = 0; i < nodes.length; i++) {
				if(nodes[i].attributes.type == '2'){
					$('#tDt').tree('collapse',nodes[i].target);
				}
			}
		}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div class="easyui-layout"  fit=true>
		<input type="hidden" value="${flag}" id="flag">
		<div id="p" data-options="region:'west',tools:'#toolSMId',split:true" title="分诊单位"
			style="width: 15%; ">
			<div id="toolSMId">
		    	<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div style="width: 100%; height: 100%; overflow-y: auto;">
				<ul id="tDt"></ul>
			</div>
		</div>
		<div data-options="region:'center'" style="border-top:0;" >
			<div class="easyui-layout"  fit=true>
				<div id="query" data-options="region:'north',border:false" style="height: 140px">
					<div style="width: 100%;height: 32px">
						<table cellspacing="0" cellpadding="0" border="0" style="padding: 3px;width: 100%;">
							<tr>
							    <td>病历号/门诊号：
	                                 <input class="easyui-textbox" id="queryNo"  data-options="prompt:'病历号/门诊号'"  />
	                           &nbsp;&nbsp; 
		                            <shiro:hasPermission name="${menuAlias}:function:query">
		                                <a id="que" href="javascript:void(0)" onclick="searchReg()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		                                <c:if test="${flag=='1'}">
		                                    <a id="querybtn" href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-dengji">登记</a>
		                                </c:if> 
									    <input type="hidden" value="${flag}" id="flag">
	                                </shiro:hasPermission>
							    	<a id="readbtn" href="javascript:void(0)" iconCls="icon-bullet_feed" onclick="" class="easyui-linkbutton">读卡</a>
								    <shiro:hasPermission name="${menuAlias}:function:save">
									    <a id="btnAddUser" href="javascript:save();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									</shiro:hasPermission>
								    <a id="btnCleUser" href="javascript:cle();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
								<span id="msg" style="font-weight: bold;color: red;display: none;"></span>
								</td>
								
							</tr>
						</table>
					</div>
				    <form id="saveForm" method="post" style="width: 100%;height: 70px">
				        <input type="hidden" id="idd" name = "registration.id">
						<table id="table" class="honry-table" fit="true" cellpadding="0" cellspacing="0" border="0" style="width: 100%;border-left:0;">
	                         <tr>
	                             <td class="honry-lable" style="width: 7%;border-left:0;">姓  名：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="patientName" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">门诊号：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="clinicCode" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">就诊卡号：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="cardId" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">病历号：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="midicalrecordId" editable="false" style="width: 110px"/></td>
	                         </tr>
	                         <tr>
	                             <td class="honry-lable" style="width: 7%;border-left:0;">挂号专家：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="doctName" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">挂号级别：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="gradeName" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">优先级别：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-combobox" id="seeOptimize" name = "registration.seeOptimize" style="width: 110px"/>
	                             	<a href="javascript:delSelectedData('seeOptimize');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
	                             </td>
	                             <td class="honry-lable" style="width: 7%">分诊类型：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-combobox" id="triageType" name = "registration.triageType" style="width: 110px"/>
	                             	<a href="javascript:delSelectedData('triageType');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
	                             </td>
	                         </tr>
	                         <tr>
	                             <td class="honry-lable" style="width: 7%;border-left:0;">顺序号：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="orderNo" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">挂号时间：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="noonCode" editable="false" style="width: 110px"/></td>
	                             
	                             <td class="honry-lable" style="width: 7%">挂号科室：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="deptName" editable="false" style="width: 110px"/></td>
	                             <td class="honry-lable" style="width: 7%">就诊科室：</td>
	                             <td class="honry-info" style="width: 8%"><input class="easyui-textbox" id="clinicName" editable="false" style="width: 110px"/></td>
	                         </tr>
	                     </table>
	                 </form>
				</div>
				<div data-options="region:'center'" style="width: 100%;height: 83%;border-left:0;">
					<table id="list"  fit="true"
						 data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'patientName'" style="width: 5%">姓名</th>
								<th data-options="field:'clinicCode'" style="width: 9%">门诊号</th>
								<th data-options="field:'cardId'" style="width: 9%">就诊卡号</th>
								<th data-options="field:'midicalrecordId'" style="width: 9%">病历号</th>
								<th data-options="field:'doctName'" style="width: 9%">挂号专家</th>
								<th data-options="field:'seeOptimize',width:'9%',formatter:seeOptimizeFamater" >优先级别</th>
								<th data-options="field:'triageType',width:'9%',formatter:triageFamater" >分诊类型</th>
								<th data-options="field:'gradeName'" style="width: 9%">专家级别</th>
								<th data-options="field:'orderNo'" style="width: 5%">顺序号</th>
								<th data-options="field:'deptName'" style="width: 9%">挂号科室</th>
								<th data-options="field:'clinicName'" style="width: 9%">就诊诊室</th>
								<th data-options="field:'doctCode',hidden: true" >专家id</th>
								<th data-options="field:'deptCode',hidden:true" >科室id</th>
								<th data-options="field:'noonCode',formatter: noonCodeFamater"
									style="width: 5%">挂号午别</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div id="diaRegister" class="easyui-dialog" title="患者选择"
			style="width: 1000; height: 500; padding: 5px"
			data-options="modal:true, closed:true">
			<form id="registerForm" method="post" style="width: 100%;height: 100%">
				<input type="hidden" name="registerInfoJson" id="registerInfoJson">
				<table id="registerInfoDatagrid"
					data-options="fit:true,fitColumns:true,singleSelect:true,toolbar:'#toolbar'">
				</table>
			</form>
			<div id="toolbar">
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a id="btnAdd" href="javascript:register();"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-add',plain:true">登记</a>
				</shiro:hasPermission>
			</div>
		</div>
	</div>
</body>
</head>