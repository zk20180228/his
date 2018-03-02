<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>坐诊医生工作量统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
$(function(){
			$.ajax({
				url: "<%=basePath%>baseinfo/department/getDeptMap.action",
				success: function(data) {
// 						$("#dept").val(data[nowDept]);
				}
			});
			$.ajax({
				url: "<%=basePath%>baseinfo/employee/getEmplMap.action",
				success: function(data) {
						//选择科室
						$(".deptInput").MenuList({
							width :530, //设置宽度，不写默认为530，不要加单位
							height :400, //设置高度，不写默认为400，不要加单位
							dropmenu:"#m2",//弹出层id，必须要写
							isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
							para:"C",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
							firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
							relativeInput:".doctorInput",	//与其级联的文本框，必须要写
							relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
						});
						//选择医生
						$(".doctorInput").MenuList({
							width :530,
							height :400,
							dropmenu:"#m3",//弹出层
							isSecond:true,	//是否是二级联动的第二级
							para:"C",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
							firsturl: "<%=basePath %>statistics/RegisterInfoGzltj/getDoctorList.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
							relativeInput:".deptInput"	//与其级联的文本框
						});
					query();
				}
			});
	
// 	$('#dept').combogrid({   
<%-- 		url : '<%=basePath%>register/newInfo/deptCombobox.action', --%>
// 		striped:true,
// 		checkOnSelect:false,
// 		selectOnCheck:true,
// 		singleSelect:false,
// 		multiple:true,
// 		fitColumns : false,//自适应列宽
// // 		panelWidth : 130,//容器宽度
// 		panelHeight:320,//高度
// 		idField : 'deptCode',
// 		textField : 'deptName',
// 		mode:'local',
// 		filter: function(q, row){
// 			q = q.substring(q.lastIndexOf(","),q.length);
// 			var keys = new Array();
// 			keys[keys.length] = 'deptCode';
// 			keys[keys.length] = 'deptName';
// 			keys[keys.length] = 'deptPinyin';
// 			keys[keys.length] = 'deptWb';
// 			return filterLocalCombobox(q, row, keys);
// 		},
// 		columns : [ [{
// 			field : 'deptName',
// 			title : '坐诊科室',
// 			width : 145,
// 		}
// 		] ],
// 		onSelect:function(rec){
// 			var deptCodes = '';
// 			var depts = $('#dept').combobox('getValues');
// 			for (var i = 0; i < depts.length; i++) {
// 				if(deptCodes!=''){
// 					deptCodes+="','"
// 				}
// 				deptCodes = deptCodes + depts[i];
// 			}
// 			deptCodes.substring(0,deptCodes.lastIndexOf(","));
<%-- 			var url='<%=basePath%>statistics/empWorkStats/empCombobox.action?emp='+deptCodes; --%>
// 			$('#emp').combogrid('grid').datagrid('reload', {"emp":deptCodes});
// 			$('#emp').combogrid('setValue','');
// 		}
// 	 });
	

	
// 	$('#beginTime').combo({
// 		onHidePanel: function(date){
// 			var beginTime = $('#beginTime').datebox('getValue');
// 			var endTime = $('#endTime').datebox('getValue');
// 			var dept = $('#dept').combobox('getValue');
// 			var emp = $('#emp').combobox('getValue');
// 			$('#beginTimeText').text(beginTime);
// 			queryList(beginTime,endTime,dept,emp);
// 	    }
// 	});

// 	$('#endTime').combo({
// 		onHidePanel: function(date){
// 			var beginTime = $('#beginTime').datebox('getValue');
// 			var endTime = $('#endTime').datebox('getValue');
// 			var dept = $('#dept').combobox('getValue');
// 			var emp = $('#emp').combobox('getValue');
// 			$('#endTimeText').text(endTime);
// 			queryList(beginTime,endTime,dept,emp);
// 	    }
// 	});
	
// 	searchOne();

	/*******************************************************************************************************************/
});
function query(){
	var beginTime = $('#beginTime').val();
	//结束时间要加一天
	var endTime = $('#endTime').val();
	if(beginTime==null || beginTime=="" || endTime==null || endTime==""){
		$.messager.alert("提示","请填写正确的时间范围！");
		return ;
		}
	var date = new Date(endTime);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var end = date.Format("yyyy-MM-dd");
	if(beginTime&&endTime){
        if(beginTime>endTime){
          $.messager.alert("提示","开始时间不能大于结束时间！");
          return ;
        }
      }
	var deptCode_s = $('#dept').getMenuIds();
// 	$('h2','.xmenu').attr('class').indexOf('selected')!=-1||
	if(deptCode_s==null||deptCode_s==""){
		 deptCode_s = "all"; 
	 }
	var expxrts = $('#emp').getMenuIds();
	
	var dept = new Array();
	dept = deptCode_s.split(",");
	var dept2=$('#dept').val();

	var emp  = new Array();
	emp = expxrts.split(",");
	var emp2 = $('#emp').val();
	
 	if($.trim(dept2)==""){
			dept="";
	 }
 	
	if($.trim(emp2)==""){
		emp="";
	}
	
// 	var dept = $('#dept').combobox('getValues');
// 	var dept2 = $('#dept').combobox('getText');
// 	var emp = $('#emp').combobox('getValues');
// 	var emp2 = $('#emp').combobox('getText');
// 	var row = $('#emp').combogrid('grid').datagrid('getSelected');
	var deptcodes = '';
	var empcodes = '';
	for(var i=0;i<dept.length;i++){
		if(deptcodes!=''){
			deptcodes+=",";
		}
		deptcodes =deptcodes+dept[i];
	}
// 		deptcodes.substring(0,deptcodes.lastIndexOf(","));
	for(var i=0;i<emp.length;i++){
		if(empcodes!=''){
			empcodes+=",";
		}
		empcodes = empcodes+emp[i];
	}
		empcodes.substring(0,empcodes.lastIndexOf(","));
	if(beginTime==null || beginTime==""){
		beginTime="${beginTime}";
  	}
  	if(endTime==null || endTime==""){
  		end="${endTime}";
  	}
// 	queryList(beginTime,end,deptcodes,empcodes);
	queryList(beginTime,end,deptCode_s,empcodes);
	$('#beginTimeText').val(beginTime);
	$('#endTimeText').val($('#endTime').val());
// 	$('#deptText').val(dept2);
// 	$('#empText').val(emp2);
	
	
}
function searchFinal(Stime,Etime){
	$('#beginTime').val(Stime);
	$('#endTime').val(Etime);
	query();
 }
//距离当前多少天的日期
 function GetDateStr(AddDayCount) {
	 var dd = new Date();
	 dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	 var y = dd.getFullYear();
	 var m = dd.getMonth()+1;//获取当前月份的日期
	 if(Number(m)<10){
         m = "0"+m;
       }
       var d = dd.getDate();
       if(Number(d)<10){
         d = "0"+d;
       }
	 return y+"-"+m+"-"+d;
}
 //查询当天
function searchOne(){
	var Stime = GetDateStr(0);
	var Etime = GetDateStr(0);
	searchFinal(Stime,Etime);
	$("#endTime").val(Stime);
	$('#endTimeText').val($('#endTime').val());
	
 }
 //查询前三天
function searchThree(){
	var Stime = GetDateStr(-3);
	var Etime = GetDateStr(-1);
	searchFinal(Stime,Etime);
	$("#endTime").val(GetDateStr(-1)); 
	$('#endTimeText').val($('#endTime').val());
}
//查询前七天
function searchSeven(){
	var Stime = GetDateStr(-7);
	var Etime = GetDateStr(-1);
	searchFinal(Stime,Etime);
	$("#endTime").val(GetDateStr(-1)); 
	$('#endTimeText').val($('#endTime').val());
}
//查询前15天 zhangkui 2017-04-17
function searchFifteen(){
	var Stime = GetDateStr(-15);
	var Etime = GetDateStr(-1);
	searchFinal(Stime,Etime);
	$("#endTime").val(GetDateStr(-1)); 
	$('#endTimeText').val($('#endTime').val());
}


//日期格式转换
Date.prototype.Format = function(fmt)   
{   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
} 
//获取每月第一天
function getCurrentMonthFirst(){
	 var date=new Date();
	 date.setDate(1);
	 return date.Format("yyyy-MM-dd");
}
//获取每月最后一天
function getCurrentMonthLast(){
	 var date=new Date();
	 var currentMonth=date.getMonth();
	 var nextMonth=++currentMonth;
	 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
	 var oneDay=1000*60*60*24;
	 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
} 
//上月
function beforeMonth(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth();
    var nowMonth = month;
    var nowYear = year;
    if(month==0)
    {
        month=12;
        nowMonth = "01";
        year=year-1;
    }
    if (month < 10) {
        nowMonth = "0" +(month+1);
        month = "0" + month;
    }
    var Stime = year + "-" + month + "-" + "01";//上个月的第一天
    var lastDate = new Date(year, month, 0);
    var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天

    searchFinal(Stime,lastDay);
    $("#endTime").val(lastDay);
    $('#endTimeText').val(lastDay);
}
//查询当月
function searchMonth(){
	var Stime = getCurrentMonthFirst();
	//var Etime = getCurrentMonthLast();
	// 	var date=new Date();
	// 	var Etime = date.Format("yyyy-MM-dd");
	var Etime = GetDateStr(0);	
	searchFinal(Stime,Etime);
	//时间回显是现在
	$("#endTime").val(GetDateStr(0)); 
	$('#endTimeText').val($('#endTime').val());
	
}
//查询当年
function searchYear(){
	//var Etime = new Date().getFullYear()+"-12-31";
// 	var date=new Date();
// 	var Etime = date.Format("yyyy-MM-dd");
	var Etime = GetDateStr(0);	
	var Stime = new Date().getFullYear()+"-01-01";
	searchFinal(Stime,Etime);
	//时间回显是现在
	$("#endTime").val(GetDateStr(0));  
	$('#endTimeText').val($('#endTime').val());
	
}


/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
		$('#dept').val("");
		$('#emp').val("");
		$("a[name='menu-confirm-cancel']").click();
		$("a[name='menu-confirm-clear']").click();
	searchMonth();
}

function queryList(beginTime,endTime,dept,emp){
	$('#list').datagrid({
		 fit:true,
		 fitColumns:true,
//          rownumbers:true,
		 pageSize:20,
		 pageList:[10,20,50,100],
      	 pagination:true,
      	 rownumbers:true,
         border:true,
         checkOnSelect:true,
         selectOnCheck:false,
         singleSelect:true,
		 url: "<%=basePath%>statistics/empWorkStats/queryList.action",
		 queryParams:{"beginTime":beginTime,"endTime":endTime,"dept":dept,"emp":emp,menuAlias:menuAlias}
	});
}

</script>
</head>
<style>
body{z-index: 0}
.datagrid-header-rownumber,.datagrid-cell-rownumber{
   width:50px;
</style>
</style>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; z-index: 0;overflowy:auto;"> 
  <form id="searchForm"   style="padding-top:5px;padding-left:5px;">
    	<table id="searchTab" style="height:38px; width: 100%;z-index: 0;padding:4px 5px 6px 5px;">
			<tr>
				<td style="width:40px;" align="left">日期:</td>
				<td style="width:105px;">
					<input id="beginTime" class="Wdate" type="text" value="${beginTime}" onClick="WdatePicker()" style="height:22px; width:100px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
<%-- 				<span style="margin-right:4"><input id="beginTime" class="Wdate" type="text" value="${beginTime}" onClick="WdatePicker()" style="height:22px; width:105px;border: 1px solid #95b8e7;border-radius: 5px;"/></span> --%>
				</td>
				
				<td style="width:30px;" align="center">至</td>
				<td style="width:105px" id="td1">
					<input id="endTime" class="Wdate" type="text" value="${endTime}" onClick="WdatePicker()" style="height:22px;width:100px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
						<td style="width:85px;" align="right"><span style="margin-right:5">坐诊科室:</span></td>
				   			<td style="width:120px;z-index:1;position: relative;left: 0px;right:0px;" class="newMenu" >
					    	       	<div class="deptInput menuInput" style="width:120px; "><input class="ksnew" id="dept" readonly="readonly" style="width:95px;" /><span ></span></div> 
<!-- 					    	       	<input id="dept" class="easyui-combogrid" data-options="prompt:'输入查询'" style="width:130px;"/> -->
					    	       		<div id="m2" class="xmenu" style="display: none;">
					    	       		<div class="searchDept" >
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">取消</span>
											</a>						
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">确定</span>
											</a>
					    	       		</div>
										<div class="select-info" style="display:none; ">	
											<label class="top-label">已选科室：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl"  >
										<div class="addlist"></div>
										<div class="tip" style="display:none;">没有检索到数据</div>		 
										</div>	
									</div>
								</td>
<!-- 					<input id="dept" class="easyui-combobox" data-options="multiple:true"> -->
<!-- 					<input id="dept" class="easyui-combogrid" data-options="prompt:'输入查询'" style="width:130px;"/> -->


					<td style="width:75px;" align="right"><span style="margin-left:3">坐诊医生:</span></td>
<!-- 					<span style="margin-left:10">坐诊医生:</span> -->
<!-- 					<input id="emp" class="easyui-combobox" data-options="valueField:'jobNo',textField:'name',multiple:true"> -->
						<td style="width:120px;z-index:1;position: relative;" class="newMenu">
					    	       	<!-- <input id="zj" name="expxrt" class="easyui-combobox" data-options="valueField:'jobNo',textField:'name',multiple:true"/>  -->
					    			<div class="doctorInput menuInput" style="width:120px;margin-left:5;margin-right:3;"><input class="ksnew" id="emp" readonly="readonly"  style="width:95px;" ></input> <span></span></div> 
<!-- 					<input class="easyui-combogrid" id="emp" data-options="prompt:'输入查询'" style="width:100px;"/> -->
					    	       	<div id="m3" class="xmenu" style="display: none;">
					    	       		<div class="searchDept" >
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu" ><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" >
												<span class="a-btn-text">取消</span>
											</a>
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">确定</span>
											</a>											
					    	       		</div>
										<div class="select-info" style="display:none">	
											<label class="top-label">已选医生：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl" >
											<div class="addlist"></div>
											<div class="tip" style="display:none">没有检索到数据</div>			 
										</div>
									</div>
					    		</td>
					
						<td>
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="query()" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					
						<td style='text-align:right;margin-right:5px'>
								<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
								<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
								<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
								<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
									<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
								<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
								<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
						</td>
			</tr>
		</table>
	</form>  
       
    <div data-options="region:'center',border:false" style="width:100%;height:94.5%">
    	<div class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north'" style="height:80px;width: 100%;">
		    	<table style="width:100%;z-index: 0">
		    		<tr>
		    			<td align="center"><font size="6" class="empWorkTit">坐诊医生工作量统计</font></td>
		    		</tr>
		    	</table>
		    	<table style="padding: 5px 5px 5px 5px;">
		    		<tr>
		    			<td>统计日期：</td>
		    			<td><input id="beginTimeText" readonly="readonly" style="border:0px;" size="10">至 <input id="endTimeText" readonly="readonly" style="border:0px;" size="10"></td>
<!-- 		    			<td style="width:250px">&nbsp&nbsp统计科室：<input id="deptText" readonly="readonly" style="border:0px; text-overflow:ellipsis; white-space:nowrap" size="10" ></td> -->
<!-- 		    			<td>&nbsp&nbsp统计医生：</td><td><input id="empText" readonly="readonly" style="border:0px; text-overflow:ellipsis; white-space:nowrap" size="10"></td> -->
		    		</tr>
		    	</table>
		    </div> 
		      
		    <div data-options="region:'center',border:false" style="width:100%;">
		    	<table id="list" class="easyui-datagrid" style="width:100%;z-index: 0" data-options="fit:true">    
				    <thead>   
				        <tr>
				            <th data-options="field:'deptCode',width:'15%'">科室编码</th>
				            <th data-options="field:'deptName',width:'10%'">科室名称</th>  
				            <th data-options="field:'empCode',width:'15%'">坐诊医生编码</th>   
				            <th data-options="field:'empName',width:'10%'">坐诊医生名称</th>   
				            <th data-options="field:'infoSum',width:'10%'">挂号人数</th> 
				            <th data-options="field:'seeSum',width:'10%'">看诊人数</th>   
				        </tr>   
				    </thead>   
				</table>
		    </div>
		</div>  
    </div>
   
</div>  
</body>
</html>