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
 * 员工全局变量
 */
 var empMap = "";
 //存放摆药单号
 var druged='';
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
	var opType=4;
	//申请状态
	var applyState = "7";
	//通知状态
	var sendFlag = "0,1";
	//初始化加载树
	if(sendType!=""&&sendType!=null){
		approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,"");
	}
	
	/**
	 * 切换核准页签
	 */
	 $('#tt').tabs({    
		 border:false,
		 onSelect:function(title){
			 if(title=="未核准"){
				 if(sendType!=""&&sendType!=null){
				 	approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,"");
				 	$("#deptSummary").datagrid('loadData', { total: 0, rows: [] });
			    	$("#detailed").datagrid('loadData', { total: 0, rows: [] });
				 }
			 }else if(title=="已核准"){
				 var applyStates = "2";
				 if(sendType!=""&&sendType!=null){
					 approveDrugTree(controlCode,sendType,opType,applyStates,sendFlag,"");
					 $("#deptSummary").datagrid('loadData', { total: 0, rows: [] });
				     $("#detailed").datagrid('loadData', { total: 0, rows: [] });
				 }
			 }
		 }
	 });
	
	 /**
	 * 切换明细页签
	 */
	$('#drugDetail').tabs({    
	    border:false,    
	    onSelect:function(title){
	    	var tabs = $('#tt').tabs('getSelected');
    		var indexs = $('#tt').tabs('getTabIndex',tabs);
    		var applyState = null;
    		if(indexs==0){
    			applyState = "7";
    		}else{
    			applyState = "2";
    		}
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
						url: "<%=basePath%>inpatient/auditDrug/detailed.action?menuAlias=${menuAlias}",
						queryParams:{"parameter.drugedBill":drugedBill,"parameter.applyState":applyState,"parameter.opType":4,"parameter.deptId":deptId},
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
	    		$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action?menuAlias=${menuAlias}",
					queryParams:{"parameter.drugedBill":drugedBill,"parameter.applyState":applyState,"parameter.opType":4},
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
	    	}
	   	 }    
	});
});

/**
 * 加载摆药通知单树(未核准)
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
                $("#ts1").show();
             }else{
            	 $("#ts1").hide();
             }   
         },
		onClick: function(node){//点击节点
			if(node.attributes.type=="2"){
				$('#drugDetail').tabs('select','摆药明细记录');
				$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action",
					queryParams:{"parameter.drugedBill":node.id,"parameter.applyState":"7","parameter.opType":4},
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
					} 
				});
				$('#drugedBill').val(node.id);
				$('#deptId').val(node.attributes.dept);
			}
		}
	});
}

/**
 * 加载摆药通知单树(已核准)
 */
function approveDrugTree(controlCode,sendType,opType,applyState,sendFlag,medicalrecordId){
	$('#tEt').tree({    
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
                $("#ts2").show();
             }else{
            	 $("#ts2").hide();
             }   
         },
		onDblClick: function(node){//点击节点
			if(node.attributes.type=="2"){
				$('#drugDetail').tabs('select','摆药明细记录');
				$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findDeptSummary.action",
					queryParams:{"parameter.drugedBill":node.id,"parameter.applyState":"2","parameter.opType":4},
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
					} 
				});
				$('#drugedBill').val(node.id);
				$('#deptId').val(node.attributes.dept);
			}
		}
	});
}


/**
 * 核准操作
 */
function examineDrug(){
	var tabs = $('#tt').tabs('getSelected');
	var indexs = $('#tt').tabs('getTabIndex',tabs);
	if(indexs==0){
		//获取当天选择标签页
		var tab = $('#drugDetail').tabs('getSelected');
		var index = $('#drugDetail').tabs('getTabIndex',tab);
		if(index==0){//摆药明细记录
			var listRows = $('#deptSummary').datagrid('getChecked');
			var applyNumber = "";
			var ids = "";
			if(listRows.length>0){
				for(var i =0;i<listRows.length;i++){
					if(applyNumber!=""){
						applyNumber = applyNumber + ",";
					}
					applyNumber = applyNumber + listRows[i].applyNumber;
					if(ids!=""){
						ids = ids = "','";
					}
					ids = ids + listRows[i].id;
				}
				
				$.messager.progress({text:'正在核准，请稍后。。。'});
				$.ajax({
					url: "<%=basePath%>inpatient/auditDrug/examineDrugSaveAndUpdate.action",
					type:'post',
					data:{"parameter.applyNumberCode":applyNumber,"parameter.ids":ids},
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
				$.messager.alert("操作提示","对不起，没有摆药单，请重新选择");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
		}else{//科室汇总记录
			var listRows = $('#detailed').datagrid('getSelections');
			if(listRows.length<=0){
				$.messager.alert("操作提示","对不起，没有摆药单，请重新选择");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
			var deptId = $('#deptId').val();
			$.messager.progress({text:''});
			$.ajax({
				url: "<%=basePath%>inpatient/auditDrug/approvalDeptSave.action",
				type:'post',
				data:{"deptId":deptId},
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
		}
	}else{
		$.messager.alert("操作提示","已经是核准状态请不要做重复操作");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
	}
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
 * 返回主页面摆药操作
 */
function returns(){
	window.location.href="<%=basePath%>inpatient/auditDrug/auditList.action?menuAlias=BYDHZ";
}

/**
 * 刷新
 */
function refresh(){
 //获得摆药台ID
	var controlCode = $('#controlCode').val();
	//获得发送类型
	var sendType = $('#sendType').val();
	//常量 1 门诊发药 ， 2 内部入库,3 门诊退药，4 住院摆药 ,5住院退药
	var opType=4;
	//申请状态
	var applyState = "7";
	//通知状态
	var sendFlag = "0,1";
	//初始化加载树
	$('#tt').tabs('select',"未核准");
	$('#drugDetail').tabs('select',"摆药明细记录");
	if(sendType!=""&&sendType!=null){
		approveNoDrugTree(controlCode,sendType,opType,applyState,sendFlag,"");
	}
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


function examTreetDt(drugHouse,outState){
	$('#tDt').tree({    
		url:"<%=basePath%>inpatient/auditDrug/examineNoDrugTree.action?drugHouse="+drugHouse+"&outstore.outState="+outState,
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
                $("#ts1").show();
             }else{
            	 $("#ts1").hide();
             }   
         },
		onDblClick: function(node){//点击节点
			if(node.attributes.type=="1"){
				$('#drugDetail').tabs('select','摆药明细记录');
				$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findExamineDeptSummary.action?menuAlias=${menuAlias}",
					queryParams:{"applyout.drugedBill":node.id,"applyout.opType":1},
			        onDblClickRow: function (rowIndex, rowData) {//双击查看
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
				$('#billNo').val(node.id);
				$('#deptId').val(node.attributes.dept);
			}
		}
	});
}

function examTreetEt(drugHouse,outState){
	$('#tEt').tree({    
		url:"<%=basePath%>inpatient/auditDrug/examineNoDrugTree.action?drugHouse="+drugHouse+"&outstore.outState="+outState,
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
                $("#ts2").show();
             }else{
            	 $("#ts2").hide();
             }   
         },
		onDblClick: function(node){//点击节点
			if(node.attributes.type=="1"){
				$('#drugDetail').tabs('select','摆药明细记录');
				$('#deptSummary').datagrid({
					url: "<%=basePath%>inpatient/auditDrug/findExamineDeptSummary.action?menuAlias=${menuAlias}",
					queryParams:{"applyout.drugedBill":node.id,"applyout.opType":2},
			        onDblClickRow: function (rowIndex, rowData) {//双击查看
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
				$('#billNo').val(node.id);
				$('#deptId').val(node.attributes.dept);
			}
		}
	});
}

</script>	
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="width:85%;height:40px;">
		<table style="width:100%;border:none;padding:4px;">
			<tr>
				<td>
					<shiro:hasPermission name="${menuAlias}:function:approve">
					<a href="javascript:void(0)" onclick="examineDrug()" class="easyui-linkbutton" iconCls="icon-user_earth">核准摆药单</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="refresh()" class="easyui-linkbutton" iconCls="icon-2012080412111">刷新</a>
					<a href="javascript:void(0)" onclick="returns()" class="easyui-linkbutton" iconCls="icon-2012080412301">返回</a>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'west'" style="width:15%;" >
		<div id="tt" class="easyui-tabs" data-options="fit:true">   
		    <div title="未核准" style="height:100%">   
		    	<br> <span id="ts1" style="display: none;padding-left: 100px; ">无未核准信息</span>
		        <ul id="tDt"></ul> 
		    </div>   
		    <div title="已核准" style="height:100%">
		    	<br> <span id="ts2" style="display: none;padding-left: 100px; ">无已核准信息</span>   
		        <ul id="tEt"></ul> 
		    </div>   
		</div>  
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
				<div title="摆药明细记录" data-options="fit:true">
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
					            <th data-options="field:'specsName',width:'20%'">药品名称[规格]</th>   
					            <th data-options="field:'applyNum',width:'15%'">数量</th>   
					            <th data-options="field:'sumCost',width:'15%'">金额</th> 
					             <th data-options="field:'drugedBill',hidden:true"></th>   
					        </tr>   
					    </thead>   
					</table>
				</div>
			</div>
    </div>
</div>
</body>
</html>