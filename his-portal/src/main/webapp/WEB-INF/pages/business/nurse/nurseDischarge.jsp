<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
	var payKindMap=new Map();
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
</script>
</head>
<body>
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center'" style="fit:true" align="center">
	    	<form id="editForm" method="post" >
				<table class="honry-table" data-options="width:200px" cellpadding="1" cellspacing="1"  border="1px solid black" style="width:60%;margin-top: 5px">
		    		<tr>
		    			<input  type="hidden" id="admNo" name="inpatientInfo.inpatientNo">
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">病历号:</td>
		    			<td ><input id="caseNo" name="medicalrecordId" readonly="readonly" value="${medicalreId }" class="easyui-textbox" data-options="required:true" />
		    			<input id="inpatientNO" value="${inpatientNo }" type="hidden"></td>
		    			<input id="infoId" type="hidden" name="id"></td>
		    			<td  class="honry-lable" style="font: 14px;text-align: right;width: 15%">姓名:</td>
		    			<td><input id="name" name="name" class="easyui-textbox" readonly="readonly"/></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">入院日期:</td>
		    			<td>
<!-- 		    			<input id="inTime" name="inTime" class="easyui-textbox" readonly="readonly"/> -->
		                <input id="inTime" name="inTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    			</td>
		    		</tr>
		    		<tr>
		    				<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">性别:</td>
		    			<td><input id="sex" name="sex" class="easyui-textbox" readonly="readonly"/></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">结算类别:</td>
		    			<td><input id="paykindCode" name="paykindCode" class="easyui-textbox" readonly="readonly"/></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">年龄:</td>
		    			<td><input id="age" name="reportAge" class="easyui-textbox" readonly="readonly"/></td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">科室:</td>
		    			<td><input id="deptCode" name="deptCode" class="easyui-textbox" readonly="readonly"/></td>
		    			<td class="honry-lable" style="font: 14px; text-align: right;width: 15%">床号:</td>
		    			<td><input id="bedId" name="bedId" class="easyui-textbox" readonly="readonly"/></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">总费用:</td>
		    			<td><input id="balanceCost" name="balanceCost" class="easyui-textbox" readonly="readonly"/></td>
		    		</tr>
		    		<tr>
						
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">余额:</td>
		    			<td><input id="freeCost" name="balance" class="easyui-textbox" readonly="readonly"/></td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">出院日期:</td>
		    			<td>
			    			<input type="hidden" id="prepayOutdate"  name="prepayOutdate"/>
			    			<input id="outDate"  name="inpatientInfo.outDate" class="Wdate" type="text" value="${date}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    			</td>
		    			<td class="honry-lable" style="font: 14px;text-align: right;width: 15%">出院情况:</td>
		    			<td><input id="state" name="inpatientInfo.outState" data-options="required:true"></td>
		    		</tr>
				</table>
			</form>
			<div style="padding:5px; text-align: center; width: 60%">
				<shiro:hasPermission name="${menuAlias}:function:save">
				&nbsp;&nbsp;<a href="javascript:submitAgo();"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:set">
				&nbsp;<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清&nbsp;除&nbsp;</a>
				</shiro:hasPermission>
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
	<script type="text/javascript">
		$(function(){
			
			var caseNo = $("#inpatientNO").val();
			//渲染科室
					$.ajax({    //病历号
						url:"<%=basePath%>nursestation/nurse/getInfobyId.action",
						data:{inpatientNo:caseNo},
						type:'post',
						success: function(rowData) {
							if(rowData!=""&&rowData!=null ){
								$('#infoId').val(rowData.id);//id
								$('#admNo').val(rowData.inpatientNo);//住院流水号
								$('#caseNo').textbox("setValue",rowData.medicalrecordId);//病历号
								$('#name').textbox("setValue",rowData.patientName);//姓名
								$('#inTime').val(rowData.inDate);//入院日期
								$('#sex').textbox("setValue",rowData.reportSexName);//性别
								$('#paykindCode').textbox("setValue",payKindMap.get(rowData.paykindCode));//结算类别
								var ages=DateOfBirth(rowData.reportBirthday);
								 //年龄
								 if(ages.get("nianling")=="0"){
									$('#age').textbox('setValue',"0");

								 }else{
									$('#age').textbox('setValue',ages.get("nianling")+ages.get('ageUnits'));

								 }
								
								$('#deptCode').textbox('setValue',rowData.deptName);//科室
								if(rowData.freeCost==0){
									$('#freeCost').textbox("setValue","0");//余额
								}else{
									$('#freeCost').textbox("setValue",rowData.freeCost);//余额
								}
								if(rowData.totCost==0){
									$('#balanceCost').textbox("setValue","0");//总费用
								}else{
									$('#balanceCost').textbox("setValue",rowData.totCost);//总费用
								}
								
								$('#prepayOutdate').val(rowData.prepayOutdate);//预约出院时间
								$('#bedId').textbox("setValue",rowData.bedName);//床号
							}
						}
					});
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
		});
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
	//清除页面填写信息
	function clear(){
		$('#editForm').form('clear');
	}
	
		//submit()
		//查看退费申请表
	function submitAgo(){
		if (!$('#editForm').form('validate')) {
			$.messager.alert('提示','请先把信息填写完整后再保存！');
			return false;
		}
		var type=$('#caseNo').textbox('getText');
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
</body>
</html>