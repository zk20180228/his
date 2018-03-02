<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院接诊</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;
		border-spacing: 0;
		border: 1px solid #95b8e7;
		width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: right;
		width: 10%;
	}
	.tableCss .TDinput {
	    width: 15%;
    }
</style>
<script type="text/javascript">
	var inNo='';
	var paykindMap=new Map();
	var empMap=new Map();
	var flag = false;	
	
	 var temperature ="";
    var mid ="";
	var reportBedwardId="";//病区
	var now="${now}"
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
		 month = "0" + month;
		}
   if (strDate >= 0 && strDate <= 9) {
			 strDate = "0" + strDate;
			 }
   var now1 = date.getFullYear() + seperator1 + month + seperator1 + strDate
		 + " " + date.getHours() + seperator2 + date.getMinutes()
		 + seperator2 + date.getSeconds();
	now1=now1.replace(/-(\d{1}\b)/g,'-0$1');
	now1=now1.replace(/:(\d{1}\b)/g,':0$1');
	 /**
	 *保存状态
	 */
	var statnum=null;
	//人员科室		
	 $(function(){
		 var deptFlg = "${deptFlg}"
		 console.log(deptFlg)
		 
		var str="${medicalId}"   //获得病历号
		$('#medicalrecordId').textbox('setValue',str);   
		$('#chiefDocCode').combobox('disable');
		$('#houseDocCode').combobox('disable');
		$('#chargeDocCode').combobox('disable');
		$('#dutyNurseCode').combobox('disable');
		if(str.length>0){
			queryMedicalrecordId();
		}
		bindBlackEvent('chuanghao',openBedWin,'easyui');
		/**
		*设置入院来源的默认值（默认值为 门诊）
		*/
		$('#source').combobox('setValue','01');
		
	    //下拉框初始化
		//1 性别
		$('#sex').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
		    valueField:'encode',    
		    textField:'name',
		    editable:true  
		});
		var sexHid = $('#sexHid').val();
		if(sexHid!=null&&sexHid!=""){
			$('#sex').combobox('setValue',sexHid);
		}
		//出生日期与年龄的联动
		$('#reportBirthday').blur(function(){
			    var date =$('#reportBirthday').val();
			    if(date!=null){
			    var arr=date.split("-");	
				var y = arr[0];
				var m = arr[1];
				var d = arr[2];
				var agedate=DateOfBirth(y+"-"+m+"-"+d);
				if(agedate.get("nianling")=="0"){
					$('#age').textbox('setValue',"0");
				}else{
					$('#age').textbox('setValue',agedate.get("nianling"));	
				}
				$('#reportAge').textbox('setValue',agedate.get("nianling"));	
				$('#reportAgeunit').text(agedate.get('ageUnits'));
				$('#reportAgeunit1').val(agedate.get('ageUnits'));
			    }
		});
		 //结算类别
		 $.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
		    async:false,
		    type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					paykindMap.put(type[i].encode,type[i].name);
				}
			}
		 });
		//3 国籍 country counryHid
		$('#country').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=country'/>",
		    valueField:'encode',    
		    textField:'name',
		    editable:true
		});
		var counryHid = $('#counryHid').val();
		if(counryHid!=null&&counryHid!=""){
			$('#country').combobox('setValue',counryHid);
		}
		//4 民族 nation nationHid
		$('#nation').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=nationality'/>",
		    valueField:'encode',    
		    textField:'name',
		    editable:true
		});
		var nationHid = $('#nationHid').val();
		if(nationHid!=null&&nationHid!=""){
			$('#nation').combobox('setValue',nationHid);
		}
		//5 职业 occupation  occupationHid
		$('#occupation').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>",
		    valueField:'encode',    
		    textField:'name',
		    editable:true
		});
		var occupationHid = $('#occupationHid').val();
		if(occupationHid!=null&&occupationHid!=""){
			$('#occupation').combobox('setValue',occupationHid);
		}
		//6 婚姻 marry  marryHid
		$('#marry').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=marry'/>",
		    valueField:'encode',    
		    textField:'name',
		    editable:true
		});
		var marryHid = $('#marryHid').val();
		if(marryHid!=null&&marryHid!=""){
			$('#marry').combobox('setValue',marryHid);
		}
		//入院途径
		$('#inAvenue').combobox({
			url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=brlydq",
			valueField:'encode',
			textField:'name'
		});
		//7 入院来源 source sourceHid
		$('#source').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=source'/>",
		    valueField:'encode',    
		    textField:'name',
		    editable:true
		});
		 /**
		 * 回车弹出住院医师选择窗口
		 * @author  wanxing
		 * @date 2016-03-28 17:51
		 * @version 1.0
		 */
		 bindEnterEvent('houseDocCode',popWinToHouseDoc,'easyui');//绑定回车事件
		/**
		 * 回车弹出主任医师选择窗口
		 * @author  wanxing
		 * @date 2016-03-28 17:53
		 * @version 1.0
		 */
		 bindEnterEvent('chiefDocCode',popWinToChiefDoc,'easyui');//绑定回车事件
		
		/**
		 * 回车弹出主任医师选择窗口
		 * @author  wanxing
		 * @date 2016-03-28 17:59
		 * @version 1.0
		 */
		 bindEnterEvent('chargeDocCode',popWinToChargeDoc,'easyui');//绑定回车事件
		 
		/**
		 * 回车弹出责任护士选择窗口
		 * @author  wanxing
		 * @date 2016-03-28 17:59
		 * @version 1.0
		 */
		 bindEnterEvent('dutyNurseCode',popWinToDutyNurse,'easyui');//绑定回车事件
		 
		 
		
		
		//绑定回车键查询
		bindEnterEvent('medicalrecordId',queryMedicalrecordId,'easyui');
		
		/**
		 * 回车弹出国籍选择窗口
		 * @author  wanxing
		 * @date 2016-03-22 17:40
		 * @version 1.0
		 */
		 bindEnterEvent('country',popWinToCountry,'easyui');//绑定回车事件
		
		 
	 /**
		 * 回车弹出民族选择窗口
		 * @author  wanxing
		 * @date 2016-03-22  17:50
		 * @version 1.0
		 */
		 bindEnterEvent('nation',popWinToNationality,'easyui');//绑定回车事件
		
	 /**
		 * 回车弹出职业选择窗口
		 * @author  wanxing
		 * @date 2016-03-22  17:55
		 * @version 1.0
		 */
		 bindEnterEvent('occupation',popWinToOccupation,'easyui');//绑定回车事件
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
	
	 function popWinToChargeDoc(){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=chargeDocCode&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToDutyNurse(){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=dutyNurseCode&employeeType=2";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	 }
	 function popWinToHouseDoc(){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=houseDocCode&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToChiefDoc(){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=chiefDocCode&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToNationality(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=nationality&textId=nation";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToCountry(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=country&textId=country";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 function popWinToOccupation(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=occupation&textId=occupation";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	//下拉框赋值
	function selectSetVal(nameAttr,valId){
	   $("#"+nameAttr+"").combobox('setValue',valId);
	} 
	function openBedWin(){
		 if($('#patientName').textbox('getValue')==null||$('#patientName').textbox('getValue')==''){
			$('#chuanghao').textbox('setValue','');
			$.messager.alert('提示','请先查询需要进行接诊的患者'); 
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return ;
		}
		$('#chuanghao').textbox('setValue',"");
		var tempWinPath = "<%=basePath%>inpatient/info/bedInfoList.action?reportBedwardId="+reportBedwardId;
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=0,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-65) +',scrollbars,resizable=no,toolbar=no')
	
	}
	//病历号查询查询
	function queryMedicalrecordId(){
		 var io=$('#medicalrecordId').textbox('getText');
		 clear();
		$('#medicalrecordId').textbox('setValue',io);
		 if(io ==''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		 }	
		 if(flag==false){
			//住院医生
			  $('#houseDocCode').combogrid({ 
			  		url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
			  		disabled : false,
			  		mode:'remote',
			  		panelWidth:450, 
					rownumbers : true,//显示序号 
					pagination : true,//是否显示分页栏
					fitColumns : true,//自适应列宽
					pageSize : 5,//每页显示的记录条数，默认为10  
					pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
					idField : 'name',
					textField : 'name',
					columns : [ [ {
						field : 'jobNo',
						title : '编号',
						width : 200
					}, {
						field : 'name',
						title : '姓名',
						width : 200,
					}, {
						field : 'deptName',
						title : '所在科室',
						width : 200
					}] ],
					onSelect: function (rowIndex, rowData){
						$('#houseDocCode1').val(rowData.jobNo);
						$('#houseDocName').val(rowData.name);
					}
		  	});
			 //主任医生
			$('#chiefDocCode').combogrid({ 
				url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
				disabled : false,
				mode:'remote',
				panelWidth:450, 
				rownumbers : true,//显示序号 
				pagination : true,//是否显示分页栏
				fitColumns : true,//自适应列宽
				pageSize : 5,//每页显示的记录条数，默认为10  
				pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
				idField : 'name',
				textField : 'name',
				columns : [ [ {
					field : 'jobNo',
					title : '编号',
					width : 200
				}, {
					field : 'name',
					title : '姓名',
					width : 200,
				}, {
					field : 'deptName',
					title : '所在科室',
					width : 200
				}] ],
				onSelect: function (rowIndex, rowData){
					$('#chiefDocCode1').val(rowData.jobNo);
					$('#chiefDocName').val(rowData.name);
				}
			});
			 //主治医生
			 $('#chargeDocCode').combogrid({ 
		  		url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
		  		disabled : false,
		  		mode:'remote',
		  		panelWidth:450, 
				rownumbers : true,//显示序号 
				pagination : true,//是否显示分页栏
				fitColumns : true,//自适应列宽
				pageSize : 5,//每页显示的记录条数，默认为10  
				pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
				idField : 'name',
				textField : 'name',
				columns : [ [ {
					field : 'jobNo',
					title : '编号',
					width : 200
				}, {
					field : 'name',
					title : '姓名',
					width : 200,
				}, {
					field : 'deptName',
					title : '所在科室',
					width : 200
				}] ],
				onSelect: function (rowIndex, rowData){
					$('#chargeDocCode1').val(rowData.jobNo);
					$('#chargeDocName').val(rowData.name);
				}
			});
			 //责任护士
			 $('#dutyNurseCode').combogrid({ 
					url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=hs",
					disabled : false,
					mode:'remote',
					panelWidth:450, 
					rownumbers : true,//显示序号 
					pagination : true,//是否显示分页栏
					fitColumns : true,//自适应列宽
					pageSize : 5,//每页显示的记录条数，默认为10  
					pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
					idField : 'name',
					textField : 'name',
					columns : [ [ {
						field : 'jobNo',
						title : '编号',
						width : 200
					}, {
						field : 'name',
						title : '姓名',
						width : 200,
					}, {
						field : 'deptName',
						title : '所在科室',
						width : 200
					}] ],
					onSelect: function (rowIndex, rowData){
						$('#dutyNurseCode1').val(rowData.jobNo);
						$('#dutyNurseName').val(rowData.name);
					}
					
			});
			 flag = true;
		 }
		 $('#chiefDocCode').combobox('enable');
		 $('#houseDocCode').combobox('enable');
		 $('#chargeDocCode').combobox('enable');
		 $('#dutyNurseCode').combobox('enable');
		 $('#baocun').linkbutton({disabled:false});
		 $.messager.progress({text:'正在查询请稍候......',modal:true});
		 $.ajax({
			 url:'<%=basePath%>inpatient/admission/getMedById.action?medicalrecordId='+io, 
			  success:function(patlist){ 
				  $.messager.progress('close');
				 if(patlist.length>1){
				 	$("#diaInpatient").window('open');
					$("#infoDatagrid").datagrid({
						 url:'<%=basePath%>inpatient/admission/getMedById.action?menuAlias=${menuAlias}&medicalrecordId='+io, 
							 columns:[[    
								        {field:'inpatientNo',title:'住院流水号',width:'25%',align:'center'} ,    
								        {field:'medicalrecordId',title:'病历号',width:'24%',align:'center'} ,  
								        {field:'reportSexName',title:'性别',width:'24%',align:'center'} ,
								        {field:'patientName',title:'姓名',width:'25%',align:'center'} ,   
								        {field:'remark',title:'年龄',width:'25%',align:'center'} 
								   ]] ,
						    onDblClickRow:function(rowIndex, rowData){
								inNo=rowData.inpatientNo;
						    	searchFrom(rowData.inpatientNo); 
								$("#diaInpatient").window('close');
						    }
						}); 
				 }else if(patlist.length==1){
					 inNo =patlist[0].inpatientNo;
					 searchFrom(patlist[0].inpatientNo);  
				 }else{
					 $.ajax({
						 url:'<%=basePath%>inpatient/admission/queryPatientStatInfo.action?medId='+io,
						 success:function(data){
							 if(data=="2"){
								 $.messager.alert('提示','该患者没有进行住院登记');
							 }else if(data=="1"){
								 $.messager.alert('提示','病历号有误,请重新输入');
							 }else if(data=="3"){
								 $.messager.alert('提示','未知错误请联系管理员');
							 }else{
								 var dataObj = data[0];
								 if(dataObj.patientName!=null&&dataObj.patientName!=""){
										if("${nurdept}"!=dataObj.nurseCellCode){
											$.messager.alert('提示','该患者不在当前登录病区下，不能进行住院接诊');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											$.messager.progress("close");
											return false;
										}
										reportBedwardId=dataObj.nurseCellCode;										
										//是否有婴儿
										if(dataObj.haveBabyFlag==1){
											$('#haveBabyFlag').prop("checked","checked");
										}
										inNo =dataObj.inpatientNo;
										mid=dataObj.oldMedicalrecordId;
										$('#mainId').val(dataObj.id);
										$('#inpatientNo').val(dataObj.inpatientNo);
										$('#postureDate').val(now1);
										
										$('#inState').val(dataObj.inState);
										$('#oldMedicalrecordId').val(dataObj.oldMedicalrecordId);
										$('#patientName').textbox('setValue',dataObj.patientName);
										selectSetVal('sex',dataObj.reportSex);
										$('#settlement').textbox('setValue',paykindMap.get(dataObj.paykindCode));
										$('#birthArea').textbox('setValue',dataObj.birthArea);
										selectSetVal('country',dataObj.counCode);
										selectSetVal('nation',dataObj.nationCode);
										var brithday = dataObj.reportBirthday.split(" ");
										$('#reportBirthday').val(brithday[0]);
										var ages=DateOfBirth(dataObj.reportBirthday);
										$('#reportAge').numberbox('setValue',ages.get("nianling"));
										$('#reportAgeunit').text(ages.get('ageUnits'));
										$('#reportAgeunit1').val(ages.get('ageUnits'));
										$('#dist').textbox('setValue',dataObj.dist);
										selectSetVal('occupation',dataObj.profCode);
										 //证件类型
										$('#certificatesType').val(dataObj.certificatesType);
											var type=$('#certificatesType').val();
											if(type=="3"){
												//证件号码
												$('#certificatesNo').numberbox('setText',dataObj.certificatesNo);
												$('#certificatesNo1').val(dataObj.certificatesNo);
											}else{
												//证件号码
												$('#certificatesNo').numberbox('setText',null);
												$('#certificatesNo1').val(dataObj.certificatesNo);
											}
//											$('#certificatesNo').numberbox('setValue',dataObj.certificatesNo);
										$('#workName').textbox('setValue',dataObj.workName);
										$('#workTel').textbox('setValue',dataObj.workTel);
										selectSetVal('marry',dataObj.mari);
										$('#home').textbox('setValue',dataObj.home);
										$('#homeTel').textbox('setValue',dataObj.homeTel);
										selectSetVal('source',dataObj.inSource);
										$('#inAvenue').combobox('setValue',dataObj.inAvenue);
										$('#comdration').combobox('setValue',dataObj.inCircs);
										
										$('#inDateId').val(dataObj.inDate);
										$('#remark').textbox('setValue',dataObj.remark);
										$('#diagName').textbox('setValue',dataObj.diagName);
										
										
										$('#feeInterval').textbox('setValue',dataObj.feeInterval);
										$('#bedIdNum').val(dataObj.bedId);//病床使用记录表主键ID
										$('#chuanghao').textbox('setValue',dataObj.bedName);//病床号（名称，只做显示用）
										$('#houseDocCode').combogrid('setValue',dataObj.houseDocName);
										$('#chargeDocCode').combogrid('setValue',dataObj.chargeDocName);
										$('#chiefDocCode').combogrid('setValue',dataObj.chiefDocName);
										$('#dutyNurseCode').combogrid('setValue',dataObj.dutyNurseName);
										     
										$.ajax({
											 url:'<%=basePath%>inpatient/admission/queryPostureInfo.action?medId='+io,
											 success:function(data){
												 	var pos = data[0];
												 	if(pos != null){
												 		$('#temperature').numberbox('setValue',pos.temperature);
														$('#pulse').numberbox('setValue',pos.pulse);
														$('#breath').numberbox('setValue',pos.breath);
														$('#pressure').textbox('setValue',pos.pressure);
														$('#weight').numberbox('setValue',pos.weight);
												 	}
											 }
										})
								 }
									 $.messager.alert('提示','该患者正在住院,无需进行接诊！');
									 $('#baocun').linkbutton({disabled:true});
									 setTimeout(function(){
										$(".messager-body").window('close');
									 },3500);
							 }
						 }
					 });
				 	}
				}
		 });
	}
	/**
	 * 表单查询
	 * @author  hedong
	 * @date 2015-08-12
	 * @version 1.0
	 */
	 
	function searchFrom(medicalrecordId) {
		//渲染员工
		$.ajax({
				url: "<%=basePath%>inpatient/admission/getEmpList.action", 
				async:false,
				type:'post',
				success: function(data) {
					if(data!=null&&data!=""){
						empMap=data;
					}		
				}
			});
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		var medId =$('#medicalrecordId').textbox('getValue')
		$('#medicalrecordId').textbox('setValue',medId);
		if(medicalrecordId){
						$.ajax({
							url:"<%=basePath%>inpatient/admission/queryInfoByPatientNo.action?medicalrecordId="+medicalrecordId,
							success: function(data) {
								var dataObj = data;
								if(dataObj.patientName!=null&&dataObj.patientName!=""){
									if("${nurdept}"!=dataObj.bingqu){
										$.messager.alert('提示','该患者不在当前登录病区下，不能进行住院接诊');
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										$.messager.progress("close");
										return false;
									}
									$.messager.progress("close");
									reportBedwardId=dataObj.bingqu;
									statnum="1";
									//是否有婴儿
									if(dataObj.haveBabyFlag==1){
										$('#haveBabyFlag').prop("checked","checked");
									}
									mid=dataObj.oldMedicalrecordId;
									$('#mainId').val(dataObj.id);
									$('#inpatientNo').val(dataObj.inpatientNo);
									$('#postureDate').val(now1);
									$('#bedIdNum').val(dataObj.bedId);//病床使用记录表主键ID
									if(dataObj.bednumber!=null&&dataObj.bednumber!=''){
										$('#chuanghao').textbox('setValue',dataObj.bedName);//病床号（名称，只做显示用）
										$('#roombedId').val(dataObj.bednumber);//隐藏域（病床表主键ID）
									}
									if(dataObj.bedName!='' && dataObj.bedName!=null){
										$.ajax({
											url:"<%=basePath%>inpatient/admission/isExistBed.action",
											data:{bed:dataObj.bedNo,bedWard:dataObj.businessBedward.id,medId:dataObj.inpatientNo},
											type:'post',
											success: function(data) {
												if(data=='no'){
												   $.messager.alert('提示','该患者预约的床位已被占用，请重新选择床位!')	;
												   $('#chuanghao').textbox('setValue','');
												}else if(data=='yes'){
													$('#chuanghao').textbox('setValue',dataObj.bedName);//病床号（名称，只做显示用）
													$('#roombedId').val(dataObj.bednumber);//隐藏域（病床表主键ID）
													$('#bedNo').val(dataObj.bedNo);
													$('#bedName').val(dataObj.bedName);
													$('#bedwardId').val(dataObj.businessBedward.id);
													$('#bedwardName').val(dataObj.businessBedward.bedwardName);
												}
											}
										});
									}
									$('#inState').val(dataObj.inState);
									$('#oldMedicalrecordId').val(dataObj.oldMedicalrecordId);
									$('#houseDocCode').combogrid('setValue',empMap[dataObj.houseDocCode]);
									$('#houseDocCode1').val(dataObj.houseDocCode);
									$('#houseDocName').val(dataObj.houseDocName);
									
									
									$('#patientName').textbox('setValue',dataObj.patientName);
									$('#patientInfoId').val(dataObj.patientInfoId);
									selectSetVal('sex',dataObj.sex);
									$('#blNumber').val($('#medicalrecordId').val());
									$('#settlement').html("");
									$('#settlement').textbox('setValue',paykindMap.get(dataObj.settlement));
									$('#birthArea').textbox('setValue',dataObj.birthArea);
									selectSetVal('country',dataObj.country);
									selectSetVal('nation',dataObj.nation);
									$('#reportBirthday').val(dataObj.reportBirthday);
									var ages=DateOfBirth(dataObj.reportBirthday);
									$('#reportAge').numberbox('setValue',ages.get("nianling"));
									$('#reportAgeunit').text(ages.get('ageUnits'));
									$('#reportAgeunit1').val(ages.get('ageUnits'));
									$('#dist').textbox('setValue',dataObj.dist);
									selectSetVal('occupation',dataObj.occupation);
									 //证件类型
									$('#certificatesType').val(dataObj.certificatesType);
										var type=$('#certificatesType').val();
										if(type=="3"){
											//证件号码
											$('#certificatesNo').numberbox('setText',dataObj.certificatesNo);
											$('#certificatesNo1').val(dataObj.certificatesNo);
										}else{
											//证件号码
											$('#certificatesNo').numberbox('setText',null);
											$('#certificatesNo1').val(dataObj.certificatesNo);
										}
									$('#workName').textbox('setValue',dataObj.workName);
									$('#workTel').textbox('setValue',dataObj.workTel);
									selectSetVal('marry',dataObj.marry);
									$('#home').textbox('setValue',dataObj.home);
									$('#homeTel').textbox('setValue',dataObj.homeTel);
									selectSetVal('source',dataObj.source);
									$('#inAvenue').combobox('setValue',dataObj.inAvenue);
									$('#comdration').combobox('setValue',dataObj.inCircs);
									
									$('#feeInterval').textbox('setValue',dataObj.feeInterval);
									$('#inDateId').val(dataObj.inDate);
									$('#remark').textbox('setValue',dataObj.remark);
									$('#diagName').textbox('setValue',dataObj.diagName);
								}else{
									$.messager.alert('提示','该患者没有进行住院登记，请检查病历号');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$.messager.progress("close");
								}
							}
						})
		}
	
	};
	/**
	 * 校验下拉
	 * @author  hedong
	 * @version 1.0
	 */
	function validateCheckBox(idAttr,textAttr){
	  var attrVal = document.getElementsByName(idAttr)[0].value;
	  if(attrVal==''){
	     $.messager.show({  
			title:'提示信息' ,   
			msg:textAttr+"不能为空,不能提交表单!"  
		 });
		 return false;
	  }
	  return true;
	}
		/**
		 * 表单提交
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
  	function checkBC(){
			var bed = $('bedwardId').val();
  			if(statnum!="1"){
  				$.messager.alert("提示","请重新查询患者进行登记");
  				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
  				return ;
  			}
			//是否有婴儿标志赋值
	  		if($('#haveBabyFlag').is(':checked')){
				$('#haveBabyFlag1').val('1');
			}else{
				$('#haveBabyFlag1').val('0');
			}
			
	  	//验证身份证号的格式
			var patientCertificatestype=$('#certificatesType').val();
			var certificatesNo=$('#certificatesNo').numberbox('getText');
			if(certificatesNo!=null&&certificatesNo!=""){
				if(!isIdCardNo(certificatesNo)){
					return;
				}
				$('#certificatesNo1').val(certificatesNo);
				$('#certificatesType').val("3");
			}
			var text1= $('#temperature').numberbox('getValue'); 
			if(text1.length>5){
				$.messager.alert('提示','体温输入超过指定长度请重新添加');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				$('#temperature').numberbox('setValue',"");
				return false;
			}
			var text2= $('#pulse').numberbox('getValue');
			if(text2.length>5){
				$.messager.alert('提示','脉搏输入超过指定长度请重新添加');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				$('#pulse').numberbox('setValue',"");
				return false;
			}
			var text3= $('#breath').numberbox('getValue');
			if(text3.length>5){
				$.messager.alert('提示','呼吸输入超过指定长度请重新添加');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				$('#breath').numberbox('setValue',"");
				return false;
			}
			var text4= $('#pressure').textbox('getValue');
			if(text4 ==null || text4==""){
				
			}else{
				var pre=text4.split("-");
				if(pre[0]==null||pre[0]==""||pre[1]==null||pre[1]==""){
					$.messager.alert('提示','请按格式输入血压范围，例如100-200');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
			}
			var text5= $('#weight').numberbox('getValue');
			if(text5.length>5){
				$.messager.alert('提示','体重输入超过指定长度请重新添加');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				$('#weight').numberbox('setValue',"");
				return false;
			}
			$('#houseDocName').val($('#houseDocCode').combogrid('getText'));

			$('#chiefDocName').val($('#chiefDocCode').combogrid('getText'));

			$('#chargeDocName').val($('#chargeDocCode').combogrid('getText'));

			$('#dutyNurseName').val($('#dutyNurseCode').combogrid('getText'));
  			
			var medicalrecordId = $('#medicalrecordId').val();
  			$('#editForm').form('submit',{
  				url:"<%=basePath%>inpatient/admission/saveAdmission.action",
		  		onSubmit:function(){ 
		  			if(!$('#editForm').form('validate')){
						$.messager.show({  
						     title:'提示信息' ,   
						     msg:'验证没有通过,不能提交表单!'  
						});
						   return false ;
				     }else{
				    	 $.messager.progress({text:'保存中，请稍后...',modal:true});
				     }
		  		},
		  		success:function(data){
		  			$.messager.progress('close');
					$('#oldmid').val(mid);
					statnum="2";
				    if(data=='0'){
				   	 //打印方法
				   	 spire();
				     $.messager.alert('通知','保存成功');
				     $("#medicalrecordId").textbox('getValue');
				    }else if(data=='1'){
				     $.messager.alert('提示','无患者住院登记信息！保存失败！','error');
				    }else if(data=='2'){
				     $.messager.alert('提示','无患者信息！保存失败！','error');
				    }else if(data=='3'){
				     $.messager.alert('提示','无法插入资料变更记录！保存失败！','error');
				    }else if(data=='4'){
				     $.messager.alert('提示','无法插入患者体征信息！保存失败！','error');
				    }else if(data=='5'){
				     $.messager.alert('提示','无法更新床位装填信息！保存失败！','error');
				    }
 				    $("#editForm").form('clear');	
		  		},
		  		error:function(date){
		  			$.messager.progress('close');
					$.messager.alert('提示','保存失败！','error');
		  		}
  			});
		}
	/**
	 * 清除页面填写信息
	 * @author  hedong
	 * @date 2015-08-12
	 * @version 1.0
	 */
	function clear(){
		$('#medicalrecordId').textbox('setValue','');
   	    $("#inState").val('');
   	    $("#oldMedicalrecordId").val('');
   	    $("#patientInfoId").val('');
   		$("#editForm").form('clear');
   		$("#feeInterval").textbox('setValue',"1");
   		//设置入院来源的默认值（默认值为 门诊）
		$('#source').combobox('setValue','01');
   		//设置入院情况为一般
   		$('#comdration').combobox('setValue',3);
   		inNo='';
	}
	//lyy  腕带打印
	function spire(){
		$.messager.confirm('确认','要打印腕带吗？',function(r){
			 if(r){
				 var timerStr = Math.random();
				 var medicalrecordId=inNo;
				 if(medicalrecordId!=null&&medicalrecordId!=''){
					 window.open ("<c:url value='/iReport/iReportPrint/iReportToSpireRecord.action?randomId='/>"+timerStr+"&medicalrecordId="+medicalrecordId+"&fileName=wd",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
					 clear();
				 }else{
					 $.messager.alert('提示','请先对患者进行住院接诊，再打印腕带');
				 }
			 }
		});
	}
	
	/**
	* 回车弹出病床号选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToBed(){
		
	 	 if($('#patientName').textbox('getValue')==null||$('#patientName').textbox('getValue')==''){
		$('#chuanghao').textbox('setValue','');
		$.messager.alert('提示','请先查询需要进行接诊的患者'); 
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}  
	popWinBedCallBackFn = function(node){
	    	$("#bedIdNum").val(node.id);
			 $("#chuanghao").textbox('setValue',node.bedName);
	};
	var tempWinPath = "<%=basePath%>popWin/popWinBusinessHospitalbed/toBusinessHospitalbedPopWin.action?textId=chuanghao&id="+reportBedwardId+"&type=1";
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
		+',scrollbars,resizable=yes,toolbar=yes')
	}
	
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="">
	<div id="divLayout" class="easyui-layout" fit=true>
		<div data-options="region:'center'" style="padding: 5px;border-top:0">
			<input id="oldmid" type="hidden" />
			<form id="editForm" method="post">
				<table width="100%" style="margin-top: 0px;">
					<tr>
						<td>
								<div style="height: 25px; line-height: 25px;padding:0px 0px 10px 0px">
									病历号：<input class="easyui-textbox" id="medicalrecordId" data-options="prompt:'输入病历号回车查询'" style="width: 200px;" />
										<shiro:hasPermission name="${menuAlias}:function:query">
											<a href="javascript:void(0)" onclick="queryMedicalrecordId()" class="easyui-linkbutton" iconCls="icon-search" style="margin:0px 0px 0px 30px" style="height: 22px;">查询</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="${menuAlias}:function:readCard">
											<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
		       							</shiro:hasPermission>
							        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
											<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
		       							</shiro:hasPermission>
										&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkBox" id="oneTimeFlg"
										onclick="" />一次性使用标识&nbsp;&nbsp;&nbsp;&nbsp; <input
										type="checkBox" id="oneTimeFlg" onclick="" checked="checked" />是否打印诊断&nbsp;&nbsp;
								</div>
						</td>
					</tr>
					<tr>
						<td><input type="hidden" name="inState" id="inState" value="" />
						<%--住院状态 用于页面判断是否可点击保存操作 只有登记状态时才能做接诊 --%> 
						<input type="hidden" name="oldMedicalrecordId" id="oldMedicalrecordId" value="" />
						<%--病历号 用于更新患者信息表 --%> 
						<input type="hidden" name="patientInfoId" id="patientInfoId" value="" />
						<%--住院主表id 用于住院主表信息 的更新 --%> 
						<input type="hidden" id="sexHid" value=""> <input type="hidden" id="mainId" name="id" />
						<%--主键ID --%> 
						<input type="hidden" id="inpatientNo" name="inpatientNo" />
						<%--住院流水号 --%>
								<table class="tableCss">
									<tr>
										<td colspan="8">基本信息</td>
									</tr>
									<tr>
										<td class="TDlabel">姓名：</td>
										<td class="TDinput">
											<input class="easyui-textbox" id="patientName"
											name="patientName" data-options="required:true"
											missingMessage="请输入姓名" /></td>
										<td class="TDlabel">性别：</td>
										<td class="TDinput"><input id="sex" name="sex"
											data-options="required:true"></td>
										<td class="TDlabel">出生日期：</td>
										<td class="TDinput">
											<input id="reportBirthday" name="reportBirthday" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
											</td>
										<td class="TDlabel">年龄：</td>
										<td class="TDinput"><input class="easyui-numberbox"
											id="reportAge" name="reportAge" readonly="readonly"
											data-options="required:true" /> <span id="reportAgeunit"></span>
											<input id="reportAgeunit1" name="reportAgeunit" type="hidden" />
										</td>
									</tr>
									<tr>
										<td class="TDlabel">国籍：</td>
										<td class="TDinput"><input type="hidden" id="counryHid"
											value=""> <input id="country" name="country"><a
											href="javascript:delSelectedData('country');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
										<td class="TDlabel">身份证号：</td>
										<td ><input class="easyui-numberbox"
											id="certificatesNo" name="certificatesNo1"/></td>
										<input type="hidden"  id="certificatesNo1"  name="certificatesNo"  />
									    <input type="hidden" id="certificatesType"  name="certificatesType"  />
										<td class="TDlabel">民族：</td>
										<td class="TDinput"><input type="hidden" id="nationHid"
											value=""> <input id="nation" name="nation"
											data-options="required:true"> <a
											href="javascript:delSelectedData('nation');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
										<td class="TDlabel">职业：</td>
										<td class="TDinput"><input type="hidden"
											id="occupationHid" value=""> <input id="occupation"
											name="occupation"> <a
											href="javascript:delSelectedData('occupation');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
									</tr>
									<tr>
										<td class="TDlabel">婚姻状况：</td>
										<td class="TDinput"><input type="hidden" id="marryHid"
											value=""> <input id="marry" name="marry"></td>
											<td class="TDlabel">结算方式：</td>
										<td class="TDinput"><input id="settlement"
											name="settlement" class="easyui-textbox" readonly></td>
										<td class="TDlabel">籍贯：</td>
										<td class="TDinput" colspan="3"><input class="easyui-textbox"
											id="dist" name="dist" value="" missingMessage="请输入籍贯" style="width: 60%;"/></td>
									</tr>
									<tr>
										<td class="TDlabel">家庭电话：</td>
										<td class="TDinput"><input class="easyui-numberbox"
											id="homeTel" name="homeTel" value="" missingMessage="请输入家庭电话" /></td>
										<td class="TDlabel">家庭地址：</td>
										<%--户口家庭住址--%>
										<td colspan="5"><input class="easyui-textbox" id="home"
											name="home" value="" data-options="required:true,"
											missingMessage="请输入家庭地址" style="width:50%;" /></td>
									</tr>
									<tr>
										<td class="TDlabel">单位电话：</td>
										<%--工作单位电话--%>
										<td class="TDinput"><input class="easyui-numberbox"
											id="workTel" name="workTel" value="" missingMessage="请输入单位电话" /></td>
										<td class="TDlabel">工作单位：</td>
										<td colspan="5"><input class="easyui-textbox"
											id="workName" name="workName" value=""
											 missingMessage="请输入工作单位" style="width:50%;"/></td>
									</tr>
									<tr>
										<td colspan="8">登记信息</td>
									</tr>
									<tr>
										<td class="TDlabel">入院来源：</td>
										<td class="TDinput">
<!-- 										<input type="hidden" id="sourceHid" value="">  -->
											<input id="source" name="source" class="easyui-combobox" value="01"
											data-options="required:true"></td>
										<td class="TDlabel">入院途径：</td>
										<td class="TDinput"><input class="easyui-combobox"
											id="inAvenue" name="inAvenue" data-options="required:true"
											missingMessage="请输入入院途径" /></td>
										<td class="TDlabel">入院情况：</td>
										<td colspan="3"><select id="comdration"
											class="easyui-combobox"  name="inCircs"
											data-options="required:true" style="width: 150px">
												<option value="3">一般</option>
												<option value="2">急</option>
												<option value="1">危</option>
										</select></td>
									</tr>
									<tr>
										<td class="TDlabel">入院日期：</td>
										<td class="TDinput">
											<input id="inDateId" name="inDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
											</td>
										<td class="TDlabel">备注：</td>
										<td colspan="5"><input class="easyui-textbox" id="remark"
											name="remark" style="width:50%;" /></td>
									</tr>
									<tr>
										<td class="TDlabel">是否有婴儿：</td>
										<td class="TDinput"><input type="checkBox"
											id="haveBabyFlag" name="haveBabyFlag1" /></td>
											<input type="hidden"
											id="haveBabyFlag1" name="haveBabyFlag" />
										<td class="TDlabel">入院诊断：</td>
										<%--诊断名称--%>
										<td colspan="5"><input class="easyui-textbox"
											 rows="2" cols="125" id="diagName"
											name="diagName" data-options="required:true"
											missingMessage="请输入入院诊断" style="width:50%;" /> <!--	    			                	<input class="easyui-textbox" id="diagName" name="diagName" value="" data-options="required:true,width:425" missingMessage="请输入入院诊断"/>-->
										</td>
									</tr>
									<tr>
										<td colspan="8">床位信息</td>
									</tr>
									<tr>
										<td class="TDlabel">床费间隔：</td>
										<td class="TDinput"><input class="easyui-textbox"
											id="feeInterval" name="feeInterval" value="1"
											data-options="required:true" readly="readonly"
											missingMessage="请输入床费间隔" /></td>
										<td class="TDlabel">床位号：</td>
										<td class="TDinput"><input class="easyui-textbox"
											id="chuanghao" value=""
											data-options="required:true,prompt:'空格选择床位'"
											missingMessage="请输入床位号" />
											 <input id="bedIdNum" name="bedId"type="hidden">
											 <input id="roombedId" name="bedName1" type="hidden">
											 <input id="bedNo" name="bedNo" type="hidden">
											 <input id="bedName" name="bedName" type="hidden">
											 <input id="bedwardId" name="businessBedward.id" type="hidden">
											 <input id="bedwardName" name="businessBedward.bedwardName" type="hidden">
									    </td>
										<td class="TDlabel">住院医生：</td>
										<td colspan="5"><input id="houseDocCode"
											name="houseDocCode1" class="easyui-combobox" /> <a
											href="javascript:delSelectedData('houseDocCode');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a>
											<input id="houseDocCode1"
											name="houseDocCode" type="hidden"/>
											<input id="houseDocName"
											name="houseDocName" type="hidden"/>
											</td>
									</tr>
									<tr>
										<td class="TDlabel">主任医生：</td>
										<td class="TDinput"><input id="chiefDocCode"
											name="chiefDocCode1" class="easyui-combobox" /> <a
											href="javascript:delSelectedData('chiefDocCode');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a>
											<input id="chiefDocCode1"
											name="chiefDocCode" type="hidden"/>
											<input id="chiefDocName"
											name="chiefDocName" type="hidden"/>
											</td>
										<td class="TDlabel">主治医生：</td>
										<td class="TDinput"><input id="chargeDocCode"
											name="chargeDocCode1" class="easyui-combobox" /> <a
											href="javascript:delSelectedData('chargeDocCode');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a>
											<input id="chargeDocCode1"
											name="chargeDocCode" type="hidden"/>
											<input id="chargeDocName"
											name="chargeDocName" type="hidden"/>
											</td>
										<td class="TDlabel">责任护士：</td>
										<td colspan="5"><input id="dutyNurseCode"
											name="dutyNurseCode1" class="easyui-combobox" /> <a
											href="javascript:delSelectedData('dutyNurseCode');"
											class="easyui-linkbutton"
											data-options="iconCls:'icon-opera_clear',plain:true"></a>
											<input id="dutyNurseCode1"
											name="dutyNurseCode" type="hidden"/>
											<input id="dutyNurseName"
											name="dutyNurseName" type="hidden"/>
											</td>
									</tr>
									<tr>
										<td colspan="8">生命体征信息</td>
									</tr>
									<tr>
										<td class="TDlabel">体温(°C)：</td>
										<td class="TDinput"><input class="easyui-numberbox"
											id="temperature" name="temperature" value="" 
											data-options="precision:1" missingMessage="请输入体温" /></td>
										<td class="TDlabel">脉搏(次/分钟)：</td>
										<td sclass="TDinput"><input class="easyui-numberbox"
											id="pulse" name="pulse" value="" 
											missingMessage="请输入脉搏" /></td>
										<td class="TDlabel">呼吸(次/分钟)：</td>
										<td colspan="5"><input class="easyui-numberbox"
											id="breath" name="breath" value=""
										    missingMessage="请输入呼吸" /></td>
									</tr>
									<tr>
										<td class="TDlabel">血压(mmHg)：</td>
										<td class="TDinput"><input class="easyui-textbox"
											id="pressure" name="pressure" value=""
											data-options="prompt:'例如100-120'"
											missingMessage="请输入血压" /></td>
										<td class="TDlabel">体重(kg)：</td>
										<td class="TDinput"><input class="easyui-numberbox"
											id="weight" name="weight" value=""
										    missingMessage="请输入体重" /></td>
										<td class="TDlabel">录入时间：</td>
										<td colspan="5">
                                           <input id="postureDate" name="postureDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
											</td>
									</tr>
								</table>
						</td>
					</tr>
					<tr>
						<td style="text-align: center; padding-top: 10px;">
						<shiro:hasPermission
								name="${menuAlias}:function:save">
								<a href="javascript:checkBC();void(0)" class="easyui-linkbutton" id="baocun" 
									data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>&nbsp;&nbsp;
						</shiro:hasPermission> 
							<a href="javascript:clear();void(0)"
							data-options="iconCls:'icon-clear'" class="easyui-linkbutton">清&nbsp;屏&nbsp;</a>&nbsp;&nbsp;
							<a href="javascript:spire();" data-options="iconCls:'icon-printer'"
							id="print" class="easyui-linkbutton">打印腕带</a>&nbsp;&nbsp;</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:10" data-options="modal:true, closed:true">   
   		<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
</body>
</html>