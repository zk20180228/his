//Parameter

var curWwwPath=window.document.location.href;
var pathName=window.document.location.pathname;
var pos=curWwwPath.indexOf(pathName);
var localhostPaht=curWwwPath.substring(0,pos);
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var basePath=localhostPaht+projectName+"/";
var copyAdviceArr = new Array();
var colorInfoUnitsMap = new Map();//单位Map
var colorInfoUnitsList = null;//单位List
var colorInfoFrequencyMap = new Map();//频次Map
var colorInfoFrequencyList = null;//频次List
var colorInfoUsageMap = new Map();//用法Map
var colorInfoUsageList = null;//用法List
var colorInfoRemarksMap = new Map();//备注Map
var colorInfoRemarksList = null;//备注List
var colorInfoExeDeptMap = new Map();//执行科室Map
var colorInfoExeDeptList = null;//执行科室List
var colorInfoCheckpointMap = new Map();//检查部位Map
var colorInfoCheckpointList = null;//检查部位List
var colorInfoSampleTeptMap = new Map();//样本类型Map
var colorInfoSampleTeptList = null;//样本类型List
var judgeDocDrugGradeMap = null;
var judgeDrugGradeMap = null;
var judgeDrugGradeAllMap = null;
var groupVal = 1;
var judgeMap = null;
var sysTypeMap = null;
var colorInfoDiseasetMap = null;//疾病分类
var toolArr = new Array('首页','上一页','下一页','尾页','刷新');

//First Home Start

/**  
 *  
 * 查询患者信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function queryPatient(type){
	var idCardNo = $('#idCardNo').textbox('getText');
	if($.trim(idCardNo)==null||$.trim(idCardNo)==''){
		$.messager.alert('提示','请输入就诊卡号！',null,function(){});
		return;
	}
	$.ajax({
		type:"post",
		url:basePath+"outpatient/advice/queryPatientByidCardNo.action",
		data:{idCardNo:$.trim(idCardNo),type:type},
		success: function(dataMap) {
			cleanPatientInfo();
			if(dataMap.resMsg=='success'){
				if(dataMap.resCode.length==1){
					voluationPatientInfo(dataMap.resCode[0]);
				}else{
					$('#win').window('open');  
					$('#patientList').datagrid('loadData',dataMap.resCode);
				}
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		},
		error: function(){
			$.messager.alert('提示','患者信息获取失败！',null,function(){});
		}
	});
}

/**  
 *  
 * 诊出
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function passPatient(){
	var options = $('#adEndAdviceBtn').linkbutton('options');
	if(options.disabled){//非开立状态
		var clinicNo = $('#clinicNoTdId').html();
		if(clinicNoTdId==null||clinicNoTdId==''){
			$.messager.alert('提示','当前没有看诊患者！',null,function(){});
		}
		$.ajax({
			type:"post",
			url:basePath+"outpatient/advice/passPatient.action",
			data:{clinicNo:clinicNo},
			success: function(dataMap) {
				if(dataMap.resMsg=="success"){
					cleanPatientInfo();
					refWaitEt();
					refFulfilEt();
					if($('#isAuditing').val()=='true'){
						refAuditEt();
					}
				}
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			},
			error: function(){
				$.messager.alert('提示','请求失败！',null,function(){});
			}
		});
	}else{
		$.messager.alert('提示','医嘱开立状态无法进行诊出操作！',null,function(){});
	}
}

/**  
 *  
 * 刷新待诊树
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function refWaitEt(){
	$('#waitEt').tree('reload');
}

/**  
 *  
 * 刷新已诊树
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function refFulfilEt(){
	$('#fulfilEt').tree('reload');
}

/**  
 *  
 * 刷新已诊树
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function refAuditEt(){
	$('#auditEt').tree('reload');
}

/**  
 *  
 * 查询历史医嘱信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function queryHisAdvice(caseNo){
	$.ajax({
		type:"post",
		url:basePath+"outpatient/advice/queryHisAdvice.action",
		data:{patientNo:caseNo},
		success: function(dataMap) {
			if(dataMap.resMsg=='success'){
				$('#hisAdviceTree').tree({data:JSON.parse(dataMap.resCode)});
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		},
		error: function(){
			$.messager.alert('提示','请求失败！',null,function(){});
		}
	});
}

/**  
 *  
 * 患者信息赋值
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function queryPatientByclinicNo(clinicNo){
	$.ajax({
		type:"post",
		url:basePath+"outpatient/advice/queryPatientByclinicNo.action",
		data:{clinicNo:clinicNo},
		success: function(dataMap) {
			cleanPatientInfo();
			if(dataMap.resMsg=='success'){
				voluationPatientInfo(dataMap.resCode);
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		},
		error: function(){
			$.messager.alert('提示','患者信息获取失败！',null,function(){});
		}
	});
}

/**  
 *  
 * 清空患者信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function cleanPatientInfo(){
	$('#idCardNoInpId').val('');
	$('#nameTdId').html('');
	$('#deptTdId').html('');
	$('#docTdId').html('');
	$('#clinicNoTdId').html('');
	$('#assUnitTdId').html('');
	$('#caseNoTdId').html('');
	$('#sexTdId').html('');
	$('#ageTdId').html('');
	$('#orderNoTdId').html('');
}

/**  
 *  
 * 患者信息赋值
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function voluationPatientInfo(info){
	$('#idCardNoInpId').val(info.idCardNo);
	$('#nameTdId').html(info.name);
	$('#deptTdId').html(info.dept);
	$('#docTdId').html(info.doc);
	$('#clinicNoTdId').html(info.clinicNo);
	$('#assUnitTdId').html(info.assUnit);
	$('#caseNoTdId').html(info.caseNo);
	$('#sexTdId').html(info.sex);
	$('#ageTdId').html(info.age);
	$('#orderNoTdId').html(info.orderNo);
	queryAdvice(info.clinicNo);
	queryRecord(info.clinicNo);
	msgShow('提示','该患者【CT门诊--胸部X光片】暂未出结果！',20000);
}

/**  
 *  
 * 查询病历信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function queryRecord(clinicNo){
	clearRecord();
	$.ajax({
		type:"post",
		url:basePath+"outpatient/advice/queryRecord.action",
		data:{clinicNo:clinicNo},
		success: function(dataMap) {
			if(dataMap.resMsg=='success'){
				var outMedData = dataMap.resCode;
				if(outMedData!=null&&outMedData!=''){
					$('#patientNoCase').val(outMedData.patientNo);
					$('#maindesc').textbox('setValue',outMedData.maindesc);//主诉
					$('#allergichistory').textbox('setValue',outMedData.allergichistory);//过敏史
					$('#heredityHis').textbox('setValue',outMedData.heredityHis);//家族遗传史
					$('#presentillness').textbox('setValue',outMedData.presentillness);//现病史
					$('#temperature').numberbox('setValue',outMedData.temperature);//体温
					$('#pulse').numberbox('setValue',outMedData.pulse);//脉搏
					$('#breathing').numberbox('setValue',outMedData.breathing);//呼吸
					$('#bloodPressure').textbox('setValue',outMedData.bloodPressure);//血压
					$('#physicalExamination').textbox('setValue',outMedData.physicalExamination);//体格检查
					$('#historyspecil').textbox('setValue',outMedData.historyspecil);//病史和特征
					$('#advice').textbox('setValue',outMedData.advice);//医嘱建议
					$('#diagnose1').textbox('setValue',outMedData.diagnose1);//诊断检查
					$('#checkresult').textbox('setValue',outMedData.checkresult);//校验检查
					$('#diagnoseDate').val(outMedData.diagnoseDate);//发病日期
					//初诊，急诊，复诊 复选框回显 
					if(outMedData.diagnoseType==0||outMedData.diagnoseType==null){
						$('#cz').prop("checked", true); 
					}else if(outMedData.diagnoseType==1){
						$('#fz').prop("checked", true); 
					}else{
						$('#jz').prop("checked", true); 
					}
				}
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		},
		error: function(){
			$.messager.alert('提示','请求失败！',null,function(){});
		}
	});
}

/**  
 *  
 * 清空病历信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearRecord(){
	$('#cz').prop("checked", true); 
	$('#diagnoseDate').val('');
	$('#maindesc').textbox('setValue',"");
	$('#allergichistory').textbox('setValue',"");
	$('#heredityHis').textbox('setValue',"");
	$('#diagnose1').textbox('setValue',"");
	$('#presentillness').textbox('setValue',"");
	$('#temperature').textbox('setValue',"");
	$('#pulse').textbox('setValue',"");
	$('#breathing').textbox('setValue',"");
	$('#bloodPressure').textbox('setValue',"");
	$('#physicalExamination').textbox('setValue',"");
	$('#checkresult').textbox('setValue',"");
	$('#advice').textbox('setValue',"");
	$('#historyspecil').textbox('setValue',"");
}

/**  
 *  
 * 查询医嘱信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function queryAdvice(clinicNo){
	$("#adDgList").datagrid("loading");
	$.ajax({
		type:"post",
		url:basePath+"outpatient/advice/queryMedicalrecordHisList.action",
		data:{clinicNo:clinicNo},
		success: function(dataMap) {
			if(dataMap.resMsg=='success'){
				$("#adDgList").datagrid("loaded");
				insertRows("adDgList",true,dataMap.resCode);
				reloadRow();
			}else{
				$("#adDgList").datagrid("loaded");
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		},
		error: function(){
			$("#adDgList").datagrid("loaded");
			$.messager.alert('提示','请求失败！',null,function(){});
		}
	});
}

/**  
 *  
 * 添加医嘱信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function insertRows(id,bool,rows){
	if(bool){
		clearDgAdDgList(id);
	}
	for(var i=0;i<rows.length;i++){
		var lastIndex = $('#'+id).datagrid('appendRow',{
			id:rows[i].id,
			adviceNo:rows[i].adviceNo,
			colour:rows[i].colour,
			limit:rows[i].limit,//省市限制
			type:rows[i].type,//类型
			ty:rows[i].ty,//是否为药品
			adviceType:rows[i].adviceType,//医嘱类型
			adviceId:rows[i].adviceId,//医嘱名称Id
			adviceName:rows[i].adviceName,//医嘱名称Hid
			adviceNameView:rows[i].adviceNameView,//医嘱名称
			adPrice:rows[i].adPrice,//价格
			adPackUnitHid:rows[i].adPackUnitHid,//包装单位
			adMinUnitHid:rows[i].adMinUnitHid,//单位
			adDosaUnitHid:rows[i].adDosaUnitHid,//剂量
			adDosaUnitHidJudge:rows[i].adDosaUnitHidJudge,
			adDrugBasiHid:rows[i].adDrugBasiHid,//基本剂量
			specs:rows[i].specs,//规格
			sysType:rows[i].sysType,//系统类别
			drugType:rows[i].drugType,//药品类别
			minimumcost:rows[i].minimumcost,//最小费用代码
			packagingnum:rows[i].packagingnum,//包装数量
			nature:rows[i].nature,//药品性质
			ismanufacture:rows[i].ismanufacture,//自制药标志
			dosageform:rows[i].dosageform,//剂型
			isInformedconsent:rows[i].isInformedconsent,//是否知情同意书
			auditing:rows[i].auditing,
			group:rows[i].group,//组
			totalNum:rows[i].totalNum,//总量
			totalUnitHid:rows[i].totalUnitHid,//总单位Id
			totalUnitHidJudge:rows[i].totalUnitHidJudge,
			totalUnit:rows[i].totalUnit,//总单位
			dosageHid:rows[i].dosageHid,//每次用量
			dosageMin:rows[i].dosageMin,//每次剂量
			dosage:rows[i].dosage,//每次用量
			unit:rows[i].unit,//单位
			setNum:rows[i].setNum,//付数
			frequencyHid:rows[i].frequencyHid,//频次Id
			frequency:rows[i].frequency,//频次编码
			usageNameHid:rows[i].usageNameHid,//用法Id
			usageName:rows[i].usageName,//用法名称
			injectionNum:rows[i].injectionNum,//院注次数
			openDoctor:rows[i].openDoctor,//开立医生
			executiveDeptHid:rows[i].executiveDeptHid,//执行科室Id
			executiveDept:rows[i].executiveDept,//执行科室
			isUrgentHid:rows[i].isUrgentHid,//加急Id
			isUrgent:rows[i].isUrgent,//加急
			inspectPartId:rows[i].inspectPartId,//检查部位Id
			inspectPart:rows[i].inspectPart,//检查部位
			sampleTeptHid:rows[i].sampleTeptHid,
			sampleTept:rows[i].sampleTept,//样本类型
			minusDeptHid:rows[i].minusDeptHid,//扣库科室Id
			minusDept:rows[i].minusDept,//扣库科室
			remark:rows[i].remark,//备注
			inputPeop:rows[i].inputPeop,//录入人
			openDept:rows[i].openDept,//开立科室
			startTime:rows[i].startTime,//开立时间
			endTime:rows[i].endTime,//结束时间
			stopPeop:rows[i].stopPeop,//停止人
			isSkinHid:rows[i].isSkinHid,//是否需要皮试Id
			isSkin:rows[i].isSkin,//是否需要皮试
			splitattr:rows[i].splitattr,//拆分属性
			property:rows[i].property//拆分属性维护
		}).datagrid('getRows').length-1;
	}
}

/**  
 *  
 * 刷新行
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function reloadRow(){
	 var rows = $('#adDgList').datagrid('getRows');
	 for(var i=0;i<rows.length;i++){
		 $('#adDgList').datagrid('refreshRow',$('#adDgList').datagrid('getRowIndex',rows[i])); 
	 }
}

/**  
 *  
 * 清空datagrid数据
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearDgAdDgList(id){
	$('#'+id).datagrid('loadData',[]);
}

/**  
 *  
 * 开立医嘱
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adStartAdvice(){
	var pharmacyInputId = $('#pharmacyCombobox').combobox('getValue');
	if(pharmacyInputId==null||pharmacyInputId==''){
		$.messager.alert('提示',"请选择药房!",null,function(){});
		return;
	}
	if($('#clinicNoTdId').html()!=null&&$('#clinicNoTdId').html()!=''){
		adStartAdviceInt();
	}else{
		$.messager.alert('提示',"现诊信息不存在，请查询待诊信息！",null,function(){});
	}
}

/**  
 *  
 * 开立医嘱逻辑处理
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adStartAdviceInt(){
	$('#adEndAdviceBtn').menubutton('enable');//退出开立
	$('#adSaveAdviceBtn').menubutton('enable');//保存医嘱
	$('#adDelAdviceBtn').menubutton('enable');//删除医嘱
	$('#adHerbMedicineBtn').menubutton('enable');//草药查询
	$('#adAddGroupBtn').menubutton('enable');//添加组套
	$('#adCancelGroupBtn').menubutton('enable');//取消组套
	$('#adSaveGroupBtn').menubutton('enable');//保存组套
	$('#adLisResultBtn').menubutton('enable');//lis结果查询
	$('#adPacsResultBtn').menubutton('enable');//Pacs结果查询
	$('#adPrintAdviceBtn').menubutton('enable');//打印医嘱
	$('#adInspectionListBtn').menubutton('enable');//检查单
	$('#adInspectionListBtnJY').menubutton('enable');//检验单
	$('#indexTabs').tabs('select','医嘱');
	//获取组套信息
	$('#adStackTree').tree({	
		url:basePath+"nursestation/nurseCharge/stackAndStackInfoForTree.action",
	 	queryParams:{drugType:"2",type:"2"},
		onDblClick:function(node){
			if(node.attributes.isOPen=="1"){
				AdddilogModel("chinMediModleDivId","【"+node.text+"】详情信息",basePath+"outpatient/advice/viewStackInfoModle.action?stackId="+node.id,1100,400);
			}else{
				$(this).tree(node.state==='closed'?'expand':'collapse',node.target);
			}
		},
		onLoadSuccess:function(node, data){
			if(data.length>0){
				$('#adStackTree').tree('collapseAll');
			}
		}
	}); 
	$('#advEl').layout('expand','west');//展开组套信息
	//获取项目类别
	$('#adProjectTdId').combobox({disabled:false});
	$('#adProjectTdId').combobox('reload',basePath+"baseinfo/advice/querySystemTypesByTypeId.action?typeId="+$('#advName').val()); 
	//获得渲染数据
	getColorInfo();
}

/**  
 *  
 * 获得渲染数据
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function getColorInfo(){
	
	//获得系统类别
	$.ajax({
		url: basePath+"outpatient/advice/queryJudgeSysTypeAllMap.action",
		type:'post',
		success: function(date) {
			sysTypeMap = date;
		}
	});
	//获得对比变量
	$.ajax({
		url: basePath+"outpatient/advice/queryColorInfoJudgeMap.action",
		type:'post',
		success: function(date) {
			judgeMap = date;
		}
	});
	//获得单位
	$.ajax({
		url: basePath+"outpatient/advice/queryColorInfoUnitsList.action",
		type:'post',
		success: function(date) {
			colorInfoUnitsList = date;
			if(colorInfoUnitsList!=null&&colorInfoUnitsList.length>0){
				for(var i=0;i<colorInfoUnitsList.length;i++){
					colorInfoUnitsMap.put(colorInfoUnitsList[i].code+"_"+colorInfoUnitsList[i].organize,colorInfoUnitsList[i].name);
				}
			}
		}
	});
	//获得频次*
	$.ajax({
		url: basePath+"outpatient/advice/queryColorInfoFrequencyList.action",
		type:'post',
		success: function(date) {
			colorInfoFrequencyList = date;
			if(colorInfoFrequencyList!=null&&colorInfoFrequencyList.length>0){
				for(var i=0;i<colorInfoFrequencyList.length;i++){
					colorInfoFrequencyMap.put(colorInfoFrequencyList[i].encode,colorInfoFrequencyList[i].name);
				}
			}
		}
	});
	//获得用法
	$.ajax({
		url: basePath+"baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type" : "useage"},
		type:'post',
		success: function(date) {
			colorInfoUsageList = date;
			if(colorInfoUsageList!=null&&colorInfoUsageList.length>0){
				for(var i=0;i<colorInfoUsageList.length;i++){
					colorInfoUsageMap.put(colorInfoUsageList[i].encode,colorInfoUsageList[i].name);
				}
			}
		}
	});
	//获得备注 
	$.ajax({
		url: basePath+"baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type" : "opendocadvmark"},
		success: function(date) {
			colorInfoRemarksList = date;
			if(colorInfoRemarksList!=null&&colorInfoRemarksList.length>0){
				for(var i=0;i<colorInfoRemarksList.length;i++){
					colorInfoRemarksMap.put(colorInfoRemarksList[i].encode,colorInfoRemarksList[i].name);
				}
			}
		}
	});
	//获得执行科室
	$.ajax({
		url: basePath+"outpatient/advice/queryColorInfoExeDeptList.action",
		type:'post',
		success: function(date) {
			colorInfoExeDeptList = date;
			if(colorInfoExeDeptList!=null&&colorInfoExeDeptList.length>0){
				for(var i=0;i<colorInfoExeDeptList.length;i++){
					colorInfoExeDeptMap.put(colorInfoExeDeptList[i].deptCode,colorInfoExeDeptList[i].deptName);
				}
			}
		}
	});
	//获得检查部位 
	$.ajax({
		url: basePath+"baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type" : "checkpoint"},
		success: function(date) {
			colorInfoCheckpointList = date;
			if(colorInfoCheckpointList!=null&&colorInfoCheckpointList.length>0){
				for(var i=0;i<colorInfoCheckpointList.length;i++){
					colorInfoCheckpointMap.put(colorInfoCheckpointList[i].encode,colorInfoCheckpointList[i].name);
				}
			}
		}
	});
	//获得样本类型 
	$.ajax({
		url: basePath+"baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type" : "laboratorysample"},
		success: function(date) {
			colorInfoSampleTeptList = date;
			if(colorInfoSampleTeptList!=null&&colorInfoSampleTeptList.length>0){
				for(var i=0;i<colorInfoSampleTeptList.length;i++){
					colorInfoSampleTeptMap.put(colorInfoSampleTeptList[i].encode,colorInfoSampleTeptList[i].name);
				}
			}
		}
	});
	//获得医生职级和药品等级对照关系key药品等级编码value等级名称*
	$.ajax({
		url: basePath+"outpatient/advice/queryJudgeDocDrugGradeMap.action",
		type:'post',
		success: function(date) {
			judgeDocDrugGradeMap = date;
		}
	});
	// 获得药品等级key药品等级id value等级编码*
	$.ajax({
		url: basePath+"outpatient/advice/queryJudgeDrugGradeMap.action",
		type:'post',
		success: function(date) {
			judgeDrugGradeMap = date;
		}
	});
	//获得全部药品等级key药品等级id value等级名称*
	$.ajax({
		url: basePath+"outpatient/advice/queryJudgeDrugGradeAllMap.action",
		type:'post',
		success: function(date) {
			judgeDrugGradeAllMap = date;
		}
	});
	//获得医生可开立的全部特限药
	$.ajax({
		url: basePath+"outpatient/advice/querySpecialDrugMap.action",
		type:'get',
		success: function(dateMap) {
			specialDrugMap = dateMap;
		}
	});
	//获得疾病分类
	$.ajax({
		url: basePath+"baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=diseasetype",
		type:'post',
		success: function(date) {
			colorInfoDiseasetMap = date;
		}
	});
}

/**  
 *  
 * 获得当前时间
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function showTimeMedical() {
	var date = new Date();
	this.year = date.getFullYear();
	this.month = (date.getMonth() + 1) < 10 ? "0"
			+ (date.getMonth() + 1) : (date.getMonth() + 1);
	this.date = date.getDate() < 10 ? "0" + date.getDate() : date
			.getDate();
	var retVal = this.year + "-" + this.month + "-" + this.date + " ";
	this.hour = date.getHours() < 10 ? "0" + date.getHours() : date
			.getHours();
	this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes()
			: date.getMinutes();
	this.second = date.getSeconds() < 10 ? "0" + date.getSeconds()
			: date.getSeconds();
	retVal += this.hour + ":" + this.minute + ":" + this.second;
	return retVal;
}

/**  
 *  
 * 删除医嘱
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adDelAdvice(){
	var rows = $('#adDgList').datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		var ids = '';
		for(var i=0;i<rows.length;i++){
			if(rows[i].id!=null&&rows[i].id!=''){
				if(ids!=''){
					ids += ','
				}
				ids += rows[i].id;
			}else{
				$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',rows[i]));
			}
		}
		if(ids!=''){
			$.messager.defaults={
					ok:'确定',
					cancel:'取消',
					width:350,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					closable:false
			};
			$.messager.confirm('提示信息','所选信息已划价，是否删除？', function(res){//提示是否删除
					if (res){
						$('#adDelAdviceBtn').menubutton('disable');//禁用
		 				$.ajax({
		 					url: basePath+"outpatient/advice/delAdvice.action?id="+ids,
		 					type:'post',
		 					data:{"id":ids},
		 					success: function(dataMap) {
		 						if(dataMap.resMsg=="success"){
		 							var rowsDel = $('#adDgList').datagrid('getChecked');
		 							if(rowsDel!=null&&rowsDel.length>0){
		 								for(var i=0;i<rowsDel.length;i++){
		 									$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',rowsDel[i]));
		 								}
		 							}
		 						}
		 						$.messager.alert('提示',dataMap.resCode,null,function(){});
		 						$('#adDelAdviceBtn').menubutton('enable');//启用
		 					}
		 				});
					}
			});
		}
		reloadRow();//刷新
	}else{
		$.messager.alert('提示',"请选择要删除的医嘱信息!",null,function(){});	
		return;			
	}
}

/**  
 *  
 * 退出开立
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adEndAdvice(){
	var rows = $('#adDgList').datagrid('getRows');
	if(rows!=null&&rows.length>0){
		$.messager.defaults={
				ok:'确定',
				cancel:'取消',
				width:250,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closable:false
		};
		jQuery.messager.confirm("提示","退出开立状态将丢失未保存的医嘱信息，是否继续？",function(event){
			if(event){
				$('#adEndAdviceBtn').menubutton('disable');//退出开立
				$('#adSaveAdviceBtn').menubutton('disable');//保存医嘱
				$('#adDelAdviceBtn').menubutton('disable');//删除医嘱
				$('#adHerbMedicineBtn').menubutton('disable');//草药查询
				$('#adAddGroupBtn').menubutton('disable');//添加组套
				$('#adCancelGroupBtn').menubutton('disable');//取消组套
				$('#adSaveGroupBtn').menubutton('disable');//保存组套
				$('#adLisResultBtn').menubutton('disable');//lis结果查询
				$('#adPacsResultBtn').menubutton('disable');//Pacs结果查询
				$('#adStackTree').tree('loadData',[]);//清除组套Tree
				$("#advEl").layout('collapse','west');//隐藏组套信息
				$('#adPrintAdviceBtn').menubutton('disable');//打印医嘱
				$('#adInspectionListBtn').menubutton('disable');//检查单
				$('#adInspectionListBtnJY').menubutton('disable');//检验单
				clearProject();//清空项目类别区域信息
				clearDgAdDgList("adDgList");//清空datagrid数据
			}
		});
	}else{
		$('#adEndAdviceBtn').menubutton('disable');//退出开立
		$('#adSaveAdviceBtn').menubutton('disable');//保存医嘱
		$('#adDelAdviceBtn').menubutton('disable');//删除医嘱
		$('#adHerbMedicineBtn').menubutton('disable');//草药查询
		$('#adAddGroupBtn').menubutton('disable');//添加组套
		$('#adCancelGroupBtn').menubutton('disable');//取消组套
		$('#adSaveGroupBtn').menubutton('disable');//保存组套
		$('#adLisResultBtn').menubutton('disable');//lis结果查询
		$('#adPacsResultBtn').menubutton('disable');//Pacs结果查询
		$('#adStackTree').tree('loadData',[]);//清除组套Tree
		$("#advEl").layout('collapse','west');//隐藏组套信息
		$('#adPrintAdviceBtn').menubutton('disable');//打印医嘱
		$('#adInspectionListBtn').menubutton('disable');//检查单
		$('#adInspectionListBtnJY').menubutton('disable');//检验单
		clearProject();//清空项目类别区域信息
		clearDgAdDgList("adDgList");//清空datagrid数据
	}
}

/**  
 *  
 * 清空并禁用项目类别区域信息
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearProject(){
	$('#adProjectTdId').combobox('clear');//项目类别下拉框清空
	$('#adProjectTdId').combobox('disable');//禁用项目类别下拉框
	$('#adProjectNameTdId').textbox('clear');
	$('#adProjectNameTdId').textbox('disable');
	$('#adProjectNumTdId').numberspinner('clear');
	$('#adProjectNumTdId').numberspinner('disable');
	$('#adProjectUnitTdId').combobox('clear');
	$('#adProjectUnitTdId').combobox('disable');
	clearProjectAux();
	$('#adChinMediDivId').hide();
	$('#adWestMediDivId').hide();
	$('#adNotDrugDivId').hide();
}

/**  
 *  
 * 清空辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearProjectAux(){
	clearWestMediDiv();
	clearChinMediDiv();
	clearNotDrugDiv();
}

/**  
 *  
 * 清空西药,中成药辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearWestMediDiv(){
	$('#adWestMediMinUnitTdId').text('');
	$('#adWestMediDosDosaTdId').text('');
	$('#adWestMediDosMaxTdId').numberbox('clear');
	$('#adWestMediDosMinTdId').numberbox('clear');
	$('#adWestMediFreTdId').textbox('clear');
	$('#adWestMediUsaTdId').textbox('clear');
	$('#adWestMediRemTdId').textbox('clear');
	$('#adWestMediUrgTdId').attr("checked",false);
}

/**  
 *  
 * 清空非药品辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearNotDrugDiv(){
	$('#adNotDrugExeTdId').textbox('clear');
	$('#adNotDrugInsTdId').textbox('clear');
	$('#adNotDrugRemTdId').textbox('clear');
	$('#adNotDrugUrgTdId').attr("checked",false);
}

/**  
 *  
 * 清空草药辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearChinMediDiv(){
	$('#adChinMediNumTdId').numberbox('clear');
	$('#adChinMediUsaTdId').textbox('clear');
	$('#adChinMediRemTdId').textbox('clear');
}

/**  
 *  
 * 组合
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adAddGroup(){
	var rows = $('#adDgList').datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		if(rows.length==1){
			$.messager.alert('提示',"无法对一条记录进行组合!",null,function(){});
			return;
		}
		//记录第一个项目的索引
		var fIndex = $('#adDgList').datagrid('getRowIndex',rows[0]);
		//存放组合信息的数组对象
		var groupArr = new Array();
		//存放对比数据
		var sysArr = new Array();//系统类别
		var freArr = new Array();//频次
		var usaArr = new Array();//用法
		var exeArr = new Array();//执行科室
		var numArr = new Array();//服数
		var totArr = new Array();//总量
		var samArr = new Array();//样本类型
		for(var i=0;i<rows.length;i++){
			groupArr[groupArr.length] = rows[i];//存放组合对象
			sysArr[sysArr.length] = rows[i].sysType;
			freArr[freArr.length] = rows[i].frequencyHid;
			usaArr[usaArr.length] = rows[i].usageName;
			exeArr[exeArr.length] = rows[i].executiveDeptHid;
			numArr[numArr.length] = rows[i].setNum;
			//业务调整 数量不做为组合条件  2017-03-02 16:03 aizhonghua
//			totArr[totArr.length] = rows[i].totalNum;
			samArr[samArr.length] = rows[i].sampleTeptHid;
		}
		var isPassSys = validArrIsEqual(sysArr);
		if(!isPassSys){
			$.messager.alert('提示',"所选记录的类别不同，无法进行组合操作！",null,function(){});
			return;
		}
		var isPassFre = validArrIsEqual(freArr);
		if(!isPassFre){
			$.messager.alert('提示',"所选记录的频次不同，无法进行组合操作！",null,function(){});
			return;
		}
		var isPassUsa = validArrIsEqual(usaArr);
		if(!isPassUsa){
			$.messager.alert('提示',"所选记录的用法不同，无法进行组合操作！",null,function(){});
			return;
		}
		var isPassExe = validArrIsEqual(exeArr);
		if(!isPassExe){
			$.messager.alert('提示',"所选记录的执行科室不同，无法进行组合操作！",null,function(){});
			return;
		}
		var isPassNum = validArrIsEqual(numArr);
		if(!isPassNum){
			$.messager.alert('提示',"所选记录的服数不同，无法进行组合操作！",null,function(){});
			return;
		}
		//业务调整 数量不做为组合条件  2017-03-02 16:03 aizhonghua
//		var isPassTot = validArrIsEqual(totArr);
//		if(!isPassTot){
//			$.messager.alert('提示',"所选记录的总量不同，无法进行组合操作！",null,function(){});
//			return;
//		}
		var isPassSam = validArrIsEqual(samArr);
		if(!isPassSam){
			$.messager.alert('提示',"所选记录的样本类型不同，无法进行组合操作！",null,function(){});
			return;
		}
		for(var i=0;i<rows.length;i++){
			$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',rows[i]));
		}
		for(var i=0;i<groupArr.length;i++){
			$('#adDgList').datagrid('insertRow',{
				index:fIndex+i,
				row:groupArr[i]
			});
			$('#adDgList').datagrid('updateRow',{
				index:fIndex+i,
				row: {
					group:groupVal
				}
			});
		}
		groupVal += 1;
		reloadRow();//刷新
	}else{
		$.messager.alert('提示',"请选择要组合的医嘱信息!",null,function(){});	
		return;			
	}
}

/**  
 *  
 * 判断数组中元素是否全部相同
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function validArrIsEqual(arr){
	var coun = arr.length;
	if(coun<=0){
		return false;
	}
	var sum = 1;
	var re = arr[0];
	for(var i=1;i<coun; i++){
		if(arr[i]==re){
			sum += 1;
		}
	}
	if(coun==sum){
		return true;
	}else{
		return false;
	}
}

/**  
 *  
 * 取消组合
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adCancelGroup(){
	var rows = $('#adDgList').datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		var datas = new Array();//存放需要拆分的组合的标记
		for(var i=0;i<rows.length;i++){
			if(rows[i].group!=null&&rows[i].group!=''){
				if(datas.indexOf(rows[i].group)==-1){
					datas[datas.length] = rows[i].group;
				}
			}
		}
		if(datas.length>0){
			var datasDel = new Array();//存放需要拆分的组合
			var rowsHis = $('#adDgList').datagrid('getRows');
			$('#adDgList').datagrid('uncheckAll');
			for(var i=0;i<rowsHis.length;i++){
				for(var j=0;j<datas.length;j++){
					if(rowsHis[i].group==datas[j]){
						datasDel[datasDel.length] = rowsHis[i];
						$('#adDgList').datagrid('checkRow',$('#adDgList').datagrid('getRowIndex',rowsHis[i]));
					}
				}
			}
			var rowsDel = $('#adDgList').datagrid('getChecked');
			//记录第一个项目的索引
			var fIndex = $('#adDgList').datagrid('getRowIndex',rowsDel[0]);
			for(var i=0;i<rowsDel.length;i++){
				$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',rowsDel[i]));
			}
			for(var i=0;i<datasDel.length;i++){
				$('#adDgList').datagrid('insertRow',{
					index:fIndex+i,
					row:datasDel[i]
				});
				$('#adDgList').datagrid('updateRow',{
					index:fIndex+i,
					row: {
						group:null
					}
				});
			}
		}else{
			$.messager.alert('提示',"没有符合拆分条件的组合!",null,function(){});
			return;
		}
		reloadRow();//刷新
	}else{
		$.messager.alert('提示',"请选择需要拆分的组合!",null,function(){});
		return;
	}
}

/**  
 *  
 * 打印医嘱
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adPrintAdvice(){
	//GH 2016年11月23日11:27:21  修改门诊号和病历号获取方法   clinicNoTdId caseNoTdId
	var clinicCode = $("#clinicNoTdId").html();//门诊号
	var patientNo = $("#caseNoTdId").html();//病历号  
	if(clinicCode!=null&&clinicCode!=''&&patientNo!=null&&patientNo!=''){
		var timerStr = Math.random();
		window.open (basePath+"outpatient/advice/iReportToMedicalRecord.action?isLow=&randomId="+timerStr+"&patientNo="+patientNo+"&clinicCode="+clinicCode+"&fileName=outpatientAdviceList_new",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}else{
		$.messager.alert('提示',"无医嘱信息,无法打印医嘱!",null,function(){});
	}
}

/**  
 *  
 * 检查单
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adInspectionCheckList(){
	//GH 2016年11月23日11:27:21  修改门诊号和病历号获取方法   clinicNoTdId caseNoTdId
	var clinicCode = $("#clinicNoTdId").html(); //门诊号
	var midicalrecordId = $("#caseNoTdId").html();//病历号
	if(clinicCode!=null&&clinicCode!=''&&midicalrecordId!=null&&midicalrecordId!=''){
		$.post(basePath+"outpatient/medicalrecord/getExecDpcdByclinicCode.action",{clinicCode:clinicCode,patientNo:midicalrecordId},function(dataMap){
			if(dataMap.resMsg=='success'){
				var timerStr = Math.random();
				window.open (basePath+"outpatient/advice/iReportToInspectionCheck.action?randomId="+timerStr+"&clinicCode="+clinicCode+"&patientNo="+midicalrecordId+"&fileName=newJSD",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		});
	}else{
		$.messager.alert('提示',"无检查单信息,无法打印检查单!",null,function(){});
	}
}

/**  
 *  
 * 检验单
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
var deptIdList = null;//执行科室ID
function adInspectionListJY(){
	//GH 2016年11月23日11:27:21  修改门诊号和病历号获取方法   clinicNoTdId caseNoTdId
	var clinicCode = $('#clinicNoTdId').html();//门诊号
	var medicalrecordId = $('#caseNoTdId').html();//病历号
	if(clinicCode!=null&&clinicCode!=''&&medicalrecordId!=null&&medicalrecordId!=''){
		jQuery.post(basePath+"outpatient/medicalrecord/getDeptId.action",{clinicCode:clinicCode,patientNo:medicalrecordId},function(dataMap){
			if(dataMap.resMsg=='success'){
				var timerStr = Math.random();
				window.open (basePath+"iReport/iReportPrint/iReportToMenZhenJY.action?randomId="+timerStr+"&clinicCode="+clinicCode+"&MedicalrecordId="+medicalrecordId+"&fileName=menzhenjianyanshenqing",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		});
	}else{
		$.messager.alert('提示',"无检验单信息,无法打印检验单!",null,function(){});
	}
}

/**  
 *  
 * 保存医嘱
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adSaveAdvice(){
	var clinicNo = $('#clinicNoTdId').html();
	var patientNo = $('#caseNoTdId').html();
	if(clinicNo==null||clinicNo==''||patientNo==null||patientNo==''){
		$.messager.alert('提示','患者信息不存在，无法开立医嘱！',null,function(){});
	}
	var rows = $('#adDgList').datagrid('getRows');
	if(rows!=null&&rows.length>0){
		$.messager.defaults={
				ok:'确定',
				cancel:'取消',
				width:250,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closable:false
		};
		jQuery.messager.confirm("提示","确定开立医嘱？",function(event){
			if(event){
				$("#adDgList").datagrid("loading");
				$('#adSaveAdviceBtn').menubutton('disable');//禁用保存按钮
				$.ajax({ 
					url: basePath+"outpatient/advice/savaAdviceInfo.action", 
					type:'post',
					data:{"patientNo":patientNo,"clinicNo":clinicNo,"jsonData":JSON.stringify(rows)},
					success: function(dataMap) {
						if(dataMap.resMsg=="error"){
							$("#adDgList").datagrid("loaded");
							$('#adSaveAdviceBtn').menubutton('enable');
							$.messager.alert('提示',dataMap.resCode,null,function(){});
							return;
						}else if(dataMap.resMsg=="success"){
							$("#adDgList").datagrid("loaded");
							$('#adSaveAdviceBtn').menubutton('enable');
							$.messager.alert('提示',dataMap.resCode,null,function(){});
							queryAdvice($('#clinicNoTdId').html());
						}else{
							$("#adDgList").datagrid("loaded");
							$('#adSaveAdviceBtn').menubutton('enable');
							$.messager.alert('提示','未知错误,请联系管理员!',null,function(){});
							return;
						}
					},
					error:function() {
						$("#adDgList").datagrid("loaded");
						$('#adSaveAdviceBtn').menubutton('enable');//保存医嘱
						$.messager.alert('提示','请求失败！',null,function(){});
					}
				});
			}
		});
	}else{
		$.messager.alert('提示',"请添加医嘱信息!",null,function(){});
		return;
	}
}

/**  
 *  
 * 保存组套
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adSaveGroup(){
	var rows = $('#adDgList').datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		if(rows.length==1){
			$.messager.alert('提示',"至少选择两条记录添加组套!",null,function(){});
			return
		}else{
			$('#stackSaveModleDivId').dialog({    
			    title:'确认组套信息',    
			    width:1300,    
			    height:400,  
			    closed: false,
			    closable:false,    
			    cache: false,
			    modal: true   
			});
			for(var i=0;i<rows.length;i++){
				var adPackUnitHid = rows[i].adPackUnitHid.split("_");
				$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
					id:rows[i].adviceId,//名称
					name:rows[i].adviceName,//名称
					spec:rows[i].specs,//规格
					packagingnum:rows[i].packagingnum,//包装数量
					unit:adPackUnitHid[0],//单位
					unitView:colorInfoUnitsMap.get(rows[i].adPackUnitHid),//单位
					defaultprice:rows[i].adPrice,//默认价
//					childrenprice:'',//儿童价
//					specialprice:'',//特诊价
					frequencyCode:rows[i].frequencyHid,//频次编码
					frequencyCodeView:colorInfoFrequencyMap.get(rows[i].frequencyHid),//频次编码
					usageCode:rows[i].usageNameHid,//用法名称
					usageCodeView:colorInfoUsageMap.get(rows[i].usageNameHid),//用法名称
					onceDose:rows[i].dosageHid,//每次服用剂量
					doseUnit:rows[i].adDosaUnitHid,//剂量单位
					doseUnitView:colorInfoUnitsMap.get(rows[i].adDosaUnitHid+"_"+judgeMap["doseUnit"]),//剂量单位
					mainDrugshow:1,//主药标记
					dateBgn:rows[i].startTime,//医嘱开始时间
					dateEnd:rows[i].endTime,//医嘱结束时间
					itemNote:rows[i].inspectPart,//检查部位
					days:rows[i].setNum,//草药付数
					//intervaldays:'',//间隔天数
					remark:rows[i].remark,//备注
					isDrugShow:rows[i].ty,//是否药品
					isDrugShowView:rows[i].ty==1?'是':'否',
					stackInfoNum:rows[i].totalNum//开立数量
				});
			} 
		}
	}else{
		$.messager.alert('提示',"请选择需要添加组套的信息!");
		return;
	}
}

/**  
 *  
 * 消息窗口
 * @Author：aizhonghua
 * @CreateDate：2016-6-7 下午06:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-7 下午06:56:59  
 * @ModifyRmk： 
 * @version 1.0
 *
 */
function msgShow(title,msg,timeout){
	$.messager.show({
		title:title,
		msg:msg,
		timeout:timeout,
		showType:'slide'
	});
}

/**  
 *  
 * 草药医嘱
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adHerbMedicine(){
	AdddilogModel("chinMediModleDivId","草药信息",basePath+"outpatient/advice/viewChinMediModle.action",1100,400);
}

/**  
 *  
 * 加载模式窗口：草药医嘱/组套
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function AdddilogModel(id,title,url,width,height) {
	$('#'+id).dialog({    
	    title: title,    
	    width: width,    
	    height: height,    
	    closed: false,    
	    cache: false,
	    href: url,    
	    modal: true   
	});    
}

//关闭dialog
function closeDialog() {
	$('#addun').dialog('close');  
}

//lis结果查询
function adLisResult(){
	$.messager.alert('提示',"lis结果查询,该功能建设中,请联系管理员!",null,function(){});
}
//Pacs结果查询
function adPacsResult(){
	$.messager.alert('提示',"Pacs结果查询,该功能建设中,请联系管理员!",null,function(){});
}

//First Home End

//Second Advice Start

/**  
 *  
 * 获得adDgList索引
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function getIndexForAdDgList(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		return $('#adDgList').datagrid('getRowIndex',row);
	}else{
		return -1;
	}
}

/**  
 *  
 * 添加颜色标识
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function functionColour(value,row,index){
	if(row.colour==null||row.colour==''){
		return 'background-color:#00FF00;';
	}else if(row.colour==0){
		return 'background-color:#00FF00;';
	}else if(row.colour==1){//收费
		return 'background-color:#EEEE00;';
	}else if(row.colour==3){//作废
		return 'background-color:#FF0000;';
	}else if(row.colour==4){//待审核
		return 'background-color:#00FF00;';
	}else if(row.colour==5){//已审核
		return 'background-color:#4A4AFF;';
	}else if(row.colour==6){//已审核未通过
		return 'background-color:#FF0000;';
	}
}

/**  
 *  
 * 行号
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function functionRowNum(value,row,index){
	return index+1;
}

/**  
 *  
 * 组合号渲染
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function functionGroup(value,row,index){
	var rwos = $('#adDgList').datagrid('getRows');
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

/**  
 *  
 * 类别
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function funcType(value,row,index){
	if(value!=null&&value!=''){
		if(value==1){
			return '药品';
		}else{
			return '非药品';
		}
	}else{
		return '非药品';
	}
}

/**  
 *  
 * 疾病分类
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function forDiseaset(value,row,index){
	if(value!=null&&value!=''){
		return colorInfoDiseasetMap[value];
	}else{
		return value;
	}
}

/**  
 *  
 * 清空辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearProjectAux(){
	clearWestMediDiv();
	clearChinMediDiv();
	clearNotDrugDiv();
}

/**  
 *  
 * 清空西药,中成药辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearWestMediDiv(){
	$('#adWestMediMinUnitTdId').text('');
	$('#adWestMediDosDosaTdId').text('');
	$('#adWestMediDosMaxTdId').numberbox('clear');
	$('#adWestMediDosMinTdId').numberbox('clear');
	$('#adWestMediFreTdId').textbox('clear');
	$('#adWestMediUsaTdId').textbox('clear');
	$('#adWestMediRemTdId').textbox('clear');
	$('#adWestMediUrgTdId').attr("checked",false);
}

/**  
 *  
 * 清空草药辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearChinMediDiv(){
	$('#adChinMediNumTdId').numberbox('clear');
	$('#adChinMediUsaTdId').textbox('clear');
	$('#adChinMediRemTdId').textbox('clear');
}

/**  
 *  
 * 清空非药品辅助区域
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearNotDrugDiv(){
	$('#adNotDrugExeTdId').textbox('clear');
	$('#adNotDrugInsTdId').textbox('clear');
	$('#adNotDrugRemTdId').textbox('clear');
	$('#adNotDrugUrgTdId').attr("checked",false);
}

/**  
 *  
 * 清空项目类别区域的textbox
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function clearProjectTextbox(){
	$('#adProjectNameTdId').combogrid('clear');
	$('#adProjectNumTdId').numberspinner('clear');
	$('#adProjectUnitTdId').combobox('clear');
}

/**  
 *  
 * 启用项目类别区域的textbox
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function enableProject(){
	$('#adProjectNameTdId').combogrid('enable');
	$('#adProjectNumTdId').numberspinner('enable');
}

/**  
 *  
 * 初始化单位下拉框
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function initCombobox(bool,map,unit,minUnit){
	var dataint = null;
	if(bool){
		var retVal = "[";
		if(map!=null){
			map.each(function(key,value,index){
				if(retVal.length>1){
					retVal+=",";
				}
				retVal+="{\"encode\":\""+key+"\",\"name\":\""+value+"\"}";
			})
		}
		retVal +="]";
		dataint = eval("("+retVal+")");
	}else{
		var retVal = "[";
		if(unit==minUnit){
			if(unit!=null&&unit!=''){
				retVal+="{\"encode\":\""+unit+"\",\"name\":\""+map.get(unit)+"\"}";
			}
		}else{
			if(unit!=null&&unit!=''){
				retVal+="{\"encode\":\""+unit+"\",\"name\":\""+map.get(unit)+"\"}";
			}
			if(minUnit!=null&&minUnit!=''){
				retVal+=",{\"encode\":\""+minUnit+"\",\"name\":\""+map.get(minUnit)+"\"}";
			}
		}
		if(retVal=="["){
			map.each(function(key,value,index){
				if(value=="次"){
					if(retVal.length==1){
						retVal+="{\"encode\":\""+key+"\",\"name\":\""+value+"\"}";
					}
				}
			});
		}
		retVal +="]";
		dataint = eval("("+retVal+")");
	}
	$('#adProjectUnitTdId').combobox({
		data:dataint,	
		valueField:'encode',	
		textField:'name',
		editable:false,
		disabled:false,
		onSelect:function(record){
			if(record!=null&&record!=''){
				var row = $('#adDgList').datagrid('getSelected');
				if(row!=null){
					var index = $('#adDgList').datagrid('getRowIndex',row);
					var val = record.encode.split('_');
					if(val[1]==judgeMap["minunit"]){
						if(row.splitattr!=judgeMap["splitDrug"]){//不可拆分
							$(this).combobox('select',row.adPackUnitHid);
							msgShow('提示','【'+row.adviceName+'】不可拆分！',9000);
						}else{
							if($.inArray(row.property,judgeMap["splitDrugArr"])==-1){//不可拆分
								$(this).combobox('select',row.adPackUnitHid);
								msgShow('提示','【'+row.adviceName+'】拆分属性维护不可拆分！',9000);
							}else{
								$('#adDgList').datagrid('updateRow',{
									index: index,
									row: {
										totalUnitHid:val[0],
										totalUnitHidJudge:record.encode,
										totalUnit:record.name
									}
								});
							}
						}
					}else{
						$('#adDgList').datagrid('updateRow',{
							index: index,
							row: {
								totalUnitHid:val[0],
								totalUnitHidJudge:record.encode,
								totalUnit:record.name
							}
						});
					}
				}
			}
		}
	});
}

/**  
 *  
 * 添加医嘱项目-判断停用及库存
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function initAdviceInfo(rowData){
	if(rowData.stop_flg==1){
		$.messager.alert('提示','【'+rowData.name+'】已停用，请重新选择！',null,function(){});
		return;
	}
	if(rowData.ty==1&&rowData.surSum<0){
		$.messager.alert('提示','【'+rowData.name+'】库存不足，请重新选择！',null,function(){});
		return;
	}
	if(rowData.ty==1&&rowData.surSum==0){
		$.messager.defaults={
				ok:'是',
				cancel:'否',
				width:300,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closable:false
		};
		$.messager.confirm('提示信息', '【'+rowData.name+'】库存为0，是否继续开立？', function(r){
			if (r){
				if(judgeMap["openZero"]==judgeMap["isOpenZero"]){//可以开立
					judgeAdviceInfo(rowData);//判断特限药品
				}else{
					$.messager.alert('提示','【系统设置】不允许开立库存为0的药品！',null,function(){});
					return;
				}
			}else{
				return;
			}
		});
	}else{
		judgeAdviceInfo(rowData);//判断特限药品
	}
}

/**  
 *  
 * 添加医嘱项目-判断特限药品
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function judgeAdviceInfo(rowData){
	if(rowData.ty==1){//药品
		if(rowData.restrictionofantibiotic==judgeMap["speDrugRank"]){//职级限制
			if(judgeDrugGradeMap[rowData.drugGrade]==null||judgeDrugGradeMap[rowData.drugGrade]==''){
				$.messager.alert('提示','【'+rowData.name+'】的等级信息错误，请联系管理员！',null,function(){});
				return;
			}else{
				if($('#isAuditing').val()=='false'&&(judgeDocDrugGradeMap[judgeDrugGradeMap[rowData.drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[rowData.drugGrade]]=='')){
					$.messager.confirm('提示信息', '药品【'+rowData.name+'】为职级特限药，您没有权限开立等级为【'+judgeDrugGradeAllMap[rowData.drugGrade]+'】的特限药品，需提交上级医师进行审核！是否确定开立？', function(r){
						if (r){
							skinAdviceInfo(rowData,true);//判断皮试
						}else{
							return;
						}
					});
				}else{
					skinAdviceInfo(rowData,false);//判断皮试
				}
			}
		}else if(rowData.restrictionofantibiotic==judgeMap["speDrugSpe"]){//特殊限制
			if($('#isAuditing').val()=='false'&&(specialDrugMap[rowData.code]==null||specialDrugMap[rowData.code]=='')){
				$.ajax({
					type:"post",
					url:basePath+"outpatient/advice/querySpeDrugApply.action",
					data:{clinicNo:$('#clinicNoTdId').html(),para:rowData.code},
					success: function(dataMap) {
						if(dataMap.resMsg=='success'){
							skinAdviceInfo(rowData,false);//判断皮试
						}else{
							$.messager.alert('提示','药品【'+rowData.name+'】为特殊管理类特限药，'+dataMap.resCode,null,function(){});
						}
					},
					error: function(){
						$.messager.alert('提示','请求失败！',null,function(){});
					}
				});
			}else{//可以开立
				skinAdviceInfo(rowData,false);//判断皮试
			}
		}else{//无限制
			skinAdviceInfo(rowData,false);//判断皮试
		}
	}else{//非药品
		addDatagrid(rowData,1,'',false);//直接添加
	}
}

/**  
 *  
 * 添加医嘱项目-判断皮试
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function skinAdviceInfo(rowData,bool){
	var isSkinHid = 1;
	var isSkin = '不需要皮试';
	if(rowData.ty==1&&rowData.istestsensitivity!=null&&rowData.istestsensitivity!=0){
		$.messager.defaults={
				ok:'是',
				cancel:'否',
				width:250,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closable:false
		};
		$.messager.confirm('提示信息', '【'+rowData.name+'】是否皮试？', function(r){
			if (r){
				isSkinHid = 2;
				isSkin = '需要皮试，未做';
			}
			addDatagrid(rowData,isSkinHid,isSkin,bool);
		});
	}else{
		addDatagrid(rowData,isSkinHid,isSkin,bool);
	}
}

/**  
 *  
 * 项目选择
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function addDatagrid(rowData,isSkinHid,isSkin,bool){
	var img = "";
	if(bool){
		img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+basePath+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
	}
	var unit = $.trim(rowData.unit);
	if(rowData.ty==0&&(unit==null||unit=='')){
		colorInfoUnitsMap.each(function(key,value,index){
			if(value=="次"){
				if(unit==null||unit==''){
					unit = $.trim(value);
				}
			}
		});
	}
	var mes = '';
	if(rowData.nature==judgeMap["drugPropS"]){//毒麻
		mes += '您开立了【毒麻类】药品，需附加开立手工毒麻药处方单！';
	}
	if(rowData.nature==judgeMap["drugPropP"]){//精神类
		mes += '您开立了【精神类】药品，二类精神类药品需要附加开立手工处方单！';
	}
	if(mes!=''){
		msgShow('提示',mes,9000);
	}
	var lastIndex = $('#adDgList').datagrid('appendRow',{
		limit:rowData.isProvincelimit==1?'X':(rowData.isCitylimit==1)?'S':null,
		type:rowData.sysType,//类型
		ty:rowData.ty,//是否为药品
		adviceType:'临时医嘱',//医嘱类型
		adviceId:rowData.code,//医嘱名称Id
		adviceName:rowData.name,//医嘱名称Hid
		adviceNameView:rowData.ty==1?(img+'['+rowData.price+'元/'+colorInfoUnitsMap.get(unit+"_"+judgeMap["packunit"])+']'+rowData.name+(rowData.spec==null?'':'['+rowData.spec+']')):((rowData.isInformedconsent==1?'√':'')+'['+rowData.price+'元/'+colorInfoUnitsMap.get(unit+"_"+judgeMap["nonmedicineencoding"])+']'+rowData.name+(rowData.spec==null?'':'['+rowData.spec+']')),//医嘱名称
		adPrice:rowData.price,//价格
		adPackUnitHid:(rowData.ty==1?rowData.unit+"_"+judgeMap["packunit"]:(rowData.unit==null?'次':$.trim(rowData.unit))+"_"+judgeMap["nonmedicineencoding"]),//包装单位
		adMinUnitHid:rowData.ty==1?rowData.minimumUnit+"_"+judgeMap["minunit"]:null,//单位
		adDosaUnitHid:rowData.ty==1?rowData.doseunit:null,//剂量
		adDosaUnitHidJudge:rowData.ty==1?rowData.doseunit+"_"+judgeMap["doseUnit"]:null,//剂量
		adDrugBasiHid:rowData.ty==1?rowData.basicdose:null,//基本剂量
		specs:rowData.spec,//规格
		sysType:rowData.sysType,//系统类别
		drugType:rowData.ty==1?rowData.type:null,//药品类别
		minimumcost:rowData.minimumcost,//最小费用代码
		packagingnum:rowData.packagingnum,//包装数量
		nature:rowData.ty==1?rowData.nature:null,//药品性质
		ismanufacture:rowData.ty==1?rowData.ismanufacture:null,//自制药标志
		dosageform:rowData.ty==1?rowData.dosageform:null,//剂型
		isInformedconsent:rowData.isInformedconsent==null?0:rowData.isInformedconsent,//是否知情同意书	
		auditing:bool?1:0,
		group:null,//组
		totalNum:1,//总量
		totalUnitHid:unit,//总单位Id1
		totalUnitHidJudge:rowData.ty==1?unit+"_"+judgeMap["packunit"]:unit+"_"+judgeMap["nonmedicineencoding"],//总单位Id
		totalUnit:rowData.ty==1?colorInfoUnitsMap.get(unit+"_"+judgeMap["packunit"]):colorInfoUnitsMap.get(unit+"_"+judgeMap["nonmedicineencoding"]),//总单位
		dosageHid:(rowData.ty==1)?((rowData.oncedosage==null||rowData.oncedosage==0)?1:rowData.oncedosage):null,//每次用量
		dosageMin:(rowData.ty==1)?((rowData.oncedosage==null||rowData.oncedosage==0)?1*rowData.basicdose:rowData.oncedosage*rowData.basicdose):null,//每次剂量
		dosage:(rowData.ty==1)?((rowData.oncedosage==null||rowData.oncedosage==0)?1:rowData.oncedosage)+colorInfoUnitsMap.get(rowData.minimumUnit+"_"+judgeMap["minunit"])+'='+(((rowData.oncedosage==null||rowData.oncedosage==0)?1:rowData.oncedosage)*(rowData.basicdose.toFixed(2))).toFixed(2)+colorInfoUnitsMap.get(rowData.doseunit+"_"+judgeMap["doseUnit"]):null,//每次用量
		unit:colorInfoUnitsMap.get(rowData.doseunit+"_"+judgeMap["doseUnit"]),//单位
		setNum:rowData.type==judgeMap["herbsId"]?1:null,//付数
		frequencyHid:(rowData.frequency!=null&&rowData.frequency!='')?rowData.frequency:rowData.ty==1?judgeMap["drugFre"]:judgeMap["unDrugUse"],//频次Id 
		frequency:colorInfoFrequencyMap.get((rowData.frequency!=null&&rowData.frequency!='')?rowData.frequency:rowData.ty==1?judgeMap["drugFre"]:judgeMap["unDrugUse"]),//频次编码
		usageNameHid:(rowData.usemode!=null&&rowData.usemode!='')?rowData.usemode:rowData.ty==1?judgeMap["drugUse"]:null,//用法Id
		usageName:(rowData.usemode!=null&&rowData.usemode!='')?colorInfoUsageMap.get(rowData.usemode):rowData.ty==1?colorInfoUsageMap.get(judgeMap["drugUse"]):null,//用法名称
		//injectionNum:rowData,//院注次数
		openDoctor:$('#advUserName').val(),//开立医生
		executiveDeptHid:rowData.ty==1?$('#pharmacyCombobox').combobox('getValue'):rowData.dept==null?$('#outMedideptId').val():rowData.dept,//执行科室Id
		executiveDept:rowData.ty==1?$('#pharmacyCombobox').combobox('getText'):rowData.dept==null?$('#outMedideptName').val():colorInfoExeDeptMap.get(rowData.dept),//执行科室
		isUrgentHid:0,//加急Id
		isUrgent:'否',//加急
		inspectPartId:rowData.inspectionsite,//检查部位Id
		inspectPart:colorInfoCheckpointMap.get(rowData.inspectionsite),//检查部位
		sampleTeptHid:rowData.labsample,
		sampleTept:colorInfoSampleTeptMap.get(rowData.labsample),//样本类型
		minusDeptHid:rowData.ty==1?$('#pharmacyCombobox').combobox('getValue'):null,//扣库科室Id
		minusDept:rowData.ty==1?$('#pharmacyCombobox').combobox('getText'):null,//扣库科室
		remark:rowData.remark,//备注
		inputPeop:$('#advUserName').val(),//录入人
		openDept:$('#outMedideptName').val(),//开立科室
		startTime:showTimeMedical(),//开立时间
		//endTime:rowData,//停止时间
		//stopPeop:rowData,//停止人
		isSkinHid:isSkinHid,//是否需要皮试Id
		isSkin:isSkin,//是否需要皮试
		splitattr:rowData.splitattr,//拆分属性
		property:rowData.property//拆分属性维护
	}).datagrid('getRows').length-1;
	$('#adDgList').datagrid('selectRow',lastIndex);
}

/**  
 *  
 * 清除名称
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function delSelectedData(){
	$('#adProjectNameTdId').combogrid('clear');
	$('#adProjectNameTdId').combogrid('grid').datagrid('load',{name:null,type:$('#adProjectTdId').combobox('getValue'),typeChi:$('#adProjectTdId').combobox('getText')});
}

/**  
 *  
 * 清除频次
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdWestMediFreTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		$('#adWestMediFreTdId').combobox('select',row.ty==1?judgeMap["drugFre"]:judgeMap["unDrugUse"]);
	}else{
		$('#adWestMediFreTdId').combobox('clear');
	}
}

/**  
 *  
 * 清除备注
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdWestMediRemTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		row.remark = null;
		$('#adDgList').datagrid('updateRow',{
			index:$('#adDgList').datagrid('getRowIndex',row),
			row:row
		});
	}
	$('#adWestMediRemTdId').combobox('clear');
}

/**  
 *  
 * 清除备注
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdNotDrugRemTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		row.remark = null;
		$('#adDgList').datagrid('updateRow',{
			index:$('#adDgList').datagrid('getRowIndex',row),
			row:row
		});
	}
	$('#adNotDrugRemTdId').combobox('clear');
}

/**  
 *  
 * 清除样本类型
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdNotDrugSamTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		row.sampleTeptHid = null;
		row.sampleTept = null;
		$('#adDgList').datagrid('updateRow',{
			index:$('#adDgList').datagrid('getRowIndex',row),
			row:row
		});
	}
	$('#adNotDrugSamTdId').combobox('clear');
}

/**  
 *  
 * 清除备注
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdChinMediRemTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		row.remark = null;
		$('#adDgList').datagrid('updateRow',{
			index:$('#adDgList').datagrid('getRowIndex',row),
			row:row
		});
	}
	$('#adChinMediRemTdId').combobox('clear');
}

/**  
 *  
 * 清除用法
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdWestMediUsaTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null&&row.ty==1){
		$('#adWestMediUsaTdId').combobox('select',judgeMap["drugUse"]);
	}else{
		$('#adWestMediUsaTdId').combobox('clear');
	}
}

/**  
 *  
 * 清除部位
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdNotDrugInsTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		row.inspectPartId = null;
		row.inspectPart = null;
		$('#adDgList').datagrid('updateRow',{
			index:$('#adDgList').datagrid('getRowIndex',row),
			row:row
		});
	}
	$('#adNotDrugInsTdId').combobox('clear');
}

/**  
 *  
 * 清除用法
 * @Author：aizhonghua
 * @CreateDate：2017-2-27 下午16:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-2-27 下午16:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function delAdChinMediUsaTdId(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null&&row.ty==1){
		$('#adChinMediUsaTdId').combobox('select',judgeMap["drugUse"]);
	}else{
		$('#adChinMediUsaTdId').combobox('clear');
	}
}

/**  
 *  
 * 启用功能选项
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function enaFunction(){
	$('#adProjectNumTdId').numberspinner('enable');
	$('#adProjectUnitTdId').combobox('enable');
	$('#adWestMediDosMaxTdId').numberbox('enable');
	$('#adWestMediDosMinTdId').numberbox('enable');
	$('#adWestMediFreTdId').combobox('enable');
	$('#adWestMediUsaTdId').combobox('enable');
	$('#adWestMediRemTdId').combobox('enable');
	$('#adWestMediSkiTdId').combobox('enable');
	$('#adWestMediUrgTdId').attr("disabled",false);
	$('#adChinMediNumTdId').numberbox('enable');
	$('#adChinMediUsaTdId').combobox('enable');
	$('#adChinMediRemTdId').combobox('enable');
	$('#adNotDrugExeTdId').combobox('enable');
	$('#adNotDrugInsTdId').combobox('enable');
	$('#adNotDrugSamTdId').combobox('enable');
	$('#adNotDrugRemTdId').combobox('enable');
	$('#adNotDrugUrgTdId').attr("disabled",false);
}

/**  
 *  
 * 医嘱复制
 * @Author：aizhonghua
 * @CreateDate：2016-6-7 下午06:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-7 下午06:56:59  
 * @ModifyRmk： 
 * @version 1.0
 *
 */
function copyHis(id){
	var rows = $('#'+id).datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		if(copyAdviceArr.length>0){
			$.messager.defaults={
    				ok:'确定',
    				cancel:'取消',
    				width:350,
    				collapsible:false,
    				minimizable:false,
    				maximizable:false,
    				closable:false
    		};
			$.messager.confirm('提示信息','已存在复制的医嘱信息，是否覆盖？', function(res){//提示是否删除
				if (res){
					copyAdviceArr = [];
					for(var i=0;i<rows.length;i++){
						var copyRow = jQuery.extend(true, {},rows[i]);
						copyRow.id = null;
						copyRow.colour = null;
						copyRow.group = null;
						copyRow.startTime=showTimeMedical();
						if(id=='hisAdvice'){
							copyRow.openDoctor = null;
							copyRow.inputPeop = null;
							copyRow.openDept = null;
						}
						copyAdviceArr[copyAdviceArr.length] = copyRow;
					}
					msgShow('提示','复制医嘱成功！',3000);
				}
		    });
		}else{
			for(var i=0;i<rows.length;i++){
				var copyRow = jQuery.extend(true, {},rows[i]);
				copyRow.colour = null;
				copyRow.id = null;
				copyRow.group = null;
				copyRow.startTime=showTimeMedical();
				if(id=='hisAdvice'){
					copyRow.openDoctor = null;
					copyRow.inputPeop = null;
					copyRow.openDept = null;
				}
				copyAdviceArr[copyAdviceArr.length] = copyRow;
			}
			msgShow('提示','复制医嘱成功！',3000);
		}
	}else{
		msgShow('提示','请选择需要复制的医嘱！',3000);
	}
}

/**  
 *  
 * 医嘱粘贴
 * @Author：aizhonghua
 * @CreateDate：2016-6-7 下午06:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-7 下午06:56:59  
 * @ModifyRmk： 
 * @version 1.0
 *
 */
function pasteHis(){
	if(copyAdviceArr.length<=0){
		msgShow('提示','没有复制的医嘱信息！',3000);
	}else{
		var copyAdviceArrCopy = jQuery.extend(true, [], copyAdviceArr);
		for(var i=0;i<copyAdviceArrCopy.length;i++){
			$('#adDgList').datagrid('appendRow',
				copyAdviceArrCopy[i]
			);
		}
		reloadRow();
	}
}

/**  
 *  
 * 下拉框验证方法
 * @Author：aizhonghua
 * @CreateDate：2016-6-7 下午06:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-7 下午06:56:59  
 * @ModifyRmk： 
 * @version 1.0
 *
 */
function isValidComboboxValue(id,v,t){
	var data = $('#'+id).combobox('getData');
	var value = $('#'+id).combobox('getValue');
	var text = $('#'+id).combobox('getText');
	if(value==null||value==''||text==null||text==''){
		$('#'+id).combobox('setValue',null);
		return false;
	}
	if(data.length>0){
		for(var i=0;i<data.length;i++){
			if(data[i][v]==value&&data[i][t]==text){
				return true;
			}
		}
		$('#'+id).combobox('setValue',null);
		return false;
	}else{
		$('#'+id).combobox('setValue',null);
		return false;
	}
}

/**  
 *  
 * 是否加急
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function onClickIsUrgent(id){
	var index = getIndexForAdDgList();
	if(index>=0){
		if($('#'+id).is(':checked')){
			$('#adDgList').datagrid('updateRow',{
				index: index,
				row: {
					isUrgentHid:1,
					isUrgent:'是'
				}
			});
		}else{
			$('#adDgList').datagrid('updateRow',{
				index: index,
				row: {
					isUrgentHid:0,
					isUrgent:'否'
				}
			});
		}
	}
}
/**  
 *  
 * 上移
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceUpMove(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		var index = $('#adDgList').datagrid('getRowIndex',row);
		if(index<=0){
			$.messager.alert('提示',"操作异常！",null,function(){});
			return;
		}
		var rows = $('#adDgList').datagrid('getRows');
		if(row.group!=null&&row.group!=''){
			var group = row.group;
			var upGroup = rows[index-1].group;
			if(group==upGroup){
				$('#adDgList').datagrid('deleteRow',index);
				$('#adDgList').datagrid('insertRow',{
					index:index-1,
					row:row
				});
				$('#adDgList').datagrid("selectRow", index-1);
			}else{
				var datasArr = new Array();
				for(var i=index;i<rows.length;i++){
					if(rows[i].group==group){
						datasArr[datasArr.length] = rows[i];
					}
				}
				if(datasArr.length>0){
					for(var i=0;i<datasArr.length;i++){
						$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',datasArr[i]));
					}
					var nowIndex = index-1;
					if(upGroup!=null&&upGroup!=''){
						for(var i=index-1;i>=0;i--){
							if(rows[i].group==upGroup){
								nowIndex = $('#adDgList').datagrid('getRowIndex',rows[i])
							}
						}
					}
					for(var i=datasArr.length-1;i>=0;i--){
						$('#adDgList').datagrid('insertRow',{
							index:nowIndex,
							row:datasArr[i]
						});
					}
					$('#adDgList').datagrid("selectRow",nowIndex);
				}
			}
		}else{
			if(rows[index-1].group!=null&&rows[index-1].group!=''){
				var group = rows[index-1].group;
				var datasArr = new Array();
				for(var i=0;i<index;i++){
					if(rows[i].group==group){
						datasArr[datasArr.length] = rows[i];
					}
				}
				if(datasArr.length>0){
					for(var i=0;i<datasArr.length;i++){
						$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',datasArr[i]));
					}
					var nowIndex = $('#adDgList').datagrid('getRowIndex',row);
					for(var i=datasArr.length-1;i>=0;i--){
						$('#adDgList').datagrid('insertRow',{
							index:nowIndex+1,
							row:datasArr[i]
						});
					}
				}
			}else{
				$('#adDgList').datagrid('deleteRow',index);
				$('#adDgList').datagrid('insertRow',{
					index:index-1,
					row:row
				});
				$('#adDgList').datagrid("selectRow", index-1);
			}
		}
		reloadRow();//刷新
	}else{
		$.messager.alert('提示',"操作异常！",null,function(){});
		return;
	}
}

/**  
 *  
 * 下移
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceDownMove(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		var index = $('#adDgList').datagrid('getRowIndex',row);
		var rows = $('#adDgList').datagrid('getRows');
		if(index>=rows.length-1){
			$.messager.alert('提示',"操作异常！",null,function(){});
			return;	
		}
		if(row.group!=null&&row.group!=''){
			var group = row.group;
			var downGroup = rows[index+1].group;
			if(group==downGroup){
				$('#adDgList').datagrid('deleteRow',index);
				$('#adDgList').datagrid('insertRow',{
					index:index+1,
					row:row
				});
				$('#adDgList').datagrid("selectRow", index+1);
			}else{
				var datasArr = new Array();
				for(var i=index;i>=0;i--){
					if(rows[i].group==group){
						datasArr[datasArr.length] = rows[i];
					}
				}
				if(datasArr.length>0){
					var downRow = rows[index+1];
					for(var i=0;i<datasArr.length;i++){
						$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',datasArr[i]));
					}
					if(downGroup!=null&&downGroup!=''){
						var rowsNow = $('#adDgList').datagrid('getRows');
						for(var i=(index+1-datasArr.length);i<rowsNow.length;i++){
							if(rowsNow[i].group==downGroup){
								downRow = rows[i];
							}
						}
					}
					var nowIndex = $('#adDgList').datagrid('getRowIndex',downRow);
					for(var i=0;i<datasArr.length;i++){
						$('#adDgList').datagrid('insertRow',{
							index:nowIndex+1,
							row:datasArr[i]
						});
					}
					$('#adDgList').datagrid("selectRow",nowIndex+datasArr.length);
				}
			}
		}else{
			if(rows[index+1].group!=null&&rows[index+1].group!=''){
				var group = rows[index+1].group;
				var datasArr = new Array();
				for(var i=index+1;i<=rows.length-1;i++){
					if(rows[i].group==group){
						datasArr[datasArr.length] = rows[i];
					}
				}
				if(datasArr.length>0){
					for(var i=0;i<datasArr.length;i++){
						$('#adDgList').datagrid('deleteRow',$('#adDgList').datagrid('getRowIndex',datasArr[i]));
					}
					var nowIndex = $('#adDgList').datagrid('getRowIndex',row);
					for(var i=datasArr.length-1;i>=0;i--){
						$('#adDgList').datagrid('insertRow',{
							index:nowIndex,
							row:datasArr[i]
						});
					}
				}
			}else{
				$('#adDgList').datagrid('deleteRow',index);
				$('#adDgList').datagrid('insertRow',{
					index:index+1,
					row:row
				});
				$('#adDgList').datagrid("selectRow", index+1);
			}
		}
		reloadRow();//刷新
	}else{
		$.messager.alert('提示',"操作异常！",null,function(){});
		return;
	}
}

/**  
 *  
 * 修改价格
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function adviceEditPrice(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null&&row.ismanufacture==3){
		$('#adviceListWin').window({	
			title:row.adviceName,
			width:250,
			height:155,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			closable:false,
			resizable:false,
			modal:true
		}); 
		$('#adviceListWinIputId').next("span").children().first().focus();
		$('#adviceListWinIputId').numberbox('setText',row.adPrice);
		$('#adviceListWin').window('open');
	}else{
		$.messager.alert('提示',"该项目非自制药品，无法修改价格！",null,function(){});
	}
}

/**  
 *  
 * 修改价格确认
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function adviceEditPriceSave(){
	var price = $('#adviceListWinIputId').numberbox('getText');
	var rowData = $('#adDgList').datagrid('getSelected');
	var index = $('#adDgList').datagrid('getRowIndex',rowData);
	$('#adDgList').datagrid('updateRow',{
		index: index,
		row: {
			adPrice:price,
			adviceNameView:'['+price+'/'+colorInfoUnitsMap.get(rowData.adMinUnitHid)+']'+rowData.adviceName+'[1'+colorInfoUnitsMap.get(rowData.adMinUnitHid)+'='+rowData.adDrugBasiHid+colorInfoUnitsMap.get(rowData.adDosaUnitHidJudge)+']'//医嘱名称
		}
	});
	adviceEditPriceCancel();
}
		
/**  
 *  
 * 修改价格取消
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function adviceEditPriceCancel(){
	$('#adviceListWin').window('close');
}

/**  
 *  
 * 院注次数
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceListInjNum(row){
	$('#adviceListInjNumWin').window({	
		title:'院注次数',
		width:350,
		height:135,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closable:false,
		resizable:false,
		modal:true
	}); 
	var dosageMin = row.dosageMin==0?1:row.dosageMin
	$('#adviceListInjdayWinIputId').html("");
	$('#adviceListInjNumWinIputId').numberbox('setText',"");
	$('#adviceListInjNumWinYzId').text(row.totalNum);
	$('#adviceListInjNumWinmcId').text(dosageMin+colorInfoUnitsMap.get(row.adDosaUnitHidJudge));
	$('#adviceListInjNumWinIputId').next("span").children().first().focus();
	if(row.injectionNum!=null&&row.injectionNum!=''){
		if(colorInfoFrequencyList!=null){
			for(var i=0;i<colorInfoFrequencyList.length;i++){
				if(row.frequencyHid==colorInfoFrequencyList[i].encode){
					var fre = colorInfoFrequencyList[i];
					if(fre.frequencyUnit=='D'){
						var adviceListInjday = (row.injectionNum)/(fre.frequencyTime);
						$('#adviceListInjdayWinIputId').html(adviceListInjday.toFixed(2)+'天');
					}else if(fre.frequencyUnit=='W'){
						var adviceListInjday = (row.injectionNum)/(fre.frequencyTime);
						$('#adviceListInjdayWinIputId').html(adviceListInjday.toFixed(2)+'周');
					}
				}
			}
		}
		$('#adviceListInjNumWinIputId').numberbox('setText',row.injectionNum);
	}else{
		var injectionNum = row.totalNum*row.adDrugBasiHid/dosageMin;
		if(colorInfoFrequencyList!=null){
			for(var i=0;i<colorInfoFrequencyList.length;i++){
				if(row.frequencyHid==colorInfoFrequencyList[i].encode){
					var fre = colorInfoFrequencyList[i];
					if(fre.frequencyUnit=='D'){
						var adviceListInjday = injectionNum/(fre.frequencyTime);
						$('#adviceListInjdayWinIputId').html(adviceListInjday.toFixed(2)+'天');
					}else if(fre.frequencyUnit=='W'){
						var adviceListInjday = injectionNum/(fre.frequencyTime);
						$('#adviceListInjdayWinIputId').html(adviceListInjday.toFixed(2)+'周');
					}
				}
			}
		}
		$('#adviceListInjNumWinIputId').numberbox('setText',injectionNum);
	}
	$('#adviceListInjNumWin').window('open');
}

/**  
 *  
 * 修改院注次数
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceEditInjNum(){
	var row = $('#adDgList').datagrid('getSelected');
	adviceListInjNum(row);
}

/**  
 *  
 * 修改院注次数
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceEditInjNumSave(){
	var injNum = $('#adviceListInjNumWinIputId').numberbox('getText');
	var rowData = $('#adDgList').datagrid('getSelected');
	var index = $('#adDgList').datagrid('getRowIndex',rowData);
	$('#adDgList').datagrid('updateRow',{
		index: index,
		row: {
			injectionNum:injNum
		}
	});
	$('#adviceListInjNumWin').window('close');
}

/**  
 *  
 * 医嘱审核
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceAuditing(){
	var row = $('#adDgList').datagrid('getSelected');
	if(row!=null){
		$('#adviceAuditingWinId').val(row.id);
		$('#adviceAuditingWin').window({	
			title:row.adviceName,
			width:300,
			height:160,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			closable:false,
			resizable:false,
			modal:true
		}); 
		$('#adviceAuditingWin').window('open');
	}else{
		$.messager.alert('提示',"请选择需要审核的医嘱！",null,function(){});
	}
}

/**  
 *  
 * 审核取消
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceAuditingCancel(){
	$('#adviceAuditingWinId').val('');
	$('#adviceAuditingWinOk').prop("checked","checked");
	$('#adviceAuditingRemarks').val('');
	$('#adviceAuditingWin').window('close');
}

/**  
 *  
 * 审核
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function adviceAuditingSave(){
	var id = $('#adviceAuditingWinId').val();
	var start = $("input[name='start']:checked").val();
	var remarks = $('#adviceAuditingRemarks').val();
	if(id!=null&&id!=''){
		if(start!=null&&start!=''){
			$.messager.defaults={
					ok:'确定',
					cancel:'取消',
					width:250,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					closable:false
			};
			jQuery.messager.confirm("提示","确定提交审核信息？",function(event){
				if(event){
					$.ajax({
						url: basePath+"outpatient/advice/savaAdviceAuditing.action",
						data:{"id":id,"start":start,"remarks":remarks},
						type:'post',
						success: function(dataMap) {
							if(dataMap.resMsg=="error"){
				   				$.messager.alert('提示',dataMap.resCode,null,function(){});
				   				adviceAuditingCancel();
								return;
							}else if(dataMap.resMsg=="success"){
								$.messager.alert('提示',dataMap.resCode,null,function(){});
								queryAdvice(dataMap.resValue);	
								adviceAuditingCancel();
							}else{
								$.messager.alert('提示','未知错误,请联系管理员!',null,function(){});
								adviceAuditingCancel();
								return;
							}
						}
					});
				}
			});
		}else{
			$.messager.alert('提示',"请选择审核状态！",null,function(){});
			return;
		}
	}else{
		$.messager.alert('提示',"审核信息有误，请重新选择！",null,function(){});
		adviceAuditingCancel();
		return;
	}
}

//组套确定
function adStackInfoModelYes(){
	var rows = $('#adStackInfoModelDgId').datagrid('getChecked');
	if(rows!=null&&rows.length>0){
		var isStock = true;
		var isSpeDrugRank = false;
		var speDrugRankArr = new Array();
		var isSpeDrugSpe = false;
		var speDrugSpeArr = new Array();
		for(var i=0;i<rows.length;i++){
			if(rows[i].stop_flg==1){
				$.messager.alert('提示','【'+rows[i].name+'】已停用，请重新选择！',null,function(){});
				return;
			}
			if(rows[i].storeSum!=null&&rows[i].storeSum!=''&&rows[i].storeSum-rows[i].preoutSum<0){
				$.messager.alert('提示','【'+rows[i].name+'】库存不足，请重新选择！',null,function(){});
				return;
			}
			if(rows[i].ty==1&&rows[i].storeSum-rows[i].preoutSum==0){
				isStock = false;
			}
			if(rows[i].ty==1){
				if(rows[i].drugRestrictionofantibiotic==judgeMap["speDrugRank"]){//职级限制
					if(judgeDrugGradeMap[rows[i].drugGrade]==null||judgeDrugGradeMap[rows[i].drugGrade]==''){
						$.messager.alert('提示','【'+rows[i].name+'】的等级信息错误，请联系管理员！',null,function(){});
						return;
					}
					if($('#isAuditing').val()=='false'&&(judgeDocDrugGradeMap[judgeDrugGradeMap[rows[i].drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[rows[i].drugGrade]]=='')){
						if($.inArray(rows[i].name+"_"+judgeDrugGradeAllMap[rows[i].drugGrade],speDrugRankArr)==-1){
							speDrugRankArr[speDrugRankArr.length] = rows[i].name+"_"+judgeDrugGradeAllMap[rows[i].drugGrade];
						}
						if(!isSpeDrugRank){
							isSpeDrugRank = true;
						}
					}
				}
				if(rows[i].drugRestrictionofantibiotic==judgeMap["speDrugSpe"]){//特殊管理
					if($('#isAuditing').val()=='false'&&(specialDrugMap[rows[i].code]==null||specialDrugMap[rows[i].code]=='')){
						if($.inArray(rows[i].code+"_"+rows[i].name,speDrugSpeArr)==-1){
							speDrugSpeArr[speDrugSpeArr.length] = rows[i].code+"_"+rows[i].name;
						}
						if(!isSpeDrugSpe){
							isSpeDrugSpe = true;
						}
					}
				}
			}
		}
		if(isStock){//库存充足
			judgeSpeDrugRankAdviceInfo(rows,isSpeDrugRank,speDrugRankArr,isSpeDrugSpe,speDrugSpeArr);
		}else{//库存不足
			$.messager.defaults={
					ok:'是',
					cancel:'否',
					width:450,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					closable:false
			};
			$.messager.confirm('提示信息', '所选信息中有库存为0的药品，是否继续开立？', function(r){
				if (r){
					if(judgeMap["openZero"]==judgeMap["isOpenZero"]){//可以开立
						judgeSpeDrugRankAdviceInfo(rowsrows,isSpeDrugRank,speDrugRankArr,isSpeDrugSpe,speDrugSpeArr);
					}else{
						$.messager.alert('提示','【系统设置】不允许开立库存为0的药品！',null,function(){});
						return;
					}
				}else{
					return;
				}
			});
		}
	}else{
		$.messager.alert('提示','请选择组套信息！',null,function(){});
	}
}

//组套确定-判断职级特限药
function judgeSpeDrugRankAdviceInfo(rows,isSpeDrugRank,speDrugRankArr,isSpeDrugSpe,speDrugSpeArr){
	if(isSpeDrugRank){//职级限制
		var name = '';
		for(var i=0;i<speDrugRankArr.length;i++){
			var arr = speDrugRankArr[i].split('_');
			if(name!=''){
				name += "、<br>";
			}
			name += "【"+arr[0]+"】"+"【"+arr[1]+"】";
		}
		$.messager.confirm('提示信息', '所选信息中包含职级限制药品：<br>'+name+'，需提交上级医师进行审核！是否确定开立？', function(r){
			if (r){
				judgeSpeDrugSpeAdviceInfo(rows,isSpeDrugSpe,speDrugSpeArr,true);
			}else{
				return;
			}
		});
	}else{//特殊管理
		judgeSpeDrugSpeAdviceInfo(rows,isSpeDrugSpe,speDrugSpeArr,false);
	}
}
//组套确定-判断特殊管理特限药
function judgeSpeDrugSpeAdviceInfo(rows,isSpeDrugSpe,speDrugSpeArr,bool){
	if(isSpeDrugSpe){//特殊管理
		var code = '';
		for(var i=0;i<speDrugSpeArr.length;i++){
			var arr = speDrugSpeArr[i].split('_');
			if(code!=''){
				code += ',';
			}
			code += arr[0];
		}
		$.ajax({
			type:"post",
			url:basePath+"outpatient/advice/querySpeDrugApplyStack.action",
			data:{clinicNo:$('#clinicNoTdId').html(),para:code},
			success: function(dataMap) {
				if(dataMap.resMsg=='success'){
					skinStackAdviceInfo(rows,bool);
				}else{
					$.messager.alert('提示',dataMap.resCode,null,function(){});
				}
			},
			error: function(){
				$.messager.alert('提示','请求失败！',null,function(){});
			}
		});
	}else{//皮试
		skinStackAdviceInfo(rows,bool);
	}
}
//皮试
function skinStackAdviceInfo(rows,bool){
	var isSkin = false;
	var isSkinArr = new Array();
	for(var i=0;i<rows.length;i++){
		if(rows[i].ty==1&&rows[i].drugIstestsensitivity!=null&&rows[i].drugIstestsensitivity!=0){
			if(!isSkin){
				isSkin = true;
			}
			if($.inArray(rows[i].name,isSkinArr)==-1){
				isSkinArr[isSkinArr.length] = rows[i].name;
			}
		}
	}
	if(isSkin){//需要皮试
		var isSkinHid = 1;
		var isSkin = '不需要皮试';
		$.messager.defaults={
				ok:'是',
				cancel:'否',
				width:250,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closable:false
		};
		var name = '';
		for(var i=0;i<isSkinArr.length;i++){
			if(name!=''){
				name += ',';
			}
			name += isSkinArr[i];
		}
		$.messager.confirm('提示信息', '【'+name+'】是否皮试？', function(r){
			if (r){
				isSkinHid = 2;
				isSkin = '需要皮试，未做';
			}
			addStackDatagrid(rows,isSkinHid,isSkin,bool);
		});
	}else{//不需要皮试
		addStackDatagrid(rows,1,null,bool);
	}
	
}

function addStackDatagrid(rows,isSkinHid,isSkin,bool){
	var rowsAll = $('#adStackInfoModelDgId').datagrid('getRows');
	var gV = null;
	if(rows.length>1){
		if(rows.length==rowsAll.length){
			gV = groupVal;
			groupVal += 1;
		}
	}
	var img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+basePath+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
	for(var i=0;i<rows.length;i++){
		var lastIndex = $('#adDgList').datagrid('appendRow',{
			limit:rows[i].unDrugIsprovincelimit==1?'X':(rows[i].unDrugIscitylimit==1)?'S':null,
			type:rows[i].drugSystype,//类型
			ty:rows[i].ty,//是否为药品
			adviceType:'临时医嘱',//医嘱类型
			adviceId:rows[i].code,//医嘱名称Id
			adviceName:rows[i].name,//医嘱名称Hid
			adviceNameView:rows[i].ty==1?(bool?img:'')+'['+rows[i].drugRetailprice+'元/'+colorInfoUnitsMap.get(rows[i].stackInfoUnit+"_"+judgeMap["packunit"])+']'+rows[i].name+(rows[i].spec==null?'':('['+rows[i].spec+']')):(rows[i].unDrugIsinformedconsent==1?'√':'')+'['+rows[i].drugRetailprice+'元/'+colorInfoUnitsMap.get(rows[i].stackInfoUnit+"_"+judgeMap["nonmedicineencoding"])+']'+rows[i].name,//医嘱名称
			adPrice:rows[i].drugRetailprice,//价格
			adPackUnitHid:rows[i].ty==1?rows[i].drugPackagingunit+"_"+judgeMap["packunit"]:rows[i].stackInfoUnit+"_"+judgeMap["nonmedicineencoding"],//包装单位
			adMinUnitHid:rows[i].ty==1?rows[i].unit+"_"+judgeMap["minunit"]:null,//单位
			adDosaUnitHid:rows[i].ty==1?rows[i].drugDoseunit:null,//剂量
			adDosaUnitHidJudge:rows[i].ty==1?rows[i].drugDoseunit+"_"+judgeMap["doseUnit"]:null,//剂量
			adDrugBasiHid:rows[i].ty==1?rows[i].drugBasicdose:null,//基本剂量
			specs:rows[i].spec,//规格
			sysType:rows[i].drugSystype,//系统类别
			drugType:rows[i].ty==1?rows[i].drugType:null,//药品类别
			minimumcost:rows[i].drugMinimumcost,//最小费用代码
			packagingnum:rows[i].packagingnum,//包装数量
			nature:rows[i].ty==1?rows[i].drugNature:null,//药品性质
			ismanufacture:rows[i].ty==1?rows[i].drugIsmanufacture:null,//自制药标志
			dosageform:rows[i].ty==1?rows[i].drugDosageform:null,//剂型
			isInformedconsent:rows[i].isInformedconsent==null?0:rows[i].isInformedconsent,//是否知情同意书
			auditing:bool?1:0,
			group:gV,//组
			totalNum:rows[i].stackInfoNum,//总量
			totalUnitHid:rows[i].stackInfoUnit,//总单位Id
			totalUnitHidJudge:rows[i].ty==1?rows[i].stackInfoUnit+"_"+judgeMap["packunit"]:rows[i].stackInfoUnit+"_"+judgeMap["nonmedicineencoding"],//总单位Id
			totalUnit:rows[i].ty==1?colorInfoUnitsMap.get(rows[i].stackInfoUnit+"_"+judgeMap["packunit"]):colorInfoUnitsMap.get(rows[i].stackInfoUnit+"_"+judgeMap["nonmedicineencoding"]),//总单位
			dosageHid:(rows[i].ty==1)?((rows[i].oncedosage==null||rows[i].oncedosage==0)?1:rows[i].drugOncedosage):null,//每次用量 
			dosageMin:(rows[i].ty==1)?((rows[i].oncedosage==null||rows[i].oncedosage==0)?1*rows[i].drugBasicdose:rows[i].drugOncedosage*rows[i].drugBasicdose):null,//每次剂量
			dosage:(rows[i].ty==1)?(((rows[i].drugOncedosage==null||rows[i].drugOncedosage==0)?1:rows[i].drugOncedosage)+colorInfoUnitsMap.get(rows[i].unit+"_"+judgeMap["minunit"])+'='+((((rows[i].drugOncedosage==null||rows[i].drugOncedosage==0)?1:rows[i].drugOncedosage.toFixed(2))*(rows[i].drugBasicdose.toFixed(2))).toFixed(2))+colorInfoUnitsMap.get(rows[i].drugDoseunit+"_"+judgeMap["doseUnit"])):null,//每次用量
			unit:colorInfoUnitsMap.get(rows[i].drugDoseunit+"_"+judgeMap["doseUnit"]),//单位
			setNum:rows[i].drugType==judgeMap["herbsId"]?rows[i].days:null,//付数
			frequencyHid:rows[i].drugFrequency,//频次Id
			frequency:colorInfoFrequencyMap.get(rows[i].drugFrequency),//频次编码
			usageNameHid:rows[i].drugUsemode,//用法Id
			usageName:colorInfoUsageMap.get(rows[i].drugUsemode),//用法名称
			//injectionNum:rows[i],//院注次数
			openDoctor:$('#advUserName').val(),//开立医生
			executiveDeptHid:rows[i].ty==1?$('#pharmacyCombobox').combobox('getValue'):rows[i].unDrugDept==null?$('#outMedideptId').val():rows[i].unDrugDept,//执行科室Id
			executiveDept:rows[i].ty==1?$('#pharmacyCombobox').combobox('getText'):rows[i].unDrugDept==null?colorInfoExeDeptMap.get($('#outMedideptId').val()):colorInfoExeDeptMap.get(rows[i].unDrugDept),//执行科室
			isUrgentHid:0,//加急Id
			isUrgent:'否',//加急
			inspectPartId:rows[i].unDrugInspectionsite,//检查部位Id
			inspectPart:colorInfoCheckpointMap.get(rows[i].unDrugInspectionsite),//检查部位
			sampleTeptHid:rows[i].labsample,
			sampleTept:colorInfoSampleTeptMap.get(rows[i].labsample),//样本类型
			minusDeptHid:rows[i].ty==1?$('#pharmacyCombobox').combobox('getValue'):null,//扣库科室Id
			minusDept:rows[i].ty==1?$('#pharmacyCombobox').combobox('getText'):null,//扣库科室
			remark:rows[i].drugRemark,//备注
			inputPeop:$('#advUserName').val(),//录入人
			openDept:$('#outMedideptName').val(),//开立科室
			startTime:showTimeMedical(),//开立时间
			//endTime:rows[i],//停止时间
			//stopPeop:rows[i],//停止人
			isSkinHid:isSkinHid,//是否需要皮试Id
			isSkin:isSkin,//是否需要皮试
			splitattr:rows[i].drugSplitattr,//拆分属性
			property:rows[i].property//拆分属性维护
		}).datagrid('getRows').length-1;
		$('#adDgList').datagrid('selectRow',lastIndex);
	}
	reloadRow();//刷新
	$('#chinMediModleDivId').dialog('close');
}

//Second Advice End

//Third HisAdvice Start

/**  
 *  
 * 组合号渲染
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function functionGroupHis(value,row,index){
	var rwos = $('#hisAdvice').datagrid('getRows');
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

/**  
 *  
 * 刷新
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function reloadHis(){
	 var rows = $('#hisAdvice').datagrid('getRows');
	 for(var i=0;i<rows.length;i++){
		 $('#hisAdvice').datagrid('refreshRow',$('#hisAdvice').datagrid('getRowIndex',rows[i])); 
	 }
}

/**  
 *  
 * 查询历史医嘱
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @param deptIsforregister 是否是挂号科室 1是 0否
 * @version 1.0
 *
 */
function searchInfoHis(){
	var idCardNo = $('#idCardNoHis').textbox('getText');
	if($.trim(idCardNo)==null||$.trim(idCardNo)==''){
		$.messager.alert('提示','请输入就诊卡号！',null,function(){});
		return;
	}
	$.ajax({
		type:"post",
		url:basePath+"outpatient/advice/searchInfoHid.action",
		data:{idCardNo:idCardNo},
		success: function(dataMap) {
			if(dataMap.resMsg=='success'){
				$('#hisAdviceTree').tree({data:JSON.parse(dataMap.resCode)});
			}else{
				$.messager.alert('提示',dataMap.resCode,null,function(){});
			}
		},
       error: function(){
       	$.messager.alert('提示','请求失败！',null,function(){});
       }
	});
}

//Third HisAdvice End

//Fourth Drug Start

//加载dialog
function AdddilogDrugs(title, url) {
	$('#addun').dialog({	
		title: title,	
		width:'70%',	
		height:'70%',	
		closed: false,	
		cache: false,	
		href: url,	
		modal: true   
	});	
}
 
 //查询
function searchFromDrug() {
	var drugName = $('#drugName').textbox('getValue');
	$('#listdurg').datagrid('load', {
		drugName : drugName
	});
}

//Fourth Drug End

//Fifth UnDrug Start

//加载dialog
function AdddilogUndrug(title, url) {
	$('#addun').dialog({
		title: title,	
		width:'70%',	
		height:'70%',
		closed: false,	
		cache: false,	
		href: url,	
		modal: true   
	});	
}
 
//查询
function searchFromNondrug() {
	var undrugName = $('#undrugName').textbox('getValue');
	$('#listnon').datagrid('load', {
		undrugNamequery : undrugName
	});
}

//显示疾病类别格式化 
function diseasetypeFamater(value){
	if(value!=null){
		return diseaseclassificationMap[value];	
	}
}
//替换字符
function replaceTrueOrFalse(val){
	if(val == 1){
		return '是';
	}if(val == 0){
		return '否';
	}if(val == ""){
		return '否';
	}
	
}

//Fifth UnDrug End

//Sixth Other Start

/**  
 *  
 * 禁用功能选项
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:59  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:59  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function disFunction(){
	$('#adProjectNumTdId').numberspinner('disable');
	$('#adProjectUnitTdId').combobox('disable');
	$('#adWestMediDosMaxTdId').numberbox('disable');
	$('#adWestMediDosMinTdId').numberbox('disable');
	$('#adWestMediFreTdId').combobox('disable');
	$('#adWestMediUsaTdId').combobox('disable');
	$('#adWestMediRemTdId').combobox('disable');
	$('#adWestMediSkiTdId').combobox('disable');
	$('#adWestMediUrgTdId').attr("disabled",true);
	$('#adChinMediNumTdId').numberbox('disable');
	$('#adChinMediUsaTdId').combobox('disable');
	$('#adChinMediRemTdId').combobox('disable');
	$('#adNotDrugExeTdId').combobox('disable');
	$('#adNotDrugInsTdId').combobox('disable');
	$('#adNotDrugSamTdId').combobox('disable');
	$('#adNotDrugRemTdId').combobox('disable');
	$('#adNotDrugUrgTdId').attr("disabled",true);
}

//Sixth Other End