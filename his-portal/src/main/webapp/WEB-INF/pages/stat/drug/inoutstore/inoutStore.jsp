<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>住院药房入出库台账查询</title>
</head>
<body style="margin: 0px; padding: 0px;">
<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
	<div data-options="region:'center',fit:true,border:false" style="width: 100%; height: 93%;">
		<div class="easyui-layout" style="width: 100%; height: 100%;" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%; height: 65px;padding: 5px 5px 5px 5px;">
				<form id="searchForm">
					<table border="0" style="width: 100%" cellspacing="0" cellpadding="0">
					     <tr>
					        <td style="width:8% ">
					       	 	<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias }:function:set">
								<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
						   		</shiro:hasPermission>
						    </td>
							<td style='text-align: right; padding-right: 5px'>
								<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>&nbsp;
								<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>&nbsp;
								<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>&nbsp;
								<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>&nbsp;
								<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>&nbsp;
								<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>&nbsp;
								<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>&nbsp;
							</td>
					    </tr>
					    <tr style="height:5px"></tr>
						<tr>
							<td>
								日期:
								<input id="sTime"  name="t1.beginTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td align="left" style="padding-left: 5px;">
								至
								<input id="eTime" name="t1.endTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								&nbsp;&nbsp;药品名称:<input id="drugName" class="easyui-combobox" name="t1.code"/>
								&nbsp;&nbsp;入出库类型:<input id="inoutType" class="easyui-combobox" name="t1.type"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%; height: 95%;">
				<table id="grid" data-options=" pagination:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true"></table>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var deptMap="";
var empMap="";
function clear(){
	$('#sTime').val("${sTime}");
	$('#eTime').val("${eTime}");
	$('#drugName').combobox('setValue',"");
	$('#inoutType').combobox('setValue',"0");
	queryData();
}
$(function(){
	$.ajax({
		url:'<%=basePath%>sys/decorate/queryEmpCodeAndNameMap.action',
		success: function(comData) {
			empMap = comData;
		}
	});
	 
	$.ajax({
		url:'<%=basePath%>baseinfo/department/getDeptMap.action', 
		success: function(comData) {
			deptMap = comData;
		}
	});
	
	//渲染员工
	function functionEmpCom(value,row,index){
			if(value!=null&&value!=''){
				if(empMap[value]!=null&&empMap[value]!=""){
				return empMap[value];
				}
				return value;
			}
		}	
	//渲染部门 
	function functionDeptMap(value,row,index){
			if(value!=null&&value!=''){
				return deptMap[value];
			}
		}	
	$('#sTime').val("${sTime}");
	$('#eTime').val("${eTime}");
	$('#drugName').combobox({
		url : "<c:url value='/statistics/InOutStore/getDrugInfo.action'/>",
		valueField:'drugCode',
		textField:'tradeName',
		onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].drugCode) {
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
		    keys[keys.length] = 'drugCode';
		    keys[keys.length] = 'tradeName';
		    keys[keys.length] = 'pinyin';
		    keys[keys.length] = 'wb';
		    keys[keys.length] = 'inputCode';
		    return filterLocalCombobox(q, row, keys);
		}
	});
	$('#inoutType').combobox({        
		valueField:'type',
		textField:'text',
		data: [{
			type: 0,
			text: '出库'
		},{
			type: 1,
			text: '入库'
		}]
	});
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	$('#grid').datagrid({
		url:'<%=basePath%>statistics/InOutStore/inOutStoreData.action'+listParam,
		columns:[[
		          {field:'drugCode',title:'药品编码',width:'15%',align:'center'},
		          {field:'tradeName',title:'药品名称',width:'10%',align:'center'},
		          {field:'specs',title:'药品规格',width:'10%',align:'center'},
		          {field:'inoutNum',title:'入出库数量',width:'10%',align:'right',halign:'center'},
		          {field:'packQty',title:'包装数量',width:'10%',align:'right',halign:'center'},
		          {field:'drugDeptName',title:'科室',width:'10%',align:'center'},
		          {field:'drugDeptCode',title:'科室编码',width:'15%',align:'center'},
		          {field:'inoutDate',title:'入出库日期',width:'10%',align:'center',formatter:functionDate},
		          {field:'userName',title:'核准人',width:'9%',align:'center',formatter:functionEmpCom}
		        ]],
				singleSelect:true,
				onLoadSuccess : function(data){
					var pager = $(this).datagrid('getPager');
					var aArr = $(pager).find('a');
					var iArr = $(pager).find('input');
					$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
					for(var i=0;i<aArr.length;i++){
						$(aArr[i]).tooltip({
							content:toolArr[i],
							hideDelay:1
						});
						$(aArr[i]).tooltip('hide');
					}
				}
	});
	
});
function queryData(){
	var startTime=$('#sTime').val();
	var endTime=$('#eTime').val();
	if(startTime&&endTime){
	    if(startTime>endTime){
	      $.messager.alert("提示","开始时间不能大于结束时间！");
	      close_alert();
	      return ;
	    }
	  }
	var formData=getFormData('searchForm');
	$('#grid').datagrid('load',formData);
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
//按时间段查询
function queryMidday(val){
	if(val==1){
		var myDate = new Date();
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day=year+"-"+month+"-"+date;
		var Stime = $('#sTime').val(day);
	    var Etime = $('#eTime').val(day);
	    queryData();
	}else if(val==2){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day2=year+"-"+month+"-"+date;
		var Stime = $('#sTime').val(day2);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day2=year+"-"+month+"-"+date;
	    var Etime = $('#eTime').val(day2);
	    queryData();
	}else if(val==3){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day3=year+"-"+month+"-"+date;
		var Stime = $('#sTime').val(day3);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day3=year+"-"+month+"-"+date;
	    var Etime = $('#eTime').val(day3);
	    queryData();
	}else if(val==4){
		var myDate = new Date();
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		month=month<10?"0"+month:month;
		var day=year+"-"+month+"-"+"01";
		var Stime = $('#sTime').val(day);
		  myDate = new Date();
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
		 month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day=year+"-"+month+"-"+date;
	    var Etime = $('#eTime').val(day);
	    queryData();
	}else if(val==5){
		var myDate = new Date();
		var year=myDate.getFullYear();
		var day=year+"-"+"01"+"-"+"01";
		var Stime = $('#sTime').val(day);
	     myDate = new Date();
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
		 month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day=year+"-"+month+"-"+date;
	     var Etime = $('#eTime').val(day);
	    queryData();
	}else if(val==6){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day3=year+"-"+month+"-"+date;
		var Stime = $('#sTime').val(day3);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day3=year+"-"+month+"-"+date;
	    var Etime = $('#eTime').val(day3);
	    queryData();
	}else if(val==7){
		var date=new Date();
		var year=date.getFullYear();
		var month=date.getMonth();
		if(month==0){
			year=year-1;
			month=12;
		}
		var startTime=year+'-'+month+'-01';
		 $('#sTime').val(startTime);
		var date=new Date(year,month,0);
		var endTime=year+'-'+month+'-'+date.getDate();
		$('#eTime').val(endTime);
		queryData();
	}
}
//格式化时间
function functionDate(value,row,index){
	if(null!=value&&''!=value){
		return value.split(' ')[0];
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</html>