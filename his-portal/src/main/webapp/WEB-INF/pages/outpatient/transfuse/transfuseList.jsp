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
<title>门诊配液</title>
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
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height: 130px;padding:10px 10px 13px 10px">
	<form id="form1">
		就诊卡号：<input  class="easyui-textbox"  data-options="prompt:'就诊卡号 输入回车执行查询'" id="clinicCode1"/>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryPatientInfo()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 30px" >查&nbsp;询&nbsp;</a>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="saveForm()" data-options="iconCls:'icon-save'" style="margin:0px 0px 0px 30px" >保&nbsp;存&nbsp;</a>
<!-- 		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryMedicalrecordId()" data-options="iconCls:'icon-save'" style="margin:0px 0px 0px 30px" >登&nbsp;记&nbsp;</a> -->
<!-- 		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryMedicalrecordId()" data-options="iconCls:'icon-delete'" style="margin:0px 0px 0px 30px" >清&nbsp;空&nbsp;</a> -->
		<br>
		<table class="tableCss" style="width: 100%;margin-top: 10px">
			<tr>
				<td class="TDlabel">姓名：</td>
				<td class="TDinput"><input  class="easyui-textbox"   id="name" readonly="readonly"/></td>
				<td class="TDlabel">性别：</td>
				<td class="TDinput"><input  class="easyui-textbox"  id="sex"  readonly="readonly"/></td>
				<td class="TDlabel">出生日期：</td>
				<td class="TDinput"><input  class="easyui-textbox"  id="birthday"   readonly="readonly"/></td>
				<td class="TDlabel">病历号：</td>
				<td class="TDinput"><input  class="easyui-textbox"  id="patientNo" name="OutpatientMixLiquid.patientNo"readonly="readonly" /></td>
			</tr>
			<tr>
				<td class="TDlabel">开单科室：</td>
				<td class="TDinput"><input  class="easyui-textbox" id="doctDpcd" name="OutpatientMixLiquid.doctDpcd"  readonly="readonly"/></td>
				<td class="TDlabel">开单医生：</td>
				<td class="TDinput"><input  class="easyui-textbox"  id="doctCode" name="OutpatientMixLiquid.doctCode" readonly="readonly"/></td>
				<td class="TDlabel">开单时间：</td>
				<td class="TDinput"><input  class="easyui-textbox" id="operDate" name="OutpatientMixLiquid.operDate" readonly="readonly" /></td>
				<td class="TDlabel">门诊号：</td>
				<td class="TDinput"><input  class="easyui-textbox" id="clinicCode" name="OutpatientMixLiquid.clinicCode" readonly="readonly"/></td>
			</tr>
		</table>
		</form>
	</div>
	<div data-options="region:'center',title:'院注信息'" style="width: 100%;height: 75%;">
		<div id="tt" class="easyui-tabs" data-options="fit:true,tabPosition:'bottom',border:false">   
		    <div title="待院注信息" style="width: 100%;height: 100%;padding:0px 5px 5px 5px;" data-options="border:false">   
		        <table id="table1" class="easyui-datagrid" data-options="fit:true,border:false">
				</table>
		    </div>   
		    <div title="已院注信息" style="width: 100%;height: 100%;padding:0px 5px 5px 5px;" data-options="border:false">   
		          <table id="table2" class="easyui-datagrid" data-options="fit:true,border:false,singleSelect:true,rownumbers:true" >
		          	<thead>
		          		<tr>
		          			<th data-options="field:'confirmDate',width:'8%'" >配液时间</th>
		          			<th data-options="field:'itemName',width:'10%'" >项目名称</th>
		          			<th data-options="field:'frequencyCode',width:'12%',formatter:functionpc" >频次</th>
		          			<th data-options="field:'onceDose',width:'4%'" >每次用量</th>
		          			<th data-options="field:'onceUnit',width:'6%',formatter:functionunit" >每次用量单位</th>
		          			<th data-options="field:'specs',width:'8%'" >规格</th>
		          			<th data-options="field:'injectNumber',width:'4%'" >院注次数</th>
		          			<th data-options="field:'execDpcd',width:'8%',formatter:functionDept" >执行科室</th>
		          			<th data-options="field:'hypotest',width:'10%',formatter:functionps" >皮试结果</th>
		          			<th data-options="field:'confirmCode',width:'7%',formatter:functiondoc" >配液人</th>
		          			<th data-options="field:'describe',width:'22%'" >输液临床反应</th>
		          		</tr>
		          	</thead>
		          </table>
		    </div>   
		</div> 
	</div>
<script type="text/javascript">
	/*
	*
	*/
	var deptMap=null;
	var pcMap=null;
	var untiMap=null;
	var empMap=null;
	var sexMap=new Map();
	$(function(){
		//性别渲染
		$.ajax({
			url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
			data:{"type":"sex"},
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
			}
		});
		//查询医生Map
		$.ajax({
			url:'<%=basePath%>outpatient/transfuse/queryDoctrans.action',
			success:function(data){
				empMap=data;
			}
		});
		//查询科室Map
		$.ajax({
			url:'<%=basePath%>baseinfo/department/getDeptMap.action',
			success:function(data){
				deptMap=data;
			}
		});
		//查询频次Map
		$.ajax({
			url:'<%=basePath%>outpatient/transfuse/queryFrequencyTrans.action',
			success:function(data){
				pcMap=data;
			}
		});
		//每次计量单位
		$.ajax({
			url:'<%=basePath%>outpatient/transfuse/queryOnceUnti.action',
			success:function(data){
				untiMap=data;
			}
		});
		$('#table1').datagrid({
			columns:[[ 
					  {field:'ck',checkbox:true},
					  
			          {field:'itemName',title:'项目名称',width:'8%'},    
			          {field:'frequencyCode',title:'频次',formatter:functionpc,width:'8%'},    
			          {field:'specs',title:'规格',width:'8%'},    
			          {field:'injectNumber',title:'院注次数',width:'7%'},    
			          {field:'onceDose',title:'每次用量',width:'7%'},    
			          {field:'onceUnit',title:'每次用量单位',width:'7%',formatter:functionunit},    
			          {field:'execDpcd',title:'执行科室',formatter:functionDept,width:'8%'},    
			          {field:'hypotest',title:'皮试结果',formatter:functionps,width:'10%'},    
			          {field:'confirmCode',title:'配液人',width:'7%',formatter:functiondoc,
			        	  editor:{type:'combobox',options:{
			        		  url :'<%=basePath%>outpatient/transfuse/queryEmptrans.action',
			        		  valueField:'jobNo',
			        		  textField:'name'
			          	  }}
			          },    
			          {field:'describe',title:'输液临床反应',width:'22%',editor:{type:'textbox'}}  
			]],
			onDblClickRow:function(index, row){
				var rows=$('#table1').datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$('#table1').datagrid('endEdit',i);
				}
				$('#table1').datagrid('beginEdit',index);
			}
		});
		bindEnterEvent('clinicCode1',queryPatientInfo,'easyui');//绑定回车事件
	});
	function queryPatientInfo(){
		var no=$('#clinicCode1').textbox('getValue');
		$.ajax({
			url:'<%=basePath%>outpatient/transfuse/queryPatientYZInfo.action?clinicCode='+no,
			success:function(data){
				var dataMap=data;
				if(dataMap.key=="none"){
					$.messager.alert('提示',dataMap.value,'info');
				}else{
					//姓名
					$('#name').textbox('setValue',dataMap.value2.patientName);
					//性别
					$('#sex').textbox('setValue',sexMap.get(dataMap.value2.patientSex));
					//出生日期
					$('#birthday').textbox('setValue',dataMap.value2.patientBirthday.substring(0,10));
					//病历号
					$('#patientNo').textbox('setValue',dataMap.value2.medicalrecordId);
					//门诊号
					var docData=dataMap.value1[0];
					$('#clinicCode').textbox('setValue',docData.clinicCode);
					//开立科室
					$('#doctDpcd').textbox('setValue',docData.regDeptName);
					//开立医生
					$('#doctCode').textbox('setValue',empMap[docData.doctCode]);
					//开立时间
					$('#operDate').textbox('setValue',docData.operDate);
				}
			}
		});
		$('#table1').datagrid({
			url:'<%=basePath%>outpatient/transfuse/queryRecipedetail.action?clinicCode='+no,
		});
		$('#table2').datagrid({
			url:'<%=basePath%>outpatient/transfuse/queryMixliquid.action?clinicCode='+no,
			onLoadSuccess: function (data) {//默认选中
				$('#table2').datagrid("autoMergeCells", ['confirmDate']);
			} 
		});
	}
	function saveForm(){
		var clinicCode=$('#clinicCode').textbox('getValue');
//  		var rowss=$('#table1').datagrid('getRows');
		var rowss = $('#table1').datagrid('getSelections');
		if(rowss.length<1){
			$.messager.alert('提示','没有数据,无法保存!');
			return;
		}
		for(var i=0;i<rowss.length;i++){
			$('#table1').datagrid('endEdit',i);
			var conf=rowss[i].confirmCode;
			if(typeof(conf)=='undefined'||conf==""){
				$('#table1').datagrid('selectRow',i);
				$.messager.alert('提示','请选择配液人!');
				return;
			}
			if(typeof(empMap[conf])=='undefined'){
				$('#table1').datagrid('selectRow',i);
				$.messager.alert('提示','请填写正确的配液人信息!');
				return;
			}
		}
		var rowdata=JSON.stringify(rowss);
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$.ajax({
			url:'<%=basePath%>outpatient/transfuse/saveform.action',
			type:'post',
			async: true,
			dataType: 'json',
			data:{'rowdata':rowdata,'clinicCode':clinicCode},
			success:function(data){
				if(data){
					$.messager.progress('close');
					$.messager.alert('提示','保存成功');
					 window.location.reload();
				}else{
					$.messager.progress('close');
					$.messager.alert('提示','保存失败');
				}
			}
		});
		
	}
	//科室
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//操作人
	function functiondoc(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	//皮试结果
	function functionps(value,row,index){
		if(value==1){
			return '不需要皮试';
		}else if(value==2){
			return '需要皮试，未做';
		}else if(value==3){
			return '皮试阳';
		}else if(value==4){
			return '皮试阴';
		}
	}
	//频次
	function functionpc(value,row,index){
		if(value!=null&&value!=''){
			return pcMap[value];
		}
	}
	//每次用量单位
	function functionunit(value,row,index){
		if(value!=null&&value!=''){
			return untiMap[value];
		}
	}
	/**
	 * 合并单元格
	 */
	$.extend($.fn.datagrid.methods, {
		autoMergeCells: function (jq, fields) {
			return jq.each(function () {
				var target = $(this);
				if (!fields) {
					fields = target.datagrid("getColumnFields");
				}
				var rows = target.datagrid("getRows");
				var i = 0,
				j = 0,
				temp = {};
				for (i; i < rows.length; i++) {
					var row = rows[i];
					j = 0;
					for (j; j < fields.length; j++) {
						var field = fields[j];
						var tf = temp[field];
						if (!tf) {
							tf = temp[field] = {};
							tf[row[field]] = [i];
						} else {
							var tfv = tf[row[field]];
							if (tfv) {
								tfv.push(i);
							} else {
								tfv = tf[row[field]] = [i];
							}
						}
					}
				}
				$.each(temp, function (field, colunm) {
					$.each(colunm, function () {
					var group = this;
						if (group.length > 1) {
							var before,
							after,
							megerIndex = group[0];
							for (var i = 0; i < group.length; i++) {
								before = group[i];
								after = group[i + 1];
								if (after && (after - before) == 1) {
								    continue;
								}
								var rowspan = before - megerIndex + 1;
								if (rowspan > 1) {
									target.datagrid('mergeCells', {
										index: megerIndex,
										field: field,
										rowspan: rowspan
									});
								}
								if (after && (after - before) != 1) {
								    megerIndex = after;
								}
							}
						}
					});
				});
			});
		}
	});
</script>
</body>
</html>