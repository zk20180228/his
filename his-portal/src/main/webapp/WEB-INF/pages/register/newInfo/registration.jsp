<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Object obj =request.getAttribute("mpMap");
	String mpMap="";
	if(obj!=null){
		mpMap=obj.toString();
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊挂号</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/datagrid-scrollview.js"></script>
<script type="text/javascript">
//全局变量
var sexMap=new Map();
var easonMap=new Map();//停诊原因
var middyPMap=new Map();//获取参数表中的午别参数
var submitFlag = "1";//设置只能点击一次操作的标志
$(function(){
	//渲染停诊原因
	$.ajax({
		url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
		data:{"type":"stopReason"},
		type:'post',
		success: function(data) {
			for(var i=0;i<data.length;i++){
				easonMap.put(data[i].encode,data[i].name);
			}
		}
	});
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
	
	//挂号类别（下拉框）
	$('#type').combobox({    
	    url: "<%=basePath%>register/newInfo/typeCombobox.action",   
	    valueField:'encode',    
	    textField:'name',
	    multiple:false,
	    onLoadSuccess: function (data) { 
	    	$('#type').combobox('select', data[0].encode);
	    }
	});	
	
	//挂号级别
	$('#grade').combobox({    
	    url: "<%=basePath%>register/newInfo/gradeCombobox.action",   
	    valueField:'code',    
	    textField:'name',
	    mode:'remote',
	    multiple:false,
	    onLoadSuccess:function(data){
	    	if(data!=null && data.length==1){
	    		var code= data[0].code;
	    		$('#grade').combobox('setValue',code);
	    		queryLinkage();
		    	$('#emp').combobox('setValue',"");
		    	var dept = $('#dept').combobox('getValue');
		    	var midday = $('#midday').combobox('getValue');
		    	$('#emp').combobox('reload', {"ationInfo.reglevlCode":code,"ationInfo.noonCode":midday,"ationInfo.deptCode":dept});
	    	}
	    },
	    onSelect:function(record){
	    	queryLinkage();
	    	$('#emp').combobox('setValue',"");
	    	var dept = $('#dept').combobox('getValue');
	    	var midday = $('#midday').combobox('getValue');
	    	$('#emp').combobox('reload', {"ationInfo.reglevlCode":record.code,"ationInfo.noonCode":midday,"ationInfo.deptCode":dept});
	    	
	    	var contractunit = $('#contractunit').combobox('getValue');
	    	if(contractunit==null||contractunit==''){
	    		$('#contractunit').combobox('setValue','01');
	    	}
	   		$.ajax({
	   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
	   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":record.id},
				success: function(data) {
					$('#fee').text(data.sumCost);
					$('#fees').val(data.sumCost);
				}
			}); 	
	    }
	});	
	
	
	//挂号科室
	$('#dept').combobox({    
	    url: "<%=basePath%>register/newInfo/deptCombobox.action",   
	    valueField:'deptCode',    
	    textField:'deptName',
	    mode:'remote',
	    multiple:false,
	    onLoadSuccess:function(data){
	    	if(data!=null && data.length==1){
	    		var deptcode= data[0].deptCode;
	    		$('#dept').combobox('setValue',deptcode);
	    	    queryLinkage();
	    	    $('#emp').combobox('setValue',"");
	    	    var grade = $('#grade').combobox('getValue');
	    	    var midday = $('#midday').combobox('getValue');
	   		    $('#emp').combobox('reload', {"ationInfo.reglevlCode":grade,"ationInfo.noonCode":midday,"ationInfo.deptCode":deptcode});
	    	}
	    },
	    onSelect:function(record){
	    	queryLinkage();
	    	$('#emp').combobox('setValue',"");
	    	var grade = $('#grade').combobox('getValue');
	    	var midday = $('#midday').combobox('getValue');
	   		$('#emp').combobox('reload', {"ationInfo.reglevlCode":grade,"ationInfo.noonCode":midday,"ationInfo.deptCode":record.deptCode});
	    }
	});	
	
	bindEnterEvent('dept',popWinToDept,'easyui');//绑定回车事件
	
	
	//合同单位
	$('#contractunit').combobox({    
	    url: "<%=basePath%>register/newInfo/contCombobox.action",   
	    valueField:'encode',    
	    textField:'name',
	    //mode:'remote',
	    multiple:false,
	    onLoadSuccess: function (data) { 
	    	$('#contractunit').combobox('select', data[0].encode);
	    },
	    onSelect:function(record){
	    	var grade = $('#grade').combobox('getValue');
	    	if(grade!=""){
	    		$.ajax({
	    			url: "<%=basePath%>register/newInfo/feeCombobox.action",
		   			data:{"ationInfo.pactCode":record.encode,"ationInfo.reglevlCode":grade},  
					type:'post',
					success: function(idCardObj) {
						if(idCardObj.sumCost==null||idCardObj.sumCost==""){
							$('#fee').text("");
							$('#fees').val("");
						}else{
							$('#fee').text(idCardObj.sumCost);
							$('#fees').val(idCardObj.sumCost);
						}
					}
				}); 
	    	}
	    }
	});	
	
	bindEnterEvent('contractunit',popWinToContractunit,'easyui');//绑定合同单位回车事件
	
	//银行
	$('#bank').combobox({    
	    url: "<%=basePath%>register/newInfo/bankCombobox.action",   
	    valueField:'id',    
	    textField:'name',
	    multiple:false
	});	
	
	/**
	 * 开户银行回车弹出事件高丽恒
	 * 2016-03-22 18:41
	 */
	bindEnterEvent('bank',popWinToCodeBank,'easyui');
		function popWinToCodeBank(){
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=bank&textId=bank";
		var aaa=window.open (tempWinPath,'newwindow',' left=200,top=70,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-150) +',scrollbars,resizable=yes,toolbar=no')
	}
	
	//午别 
	$('#midday').combobox({ 
	    onSelect:function(record){
	    	queryMidday(record.value);
	    	//当修改或选择午别时,应该先清空挂号专家信息,因为所选的午别中该专家不一定有挂号信息
	    	$('#emp').combobox('setValue','');
	    	var emp = $('#emp').combobox('getValue');
	    	var midday = $('#midday').combobox('getValue');
	    	var gradeValue=$('#grade').combobox('getValue');
	    	var deptValue=$('#dept').combobox('getValue');
	    	queryLinkage();
	    	queryStatistics(emp,midday);
	    	$('#emp').combobox('reload', {"ationInfo.reglevlCode":gradeValue,"ationInfo.noonCode":midday,"ationInfo.deptCode":deptValue});
	   	}
	});
		
		
	//加载值班医生  
	//2016年12月10日  注掉  因为修改了数据源  需要页面加载时默认的午别数据， 所以不能使用空查询条件
	//queryList("","","","");
	
	
	//动态获取午别
	var myDate = new Date();
	var hours = myDate.getHours();
	var minu = myDate.getMinutes();
	var currTime = Number(hours)*60+Number(minu);
	middyPMap = '<%=mpMap%>';
	middyPJson = $.parseJSON(middyPMap); 
	var starMornHour = middyPJson['1'].split(",")[0].split(":")[0];
	var endMornHour = middyPJson['1'].split(",")[1].split(":")[0];
	var starAfterHour = middyPJson['2'].split(",")[0].split(":")[0];
	var endAfterHour = middyPJson['2'].split(",")[1].split(":")[0];
	var starEvenHour = middyPJson['3'].split(",")[0].split(":")[0];
	var endEvenHour = middyPJson['3'].split(",")[1].split(":")[0];
	
	var starMornMinu = middyPJson['1'].split(",")[0].split(":")[1];
	var endMornMinu = middyPJson['1'].split(",")[1].split(":")[1];
	var starAfterMinu = middyPJson['2'].split(",")[0].split(":")[1];
	var endAfterMinu = middyPJson['2'].split(",")[1].split(":")[1];
	var starEvenMinu = middyPJson['3'].split(",")[0].split(":")[1];
	var endEvenMinu = middyPJson['3'].split(",")[1].split(":")[1];
	
	var starMornTime = Number(starMornHour)*60+Number(starMornMinu);
	var endMornTime = Number(endMornHour)*60+Number(endMornMinu);
	var starAfterTime = Number(starAfterHour)*60+Number(starAfterMinu);
	var endAfterTime = Number(endAfterHour)*60+Number(endAfterMinu);
	var starEvenTime = Number(starEvenHour)*60+Number(starEvenMinu);
	var endEvenTime = Number(endEvenHour)*60+Number(endEvenMinu);
	
	if(currTime>=starMornTime&&currTime<endMornTime){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '上午'
			}
		});
		$('#midday').combobox('setValue','1');
	}else if(currTime>=starAfterTime&&currTime<endAfterTime){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '下午'
			}
		});
		$('#midday').combobox('setValue','2');
	}else if(currTime>=starEvenTime&&currTime<endEvenTime){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '晚上'
			}
		});
		$('#midday').combobox('setValue','3');
	}
	//挂号专家
	$('#emp').combobox({    
	    url: "<%=basePath%>register/newInfo/empCombobox.action",   
	    valueField:'id',    
	    textField:'name',
	    mode:'remote',
	    multiple:false,
	    queryParams:{"ationInfo.deptCode":null,"ationInfo.reglevlCode":null,"ationInfo.noonCode":$('#midday').combobox('getValue')},
	    onSelect:function(record){
	    	$('#grade').combobox('setValue',record.title);
			$('#dept').combobox('setValue',record.dept);
			$('#registerDocSourceId').val(record.sourceID);
	    	queryLinkage();
	    	var midday = $('#midday').combobox('getValue');
	    	var contractunit = $('#contractunit').combobox('getValue');
	    	if(contractunit==null||contractunit==''){
	    		$('#contractunit').combobox('setValue','01');
	    	}
	   		$.ajax({
	   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
	   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":$('#grade').combobox('getValue')},
				success: function(data) {
					$('#fee').text(data.sumCost);
					$('#fees').val(data.sumCost);
				}
			}); 
	    	queryStatistics(record.id,midday);
	    }
	});	
	bindEnterEvent('emp',popWinToEmp,'easyui');//绑定挂号级别回车事件
	
	//回车查询
	bindEnterEvent('idcardNo',queryPatient,'easyui');
	
	$('#actualCollection').numberbox('textbox').bind('keyup', function(event) {
		var actualCollections = $('#actualCollection').numberbox('getText');
		var actualCollection= actualCollections*100; 
		var shouldPays = $('#shouldPay').numberbox('getValue');
		var shouldPay = shouldPays*100;
		var giveChanges =actualCollection-shouldPay;
		var giveChange = giveChanges/100;
		$('#giveChange').numberbox('setValue',giveChange);
	});
	//支付方式（下拉框）
	$('#payType').combobox({
		url:"<%=basePath%>register/newInfo/queryPayType.action",
		valueField:'encode',    
		textField:'name',
		multiple:false,
		onSelect:function(val){
			if(val.encode=='CH'){
				$('#divIds').hide(); 
				$('#divId').show();
				$('#divPassword').hide();
			}
			if(val.encode=='CA'){
				$('#divId').hide(); 
				$('#divIds').show();
				$('#divPassword').hide();
			}
			if(val.encode=='DB'){
				$('#divId').hide(); 
				$('#divIds').hide();
				$('#divPassword').hide();
			}
			if(val.encode=='YS'){
				$('#divId').hide(); 
				$('#divIds').hide();
				$('#divPassword').show();
			}
		}
	});
	//带入午别信息加载列表
	queryLinkage();
	//调用自动刷新方法
// 	refashMethods();
});

function queryList(deptCode,doctCode,reglevlCode,noonCode,flg){
	if(flg==1){
		$('#fee').text('');
		$('#limit').text('');
		$('#limitSum').text('');
		$('#speciallimit').text('');
	}
	$('#limitPrere').text('');
	$('#limitPrereInfo').text('');
	$('#limitNet').text('');
	$('#limitAdd').text('');
	noonCode = $('#midday').combobox('getValue');
	$.messager.progress({text:'加载中...',modal:true});
	$.ajax({
		type:"post",
		url:"<%=basePath%>register/newInfo/findNewList.action",
		async:true,
		data:{"ationInfo.deptCode":deptCode,"ationInfo.doctCode":doctCode,"ationInfo.reglevlCode":reglevlCode,"ationInfo.noonCode":noonCode},
		success:function(data){
			$('#infoList').datagrid({
				view:scrollview,
				rownumbers:false,
				singleSelect:true,
				autoRowHeight:false,
				pageSize:100,
				 onDblClickRow: function (rowIndex, rowData) {//双击查看
					 if(rowData.limitSum-rowData.clinicSum<=0){
						 if(rowData.appFlag==1){//是否加号
							 if(rowData.speciallimit>0){
								 $('#type').combobox('setValue','03');
							 }else{
								 $('#type').combobox('setValue','01');
							 }
							 $('#schemaNo').val(rowData.id);//排班Id
							 //$('#scheduleStarttime').val(rowData.scheduleDate+" "+rowData.scheduleStarttime+":00");//开始时间
							 //$('#scheduleEndtime').val(rowData.scheduleDate+" "+rowData.scheduleEndtime+":00");//结束使劲
							 $('#emp').combobox('setValue',rowData.employeeCode);
							 $('#emp').combobox('setText',rowData.employeeName);
							 $('#dept').combobox('setValue',rowData.deptCode);
							 $('#midday').combobox('setValue',rowData.middayCode);
							 $('#grade').combobox('setValue',rowData.gradeCode);
							 queryLinkage();
							 var contractunit = $('#contractunit').combobox('getValue');
							 var grade = $('#grade').combobox('getValue');
							 if(contractunit!=null&&contractunit!=""){
								 $.ajax({
							   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
							   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":grade},
										success: function(data) {
											$('#fee').text(data.sumCost);
											$('#fees').val(data.sumCost);
										}
									 }); 	
							 }
							
							 $('#limit').text(rowData.limitSum);
							 $('#limitSum').text(rowData.clinicSum);
							 $('#speciallimit').text(rowData.peciallimitSum);
						 }else{
							 $.messager.alert("操作提示","该医生号已满，请重新选择");
						 }
						
					 }else if(rowData.isStop==1){
						 $.messager.alert("操作提示","该医生已停诊，请重新选择");
					 }else{
						 if(rowData.speciallimit>0){
							 $('#type').combobox('setValue','03');
						 }else{
							 $('#type').combobox('setValue','01');
						 }
						 $('#schemaNo').val(rowData.id);//排班Id
						 //$('#scheduleStarttime').val(rowData.scheduleDate+" "+rowData.scheduleStarttime+":00");//开始时间
						 //$('#scheduleEndtime').val(rowData.scheduleDate+" "+rowData.scheduleEndtime+":00");//结束使劲
						 $('#emp').combobox('setValue',rowData.employeeCode);
						 $('#emp').combobox('setText',rowData.employeeName);
						 $('#dept').combobox('setValue',rowData.deptCode);
						 $('#midday').combobox('setValue',rowData.middayCode);
						 $('#grade').combobox('setValue',rowData.gradeCode);
						 queryLinkage();
						 var contractunit = $('#contractunit').combobox('getValue');
						 var grade = $('#grade').combobox('getValue');
						 if(contractunit!=null&&contractunit!=""){
					   		 $.ajax({
					   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
					   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":grade},
								success: function(data) {
									$('#fee').text(data.sumCost);
									$('#fees').val(data.sumCost);
								}
							 }); 	
						  }
						 $('#limit').text(rowData.limitSum);
						 $('#limitSum').text(rowData.clinicSum);
						 $('#speciallimit').text(rowData.peciallimitSum);
						 $('#registerDocSourceId').val(rowData.id);
					 }
				 },
				 rowStyler: function(index,row){
					 if((row.limitSum-row.clinicSum)<=0){
						 if(row.appFlag==1){
							 return 'background-color:#9400D3;color:black;';
						 }else{
							 return 'background-color:#FF0000;color:black;';
						 }
					 }else if(row.isStop==1){
						 return 'background-color:#98FB98;color:black;';
					 }
				 }
			}).datagrid('loadData', data);
			$.messager.progress('close');
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert("请求超时请重试")
		}
		
	});

}

/**
 * 等待时间
 */
function sleep(numberMillis) { 
	var now = new Date(); 
	var exitTime = now.getTime() + numberMillis; 
	while (true) { 
	now = new Date(); 
	if (now.getTime() > exitTime) 
	return; 
	} 
	}

<%-- 	旧版本查询列表
function queryList(deptCode,doctCode,reglevlCode,noonCode){
	$('#limitSum').text('');
	$('#limitPrereInfo').text('');
	$('#limitPrere').text('');
	$('#limit').text('');
	$('#limitNet').text('');
	$('#speciallimit').text('');
	$('#fee').text('');
	$('#limitAdd').text('');
	
	$('#infoList').datagrid({
		 url: "<%=basePath%>register/newInfo/findScheduleList.action",
		 queryParams:{"ationInfo.deptCode":deptCode,"ationInfo.doctCode":doctCode,"ationInfo.reglevlCode":reglevlCode,"ationInfo.noonCode":noonCode},
		 onDblClickRow: function (rowIndex, rowData) {//双击查看
			 if(((rowData.limit)+(rowData.speciallimit))-(rowData.infoAlready)<=0){
				 if(rowData.appFlag==1){
					 if(rowData.speciallimit>0){
						 $('#type').combobox('setValue','03');
					 }else{
						 $('#type').combobox('setValue','01');
					 }
					 $('#schemaNo').val(rowData.workdeptId);//排版Id
					 $('#scheduleStarttime').val(rowData.scheduleDate+" "+rowData.scheduleStarttime+":00");//开始时间
					 $('#scheduleEndtime').val(rowData.scheduleDate+" "+rowData.scheduleEndtime+":00");//结束使劲
					 $('#emp').combobox('setValue',rowData.empId);
					 $('#dept').combobox('setValue',rowData.deptId);
					 $('#midday').combobox('setValue',rowData.midday);
					 $('#grade').combobox('setValue',rowData.grade);
					 queryLinkage();
					 queryStatistics(rowData.empId,rowData.midday);
					 var contractunit = $('#contractunit').combobox('getValue');
					 var grade = $('#grade').combobox('getValue');
					 if(contractunit!=null&&contractunit!=""){
						 $.ajax({
					   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
					   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":grade},
								success: function(data) {
									$('#fee').text(data.sumCost);
									$('#fees').val(data.sumCost);
								}
							 }); 	
					 }
			   		
				 }else{
					 $.messager.alert("操作提示","该医生号已满，请重新选择");
				 }
				
			 }else if(rowData.isStop==1){
				 $.messager.alert("操作提示","该医生已停诊，请重新选择");
			 }else{
				 if(rowData.speciallimit>0){
					 $('#type').combobox('setValue','03');
				 }else{
					 $('#type').combobox('setValue','01');
				 }
				 $('#schemaNo').val(rowData.workdeptId);//排版Id
				 $('#scheduleStarttime').val(rowData.scheduleDate+" "+rowData.scheduleStarttime+":00");//开始时间
				 $('#scheduleEndtime').val(rowData.scheduleDate+" "+rowData.scheduleEndtime+":00");//结束使劲
				 $('#emp').combobox('setValue',rowData.empId);
				 $('#dept').combobox('setValue',rowData.deptId);
				 $('#midday').combobox('setValue',rowData.midday);
				 $('#grade').combobox('setValue',rowData.grade);
				 queryLinkage();
				 queryStatistics(rowData.empId,rowData.midday);
				 var contractunit = $('#contractunit').combobox('getValue');
				 var grade = $('#grade').combobox('getValue');
				 if(contractunit!=null&&contractunit!=""){
			   		 $.ajax({
			   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
			   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":grade},
						success: function(data) {
							$('#fee').text(data.sumCost);
							$('#fees').val(data.sumCost);
						}
					 }); 	
				  }
			 }
		 },
		 rowStyler: function(index,row){
			 if(((row.limit)+(row.speciallimit))-(row.infoAlready)<=0){
				 if(row.appFlag==1){
					 return 'background-color:#9400D3;color:black;';
				 }else{
					 return 'background-color:#FF0000;color:black;';
				 }
			 }else if(row.isStop==1){
				 return 'background-color:#98FB98;color:black;';
			 }
		 }
	});
} --%>
//计算
function formatSurplus(val,row){
	infoSurplu = row.limitSum-row.clinicSum;
	if(infoSurplu>0){
		return infoSurplu;
	}else{
		return 0;
	}
}

/* //渲染挂号午别
function formatCheckBox(val,row){
	return midDayMap.get(val);
} */

function queryLinkage(){
	var grade = $('#grade').combobox('getValue');
	var dept = $('#dept').combobox('getValue');
	var emp = $('#emp').combobox('getValue');
	var midday = $('#midday').combobox('getValue');
	queryList(dept,emp,grade,midday,1);
}

function queryStatistics(doctCode,noonCode){
	var preregisterNo = $('#preregisterNo').val();
	$.ajax({
		url: "<%=basePath%>register/newInfo/queryStatistics.action",
		type:'post',
		data:{"ationInfo.doctCode":doctCode,"ationInfo.noonCode":noonCode},
		success: function(idCardObj) {
			$('#limitSum').text(Number(idCardObj.limitSum)-Number(idCardObj.limitPrereInfo));//总的已挂人数已挂人数
			$('#limit').text(Number(idCardObj.limit)-Number(idCardObj.limitPrereInfo));//挂号总额
			$('#limitPrere').text(idCardObj.limitPrere);//
			$('#limitPrereInfo').text(idCardObj.limitPrereInfo);//预约已挂已挂人数
			$('#limitAdd').text(idCardObj.limitAdd);//
			$('#limitNet').text(idCardObj.limitNet);//
			$('#speciallimit').text(idCardObj.speciallimit);//特殊限额
			if(idCardObj.speciallimit>0){
				$('#type').combobox('setValue','03');
			}else if(preregisterNo!=null&&preregisterNo!=""){
				$('#type').combobox('setValue','02');
			}else{
				$('#type').combobox('setValue','01');
			}
			if(idCardObj.speciallimit!=0&&idCardObj.speciallimit!=null&&idCardObj.speciallimit!=""){
				//特诊挂号，挂号费 ，自费
		   		$.ajax({
		   			url: "<%=basePath%>register/newInfo/speciallimitInfo.action",
		   			type:'post',
		   			data:{speciallimitInfo:speciallimitInfo},
					success: function(data) {
						$('#fee').text(data.parameterValue);
						$('#fees').val(data.parameterValue);
					}
				});
			}
		}
	});
}

function queryPatient(){
	$('#nos').text("");
	$('#midicalrecordNo').text("");
	$('#name').text("");
	$('#sex').text("");
	$('#ageUnits').text("");
	$('#ageUnit').val("");
	$('#age').text("");
	
	var idcardId = $('#idcardNo').val();
	if(idcardId==""||idcardId==null){
		$.messager.alert("操作提示","请输入就诊卡号");
		return false;
	}
	
	$.ajax({
		url: "<%=basePath%>register/newInfo/queryRegisterInfo.action",
		type:'post',
		data:{"ationInfo.cardNo":idcardId},
		success: function(dataObj) {
			if(dataObj.resMsg=="error"){
				$.messager.alert("操作提示",dataObj.resCode);
			}else{
				$('#name').text(dataObj.patient.patientName);//给患者赋值
				$('#sex').text(sexMap.get(dataObj.patient.patientSex));
				$('#midicalrecordNo').text(dataObj.patient.medicalrecordId);//病历号
				var unit = dataObj.patient.unit;
				if(unit!=null&&unit!=""){
					$('#contractunit').combobox('setValue',dataObj.patient.unit);//合同单位
				}
				//得到患者的出生年月日 age
				var age = dataObj.patient.patientBirthday;
				var ages=DateOfBirth(age);
			    $('#age').text(ages.get("nianling"));
			    $('#ageUnits').text(ages.get("ageUnits"));			   
			    $('#cardId').val(dataObj.cardId);
			    $('#patient').val(dataObj.patient);
			    $('#clinicCode').val(dataObj.preregister);
				$('#midicalrecordNoHidden').val(dataObj.patient.medicalrecordId);
			    $('#ageHidden').val(ages.get("nianling"));
			    $('#ageUnit').val(ages.get("ageUnits"));
			    
				if(dataObj.patient.unit==null||dataObj.patient.unit==""){
					$('#fee').text("");
				}else{
					var grade = $('#grade').combobox('getValue');
			    	if(grade!=""){
			    		$.ajax({
				   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
				   			data:{"ationInfo.pactCode":dataObj.patient.unit,"ationInfo.reglevlCode":grade},
							success: function(data) {
								$('#fee').text(data.sumCost);
								$('#fees').val(data.sumCost);
							}
					    }); 	
			    	}
				}
				var preregisterId = dataObj.preregister;
				if(dataObj.resMsg=="success"){
					$('#nos').text(dataObj.preregister);//门诊号
					//根据就诊卡号查询预约信息
					$('#priIdcardNo').textbox('setValue',idcardId);
					$.messager.confirm('提示框', '该患者有预约,你确定要进入预约查询页面吗?',function(res){
						if(res){
							windowPreregisters();
						}
					});
// 					$('#PreregisterList').datagrid('load', {
// 						idcardId : idcardId
// 					});
				
				}else{
					$('#nos').text(dataObj.preregister);//门诊号
				}
			 	 //获取已挂号患者
				$('#infoHisList').datagrid({
					url: "<%=basePath%>register/newInfo/findInfoHisList.action?menuAlias=${menuAlias}",
					queryParams:{'ationInfo.cardNo':idcardId}
				});
			}
		}
	});	

}





/**
* 回车弹出挂号科室选择窗口
* @author  zhuxiaolu
* @param deptIsforregister 是否是挂号科室 1是 0否
* @param deptType 部门类型
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/
function popWinToDept(){
	var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=dept&deptType=C";
	window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='
	+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
}		

/**
* 回车弹出挂号专家选择窗口
* @author  zhuxiaolu
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/
function popWinToEmp(){
	var dept=$('#dept').combobox('getValue');
	var grade=$('#grade').combobox('getValue');
	var midday = $('#midday').combobox('getValue');
	var tempWinPath = "<%=basePath%>popWin/popWinRegisterSchedule/toRegisterSchedulePopWin.action?textId=emp&dept="+dept+"&grade="+grade+"&midday="+midday;
	window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='
	+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
}

/**
* 回车弹出合同单位选择窗口
* @author  zhuxiaolu
* @param textId 页面上commbox的的id
* @date 2016-03-22 14:30   
* @version 1.0
*/
function popWinToContractunit(){
	var tempWinPath = "<%=basePath%>/popWin/popWinUnit/pWCUnitList.action?textId=contractunit";
	window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='
	+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
}
	
//弹窗（输入卡号）
function windowPreregisters(){
	$('#windowPreregister').window('open');
	var date = new Date();
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
	+ (date.getMonth() + 1);
	var strDate = date.getFullYear() + '-' + month + '-' + day;
	//$('#idcardNo').textbox('setValue',$('#idcardNoss').val());
	$('#preregisterNos').textbox('setValue',"");
	$('#preregisterName').textbox('setValue',"");
	$('#preregisterCertificatesno').textbox('setValue',"");
	$('#preregisterPhone').textbox('setValue',"");
	$('#preregisterDate').val(strDate);
	$('#priIdcardNo').textbox('setValue',$('#idcardNo').val());
	findPreregister();
}

//查询午别
function queryMidday(val){
	var s= $('#midday').combobox('getValue');
	if(val!=s){
		$('#midday').combobox('setValue',val);
	}
	if(val==0){
		$('#midday').combobox('setValue','');
	}
	if(val==1){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '上午'
			}
		});
	}else if(val==2){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '下午'
			}
		});
	}else if(val==3){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '晚上'
			}
		});
	}else{
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '全天'
			}
		});
		val=null;
	}
	var deptId = $('#dept').combobox('getValue');
	var grade =  $('#grade').combobox('getValue');
	var empId = $('#emp').combobox('getValue');
	$.messager.progress({text:'加载中...',modal:true});
	$.ajax({
		type:"post",
		url:"<%=basePath%>register/newInfo/findNewList.action",
		async:true,
		data:{"ationInfo.deptCode":deptId,"ationInfo.doctCode":empId,"ationInfo.reglevlCode":grade,"ationInfo.noonCode":val},
		success:function(data){
			$('#infoList').datagrid('loadData', data);
			$.messager.progress('close');
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert("请求超时请重试")
		}
	});
}

//弹窗（预约查询）
function windowPreregister(){
	$('#windowPreregister').window('open');
	var date = new Date();
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
	+ (date.getMonth() + 1);
	var strDate = date.getFullYear() + '-' + month + '-' + day;
	$('#priIdcardNo').textbox('setValue',"");
	$('#preregisterNos').textbox('setValue',"");
	$('#preregisterName').textbox('setValue',"");
	$('#preregisterCertificatesno').textbox('setValue',"");
	$('#preregisterPhone').textbox('setValue',"");
	$('#preregisterDate').val(strDate);
	findPreregister();
}

//刷新
function refurbish(){
	 queryList("","","","",1);
}
//自动刷新
function refashMethods(){
	var midday = $('#midday').combobox('getValue');
	queryList("","","",midday,0);
	t=setTimeout('refashMethods()',2000);
	$('#autorefash').linkbutton('disable');
}
//暂停
function pauseMethods(){
	clearTimeout(t); 
	$('#autorefash').linkbutton('enable');
}
function clears(){
	$('#idcardNo').textbox('setValue','');
	$('#cardId').val('');
	$('#dept').combobox('setValue','');
	$('#grade').combobox('setValue','');
	$('#emp').combobox('setValue','');
	$('#name').text('');
	$('#sex').text('');
	$('#age').text('');
	$('#midicalrecordNo').text('');
	$('#nos').text('');
	$('#ageUnits').text('');
	$('#limitSum').text('');
	$('#limitPrereInfo').text('');
	$('#limitPrere').text('');
	$('#limit').text('');
	$('#limitNet').text('');
	$('#speciallimit').text('');
	$('#fee').text('');
	$('#limitAdd').text('');
	delSelectedData('contractunit');//清空合同单位
	clearComboboxValue('midday');//清空午别
	$('#emp').combobox('reload', {"ationInfo.reglevlCode":null,"ationInfo.noonCode":null,"ationInfo.deptCode":null});

}

//弹窗(支付)
function windowOpen(){
	submitFlag = "1";
 	$('#actualCollection').numberbox('setValue','');
	$('#shouldPay').numberbox('setValue','');
	$('#giveChange').numberbox('setValue','');
	changePay();
	var deptId = $('#dept').combobox('getValue');
	var grade =  $('#grade').combobox('getValue');
	var midday =  $('#midday').combobox('getValue');
	var empId = $('#emp').combobox('getValue');
	var money = $('#fee').text();
	$('#actualCollection').numberbox('setValue',money);
	$('#shouldPay').numberbox('setValue',money);
	$('#giveChange').numberbox('setValue',0);
	var midicalrecordNo = $('#midicalrecordNo').text();
	if($('#idcardNo').textbox('getValue')==""){
		$.messager.alert("操作提示","请输入就诊卡");
		return;
	}
	if($('#type').combobox('getValue')==""){
		$.messager.alert("操作提示","请录入挂号类别");
		return;
	}
	if(deptId==""){
		$.messager.alert("操作提示","请录入科室");
		return;
	}
	if(grade==""){
		$.messager.alert("操作提示","请录入级别");
		return;
	}
	if(midday==""){
		$.messager.alert("操作提示","请录入午别");
		return;
	}
	if(empId==""){
		$.messager.alert("操作提示","请录入医生");
		return;
	}
	if($('#nos').text()==""){
		$.messager.alert("操作提示","请录入门诊号");
		return;
	}
	if($('#contractunit').combobox('getValue')==""){
		$.messager.alert("操作提示","请录入合同单位");
		return;
	}
	if($('#fees').val()==""){
		$.messager.alert("操作提示","请录入挂号费");
		return;
	}
	
	$('#pactName').val($('#contractunit').combobox('getText'));
	$('#reglevlName').val($('#grade').combobox('getText'));
	$('#deptName').val($('#dept').combobox('getText'));
	$('#doctName').val($('#emp').combobox('getText'));
	$.ajax({
		url: "<%=basePath%>register/newInfo/findInfoVo.action",
		data:{"ationInfo.deptCode":deptId,"ationInfo.doctCode":empId,"ationInfo.reglevlCode":grade,"ationInfo.noonCode":midday,"ationInfo.midicalrecordId":midicalrecordNo},
		type:'post',
		success: function(idCardObj){
			if(idCardObj.resMsg=="success"&&idCardObj.resMsgs=="success"){
				$('#windowOpen').window('open');
			}else{
				if(idCardObj.resMsg=="error"){
					var emp = $('#emp').combobox('getText');
					var doctName = $('#doctName').val();
					var midday = $('#midday').combobox('getText');
					$.messager.alert("操作提示","你选择"+doctName+""+midday+"的号已挂过");
				}else{
					$.messager.alert("操作提示","未查询到号源信息，请核对后重试...");
				}
			}
		}
	}); 
	
}
//渲染停诊原因
function formatEason(value,row,index){
	if(value!=null&&value!=''){
		return easonMap.get(value);
	}
	
}

//勾选病历本
function changePay(){
	if($("#payben").is(":checked")){
		$.ajax({
			url: "<%=basePath%>register/newInfo/changePay.action",
			type:'post',
			success: function(idCardObj){
				var shouldPay =	$('#shouldPay').numberbox('getValue');
				shouldPay = parseFloat(shouldPay) +(parseFloat(idCardObj.parameterValue));
				$('#shouldPay').numberbox('setValue',shouldPay);
				$('#actualCollection').numberbox('setValue',shouldPay);
				
				$('#medicalRecordBookPay').val(idCardObj.parameterValue);
				$('#medicalRecordBookFlay').val(1);
				var newSum = $('#actualCollection').numberbox('getValue');
				$('#giveChange').numberbox('setValue',newSum-shouldPay);
				
			}
		}); 			
	}else{
		var fee = $('#fee').text();
		$('#shouldPay').numberbox('setValue',fee);
		$('#medicalRecordBookPay').val(0);
		$('#medicalRecordBookFlay').val(2);
		var newSum = $('#actualCollection').numberbox('getValue');
		$('#giveChange').numberbox('setValue',newSum-fee);
	}
}

function save(){
	var giveChange = $('#giveChange').numberbox('getValue');
	$.ajax({
		url : '<%=basePath%>register/newInfo/queryBlack.action',
			type:'post',
			success: function(data) {
				if(data==1){
					$.messager.alert('提示',"您在挂号员黑名单中，不能挂号!");
					return;
				}else{
					if(giveChange<0){
						$.messager.alert("操作提示","对不起，费用不足，不能支付");
						return;
					}
					passWords();	
				}
			}
		});
}



function passWords(){
	var payType = $('#payType').combobox('getValue');
	if(payType==null||payType==""){
		$.messager.alert("操作提示","请选择支付类型");
		return;
	}
	if(payType=='YS'){
		$("#hiddenPaytype").val(payType);
		var passwords = $('#password').textbox('getValue')
		var blhcs = $('#midicalrecordNo').text();
		if(passwords==null||passwords==""){
			$.messager.alert("操作提示","请选输入密码");
			return;
		}else{
			$.ajax({
				url: '<%=basePath%>register/newInfo/veriPassWord.action',
				data:{"passwords":passwords,"ationInfo.midicalrecordId":blhcs},
				success: function(data) {
					var shouldPay = $('#shouldPay').numberbox('getValue');
					if(data.accountPassword==null){
						$.messager.alert("操作提示","密码错误，请重新输入");
						return;
					}else{
						if(data.isEmpower!="-1"){
							if(data.accountBalance>=shouldPay){
								submit();
							}else{
								$.messager.alert("操作提示","您账户余额不足！");
								return;
							}
						}else{
								$.messager.alert("操作提示","该患者没有账户！");
								return;
						}
					}
					
				}
			});
		}
	}else{
		if(submitFlag == "1"){
			submitFlag = "0";
			submit();
		}
	}
}

function submit(){
	var payType = $('#payType').combobox('getValue');
	$("#hiddenPaytype").val(payType);
	$('#editForm').form('submit',{  
		url: '<%=basePath%>register/newInfo/saveInfo.action',
        onSubmit:function(){
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});	
        },  
        success:function(data){
        	$.messager.progress('close');
        	var dataMap = eval("("+data+")");
        	
   			if(dataMap.resMsg=="error"){
        		$.messager.alert("操作提示",dataMap.resCode);
        	}else if(dataMap.resMsg=="success"){
        		$('#windowOpen').window('close');
	     		submitFlag == "1";
        		jQuery.messager.confirm("提示","支付成功，是否打印挂号条？",function(event){
        			if(event){
        				window.open ("<c:url value='/register/newInfo/ireportRegisterJavaBean.action?fileName=GuaHaoDan'/>&tId="+dataMap.resCode,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
        				updateregister(dataMap.resCode,"1");
        				url: '<%=basePath%>register/newInfo/veriPassWord.action',
        				window.location.href="<%=basePath%>register/newInfo/toView.action?menuAlias=${menuAlias}";
        			}else{
        				updateregister(dataMap.resCode,"0");
        				window.location.href="<%=basePath%>register/newInfo/toView.action?menuAlias=${menuAlias}";
        			}
        		});
        	}else{
        		$.messager.alert("操作提示",'未知错误,请联系管理员!');
        	}
        },
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert("操作提示",'保存失败！');
		}
	});
}

//更新挂号的打印记录
function updateregister(resCode,number){
	$.ajax({
		url: '<%=basePath%>register/newInfo/updateRegistration.action',
		data:{"ationInfo.id":resCode,q:number}
	});
}
//清除挂号级别、挂号科室、挂号专家下拉框的值后,动态加载右侧挂号专家数据
function clearComboboxValue(id){
	if(id=='midday'){
		var tab = $('#scheduleTabId').tabs('getTab',0);
		$('#scheduleTabId').tabs('update', {
			tab: tab,
			options: {
				title: '全天'
			}
		});
		$('#midday').combobox('setValue','');
	}
	delSelectedData(id);
	var gradeValue=$('#grade').combobox('getValue');
	var deptValue=$('#dept').combobox('getValue');
	var midday = $('#midday').combobox('getValue');
	if(id!='midday'){
		delSelectedData('emp');
	}
	$('#emp').combobox('reload', {"ationInfo.reglevlCode":gradeValue,"ationInfo.noonCode":midday,"ationInfo.deptCode":deptValue});
	$('#infoList').datagrid('reload', {"ationInfo.reglevlCode":gradeValue,"ationInfo.noonCode":midday,"ationInfo.deptCode":deptValue});
	$('#'+id).combobox('reload');
	//清空专家时，清空相关费用
	if(id=='emp'){
		$('#limit').text('');
		$('#limitSum').text('');
		$('#speciallimit').text('');
	}
	//清空挂号级别时，清空挂号费
	if(id=='grade'){
		$('#fee').text('');
	}
}



function findPreregister(){
	var prCard= $('#priIdcardNo').val();
	var pDate=$('#preregisterDate').val();
	//查询预约信息
	$('#PreregisterList').datagrid({
		pagination:true,
		pageSize:10,
		pageList:[10,20,30,40,50],
		url: '<%=basePath%>register/newInfo/findPreregisterList.action?',
		queryParams:{'preregister.idcardId':prCard,'preregister.preregisterDate':pDate},
		onLoadSuccess: function(data){
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
			}
		},
		onDblClickRow: function (rowIndex, rowData) {//双击查看
			$('#type').combobox('setValue','02');
			var myDate = rowData.preregisterDate;
			var date = new Date();
			var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
			var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
			+ (date.getMonth() + 1);
			var strDate = date.getFullYear() + '-' + month + '-' + day;

			if(myDate!=strDate){
				$.messager.alert("操作提示","您选择的预约不是当天预约，请选择当天预约");
				return false;
			}
			//判断是否有就诊卡号
	   		$.ajax({
	   			url: '<%=basePath%>register/newInfo/judgeIdcrad.action',
	   			data:{"preregister.idcardId":rowData.idcardId},
				type:'post',
				success: function(idCardObj) {
					if(idCardObj.idCardNo!=null && idCardObj.idCardNo!=""){
						if(rowData.preregisterExpert==null||rowData.preregisterExpert==""){
							$.messager.alert("操作提示","您选择的预约没有预约专家，请重新预约！");
							return false;
						}
						if(rowData.midday==null||rowData.midday==""){
							$.messager.alert("操作提示","您选择的预约没有预约午别，请重新预约！");
							return false;
						}
						$('#patientId').val(idCardObj.patientId);
						$('#idcardNo').textbox('setValue',idCardObj.idCardNo);
						
						$('#emp').combobox('setValue',rowData.preregisterExpert);
						$('#dept').combobox('setValue',rowData.preregisterDept);
						$('#midday').combobox('setValue',rowData.midday);
						$('#grade').combobox('setValue',rowData.preregisterGrade);
						$('#state').val("appointment");//预约标识
						$('#preregisterNo').val(rowData.preregisterNo);
						$('#infoList').datagrid('load', {
							"ationInfo.reglevlCode" : rowData.preregisterGrade,
							"ationInfo.doctCode" : rowData.preregisterExpert,
							"ationInfo.noonCode": rowData.midday,
							"ationInfo.deptCode" : rowData.preregisterDept
						});
						queryStatistics(rowData.preregisterExpert,rowData.midday);
						//查询患者Id
				   		$.ajax({
							url: "<%=basePath%>outpatient/info/queryPreregisterCertificatesno.action", 
							data:{"idcardId":rowData.idcardId},
							type:'post',
							success: function(data) {
								var idCardObj = data;
								$('#patientId').val(idCardObj.id);
								$('#midicalrecordNo').text(rowData.medicalrecordId);
								$('#midicalrecordId').val(rowData.medicalrecordId);
								$('#midicalrecordNoHidden').val(rowData.medicalrecordId);
								$('#nos').text(rowData.preregisterNo);
								$('#clinicCode').val(rowData.preregisterNo);
								$('#no').val(rowData.preregisterNo);
								$('#name').text(rowData.preregisterName);
								$('#contractunit').combobox('setValue',idCardObj.unit);
								$('#sex').text(sexMap.get(idCardObj.patientSex));
								 var age = idCardObj.patientBirthday;
								var ages=DateOfBirth(age);
							    $('#age').text(ages.get("nianling"));
							    $('#ageUnits').text(ages.get("ageUnits"));
							}
						});
				   		//合同单位取值
						var contractunit = $('#contractunit').combobox('getValue');
						if(contractunit!=""&&contractunit!=null){
							//挂号费
							 $.ajax({
						   			url: "<%=basePath%>register/newInfo/feeCombobox.action",
						   			data:{"ationInfo.pactCode":contractunit,"ationInfo.reglevlCode":rowData.preregisterGrade},
									success: function(data) {
										$('#fee').text(data.sumCost);
										$('#fees').val(data.sumCost);
									}
							 }); 	
				   		} 
						queryLinkage();
					}else{
						$.messager.alert("操作提示","该患者没有办理就诊卡，请先办理就诊卡");
					}
				}
			});
			$('#windowPreregister').window('close');
		}
	});
	//回车查询
	bindEnterEvent('preregisterNos',queryPreregister,'easyui');
	bindEnterEvent('priIdcardNo',queryPreregister,'easyui');
	bindEnterEvent('preregisterName',queryPreregister,'easyui');
	bindEnterEvent('preregisterCertificatesno',queryPreregister,'easyui');
	bindEnterEvent('preregisterPhone',queryPreregister,'easyui');
	bindEnterEvent('preregisterDate',queryPreregister,'calendar');
	
}
function queryPreregister(){
	var preregisterNos = $('#preregisterNos').textbox('getValue');//预约号
	var priIdcardNo = $('#priIdcardNo').textbox('getValue');//卡号
	var preregisterName = $('#preregisterName').textbox('getValue');//姓名
	var preregisterCertificatesno = $('#preregisterCertificatesno').textbox('getValue');//证件号
	var preregisterPhone = $('#preregisterPhone').textbox('getValue');//电话号
	var preregisterDate = $('#preregisterDate').val();//预约日期
	$('#PreregisterList').datagrid('load',{
		'preregister.preregisterNo':preregisterNos,
		'preregister.idcardId':priIdcardNo,
		'preregister.preregisterName':preregisterName,
		'preregister.preregisterCertificatesno':preregisterCertificatesno,
		'preregister.preregisterPhone':preregisterPhone,
		'preregister.preregisterDate':preregisterDate
	});
}
function formatYnsee(val,row){
	if(val==1){
		return "是";
	}else{
		return "否";
	}
}
/*******************************开始读卡***********************************************/
//定义一个事件（读卡）
function read_card_ic(){
	var card_value = app.read_ic();
	if(card_value=='0'||card_value==undefined||card_value==''){
		$.messager.alert('提示','此卡号['+card_value+']无效');
		return;
	}
	$("#idcardNo").textbox("setValue",card_value);
	queryPatient();
};
/*******************************结束读卡***********************************************/

</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.tableCss{
	border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
}
.tableCss .TDlabel{
	text-align: right;
	font-size:14px;
	width:30%;
}
.tableCss td{
	border: 1px solid #95b8e7;
	padding: 5px 5px;
	word-break: keep-all;
	white-space:nowrap;
}
.easyuiInput{
	width:15%;
}
.Input{
	width:20%;
}
.panel-body{
	border-left:0
}
</style>
</head>
<body>
<div class="easyui-layout" data-options="fit:true" >
	<div id="top" data-options="region:'north',border:false" style="width: 99%;height:40px;">
		<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
			<tr>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="windowPreregister()" iconCls="icon-presearch">预约查询</a>&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="windowOpen()" iconCls="icon-money_yen">支付</a>&nbsp;
<!-- 				<a href="javascript:void(0)"  class="easyui-linkbutton readCard"  iconCls="icon-bullet_feed"  type_id="registerInfoList_card_no" cardNo="" style="height:25px;">读卡</a>&nbsp;-->
					<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;					
					<input type="hidden" id="registerInfoList_card_no">
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="icon-clear" style="height:25px;">清空</a>&nbsp;
<!-- 				</td> -->
<!-- 				<td> -->
					<a href="javascript:void(0)" onclick="queryMidday(1)" class="easyui-linkbutton" iconCls="icon-date">上午</a>&nbsp;
					<a href="javascript:void(0)" onclick="queryMidday(2)" class="easyui-linkbutton" iconCls="icon-date">下午</a>&nbsp;
					<a href="javascript:void(0)" onclick="queryMidday(3)" class="easyui-linkbutton" iconCls="icon-date">晚上</a>&nbsp;
					<a href="javascript:void(0)" onclick="queryMidday(0)" class="easyui-linkbutton" iconCls="icon-date">全天</a>&nbsp;
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'west',title:'患者信息'" style="width:45%;height: 89%">
		<form id="editForm" method="post">
		<div data-options="split:false" style="width:100%;">
			<div style="padding: 5px 5px 5px 5px;">
				<input id="patient" name="patient" type="hidden"><%--患者ID隐藏域--%>
				<input id="registerDocSourceId" name="registerDocSource.id" type="hidden"><%--医生号源ID隐藏域--%>
				<input type="hidden" id="medicalRecordBookPay" name="ationInfo.bookFee" value="${medicalRecordBookPay }"><%--病历本费的隐藏域--%>
				<input type="hidden" id="medicalRecordBookFlay" name="ationInfo.bookFlag" value="2"><%--是否购买病历本的隐藏域--%>
				<input type="hidden" id="clinicCode" name="ationInfo.clinicCode">
				<input type="hidden" id="midicalrecordNoHidden" name="ationInfo.midicalrecordId">
				<input type="hidden" id="ageHidden" name="ationInfo.patientAge">
				<input type="hidden" name="ationInfo.patientAgeunit" id="ageUnit">
				<input type="hidden" id="fees" name ="ationInfo.sumCost">
				<input type="hidden" id="hiddenPaytype" name ="ationInfo.payType">
				<input type="hidden" id="schemaNo" name ="ationInfo.schemaNo"><%--排版Id--%>
				<input type="hidden" id="scheduleStarttime" name ="ationInfo.beginTime"><%--看诊开始时间--%>
				<input type="hidden" id="scheduleEndtime" name ="ationInfo.endTime"><%--看诊结束时间--%>
				<input type="hidden" id="preregisterNo" name ="preregisterNo"><%--预约单号--%>
				<table class="tableCss">
					<tr>
						<td class="TDlabel" style="width: 10%">卡号 ：</td>
						<td colspan="1">
							<input id="idcardNo" name="ationInfo.cardNo" class="easyui-textbox" data-options="required:true" >
							<input id="cardId" name="ationInfo.cardId" type="hidden">
						</td>
						<td class="TDlabel" style="width: 10%">挂号级别 ：</td>
		    			<td class="Input">
			    	       	<input class="easyui-combobox" id="grade" name="ationInfo.reglevlCode"  data-options="required:true"/> 
			    	       	<input  type="hidden" id="gradeCode"/> 
			    	       	<input  type="hidden" id="reglevlName" name="ationInfo.reglevlName"/> 
			    	       	<a href="javascript:clearComboboxValue('grade');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    		</td>
					</tr>
					<tr>
			    		<td class="TDlabel" style="width: 10%">挂号科室  ：</td>
		    			<td class="Input">
			    	       	<input class="easyui-combobox" id="dept" name="ationInfo.deptCode" data-options="required:true"/> 
			    	       	<input  type="hidden" id="deptName" name="ationInfo.deptName"/>
			    			<a href="javascript:clearComboboxValue('dept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    		</td>
			    		<td class="TDlabel" style="width: 10%">挂号专家 ：</td>
		    			<td class="Input">
			    	       	<input class="easyui-combobox" id="emp" name="ationInfo.doctCode"  data-options="required:true"/>
			    	       	<a href="javascript:clearComboboxValue('emp');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> 
			    	       	<input  type="hidden" id="doctName" name="ationInfo.doctName"/>
			    		</td>
					</tr>
					<tr>
						
			    		<td class="TDlabel" style="width: 10%;display :none">挂号类别 ：</td>
						<td class="Input" style="display :none" >
							<input class="easyui-combobox" name="ationInfo.ynbook" id="type" class="easyui-textbox" data-options="required:true" >
						</td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">合同单位  ：</td>
		    			<td class="Input">
			    	       	<input class="easyui-combobox" id="contractunit"  name="ationInfo.pactCode"  data-options="required:true"> 
			    			<a href="javascript:delSelectedData('contractunit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> 
			    			<input  type="hidden" id="pactName" name="ationInfo.pactName"/>
			    		</td>
			    		<td class="TDlabel" style="width: 10%">挂号午别 ：</td>
	    				<td class="Input">
		    	      	 	<input class="easyui-combobox" id="midday" name="ationInfo.noonCode" data-options=" valueField: 'value',textField: 'label',data: [{label: '上午',value: '1'},{label: '下午',value: '2'},{label: '晚上',value: '3'}],required:true" /> 
		    	      	 	<a href="javascript:clearComboboxValue('midday');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
			    	</tr>
			    	<tr>
			    		<td colspan="4"></td>
			    	</tr>
					<tr>
						<td class="TDlabel" style="width: 10%" >门诊号  ：</td>
						<td class="Input" id="nos"></td>
						<td class="TDlabel" style="width: 10%">病历号  ：</td>
						<td class="Input" id="midicalrecordNo"></td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">姓名  ：</td>
						<td class="Input" id="name"></td>
						<td class="TDlabel" style="width: 10%">性别  ：</td>
						<td class="Input" id="sex"></td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">年龄  ：</td>
						<td class="Input" id="age"></td>
						<td class="TDlabel" style="width: 10%">年龄单位  ：</td>
						<td class="Input" id="ageUnits"></td>
					</tr>
					<tr>
			    		<td colspan="4"></td>
			    	</tr>
					<!-- 原版 挂号信息
					<tr>
						<td class="TDlabel" style="width: 10%">挂号费 ：</td>
						<td class="Input" id="fee"></td>
						<td class="TDlabel" style="width: 10%">加号数  ：</td>
						<td class="Input" id="limitAdd"></td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">挂号总额  ：</td>
						<td class="Input" id="limitSum"></td>
						<td class="TDlabel" style="width: 10%">预约已挂  ：</td>
						<td class="Input" id="limitPrereInfo"></td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">预约限额  ：</td>
						<td class="Input" id="limitPrere"></td>
						<td class="TDlabel" style="width: 10%">挂号限额  ：</td>
						<td class="Input" id="limit"></td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">网络限额  ：</td>
						<td class="Input" id="limitNet"></td>
						<td class="TDlabel" style="width: 10%">特诊限额  ：</td>
						<td class="Input" id="speciallimit"></td>
					</tr>-->
					<tr>
						<td class="TDlabel" style="width: 10%">挂号费 ：</td>
						<td class="Input" id="fee"></td>
						<td class="TDlabel" style="width: 10%">挂号总额  ：</td>
						<td class="Input" id="limit"></td>
					</tr>
					<tr>
						<td class="TDlabel" style="width: 10%">已挂人数  ：</td>
						<td class="Input" id="limitSum"></td>
						<td class="TDlabel" style="width: 10%">特诊限额  ：</td>
						<td class="Input" id="speciallimit"></td>
					</tr>
				</table>	
			</div>				
	    </div>
		</form>
	</div>
	<div data-options="region:'center',title:'值班专家信息'" style="width:55%;height:89%;">
	    <div class="easyui-layout" data-options="fit:true">
	    	<div data-options="region:'north'" style="width:100%;height: 35px;border-top:0;">
		  		<div style="width:98%;margin-top: 2px;">
			    	<table style="width:100%;">
			    		<tr>
			    			<td>
								<a href="javascript:void(0)" onclick="refurbish()" class="easyui-linkbutton" iconCls="icon-reload" style="height:25px;">刷新</a>&nbsp;
			    				<a id="autorefash" href="javascript:void(0)" onclick="refashMethods()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >自动刷新</a>&nbsp;
								<a id="autorefashPause" href="javascript:void(0)" onclick="pauseMethods()" class="easyui-linkbutton" data-options="iconCls:'icon-2012080412263'">暂停</a>&nbsp;
			    				<span style="background-color: FF0000">&nbsp;</span>
			    				<span>号源已满，不可加号</span>&nbsp;
			    				<span style="background-color: #9400D3">&nbsp;</span>
			    				<span>号源已满,但可加号</span>&nbsp;
			    				<span style="background-color: #98FB98">&nbsp;</span>
			    				<span>医生停诊</span>&nbsp;
			    			</td>
			    		</tr>
			    	</table>
		    	</div>
		    </div>
		    <div data-options="region:'center',border:false" style="width: 100%;height: 90%" >
		    	<div id="scheduleTabId" class="easyui-tabs" data-options="fit:true" data-options="border:true">   
				    <div title="值班" style="width: 98%;height: 98%">   
				        <table id="infoList" class="easyui-datagrid"  data-options="border:false,method:'post',rownumbers:true,idField: 'id',striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
								<!-- 原版 挂号列表  
									<th data-options="field:'empName'" style="width:11%">医生名</th>
									<th data-options="field:'titleName'" style="width:15%">级别名</th>
									<th data-options="field:'deptName'" style="width:15%">科室名</th>
									<th data-options="field:'midday'" style="width:6%" formatter="formatCheckBox">午别</th>
									<th data-options="field:'empId'"  hidden="true">医生</th>
									<th data-options="field:'deptId'" hidden="true">科室</th>
									<th data-options="field:'grade'" hidden="true" >级别</th>
									<th data-options="field:'clinic'" style="width:15%" >诊室</th>
									<th data-options="field:'limit'" style="width:6%" >挂号额限</th>
									<th data-options="field:'speciallimit'" style="width:6%" >特诊额限</th>
									<th data-options="field:'infoAlready'" style="width:6%">已挂人数</th>
								  -->
									<th data-options="field:'employeeName'" style="width:8%">医生名</th>
									<th data-options="field:'gradeName'" style="width:10%">级别名</th>
									<th data-options="field:'deptName'" style="width:12%">科室名</th>
									<th data-options="field:'middayName'" style="width:4%" >午别</th>
									<th data-options="field:'employeeCode'"  hidden="true">医生</th>
									<th data-options="field:'deptCode'" hidden="true">科室</th>
									<th data-options="field:'gradeCode'" hidden="true" >级别</th>
									<th data-options="field:'clinicName'" style="width:12%">诊室</th>
									<th data-options="field:'limitSum'" style="width:7%" >挂号额限</th>
									<th data-options="field:'clinicSum'" style="width:7%">已挂人数</th>
									<th data-options="field:'infoSurplus'" formatter="formatSurplus" style="width:7%">号数剩余</th>
									<th data-options="field:'preregisterSum'" style="width:7%" >预约人数</th>
									<th data-options="field:'preclinicSum'" style="width:10%" >预约已取号人数</th>
									<th data-options="field:'peciallimitSum'" style="width:7%" >特诊额限</th>
									<th data-options="field:'stopReason'" style="width:7%"  formatter="formatEason">停诊原因</th>
								</tr>
							</thead>
						</table> 
				    </div>   
				    <div title="已挂" style="width: 98%;height: 98%">   
				         <table id="infoHisList" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'patientName'" style="width:20%">患者姓名</th>
									<th data-options="field:'doctName'" style="width:20%">医生名</th>
									<th data-options="field:'reglevlName'" style="width:20%">级别名</th>
									<th data-options="field:'deptName'" style="width:15%">科室名</th>
									<th data-options="field:'noonCodeNmae'" style="width:15%">午别</th>
									<th data-options="field:'ynsee',formatter:formatYnsee" style="width:15%">是否看诊</th>
								</tr>
							</thead>
						</table>
				    </div>   
				</div> 
			</div>
		</div>
		<div id="windowOpen" class="easyui-window" title="支付方式" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:800px;height:400px;">
			<div style="padding: 5px 5px 5px 5px;">
				<table class="tableCss">
					<tr>
						<td class="TDlabel" style="font-size:14px">实际收款：</td>
						<td><input id="actualCollection" class="easyui-numberbox" data-options="precision:2"/></td>
						<td class="TDlabel" style="font-size:14px">应缴纳金额：</td>
						<td><input id="shouldPay"  class="easyui-numberbox" readonly="readonly" data-options="precision:2"/></td>
						<td class="TDlabel" style="font-size:14px">剩余金额：</td>
						<td><input id="giveChange"  class="easyui-numberbox" readonly="readonly" data-options="precision:2"></input>  </td>
					</tr>
				</table>
			</div>
			<div style="padding: 0px 5px 5px 5px;">
				<table class="tableCss">
					<tr>
						<td class="TDlabel" style="font-size:14px;;width:25%">支付方式：</td>
						<td width="150px"><input id="payType" name="payType" value ="CA"  data-options="required:true" style="width:100px" ></td>
						<td class="TDlabel" style="font-size:14px;;width:25%">是否购买病历本：</td>
						<td><input id="payben" type="checkbox" onclick="changePay()"/></td>
					</tr>
				</table>
			</div>
			<div id="divPassword" style="display:none;padding: 0px 5px 5px 5px;">
				<table class="tableCss">
					<tr>
						<td class="TDlabel" style="font-size:14px">账户密码：</td>
				        <td>
							<input id="password" class="easyui-textbox" type="password"/>
				   	    </td>
				   	</tr>
				</table>
			</div>
			<div id="divId" style="display:none;padding: 0px 5px 5px 5px;">
				<table class="tableCss">
					<tr>
						<td class="TDlabel" style="font-size:14px">开户单位：</td>
				        <td>
							<input id="bankUnit" class="easyui-textbox" name="bankUnit" data-options="required:true" />
				   	    </td>
				   	</tr>
				   	<tr>
				   	    <td class="TDlabel" style="font-size:14px">开户银行：</td>
				        <td>
					        <input id="bank" name="bank"  data-options="required:true"   />
					        <a href="javascript:delSelectedData('bank');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
				        </td>
				    </tr>
				   	<tr>
				        <td class="TDlabel" style="font-size:14px">银行账号：</td>
				        <td>
					        <input id="bankAccount" class="easyui-textbox" name="bankAccount" data-options="required:true"  />
				        </td>
				    </tr>
				   	<tr>
				        <td class="TDlabel" style="font-size:14px">小票号：</td>
				        <td>
					        <input id="bankBillno" class="easyui-textbox" name="bankBillno" data-options="required:true"  />
				        </td>
					</tr>
				</table>
			</div>
			<div style="padding: 0px 5px 5px 5px;">
				<table>
					<tr>
						<td>
							<shiro:hasPermission name="${menuAlias}:function:pay">
								<a href="javascript:save();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">收费</a>
							</shiro:hasPermission>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#windowOpen').window('close')" data-options="iconCls:'icon-cancel'">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		 <div id="windowPreregister" class="easyui-window" title="预约查询列表" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:80%;height:50%;">
				<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="width:100%;height:70px ;padding:8px 5px 5px 5px;">
				    <table style="width:1200px;border:false;padding-top:8px;height:10px">
						<tr>
							<td nowrap="nowrap" style="width:50px">卡号：</td>
							<td  >
								<input class="easyui-textbox" id="priIdcardNo" style="width:100px"  />
							</td>
							<td  nowrap="nowrap" style="width:80px" class="yuyueSize">&nbsp;预约号：</td>
							<td >
								<input class="easyui-textbox" id="preregisterNos" style="width:100px" />
							</td>
							<td  nowrap="nowrap" style="width:70px" class="yuyueSize1">&nbsp;姓名：</td>
							<td  >
								<input class="easyui-textbox" id="preregisterName" style="width:100px" />
							</td>
							<td  nowrap="nowrap" style="width:80px" class="yuyueSize">&nbsp;证件号：</td>
							<td  >
								<input class="easyui-textbox" id="preregisterCertificatesno" style="width:100px" />
							</td>
							<td  nowrap="nowrap" style="width:70px" class="yuyueSize1">&nbsp;电话：</td>
							<td  >
								<input class="easyui-textbox" id="preregisterPhone" style="width:100px" />
							</td>
							<td  nowrap="nowrap" style="width:70px" class="yuyueSize1">&nbsp;日期：</td>
							<td  >
								<input id="preregisterDate" onchange="queryPreregister()"  class="Wdate" style="width:110px" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+6}'})" data-options="showSeconds:true ">
							</td>
							<td style="width:110px">
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="queryPreregister()" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center'" style="width:100%;height:69%;">
					<div style="width: 100%;height: 100%">
					<table id="PreregisterList" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
						<thead>
								<tr>
									<th data-options="field:'preregisterNo'" style="width:10%">预约号</th>
									<th data-options="field:'preregisterName'" style="width:10%">姓名</th>
									<th data-options="field:'preregisterCertificatesno'" style="width:10%">证件号</th>
									<th data-options="field:'preregisterDeptname'" style="width:10%" >科室</th>
									<th data-options="field:'preregisterGradename'" style="width:10%">级别</th>
									<th data-options="field:'preregisterExpertname'"  style="width:10%">专家</th>
									<th data-options="field:'preregisterMiddayname'" style="width:11%">午别</th>
									<th data-options="field:'preregisterDate'" style="width:10%">预约时间</th>
									<th data-options="field:'createTime'" style="width:10%">操作时间</th>
								</tr>
							</thead>
					</table>
					</div>
				</div>
			</div>
		</div>
    </div> 
</div>
</body>
</html>