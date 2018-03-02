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
<title>出院登记</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var payMap=new Map();
	/**  
	 *  
	 * @Description：过滤	
	 * @Author：donghe
	 * @CreateDate：2017-3-24
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
	$(function(){
		
		bindEnterEvent('caseNo1',queryMed,'easyui');//绑定回车事件
		//出院情况
		$('#state').combobox({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
			queryParams:{type:"outcom"},
			valueField : 'encode',
			textField : 'name',
			mode:'local',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		$.ajax({
			url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
			data:{type:"paykind"},
			async:false,
			success: function(data) {
				var payType = data;
				for(var i=0;i<payType.length;i++){
					payMap.put(payType[i].encode,payType[i].name);
				}
			}
		});
	})
	
	
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
				$('#caseNo1').textbox('setValue',data);
				queryMed();
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
					$('#caseNo1').textbox('setValue',data);
					queryMed();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	function queryMed(){
		var type=$('#caseNo1').textbox('getValue');  //病历号
		if(type == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			$.ajax({
				url: '<%=basePath%>inpatient/info/queryInpinfoM.action',
				data:{type:type},
				type:'post',
				success: function(data) {
					var info=data;
					queryinfo(info);
				},error:function(){
					$.messager.alert("提示","未查询到该病历号信息！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$("#editForm").form("reset");
				}
			});
		}
	}
	//回车事件
	function queryMedicalrecordId(){
		var type=$('#caseNo').textbox('getValue');  //就诊卡号
		if(type == ''){
			$.messager.alert('提示','请输入就诊卡号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			$.ajax({
				url: '<%=basePath%>inpatient/info/queryInpinfo.action',
				data:{type:type},
				type:'post',
				success: function(data) {
					var info=data;
					queryinfo(info);
				},error:function(){
					$.messager.alert("提示","未查询到该就诊卡号信息！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$("#editForm").form("reset");
				}
			});
		}	
	}
	function queryinfo(info){
		if(info.length>1){
			$("#diaInpatient").window('open');
			$("#infoDatagrid").datagrid({
				data:info,
				method:"post",
			    columns:[[    
			        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,
			        {field:'id',title:'病历号',width:'20%',align:'center'},
			        {field:'reportSexName',title:'性别',width:'20%',align:'center'},
			        {field:'patientName',title:'姓名',width:'20%',align:'center'}   
			        
			    ]] ,
			    onDblClickRow:function(rowIndex, rowData){
			    	var balance = rowData.balance+"";
			    	$('#freeCost').textbox("setValue",balance);//余额
					$('#admNo').val(info[0].id);//住院号
					$('#caseNo1').textbox("setValue",rowData.medicalrecordId);//病历号
					$('#name').textbox("setValue",rowData.patientName);//姓名
					$("#age").textbox("setValue",rowData.reportAge);
					var ages=DateOfBirth(rowData.reportBirthday);
					 //年龄
					 if(ages.get("nianling")=="0"){
						$('#age').textbox('setValue',"0");

					 }else{
						$('#age').textbox('setValue',ages.get("nianling"));

					 }
					$('#inTime').textbox("setValue",rowData.inDate);//入院日期
					$('#sex').textbox("setValue",rowData.reportSexName);//性别
					$('#deptCode').textbox("setValue",rowData.deptName);//科室
					var totCost="0";
					if(info[0].totCost==null||info[0].totCost==""){
						totCost="0";
					}else{
						totCost = info[0].totCost+"";
					}
					$('#balanceCost').textbox("setValue",totCost);//总费用
					$('#prepayOutdate').val(rowData.prepayOutdate);//预约出院时间
					$('#paykindCode1').textbox("setValue",payMap.get(rowData.paykindCode));//结算类别
					$('#paykindCode').val(rowData.paykindCode);//结算类别
					$('#bedId').val(rowData.bedId);//床号
					$('#bedName').textbox("setValue",rowData.bedName);//床号
					$("#diaInpatient").window('close');										
			    }
			});
		}else if(info.length==1){
					$('#admNo').val(info[0].id);//住院号
					$('#caseNo1').textbox("setValue",info[0].medicalrecordId);//病历号
					$('#name').textbox("setValue",info[0].patientName);//姓名
					$("#age").textbox("setValue",info[0].reportAge);
					var ages=DateOfBirth(info[0].reportBirthday);
					 //年龄
					 if(ages.get("nianling")=="0"){
						$('#age').textbox('setValue',"0");

					 }else{
						$('#age').textbox('setValue',ages.get("nianling"));

					 }
					$('#reportAgeunit').text(ages.get('ageUnits'));
					$('#reportAgeunit1').val(ages.get('ageUnits'));
					$('#inTime').textbox("setValue",info[0].inDate);//入院日期
					$('#sex').textbox("setValue",info[0].reportSexName);//性别
					$('#deptCode').textbox("setValue",info[0].deptName);//科室
					var totCost="0";
					if(info[0].totCost==null||info[0].totCost==""){
						totCost="0";
					}else{
						totCost = info[0].totCost+"";
					}
					$('#balanceCost').textbox("setValue",totCost);//总费用
					var balance = info[0].balance+"";
					$('#freeCost').textbox("setValue",balance);//余额
					$('#prepayOutdate').val(info[0].prepayOutdate);//预约出院时间
					$('#paykindCode1').textbox("setValue",payMap.get(info[0].paykindCode));//结算类别
					$('#paykindCode').val(info[0].paykindCode);//结算类别
					$('#bedId').val(info[0].bedId);//床号
					$('#bedName').textbox("setValue",info[0].bedName);//床号
	}else{
		$.messager.alert("提示","未查询到该病历号信息！");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		$("#editForm").form("reset");
	}
	}
	//清除页面填写信息
	function clear(){
		$('#editForm').form('reset');
	}	
		//submit()
		//查看退费申请表
	function submitAgo(){
		var type=$('#caseNo1').textbox('getText');
		if (!$('#editForm').form('validate')) {
			$.messager.alert('提示','请先把信息填写完整后再保存！');
			return false;
		}
		if(type!=""){
			$.messager.progress({text:'正在处理,请稍等...',modal:true});
			$.ajax({
				url:'<%=basePath%>inpatient/info/queryDrugApplyoutNowList.action',
				data:{'inpatientInfo.inpatientNo':$('#admNo').val()},		
				type:'post',
				success: function(data) {
					if(data>0){
						$("#windowOpen1").window('open');
						$("#druoappleoutDatagrid").datagrid({
							data:data,
							method:"post",
							columns:[[
								{field:'tradeName',title:'项目名称',width:'30%',align:'center'},
								{field:'specs',title:'规格',width:'20%',align:'center'},
								{field:'dfqCexp',title:'频次',width:'20%',align:'center'},
								{field:'orderType',title:'医嘱类别',width:'10%',align:'center'},
								{field:'useName',title:'用法',width:'20%',align:'center'},
							]]
						});
					}else{
						returnCost();//判断退费
					}
				}
			});
		}else{
			$.messager.alert("提示","请输入病历号！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	/**
	* 查询患者是否存在未确认的退费申请
	**/
	function returnCost(){
		$.ajax({
			url:'<%=basePath%>inpatient/info/ajaxCanceliem.action',
			data:{'inpatientInfo.inpatientNo':$('#admNo').val()},		
			type:'post',
			success: function(data) {
				$.messager.progress('close');
				var idCardObj = data;
				if(idCardObj.resMsg=="0"){
					$.messager.alert('提示','您还有未确认退费的申请，请退费或取消退费后再做出院登记！');
				}else if(idCardObj.resMsg=="1"){
					order();//判断患者是否存在未执行未作废的医嘱
				}else if(idCardObj.resMsg=="2"){
					$("#windowOpen").window('open');
					$("#CancelitemNowDatagrid").datagrid({
						data:idCardObj.info,
						method:"post",
						columns:[[
							{field:'itemName',title:'项目名称',width:'30%',align:'center'},
							{field:'specs',title:'规格',width:'20%',align:'center'},
							{field:'chargeFlag',title:'退费状态',width:'20%',align:'center'},
							{field:'quantity',title:'数量',width:'10%',align:'center'},
							{field:'salePrice',title:'价格',width:'20%',align:'center'},
						]]
					});
				}
			}
		});
	};
	/**
	* 判断患者是否存在未执行未作废的医嘱
	**/
	function order(){
		$.ajax({
			url:'<%=basePath%>inpatient/info/queryInpatientOrderNowList.action',
			data:{'inpatientInfo.inpatientNo':$('#admNo').val()},		
			type:'post',
			success: function(data) {
				if(data.length>0){
					$.messager.confirm('确认',"确认出院？还有"+data.length+"条长期医嘱没有停止，如需停止，请取消并通知相关医生停止医嘱！",function(r){    
						if (r){
							submit();
						}
					});
				}else{
					submit();
				}
			}
		});
	}
	//表单提交
	function submit(){
		$("#windowOpen").window('close');
		var outDate = $('#outDate').val();
		if(outDate==null||outDate==''){
			$.messager.alert('提示','请填写出院时间！');
			return false;
		}
		var prepayOutdate = $('#prepayOutdate').val()
		var sDate = new Date(outDate.replace(/\-/g, "\/"));
		if(prepayOutdate==null||prepayOutdate==""){
			save();
		}else{
			if(prepayOutdate=='0001-01-01'||prepayOutdate=='0002-01-01'){
				save();
			}else{
				var eDate = new Date(prepayOutdate.replace(/\-/g, "\/"));
				if(sDate>eDate){
					save();
				}else{
					$.messager.confirm("提示","确定提前出院吗？", function (data) {
						if(data){
							save();
						}				
					});
				}
			}
		}
	}
	function save(){
		var inpatientNo =$('#admNo').val();
		if(inpatientNo==null || inpatientNo.length==0){
			$.messager.alert('提示','还未查询患者信息，不能进行保存操作！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return ;
		}
	    $.messager.progress({text:"保存中,请稍等......",modal:true});
		$('#editForm').form('submit',{  
	        url:'<%=basePath%>inpatient/info/saveInpinfoOut.action', 
	        type:'post',
	    	data:{'inpatientInfo.inpatientNo':$('#admNo').val()},
	        success:function(data){ 
			    $.messager.progress('close');
	        	if(data=="success"){
		        	$.messager.alert('提示','保存成功');
		        	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	        	}else if(data=="error"){
					$.messager.alert('提示','保存失败！');
	        	}
	        	$("#editForm").form('reset');
	        },
			error : function(data) {
				$.messager.alert('提示','保存失败！');	
			}							         
	    });
	}
	function cancel(){
		$("#windowOpen").window('close');
	}
	function cancel1(){
		$("#windowOpen1").window('close');
	}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>		
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout"  class="easyui-layout" fit=true>
		<div data-options="regio:'center',split:false"
			style="padding: 10px; min-height: 80px; height: auto;" data-options="border:false">
			<div id="top" style="height: 25px; line-height: 25px;padding:0px 0px 25px 0px">
			<fieldset style="width:854px;border:0px;margin-left:auto;margin-right:auto;">	
				<table class="honry-table" style="width:100%;padding:5px 5px 1px 5px;"> 
					<tr>
						<td>
							病历号：<input id="caseNo1" name="medicalrecordId" class="easyui-textbox" data-options="prompt:'输入病历号回车查询 ',required:true" />&nbsp;&nbsp;
							<shiro:hasPermission name="${menuAlias}:function:query">
								&nbsp;<a href="javascript:void(0)" onclick="queryMed()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:readCard">
								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
							</shiro:hasPermission>
				        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
				        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:save">
								<a href="javascript:submitAgo();"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
							</shiro:hasPermission>
								<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
						</td>
					</tr>
				</table>
			</fieldset>
			</div>
			<div style="font-size:14px;border: 0px;" data-options="border:false">
				<fieldset style="width:854px;border:0px;margin-left:auto;margin-right:auto;">
					<form id="editForm" method="post" data-options="border:false" style="border: 0px;">	
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;"  data-options="method:'post',split:false">
						<tr>
							<td  class="honry-lable" style="font: 14px;text-align: right;width: 15%">姓名:</td>
			    			<td><input id="name" name="name" class="easyui-textbox" readonly="readonly"/>
			    				<input  type="hidden" id="admNo" name="inpatientInfo.inpatientNo">
			    			</td>
			    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">年龄:</td>
			    			<td><input id="age" name="reportAge" class="easyui-textbox" readonly="readonly"/>
			    			<span id="reportAgeunit" ></span>
							<input id="reportAgeunit1" name="reportAgeunit" type="hidden"/>
			    			</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">性别:</td>
			    			<td><input id="sex" name="sex" class="easyui-textbox" readonly="readonly"/></td>
			    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">结算类别:</td>
			    			<td><input id="paykindCode1" name="paykindCode1" class="easyui-textbox" readonly="readonly"/>
			    				<input id="paykindCode" name="paykindCode" type="hidden"/>
			    			</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">科室:</td>
			    			<td><input id="deptCode" name="deptCode" class="easyui-textbox" readonly="readonly"/></td>
			    			<td class="honry-lable" style="font: 14px; text-align: right;width: 15%">床号:</td>
			    			<td><input id="bedId" name="bedId" type="hidden"/>
			    			<input id="bedName" name="bedName" class="easyui-textbox" readonly="readonly"/></td>
						</tr>
						<tr>
							<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">总费用:</td>
			    			<td><input id="balanceCost" name="balanceCost" class="easyui-textbox" readonly="readonly"/></td>
			    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">余额:</td>
			    			<td><input id="freeCost" name="balance" class="easyui-textbox" readonly="readonly"/></td>
			    		</tr>
			    		<tr>
			    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">入院日期:</td>
			    			<td><input id="inTime" name="inTime" class="easyui-textbox" readonly="readonly"/></td>
							<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">出院时间:</td>
			    			<td>
				    			<input type="hidden" id="prepayOutdate"  name="prepayOutdate"/>
				    			<input id="outDate"  name="inpatientInfo.outDate" class="Wdate" type="text" value="${newDate}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			    			</td>
			    		</tr>
			    		<tr>
			    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">出院情况:</td>
			    			<td colspan="3"><input id="state" name="inpatientInfo.outState" data-options="required:true"></td>
			    		</tr>
					</table>
					</form>
				</fieldset>
			</div>
		</div>
	</div>
	<div id="windowOpen" class="easyui-dialog" title="存在退费申请记录，是否继续？" style="width:500;height:500;" data-options="modal:true, closed:true" align="center">
		<table id="CancelitemNowDatagrid" style="width: 100%;height: 400;" data-options="fitColumns:true,singleSelect:true"></table>
		<a href="javascript:order();"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">继续</a>
		&nbsp;<a href="javascript:cancel();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 60 40 60" data-options="modal:true, closed:true">   
		<table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true"></table>
	</div>
	<div id="windowOpen1" class="easyui-dialog" title="存在有效的未摆药的医嘱" style="width:500;height:500;" data-options="modal:true, closed:true" align="center">
		<table id="druoappleoutDatagrid" style="width: 100%;height: 400;" data-options="fitColumns:true,singleSelect:true"></table>
		&nbsp;<a href="javascript:cancel1();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</body>
</html>