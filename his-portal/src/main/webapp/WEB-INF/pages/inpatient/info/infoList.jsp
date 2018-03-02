<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院登记</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/>
<style type="text/css">
	#AddInpatientInfo span.textbox .textbox-text {
		height: 20px;
	}
</style>
<script type="text/javascript">
	var deptMap = null;//科室
	var relationMap=null;//关系
	var nationalityMap=null;//民族
	var occupationMap=null;//职业
	var countryMap=null;//国籍
	var cityMap="";
	var yanzhengjieguo=null;
	var sexMap=new Map();
	var flag=false;
	$(function(){
		/**
		 *  
		 * @Description：担保类型
		 * @Author：涂川江
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$('#bingqu').combobox({disabled:true});
		$('#suretyType').combobox({    
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=suretytype",
			valueField:'encode',    
			textField:'name',
		});
		
		$('#paykind').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=paykind",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
	
		 $('#emplCode').combobox({
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
				},
				onSelect:function(data){
			    	$('#emplName').val($('#emplCode').combobox('getText'));
		    	}
			});
		if($("#medicalrecordId").textbox('getValue')==null||$("#medicalrecordId").textbox('getValue')==""){
			$("#patientName").readonly=true;
		}
		//出生日期与年龄的联动
	$('#birDate').blur(function(){
		       var date=$('#birDate').val();
		       var arr=date.split("-");
				var y =arr[0];
				var m =arr[1];
				var d =arr[2];
				var agedate=DateOfBirth(y+"-"+m+"-"+d);
				if(agedate.get("nianling")=="0"){
					$('#reportAge').textbox('setValue',"0");	
				}else{
					$('#reportAge').textbox('setValue',agedate.get("nianling"));	
				}
				$('#reportAgeunit').text(agedate.get('ageUnits'));
				$('#reportAgeunit1').val(agedate.get('ageUnits'));
		});
		bindEnterEvent('bingqu',popWinToBingQu,'easyui');//绑定回车事件
		//住院科室(下拉框)
		$('#reportDept').combobox({   
			url: "<%=basePath%>inpatient/InpatientProof/queryInHosDept.action",  
		    valueField:'deptCode',    
		    textField:'deptName',
		    mode:'remote',
		    editable:true,
		    onSelect:function(data){
		    	$('#reportDeptName').val($('#reportDept').combobox('getText'));
		    	$('#bingqu').combobox('setValue',"");
		    	$('#bingqu').combobox('reload',"<c:url value='/inpatient/InpatientProof/querybingqu.action'/>?departmentCode="+data.deptCode);
		    	$('#bingqu').combobox({disabled:false});
		    },
			onLoadSuccess:function(){
				 var data = $('#reportDept').combobox('getData');
	             if (data.length == 1) {
	                 $('#reportDept').combobox('setValue', data[0].deptCode);
	                 $('#bingqu').combobox('setValue',"");
	 		    	 $('#bingqu').combobox('reload',"<c:url value='/inpatient/InpatientProof/querybingqu.action'/>?departmentCode="+data[0].deptCode);
	 		    	 $('#bingqu').combobox({disabled:false});
	             }
		    }
		});

		bindEnterEvent('reportDept',popWinToDept,'easyui');//绑定回车事件
		//患者状态下拉框(1:病危，2：病重，3：手术，4：常规)
		$('#patientStatus').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=status ",
			async:false,
			type:'post',
		    valueField:'encode',    
		    textField:'name',
		    multiple:false
		});
		/**
		 * 回车弹出国籍选择窗口
		 * @author  wanxing
		 * @date 2016-03-22 17:20
		 * @version 1.0
		 */
		 bindEnterEvent('country',popWinToCountry,'easyui');//绑定回车事件
		
		 
	 /**
		 * 回车弹出民族选择窗口
		 * @author  wanxing
		 * @date 2016-03-22  14:38
		 * @version 1.0
		 */
		 bindEnterEvent('nation',popWinToNationality,'easyui');//绑定回车事件
		 
	 /**
		 * 回车弹出职业选择窗口
		 * @author  wanxing
		 * @date 2016-03-22  17:32
		 * @version 1.0
		 */
		 bindEnterEvent('Occupation',popWinToOccupation,'easyui');//绑定回车事件
		 
		/**
		 * 回车弹出联系人关系选择窗口
		 * @author  wanxing
		 * @date 2016-03-22  17:40
		 * @version 1.0
		 */
		 bindEnterEvent('Relation',popWinToRelation,'easyui');//绑定回车事件
		
			bindEnterEvent('medicalrecordId',queryMedicalrecordId,'easyui');//回车方法
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
				$('#medicalrecordId').textbox('setValue',data);
				queryMedicalrecordId();
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
						$('#medicalrecordId').textbox('setValue',data);
						queryMedicalrecordId();
					}
			 });
		};
	/*******************************结束读身份证***********************************************/
	
	function popWinToOccupation(){
		$('#Occupation').combobox('setValue','');
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=occupation&textId=Occupation";
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToCountry(){
		 	$('#country').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=country&textId=country";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToNationality(){
		$('#nation').combobox('setValue','');
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=nationality&textId=nation";
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToRelation(){
			$('#Relation').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=relation&textId=Relation";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	
	//打开病床选择界面
	function openBedWin(){
		$('#bedIdNum').textbox('setValue',"");
		if(reportBedwardId!=null&&reportBedwardId!=""){
			var tempWinPath = "<%=basePath%>inpatient/info/bedInfoList.action?reportBedwardId="+reportBedwardId;
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
		}else{
			$.messager.alert('提示','请先选择病区');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
		/**
		*提交数据 保存到InpatientInfo、预交金表、担保金表、中
		**/
		function submit(flg){
			if($('#prepayCost').textbox('getValue')!=''){
				var flg=$("#invoiceNoflay").val();
				if(flg=="error"){
					$.messager.alert("操作提示", "请领取预交收据发票,领取后请刷新页面");
					return;
			 	}
			}
			if($('#inPatientDate').val()==''||$('#inPatientDate').val()==null){
				$.messager.alert('提示','请选择入院时间！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
			//验证身份证号的格式
			var patientCertificatestype=$('#certificatesType').val();
			var idcardno=$('#idcard').textbox('getValue');
			if(idcardno!=null&&idcardno!=""){
				if(!isIdCardNo(idcardno)){
					return;
				}
				$('#idcard1').val(idcardno);
				$('#certificatesType').val('3');
			}
			if($('#paykind').combobox('getValue')==02){
				if($('#mcardNo').textbox('getValue')==''){
					$.messager.alert('提示','请填写医疗证号');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}
			
			//填写担保金，则担保人担保类型必填
			if($('#suretyCost').numberbox('getValue')!=''){
				if($('#suretyName').textbox('getValue')==''||$('#suretyType').combobox('getValue')==''){
					$.messager.alert('提示','请将担保人和担保类型填写完整');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}
			if($('#suretyName').textbox('getValue')!=''||$('#suretyType').combobox('getValue')!=''){
				if($('#suretyCost').numberbox('getValue')==''){
					$.messager.alert('提示','请将担保人、担保类型和担保金额信息填写完整');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}
			if($('#babyFlag').is(':checked')){
				$('#babyFlag').val(1);
			}else{
				$('#babyFlag').val(0);
			}
			if($('#haveBabyFlag').is(':checked')){
				$('#haveBabyFlag').val(1);
			}else{
				$('#haveBabyFlag').val(2);
			}
			var io=$('#medicalrecordId').val();
			var menzhen=$('#menzhen').textbox('getValue');
			if(menzhen==null||menzhen==""){
				$.messager.alert('提示','请填写完整数据');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return ;
			}
			//验证页面数据输入格式长度
			comparedata();
			//判断是否动过了页面验证
			if(yanzhengjieguo==1){
				
				 $('#bingquName').val($('#bingqu').combobox('getText'));
				 $.messager.progress({text:"保存中,请稍等......",modal:true});
				//提交表单
				$('#AddInpatientInfo').form('submit',{
			  		url:"<%=basePath%>inpatient/info/editInpatientInfo.action",
			  		 onSubmit:function(){ 
			  		 	if(!$('#AddInpatientInfo').form('validate')){
							$.messager.show({  
							     title:'提示信息' ,   
							     msg:'验证没有通过,不能提交表单!'  
							}); 
							  $.messager.progress('close');
							   return false ;
					     }
					 },  
					 success:function(data){
						if(data=="success"){
							$.messager.progress('close');
							$.messager.alert('提示','保存成功');
	 						setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$("#AddInpatientInfo").form('clear');
							$('#medicalrecordId').textbox('setValue','');
							$('#fangfeijiange').textbox('setValue','1');
							$('#aSourse').combobox('setValue','1');
							$('#Sourse').combobox('setValue','01');
					   	}
					 },
					 error:function(date){
						$.messager.alert('提示','保存失败');
					}
			  	});
			}
		}
   	//回车查询显示
	function queryMedicalrecordId(){
 		var pid=$('#medicalrecordId').val();
		if(pid == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(flag==false){
			//出生地
			$.ajax({
				url: "<c:url value='/inpatient/prepayin/queryCity.action'/>",
				async:false,
				type:'post',
				success: function(data) {
					cityMap = data;
				}
			});
			//病区(下拉框)
			$('#bingqu').combobox({   
				url: "<%=basePath%>inpatient/InpatientProof/querybingqu.action",
				async:false,
				mode:'remote',
				editable:true,
			    valueField:'deptCode',    
			    textField:'deptName'
			});
			//性别渲染
			$.ajax({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
				async:false,
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			});
			flag = true;
		}
		var sourse = $('#Sourse').combobox('getValue');
		if(sourse=='01' || sourse=='02'){
			$.messager.progress({text:'查询中，请稍后...',modal:true});
			$.ajax({
				  url:'<%=basePath%>inpatient/info/getdengjiInfo.action?medicalrecordId='+pid,
				  success:function(data){ 
					  $.messager.progress('close');
					 var plist=data;
					 if(plist.length>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							url:'<%=basePath%>inpatient/info/getdengjiInfo.action?menuAlias=${menuAlias}&medicalrecordId='+pid,
							    columns:[[    
							        {field:'medicalrecordId',title:'病历号',width:'24%',align:'center'} ,  
							        {field:'reportSex',title:'性别',width:'10%',align:'center',formatter:function (value,row,index){return sexMap.get(value);}} ,
							        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
							        {field:'reportRemark',title:'年龄',width:'20%',align:'center'},
							        {field:'idcardNo',title:'门诊号',width:'25%',align:'center'} 
							    ]] ,
							    onDblClickRow:function(rowIndex, rowData){
									$("#diaInpatient").window('close');
							    	searchValue(rowData.medicalrecordId,rowData.unit,rowData.certificatesNo,rowData.idcardNo); 
							    }
							});
					  }else if(plist.length==0){
							$.messager.alert('提示','该患者没有开立住院证明,不能进行住院登记');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
					  }else if(plist.length==1){
						  var io=plist[0].medicalrecordId;
						  searchValue(io,plist[0].unit,plist[0].certificatesNo,plist[0].idcardNo);  
					  }
					}
			 });
		}else if(sourse=='03'){
			$.messager.progress({text:'查询中，请稍后...',modal:true});
			$.ajax({
				url:'<%=basePath%>inpatient/info/getPatientInfo.action?medicalrecordId='+pid,
				success:function(data){
					 $.messager.progress('close');
					 var pInfo = data;
					 if(pInfo.length==0){
						 $.messager.alert('提示','请核对病历号！');
						 setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
					 }else{
						 $.ajax({
							 url:"<%=basePath%>inpatient/info/queryMedicalrecordIdDate.action?medicalrecordId="+pInfo[0].medicalrecordId,
							 async:false,
							 success:function(data){
								 var dataMap = eval("("+data+")");
								 if(dataMap.resMsg=="R"){
			  						 $.messager.alert('提示',dataMap.resCode);
			  						 setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
			  						 $("#AddInpatientInfo").form('clear');
									 $('#medicalrecordId').textbox('setValue','');
									 $('#fangfeijiange').textbox('setValue','1');
									 $('#aSourse').combobox('setValue','1');
								     $('#Sourse').combobox('setValue','01');
			  						 result=false;
			  					 }else if(dataMap.resMsg=='I'){
			  						 $.messager.alert('提示',dataMap.resCode);
			  						 setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
			  						 $("#AddInpatientInfo").form('clear');
									 $('#medicalrecordId').textbox('setValue','');
									 $('#fangfeijiange').textbox('setValue','1');
									 $('#aSourse').combobox('setValue','1');
								     $('#Sourse').combobox('setValue','01');
								     result=false;
			  					 }else if(dataMap.resMsg=='B'){
			  						 $.messager.alert('提示',dataMap.resCode);
			  						 setTimeout(function(){
									 	$(".messager-body").window('close');
									 },3500);
			  						 $("#AddInpatientInfo").form('clear');
									 $('#medicalrecordId').textbox('setValue','');
									 $('#fangfeijiange').textbox('setValue','1');
									 $('#aSourse').combobox('setValue','1');
								     $('#Sourse').combobox('setValue','01');
								     result=false;
			  					 }else if(dataMap.resMsg=='P'){
			  						 $.messager.alert('提示',dataMap.resCode);
			  						 setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
			  						 $("#AddInpatientInfo").form('clear');
									 $('#medicalrecordId').textbox('setValue','');
									 $('#fangfeijiange').textbox('setValue','1');
									 $('#aSourse').combobox('setValue','1');
								     $('#Sourse').combobox('setValue','01');
								     result=false;
			  					 }else{
			  						$.ajax({//根据病历号查询住院次数
								  		url:"<%=basePath%>inpatient/info/queryCount.action?medicalrecordId="+pInfo[0].medicalrecordId,
								  		async:false,		
								  		success:function(dataNum){
								  			 num=dataNum;
								  			 if(num<10){
								  				 num="0"+num;
								  			 }
								  		}		
								  });
								 var inpat=pInfo[0].medicalrecordId.slice(-8);
								 var unit = pInfo[0].unit;
								 var payKind ='';
								 $.ajax({
									 url:'<%=basePath%>inpatient/info/getPaykind.action?unit='+unit,
									 async:false,	 
									 success:function(data){
									    payKind = data;
									 }
								 });
							     	$("#inpatientNo").val("ZY"+num+inpat);
							     	$('#medicalrecordIdNo').val(pInfo[0].medicalrecordId);
							   		$('#pactCode').val(pInfo[0].unit);
									$('#medreID').textbox('setValue',pInfo[0].medicalrecordId);
									//姓名
									$('#patientName').textbox('setValue',pInfo[0].patientName);
									//性别
									$('#sexCombobox').combobox('setValue',pInfo[0].patientSex);
									//结算方式
									$('#paykind').combobox('setValue',payKind);
								    //生日
									$('#birDate').val(pInfo[0].patientBirthday);
									var ages=DateOfBirth(pInfo[0].patientBirthday);
									 //年龄
									 if(ages.get("nianling")=="0"){
										$('#reportAge').textbox('setValue',"0");

									 }else{
										$('#reportAge').textbox('setValue',ages.get("nianling"));

									 }
									$('#reportAgeunit').text(ages.get('ageUnits'));
									$('#reportAgeunit1').val(ages.get('ageUnits'));
									//就诊卡号
									$('#idcardNo').val(pInfo[0].cardNo);
									//证件类型
									$('#certificatesType').val(pInfo[0].patientCertificatestype);
									var type=$('#certificatesType').val();
									if(type=="3"){
										//证件号码
										$('#idcard').textbox('setValue',pInfo[0].patientCertificatesno);
										$('#idcard1').val(pInfo[0].patientCertificatesno);
									}else{
										//证件号码
										$('#idcard').textbox('setValue',null);
										$('#idcard1').val(pInfo[0].patientCertificatesno);
									}
									//婚姻状况
									$('#marry').combobox('setValue',pInfo[0].patientWarriage);
									//国籍
									$('#country').combobox('setValue',pInfo[0].patientNationality);
									//民族
									$('#nation').combobox('setValue',pInfo[0].patientNation);
									//籍贯
									$('#nativeplace').textbox('setValue',pInfo[0].patientNativeplace);
									//职业
									$('#Occupation').combobox('setValue',pInfo[0].patientOccupation);
									//联系人
									$('#relat').textbox('setValue',pInfo[0].patientLinkman);
									//关系
									$('#Relation').combobox('setValue',pInfo[0].patientLinkrelation);
									//出生地
									$('#birFrom').textbox('setValue',cityMap[pInfo[0].patientBirthplace]);
									$('#birFrom1').val(pInfo[0].patientBirthplace);
									//家庭地址
									$('#address').textbox('setValue',pInfo[0].patientAddress);
									//工作单位
									$('#Depana').textbox('setValue',pInfo[0].patientWorkunit);
									//联系地址
									$('#Depanature').textbox('setValue',pInfo[0].patientLinkaddress);
									//联系电话
									$('#phoneNumber').textbox('setValue',pInfo[0].patientLinkphone);
									//家庭电话
									$('#homephone').textbox('setValue',pInfo[0].patientPhone);
									//单位电话
									$('#danphone').textbox('setValue',pInfo[0].patientWorkphone);
									//入院时间
									var now = "${now}";
									$('#inPatientDate').val(now);
									//拿到病历号显示以往的住院登记证明记录
									var inpatientNoSerc = pInfo[0].medicalrecordId; 
							 }
						}
					})
			  	 }
			 }
		 });
		}
	}
	/**
	   * 回车弹出科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
		
	function popWinToDept(){
		$('#reportDept').combobox('setValue','');
		var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptType=I&textId=reportDept";
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
	}
	/**
	   * 回车弹出病区弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
		
	function popWinToBingQu(){
			var keshi = $('#reportDept').combobox('getValue');
			var tempWinPath = "<%=basePath%>popWin/pWWardDepartment/pWWardDepartment.action?nameTmp=SysDep artment&textId=bingqu&keshic="+keshi;
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
		  
	}
	//回车查询调用方法显示
	function searchValue(io,unit,certificatesNo,idcardNo){
		   var num="";
		   if(io!=null&&io!=""){
			   $.ajax({
				   url:'<%=basePath%>inpatient/info/queryProofList.action?c_no='+io,
					  data:{idcardNo:idcardNo},
					  type:'post',
					  success:function(data){
						  var patlist=data;
						  var dt=patlist.reportIssuingdate;
						  var d=(new Date(dt).getTime())/1000 / 60 / 60 /24;
						  var date=new Date();
						  var c=(new Date(date).getTime())/1000 / 60 / 60 /24;
						  var b=(c-d);
						  if(b>10){
							  $.messager.alert('提示','您的门诊号已经过期！');
							  setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							  return false;
						  }
						  if(patlist.patientName!=null&&patlist.patientName!=''){
							  var result=true;
							  $.ajax({
								  url:"<%=basePath%>inpatient/info/queryMedicalrecordIdDate.action?medicalrecordId="+io,
									async:false,
									success:function(data){
										var dataMap = eval("("+data+")");
											 if(dataMap.resMsg=="R"){
						  						 $.messager.alert('提示',dataMap.resCode);
						  						setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
						  						 $("#AddInpatientInfo").form('clear');
												 $('#medicalrecordId').textbox('setValue','');
												 $('#fangfeijiange').textbox('setValue','1');
												 $('#aSourse').combobox('setValue','1');
											     $('#Sourse').combobox('setValue','01');
						  						 result=false;
						  					 }else if(dataMap.resMsg=='I'){
						  						 $.messager.alert('提示',dataMap.resCode);
						  						 setTimeout(function(){
												 	$(".messager-body").window('close');
												 },3500);
						  						 $("#AddInpatientInfo").form('clear');
												 $('#medicalrecordId').textbox('setValue','');
												 $('#fangfeijiange').textbox('setValue','1');
												 $('#aSourse').combobox('setValue','1');
											     $('#Sourse').combobox('setValue','01');
											     result=false;
						  					 }else if(dataMap.resMsg=='B'){
						  						 $.messager.alert('提示',dataMap.resCode);
						  						setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
						  						 $("#AddInpatientInfo").form('clear');
												 $('#medicalrecordId').textbox('setValue','');
												 $('#fangfeijiange').textbox('setValue','1');
												 $('#aSourse').combobox('setValue','1');
											     $('#Sourse').combobox('setValue','01');
											     result=false;
						  					 }else if(dataMap.resMsg=='P'){
						  						 $.messager.alert('提示',dataMap.resCode);
						  						setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
						  						 $("#AddInpatientInfo").form('clear');
												 $('#medicalrecordId').textbox('setValue','');
												 $('#fangfeijiange').textbox('setValue','1');
												 $('#aSourse').combobox('setValue','1');
											     $('#Sourse').combobox('setValue','01');
											     result=false;
						  					 }else if(dataMap.resMsg=='have'){
													$('#bingqu').combobox({disabled:false});
							  					   var pal=dataMap.resCode;
								  					   var inpat=io.slice(-8);
														var encode="";
														if(!dataMap.resCode.paykindCode){
															encode="01";
															code="01";
														}else{
															encode=unit;
															code=unit;
														}
														$("#inpatientNo").val("ZY"+dataMap.innum+inpat);
														$('#medicalrecordIdNo').val(io);
														$('#pactCode').val(encode);
														$('#medreID').textbox('setValue',io);
														//开据医生
														$('#emplCode').combobox('setValue',patlist.reportIssuingdoc);
														$('#emplName').val($('#emplCode').combobox('getText'));
														//姓名
														$('#patientName').textbox('setValue',patlist.patientName);
														//性别
														$('#sexCombobox').combobox('setValue',patlist.reportSex);
														//结算方式
														$('#paykind').combobox('setValue',patlist.paykindcode);
														 //生日
														$('#birDate').val(patlist.reportBirthday);
														var ages=DateOfBirth(patlist.reportBirthday);
														if(ages.get("nianling")=="0"){
															$('#reportAge').textbox('setValue',"0");	
														}else{
  														$('#reportAge').textbox('setValue',ages.get("nianling"));
														}
														 //年龄单位
														$('#reportAgeunit').text(ages.get('ageUnits'));
														$('#reportAgeunit1').val(ages.get('ageUnits'));
														 //科室
													    $('#reportDept').combobox('setValue',pal.deptCode);
													    $('#reportDeptName').val($('#reportDept').combobox('getText'));
														$('#bingqu').combobox('reload',"<c:url value='/inpatient/info/querydeptCombobox.action'/>?deptId="+pal.deptCode);
														$('#bingqu').combobox('setValue',pal.nurseCellCode);
														$('#bingquName').val($('#bingqu').combobox('getText'));
														 //床号（病床ID）
														 $('#bedName').val(pal.bedNo);
														 $('#bedNo').val(pal.bedId);
														 $('#bedwardId').val(pal.bedwardID);
														 $('#bedwardName').val(pal.bedwardName);
														 //住院医生
														 $('#houseDocName').val(pal.predoctName);
														 $('#houseDocCode').val(pal.predoctCode);
														 //医疗证号
														 $('#mcardNo').textbox('setValue',pal.mcardNo);
														 //就诊卡号
														 $('#idcardNo').val(patlist.idno);
														 //证件类型
														 $('#certificatesType').val(patlist.certificatesType);
														 var type=$('#certificatesType').val();
														 if(type=="3"){
															//证件号码
															$('#idcard').textbox('setValue',patlist.certificatesNo);
															$('#idcard1').val(patlist.certificatesNo);
														 }else{
															//证件号码
															$('#idcard').textbox('setValue','');
															$('#idcard1').val(patlist.certificatesNo);
														 }
														//婚姻状况
														$('#marry').combobox('setValue',pal.mari);
														//国籍
														$('#country').combobox('setValue',pal.counCode);
														//民族
														$('#nation').combobox('setValue',pal.nationCode);
														//籍贯
														$('#nativeplace').textbox('setValue',pal.dist);
														//职业
														$('#Occupation').combobox('setValue',pal.profCode);
														//联系人
														$('#relat').textbox('setValue',pal.linkmaName);
														//关系
														$('#Relation').combobox('setValue',pal.relaCode);
														//出生地
														$('#birFrom').textbox('setValue',cityMap[pal.birthArea]);
														$('#birFrom1').val(pal.birthArea);
														//家庭地址
														$('#address').textbox('setValue',pal.home);
														//工作单位
														$('#Depana').textbox('setValue',pal.workName);
														//联系地址
														$('#Depanature').textbox('setValue',pal.linkmaAdd);
														//联系电话
														$('#phoneNumber').textbox('setValue',pal.linkmaTel);
														//家庭电话
														$('#homephone').textbox('setValue',pal.homeTel);
														//单位电话
														$('#danphone').textbox('setValue',pal.workTel); 
														//入院时间
														var now = "${now}";
														$('#inPatientDate').val(now);
														 //入院情况
														$('#comdration').combobox('setValue',patlist.reportSituation);
														//门诊诊断
														$('#menzhen').textbox('setValue',pal.diagCode);
														//病区
														$('#bingqu').combobox('setValue',pal.nurseCellCode);
														$('#bingquName').val($('#bingqu').combobox('getText'));
								  						 result=false;
								  					 }else if(dataMap.resMsg=='none'){
								  						$.ajax({//根据病历号查询住院次数
														  		url:"<%=basePath%>inpatient/info/queryCount.action?medicalrecordId="+io,
														  		async:false,		
														  		success:function(dataNum){
														  			 num=dataNum;
														  			 if(num<10){
														  				 num="0"+num;
														  			 }
														  		}		
														  });
								  						$.ajax({
								  							url:"<%=basePath%>inpatient/info/getPatientInfoByCerNo.action?cerno="+patlist.medicalrecordId,		
  															success:function(pp){
  																var plist=pp;
																	 $.ajax({
																		 url:"<%=basePath%>inpatient/info/queryProofInfo.action?certificatesNo="+patlist.medicalrecordId,
 																		  success:function(data){
																			$('#bingqu').combobox({disabled:false});
 																			 var result=eval("("+data+")");
																			  if(result.resMsg=="yes"){
																				 var inpat=io.slice(-8);		
																					encode=plist[0].unit;
																					code=plist[0].unit;
																					if(encode==null||encode==""){
																						encode="01";
																						code="01";
																					}
																					$("#inpatientNo").val("ZY"+num+inpat);
																					$('#medicalrecordIdNo').val(io);
																					$('#pactCode').val(code);
																					$('#medreID').textbox('setValue',io);
																					$('#emplCode').combobox('setValue',patlist.reportIssuingdoc);
																					$('#emplName').val($('#emplCode').combobox('getText'));
																					//姓名
																					$('#patientName').textbox('setValue',patlist.patientName);
																					//性别
																					$('#sexCombobox').combobox('setValue',patlist.reportSex);
																					//结算方式
																					$('#paykind').combobox('setValue',patlist.paykindcode);
																					 //生日
																					$('#birDate').val(patlist.reportBirthday);
																					var ages=DateOfBirth(patlist.reportBirthday);
																					 //年龄
																					 if(ages.get("nianling")=="0"){
   																					$('#reportAge').textbox('setValue',"0");

																					 }else{
   																					$('#reportAge').textbox('setValue',ages.get("nianling"));

																					 }
																					$('#reportAgeunit').text(ages.get('ageUnits'));
																					$('#reportAgeunit1').val(ages.get('ageUnits'));
																					 //科室
																				    $('#reportDept').combobox('setValue',patlist.reportDept);
																				    $('#reportDeptName').val($('#reportDept').combobox('getText'));
																					$('#bingqu').combobox('reload',"<c:url value='/inpatient/InpatientProof/querybingqu.action'/>?departmentCode="+patlist.reportDept);
																					$('#bingqu').combobox('setValue',patlist.reportBedward);
																					$('#bingquName').val($('#bingqu').combobox('getText'));
																					 //就诊卡号
																					 $('#idcardNo').val(patlist.idno);
																					 //证件类型
																					$('#certificatesType').val(patlist.certificatesType);
																					var type=$('#certificatesType').val();
																					if(type=="3"){
																						//证件号码
																						$('#idcard').textbox('setValue',patlist.certificatesNo);
																						$('#idcard1').val(patlist.certificatesNo);
																					}else{
																						//证件号码
																						$('#idcard').textbox('setValue',null);
																						$('#idcard1').val(patlist.certificatesNo);
																					}
																					//婚姻状况
																					$('#marry').combobox('setValue',plist[0].patientWarriage);
																					//国籍
																					$('#country').combobox('setValue',plist[0].patientNationality);
																					//民族
																					$('#nation').combobox('setValue',plist[0].patientNation);
																					//籍贯
																					$('#nativeplace').textbox('setValue',plist[0].patientNativeplace);
																					//职业
																					$('#Occupation').combobox('setValue',plist[0].patientOccupation);
																					//联系人
																					$('#relat').textbox('setValue',plist[0].patientLinkman);
																					//关系
																					$('#Relation').combobox('setValue',plist[0].patientLinkrelation);
																					//出生地
																					$('#birFrom').textbox('setValue',cityMap[plist[0].patientBirthplace]);
																					$('#birFrom1').val(plist[0].patientBirthplace);
																					//家庭地址
																					$('#address').textbox('setValue',plist[0].patientAddress);
																					//工作单位
																					$('#Depana').textbox('setValue',plist[0].patientWorkunit);
																					//联系地址
																					$('#Depanature').textbox('setValue',plist[0].patientLinkaddress);
																					//联系电话
																					$('#phoneNumber').textbox('setValue',plist[0].patientLinkphone);
																					//家庭电话
																					$('#homephone').textbox('setValue',plist[0].patientPhone);
																					//单位电话
																					$('#danphone').textbox('setValue',plist[0].patientWorkphone); 
																					//入院时间
																					var now = "${now}";
																					$('#inPatientDate').val(now);
																					 //入院情况
																					$('#comdration').combobox('setValue',patlist.reportSituation);
																					//门诊诊断
																					$('#menzhen').textbox('setValue',patlist.reportDiagnose);
																					//病区
																					$('#bingqu').combobox('setValue',patlist.reportBedward);
																					$('#bingquName').val($('#bingqu').combobox('getText'));
																					//拿到病历号显示以往的住院登记证明记录
																					var inpatientNoSerc = plist[0].medicalrecordId;
																				  
																			  }else if(result.resMsg=="no"){
																				 $.messager.alert('提示',result.resCode);
																				 setTimeout(function(){
																						$(".messager-body").window('close');
																					},3500);
																				 $("#AddInpatientInfo").form('clear');
																				 $('#medicalrecordId').textbox('setValue','');
																				 $('#fangfeijiange').textbox('setValue','1');
																				 $('#medicalrecordId').textbox('setValue','');
																				 $('#aSourse').combobox('setValue','1');
																				 $('#Sourse').combobox('setValue','01');  
																			  }else{
																				 $.messager.alert('提示','未知错误请联系管理员');
																				 setTimeout(function(){
																						$(".messager-body").window('close');
																					},3500);
																			  }
 																		  }
																	 });
  															}
								  							
								  						});
								  					 }
									}
								  
							  });
						  }else{
							  $.messager.alert('提示','该患者没有有效的住院证明');
							  setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							  $("#AddInpatientInfo").form('clear'); 
							  $('#medicalrecordId').textbox('setValue','');
							  $('#fangfeijiange').textbox('setValue','1');
							  $('#aSourse').combobox('setValue','1');
							  $('#Sourse').combobox('setValue','01');
							  return false;
						  }
					  }
				   
			   });
		   }
	};
	//验证页面数据输入格式长度
	function comparedata(){
		if(Date.parse($('#inPatientDate').val())>Date.parse("${now}")){
			$.messager.alert('提示','入院时间不能晚于当前时间，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#inPatientDate').val('');
			return false;
		}
		if(Date.parse($('#birDate').val())>Date.parse("${now}")){
			$.messager.alert('提示','出生日期不能晚于当前时间，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#birDate').val('');
			return false;
		}
		if($('#mcardNo').textbox('getValue')!=null&&$('#mcardNo').textbox('getValue')!=""){
	 		if($('#mcardNo').textbox('getValue').length!=18){
				$.messager.alert('提示','请重新输入18位医疗证号');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
		}
		if($('#comp').textbox('getValue')!=null&&$('#comp').textbox('getValue')!=""){
			if($('#comp').textbox('getValue').length!=20){
				$.messager.alert('提示','请重新输入20位电脑号');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
		}
		if($('#patientName').textbox('getValue').length>20){
			$.messager.alert('提示','姓名过长,请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#patientName').textbox('setValue',"");
			return false;
		}
		if($('#Depanature').textbox('getValue').length>50){
			$.messager.alert('提示','工作单位过长,请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#Depanature').textbox('setValue',"");
			return false;
		}
		if($('#relat').textbox('getValue').length>20){
			$.messager.alert('提示','联系人过长,请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#relat').textbox('setValue',"");
			return false;
		}
		if($('#nativeplace').textbox('getValue').length>20){
			$.messager.alert('提示','籍贯过长，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#nativeplace').textbox('setValue',"");
			return false;
		}
		if($('#phoneNumber').textbox('getValue').length>30){
			$.messager.alert('提示','联系电话过长，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#phoneNumber').textbox('setValue',"");
			return false;
		}
		if($('#homephone').textbox('getValue').length>30){
			$.messager.alert('提示','联系电话过长，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#homephone').textbox('setValue',"");
			return false;
		}
		if($('#danphone').textbox('getValue').length>30){
			$.messager.alert('提示','单位电话过长，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#danphone').textbox('setValue',"");
			return false;
		}
		if($('#menzhen').textbox('getValue').length>50){
			$.messager.alert('提示','门诊诊断过长，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#menzhen').textbox('setValue',"");
			return false;
		}
		yanzhengjieguo="1";
	}
	//渲染科室		
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	function babyclick(){
		if($('#babyFlag').is(':checked')){
			$('#babyFlag').val(1);
		}else{
			$('#babyFlag').val(0);
		}
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
	function resetClick(){
		$("#AddInpatientInfo").form('clear');
		$('#bingqu').combobox({disabled:true});
		$('#medicalrecordId').textbox('setValue','');
		$('#fangfeijiange').textbox('setValue','1');
		//设置入院来源的默认值（默认值为 门诊）
		$('#Sourse').combobox('setValue','01');
		//设置入院途径的默认值（本市）
		$('#aSourse').combobox('setValue','1');
		//入院情况
		$('#comdration').combobox('setValue',"3");
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="">
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false,fit:true" align="center" style="padding: 5px;">
		<form id="AddInpatientInfo" method="post" onSubmit="return checkEmpty(form)">
		<table width="100%" style="margin-top: 0px;">
			<tr>
				<td>
					<div style="height: 25px; line-height: 25px;padding:0px 0px 10px 0px">
						病历号：<input id="medicalrecordId" class="easyui-textbox"  data-options="prompt:'输入就病历号回车查询'" style="width: 180px;height: 20px;" />
						<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryMedicalrecordId()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 30px" >查询</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard">
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
						</shiro:hasPermission>
		        		<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        			<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						</shiro:hasPermission>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table class="tableCss">
						<tr>
							<td colspan="6">基本信息</td>
						</tr>
						<tr>
							<td class="TDlabel">姓名：</td>
							<td class="TDinput">
								<input type="hidden" id="invoiceNoflay" value="${invoiceNoflag }"/>
								<input id="medicalrecordIdNo" name="inpatientInfo.medicalrecordId" type="hidden"/>
								<input id="pactCode" name="inpatientInfo.pactCode" type="hidden"/>
								<input id="bedName" name="inpatientInfo.bedName" type="hidden"/>
								<input id="bedNo" name="inpatientInfo.bedNo" type="hidden"/>
								<input id="bedId" name="inpatientInfo.bedId" type="hidden"/>
								<input id="bedwardId" name="inpatientInfo.bedwardId" type="hidden"/>
								<input id="bedwardName" name="inpatientInfo.bedwardName" type="hidden"/>
								<input id="houseDocCode" name="inpatientInfo.houseDocCode" type="hidden"/>
								<input id="houseDocName" name="inpatientInfo.houseDocName" type="hidden"/>
								<input class="easyui-textbox" id="patientName"  name="inpatientInfo.patientName" data-options="required:true"/>
							</td>
							<td class="TDlabel">性别：</td>
							<td class="TDinput">
								<input id="sexCombobox" class="easyui-combobox" name="inpatientInfo.reportSex" data-options="required:true,url : '<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ',valueField:'encode',textField:'name'"  />
							</td>
							<td class="TDlabel">年龄：</td>
							<td class="TDinput">
								<input class="easyui-textbox" name="inpatientInfo.reportAge" id="reportAge"  />
								<span id="reportAgeunit" ></span>
								<input id="reportAgeunit1" name="inpatientInfo.reportAgeunit" type="hidden"/>
								<input id="idcardNo" name="inpatientInfo.idcardNo" type="hidden"/>
							</td>
						</tr>
						<tr>
							<td class="TDlabel">出生日期：</td>
							<td class="TDinput">
                                    <input id="birDate" name="inpatientInfo.reportBirthday" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="TDlabel">病历号：</td>
							<td class="TDinput">
								<input id="medreID"  class="easyui-textbox" readonly="readonly" data-options="required:true">
								<input id="inpatientNo"  type="hidden" name="inpatientInfo.inpatientNo"/>
							</td>
							<td class="TDlabel">结算方式：</td>
							<td class="TDinput">
								<input id="paykind" class="easyui-combobox" name="inpatientInfo.paykindCode"  />
							</td>
						</tr>
						<tr>
							<td class="TDlabel">住院科室：</td>
							<td class="TDinput">
								 <input id="reportDeptName" type="hidden" name="inpatientInfo.deptName"/>
								 <input id="reportDept" class="easyui-combobox" name="inpatientInfo.deptCode" data-options="required:true"/>
								 <a href="javascript:delSelectedData('reportDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="TDlabel">病区：</td>
							<td class="TDinput">
								<input id="bingqu" name="inpatientInfo.nurseCellCode" data-options="required:true"/>
								<input id="bingquName" name="inpatientInfo.nurseCellName" type="hidden"/>
								<a href="javascript:delSelectedData('bingqu');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="TDlabel">患者状态：</td>
							<td class="TDinput">
								<input class="easyui-combobox" id="patientStatus" name="inpatientInfo.patientStatus">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
						<table class="tableCss">
							<tr>
								<td colspan="6">附属信息</td>
							</tr>
							<tr>
								<td class="TDlabel">医疗证号：</td>
								<td class="TDinput">
									<input class="easyui-textbox" type="text" name="inpatientInfo.mcardNo"  id="mcardNo" />
								</td>
								<td class="TDlabel">电脑号：</td>
								<td class="TDinput">
									<input class="easyui-textbox" type="text" id="comp" name="inpatientInfo.procreatePcno" />
								</td>
								<td class="TDlabel">身份证号：</td>
								<td class="TDinput">
									<input class="easyui-textbox"  id="idcard"  name="certificatesNo"  />
									<input type="hidden"  id="idcard1"  name="inpatientInfo.certificatesNo"  />
									<input type="hidden" id="certificatesType"  name="inpatientInfo.certificatesType"  />
								</td>
								
							</tr>
							<tr>
								<td class="TDlabel">国籍：</td>
								<td class="TDinput">
									<input id="country" class="easyui-combobox"  name="inpatientInfo.counCode" data-options="valueField:'encode',textField:'name',url:'<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=country'/>',model:'remote'" />
									<a href="javascript:delSelectedData('country');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
								<td class="TDlabel">出生地：</td>
								<td class="TDinput">
									<input id="birFrom" class="easyui-textbox" />
									<input id="birFrom1" type="hidden" name="inpatientInfo.birthArea" />
								</td>
								<td class="TDlabel">婚姻状况：</td>
								<td class="TDinput">
									<input id="marry"  name="inpatientInfo.mari" class="easyui-combobox" data-options="valueField:'encode',textField:'name',url:'<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=marry'/>'"/>
								</td>	
								
							</tr>
							<tr>
							<td class="TDlabel">民族：</td>
								<td class="TDinput">
									<input id="nation" class="easyui-combobox" name="inpatientInfo.nationCode" data-options="valueField:'encode',textField:'name',url:'<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=nationality'/>'"/>
										<a href="javascript:delSelectedData('nation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
								<td class="TDlabel">籍贯：</td>
								<td colspan="3">
									<input id="nativeplace" class="easyui-textbox" name="inpatientInfo.dist" style="width: 65%;"/>
								</td>
								
							</tr>
							<tr>
								<td class="TDlabel">家庭电话：</td>
								<td class="TDinput">
									<input class="easyui-textbox" id="homephone" type="text" name="inpatientInfo.homeTel" />
								</td>
								<td class="TDlabel">家庭住址：</td>
								<td colspan="3">
									<input id="address" class="easyui-textbox" name="inpatientInfo.home" style="width:65%;"/>
								</td>
							</tr>
							<tr>
								<td class="TDlabel">职业：</td>
								<td class="TDinput">
									<input id="Occupation" class="easyui-combobox" name="inpatientInfo.profCode" data-options="valueField:'encode',textField:'name',url:'<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>'"/>
										<a href="javascript:delSelectedData('Occupation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
								
								<td class="TDlabel">联系人：</td>
								<td class="TDinput">
									<input class="easyui-textbox" id="relat" type="text" name="inpatientInfo.linkmanName"  />
								</td>
								<td class="TDlabel">关系：</td>
								<td class="TDinput">
									<input id="Relation" class="easyui-combobox" name="inpatientInfo.relaCode" data-options="valueField:'encode',textField:'name',url:'<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=relation'/>'"/>
									<a href="javascript:delSelectedData('Relation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
							</tr>
							<tr>
								<td class="TDlabel">联系人地址：</td>
								<td class="TDinput">
									<input id="Depanature" class="easyui-textbox" name="inpatientInfo.linkmanAddress"/>
								</td>
								<td class="TDlabel">联系电话：</td>
								<td class="TDinput">
									<input class="easyui-textbox" id="phoneNumber" type="text" name="inpatientInfo.linkmanTel" />
								</td>
								<td class="TDlabel">入院途径：</td>
								<td class="TDinput">
									<input id="aSourse" class="easyui-combobox" value="1" name="inpatientInfo.inAvenue"  data-options="valueField:'encode',textField:'name',url:'<%=basePath%>inpatient/info/getbrlydqo.action'"/>
								</td>
							</tr>
							<tr>
								<td class="TDlabel">单位电话：</td>
								<td class="TDinput">
									<input id="danphone" class="easyui-textbox" type="text" name="inpatientInfo.workTel"  />
								</td>
								<td class="TDlabel">入院情况：</td>
								<td class="TDinput">
									<select id="comdration" class="easyui-combobox" style="width: 150px;" name="inpatientInfo.inCircs">
										  <option value="3">一般</option>   	
		                                  <option value="2">急</option>     
		                                  <option value="1">危机</option>   
		                            </select> 
								</td>
								<td class="TDlabel">房费间隔：</td>
								<td class="TDinput">
									<input class="easyui-textbox" type="text"  value="1" readonly="readonly" name="inpatientInfo.feeInterval"  id="fangfeijiange"/>
								</td>
							</tr>
							<tr>
								<td class="TDlabel">入院时间：</td>
								<td class="TDinput">
									<input id="inPatientDate"  name="inpatientInfo.inDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td class="TDlabel">入院来源：</td>
								<td class="TDinput">
									<input id="Sourse" class="easyui-combobox" name="inpatientInfo.inSource" value="01"
									data-options="valueField:'encode',textField:'name',url:'<%=basePath %>inpatient/info/getsource.action'"/>
								</td>
								<td class="TDlabel">预交金额：</td>
								<td class="TDinput">
									<input id="prepayCost" name="inpatientInPrepay.prepayCost" class="easyui-numberbox"/>
								</td>
							</tr>
							<tr>
								<td class="TDlabel">血滞纳金：</td>
								<td class="TDinput">
									<input class="easyui-numberbox" value="" data-options="min:0,precision:2" id="booldmoney"  name="inpatientInfo.bloodLatefee"/>
								</td>
								<td class="TDlabel">工作单位：</td>
								<td colspan="7">
									<input id="Depana" class="easyui-textbox" style="width: 65%" name="inpatientInfo.workName" />
								</td>
							</tr>
							<tr>
								<td class="TDlabel">担保类型：</td>
								<td class="TDinput">
									<input id="suretyType" name="inpatientSurety.suretyType"  class="easyui-combobox"/>
								</td>
								<td class="TDlabel">担保人：</td>
								<td class="TDinput">
									<input id="suretyName" name="inpatientSurety.suretyName"  class="easyui-textbox"/>
								</td>	
								<td class="TDlabel">担保金额：</td>
								<td class="TDinput">
									<input id="suretyCost" name="inpatientSurety.suretyCost"  class="easyui-numberbox"/>
								</td>	
							</tr>
								<td class="TDlabel">开据医生：</td>
								<td class="TDinput">
									<input id="emplName" type="hidden" name="inpatientInfo.emplName"/>
									<input class="easyui-combobox" id="emplCode" name="inpatientInfo.emplCode" />
								</td>	
								<td class="TDlabel">是否是婴儿：</td>
								<td class="TDinput">
									<input type="checkBox" id="babyFlag" name="inpatientInfo.babyFlag" />
								</td>	
								<td class="TDlabel">是否有婴儿：</td>
								<td colspan="7">
									<input type="checkBox" id="haveBabyFlag" name="inpatientInfo.haveBabyFlag" />
								</td>
							<tr>
							</tr>
							<tr>
								<td class="TDlabel">门诊诊断：</td>
								<td colspan="7">
									<input class="easyui-textbox" style="width: 43%" id="menzhen" name="inpatientInfo.diagName" data-options="required:true"/>
								</td>
							</tr>
						</table>
				</td>
			</tr>
			<tr>
				<td style="text-align: center;padding-top: 10px;">
					<shiro:hasPermission name="${menuAlias}:function:save">
						<a id="hospitalOKbtn" href="javascript:submit(0)" data-options="iconCls:'icon-save'" class="easyui-linkbutton">确定登记</a>
					</shiro:hasPermission>
					&nbsp;<a id="resetButton" href="javascript:resetClick();" data-options="iconCls:'icon-clear'" class="easyui-linkbutton">清空所填</a>
				</td>
			</tr>
		</table>
		</form>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
   		<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
</div>
</body>
</html>