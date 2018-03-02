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
	<title>日程安排</title>
<link href='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />
<script src='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/jquery/jquery-ui-1.10.2.custom.min.js'></script>
<script src='${pageContext.request.contextPath}/javascript/js/fullcalendar-1.6.0/fullcalendar/fullcalendar.js'></script>
<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
</style>
<script type="text/javascript">
$(function(){
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	$('#calendar').fullCalendar({
		header: {////设置日历头部信息
			left: 'prevYear,prev,next,nextYear today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
			contentHeight: '650',
			firstHour:8,
			minTime:'00:00',
			maxTime:'24:00',
			slotMinutes:30,
			allDaySlot:true,//在agenda视图模式下，是否在日历上方显示all-day(全天)
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
				prev: '<',
				next: '>',
				prevYear:'<<',
				nextYear:'>>'
			},
			editable:true,//可以拖动
			events: "<c:url value='/oa/agenda/agendaAction/qeryScheduleList.action'/>?menuAlias=${menuAlias}'/>",//事件数据
			dayClick:function(date,allDay,jsEvent,view){//dao
				var currDate=new Date();
				var year=currDate.getFullYear(); 
				var month=currDate.getMonth()+1;
				month=month>9?month:"0"+month;
				var day=currDate.getDate();
				day=day>9?day:"0"+day;
				var time=year+'/'+month+'/'+day+' 00:00:00';
				var today = new Date(time).getTime();
				
				
				var year1=date.getFullYear(); 
				var month1=date.getMonth()+1;
				month1=month1>9?month1:"0"+month1;
				var day1=date.getDate();
				day1=day1>9?day1:"0"+day1;
				var time1=year1+'-'+month1+'-'+day1;
			    if(date>=today){
				  Adddilog("新建日程","<c:url value='/oa/agenda/agendaAction/toEdit.action'/>?menuAlias=${menuAlias}&t="+time1,'530','465');
			    }else{
			    	$.messager.alert('提示',"过期的时间不能进行日程安排！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
			    }
			},
			eventClick:function(event,jsEvent,view){
				var id=event.id;
				Adddilog("导向","<c:url value='/oa/agenda/agendaAction/toLead.action'/>?t="+id,"400","170");
			},
			dayRender: function (date, cell) {
			    var today = new Date();
			    var ty=today.getFullYear();
			    var tm=today.getMonth();
			    var td=today.getDate();
			    
			    var dy=date.getFullYear();
			    var dm=date.getMonth();
			    var dd=date.getDate();
			    
                 if(dy==ty&&tm==dm&&td==dd){
			    	cell.css("background-color", "");
                 }else{
                	 if(date<today){
                      cell.css("background-color", "#DDDDDD");
                      }else{
                	  cell.css("background-color", "");
                      }
			      }
			},
			eventDrop:function( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ) { 
			  var start=event.start;
			  var end=event.end;
			  start=changeDate(start);
			  end=changeDate(end);
			  var id=event.id;
			  $.ajax({
		             type: "POST",
		             url: "<c:url value='/oa/agenda/agendaAction/liveUpdate.action'/>",
		             data: {"start":start,"end":end,"t":id}
			  });
			},
			eventResize:function(  event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view  ) { 
				var end=event.end;
				end=changeDate(end);
				var id=event.id;
				$.ajax({
		             type: "POST",
		             url: "<c:url value='/oa/agenda/agendaAction/sizeUpdate.action'/>",
		             data: {"end":end,"t":id}
				 });
			}
	});
	           $('#calendar').fullCalendar('changeView',"agendaWeek");
});
//加载模式窗口
function Adddilog(title, url,width,height) {
	$('#edit').dialog({    
	    title: title,    
	    width: width,    
	    height:height,    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true,
	    shadow: false
	});    
}
//关闭模式窗口
function closeLayout(){
	$('#edit').window('close'); 
}

//转换日期格式
function changeDate(date){
	var y=date.getFullYear();
	var M=date.getMonth()+1;
	M=M>9?M:"0"+M;
	var d=date.getDate();
	d=d>9?d:"0"+d;
	var h=date.getHours();
	h=h>9?h:"0"+h;
	var m=date.getMinutes();
	m=m>9?m:"0"+m;
	var newdate =y+"-"+M+'-'+d+" "+h+':'+m+':00';
	return newdate;
}
function changeView(){
	window.location.href="<c:url value='/oa/agenda/agendaAction/sheetListToView.action'/>";
}
</script>
</head>
<body style="margin: 0px;padding: 0px;">
    <div style='width:100%;height:4%;padding-top:5px;'>
    <a href="javascript:changeView();" class="easyui-linkbutton" data-options="iconCls:'icon-06'">列表视图</a>
    </div>
	<div id="calendar" style="width: 100%;height:95%;overflow:auto;padding-left:0%;padding-top:0px"></div>
	<div id="edit"></div>
</body>
</html>
