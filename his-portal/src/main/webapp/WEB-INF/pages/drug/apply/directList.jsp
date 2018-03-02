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
<title>直接退费</title>
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

.tableCss td {
	border-right: 1px solid #95b8e7;
	border-bottom: 1px solid #95b8e7;
	padding: 5px 15px;
	word-break: keep-all;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
//渲染表单中的包装单位
var unitMap="";
//全局变量
var drugNum=0;
//合同单位渲染
var pactCodeMap="";  
var packMap;
//费用代码
var miniCostMap=""
//列表中所有药品
var drug;
//列表中所有非药品
var unDrug;
//存放所有手术序号
var lst=new Array();
$(function(){
	loadTree();
	
	funformat();
	
	listDrugDblClick();
	
	DrugBackClick();
	
	funDrugEvent();
	
	listNotDrugDblClick();
	
	notDrugBackClick();
	
	funNotdrugEvent();
	
	bindEnterEvent('medicalrecordId',searchEvent,'easyui');
})
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
		var card_value=app.read_sfz();
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
/*******************************结束读身份证***********************************************/
 /***
  * 药品标签页，数量的键盘弹起事件
  * listDrug
  *		：药品列表
  * listDrugBack
  *		：药品申请区列表
  */
function funDrugEvent(){
	$('#num').numberbox('textbox').bind('keyup', function(event) {
		var numtext = $('#num').numberbox('getText');
			numtext = parseInt(numtext);
		if(isNaN(numtext) || numtext<1){
			numtext = parseInt(1);
		}
		var pd = $('#drugorback').val();
    	//药品退费申请列表的单击事件 
		if(pd==1){
			var backRow = $('#listDrugBack').datagrid('getSelected');
    		var backIndex = $('#listDrugBack').datagrid('getRowIndex',backRow);
    		
    		if(backRow.applyNo != null && backRow.senddrugFlag == 2){
    			$.messager.alert('提示','该药品已完成实时退药操作，此操作不可进行！');
    			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
    			return;
    		}
    		var drugRows=$('#listDrug').datagrid('getRows');
    		//durgList行号
    		var durgIndex;
			for(var i=0;i<drugRows.length;i++){
				var obj = drugRows[i];
				if(obj.recipeNo==backRow.recipeNo){
					if(obj.sequenceNo==backRow.sequenceNo){
						durgIndex=i;
						break;
					}
				}
			}        	
			
			$('#listDrug').datagrid('selectRow',durgIndex);
			var drugRow = $('#listDrug').datagrid('getSelected');
			
			var sum = parseInt(backRow.quantity)+ parseInt(drugRow.nobackNum);
    		if(numtext > sum){
    			$.messager.alert('友情提示','该药品可退的数量最大为'+sum);
    			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
    			
    		}else{
    			$('#listDrugBack').datagrid('updateRow',{
           			 index: backIndex,
           			 row: {
           				 quantity:numtext
           			 }
           		 });
    			
    			$('#listDrug').datagrid('updateRow',{
           			 index: durgIndex,
           			 row: {
           				nobackNum:sum-numtext,
           				moneySum:((sum-numtext)*drugRow.price).toFixed(2)
           			 }
           		 });
    		}
    		
		}else if(pd==0){
			var row = $('#listDrug').datagrid('getSelected');
    		var drugIndex = $('#listDrug').datagrid('getRowIndex',row);
   			var obj=$('#listDrugBack').datagrid('getRows');
			var pd =  true;
   			var backIndex;
   			var returnDrug;//退药数量
   			var returnMany;//金额
   			for(var i=0;i<obj.length;i++){
   				var n=obj[i];
   				if(n.recipeNo==row.recipeNo){
   					if(n.sequenceNo==row.sequenceNo){
   						 returnMany=n.ck;
   						 returnDrug=n.quantity;
   						 pd = false;
   						 backIndex = i;
   						 break;
   					 }
   				}
   			}
   			 /* 判断结果，相应操作 */
   			 if(pd){
   				var sumCost;//总金额
   				if(row.extFlag3=='1'){
   						sumCost=row.moneySum/(row.nobackNum/row.packQty)*numtext;
	     				numtext=numtext*row.packQty;
	     			}else{
	     				sumCost=row.moneySum/row.nobackNum*numtext;
	     				numtext=numtext;
	     			}
   				if(numtext > row.nobackNum){
   	     			$.messager.alert('友情提示','该药品的可退数量不足！');
   	     		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
   	     			$('#num').numberbox('setValue','');
   	     		}else{
   	     			/* 申请区数据 */
    				 $('#listDrugBack').datagrid('appendRow',{
    					 id:row.id,
    					 drugCode:row.drugCode,
    					 drugName:row.drugName,
    					 extFlag3:row.extFlag3,
    					 specs:row.specs,
    					 price:row.price,
    					 packQty:row.packQty,
    					 quantity:numtext,
    					 unit:row.unit,
    					 showUnit:row.showUnit,
    					 senddrugFlag:row.senddrugFlag,
    					 recipeNo:row.recipeNo,
    					 sequenceNo:row.sequenceNo,
    					 operationId:row.operationId,
    					 ck:sumCost.toFixed(2)
    				 });
    				 /* 药品区修改可退数量 */
    				 if(row.extFlag3=='1'){
    					 var flag=row.nobackNum/row.packQty;
    					 $('#listDrug').datagrid('updateRow',{
    	    					index: drugIndex,
    	    					row: {
    	    						nobackNum:row.nobackNum-numtext,
    	    						moneySum:(row.moneySum-sumCost).toFixed(2)
    	    					}
    	    				}); 
    				 }else{
    					 $('#listDrug').datagrid('updateRow',{
 	    					index: drugIndex,
 	    					row: {
 	    						nobackNum:(row.nobackNum-numtext),
 	    						moneySum:(row.moneySum-sumCost).toFixed(2)
 	    					}
 	    				}); 
    				 }
    				 
   	     		}
   			 }else{
   				$('#listDrugBack').datagrid('selectRow',backIndex);
   				var backRow = $('#listDrugBack').datagrid('getSelected');
   				
   				if(backRow.applyNo != null && backRow.senddrugFlag == 2){
   	    			$.messager.alert('提示','该药品已完成实时退药操作，此操作不可进行！');
   	    			setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
   	    			return;
   	    		}
   				var maxSum;//最大数
   				var sumCost;//总金额
   				var tuifei;//退费金额
   				var flag;
   				if(row.extFlag3=='1'){
   						maxSum=returnDrug+row.nobackNum;
	   					sumCost=parseFloat(row.moneySum)+parseFloat(returnMany);
	     				tuifei=sumCost/(maxSum/row.packQty)*numtext;
	     				numtext=numtext*row.packQty;
	     				flag=maxSum/row.packQty;
	     			}else{
	     				maxSum=returnDrug+row.nobackNum;
	     				sumCost=parseFloat(row.moneySum)+parseFloat(returnMany);;
	     				tuifei=sumCost/maxSum*numtext;
	     				numtext=numtext;
	     				flag=maxSum
	     			}
   				if(numtext > maxSum){
   					$.messager.alert('友情提示','该药品可退的数量最大为'+(flag));
   					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
   	    			$('#num').numberbox('setValue','');
   				}else{
   					$('#listDrugBack').datagrid('updateRow',{
              			 index: backIndex,
              			 row: {
              				 quantity:numtext,
              				 ck:tuifei.toFixed(2)
              			 }
              		 });
   					
   					/* 药品区修改可退数量 */
   				 if(row.extFlag3=='1'){
   					 var flag=row.nobackNum/row.packQty;
   					 $('#listDrug').datagrid('updateRow',{
   	    					index: drugIndex,
   	    					row: {
   	    						nobackNum:maxSum-numtext,
   	    						moneySum:(sumCost-tuifei).toFixed(2)
   	    					}
   	    				}); 
   				 }else{
   					 $('#listDrug').datagrid('updateRow',{
	    					index: drugIndex,
	    					row: {
	    						nobackNum:(maxSum-numtext),
	    						moneySum:(sumCost-tuifei).toFixed(2)
	    					}
	    				}); 
   				 }
   				}
   			 } 
		}
	});
}


/***
 * 非药品标签页，数量文本框键盘抬起事件
 *
 * listNotDrug
 *		：非药品列表
 * listNotDrugBack
 *		：非药品申请区列表
 */
function funNotdrugEvent(){
	$('#itemNum').numberbox('textbox').bind('keyup',function(event){
		var numtext = $('#itemNum').numberbox('getText');
		numtext = parseInt(numtext);
		if(isNaN(numtext) || numtext<1){
			numtext = parseInt(1);
		}
		
		var pd = $('#notDrugorback').val();
		
		//药品退费申请列表的单击事件 
		if(pd==1){
 			var backRow = $('#listNotDrugBack').datagrid('getSelected');
    		var backIndex = $('#listNotDrugBack').datagrid('getRowIndex',backRow);
			
    		var drugRows=$('#listNotDrug').datagrid('getRows');
    		//durgList行号
    		var durgIndex;
			for(var i=0;i<drugRows.length;i++){
				var obj = drugRows[i];
				if(obj.recipeNo==backRow.recipeNo){
					if(obj.sequenceNo==backRow.sequenceNo){
						durgIndex=i;
						break;
					}
				}
			}        	
    		
			$('#listNotDrug').datagrid('selectRow',durgIndex);
			var drugRow = $('#listNotDrug').datagrid('getSelected');
    		
			var sum = parseInt(backRow.quantity) + parseInt(drugRow.nobackNum);
			if(numtext > sum){
    			$.messager.alert('友情提示','该项目可退的数量最大值为：'+sum);
    			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
    			
    		}else{
    			$('#listNotDrugBack').datagrid('updateRow',{
           			 index: backIndex,
           			 row: {
           				 quantity:numtext
           			 }
           		 });
    			
    			$('#listNotDrug').datagrid('updateRow',{
           			 index: durgIndex,
           			 row: {
           				nobackNum:sum-numtext,
           				moneySum:((sum-numtext)*drugRow.price).toFixed(2)
           			 }
           		 });
    		}
			
		}else if(pd==0){
			var row = $('#listNotDrug').datagrid('getSelected');
    		var notDrugIndex = $('#listNotDrug').datagrid('getRowIndex',row);
			
    		var obj=$('#listNotDrugBack').datagrid('getRows');
			var pd =  true;
   			var backIndex;
			
   			for(var i=0;i<obj.length;i++){
   				var n=obj[i];
   				if(n.recipeNo == row.recipeNo){
   					if(n.sequenceNo == row.sequenceNo){
   						 pd = false;
   						 backIndex = i;
   						 break;
   					 }
   				}
   			}
   			
   			/* 判断结果，相应操作 */
  			 if(pd){
  				if(numtext > row.nobackNum){
   	     			$.messager.alert('友情提示','该药品的可退数量不足！');
	   	     		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
   	     			$('#num').numberbox('setValue','');
  				}else{
  					 /* 申请区数据 */
  					 $('#listNotDrugBack').datagrid('appendRow',{
  						 id:row.id,
  						 objCode:row.objCode,
  						 objName:row.objName,
  						 costName:row.costName,
  						 price:row.price,
  						 quantity:numtext,
  						 unit:row.unit,
  						 executeDept:row.executeDept,
  						 senddrugFlag:row.senddrugFlag,
  						 recipeNo:row.recipeNo,
  						 sequenceNo:row.sequenceNo,
  						operationId:row.operationId
  					 });
					 
  					 /* 非药品区修改可退数量 */
  					 $('#listNotDrug').datagrid('updateRow',{
						index: notDrugIndex,
						row: {
							nobackNum:row.nobackNum-numtext,
							moneySum:((row.nobackNum-numtext)*row.price).toFixed(2)
						}
					});
  				}
  			 }else{
  				$('#listNotDrugBack').datagrid('selectRow',backIndex);
  				var backRow = $('#listNotDrugBack').datagrid('getSelected');
  				var sum = parseInt(backRow.quantity)+ parseInt(row.nobackNum);
  				if(numtext > sum){
   					$.messager.alert('友情提示','该药品可退的数量最大为'+sum);
   					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
   	    			$('#num').numberbox('setValue','');
   				}else{
   					/* 非药品申请区数据 */
   					$('#listNotDrugBack').datagrid('updateRow',{
             			 index: backIndex,
             			 row: {
             				 quantity:numtext
             			 }
             		 });
   					 
   					 /* 非药品区修改可退数量 */
   					$('#listNotDrug').datagrid('updateRow',{
						index: notDrugIndex,
						row: {
							nobackNum:sum-numtext,
							moneySum:((sum-numtext)*row.price).toFixed(2)
						}
					});
   				}
  			 }
		}
	});
}


/* 加载患者信息树 */
function loadTree(){
	$('#tDt').tree({    
		url:"<%=basePath%>nursestation/nurseCharge/treeNurseCharge.action?deptId="+$("#deptId").val(),
	    method:'get',
	    animate:true,  //点在展开或折叠的时候是否显示动画效果
	    lines:true,    //是否显示树控件上的虚线
		state:closed,
		formatter:function(node){//统计节点总数
			var s = node.text;
			if (node.children){
				if(node.children.length!=0){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
			}  
			return s;
		},
 	    onSelect: function(node){
 	    	if(node.id != 1){
 	   			if(node.attributes.medicalrecordId == 'undefined'||node.attributes.medicalrecordId==null||node.attributes.medicalrecordId==""){
 	   			}else{
		 	   		$('#firstDate').val('');
					$('#endDate').val('');
					$('#objname').textbox('setValue','');
					$('#medicalrecordId').textbox('setValue',node.attributes.medicalrecordId);
					$('#inpatientNo').val(node.attributes.inpatientNo);
		
			   			loadData(node.attributes.medicalrecordId);
			   			loadTabs(node.attributes.medicalrecordId,node.attributes.inpatientNo);
			   			
			   			$('#DrugName').textbox('setValue','');
					$('#price').textbox('setValue','');
					$('#num').numberbox('setValue',''); 
					$('#drugorback').val('');
					
			   			$('#itemName').textbox('setValue','');
					$('#itemPrice').textbox('setValue','');
					$('#itemNum').numberbox('setValue',''); 
					$('#notDrugorback').val('');
 	   			}	
 	   		}
		}
	}); 
}

/* 根据病历号，加载患者基本信息 */
function loadData(blh){
	var patientBasicData;
	var bedName;
	var inpatiNo=$('#inpatientNo').val();
	$.ajax({
		url: "<%=basePath%>inpatient/applydrug/patientBasicData.action", 
		type:'post',
		async:false,
		data:{'inpatientNo':inpatiNo},
		success: function(data) {
			patientBasicData = data;
			
		}
	});
	showData(patientBasicData,patientBasicData.bedName);
}

/* 填充患者信息 */
function showData(data,bedName){
// 	$('#medicalrecordId').textbox('setValue',data.medicalrecordId);
	$('#medicalrecordId1').val(data.medicalrecordId);
	$("#inpatientNo").val(data.inpatientNo);
	$('#name').textbox('setValue',data.patientName);	
	$('#pactCode').textbox('setValue',pactCodeMap[data.pactCode]);	 
	$('#inhosDept').textbox('setValue',data.deptName);	 
	$('#bedId').textbox('setValue',bedName); 
}

/* 加载tabs信息 */
function loadTabs(blh,inpatientNo){
	var objName = $('#objname').textbox('getValue');
	var firstDate = $('#firstDate').val();
	var endDate = $('#endDate').val();
	var inpatient =$('#inpatientNo').val();
	$('#listDrug').datagrid({
		data:[]
	});
	$('#listDrug').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryDrugApply.action?menuAlias=${menuAlias}',
		method:'post',
		queryParams:{
			medicalrecordId:blh,
			inpatientNo:inpatient,
			objName:objName,
			firstDate:firstDate,
			endDate:endDate
		},
		onLoadSuccess: function (data) {
			drug=data;
		}
	});
	$('#listDrugBack').datagrid({
		data:[]
	});
	$('#listDrugBack').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryDrugBack.action?menuAlias=${menuAlias}',
		queryParams:{
				medicalrecordId:blh,
				inpatientNo:inpatient
		},
		onClickRow:function(rowIndex, rowData){
			var operation='';
			var flg=0;
			var cancelNotDrugBack=$('#listNotDrugBack').datagrid('getRows');
			var cancelDrugBack=$('#listDrugBack').datagrid('getRows');
			if(rowData.operationId!=null&&rowData.operationId!=""){
				for(var i=0;i<cancelNotDrugBack.length;i++){
					if(cancelNotDrugBack[i].operationId==rowData.operationId){
						var index=$('#listNotDrugBack').datagrid('getRowIndex',cancelNotDrugBack[i]);
					    $('#listNotDrugBack').datagrid('checkRow',index);
						
					}
				}
				for(var i=0;i<cancelDrugBack.length;i++){
					if(cancelDrugBack[i].operationId==rowData.operationId){
						var index=$('#listDrugBack').datagrid('getRowIndex',cancelDrugBack[i]);
						$('#listDrugBack').datagrid('checkRow',index);
						
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
					$.messager.alert('提示','手术药品及非药品必须一次性全部退费！！');
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
									$('#listDrugBack').datagrid('checkRow',index);
								}
							}
							
							var rowss=$('#listNotDrugBack').datagrid('getSelections');
							$('#listNotDrugBack').datagrid('clearSelections');
							for(var i=0;i<rowss.length;i++){
								if(rowss[i].operationId!=rowData.operationId){
									var index=$('#listNotDrugBack').datagrid('getRowIndex',rowss[i]);
									$('#listNotDrugBack').datagrid('checkRow',index);
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
	
	
	$('#listNotDrug').datagrid({
		url:'<%=basePath %>inpatient/applydrug/queryNotDrugApply.action?menuAlias=${menuAlias}',
		method:'post',
		queryParams:{
			medicalrecordId:blh,
			inpatientNo:inpatient,
			objName:objName,
			firstDate:firstDate,
			endDate:endDate
		},
		onLoadSuccess: function (data) {
			unDrug=data;
		}
	});
	$('#listNotDrugBack').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryNotDrugBack.action?menuAlias=${menuAlias}',
		queryParams:{
			medicalrecordId:blh,
			inpatientNo:inpatient
		},
		onClickRow:function(rowIndex, rowData){
			var operation='';
			var flg=0;
			var cancelNotDrugBack=$('#listNotDrugBack').datagrid('getRows');
			var cancelDrugBack=$('#listDrugBack').datagrid('getRows');
			if(rowData.operationId!=null&&rowData.operationId!=""){
				for(var i=0;i<cancelNotDrugBack.length;i++){
					if(cancelNotDrugBack[i].operationId==rowData.operationId){
						var index=$('#listNotDrugBack').datagrid('getRowIndex',cancelNotDrugBack[i]);
					    $('#listNotDrugBack').datagrid('checkRow',index);
						
					}
				}
				for(var i=0;i<cancelDrugBack.length;i++){
					if(cancelDrugBack[i].operationId==rowData.operationId){
						var index=$('#listDrugBack').datagrid('getRowIndex',cancelDrugBack[i]);
						$('#listDrugBack').datagrid('checkRow',index);
						
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
					$.messager.alert('提示','手术药品及非药品必须一次性全部退费！！');
					lst[lst.length+1] = operation;
				}
			}
		},
		onUnselect:function(rowIndex, rowData){
			if(rowData!='undefined'){
				if(rowData.operationId!=null&&rowData.operationId!=""){
					$.messager.confirm('确认','手术药品及非药品必须一次性全部取消退费申请，确认全部取消吗？',function(r){    
					    if (r){
					    	var rows=$('#listDrugBack').datagrid('getSelections');
							$('#listDrugBack').datagrid('clearSelections');
							for(var i=0;i<rows.length;i++){
								if(rows[i].operationId!=rowData.operationId){
									var index=$('#listDrugBack').datagrid('getRowIndex',rows[i]);
									$('#listDrugBack').datagrid('checkRow',index);
								}
							}
							
							var rowss=$('#listNotDrugBack').datagrid('getSelections');
							$('#listNotDrugBack').datagrid('clearSelections');
							for(var i=0;i<rowss.length;i++){
								if(rowss[i].operationId!=rowData.operationId){
									var index=$('#listNotDrugBack').datagrid('getRowIndex',rowss[i]);
									$('#listNotDrugBack').datagrid('checkRow',index);
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






<%------------------------------------------------------------------- 药品标签页操作begin	----------------------------------------------------------------------%>
/***
 * 药品列表的双击事件
 */
function listDrugDblClick(){
	//药品未申请 
	$('#listDrug').datagrid({
		onClickRow: function(index,row){
			$('#DrugName').textbox('setValue',row.drugName);
			$('#price').textbox('setValue',row.price);
			$('#num').numberbox('setValue',''); 
			$('#drugorback').val(0);
		},onDblClickRow: function(index,row){
			if(row.nobackNum > 0){
				//全退是否选中
				if($('#cheacks').is(':checked')) {
					if(row.operationId!=null&&row.operationId!=""){
						$('#DrugName').textbox('setValue','');
						$('#price').textbox('setValue','');
						$('#num').numberbox('setValue',''); 
						$.messager.confirm('确认','手术药品及非药品必须一次性全部退费，确认全部退费吗?',function(r){    
						    if (r){
									for(var i=0;i<drug.rows.length;i++){
										if(drug.rows[i].operationId==row.operationId){
											addAllDrugBack(drug.rows[i]);
										}
									}
									for(var i=0;i<unDrug.rows.length;i++){
										if(unDrug.rows[i].operationId==row.operationId){
											addAllNotDrugBack(unDrug.rows[i]);
										}
									}
						    }})
					}else{
						$('#DrugName').textbox('setValue','');
						$('#price').textbox('setValue','');
						$('#num').numberbox('setValue',''); 
						addAllDrugBack(row);
					}
				}else{
					if(row.operationId!=null&&row.operationId!=""){
						$('#DrugName').textbox('setValue','');
						$('#price').textbox('setValue','');
						$('#num').numberbox('setValue',''); 
						$.messager.confirm('确认','手术药品及非药品必须一次性全部退费，确认全部退费吗?',function(r){    
						    if (r){
									for(var i=0;i<drug.rows.length;i++){
										if(drug.rows[i].operationId==row.operationId){
											addAllDrugBack(drug.rows[i]);
										}
									}
									for(var i=0;i<unDrug.rows.length;i++){
										if(unDrug.rows[i].operationId==row.operationId){
											addAllNotDrugBack(unDrug.rows[i]);
										}
									}
						    }})
					}else{
						$('#num').numberbox('setValue',1);
						addPartDrugBack(row);
					}
					
				}
					
			}else{
				$.messager.alert('友情提示','该药品可退数量不足');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
	});
}


/***
 * 为药品申请区域进行全退操作
 *	row ：要添加的数据 *	
 */
 function addAllDrugBack(row){
		var obj=$('#listDrugBack').datagrid('getRows');
		var drugIndex = $('#listDrug').datagrid('getRowIndex',row);
		
		/* 循环判断已申请状态中是否存在所选药品 */
		var pd =  true;
		//DrugBack的索引号
		var indexd;
		for(var i=0;i<obj.length;i++){
			var n=obj[i];
			if(n.recipeNo==row.recipeNo){
				if(n.sequenceNo==row.sequenceNo){
					 pd = false;
					 indexd = i;
					 break;
				 }
			}
		}
		
		 /* 判断结果，相应操作 */
		 if(pd){
			 /* 申请区数据 */
			 $('#listDrugBack').datagrid('appendRow',{
				 id:row.id,
				 drugCode:row.drugCode,
				 drugName:row.drugName,
				 specs:row.specs,
				 price:row.price,
				 quantity:row.nobackNum,
				 unit:row.unit,
				 senddrugFlag:row.senddrugFlag,
				 recipeNo:row.recipeNo,
				 sequenceNo:row.sequenceNo,
				 operationId:row.operationId,
				 ck:row.moneySum,
				 showUnit:row.showUnit,
				 extFlag3:row.extFlag3,
				 packQty:row.packQty
			 });
			 
			 /* 药品区修改可退数量 */
			 $('#listDrug').datagrid('updateRow',{
				index: drugIndex,
				row: {
					nobackNum:0,
					moneySum:0.0
				}
			});
		 }else{
			 /* 申请区数据 */
			 $('#listDrugBack').datagrid('selectRow',indexd);
			 var rows = $('#listDrugBack').datagrid('getRows');
			 var drugBackRow = rows[indexd];
			 //状态判断
			 if(drugBackRow.applyNo != null && drugBackRow.senddrugFlag == 2){
	   			$.messager.alert('提示','该药品已完成实时退药操作，此操作不可进行！');
	   			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	   			return;
	   		 }
			 
			 var drugBackNum =  parseInt(drugBackRow.quantity);
			 
			 $('#listDrugBack').datagrid('updateRow',{
				 index: indexd,
				 row: {
					 quantity:row.nobackNum + drugBackNum,
					 ck:(Number(row.moneySum)+Number(drugBackRow.ck)).toFixed(2)
				 }
			 });
			 
			 $('#listDrug').datagrid('updateRow',{
				 index: drugIndex,
				 row: {
					 nobackNum:0,
					 moneySum:0.0
				 }
			 });
		 }
	}

/***
 * 药品申请区域进行部分退操作
 *	row ：要添加的数据 
 */
 function addPartDrugBack(row){
		
		var obj=$('#listDrugBack').datagrid('getRows');
		var drugIndex = $('#listDrug').datagrid('getRowIndex',row);
		/* 循环判断已申请状态中是否存在所选药品 */
		var pd =  true;
		//DrugBack 行号
		var indexd;
		for(var i=0;i<obj.length;i++){
			var n=obj[i];
			if(n.recipeNo==row.recipeNo){
				if(n.sequenceNo==row.sequenceNo){
					 pd = false;
					 indexd = i;
					 break;
				 }
			}
		}
		 /* 判断结果，相应操作 */
		 if(pd){
			 var cost = 0.00;
			 var quantity = 0;
			 if(row.extFlag3=='1'){//包装数量
				 cost = (row.moneySum/row.nobackNum)*row.packQty;
				 quantity = row.packQty;
			 }else{
				 cost = row.moneySum/row.nobackNum;
				 quantity = 1;
			 }
			 /* 申请区数据 */
			 $('#listDrugBack').datagrid('appendRow',{
				 id:row.id,
				 drugCode:row.drugCode,
				 drugName:row.drugName,
				 specs:row.specs,
				 price:row.price,
				 quantity:quantity,
				 unit:row.unit,
				 showUnit:row.showUnit,
				 senddrugFlag:row.senddrugFlag,
				 recipeNo:row.recipeNo,
				 sequenceNo:row.sequenceNo,
				 operationId:row.operationId,
				 extFlag3:row.extFlag3,
				 ck:cost.toFixed(2),
				 packQty:row.packQty
			 });
			 var number = 1;
			 if(row.extFlag3=='1'){//包装数量
				 number = row.packQty*1;
				 /* 药品区修改可退数量 */
				 $('#listDrug').datagrid('updateRow',{
						index: drugIndex,
						row: {
							nobackNum:row.nobackNum-number,
							moneySum:(row.moneySum-(row.moneySum/(row.nobackNum/row.packQty))).toFixed(2)
						}
				 });
			 }else{
				 /* 药品区修改可退数量 */
				 $('#listDrug').datagrid('updateRow',{
						index: drugIndex,
						row: {
							nobackNum:row.nobackNum-1,
							moneySum:(row.moneySum-(row.moneySum/row.nobackNum)).toFixed(2)
						}
				 });
			 }
			 
		 }else{
			 $('#listDrugBack').datagrid('selectRow',indexd);
			 var rows = $('#listDrugBack').datagrid('getRows');
			 var drugRow = rows[indexd];
			 //状态判断
			 if(drugRow.applyNo != null && drugRow.senddrugFlag == 2){
	   			$.messager.alert('提示','该药品已完成实时退药操作，此操作不可进行！');
	   			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	   			return;
	   		 }
			 var DrugBackCost = 0.00;
			 DrugBackCost = rows[indexd].ck;
			 var sum = 0.00;
			 var number = 1;
			 var mon = 0.00;
			 var quantity = 0;
			 if(row.extFlag3=='1'){
				 number = row.packQty*1;
				 sum = ((row.moneySum/row.nobackNum)*row.packQty).toFixed(2);
				 mon = (row.moneySum-(row.moneySum/(row.nobackNum/row.packQty))).toFixed(2);
				 quantity = row.packQty;
			 }else{
				 quantity = 1;
				 sum = (row.moneySum/row.nobackNum).toFixed(2);
				 mon = (row.moneySum-(row.moneySum/row.nobackNum)).toFixed(2);
			 }
			 /* 申请区数据 */
			 $('#listDrugBack').datagrid('updateRow',{
				 index: indexd,
				 row: {
					 quantity:drugRow.quantity+quantity,
					 ck:Number(sum)+Number(DrugBackCost)
				 }
			 });
			 
			 $('#listDrug').datagrid('updateRow',{
				 index: drugIndex,
				 row: {
					 nobackNum:row.nobackNum-number,
					 moneySum:mon
				 }
			 });
		 }
	}

/* 药品申请区列表单击事件 */
function DrugBackClick(){
	$('#listDrugBack').datagrid({
		onClickRow: function(index,row){
			$('#DrugName').textbox('setValue',row.drugName);
			$('#price').textbox('setValue',row.price);
			$('#num').numberbox('setValue',row.quantity);
			$('#drugorback').val(1);
		}
	});
}


<%-------------------------------------------------------------------------- 非药品标签页操作begin	--------------------------------------------------------------%>


/***
 * 非药品列表双击事件
 */
function listNotDrugDblClick(){
	$('#listNotDrug').datagrid({
		onClickRow: function(index,row){
			$('#itemName').textbox('setValue',row.objName);
			$('#itemPrice').textbox('setValue',row.price);
			$('#itemNum').numberbox('setValue',''); 
			$('#notDrugorback').val(0);
		},onDblClickRow: function(index,row){
			if(row.nobackNum > 0){
				//全退是否选中
				if($('#cheacknot').is(':checked')) {
					if(row.operationId!=null&&row.operationId!=""){
						$('#itemName').textbox('setValue','');
						$('#itemPrice').textbox('setValue','');
						$('#itemNum').numberbox('setValue',''); 
						$.messager.confirm('确认','手术药品及非药品必须一次性全部退费，确认全部退费吗?',function(r){    
						    if (r){
									for(var i=0;i<drug.rows.length;i++){
										if(drug.rows[i].operationId==row.operationId){
											addAllDrugBack(drug.rows[i]);
										}
									}
									for(var i=0;i<unDrug.rows.length;i++){
										if(unDrug.rows[i].operationId==row.operationId){
											addAllNotDrugBack(unDrug.rows[i]);
										}
									}
						    }})
					}else{
						$('#itemName').textbox('setValue','');
						$('#itemPrice').textbox('setValue','');
						$('#itemNum').numberbox('setValue',''); 
						addAllNotDrugBack(row);
					}
				}else{
					if(row.operationId!=null&&row.operationId!=""){
						$('#itemName').textbox('setValue','');
						$('#itemPrice').textbox('setValue','');
						$('#itemNum').numberbox('setValue',''); 
						$.messager.confirm('确认','手术药品及非药品必须一次性全部退费，确认全部退费吗?',function(r){    
						    if (r){
								for(var i=0;i<drug.rows.length;i++){
									if(drug.rows[i].operationId==row.operationId){
										addAllDrugBack(drug.rows[i]);
									}
								}
								for(var i=0;i<unDrug.rows.length;i++){
									if(unDrug.rows[i].operationId==row.operationId){
										addAllNotDrugBack(unDrug.rows[i]);
									}
								}
						    }})
					}else{
						$('#itemNum').textbox('setValue',1);
						addPartNotDrugBack(row);
					}
				}
			}else{
				$.messager.alert('友情提示','该药品可退数量不足');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
	});
}


/***
 * 非药品申请区全退操作
 * row : 要添加的数据
 */
function addAllNotDrugBack(row){
	var obj=$('#listNotDrugBack').datagrid('getRows');
	var drugIndex = $('#listNotDrug').datagrid('getRowIndex',row);
	
	/* 循环判断已申请状态中是否存在所选药品 */
	var pd =  true;
	//DrugBack的索引号
	var indexd;
	 
	for(var i=0;i<obj.length;i++){
		var n=obj[i];
		if(n.recipeNo==row.recipeNo){
			if(n.sequenceNo==row.sequenceNo){
				 pd = false;
				 indexd = i;
				 break;
			 }
		}
	}
	
	 /* 判断结果，相应操作 */
	 if(pd){
		 /* 申请区数据 */
		 $('#listNotDrugBack').datagrid('appendRow',{
			 id:row.id,
			 objCode:row.objCode,
			 objName:row.objName,
			 costName:row.costName,
			 price:row.price,
			 quantity:row.nobackNum,
			 unit:row.unit,
			 executeDept:row.executeDept,
			 senddrugFlag:row.senddrugFlag,
			 recipeNo:row.recipeNo,
			 sequenceNo:row.sequenceNo,
			 operationId:row.operationId
		 });
		 
		 /* 药品区修改可退数量 */
		 $('#listNotDrug').datagrid('updateRow',{
			index: drugIndex,
			row: {
				nobackNum:0,
				moneySum:0.0
			}
		});
		 
	 }else{
		 /* 申请区数据 */
		 $('#listNotDrugBack').datagrid('selectRow',indexd);
		 var notDrugBack =  $('#listNotDrugBack').datagrid('getSelected');
		 var notDrugBackNum =  parseInt(notDrugBack.quantity);
		 
		 $('#listNotDrugBack').datagrid('updateRow',{
			 index: indexd,
			 row: {
				 quantity:row.nobackNum + notDrugBackNum
			 }
		 });
		 
		 $('#listNotDrug').datagrid('updateRow',{
			 index: drugIndex,
			 row: {
				 nobackNum:0,
				 moneySum:0.0
			 }
		 });
	 }
}


/***
 * 非药品申请区部分退操作
 * row : 数据
 */
function addPartNotDrugBack(row){
	var obj=$('#listNotDrugBack').datagrid('getRows');
	var drugIndex = $('#listNotDrug').datagrid('getRowIndex',row);
	
	/* 循环判断已申请状态中是否存在所选药品 */
	var pd =  true;
	//DrugBack 行号
	var indexd;
	for(var i=0;i<obj.length;i++){
		var n=obj[i];
		if(n.recipeNo==row.recipeNo){
			if(n.sequenceNo==row.sequenceNo){
				 pd = false;
				 indexd = i;
				 break;
			 }
		}
	}
	
	 /* 判断结果，相应操作 */
	 if(pd){
		 /* 申请区数据 */
		 $('#listNotDrugBack').datagrid('appendRow',{
			 id:row.id,
			 objCode:row.objCode,
			 objName:row.objName,
			 costName:row.costName,
			 price:row.price,
			 quantity:1,
			 unit:row.unit,
			 executeDept:row.executeDept,
			 senddrugFlag:row.senddrugFlag,
			 recipeNo:row.recipeNo,
			 sequenceNo:row.sequenceNo,
			 operationId:row.operationId
		 });
		 
		 /* 药品区修改可退数量 */
		 $('#listNotDrug').datagrid('updateRow',{
				index: drugIndex,
				row: {
					nobackNum:row.nobackNum-1,
					moneySum:((row.nobackNum-1)*row.price).toFixed(2)
				}
			});
	 }else{
		 var rows = $('#listNotDrugBack').datagrid('getRows');
		 var drugRow = rows[indexd];
		 $('#listNotDrugBack').datagrid('selectRow',indexd);
		 /* 申请区数据 */
		 $('#listNotDrugBack').datagrid('updateRow',{
			 index: indexd,
			 row: {
				 quantity:drugRow.quantity+1
			 }
		 });
		 
		 $('#listNotDrug').datagrid('updateRow',{
			 index: drugIndex,
			 row: {
				 nobackNum:row.nobackNum-1,
				 moneySum:((row.nobackNum-1)*row.price).toFixed(2)
			 }
		 });
	 }
}


/* 非药品申请区列表单击事件 */
function notDrugBackClick(){
	$('#listNotDrugBack').datagrid({
		onClickRow: function(index,row){
			$('#itemName').textbox('setValue',row.objName);
			$('#itemPrice').textbox('setValue',row.price);
			$('#itemNum').numberbox('setValue',row.quantity);
			$('#notDrugorback').val(1);
		}
	});
}


<%------------------------------------------------------------------------  弹框 事件  	 --------------------------------------------------------------------------%>
//病历号后六位搜索
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
							        {field:'reportSex',title:'性别',width:'20%',align:'center'} , 
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



/* 项目检索 */
function retrieval(){
	$('#DrugName').textbox('setValue','');
	$('#price').textbox('setValue','');
	$('#num').numberbox('setValue',''); 
	$('#drugorback').val('');
	
	$('#itemName').textbox('setValue','');
	$('#itemPrice').textbox('setValue','');
	$('#itemNum').numberbox('setValue',''); 
	$('#notDrugorback').val('');
	
	var blh = $('#medicalrecordId1').val();  
	var inpatientNo = $('#inpatientNo').val();  
	loadretrieval(blh);
	
}

/* 加载tabs信息 */
function loadretrieval(blh){
	var objName = $('#objname').textbox('getValue');
	var firstDate = $('#firstDate').val();
	var endDate = $('#endDate').val();
	var inpatient =$('#inpatientNo').val();
	$('#listDrug').datagrid({
		url:'<%=basePath%>inpatient/applydrug/queryDrugApply.action?menuAlias=${menuAlias}',
		method:'post',
		queryParams:{
			medicalrecordId:blh,
			inpatientNo:inpatient,
			objName:objName,
			firstDate:firstDate,
			endDate:endDate
		}
	});
	
	
	$('#listNotDrug').datagrid({
		url:'<%=basePath %>inpatient/applydrug/queryNotDrugApply.action?menuAlias=${menuAlias}',
		method:'post',
		queryParams:{
			medicalrecordId:blh,
			inpatientNo:inpatient,
			objName:objName,
			firstDate:firstDate,
			endDate:endDate
		}
	});
}


<%------------------------------------------------------------------------  后台操作  	 --------------------------------------------------------------------------%>

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
				brr[i]=n;
				if(n.applyNo != null){
					arr[i]=n.applyNo;
					j=i+1;
					 a=true;
					
				}
			});
		}
		var objs=$('#listNotDrugBack').datagrid('getSelections');
		if(objs.length){
			$.each(objs,function(i,n){
				brr1[i]=n;
				if(n.applyNo != null){
					arr1[i]=n.applyNo;
					j=i+1;
					 a=true;
				}
			});
		}
		Array.prototype.push.apply(arr, arr1); 
		$.messager.confirm('确认','您确认想要取消申请去所选中的记录吗？',function(r){    
		    if (r){  
		    	if(a){
		    		$.ajax({
						url:'<%=basePath%>inpatient/applydrug/delDrugOrNotDrugApply.action',
						type:'post',
						traditional:true, 
						data:{'ids':arr},
						success:function(data){
							if(data.resCode == 1){
								$.messager.alert('提示','取消成功！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								//视图删除
						    	$.each(brr,function(i,n){
						    		var obj=$('#listDrug').datagrid('getRows');
						    		var indexd;
						    		for(var i=0;i<obj.length;i++){
						    			var m=obj[i];
						    			if(m.recipeNo==n.recipeNo){
						    				if(m.sequenceNo==n.sequenceNo){
						    					 indexd = i;
						    					 break;
						    				 }
						    			}
						    		}
						    		 $('#listDrug').datagrid('selectRow',indexd);
						    		 var drugRow = $('#listDrug').datagrid('getSelected');
						    		 
						    		 $('#listDrug').datagrid('updateRow',{
						    			 index: indexd,
						    			 row: {
						    				 nobackNum:drugRow.nobackNum+n.quantity,
						    			 }
						    		 });
						    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
						    		 $('#listDrugBack').datagrid('unselectRow',indexd);
								});
								
						    	 //视图删除
						    	$.each(brr1,function(i,n){
						    		var obj=$('#listNotDrug').datagrid('getRows');
						    		var indexd;
						    		for(var i=0;i<obj.length;i++){
						    			var m=obj[i];
						    			if(m.recipeNo==n.recipeNo){
						    				if(m.sequenceNo==n.sequenceNo){
						    					 indexd = i;
						    					 break;
						    				 }
						    			}
						    		}
						    		 
						    		 $('#listNotDrug').datagrid('selectRow',indexd);
						    		 var drugRow = $('#listNotDrug').datagrid('getSelected');
						    		 
						    		 $('#listNotDrug').datagrid('updateRow',{
						    			 index: indexd,
						    			 row: {
						    				 nobackNum:drugRow.nobackNum+n.quantity,
						    			 }
						    		 });
						    		 
						    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
						    		 $('#listNotDrug').datagrid('unselectRow',indexd);
								});
								
							}
						}
					});
		    	}else{
		    		//视图删除
			    	$.each(brr,function(i,n){
			    		var obj=$('#listDrug').datagrid('getRows');
			    		var indexd;
			    		for(var i=0;i<obj.length;i++){
			    			var m=obj[i];
			    			if(m.recipeNo==n.recipeNo){
			    				if(m.sequenceNo==n.sequenceNo){
			    					 indexd = i;
			    					 break;
			    				 }
			    			}
			    		}
			    		 $('#listDrug').datagrid('selectRow',indexd);
			    		 var drugRow = $('#listDrug').datagrid('getSelected');
			    		 
			    		 $('#listDrug').datagrid('updateRow',{
			    			 index: indexd,
			    			 row: {
			    				 nobackNum:drugRow.nobackNum+n.quantity,
			    			 }
			    		 });
			    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
			    		 $('#listDrugBack').datagrid('unselectRow',indexd);
					});
					
			    	 //视图删除
			    	$.each(brr1,function(i,n){
			    		var obj=$('#listNotDrug').datagrid('getRows');
			    		var indexd;
			    		for(var i=0;i<obj.length;i++){
			    			var m=obj[i];
			    			if(m.recipeNo==n.recipeNo){
			    				if(m.sequenceNo==n.sequenceNo){
			    					 indexd = i;
			    					 break;
			    				 }
			    			}
			    		}
			    		 
			    		 $('#listNotDrug').datagrid('selectRow',indexd);
			    		 var drugRow = $('#listNotDrug').datagrid('getSelected');
			    		 
			    		 $('#listNotDrug').datagrid('updateRow',{
			    			 index: indexd,
			    			 row: {
			    				 nobackNum:drugRow.nobackNum+n.quantity,
			    			 }
			    		 });
			    		 
			    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
			    		 $('#listNotDrug').datagrid('unselectRow',indexd);
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
					brr[i]=n;
					if(n.applyNo != null){
						arr[i]=n.applyNo;
						j=i+1;
						a=true;
					}
				});
				
				$.messager.confirm('确认','您确认想要取消药品申请去所选中的记录吗？',function(r){    
				    if (r){  
				    	if(a){
	   			    		$.ajax({
	    						url:'<%=basePath%>inpatient/applydrug/delDrugOrNotDrugApply.action',
	    						type:'post',
	    						traditional:true, 
	    						data:{'ids':arr},
	    						success:function(data){
	    							if(data.resCode == 1){
	    								$.messager.alert('提示',"取消成功！");
	    								setTimeout(function(){
	    									$(".messager-body").window('close');
	    								},3500);
	    								//视图删除
	    						    	$.each(brr,function(i,n){
	    						    		var obj=$('#listDrug').datagrid('getRows');
	    						    		var indexd;
	    						    		for(var i=0;i<obj.length;i++){
	    						    			var m=obj[i];
	    						    			if(m.recipeNo==n.recipeNo){
	    						    				if(m.sequenceNo==n.sequenceNo){
	    						    					 indexd = i;
	    						    					 break;
	    						    				 }
	    						    			}
	    						    		}
	    						    		 
	    						    		 $('#listDrug').datagrid('selectRow',indexd);
	    						    		 var drugRow = $('#listDrug').datagrid('getSelected');
	    						    		 
	    						    		 $('#listDrug').datagrid('updateRow',{
	    						    			 index: indexd,
	    						    			 row: {
	    						    				 nobackNum:drugRow.nobackNum+n.quantity,
	    						    			 }
	    						    		 });
	    						    		 
	    						    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
	    						    		 $('#listDrugBack').datagrid('unselectRow',indexd);
	    								});
	    							}
	    						}
	    					});
				    	}else{
				    		//视图删除
					    	$.each(brr,function(i,n){
					    		var obj=$('#listDrug').datagrid('getRows');
					    		var indexd;
					    		for(var i=0;i<obj.length;i++){
					    			var m=obj[i];
					    			if(m.recipeNo==n.recipeNo){
					    				if(m.sequenceNo==n.sequenceNo){
					    					 indexd = i;
					    					 break;
					    				 }
					    			}
					    		}
					    		 
					    		 $('#listDrug').datagrid('selectRow',indexd);
					    		 var drugRow = $('#listDrug').datagrid('getSelected');
					    		 
					    		 $('#listDrug').datagrid('updateRow',{
					    			 index: indexd,
					    			 row: {
					    				 nobackNum:drugRow.nobackNum+n.quantity,
					    			 }
					    		 });
					    		 
					    		 $('#listDrugBack').datagrid('deleteRow',$('#listDrugBack').edatagrid('getRowIndex',n));
					    		 $('#listDrugBack').datagrid('unselectRow',indexd);
							});
				    	}
				    }    
				});
			}else{
				$.messager.alert('友情提示','请选择药品申请区，要取消的药品');
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
				$.messager.confirm('确认','您确认想要取消非药品申请去所选中的记录吗？',function(r){    
				    if (r){    
				    	if(a){
	   			    		$.ajax({
	    						url:'<%=basePath%>inpatient/applydrug/delDrugOrNotDrugApply.action',
	    						type:'post',
	    						traditional:true, 
	    						data:{'ids':arr},
	    						success:function(data){
	    							if(data.resCode == 1){
	    								$.messager.alert('提示',"取消成功！");
	    								setTimeout(function(){
	    									$(".messager-body").window('close');
	    								},3500);
	    								 //视图删除
	    						    	$.each(brr,function(i,n){
	    						    		var obj=$('#listNotDrug').datagrid('getRows');
	    						    		var indexd;
	    						    		for(var i=0;i<obj.length;i++){
	    						    			var m=obj[i];
	    						    			if(m.recipeNo==n.recipeNo){
	    						    				if(m.sequenceNo==n.sequenceNo){
	    						    					 indexd = i;
	    						    					 break;
	    						    				 }
	    						    			}
	    						    		}
	    						    		 
	    						    		 $('#listNotDrug').datagrid('selectRow',indexd);
	    						    		 var drugRow = $('#listNotDrug').datagrid('getSelected');
	    						    		 
	    						    		 $('#listNotDrug').datagrid('updateRow',{
	    						    			 index: indexd,
	    						    			 row: {
	    						    				 nobackNum:drugRow.nobackNum+n.quantity,
	    						    			 }
	    						    		 });
	    						    		 
	    						    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
	    						    		 $('#listNotDrug').datagrid('unselectRow',indexd);
	    								});
	    							}else{
	    								$.messager.alert('提示',data.resMes,'error');
	    								
	    							}
	    						}
	    					});
				    	}else{
				    		 //视图删除
					    	$.each(brr,function(i,n){
					    		var obj=$('#listNotDrug').datagrid('getRows');
					    		var indexd;
					    		for(var i=0;i<obj.length;i++){
					    			var m=obj[i];
					    			if(m.recipeNo==n.recipeNo){
					    				if(m.sequenceNo==n.sequenceNo){
					    					 indexd = i;
					    					 break;
					    				 }
					    			}
					    		}
					    		
					    		var indbacl = $('#listNotDrugBack').edatagrid('getRowIndex',n);
					    		 $('#listNotDrugBack').datagrid('selectRow',indbacl);
					    		 var backRow = $('#listNotDrugBack').datagrid('getSelected');
					    		 
					    		 $('#listNotDrug').datagrid('selectRow',indexd);
					    		 var drugRow = $('#listNotDrug').datagrid('getSelected');
					    		 
					    		 $('#listNotDrug').datagrid('updateRow',{
					    			 index: indexd,
					    			 row: {
					    				 nobackNum:drugRow.nobackNum+backRow.quantity,
					    			 }
					    		 });
					    		 
					    		 $('#listNotDrugBack').datagrid('deleteRow',$('#listNotDrugBack').edatagrid('getRowIndex',n));
					    		 $('#listNotDrug').datagrid('unselectRow',indexd);
							});
				    	}
				    }
				});
			}else{
				$.messager.alert('友情提示','请选择非药品申请区，要取消的非药品');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
	}
}

 
/***
 * 保存申请
 */
function save(){
	$('#drugJson').val(JSON.stringify( $('#listDrugBack').datagrid("getRows")));
	$('#notDrugJson').val(JSON.stringify( $('#listNotDrugBack').datagrid("getRows")));
	$('#saveform').form('submit',{
        url:'<%=basePath %>inpatient/applydrug/directSave.action',
        onSubmit:function(){ 
        	var medicalrecordId = $('#medicalrecordId1').val();
        	var drugJson = $('#drugJson').val();
        	var notDrugJson = $('#notDrugJson').val();
        	if(medicalrecordId==''){
        		$.messager.alert('友情提示','患者病历号不能为空');
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        		return false;
        	}else if(drugJson=='[]' && notDrugJson=='[]'){
        		$.messager.alert('友情提示','请申请内容为空');
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        		return false;
        	}else{
        		$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
        		return true;
        	}
        },  
        success:function(obj){
        	var data = eval("("+obj+")");
        	$.messager.progress('close');
        	$.messager.alert('提示',data.resMsg);
        	setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
        	var blh = $('#medicalrecordId1').val();  
        	var inpatientNo = $('#inpatientNo').val();  
        	loadTabs(blh,inpatientNo);
        }
	});
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
	//渲染表单中的包装单位
	$.ajax({
		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=minunit", 
		type:'post',
		success: function(data) {
			packMap = data;
		}
	});
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


//渲染表单中的费用名称
function funminiCost(value,row,index){
	if(value!=null&&value!=''){
		return miniCostMap[value];
	}
}

/***
 * 非药品物资标识
 * 0非药品 2物资
 */
function funitemFlag(value,row,index){
	if(value == 0){
		return "非药品";
	}else if(value == 2){
		return "物资";
	}
}

//渲染表单中的包装单位
function funPackUnit(value,row,index){
	if(null!=row.extFlag3&&''!=row.extFlag3){
 		if(row.extFlag3=='1'){//包装单位
 			if(value!=null&&value!=''){
 	 			return unitMap[value];
 	 		}
 		}else if(row.extFlag3=='2'){//最小单位
 			if(value!=null&&value!=''){
 	 			return packMap[value];
 	 		}
 		}
 		
 	}
}
//渲染数量
function funNum(value,row,index){
	if(value!=null&&value!=''){
		if(row.extFlag3=="1"){//包装单位
			return value/row.packQty;
		}else if(row.extFlag3=="2"){//最小单位
			return value;
		}
	}
	return value;
}
</script> 
</head>
<body>
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',border:false" style="height:50px;width: 100%;padding-top: 10px;">
	    	<span style="margin-left: 0px;">
   				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="searchEvent()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</shiro:hasPermission> 
				<shiro:hasPermission name="${menuAlias}:function:readCard">
					<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
				</shiro:hasPermission>
	        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
	        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
				</shiro:hasPermission>
	 			<shiro:hasPermission name="${menuAlias}:function:cancel">
	 				<a href="javascript:remove()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	 			</shiro:hasPermission>
	 			<shiro:hasPermission name="${menuAlias}:function:returns">
	 				<a href="javascript:save()"  class="easyui-linkbutton" data-options="iconCls:'icon-backfee2'">直接退费</a>
	 			</shiro:hasPermission>
  			</span>
	    </div>
	    <div data-options="region:'west',title:'患者信息',split:true" style="width:16%;height: 100%">
	    	<ul id="tDt"></ul>
	    </div>   
	    <div data-options="region:'center',border:false" style="width: 84%;height: 100%">
	    	<div id="cc1" class="easyui-layout" data-options="fit:true">   
			    <div data-options="region:'north'" style="height:100px;padding: 5px;width: 100%;overflow-y:hidden">
			    	<form id="saveform"  method="post">
				    	<table id="tp" class="tableCss" style="width: 100%;">
				    		<tr>
								<td class="tableLabel">病历号：</td>
								<td><input class="easyui-textbox" id="medicalrecordId" name='medicalrecordId1' data-options="prompt:'请输入病历号回车查询'"/>
									<input  id="medicalrecordId1" name='medicalrecordId' type="hidden"/>
									<input type="hidden" id="inpatientNo" name="inpatientNo">
									<input type="hidden" id="deptId" name="deptId" value="${deptId }"> 
								</td>
								<td class="tableLabel">姓名：</td>
								<td><input class="easyui-textbox" id="name" readonly="readonly"/></td>
								<td class="tableLabel">合同单位：</td>
								<td><input class="easyui-textbox" id="pactCode" readonly="readonly" /></td>
								<td class="tableLabel">住院科室：</td>
								<td><input  class="easyui-textbox"id="inhosDept" readonly="readonly"/></td>
								<td class="tableLabel">床号：</td>
								<td><input class="easyui-textbox" id="bedId" readonly="readonly"/></td>
								<td style="display: none"><input id="drugJson" name="drugJson"/></td>
								<td style="display: none"><input id="notDrugJson" name="notDrugJson"/></td>
							</tr>
				    	</table>
				    	<table id="table2" class="tableCss" style="width: 100%;margin-top: 10px;">
				    		<tr>
								<td>
									<span>
										记账日期：
										<input id="firstDate" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										至
										<input id="endDate" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									</span>
									<span style="margin-left: 20px">
										项目检索：<input class="easyui-textbox" id="objname" />
									</span>
									<span style="margin-left: 20px">
									<shiro:hasPermission name="${menuAlias}:function:query">
										<a href="javascript:retrieval()" class="easyui-linkbutton" iconCls="icon-search">查询明细</a>
										</shiro:hasPermission>
									</span>
								</td>
							</tr>
				    	</table>
				    </form>
			    </div>   
			    <div data-options="region:'center',border:false" style="width: 100%;height: 95%">
			    	<div id="ttdiv" class="easyui-tabs" data-options="fit:true">   
					    <div title="患者药品区" style="width: 100%;height: 100%"	>   
					        <div id="cc2" class="easyui-layout" data-options="fit:true">   
							    <div data-options="region:'north',border:false" style="height:47%;width: 100%">
							    	<table id="listDrug" class="easyui-datagrid" style="width: 100%;height: 90%;" 
							         data-options="rownumbers:true,idField: 'id',striped:true,border:false,
							          checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
							        	<thead>
							        		<tr>
							        			<th data-options="field:'id',checkbox:true"></th>   
							        			<th data-options="field:'drugCode',hidden:true" style="width: 9%">药品编码</th>  
							        		    <th data-options="field:'drugName'" style="width: 17%">药品名称</th>   
									            <th data-options="field:'specs'" style="width: 9%">规格</th>  
									            <th data-options="field:'costName',formatter:funminiCost" style="width: 9%">费用名称</th>   
									            <th data-options="field:'price'" style="width: 9%" align="right" halign="left">价格</th>   
									            <th data-options="field:'nobackNum',formatter:funNum" style="width: 7%">可退数量</th>   
									            <!-- <th data-options="field:'unit'" style="width: 9%">单位</th> --> 
			 						            <th data-options="field:'unit',hidden:true" style="width: 9%">单位</th>
			 						            <th data-options="field:'showUnit',formatter:funPackUnit" style="width: 9%">单位</th>    
									            <th data-options="field:'moneySum'" style="width: 9%" align="right" halign="left">金额</th> 
									            <th data-options="field:'executeDeptName'" style="width: 9%">执行科室</th>  
									            <th data-options="field:'recipeDocName'" style="width: 9%">开方医师</th> 
									            <th data-options="field:'execDate'" style="width: 9%">记账日期</th>
									            <th data-options="field:'senddrugFlag',formatter:funIsDispensing" style="width: 9%">发药状态</th>
									            <th data-options="field:'code'" style="width: 9%">编码</th>
									            <th data-options="field:'moOrder'" style="width:auto">医嘱号</th>   
									            <th data-options="field:'moExecSqn'" style="width: auto">医嘱执行号</th>   
									            <th data-options="field:'recipeNo'" style="width: 9%">处方号</th>   
									            <th data-options="field:'sequenceNo'"  style="width: 9%">处方内流水号</th> 
									            <th data-options="field:'packQty'" style="width: 9%">包装数</th>   
									            <th data-options="field:'isUnit'" style="width: 9%">是否包装单位</th>   
									            <th data-options="field:'pinyin'" style="width: 9%">拼音码</th>   
									            <th data-options="field:'recipeDeptName'"  style="width: 9%">开方科室</th>
									            <th data-options="field:'operationId',hidden:true">手术序号</th>
								        	</tr>
							        	</thead>
							        </table>
							    </div>   
							    <div data-options="region:'center'" style="width: 10%;height: 40px;border-left:0">
							    	 <table style="height:100%;">
										<tr>
											<td style="padding-left: 10px;">药品名称：<input class="easyui-textbox" id="DrugName" readonly="readonly"/></td>
											<td style="padding-left: 10px;">价格：<input class="easyui-textbox" id="price" readonly="readonly"/></td>
											<td style="padding-left: 10px;">数量：<input class="easyui-numberbox" id="num" data-options="min:1"/></td>					
											<td style="padding-left: 20px;"><input type="checkbox" id="cheacks"/>全退</td>
											<td style="padding-left: 20px;"><input id='drugorback' type='hidden' /></td>
										</tr>
									</table>
							    </div>   
							    <div data-options="region:'south'" style="height:45%;width: 100%;border-left:0">
							    	<table id="listDrugBack" class="easyui-datagrid" title='药品退费申请区' style="width: 100%;height:90%;" 
							         data-options="rownumbers:true,striped:true,border:false,
							         checkOnSelect:true,selectOnCheck:true,singleSelect:false,fit:true">   
							        	<thead>
							        		<tr>
							        			<th data-options="field:'applyNo',hidden:true" ></th> 
							        			<th data-options="field:'id',hidden:true">id</th>     
							        			<th data-options="field:'drugCode',hidden:true">药品编码</th>
							        		    <th data-options="field:'drugName'" style="width: 15%">药品名称</th>   
									            <th data-options="field:'specs'" style="width: 9%">规格</th>  
									            <th data-options="field:'price'" style="width: 9%" align="right" halign="left">价格</th>   
									            <th data-options="field:'quantity',formatter:funNum" style="width: 9%">退费数量</th>   
									            <th data-options="field:'unit',hidden:true" style="width: 9%">单位</th> 
									            <th data-options="field:'showUnit',formatter:funPackUnit" style="width: 9%">单位</th>  
									            <th data-options="field:'ck'" 
							           						 style="width: 9%" align="right" halign="left">金额</th>  
									            <th data-options="field:'senddrugFlag',formatter:funIsDispensing" style="width: 9%">发药状态</th>   
									            <th data-options="field:'isReturnsApply',
									            		formatter:function(value,row,index){
									            			if(row.applyNo){
									            				return '是';
									            			}else{
									            				return '否';
									            			}
									            		}"  
									            		style="width: 9%">是否退费申请</th>   
									            <th data-options="field:'recipeNo'" style="width: 9%">处方号</th>   
									            <th data-options="field:'sequenceNo'"  style="width: 9%">处方内流水号</th> 
									            <th data-options="field:'operationId',hidden:true">手术序号</th>
								        	</tr>
							        	</thead>
							        </table>
							    </div>   
							</div>  
					    </div>   
					     <div title="患者非药品区" style="width: 100%;height: 100%">   
					        <div id="cc3" class="easyui-layout" data-options="fit:true">   
							    <div data-options="region:'north',border:false" style="height:47%;width: 100%">
							    	<table id="listNotDrug" class="easyui-datagrid"  style="width: 100%;height: 90%;"
							         data-options="rownumbers:true,idField: 'id',striped:true,border:false,fitColumns:false,
							          checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">   
							        	<thead>
							        		<tr>
							        			<th data-options="field:'id',checkbox:true">id</th> 
							        		    <th data-options="field:'objCode',hidden:true" style="width: 9%">项目编码</th>   
							        		    <th data-options="field:'objName'" style="width: 17%">项目名称</th>   
									            <th data-options="field:'costName',formatter:funminiCost" style="width: 9%">费用名称</th>   
									            <th data-options="field:'price'" style="width: 9%" align="right" halign="left">价格</th>   
									            <th data-options="field:'nobackNum'" style="width: 9%">可退数量</th>   
									            <th data-options="field:'unit'" style="width: 9%">单位</th> 
									            <th data-options="field:'moneySum',
									            		formatter: function(value,row,index){
															return row.price * row.nobackNum;
														}"
									            		style="width: 9%" align="right" halign="left">金额</th> 
									            <th data-options="field:'executeDeptName'" style="width: 9%">执行科室</th>  
									            <th data-options="field:'recipeDocName'" style="width: 9%">开方医师</th> 
									            <th data-options="field:'execDate'" style="width: 9%">记账日期</th>  
									            <th data-options="field:'senddrugFlag',formatter:funIsExecute" style="width: 9%">是否执行</th>
									            <th data-options="field:'code'" style="width: 9%">编码</th>        
									            <th data-options="field:'moOrder'" style="width: 9%">医嘱号</th>   
									            <th data-options="field:'moExecSqn'" style="width: 9%">医嘱执行号</th>   
									            <th data-options="field:'recipeNo'" style="width: 9%">处方号</th>   
									            <th data-options="field:'sequenceNo'"  style="width: 9%">处方流水号</th>    
									            <th data-options="field:'pinyin'" style="width: 9%">拼音码</th>   
									            <th data-options="field:'recipeDeptName'"  style="width: 9%">开方科室</th>
									            <th data-options="field:'updateSequenceno'"  style="width: 9%">出库流水号</th>
									            <th data-options="field:'stockNo'"  style="width: 9%">库存序号</th>
									            <th data-options="field:'itemFlag',formatter:funitemFlag"  style="width: 9%">标识</th>
									            <th data-options="field:'operationId',hidden:true">手术序号</th>
								        	</tr>
							        	</thead>   
							        </table>
							    </div>   
							    <div data-options="region:'center'" style="width: 10%;height: 40px;">
							    	<table style="height:100%;">
										<tr>
											<td style="padding-left: 10px;">项目名称：<input class="easyui-textbox" id="itemName" readonly="readonly" /></td>
											<td style="padding-left: 10px;">价格：<input class="easyui-textbox" id="itemPrice" readonly="readonly"/></td>
											<td style="padding-left: 10px;">数量：<input class="easyui-numberbox" id="itemNum"/><td>
											<td style="padding-left: 20px;"><input type="checkbox" id="cheacknot"/>全退</td>
											<td style="padding-left: 20px;"><input id='notDrugorback' type='hidden'/></td>
										</tr>
									</table>
							    </div>  
							     <div data-options="region:'south'" style="height:45%;width: 100%">
							     	<table id="listNotDrugBack" class="easyui-datagrid" title='非药品退费申请区' style="width: 100%;height:390px;min-height: 200px;"
								         data-options="rownumbers:true,striped:true,border:false,
								         checkOnSelect:true,selectOnCheck:true,singleSelect:false,width:'100%',fit:true">   
							        	<thead>
							        		<tr>
							        			<th data-options="field:'applyNo',hidden:true"></th> 
							        			<th data-options="field:'id',hidden:true">id</th> 
							        			<th data-options="field:'objCode',hidden:true" style="width: 9%">项目编码</th>   
							        		    <th data-options="field:'objName'" style="width: 15%">项目名称</th>   
									            <th data-options="field:'costName',formatter:funminiCost" style="width: 9%">费用名称</th>  
									            <th data-options="field:'price'" style="width: 9%" align="right" halign="left">价格</th>  
									            <th data-options="field:'quantity'" style="width: 5%">退费数量</th>  
									            <th data-options="field:'unit'" style="width: 9%">单位</th> 
									             <th data-options="field:'ck',
														           formatter: function(value,row,index){
																		return (row.price * row.quantity).toFixed(2);
														            }" 
							           						 style="width: 9%" align="right" halign="left">金额</th>  
									            <th data-options="field:'executeDeptName'" style="width: 9%">执行科室</th>  
									            <th data-options="field:'senddrugFlag',formatter:funIsExecute" style="width: 7%">是否执行</th>  
									             <th data-options="field:'isReturnsApply',
										            		formatter:function(value,row,index){
										            			if(row.applyNo){
										            				return '是';
										            			}else{
										            				return '否';
										            			}
										            		}"  
									            		style="width: 7%">是否退费申请</th>   
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
			    </div>   
			</div>
	    </div>   
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="双击选择患者" style="width:1000;height:500;padding:5" data-options="modal:true, closed:true">   
	   <table id="infoDatagrid"  style="width:100%;height:100%" data-options="fitColumns:true,singleSelect:true"></table>
	</div>  

</body>
</html>