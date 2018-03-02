<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>摆药单查询</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	$(function(){
		 // 默认选择汇总，显示汇总table，隐藏明细table
		 $('#hz').prop("checked", true); 
		$('#billSearchHzdiv').show();
		$('#billSearchMxdiv').hide();  
		
		//加载手术摆药单树
		tree();
		bindEnterEvent('bname',searchFrom,'easyui');
		//加载数据表格
		$("#billSearchHzList").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{drugedBill:null,applyState:null,bname:null},
			url:'<%=basePath %>/statistics/BillSearch/queryBillClassHz.action',
			onLoadSuccess:function(data){
			}
			
		});
		//申请状态
		$('#applyState').combobox({
			valueField: 'id',    
	        textField: 'text',
			data:[{
						id:'0',
						text:'申请'
					},{
						id:'1',
						text:'配药'
					},{
						id:'2',
						text:'核准出库'
					},{
						id:'3',
						text:'作废'
					},{
						id:'4',
						text:'暂不摆药'
					},{
						id:'5',
						text:'集中发送已打印'
					},{
						id:'6',
						text:'集中发送未打印'
					},{
						id:'7',
						text:'审核'
				}]
		});
	})
	function onclickhz(){
		$('#hz').prop("checked", true); 
		$('#billSearchHzdiv').show();
		$('#billSearchMxdiv').hide();
		var applyState=$("#applyState").combobox("getValue");
		var drugedBill=$('#drugedBill').val();
		$("#billSearchHzList").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{drugedBill:drugedBill,applyState:applyState},
			url:'<%=basePath %>/statistics/BillSearch/queryBillClassHz.action'
			
		});  
		 
	}
	function onclickmx(){
		$('#mx').prop("checked", true); 
		$('#billSearchHzdiv').hide();
		$('#billSearchMxdiv').show();
		var applyState=$("#applyState").combobox("getValue");
		var drugedBill=$('#drugedBill').val();
		$("#billSearchMxList").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{drugedBill:drugedBill,applyState:applyState},
			url:'<%=basePath %>/statistics/BillSearch/queryBillClassMx.action'
			
		});  
	}
	
	/**
	 * @Description:加载摆药单树
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月12日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	function tree(){
		$('#tDt').tree({ 
			url:"<%=basePath%>statistics/BillSearch/getBillSearchTree.action",
		    method:'post',
		    animate:true,  //点在展开或折叠的时候是否显示动画效果
		    lines:true,    //是否显示树控件上的虚线
		    state:'closed',//节点不展开
		    formatter:function(node){//统计节点总数
				var s = node.text;
				  if (node.children){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}  
				return s;
			},
			onBeforeLoad:function(node, param){
				if(node!=null){
					return false;
				}
			},
			
			onSelect: function(node){//点击节点
				if(node.id==2){
					var drugedBill=node.attributes.drugedBill;
					var bname=$("#bname").textbox("getValue");
					var applyState=$("#applyState").combobox("getValue");
					$('#drugedBill').val(drugedBill);
					if ($("#hz").prop("checked")==true){
						 $("#billSearchHzList").datagrid('load',{drugedBill:drugedBill,applyState:applyState,bname:bname});
					}else{
						$("#billSearchMxList").datagrid('load',{drugedBill:drugedBill,applyState:applyState,bname:bname});
					}
				}
			}
		}); 
	}
	
	/**
	 * @Description:摆药单树的查询事件
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年4月20日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	function query(){
		 var beginTime = $('#beginTime').datebox('getValue');
    	 var endTime = $('#endTime').datebox('getValue');
    	 var drugedBill = $('#drugedBill').textbox('getValue');
    	 var applyState = $('#applyState').combobox('getValue');
    	 $('#tDt').tree('options').url ='<%=basePath%>statistics/BillSearch/getBillSearchTree.action?beginTime='+beginTime+'&endTime='+endTime+'&drugedBill='+drugedBill+'&applyState='+applyState;
		 $('#tDt').tree('reload');
	     $("#billSearchHzList").datagrid('load',{drugedBill:null,applyState:null,bname:null});
		 $("#billSearchMxList").datagrid('load',{drugedBill:null,applyState:null,bname:null});
	}
	/**
	 * 查询
	 * @author  tangfeishuai
	 * @date 2016-06-13
	 * @version 1.0
	 */
	function searchFrom() {
		var drugedBill = $('#drugedBill').val();
		var bname=$("#bname").textbox("getValue");
		var applyState=$("#applyState").combobox("getValue");
		if ($("#hz").prop("checked")==true){
			 $("#billSearchHzList").datagrid('load',{drugedBill:drugedBill,applyState:applyState,bname:bname});
		}else{
			$("#billSearchMxList").datagrid('load',{drugedBill:drugedBill,applyState:applyState,bname:bname});
		} 
		
	}
	/**
	 * 导出
	 * @author  tangfeishuai
	 * @date 2016-06-29
	 * @version 1.0
	 */
	function save() {
		var drugedBill = $('#drugedBill').val();
		var bname=$("#bname").textbox("getValue");
		var applyState=$("#applyState").combobox("getValue");
		if ($("#hz").prop("checked")==true){
			$.messager.confirm('确认', '确定要导出摆药单汇总信息吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {
						url :"<%=basePath%>/statistics/BillSearch/outBillClassHzVo.action",
						onSubmit : function(param) {
							param.drugedBill=drugedBill;
							param.bname=bname;
							param.applyState=applyState;
						},
						success : function(data) {
							$.messager.alert("操作提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					})
				}
			});  
		}else{
			$.messager.confirm('确认', '确定要导出摆药单明细信息吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {
						url :"<%=basePath%>/statistics/BillSearch/outBillClassMxVo.action",
						onSubmit : function(param) {
							param.drugedBill=drugedBill;
							param.bname=bname;
							param.applyState=applyState;
						},
						success : function(data) {
							$.messager.alert("操作提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					})
				}
			});
		} 
		
	}
	/**
	 * 打印报表
	 * @author  tangfeishuai
	 * @date 2016-06-29
	 * @version 1.0
	 */
	function edit() {
		var drugedBill = $('#drugedBill').val();
		var bname=$("#bname").textbox("getValue");
		var applyState=$("#applyState").combobox("getValue");
		if ($("#hz").prop("checked")==true){
			$.messager.confirm('打印摆药单汇总', '是否打印摆药单汇总信息?', function(res) {  //提示是否打印假条
				if (res){
					var timerStr = Math.random();
					window.open ("<c:url value='/iReport/iReportPrint/iReportToDispensSum.action?randomId='/>"+timerStr+"&drugedBill="+drugedBill+"&bname="+bname+"&applyState="+applyState+"&fileName=BYDHZCX",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
					};
			 })
		}else{
			$.messager.confirm('打印摆药单明细', '是否打印摆药单明细信息?', function(res) {  //提示是否打印假条
				if (res){
					var timerStr = Math.random();
					window.open ("<c:url value='/iReport/iReportPrint/iReportToDispensDetail.action?randomId='/>"+timerStr+"&drugedBill="+drugedBill+"&bname="+bname+"&applyState="+applyState+"&fileName=BYDMXCX",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
					};
			 })
		} 
		
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="cc" class="easyui-layout" data-options="fit:true"> 
			<div id="p" data-options="region:'north',split:true" style="width:100%;height:5%;">
				<div id="toolbarId" style="padding:5px 5px 5px 5px;">
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">	
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">导出</a> 
				</shiro:hasPermission>	
				</div>
			</div>
			<div data-options="region:'center'" style="width: 100%;height: 90%;">
				<div id="c" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'west',split:true,border:false" style="width: 25%;padding:5px">
						<div style="padding: 5px 5px 5px 5px;">
							<div style="padding:5px">
								汇总：<input type="radio" onclick="javascript:onclickhz()"  id="hz" name="drug"> 明细：<input type="radio" id="mx" onclick="javascript:onclickmx()"  name="drug">
							</div>
				    		<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
				    			<tr>
				    				<td>摆药开始时间：</td>
				    				<td colspan="3"><input class="easyui-datebox" id="beginTime" style="width:200px" /></input></td>
				    			</tr>
				    			<tr>
				    				<td>摆药结束时间：</td>
				    				<td colspan="3"><input class="easyui-datebox" id="endTime"  style="width:200px" /></td>
				    			</tr>
				    			<tr>
				    				<td>摆药单号检索：</td>
				    				<td colspan="3"><input class="easyui-textbox" id="drugedBill" style="width:200px"/></td>
				    			</tr>
				    			<tr>
				    				<td>申请状态：</td>
				    				<td colspan="3"><input class="easyui-textbox" id="applyState" style="width:200px" />
				    				<a href="javascript:delSelectedData('applyState');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
				    				</td>
									
				    			</tr>
				    		</table>
				    	</div>
						<div id="treeDiv" >
							<ul id="tDt">加载中，请稍等...</ul>
						</div>
					</div>
					<div data-options="region:'center',split:true"  title="摆药单药品信息" style="width: 75%;height:100%">
						<div class="easyui-layout" data-options="fit:true">
							<div data-options="region:'north',split:false,border:false" style="width: 25%;padding:5px">
								<div style="padding:5px">
									药品过滤：<input class="easyui-textbox" id="bname" style="width:200px">
											<input type="hidden" id="drugedBill" >
								</div>
							</div>
							<div data-options="region:'center',split:false"  style="width: 75%;height:100%">
								<div id="billSearchHzdiv" style="height:100%">
									<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;">
										<thead>
											<tr>
												<th data-options="field:'ck',checkbox:true" ></th>
												<th data-options="field:'drugName',width:'12%',halign:'center' ">药品名称</th>
												<th data-options="field:'specs',width:'12%',halign:'center'">规格</th>
												<th data-options="field:'drugedNum',width:'12%',halign:'center'">总量</th>
												<th data-options="field:'deptCode',width:'12%',halign:'center'">申请科室</th>
												<th data-options="field:'drugDeptCode',width:'12%',halign:'center'">领药药房</th>
												<th data-options="field:'billClassName',width:'12%',halign:'center'">摆药单</th>
												<th data-options="field:'drugPinYin',width:'12%',halign:'center'">拼音码</th>
												<th data-options="field:'drugWb',width:'12%',halign:'center'">五笔码</th>
											</tr>
										</thead>
									</table>
								</div>
								<div id="billSearchMxdiv" style="height:100%">
									<table id="billSearchMxList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;">
										<thead>
											<tr>
												<th data-options="field:'ck',checkbox:true" ></th>
												<th data-options="field:'bedName',width:'3%',halign:'center' ">床号</th>
												<th data-options="field:'patientName',width:'4%',halign:'center'">姓名</th>
												<th data-options="field:'inpatientNo',width:'7%',halign:'center'">病历号</th>
												<th data-options="field:'tradeName',width:'10%',halign:'center'">药品名称</th>
												<th data-options="field:'specs',width:'5%',halign:'center'">规格</th>
												<th data-options="field:'doseOnce',width:'3%',halign:'center'">每次量</th>
												<th data-options="field:'doseUnit',width:'5%',halign:'center'">剂量单位</th>
												<th data-options="field:'dfqFerq',width:'5%',halign:'center' ">频次</th>
												<th data-options="field:'useName',width:'5%',halign:'center'">用法</th>
												<th data-options="field:'drugedNum',width:'3%',halign:'center'">总量</th>
												<th data-options="field:'deptCode',width:'6%',halign:'center'">申请科室</th>
												<th data-options="field:'drugDeptCode',width:'6%',halign:'center'">取药药房</th>
												<th data-options="field:'billClassName',width:'8%',halign:'center'">摆药单</th>
												<th data-options="field:'printDate',width:'8%',halign:'center'">发药时间</th>
												<th data-options="field:'drugPinYin',width:'6%',halign:'center'">拼音码</th>
												<th data-options="field:'drugWb',width:'6%',halign:'center'">五笔码</th>
												<th data-options="field:'validState',width:'5%',halign:'center'">有效性</th>
												<th data-options="field:'state',width:'5%',halign:'center'">状态</th>
											</tr>
										</thead>
									</table>
								</div> 
								<form id="saveForm" method="post"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>