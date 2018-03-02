<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
var oo="${menuAlias}";
/**
 * 医嘱类型全局变量
 */
var submissive ="";
/**
 * 单位全局变量
 */
var packMap = "";
//存放摆药单号
var druged='';
/**
 * 初始化加载
 */
$(function(){
	//查询药品单位
	$.ajax({
		url: "<%=basePath%>inpatient/auditDrug/packFunction.action",
		type:'post',
		success: function(packData) {
			packMap = packData
		}
	});
	
	//查询医嘱类型
	$.ajax({
		url: "<%=basePath%>inpatient/auditDrug/submissiveFunction.action",
		type:'post',
		success: function(submissiveData) {
			submissiveMap = submissiveData
		}
	});
	
	
	//根据当前登陆药房获取药房下的摆药台
	$('#controlId').combobox({    
		url: "<%=basePath%>inpatient/auditDrug/queryDrugHouse.action",   
	    valueField:'id',    
	    textField:'controlName',
	    multiple:false,
	    select:"${parameter.controlId }",
	    onLoadSuccess:function(none){
	    	if(none!=null && none.length>0){
		    	$('#controlId').combobox('select',none[0].id);
	    	}
	    },
	    onSelect: function(recode){
	    	$('#controlCode').val(recode.id);
	    	$('#sendType').val(recode.sendType);
	    	$('#drugedBill').val("")
	    	$('#showLevel').val(recode.showLevel);
	    	//常量 1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药
	    	var opType=4;
	    	//申请状态
	    	var applyState = "5,6"
	    	//通知状态
	    	var sendFlag = "0";
	    	//初始化加载树
	    	approveNoDrugTree(recode.id,recode.sendType,opType,applyState,sendFlag,"");
	    	$("#deptSummary").datagrid('loadData', { total: 0, rows: [] });
	    	$("#detailed").datagrid('loadData', { total: 0, rows: [] });
        }
	});
	
	//实现同时切换tab效果药品
	$('#drugDetail').tabs({    
	    border:false,    
	    onSelect:function(title){
	    	if(title=="科室汇总记录"){
	    		var drugedBill = $('#drugedBill').val();
	    		var deptId = $('#deptId').val();
	    		var showLevel = $('#showLevel').val();
	    		if(showLevel!=0){
	    			$.messager.alert("操作提示","该摆药台不存在科室汇总信息");
	    			setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	    			$('#drugDetail').tabs('select','摆药明细记录'); 
	    		}else{
	    			
	    			$('#detailed').datagrid({
	    				selectOnCheck:false,
	    				checkOnSelect:false,
						url: "<%=basePath%>inpatient/auditDrug/detailed.action?menuAlias=${menuAlias}",
						queryParams:{"parameter.drugedBill":drugedBill,"parameter.applyState":"5,6","parameter.opType":4,"parameter.deptId":deptId},
						onClickRow:function(rowIndex, rowData){
							if(rowData.drugedBill!=druged){
								var rows = $('#detailed').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].drugedBill==rowData.drugedBill){
										var index = $('#detailed').datagrid('getRowIndex',rows[i]);
										$('#detailed').datagrid('selectRow',index);
									}
								}
							}
							var rows=$('#detailed').datagrid('getSelections');
							
							if(rows.length==0){
								druged='';
							}
						},
						onUnselect:function(rowIndex, rowData){
					    	var rows=$('#detailed').datagrid('getSelections');
							$('#detailed').datagrid('clearSelections');
							$('#detailed').datagrid('clearChecked');
							 for(var i=0;i<rows.length;i++){
								var drugedBill1 = rows[i].drugedBill;
								if(rows[i].drugedBill!=rowData.drugedBill){
									var index=$('#detailed').datagrid('getRowIndex',rows[i]);
									$('#detailed').datagrid('selectRow',index);
								}
							} 
							druged=rowData.drugedBill;
						}
					});
	    		}
	    	}else{
	    		var drugedBill = $('#drugedBill').val();
	    		var applyState = "5,6";
	    		var opType = 4;
	    		datagridList(drugedBill,applyState,opType);
	    	}
	   	 }    
	});
});

/**
 * 加载列表摆药明细记录
 */
function datagridList(drugedBill,applyState,opType){
	$('#deptSummary').datagrid({
		url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action?menuAlias=${menuAlias}",
		queryParams:{"parameter.drugedBill":drugedBill,"parameter.applyState":applyState,"parameter.opType":opType},
		onLoadSuccess: function (data) {//默认选中
			$('#deptSummary').datagrid("autoMergeCells", ['name','bedno']);
		},
		onSelect:function(rowIndex,rowData){
			$('#deptSummary').datagrid('checkAll')
		},
		onUnselect:function(rowIndex,rowData){
			$('#deptSummary').datagrid('uncheckAll')
		}
	});
}

/**
 * 加载摆药通知单树
 */
function approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,medicalrecordId){
	$.messager.progress({text:'加载中，请稍后...',modal:true});
	$('#tDt').tree({    
		url:"<%=basePath%>inpatient/auditDrug/approveNoDrugTree.action",
		queryParams:{'parameter.controlId':controlCode,'parameter.sendType':sendType,'parameter.opType':opType,'parameter.applyState':applyState,'parameter.sendFlag':sendFlag,'parameter.medicalrecordId':medicalrecordId},
	    method:'post',
	    animate:true,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			if (node.children){
				s += (node.children.length==0)?'':'&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
			}
			return s;
		}, onLoadSuccess : function(node, data) {  
			 $.messager.progress('close');
             if (data.length==0) {
                $("#ts").show();
             }else{
            	 $("#ts").hide();
             }   
         },
		onClick: function(node){//点击节点
			if(node.attributes.type=="2"){
				$('#drugDetail').tabs('select','摆药明细记录');
				$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action?menuAlias=${menuAlias}",
					queryParams:{"parameter.drugedBill":node.id,"parameter.applyState":"5,6","parameter.opType":4},
					onLoadSuccess: function (data) {//默认选中
						$('#deptSummary').datagrid("autoMergeCells", ['name','bedno']);
					},
					onCheck:function(rowIndex,rowData){
						$('#deptSummary').datagrid('checkAll')
					},
					onSelect:function(rowIndex,rowData){
						$('#deptSummary').datagrid('checkAll')
					},
					onUnselect:function(rowIndex,rowData){
						$('#deptSummary').datagrid('uncheckAll')
					},
					onUncheck:function(rowIndex,rowData){
						$('#deptSummary').datagrid('uncheckAll')
					},
				});
				$('#drugedBill').val(node.id);
				$('#deptId').val(node.attributes.dept);
			}
		}
		
	});
}

/**
 * 跳转摆药单核准
 */
function examineDrug(){
	var parameterHz = $('#parameterHz').val();
	var parameterTf = $('#parameterTf').val();
	var sendType = $('#sendType').val();
	var controlCode = $('#controlCode').val();
	var showLevel = $('#showLevel').val();
	
	window.location.href="<%=basePath%>inpatient/auditDrug/examineDrugList.action?parameter.parameterHz="+parameterHz+"&parameter.parameterTf="+parameterTf+"&parameter.sendType="+sendType+"&parameter.controlId="+controlCode+"&parameter.showLevel="+showLevel+"&menuAlias="+oo;
}

/**
 * 退药台
 */
function refund(){
	var parameterHz = $('#parameterHz').val();
	var parameterTf = $('#parameterTf').val();
	var sendType = $('#sendType').val();
	var controlCode = $('#controlCode').val();
	var showLevel = $('#showLevel').val();
	window.location.href="<%=basePath%>inpatient/auditDrug/withdrawalDrugList.action?parameter.parameterHz="+parameterHz+"&parameter.parameterTf="+parameterTf+"&parameter.sendType="+sendType+"&parameter.controlId="+controlCode+"&parameter.showLevel="+showLevel+"&menuAlias="+oo;
}

/**
 * 摆药单审批
 */
function approvalDrug(){
	var parameterHz = $('#parameterHz').val();
	var parameterTf = $('#parameterTf').val();
	var sendType = $('#sendType').val();
	//获取当天选择标签页
	var tab = $('#drugDetail').tabs('getSelected');
	var index = $('#drugDetail').tabs('getTabIndex',tab);
	if(index==0){//摆药明细记录
		var listRows = $('#deptSummary').datagrid('getChecked');
		var applyNumber = "";
		if(listRows.length>0){
			for(var i =0;i<listRows.length;i++){
				if(applyNumber!=""){
					applyNumber = applyNumber + ",";
				}
				applyNumber = applyNumber + listRows[i].applyNumber;
			}
			$.messager.progress({text:'正在审批，请稍后。。。'});
			$.ajax({
				url: "<%=basePath%>inpatient/auditDrug/approvalDrugSave.action",
				type:'post',
				data:{"parameter.applyNumberCode":applyNumber,"parameter.parameterHz":parameterHz},
				success:function(datasMap){
					$.messager.progress('close');
					if(datasMap.resMsg=="success"){
						$.messager.alert('我的消息',datasMap.resCode);
						setTimeout(
							function shuxin(){
								window.location.href="<%=basePath%>inpatient/auditDrug/auditList.action?menuAlias=BYDHZ"
							}
						,2000);
					}else if(datasMap.resMsg=="error"){
						$.messager.alert("操作提示", datasMap.resCode);
					}
				}
			});
		}else{
			$.messager.alert("操作提示","对不起，没有摆药单，请重新选择");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
	}else{//科室汇总记录
		var listRows = $('#detailed').datagrid('getSelections');
		var applyNumber = "";
		if(listRows.length>0){
			for(var i =0;i<listRows.length;i++){
				if(applyNumber!=""){
					applyNumber = applyNumber + ",";
				}
				applyNumber = applyNumber + listRows[i].applyNumber;
			}
			$.messager.progress({text:'正在审批，请稍后。。。'});
			$.ajax({
				url: "<%=basePath%>inpatient/auditDrug/approvalDrugSave.action",
				type:'post',
				data:{"parameter.applyNumberCode":applyNumber,"parameter.parameterHz":parameterHz},
				success:function(datasMap){
					$.messager.progress('close');
					if(datasMap.resMsg=="success"){
						$.messager.alert('我的消息',datasMap.resCode);
						setTimeout(
							function shuxin(){
								window.location.href="<%=basePath%>inpatient/auditDrug/auditList.action?menuAlias=BYDHZ"
							}
						,2000);
					}else if(datasMap.resMsg=="error"){
						$.messager.alert("操作提示", datasMap.resCode);
					}
				}
			});
		}else{
			$.messager.alert("操作提示","对不起，没有摆药单，请重新选择");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
	}
}

/**
 * 刷新
 */
function refresh(){
	window.location.href="<%=basePath%>inpatient/auditDrug/auditList.action?menuAlias=BYDHZ";
}

/**
 * 合并单元格
 */
$.extend($.fn.datagrid.methods, {
	autoMergeCells: function (jq, fields) {
		return jq.each(function () {
			var target = $(this);
			if (!fields) {
				fields = target.datagrid("getColumnFields");
			}
			var rows = target.datagrid("getRows");
			var i = 0,
			j = 0,
			temp = {};
			for (i; i < rows.length; i++) {
				var row = rows[i];
				j = 0;
				for (j; j < fields.length; j++) {
					var field = fields[j];
					var tf = temp[field];
					if (!tf) {
						tf = temp[field] = {};
						tf[row[field]] = [i];
					} else {
						var tfv = tf[row[field]];
						if (tfv) {
							tfv.push(i);
						} else {
							tfv = tf[row[field]] = [i];
						}
					}
				}
			}
			$.each(temp, function (field, colunm) {
				$.each(colunm, function () {
				var group = this;
					if (group.length > 1) {
						var before,
						after,
						megerIndex = group[0];
						for (var i = 0; i < group.length; i++) {
							before = group[i];
							after = group[i + 1];
							if (after && (after - before) == 1) {
							    continue;
							}
							var rowspan = before - megerIndex + 1;
							if (rowspan > 1) {
								target.datagrid('mergeCells', {
									index: megerIndex,
									field: field,
									rowspan: rowspan
								});
							}
							if (after && (after - before) != 1) {
							    megerIndex = after;
							}
						}
					}
				});
			});
		});
	}
});



/**
 * 单位渲染
 */
function functionPack(value,row,index){
	if(value!=null&&value!=''){
		return packMap[value];
	}
}

/**
 * 医嘱类型渲染
 */
function functionSubmissive(value,row,index){
	if(value!=null&&value!=''){
		return submissiveMap[value];
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="width:100%;height:40px;">
		<input type="hidden" id="parameterHz" value="${parameter.parameterHz }">
		<input type="hidden" id="parameterTf" value="${parameter.parameterTf }">
		<input type="hidden" id="sendType" value="${parameter.sendType }">
		<input type="hidden" id="controlCode" value="${parameter.controlId }">
		<input type="hidden" id="showLevel" value="${parameter.showLevel }">
		<input type="hidden" id="drugedBill">
		<input type="hidden" id="deptId">
		<table style="width:100%;border:none ;padding:4px;">
			<tr>
				<td nowrap="nowrap">
					摆药台：<input id="controlId" >&nbsp;
					<a href="javascript:void(0)" onclick="examineDrug()" class="easyui-linkbutton" iconCls="icon-user_earth">核准摆药单</a>
					<shiro:hasPermission name="${menuAlias}:function:check">
					<a href="javascript:void(0)" onclick="approvalDrug()" class="easyui-linkbutton" iconCls="icon-user_edit">审批摆药单</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="refund()" class="easyui-linkbutton" iconCls="icon-user_go">退药台</a>
					<a href="javascript:void(0)" onclick="refresh()" class="easyui-linkbutton" iconCls="icon-reload">刷新</a>
				</td>
			</tr>
		</table>
    </div>   
    <div data-options="region:'west',split:true" style="width:15%;" >
    <br> <span id="ts" style="display: none;padding-left: 100px; ">无摆药单信息</span>
		<ul id="tDt"></ul> 
		
    </div>   
    <div data-options="region:'center'" style="width:85%;height:800px" >
		<div id="drugDetail" class="easyui-tabs"  data-options="fit:true">
			<div title="摆药明细记录" style="width:100%;height: 100%" >
				 <table id="deptSummary" class="easyui-datagrid" style="width:100%" data-options="fit:true,border:false">    
				    <thead>   
				        <tr>
				            <th data-options="field:'bedno',width:'100px'"align="center">床号</th>
				            <th data-options="field:'bednoId',hidden:true,width:'50px'">床号</th>  
				            <th data-options="field:'name',width:'100px'"align="center" halign="center">患者姓名</th>   
				            <th data-options="field:'specsName',width:'300px'"align="center" halign="center">药品名称[规格]</th>   
				            <th data-options="field:'retailPrice',width:'100px'"align="right" halign="center">零售价</th> 
				            <th data-options="field:'minUnit',formatter:functionPack,width:'100px'"align="center" halign="center">单位</th>   
				            <th data-options="field:'applyNum',width:'50px'"align="right" halign="center">总量</th>
				            <th data-options="field:'sumCost',width:'100px'" align="right" halign="center">总金额</th>   
				            <th data-options="field:'doseOnce',width:'100px'"align="right" halign="center">每次剂量</th>
				            <th data-options="field:'usageCode',hidden:true,width:'100px'"align="center" halign="center">用法code</th>
				            <th data-options="field:'useName',width:'100px'"align="center" halign="center">用法</th>
				            <th data-options="field:'dfqFreq',hidden:true,width:'100px'"align="center" halign="center">频次code</th>
				            <th data-options="field:'dfqCexp',width:'200px'"align="center" halign="center">频次</th>
				            <th data-options="field:'applyDate',width:'150px'"align="center" halign="center">申请时间</th>
				            <th data-options="field:'applyNumber',width:'100px'"align="center" halign="center">申请单号</th>
				            <th data-options="field:'orderType',formatter:functionSubmissive,width:'100px'"align="center" halign="center">医嘱类型</th>
				            <th data-options="field:'id',hidden:true">id</th>    
				        </tr>   
				    </thead>   
				</table>
			</div>
			<div title="科室汇总记录" style="height:650px">
				<table id="detailed" class="easyui-datagrid" data-options="fit:true,border:false">   
				    <thead>   
				        <tr>
				            <th data-options="field:'specsName',width:'25%'">药品名称[规格]</th>   
				            <th data-options="field:'applyNum',width:'15%'">数量</th>   
				            <th data-options="field:'sumCost',width:'15%'">金额</th>
				            <th data-options="field:'applyNumber',hidden:true,width:'15%'"></th>
				             <th data-options="field:'drugedBill',hidden:true"></th>   
				        </tr>   
				    </thead>   
				</table>
			</div>
		</div>
    </div>   
</div>  
<div style="padding:0px 5px 0px 5px;">
	<div class="easyui-layout" style="width:100%;height:100%;">
		
	</div>
</div>
</body>
</html>