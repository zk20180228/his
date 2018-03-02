<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>在院病人费用分析</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div style="width:100%;height:7%;padding: 5px" data-options="region:'north',border-top:0">
	<a  href="javascript:openWin();" class="easyui-linkbutton" data-options="iconCls:'icon-search',disabled:false">选择维度</a>
	</div>
	<div id="deptChoseDiv"  title="分析结果" style="width:100%;height:92%;" data-options="region:'center'">   
		<table id="grid1" class="easyui-datagrid" data-options="fitColumns:true"></table>
	</div>
	<div id="win" class="easyui-window" title="维度选择"
	data-options="iconCls:'icon-book',closed:true,modal:true,closable:true,collapsible:false,minimizable:false,maximizable:false"
	style="width:700px;height:650px">
		 <form id="searchForm">
		 <table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
								<tr>
								    <td align="left" style="border-right: none;width: 30%;" id="msgTd">
										&nbsp;&nbsp;
									</td>
									<td align="right" style="border-left: none;">
										&nbsp;&nbsp; <a href="javascript:confirmWd();" class="easyui-linkbutton" >确定</a>
										&nbsp;&nbsp; <a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
									</td>
								</tr>
								<tr>
								<td style="width: 320px;height: 40px;" class="honry-lable" style="width: 5%;" nowrap="nowrap">统计方式
								 </td>
								 <td>
								 <input type="radio" style='width:50px;' name='n'  value="1" checked="checked"/>年
								 <input type="radio" style='width:50px;' name='n'  value="2" />季
								 <input type="radio" style='width:50px;' name='n'  value="3" />月
								 <input type="radio" style='width:50px;' name='n'  value="4" />日
								 </td>
								</tr>
								<tr>
								 <td style="width: 320px;height: 40px;" class="honry-lable" style="width: 5%;" nowrap="nowrap">年度
								 </td>
								<td style="width: 320px;height: 40px;" class="honry-info" nowrap="nowrap">
										&nbsp;<input class='easyui-textbox' style='width:145px;' name='datevo.year1' id="yearTdId1" />
										&nbsp;至&nbsp;<input class='easyui-textbox' style='width:145px;' name='datevo.year2' id="yearTdId2" />
								</td>
								</tr>
								<tr>
									<td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>季度
									</td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									    &nbsp;<input class='easyui-combobox' style='width:145px;' id='seasonTdId1' name='datevo.quarter1'  value='1'  />
									 &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='seasonTdId2' name='datevo.quarter2'  value='1'  />
										</td>
									 	</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>月度
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									 &nbsp;<input class='easyui-combobox' style='width:145px;' id='monthTdId1' name='datevo.month1'  value='1'  />
								        &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='monthTdId2' name='datevo.month2'  value='1'  />
									  </td>
									 	</tr>
									 	
									 	<!-- <tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>周
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									    &nbsp;<input class='easyui-combobox' style='width:145px;' id='weekTdId1' name='week'  value=''  />
								         &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='weekTdId2' name='week'  value=''  />
									  </td>
									 	</tr> -->
									 	<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>日
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap' id='tmpTdToDay'>
									   &nbsp;<input class='easyui-numberbox' style='width:145px;' id='dayTdId1'  name='datevo.day1' value='1'  />
									   &nbsp;至&nbsp;<input class='easyui-numberbox' style='width:145px;' id='dayTdId2' name='datevo.day2'  value='1'  />
									 </td>
									 </tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>科室
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="deptTdId" class="easyui-combobox" value="" name="dept" style='width:200px;'></input>
								     </td>
									</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>费用类别
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="costTdId" class="easyui-combotree" value="" name="cost" style='width:200px;'></input>
									 
								     </td>
									</tr>
			</table>
		 </form>
	</div>
</body>
<script type="text/javascript">
var columns;
var deptInfo;//存放科室id和name
$(function(){
	$.ajax({
		url:'<%=basePath%>statistics/patientsCost/getDeptInfo.action',
		dataType:'json',
		success:function(data){
			deptInfo=data;
			
		}
	});
	
	$('#costTdId').combobox({
		url : "<c:url value='/statistics/patientsCost/getFeeName.action'/>",
		multiple:true,
		valueField : 'feeStatName',
		textField : 'feeStatName',
	});
	
	$('#deptTdId').combobox({
		url : "<c:url value='/statistics/patientsCost/getDeptInfo.action'/>",
		multiple:true,
		valueField : 'deptName',
		textField : 'deptName',
		
	});
	// 文本框 失去焦点事件
	$("input",$("#yearTdId1").next("span")).blur(function()  
	{  
		if($("#yearTdId1").val()){
			if(!validateNum($("#yearTdId1").val())){
				 $("#yearTdId1").textbox('setValue','');
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>年度只能为非0开头的4位有效整数！</font>"); 
				 $("#yearTdId1").next("span").children().first().focus();
				 return;
			  }
		}
    });
	
   $("#yearTdId2").next("span").children().first().focus(function(){
	   $("#msgTd").html("&nbsp;");
	   if(!$("#yearTdId1").val()){//如果第一个年度没有值则不能输入第二个值
		   $("#msgTd").append("<font color='red'>请先输入第一个年度！</font>");
		   $("#yearTdId1").next("span").children().first().focus();
		   $("#yearTdId2").val("");
	   }
	});
	$("#yearTdId2").next("span").children().first().blur(function(){
	    if($("#yearTdId2").val()){
	    	if(!validateNum($("#yearTdId2").val())){
				 $("#yearTdId2").textbox('setValue','');
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>年度只能为非0开头的4位有效整数！</font>"); 
				 $("#yearTdId2").next("span").children().first().focus();
				 return;
			 }
		     if($("#yearTdId2").val()<=$("#yearTdId1").val()){
		    	 $("#yearTdId2").textbox('setValue','');
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>第二个年度不能小于或等于第一个年度！</font>"); 
				 $("#yearTdId2").next("span").children().first().focus();
				 return;
		     }
	    } 
		
	});
	$('#seasonTdId1').combobox({
		valueField: 'id',
		textField: 'text',
		data: [{
			id: '1',
			text: '1'
		},{
			id: '2',
			text: '2'
		},{
			id: '3',
			text: '3'
		},{
			id: '4',
			text: '4'
		}]
	});
	$('#seasonTdId2').combobox({
		valueField: 'id',
		textField: 'text',
		data: [{
			id: '1',
			text: '1'
		},{
			id: '2',
			text: '2'
		},{
			id: '3',
			text: '3'
		},{
			id: '4',
			text: '4'
		}]
	});
	$('#monthTdId1').combobox({
		valueField: 'id',
		textField: 'text',
		data: [{
			id: '1',
			text: '1'
		},{
			id: '2',
			text: '2'
		},{
			id: '3',
			text: '3'
		},{
			id: '4',
			text: '4'
		},{
			id: '5',
			text: '5'
		},{
			id: '6',
			text: '6'
		},{
			id: '7',
			text: '7'
		},{
			id: '8',
			text: '8'
		},{
			id: '9',
			text: '9'
		},{
			id: '10',
			text: '10'
		},{
			id: '11',
			text: '11'
		},{
			id: '12',
			text: '12'
		}]
	});
	$('#monthTdId2').combobox({
		valueField: 'id',
		textField: 'text',
		data: [{
			id: '1',
			text: '1'
		},{
			id: '2',
			text: '2'
		},{
			id: '3',
			text: '3'
		},{
			id: '4',
			text: '4'
		},{
			id: '5',
			text: '5'
		},{
			id: '6',
			text: '6'
		},{
			id: '7',
			text: '7'
		},{
			id: '8',
			text: '8'
		},{
			id: '9',
			text: '9'
		},{
			id: '10',
			text: '10'
		},{
			id: '11',
			text: '11'
		},{
			id: '12',
			text: '12'
		}]
	});
	
});

function inhos_deptcodefunction(value,row,index){
	for (var i = 0; i < deptInfo.length; i++) {
		if(deptInfo[i].deptName==row.inhos_deptcode){
			return deptInfo[i].deptName;
		}
	}
}
function costfunction(value,row,index){
	return value;
}
function openWin(){
	$('#win').window('open');
}
function validateNum(tmpVal){//非零开头的4位有效数字  "^([1-9][0-9]{0,3})$  1-4位
	var reg = new RegExp("^([1-9][0-9]{3})$");//四位整数  
	return reg.test(tmpVal)
}
function validateNumForAge(tmpVal){//非零开头的3位有效数字  ^([0-9][0-9]{0,2}[\.]?[0-9]{1})$  100.5
	var reg = new RegExp("^([1-9][0-9]{0,2})$");//1-3为有效数字
	return reg.test(tmpVal)
}
function isNullStr( str ){
	if ( str == "" ) return true;
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	return re.test(str);
	}

/**
 * 确定
 */
function confirmWd(){
	var year1=$("#yearTdId1").val();//获取起始年份
	var year2=$("#yearTdId2").val();//获取截止年份
	var quarter1=$("#seasonTdId1").combobox('getValue');//获取起始季度
	var quarter2=$("#seasonTdId2").combobox('getValue');//获取终止季度
	var month1=$("#monthTdId1").combobox('getValue');//获取起始月份
	var month2=$("#monthTdId2").combobox('getValue');//获取终止月份
	var day1=$("#dayTdId1").val();//获取起始日
	var day2=$("#dayTdId2").val();//获取终止日
	var dateVO =[ Number(year1),Number(year2),Number(quarter1),Number(quarter2),Number(month1),Number(month2),Number(day1),Number(day2)];
	var dateType= $('input[name="n"]:checked').val();//获取统计方式
	var dimensionArray=['inhos_deptcode','科室','cost','费用类别'];//构造维度种类数组
	var dimensionNameArray =['药品费用','非药品费用','总费用'];
	columns=publicStatistics(dateVO,dimensionArray,Number(dateType),dimensionNameArray);//调用函数 生成表头
	$('#win').window('close');
	var deptval = $('#deptTdId').combobox('getValues');
	if(deptval.length>1){
		var deptValue='';
		for (var i = 0; i < deptval.length; i++) {
			if("全部"==deptval[i]){
				deptValue='全部';
				break;
			}
			if(i<deptval.length-1){
				deptValue+=deptval[i]+",";
			}else{
				deptValue+=deptval[i];
			}
		}
		
	$('#deptTdId').combobox('setValue',deptValue);
	}
	var costval = $('#costTdId').combobox('getValues');
	if(costval.length>1){
		var costValue='';
		for (var i = 0; i < costval.length; i++) {
			if("全部"==costval[i]){
				costValue='全部';
				break;
			}
			if(i<costval.length-1){
				costValue+=costval[i]+",";
			}else{
				costValue+=costval[i];
			}
		}
		
	$('#costTdId').combobox('setValue',costValue);
	}
	
	var formData=getFormData('searchForm');
	$('#grid1').datagrid({
		url:'<%=basePath%>statistics/patientsCost/getResults.action',
		columns:columns,
		queryParams:formData,
		multiSort:true,
		sortName:'inhos_deptcode',
		remoteSort:false,
		onLoadSuccess:function(){
			mergeCellsOfValue("grid1", 'inhos_deptcode,cost');
		}
	});	
	
}

/**
 * 清除
 */
function clear(){
	window.location.reload();
}

function conveterParamsToJson(paramsAndValues) {
	var jsonObj = {};

	var param = paramsAndValues.split("&");
	for (var i = 0; param != null && i < param.length; i++) {
		var para = param[i].split("=");
		jsonObj[para[0]] = para[1];
	}

	return jsonObj;
}

/**
 * 将表单数据封装为json
 * @param form
 * @returns
 */
function getFormData(form) {
	var formValues = $("#" + form).serialize();

	//关于jquery的serialize方法转换空格为+号的解决方法  
	formValues = formValues.replace(/\+/g, " "); // g表示对整个字符串中符合条件的都进行替换  
	var temp = decodeURIComponent(JSON
			.stringify(conveterParamsToJson(formValues)));
	var queryParam = JSON.parse(temp);
	return queryParam;
}
</script>
</html>