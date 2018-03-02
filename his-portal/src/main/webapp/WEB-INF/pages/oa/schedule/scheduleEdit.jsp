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
</head>
<body>
	
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form'" style="padding:10px">
		<form id="editForm" method="post">
			<input type="hidden" id="id" name="schedule.id"  value="${schedule.id}">
			<input type="hidden" id="dayFlg" name="schedule.dayFlg"  >
			<input type="hidden" id="start" name="schedule.start" >
			<input type="hidden" id="end" name="schedule.end" >
			<input type="hidden" id="timeMinus" name="schedule.timeMinus"  >
			<input type="hidden" id="timeUnit" name="schedule.timeUnit" >
			<input type="hidden" id="time" name="schedule.time" >
			<input type="hidden" id="isZDY" name="schedule.isZDY" >
			<input type="hidden" id="hhmm" name="schedule.hhmm" >
			<input type="hidden" id="scheduleFlg" name="schedule.scheduleFlg" >
			<div style="padding:5px 5px 5px 5px;">
			<div>
				<table id="yyfs" class="honry-table" cellpadding="1" cellspacing="1"
					 style="width: 100%;height:82%; padding: 5px;">
					<tr>
						<td class="honry-lable">
							标题 :
						</td>
						<td >
							<input id="title" name ="schedule.title" class="easyui-textbox" style="multiline:true;height:20px" value="${schedule.title }" data-options="required:true,placeholder:'请输入标题'"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							全天 :
						</td>
						<td >
							 <input type="checkbox" id="df" /> 
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							开始时间 :
						</td>
						<td>
						<div style="width:105px;float:left">
						  <input id="startTime" class="easyui-textbox" name="startTime"  value="${stime}"  style="width:105px" readonly/>
						</div>
						<div id="s"  style="width:60px;padding:0px 0px 0px 110px">
						  <input id="ss" class="easyui-timespinner"  style="width:60px" value="${stimef}"  data-options="min:'00:00',showSeconds:false" />  
						</div>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							结束时间 :
						</td>
						<td>
						<div style="width:105px;float:left">
							<input id="endTime" class="easyui-textbox"  name="schedule.endTime" value="${stime}"  style="width:105px" readonly>
						</div>
						   <div id="e"  style="width:60px;padding:0px 0px 0px 110px">
							<input id="se" class="easyui-timespinner"  style="width:60px;" value="${etimef}"  data-options="min:'00:00',showSeconds:false" /> 
						   </div>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							自定义 :
						</td>
						<td>
							<input type="checkbox" id="zdy">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							提醒时间设置:
						</td>
						<td>
							<div id="t1"><input class="easyui-combobox"   id="time1" /></div>
							<div id="t2"><input class="easyui-combobox"   id="time2" /></div>
							<div id="t3"><input class="easyui-numberbox"  value="${schedule.timeMinus}" id="time31" style="width:60px"/>天前&nbsp;<input class="easyui-timespinner"  value="${schedule.hhmm}"  id="time32"  style="width:60px"  data-options="min:'00:00',showSeconds:false" /></div>
							<div id="t4"><input class="easyui-numberbox" value="${schedule.timeMinus}"  style='width:50px'  id="time41" />&nbsp;<input class="easyui-combobox"  id="time42"  style='width:60px'>前</div>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							备注 :
						</td>
						<td>
							<input id="remark" name="schedule.remark"  class="easyui-textbox" value="${schedule.remark}">
						</td>
					</tr>
				</table>
				</div>
				<div style="text-align:center;padding:5px 0px 0px 0px ">
				<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>
				&nbsp;<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
				</div>
				</div>
		</form>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
	<script type="text/javascript">
	var stime='${stime}';
	var stimef='${stimef}';
	var etimef='${etimef}';
	var dayFlg="${schedule.dayFlg}";
	var idZDY="${schedule.isZDY}";
	var timeMinus="${schedule.timeMinus}";
	var timeUnit="${schedule.timeUnit}";
	var scheduleFlg="${schedule.scheduleFlg}";
	var HHMM="${schedule.hhmm}";
	var id="${schedule.id}";
	var flag="${flag}"
	$(function(){
		//编辑回显设置
		if(id!=null&&id!=""){
			if(dayFlg==0&&idZDY==0){//非全天非自定义
				$("#time1").combobox({
					url:"<c:url value='/oa/agenda/agendaAction/queryMR1.action'/>",    
				    valueField:'id',    
				    textField:'text',
				    editable:false,
				    onLoadSuccess: function () { //加载完成后,设置选中第一项  
		                var val = $(this).combobox('getData');  
		                for (var item in val[scheduleFlg]) {  
		                    if (item == 'id') {  
		                        $(this).combobox('select', val[scheduleFlg][item]);  
		                    }  
		                }  
		            }  
				})
				
				$("#time2").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryMR2.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			$("#time42").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryUnit.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			
			    //设置显示,隐藏组件
			    $("#t1").show();
				$("#t2").hide();
				$("#t3").hide();
				$("#t4").hide();
				$("#s").show();
				$("#e").show();
			}else if(dayFlg==1&&idZDY==0){//全天非自定义
				//复选框回显
				$('#df').attr('checked', true);
				$("#time1").combobox({
					url:"<c:url value='/oa/agenda/agendaAction/queryMR1.action'/>",    
				    valueField:'id',    
				    textField:'text',
				    editable:false,
				    onLoadSuccess: function () { //加载完成后,设置选中第一项  
		                var val = $(this).combobox('getData');  
		                for (var item in val[0]) {  
		                    if (item == 'id') {  
		                        $(this).combobox('select', val[0][item]);  
		                    }  
		                }  
		            }  
				})
				
				$("#time2").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryMR2.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[scheduleFlg]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[scheduleFlg][item]);  
	                    }  
	                }  
	            }  
			})
			$("#time42").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryUnit.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			
			//设置显示,隐藏组件
			$("#t1").hide();
		    $("#t2").show();
			$("#t3").hide();
			$("#t4").hide();
			$("#s").hide();
			$("#e").hide();
			
			
			}else if(dayFlg==0&&idZDY==1){//非全天自定义
				//复选框回显
				$('#zdy').attr('checked', true);
				
				//设置显示,隐藏组件
				$("#t1").hide();
			    $("#t2").hide();
				$("#t3").hide();
				$("#t4").show();
				$("#s").show();
				$("#e").show();
                var t=0;
                if(timeUnit=="m"){
                	t=0;
                }else if(timeUnit=="h"){
                	t=1;
                }else if(timeUnit=='d'){
                	t=2;
                }else if(timeUnit=='w'){
                	t=3;
                }
                $("#time1").combobox({
					url:"<c:url value='/oa/agenda/agendaAction/queryMR1.action'/>",    
				    valueField:'id',    
				    textField:'text',
				    editable:false,
				    onLoadSuccess: function () { //加载完成后,设置选中第一项  
		                var val = $(this).combobox('getData');  
		                for (var item in val[0]) {  
		                    if (item == 'id') {  
		                        $(this).combobox('select', val[0][item]);  
		                    }  
		                }  
		            }  
				})
				
				$("#time2").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryMR2.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			$("#time42").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryUnit.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[t]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[t][item]);  
	                    }  
	                }  
	            }  
			}) 
			
			}else if(dayFlg==1&&idZDY==1){//全天自定义
				//复选框回显
				$('#df').attr('checked', true);
				$('#zdy').attr('checked', true);
				//设置显示,隐藏组件
				$("#t1").hide();
			    $("#t2").hide();
				$("#t3").show();
				$("#t4").hide();
				$("#s").hide();
				$("#e").hide();
				
				
				$("#time1").combobox({
					url:"<c:url value='/oa/agenda/agendaAction/queryMR1.action'/>",    
				    valueField:'id',    
				    textField:'text',
				    editable:false,
				    onLoadSuccess: function () { //加载完成后,设置选中第一项  
		                var val = $(this).combobox('getData');  
		                for (var item in val[0]) {  
		                    if (item == 'id') {  
		                        $(this).combobox('select', val[0][item]);  
		                    }  
		                }  
		            }  
				})
				$("#time2").combobox({
					url:"<c:url value='/oa/agenda/agendaAction/queryMR2.action'/>",    
				    valueField:'id',    
				    textField:'text',
				    editable:false,
				    onLoadSuccess: function () { //加载完成后,设置选中第一项  
		                var val = $(this).combobox('getData');  
		                for (var item in val[0]) {  
		                    if (item == 'id') {  
		                        $(this).combobox('select', val[0][item]);  
		                    }  
		                }  
		            }  
				})
				$("#time42").combobox({
					url:"<c:url value='/oa/agenda/agendaAction/queryUnit.action'/>",    
				    valueField:'id',    
				    textField:'text',
				    editable:false,
				    onLoadSuccess: function () { //加载完成后,设置选中第一项  
		                var val = $(this).combobox('getData');  
		                for (var item in val[0]) {  
		                    if (item == 'id') {  
		                        $(this).combobox('select', val[0][item]);  
		                    }  
		                }  
		            }  
				})
			}
			
			
			
			
			
		}
		
		
		
		
		
		/* *********************************************************************************************************************** */
		
		else{
			$("#time1").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryMR1.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			$("#time2").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryMR2.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			$("#time42").combobox({
				url:"<c:url value='/oa/agenda/agendaAction/queryUnit.action'/>",    
			    valueField:'id',    
			    textField:'text',
			    editable:false,
			    onLoadSuccess: function () { //加载完成后,设置选中第一项  
	                var val = $(this).combobox('getData');  
	                for (var item in val[0]) {  
	                    if (item == 'id') {  
	                        $(this).combobox('select', val[0][item]);  
	                    }  
	                }  
	            }  
			})
			
			$("#t1").show();
			$("#t2").hide();
			$("#t3").hide();
			$("#t4").hide();
			
		}
		
		$("#df").change(function() {
			if($("#df").is(':checked')){
				$("#s").hide();
				$("#e").hide();
				if($("#zdy").is(':checked')){//全天自定义
				$("#t1").hide();
				$("#t2").hide();
				$("#t3").show();
				$("#t4").hide();
				}else{//全天非自定义
					$("#t1").hide();
					$("#t2").show();
					$("#t3").hide();
					$("#t4").hide();
				}
			}else{
				$("#s").show();
				$("#e").show();
				if($("#zdy").is(':checked')){//非全天自定义
					$("#t1").hide();
					$("#t2").hide();
					$("#t3").hide();
					$("#t4").show();
					}else{//非全天非自定义
						$("#t1").show();
						$("#t2").hide();
						$("#t3").hide();
						$("#t4").hide();
					}
			}
			});
		
		$("#zdy").change(function() {
			if($("#zdy").is(':checked')){
				if($("#df").is(':checked')){//全天自定义
				$("#t1").hide();
				$("#t2").hide();
				$("#t3").show();
				$("#t4").hide();
				}else{//非全天自定义
					$("#t1").hide();
					$("#t2").hide();
					$("#t3").hide();
					$("#t4").show();
				}
			}else{
				if($("#df").is(':checked')){//全天fei自定义
					$("#t1").hide();
					$("#t2").show();
					$("#t3").hide();
					$("#t4").hide();
					}else{//非全天非自定义
						$("#t1").show();
						$("#t2").hide();
						$("#t3").hide();
						$("#t4").hide();
					}
			}
			});
    	});
//提交验证
function submit(){
	queryPreInfo();
}
//校验
function queryPreInfo(){
	if($("#df").is(':checked')){
		if($("#zdy").is(':checked')){//全天自定义
			var time31=$('#time31').numberbox('getValue');
             if(time31!==null&&time31!==""){
            	 if(time31>0){//通过并赋值
            		 //设置起始时间
            		 var startTime=$("#startTime").textbox("getText");
            		 $("#start").val(startTime);
            		 $("#end").val(startTime);
            		 //设置复选框
            		 $("#dayFlg").val("1");
            		 $("#isZDY").val("1");
            		 //设置时间以及单位,hhmm
            		 $("#timeMinus").val(time31);
            		 $("#timeUnit").val("d");
            		 var hhmm=$("#time32").timespinner('getValue'); 
            		 $("#hhmm").val(hhmm);
            		 //计算提醒时间
            		 var arr =startTime.split("-");
            		 var y=arr[0];
            		 var m=arr[1];
            		 var d=arr[2];
            		 var date = new Date(y,m-1,d);
            		 date.setDate(date.getDate()-time31);
            		 y = date.getFullYear();
            		 m=date.getMonth()+1;
            		 d=date.getDate();
            		 m=m>9?m:"0"+m;
            		 d=d>9?d:"0"+d;
            		 var time=y+"-"+m+"-"+d+" "+hhmm+":00";
            		 $("#time").val(time);
            		 submitSave();
            	 }else{
            		 $.messager.alert('提示',"开始时间不能大于结束时间！");
 					setTimeout(function(){
 						$(".messager-body").window('close');
 					},3000);
 					return;
            	 }
             }else{//校验不通过
            	 $.messager.alert('提示',"请完善提醒时间！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
					return;
             } 
			
		}else{//全天非自定义直接赋值
			 //设置起始时间
   		     var startTime=$("#startTime").textbox("getText");
   		     $("#start").val(startTime);
   		     $("#end").val(startTime);	
   		     //设置复选框
    		 $("#dayFlg").val("1");
    		 $("#isZDY").val("0");
    		 //计算提醒时间
    		 var arr2 =startTime.split("-");
             var y2=arr2[0];
        	 var m2=arr2[1];
        	 var d2=arr2[2];
        	 var date2 = new Date(y2,m2-1,d2);
    		 var  t=$("#time2").combobox("getValue");
    		 var time="";
    		 if(t==0){
    			 time=startTime+" 09:00:00";
    		 }else if(t==1){
        		 date2.setDate(date2.getDate()-1);
        		 y2 = date2.getFullYear();
        		 m2=date2.getMonth()+1;
        		 d2=date2.getDate();
        		 m2=m2>9?m2:"0"+m2;
        		 d2=d2>9?d2:"0"+d2;
        		 var time=y2+"-"+m2+"-"+d2+" 17:00:00";
    		 }else if(t==2){
    			 date2.setDate(date2.getDate()-1);
        		 y2 = date2.getFullYear();
        		 m2=date2.getMonth()+1;
        		 d2=date2.getDate();
        		 m2=m2>9?m2:"0"+m2;
        		 d2=d2>9?d2:"0"+d2;
        		 var time=y2+"-"+m2+"-"+d2+" 09:00:00";
    		 }else if(t==3){
    			 date2.setDate(date2.getDate()-2);
        		 y2 = date2.getFullYear();
        		 m2=date2.getMonth()+1;
        		 d2=date2.getDate();
        		 m2=m2>9?m2:"0"+m2;
        		 d2=d2>9?d2:"0"+d2;
        		 var time=y2+"-"+m2+"-"+d2+" 09:00:00";
    		 }else if(t==4){
    			 date2.setDate(date2.getDate()-7);
        		 y2 = date2.getFullYear();
        		 m2=date2.getMonth()+1;
        		 d2=date2.getDate();
        		 m2=m2>9?m2:"0"+m2;
        		 d2=d2>9?d2:"0"+d2;
        		 var time=y2+"-"+m2+"-"+d2+" 09:00:00";
    		 }
    		 $("#time").val(time);
    		 //非自定义选项
    		 $("#scheduleFlg").val(t);
			 submitSave();
		}
	}else{
     if($("#zdy").is(':checked')){//非全天自定义
    		var time41=$('#time41').numberbox('getValue');
			 if(time41!==null&&time41!==""){
           	 if(time41>0){//通过并赋值
           	   //设置起始时间
               var startTime=$("#startTime").textbox("getText"); 
               var ss=$("#ss").timespinner('getValue'); 
               var se=$("#se").timespinner('getValue');
               var sarr =ss.split(":");
               var earr =se.split(":");
               var sd=new Date(2017,1,1,sarr[0],sarr[1],1);
               var ed=new Date(2017,1,1,earr[0],earr[1],1);
               if(sd>ed){
	           	   $.messager.alert('提示',"开始时间不能大于结束时间！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
					return;
               }
               $("#start").val(startTime+" "+ss);
      		   $("#end").val(startTime+" "+se);
      		   //设置复选框
      		   $("#dayFlg").val("0");
      		   $("#isZDY").val("1");	 
      		   //设置时间以及单位,hhmm
      		   var time41=$("#time41").numberbox('getValue');
      		   var time42=$("#time42").combobox("getValue");
      		   $("#timeMinus").val(time41);
      		   $("#timeUnit").val(time42);	 
      		   //计算提醒时间 
      		   var time3=0;
           	   if(time42=="m"){
           		   time3=time41;
           	   }else if(time42=="h"){
           		   time3=time41*60;
           	   }else if(time42=="d"){
           		   time3=time41*60*24;
           	   }else if(time42=="w"){
           		   time3=time41*60*24*7;
           	   }	 
           	   var h3=$("#ss").timespinner('getHours'); 
           	   var mi3=$("#ss").timespinner('getMinutes'); 
	           var arr3 =startTime.split("-");
	   		   var y3=arr3[0];
	   		   var m3=arr3[1];
	   		   var d3=arr3[2];
	   		   var date3 = new Date(y3,m3-1,d3,h3,mi3,0);
	   		   date3.setMinutes(date3.getMinutes()-time3);
	   		   y3 = date3.getFullYear();
	   		   m3=date3.getMonth()+1;
	   		   d3=date3.getDate();
	   		   h3=date3.getHours();
	   		   mi3=date3.getMinutes();
	   		   s3=date3.getSeconds();
	   		   m3=m3>9?m3:"0"+m3;
	   		   d3=d3>9?d3:"0"+d3;
	   		   h3=h3>9?h3:"0"+h3;
	   		   mi3=mi3>9?mi3:"0"+mi3;
	   		   var time=y3+"-"+m3+"-"+d3+" "+h3+":"+mi3+":00";
   		       $("#time").val(time);
           	   submitSave();
           	 }else{
           		 $.messager.alert('提示',"开始时间不能大于结束时间！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
					return;
           	 }
            }else{//校验不通过
           	 $.messager.alert('提示',"请完善提醒时间！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
					return;
            } 
		}else{//非全天非自定义
			//设置起始时间
            var startTime=$("#startTime").textbox("getText"); 
            var ss=$("#ss").timespinner('getValue'); 
            var se=$("#se").timespinner('getValue'); 
            $("#start").val(startTime+" "+ss);
   		    $("#end").val(startTime+" "+se);
	   		 var sarr =ss.split(":");
	         var earr =se.split(":");
	         var sd=new Date(2017,1,1,sarr[0],sarr[1],1);
	         var ed=new Date(2017,1,1,earr[0],earr[1],1);
	         if(sd>ed){
	         	   $.messager.alert('提示',"开始时间不能大于结束时间！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
					return;
	         }
   		    //设置复选框
   		    $("#dayFlg").val("0");
   		    $("#isZDY").val("0");	 
			 //计算时间
			 var  t=$("#time1").combobox("getValue");
			 var arr4 =startTime.split("-");
             var y4=arr4[0];
	      	 var m4=arr4[1];
	      	 var d4=arr4[2];
	      	 var h4=$("#ss").timespinner('getHours');
	      	 var mi4=$("#ss").timespinner('getMinutes');
	      	 var s4=0;
	      	 var date4 = new Date(y4,m4-1,d4,h4,mi4,s4);
	  		 var time="";
	  		 if(t==0){
	  			 time=startTime+" "+ss;
	  		 }else if(t==1){
	      		 date4.setMinutes(date4.getMinutes()-10);
	      		 y4 = date4.getFullYear();
	      		 m4=date4.getMonth()+1;
	      		 d4=date4.getDate();
	      		 h4=date4.getHours();
	      		 mi4=date4.getMinutes();
	      		 s4=date4.getSeconds();
	      		 m4=m4>9?m4:"0"+m4;
	      		 d4=d4>9?d4:"0"+d4;
	      		 h4=h4>9?h4:"0"+h4;
	      		 mi4=mi4>9?mi4:"0"+mi4;
	      		 s4=s4>9?s4:"0"+s4;
	      		 var time=y4+"-"+m4+"-"+d4+" "+h4+":"+mi4+":"+s4;
	  		 }else if(t==2){
	  			date4.setMinutes(date4.getMinutes()-30);
	      		 y4 = date4.getFullYear();
	      		 m4=date4.getMonth()+1;
	      		 d4=date4.getDate();
	      		 h4=date4.getHours();
	      		 mi4=date4.getMinutes();
	      		 s4=date4.getSeconds();
	      		 m4=m4>9?m4:"0"+m4;
	      		 d4=d4>9?d4:"0"+d4;
	      		 h4=h4>9?h4:"0"+h4;
	      		 mi4=mi4>9?mi4:"0"+mi4;
	      		 s4=s4>9?s4:"0"+s4;
	      		 var time=y4+"-"+m4+"-"+d4+" "+h4+":"+mi4+":"+s4;
	  		 }else if(t==3){
	  			date4.setMinutes(date4.getMinutes()-60);
	      		 y4 = date4.getFullYear();
	      		 m4=date4.getMonth()+1;
	      		 d4=date4.getDate();
	      		 h4=date4.getHours();
	      		 mi4=date4.getMinutes();
	      		 s4=date4.getSeconds();
	      		 m4=m4>9?m4:"0"+m4;
	      		 d4=d4>9?d4:"0"+d4;
	      		 h4=h4>9?h4:"0"+h4;
	      		 mi4=mi4>9?mi4:"0"+mi4;
	      		 s4=s4>9?s4:"0"+s4;
	      		 var time=y4+"-"+m4+"-"+d4+" "+h4+":"+mi4+":"+s4;
	  		 }else if(t==4){
	  			date4.setMinutes(date4.getMinutes()-1440);
	      		 y4 = date4.getFullYear();
	      		 m4=date4.getMonth()+1;
	      		 d4=date4.getDate();
	      		 h4=date4.getHours();
	      		 mi4=date4.getMinutes();
	      		 s4=date4.getSeconds();
	      		 m4=m4>9?m4:"0"+m4;
	      		 d4=d4>9?d4:"0"+d4;
	      		 h4=h4>9?h4:"0"+h4;
	      		 mi4=mi4>9?mi4:"0"+mi4;
	      		 s4=s4>9?s4:"0"+s4;
	      		 var time=y4+"-"+m4+"-"+d4+" "+h4+":"+mi4+":"+s4;
	  		 }
			$("#time").val(time);
			//非自定义选项
   		    $("#scheduleFlg").val(t);
			submitSave();
			
		}
	}
}

function submitSave(){
	$('#editForm').form('submit', {
		url : "<c:url value='/oa/agenda/agendaAction/saveOrUpdate.action'/>",
		onSubmit : function() {
			if(!$('#editForm').form('validate')){
				$.messager.show({  
			         title:'提示信息' ,   
			         msg:'验证没有通过,不能提交表单!'  
			    }); 
			       return false ;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success:function(data){
			if(data==1){
				$.messager.progress('close');
				closeLayout();
				$.messager.alert("操作提示","系统异常，请联系管理员");
			}
			if(data==0){
				$.messager.progress('close');
				closeLayout();
				if(flag==1){
					reload();
				}else{
				 window.location.href="<c:url value='/oa/agenda/agendaAction/agendaActionToView.action'/>";
				}
			}
		 }
		}); 
}
	
</script>
</body>
</html>