<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊停诊统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
$(function(){
		var firstData="${firstData}"
		//结束时间加一天
		var endTime="${dataTime}";
		var date = new Date(endTime);
		date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
		var endData= date.Format("yyyy-MM-dd");
		$("#list").datagrid({
		queryParams:{firstData:firstData,endData:endData},
		border:false,
		fit:true,
		fitColumns:true,
		singleSelect:true,
		rownumbers:true,
		checkOnSelect:true,
		selectOnCheck:false,
		url:"<%=basePath%>statistics/ReservationStatistics/queryOutpatientStop.action"
	});
});
	
	//查询
	function searchFrom(){
		var firstData=$("#startData").val();
		var endTime=$("#endData").val();
		if(firstData==null || firstData=="" || endTime==null || endTime==""){
			$.messager.alert("提示","请填写正确的时间范围！");
			return ;
  		}
		var date = new Date(endTime);
		date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
		var end = date.Format("yyyy-MM-dd");
		searchFinal(firstData,end);
		$("#endData").val(endTime);
	}
	function searchFinal(Stime,Etime){
		if(Stime&&Etime){
	          if(Stime>Etime){
	            $.messager.alert("提示","开始时间不能大于结束时间！");
	            return ;
	          }
	        }
		$('#startData').val(Stime);
		$('#endData').val(Etime);
		$("#list").datagrid('load',{
			firstData:Stime,
			endData:Etime
		});
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
		var Etime = GetDateStr(1);
		searchFinal(Stime,Etime);
		$("#endData").val(GetDateStr(0)); 
	 }
	 //查询前三天
	function searchThree(){
		var Stime = GetDateStr(-3);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$("#endData").val(GetDateStr(-1)); 
	}
	//查询前七天
	function searchSeven(){
		var Stime = GetDateStr(-7);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$("#endData").val(GetDateStr(-1)); 
	}
	//查询前15天 zhangkui 2017-04-17
	function searchFifteen(){
		var Stime = GetDateStr(-15);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$("#endData").val(GetDateStr(-1)); 
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
		  var Etime= nowYear+"-"+nowMonth+"-01";
			  
		  searchFinal(Stime,Etime);
		  $('#endData').val(lastDay);
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
	//查询当月
	function searchMonth(){
		var Stime = getCurrentMonthFirst();
		//var Etime = getCurrentMonthLast();
		//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Etime = GetDateStr(1);	
		searchFinal(Stime,Etime);
		$("#endData").val(GetDateStr(0)); 
	}
	//查询当年
	function searchYear(){
		//var Etime = new Date().getFullYear()+"-12-31";
		//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Etime = GetDateStr(1);	
		var Stime = new Date().getFullYear()+"-01-01";
		searchFinal(Stime,Etime);
		$("#endData").val(GetDateStr(0)); 
	}
	
	
	
	
	
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$("#startData").val('${firstData }');
		$("#endData").val('${dataTime }');
		searchFrom();
	}
	//导出
	function exportDown() {
		
		var firstData=$("#startData").val();
		var endData=$("#endData").val();
		if(firstData!=null&&firstData!=''&&endData!=null&&endData!=''){
		
		}else{
	          $.messager.alert("提示","日期不能为空！");
	          return;
		}
		var rows = $("#list").datagrid("getRows");
		if (rows==null||rows.length==1) {
			$.messager.alert("提示","没有数据，不能导出！");
			return;
		}else{
			$.messager.confirm('提示','确定要导出吗？',function(res){
				if(res){
					$("#exportForm").form('submit',{
						url:"<%=basePath%>statistics/ReservationStatistics/expOutpatientStop.action",
						onSubmit:function(param){
							param.firstData=firstData,
							param.endData=endData
						},
						success : function(data) {
							$.messager.alert("提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("提示", "导出失败！", "error");
						}
					})
				}
			});
		}
	}
	//打印方法
	function experStamp() {
		var firstTime=$("#startData").val();
 		var endTime=$("#endData").val();
		if(firstTime!=null&&firstTime!=''&&endTime!=null&&endTime!=''){
		}else{
	          $.messager.alert("提示","日期不能为空！");
	          return;
		}
		var rows = $("#list").datagrid("getRows");
		if (rows==null||rows.length==1) {
			$.messager.alert("提示","没有数据，不能打印！"); 	
			return;
		}else{
	 		$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if(res){
				    //hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
				    //给表单的隐藏字段赋值
				    $("#reportToLogin").val(firstTime);
				    $("#reportToEnd").val(endTime);
				    $("#reportToRows").val(JSON.stringify(rows));
				    var fileName="iReportOutpatientStop";
				
				    //表单提交 target
				    var formTarget="hiddenFormWin";
			        var tmpPath = "<%=basePath%>statistics/ReservationStatistics/iReportOutpatientStop.action?fileName="+fileName;
			        //设置表单target
			        $("#reportToHiddenForm").attr("target",formTarget);
			        //设置表单访问路径
					$("#reportToHiddenForm").attr("action",tmpPath); 
			        //表单提交时打开一个空的窗口
				    $("#reportToHiddenForm").submit(function(e){
				    	 var timerStr = Math.random();
						 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
					});  
				    //表单提交
				    $("#reportToHiddenForm").submit();
		     }
	 		});
		} 
	}
	//退出
	function closeDown(){
		self.parent.$('#tabs').tabs('close',"门诊停诊统计");
	}
</script>
</head>
<body style="margin: 0px;padding: 0px">
<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'north',border:false" style="height:100px;padding:5px 5px 0px 5px;">
						<table id="searchTab" style="width: 100%;">
							<tr>
								<!-- 开始时间 --> 
								<td style="width:40px;" align="left">日期:</td>
								<td style="width:110px;" >
									<input id="startData" class="Wdate" type="text" value="${firstData}"  data-options="showSeconds:false" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<!-- 结束时间 --> 
								<td style="width:40px;" align="center">至</td>
								<td style="width:110px;">
									<input id="endData" class="Wdate" type="text" name="Stime" value="${dataTime}" data-options="showSeconds:false" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td>
												<span style="margin-left:5"><a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a></span>
												<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
												<a href="javascript:void(0)" onclick="experStamp()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
												<a href="javascript:void(0)" onclick="exportDown()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
									<!-- 		<a href="javascript:void(0)" onclick="closeDown()" class="easyui-linkbutton" iconCls="icon-cancel">退出</a> -->
								</td>
								<td style='text-align: right'>
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
				<h5 id="hh" style="font-size: 32;font: bold;text-align: center;padding-top: 10px">门诊停诊统计表</h5>
	</div>
	<div data-options="region:'center',border:false" style="height:90%">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false" >
				<table id="list" class="easyui-datagrid">
					<thead>
						<tr>
							<th data-options="field:'deptName',rowspan:2,width:'20%',halign:'center',align:'right'">出诊科室</th>   
							<th data-options="field:'name',colspan:4,align:'center'">停诊原因</th>
							<th data-options="field:'sum',rowspan:2,width:'18%',halign:'center',align:'right'">停诊人次合计</th> 
						</tr>
						<tr>
							<th data-options="field:'sumSick',width:'15%',halign:'center',align:'right'">生病</th>
							<th data-options="field:'sumEvection',width:'15%',halign:'center',align:'right'">出差</th>
							<th data-options="field:'sumMeet',width:'15%',halign:'center',align:'right'">开会</th>
							<th data-options="field:'sumOther',width:'15%',halign:'center',align:'right'">其他</th>
						</tr>
					</thead>
				</table>
						<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="firstData" id="reportToLogin" value=""/>
						<input type="hidden" name="endData" id="reportToEnd" value=""/>
						<input type="hidden" name="rows" id="reportToRows" value=""/>
						<input type="hidden" name="fileName" id="reportToFileName" value=""/>
						</form>
				<form id="exportForm"/>
			</div>
			<div data-options="region:'south',border:false" style="height: 22px">
				备注：此表显示全院门诊各科室的停诊原因，按停诊总次数降序排列，停诊人次为排版后却取消的坐诊，
				涉及的停诊原因包括生病、开会、出差和其他。
			</div>
		</div>
	</div>
</div>
</body>
</html>