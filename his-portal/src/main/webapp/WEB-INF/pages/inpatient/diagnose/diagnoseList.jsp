<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院诊断</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var inpatientNo1;//就诊卡号
	var medicalrecordId;//病历号 用于双击回显
	var empMap="";
	var payMap=new Map();
	function clearinfo(){
		$('#sex').text("");
		$('#inTime').text("");
		$('#dept').text("");
		$('#name').text("");
		$('#inpatientNo').val("");
	}
   //回车事件
	$(function(){
		
		var deptFlg = "${deptFlg}"
		console.log(deptFlg)
		$('#diagCode').combobox({
		    data:[{"id":1,"text":"ICD10"},{"id":2,"text":"医保"}],    
		    valueField:'id',    
		    textField:'text',
		    required:true,    
		    onSelect:function(record){
		    	$("#icdCode").textbox('setValue','');
		    }
		    
		});
		//诊断医生
		$('#diagDoct').combobox({  
			url:'<%=basePath%>inpatient/InpatientProof/employeeCombobox.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		$('#diagKind').combobox({
		    data:[{"id":1,"text":"主要诊断"},{"id":2,"text":"其他诊断"},{"id":3,"text":"并发症"},{"id":4,"text":"院内感染"},{"id":5,"text":"损伤"},{"id":6,"text":"病理诊断"},{"id":7,"text":"过敏药"},{"id":8,"text":"新生儿疾病"},{"id":9,"text":"新生儿院感 "}],  
		    valueField:'id',    
		    textField:'text',
		    required:true,    
		    editable:true
		});
		bindEnterEvent('diagDoct',popWinToEmployee,'easyui');//绑定回车事件
		bindEnterEvent('admNo',keyupNo,'easyui');
		bindEnterEvent('admNo1',keyupMo,'easyui');
		bindEnterEvent('icdCode',querycode,'easyui');
	});
   $(function(){
	 //渲染医生
		$.ajax({
			url: "<%=basePath%>inpatient/InpatientProof/queryEmpMapPublic.action",
			success: function(empData) {
				empMap = empData;
			}
		});
		$.ajax({
			url:"<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action", 
			data:{"type":"paykind"},
			success: function(data) {
				var payType = data;
				for(var i=0;i<payType.length;i++){
					payMap.put(payType[i].encode,payType[i].name);
				}
			}
		}); 
   });
   
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
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
				$('#admNo1').textbox('setValue',data);
				keyupMo();
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
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) {
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#admNo1').textbox('setValue',data);
					keyupMo();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	//渲染医生
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
     function funDiagkingType(value,row,index){
		   if(value==1){
			   return "主要诊断";
		   }else if(value==2){
			   return "其他诊断";
		   }else if(value==3){
			   return "并发症";
		   }else if(value==4){
			   return "院内感染";
		   }else if(value==5){
			   return "损伤";
		   }else if(value==6){
			   return "病理诊断";
		   }else if(value==7){
			   return "过敏药";
		   }else if(value==8){
			   return "新生儿疾病";
		   }else if(value==9){
			   return "新生儿院感";
		   }
      }
      //加载诊断代码
      function querycode(){
    	  var code=$("#diagCode").combobox("getValue");
    	  if(code=="2"||code=="医保"){
    		  Adddilog("医保诊断代码", "<%=basePath%>inpatient/diagnose/diagnoseCode.action",'selectUser');
    	  }else if(code=="1"||code=="ICD10"){
    		  Adddilog("ICD诊断代码", "<%=basePath%>inpatient/diagnose/diagnoseIcdCode.action",'selectUser');
    	  }else{
    		  $.messager.alert("提示","请先选择ICD代码类别");
    		  setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
    	  }
	     
      }
   //加载dialog
	  function Adddilog(title, url,id) {
		   if(title=="医保诊断代码"){
			   $('#'+id).dialog({    
				    title: title,    
				    width: '35%',    
				    height:'80%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true 
				   });  
		   }else{
			   $('#'+id).dialog({    
				    title: title,    
				    width: '30%',    
				    height:'80%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true 
				   });  
		   }
		
	   }
		//打开dialog
		function openDialog(id) {
			$('#'+id).dialog('open'); 
		}
		//关闭dialog
		function closeDialog(id) {
			$('#'+id).dialog('close');  
		}
	function keyupMo(){
		var main;//获取选中行是否为主诊断
		var noo=$("#admNo1").textbox("getText");
		
		if(noo == ''){
			$.messager.alert('提示','请输入正确的病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			clearinfo();
			$("#editForm").form("clear");
			var row=$("#list").datagrid("getRows");
			for(var i=0;i<row.length;i++){
				index=$("#list").datagrid("getRowIndex",row[i]);
				$("#list").datagrid('deleteRow',index);
			}
			return false;
		}
			$.ajax({
				url: "<%=basePath%>inpatient/diagnose/queryFormBMyIdds.action?no="+noo,
				type:"post",
					success: function(data) {
						clearinfo();
						$("#editForm").form("clear");
						var row=$("#list").datagrid("getRows");
						for(var i=row.length-1;i>=0;i--){
							index=$("#list").datagrid("getRowIndex",row[i]);
							$("#list").datagrid('deleteRow',index);
						}
					    if(data!=null&&data!=""&&data!="error"){
							var info =data;
							queryInfo(info);
						}else{
							$.messager.alert("提示","未查到该患者信息");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							clearinfo();
							$("#editForm").form("reset");
							var row=$("#list").datagrid("getRows");
							for(var i=0;i<row.length;i++){
								index=$("#list").datagrid("getRowIndex",row[i]);
								$("#list").datagrid('deleteRow',index);
							}
						}
					}
				});
	}
	//加载患者信息
	function keyupNo(){
		var main;//获取选中行是否为主诊断
		var noo=$("#admNo").textbox("getText");
		//渲染医生
		$.ajax({
			url: "<%=basePath%>inpatient/InpatientProof/queryEmpMapPublic.action",
			success: function(empData) {
				empMap = empData;
			}
		});
		$.ajax({
			url:"<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action", 
			data:{"type":"paykind"},
			success: function(data) {
				var payType = data;
				for(var i=0;i<payType.length;i++){
					payMap.put(payType[i].encode,payType[i].name);
				}
			}
		});
		if(noo == ''){
			$.messager.alert('提示','请输入正确的就诊卡号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			clearinfo();
			$("#editForm").form("clear");
			var row=$("#list").datagrid("getRows");
			for(var i=0;i<row.length;i++){
				index=$("#list").datagrid("getRowIndex",row[i]);
				$("#list").datagrid('deleteRow',index);
			}
			return false;
		}
			$.ajax({
				url: "<%=basePath%>inpatient/diagnose/queryFormByIdds.action?no="+noo,
				type:"post",
					success: function(data) {
						clearinfo();
						$("#editForm").form("clear");
						var row=$("#list").datagrid("getRows");
						for(var i=0;i<row.length;i++){
							index=$("#list").datagrid("getRowIndex",row[i]);
							$("#list").datagrid('deleteRow',index);
						}
					    if(data!=null&&data!=""&&data!="error"){
							var info = eval("("+data+")");
							var no  = info[0].inpatientNo;
							if(info.length==1){
								if(no!=""){
					 				$.ajax({
										url: "<%=basePath%>inpatient/diagnose/queryInpinfoDiagnose.action?no="+no,
										type:"post",
					 					success: function(data) {
					 						var info = eval("("+data+")");
					 						if(info.id!=null&&info.id!=""){
					 							$("#id").val(info.id);//id号
					 							$("#inpatientNo").val(info.inpatientNo);//住院流水号
					 							$("#inpatient").val(info.inpatientNo);//住院流水号
					 							$("#admNo").textbox("setValue",info.idcardNo);//就诊卡号
					 							$("#admNo1").val(info.medicalrecordId);//病历号
					 							$("#name").textbox("setValue",info.patientName);//姓名
					 							$("#sex").textbox("setValue",info.reportSexName);//性别
					 							$("#inTime").textbox("setValue",info.inDate);//入院时间
					 							$("#dept").textbox("setValue",info.deptName);//科室
					 							var pay = info.paykindCode;
					 							$("#operType").textbox("setValue",payMap.get(info.paykindCode));//结算类别
					 							var inpatientNo = info.inpatientNo;
					 							$('#list').datagrid({
					 							   url:"<%=basePath%>inpatient/diagnose/queryDiagnoseByInpNo.action?menuAlias=${menuAlias}&id="+inpatientNo,
					 							   onDblClickRow: function(rowIndex,rowDate){
					 								  $('#admNo1').textbox('setValue',rowDate);
					 								    $("#detailId").val(rowDate.detailId);
					 							  		$("#inpatient").val($("#inpatientNo").val());
					 							  		$('#diagKind').combobox('setValue',rowDate.diagKind);
														$('#diagDoct').combobox('setValue',rowDate.diagDoct);
														$('#diagDate').val(rowDate.diagDate);
														$('#mainFlay_value').val(rowDate.mainFlay);
														$('#diagCode').combobox('setValue',rowDate.icdCode);
					 									$('#diagCodeb').val(rowDate.icdCode);
					 									$("#icdCode1").val(rowDate.diagCode);
					 									$('#icdCode').textbox('setValue',rowDate.diagName);
					 									if(rowDate.main==1){
															$('#mainFlay').prop("checked",true);
															$('#mainFlay').attr("disabled",false);
														}else{
															$('#mainFlay').prop("checked",false);
														}
					 								 
													},
													onSelect: function(rowIndex,rowDate){
														$('#list').datagrid("unselectRow",rowIndex);
					 									 var row=$("#list").datagrid("getRows");
								 							for(var i=0;i<row.length;i++){
								 								if(row[i].main==1){
															  		 $('#mainFlay').attr("disabled",true);
															  	 }
								 							}
														
													},
					 							  onLoadSuccess:function(data){
					 									 for(var i=0;i<data.total;i++){
														  	 var row=data.rows[i];
															  	 if(row.main==1){
															  		 $('#mainFlay').attr("disabled",true);
															  		 break;
															  	 }
															
													       }
					 								  }
														  
					 							}); 
					 							
					 							
					 						}else{
					 							$.messager.alert("提示","未查询到该就诊卡号信息！");
					 							setTimeout(function(){
					 								$(".messager-body").window('close');
					 							},3500);
					 							$("#editForm").form("reset");
					 							var row=$("#list").datagrid("getRows");
					 							for(var i=0;i<row.length;i++){
					 								index=$("#list").datagrid("getRowIndex",row[i]);
					 								$("#list").datagrid('deleteRow',index);
					 							}
					 						}
					 					}
					 				});
					 			}else{
					 				$.messager.alert("提示","请输入就诊卡号!");
					 				setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$("#editForm").form("reset");
									var row=$("#list").datagrid("getRows");
									for(var i=0;i<row.length;i++){
										index=$("#list").datagrid("getRowIndex",row[i]);
										$("#list").datagrid('deleteRow',index);
									}
					 			}
							}else if(info.length>1){
								$("#diaInpatient").window('open');
								$("#infoDatagrid").datagrid({
									url: "<%=basePath%>inpatient/diagnose/queryFormByIdds.action?menuAlias=${menuAlias}&no="+noo,
								    columns:[[    
								        {field:'inpatientNo',title:'住院流水号',width:'30%',align:'center'} ,    
								        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
								        {field:'reportSexName',title:'性别',width:'10%',align:'center'} ,
								        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
								        {field:'certificatesNo',title:'身份证号',width:'30%',align:'center'} 
								    ]] ,
								    onDblClickRow:function(rowIndex, rowData){
								    	$("#id").val(rowData.id);//id号
			 							$("#inpatientNo").val(rowData.inpatientNo);//住院流水号
			 							$("#inpatient").val(rowData.inpatientNo);//住院流水号
			 							$("#admNo").textbox("setValue",rowData.idcardNo);//就诊卡号
			 							$("#admNo1").val(rowData.medicalrecordId);//病历号
			 							$("#name").textbox("setValue",rowData.patientName);//姓名
			 							$("#sex").textbox("setValue",rowData.reportSexName);//性别
			 							$("#inTime").textbox("setValue",rowData.inDate);//入院时间
			 							$("#dept").textbox("setValue",rowData.deptName);//科室
			 							$("#operType").textbox("setValue",payMap.get(rowData.paykindCode));//结算类别
			 							var inpatientNo = rowData.inpatientNo;
			 							$("#diaInpatient").window('close');
			 							$('#list').datagrid({
			 							   url:"<%=basePath%>inpatient/diagnose/queryDiagnoseByInpNo.action?menuAlias=${menuAlias}&id="+inpatientNo,
			 							  onDblClickRow: function(rowIndex,rowDate){
			 								 var row=$("#list").datagrid("getRows");
					 							for(var i=0;i<row.length;i++){
					 								if(row[i].mainFlay==1){
												  		 $('#mainFlay').attr("disabled",true);
												  	 }
					 							}
			 							  		$("#detailId").val(rowDate.detailId);
			 							  		$("#inpatient").val($("#inpatientNo").val());
			 							  		$('#diagKind').combobox('setValue',rowDate.diagKind);
												$('#diagDoct').combobox('setValue',rowDate.diagDoct);
												$('#diagDate').val(rowDate.diagDate);
												$('#mainFlay_value').val(rowDate.mainFlay);
												$('#diagCode').combobox('setValue',rowDate.icdCode);
			 									$('#diagCodeb').val(rowDate.icdCode);
			 									$("#icdCode1").val(rowDate.diagCode);
			 									$('#icdCode').textbox('setValue',rowDate.diagName);
			 									if(rowDate.main==1){
													$('#mainFlay').prop("checked",true);
													$('#mainFlay').attr("disabled",false);
												}else{
													$('#mainFlay').prop("checked",false);
												}
											},
											onSelect: function(rowIndex,rowDate){
												$('#list').datagrid("unselectRow",rowIndex);
			 									 var row=$("#list").datagrid("getRows");
						 							for(var i=0;i<row.length;i++){
						 								if(row[i].main==1){
													  		 $('#mainFlay').attr("disabled",true);
													  	 }
						 							}
												
											},
			 							  onLoadSuccess:function(data){
			 									 for(var i=0;i<data.total;i++){
												  	 var row=data.rows[i];
													  	 if(row.main==1){
													  		 $('#mainFlay').attr("disabled",true);
													  		 break;
													  	 }
													  
											       }
			 								  }
			 							}); 
								    }
								});
							}
							queryInfo(info);
						}else{
							$.messager.alert("提示","未查到该患者信息");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							clearinfo();
							$("#editForm").form("reset");
							var row=$("#list").datagrid("getRows");
							for(var i=0;i<row.length;i++){
								index=$("#list").datagrid("getRowIndex",row[i]);
								$("#list").datagrid('deleteRow',index);
							}
						}
					}
				});
	}
	function queryInfo(info){
		var no  = info[0].inpatientNo;
		if(info.length==1){
			if(no!=""){
 				$.ajax({
					url: "<%=basePath%>inpatient/diagnose/queryInpinfoDiagnose.action?no="+no,
					type:"post",
 					success: function(data) {
 						var info =data;
 						if(info.id!=null&&info.id!=""){
 							$("#id").val(info.id);//id号
 							$("#inpatientNo").val(info.inpatientNo);//住院流水号
 							$("#inpatient").val(info.inpatientNo);//住院流水号
 							$("#admNo").textbox("setValue",info.idcardNo);//就诊卡号
 							$("#admNo1").textbox("setValue",info.medicalrecordId);//病历号
 							medicalrecordId=info.medicalrecordId;//用于保存后双击回显
 							inpatientNo1=info.idcardNo;
 							$("#name").text(info.patientName);//姓名
 							$("#sex").text(info.reportSexName);//性别
 							$("#inTime").text(info.inDate);//入院时间
 							$("#dept").text(info.deptName);//科室
 							var pay = info.paykindCode;
 							$("#operType").val(payMap.get(info.paykindCode));//结算类别
 							var inpatientNo = info.inpatientNo;
 							$('#list').datagrid({
 							   url:"<%=basePath%>inpatient/diagnose/queryDiagnoseByInpNo.action?menuAlias=${menuAlias}&id="+inpatientNo,
 							   onDblClickRow: function(rowIndex,rowDate){
 								  $("#admNo").textbox("setValue",inpatientNo1);//就诊卡号
 		 							$("#admNo1").textbox("setValue",medicalrecordId);//病历号
 								   $("#detailId").val(rowDate.detailId);
 							  		$("#inpatient").val($("#inpatientNo").val());
 							  		$('#diagKind').combobox('setValue',rowDate.diagKind);
									$('#diagDoct').combobox('setValue',rowDate.diagDoct);
									$('#diagDate').val(rowDate.diagDate);
									$('#mainFlay_value').val(rowDate.mainFlay);
									$('#diagCode').combobox('setValue',rowDate.icdCode);
 									$('#diagCodeb').val(rowDate.icdCode);
 									$("#icdCode1").val(rowDate.diagCode);
 									$('#icdCode').textbox('setValue',rowDate.diagName);
 									if(rowDate.main==1){
										$('#mainFlay').prop("checked",true);
										$('#mainFlay').attr("disabled",false);
									}else{
										$('#mainFlay').prop("checked",false);
									}
								},
								onSelect: function(rowIndex,rowDate){
									$('#list').datagrid("unselectRow",rowIndex);
 									 var row=$("#list").datagrid("getRows");
			 							for(var i=0;i<row.length;i++){
			 								if(row[i].main==1){
										  		 $('#mainFlay').attr("disabled",true);
										  	 }
			 							}
									
								},
 							  onLoadSuccess:function(data){
 									 for(var i=0;i<data.total;i++){
									  	 var row=data.rows[i];
										  	 if(row.main==1){
										  		 $('#mainFlay').attr("disabled",true);
										  		 break;
										  	 }
										
								       }
 								  }
 							}); 
 						}else{
 							$.messager.alert("提示","未查询到该就诊卡号信息！");
 							setTimeout(function(){
 								$(".messager-body").window('close');
 							},3500);
 							clearinfo();
 							$("#editForm").form("reset");
 							var row=$("#list").datagrid("getRows");
 							for(var i=0;i<row.length;i++){
 								index=$("#list").datagrid("getRowIndex",row[i]);
 								$("#list").datagrid('deleteRow',index);
 							}
 						}
 					}
 				});
 			}else{
 				$.messager.alert("提示","请输入就诊卡号!");
 				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
 				clearinfo();
				$("#editForm").form("reset");
				var row=$("#list").datagrid("getRows");
				for(var i=0;i<row.length;i++){
					index=$("#list").datagrid("getRowIndex",row[i]);
					$("#list").datagrid('deleteRow',index);
				}
 			}
		}else if(info.length>1){
			$("#diaInpatient").window('open');
			$("#infoDatagrid").datagrid({
				data:info,
			    columns:[[    
			        {field:'inpatientNo',title:'住院流水号',width:'30%',align:'center'} ,    
			        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
			        {field:'reportSexName',title:'性别',width:'10%',align:'center'} ,
			        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
			        {field:'certificatesNo',title:'身份证号',width:'30%',align:'center'} 
			    ]] ,
			    onDblClickRow:function(rowIndex, rowData){
			    	$("#id").val(rowData.id);//id号
						$("#inpatientNo").val(rowData.inpatientNo);//住院流水号
						$("#inpatient").val(rowData.inpatientNo);//住院流水号
						$("#admNo").textbox("setValue",rowData.idcardNo);//就诊卡号
						$("#admNo1").textbox("setValue",rowData.medicalrecordId);//病历号
						$("#name").text(rowData.patientName);//姓名
						$("#sex").text(rowData.reportSexName);//性别
						$("#inTime").text(rowData.inDate);//入院时间
						$("#dept").text(rowData.deptName);//科室
						$("#operType").val(payMap.get(rowData.paykindCode));//结算类别
						var inpatientNo = rowData.inpatientNo;
						$("#diaInpatient").window('close');
						$('#list').datagrid({
						   url:"<%=basePath%>inpatient/diagnose/queryDiagnoseByInpNo.action?menuAlias=${menuAlias}&id="+inpatientNo,
						  onDblClickRow: function(rowIndex,rowDate){
							 var row=$("#list").datagrid("getRows");
 							for(var i=0;i<row.length;i++){
 								if(row[i].mainFlay==1){
							  		 $('#mainFlay').attr("disabled",true);
							  	 }
 							}
						  		$("#detailId").val(rowDate.detailId);
						  		$("#inpatient").val($("#inpatientNo").val());
						  		$('#diagKind').combobox('setValue',rowDate.diagKind);
							$('#diagDoct').combobox('setValue',rowDate.diagDoct);
							$('#diagDate').val(rowDate.diagDate);
							$('#mainFlay_value').val(rowDate.mainFlay);
							$('#diagCode').combobox('setValue',rowDate.icdCode);
								$('#diagCodeb').val(rowDate.icdCode);
								$("#icdCode1").val(rowDate.diagCode);
								$('#icdCode').textbox('setValue',rowDate.diagName);
								if(rowDate.main==1){
								$('#mainFlay').prop("checked",true);
								$('#mainFlay').attr("disabled",false);
							}else{
								$('#mainFlay').prop("checked",false);
							}
						},
						onSelect: function(rowIndex,rowDate){
							$('#list').datagrid("unselectRow",rowIndex);
								 var row=$("#list").datagrid("getRows");
	 							for(var i=0;i<row.length;i++){
	 								if(row[i].main==1){
								  		 $('#mainFlay').attr("disabled",true);
								  	 }
	 							}
							
						},
						  onLoadSuccess:function(data){
								 for(var i=0;i<data.total;i++){
							  	 var row=data.rows[i];
								  	 if(row.main==1){
								  		 $('#mainFlay').attr("disabled",true);
								  		 break;
								  	 }
						       }
							  }
						}); 
			    }
			});
		}
	}
	//表单提交
	function onsubmit(){ 
		var inpatientNo =$("#inpatientNo").val();
		if($("#detailId").val()!=""&&$("#detailId").val()!=null){
			if($('#mainFlay').prop("checked")){//主诊断
	    		$('#mainFlay_value').val("1");
	    	}else {
	    		$('#mainFlay_value').val("0");
	    	}
			 var diagCode=$('#diagCode').combobox('getText');
			 $.messager.progress({text:"保存中，请稍候...", modal:true});
		     $('#editForm').form('submit',{  
		        url:"<%=basePath%>inpatient/diagnose/saveInpinfoOut.action?diagCode="+diagCode,  
		        onSubmit:function(){ 
     	  		 	if(!$('#editForm').form('validate')){
     					$.messager.show({  
     					     title:'提示信息' ,   
     					     msg:'验证没有通过,不能提交表单!'  
     					}); 
         				$.messager.progress('close');
     					   return false ;
     			     }
     			 }, 
	     			success:function(){
	     				$.messager.progress('close');
	     				$.messager.alert("提示","保存成功");
	     				setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
     					$("#editForm").form("reset");
     					$('#list').datagrid('reload'); 
	     			  	
	     			 },
	     			error:function(data){
	     				$.messager.progress('close');
	     				$.messager.alert("提示","保存失败");
	     			}
	     	  	}); 
			   
		}else{
			 if($('#mainFlay').prop("checked")){//是否主诊断
		    		$('#mainFlay_value').val("1");
		    	}else {
		    		$('#mainFlay_value').val("0");
		    	}
				var diagCode=$('#diagCode').combobox('getText');
				 $.messager.progress({text:"保存中，请稍候...", modal:true});
			     $('#editForm').form('submit',{  
			        url:"<%=basePath%>inpatient/diagnose/saveInpinfoOut.action?diagCode="+diagCode,
		     			 onSubmit:function(){ 
		     	  		 	if(!$('#editForm').form('validate')){
		     					$.messager.show({  
		     					     title:'提示信息' ,   
		     					     msg:'验证没有通过,不能提交表单!'  
		     					}); 
			     				$.messager.progress('close');
	     					    return false ;
		     			     }
		     			 }, 
		     			success:function(){
		     				$.messager.progress('close');
		     				$.messager.alert("提示","保存成功");
		     				setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
		     					$("#editForm").form("reset");
		     					$('#list').datagrid('reload'); 
		     			  	
		     			 },
		     			error:function(data){
		     				$.messager.progress('close');
		     				$.messager.alert("提示","保存失败");
		     			}
		     	  	});
		
		}
		
    }	    
	 //复选框
    function funcIsabOutChildeen(value,row,index){
		if(value=="1"){
			 return "<input type='checkbox' disabled='disabled' checked='checked'>";
		}else{
			 return '<input type="checkbox" disabled="disabled">';
		}
	
	 }
	//清空
	function clear(){
		$("#editForm").form("reset");
		 var row=$("#list").datagrid("getRows");
			for(var i=0;i<row.length;i++){
				if(row[i].mainFlay==1){
			  		 $('#mainFlay').attr("disabled",true);
			  	 }
			}
	}
	//清除页面填写信息
	function clearqy(){
		clearinfo();
		$("#editForm").form("reset");
		var rows=$("#list").datagrid("getRows");
		var l = rows.length;
		for(var i=0;i<l;i++){
			$("#list").datagrid("deleteRow",$("#list").datagrid("getRowIndex",rows[0]));
		}
	}
	/**
	   * 回车弹出诊断医师弹框
	   * @author  zhuxiaolu
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToEmployee(){
		    var deptid=$('#dept').val();
	    	var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=diagDoct&employeeType=1&deptIds="+deptid;
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
			170)+',scrollbars,resizable=yes,toolbar=yes')
	 }
	
	  function maincheck(){
		var flag =$('#mainFlay_value').val();
		$.messager.confirm('确认','确定修改主诊断吗？',function(r){
			if(r){
				if($('#mainFlay').prop("checked")){//主诊断
		    		$('#mainFlay_value').val("1");
		    	}else {
		    		$('#mainFlay_value').val("0");
		    	}
			}else{
				if(flag=='1'){
					$('#mainFlay').prop("checked",true);
					$('#mainFlay_value').val("1");
				}else{
					$('#mainFlay').prop("checked",false);
					$('#mainFlay_value').val("0");
				}
			}
		});
	  }
		/**  
		 *  
		 * @Description：过滤	
		 * @Author：zhuxiaolu
		 * @CreateDate：2016-11-1
		 * @version 1.0
		 * @throws IOException 
		 *
		 */ 
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
	</script>	
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div id="tt"  region="north" border="false" style="height: 23%">
			<form id="editForm" method="post" >
				<table class="honry-table" cellpadding="1" cellspacing="1" style="width: 100%;border-bottom-width: 0px;border-top:0">
		    		<tr>
		    			<td style="font: 25px;text-align: left;font-weight: bold;" colspan="6">
		    			<shiro:hasPermission name="${menuAlias}:function:query">
						&nbsp;<a href="javascript:void(0)" onclick="keyupMo()" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;
						</shiro:hasPermission>
			    		<shiro:hasPermission name="${menuAlias}:function:readCard">
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;
						</shiro:hasPermission>
			        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        			<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>&nbsp;
						</shiro:hasPermission>
		    			<shiro:hasPermission name="${menuAlias}:function:save">
							<a href="javascript:onsubmit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;
						</shiro:hasPermission>
							<a href="javascript:clearqy();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清空</a>
		    		</td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable"  style="font: 14px;text-align: right;width: 15%;">病历号：</td>
		    			<td style="width: 18%">
		    				<input class="easyui-textbox" id="admNo1" name="medicalrecordId" data-options="prompt:'输入病历号回车查询',required:true">
		    			</td>
		    			<td  class="honry-lable"  style="text-align:right; width: 15%;">就诊卡号:</td>
		    			<td style="width: 18%"><input type="hidden" id="id" ><input type="hidden" id="inpatientNo" >
		    				<input id="admNo" name="medicalrecordId1" class="easyui-textbox" data-options="prompt:'输入就诊卡号回车查询',required:true" /></td>
		    			<td class="honry-lable"  style="font: 14px;text-align: right;width: 15%;">姓名：</td>
		    			<td  style="width: 18%" id="name"></td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable"  style="font: 14px;text-align: right;width: 15%;">性别：</td>
		    			<td style="width: 18%" id="sex"></td>
		    			<td class="honry-lable" style="text-align: right;width: 15%;">入院时间：</td>
		    			<td style="width: 18%" id="inTime"></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">住院科室：</td>
		    			<td style="width: 18%" id="dept"><input id="operType" type="hidden"/></td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">诊断类型：</td>
		    			<td style="width: 18%"><input type="hidden" id="detailId" name="id"><input type="hidden" id="inpatient" name="inpatientNo">
		    			<input id="diagKind" class="easyui-combobox" name="diagKind" data-options="required:true"></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">诊断医师：</td>
		    			<td style="width: 18%"><input  id="deptId"  value="${deptId}" type="hidden"/>
		    			<input id="diagDoct" name="doctName" data-options="required:true" value="${userId }"/>
		    			<a href="javascript:delSelectedData('diagDoct');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">诊断日期：</td>
		    			<td style="width: 18%">
		    			<input id="diagDate" name="diagDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+7}'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    			  </td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">是否主诊断：</td>
		    			<td style="width: 18%">
		    				<input type="hidden" value="0" name="mainFlay" id="mainFlay_value" /> 
		    				<input type="checkbox"  id="mainFlay" onclick="maincheck()">
		    			</td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">ICD代码类别：</td>
		    			<td style="width: 18%"><input type="hidden" name="diagCode" id="diagCodeb">
		    			<input id="diagCode" class="easyui-combobox"  data-options="required:true"/>
		    			</td>
		    			 <td class="honry-lable" style="font: 14px;text-align: right;width: 15%;">诊断代码：</td>
		    			 <input id="diagName" name="diagName" type="hidden" >
		    			 <input id="icdCode1" name="icdCode" type="hidden" >
		    			<td style="width: 18%"><input class="easyui-textbox" id="icdCode" name="icdCode1" value="${icdCode}" data-options="prompt:'回车选择诊断代码',required:true">
		    			</td>
		    		</tr>
		    	</table>
			</form>
		</div>
		<div data-options="region:'center',border:false" style="height:50%;width: 100%">
			<table id="list" class="easyui-datagrid"  style="width: 100%;text-align: center;margin-top: 5px " data-options="fit:true,idField:'id',method:'post',fitColumns:true,singleSelect:true,checkOnSelect:false,selectOnCheck:false">
				<thead >
					<tr>
						<th data-options="field:'diagKind',formatter: funDiagkingType" align="center" style="font: 14px;width: 15%;">诊断类型</th>
						<th data-options="field:'diagName'" align="center" style="font: 14px;width: 15%">诊断名称</th>
						<th data-options="field:'diagDoct',formatter:functionEmp" align="center" style="font: 14px;width: 15%">诊断医师</th>
						<th data-options="field:'diagDate'" align="center" style="font: 14px;width: 15%">诊断时间</th>
						<th data-options="field:'icdCode'" align="center" style="font: 14px;width: 15%">ICD类别</th>
						<th data-options="field:'mainFlay',formatter: funcIsabOutChildeen"  align="center" style="font: 14px;width: 15%" >是否主诊断</th>
						<th data-options="field:'main'" hidden='true' align="center"  style="font: 14px;width: 15%" >是否主诊断</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="selectUser"></div>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 40 40 40" data-options="modal:true, closed:true">   
	   	<table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true,checkOnSelect:false,selectOnCheck:false">   
		</table>  
   	</div>
</body>
</html>