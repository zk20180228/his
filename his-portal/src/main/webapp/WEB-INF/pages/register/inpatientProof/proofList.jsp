<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>开立住院证</title>
	<%@ include file="/common/metas.jsp" %>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
		/**
		*点击时获取表格记录中的预约号（门诊号）
		*/
		var regisNo=null;
		var deptMap = new Map();  //部门
		var empMap = null;
		var typeMap = null;
		var birthday = "";
		/**
		*定义全局变量用于接收dept的ID然后进行比较 
		*/
		var medicalrecordId="";
		var mainId="";
		var no="";
		var zjhm="";
		var statedate=null;
		var dayinState="1";
		var mid ="";
		var sexMap=new Map();
		$.fn.combobox.defaults.filter = function(q, row){     
			var opts = $(this).combobox('options');     
			return row[opts.textField].indexOf(q) >= 0; 
		}
		$(function(){
			//页面加载病区禁用
			$('#reportBedward').combobox({disabled:true});
			//页面加载打印禁用
			$('#baocun').linkbutton({disabled:false});
			$('#dayin').linkbutton({disabled:true});
			/**
			*加载数据
			*/
			//查询证件类型下拉列表内容
			$('#patientCertificatestype').combobox({    
				url: '<%=basePath%>inpatient/InpatientProof/queryCertypeListForcomboboxPublic.action',
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    editable:false
			});
			//查询合同单位下拉列表内容
			$('#contractunit').combobox({    
				url: '<%=basePath%>register/newInfo/contCombobox.action',
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    editable:true,
			});
			//查询科室下拉列表
			$('#keshi').combobox({   
				url: "<%=basePath%>inpatient/InpatientProof/queryInHosDept.action",  
			    valueField:'deptCode',    
			    textField:'deptName',
			    required:true,
			    mode:'remote',
	            onSelect:function(data){
					$('#reportBedward').combobox('reload',"<c:url value='/inpatient/InpatientProof/querybingqu.action'/>?departmentCode="+data.deptCode);
	     			$('#reportBedward').combobox({disabled:false});
			    	$('#reportBedward').combobox('setValue',"");
			    },
				onLoadSuccess:function(){
				var data = $('#keshi').combobox('getData');
	            if (data.length == 1) {
	                $('#keshi').combobox('setValue', data[0].deptCode);
	                $('#reportBedward').combobox('setValue',"");
	 		    	$('#reportBedward').combobox('reload',"<c:url value='/inpatient/InpatientProof/querybingqu.action'/>?departmentCode="+data[0].deptCode);
	 		    	$('#reportBedward').combobox({disabled:false});
	            }
		     }
			    
			});
			//主治医师(下拉框)
			$('#kzys').combobox({
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
			/**
			*回车事件
			*/
			//就诊卡号回车查询
			bindEnterEvent('medicalrecordId',queryList,'easyui');
			// 合同单位弹出事件
			bindEnterEvent('contractunit',openBusinessContractunitWin,'easyui');//合同单位
			bindEnterEvent('keshi',popWinToDept,'easyui');//科室绑定回车事件
			bindEnterEvent('kzys',popWinToKZYS,'easyui');//主治医生绑定回车事件
			bindEnterEvent('reportBedward',openSysDepartmentWin,'easyui');//病室(病区)
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
					$('#medicalrecordId').textbox('setValue',data);
					queryList();
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
					$('#medicalrecordId').textbox('setValue',data);
					queryList();
				}
			});
		};
		/*******************************结束读身份证***********************************************/
		function jusnl(){
			//出生日期
			var date = $('#csrq').val();
			date = eval('new Date(' + date.replace(/\d+(?=-[^-]+$)/, 
					   function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var year=y+"-"+m+"-"+d;
			var agedate=DateOfBirth(year);
			if(agedate.get("nianling")=="0"){
				$('#age').textbox('setValue',"0");
			}else{
				$('#age').textbox('setValue',agedate.get("nianling"));	
			}
			$('#nianlingdanwei').text(agedate.get('ageUnits'));
			$('#nianlingdanwei1').val(agedate.get('ageUnits'));
		}
		function popWinToKZYS(){
			 var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=kzys";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}
		function openBusinessContractunitWin(){
			var tempWinPath = "<%=basePath%>popWin/popWinUnit/pWCUnitList.action?nameTmp=BusinessContractunit&textId=contractunit";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
		}
		
		 /**
		   * 回车弹出住院科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
			
		function popWinToDept(){
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=keshi&deptType=I";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
		}	
		/* 
		* 因读卡功能暂未实现，所以读卡暂取输入框的值，点击读卡时可能会提示请输入就诊卡号
		* dtl
		* 2016年12月1日19:49:11
		*/
		//查询事件
		function queryList(/* flag */){
			/*
			渲染表单数据
			*/
			//渲染表单中的挂号专家
			$.ajax({
				url: "<c:url value='/inpatient/InpatientProof/queryEmpMapPublic.action'/>",
				async:false,
				type:'post',
				success: function(empData) {
					empMap = empData;
				}
			});
			//性别渲染
			$.ajax({
				url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
				async:false,
				data:{"type":"sex"},
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			});
			//渲染表单中的挂号科室
			$.ajax({
				url: '<%=basePath%>inpatient/InpatientProof/queryDeptMapPublicByRe.action',
				async:false,
				success: function(deptData) {
					deptMap = deptData;
				}
			});
			//渲染表单中的挂号类别
			$.ajax({
				url: '<%=basePath%>inpatient/InpatientProof/queryRegisterTypeMapPublic.action',
				async:false,
				success: function(typeData) {
					typeMap = typeData;
				}
			});
			 //病室(病区)
			$('#reportBedward').combobox({    
			    url:"<%=basePath%>inpatient/InpatientProof/querybingqu.action",   
		  		mode:'remote',
			    valueField:'deptCode',    
			    textField:'deptName'
		    });
			 
			$('#dayin').linkbutton({disabled:true});
			medicalrecordId = $('#medicalrecordId').textbox('getValue');
			if(medicalrecordId == ''){
				$.messager.alert('提示','请输入病历号！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			//清除页面表单数据
			var name = $('#patientNamesss').textbox('getValue');
			if(name!=null&&name!=''){
	 			clear();
			}
			$('#medicalrecordId').textbox('setValue',medicalrecordId);
			//查询挂号信息表中该患者是否有  有效的 挂号记录
			$('#listlist').datagrid({
				url: "<%=basePath%>inpatient/InpatientProof/queryInfoListHis.action",
				queryParams:{medicalrecordId:medicalrecordId},
				rowStyler:function(index,row){
					if(row.state==2){
						return 'background-color:#00FF00;color:black;';
					}
				},
				onLoadSuccess:function(data){
					if(data.rows.length=="0"){
						$.messager.alert('提示','患者没有有效的挂号记录！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else if(data.rows.length=="1"){
						//只有一条挂号记录时，直接查询证明表
						mid = data.rows[0].medicalrecordId;
						searchProofByResinfo(data.rows[0].medicalrecordId,data.rows[0].no);
					}
				},
				onClickRow:function(index, row){
					mid = row.medicalrecordId;
					searchProofByResinfo(row.medicalrecordId,row.no);
				}
			});
		}
		//通过挂号记录查询证明表里是否有证明记录（参数:病历号和门诊号）
		function searchProofByResinfo(medid,no){
			$.ajax({
				url:'<%=basePath%>inpatient/InpatientProof/searchProofByResinfo.action',
				data:{midicalrecordId:medid,regisNo:no},
				success:function(data){
					var datemap = data;
					if(datemap.key=="one" ||datemap.key=='O'||datemap.key=='N'){
						huixianyemian(datemap);
					}else if(datemap.key=="none"){
						var pageNo=$('#medicalrecordId').textbox('getValue');
						clear();
						$('#medicalrecordId').textbox('setValue',pageNo);
						searchform(medid,no);
					}else {
						dayinState="2";
						huixianyemian(datemap);
						$.messager.alert('提示',datemap.mes);
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						$('#baocun').linkbutton({disabled:true});
					}
				}
			});
		}
		/**
		*点击Datagrid查询表单(通过门诊号)
		*/
		function searchform(medid,no){
			$('#baocun').linkbutton({disabled:false});
			$('#dayin').linkbutton({disabled:true});
			var now="${now}";
			$.ajax({
	   			url: "<%=basePath%>inpatient/InpatientProof/queryMedicalrecordId.action",
	   			data:{regisNo:no},
				success: function(data) {
					var dataMap = data;
						statedate="yes";
						$('#kzrq').val(now);
						$('#patientNamesss').textbox('setValue',dataMap.patientName);
						$('#address').textbox('setValue',dataMap.address);
						$('#zjhm').textbox('setValue',dataMap.patientIdenno);
						$('#csrq').val(dataMap.patientBirthday);
						$('#idcardNo').textbox('setValue',dataMap.clinicCode);	
						var ages=DateOfBirth(dataMap.patientBirthday);
						$('#nianlingdanwei').text(ages.get('ageUnits'));
						$('#nianlingdanwei1').val(ages.get('ageUnits'));
						if(ages.get("nianling")=="0"){
							$('#age').textbox('setValue',"0");	
						}else{
							$('#age').textbox('setValue',ages.get("nianling"));	
						}
						$('#kzys').combobox('setValue',dataMap.doctCode);	
// 						挂号表中的退号原因字段，后台暂存时set了门诊诊断
						$('#zd').textbox('setValue',dataMap.backnumberReason);
						$('#sex').combobox('setValue',dataMap.patientSex);
						$('#contractunit').combobox('setValue',dataMap.pactCode);
						$('#patientCertificatestype').combobox('setValue',dataMap.cardType);
						//病历号
						$('#newMedicalrecordId').textbox('setValue',dataMap.midicalrecordId);
						//住院科室 $('#keshi').combobox('setValue',dataMap.dept);
				}
			}); 
		}
		//查询到住院证明的记录回显到页面
		function huixianyemian(datemap){
			$('#dayin').linkbutton({disabled:false});
			$('#baocun').linkbutton({disabled:false});
 			$('#reportBedward').combobox({disabled:false});
			statedate="yes";
			//主键Id
			$('#mainId').val(datemap.value.id);
			
			$('#createUser').val(datemap.value.createUser);
			$('#createDept').val(datemap.value.createDept);
			$('#createTime').val(datemap.value.createTime);
			
			//门诊号
			$('#idcardNo').textbox('setValue',datemap.value.idcardNo);
			//开证日期
			$('#kzrq').val(datemap.value.reportIssuingdate);
			//姓名
			$('#patientNamesss').textbox('setValue',datemap.value.patientName);
			//合同单位
			$('#contractunit').combobox('setValue',datemap.value.contractUnit);
			//证件类型
			$('#patientCertificatestype').combobox('setValue',datemap.value.certificatesType);
			//证件号码
			$('#zjhm').textbox('setValue',datemap.value.certificatesNo);
			//性别
			$('#sex').combobox('setValue',datemap.value.reportSex);
			//地址
			$('#address').textbox('setValue',datemap.value.reportAddress);
			var ages=DateOfBirth(datemap.value.reportBirthday);
			$('#nianlingdanwei').text(ages.get('ageUnits'));
			$('#nianlingdanwei1').val(ages.get('ageUnits'));
			if(ages.get("nianling")=="0"){
				$('#age').textbox('setValue',"0");	
			}else{
				$('#age').textbox('setValue',ages.get("nianling"));	
			}
			$('#nianlingdanwei').text(datemap.value.reportAgeunit);
			$('#nianlingdanwei1').val(datemap.value.reportAgeunit);
			//开立医生
			$('#kzys').combobox('setValue',datemap.value.reportIssuingdoc);
			//病历号
			$('#newMedicalrecordId').textbox('setValue',datemap.value.medicalrecordId);
			//出生日期
			$('#csrq').val(datemap.value.reportBirthday.substring(0,10));
			//住院科室
			$('#keshi').combobox('setValue',datemap.value.reportDept);
			$('#reportBedward').combobox('reload',"<c:url value='/inpatient/InpatientProof/querybingqu.action'/>?departmentId="+datemap.value.reportDept.id);
			//病区
			$('#reportBedward').combobox('setValue',datemap.value.reportBedward);
			//输血数量
			$('#bloodqty').numberbox('setValue',datemap.value.reportBloodqty);
			//住院约计天数
			$('#daycount').numberbox('setValue',datemap.value.inpatientDaycount);
			//备注
			$('#beizhu').textbox('setValue',datemap.value.reportRemark);
			//诊断
			$('#zd').textbox('setValue',datemap.value.reportDiagnose);
			//X光照相
			if(datemap.value.reportXflag==1){
				$('#yb').prop("checked","checked");
			}else if(datemap.value.reportXflag==2){
				$('#tebie').prop("checked","checked");
			}
			//贵重药品
			if(datemap.value.reportDrugflag==1){
				$('#yg').prop("checked","checked");
			}else if(datemap.value.reportDrugflag==0){
				$('#buyong').prop("checked","checked");
			}
			//手术类型
			if(datemap.value.reportOpstype==1){
				$('#da').prop("checked","checked");
			}else if(datemap.value.reportOpstype==2){
				$('#zhong').prop("checked","checked");
			}else if(datemap.value.reportOpstype==3){
				$('#xiao').prop("checked","checked");
			}
			//入院情况
			if(datemap.value.reportSituation==1){
				$('#weiji').prop("checked","checked");
			}else if(datemap.value.reportSituation==2){
				$('#ji').prop("checked","checked");
			}else if(datemap.value.reportSituation==3){
				$('#yiban').prop("checked","checked");
			}
			//入院状态
			if(datemap.value.reportStatus==1){
				$('#zixing').prop("checked","checked");
			}else if(datemap.value.reportStatus==2){
				$('#husong').prop("checked","checked");
			}else if(datemap.value.reportStatus==3){
				$('#qiangjiu').prop("checked","checked");
			}
			//坐卧
			if(datemap.value.reportClinostatism==1){
				$('#banwo').prop("checked","checked");
			}else if(datemap.value.reportClinostatism==2){
				$('#xiuke').prop("checked","checked");
			}
			//食
			if(datemap.value.reportDiet==1){
				$('#jinshi').prop("checked","checked");
			}else if(datemap.value.reportDiet==2){
				$('#shi').prop("checked","checked");
			}
			//抬价
			if(datemap.value.reportShillflag==1){
				$('#taijia').prop("checked","checked");
			}
			//沐浴
			if(datemap.value.reportBathflag==1){
				$('#muyu').prop("checked","checked");
			}
			//理发
			if(datemap.value.reportHaircut==1){
				$('#lifa').prop("checked","checked");
			}
			//修改时通过单选按钮或者复选按钮的选中情况为相应字段赋值
			if($('#taijia').is(':checked')){
				$('#taijia').val(1);
			}else{
				$('#taijia').val(0);
		    }
			if($('#muyu').is(':checked')){
				$('#muyu').val(1);
			}else{
				$('#muyu').val(0);
		    }
			if($('#lifa').is(':checked')){
				$('#lifa').val(1);
			}else{
				$('#lifa').val(0);
		    }
			if($('#weiji').is(':checked')){
				$('#situationFlgHidden').val(1);
			}if($('#ji').is(':checked')){
				$('#situationFlgHidden').val(2);
		    }if($('#yiban').is(':checked')){
				$('#situationFlgHidden').val(3);
		    }
		    if($('#zixing').is(':checked')){
				$('#statusFlgHidden').val(1);
			}if($('#husong').is(':checked')){
				$('#statusFlgHidden').val(2);
		    }if($('#qiangjiu').is(':checked')){
				$('#statusFlgHidden').val(3);
		    }
			if($('#banwo').is(':checked')){
				$('#clinostatismFlgHidden').val(1);
			}if($('#xiuke').is(':checked')){
				$('#clinostatismFlgHidden').val(2);
			}
			if($('#jinshi').is(':checked')){
				$('#dietFlgHidden').val(1);
			}if($('#shi').is(':checked')){
				$('#dietFlgHidden').val(2);
			}
			if($('#nan').is(':checked')){
				$('#sexHidden').val(1);
			}if($('#nv').is(':checked')){
				$('#sexHidden').val(2);
			}
			if($('#yg').is(':checked')){
				$('#drugFlgHidden').val(1);
			}if($('#buyong').is(':checked')){
				$('#drugFlgHidden').val(0);
			}
			if($('#da').is(':checked')){
				$('#opstypeFlgHidden').val(1);
			}if($('#zhong').is(':checked')){
				$('#opstypeFlgHidden').val(2);
			}if($('#xiao').is(':checked')){
				$('#opstypeFlgHidden').val(3);
			}if($('#yb').is(':checked')){
				$('#bloodqtyFlgHidden').val(1);
			}if($('#tebie').is(':checked')){
				$('#bloodqtyFlgHidden').val(2);
			}
		}
		//清空页面
		function clear(){
			$('#tabletable tr td input[type=radio]').attr("checked",false);
			$('#tabletable tr td input[type=checkbox]').attr("checked",false);
			$('#sex').combobox('setValue',"");
			$('#contractunit').combobox('setValue',"");
			$('#patientCertificatestype').combobox('setValue',"");
 			$('#keshi').combobox({required:false});
 			$('#keshi').combobox('setValue',"");
			$('#kzys').textbox('setValue',"");	
			$('#kzrq').val("");	
			$('#csrq').val("");	
			$('#age').textbox('setValue',"");	
			$('#idcardNo').textbox('setValue',"");	
			$('#patientNamesss').textbox('setValue',"");	
			$('#zjhm').textbox('setValue',"");	
			$('#address').textbox('setValue',"");	
			$('#reportBedward').combobox({required:false});
			$('#reportBedward').combobox('setValue',"");	
			$('#beizhu').textbox('setValue',"");	
			$('#zd').textbox('setValue',"");	
			$('#editFormss').form('clear');
			$('#keshi').combobox({required:true});
 			$('#reportBedward').combobox({required:true});

		}
		//复选按钮赋值抬价
		function onclickBoxtaijia(id){
			if($('#taijia').is(':checked')){
				$('#taijia').val(1);
			}else{
				$('#taijia').val(0);
		   }
			
		}
		//复选按钮赋值沐浴
		function onclickBoxmuyu(id){
			if($('#muyu').is(':checked')){
				$('#muyu').val(1);
			}else{
				$('#muyu').val(0);
		   }
			
		}
		//复选按钮赋值理发
		function onclickBoxlifa(id){
			if($('#lifa').is(':checked')){
				$('#lifa').val(1);
			}else{
				$('#lifa').val(0);
		   }
			
		}
		//单选按钮赋值入院情况
		function onclickBoxryqk(id){
			if($('#weiji').is(':checked')){
				$('#situationFlgHidden').val(1);
			}if($('#ji').is(':checked')){
				$('#situationFlgHidden').val(2);
		   }if($('#yiban').is(':checked')){
				$('#situationFlgHidden').val(3);
		   }
		}
		//单选按钮赋值入院状态
		function onclickBoxryzt(id){
			if($('#zixing').is(':checked')){
				$('#statusFlgHidden').val(1);
			}if($('#husong').is(':checked')){
				$('#statusFlgHidden').val(2);
		   }if($('#qiangjiu').is(':checked')){
				$('#statusFlgHidden').val(3);
		   }
		   
		}
		//单选按钮赋值卧床
		function onclickBoxwo(id){
			if($('#banwo').is(':checked')){
				$('#clinostatismFlgHidden').val(1);
			}if($('#xiuke').is(':checked')){
				$('#clinostatismFlgHidden').val(2);
			}if($('#chuliwu').is(':checked')){
				$('#clinostatismFlgHidden').val(null);
			}
			
		}
		//单选按钮赋值禁食
		function onclickBoxjs(id){
			if($('#jinshi').is(':checked')){
				$('#dietFlgHidden').val(1);
			}if($('#shi').is(':checked')){
				$('#dietFlgHidden').val(2);
			}
			
		}
		 //单选按钮赋值贵重药品
		function onclickBoxgz(id){
			if($('#yg').is(':checked')){
				$('#drugFlgHidden').val(1);
			}if($('#buyong').is(':checked')){
				$('#drugFlgHidden').val(0);
			}
			
		}
		 //单选按钮赋值手术类型
		function onclickBoxlx(id){
			if($('#da').is(':checked')){
				$('#opstypeFlgHidden').val(1);
			}if($('#zhong').is(':checked')){
				$('#opstypeFlgHidden').val(2);
			}if($('#xiao').is(':checked')){
				$('#opstypeFlgHidden').val(3);
			}else if($('#shoushuwu').is(':checked')){
				$('#opstypeFlgHidden').val(null);
			}
		}
		function onclickBoxx(id){
			if($('#yb').is(':checked')){
				$('#bloodqtyFlgHidden').val(1);
			}else if($('#tebie').is(':checked')){
				$('#bloodqtyFlgHidden').val(2);
			}else if($('#xwu').is(':checked')){
				$('#bloodqtyFlgHidden').val(null);
			}
			
		}
		/**
		 * 病室(病区)弹出事件高丽恒
		 * 2016-03-23 09:32
		 */
		function openSysDepartmentWin(){
			var keshi = $('#keshi').combobox('getValue');
			var tempWinPath = "<%=basePath%>popWin/pWWardDepartment/pWWardDepartment.action?nameTmp=SysDepartment&textId=reportBedward&keshic="+keshi;
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
		}
		//验证需要需要提交的表单
	  	function submitsubmit(){
			 zjhm=$('#zjhm').textbox('getValue');
			 if(statedate!="yes"){
				 $.messager.alert('提示','请选择需要办理住院证明的患者');
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				 return false;
			 }
			 if($('#patientCertificatestype').combobox('getText')=='身份证'){
				 if(!isIdCardNo(zjhm)){
					 $('#zjhm').textbox('setValue','');
					 return false;
				 }else{
					 saveZ();
				 }
			 }else{
				 saveZ();
			 }
		}
		//提交保存住院证明
		function saveZ(){
			$.ajax({
 				 url:"<%=basePath%>inpatient/InpatientProof/queryInpatientInfo.action",
 				 data:{mid:mid},
 				 async:false,
 				 success:function(data){
 					$.messager.progress('close');
 					 var dataResult=eval("("+data+")");
 					 if(dataResult.resMsg=="yesRI"){
 						$.messager.alert('提示',dataResult.resCode);
 						setTimeout(function(){
 							$(".messager-body").window('close');
 						},3500);
 						clear();
 						var item3 = $('#listlist').datagrid('getRows');    
 			            for (var i = item3.length - 1; i >= 0; i--) {    
 			                var index = $('#listlist').datagrid('getRowIndex', item3[i]);    
 			                $('#listlist').datagrid('deleteRow', index);    
 			            }
 					 }else if(dataResult.resMsg=="success"){
 							birthday = $('#csrq').val();
 							saveaaaaaa();
 					 }else if(dataResult.resMsg="yes0"){
 						$.messager.alert('提示',dataResult.resCode);
 						setTimeout(function(){
 							$(".messager-body").window('close');
 						},3500);
 						clear();
 						return ;
 					 }
 				 }
 			 });
		}
		// 验证身份证格式是否正确
		function isIdCardNo(idCard) {
			var id = idCard;
			var id_length = id.length;

			if (id_length == 0) {
				$.messager.alert("操作提示", "请输入身份证号码!");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}

			if (id_length != 15 && id_length != 18) {
				$.messager.alert("操作提示", "身份证号长度应为15位或18位！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}

			if (id_length == 15) {
				yyyy = "19" + id.substring(6, 8);
				mm = id.substring(8, 10);
				dd = id.substring(10, 12);

				if (mm > 12 || mm <= 0) {
					$.messager.alert("操作提示", "输入身份证号,月份非法！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}

				if (dd > 31 || dd <= 0) {
					$.messager.alert("操作提示", "输入身份证号,日期非法！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}

				birthday = yyyy + "-" + mm + "-" + dd;

				if ("13579".indexOf(id.substring(14, 15)) != -1) {
					sex = "1";
				} else {
					sex = "2";
				}
			} else if (id_length == 18) {
				if (id.indexOf("X") > 0 && id.indexOf("X") != 17 || id.indexOf("x") > 0
						&& id.indexOf("x") != 17) {
					$.messager.alert("操作提示", "身份证中\"X\"输入位置不正确！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}

				yyyy = id.substring(6, 10);
				if (yyyy > 2200 || yyyy < 1900) {
					$.messager.alert("操作提示", "输入身份证号,年份非法！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}

				mm = id.substring(10, 12);
				if (mm > 12 || mm <= 0) {
					$.messager.alert("操作提示", "输入身份证号,月份非法！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}

				dd = id.substring(12, 14);
				if (dd > 31 || dd <= 0) {
					$.messager.alert("操作提示", "输入身份证号,日期非法！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}

				birthday = id.substring(6, 10) + "-" + id.substring(10, 12) + "-"
						+ id.substring(12, 14);
				if ("13579".indexOf(id.substring(16, 17)) > -1) {
					sex = "1";
				} else {
					sex = "2";
				}
			}

			return true;
		}
		//渲染收费状态
		function functionState(value,row,index){
			if(value==1){
				return index+1;
			}else{
				return '<span style=\'color:red;\'>未</span>'
			}
		}
		//渲染科室		
		function functionDept(value,row,index){
			if(value!=null&&value!=''){
				return deptMap[value];
			}
		}
		//渲染性别		
		function sexRend(value,row,index){
			if(value!=null&&value!=''){
				return sexMap.get(value);
			}
		}
		//渲染部门
		function functionType(value,row,index){
			if(value!=null&&value!=''){
				return typeMap[value];
			}
		}	
		//渲染人员
		function functionEmp(value,row,index){
			if(value!=null&&value!=''){
				return empMap[value];
			}
		}
		//保存方法
		function saveaaaaaa(){
			 $.messager.progress({text:"保存中,请稍等......",modal:true});
			 $('#editFormss').form('submit', {
			        url:"<%=basePath%>inpatient/InpatientProof/editInfoPreregisters.action",
			   		onSubmit:function(){ 
			  		 	if(!$('#editFormss').form('validate')){
							$.messager.show({  
							     title:'提示信息' ,   
							     msg:'验证没有通过,不能提交表单!'  
							}); 
							 $.messager.progress('close');
							 return false ;
					     }
					 },
					success:function(data){ 
						var data = eval('(' + data + ')');  
						var mid=data;
						if($('#mainId').val()==null||$('#mainId').val()==""){
							$.messager.confirm('确认','是否要打印住院证明？',function(r){    
							    if (r){
							    	printout(mid.id,mid.reportBirthday);    
							    	clear();
							    	var item3 = $('#listlist').datagrid('getRows');    
			  			            for (var i = item3.length - 1; i >= 0; i--) {    
			  			                var index = $('#listlist').datagrid('getRowIndex', item3[i]);    
			  			                $('#listlist').datagrid('deleteRow', index);    
			  			            }
							    }else{
							    	clear();
							    	var item3 = $('#listlist').datagrid('getRows');    
			  			            for (var i = item3.length - 1; i >= 0; i--) {    
			  			                var index = $('#listlist').datagrid('getRowIndex', item3[i]);    
			  			                $('#listlist').datagrid('deleteRow', index);    
			  			            }
							    }    
							}); 
						}
						$.messager.progress('close');
						clear();
						var item3 = $('#listlist').datagrid('getRows');    
  			            for (var i = item3.length - 1; i >= 0; i--) {    
  			                var index = $('#listlist').datagrid('getRowIndex', item3[i]);    
  			                $('#listlist').datagrid('deleteRow', index);    
  			            }
				 	},
					error : function(data) {
						$.messager.alert('提示','未知错误请联系管理员');
						return;
					}
			  }); 
		}
		function submitsubmit1(){
			var birthday = $('#csrq').val();
			var mid=$('#mainId').val();
			printout(mid,birthday);
		}
		
		//保存的时候打印
		function printout(mid,birthday){
			if(mid==""||mid==null){
				$.messager.alert('提示','请选择要打印的患者证明记录');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				var age=DateOfBirth(birthday.split(' ')[0])
				var age=age.get("nianling")+age.get('ageUnits');
			 	var timerStr = Math.random();
			 	window.open ("<c:url value='/inpatient/InpatientProof/iReportInpatientProof.action?randomId='/>"+timerStr+"&CERTIFICATES_NO="+mid+"&age="+encodeURIComponent(encodeURIComponent(age))+"&fileName=zhuyuanzheng",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}
		}
		
		//直接打印
		function print(){
			var birthday = $('#csrq').val();
			var age =$('#age').textbox('getValue')+$('#nianlingdanwei1').val()+'';
			var mid=$('#mainId').val();
			if(mid==''||mid==null||mid=='undefind'){
				$.messager.alert('提示','请查询要打印的患者证明记录');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return ;
			}
			var timerStr = Math.random();
			window.open ("<c:url value='/inpatient/InpatientProof/iReportInpatientProof.action?randomId='/>"+timerStr+"&CERTIFICATES_NO="+mid+"&age="+encodeURIComponent(encodeURIComponent(age))+"&fileName=zhuyuanzheng",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
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
<body style="margin: 0px;padding: 0px;">
<div class="easyui-layout" style="width:100%;height:100%;text-align:center;">
   <div data-options="region:'center'" style="border-top:0">
		<div id="divLayout" class="easyui-layout" fit=true >
			<div data-options="region:'center',split:false,border:false" style="width:50%;height:80%;text-align:center;">
			 <form id="editFormss" method="post" style="margin-left:auto;margin-right:auto;"  data-options="novalidate:false">
				 <fieldset style="width:854px;border:0px;margin-left:auto;margin-right:auto;">
						<div style="margin-left:auto;margin-right:auto;">
							<table  style="border:0px solid black;margin-left:auto;margin-right:auto">
								<tr >
									<td style="padding: 5px 0px;font-size:14px;">病历号：
										<input class="easyui-textbox" id="medicalrecordId"  style="width:180px;" data-options="prompt:'输入病历号回车查询'"/>
       									<shiro:hasPermission name="${menuAlias}:function:query">
							        		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 30px" >查询</a>
	        							</shiro:hasPermission>
	        							<shiro:hasPermission name="${menuAlias}:function:readCard">
	        								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
	        							</shiro:hasPermission>
							        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
							        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
	        							</shiro:hasPermission>
							        </td>
								</tr>
							</table>
						</div>
						<div id="p" class="easyui-panel" title="挂号信息" style="margin-left:auto;margin-right:auto;">
							<table id="listlist" class="easyui-datagrid" style="width:100%;margin-left:auto;margin-right:auto;" data-options="striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
								<thead >
									<tr>
									<input type="hidden" id="createUser" name="createUser" >
	  								<input type="hidden" id="createDept" name="createDept"  >
	  								<input type="hidden" id="createTime" name="createTime" />
										<th data-options="field:'state',width:'4%',formatter:functionState">
										<th data-options="field:'medicalrecordId',hidden:'true'">
										<th data-options="field:'patientName',width:'10%'">
											姓名
										</th>																			
										<th data-options="field:'patientSex',width:'5%',formatter:sexRend ">性别</th>																			
										<th data-options="field:'no',width:'18%'">门诊卡号</th>																			
										<th
											data-options="field:'dept',width:'15%',formatter: functionDept">
											挂号科室
										</th>
										<th
											data-options="field:'expxrt',width:'15%',formatter:functionEmp">
											挂号专家</th>
										<th
											data-options="field:'type',width:'15%',formatter:functionType">
											挂号类别
										</th>
										<th
											data-options="field:'rdate',width:'20%'">
											挂号日期
										</th>
									</tr>
								</thead>
							</table>
						</div>
				 </fieldset>	   
			       <fieldset style="width:899px;border:0px;margin-left:auto;margin-right:auto;">
						<table id="tabletable" style="font-size: 14px;margin-left:auto;margin-right:auto;" class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black">
							<tr>
								  <input  type="hidden" id="mainId" name ="id"/>
								<td class="honry-lable">
										姓名：</td>
								<td>
									<input id="patientNamesss" class="easyui-textbox" readonly="readonly" name="patientName"/>
								</td>
								<td class="honry-lable">性别：</td>
								<td>
									<input id="sex" class="easyui-combobox" name="reportSex" data-options="url : '<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ',valueField:'encode',textField:'name'"  />
								</td>
							</tr>
				  			<tr>
					  			<td class="honry-lable">
									年龄：</td>
								<td>
									<input id="age" class="easyui-textbox" name="reportAge"  readonly="readonly"/>
									<span id="nianlingdanwei" ></span>
									<input id="nianlingdanwei1" name="reportAgeunit" type="hidden"/>
								</td>
								<td class="honry-lable">
									出生日期：</td>
								<td>
									<input id="csrq" name="reportBirthday" onchange="jusnl()" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
				 		  </tr>
				 		 <tr>
								<td class="honry-lable" style="width: 25%">证件类型：</td>
								<td style="width: 25%">
									    <input id="patientCertificatestype"  class="easyui-combobox" name="certificatesType"/> 
								</td>
								<td class="honry-lable">
										证件号码：</td>
								<td>
									<input id="zjhm" class="easyui-textbox" name="certificatesNo"  />
								</td>
				 		 </tr>
						<tr>
							<td class="honry-lable">地址：</td>
							<td>
								<input id="address" class="easyui-textbox" name="reportAddress" >
							</td>
							<td class="honry-lable">
									合同单位：</td>
								<td>
									<input id="contractunit" class="easyui-combobox" name="contractUnit"  />
<!-- 									<a href="javascript:delSelectedData('contractunit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
								</td>
						</tr>
						<tr>
							<td class="honry-lable" >
										门诊号：</td>
							      <td>
										<input id="idcardNo" class="easyui-textbox" name="idcardNo" />
							      </td>
							<td class="honry-lable">
								住院科室：</td>
							<td>
								<input id="keshi" name="reportDept" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病历号：</td>
							<td >
								<input class="easyui-textbox" id="newMedicalrecordId" name="medicalrecordId" readonly="readonly">
							</td>
							<td class="honry-lable">
								开立医生：</td>
							<td>
									<input id="kzys" name="reportIssuingdoc" class="easyui-combobox" />
									<a href="javascript:delSelectedData('kzys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
						</tr>
						
							<td class="honry-lable">
								病区：
								</td>
							<td>
								<input id="reportBedward" class="easyui-combobox" name="reportBedward" data-options="required:true"/>
<!-- 								<a href="javascript:delSelectedData('reportBedward');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
							</td>
							
								<td class="honry-lable">输血数量：</td>
				    		<td>
					    		<input id="bloodqty" class="easyui-numberBox"  name="reportBloodqty"/>
				    		</td>
						<tr>
							<td class="honry-lable">住院约计天数：</td>
					    	<td >
					    		<input id="daycount" class="easyui-numberBox"name="inpatientDaycount"/>
				    		</td>
				    		<td class="honry-lable">开证日期：</td>
							<td>
								<input id="kzrq"  name="reportIssuingdate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
				    		
						</tr>
						<tr>
							<td class="honry-lable">备注：</td>
							<td colspan="3">
								<textarea class="easyui-textbox" style="width:90%;height:60px"id="beizhu" name="reportRemark" data-options="multiline:true"></textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">诊断：</td>
							<td colspan="3">
								<textarea class="easyui-textbox" style="width:90%;height:60px" id="zd" name="reportDiagnose" data-options="multiline:true"></textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">贵重药品：</td>
					    	<td>
						    	<input id="drugFlgHidden"  name="reportDrugflag" type="hidden"  value="${reportDrugflag }"/>
						    	<input type="radio" id="yg" onclick="javascript:onclickBoxgz(1)"  name="reportDrugflagg"  />用
						    	<input type="radio" id="buyong" onclick="javascript:onclickBoxgz(0)"  name="reportDrugflagg"  />不用
				    		</td>
				    		<td class="honry-lable">手术类型：</td>
				    		<td>
						    	<input id="opstypeFlgHidden" type="hidden"  name="reportOpstype" value="${reportOpstype }" />
						    	<input type="radio" id="da" onclick="javascript:onclickBoxlx(1)" name="reportOpstypee"  />大
						    	<input type="radio" id="zhong" onclick="javascript:onclickBoxlx(2)" name="reportOpstypee"  />中
						    	<input type="radio" id="xiao" onclick="javascript:onclickBoxlx(3)" name="reportOpstypee"  />小
						    	<input type="radio" id="shoushuwu" onclick="javascript:onclickBoxlx(4)" name="reportOpstypee"  />无
				    		</td>
				    	</tr>
					<tr>
						<td class="honry-lable">入院情况：</td>
				    	<td>
					    	<input id="situationFlgHidden" type="hidden" name="reportSituation" value="${reportSituation }"/>
					    	<input type="radio" id="weiji" onclick="onclickBoxryqk(1)"name="report" />危机
					    	<input type="radio" id="ji"   onclick="onclickBoxryqk(2)"   name="report" />急
					    	<input type="radio" id="yiban" onclick="onclickBoxryqk(3)" name="report" />一般
			    		</td>
						<td class="honry-lable">入院状态：</td>
				    	<td>
					    	<input id="statusFlgHidden" type="hidden" name="reportStatus" value="${reportStatus }"/>
					    	<input type="radio" id="zixing" onclick="javascript:onclickBoxryzt(1)" name="reportStatuss"/>自行
					    	<input type="radio" id="husong" onclick="javascript:onclickBoxryzt(2)" name="reportStatuss" />护送
					    	<input type="radio" id="qiangjiu" onclick="javascript:onclickBoxryzt(3)" name="reportStatuss"/>抢救
			    		</td>
					</tr>
					<tr>
						<td class="honry-lable">是否禁食：</td>
						<td>
							<input id="dietFlgHidden" type="hidden" name="reportDiet" value="${reportDiet }"/>
					    	<input type="radio" id="jinshi" onclick="javascript:onclickBoxjs(1)" name="reportDiett"/>禁食
					    	<input type="radio" id="shi" onclick="javascript:onclickBoxjs(2)" name="reportDiett"/>食
						</td>
						<td class="honry-lable">X光照相：</td>
				    	<td>
					    	<input id="bloodqtyFlgHidden" type="hidden"  name="reportXflag"  value="${reportXflag }" />
					    	<input type="radio" id="yb" onclick="javascript:onclickBoxx(1)"  name="reportXflagg" />一般
					    	<input type="radio" id="tebie" onclick="javascript:onclickBoxx(2)"  name="reportXflagg" />特别
					    	<input type="radio" id="xwu" onclick="javascript:onclickBoxx(3)"  name="reportXflagg" />无
					    </td>
					</tr>
					  <tr>
						<td class="honry-lable">入院处理：</td>
				    	<td colspan = "3">
					    	<input id="clinostatismFlgHidden" type="hidden" name="reportClinostatism" value="${reportClinostatism }"/>
					    	<input type="radio" id="banwo" onclick="javascript:onclickBoxwo(1)" name="reportClinostatismm"/>半卧
					    	<input type="radio" id="xiuke" onclick="javascript:onclickBoxwo(2)" name="reportClinostatismm"/>休克卧
					    	<input type="radio" id="chuliwu" onclick="javascript:onclickBoxwo(3)" name="reportClinostatismm"/>无
					    	　　
					    	　　
					    	
					    	　　　　
					    	　　
<%-- 					    	<input id="ShillflagFlgHidden" type="hidden" name="reportShillflag" value="${reportShillflag }"/> --%>
					    	<input type="checkbox" id="taijia" onclick="javascript:onclickBoxtaijia(1)"  name="reportShillflag"/>抬价
<%-- 					    	<input id="bathflagFlgHidden" type="hidden" name="reportBathflag" value="${reportBathflag }"/> --%>
					    	<input type="checkbox" id="muyu" onclick="javascript:onclickBoxmuyu(2)"  name="reportBathflag"/>沐浴
<%-- 					    	<input id="haircutFlgHidden" type="hidden" name="reportHaircut" value="${reportHaircut }" name="reportHaircutt"/> --%>
					    	<input type="checkbox" id="lifa" onclick="javascript:onclickBoxlifa(3)" name="reportHaircut" />理发
			    		</td>
					</tr>
					
			</table>
			</fieldset>
					</form>
					<div style="padding: 10px;height:80px; " >
					<shiro:hasPermission name="${menuAlias}:function:save">
						<a href="javascript:void(0)" id="baocun" class="easyui-linkbutton" onclick="submitsubmit()" data-options="iconCls:'icon-save'" style="margin-left:auto;margin-right:auto;" >保&nbsp;存&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:print">
						<a href="javascript:void(0)" id="dayin" class="easyui-linkbutton" onclick="print()" data-options="iconCls:'icon-printer'" style="margin-left:auto;margin-right:auto;" >打&nbsp;印&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</shiro:hasPermission>
					</div>
			</div>
		</div>
	</div>
</div>   	
</body>
</html>