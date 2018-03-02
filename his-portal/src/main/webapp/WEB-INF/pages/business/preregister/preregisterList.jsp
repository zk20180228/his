<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
 <%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<!DOCTYPE html>
<html>
<head>
	<title>预约挂号（医）</title>
<link href='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />
<%--<script src='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/jquery/jquery-1.9.1.min.js'></script>--%>
<%-- <%@ include file="/common/metas.jsp"%> --%>
<script src='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/jquery/jquery-ui-1.10.2.custom.min.js'></script>
<script src='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/fullcalendar/fullcalendar.js'></script>
<script type="text/javascript">
$(function(){
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	$('#calendar').fullCalendar({
		header: {////设置日历头部信息
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		
			contentHeight: '600',
			firstHour:8,
			minTime:'08:00',
			maxTime:'23:00',
			allDaySlot:false,//在agenda视图模式下，是否在日历上方显示all-day(全天)
			allDayText:'全天',//定义日历上方显示全天信息的文本
			axisFormat:'HH:mm',//设置日历agenda视图下左侧的时间显示格式，默认显示如：5:30pm
			today: '今天',
			firstDay:1,//每行第一天为周一 
			timeFormat:'HH:mm',//设置显示的日程事件的时间格式，如timeFormat: 'H:mm' 则显示24小时制的像10:30
			titleFormat:{month: 'yyyy年MM月',week:'yyyy年MM月dd{-dd日}',day:'yyyy年MM月dd日 dddd'},//
			monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
			dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
			buttonText: {
				today: '今天',
				month: '月',
				week: '周',
				day: '日',
				prev: '<<',
				next: '>>'
			},
			editable: false,//可以拖动
			events: "<c:url value='/outpatient/businesspreregister/queryInfoPeret.action'/>",//事件数据
			dayClick:function(date,allDay,jsEvent,view){//dao
			},
			eventClick:function(event,jsEvent,view){
				if(event.title.indexOf("em")>0){
					var preregisterMiddayname= encodeURI(encodeURI("晚上"));
					var midday=3;
				}else if(event.title.indexOf("pm")>0){
					var preregisterMiddayname= encodeURI(encodeURI("下午"));
					var midday=2;
				}else if(event.title.indexOf("am")>0){
					var preregisterMiddayname= encodeURI(encodeURI("上午"));
					var midday=1;
				}
				var startdate=event.start;
				var yaer = event.start.getFullYear();
				var month = (event.start.getMonth()+1);
				var day = event.start.getDate();
				var oldDates = yaer+"-"+month+"-"+day;
				var hh = event.start.getHours() > 9 ? event.start.getHours() : "0" + event.start.getHours();
				var min = (event.start.getMinutes()) > 9 ? (event.start.getMinutes()) : "0"
				+ (event.start.getMinutes());
				var hdates=hh+":"+min;
				var hhl = event.end.getHours() > 9 ? event.end.getHours() : "0" + event.end.getHours();
				var minl = (event.end.getMinutes()) > 9 ? (event.end.getMinutes()) : "0"
				+ (event.end.getMinutes());
				var hdatels=hhl+":"+minl;
				var arr1 = oldDates.split("-");
				var date1 = new Date(arr1[0],parseInt(arr1[1])-1,arr1[2]);
				if(date1<=new Date()){
					$.messager.alert("操作提示", "您的选择已过期，请您重新选择");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				var content=event.id;
				var strs= new Array(); //定义一数组
				strs=content.split("|"); //字符分割 
				var conId = strs[0];
				var date = strs[1];
				if(conId>0){
					var empId = $('#empId').val();
					var deptId = $('#deptId').val();
					var gradeId = $('#gradeId').val();
					Adddilog("查看分类","<c:url value='/outpatient/businesspreregister/preregisterEdit.action'/>?menuAlias=${menuAlias}"+"&date="+date+"&empId="+empId+"&deptId="+deptId+"&gradeId="+gradeId+"&midday="+midday+"&preregisterStarttime="+hdates+"&preregisterEndtime="+hdatels+"&preregisterMiddayname="+preregisterMiddayname);
				}else{
					$.messager.alert('提示',"号源已满");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
				
			}
	});
});

function Adddilog(title, url) {
	$('#dept').dialog({    
	    title: title,    
	    width: '40%',    
	    height:'95%',    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true,
	    shadow: false
	});    
}
  
  
</script>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="calendar" style="width: 100%;height: 100%;overflow:auto;"></div>
	<input type="hidden" id="empId" value="${request.empId }">
	<input type="hidden" id="deptId" value="${deptId }">
	<input type="hidden" id="gradeId" value="${gradeId }">
	<div id="dept"></div>
</body>
</html>
