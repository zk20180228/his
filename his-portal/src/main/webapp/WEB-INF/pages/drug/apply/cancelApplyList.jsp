<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>退费申请取消</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.tableCss {
	border-collapse: collapse;
	border-spacing: 0;
	border-left: 1px solid #95b8e7;
	border-top: 1px solid #95b8e7;
}

.tableLabel {
	text-align: right;
	width: 150px;
}

/* .tableCss td {
	border-right: 1px solid #95b8e7;
	border-bottom: 1px solid #95b8e7;
	padding: 5px 15px;
	word-break: keep-all;
	white-space: nowrap;
} */
.tableCss td {
	border-right: 0px;
	border-bottom: 0px;
	padding: 5px 15px;
	word-break: keep-all;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
//渲染表单中的包装单位
var unitMap="";
//合同单位渲染
var pactCodeMap="";  

//费用代码
var miniCostMap=""
//列表中所有药品
var drug;
//列表中所有非药品
var unDrug;
// var sexMap=new Map();
//存放所有手术序号
var lst=new Array();
$(function(){
	
	funformat();
	
	bindEnterEvent('medicalrecordId',searchEvent,'easyui');
	
});
/*******************************开始读卡***********************************************/
//定义一个事件（读卡）
function read_card_ic(){
	var card_value=app.read_ic();
	if(card_value=='0'||card_value==undefined||card_value==''){
		$.messager.alert('提示','此卡号['+card_value+']无效');
		return;
	}
	 $.ajax({
		url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
		data:{idcardOrRe:card_value},
		type:'post',
		async:false,
		success: function(data) { 
			if(data==null||data==''){
				$.messager.alert('提示','此卡号无效');
				return;
			}
			$('#medicalrecordId').textbox('setValue',data);
			searchEvent();
		}
	 });	
};
/*******************************结束读卡***********************************************/
/*******************************开始读身份证***********************************************/
//定义一个事件（读身份证）
	function read_card_sfz(){
		var card_value = app.read_sfz();
		if(card_value=='0'||card_value==undefined||card_value==''){
		$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		 $.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe : card_value},
			type:'post',
			async:false,
			success: function(data) {
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#medicalrecordId').textbox('setValue',data);
				searchEvent();
			}
		 });
	};
/*******************************结束读身份证***********************************************/
<%----------------------------------------------------------------  弹框 事件  	 --------------------------------------------------------------------------%>
function searchEvent(){
	var medicalrecordId = $('#medicalrecordId').textbox('getValue');	 
	if(medicalrecordId == ''){
		$.messager.alert('提示','请输入病历号！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return false;
	}
	$.ajax({    
		url: "<%=basePath%>inpatient/applydrug/searchPatinent.action",
		data:{'medicalrecordId':medicalrecordId},
		type:'post',
		success: function(data) {
			var dataObj =data;
			if(dataObj.length==0){
				$.messager.alert('提示',"现登录信息中，无此患者！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				if(dataObj.length>1){
					$("#diaInpatient").window('open');
					$("#diaInpatient").window('center');
					$("#infoDatagrid").datagrid({
						data:dataObj,
						 columns:[[    
							        {field:'inpatientNo',title:'住院流水号',width:'20%',align:'center'} ,    
							        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
							        {field:'name',title:'姓名',width:'20%',align:'center'} ,   
							        {field:'reportSexName',title:'性别',width:'20%',align:'center'},
							        {field:'certificatesNo',title:'证件号',width:'20%',align:'center'} 
							   ]] ,
					    onDblClickRow:function(rowIndex, rowData){
					    	//获得住院流水号
							$('#inpatientNo').val(rowData.inpatientNo);  
					    	//获得病历号
							$('#medicalrecordId1').val(rowData.medicalrecordId);  
							//获得姓名
							$('#name').textbox('setValue',rowData.name);    
							//获得合同单位
							$('#pactCode').textbox('setValue',pactCodeMap[rowData.pactCode]); 
							//住院科室
							$('#inhosDept').textbox('setValue',rowData.inhosDeptName);
							//床号
							$('#bedId').textbox('setValue',rowData.bedName); 
							$('#diaInpatient').window('close'); 
							
							loadTabs(rowData.medicalrecordId,rowData.inpatientNo);
					    }
					}); 
				}else{
					//获得住院流水号
					$('#inpatientNo').val(dataObj[0].inpatientNo);
					//获得病历号
					$('#medicalrecordId1').val(dataObj[0].medicalrecordId);  
					//获得姓名
					$('#name').textbox('setValue',dataObj[0].name);    
					//获得合同单位
					$('#pactCode').textbox('setValue',pactCodeMap[dataObj[0].pactCode]); 
					//住院科室
					$('#inhosDept').textbox('setValue',dataObj[0].inhosDeptName);
					//床号
					$('#bedId').textbox('setValue',dataObj[0].bedName);   
					
					loadTabs(dataObj[0].medicalrecordId,dataObj[0].inpatientNo);
				}
			}
		}
	});	
}

/* 加载tabs信息 */
function loadTabs(blh,inpatientNo){
	var objName = $('#objname').textbox('getValue');
	var firstDate = $('#firstDate').textbox('getValue');
	var endDate = $('#endDate').textbox('getValue');
	
	$('#listDrugBack').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryDrugBack.action?menuAlias=${menuAlias}',
		queryParams:{
			medicalrecordId:blh,
			inpatientNo:inpatientNo
		},
		onLoadSuccess: function (data) {
			drug=data;
		},
		onClickRow:function(rowIndex, rowData){
			var operation='';
			var flg=0;
			var rows=$('#listDrugBack').datagrid('getSelections');
			if(rowData.operationId!=null&&rowData.operationId!=""){
				operation = rowData.operationId;
					for(var i=0;i<drug.rows.length;i++){
						if(drug.rows[i].operationId==rowData.operationId){
							var index=$('#listDrugBack').datagrid('getRowIndex',drug.rows[i]);
						    $('#listDrugBack').datagrid('selectRow',index);
							
						}
					}
					for(var i=0;i<unDrug.rows.length;i++){
						if(unDrug.rows[i].operationId==rowData.operationId){
							var index=$('#listNotDrugBack').datagrid('getRowIndex',unDrug.rows[i]);
							$('#listNotDrugBack').datagrid('selectRow',index);
						}
					}
			}
			
			for(var i = 0;i < lst.length;i ++){
				if(lst[i] == operation){
					lst[i]="";
					flg=1;
				}
			}
			if(rowData.operationId!=null&&rowData.operationId!=""){
				if(flg == 0){
					$.messager.alert('提示','手术药品及非药品必须一次性全部取消退费申请！！');
					lst[lst.length+1] = operation;
				}
			}
			
		},
		onUnselect:function(rowIndex, rowData){
			if(rowData){
			 	if(rowData.operationId!=null&&rowData.operationId!=""){
			 		 $.messager.confirm('确认','手术药品及非药品必须一次性全部取消退费申请，确认全部取消吗？',function(r){    
					    if (r){  
					    	var rows=$('#listDrugBack').datagrid('getSelections');
							$('#listDrugBack').datagrid('clearSelections');
							for(var i=0;i<rows.length;i++){
								if(rows[i].operationId!=rowData.operationId){
									var index=$('#listDrugBack').datagrid('getRowIndex',rows[i]);
									$('#listDrugBack').datagrid('selectRow',index);
								}
							}
							var rowss=$('#listNotDrugBack').datagrid('getSelections');
							$('#listNotDrugBack').datagrid('clearSelections');
							for(var i=0;i<rowss.length;i++){
								if(rowss[i].operationId!=rowData.operationId){
									var index=$('#listNotDrugBack').datagrid('getRowIndex',rowss[i]);
									$('#listNotDrugBack').datagrid('selectRow',index);
								}
							}
					    }else{
							  lst[lst.length+1] = rowData.operationId;
						  }
					  })
				}
			}
			
		}
	});
	
	$('#listNotDrugBack').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryNotDrugBack.action?menuAlias=${menuAlias}',
		queryParams:{
				medicalrecordId:blh,
				inpatientNo:inpatientNo
		},
		onLoadSuccess: function (data) {
			unDrug=data;
		},
		onClickRow:function(rowIndex, rowData){
			var operation='';
			var flg=0;
			var rows=$('#listDrugBack').datagrid('getSelections');
			if(rowData.operationId!=null&&rowData.operationId!=""){
				operation = rowData.operationId;
					for(var i=0;i<drug.rows.length;i++){
						if(drug.rows[i].operationId==rowData.operationId){
							var index=$('#listDrugBack').datagrid('getRowIndex',drug.rows[i]);
						    $('#listDrugBack').datagrid('selectRow',index);
							
						}
					}
					for(var i=0;i<unDrug.rows.length;i++){
						if(unDrug.rows[i].operationId==rowData.operationId){
							var index=$('#listNotDrugBack').datagrid('getRowIndex',unDrug.rows[i]);
							$('#listNotDrugBack').datagrid('selectRow',index);
						}
					}
			}
			
			for(var i = 0;i < lst.length;i ++){
				if(lst[i] == operation){
					lst[i]="";
					flg=1;
				}
			}
			if(rowData.operationId!=null&&rowData.operationId!=""){
				if(flg == 0){
					$.messager.alert('提示','手术药品及非药品必须一次性全部取消退费申请！！');
					lst[lst.length+1] = operation;
				}
			}
			
		},
		onUnselect:function(rowIndex, rowData){
			if(rowData){
				if(rowData.operationId!=null&&rowData.operationId!=""){
					$.messager.confirm('确认','手术药品及非药品必须一次性全部取消退费申请，确认全部取消吗？',function(r){    
					    if (r){ 
					    	var rows=$('#listDrugBack').datagrid('getSelections');
							$('#listDrugBack').datagrid('clearSelections');
							for(var i=0;i<rows.length;i++){
								if(rows[i].operationId!=rowData.operationId){
									var index=$('#listDrugBack').datagrid('getRowIndex',rows[i]);
									$('#listDrugBack').datagrid('selectRow',index);
								}
							}
							
							var rowss=$('#listNotDrugBack').datagrid('getSelections');
							$('#listNotDrugBack').datagrid('clearSelections');
							for(var i=0;i<rowss.length;i++){
								if(rowss[i].operationId!=rowData.operationId){
									var index=$('#listNotDrugBack').datagrid('getRowIndex',rowss[i]);
									$('#listNotDrugBack').datagrid('selectRow',index);
								}
							}
					  }else{
						  lst[lst.length+1] = rowData.operationId;
					  }
					    })
				}
			}
			
		}
	});
}
function retrieTabs(blh,inpatientNo){
	var objName = $('#objname').textbox('getValue');
	var firstDate = $('#firstDate').textbox('getValue');
	var endDate = $('#endDate').textbox('getValue');
	
	$('#listDrugBack').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryDrugBack.action?menuAlias=${menuAlias}',
		queryParams:{
				medicalrecordId:blh,
				objName:objName,
				firstDate:firstDate,
				endDate:endDate,
				inpatientNo:inpatientNo
		}
	});
	
	$('#listNotDrugBack').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryNotDrugBack.action?menuAlias=${menuAlias}',
		queryParams:{
				medicalrecordId:blh,
				objName:objName,
				firstDate:firstDate,
				endDate:endDate,
				inpatientNo:inpatientNo
		}
	});
}

/* 项目检索 */
function retrieval(){
	var blh = $('#medicalrecordId1').val();  
	//获得住院流水号
	var inpatientNo = $('#inpatientNo').val();
	retrieTabs(blh,inpatientNo);
}

//清空
function clear(){
	window.location.reload();
}

/***
 * 取消申请
 */
function remove(){
	var tab = $('#ttdiv').tabs('getSelected');
	var index = $('#ttdiv').tabs('getTabIndex',tab);
	var arr =new Array();
	var brr =new Array();
	var arr1 =new Array();
	var brr1 =new Array();
	var flg=0;
	var row=$('#listDrugBack').datagrid('getSelections');
	for(var i=0;i<row.length;i++){
		if(row[i].operationId!=""&&row[i].operationId!=null){
			flg=flg+1;
		}
	}
	var rows=$('#listNotDrugBack').datagrid('getSelections');
	for(var i=0;i<rows.length;i++){
		if(rows[i].operationId!=""&&rows[i].operationId!=null){
			flg=flg+1;
		}
	}
	if(flg>0){
		var a=false;
		var obj=$('#listDrugBack').datagrid('getSelections');
		if(obj.length){
			$.each(obj,function(i,n){
				if(n.applyNo != null){
					brr[i]=n;
					arr[i]=n.applyNo;
					j=i+1;
					a=true;
				}
			});
		}
		var objs=$('#listNotDrugBack').datagrid('getSelections');
		if(objs.length){
			$.each(objs,function(i,n){
				if(n.applyNo != null){
					brr1[i]=n;
					arr1[i]=n.applyNo;
					j=i+1;
					a=true;
				}
			});
		}
		Array.prototype.push.apply(arr, arr1); 
		$.messager.confirm('确认','您确认想要取消申请区所选中的记录吗？',function(r){    
			    if (r){  
			    	if(a){
			    		$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
   			    		$.ajax({
    						url:'<%=basePath%>inpatient/applydrug/delDrugOrNotDrugApply.action',
    						type:'post',
    						traditional:true, 
    						data:{'ids':arr},
    						success:function(data){
    							$.messager.progress('close');
    							if(data.resCode == 1){
    								$.messager.alert('提示','取消成功！');
    								setTimeout(function(){
    									$(".messager-body").window('close');
    								},3500);
    								//视图删除
    						    	$.each(brr,function(i,n){
    						    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
    								});
    						    	$.each(brr1,function(i,n){
   						    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
   									});
    							}else{
    								$.messager.alert('提示',data.resMes,'error');    								
    								$('#listDrugBack').datagrid('reload');
    							}
    						}
    					});
			    	}else{
			    		//视图删除
				    	$.each(brr,function(i,n){
				    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
						});
				    	$.each(brr1,function(i,n){
					    	 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
				    	});
			    	}
			    }    
			});
		
	}else{
		if(index==0){
			var a=false;
			var obj=$('#listDrugBack').datagrid('getSelections');
			if(obj.length){
				$.each(obj,function(i,n){
					if(n.applyNo != null){
						brr[i]=n;
						arr[i]=n.applyNo;
						j=i+1;
						a=true;
					}
				});
				
				$.messager.confirm('确认','您确认想要取消药品申请区所选中的记录吗？',function(r){    
				    if (r){  
				    	if(a){
				    		$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
	   			    		$.ajax({
	    						url:'<%=basePath%>inpatient/applydrug/delDrugOrNotDrugApply.action',
	    						type:'post',
	    						traditional:true, 
	    						data:{'ids':arr},
	    						success:function(data){
	    							$.messager.progress('close');
	    							if(data.resCode == 1){
	    								$.messager.alert('提示','取消成功！');
	    								setTimeout(function(){
	    									$(".messager-body").window('close');
	    								},3500);
	    								//视图删除
	    						    	$.each(brr,function(i,n){
	    						    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
	    								});
	    							}else{
	    								$.messager.alert('提示',data.resMes,'error');    								
	    								$('#listDrugBack').datagrid('reload');
	    							}
	    						}
	    					});
				    	}else{
				    		//视图删除
					    	$.each(brr,function(i,n){
					    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
							});
				    	}
				    }    
				});
			}else{
				$.messager.alert('友情提示','请勾选药品申请区，要取消药品的复选框');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}else if(index==1){
			var a=false;
			var obj=$('#listNotDrugBack').datagrid('getSelections');
			if(obj.length){
				$.each(obj,function(i,n){
					brr[i]=n;
					if(n.applyNo != null){
						arr[i]=n.applyNo;
						j=i+1;
						a=true;
					}
				});
				$.messager.confirm('确认','您确认想要取消非药品申请区所选中的记录吗？',function(r){    
				    if (r){    
				    	if(a){
				    		$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
	   			    		$.ajax({
	    						url:'<%=basePath%>inpatient/applydrug/delDrugOrNotDrugApply.action',
	    						type:'post',
	    						traditional:true, 
	    						data:{'ids':arr},
	    						success:function(data){
	    							$.messager.progress('close');
	    							if(data.resCode == 1){
	    								$.messager.alert('提示','取消成功！');
	    								setTimeout(function(){
	    									$(".messager-body").window('close');
	    								},3500);
	    								 //视图删除
	    						    	$.each(brr,function(i,n){
	    						    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
	    								});
	    							}else{
	    								$.messager.alert('提示',data.resMes,'error');    								
	    								$('#listNotDrugBack').datagrid('reload');
	    							}
	    						}
	    					});
				    	}else{
				    		 //视图删除
					    	$.each(brr,function(i,n){
					    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
							});
				    	}
				    }    
				});
			}else{
				$.messager.alert('友情提示','请勾选非药品申请区，要取消项目的复选框');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
	}
	
	
}


<%----------------------------------------  渲染        -------------------------------------------------------------------------------------------------------%>

function funformat(){
	/* 渲染合同单位 */
	$.ajax({
		url: "<%=basePath%>inpatient/applydrug/queryBussCon.action", 
		type:'post',
		success: function(data) {
			pactCodeMap = data;
		}
	});
	/* 最小费用代码 */
	$.ajax({
		url: "<%=basePath%>inpatient/applydrug/miniCostMap.action", 
		type:'post',
		success: function(data) {
			miniCostMap = data;
		}
	});
	//渲染表单中的包装单位
	$.ajax({
		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=packunit", 
		type:'post',
		success: function(data) {
			unitMap = data;
		}
	});
}

/***
 * 格式化是否发药
 * 发药状态（0 划价 2摆药 1批费）
 */
function funIsDispensing(value,row,index){
	if(value == 2){
		return '摆药';
	}else if(value == 1){
		return '批费';
	}else if(value == 0){
		return '划价';
	}
}
/**
 * 格式化非药品是否执行
 * 发放状态（0 划价 2发放（执行） 1 批费）
 */
function funIsExecute(value,row,index){
	if(value == 2){
		return '已执行';
	}else{
		return '未执行';
	}
}


//渲染表单中的费用名称
function funminiCost(value,row,index){
	if(value!=null&&value!=''){
		return miniCostMap[value];
	}
}
//渲染表单中的包装单位
function funPackUnit(value,row,index){
	if(value!=null&&value!=''){
		return unitMap[value];
	}
}
</script>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="cc" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height:140px;width:100%;">
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div  data-options="region:'north',border:false" style="height:45px;padding-top: 10px;" > 
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchEvent()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					</shiro:hasPermission>
		        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:returns">
						<a href="javascript:remove()" class="easyui-linkbutton" data-options="iconCls:'icon-backfee2'">取消退费</a>
					</shiro:hasPermission>
				</div>
				<div  data-options="region:'center',border:false"  style="height: 60px;border-bottom-width: 0px;"> 
					<table id="tp" class="honry-table" data-options="fit:true"  style="border: 0px; width: 100%;height: 35px;">
						<tr>
							<td class="tableLabel" >病历号：</td>
							<td ><input class="easyui-textbox" id="medicalrecordId" name='medicalrecordId1' data-options="prompt:'请输入病历号回车查询'"/>
								<input  id="medicalrecordId1" name='medicalrecordId' type="hidden" />
								<input  type="hidden" id="inpatientNo"/>
							</td>
							<td class="tableLabel" >姓名：</td>
							<td ><input class="easyui-textbox" id="name" readonly="readonly"/></td>
							<td class="tableLabel" >合同单位：</td>
							<td ><input class="easyui-textbox" id="pactCode" readonly="readonly" /></td>
							<td class="tableLabel" ">住院科室：</td>
							<td ><input  class="easyui-textbox"id="inhosDept" readonly="readonly"/></td>
							<td class="tableLabel" >床号：</td>
							<td ><input class="easyui-textbox" id="bedId" readonly="readonly"/></td>
							<td style="display: none;width: 20%" ><input id="drugJson" name="drugJson"/></td>
							<td style="display: none;width: 20%"><input id="notDrugJson" name="notDrugJson"/></td>
						</tr>
					</table>
					</div>
					<div data-options="region:'south'" style="height:40px;width: 100%;border-top:0;">
						<table id="table2" class="tableCss" data-options="" style="border: 0px">
							<tr style="height:9px;">
									<span style="display: none">
										记账日期：
										<input class="easyui-datebox" id="firstDate" style="width: 180px"/>
										至
										<input class="easyui-datebox" id="endDate" style="width: 180px"/>
									</span>
									<span >
										项目检索：<input class="easyui-textbox" id="objname" />
									</span>
									<span style="margin-left: 20px">
									<shiro:hasPermission name="${menuAlias}:function:query">
										<a href="javascript:retrieval()" class="easyui-linkbutton" iconCls="icon-search">查询明细</a>
									</shiro:hasPermission>
									</span>
							</tr>
						</table>
					</div>
			</div>
		</div>
		<div id='pcenter' data-options="region:'center',border:false" style="height:  50%">
			<div id="ttdiv" class="easyui-tabs" title="患者明细" data-options="border:false,fit:true,tabPosition:'bottom'">
				<div title="药品退费申请区" style=" height: 100%; width: 100%;">
					<table id="listDrugBack" class="easyui-datagrid" 
						data-options="rownumbers:true,striped:true,border:false,fit:true,
							checkOnSelect:false,selectOnCheck:false,singleSelect:false">
						<thead>
	 						<tr> 
								<th data-options="field:'applyNo',hidden:true"></th>  
								<th data-options="field:'id',hidden:true">id</th>
								<th data-options="field:'drugCode',hidden:true">药品编码</th>
								<th data-options="field:'drugName'" style="width: auto">药品名称</th>   
								<th data-options="field:'specs'" style="width: 9%">规格</th>  
								<th data-options="field:'price'" style="width: 9%" align="right" halign="left">价格</th>   
								<th data-options="field:'quantity'" style="width: 9%">退费数量</th>   
								<!-- <th data-options="field:'unit'" style="width: 9%">单位</th> --> 
 								<th data-options="field:'unit',formatter:funPackUnit" style="width: 9%">单位</th> 
								<th data-options="field:'ck',
										formatter: function(value,row,index){
											return (row.price * row.quantity).toFixed(2);}"
										style="width: 9%" align="right" halign="left">金额</th>  
								<th data-options="field:'senddrugFlag',formatter:funIsDispensing" style="width: 9%">发药状态</th>   
								<th data-options="field:'recipeNo'" style="width: 9%">处方号</th>   
								<th data-options="field:'sequenceNo'"  style="width: 9%">处方内部流水号</th> 
								<th data-options="field:'operationId',hidden:true">手术序号</th>
							</tr>
						</thead>
					</table>
				</div>
				<div title="非药品退费申请区" style=" height: 100%; width: 100%;">
					<table id="listNotDrugBack" class="easyui-datagrid" 
						data-options="rownumbers:true,striped:true,border:false,fit:true,
							checkOnSelect:false,selectOnCheck:false,singleSelect:false">   
						<thead>
							<tr>
								<th data-options="field:'applyNo',hidden:true"></th>  
								<th data-options="field:'id',hidden:true">id</th> 
								<th data-options="field:'objCode',hidden:true" style="width: 9%">项目编码</th>   
								<th data-options="field:'objName'" style="width: 9%">项目名称</th>   
								<th data-options="field:'costName',formatter:funminiCost" style="width: 9%">费用名称</th>  
								<th data-options="field:'price'" style="width: 9%" align="right" halign="left">价格</th>  
								<th data-options="field:'quantity'" style="width: 9%">退费数量</th>  
								<th data-options="field:'unit'" style="width: 9%">单位</th> 
<!-- 								<th data-options="field:'unit',formatter:funPackUnit" style="width: 9%">单位</th>  -->
								<th data-options="field:'ck',
									formatter: function(value,row,index){
										return (row.price * row.quantity).toFixed(2);}" 
										style="width: 9%" align="right" halign="left">金额</th>  
								<th data-options="field:'executeDeptName'" style="width: 9%">执行科室</th>  
								<th data-options="field:'senddrugFlag',formatter:funIsExecute" style="width: 9%">是否执行</th>  
								<th data-options="field:'recipeNo'" style="width: 9%">处方号</th>   
								<th data-options="field:'sequenceNo'"  style="width: 8%">处方内流水号</th> 
								<th data-options="field:'operationId',hidden:true">手术序号</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
   	 <div id="diaInpatient" class="easyui-dialog" title="双击选择患者" style="width:1000;height:500;padding:5" data-options="modal:true, closed:true">   
	   <table id="infoDatagrid"  style="width:100%;height:100%" data-options="fitColumns:true,singleSelect:true"></table>
	</div>  
</body>
</html>