<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>床位绑定费用维护编辑页面</title>
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'病床费用',iconCls:'icon-form'" style="height: 100%;width: 100%" >
		<div style="padding:5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="financeFixedcharge.id" value="${financeFixedcharge.id}">
			    <table class="honry-table" style="width:550px;height: 320px;align:center;margin-left:auto;margin-right:auto;" >
			     	<tr>
				    	<td align="right" class="changeskinbg">床位等级：</td>　　
				    	<td >
				    	<input style="width: 200px;" id="chargeBedlevel" name="financeFixedcharge.chargeBedlevel" /></td>
				    </tr>
				    <tr>
				    	<td  align="right" class="changeskinbg">项目名称：</td>
				    	<td >
					    	<input style="width: 200px;margin-left: 20px;" id="undrugName" value="${drugUndrugName}"  data-options="required:true" />
					    	<input id="undrug" name="financeFixedcharge.drugUndrug" type="hidden" value="${financeFixedcharge.drugUndrug}" >
<!-- 				   			<a href="javascript:delSelectedData('undrugName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
				    	</td>
				    </tr>
				    <tr>
				    	<td  align="right" class="changeskinbg">单价：</td>
				    	<td ><input style="width: 200px;" id="chargeUnitprice" name="financeFixedcharge.chargeUnitprice" class="easyui-textbox" value="${financeFixedcharge.chargeUnitprice }" data-options="required:true" /></td>
				    </tr>
				    <tr>
					    <td  align="right" class="changeskinbg">数量：</td>
					    <td > <input style="width: 200px;" id="chargeAmount" name="financeFixedcharge.chargeAmount" class="easyui-textbox" value="${financeFixedcharge.chargeAmount }" /> </td>
				    </tr>
				     <tr>
				    	<td  align="right" class="changeskinbg"> 开始时间：</td>
				    	<td >
				    	<!--  <input style="width: 200px;" id="startTime" name="financeFixedcharge.chargeStarttime" class="easyui-datebox" value="${financeFixedcharge.chargeStarttime }" />-->
				    	<input id="startTime" name="financeFixedcharge.chargeStarttime" class="Wdate" type="text" onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',minDate:'%y-%M-%d'})"  value="${financeFixedcharge.chargeStarttime }" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;" readonly/>
				    	</td>
				    </tr>
				    <tr>
				    	<td  align="right" class="changeskinbg">结束时间：</td>
				    	<td >
				    	<input id="endTime" name="financeFixedcharge.chargeEndtime"  class="Wdate" type="text" onClick="WdatePicker({minDate: ['#F{$(\'#startTime\').val()}'],maxDate:'%y-{%M+1}-%d'})"  value="${financeFixedcharge.chargeEndtime }" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;" readonly/>
				    	<!--  <input style="width: 200px;" id="entTime" name="financeFixedcharge.chargeEndtime" class="easyui-datebox" value="${financeFixedcharge.chargeEndtime }" />-->
				    	</td>
				    </tr>
				     <tr>
				    	<td   style="width: 200px;padding-left: 150px;" colspan="2">
				    		<input name="financeFixedcharge.chargeIsaboutchildren" id="isabOutChildeen" type="checkbox" value="${financeFixedcharge.chargeIsaboutchildren }">婴儿相关 
				    		<input id="isabOutTime" name="financeFixedcharge.chargeIsabouttime" type="checkbox" value="${financeFixedcharge.chargeIsabouttime }" > 时间相关
				    	</td>
				    </tr>
				    <tr>
					    <td  align="right" class="changeskinbg">状态：</td>
					    <td>
					    	<input id="chargeState" type="hidden"  name="financeFixedcharge.chargeState" value="${financeFixedcharge.chargeState }" />&nbsp;
					    	<input type="radio" id="rd1" name="state" onclick="stateFunction()" value="1" />在用&nbsp;
					    	<input type="radio" id="rd2" name="state" onclick="stateFunction()" value="2" />停用&nbsp;
					    	<input type="radio" id="rd3" name="state" onclick="stateFunction()"  value="3"/>废弃
			    		</td>
					    
					    
					    
<%-- 					    <td ><input style="width: 200px;" id="chargeState" name="financeFixedcharge.chargeState" value="${financeFixedcharge.chargeState }" /></td> --%>
				    </tr>
			    </table>
			    <div style="text-align:center;padding:5px;margin-top: 20px;">
			    	<a href="javascript:onlyUndrug()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clearEdit()" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	     	</form>
	    </div>
	</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var startDate="${financeFixedcharge.chargeStarttime }";
var endDate="${financeFixedcharge.chargeEndtime }";
	$('#startTime').val(startDate.split(' ')[0]);
	$('#endTime').val(endDate.split(' ')[0]);
//单选按钮
function stateFunction(){
	var state=$("input[name='state']:checked").val();
	$('#chargeState').val(state);
}
var chargeState=$('#chargeState').val();
if(chargeState==1){
	$('#rd1').attr('checked', 'true');
}
if(chargeState==2){
	$('#rd2').attr('checked', 'true');
}
if(chargeState==3){
	$('#rd3').attr('checked', 'true');
}
var isabOutChildeen=$('#isabOutChildeen').val();
if(isabOutChildeen==1){
	$("#isabOutChildeen").attr("checked",'true');
}
var isabOutTime=$('#isabOutTime').val();
if(isabOutTime==1){
	$("#isabOutTime").attr("checked",'true'); 
}
//床位等级
$('#chargeBedlevel').combobox({    
	url:'<%=basePath%>baseinfo/financeFixedcharge/comboboxBedGrade.action',
	 valueField:'encode',    
	 textField:'name',
//	 mode:'remote',	//自动发送到名为'q'的HTTP请求参数到服务器检索新数据。
     required: true,
     onLoadSuccess:function(){
    	 var node=$('#tDt').tree('getSelected');
    	 $('#chargeBedlevel').combobox('select',node.id);
     },onHidePanel:function(none){
    	    var data = $(this).combobox('getData');
    	    var val = $(this).combobox('getValue');
    	    var result = true;
    	    for (var i = 0; i < data.length; i++) {
    	        if (val == data[i].encode) {
    	            result = false;
    	        }
    	    }
    	    if (result) {
    	        $(this).combobox("clear");
    	    }else{
    	        $(this).combobox('unselect',val);
    	        $(this).combobox('select',val);
    	    }
    	},
    	filter: function(q, row){
    	    var keys = new Array();
    	    keys[keys.length] = 'encode';
    	    keys[keys.length] = 'name';
    	    keys[keys.length] = 'pinyin';
    	    keys[keys.length] = 'wb';
    	    keys[keys.length] = 'inputCode';
    	    return filterLocalCombobox(q, row, keys);
    	}
    
});
$("#undrugName") .combogrid({
	 url: '<%=basePath%>baseinfo/financeFixedcharge/queryUnDrug.action',
	 disabled : false,
	 rownumbers : true,//显示序号 
	 pagination : true,//是否显示分页栏
	 striped : true,//数据背景颜色交替
	 panelWidth : 500,//容器宽度
	 panelHeight:320,//高度
	 mode:'remote',	//自动发送到名为'q'的HTTP请求参数到服务器检索新数据。
	 fitColumns : true,//自适应列宽
	 pageSize : 10,//每页显示的记录条数，默认为10  
	 pageList : [ 10, 20,50,80,100],//可以设置每页记录条数的列表  
	 idField : 'code',
	 textField : 'name',
	 columns : [ [
		{field : 'id',title : 'id',hidden:true},
		{field : 'name',title : '项目名字',width : '290'},
		{field : 'code',title : '编码',width : '200'}, 
	 ]],
	 onClickRow : function(rowIndex, rowData) {
		$('#undrug').val(rowData.code);
	 },onChange:function(q){
		 $('#undrug').val(null);
	 }
});


//清除所填信息
function clearEdit(){
	$('#editForm').form('clear');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
//床位登记项目名称唯一
function onlyUndrug(){
	var undrugName=$('#undrugName').combobox('getValue');
	var chargeBedlevel=$('#chargeBedlevel').combobox('getValue');
	var startDate=$('#startTime').val();
	var endTime=$('#endTime').val();
	if(startDate==''||endTime==''){
		$.messager.alert('提示信息','开始时间结束时间不能为空');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return false;
	}
	if(undrugName!=null&&undrugName!=''&&chargeBedlevel!=null&&chargeBedlevel!=''){
	$.ajax({
		url:"<%=basePath%>baseinfo/financeFixedcharge/saveOrUpdateCostBefore.action ",
		type: 'post',
		data:{'financeFixedcharge.drugUndrug':undrugName,'financeFixedcharge.chargeBedlevel':chargeBedlevel},
		dataType:'json',
		success:function(data){
			if(data.message==true){
				submit();
			}else{
				$.messager.alert('提示信息','当前床位等级项目名重复,请重试');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
	});
	}else{
		$.messager.alert('提示信息','请仔细确认后再提交');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
	}
	return false;
}
//保存
function submit(){
	if($('#isabOutChildeen').is(":checked")){//婴儿相关
		$('#isabOutChildeen').val(1);
	}else{
		$('#isabOutChildeen').val(0);
	}
	if($('#isabOutTime').is(":checked")){//时间相关
		$('#isabOutTime').val(1);
	}else{
		$('#isabOutTime').val(0);
	}
	
		var startTime=$('#startTime').val();
		var entTime =$('#endTime').val();
		if(startTime==null||startTime==""){
			$.messager.alert('提示信息','请填写开始时间');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		if(entTime==null||entTime==""){
			$.messager.alert('提示信息','请填写结束时间');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		var data1=new Date(startTime);
		var data2=new Date(entTime);
		if(data1.getTime()>data2.getTime()){
			$.messager.alert('提示信息','结束时间不能小于开始时间');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
	if($("#chargeState").val()==null||$("#chargeState").val()==''){
		$.messager.alert('提示信息','请选择床位使用状态');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return;
	}
  	$('#editForm').form('submit',{
 		url: "<%=basePath%>baseinfo/financeFixedcharge/saveOrUpdateCost.action ",
  		 onSubmit:function(){ 
		     return $(this).form('validate');  
		 },  
		success:function(){
			$.messager.alert('提示',"保存成功");
			closeLayout();
			 //实现刷新
			reloadList();
		  	
		 },
		error:function(data){
			$.messager.alert('提示',"保存失败");
		}
  	});
 }
 
//刷新
function reloadList(){
	//实现刷新栏目中的数据
	$('#list').datagrid('reload');
}
</script>
</body>
</html>