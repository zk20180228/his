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
<title>中途结算</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var paywayMap =new Map();
	var outDate="${now}";
	var fapiao = "${invoiceNo}";
	var feeCodeMap=new Map();
	var medicalrecordId='';
	var sexMap=new Map();
	var payKindMap=new Map();
	var payway="";
	var sign='';//判断是应收还是应返
	//当修改时的操作
	function liandong(){
		var rows=$('#infolist3').datagrid('getRows');
		var len=rows.length;
		for(var i=0;i<len;i++){
			$('#infolist3').datagrid('beginEdit',i);
		}
		var totle;
		if(sign==''){
			totle=$('#zhaoling2').textbox('getValue');//找零
		}else{
			totle=$('#shishou1').textbox('getValue');
		}
		if(len>1){
			var editor = $('#infolist3').datagrid('getEditor',{index:0,field:'cost'});
			for(var i=1;i<len;i++){
				var editors = $('#infolist3').datagrid('getEditor',{index:i,field:'cost'});
				totle-=parseFloat($(editors.target).textbox('getValue'));
			}
			$(editor.target).textbox('setValue',totle.toFixed(2));
		}else if(len>0){
			var editor = $('#infolist3').datagrid('getEditor',{index:0,field:'cost'});
			$(editor.target).textbox('setValue',totle);
		}
	}
	//lyy 打印
	function stamp(){
		$.messager.confirm('确认','要打印费用清单吗？',function(r){
			if(r){
				var timerStr = Math.random();
				var medicalrecordId=$('#medicalrecordId').val();
				if(medicalrecordId!=null&&medicalrecordId!=''){
					window.open ("<c:url value='/iReport/iReportPrint/iReportToBalanceRecord.action?randomId='/>"+timerStr+"&medicalrecordId="+medicalrecordId+"&fileName=SFJSQD",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
				}else{
					$.messager.alert('提示','病历号为空,请填写病历号！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
		});
	}
	//发票打印
	function printInvoBill(){
		var cardNo=$("#medicalrecordId").textbox('getValue');
		if(cardNo==null||cardNo==""){
			$.messager.alert('提示','病历号为空,请填写病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		var timerStr = Math.random();
		window.open ("<c:url value='/inpatient/outbalanceout/printBalace.action?randomId='/>"+timerStr+"&medicalrecordIdSearch="+cardNo,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	
	//计算减免
	function jianmian(){
		var jianmian = $('#jmMoney').val();//减免金额
		var shish = $('#yingshou').val();
		if(shish==null&&shish==""&&jianmian==null&&jianmian==""){
			$.messager.alert('提示','减免不能为空！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			$.ajax({
				url : '<%=basePath%>inpatient/saveInpatientDerate.action?jianmian='+jianmian+'&fapiao='+fapiao+'&bbb='+bbb+'&inpatientNo='+inpatientNo,
			});
		}
		var totCost=$("#jsMoney").val();
		var ecoCost=$("#yhMoney").val();
		var jianmian = $("#jmMoney").val();
		var ybMoney = $("#ybMoney").val();
		var ying = eval(totCost+"-"+detailDebitamount+"-"+parseInt(ybMoney)+"-"+jianmian+"-"+ecoCost);
		ying = parseFloat(ying.toFixed(2));
		$("#yingshou").textbox('setValue',ying);
	}
	
	$(function(){
		
		$('#outDate').val(outDate);
		$('#shishou1').textbox({
			onChange: function (newValue, oldValue) {
				if(newValue==''){
					$('#zhaoling1').textbox('setValue','');
					$('#zhaoling2').textbox('setValue','');
				}else{
					var ys = $('#yingshou').val();
					var sh = $('#shishou1').val();
					if(parseFloat(sh)>parseFloat(ys)){
						$('#payWays').combobox({
							disabled:false
						});
						var zl = parseFloat(sh)-parseFloat(ys);
						zl = parseFloat(zl.toFixed(2));
						$('#zhaoling1').textbox('setValue',zl);
						$('#zhaoling2').textbox('setValue',zl);
					}else if(parseFloat(sh)==parseFloat(ys)){
						$('#zhaoling1').textbox('setValue','0');
						$('#zhaoling2').textbox('setValue','0');
					}else{
						$.messager.alert('提示','实收金额应大于或等于应收金额！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				}
				liandong();
			}
		});
		$('#shishou2').textbox({
			onChange: function (newValue, oldValue) {
				if(newValue==''){
					$('#zhaoling2').textbox('setValue',$('#yingfan').val());
				}else{
					var ys = $('#yingfan').val();
					var sh = $('#shishou2').val();
					var zl = parseFloat(sh)+parseFloat(ys);
					zl = parseFloat(zl.toFixed(2));
					$('#zhaoling2').textbox('setValue',zl);
				}
				liandong();
			}
		});
		$('#shishou3').textbox({
			onChange: function (newValue, oldValue) {
				if(newValue==''){
					$('#zhaoling3').textbox('setValue','');
					$('#zhaoling2').textbox('setValue','');
				}else{
					var ys = $('#yingfan2').val();
					var sh = $('#shishou3').val();
					if(parseInt(sh)>parseInt(ys)){
						$('#payWays').combobox({
							disabled:false
						});
						var zl = parseFloat(sh)-parseFloat(ys);
						zl = parseFloat(zl.toFixed(2));
						$('#zhaoling3').textbox('setValue',zl);
						$('#zhaoling2').textbox('setValue',zl);
					}else if(parseInt(sh)==parseInt(ys)){
						$('#zhaoling3').textbox('setValue','0');
						$('#zhaoling2').textbox('setValue','0');
					}else{
						$.messager.alert('提示','实收金额应大于或等于应收金额！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				}
				liandong();
			}
		});
		bindEnterEvent('medicalrecordId',searchFrom,'easyui');
		//初始化无数据列表
		$("#infolist").datagrid('loadData', { total: 0, rows: [] });
		$("#infolist2").datagrid('loadData', { total: 0, rows: [] });
		/**
		 * 选择支付方式
		 */
		var a=null;
		//渲染支付方式
		$.ajax({
			url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
			type:'post',
			data:{type:"payway"},
			success:function(data){
				payway = data;
				for(var i=0;i<payway.length;i++){
					paywayMap.put(payway[i].encode,payway[i].name);
				}
			}
		});
		
		$('#payWays').combobox({
			data:payway,
			url:'<%=basePath %>inpatient/outbalanceout/queryPaywayCom.action',
			onSelect: function(param){
				var rowss=$('#infolist3').datagrid('getRows');
				a=param.encode;
				for(var q=0;q<rowss.length;q++){
					if(a==rowss[q].payWay){
						return false;
					}
				}
				var rows=$('#infolist3').datagrid('getRows');
				//计算支付方式
				var num
				if(sign==''){
					num=$('#zhaoling2').textbox('getValue');//找零
				}else{
					num=$('#shishou1').textbox('getValue');
				}
				if(rows.length!=0){
					num=0;
				}
				$('#infolist3').datagrid('appendRow',{
					payWay:param.encode,
					cost:num+"",
					bankCode:'',
					bankAccount:'',
					bankAccoutname:'',
					postransNo:''
				});
				
				for(var i=0;i<rows.length;i++){
					$('#infolist3').datagrid('beginEdit',i);
				}
			}
		});
		$('#infolist3').datagrid({
			data:[],
			onBeginEdit:function(rowIndex,rowData){
				if(rowData.payWay=='CA'){
					$('#infolist3').datagrid('beginEdit',rowIndex);
					var ed = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'cost'});
					ed.target.textbox({required:true,
						onChange: function (newValue, oldValue) {
							if(parseInt(newValue)<0){
								$.messager.alert('提示','金额不能为负！');
								return;
							}
						}	
					});
				}else if(rowData.payWay=='CD'||rowData.payWay=='DB'){
					$('#infolist3').datagrid('beginEdit',rowIndex);
					var cost = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'cost'});
					var bankCode = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankCode'});
					var bankAccount = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccount'});
					var postransNo = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'postransNo'});
					cost.target.textbox({
						required:true,
						onChange: function (newValue, oldValue) {
							if(parseInt(newValue)<0){
								$.messager.alert('提示','金额不能为负！');
								return;
							}
							if(sign!=''){
							if(parseInt($('#yingshou').textbox('getValue'))<parseInt(newValue)){
								$.messager.alert('提示','除现金外，其他支付方式不能大于应收应付金额！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}}
							liandong();
						}
					});
					bankCode.target.combobox({required:true});
					bankAccount.target.textbox({required:true});
					postransNo.target.textbox({required:true});
				}else if(rowData.payWay=='CH'||rowData.payWay=='PO'){
					$('#infolist3').datagrid('beginEdit',rowIndex);
					var cost = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'cost'});
					var bankCode = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankCode'});
					var bankAccount = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccount'});
					var bankAccoutname = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccoutname'});
					var postransNo = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'postransNo'});
					cost.target.textbox({
						required:true,
						onChange: function (newValue, oldValue) {
							if(parseInt(newValue)<0){
								$.messager.alert('提示','金额不能为负！');
								return;
							}
							if(sign!=''){
							if(parseInt($('#yingshou').textbox('getValue'))<parseInt(newValue)){
								$.messager.alert('提示','除现金外，其他支付方式不能大于应收应付金额！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}}
							liandong();
						}
					});
					bankCode.target.combobox({required:true});
					bankAccount.target.textbox({required:true});
					bankAccoutname.target.textbox({required:true});
					postransNo.target.textbox({required:true});
				}else if(rowData.payWay=='AJ'){
					$('#infolist3').datagrid('beginEdit',rowIndex);
					var cost = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'cost'});
					var bankCode = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankCode'});
					var bankAccount = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccount'});
					var bankAccoutname = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccoutname'});
					var postransNo = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'postransNo'});
					cost.target.textbox({
						required:true,
						onChange: function (newValue, oldValue) {
							if(parseInt(newValue)<0){
								$.messager.alert('提示','金额不能为负！');
								return;
							}if(sign!=''){
							if(parseInt($('#yingshou').textbox('getValue'))<parseInt(newValue)){
								$.messager.alert('提示','除现金外，其他支付方式不能大于应收应付金额！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}}
							liandong();
						}
					});
					bankCode.target.combobox({required:true});
					bankAccount.target.textbox({required:true});
					bankAccoutname.target.textbox({required:true});
					postransNo.target.textbox({required:true});
				}else{
					$('#infolist3').datagrid('beginEdit',rowIndex);
					var cost = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'cost'});
					var bankCode = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankCode'});
					var bankAccount = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccount'});
					var bankAccoutname = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'bankAccoutname'});
					var postransNo = $('#infolist3').datagrid('getEditor', {index:rowIndex,field:'postransNo'});
					cost.target.textbox({
						required:true,
						onChange: function (newValue, oldValue) {
							if(parseInt(newValue)<0){
								$.messager.alert('提示','金额不能为负！');
								return;
							}
							if(sign!=''){
							if(parseInt($('#yingshou').textbox('getValue'))<parseInt(newValue)){
								$.messager.alert('提示','除现金外，其他支付方式不能大于应收应付金额！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}}
							liandong();
						}
					});
					bankCode.target.combobox({required:true});
					bankAccount.target.textbox({required:true});
					bankAccoutname.target.textbox({required:true});
					postransNo.target.textbox({required:true});
				}
			}
		});
		//渲染最小费用名称
		$.ajax({
			url:"<%=basePath%>inpatient/costDerate/quertFreeCodeMap.action",
			type:'post',
			success:function(feeCodeData){
				feeCodeMap = feeCodeData;
			}
		});
		
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
			type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					payKindMap.put(type[i].encode,type[i].name);
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
				searchFrom();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz_all();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			var inentitys = card_value.split(",");
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:inentitys[5]},
				type:'post',
				async:false,
				success: function(data) {
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#medicalrecordId').textbox('setValue',data);
					searchFrom();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	//根据map对象渲染支付方式
	function funCtionpayMap(value,row,index){
		if(value!=null&&value!=''){
			return paywayMap.get(value);
		}
	}
	//查询
	function searchFrom() {
		var medicalrecordId = $('#medicalrecordId').val();//病历号
		if(medicalrecordId == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		else{
			queryByInpatientNo(medicalrecordId);
		}
	};
	function queryInfo(dataObj,number){
		if(number=="1"){
			if(dataObj[0].inState=='I'){
				//住院流水号
				$("#inpatientNo").val(dataObj[0].inpatientNo);
				//姓名
				$("#patientName").text(dataObj[0].patientName);
				//科室
				$("#deptCode").text(dataObj[0].deptCode);
				//入院时间
				$("#inDate").val(dataObj[0].inDate);
				//结算时间
				$("#outDate").val(outDate);
				//结算类别
				$("#paykindCode").text(payKindMap.get(dataObj[0].paykindCode));
				//病历号
				$('#medicalrecordId').textbox('setValue',dataObj[0].medicalrecordId);
				$('#medicalrecordId1').val(dataObj[0].medicalrecordId);
				//根据住院流水号   获取最大的结算序号
				gainBalanceNo(dataObj[0].inpatientNo);
				//根据住院流水号  获取担保金
				gainBond(dataObj[0].inpatientNo);
				//判断患者是否存在未确认的退费申请
				judgeReturns(dataObj[0].inpatientNo);
			}else if(dataObj[0].inState=='O'){
				$.messager.alert('提示','患者已经进行出院结算了！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}else{
				$.messager.alert('提示','患者是出院登记状态，不能进行中途结算！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
		}else if(number=="2"){
			if(dataObj.inState=='B'||dataObj.inState=='R'||dataObj.inState=='I'){
				//住院流水号
				$("#inpatientNo").val(dataObj.inpatientNo);
				//姓名
				$("#patientName").text(dataObj.patientName);
				//科室
				$("#deptCode").text(dataObj.deptCode);
				//入院时间
				$("#inDate").val(dataObj.inDate);
				//结算时间
				$("#outDate").val(outDate);
				//结算类别
				$("#paykindCode").text(payKindMap.get(dataObj.paykindCode));
				//病历号
				$('#medicalrecordId').textbox('setValue',dataObj.medicalrecordId);
				$('#medicalrecordId1').val(dataObj.medicalrecordId);
				//根据住院流水号   获取最大的结算序号
				gainBalanceNo(dataObj.inpatientNo);
				//根据住院流水号  获取担保金
				gainBond(dataObj.inpatientNo);
				//判断患者是否存在未确认的退费申请
				judgeReturns(dataObj.inpatientNo);
			}else if(dataObj.inState=='O'){
				$.messager.alert('提示','患者已经进行出院结算了！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}else{
				$.messager.alert('提示','患者是出院登记状态，不能进行中途结算！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
		}
	}
	//根据住院流水号   获取最大的结算序号
	function gainBalanceNo(inpatientNo){
		$.ajax({
			url: '<%=basePath%>inpatient/outbalanceout/querybalanceNo.action',
			data:{inpatientNo:inpatientNo},
			type:'post',
			success: function(balanceNo){
				$("#balanceNo").val(balanceNo);
			}
		});
	}
	//根据住院流水号  获取担保金
	function gainBond(inpatientNo){
		$.ajax({
			url: '<%=basePath%>inpatient/outbalanceout/queryInpatientSurety.action',
			data:{inpatientNo:inpatientNo},
			type:'post',
			success: function(suretyCost){
				$('#suretyCost').val(suretyCost);
			}
		});
	}
	//判断患者是否存在未确认的退费申请
	function judgeReturns(inpatientNo){
		$.ajax({
			url: '<%=basePath%>inpatient/outbalanceout/queryInpatientCancelitemList.action',
			data:{inpatientNo:inpatientNo},
			type:'post',
			success: function(Cancelitem){
				if(Cancelitem.resCode=="2"){
					$.messager.alert('提示','患者存在未确认的退费申请，不能进行中途结算！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else if(Cancelitem.resCode=="success"){
					//判断患者是否有未确认的退药申请
					returningMedicine(inpatientNo);
				}
			}
		});
	}
	//判断患者是否有未确认的退药申请
	function returningMedicine(inpatientNo){
	}
	//判断患者是否存在未打印的转押金信息
	function transferCost(inpatientNo){
		$.ajax({
			url : '<%=basePath%>inpatient/outbalanceout/queryInPrepayList.action',
			data:{inpatientNo:inpatientNo},
			type:'post',
			success: function(InPrepay){
				if(InPrepay=="存在"){
					$.messager.alert('提示',"患者存在未打印的中结转押金票据！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					//查询列表信息
					var outDate = "";
					var inDate = "";
					queryList(inpatientNo,inDate,outDate);
				}
			}
		});	
	}
	//查询列表信息
	function queryList(inpatientNo,inDate,outDate){
		//2.查询列表信息
		//获取费用列表
		$('#infolist').datagrid({
			url : '<%=basePath%>inpatient/outbalanceout/getInpatientFee.action',
			queryParams:{inpatientNo:inpatientNo,inDate:inDate,outDate:outDate},
			onUnselect : function(rowIndex, rowData) {
				var rowFydd = $('#infolist').datagrid("getSelections");
				//获取优惠总额
				var ecoCost=0;
				//结算金额
				var totCost=0;
				//自费金额
				var ownCost=0;
				for (var i = 0; i < rowFydd.length; i++) {
					ecoCost+=rowFydd[i].ecoCost;
					totCost+=rowFydd[i].totCost;
					ownCost+=rowFydd[i].ownCost;
				}
				totCost = parseFloat(totCost.toFixed(2));
				$('#yhMoney').text(ecoCost);
				$('#jsMoney').text(totCost);
				$('#zfMoney').val(ownCost);
				shjs();
			},
			onUncheck : function(index, row) {
				var rowFydd = $('#infolist').datagrid("getSelections");
				//获取优惠总额
				var ecoCost=0;
				//结算金额
				var totCost=0;
				//自费金额
				var ownCost=0;
				for (var i = 0; i < rowFydd.length; i++) {
					ecoCost+=rowFydd[i].ecoCost;
					totCost+=rowFydd[i].totCost;
					ownCost+=rowFydd[i].ownCost;
				}
				totCost = parseFloat(totCost.toFixed(2));
				$('#yhMoney').text(ecoCost);
				$('#jsMoney').text(totCost);
				$('#zfMoney').val(ownCost);
				shjs();
			},
			onUncheckAll : function(rows){
				$('#yhMoney').text("0");
				$('#jsMoney').text("0");
				$('#zfMoney').val("0");
				shjs();
			},
			onCheckAll : function(rows){
				//获取优惠总额
				var ecoCost=0;
				//结算金额
				var totCost=0;
				//自费金额
				var ownCost=0;
				for (var i = 0; i < rows.length; i++) {
					ecoCost+=rows[i].ecoCost;
					totCost+=rows[i].totCost;
					ownCost+=rows[i].ownCost;
				}
				totCost = parseFloat(totCost.toFixed(2));
				$('#yhMoney').text(ecoCost);
				$('#jsMoney').text(totCost);
				$('#zfMoney').val(ownCost);
				shjs();
			},
			onSelect : function(rowIndex, rowData) {
				var rowFydd = $('#infolist').datagrid("getSelections");
				if(rowFydd.length==0){
					$('#yhMoney').text("0");
					$('#jsMoney').text("0");
					$('#zfMoney').val("0");
					shjs();
				}else{
					//获取优惠总额
					var ecoCost=0;
					//结算金额
					var totCost=0;
					//自费金额
					var ownCost=0;
					for (var i = 0; i < rowFydd.length; i++) {
						ecoCost+=rowFydd[i].ecoCost;
						totCost+=rowFydd[i].totCost;
						ownCost+=rowFydd[i].ownCost;
					}
					totCost = parseFloat(totCost.toFixed(2));
					$('#yhMoney').text(ecoCost);
					$('#jsMoney').text(totCost);
					$('#zfMoney').val(ownCost);
					shjs();
				}
			},
			onLoadSuccess:function(data1){
				$('#infolist').datagrid('selectAll');
				var rowFy = $('#infolist').datagrid("getRows");
				if(rowFy.length=="0"){
					$.messager.alert('提示','该患者还未进行收费或已经结算完成！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					//获取优惠总额
					var ecoCost=0;
					//结算金额
					var totCost=0;
					//自费金额
					var ownCost=0;
					for (var i = 0; i < rowFy.length; i++) {
						ecoCost+=rowFy[i].ecoCost;
						totCost+=rowFy[i].totCost;
						ownCost+=rowFy[i].ownCost;
					}
					totCost = parseFloat(totCost.toFixed(2));
					$('#yhMoney').text(ecoCost);
					$('#jsMoney').text(totCost);
					$('#zfMoney').val(ownCost);
					//获取预交金列表
					$('#infolist2').datagrid({
						url : '<%=basePath%>inpatient/outbalanceout/queryPrepayCost.action',
						queryParams:{inpatientNo:inpatientNo,inDate:inDate,outDate:outDate},
						onLoadSuccess:function(data1){
							$('#infolist2').datagrid('selectAll');
							var data = $('#infolist2').datagrid('getRows');
							if(data.length==0){
								$('#yjMoney').text('0');
							}else{
								var prepayCost=0;
								for (var i = 0; i < data.length; i++) {
									prepayCost+=data[i].prepayCost;
								}
								$('#yjMoney').text(prepayCost);
							}
							//判断是否有单病种优惠
							querydrgs(inpatientNo,inDate,outDate);
						},
						onUnselect : function(rowIndex, rowData) {
							var row = $('#infolist2').datagrid("getSelections");
							if(row.length==0){
								$('#yjMoney').text('0');
								shjs();
							}else{
								var prepayCost=0;
								for (var i = 0; i < row.length; i++) {
									prepayCost+=row[i].prepayCost;
								}
								$('#yjMoney').text(prepayCost);
								shjs();
							}
						},
						onSelect : function(rowIndex, rowData) {
							var row = $('#infolist2').datagrid("getSelections");
							if(row.length==0){
								$('#yjMoney').text('0');
								shjs();
							}else{
								var prepayCost=0;
								for (var i = 0; i < row.length; i++) {
									prepayCost+=row[i].prepayCost;
								}
								$('#yjMoney').text(prepayCost);
								shjs();
							}
						},
						onCheckAll : function(rows){
							var prepayCost=0;
							for (var i = 0; i < rows.length; i++) {
								prepayCost+=rows[i].prepayCost;
							}
							$('#yjMoney').text(prepayCost);
							shjs();
						},
						onUncheckAll : function(rows){
							$('#yjMoney').text('0');
							shjs();
						}
					});
				}
			}
		});
	}
	
	function shjs(){
		var zonge = $('#jsMoney').text();
		var jm = $('#jmMoney').textbox('getText');
		var yj = $('#yjMoney').text();
		//实付金额=结算总额-预交金-页面中的减免金额；
		var sfff = zonge-yj-jm; 
		if(sfff>0){
			sign=1;
			sfff = parseFloat(sfff.toFixed(2));
			$('#yingshou').textbox('setValue',sfff);
			$('#yingfan').textbox('setValue','');
			$("#showw").show();
			$("#hidee").hide();
			$("#hidee1").hide();
			$('#payWays').combobox({
				disabled:true
			});
		}else if(sfff==0){
			sign=1;
			$('#yingshou').textbox('setValue',"0");
			$('#yingfan').textbox('setValue','');
			$("#showw").show();
			$("#hidee").hide();
			$("#hidee1").hide();
			$('#payWays').combobox({
				disabled:true
			});
		}else{
			sign='';
			sfff = parseFloat(sfff.toFixed(2));
			$('#yingfan').textbox('setValue',-sfff);
			$('#zhaoling2').textbox('setValue',-sfff);
			$('#yingshou').textbox('setValue','');
			$('#shishou2').textbox('setValue','0')
			$("#showw").hide();
			$("#hidee").show();
			$("#hidee1").hide();
			$('#payWays').combobox({
				disabled:false
			});
		}
	}
	function querydrgs(inpatientNo,inDate,outDate){
		//单病种
		$.ajax({
			url : '<%=basePath%>inpatient/outbalanceout/queryBusinessEcoformula.action',
			data:{inpatientNo:inpatientNo},
			type:'post',
			success: function(drgs){
				if(drgs=="1"){
					$.messager.confirm('确认', '是否使用单病种优惠吗?', function(res){
						if (res){
							$.ajax({
								url : '<%=basePath%>inpatient/outbalanceout/queryBusinessEcoicdfee.action',
								data:{inpatientNo:inpatientNo},
								type:'post',
								success: function(Cost){
									$("#jmMoney").textbox('setValue',Cost);//减免总额
									$("#yhMoney").text('0');//优惠总额
									//计算费用
									feiyong(inpatientNo);
								}
							});
						}else{
							$("#jmMoney").textbox('setValue','0');//减免总额
							//计算费用
							feiyong(inpatientNo);
						}
					});	
				}else{
					$("#jmMoney").textbox('setValue','0');//减免总额
					//计算费用
					feiyong(inpatientNo);
				}
			}
		});	
	}
	//计算费用
	function feiyong(inpatientNo){
		$.ajax({
			url : '<%=basePath%>inpatient/outbalanceout/inpatientInfoqueryFee.action',
			data:{inpatientNoFee:inpatientNo},
			type:'post',
			success: function(cos){
				//医保公费记账
				$('#ybMoney').text(cos);
				var zonge = $('#jsMoney').text();
				var jm = $('#jmMoney').textbox('getText');
				var yj = $('#yjMoney').text();
				//实付金额=结算总额-预交金-页面中的减免金额；
				var sfff = zonge-yj-jm; 
				//个人自付金额=实付金额-患者结算费用中（住院主表）的院内账户支付金额-冲预交金累计
				var zf = sfff-cos-yj;
				//获取个人自付累计
				$('#grMoney').text("0");
				if(sfff>0){
					sign=1;
					sfff = parseFloat(sfff.toFixed(2));
					$('#yingshou').textbox('setValue',sfff);
					$('#yingfan').textbox('setValue','');
					$("#showw").show();
					$("#hidee").hide();
					$("#hidee1").hide();
					$('#payWays').combobox({
						disabled:true
					});
					deleteAll();
				}else if(sfff==0){
					sign=1;
					$('#yingshou').textbox('setValue',"0");
					$('#yingfan').textbox('setValue','');
					$("#showw").show();
					$("#hidee").hide();
					$("#hidee1").hide();
					$('#payWays').combobox({
						disabled:true
					});
					deleteAll();
				}else{
					sign=0;
					sfff = parseFloat(sfff.toFixed(2));
					$('#yingfan').textbox('setValue',-sfff);
					$('#yingshou').textbox('setValue','0');
					$("#showw").hide();
					$("#hidee").show();
					$("#hidee1").hide();
					$('#payWays').combobox({
						disabled:false
					});
					deleteAll();
				}
			}
		});	
	}
	/**
	 * 根据病历号查询住院信息
	 * @author  hedong
	 * @date 2015-12-2
	 * @version 1.0
	 */
	function queryByInpatientNo(medicalrecordId){
		$.ajax({//查询住院信息
			url: '<%=basePath%>inpatient/outbalanceout/queryByInpatientNo.action',
			data:{medicalrecordIdSearch:medicalrecordId},
			type:'post',
			success: function(dataObj) {
				if(dataObj.length==0){
					$.messager.alert('提示','未查询到患者！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else if(dataObj.length==1){
					//显示信息
					queryInfo(dataObj,1);
				}else if(dataObj.length>1){
					$("#diaInpatient").window('open');
					$("#infoDatagrid").datagrid({
						data:dataObj,
						columns:[[
							{field:'inpatientNo',title:'住院流水号',width:'25%',align:'center'} ,    
							{field:'medicalrecordId',title:'病历号',width:'25%',align:'center'} ,  
							{field:'reportSex',title:'性别',width:'25%',align:'center',formatter:function(value,row,index){
								return sexMap.get(value);
							}},
							{field:'patientName',title:'姓名',width:'25%',align:'center'} ,   
						]] ,
						onDblClickRow:function(rowIndex, rowData){
							$("#diaInpatient").window('close');
							//显示信息
							queryInfo(rowData,2);
						}
					});
				}
			}
		});
	}
	//保存
	function baocun() {
		var inpatientNo = $("#inpatientNo").val();
		var medicalrecordId = $('#medicalrecordId').val();//病历号
		var ling1 = $('#zhaoling1').val();
		var ling2 = $('#zhaoling2').val();
		var ling3 = $('#zhaoling3').val();
		var sh = $('#yingshou').val();
		var sh1 = $('#yingfan').val();
		var sh2 = $('#yingfan2').val();
		var balanceNo = $("#balanceNo").val();
		var jmMoney = $("#jmMoney").val();
		var inDate = $('#inDate').val();
		var outDate = $('#outDate').val();
		if(medicalrecordId==''||medicalrecordId==null){
			$.messager.alert('提示','请先输入病历号进行查询！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			//封账前金额校验
			var total;//总金额
			var editor;//支付金额
			var rows=$('#infolist3').datagrid('getRows');
			var len=rows.length;
			if(len>0){
				if(sign==''){
					total=$('#zhaoling2').textbox('getValue');//找零
				}else{
					total=$('#shishou1').textbox('getValue');
				}
				 var editors; 
				for(var i=0;i<len;i++){
					editors = $('#infolist3').datagrid('getEditor',{index:i,field:'cost'});
					total-=parseFloat($(editors.target).textbox('getValue'));
				}
				if(total>0){
					$.messager.alert('提示','支付金额小于总金额');
					return false;
				}
			}
			//1.对患者进行封账
			$.ajax({
				url : '<%=basePath%>inpatient/outbalanceout/updateInpatientInfoList.action',
				data:{inpatientNo:inpatientNo},
				type:'post',
				success:function(date){
					if(date=="success"){
						var row = $('#infolist2').datagrid('getSelections');
						var prepayIds = "";
						for (var i = 0; i < row.length; i++) {
							if(prepayIds==""){
								prepayIds=row[i].id;
							}else{
								prepayIds+=","+row[i].id;
							}
						}
						if(sh=="0"||sh1=="0"||sh2=="0"){
							var yj = $('#yjMoney').text();
							$('#infolist3').datagrid('acceptChanges');
							$('#zfJson').val(JSON.stringify( $('#infolist3').datagrid("getRows")));
							$('#costJson').val(JSON.stringify( $('#infolist').datagrid("getRows")));
							$('#editForm').form('submit',{
								onSubmit:function(){ 
									if (!$('#editForm').form('validate')){
						  		 		$.messager.progress('close');
										$.messager.show({  
										     title:'提示信息' ,   
										     msg:'验证没有通过,不能提交表单!'  
										}); 
										   return false ;
								     }
						  		 	$.messager.progress({text:'保存中，请稍后...',modal:true});
						        },
								url:'<%=basePath%>inpatient/outbalanceout/saveBalanceZhongtu.action',
								queryParams:{outDate:outDate,inDate:inDate,sh:sh,sh1:sh1,jmMoney:jmMoney,yj:yj,zfMoney:$('#zfMoney').val(),invoiceNo:fapiao,balanceNo:balanceNo,inpatientNo:inpatientNo,medicalrecordIdSearch:medicalrecordId,prepayIds:prepayIds},
								success:function(data){
									$.messager.progress('close');
									$.messager.alert("操作提示", "结算成功！", "success");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								},
								error : function(data) {
									$.messager.alert('提示','结算失败！');
									$.messager.progress('close');
								}
							});
						}else{
							if((ling1==''||ling1==null)&&(ling2==''||ling2==null)&&(ling3==''||ling3==null)){
								$.messager.alert('提示','金额不能为空！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else{
								var yj = $('#yjMoney').text();
								$('#infolist3').datagrid('acceptChanges');
								if(JSON.stringify( $('#infolist3').datagrid("getRows")).length=="2"){
									$.messager.alert('提示','请先添加支付信息！');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}else{
									$('#costJson').val(JSON.stringify( $('#infolist').datagrid("getRows")));
									$('#zfJson').val(JSON.stringify( $('#infolist3').datagrid("getRows")));
									$('#editForm').form('submit',{
										onSubmit:function(){  
											if (!$('#editForm').form('validate')){
								  		 		$.messager.progress('close');
												$.messager.show({  
												     title:'提示信息' ,   
												     msg:'验证没有通过,不能提交表单!'  
												}); 
												   return false ;
										     }
								  		 	$.messager.progress({text:'保存中，请稍后...',modal:true});
								        },
										url:'<%=basePath%>inpatient/outbalanceout/saveBalanceZhongtu.action',
										queryParams:{outDate:outDate,inDate:inDate,sh:sh,sh1:sh1,jmMoney:jmMoney,yj:yj,zfMoney:$('#zfMoney').val(),invoiceNo:fapiao,balanceNo:balanceNo,inpatientNo:inpatientNo,medicalrecordIdSearch:medicalrecordId,prepayIds:prepayIds},
										success:function(data){
											if(data=='success'){
												 $.messager.progress('close');
												 $.messager.alert('提示信息','结算成功！','info');
												 setTimeout(function(){
														$(".messager-body").window('close');
													},3500);
												 clear(true);
											}else{
												 $.messager.alert('提示信息',data,'info');
												 $.messager.progress('close');
											}
										}
									});
								}
							}
						}
					}else{
						$.messager.alert('提示',date); 
					}
				}
			});	
		}
	};
	//按时间查询
	function zhixing(){
		var inDate = $('#inDate').val();
		var outDate = $('#outDate').val();
		var inpatientNo = $("#inpatientNo").val();
		if(inpatientNo==null||inpatientNo==''){
			$.messager.alert('提示','请先查询患者信息！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			queryList(inpatientNo,inDate,outDate);
		}
	}
	//计算器
	function Run(){
		Adddilog("计算器",'<%=basePath %>inpatient/outbalanceout/counter.action','281px','192px');
	}
	
	//渲染最小费用
	function functionFreeCode(value,row,index){
		if(value!=null&value!=""){
			return feeCodeMap[value];
		}
	}
	//清空
	function clear(){
		$('#medicalrecordId').textbox('setValue','');
		$('#patientName').text('');
		$('#deptCode').text('');
		$('#paykindCode').text('');
		$('#inpatientNo').val('');
		$('#inDate').val('');
		$('#jmMoney').textbox('setValue','');
		$('#yhMoney').text('');
		$('#fs').val('');
		$('#jsMoney').text('');
		$('#grMoney').text('');
		$('#ybMoney').text('');
		$('#yjMoney').text('');
		
		$('#shishou2').textbox('setValue','');
		$('#zhaoling2').textbox('setValue','');
		$('#yingfan').textbox('setValue','');
		$('#shishou3').textbox('setValue','');
		$('#zhaoling3').textbox('setValue','');
		$('#yingfan2').textbox('setValue','');
		
		$('#yingshou').textbox('setValue','');
		$('#zhaoling1').textbox('setValue','');
		$('#shishou1').textbox('setValue','');
		//初始化无数据列表
		var item = $('#infolist').datagrid('getRows');    
        for (var i = item.length - 1; i >= 0; i--) {    
            var index = $('#infolist').datagrid('getRowIndex', item[i]);    
            $('#infolist').datagrid('deleteRow', index);    
        } 
		var item2 = $('#infolist2').datagrid('getRows');    
        for (var i = item2.length - 1; i >= 0; i--) {    
            var index = $('#infolist2').datagrid('getRowIndex', item2[i]);    
            $('#infolist2').datagrid('deleteRow', index);    
        } 
		var item3 = $('#infolist3').datagrid('getRows');    
        for (var i = item3.length - 1; i >= 0; i--) {    
            var index = $('#infolist3').datagrid('getRowIndex', item3[i]);    
            $('#infolist3').datagrid('deleteRow', index);    
        } 
	}
	//加载模式窗口
	function Adddilog(title, url,width,height) {
		$('#menuWin').dialog({
			title: title,
			width: width,
			height: height,
			closed: false,
			cache: false,
			href: url,
			modal: false
			});
	}
	/**
	 * 关闭dialog
	 */
	function closeDialog() {
		$('#menu').dialog('close');
	}
	/**
	 * 删除添加的支付方式
	 */
	function deleteBill(){
		var rows1 = $('#infolist3').datagrid('getSelections');
		for(var i=0;i<rows1.length;i++){
			$('#infolist3').datagrid('deleteRow', $('#infolist3').datagrid('getRowIndex', rows1[i]));
		}
	}
	//第二次查询删除全部
	function deleteAll(){
		var rows1 = $('#infolist3').datagrid('getRows');
		var len=rows1.length;
		for(var i=len-1;i>=0;i--){
			$('#infolist3').datagrid('deleteRow',i);
		}
	}
	$(function(){
 		var colo=$('#colors').css('border-color');
 		colo=colo.split(')')[0]+')';
		$('#one').css('border-color',colo);
		$('#two').css('border-color',colo);
		$('#three').css('border-color',colo);
	});
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.halfwayList .panel-header{
	border-top:0;
	border-left:0
}
</style>
</head>
<body style="margin: 0px; padding: 0px"> 
	<div id="cc" class="easyui-layout" data-options="fit:true">   
		<div data-options="region:'north',border:false" style="height:150px;width: 100%">
			<div id="bb" class="easyui-layout"  data-options="fit:true">   
				<div data-options="region:'north',border:false" style="height:50px;padding-top:10px;">
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)"  onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search" style="width: 65px;height: 25px;">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:readCard">
								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
							</shiro:hasPermission>
				        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
				        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:save">
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="baocun()" data-options="iconCls:'icon-save'" style="width: 65px;height: 25px;">保存</a>
							</shiro:hasPermission>
								<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" data-options="iconCls:'icon-clear'" style="width: 65px;height: 25px;">清屏</a>
							<shiro:hasPermission name="${menuAlias}:function:print">
								<a href="javascript:stamp();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',disabled:true" style="width: 65px;height: 25px;">打印</a>
							</shiro:hasPermission>
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="Run()" data-options="iconCls:'icon-calculator'" style="width: 80px;height: 25px;">计算器</a>
							<shiro:hasPermission name="${menuAlias}:function:print">
								<a class="easyui-linkbutton" onclick="printInvoBill()" data-options="iconCls:'icon-2012081511202',disabled:true" style="width: 95px;height: 25px;">发票打印</a>
							</shiro:hasPermission>
							&nbsp;&nbsp;
								日期：
								<input id="inDate"  name="inDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								到
								<input id="outDate" name="outDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d 23:59:59'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<a href="javascript:void(0)" onclick="zhixing()" class="easyui-linkbutton" data-options="iconCls:'icon-implement'">执行</a>
							<span id="invoke" style="color: red;">发票号：${invoiceNo}</span>
				</div>
				<div data-options="region:'center',border:false" style="height: 100px" >
					<table   class="honry-table" align="center" style="width: 100%;height: 100%">
						<tr >
<!-- 							<td class="honry-lable" style="width: 12%;">病历号：</td> -->
							<td  class="honry-lable" style="width: 12%;">病历号：</td>
							<td style="width: 12%;"><input name="medicalrecordId1" id="medicalrecordId" data-options="prompt:'请输入病历号查询'" class="easyui-textbox" >
							<input name="medicalrecordId" id="medicalrecordId1" type="hidden" >
								<input id="inpatientNo" type="hidden">
								<input id="zfb" type="hidden">
								<input id="zfMoney" type="hidden">
								<input id="balanceNo" type="hidden">
								<input id="suretyCost" type="hidden">
							</td>
							<td style="width: 12%;" class="honry-lable">姓名：</td>
							<td style="width: 12%;" id="patientName" name="patientName"></td>
							<td style="width: 12%;" class="honry-lable">科室：</td>
							<td style="width: 12%;" id="deptCode" name="deptCode"></td>
							<td style="width: 12%;" class="honry-lable" >结算类别：</td>
							<td style="width: 12%;" id="paykindCode" name="paykindCode">
							</td>
						</tr>
						<tr>
							<td style="width: 12%;" class="honry-lable" >减免金额：</td>
								<td style="width: 12%;"><input id="jmMoney" name="jmMoney" onchange="jianmian()" class="easyui-textbox"></td>
							<td style="width: 12%;" class="honry-lable" >优惠金额：</td>
							<td style="width: 12%;" id="yhMoney" name="yhMoney"><input  type="hidden" id="fs"></td>
							<td style="width: 12%;" class="honry-lable" >医保/公费记账：</td>
							<td style="width: 12%;" id="ybMoney" name="ybMoney"></td>
							<td style="width: 12%;" class="honry-lable" >冲预交金累计：</td>
							<td style="width: 12%;" id="yjMoney" name="yjMoney"></td>		
						</tr>
						<tr>
							<td style="width: 12%;" class="honry-lable">结算金额：</td>
							<td style="width: 12%;" id="jsMoney" name="jsMoney"></td>
							<td style="width: 12%;" class="honry-lable" >个人自付累计：</td>
							<td style="width: 12%;" id="grMoney" name="grMoney" colspan="5"></td>
							
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false,iconCls:'icon-book'" style="height:28%;width: 100%">
			<div id="divLayout" class="easyui-layout halfwayList" data-options="fit:true,border:true">
				<div  id="colors" data-options="region:'west',border:true,title:'费用明细列表',iconCls:'icon-book'" style="width:49%;height:100%;">
					<table  class="easyui-datagrid" id="infolist" data-options="method:'post',border:false,fit:true,rownumbers:true">
						<thead>
							<tr>
							<th data-options="field:'feiyongmingxi',checkbox:true"></th>
								<th data-options="field:'feeCode',formatter:functionFreeCode" style="width: 30%">
									费用科目
								</th>
								<th data-options="field:'totCost'" style="width: 30%" align="right" halign="left">
									未结金额        
								</th>
								<th data-options="field:'totCost'" style="width: 30%" align="right" halign="left">
									结账金额 
								</th>
							</tr>
						</thead>
					</table>
				</div>
				<div data-options="region:'center',border:true,title:'预交金列表',iconCls:'icon-book'" style="width:50%;height:100%;border-top:0">
						<table class="easyui-datagrid" id="infolist2" data-options="method:'post',border:false,fit:true,rownumbers:true,idField:'id'">
							<thead>
								<tr>
									<th data-options="field:'yujiao',checkbox:true"></th>
									<th data-options="field:'createTime'" style="width: 30%">
										收取时间
									</th>
									<th data-options="field:'payWay',formatter:funCtionpayMap" style="width: 30%">
										支付方式
									</th>
									<th data-options="field:'prepayCost'" style="width: 30%" align="right" halign="left">
										预交金额
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
		</div>
		<div data-options="region:'south'" style="height:50%;width: 100%">
			<div id="aa" class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'north',border:false" id="showw"	style="width:100%;height:80px;">
					<fieldset id="one" class="honry-table changeskin"><legend><font style="font-weight: bold;font-size: 12px;">补收款项</font></legend>
					<table class="honry-table" style="width: 100%;">
						<tr>
							<td class="honry-lable" style="width: 15%">应收：</td>
							<td  style="width: 20%">
								&nbsp;&nbsp;<input name="" id="yingshou" class="easyui-textbox" >
							</td>
							<td  class="honry-lable" style="width: 15%">实收：</td>
							<td  style="width: 20%" >
								&nbsp;&nbsp;
								<input name="" id="shishou1" class="easyui-textbox" >
							</td>
							<td  class="honry-lable"  style="width: 15%">找零：</td>
							<td  style="width: 15%">
								&nbsp;&nbsp;
								<input name="" style="color: lime;" id="zhaoling1" class="easyui-textbox" >
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				
				<div data-options="region:'north'" id="hidee"	style="width:100%;height:80px;display: none">
					<fieldset id="two"><legend><font style="font-weight: bold;font-size: 12px;">返还款项</font></legend>
					<table class="honry-table" style="width: 100%">
						<tr>
							<td class="honry-lable" style="width: 15%">应返：</td>
							<td  style="width: 20%">
								&nbsp;&nbsp;
								<input name="" id="yingfan" class="easyui-textbox" >
							</td>
							<td  class="honry-lable" style="width: 15%">
									实收：
							</td>
							<td  style="width: 20%" >
								&nbsp;&nbsp;
								<input name="" id="shishou2" onchange="qwer2()" class="easyui-textbox" >
							</td>
							<td  class="honry-lable"  style="width: 15%">
									找零：
							</td>
							<td  style="width: 15%">
								&nbsp;&nbsp;
								<input name="" style="color: lime;" id="zhaoling2" class="easyui-textbox" >
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				<div data-options="region:'north'" id="hidee1"	style="width:100%;height:80px;display: none">
					<fieldset id="three"><legend><font style="font-weight: bold;font-size: 12px;">返还款项</font></legend>
					<table class="honry-table" style="width: 100%">
						<tr>
							<td class="honry-lable" style="width: 15%">应返：</td>
							<td  style="width: 20%">
								&nbsp;&nbsp;
								<input name="" id="yingfan2" class="easyui-textbox" >
							</td>
							<td  class="honry-lable" style="width: 15%">
									实收：
							</td>
							<td  style="width: 20%" >
								&nbsp;&nbsp;
								<input name="" id="shishou3" readonly="readonly" onchange="qwer3()" class="easyui-textbox" >
							</td>
							<td  class="honry-lable"  style="width: 15%">
									找零：
							</td>
							<td  style="width: 15%">
								&nbsp;&nbsp;
								<input name="" style="color: lime;" id="zhaoling3" class="easyui-textbox" >
							</td>
						</tr>
					</table>
					</fieldset>
				</div>
				<div data-options="region:'center',border:false,title:'支付列表'" style="width: 100%; ">
						<form id="editForm" method="post">
						<input type="hidden" id="zfJson" name="zfJson">
						<input type="hidden" id="costJson" name="costJson">
							<table class="easyui-datagrid" id="infolist3"  data-options="checkOnSelect:true,selectOnCheck:true,
							singleSelect:false,method:'post',rownumbers:true,
							idField:'id',border:false,toolbar:'#toolbarId'">
								<thead>
									<tr>
										<th data-options="field:'payWay',formatter:funCtionpayMap" style="width: 15%" >
											支付方式
										</th>
										<th data-options="field:'cost'" editor ="{type:'textbox'}" style="width: 15%" align="right" halign="left">
											金额
										</th>
										<th data-options="field:'bankCode'" editor="{type:'combobox',options:{
																		url:'<%=basePath %>inpatient/outbalanceout/queryBank.action',
																		valueField:'encode',
																		textField:'name'
																	}}"  style="width: 15%">
											开户银行 
										</th>
										<th data-options="field:'bankAccount'" editor ="{type:'textbox'}" style="width: 15%">
											开户账户
										</th>
										<th data-options="field:'bankAccoutname'" editor ="{type:'textbox'}" style="width: 15%">
											开户单位
										</th>
										<th data-options="field:'postransNo'" editor ="{type:'textbox'}" style="width: 15%">
											支票号/交易流水号
										</th>
									</tr>
								</thead>
							</table>
						</form>
					</div>
					<div id="toolbarId">
						<input id="payWays" class="easyui-combobox" data-options="disabled:true,prompt:'请选择支付方式',valueField:'encode',textField:'name'">
						<a href="javascript:void(0)" onclick="deleteBill()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
						<span style='color:#C24641;font-size:12px;' class="halfwaytip">　选择支付方式后，请双击填写支付信息</span>
					</div>	
				</div>
			</div>
		</div>
	<div id="menuWin"></div>
	<div id="menu"></div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;" data-options="modal:true, closed:true">   
		<table id="infoDatagrid" data-options="fitColumns:true,singleSelect:true,fit:true">   
		</table>
	</div>
</body>
</html>