<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出院患者医嘱查询</title>
</head>
<body style="margin: 0px;padding: 1px;">
<div class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
	<div class="easyui-layout leaveOrderListSize" style="width:100%;height:32px;padding:2px 1px 1px 1px;" data-options="region:'north',border:false">
		&nbsp;病历号:<input class="easyui-textbox" id="blh" style="width:200px" data-options="prompt:'请输入病历号'"/>
					<input type="hidden" id="inpatientNo">
		&nbsp;医嘱开立时间:
		<input id="start" class="Wdate" type="text" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		 至
		 <input id="end" class="Wdate" type="text" value="${endTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		<a href="javascript:query();void(0)" data-options="iconCls:'icon-search'" class="easyui-linkbutton">查询</a>
		<a href="javascript:clear();void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton">清空</a>
	</div>
	<div class="easyui-layout" style="width:100%;hight:90%;order:0px;" data-options="region:'center',iconCls:'icon-book',split:true">
		<div id="exeEt" class="easyui-tabs" data-options="tabPosition:'bottom',fit:true,border:false" style="width:100%;height:100%;">
			<div title="药品">
				<table id="exeDrugEd"></table>
			</div>
			<div title="非药品">
				<table id="exeUnDrugEd"></table>
			</div>
		</div>
	</div>
	<div id="dialog"></div>  
</div>
<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:600;padding:5" data-options="modal:true, closed:true">   
	<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
	</table>
</div>
<script type="text/javascript">
var list;
var tab=0;
function clear(){
	$('#start').val("");
	$('#end').val("");
	$('#blh').textbox('setValue',"");
	$('#inpatientNo').val("");
	$('#exeDrugEd').datagrid({
		url:'<%=basePath%>statistics/leaveOrder/queryDrugLists.action',
		queryParams:{
			queryLsh:''
		}
	});
	$('#exeUnDrugEd').datagrid({
		url:'<%=basePath%>statistics/leaveOrder/queryUnDrugList.action',
		queryParams:{
			queryLsh:''
		}
	});
}
function query(){
	var start=$('#start').val();
	var end=$('#end').val();
	var blh=$('#blh').val();
	if(blh==null||blh==""){
		$.messager.alert("提示","请输入病历号!");
		return;
	}
	if(start&&end){
		if(start>=end){
			$.messager.alert("提示","结束时间必须大于开始时间");
			return;
		}
	}
	$.messager.progress({text:'查询中，请稍后...',modal:true});
	$.ajax({
		url:'<%=basePath%>statistics/leaveOrder/queryLSHList.action',
		type : 'post',
		data:{
			queryBlh:blh,
			startTime:start,
			endTime:end 
		},
		success:function (data){
			$.messager.progress('close');
			if(data.length==1){
				$('#inpatientNo').val(data[0].inpatientNo);
				queryByChecks(data[0].inpatientNo);
			}else if(data.length>1){
				$("#diaInpatient").window('open');
				$("#infoDatagrid").datagrid({
					data:data,
					rownumbers:true,
		   			fitColumns:true,
		   			checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
		   			columns:[[
						{field:'inpatientNo',title:'住院流水号',width : '30%'},
						{field:'outDate',title:'出院日期', width : '30%'},
						{field:'inDate',title:'入院日期', width : '33%' },
						]],
					onDblClickRow:function(rowIndex, rowData){
						$("#diaInpatient").window('close');
						$('#inpatientNo').val(rowData.inpatientNo);
						queryByChecks(rowData.inpatientNo);
					}
				});
			}else{
				$.messager.alert("提示","未查询到患者!");
				return;
			}
		}
	})
}

function queryByChecks(lsh){
	var start=$('#start').val();
	var end=$('#end').val();
	var qqs = $('#exeEt').tabs('getSelected');				
	var tabs = qqs.panel('options');
	 //如果为第一选项卡
	if(tabs.title=='药品'){
		$('#exeDrugEd').datagrid({
			url:'<%=basePath%>statistics/leaveOrder/queryDrugLists.action',
			queryParams:{
				queryLsh:lsh,
				startTime:start,
				endTime:end
			}
		});
	}else if (tabs.title=='非药品'){
		$('#exeUnDrugEd').datagrid({
			url:'<%=basePath%>statistics/leaveOrder/queryUnDrugList.action',
			queryParams:{
				queryLsh:lsh,
				startTime:start,
				endTime:end
			}
		});
	}
}


$(function(){
	
	bindEnterEvent('blh',query,'easyui');
	$('#exeEt').tabs({
		onSelect:function(title,index){
			var lsh=$('#inpatientNo').val();
			var start=$('#start').val();
			var end=$('#end').val();
			if(index==0){
				$('#exeDrugEd').datagrid({
					url:'<%=basePath%>statistics/leaveOrder/queryDrugLists.action',
					queryParams:{
						queryLsh:lsh,
						startTime:start,
						endTime:end
					}
				});
			}else if (index==1){
				$('#exeUnDrugEd').datagrid({
					url:'<%=basePath%>statistics/leaveOrder/queryUnDrugList.action',
					queryParams:{
						queryLsh:lsh,
						startTime:start,
						endTime:end
					}
				});
			}
		}
	});
	/**  
	 * 医嘱执行查询-非药品列表 
	 */
	$('#exeUnDrugEd').datagrid({ 
		method:'post',
		rownumbers:true,
		striped:true,
		border:false,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fitColumns:false,
		pagination:true,
		fit:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		columns: [  
			[  	{field:'ck',checkbox:true},
				{field:'docName',title:'开立医生',width:100,align:'center'}, 
				{field:'typeCode',title:'医嘱类型',width:80,align:'center'},
				{field:'validFlag',title:'有效',width:80,align:'center',formatter:forValidFlag},
				{field:'undrugName',title:'项目名称',width:180,align:'center'},
				{field:'stockUnit',title:'单位',width:80,align:'center'},
				{field:'useTime',title:'应执行时间',width:180,align:'center'},
				{field:'decoDate',title:'分解时间',width:180,align:'center'},
				{field:'chargeDate',title:'记账时间',width:180,align:'center'},
				{field:'moDate',title:'医嘱时间',width:180,align:'center'},
				{field:'validDate',title:'停止时间',width:180,align:'center'},
				{field:'dfqCexp',title:'频次',width:100,align:'center'},
				{field:'moNote1',title:'医嘱说明',width:100,align:'center'},
				{field:'moNote2',title:'备注',width:100,align:'center'},
				{field:'moOrder',title:'医嘱号',width:100,align:'center'},   
				{field:'combNo',title:'组合号',width:80,align:'center',formatter:functionGroupExeUnDrugEd},
				{field:'execId',title:'执行号',width:80,align:'center'},  
				{field:'subtblFlag',title:'是否附材',width:80,align:'center',formatter:forYserOrNo}, 
				{field:'execDpnm',title:'执行科室',width:180,align:'center'},
				{field:'chargeFlag',title:'记账标记',width:80,align:'center',formatter:forChargeFlag}, 
				{field:'chargeUsercd',title:'记账人',width:100,align:'center'},
				{field:'validUsercd',title:'停止人',width:100,align:'center'},
				{field:'recipeNo',title:'处方号',width:100,align:'center'},
				{field:'sequenceNo',title:'处方内流水号',width:100,align:'center'},
			]  
		]
	});
	/**  
	 * 医嘱执行查询-药品列表
	 */
	$('#exeDrugEd').datagrid({  
		method:'post',
		rownumbers:true,
		striped:true,
		border:false,
		checkOnSelect:true,
		fitColumns:false,
		pagination:true,
		fit:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		columns: [  
			[  	
			   	{field:'prnFlag',title:'标记',width:80,align:'center',formatter:forPrnFlag},
				{field:'docName',title:'开立医生',width:100,align:'center'},  
				{field:'typeCode',title:'医嘱类型',width:100,align:'center'},
				{field:'validFlag',title:'有效',width:80,align:'center',formatter:forValidFlag},
				{field:'drugedFlag',title:'发送状态',width:80,align:'center',formatter:forDrugedFlag},
				{field:'drugName',title:'药品名称',width:150,align:'center'},
				{field:'specs',title:'规格',width:100,align:'center'},
				{field:'qtyTot',title:'用量',width:100,align:'center'},
				{field:'priceUnit',title:'单位',width:100,align:'center'},
				{field:'useTime',title:'应执行时间',width:180,align:'center'},
				{field:'decoDate',title:'分解时间',width:180,align:'center'},
				{field:'chargeDate',title:'记账时间',width:180,align:'center'},
				{field:'drugedDate',title:'发药时间',width:180,align:'center'}, 
				{field:'moDate',title:'医嘱时间',width:180,align:'center'},   
				{field:'frequencyName',title:'频次',width:100,align:'center'},   
				{field:'doseOnce',title:'每次剂量',width:100,align:'center'}, 
				{field:'packQty',title:'包装数',width:80,align:'center'},   
				{field:'useDays',title:'付数',width:80,align:'center'}, 
				{field:'useName',title:'用法',width:100,align:'center'}, 
				{field:'pharmacyCode',title:'取药药房',width:150,align:'center'},   
				{field:'moNote1',title:'医嘱说明',width:150,align:'center'},   
				{field:'moNote2',title:'备注',width:150,align:'center'}, 
				{field:'moOrder',title:'医嘱号',width:100,align:'center'},   
				{field:'combNo',title:'组合号',width:80,align:'center',formatter:functionGroupExeDrugEd},   
				{field:'execId',title:'执行号',width:100,align:'center'}, 
				{field:'chargeFlag',title:'记账标记',width:100,align:'center',formatter:forChargeFlag}, 
				{field:'chargeUsercd',title:'记账人',width:100,align:'center'},
				{field:'drugedDeptcd',title:'发药科室',width:120,align:'center'},
				{field:'drugedUsercd',title:'发药人',width:100,align:'center'},
				{field:'validUsercd',title:'停止人',width:100,align:'center'},
				{field:'recipeNo',title:'处方号',width:100,align:'center'},   
				{field:'sequenceNo',title:'处方内流水号',width:100,align:'center'},   
				{field:'chargePrnflag',title:'发送单打印标记',width:100,align:'center'}, 
				{field:'chargePrndate',title:'打印时间',width:100,align:'center'},   
				{field:'deptCode',title:'住院科室',width:120,align:'center'},
				{field:'nurseCellCode',title:'护理站',width:120,align:'center'},
				{field:'listDpcd',title:'开立科室',width:120,align:'center'}
			]  
		]
	});
	$("#exeDrugEd").datagrid('loadData', { total: 0, rows: [] });
	$("#exeUnDrugEd").datagrid('loadData', { total: 0, rows:[]});
});
	
	/**  
	 * 全局-未打印与已打印的渲染
	 */
	function forPrnFlag(value,row,index){
		if (value==0){
			return '未打印';
		} else {
			return '已打印';
		}
	}
	/**  
	 * 全局-有效与作废的渲染
	 */
	function forValidFlag(value,row,index){
		if (value==0){
			return '作废';
		} else {
			return '有效';
		}
	}
	/**  
	 * 全局-0不需发送/1集中发送/2分散发送/3已配药
	 */
	function forDrugedFlag(value,row,index){
		if (value==0){
			return '不需发送';
		} else if (value==1) {
			return '集中发送';
		} else if (value==2) {
			return '分散发送';
		} else if (value==3) {
			return '已配药';
		} else {
			return '';
		}
	}
	
	/**  
	 * 医嘱执行查询-药品执行信息组合号渲染
	 */
	function functionGroupExeDrugEd(value,row,index){
		return functionGroup('exeDrugEd',value,row,index);
	}
	
	/**  
	 * 医嘱执行查询-非药品执行信息组合号渲染
	 */
	function functionGroupExeUnDrugEd(value,row,index){
		return functionGroup('exeUnDrugEd',value,row,index);
	}
	/**  
	 * 全局-组合渲染
	 */
	function functionGroup(id,value,row,index){
		var rwos = $('#'+id).datagrid('getRows');
		if(value==null||value==''){
			return null;
		}else{
			if(index==0&&rwos.length>1&&value==rwos[index+1].combNo){
				return "┓";
			}else if(index==0){
				return "";
			}else if(index==rwos.length-1&&value==rwos[index-1].combNo){
				return "┛";
			}else if(index==rwos.length-1){
				return "";
			}else if(value!=rwos[index-1].combNo&&value==rwos[index+1].combNo){
				return "┓";
			}else if(value==rwos[index-1].combNo&&value!=rwos[index+1].combNo){
				return "┛";
			}else if(value==rwos[index-1].combNo&&value==rwos[index+1].combNo){
				return "┫";
			}else{
				return "";
			}
		}
	}
	/**  
	 * 全局-记账标记0待记账/1已
	 */
	function forChargeFlag(value,row,index){
		if (value==0){
			return '待记账';
		} else {
			return '已记账';
		}
	}
	/**  
	 * 全局-是与否的渲染
	 */
	function forYserOrNo(value,row,index){
		if (value==0){
			return '否';
		} else {
			return '是';
		}
	}
</script>
</body>
</html>