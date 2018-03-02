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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/hisUtil.js"></script>
<script type="text/javascript">

var packMap = "";//包装单位
var feepackMap = "";//包装单位
var feeminMap = "";//最小单位
var deptMap="";//部门
var empMap="";//医生
var contMap="";//合同单位
var frequencyMap ="";//频次
var empUserMap ="";//用户
var textFlg = "";
var SearchFromData=null;  //病历号（就诊卡号）模糊查询出的患者信息
var sexMap=new Map();
var systemTypeMap = "";
var invoiceMap = "";//Map<门诊号_分发票类型,发票号_金额> 
var pubRatio = 0.0;//公费比例
var payRatio = 0.0;//自付比例
var ownRatio = 0.0;//自费比例
var ecoRatio = 0.0;//优惠比例
var flag = "";//flag适用标识   0全部   1药品   2非药品
var feeWhenUnenougth = 0;//当库存不足的时候是否继续收费，0否/1是
//初始化加载页面
$(function(){
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	//系统类别渲染
	$.ajax({
		url : "<%=basePath %>/finance/medicinelist/getSystemTypeMap.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			systemTypeMap = data;
		}
	});
	
	//病历号回车查询
	bindEnterEvent('blhId',dialogQueryBlhOrCardNo,'easyui');
	//就诊卡号回车查询
	bindEnterEvent('jzkh',dialogQueryBlhOrCardNo,'easyui');
	//就诊卡号与病历号选择
	$('#jzkh').textbox('textbox').bind('focus', function(event) {
		$('#blhId').textbox('setValue','');
	});
	//就诊卡号与病历号选择
	$('#blhId').textbox('textbox').bind('focus', function(event) {
		$('#jzkh').textbox('setValue','');
	});
	
	
	
	//模糊非药品查询
	bindEnterEvent('codes',searchFrom,'easyui');
	//非药品查询
// 	bindEnterEvent('addNotDrugId',wondowNotDrug,'easyui');
		//非药品查询下拉框
		$('#addNotDrugId').combogrid({    
		    panelWidth:530,    
		    valueField:'undrugCode',    
		    textField:'undrugName',    
		    url:'<%=basePath%>finance/medicinelist/findNotDrugInfoViewList.action?menuAlias=${menuAlias}',    
		    queryParams:{p:$('#addNotDrugId').combogrid('getText')},
		    disabled : false,
			mode:'remote',
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			striped : true,//数据背景颜色交替
			panelWidth : 530,//容器宽度
			fitColumns : true,//自适应列宽
			pageSize : 10,//每页显示的记录条数，默认为20  
			pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
		    columns:[[    
		        {field:'undrugName',title:'名称',width:120},    
		        {field:'spec',title:'规格',width:100},    
		        {field:'defaultprice',title:'价格',width:60},    
		        {field:'unit',title:'单位',width:50},   
		        {field:'undrugInputcode',title:'自定义码',width:100}, 
		        {field:'undrugNotes',title:'注意事项',width:100}   
		    ]],
		    onSelect:function (rowIndex, rowData){
		    	var rows = $('#list').datagrid('getRows');
		    	$('#list').datagrid('endEdit',rows.length-1);
		    	$('#list').datagrid('uncheckAll');
		    	if(rows.length>0){
					for(var i=0;i<rows.length;i++){
						if(typeof rows[i].recipeNo=="undefined"){
							$('#list').datagrid('checkRow',$('#list').datagrid('getRowIndex',rows[i]));//选中一个手动添加的复选框，即选中多个复选框
							$('#list').datagrid('updateRow',{
								index: $('#list').datagrid('getRowIndex',rows[i]),
								row: {
									youli: 1
								}
							});
						}
					}
				}
		    	var conts = $('#conts').val();
				$.ajax({
					url: '<%=basePath%>finance/medicinelist/queryMinState.action',
					data:{"encode":rowData.undrugMinCode},
					success: function(dataOBJ) {
						enCodeMinMin = dataOBJ.feeStatCode;
						var index = $('#list').datagrid('appendRow',{
							stust : 1,
							FeedetailId : rowData.undrugCode,
							drugFlag:0,
							adviceName: rowData.undrugName,
							feeCode:rowData.undrugMinCode,
							totalNum :1,
							setNum:1,
							dosage:1,
							unit:rowData.unit,
							frequency:1,
							usageName:rowData.undrugNotes,
							executiveDept :"",
							adPriceSum : rowData.defaultprice,
							unitPrice :conts,//结算类型
							adPrice : rowData.defaultprice,
							remark :rowData.undrugRemark,
							injectNumber:0,
							sysType:rowData.undrugSystype,
							youli:1,
							subjobFlag:0,
							feeStatCode :enCodeMinMin,
							feeStatName:dataOBJ.feeStatName,
							confirmCode:null,
							confirmDept:null,
							confirmDate:null,
							confirmNum:0,
							issubmit:dataOBJ.issubmit,
							confirmFlag:0,
							pubCost:0.00,
							ecoCost:0.00,
							overCost:0.00,
							excessCost:0.00,
							drugOwncost:0.00,
							payCost:0.00,
							extFlag3:0
						}).datagrid('getRows').length-1;
						$('#list').datagrid('checkRow',index);
						//查询其有没有辅材 有辅材一起带出来
						$.ajax({
							url: '<%=basePath%>finance/medicinelist/findOdditionalitemByTypeCode.action',
							data:{"undrugId":rowData.undrugCode},
							success: function(dataObj){
								if(dataObj.length>0){
									for(var n=0;n<dataObj.length;n++){
										var index = $('#list').datagrid('appendRow',{
											stust :1,
											FeedetailId : dataObj[n].FeedetailId,
											drugFlag:0,
											adviceName: dataObj[n].adviceName,
											feeCode:dataObj[n].feeCode,
											totalNum :dataObj[n].totalNum,
											unit:dataObj[n].unit,
											usageName:dataObj[n].usageName,
											executiveDept :dataObj[n].executiveDept,
											adPriceSum : dataObj[n].adPriceSum,
											unitPrice :conts,
											adPrice : dataObj[n].adPrice,
											remark :dataObj[n].remark,
											injectNumber:0,
											sysType:dataObj[n].sysType,
											subjobFlag:1,
											follow:rowData.id+"F",
											wtotalNum :dataObj[n].totalNum,
											youli:1,
											feeStatCode :dataObj[n].feeStatCode,
											feeStatName:dataObj[n].feeStatName,
											confirmCode:null,
											confirmDept:null,
											confirmDate:null,
											confirmNum:0,
											issubmit:dataObj[n].issubmit,
											confirmFlag:0
										}).datagrid('getRows').length-1;
										$('#list').datagrid('checkRow',index);
										sortFunction();
									}
								}
							}
						});
					}
				});
			}
		});

	//计算实缴金额-应缴金额-找零金额
	$('#actualCollection').numberbox('textbox').bind('keyup', function(event) {
		var actualCollections = $('#actualCollection').numberbox('getText');
		var actualCollection= actualCollections*100;
		var shouldPays = $('#shouldPay').numberbox('getValue');
		var shouldPay = shouldPays*100;
		var giveChanges =actualCollection-shouldPay;
		var giveChange = giveChanges/100;
		$('#giveChange').numberbox('setValue',giveChange);
	});
	//支票-现金-银行卡-院内账户
	$('#check').numberbox('textbox').bind('keyup', function(event){
		var shouldPay = $('#yjjes').text();
		$('#hospitalAccount').numberbox('setValue','');
		$('#bankCard').numberbox('setValue','');
		var check = $('#check').numberbox('getText');
		var nowCash = shouldPay - check;
		if(nowCash<0){
			$('#check').numberbox('setValue','');
			$('#cash').numberbox('setValue',shouldPay);
			$.messager.alert("操作提示", "大于应缴金额");
			return;
		}
		$('#cash').numberbox('setValue',nowCash);
		//支持各种支付方式时请取消下面注释并注释掉上面的
// 		giveChange();
	});
	//院内账户-现金-银行卡-支票
	$('#hospitalAccount').numberbox('textbox').bind('keyup', function(event) {
		$('#check').numberbox('setValue','');
		$('#bankCard').numberbox('setValue','');
		var hospitalAccount = $('#hospitalAccount').numberbox('getText');
		var shouldPay = $('#yjjes').text();
		var nowCash = shouldPay - hospitalAccount;
		if(nowCash<0){
			$('#hospitalAccount').numberbox('setValue','');
			$('#cash').numberbox('setValue',shouldPay);
			$.messager.alert("操作提示", "大于应缴金额");
			return;
		}
		$('#cash').numberbox('setValue',nowCash);
		//支持各种支付方式时请取消下面注释并注释掉上面的
// 		giveChange();
	});
	//选择现金
	$('#cash').numberbox('textbox').bind('keyup', function(event) {
		$('#cash').numberbox('setValue',$('#yjjes').text());
		$('#bankCard').numberbox('setValue','');
		$('#check').numberbox('setValue','');
		$('#hospitalAccount').numberbox('enable');
		$('#hospitalAccount').numberbox('setValue','');
		$('#shouldPay').numberbox('setValue',$('#yjjes').text());
// 		giveChange();
	});
	//选择银行卡
	$('#bankCard').numberbox('textbox').bind('keyup', function(event) {
		var shouldPay = $('#yjjes').text();
		$('#check').numberbox('setValue','');
		$('#hospitalAccount').numberbox('setValue','');
		var bankPay = $('#bankCard').numberbox('getText');
		var nowCash = shouldPay - bankPay;
		if(nowCash<0){
			$('#bankCard').numberbox('setValue','');
			$('#cash').numberbox('setValue',shouldPay);
			$.messager.alert("操作提示", "大于应缴金额");
			return;
		}
		$('#cash').numberbox('setValue',nowCash);
		//支持各种支付方式时请取消下面注释并注释掉上面的
// 		giveChange();
	});
	//选择选择院内账户的
	$('#hospitalAccount').textbox('textbox').bind('focus', function(event) {
		$('#accountPassword').numberbox('enable');
// 		giveChange();
	});
// 	//开立科室下拉
// 	$('#deptId').combobox({    
<%-- 	url:'<%=basePath%>finance/medicinelist/quertComboboxDept.action', --%>
// 	    valueField:'deptCode',    
// 	    textField:'deptName',
// 	    onSelect:function(record){
<%-- 		 $('#docId').combobox('setValue',""); --%>
<%-- 	   		$('#docId').combobox('reload', '<%=basePath%>finance/medicinelist/quertComboboxEmp.action?feedetail.regDpcd='+record.id); --%> 
// 		}
// 	}); 
	//绑定回车事件
	bindEnterEvent('deptId',popWinToDept,'easyui');
	//开立医生下拉
// 	$('#docId').combobox({    
<%-- 		url:'<%=basePath%>finance/medicinelist/quertComboboxEmp.action?feedetail.regDpcd='+"", --%>
// 	    valueField:'jobNo',
// 	    textField:'name'
// 	}); 
	//绑定回车事件
	bindEnterEvent('docId',popWinToEmployee,'easyui');
		//合同单位下拉
		$('#contractId').combobox({    
			url:'<%=basePath%>finance/medicinelist/quertComboboxCont.action',
		    valueField:'encode',    
		    textField:'name',
		    onSelect:function(record){
		    	pubRatio = record.pubRatio;//公费比例
		    	payRatio = record.payRatio;//自负比例
		    	ownRatio = record.ownRatio;//自费比例
		    	ecoRatio = record.ecoRatio;//优惠比例
		    	flag = record.flag;//flag适用标识   0全部   1药品   2非药品
				if(0!=record.mcardFlag){//不需要医疗证号
					$('#certificate').textbox('enable');
					$('#certificateRequired').val(1);//需要输入医疗证号 1是0否
					$('#certificate').textbox({required:true});
					$('#conts').val(record.paykindCode);
					var listRows = $('#list').datagrid('getRows');
					if(listRows.length>0){
						for(var y=0;y<listRows.length;y++){
							$('#list').datagrid('updateRow',{
								index: $('#list').datagrid('getRowIndex',listRows[y]),
								row: {
									unitPrice:record.paykindCode
								}
							});
						}
					}
				}else if(0==record.mcardFlag){
					$('#certificate').textbox('disable');
					$('#certificateRequired').val(0);
					$('#certificate').textbox({required:false});
					var listRows = $('#list').datagrid('getRows');
					if(listRows.length>0){
						for(var y=0;y<listRows.length;y++){
							$('#list').datagrid('updateRow',{
								index: $('#list').datagrid('getRowIndex',listRows[y]),
								row: {
									unitPrice:record.paykindCode
								}
							});
						}
					}
				}
			},filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
			,onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].encode) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox('clear');
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
			}
		});
		$.ajax({//渲染合同单位
			url: '<%=basePath%>finance/medicinelist/querycontfunction.action',
			async:true,
			type:'post',
			success: function(contData) {
				contMap = contData;
			}
		});
		$.ajax({//渲染科室
			url: '<%=basePath%>baseinfo/department/getDeptMap.action' ,
			type:'post',
			success: function(deptData) {
				deptMap = deptData;
			}
		});
		//查询渲染药品单位
		$.ajax({
			url: "<%=basePath%>finance/refund/packFunction.action",
			type:'post',
			success: function(packData) {
				feepackMap = packData.drugPackUint;
				feeminMap = packData.drugMinUint;
			}
		});
		$.ajax({//渲染单位
			url: '<%=basePath%>finance/medicinelist/queryPackFunction.action',
			type:'post',
			success: function(packData){
				packMap = packData;
			}
		});
		$.ajax({//渲染频次
			url: '<%=basePath%>finance/medicinelist/queryfrequenyFunction.action',
			type:'post',
			success: function(frequencyDate) {
				frequencyMap = frequencyDate;
			}
		});
	
	bindEnterEvent('contractId',popWinToContractId,'easyui');

// 	$.ajax({//渲染人员
<%-- 		url: '<%=basePath%>baseinfo/employee/getEmplMap.action', --%>
// 		type:'post',
// 		async:true,
// 		success: function(empData) {	
// 			empMap = empData;
// 		}
// 	});
	
	
	var feeclinic="";
	$('#feedetailId').datagrid({
		pagination:false,
		url:'<%=basePath%>finance/medicinelist/findFeedetailStatistics.action?menuAlias=${menuAlias}',
		queryParams:{"feedetail.clinicCode":feeclinic,"feedetail.regDpcd":null,"feedetail.doctCode":null},
		onBeforeLoad : function(node){
			var param = new Map();
			if(feeclinic==""||feeclinic==null){
				return false;
			}
		},
		onCheck : function (rowIndex, rowData) {
			if(rowData.val==""||rowData.val==null){
				$.messager.progress({text:'查询中，请稍后...',modal:true});
				$.post('<%=basePath%>finance/medicinelist/findFeedetailDetails.action',
			        {"feedetail.recipeNo":rowData.recipeNo},
			        function(objData){
			        	$.messager.progress('close');
			        	var conts = $('#conts').val();
			        	var list = $('#list').datagrid('getRows');
			        	
						for(var i=0;i<objData.length;i++){
							var flag = false;
							for(var j=0;j<list.length;j++){
								if(list[j].FeedetailId==objData[i].FeedetailId){
									flag = true;
								}
							}
							if(flag){
								break;
							}
							var index = $('#list').edatagrid('appendRow',{
								stust :"",//是否是新加非药品
								FeedetailId : objData[i].FeedetailId,//表的id
								group:objData[i].group,//组套号
								frequencyHid :objData[i].frequencyHid,//频次号
								drugFlag:objData[i].drugFlag,//是否是药品
								adviceId :objData[i].adviceId,//药品非药品ID
								adviceName: objData[i].adviceName,//药品非药品名称
								feeCode:objData[i].feeCode,//最小费用代码
								totalNum :objData[i].totalNum,//数量
								setNum:objData[i].setNum,//付数
								dosageHid:objData[i].dosageHid,//用量
								unit :objData[i].unit,//单位
								executiveDept :objData[i].executiveDept,//执行科室
								adPriceSum : objData[i].adPriceSum,//总金额
								unitPrice :conts,//结算类型
								adPrice : objData[i].adPrice,//单价
								remark :objData[i].remark,//备注
								recipeNo:objData[i].recipeNo,//处方号
								usageNameHid : objData[i].usageNameHid,//用法
								youli:1,//状态
								injectNumber:objData[i].injectionNum,
								sysType:objData[i].sysType,
								extendOne: objData[i].extendOne,
								moOrder:objData[i].moOrder,
								subjobFlag:objData[i].subjobFlag,
								feeStatCode:objData[i].feeStatCode,
								feeStatName:objData[i].feeStatName,
								confirmCode:objData[i].confirmCode,
								confirmDept:objData[i].confirmDept,
								confirmDate:objData[i].confirmDate,
								confirmNum:objData[i].confirmNum,
								issubmit:objData[i].issubmit,
								confirmFlag:objData[i].confirmFlag,
								pubCost:objData[i].pubCost,
								ecoCost:objData[i].ecoCost,
								overCost:objData[i].overCost,
								excessCost:objData[i].excessCost,
								drugOwncost:objData[i].drugOwncost,
								payCost:objData[i].payCost,
								classCode:objData[i].classCode,
								extFlag3:objData[i].extFlag3
							}).datagrid('getRows').length-1;
						}
						$('#list').datagrid('checkAll');
						sortFunction();
			   	});
			}
			$('#feedetailId').datagrid('updateRow',{
				index: rowIndex,
				row: {
					val: '2'
				}
			});
		},
		onUncheck : function (rowIndex, rowData) {
			var rows = $('#list').datagrid('getRows');
			var datas = new Array();//存放数据的数组
			if(rowData.recipeNo!=null){
				for(var i=0;i<rows.length;i++){
					if(rowData.recipeNo!=rows[i].recipeNo){
						datas[datas.length] = rows[i];
					}
				}
				var listRows = $('#list').datagrid('getRows');
				if(listRows!=null&&listRows.length>0){
					var listR = listRows.length;
					for(var j=0;j<listR;j++){
						$('#list').datagrid('deleteRow',0);
					}
				}
				if(datas.length>0){
					for(var j=0;j<datas.length;j++){
						$('#list').datagrid('insertRow',{
							row:datas[j]
						});
					}
				}
				$('#feedetailId').datagrid('updateRow',{
					index: rowIndex,
					row: {
						val: ''
					}
				});
				$('#list').datagrid('checkAll');
				sortFunction();
			}
		},
		onCheckAll :function(rows){
			var conts = $('#conts').val();
			var rows = $('#feedetailId').datagrid('getChecked');
			var ids = "";
			var rowList = $('#list').datagrid('getRows');
			if(rowList!=null&&rowList.length>0){
				var listR = rowList.length;
				for(var j=0;j<listR;j++){
					$('#list').datagrid('deleteRow',0);
				}
			}
			if(rows.length>0){
				for(var i=0;i<rows.length;i++){
// 					if(rows[i].val==""||rows[i].val==null){
						if(ids!=""){
							ids = ids +"','";
						}
						ids = ids + rows[i].recipeNo;
// 					}
				}
				
				$.post('<%=basePath%>finance/medicinelist/findFeedetailDetails.action',
			        {"feedetail.recipeNo":ids},
			        function(objData){
						for(var i=0;i<objData.length;i++){
							var index = $('#list').datagrid('appendRow',{
								stust :"",//是否是新加非药品
								FeedetailId : objData[i].FeedetailId,//表的id
								group:objData[i].group,//组套号
								frequencyHid :objData[i].frequencyHid,//频次号
								drugFlag:objData[i].drugFlag,//是否是药品
								adviceId :objData[i].adviceId,//药品非药品ID
								adviceName: objData[i].adviceName,//药品非药品名称
								feeCode:objData[i].feeCode,//最小费用代码
								totalNum :objData[i].totalNum,//数量
								setNum:objData[i].setNum,//付数
								dosageHid:objData[i].dosageHid,//用量
								unit :objData[i].unit,//单位
								executiveDept :objData[i].executiveDept,//执行科室
								adPriceSum : objData[i].adPriceSum,//总金额
								unitPrice :conts,//结算类型
								adPrice : objData[i].adPrice,//单价
								remark :objData[i].remark,//备注
								recipeNo:objData[i].recipeNo,//处方号
								usageNameHid : objData[i].usageNameHid,//用法
								youli:1,//状态
								injectNumber:objData[i].injectionNum,
								sysType:objData[i].sysType,
								extendOne: objData[i].extendOne,
								moOrder:objData[i].moOrder,
								subjobFlag:objData[i].subjobFlag,
								feeStatCode:objData[i].feeStatCode,
								feeStatName:objData[i].feeStatName,
								confirmCode:objData[i].confirmCode,
								confirmDept:objData[i].confirmDept,
								confirmDate:objData[i].confirmDate,
								confirmNum:objData[i].confirmNum,
								issubmit:objData[i].issubmit,
								confirmFlag:objData[i].confirmFlag,
								pubCost:objData[i].pubCost,
								ecoCost:objData[i].ecoCost,
								overCost:objData[i].overCost,
								excessCost:objData[i].excessCost,
								drugOwncost:objData[i].drugOwncost,
								payCost:objData[i].payCost,
								extFlag3:objData[i].extFlag3
							}).datagrid('getRows').length-1;
						}
						$('#list').datagrid('checkAll');
						sortFunction();
			   	});
				if(rows.length>0){
					for(var a=0;a<rows.length;a++){
						var indexs = $('#feedetailId').datagrid('getRowIndex',rows[a]);
						$('#feedetailId').datagrid('updateRow',{
							index: indexs,
							row: {
								val: '2'
							}
						});
					}
				}
			}
			sortFunction();
		},
		onUncheckAll :function(rows){
			//改变状态
			var rows = $('#feedetailId').datagrid('getRows');
			var rowList = $('#list').datagrid('getRows');
			var datas = new Array();//存放数据的数组
			if(rowList.length>0){
				for(var a=0;a<rowList.length;a++){
					if(rowList[a].recipeNo==""||rowList[a].recipeNo==null){
						datas[datas.length] = rowList[a];
					}
				}
			}
			if(rowList!=null&&rowList.length>0){
				var listR = rowList.length;
				for(var j=0;j<listR;j++){
					$('#list').datagrid('deleteRow',0);
				}
			}
			if(datas.length>0){
				for(var j=0;j<datas.length;j++){
					$('#list').datagrid('insertRow',{
						row:datas[j]
					});
				}
			}
			if(rows.length>0){
				for(var a=0;a<rows.length;a++){
					var indexs = $('#feedetailId').datagrid('getRowIndex',rows[a]);
					$('#feedetailId').datagrid('updateRow',{
						index: indexs,
						row: {
							val: ''
						}
					});
				}
			}
			$('#list').datagrid('checkAll');
		}
	});

	$('#list').datagrid({
		pagination:false,
		remoteSort:false,
		sortOrder:'asc',
		sortName:'group',
		autoSave:true,
		url:'<%=basePath%>finance/medicinelist/findFeedetailDetails.action?menuAlias=${menuAlias}',
		onBeforeLoad : function(node, param){
		    if(node!=null&&node!=''){
		    	return false;
		    }
		},
		onSelect: function(rowIndex, rowData){
			var onDbListRows = $('#list').datagrid('getRows');
			if(onDbListRows.length>0){
				for(var m=0;m<onDbListRows.length;m++){
					var indexRows = $('#list').datagrid('getRowIndex',onDbListRows[m]);
					$('#list').datagrid('endEdit',indexRows);
				}
			}
			if(rowData.stust==1){
				$('#list').datagrid('beginEdit',rowIndex);
				var ed = $('#list').datagrid('getEditor', {index:rowIndex,field:'totalNum'});
				var t = $(ed.target).numberbox('getText');
				$(ed.target).next("span").children().first().val("").focus().val(t);
				var adPrice = rowData.adPrice;//单价
				$(ed.target).numberbox('textbox').bind('keyup', function(event) {
					var ed1 = $('#list').datagrid('getEditor', {index:rowIndex,field:'executiveDept'});
					var executiveDept = $(ed1.target).combobox('getValue');
					var totalNum = $(ed.target).numberbox('getText');
					var zjes = totalNum*adPrice;
					$('#list').datagrid('updateRow',{
						index: rowIndex,
						row: {
							adPriceSum: zjes,
							totalNum:totalNum,
							confirmNum:totalNum,
							executiveDept:executiveDept
						}
					});
					if(totalNum!=0&&totalNum!=""){
						if(rowData.subjobFlag==null||rowData.subjobFlag==""||rowData.subjobFlag==0){//主药
							if(onDbListRows.length>0){
								for(var q=0;q<onDbListRows.length;q++){
									if(rowData.FeedetailId+"F"==onDbListRows[q].follow){//取消勾选主药下的辅材
										var qtys = (onDbListRows[q].wtotalNum)*(totalNum);
										var sumMonry = (onDbListRows[q].adPrice)*(qtys);
										$('#list').datagrid('updateRow',{
											index: $('#list').datagrid('getRowIndex',onDbListRows[q]),
											row: {
												adPriceSum: sumMonry,
												totalNum:qtys
											}
										});
									}
								}
							}
						}
					}
					$('#list').datagrid('selectRow',rowIndex);
					zdjsq();
				});
			}
		},
		onCheck: function (rowIndex, rowData){
			var rows = $('#list').datagrid('getRows');
			if(rowData.subjobFlag==null||rowData.subjobFlag==""||rowData.subjobFlag==0){//主药
				if(rowData.moOrder!=null&&rowData.moOrder!=""){//医嘱
					for(var q=0;q<rows.length;q++){
						if(rowData.moOrder==rows[q].extendOne){//勾选主药下的辅材
							if(rows[q].youli!=1){//没有打钩的时候
								$('#list').datagrid('checkRow',$('#list').datagrid('getRowIndex',rows[q]));
							}
						}
					}
				}else{//自批费
					for(var q=0;q<rows.length;q++){
						if(rowData.FeedetailId+"F"==rows[q].follow){//勾选主药下的辅材
							if(rows[q].youli!=1){//没有打钩的时候
								$('#list').datagrid('checkRow',$('#list').datagrid('getRowIndex',rows[q]));
								$('#list').datagrid('updateRow',{
									index: $('#list').datagrid('getRowIndex',rows[q]),
									row: {
										youli: 1
									}
								});
							}
						}
					}
				}
			}else{//辅材
				if(rowData.moOrder!=null&&rowData.moOrder!=""){//医嘱
					$('#list').datagrid('updateRow',{
						index: rowIndex,
						row: {
							youli: 1
						}
					});
					for(var q=0;q<rows.length;q++){
						if(rowData.extendOne==rows[q].moOrder){//勾选主药下的辅材
							$('#list').datagrid('checkRow',$('#list').datagrid('getRowIndex',rows[q]));
						}
					}
				}else{//自批费
					$('#list').datagrid('updateRow',{
						index: rowIndex,
						row: {
							youli: 1
						}
					});
					var follow = (rowData.follow).substr(0,(rowData.follow).length-1);
					$('#list').datagrid('checkRow',$('#list').datagrid('getRowIndex',follow));
				}
			}
			$('#list').datagrid('updateRow',{
				index: rowIndex,
				row: {
					youli: 1
				}
			});
			
			if(rows.length>0){
				for(var i=0;i<rows.length;i++){
					if(rowData.recipeNo==rows[i].recipeNo&&rows[i].youli!=1&&typeof rows[i].recipeNo!="undefined"){
						$('#list').datagrid('checkRow',$('#list').datagrid('getRowIndex',rows[i]));//选中一个手动添加的复选框，即选中多个复选框
						$('#list').datagrid('updateRow',{
							index: $('#list').datagrid('getRowIndex',rows[i]),
							row: {
								youli: 1
							}
						});
					}
				}
			}
			
			setTimeout(function(){
				zdjsq();
			});
		},
		onUncheck :function(rowIndex, rowData){
			var rows = $('#list').datagrid('getRows');
			$('#list').datagrid('updateRow',{
				index: rowIndex,
				row: {
					youli: 2
				}
			});
			if(rowData.subjobFlag==null||rowData.subjobFlag==""||rowData.subjobFlag==0){//主药
				if(rowData.moOrder!=null&&rowData.moOrder!=""){//医嘱过来的
					if(rows.length>0){
						for(var q=0;q<rows.length;q++){
							if(rowData.moOrder==rows[q].extendOne){//取消勾选主药下的辅材
								$('#list').datagrid('uncheckRow',$('#list').datagrid('getRowIndex',rows[q]));
							}
						}
					}
				}else{//自批费过来的
					if(rows.length>0){
						for(var q=0;q<rows.length;q++){
							if(rowData.FeedetailId+"F"==rows[q].follow){//取消勾选主药下的辅材
								$('#list').datagrid('uncheckRow',$('#list').datagrid('getRowIndex',rows[q]));
							}
						}
					}
				}
			}
			if(rows.length>0){
				for(var i=0;i<rows.length;i++){
					if(rowData.recipeNo==rows[i].recipeNo&&rows[i].youli!=2&&typeof rows[i].recipeNo!="undefined"){
						$('#list').datagrid('uncheckRow',$('#list').datagrid('getRowIndex',rows[i]));
						$('#list').datagrid('updateRow',{
							index: $('#list').datagrid('getRowIndex',rows[i]),
							row: {
								youli: 2
							}
						});
					}
				}
			}
			
			setTimeout(function(){
				zdjsq();
			});
		},
		onCheckAll :function(rows){
			zdjsq();
			if(rows.length>0){
				for(var i=0;i<rows.length;i++){
					var indexs = $('#list').edatagrid('getRowIndex',rows[i]);
					$('#list').datagrid('updateRow',{
						index: indexs,
						row: {
							youli: 1
						}
					});
				}
			}
		},
		onUncheckAll :function(rows){
			zdjsq();
			if(rows.length>0){
				for(var i=0;i<rows.length;i++){
					var indexs = $('#list').edatagrid('getRowIndex',rows[i]);
					$('#list').datagrid('updateRow',{
						index: indexs,
						row: {
							youli: 2
						}
					});
				}
			}
		}
	});

	
});

/*******************************开始读卡***********************************************/
//定义一个事件（读卡）
function read_card_ic(){
	var card_value = app.read_ic();
	if(card_value=='0'||card_value==undefined||card_value=='')
	{
		$.messager.alert('提示','此卡号['+card_value+']无效');
		return;
	}
	$("#jzkh").textbox("setValue",card_value);
	var jzkh = $('#jzkh').textbox('getValue');//获取就诊卡号
	dialogQueryBlhOrCardNo();
};
/*******************************结束读卡***********************************************/
//本地下拉查询方法
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}
//计算各种支付方式的总金额，并给应缴金额赋值
function giveChange(){
	var bankCard = $('#bankCard').numberbox('getText');//银行卡
	var check = $('#check').numberbox('getText');//支票
	var hospitalAccount = $('#hospitalAccount').numberbox('getText');
	var shouldPay = $('#shouldPay').numberbox('getText');
	var cash = shouldPay-bankCard-check-hospitalAccount;//现金
	if(hospitalAccount!=null&&hospitalAccount!=''&&hospitalAccount>0){
		$('#accountPassword').textbox({required:true});
	}else{
		$('#accountPassword').textbox({required:false});
	}
// 	var change = cash+bankCard+check+hospitalAccount-shouldPay;
// 	$('#giveChange').numberbox('setValue',change.toFixed(2));
	$('#cash').numberbox('setValue',cash.toFixed(2));
}
function sortFunction() { //因为组合的问题，该方法不再使用
// 	var rows = $('#list').datagrid('getRows');
// 	rows.sort(function(a,b){
// 		if(parseInt(a.group)==parseInt(b.group)){
// 			return 0;
// 		}
// 		if(parseInt(a.group)>parseInt(b.group)){
// 			return 1;
// 		}
// 		if(parseInt(a.group)<parseInt(b.group)){
// 			return -1;
// 		}
// 	});
// 	$('#list').datagrid('load',rows);
}
//病历号模糊6位查询
function dialogQueryBlhOrCardNo(){
	var blhId = $('#blhId').textbox('getValue');//获取病历号
	var jzkh = $('#jzkh').textbox('getValue');//获取就诊卡号
	if(jzkh==""){
		if(blhId==""){
			$.messager.alert("操作提示", "请正确输入就诊卡号或病历号");
			return;
		}else{
			if(blhId!=""){
				$.post('<%=basePath%>finance/medicinelist/dialogQueryBlhOrCardNo.action?menuAlias=${menuAlias}',
			        {"feedetail.patientNo":blhId},
			        function(data){
						if(data.resSize==1){
							searchblhIdOrjzkh(data.resData[0].medicalrecordId,'');
						}else if(data.resSize>1){
							SearchFromData=data.resData;
							Adddilog('双击选择患者','<%=basePath%>finance/medicinelist/selectCardNoDialogURL.action');
						}else{
							$.messager.alert("操作提示", "患者不存在");
							return;
						}
			   	});
			}
		}
	}else{
		searchblhIdOrjzkh(blhId,jzkh);
	}
}
//根据病历号或者就诊卡号获得患者信息和挂号记录
function searchblhIdOrjzkh(blhId,jzkh){
	//清空中间收费明细列表
	var listRows = $('#list').datagrid('getRows');
	if(listRows!=null&&listRows.length>0){
		var listR = listRows.length;
		for(var j=0;j<listR;j++){
			$('#list').datagrid('deleteRow',0);
		}
	}
	//清空统计大类
	var feeList = $('#feeList').datagrid('getRows');
	if(feeList!=null&&feeList.length>0){
		var listRRR = feeList.length;
		for(var j=0;j<listRRR;j++){
			$('#feeList').datagrid('deleteRow',0);
		}
	}
	if(blhId==""&&jzkh==""){
		return;
	}
	//根据病历号||就诊卡号查询患者信息&&挂号信息
	$.ajax({
		url: '<%=basePath%>finance/medicinelist/queryBlhOrCardNo.action',
		data:{"feedetail.patientNo":blhId,"feedetail.cardNo":jzkh},
		success:function(dataMap){
			if(dataMap.resMsg=="error"){
        		$.messager.alert("操作提示",dataMap.resCode);
        		clearFeed();
        	}else if(dataMap.resMsg=="succ"){//只有一条信息的时候
        		$('#nameId').textbox('setValue',dataMap.info.patientName);//患者姓名
        		$('#blhId').textbox('setValue',dataMap.info.midicalrecordId);//病历号
        		$('#clinicCode').val(dataMap.info.clinicCode);//门诊号
				var sex = sexMap.get(dataMap.info.patientSex);
				$('#sexId').textbox('setValue',sex);//性别
				var age = dataMap.info.patientBirthday;
				var ages=DateOfBirth(age);
			    $('#ageId').textbox('setValue',ages.get("nianling")+ages.get("ageUnits"));
				$('#contractId').combobox('setValue',dataMap.info.pactCode);//合同单位
				$.ajax({//得到合同单位的结算方式
					url: '<%=basePath%>finance/medicinelist/queryCountByPaykindCode.action',
					data:{"count":dataMap.info.pactCode},
					success: function(dataOBj) {
						if(1==dataOBj.mcardFlag){
							$('#certificate').textbox('enable');
							$('#certificateRequired').val(1);//需要输入医疗证号 1是0否
							$('#certificate').textbox({required:true});
						}else{
							$('#certificate').textbox('disable');
							$('#certificateRequired').val(0);//需要输入医疗证号 1是0否
							$('#certificate').textbox({required:false});
						}
						$('#conts').val(dataOBj.paykindCode);//意义：收费时的合同单位；可以改变
						$('#newconts').val(dataOBj.paykindCode);//意义：挂号时的合同单位；不会随着好听你单位下拉框的改变而改变
						pubRatio = dataOBj.pubRatio;//公费比例
				    	payRatio = dataOBj.payRatio;//自负比例
				    	ownRatio = dataOBj.ownRatio;//自费比例
				    	ecoRatio = dataOBj.ecoRatio;//优惠比例
					}
				});
				$('#docId').textbox('setValue',dataMap.info.doctName);//开立医生
				$('#deptId').textbox('setValue',dataMap.info.deptName);//看诊科室
				$('#blhc').val(dataMap.info.midicalrecordId);//病历号
				var feeclinic=dataMap.info.clinicCode;
				$('#feedetailId').datagrid({
					pagination:false,
					url:'<%=basePath%>finance/medicinelist/findFeedetailStatistics.action?menuAlias=${menuAlias}', 
					queryParams:{"feedetail.clinicCode":feeclinic,"feedetail.regDpcdname":dataMap.info.deptName,"feedetail.doctCodename":dataMap.info.doctName},
					onBeforeLoad:function(param){
						if(!feeclinic){
							return false;
						}
					},
					onLoadSuccess: function (data) { 
						$('#feedetailId').datagrid('checkRow',0);
					}
				});
        	}else if(dataMap.resMsg=="success"){//多次挂号时
        		blh = dataMap.infoList[0].midicalrecordId;//获得病例号
        		$('#blhId').textbox('setValue',blh);//病历号
				$('#jzkh').textbox('setValue',dataMap.infoList[0].cardNo);//就诊卡号
				var clinicCodeMap = new Map(String,String);//获取门诊号map
				var clinicCodes = "";//获取门诊号集合
				for(var i = 0;i<dataMap.infoList.length;i++){
					if(i == dataMap.infoList.length - 1){
						clinicCodes += dataMap.infoList[i].clinicCode;
					}else{
						clinicCodes = clinicCodes + dataMap.infoList[i].clinicCode + "','";
					}
				}
				$('#clinicCode').val(dataMap.infoList[0].clinicCode);//门诊号
				$('#nameId').textbox('setValue',dataMap.infoList[0].patientName);//患者姓名
				var sex = sexMap.get(dataMap.infoList[0].patientSex);
				$('#sexId').textbox('setValue',sex);//性别
				var age = dataMap.infoList[0].patientBirthday;
				var ages=DateOfBirth(age);
			    $('#ageId').textbox('setValue',ages.get("nianling")+ages.get("ageUnits"));
				$('#contractId').combobox('setValue',dataMap.infoList[0].pactCode);//合同单位
				$('#blhc').val(dataMap.infoList[0].midicalrecordId);//病历号隐藏域
				$.ajax({//得到合同单位的结算方式
					url: '<%=basePath%>finance/medicinelist/queryCountByPaykindCode.action',
					data:{count:dataMap.infoList[0].pactCode},
					success: function(dataOBj) {
						if(1==dataOBj.mcardFlag){
							$('#certificate').textbox('enable');
							$('#certificateRequired').val(1);//需要输入医疗证号 1是0否
							$('#certificate').textbox({required:true});
						}else{
							$('#certificate').textbox('disable');
							$('#certificateRequired').val(0);//需要输入医疗证号 1是0否
							$('#certificate').textbox({required:false});
						}
						$('#conts').val(dataOBj.paykindCode);
					}
				});
				var feeclinic=clinicCodes;
				//刷新医嘱处方列表
				$('#feedetailId').datagrid({
					pagination:false,
					url:'<%=basePath%>finance/medicinelist/findFeedetailStatistics.action?menuAlias=${menuAlias}',
					queryParams:{"feedetail.clinicCode":feeclinic,"feedetail.regDpcdname":dataMap.infoList[0].deptName,"feedetail.doctCodename":dataMap.infoList[0].doctName},
					onBeforeLoad:function(param){
						if(!feeclinic){
							return false;
						}
					},
					onLoadSuccess: function (data) { 
						$('#feedetailId').datagrid('checkRow',0);
						var deptNames = "";//看诊科室
		        		var doctNames = "";//开立医生姓名
		        		var map = new Map();//右上角医生map，用户去除重复医生 ;以医生code为key，科室code为value
						/**
						 * @Date 2016年11月23日15:34:32
						 * @Modyfer GH
						 * @Mark 更新- 去重赋值-看诊科室和开立医生数据
						 * @UpdateDate 2016年11月24日17:58:04
						 */
		        		deptNames = data.rows[0].deptName;
						doctNames =data.rows[0].empName;
		        		for(var i = 1;i<data.rows.length;i++){
							if((deptNames.indexOf(data.rows[i].deptName)<0)&&(doctNames.indexOf(data.rows[i].empName)<0)){
								doctNames+=","+data.rows[i].empName;
								deptNames+=","+data.rows[i].deptName;
							}else if((doctNames.indexOf(data.rows[i].empName)<0)&&(deptNames.indexOf(data.rows[i].deptName)>-1)){
								doctNames+=","+data.rows[i].empName;
								deptNames+=","+data.rows[i].deptName;
							}else if ((doctNames.indexOf(data.rows[i].empName)>-1)&&(deptNames.indexOf(data.rows[i].deptName)<0)){
								doctNames+=","+data.rows[i].empName;
								deptNames+=","+data.rows[i].deptName;
							}
						}
						$('#docId').textbox('setValue',doctNames);//开立医生
						$('#deptId').textbox('setValue',deptNames);//看诊科室
					}
				});
				
				
				
// 				$('#infoList').datagrid({
<%--     				url: '<%=basePath%>finance/medicinelist/findInfoList.action?menuAlias=${menuAlias}', --%>
//     				queryParams:{"feedetail.patientNo":blh},
//     				//双击赋值，并且关闭，并且查询医嘱信息
//     				onDblClickRow :function(rowIndex, rowData){
//     					$('#blhId').textbox('setValue',blh);//病历号
//     					$('#jzkh').textbox('setValue',rowData.cardNo);//病历号
//     					$('#clinicCode').val(rowData.clinicCode);//门诊号
//     					$('#nameId').textbox('setValue',rowData.patientName);//患者姓名
//     					var sex = sexMap.get(rowData.patientSex);
//     					$('#sexId').textbox('setValue',sex);//性别
//     					var age = rowData.patientBirthday;
//     					var ages=DateOfBirth(age);
//     				    $('#ageId').textbox('setValue',ages.get("nianling")+ages.get("ageUnits"));
//     					$('#contractId').combobox('setValue',rowData.pactCode);//合同单位
//     					$('#docId').combobox('setText',rowData.doctName);//开立医生
//     					$('#deptId').combobox('setValue',rowData.deptCode);//看诊科室
//     					$('#blhc').val(rowData.midicalrecordId);//病历号隐藏域
//     					$('#queryInfoWondow').window('close');//关闭窗口
//     					$.ajax({//得到合同单位的结算方式
<%--     						url: '<%=basePath%>finance/medicinelist/queryCountByPaykindCode.action', --%>
//     						data:{count:rowData.pactCode},
//     						success: function(dataOBj) {
//     							$('#conts').val(dataOBj.paykindCode);
//     						}
//     					});
//     					//刷新医嘱处方列表
//     					$('#feedetailId').datagrid({
// 							pagination:false,
<%-- 							url:'<%=basePath%>finance/medicinelist/findFeedetailStatistics.action?menuAlias=${menuAlias}', --%>
// 							queryParams:{"feedetail.clinicCode":rowData.clinicCode,"feedetail.regDpcd":rowData.deptCode,"feedetail.doctCode":rowData.doctCode},
// 							onLoadSuccess: function (data) { 
// 								$('#feedetailId').datagrid('checkRow',0);
// 							}
//     					});
//     				}
//     			});
// 				$('#queryInfoWondow').window('open');
			}
		}
	});
}

//加载dialog
function Adddilog(title, url) {
	$('#dialog').dialog({    
	    title: title,    
	    width: '40%',    
	    height:'40%',    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true   
   });    
}
//打开dialog
function openDialog() {
	$('#dialog').dialog('open'); 
}
//关闭dialog
function closeDialog() {
	$('#dialog').dialog('close');  
}


//渲染科室
function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}	
//渲染人员
function functionEmp(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}	
//渲染合同单位
function functionConit(value,row,index){
	if(value!=null&&value!=''){
		return contMap[value];
	}
}
//渲染用户人员
function functionEmpUser(value,row,index){
	if(value!=null&&value!=''){
		return empUserMap[value];
	}
}
//渲染单位
function functionPack(value,row,index){
	if(value!=null&&value!=''){
		return packMap[value];
	}
}
/**
 * 单位渲染
 */
function functionFeePack(value,row,index){
	if(value!=null&&value!=''){
		if(row.drugFlag==1){
			if(row.extFlag3=='1'){
				return feepackMap[value];
			}else if(row.extFlag3=='0'){
				return feeminMap[value];
			}
		}
		return value;
	}
}
//渲染频次	
function functionFrequency(value,row,index){
	if(value!=null&&value!=''){
		return frequencyMap[value];
	}
}
//处方状态
function functionStatus(val,row,index){
	if(val=="0"){
		return '保存';
	}else if(val=="1"){
		return '收费';
	}else if(val=="2"){
		return '确认';
	}else if(val=="3"){
		return '作废';
	}else if(val=="4"){
		return '新开';
	}
}
//结算方式
function funMeng(val,row,index){
	if(val=="01"){
		return '<span style="color:red">自费</span>';
	}else if(val=="02"){
		return '<span style="color:red">保险</span>';
	}else if(val=="03"){
		return '<span style="color:red">公费在职</span>';
	}else if(val=="04"){
		return '<span style="color:red">公费退休</span>';
	}else if(val=="05"){
		return '<span style="color:red">公费干部</span>';
	}
}
//药品/非药品
function formatCheckBox(value,row,index){
	if(value!=null&&value!=""){
		return systemTypeMap[value];
	}
	return value;
}
//附材标志
function subjob(val,row,index){
	if(val==0||val==null||val==""){
		return '';
	}else{
		return 'F';
	}
}

//是否需要确认标记
function subokFlug(val,row,index){
	if(val==1){
		return '√';
	}else{
		return '';
	}
}

//是否确认标记
function subFlug(val,row,index){
	if(val==1){
		return '√';
	}else{
		return '';
	}
}

//自动计算器
function zdjsq(){
	var rows = $('#list').datagrid('getChecked');
	var jsonRowsList = JSON.stringify( $('#list').edatagrid('getChecked'));
	var pay = 0;
	var payCost = 0;//自付金额    自付金额来源于医保，现在默认为0
	var overCost = 0;//超标金额
	var excessCost = 0;//超标药金额
	var pubCost = 0;//公费金额（可报销金额）
	var ecoCost = 0;//优惠金额
	var payByPerson = 0;//自费金额
	var drugOwncost = 0;//自费药金额
	var drugCost = 0;//药品总金额
	var newconts = $('#newconts').val();
	var conts = $('#conts').val();
	if(rows.length>0){
		for(var i=0; i<rows.length; i++){
			pay = pay+rows[i].adPriceSum;
			if(conts==newconts){
				payCost = payCost + rows[i].payCost;
				pubCost = pubCost + rows[i].pubCost;
				ecoCost = ecoCost + rows[i].ecoCost;
			}else{
				if(flag==0||flag=="0"){//改合同单位适用于全部药品非药品
					payCost = payCost + rows[i].totalNum*rows[i].adPrice*payRatio;
					pubCost = pubCost + rows[i].totalNum*rows[i].adPrice*pubRatio;
					ecoCost = ecoCost + rows[i].totalNum*rows[i].adPrice*ecoRatio;
				}else if(flag==1||flag==1){//合同单位适用于药品
					if(row[i].drugFlag == 1){//药品
						payCost = payCost + rows[i].totalNum*rows[i].adPrice*payRatio;
						pubCost = pubCost + rows[i].totalNum*rows[i].adPrice*pubRatio;
						ecoCost = ecoCost + rows[i].totalNum*rows[i].adPrice*ecoRatio;
					}
				}else if(flag==2||flag=="2"){//合同单位适用于非药品
					if(row[i].drugFlag == 0){//非药品
						payCost = payCost + rows[i].totalNum*rows[i].adPrice*payRatio;
						pubCost = pubCost + rows[i].totalNum*rows[i].adPrice*pubRatio;
						ecoCost = ecoCost + rows[i].totalNum*rows[i].adPrice*ecoRatio;
					}
				}else{
					payCost = payCost + rows[i].payCost;
					pubCost = pubCost + rows[i].pubCost;
					ecoCost = ecoCost + rows[i].ecoCost;
				}
			}
			overCost = overCost + rows[i].overCost;
			excessCost = excessCost + rows[i].excessCost;
			drugOwncost = drugOwncost + rows[i].drugOwncost;
			if(rows[i].drugFlag==1||rows[i].drugFlag=="1"){
				drugCost += rows[i].adPriceSum;
			}
		}
	}
	pay = pay.toFixed(2);
	payByPerson = payByPerson.toFixed(2);
	payCost = payCost.toFixed(2);
	drugCost = drugCost.toFixed(2);
	excessCost = excessCost.toFixed(2);
	var realPay = pay - ecoCost;
	realPay = realPay.toFixed(2);
	$('#payCost').text(payCost);//自付金额
	$('#payCostWin').text(payCost);//自付金额  弹出框
	$('#drugCost').text(drugCost);//药品总额
	$('#realPay').text(realPay);//实收金额
	$('#excessCost').text(excessCost);//实收金额
	$('#zfje').text(pay);
	$('#ze').text(pay);
	$('#sumPayList').val(pay);
	$('#sumFees').text(pay);
	$.post('<%=basePath%>finance/medicinelist/findMinfeeStatCode.action',
        {"jsonRowsList":jsonRowsList},
        function(dataMap){
        	$('#feeList').datagrid({
            	data:dataMap
            });
//         	//动态获取发票  
        	if(dataMap.length<=0){
	        	getInvoiceNoByCode(1);
        	}else{
	        	getInvoiceNoByCode(dataMap.length);
        	}
        }
   	);
}
//非药品弹窗
// function wondowNotDrug(){
// 	var enCodeMinMin = "";
// 	var count = $('#contractId').textbox('getValue');
// 	if(count==null||count==""){
// 		$.messager.alert("操作提示","请录入患者");
// 		return;
// 	}
// 	var rows = $('#list').datagrid('getChecked');
// 	var queryParam=$('#addNotDrugId').val();
// 	var conts = $('#conts').val();
// 	$('#codes').textbox('setValue',queryParam);
// 	$('#wondowNotDrug').window('open');
// 	$('#notDrugInfoViewListId').datagrid({
// 		rownumbers: true,
// 		pagination: true,
// 		pageSize: 20,
// 		pageList: [20,30,50,100],
<%-- 		url:'<%=basePath%>finance/medicinelist/findNotDrugInfoViewList.action?menuAlias=${menuAlias}', --%>
// 		queryParams:{"undrugCodes":queryParam},
// 		onDblClickRow: function (rowIndex, rowData){
// 			$.ajax({
<%-- 				url: '<%=basePath%>finance/medicinelist/queryMinState.action', --%>
// 				data:{"encode":rowData.undrugMinCode},
// 				success: function(dataOBJ) {
// 					enCodeMinMin = dataOBJ.feeStatCode;
// 					var index = $('#list').datagrid('appendRow',{
// 						stust : 1,
// 						FeedetailId : rowData.undrugCode,
// 						drugFlag:0,
// 						adviceName: rowData.undrugName,
// 						feeCode:rowData.undrugMinCode,
// 						totalNum :1,
// 						setNum:1,
// 						dosage:1,
// 						unit:rowData.unit,
// 						frequency:1,
// 						usageName:rowData.undrugNotes,
// 						executiveDept :"",
// 						adPriceSum : rowData.defaultprice,
// 						unitPrice :conts,
// 						adPrice : rowData.defaultprice,
// 						remark :rowData.undrugRemark,
// 						injectNumber:0,
// 						sysType:rowData.undrugSystype,
// 						youli:1,
// 						subjobFlag:0,
// 						feeStatCode :enCodeMinMin,
// 						feeStatName:dataOBJ.feeStatName,
// 						confirmCode:null,
// 						confirmDept:null,
// 						confirmDate:null,
// 						confirmNum:1,
// 						issubmit:dataOBJ.issubmit,
// 						confirmFlag:0
// 					}).datagrid('getRows').length-1;
// 					$('#list').datagrid('checkRow',index);
// 					//查询其有没有辅材 有辅材一起带出来
// 					$.ajax({
<%-- 						url: '<%=basePath%>finance/medicinelist/findOdditionalitemByTypeCode.action', --%>
// 						data:{"undrugId":rowData.undrugCode},
// 						success: function(dataObj){
// 							if(dataObj.length>0){
// 								for(var n=0;n<dataObj.length;n++){
// 									var index = $('#list').datagrid('appendRow',{
// 										stust :1,
// 										FeedetailId : dataObj[n].FeedetailId,
// 										drugFlag:0,
// 										adviceName: dataObj[n].adviceName,
// 										feeCode:dataObj[n].feeCode,
// 										totalNum :dataObj[n].totalNum,
// 										unit:dataObj[n].unit,
// 										usageName:dataObj[n].usageName,
// 										executiveDept :dataObj[n].executiveDept,
// 										adPriceSum : dataObj[n].adPriceSum,
// 										unitPrice :conts,
// 										adPrice : dataObj[n].adPrice,
// 										remark :dataObj[n].remark,
// 										injectNumber:0,
// 										sysType:dataObj[n].sysType,
// 										subjobFlag:1,
// 										follow:rowData.id+"F",
// 										wtotalNum :dataObj[n].totalNum,
// 										youli:1,
// 										feeStatCode :dataObj[n].feeStatCode,
// 										feeStatName:dataObj[n].feeStatName,
// 										confirmCode:null,
// 										confirmDept:null,
// 										confirmDate:null,
// 										confirmNum:1,
// 										issubmit:dataObj[n].issubmit,
// 										confirmFlag:0
// 									}).datagrid('getRows').length-1;
// 									$('#list').datagrid('checkRow',index);
// 								}
// 							}
// 						}
// 					});
// 				}
// 			});
// 			$('#wondowNotDrug').window('close');
// 		}
// 	});
// }


//条件查询
function searchFrom(){
	var codesd =  $('#codes').textbox('getValue');
    $('#notDrugInfoViewListId').datagrid({
    	url:'<%=basePath%>finance/medicinelist/findNotDrugInfoViewList.action?menuAlias=${menuAlias}',
    	queryParams:{"undrugCodes":codesd}
    });
}
//清空方法
function clearFeed(){
	var listRows = $('#list').datagrid('getRows');
	if(listRows!=null&&listRows.length>0){
		var listR = listRows.length;
		for(var j=0;j<listR;j++){
			$('#list').datagrid('deleteRow',0);
		}
	}
	var listRowss = $('#feedetailId').datagrid('getRows');
	if(listRowss!=null&&listRowss.length>0){
		var listRR = listRowss.length;
		for(var j=0;j<listRR;j++){
			$('#feedetailId').datagrid('deleteRow',0);
		}
	}
	var feeList = $('#feeList').datagrid('getRows');
	if(feeList!=null&&feeList.length>0){
		var listRRR = feeList.length;
		for(var j=0;j<listRRR;j++){
			$('#feeList').datagrid('deleteRow',0);
		}
	}
	$('#editForm').form('reset');
	var defaultnum = 0.0;
	defaultnum = defaultnum.toFixed(2);
	$('#zfje').text(defaultnum);
	$('#ze').text(defaultnum);
	$('#payCost').text(defaultnum);
	$('#realPay').text(defaultnum);
	$('#drugCost').text(defaultnum);
	$('#ze').text(defaultnum);
	$('#selfPaidAmount').text(defaultnum);
	$('#zje').text(defaultnum);
	$('#yjjes').text(defaultnum);
	$('#excessCost').text(defaultnum);
	$('#payCostWin').text(defaultnum);
	$('#actualCollection').val(defaultnum);
	$('#shouldPay').val(defaultnum);
	$('#shouldPay').val(giveChange);
	$('#newconts').val("");
	$('#addNotDrugId').combogrid('setValue','');
	$('#accountPassword').textbox('clear');
	$('#banki').textbox('clear');
	$('#bankAccounti').textbox('clear');
	$('#bankNo').textbox('clear');
	$('#bankUniti').textbox('clear');
	$('#clinicCode').val('');
	
}

//非药品删除
function removeFeed(){
	var rowsdel = $('#list').datagrid('getChecked');
	if(rowsdel.length>0){
		for(var i=0;i<rowsdel.length;i++){
			if(rowsdel[i].stust!=1){
				$.messager.alert("操作提示", "无权限删除医生所开医嘱");
				return false;
			}
		}
		for(var i=rowsdel.length-1;i>=0;i--){
			$('#list').datagrid('deleteRow',$('#list').datagrid('getRowIndex',rowsdel[i]));
		}
		var rows = $('#list').datagrid('getChecked');
		var codes = "";
		var ids = "";
		var notDrugIds = "";//非药品
		if(rows.length>0){
			for(var i=0; i<rows.length; i++){
				pay = pay+rows[i].adPriceSum;
				if(codes!=""){
					codes = codes + "','";
				}
				codes = codes+ rows[i].feeCode;
				if(rows[i].stust==1){
					if(notDrugIds!=''){
						notDrugIds += "','";
					}
					notDrugIds += rows[i].FeedetailId;
				}else{
					if(ids!=""){
						ids+= "','";
					}
					ids += rows[i].FeedetailId;
				}
			}
		}
		zdjsq();
	}else{
		$.messager.alert("操作提示", "请选择要删除的信息");
	}
}
//划价保存
function savePrice(){
	var flag = false;
	var rowsList = $('#list').edatagrid("getChecked");
	var clinicCode = $('#clinicCode').val();
	if(clinicCode == ""||typeof clinicCode=='undefined'){
		$.messager.alert("操作提示", "请先录入患者信息");
		return;
	}
	if(rowsList.length>0){
		for(var i=0;i<rowsList.length;i++){
			if(rowsList[i].stust!=1){
				$.messager.alert("操作提示", "请不要将医嘱信息进行划价，请重新选择");
				return;
			}
			
		}
	}else{
		$.messager.alert("操作提示", "划价失败，请录入划价信息");
		return;
	}
	
	var clinicCode = $('#clinicCode').val();
	$('#list').edatagrid('acceptChanges');
	var newRows = $('#list').edatagrid("getChecked")
	var jsonRowsList = JSON.stringify(newRows);
	for(var i=0;i<newRows.length;i++){
		if(newRows[i].executiveDept==""){
			flag = true;
		}
	}
	if(flag){
		$.messager.alert("操作提示", "请选择执行科室！");
		return;
	}
	$.messager.progress({text:'划价中，请稍候...',modal:true});
	$.post('<%=basePath%>finance/medicinelist/savePrice.action',
        {"jsonRowsList":jsonRowsList,"feedetail.clinicCode":clinicCode},
        function(data){
        	$.messager.progress('close');
        	$.messager.alert("操作提示", "划价成功");
        	dialogQueryBlhOrCardNo();
   	});
}

/**
 * 支付
 */
function saveWondowFeed(){
	var name = $('#nameId').textbox('getValue');
	if(name==""||name==""){
		$.messager.alert("操作提示", "请录入患者!");
		return;
	}
	var adPrice = $('#ze').text();
	if(adPrice==""||adPrice==0){
		$.messager.alert("操作提示", "当前没有医嘱,请选择医嘱!");
		return;
	}
	var invoiceNoflay = $('#invoiceNoflay').val();
	if(invoiceNoflay=="error"){
		$.messager.alert("操作提示", "请重新领取发票,领取发票后请刷新页面");
		return;
	}
	var certificateRequired = $('#certificateRequired').val();
	if(certificateRequired==1){
		var text = $('#certificate').textbox('getText');
		if(text==null||text==""){
			$.messager.alert("操作提示", "请输入医疗证号！");
			return;
		}
	}
	//获取所有选中的收费项目
	var list = $('#list').datagrid('getChecked');
	var flag = false;
	if(list.length>0){
		for(var i=0;i<list.length;i++){
// 			if(list[i].stust==1){
// 				$.messager.alert("操作提示", "收费清单中有未划价项目，请划价后收费");
// 				return;
// 			}
			if(list[i].stust==1){
				flag = true;
				break;
			}
		
		}
	}
	if(flag){
		$.messager.confirm('确认','收费清单中有未划价项目，是否划价？',function(r){    
		    if (r){  //选择shi  
		    	savePrice();//调用划价方法
		    	return;
		    }else{
		    	$.messager.alert("操作提示", "您选择不划价保存");
		    	return;
		    }    
		});  
	}else{
		$('#shouldPay').numberbox('setValue',adPrice);
		$('#actualCollection').numberbox('setValue','');
		$('#zje').text(adPrice);
		$('#yjje').text(adPrice);
		$('#cash').textbox('setValue',adPrice);
		$('#yjjes').text(adPrice);
		$('#sumFees').text(adPrice);
		$('#saveWondowFeed').window('open');
	}
}

/**
 * 收费过程
 */
function saveHisRecipedetail(){
	var actualCollection = $('#actualCollection').val();
	var shouldPay = $('#shouldPay').numberbox('getText');
	if(actualCollection==null||actualCollection==""){
		$.messager.alert('提示','实付金额不能为空！');
		return;
	}else{
		if(Number(actualCollection)<Number(shouldPay)){
			$.messager.alert('提示','实付金额不能小于应缴金额！');
			return;
		}
	}
	//预扣库存判断 0库存不足状态
	var drugHouseJS = 0;
	//判断医技项目是否是确认状态
	var unDrugConfirm = 1;
	
	
	var rowFeeList = $('#feeList').datagrid('getRows');//获得费用统计类别数据																			 
	var rowFee = "" ;  							       //每项费用																					 
	var rowTypeName = "" ;							   //项目类别名称																				  
	var rowTypeCode = "" ;							   //项目类别代码
	var invoiceNo = $('#invoice').val();			   //发票号
	if(rowFeeList.length>0){																														  
		for(var i=0;i<rowFeeList.length;i++){																										  
			if(rowFee!=""){																															  
				rowFee = rowFee + ',';																											  
			}																																		  
			rowFee = rowFee + rowFeeList[i].fees;																									
			if(rowTypeName!=""){																													  
				rowTypeName = rowTypeName + ',';																									  
			}																																		  
			rowTypeName = rowTypeName + rowFeeList[i].feeTypeName;																					  
			if(rowTypeCode!=""){																													  
				rowTypeCode = rowTypeCode + ',';																									 
			}																																		  
			rowTypeCode = rowTypeCode + rowFeeList[i].feeTypeCode;																					  
		}																																			  
	}																																				  

	var totCost = $('#shouldPay').numberbox('getValue');//总金额
	var pactCode = $('#contractId').combobox('getValue'); //合同单位代码
	var pactName = $('#contractId').combobox('getText'); //合同单位名称
	var hzName = $('#nameId').textbox('getValue'); //患者姓名
	var clinicCode = $('#clinicCode').val();//门诊号

	var cash = $('#cash').numberbox('getValue');							//现金--金额
	var bankCard = $('#bankCard').numberbox('getValue');					//银行卡--金额
	var check = $('#check').numberbox('getValue');							//支票--金额 
	var hospitalAccount = $('#hospitalAccount').numberbox('getValue');		//院内账户--金额
	var passwords = $('#accountPassword').textbox('getValue');				//院内账户 -- 密码
	if(hospitalAccount!=null&&hospitalAccount!=''&&hospitalAccount>0){
		if(passwords==null||passwords==""){
			$.messager.alert("操作提示","请输入院内账户密码...");
			return ;
		}
	}
	var blhNO = $('#blhId').textbox('getValue');							//病历号
	var blhcs = $('#blhc').val();											//隐藏域 --病历号
	
	if(check!=null&&check!=""&&check!=0){
		var bankUniti = $('#bankUniti').textbox('getValue');				//开户银行
		var banki = $('#banki').textbox('getValue');						//开户账户
		var bankAccounti = $('#bankAccounti').textbox('getValue');			//开具单位
		var bankNo = $('#bankNo').textbox('getValue');						//支票号/交易号
		if(bankUniti==null||bankUniti==""||banki==null||banki==""||bankAccounti==null||bankAccounti==""||bankNo==null||bankNo==""){
			$.messager.alert("操作提示","请完善支票信息");
			return;
		}
	}
	
	$('#list').edatagrid('acceptChanges');
	var doctorId = "";									//所有医嘱的ID
	var feedetailIds = "";		                        //门诊收费详细药品ID																			 
	var	feedetaiNotIds = "";							//门诊收费详细非药品ID
	var recipedetailIds = "";							//处方号
	var recipNo="";
	var conts = $('#conts').val();//合同单位
	var rowList = $('#list').datagrid('getChecked');    //获得费用统计类别数据
	var jsonRowsList = JSON.stringify( $('#list').edatagrid("getChecked"));
	if(rowList.length>0){																				  											  
		for(var i=0;i<rowList.length;i++){																										      
			if(rowList[i].drugFlag==1){																												  
				if(feedetailIds!=""){																										          
					feedetailIds = feedetailIds + "','";																							  
				}																											                         
				feedetailIds = feedetailIds + rowList[i].FeedetailId;																				  
			}else{
				if(feedetaiNotIds!=''){
					feedetaiNotIds += "','";
				}
				feedetaiNotIds += rowList[i].FeedetailId;
			}
			if(doctorId!=""){
				doctorId = doctorId + "','";
			}
			doctorId = doctorId + rowList[i].FeedetailId;
		}																													  						  
	}
	var rowsLists = $('#list').datagrid('getChecked');
	if(rowsLists.length>0){
		for(var i=0;i<rowsLists.length;i++){
			if(recipedetailIds!=""){
				recipedetailIds = recipedetailIds + "','"; 
			}
			recipedetailIds = recipedetailIds + rowsLists[i].recipeNo;
		}
	}
	
	var rowsListNo = $('#feedetailId').datagrid('getChecked');
	if(rowsListNo.length>0){
		for(var i=0;i<rowsListNo.length;i++){
			if(recipNo!=""){
				recipNo = recipNo + "','"; 
			}
			recipNo = recipNo + rowsListNo[i].recipeNo;
		}
	}
	
	//预判库存
	$.ajax({
		url: '<%=basePath%>finance/medicinelist/findDrugStorage.action',  
		type:'post',
		data:{"feedetailIds":feedetailIds+"','"+feedetaiNotIds},//查询参数，药品和非药品ID，药品进行库存判断，非药品判断是否是物资，是物资的话判断库存；
		success:function(datasMap){
			if(datasMap.resMsg=="success"){
				//判断是否有账户支付
				if(hospitalAccount!=null&&hospitalAccount!=""&&hospitalAccount!=0){
					if(passwords==null||passwords==""){
						$.messager.alert("操作提示", "请输入患者账户密码");	
						return;
					}else{
						$.ajax({
							url: '<%=basePath%>finance/medicinelist/veriPassWord.action',
							data:{"passwords":passwords,"feedetail.patientNo":blhcs},
							success: function(data) {
								if(data=="NO"){
									$.messager.alert("操作提示", "账号或密码错误!");	
								}else{
									saveFeeAndUpdate(rowFee,rowTypeName,rowTypeCode,invoiceNo,cash,bankCard,hospitalAccount,check,bankUniti,banki,bankAccounti,bankNo,blhNO,totCost,feedetailIds,feedetaiNotIds,recipedetailIds,clinicCode,jsonRowsList,doctorId,recipNo,conts);
								}
							}
						});
					}
				}else{
					saveFeeAndUpdate(rowFee,rowTypeName,rowTypeCode,invoiceNo,cash,bankCard,hospitalAccount,check,bankUniti,banki,bankAccounti,bankNo,blhNO,totCost,feedetailIds,feedetaiNotIds,recipedetailIds,clinicCode,jsonRowsList,doctorId,recipNo,conts);
				}
			}else if(datasMap.resMsg=="error"){ 
// 				$.messager.alert("操作提示", datasMap.resCode);
				$.messager.confirm("提示",datasMap.resCode+"是否继续收费？",function(r){
					if(r){
						feeWhenUnenougth = 1;
						saveFeeAndUpdate(rowFee,rowTypeName,rowTypeCode,invoiceNo,cash,bankCard,hospitalAccount,check,bankUniti,banki,bankAccounti,bankNo,blhNO,totCost,feedetailIds,feedetaiNotIds,recipedetailIds,clinicCode,jsonRowsList,doctorId,recipNo,conts);
					}
				});
			}
		}
	});
}

/**
 * 校验库存
 */
function drugWarehouse(feedetailIds,drugHouseJS){
	
}

/**
 * 判断医技是否确认
 */
function unDrugWarehouse(feedetaiNotIds,clinicCode){
	$.ajax({
		url: '<%=basePath%>finance/medicinelist/unDrugWarehouse.action?',  
		data:{"feedetaiNotIds":feedetaiNotIds},
		type:'post',
		success:function(datasMap){
			if(datasMap.resMsg=="success"){
				unDrugConfirm = 0;
			}else if(datasMap.resMsg=="error"){
				unDrugConfirm = 1;
				$.messager.alert("操作提示", datasMap.resCode);
			}
		}
	})
}

/**
 * 收费确认doctorId
 */
function saveFeeAndUpdate(rowFee,rowTypeName,rowTypeCode,invoiceNo,cash,bankCard,hospitalAccount,check,bankUniti,banki,bankAccounti,bankNo,blhNO,totCost,feedetailIds,feedetaiNotIds,recipedetailIds,clinicCode,jsonRowsList,doctorId,recipNo,conts){
	$.messager.progress({text:'保存中，请稍后...',modal:true});
	var newconts = $('#newconts').val();//新的合同单位
	$.post("<%=basePath%>finance/medicinelist/chargeImplement.action",
	        {"feeWhenUnenougth":feeWhenUnenougth,"newconts":newconts,"invoiceMap":JSON.stringify(invoiceMap),"rowFee":rowFee,"rowTypeName":rowTypeName,"rowTypeCode":rowTypeCode,"invoiceNo":invoiceNo,"cash":cash,"bankCard":bankCard,"hospitalAccount":hospitalAccount,"check":check,"bankUniti":bankUniti,"banki":banki,"bankAccounti":bankAccounti,"bankNo":bankNo,"blhNO":blhNO,"totCost":totCost,"feedetailIds":feedetailIds,"feedetaiNotIds":feedetaiNotIds,"recipedetailIds":recipedetailIds,"clinicCode":clinicCode,"jsonRowsList":jsonRowsList,"doctorId":doctorId,"rowsListNo":recipNo,"pink":conts},
	        function(datassMap){
				feeWhenUnenougth = 0;
	        	$.messager.progress('close');
				if(datassMap.resMsg=="success"){
					$('#invoiceNos').val(datassMap.invoiceNos);
					$('#iReportTable').window('open');	
				}else if(datassMap.resMsg=="error"){
					$.messager.alert("错误提示", datassMap.resCode,'error');
				}else{
					$.messager.alert("错误提示", '未知错误，请联系管理员！','error');
				}
	   	});
}

function iReportButtonClose(){
	$('#iReportTable').window('close');
// 	$('#saveWondowFeed').window('close');
	closeSaveWondowFeed();
	clearFeed();
	//获取新的发票号 invoiceNum是获取数量
// 	getInvoiceNoByCode(1);
	$.ajax({
		url:"<%=basePath%>/finance/medicinelist/getInvoiceNoByCode.action",
		type:"post",
		data:{"invoiceNum":1},
		success:function(dataMap){
			var invoiceNo = dataMap["resCode"];
			$('#invoiceNo').val(invoiceNo);
			$('#invoice').val(invoiceNo);
			$('#invoiceNos').val(invoiceNo);
			$('#tdinvoiceNo').text(invoiceNo);
			$('#invoiceNoflay').val(dataMap["resMsg"]);
		}
	});
<%-- 	window.location.href="<%=basePath%>finance/medicinelist/listMedicinelist.action?menuAlias=${menuAlias}"; --%>
}
/**
 * 动态获取发票号，value是要获取的发票号数量
 */
function getInvoiceNoByCode(value){
	var getFeeSelectRows = $('#list').datagrid('getChecked');
	var feeID = "";
	for(var i=0;i<getFeeSelectRows.length;i++){
		if(feeID!=null&&feeID!=""){
			feeID +="','"
		}
		feeID += getFeeSelectRows[i].FeedetailId;
	}
	$.ajax({
		url:"<%=basePath%>/finance/medicinelist/getInvoiceNoByCode.action",
		type:"post",
		data:{"invoiceNum":value},
		success:function(dataMap){
			var invoiceNo = dataMap["resCode"];
			var resMsg = dataMap["resMsg"];
			if(resMsg == "error"){
				$('#invoiceNo').val(invoiceNo);
			}
// 			$('#invoiceNo').val(invoiceNo);
			$('#invoice').val(invoiceNo);
			$('#invoiceNos').val(invoiceNo);
			$('#tdinvoiceNo').text(invoiceNo);
			$('#invoiceNoflay').val(resMsg);
			
			if(resMsg=="success"){
				$.ajax({
					url:"<%=basePath%>/finance/medicinelist/getMoney.action",
					type:"post",
					data:{"feeID":feeID},
					success:function(dataMap){
						invoiceMap = dataMap;
						var invoiceMoney = new Array();
						var a = 0;
						var tabRow = document.getElementById("invoiceTab");
						var tabRowL = tabRow.rows.length;
						if(tabRowL>2){
							for(var i=0;i<tabRowL-2;i++){//移除之前德的发票号信息
								$('#tr'+i).remove();
							}
						}
						$.each(dataMap,function(key,value){
							invoiceMoney = value.split('_');
							var invNO =  invoiceMoney[0]
							var money = invoiceMoney[1];
// 							money = money.toFixed(2);
							var newTRhtml="<tr id = 'tr"+a+"'><td class='honry-lable' style='width:50px;border-left:0;text-align:left;'>票号：</td>"+
											"<td style='width:150px;' >"+invoiceMoney[0]+"</td>"+
											"<td class='honry-lable' style='width:50px;text-align:left;'>金额：</td>"+
											"<td style='width:150px;'>"+invoiceMoney[1]+"</td>"+
											"<td class='honry-lable' style='width:50px;text-align:left;'>发票类型：</td>"+
											"<td class='honry-lable' style='width:150px;text-align:left;border-right:0;'>收费</td>"+
											"</tr>"; 
							$('#invoiceTab tr:eq(-3)').after(newTRhtml);
							a+=1;
						});
					}
				});
			}
				
		}
	});
}
/**
 * 报表打印
 */
function iReportButton(){
	var INVOICE_NO=$("#invoiceNos").val();//发票号
	var invoiceArr = new Array();
	invoiceArr=INVOICE_NO.split(",");
	var timerStr = Math.random();
	if(!$("#iReport1").is(":checked")&&!$("#iReport2").is(":checked")){
		$.messager.alert('提示',"请选择打印选项！");
		return false;
	}
	if($("#iReport1").is(":checked")){
		for(var i=0;i<invoiceArr.length;i++){
			window.open ("<%=basePath%>iReport/iReportPrint/iReportInvoiceMedicinelist.action?randomId="+timerStr+"&INID="+invoiceArr[i]+"&fileName=Invoice",'newwindow'+i,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');		
		}
	}
	if($("#iReport2").is(":checked")){
		var CARD_NO = $("#jzkh").textbox('getValue');
		var invoiceNS= "";
		for(var i=0;i<invoiceArr.length;i++){
			if(invoiceNS!=""){
				invoiceNS = invoiceNS + "','";
			}
			invoiceNS = invoiceNS + invoiceArr[i];
		}
		window.open ("<%=basePath%>iReport/iReportPrint/iReportMedicinelist.action?randomId="+timerStr+"&CARD_NO="+CARD_NO+"&INVOICE_NO="+invoiceNS+"&fileName=${menuAlias}",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');
	}
	if($("#iReport3").is(":checked")){
		var clinicCode = $('#clinicCode').val();//门诊号
		var midicalrecordId = $("#blhc").val();//病历号
		if(clinicCode!=null&&clinicCode!=''&&midicalrecordId!=null&&midicalrecordId!=''){
			$.post("<%=basePath%>finance/recipedetail/getExecDpcdByclinicCode.action",{clinicCode:clinicCode,patientNo:midicalrecordId},function(data){
				if(data.length>0){
					for(var i = 0;i<data.length;i++){
						 var timerStr = Math.random();
						  window.open ("<%=basePath%>iReport/iReportPrint/iReportToInspectionCheck.action?randomId="+timerStr+"&clinicCode="+clinicCode+"&execDpcd="+data[i]+"&fileName=JCSQD",'newwindow'+data[i],'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
					}
				}else{
					$.messager.alert('提示',"无检查单信息,无法打印检查单!");
				}
			},"json");
		}else{
			$.messager.alert('提示',"无检查单信息,无法打印检查单!");
		}
	}
	if($("#iReport4").is(":checked")){
		var clinicCode = $('#clinicCode').val();//门诊号
		var midicalrecordId = $("#blhc").val();//病历号
		if(clinicCode!=null&&clinicCode!=''&&midicalrecordId!=null&&midicalrecordId!=''){
			jQuery.post("<%=request.getContextPath()%>/inpatient/getDeptId.action",{"clinicCode":clinicCode},function(result){
				var deptIdList = eval("("+result+")");
				if(deptIdList!=null&&deptIdList.length>0){
					for(var i=0;i<deptIdList.length;i++){
						var deptId = deptIdList[i];
						var timerStr = Math.random();
						window.open ("<%=basePath%>iReport/iReportPrint/iReportToMenZhenJY.action?randomId="+timerStr+"&clinicCode="+clinicCode+"&MedicalrecordId="+medicalrecordId+"&DEPT_ID="+deptId+"&fileName=menzhenjianyanshenqing",'newwindow'+deptId,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				}else{
					$.messager.alert('提示',"无检验单信息,无法打印检验单！");
				}
			});
		}else{
			$.messager.alert('提示',"无检验单信息,无法打印检验单!");
		}
	}
}
 
//加载dialog
function Adddilog(title, url) {
	$('#dialog').dialog({    
	    title: title,    
	    width: '40%',    
	    height:'40%',    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true   
   });    
}
//打开dialog
function openDialog() {
	$('#dialog').dialog('open'); 
}
//关闭dialog
function closeDialog() {
	$('#dialog').dialog('close');  
}


/**
 * 回车弹出看诊科室弹框
 * @author  zhuxiaolu
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @param textId 页面上commbox的的id
 * @date 2016-03-22 14:30   
 * @version 1.0
 */
 function popWinToDept(){
	 $('#docId').textbox('setValue','');
		var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=deptId";
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
 }

 /**
  * 回车弹出开立医师弹框
  * @author  zhuxiaolu
  * @param textId 页面上commbox的的id
  * @date 2016-03-22 14:30   
  * @version 1.0
  */
  function popWinToEmployee(){
	   var deptid=$('#deptId').textbox('getValue');
	   if(deptid){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=docId&employeeType=1&deptIds="+deptid;
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}else{
			$.messager.alert('提示','请选择选择科室');
		}
  }
  /**
   * 回车弹出开立医师弹框
   * @author  zhuxiaolu
   * @param textId 页面上commbox的的id
   * @date 2016-03-22 14:30   
   * @version 1.0
   */
   function popWinToContractId(){
	   var tempWinPath = "<%=basePath%>/popWin/popWinUnit/pWCUnitList.action?textId=contractId";
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='
		+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
   }
	/**  
	  *  
	  * 组合号渲染
	  *
	  *
	  */
	function functionGroup(value,row,index){
		var rwos = $('#list').datagrid('getRows');
		if(value==null||value==''){
			return null;
		}else{
			if(value==null||value==''){
				return null;
			}else{
				if(index==0&&rwos.length>1&&value==rwos[index+1].group){
					return "┓";
				}else if(index==0){
					return "";
				}else if(index==rwos.length-1&&value==rwos[index-1].group){
					return "┛";
				}else if(index==rwos.length-1){
					return "";
				}else if(value!=rwos[index-1].group&&value==rwos[index+1].group){
					return "┓";
				}else if(value==rwos[index-1].group&&value!=rwos[index+1].group){
					return "┛";
				}else if(value==rwos[index-1].group&&value==rwos[index+1].group){
					return "┫";
				}else{
					return "";
				}
			}
		}
	}
	function chargePriceColor(value,row){
		return '<span style="color:red;font-weight:bold">'+value+'</span>';
	}
	$.extend($.fn.textbox.defaults.rules, {  
	    number : {  
	        validator : function(value, param) {  
	            return /^[0-9]*$/.test(value);  
	        },  
	        message : "请输入数字"  
	    },  
	    length: {  
	        validator: function(value, param){  
	            return param == value.length;  
	        },  
	        message: '请输入18位有效数字'  
	    }
	 });    
	function closeSaveWondowFeed(){
		$('#bankCard').numberbox('setText','');//银行卡
		$('#check').numberbox('setText','');//支票
		$('#hospitalAccount').numberbox('setText','');
		$('#saveWondowFeed').window('close');
	}
	//打开更改发票号的窗口
	function changeInvoiceNoWIN(){
		$.ajax({
			url:"<%=basePath%>/finance/medicinelist/getInvoiceNoByCode.action",
			type:"post",
			data:{"invoiceNum":1},
			success:function(dataMap){
				if(dataMap.resMsg=="success"){
					var invoice = dataMap.resCode//获取当前使用的第一个发票号
					var invoiceID = dataMap[invoice];
					$('#changeInvoiceNo').textbox('setValue',invoice);
					$('#changeInvoiceID').val(invoiceID);
					$('#changeInvoiceNoWIN').window('open');
				}else{
					$.messager.alert('条件不满足,不能调整!');
				}
			}
		});
	}
	//保存更改后的发票号
	function saveChangeInvoice(){
		var id = $('#changeInvoiceID').val();
		var invoiceNo = $('#changeInvoiceNo').textbox('getValue');
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$.ajax({
			url:"<%=basePath%>/finance/medicinelist/changeInvoiceNO.action",
			type:"post",
			data:{'id':id,'invoiceNo':invoiceNo},
			success:function(data){
				$('#changeInvoiceNoWIN').window('close');
				$.messager.progress('close');
				if("success"==data.resMsg){
					$.messager.alert("提示",data.resCode,'info',function(){
						window.location.href="<%=basePath%>finance/medicinelist/listMedicinelist.action?menuAlias=${menuAlias}";
					});
					return;
				}else{
					$.messager.alert("提示","系统繁忙，请稍候重试！");
					return;
				}
				//刷新页面
			},
			error:function(){
				$('#changeInvoiceNoWIN').window('close');
				$.messager.progress('close');
				$.messager.alert("提示","网络繁忙，请稍候重试！");
				return;
			}
		});
	}
</script>
<style type="text/css">
table.honry-table td .datagrid-header-row td{
	border-top:0px;
	border-left:0px;
}
.datagrid-btable{
	border-collapse: collapse;
}
.datagrid-btable tr td:first-child{
	border-left:0;
}
</style>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="width:100%;height:65%;border-top:0px;">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north'" style="width: 100%;height: 150px;border-top:0px;border-left:0px;">
	    		<div class="easyui-layout" data-options="fit:true">
		    		<div data-options="region:'west'" style="width:52%;border-top:0px;border-left:0px;border-bottom:0px;">
		    			<div style="padding:5px 5px 5px 5px;">
							<a id="btnSave" href="javascript:saveWondowFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen',disabled:false">支付</a>&nbsp;&nbsp;
							<a id="btnSerach" href="javascript:dialogQueryBlhOrCardNo();" class="easyui-linkbutton" data-options="iconCls:'icon-search',disabled:false">查询</a>&nbsp;&nbsp;
							<a id="btnClear" href="javascript:clearFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-clear',disabled:false">清屏</a>&nbsp;&nbsp;
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;&nbsp;
							<a id="priceSave" href="javascript:savePrice();" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen',disabled:false">划价保存</a>&nbsp;&nbsp;
						</div> 	
						<div style="padding:5px 5px 5px 5px;">
							<form id="editForm" method="post">
								<input id="sumPayList" type="hidden">
								<input id="blhc" type="hidden">
								<input id="clinicCode" type="hidden">
								<input id="conts" type="hidden">
								<input id="newconts" type="hidden">
								<input id="invoiceNoflay" type="hidden" value="${feedetail.invoiceNoflay }">
						    	<table border="0" style="float:left;">
									<tr>
										<td>就诊卡号：</td>
										<td><input id="jzkh" class="easyui-textbox"  style="width:120px;">&nbsp;</td>
										<td>病历号：</td>
										<td><input id="blhId" class="easyui-textbox" style="width:120px;">&nbsp;</td>
										<td>患者姓名：</td>
										<td><a /><input id="nameId" name="name" class="easyui-textbox"  readonly="readonly" style="width:120px;">&nbsp;</td>
									</tr>	
									<tr>
										<td>性别：</td>
										<td><input id="sexId" class="easyui-textbox"  readonly="readonly" style="width:120px;">&nbsp;</td>
										<td>年龄：</td>
										<td><input id="ageId" class="easyui-textbox"  readonly="readonly" style="width:120px;">&nbsp;</td>
										<td>看诊科室：</td>
										<td><a  class="easyui-tooltip" data-options="hideEvent:'mouseleave',trackMouse:true,content: function(){ var deptId = $('#deptId').textbox('getValue'); return deptId;}">
											<input id="deptId" class="easyui-textbox" readonly="readonly"  style="width:120px;" ></a>
<!-- 										<a href="javascript:delSelectedData('deptId');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
										</td>
									</tr>	
									<tr>
										<td>医疗证号：</td>
										<input id="certificateRequired" type="hidden" value="0"/>
										<td><input id="certificate" name="certificate" class="easyui-textbox" disabled="true" style="width:120px;" 
													data-options = "validType:['number','length[18]'],invalidMessage:'请输入18位有效数字',missingMessage:'请输入18位有效数字' ">&nbsp;</td>
										
										<td>合同单位：</td>
										<td><input id="contractId" name="pactCode" class="easyui-combobox" style="width:120px;">
											<a href="javascript:delSelectedData('contractId');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
										</td>
										<td>开立医生：</td>
										<td><a class="easyui-tooltip" data-options="hideEvent:'mouseleave',trackMouse:true,content: function(){ var docId = $('#docId').textbox('getValue'); return docId;}"> 
											<input id="docId"  readonly="readonly" class="easyui-textbox" style="width:120px;" ></a> &nbsp;&nbsp;</td>
									</tr>
								</table>
							</form>
						</div>	
		    		</div>   
		    		<div data-options="region:'center'" style="width: 48%;border-top:0px;border-left:0px;border-bottom:0px;">
		    			<table id="feedetailId" class="easyui-datagrid" data-options="fit:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				    		 <thead>   
						        <tr>   
						            <th data-options="field:'ck',checkbox:true" ></th>
						            <th data-options="field:'deptName'" style="width:16%;">科室</th>   
						            <th data-options="field:'empName'" style="width:13%;">专家</th>   
						            <th data-options="field:'sumMoney',align:'right'" style="width:13%;">总价格</th>
						            <th data-options="field:'recipedStatus',formatter:functionStatus" style="width:10%;">状态</th>
						            <th data-options="field:'val',hidden:true" style="width:13%;">游离</th>   
						            <th data-options="field:'regDate'" style="width:22%;">挂号日期</th>   
						            <th data-options="field:'operDate'" style="width:22%;">划价时间</th>   
						        </tr>   
						    </thead>  
				    	</table>
		    		</div>
	    		</div> 
    		</div>  
    		<div data-options="region:'center',border:false" style="width:100%;">
		    	<table id="list" data-options="idField:'FeedetailId',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,toolbar:'#toolbarId',fit:true">   
				    <thead>   
				        <tr>
				        	<th data-options="field:'ck',checkbox:true" ></th> 
				        	<th data-options="field:'stust',hidden:true">状态</th>
				        	<th data-options="field:'extFlag3',hidden:true" width="2%"></th>
				        	<th data-options="field:'subjobFlag',formatter:subjob" width="2%"></th>
				        	<th data-options="field:'issubmit',hidden:true,formatter:subokFlug" width="5%">是否需要确认</th>
				            <th data-options="field:'confirmFlag',hidden:true,formatter:subFlug" width="5%">是否确认</th> 
				        	<th data-options="field:'sysType'" width="4%" formatter="formatCheckBox">类别</th>
				        	<th data-options="field:'drugFlag',hidden:true" width="6%" >是否是药品</th>
				            <th data-options="field:'FeedetailId',hidden:true" width="10%">编码</th>
				            <th data-options="field:'follow',hidden:true" width="10%">跟随</th>
				            <th data-options="field:'adviceName'" width="13%">项目名称</th>
				            <th data-options="field:'adviceId',hidden:true" width="8%">项目代码</th>
				            <th data-options="field:'group' ,formatter:functionGroup,sortable:'true' " width="4%">组合号</th>
				            <th data-options="field:'feeCode',hidden:true" width="8%">最小费用类别编码</th>
				            <th data-options="field:'totalNum',editor:{type:'numberbox'}" width="4%" >数量</th>   
				            <th data-options="field:'setNum'" width="4%">付数</th>
				            <th data-options="field:'dosageHid'" width="5%">用量</th>
				            <th data-options="field:'unit',formatter:functionFeePack " width="4%">单位</th>
				            <th data-options="field:'frequencyHid',formatter:functionFrequency" width="8%">频次</th>
				            <th data-options="field:'usageNameHid'" width="8%">用法</th>
				            <th data-options="field:'executiveDept',formatter:functionDept,editor:{type:'combobox',options:{valueField:'id',textField:'deptName',multiple:false,method:'post'
				            ,filter:function(q,row){
									var keys = new Array();
									keys[keys.length] = 'deptCode';
									keys[keys.length] = 'deptName';
									keys[keys.length] = 'deptPinyin';
									keys[keys.length] = 'pinyin';
									keys[keys.length] = 'deptInputcode';
									return filterLocalCombobox(q, row, keys);
								},onHidePanel:function(none){
								    var data = $(this).combobox('getData');
								    var val = $(this).combobox('getValue');
								    var result = true;
								    for (var i = 0; i < data.length; i++) {
								        if (val == data[i].deptCode) {
								            result = false;
								        }
								    }
								    if (result) {
								        $(this).combobox('clear');
								    }else{
								        $(this).combobox('unselect',val);
								        $(this).combobox('select',val);
								    }
								}
				            ,url:'${pageContext.request.contextPath}/finance/medicinelist/queryEdComboboxDept.action'}}" width="8%">执行科室</th>
				            <th data-options="field:'adPriceSum',align:'right',formatter:chargePriceColor" width="4%">金额</th>  
				            <th data-options="field:'unitPrice',formatter:funMeng" width="5%">结算类型</th> 
				            <th data-options="field:'adPrice',align:'right'" width="4%">单价</th>
				            <th data-options="field:'youli',hidden:true" width="8%">游离</th>
				            <th data-options="field:'recipeNo',hidden:true" width="8%">处方号</th>
				            <th data-options="field:'injectNumber'" width="5%">院注次数</th>
				            <th data-options="field:'extendOne',hidden:true">拓展标记</th>
				            <th data-options="field:'moOrder',hidden:true">医嘱流水号</th>
				            <th data-options="field:'wtotalNum',hidden:true">伪数量</th>
				            <th data-options="field:'feeStatCode',hidden:true">统计大类代码</th>
				            <th data-options="field:'feeStatName',hidden:true">统计大类名称</th>
				            <th data-options="field:'confirmNum'" width="5%">确认数量</th>
				        </tr>   
				    </thead>   
				</table> 
			</div>
    	</div>
    </div>   
    <div data-options="region:'center',border:false" style="width: 100%;height:height:35%;">
		<input id="invoice" type="hidden" value="${feedetail.invoiceNo }"/>
			  	<table class="honry-table" style="width: 100%;height: 100%;">
						<tr height="15%">
							<td width="70%" colspan="4" style="background: #E0ECFF;" align="left" class="medchangeskin">门诊收据&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;起始发票号： <input class="medchangeskin" style="background: #E0ECFF;border:0px;editable:false;width:180px;" readonly="true" id="invoiceNo" value="${feedetail.invoiceNo}"><a href = "javascript:changeInvoiceNoWIN();" class='easyui-linkbutton' data-options="iconCls:'icon-changeinvoice'">调整</a> <input type="hidden" id="invoiceNos" value="${feedetail.invoiceNo}"></td>
							<td  width="30%" style="background: #E0ECFF;" align="left" colspan="6" class="medchangeskin">项目信息</td>
						</tr>
						<tr height="15%">
							<td width="52%" colspan="4" rowspan="5" style="padding: 0; border:0px">
							<table id="feeList" class="easyui-datagrid"  data-options="fit:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
								<thead>   
							        <tr>
							            <th data-options="field:'feeTypeName'" width="40%">统计类别费用</th>
							            <th data-options="field:'fees'" width="40%">金额</th>
							            <th data-options="field:'feeTypeCode',hidden:true" width="20%">统计费用类别代码</th>
							        </tr>   
							    </thead>   
							</table>
							</td>
							<td width="8%" style="background: #E0ECFF;" class="medchangeskin">自费金额：</td>
							<td width="8%" id="zfje">0.00</td>
							<td width="8%" style="background: #E0ECFF;" class="medchangeskin">自付金额：</td>
							<td width="8%" id="payCost">0.00</td>
							<td width="8%" style="background: #E0ECFF;" class="medchangeskin">记账金额：</td>
							<td width="8%">0.00</td>
						</tr>
						<tr height="15%">
							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">实收金额：</td>
							<td width="5%" id="realPay">0.00</td>
							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">找零金额：</td>
							<td width="5%">0.00</td>
							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">药品总额：</td>
							<td width="5%" id="drugCost">0.00</td>
						</tr>
						<tr height="15%">
							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">发药药房：</td>
							<td width="15%" colspan="3"></td>
							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">总计：</td>
							<td width="5%" id="ze"></td>
						</tr>
<!-- 						<tr height="15%"> -->
<!-- 							<td width="30%" style="background: #E0ECFF;" align="left" colspan="6" class="medchangeskin">诊断信息</td> -->
<!-- 						</tr> -->
<!-- 						<tr height="15%"> -->
<!-- 							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">ICD：</td> -->
<!-- 							<td width="10%" colspan="2"></td> -->
<!-- 							<td width="5%" style="background: #E0ECFF;" class="medchangeskin">诊断名称：</td> -->
<!-- 							<td width="10%" colspan="2"></td> -->
<!-- 						</tr> -->
				</table>
    </div>   
    <div id="queryInfoWondow" class="easyui-window" title="挂号信息" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false"style="width:800px;height:500px;">
    	<table id="infoList" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false" style="width:100%;height:100%">
   			  <thead>   
			        <tr> 
			            <th data-options="field:'deptCode',formatter:functionDept" style="width: 20%">挂号科室</th>   
			            <th data-options="field:'doctCode',formatter:functionEmp" style="width: 20%">挂号专家</th>   
			            <th data-options="field:'pactCode',formatter:functionConit" style="width: 20%">合同单位</th>
			            <th data-options="field:'regDate'" style="width: 20%">挂号日期</th>
			            <th data-options="field:'orderNo'" style="width: 20%">顺序号</th>
			        </tr>   
			  </thead>   
   		</table>
    </div>
    <div id="toolbarId">
		<a id="btnRemove" href="javascript:void(0)" onclick="removeFeed()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		<input  id="addNotDrugId" data-options="prompt:'代码,名称,拼音,五笔'"  class="easyui-combogrid"  style="width: 200px"/>
		&nbsp;&nbsp;&nbsp;&nbsp;辅材标志：F
	</div>
	<div id="wondowNotDrug" class="easyui-window" title="非药品" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:1200px;height:500px;">
	    <div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" style="width:100%;height:12% ;padding:5px 5px 5px 5px;">
	    	<table style="width:100%;border:1px solid #95b8e7;">
				<tr>
					<td style="padding: 5px 15px;font-size:14px" >查询条件：
						<input  id="codes" data-options="prompt:'名称'"  class="easyui-textbox"  style="width: 200px"/>
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</td>
				</tr>
			</table>	
	    </div>
	   	<div data-options="region:'center'" style="width:100%;height:65%;">
	    	<table id="notDrugInfoViewListId" style="width:100%;height:100%" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'undrugName'" style="width:200px">名称</th>
						<th data-options="field:'spec'" style="width:100px">规格</th>
						<th data-options="field:'defaultprice'" style="width:100px">价格</th>
						<th data-options="field:'unit',formatter:functionPack" style="width:100px">单位</th>
						<th data-options="field:'undrugInputcode'" style="width:150px">自定义码</th>
						<th data-options="field:'undrugNotes'" style="width:100px" >注意事项</th>
					</tr>
				</thead>
			</table>
	    </div>
	</div>
	</div>
	<div id="saveWondowFeed" class="easyui-window" title="支付方式" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,closable:false,minimizable:false,maximizable:false" style="width:1000px;height:500px;">
    	<div style="padding:5px 5px 5px 5px;">	   	
			<table>
				<tr>
					<td>
						<a href="javascript:saveHisRecipedetail();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">收费</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeSaveWondowFeed()" data-options="iconCls:'icon-cancel'">关闭</a>
					</td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">	   	
			<table class="honry-table" style="width:100%">
				<tr>
					<td class="honry-lable" style="width:80px;text-align: right;font-size:14px;">自费金额：</td>
					<td width="100px" id = "selfPaidAmount">0.00</td>
					<td class="honry-lable" style="width:80px;text-align: right;font-size:14px;">记账金额：</td>
					<td width="100px">0.00</td>
					<td class="honry-lable" style="width:80px;text-align: right;font-size:14px;">总金额：</td>
					<td width="100px" id="zje"></td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:80px;text-align: right;font-size:14px;">应缴金额：</td>
					<td width="100px" id="yjjes"></td>
					<td class="honry-lable" style="width:80px;text-align: right;font-size:14px;">超标药金额：</td>
					<td width="100px" id="excessCost">0.00</td>
					<td class="honry-lable" style="width:80px;text-align: right;font-size:14px;">自付金额：</td>
					<td width="100px" id="payCostWin">0.00</td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<table class="honry-table" style="width:100%">
				<tr>
					<td style="background: #ff3e96;width:80px;text-align: right;font-size:14px;">实付金额：</td>
					<td><input id="actualCollection" class="easyui-numberbox" data-options="precision:2,required:true" style="width:100px"/></td>
					<td style="background: #ff3e96;width:80px;text-align: right;font-size:14px;">应缴金额：</td>
					<td><input id="shouldPay"  class="easyui-numberbox" readonly="readonly" data-options="precision:2" style="width:100px"/></td>
					<td style="background: #ff3e96;width:80px;text-align: right;font-size:14px;">找零金额：</td>
					<td><input id="giveChange"  class="easyui-numberbox" readonly="readonly" data-options="precision:2" style="width:100px"></input></td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<div id="scheduleTabId" class="easyui-tabs" style="width:100%;height:300px;">   
			    <div title="支付方式">   
			    	<table class="honry-table" style="width:100%;border:0;">
			    		<tr>
			    			<td style="border-left:0;border-top:0;">支付方式</td>
			    			<td style="border-top:0;">金额</td>
			    			<td style="border-top:0;">开户银行/院内账户密码</td>
			    			<td style="border-top:0;">开户账户</td>
			    			<td style="border-top:0;">开具单位</td>
			    			<td style="border-top:0;border-right:0;">支票号/交易号</td>
			    		</tr>
			    		<tr>
			    			<td style="border-left:0;">现金</td>
			    			<td><input class="easyui-numberbox" data-options="precision:2" id="cash" style="width:100px"></td>
			    			<td></td>
			    			<td></td>
			    			<td></td>
			    			<td style="border-right:0;"></td>
			    		</tr>
			    		<tr>
			    			<td style="border-left:0;">银行卡</td>
			    			<td><input class="easyui-numberbox" data-options="precision:2" id="bankCard" style="width:100px"></td>
			    			<td></td>
			    			<td></td>
			    			<td></td>
			    			<td style="border-right:0;"></td>
			    		</tr>
			    		<tr>
			    			<td style="border-left:0;">支票</td>
			    			<td><input class="easyui-numberbox" data-options="precision:2" id="check" style="width:100px"></td>
			    			<td><input class="easyui-textbox" id="bankUniti" style="width:100px"></td>
			    			<td><input class="easyui-textbox" id="banki" style="width:100px"></td>
			    			<td><input class="easyui-textbox" id="bankAccounti" style="width:100px"></td>
			    			<td style="border-right:0;"><input class="easyui-textbox" id="bankNo" style="width:100px"></td>
			    		</tr>
			    		<tr>
			    			<td style="border-left:0;">院内账户</td>
			    			<td><input class="easyui-numberbox" data-options="precision:2" id="hospitalAccount" style="width:100px"></td>
			    			<td><input class="easyui-textbox" type="password" id="accountPassword"  disabled="true" style="width:100px"></td>
			    			<td></td>
			    			<td></td>
			    			<td style="border-right:0;"></td>
			    		</tr>
			    	</table>
			    </div>   
			    <div title="分发票">   
			       <table class="honry-table" id = "invoiceTab"  style="width:100%;border:0;">
						<tr>
							<td class="honry-lable" colspan="6" style="border-left:0;border-top:0;border-right:0;text-align:left;">分发票</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td class="honry-lable" style="width:50px;">票号：</td> -->
<%-- 							<td style="width:150px;" id="tdinvoiceNo">${feedetail.invoiceNo}</td> --%>
<!-- 							<td class="honry-lable" style="width:50px;">金额：</td> -->
<!-- 							<td style="width:150px;" id="sumFees"></td> -->
<!-- 							<td class="honry-lable" style="width:50px;">发票类型：</td> -->
<!-- 							<td class="honry-lable" style="width:150px;">收费</td> -->
<!-- 						</tr> -->
						<tr>
							<td class="honry-lable" style="width:50px;text-align:left;border-left:0;">自费：</td>
							<td style="width:150px;"></td>
							<td class="honry-lable" style="width:50px;text-align:left;">自付：</td>
							<td style="width:150px;"></td>
							<td class="honry-lable" style="width:50px;text-align:left;">记账：</td>
							<td style="width:150px;border-right:0;"></td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:50px;text-align:left;border-left:0;">总金额：</td>
							<td style="width:150px;" id="sumFees"></td>
						</tr>
					</table>
			    </div>   
			</div>  
		</div>
	</div>
	<div id="iReportTable" class="easyui-window" title="报表打印" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false,closable:false" style="width:500px;height:200px;">
	    <table class="honry-table" style="width:100%">
			<tr>
				<td class="honry-lable" style="width:50px;text-align:left;">收费发票：</td>
				<td style="width:150px;"><input id="iReport1" type="checkbox"/></td>
				<td class="honry-lable" style="width:50px;text-align:left;">收费清单：</td>
				<td style="width:150px;"><input id="iReport2" type="checkbox"/></td>
			</tr>
			<tr>
				<td colspan="4"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="iReportButton()" data-options="iconCls:'icon-2012081511202'">打印</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="iReportButtonClose()" data-options="iconCls:'icon-cancel'">关闭</a></td>
			</tr>
		</table>
	</div>
	<div id="changeInvoiceNoWIN" class="easyui-window" title="发票号调整" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false,closable:false" style="width:500x;height:100px;">
		<table style="margin:auto;">
			<tr align="center" >
				<td >
			    	<input id ="changeInvoiceNo" class="easyui-textbox">
			    	<input id ="changeInvoiceID" type="hidden">
			    </td>
			</tr>
			<tr align="center" >
			    <td>
					<a href="javascript:saveChangeInvoice();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#changeInvoiceNoWIN').window('close');" data-options="iconCls:'icon-cancel'">关闭</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="dialog"></div>   
</div>
</body>
</html>