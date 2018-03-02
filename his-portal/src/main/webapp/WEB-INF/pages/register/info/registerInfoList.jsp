<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <%@ include file="/common/metas.jsp"%> --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊挂号</title>
<%-- <link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/> --%>
<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
		var deptMap = "";//科室
		var gradeMap = "";//级别
		var empMap = "";//人员
		var PayTypeArray = null;//支付方式
		var sexMap=new Map();
		var easonMap=new Map();//停诊原因
		
			
		$(function(){
		//渲染停诊原因
			$.ajax({
				url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
				data:{"type":"stopReason"},
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						easonMap.put(v[i].encode,v[i].name);
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
			    url: "<%=basePath%>outpatient/info/typeCombobox.action",   
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    onLoadSuccess: function (data) { 
			    	$('#type').combobox('select', data[0].encode);
			    }
			});
			//挂号科室
			$('#dept').combobox({    
			    url: "<%=basePath%>outpatient/info/deptCombobox.action",     
			    valueField:'id',    
			    textField:'deptName',
			    multiple:false,
			    onSelect:function(record){
			    	var gradeId = $('#titless').val();
			    	$('#emp').combobox('setValue',"");
			   		$('#emp').combobox('reload', "<%=basePath%>outpatient/info/empCombobox.action?dept="+record.id+"&registerInfo.grade="+gradeId);
			   		var empId =  $('#emp').combobox('getValue');
			   		var deptId = record.id;
			   		var midday =  $('#midday').combobox('getValue');
			   		$('#infoList').datagrid('load', {
						gradeId : gradeId,
						empId : empId,
						midday : midday,
						deptId : deptId
					});
			    }
			});
			bindEnterEvent('dept',popWinToDept,'easyui');//绑定回车事件
			//挂号级别
			$('#grade').combobox({    
			    url: "<%=basePath%>outpatient/info/gradeCombobox.action",      
			    valueField:'id',    
			    textField:'name',
			    multiple:false,
			    onSelect:function(record){
			    	var midday =  $('#midday').combobox('getValue');
			    	$('#emp').combobox('setValue',"");
			   		$('#emp').combobox('reload', "<%=basePath%>outpatient/info/empCombobox.action?grade="+record.encode+"&midday="+midday);
			   		var deptId = $('#dept').combobox('getValue');
			   		var empId =  $('#emp').combobox('getValue');
			   		var gradeId = record.id;
			   		$('#infoList').datagrid('load', {
						gradeId : gradeId,
						empId : empId,
						midday : midday,
						deptId : deptId
					});
			   		var contractunit = $('#contractunit').combobox('getValue');
			   		$.ajax({
			   			url: "<%=basePath%>outpatient/info/feeCombobox.action?unitId="+contractunit+"&gradeId="+record.id,
						success: function(data) {
							var idCardObj = eval("("+data+")");
							$('#fee').text(idCardObj.registerFee);
							$('#fees').val(idCardObj.registerFee);
							$('#shouldPay').numberbox('setValue',idCardObj.registerFee);
						}
					}); 	
			    }
			});
			bindEnterEvent('grade',popWinToGrade,'easyui');//绑定挂号级别回车事件
			//挂号专家
			$('#emp').combobox({    
			 	url: "<%=basePath%>outpatient/info/empCombobox.action",      
			    valueField:'id',    
			    textField:'name',
			    multiple:false,
			    onSelect:function(record){
			    	var grade = $('#grade').combobox('getValue');
			    	var midday = $('#midday').combobox('getValue');
			    	if(grade==""){
			    		$.messager.alert("操作提示","请先选择挂号级别");
			    		$('#emp').combobox('setValue',"");
			    		return false;
			    	}
			    	if(midday==""){
			    		$.messager.alert("操作提示","请先选择午别");
			    		$('#emp').combobox('setValue',"");
			    	}
			    	$.ajax({
						url: "<%=basePath%>outpatient/info/queryGradeTitle.action?encode="+record.title,  
						success: function(data) {
							var idCardObj = eval("("+data+")");
							$('#grade').combobox('setValue',idCardObj.id);
						}
					});
			    	var deptId = $('#dept').combobox('getValue');
			   		var empId = record.id;
			   		$('#infoList').datagrid('load', {
			   			midday : midday,
						empId : empId
					});
			   		//统计
			   		$.ajax({
						url: "<%=basePath%>outpatient/info/queryStatistics.action?empId="+empId+"&midday="+midday,  
						success: function(data) {
							var idCardObj = eval("("+data+")");
							$('#limitSum').text(idCardObj.limitSum);
							$('#limit').text(idCardObj.limit);
							$('#limitPrere').text(idCardObj.limitPrere);
							$('#limitPrereInfo').text(idCardObj.limitPrereInfo);
							$('#limitAdd').text(idCardObj.limitAdd);
							$('#limitNet').text(idCardObj.limitNet);
							$('#speciallimit').text(idCardObj.speciallimit);
							if(idCardObj.speciallimit>0){
								$('#type').combobox('setValue','03');
							}else{
								$('#type').combobox('setValue','01');
							}
							if(idCardObj.speciallimit!=0&&idCardObj.speciallimit!=null&&idCardObj.speciallimit!=""){
								//特诊挂号，挂号费 ，自费
						   		$.ajax({
									url: "<%=basePath%>outpatient/info/speciallimitInfo.action?speciallimitInfo=speciallimitInfo",  
									success: function(data) {
										var dataObj = eval("("+data+")");
										$('#fee').text(dataObj.parameterValue);
									}
								});
							}
						}
					});
			    }
			});
			bindEnterEvent('emp',popWinToEmp,'easyui');//绑定挂号级别回车事件
			//合同单位（下拉框）
			//与挂号级别关联
			//影响挂号费
			$('#contractunit').combobox({    
				url: "<c:url value='/outpatient/info/businessContractunitCombobox.action'/>",
			    valueField:'id',    
			    textField:'name',
			    multiple:false,
			    onLoadSuccess: function (data) { 
			    	$('#contractunit').combobox('select', data[0].id);
			    },
			    onSelect:function(record){
			    	var grade = $('#grade').combobox('getValue');
			    	if(grade!=""){
			    		$.ajax({
				   			url: "<c:url value='/outpatient/info/feeCombobox.action'/>?unitId="+record.id+"&gradeId="+grade,
							type:'post',
							success: function(data) {
								var idCardObj = eval("("+data+")");
								if(idCardObj.registerFee==null||idCardObj.registerFee==""){
									$('#fee').text("");
								}else{
									$('#fee').text(idCardObj.registerFee);
								}
								$('#fees').val(idCardObj.registerFee);
								$('#shouldPay').numberbox('setValue',idCardObj.registerFee);
							}
						}); 
			    	}
			    }
			});
			bindEnterEvent('contractunit',popWinToContractunit,'easyui');//绑定合同单位回车事件
			//银行下拉框
			
			$('#bank').combobox({    
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bank'/>",
			    valueField:'encode',    
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
			
			//加载值班医生
			$('#infoList').datagrid({
				url: "<c:url value='/outpatient/info/findScheduleList.action'/>",
				onDblClickRow: function (rowIndex, rowData) {//双击查看
					if(((rowData.limit)+(rowData.speciallimit))-(rowData.infoAlready)<=0){
						$.messager.alert("操作提示","该医生号已满，请重新选择");
					}else if(rowData.isStop==1){
						$.messager.alert("操作提示","该医生已停诊，请重新选择");
					}else{
						$('#emp').combobox('reload', "<c:url value='/outpatient/info/empCombobox.action'/>?dept="+rowData.deptId);
						if(rowData.speciallimit>0){
							$('#type').combobox('setValue','03');
						}else{
							$('#type').combobox('setValue','01');
						}
						$('#emp').combobox('setValue',rowData.empId);
						$('#dept').combobox('setValue',rowData.deptId);
						$('#midday').combobox('setValue',rowData.midday);
						$('#grade').combobox('setValue',rowData.grade);
						$('#infoList').datagrid('load', {
							gradeId : rowData.grade,
							empId : rowData.empId,
							midday : rowData.midday,
							deptId : rowData.deptId
						});
						//合同单位取值
						var contractunit = $('#contractunit').combobox('getValue');
						$.ajax({
				   			url: "<c:url value='/outpatient/info/feeCombobox.action'/>?unitId="+contractunit+"&gradeId="+rowData.grade,
							type:'post',
							success: function(data) {
								var idCardObj = eval("("+data+")");
								$('#fee').text(idCardObj.registerFee);
								$('#fees').val(idCardObj.registerFee);
								$('#shouldPay').numberbox('setValue',idCardObj.registerFee);
							}
						}); 
						//统计
				   		$.ajax({
							url: "<c:url value='/outpatient/info/queryStatistics.action'/>?empId="+rowData.empId+"&midday="+rowData.midday,  
							type:'post',
							success: function(idCardObj) {
								$('#limitSum').text(idCardObj.limitSum);
								$('#limit').text(idCardObj.limit);
								$('#limitPrere').text(idCardObj.limitPrere);
								$('#limitPrereInfo').text(idCardObj.limitPrereInfo);
								$('#limitAdd').text(idCardObj.limitAdd);
								$('#limitNet').text(idCardObj.limitNet);
								$('#speciallimit').text(idCardObj.speciallimit);
								if(idCardObj.speciallimit>0){
									$('#type').combobox('setValue','03');
								}else{
									$('#type').combobox('setValue','01');
								}
								if(idCardObj.speciallimit!=0&&idCardObj.speciallimit!=null&&idCardObj.speciallimit!=""){
									//特诊挂号，挂号费 ，自费
							   		$.ajax({
										url: "<c:url value='/outpatient/info/speciallimitInfo.action'/>?speciallimitInfo=speciallimitInfo",  
										type:'post',
										success: function(data) {
											var dataObj = eval("("+data+")");
											$('#fee').text(dataObj.parameterValue);
										}
									});
								}
							}
						});
					}
				},
				//改变颜色
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
			//午别 
			$('#midday').combobox({ 
			    onSelect:function(record){
			    	var deptId = $('#dept').combobox('getValue');
			   		var gradeId =  $('#titless').val();
			   		var empId =  $('#emp').combobox('getValue');
			   		var midday = record.value;
			   		$('#infoList').datagrid('load', {
						gradeId : gradeId,
						empId : empId,
						midday : midday,
						deptId : deptId
					});
			   		//统计
			   		$.ajax({
						url: "<c:url value='/outpatient/info/queryStatistics.action'/>?empId="+empId+"&midday="+midday,  
						type:'post',
						success: function(data) {
							var idCardObj = eval("("+data+")");
							$('#limitSum').text(idCardObj.limitSum);
							$('#limit').text(idCardObj.limit);
							$('#limitPrere').text(idCardObj.limitPrere);
							$('#limitPrereInfo').text(idCardObj.limitPrereInfo);
							$('#limitAdd').text(idCardObj.limitAdd);
							$('#limitNet').text(idCardObj.limitNet);
							$('#speciallimit').text(idCardObj.speciallimit);
							if(idCardObj.speciallimit>0){
								$('#type').combobox('setValue','03');
							}else{
								$('#type').combobox('setValue','01');
							}
							if(idCardObj.speciallimit!=0&&idCardObj.speciallimit!=null&&idCardObj.speciallimit!=""){
								//特诊挂号，挂号费 ，自费
						   		$.ajax({
									url: "<c:url value='/outpatient/info/speciallimitInfo.action'/>?speciallimitInfo=speciallimitInfo",  
									type:'post',
									success: function(data) {
										var dataObj = eval("("+data+")");
										$('#fee').text(dataObj.parameterValue);
									}
								});
							}
						}
					});
			   	}
			});
			//回车查询
			bindEnterEvent('idcardNo',query,'easyui');
			
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
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=payway",
				valueField:'encode',    
				textField:'name',
				multiple:false,
				onSelect:function(val){
					if(val.encode=='3'){
						$('#divIds').hide(); 
						$('#divId').show();
						$('#divPassword').hide();
					}
					if(val.encode=='1'){
						$('#divId').hide(); 
						$('#divIds').show();
						$('#divPassword').hide();
					}
					if(val.encode=='2'){
						$('#divId').hide(); 
						$('#divIds').hide();
						$('#divPassword').hide();
					}
					if(val.encode=='4'){
						$('#divId').hide(); 
						$('#divIds').hide();
						$('#divPassword').show();
					}
				}
			});
			//查询预约信息
			$('#PreregisterList').datagrid({
				url: "<c:url value='/outpatient/info/findPreregisterList.action?menuAlias=${menuAlias}'/>",
				onDblClickRow: function (rowIndex, rowData) {//双击查看
					$('#type').combobox('setValue','02');
					//判断是否有就诊卡号
			   		$.ajax({
						url: "<c:url value='/outpatient/info/judgeIdcrad.action'/>?preNo="+rowData.preregisterCertificatesno,  
						type:'post',
						success: function(data) {
							var idCardObj = eval("("+data+")");		
							if(idCardObj.idCardNo!=null||idCardObj.idCardNo!=""){
								$('#patientId').val(idCardObj.patientId);
								$('#idcardNo').textbox('setValue',idCardObj.idCardNo);
								var date = rowData.preregisterDate;
								if("|"+showTime(false,false).trim()+"|"!="|"+date.trim()+"|"){
									$.messager.alert("操作提示","您选择的预约不是当天预约，请选择当天预约");
									return false;
								}
								$('#emp').combobox('setValue',rowData.preregisterExpert.id);
								$('#dept').combobox('setValue',rowData.preregisterDept.id);
								$('#midday').combobox('setValue',rowData.midday);
								$('#grade').combobox('setValue',rowData.preregisterGrade.id);
								$('#state').val("appointment");//预约标识
								$('#preregisterNo').val(rowData.id);
								$('#infoList').datagrid('load', {
									gradeId : rowData.preregisterGrade.id,
									empId : rowData.preregisterExpert.id,
									midday : rowData.midday,
									deptId : rowData.preregisterDept.id
								});
								//统计
						   		$.ajax({
									url: "<c:url value='/outpatient/info/queryStatistics.action'/>?empId="+rowData.preregisterExpert.id+"&midday="+rowData.midday,  
									type:'post',
									success: function(data) {
										var idCardObj = eval("("+data+")");
										$('#limitSum').text(idCardObj.limitSum);
										$('#limit').text(idCardObj.limit);
										$('#limitPrere').text(idCardObj.limitPrere);
										$('#limitPrereInfo').text(idCardObj.limitPrereInfo);
										$('#limitAdd').text(idCardObj.limitAdd);
										$('#limitNet').text(idCardObj.limitNet);
										$('#speciallimit').text(idCardObj.speciallimit);
										if(idCardObj.speciallimit>0){
											$('#type').combobox('setValue','03');
										}else{
											$('#type').combobox('setValue','01');
										}
										if(idCardObj.speciallimit!=0&&idCardObj.speciallimit!=null&&idCardObj.speciallimit!=""){
											//特诊挂号，挂号费 ，自费
									   		$.ajax({
												url: "<c:url value='/outpatient/info/speciallimitInfo.action'/>?speciallimitInfo=speciallimitInfo",  
												type:'post',
												success: function(data) {
													var dataObj = eval("("+data+")");
													$('#fee').text(dataObj.parameterValue);
												}
											});
										}
									}
								});
								//查询患者Id
						   		$.ajax({
									url: "<c:url value='/outpatient/info/queryPreregisterCertificatesno.action'/>?preregisterCertificatesno="+rowData.preregisterCertificatesno,  
									type:'post',
									success: function(data) {
										var idCardObj = data;
										$('#patientId').val(idCardObj.id);
										$('#midicalrecordNo').text(rowData.medicalrecordId);
										$('#midicalrecordId').val(rowData.medicalrecordId);
										$('#nos').text(rowData.preregisterNo);
										$('#no').val(rowData.preregisterNo);
										$('#name').text(rowData.preregisterName);
										$('#sex').text(sexMap.get(rowData.preregisterSex));
										var age = rowData.preregisterAge;
										var ages=DateOfBirth(age);
									    $('#age').text(ages.get("nianling"));
									    $('#ageUnits').text(ages.get("ageUnits"));
									}
								});
						   		//合同单位取值
								var contractunit = $('#contractunit').combobox('getValue');
								$.ajax({
						   			url: "<c:url value='/outpatient/info/feeCombobox.action'/>?unitId="+contractunit+"&gradeId="+rowData.preregisterGrade.id,
									type:'post',
									success: function(data) {
										var idCardObj = eval("("+data+")");
										$('#fee').text(idCardObj.registerFee);
										$('#fees').val(idCardObj.registerFee);
										$('#shouldPay').val(idCardObj.registerFee);
									}
								}); 
								//刷新患者信息列表
								$.ajax({
									url: "<c:url value='/outpatient/changeDeptLog/querydeptComboboxs.action'/>", 
									type:'post',
									success: function(deptData) {
										deptMap = eval("("+deptData+")");
									}
								});	
								$.ajax({
									url: "<c:url value='/outpatient/changeDeptLog/querygradeComboboxs.action'/>",
									type:'post',
									success: function(gradeData) {
										gradeMap = eval("("+gradeData+")");
									}
								});	
								$.ajax({
									url: "<c:url value='/outpatient/changeDeptLog/queryempComboboxs.action'/>",
									type:'post',
									success: function(empData) {
										empMap = eval("("+empData+")");
									}
								});
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
			//时间查询
			$('#preregisterDate').datebox({    
			    onSelect: function(data){
			    	var date = $('#preregisterDate').datebox('getValue');
			    	$('#PreregisterList').datagrid('load', {
			    		preDate : date
					});
			    }
			}); 
			
			
			//动态获取午别
			var myDate = new Date();
			myDate.getHours();
			if(myDate.getHours()>8&&myDate.getHours()<=12){
				var tab = $('#scheduleTabId').tabs('getTab',0);
				$('#scheduleTabId').tabs('update', {
					tab: tab,
					options: {
						title: '上午'
					}
				});
				$('#midday').combobox('setValue','1');
			}else if(myDate.getHours()>12&&myDate.getHours()<=20){
				var tab = $('#scheduleTabId').tabs('getTab',0);
				$('#scheduleTabId').tabs('update', {
					tab: tab,
					options: {
						title: '下午'
					}
				});
				$('#midday').combobox('setValue','2');
			}else{
				var tab = $('#scheduleTabId').tabs('getTab',0);
				$('#scheduleTabId').tabs('update', {
					tab: tab,
					options: {
						title: '晚上'
					}
				});
				$('#midday').combobox('setValue','3');
			}
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
			$("#idcardNo").textbox("setValue",card_value);
			var idcardId = $('#idcardNo').val();
			query();
		};
		/*******************************结束读卡***********************************************/
		//渲染挂号午别
		function formatCheckBox(val,row){
			if(val=="1"){
				return '上午';
			}else if(val=="2"){
				return '下午';
			}else if(val=="3"){
				return '晚上';
			}
		}
		//计算
		function formatSurplus(val,row){
			infoSurplu = ((row.limit)+(row.speciallimit))-(row.infoAlready);
			if(infoSurplu>0){
				return infoSurplu;
			}else{
				return 0;
			}
		}
		//根据就诊卡号查询患者信息
		function query(){
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
				url: "<c:url value='/outpatient/info/queryRegisterInfo.action'/>?idcardNo="+idcardId,
				type:'post',
				success: function(data) {
					var dataObj = eval("("+data+")");
					if(dataObj.black=="error"){
						$.messager.alert("操作提示", "患者在黑名单中，不能挂号");
						return false;
					}
					if(dataObj.infoPatient.patientId==null||dataObj.infoPatient.patientId==""){
						$.messager.alert("操作提示", "卡号不存在");
						return false;
					}
					$('#patientId').val(dataObj.infoPatient.patientId);
					$('#name').text(dataObj.infoPatient.name);//给患者赋值
					$('#sex').text(sexMap.get(dataObj.infoPatient.sex));
					$('#midicalrecordNo').text(dataObj.infoPatient.infoMedicalrecordId);//病例号
					$('#midicalrecordId').val(dataObj.infoPatient.infoMedicalrecordId);//病例号 隐藏域
					$('#contractunit').combobox('setValue',dataObj.infoPatient.cout);//合同单位
					var preregisterId = dataObj.stuts;
					if(preregisterId!=""&&preregisterId!=null){
						$('#nos').text(dataObj.preregister);//门诊号
						$('#no').val(dataObj.preregister);//门诊号 隐藏域
						//根据就诊卡号查询预约信息
						$('#priIdcardNo').textbox('setValue',idcardId);
						$.messager.confirm('提示框', '该患者有预约,你确定要进入预约查询页面吗?',function(res){
							if(res){
								windowPreregisters();
							}
						});
						$('#PreregisterList').datagrid('load', {
							idcardId : idcardId
						});
					}else{
						$('#nos').text(dataObj.preregister);//门诊号
						$('#no').val(dataObj.preregister);//病例号
					}
					$('#idcardIds').val(dataObj.infoPatient.idCardNo);
					//得到患者的出生年月日 age
					var age = dataObj.infoPatient.dates;
					var ages=DateOfBirth(age);
				    $('#age').text(ages.get("nianling"));
				    $('#ageUnits').text(ages.get("ageUnits"));
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/querydeptComboboxs.action'/>", 
				type:'post',
				success: function(deptData) {
					deptMap = eval("("+deptData+")");
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/querygradeComboboxs.action'/>",
				type:'post',
				success: function(gradeData) {
					gradeMap = eval("("+gradeData+")");
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/queryempComboboxs.action'/>",
				type:'post',
				success: function(empData) {
					empMap = eval("("+empData+")");
					$('#infoHisList').datagrid({
						url: "<c:url value='/outpatient/info/findInfoHisList.action'/>?menuAlias=${menuAlias}&idcardNo="+idcardId
					});
				}
			});
		}
		//渲染停诊原因
		function formatEason(value,row,index){
			if(value!=null&&value!=''){
				return easonMap[value];
			}
			
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
		//渲染级别
		function functionGrade(value,row,index){
			if(value!=null&&value!=''){
				return gradeMap[value];
			}
		}	
		//查询午别
		function queryMidday(val){
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
			}else{
				var tab = $('#scheduleTabId').tabs('getTab',0);
				$('#scheduleTabId').tabs('update', {
					tab: tab,
					options: {
						title: '晚上'
					}
				});
			}
			$('#infoList').datagrid('load', {
				midday : val
			});
		}
		//弹窗(支付)
		function windowOpen(){
			var deptId = $('#dept').combobox('getValue');
			var grade =  $('#grade').combobox('getValue');
			var midday =  $('#midday').combobox('getValue');
			var empId = $('#emp').combobox('getValue');
			var state = $('#state').val();
			var money = $('#fee').text();
			var midicalrecordNo = $('#midicalrecordNo').text();
			$('#shouldPay').numberbox('setValue',money);
			if($('#idcardNo').textbox('getValue')==""){
				$.messager.alert("操作提示","请输入就诊卡");
				return;
			}
			if($('#patientId').val()==""){
				$.messager.alert("操作提示","请录入患者");
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
			//验证还有没有号源
			$.ajax({
				url: "<c:url value='/outpatient/info/findInfoVo.action'/>?empId="+empId+"&midday="+midday+"&gradeId="+grade+"&deptId="+deptId+"&state="+state+"&blhcs="+midicalrecordNo,
				type:'post',
				success: function(idCardObj){
					if(idCardObj==0){
						$('#windowOpen').window('open');
					}else{
						var emp = $('#emp').combobox('getText');
						var midday = $('#midday').combobox('getText');
						$.messager.alert("操作提示","你选择"+emp+""+midday+"的号已挂过");
					}
				}
			}); 
		}
		
		//弹窗（预约查询）
		function windowPreregister(){
			$('#priIdcardNo').textbox('setValue',"");
			$('#preregisterNos').textbox('setValue',"");
			$('#preregisterName').textbox('setValue',"");
			$('#preregisterCertificatesno').textbox('setValue',"");
			$('#preregisterPhone').textbox('setValue',"");
			$('#PreregisterList').datagrid('load',{});
			var myDate = new Date();
			var newDate = myDate.toLocaleDateString();
			$('#preregisterDate').datebox('setValue',newDate);
			$('#windowPreregister').window('open');
		}
		//弹窗（输入卡号）
		function windowPreregisters(){
			$('#idcardNo').textbox('setValue',$('#idcardNoss').val());
			$('#preregisterNos').textbox('setValue',"");
			$('#preregisterName').textbox('setValue',"");
			$('#preregisterCertificatesno').textbox('setValue',"");
			$('#preregisterPhone').textbox('setValue',"");
			$('#preregisterDate').datebox('setValue',"");
			$('#windowPreregister').window('open');
		}
		//预约条件查询
		function queryPreregister(){
			var priIdcardNo = $('#priIdcardNo').textbox('getValue');
			var preregisterNo = $('#preregisterNos').textbox('getValue');
			var preregisterName = $('#preregisterName').textbox('getValue');
			var preregisterCertificatesno = $('#preregisterCertificatesno').textbox('getValue');
			var preregisterPhone = $('#preregisterPhone').textbox('getValue');
			$('#PreregisterList').datagrid('load', {
				preregisterNo : preregisterNo,
				preregisterName : preregisterName,
				preregisterCertificatesno : preregisterCertificatesno,
				idcardId : priIdcardNo,
				phone : preregisterPhone
			});
		}
		//提交表单
		function save(){ 
			var payType =$('#payType').combobox('getValue');
			$('#payTypei').val(payType);
			var bankUnit =$('#bankUnit').val();
			$('#bankUniti').val(bankUnit);
			var bank =$('#bank').val();
			$('#banki').val(bank);
			var bankAccount =$('#bankAccount').val();
			$('#bankAccounti').val(bankAccount);
			var bankBillno =$('#bankBillno').val();
			$('#bankBillnoi').val(bankBillno);
			submit();	
		}
		//挂号提交
		function submit(){ 
			var deptId = $('#dept').combobox('getValue');
			var grade =  $('#grade').combobox('getValue');
			var midday =  $('#midday').combobox('getValue');
			var empId = $('#emp').combobox('getValue');
			var payType = $('#payType').combobox('getValue');
			var blhcs = $('#midicalrecordNo').text();
			var itemFlag=0;
			if(payType==null||payType==""){
				$.messager.alert("操作提示","请选择支付类型");
				return;
			}
			if(payType=='4'){
				var passwords = $('#password').textbox('getValue')
				if(passwords==null||passwords==""){
					$.messager.alert("操作提示","请选输入密码");
					return;
				}else{
					$.ajax({
						url: '<%=basePath%>outpatient/info/veriPassWord.action',
						data:{"passwords":passwords,"blhcs":blhcs},
						success: function(data) {
							if(data=="NO"){
								$.messager.alert("操作提示", "患者密码错误，请重新输入");	
							}else if(data=="NOK"){
								$.messager.alert("操作提示", "该患者没有账户");
							}else{
								if($("#payben").is(":checked")){
									itemFlag=1;				
								}
								$.ajax({
									url: "<c:url value='/outpatient/info/findInfoVo.action'/>?empId="+empId+"&midday="+midday+"&grade="+grade+"&deptId="+deptId+"&blhcs="+blhcs,
									type:'post',
									success: function(idCardObj) {
										if(idCardObj==0){
											$('#editForm').form('submit',{  
												url: "<c:url value='/outpatient/info/saveRegisterInfo.action'/>",
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
										        		var id = dataMap.resCode;
										        		jQuery.messager.confirm("提示","支付成功，是否打印挂号条？",function(event){
										        			if(event){
										        				window.open ("<c:url value='/iReport/iReportPrint/iReportToregisterInfo.action?fileName=MZGHD'/>&tId="+dataMap.resCode,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
										        				window.location.href="<c:url value='/outpatient/info/listRegisterInfo.action'/>?menuAlias=${menuAlias}";
										        			}else{
										        				window.location.href="<c:url value='/outpatient/info/listRegisterInfo.action'/>?menuAlias=${menuAlias}";
										        			}
										        		});
//				 						        		var infoExenp = $('#infoExenp').val();
//				 						        		if(infoExenp==0){//关闭（不添加发票信息）
//				 						        			window.location.href="<c:url value='/register/listregisterInfo.action'/>?menuAlias=${menuAlias}";
//				 						        		}else{
//									        			
										        	}else{
										        		$.messager.alert("操作提示",'未知错误,请联系管理员!');
										        	}
										        },
												error : function(data) {
													$.messager.alert("操作提示",'保存失败！');
												}							         
										    }); 
															
										}else{
											$.messager.alert("操作提示","你选择的号已挂过");
											return false;
										}
									}
								}); 
							}
						}
					});
				}
			}else{
				if($("#payben").is(":checked")){
					itemFlag=1;				
				}
				$.ajax({
					url: "<c:url value='/outpatient/info/findInfoVo.action'/>?empId="+empId+"&midday="+midday+"&grade="+grade+"&deptId="+deptId+"&blhcs="+blhcs,
					type:'post',
					success: function(idCardObj) {
						if(idCardObj==0){
							$('#editForm').form('submit',{  
								url: "<c:url value='/outpatient/info/saveRegisterInfo.action'/>",
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
						        		var id = dataMap.resCode;
						        		jQuery.messager.confirm("提示","支付成功，是否打印挂号条？",function(event){
						        			if(event){
						        				window.open ("<c:url value='/iReport/iReportPrint/iReportToregisterInfo.action?fileName=MZGHD'/>&tId="+dataMap.resCode,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
						        				window.location.href="<c:url value='/outpatient/info/listregisterInfo.action'/>?menuAlias=${menuAlias}";
						        			}else{
						        				window.location.href="<c:url value='/outpatient/info/listregisterInfo.action'/>?menuAlias=${menuAlias}";
						        			}
						        		});
// 						        		var infoExenp = $('#infoExenp').val();
// 						        		if(infoExenp==0){//关闭（不添加发票信息）
// 						        			window.location.href="<c:url value='/register/listregisterInfo.action'/>?menuAlias=${menuAlias}";
// 						        		}else{
						        			
// 						        		}
						        	}else{
						        		$.messager.alert("操作提示",'未知错误,请联系管理员!');
						        	}
						        },
								error : function(data) {
									$.messager.alert("操作提示",'保存失败！');
								}							         
						    }); 
											
						}else{
							$.messager.alert("操作提示","你选择的号已挂过");
							return false;
						}
					}
				}); 
			}
		}
		
		//刷新
		function refurbish(){
			$('#infoList').datagrid('load', {});
		}
		
		$('#infoHisList').datagrid({
			url: "<c:url value='/outpatient/info/findInfoHisList.action'/>?menuAlias=${menuAlias}&idcardNo="+idCardObj.idCardNo
		});
		//勾选病历本
		function changePay(){
			if($("#payben").is(":checked")){
				$.ajax({
					url: "<c:url value='/outpatient/info/changePay.action'/>",
					type:'post',
					success: function(data){
					var idCardObj = eval("("+data+")");
						var shouldPay =	$('#shouldPay').numberbox('getValue');
						shouldPay = parseFloat(shouldPay) +(parseFloat(idCardObj.parameterValue));
						$('#shouldPay').numberbox('setValue',shouldPay);
						$('#medicalRecordBookPay').val(idCardObj.parameterValue);
						$('#medicalRecordBookFlay').val(1);
						var newSum = $('#actualCollection').numberbox('getValue');
						$('#giveChange').numberbox('setValue',newSum-shouldPay);
					}
				}); 			
			}else{
				var fee = $('#fees').val();
				$('#shouldPay').numberbox('setValue',fee);
				$('#medicalRecordBookPay').val(0);
				$('#medicalRecordBookFlay').val(2);
				var newSum = $('#actualCollection').numberbox('getValue');
				$('#giveChange').numberbox('setValue',newSum-fee);
			}
		}
		
		function clears(){
			$('#dept').combobox('setValue','');
			$('#grade').combobox('setValue','');
			$('#emp').combobox('setValue','');
			$('#name').text('');
			$('#sex').text('');
			$('#age').text('');
			$('#midicalrecordNo').text('');
			$('#nos').text('');
			$('#ageUnits').text('');
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
		* 回车弹出挂号级别选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		function popWinToGrade(){
			
			popWinGradeCallBackFn = function(node){
				
		    	$("#gradeCode").val(node.encode);
		    	$('#grade').combobox('setValue',node.name);
		    	var deptId = $('#dept').combobox('getValue');
				var deptId = $('#dept').combobox('getValue');
		   		var empId =  $('#emp').combobox('getValue');
		   		var gradeId = $("#grade").combobox('getValue');
		   		var midday =  $('#midday').combobox('getValue');
		    	$('#infoList').datagrid('load', {
		    	
		 			gradeId : gradeId,
					empId : empId,
					midday : midday,
					deptId : deptId
				})
		    
			};
			
			var tempWinPath = "<%=basePath%>popWin/popWinGrade/popWinRegisterGradeBedList.action?textId=grade";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='
			+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes');
			
			
			
			   		
		}
		/**
		* 回车弹出挂号专家选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		function popWinToEmp(){
			var dept=$("#dept").combobox('getValue');
			var grade=$("#gradeCode").val();
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
		
		
	</script>		
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
	</style>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true" >
		<div id="top" data-options="region:'north',border:false" style="width: 99%;height:40px;">
			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
				<tr>
					<td>
						<a href="javascript:void(0)" onclick="queryMidday(1)" class="easyui-linkbutton" iconCls="icon-date">上午</a>&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="queryMidday(2)" class="easyui-linkbutton" iconCls="icon-date">下午</a>&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="queryMidday(3)" class="easyui-linkbutton" iconCls="icon-date">晚上</a>&nbsp;&nbsp;
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="windowPreregister()" iconCls="icon-search">预约查询</a>&nbsp;&nbsp;
						<shiro:hasPermission name="${menuAlias}:function:pay">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="windowOpen()" iconCls="icon-money_yen">支付</a>&nbsp;&nbsp;
						</shiro:hasPermission>
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'" style="height:25px;">读卡</a>&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="icon-clear" style="height:25px;">清空</a>&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="refurbish()" class="easyui-linkbutton" iconCls="icon-reload" style="height:25px;">刷新</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'west',border:false,title:'患者信息'" style="width:44%;height: 89%">
			<form id="editForm">
				<input type="hidden" id="payTypei" name="payType">
				<input type="hidden" id="bankUniti" name="bankUnit">
				<input type="hidden" id="medicalRecordBookPay" name="medicalRecordBookPay" value="${medicalRecordBookPay }"><%--病历本费的隐藏域--%>
				<input type="hidden" id="medicalRecordBookFlay" name="medicalRecordBookFlay" value="2"><%--是否购买病历本的隐藏域--%>
				<input type="hidden" id="banki" name="banki">
				<input type="hidden" id="infoExenp" value="${infoExenp }">
				<input type="hidden" id="idcardIds" name="idcardId">
				<input type="hidden" id="bankAccounti" name="bankAccount">
				<input type="hidden" id="bankBillnoi" name="bankBillno">
				<input type="hidden" id="gradeId"><%--挂号级别的隐藏域--%>
				<input type="hidden" name="fee" id="fees"><%--挂号费的隐藏域--%>
				<input type="hidden" name="patientIds" id="patientId"><%--患者ID隐藏域--%>
				<input type="hidden" name="midicalrecordId" id="midicalrecordId"><%--病例号的隐藏域--%>
				<input type="hidden" name="no" id="no"><%--预约号隐藏域--%>
				<input type="hidden" name="preregisterNo" id="preregisterNo"><%--回显预约ID隐藏域--%>
				<input type="hidden" id="idcardNoss"><%--回显就诊卡号的隐藏域--%>
				<input type="hidden" id="state"><%--判断在没有号源的情况下，从预约挂号取号的隐藏域--%>
			<div data-options="split:false" style="width:100%;">
					<div style="padding: 5px 5px 5px 5px;">
						<table class="tableCss">
							<tr>
								<td class="TDlabel">卡号 ：</td>
								<td colspan="3">
								<input id="idcardNo" class="easyui-textbox" data-options="required:true" >
								</td>
							</tr>
							<tr>
								<td class="TDlabel">挂号级别 ：</td>
				    			<td class="Input">
					    	       	<input class="easyui-combobox" id="grade" name="grade"  data-options="required:true"/> 
					    	       	<input  type="hidden" id="gradeCode"/> 
					    	       	<a href="javascript:delSelectedData('grade');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					    		</td>
					    		<td class="TDlabel">挂号科室  ：</td>
				    			<td class="Input">
					    	       	<input class="easyui-combobox" id="dept" name="dept" data-options="required:true"/> 
					    			<a href="javascript:delSelectedData('dept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					    		</td>
							</tr>
							<tr>
								<td class="TDlabel">挂号专家 ：</td>
				    			<td class="Input">
					    	       	<input class="easyui-combobox" id="emp" name="expxrt"  data-options="required:true"/>
					    	       	<a href="javascript:delSelectedData('emp');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> 
					    		</td>
					    		<td class="TDlabel">挂号类别 ：</td>
								<td class="Input">
									<input class="easyui-combobox" name="type" id="type" class="easyui-textbox" data-options="required:true">
								</td>
							</tr>
							<tr>
								<td class="TDlabel">合同单位  ：</td>
				    			<td class="Input">
					    	       	<input class="easyui-combobox" id="contractunit"  name="contractunit"  data-options="required:true"> 
					    			<a href="javascript:delSelectedData('contractunit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> 
					    		</td>
					    		<td class="TDlabel">挂号午别 ：</td>
			    				<td class="Input">
				    	      	 	<input class="easyui-combobox" id="midday" name="midday" data-options=" valueField: 'value',textField: 'label',data: [{label: '上午',value: '1'},{label: '下午',value: '2'},{label: '晚上',value: '3'}],required:true" /> 
				    			</td>
					    	</tr>
					    	<tr>
					    		<td colspan="4"></td>
					    	</tr>
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 					<div style="padding: 0px 5px 5px 5px;"> -->
<!-- 						<table class="tableCss"> -->
							<tr>
								<td class="TDlabel" >门诊号  ：</td>
								<td class="Input" id="nos"></td>
								<td class="TDlabel">病例号  ：</td>
								<td class="Input" id="midicalrecordNo"></td>
							</tr>
							<tr>
								<td class="TDlabel">姓名  ：</td>
								<td class="Input" id="name"></td>
								<td class="TDlabel">性别  ：</td>
								<td class="Input" id="sex"></td>
							</tr>
							<tr>
								<td class="TDlabel">年龄  ：</td>
								<td class="Input" id="age"></td>
								<td class="TDlabel">年龄单位  ：</td>
								<td class="Input" id="ageUnits"><input type="hidden" name="ageUnit" id="ageUnit"></td>
							</tr>
							<tr>
					    		<td colspan="4"></td>
					    	</tr>
							<tr>
								<td class="TDlabel">挂号费 ：</td>
								<td class="Input" id="fee"></td>
								<td class="TDlabel">加号数  ：</td>
								<td class="Input" id="limitAdd"></td>
							</tr>
							<tr>
								<td class="TDlabel">挂号总额  ：</td>
								<td class="Input" id="limitSum"></td>
								<td class="TDlabel">预约已挂  ：</td>
								<td class="Input" id="limitPrereInfo"></td>
							</tr>
							<tr>
								<td class="TDlabel">预约限额  ：</td>
								<td class="Input" id="limitPrere"></td>
								<td class="TDlabel">挂号限额  ：</td>
								<td class="Input" id="limit"></td>
							</tr>
							<tr>
								<td class="TDlabel">网络限额  ：</td>
								<td class="Input" id="limitNet"></td>
								<td class="TDlabel">特诊限额  ：</td>
								<td class="Input" id="speciallimit"></td>
							</tr>
						</table>	
					</div>				
			    </div>
			</form>
		</div>
	    <div data-options="region:'center',title:'值班专家信息',border:false" style="width:55%;height:89%;">
	    <div class="easyui-layout" data-options="fit:true">
	    	<div data-options="region:'north'" style="width:100%;height: 30px">
		  		<div style="width:98%;margin-top: 5px;">
			    	<table style="width:100%;">
			    		<tr>
			    			<td>
			    				<span style="background-color: FF0000">&nbsp;&nbsp;</span>
			    				<span>号源已满，不可加号</span>&nbsp;&nbsp;
			    				<span style="background-color: #9400D3">&nbsp;&nbsp;</span>
			    				<span>号源已满,但可加号</span>&nbsp;&nbsp;
			    				<span style="background-color: #98FB98">&nbsp;&nbsp;</span>
			    				<span>医生停诊</span>&nbsp;&nbsp;
			    			</td>
			    		</tr>
			    	</table>
		    	</div>
		    </div>
		    <div data-options="region:'center'"style="width: 100%;height: 90%" >
		    	<div id="scheduleTabId" class="easyui-tabs" data-options="fit:true" data-options="border:true">   
				    <div title="" style="width: 98%;height: 98%">   
				        <table id="infoList" class="easyui-datagrid"  data-options="border:false,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
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
									<th data-options="field:'infoSurplus'" style="width:6%" formatter="formatSurplus" >号数剩余</th>
									<th data-options="field:'stoprEason'" style="width:11%" formatter="formatEason" >停诊原因</th>
								</tr>
							</thead>
						</table> 
				    </div>   
				    <div title="已挂" style="width: 98%;height: 98%">   
				         <table id="infoHisList" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'expxrt'" formatter="functionEmp" style="width:20%">医生名</th>
									<th data-options="field:'grade'" formatter="functionGrade" style="width:20%">级别名</th>
									<th data-options="field:'dept'" formatter="functionDept"  style="width:15%">科室名</th>
									<th data-options="field:'dept'" formatter="functionDept"  style="width:20%">专科</th>
								</tr>
							</thead>
						</table>
				    </div>   
				</div> 
			</div>
		</div>
			<!-- 			<div id="zf"></div> -->
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
									<td style="font-size:14px;background: #E0ECFF;width:25%">支付方式：</td>
									<td width="150px"><input id="payType" name="payType" value ="1" class="easyui-combobox" data-options="required:true" style="width:100px" ></td>
									<td style="font-size:14px;background: #E0ECFF;width:25%">是否购买病历本：</td>
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
										<a href="javascript:save();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">收费</a>
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#windowOpen').window('close')" data-options="iconCls:'icon-cancel'">关闭</a>
									</td>
								</tr>
							</table>
						</div>
					</div>
					 <div id="windowPreregister" class="easyui-window" title="预约查询列表" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:80%;height:50%;">
							<div class="easyui-layout" data-options="fit:true">
							<div data-options="region:'north'" style="width:100%;height:30% ;padding:5px 5px 5px 5px;">
							    <table style="width:100%;border:false;padding:5px;">
									<tr>
										<td nowrap="nowrap">卡号：</td>
										<td  >
											<input class="easyui-textbox" id="priIdcardNo" style="width:100px"  />
										</td>
										<td>
										<td  nowrap="nowrap">&nbsp;&nbsp;预约号：</td>
										<td >
											<input class="easyui-textbox" id="preregisterNos" style="width:100px" />
										</td>
										<td  nowrap="nowrap">&nbsp;&nbsp;姓名：</td>
										<td  >
											<input class="easyui-textbox" id="preregisterName" style="width:100px" />
										</td>
										<td  nowrap="nowrap">&nbsp;&nbsp;证件号：</td>
										<td  >
											<input class="easyui-textbox" id="preregisterCertificatesno" style="width:100px" />
										</td>
										<td  nowrap="nowrap">&nbsp;&nbsp;电话：</td>
										<td  >
											<input class="easyui-textbox" id="preregisterPhone" style="width:100px" />
										</td>
										<td  nowrap="nowrap">&nbsp;&nbsp;日期：</td>
										<td  >
											<input id="preregisterDate" style="width:110px" />
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
												<th data-options="field:'deptName',formatter: function(value,row,index){
															if (row.preregisterDept){
																return row.preregisterDept.deptName;
															} else {
																return value;
															}
														}" style="width:10%" >科室</th>
												<th data-options="field:'jbname',formatter: function(value,row,index){
															if (row.preregisterGrade){
																return row.preregisterGrade.name;
															} else {
																return value;
															}
														}" style="width:10%">级别</th>
												<th data-options="field:'zjname',formatter: function(value,row,index){
															if (row.preregisterExpert){
																return row.preregisterExpert.name;
															} else {
																return value;
															}
														}"  style="width:10%">专家</th>
												<th data-options="field:'midday'" style="width:11%" formatter="formatCheckBox">午别</th>
												<th data-options="field:'preregisterDate'" style="width:10%">预约时间</th>
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