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
/**
 * 医嘱类型全局变量
 */
var submissive ="";
/**
 * 单位全局变量
 */
var packMap = "";
/**
 * 加载页面
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
	
	//获得摆药台ID
	var controlCode = $('#controlCode').val();
	//获得发送类型
	var sendType = $('#sendType').val();
	//常量 1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药
	var opType=5;
	//申请状态
	var applyState = "5,6";
	//通知状态
	var sendFlag = "0";
	//初始化加载树
	if(sendType!=""&&sendType!=null){
		approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,"");
	}
	
	//实现同时切换tab效果药品
	$('#drugDetail').tabs({    
	    border:false,    
	    onSelect:function(title){
	    	if(title=="科室汇总记录"){
	    		var drugedBill = $('#drugedBill').val();
	    		var deptId = $('#deptId').val();
	    		var showLevel = $('#showLevel').val();
    			$('#detailed').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/detailed.action",
					queryParams:{"parameter.drugedBill":drugedBill,"parameter.applyState":"5,6","parameter.opType":5,"parameter.deptId":deptId}
				});
	    	}else{
	    		var drugedBill = $('#drugedBill').val();
	    		$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action",
					queryParams:{"parameter.drugedBill":drugedBill,"parameter.applyState":"5,6","parameter.opType":5},
					onLoadSuccess: function (data) {//默认选中
						$('#deptSummary').datagrid("autoMergeCells", ['name','bedno']);
					} 
				});
	    	}
	   	 }    
	});
});

/**
 * 加载摆药通知单树
 */
function approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,medicalrecordId){
	$('#tDt').tree({    
		url:"<%=basePath%>inpatient/auditDrug/approveNoDrugTree.action?parameter.controlId="+controlCode+"&parameter.sendType="+sendType+"&parameter.opType="+opType+"&parameter.applyState="+applyState+"&parameter.sendFlag="+sendFlag+"&parameter.medicalrecordId="+medicalrecordId,
	    method:'get',
	    animate:true,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			if (node.children){
				s += (node.children.length==0)?'':'&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
			}
			return s;
		},onLoadSuccess : function(node, data) {  
             if (data.length==0) {
                $("#ts3").show();
             }else{
            	 $("#ts3").hide();
             }   
         },
		onDblClick: function(node){//点击节点
			if(node.attributes.type=="2"){
				$('#drugDetail').tabs('select','退药明细记录');
				$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action",
					queryParams:{"parameter.drugedBill":node.id,"parameter.applyState":"5,6","parameter.opType":5},
					onLoadSuccess: function (data) {//默认选中
						$('#deptSummary').datagrid("autoMergeCells", ['name','bedno']);
					} 
				});
				$('#drugedBill').val(node.id);
				$('#deptId').val(node.attributes.dept);
			}
		}
	});
}

/**
 * 返回操作
 */
function returnDrug(){
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
 * 刷新
 */
function refresh(){
	//获得摆药台ID
	var controlCode = $('#controlCode').val();
	//获得发送类型
	var sendType = $('#sendType').val();
	//常量 1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药
	var opType=5;
	//申请状态
	var applyState = "5,6";
	//通知状态
	var sendFlag = "0";
	//初始化加载树
	if(sendType!=""&&sendType!=null){
		approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,"");
	}
	$('#drugDetail').tabs('select',"退药明细记录");
	$("#deptSummary").datagrid('loadData', { total: 0, rows: [] });
	$("#detailed").datagrid('loadData', { total: 0, rows: [] });
}

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

/**
 * 退药过程
 */
function withdrawalDrug(){
	var parameterHz = $('#parameterHz').val();
	var parameterTf = $('#parameterTf').val();
	var tab = $('#drugDetail').tabs('getSelected');
	var index = $('#drugDetail').tabs('getTabIndex',tab);
	if(index==0){
		var listRows = $('#deptSummary').datagrid('getChecked');
		var applyNumber = "";
		if(listRows.length>0){
			for(var i=0;i<listRows.length;i++){
				if(applyNumber!=""){
					applyNumber = applyNumber + ",";
				}
				applyNumber = applyNumber + listRows[i].applyNumber;
			}
			$.messager.progress({text:'正在退药，请稍后。。。'});
			$.ajax({
				url: "<%=basePath%>inpatient/auditDrug/withdrawalDrug.action",
				type:'post',
				data:{"parameter.applyNumberCode":applyNumber,"parameter.parameterTf":parameterTf},
				success:function(datasMap){
					$.messager.progress('close');
					if(datasMap.resMsg=="success"){
						$.messager.alert('我的消息',datasMap.resCode);
						refresh();
					}else if(datasMap.resMsg=="error"){
						$.messager.alert("操作提示", datasMap.resCode);
					}
				}
			});
		}else{
			$.messager.alert("操作提示","对不起，没有退药单，请重新选择");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
	}else{
		var listRows = $('#detailed').datagrid('getChecked');
		var applyNumber = "";
		if(listRows.length>0){
			for(var i =0;i<listRows.length;i++){
				if(applyNumber!=""){
					applyNumber = applyNumber + ",";
				}
				applyNumber = applyNumber + listRows[i].applyNumber;
			}
			$.messager.progress({text:'正在退药，请稍后。。。'});
			$.ajax({
				url: "<%=basePath%>inpatient/auditDrug/withdrawalDrug.action",
				type:'post',
				data:{"parameter.applyNumberCode":applyNumber,"parameter.parameterTf":parameterTf},
				success:function(datasMap){
					$.messager.progress('close');
					if(datasMap.resMsg=="success"){
						$.messager.alert('操作提示',datasMap.resCode);
						refresh();
					}else if(datasMap.resMsg=="error"){
						$.messager.alert("操作提示", datasMap.resCode);
					}
				}
			});
		}else{
			$.messager.alert("操作提示","对不起，没有退药单，请重新选择");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
	}
}

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="width:100%;height:40px;">
    	<table style="width:100%;border:none;padding:4px;">
			<tr>
				<td>
				  <shiro:hasPermission name="${menuAlias}:function:returnDrug">
					<a href="javascript:void(0)" onclick="withdrawalDrug()" class="easyui-linkbutton" iconCls="icon-basket_remove">退药</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="returnDrug()" class="easyui-linkbutton" iconCls="icon-2012080412301">返回</a>
					<a href="javascript:void(0)" onclick="refresh()" class="easyui-linkbutton" iconCls="icon-2012080412111">刷新</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'west'" style="width:15%;" >
    	<br> <span id="ts3" style="display: none;padding-left: 100px; ">无退药信息</span>
		<ul id="tDt"></ul> 
    </div>
    <div data-options="region:'center'" style="width:84%;height:800px" >
    	<input type="hidden" id="parameterHz" value="${parameter.parameterHz }">
			<input type="hidden" id="parameterTf" value="${parameter.parameterTf }">
			<input type="hidden" id="sendType" value="${parameter.sendType }">
			<input type="hidden" id="controlCode" value="${parameter.controlId }">
			<input type="hidden" id="showLevel" value="${parameter.showLevel }">
			<input type="hidden" id="drugedBill">
			<input type="hidden" id="deptId">
			<div id="drugDetail" class="easyui-tabs" data-options="fit:true">
				<div title="退药明细记录" data-options="fit:true">
					 <table id="deptSummary" class="easyui-datagrid" data-options="fit:true">    
					    <thead>   
					        <tr>
					        	<th data-options="field:'ck',checkbox:true" ></th> 
					            <th data-options="field:'bedno',width:'5%'">床号</th>
					            <th data-options="field:'bednoId',hidden:true,width:'5%'">床号</th>  
					            <th data-options="field:'name',width:'8%'">患者姓名</th>   
					            <th data-options="field:'specsName',width:'15%'">药品名称[规格]</th>   
					            <th data-options="field:'retailPrice',width:'5%'">零售价</th> 
					            <th data-options="field:'minUnit',formatter:functionPack,width:'10%'">单位</th>   
					            <th data-options="field:'applyNum',width:'5%'">总量</th>
					            <th data-options="field:'sumCost',width:'5%'">总金额</th>   
					            <th data-options="field:'doseOnce',width:'5%'">每次剂量</th>
					            <th data-options="field:'usageCode',hidden:true,width:'5%'">用法code</th>
					            <th data-options="field:'useName',width:'8%'">用法</th>
					            <th data-options="field:'dfqFreq',hidden:true,width:'5%'">频次code</th>
					            <th data-options="field:'dfqCexp',width:'8%'">频次</th>
					            <th data-options="field:'applyDate',width:'10%'">申请时间</th>
					            <th data-options="field:'applyNumber',width:'5%'">申请单号</th>
					            <th data-options="field:'orderType',formatter:functionSubmissive,width:'5%'">医嘱类型</th>
					            <th data-options="field:'id',hidden:true">id</th>    
					        </tr>   
					    </thead>   
					</table>
				</div>
				<div title="科室汇总记录" data-options="fit:true">
					<table id="detailed" class="easyui-datagrid" data-options="fit:true">   
					    <thead>   
					        <tr>
					        	<th data-options="field:'ck',checkbox:true" ></th> 
					            <th data-options="field:'specsName',width:'20%'">药品名称[规格]</th>   
					            <th data-options="field:'applyNum',width:'15%'">数量</th>   
					            <th data-options="field:'sumCost',width:'15%'">金额</th>
					            <th data-options="field:'applyNumber',hidden:true,width:'15%'"></th>  
					        </tr>   
					    </thead>   
					</table>
				</div>
			</div>
    </div>
</div>
</body>
</html>