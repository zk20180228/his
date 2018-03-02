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
	<title>每月综合仪表盘</title>
<style type="text/css">
.fo{
	width:25%;
}
.center{text-align:center; }
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">

/*
 * 时间控件的点击事件
 */
	function pickedFunc(){
		start();
	}
	var timeAllPage="${Etime}";
	var nowMonth;//当前月
	var lasttMonth//上一月
	var lastYearMonth;//上年同月
	//时间数组
	var arr=[];
	//时间数据对应数据
	var arrDate=[];
    /* top1 */
    var goTotalPer=0;
    var goPeople='出院人数：计算中..';
    var totals='2000人';
    var halfTotals='1000人';

    /* top2 */
  	var hmtotals;//病床适用率
    var hmtotalsList;//病床使用率显示
    /* top3 */
    var mOperationApply='手术例数：计算中..';
    var mOper=0;
    var mTotals='400例';
    var mHalfTotals='200例';
    /* top4 */
    var cure=0;
    var better=0;
    var healed=0;
    var death=0;
    var other=0;
    /* top5 */
    var totCost=[0,0,0,0,0,0,0,0,0,0,0,0];
    $(function(){
	    var show = document.getElementById("show");
	    var time = new Date();
	    // 程序计时的月从0开始取值后+1
	    var m = time.getMonth() + 1;
	    var t = time.getFullYear() + "-" + m + "-"
	       + time.getDate() + " " + time.getHours() + ":"
	       + time.getMinutes() + ":" + time.getSeconds();
	    show.innerHTML = t;
	    
    	$('#deptName').combobox({
			url: '<%=basePath%>/baseinfo/department/departmentCombobox.action',
			valueField:'deptCode',    
			textField:'deptName',
			onLoadSuccess:function(){
				$(this).combobox('setText','全部');
			},
			onSelect:function(){
				start();
            }
		});
    	top1();
    	top2();
    	top3();
    	mid1();
    	mid2_1();
    	mid2_2();
    	bottom1();
    	bottom2();
    	start();
    });
    function start(){
    	var html1='';
    	var html2='';
    	var html3='';
    	var html4='';
    	var html5='';
    	
   	 	
   	 /*
   	  * 获取选择月份 的前12个月的时间段
   	  */
   	  arr.splice(0,arr.length);//清空数组
   	  var str =$("#time").val();
 	  var myDate = new Date(str.replace(/-/,"/"))
 	  
   	  var month=myDate.getMonth()+1;//今年月份
   	  var year=myDate.getFullYear();//今年
   	  var lowYear=parseInt(year)-1
   	  var lastYear= lowYear+'-'+(parseInt(month)+1)+'-01';
   	  var nowYear=year+'-'+month+'-'+myDate.getDate();
   	  var yearl=year-1;//上一年
   	  var monthl=month+1;//上一年月份
   	  var time="";
   	  for(var i=0;i<12;i++){
   	 	 for(var i=monthl;i<13;i++){
   	 		 time=yearl+"-"+i;
   	 		 arr.push(time.replace(/-(\d{1}\b)/g,'-0$1'));
   	 	 }
   	 	 for(var j=1;j<month+1;j++){
   	 		 time=year+"-"+j;
   	 		 arr.push(time.replace(/-(\d{1})/g,'-0$1'));
   	 	 }	 
   	  }
   	nowMonth=str;//当前
   	if(month==1){
   	lasttMonth=yearl+'-12';//上一月
   	}else{
    lasttMonth=year+'-'+(month-1);//上一月
   	}
   	lasttMonth=lasttMonth.replace(/-(\d{1}\b)/g,'-0$1');
   	lastYearMonth=lowYear+'-'+month;//上年同月
   	lastYearMonth=lastYearMonth.replace(/-(\d{1}\b)/g,'-0$1');
   	  bottom1();
   	 $.ajax({ 
		   url:"<%=basePath%>statistics/monthlyDashboard/queryHospExpenses.action",
		   type:"post",
		   data: {startDate:lastYear,endDate:nowYear, date:$("#time").val(),deptName:$("#deptName").combobox("getText")},
           dataType: "json",
		   success:function(data){
		       var totalNow=0;//当前月收入
		       var tatalLastMonth=0;//上一月收入
		       var totalLastYearMonth=0;//上年同月收入
			   totCost=data;
		       arrDate=[];
			   var len=arr.length;
			   for(var i=0;i<len;i++){
				   if(totCost[arr[i]]!=null){
					   arrDate.push(totCost[arr[i]].toFixed(2));
				   }else{
					   arrDate.push(0);
				   }
			   }
			   
			   if(totCost[nowMonth]!=null){
				   totalNow=parseInt(totCost[nowMonth]);
			   }
			   if(totCost[lasttMonth]!=null){
				   tatalLastMonth=parseInt(totCost[lasttMonth]);
			   }
			   if(totCost[lastYearMonth]!=null){
				   totalLastYearMonth=parseInt(totCost[lastYearMonth]);
			   }
 		      html2='<span>'+(tatalLastMonth==0?'0.00':((totalNow-tatalLastMonth)/tatalLastMonth*100).toFixed(2))+'%</span>';
 		      html3='<span>'+(totalLastYearMonth==0?'0.00':((totalNow-totalLastYearMonth)/totalLastYearMonth*100).toFixed(2))+'%</span>';
 		      $('#total').html(fmoney(totalNow/10000,2)+'万元');
 		      $("#yearPer").html(html2);
 			  $("#monthPer").html(html3);
 			  bottom1();
		   }
 	 });
   	 $.ajax({ 
 		   url:"<%=basePath%>statistics/monthlyDashboard/queryInpatientInfoNowGo.action",
 		   type:"post",
 		   data: {date:$("#time").val(), deptName:$("#deptName").combobox("getText")},
           dataType: "json",
 		   success:function(data){
 			   var total=0;//当前月人数
 			   var lastTotal=0;//上一月人数
 			   var lastYearTotal=0;//上一年同月人数
 			   var allTotal=0;//总人数
 			    if(data!=null&&data!=''){
 			    	for(var i=0;i<data.length;i++){
 			    		if(null!=data[i].yearAndMonth&&!isNaN(data[i].countLeave)){
 			    			if(data[i].yearAndMonth==nowMonth){//当前月人数
 		 			    		total+=parseInt(data[i].countLeave);
 			    			}
 			    			if(data[i].yearAndMonth==lasttMonth){//上一月人数
 			    				lastTotal+=parseInt(data[i].countLeave);
 			    			}
 			    			if(data[i].yearAndMonth==lastYearMonth){//上年同月人数
 			    				lastYearTotal+=parseInt(data[i].countLeave);
 			    			}
 			    			if(data[i].yearAndMonth!=lastYearMonth){
 			    				allTotal+=parseInt(data[i].countLeave);
 			    			}
 			    			
 			    		}
 			    		
 			    	}
 			    	
 			    }
 			  goPeople=total+"人";
 			  totals=total+"人";
 			  goTotalPer=100;//出院百分比
 			  halfTotals=(total/2).toFixed(0)+"人";
 			  html1='<span>'+((allTotal/12).toFixed(0))+"人"+'</span>';
 			  html2='<span>'+(lastYearTotal==0?'0.00':((total-lastYearTotal)/lastYearTotal*100).toFixed(2))+'%</span>';
 			  html3='<span>'+(lastTotal==0?'0.00':((total-lastTotal)/lastTotal*100).toFixed(2))+'%</span>';
 			  $("#avg1").html(html1);
 			  $("#avgPer1").html(html2);
 			  $("#mPer1").html(html3);
 			  top1();
 		   }
    	});
   	 $.ajax({ 
 		   url:"<%=basePath%>statistics/monthlyDashboard/queryOperationApply.action",
 		   type:"post",
 		   data: {date:$('#time').val(), deptCode:$("#deptName").combobox("getValue")},
           dataType: "json",
 		   success:function(data){
 			   var operaTion=0;//循环计算手术例数
 			   var deptOperaTion=0;//科室手术例数
 			   var deptNow=$('#deptName').combobox('getValue');
 			  var total=0;//当前月人数
			   var lastTotal=0;//上一月平均天数
			   var lastYearTotal=0;//上一年同月住院天数
// 			   var allTotal=0;//总住院天数
 			   if(deptNow==''){
 				   deptNow='ALL';
 			   }
			   var deptNum=0;//科室
			   var depted=0;
			   var deptyed=0;
 			   var len=data.length;
 			   for(var i=0;i<len;i++){
 				   if(data[i].value!=null&&data[i].value!=''){
 					  if('ALL'==deptNow){
 						if(data[i].name==nowMonth&&!isNaN(parseFloat(data[i].inhost))){//当前月住院天数
 							operaTion+=parseInt(data[i].value);  //总计手术
 							deptOperaTion+=parseInt(data[i].value);
 							deptNum++;
//  							allTotal+=parseInt(data[i].inhost);
	 			    		total+=parseFloat(data[i].inhost);
		    			}
		    			if(data[i].name==lasttMonth&&!isNaN(parseInt(data[i].inhost))){//上一月天数
		    				depted++;
		    				lastTotal+=parseFloat(data[i].inhost);
		    			}
		    			if(data[i].name==lastYearMonth&&!isNaN(parseInt(data[i].inhost))){//上年同月人数
		    				deptyed++;
		    				lastYearTotal+=parseFloat(data[i].inhost);
		    			}
// 		    			if(data[i].name!=lastYearMonth&&!isNaN(parseInt(data[i].inhost))){
// 		    				allTotal+=parseInt(data[i].inhost);
// 		    			}
 					  }else if(deptNow==data[i].dept){
 						 deptOperaTion+=parseInt(data[i].value);
 						if(data[i].name==nowMonth&&!isNaN(parseInt(data[i].inhost))){//当前月住院天数
	 			    		total+=parseFloat(data[i].inhost);
	 			    		deptNum++;
// 	 			    		allTotal+=parseFloat(data[i].inhost);
	 			    		
		    			}
		    			if(data[i].name==lasttMonth&&!isNaN(parseInt(data[i].inhost))){//上一月天数
		    				depted++;
		    				lastTotal+=parseFloat(data[i].inhost);
		    			}
		    			if(data[i].name==lastYearMonth&&!isNaN(parseInt(data[i].inhost))){//上年同月人数
		    				deptyed++;
		    				lastYearTotal+=parseFloat(data[i].inhost);
		    			}
// 		    			if(data[i].name!=lastYearMonth&&!isNaN(parseInt(data[i].inhost))){
		    				
// 		    			}
 					  }
 				   }
 			   }
 			   lastYearTotal=(deptyed==0?0:lastYearTotal/deptyed);
 			   lastTotal=(depted==0?0:lastTotal/depted);
 			  total=(total==0?0:total/deptNum);
//  			   console.info(lastYearTotal);
//  			   console.info(lastTotal);
//  			   console.info(allTotal);
 			   if(operaTion==0){
 				  operaTion=1;
 			   }else{
 				  mOper=(deptOperaTion/operaTion*100).toFixed(2); 
 			   }
 			  mOperationApply="手术总例数："+(operaTion)+"例";//科室手术例数
 			  mTotals=operaTion+"例";//总例数
 			  var maxOper=operaTion;
 			  mHalfTotals=(operaTion/2).toFixed(0)+"例";
 			  html1='<span>'+(total).toFixed(0)+"天"+'</span>';
 			  html2='<span>'+(lastYearTotal==0?'0.00':((total-lastYearTotal)/lastYearTotal*100).toFixed(2))+'%</span>';
 			  html3='<span>'+(lastTotal==0?'0.00':((total-lastTotal)/lastTotal*100).toFixed(2))+'%</span>';
 			  $("#avg3").html(html1);
 			  $("#avgPer3").html(html2);
 			  $("#mPer3").html(html3);
 			  top3(maxOper);
 		   }
    	});
   	 $.ajax({ 
		   url:"<%=basePath%>statistics/monthlyDashboard/queryWardApply.action",
		   type:"post",
		   data: {date:$('#time').val(), deptName:$("#deptName").combobox("getText")},
           dataType: "json",
		   success:function(data){
			   var len=data.length;
			   var reg=new RegExp("^"+"\.");
			   var dada;
			   var ward=0;
			   var total=0;//当前月人数
 			   var lastTotal=0;//上一月平均天数
 			   var lastYearTotal=0;//上一年同月住院天数
//  			   var allTotal=0;//总住院天数
			   var depted=0;
			   var deptyed=0;
 			   var deptNum=0;
 			    if(data!=null&&data!=''){
 			    	for(var i=0;i<len;i++){
 			    		if(null!=data[i].name&&!isNaN(data[i].inhost)){
 			    			if(data[i].name==nowMonth){//当前月住院天数
//  			    				allTotal+=parseFloat(data[i].inhost);
 			    			   deptNum++;//当月科室累加
 		 			    	   total+=parseFloat(data[i].inhost);
 		 			    	   dada=data[i].value;
 		 			    	  if(!isNaN(dada)){
 	 							   ward+=parseFloat(dada); 
 	 						   }else{
 	 							   ward+=parseFloat('0'+dada);
 	 						   }
 			    			}
 			    			if(data[i].name==lasttMonth){//上一月天数
 			    				lastTotal+=parseFloat(data[i].inhost);
 			    				depted++;
 			    			}
 			    			if(data[i].name==lastYearMonth){//上年同月人数
 			    				lastYearTotal+=parseFloat(data[i].inhost);
 			    				deptyed++;
 			    			}
//  			    			if(data[i].name!=lastYearMonth){
 			    				
//  			    			}
 			    		}
 			    		
 			    	}
 			    	
 			    }
 			   lastYearTotal=(deptyed==0?1:lastYearTotal/deptyed);
 			  lastTotal=(depted==0?1:lastTotal/depted);
 			  total=(deptNum==0?1:total/deptNum);
			   hmtotals=ward.toFixed(2);
			   hmtotalsList='病床使用率:'+ward.toFixed(2)+'%'
			   bfsyAvg=(total).toFixed(0);//病房使用率人均住院天数
			   bfsyTb=(lastYearTotal==0?'0.00':(((total-lastYearTotal)/lastYearTotal*100).toFixed(2))) ; //病房使用率同比
			   bfsyHb=(lastTotal==0?'0.00':(((total-lastTotal)/lastTotal*100).toFixed(2))) ;;
			   $('#avg2').html(bfsyAvg+'天');
			   $('#avgPer2').html(bfsyTb+'%');
			   $('#mPer2').html(bfsyHb+'%');
			   top2();
		   }
   	 });
   	  $.ajax({ 
 		   url:"<%=basePath%>statistics/monthlyDashboard/queryTreatment.action",
 		   type:"post",
 		   data: {date:$('#time').val(), deptName:$("#deptName").combobox("getText")},
           dataType: "json",
 		   success:function(data){
 			   var leixingArr="["
 			   var len=data.length;
 			  cure=0;
 			  better=0;
 			  healed=0;
 			  death=0;
 			  other=0;
 			  var weizhi=0;
 			  var stat;
 			  var value;
 			   for(var i=0;i<len;i++){
 				   value=data[i].value;
 				   stat=data[i].stat;
 				   if(!isNaN(value)){
 					  if(stat=='好转'){
 	 					  better+=parseInt(value);
 	 				   }else if(stat=='其他'){
 	 					  other+=parseInt(value);
 	 				   }else if(stat=='死亡'){
 	 					  death+=parseInt(value);
 	 				   }else if(stat=='未愈'){
 	 					  weizhi+=parseInt(value);
 	 				   }else if(stat=='无效'){
 	 					  healed+=parseInt(value);
 	 				   }else if(stat=='治愈'){
 	 					  cure+=parseInt(value);
 	 				   }  
 				   }
 			   }
	 			   	if(better!=0){
	 			   	leixingArr+= "{name:'好转',value:'"+better+"'},";	
	 			   	}
	 			   if(other!=0){
		 			leixingArr+= "{name:'其他',value:'"+other+"'},";
	 			   }
		 			if(death!=0){
		 			leixingArr+= "{name:'死亡',value:'"+death+"'},";
		 			}
		 			if(weizhi!=0){
		 			leixingArr+= "{name:'未愈',value:'"+weizhi+"'},";
		 			}
		 			if(healed!=0){
		 			leixingArr+= "{name:'无效',value:'"+healed+"'},";
		 			}
		 			if(cure!=0){
		 			leixingArr+= "{name:'治愈',value:'"+cure+"'},";
		 			}
		 			len=leixingArr.length;
		 			if(len>1){
		 				leixingArr=leixingArr.substr(0,leixingArr.length-1);
	 			     	leixingArr+="]";
 			   	    	mid1(leixingArr);
		 			}else{
		 				$('#midOne').html('<H1 >暂无数据</H1>');
		 			}
 			  html1='<span>'+better+'</span>';
 			  html2='<span>'+other+'</span>';
 			  html3='<span>'+death+'</span>';
 			  html4='<span>'+healed+'</span>';
 			  html5='<span>'+cure+'</span>';
 			  html6='<span>'+weizhi+'</span>';
 			  $("#betterPer").html(html1);
 			  $("#otherPer").html(html2);
 			  $("#deathPer").html(html3);
 			  $("#healedPer").html(html4);
 			  $("#curePer").html(html5);
 			  $('#weizhi').html(html6)
 			
 		   }
    	});
    } 
	// 出院，仪表盘
	function top1(){
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		
		require(
			    [
			        'echarts',
			        'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
		    var myChartt1 = ec.init(document.getElementById('topOne'));
		    optiont1 = {
		    		title: {
		                 text : '出院人数',
		             },
		    	    series : [
		    	        {
		    	            name:'出院人数',
		    	            type:'gauge',
		    	            center : ['50%', '70%'],    // 默认全局居中
		    	            radius : [0, '100%'],
		    	            startAngle: 160,
		    	            endAngle : 20,
		    	            min: 0,                     // 最小值
		    	            max: 90,                   // 最大值
		    	            precision: 0,               // 小数精度，默认为0，无小数点
		    	            splitNumber: 2,             // 分割段数，默认为5
		    	            axisLine: {            // 坐标轴线
		    	                show: true,        // 默认显示，属性show控制显示与否
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: [[0.85, '#00ff00'],[0.95, '#ffd700'],[1, '#ff4500']], 
		    	                    width: 36
		    	                }
		    	            },
		    	            axisTick: {            // 坐标轴小标记
		    	                show: 0,        // 属性show控制显示与否，默认不显示
		    	                splitNumber: 5,    // 每份split细分多少段
		    	                length :8,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: '#eee',
		    	                    width: 1,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
		    	                show: true,
		    	                formatter: function(v){
		    	                    switch (v+''){
		    	                        case '0': return '0人';
		    	                        case '45': return halfTotals;
		    	                        case '90': return totals;
		    	                        default: return '';
		    	                    }
		    	                },
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333'
		    	                }
		    	            },
		    	            splitLine: {           // 分隔线
		    	                show: 0,        // 默认显示，属性show控制显示与否
		    	                length :30,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
		    	                    color: 'red',
		    	                    width: 2,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            pointer : {
		    	                length : '90%',
		    	                width : 2,
		    	                color : '#333'
		    	            },
		    	            title : {
		    	                show : true,
		    	                offsetCenter: [0, '45%'],       // x, y，单位px
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            detail : {
		    	                show : true,
		    	                backgroundColor: 'rgba(0,0,0,0)',
		    	                borderWidth: 0,
		    	                borderColor: '#ccc',
		    	                width: 100,
		    	                height: 40,
		    	                offsetCenter: [0, '7%'],       // x, y，单位px
		    	                formatter:'{value}%',
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: 'auto',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            data:[{value: goTotalPer, name:goPeople}]
		    	        }
		    	    ]
		    	};
// 		     setInterval(function () {
// 		        option1.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
		      
// 		    },2000); 
		      myChartt1.setOption(optiont1, true);
		     
			    }  
		    );
		
	}
	function top2(){
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		
		require(
			    [
			        'echarts',
			        'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
		    var myChart2 = ec.init(document.getElementById('topTwo'));
		     option2 = {
		    		title: {
		                 text : '病床使用率',
		             },
		    	    series : [
		    	        {
		    	            name:'病床使用率',
		    	            type:'gauge',
		    	            center : ['50%', '70%'],    // 默认全局居中
		    	            radius : [0, '100%'],
		    	            startAngle: 160,
		    	            endAngle : 20,
		    	            min: 0,                     // 最小值
		    	            max: 90,                   // 最大值
		    	            precision: 0,               // 小数精度，默认为0，无小数点
		    	            splitNumber: 2,             // 分割段数，默认为5
		    	            axisLine: {            // 坐标轴线
		    	                show: true,        // 默认显示，属性show控制显示与否
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: [[0.7, '#00ff00'],[1, '#ff4500']], 
		    	                    width: 36
		    	                }
		    	            },
		    	            axisTick: {            // 坐标轴小标记
		    	                show: 0,        // 属性show控制显示与否，默认不显示
		    	                splitNumber: 5,    // 每份split细分多少段
		    	                length :8,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: '#eee',
		    	                    width: 1,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
		    	                show: true,
		    	                formatter: function(v){
		    	                    switch (v+''){
		    	                        case '0': return '0%';
		    	                        case '45': return '75%';
		    	                        case '90': return '150%';
		    	                        default: return '';
		    	                    }
		    	                },
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333'
		    	                }
		    	            },
		    	            splitLine: {           // 分隔线
		    	                show: 0,        // 默认显示，属性show控制显示与否
		    	                length :30,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
		    	                    color: 'red',
		    	                    width: 2,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            pointer : {
		    	                length : '90%',
		    	                width : 2,
		    	                color : '#333'
		    	            },
		    	            title : {
		    	                show : true,
		    	                offsetCenter: [0, '45%'],       // x, y，单位px
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            detail : {
		    	                show : true,
		    	                backgroundColor: 'rgba(0,0,0,0)',
		    	                borderWidth: 0,
		    	                borderColor: '#ccc',
		    	                width: 100,
		    	                height: 40,
		    	                offsetCenter: [0, '7%'],       // x, y，单位px
		    	                formatter:'{value}%',
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: 'auto',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            data:[{value: hmtotals, name: hmtotalsList}]
		    	        }
		    	    ]
		    	};
		    setInterval(function () {
		    	option2.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
		      
		    },2000);
		      myChart2.setOption(option2, true);
			    }
		    );
	}
	function top3(maxOper){
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		
		require(
			    [
			        'echarts',
			        'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
		    var myChart2 = ec.init(document.getElementById('topThr'));
		     option3 = {
		    		title: {
		                 text : '手术例数',
		             },
		    	    series : [
		    	        {
		    	            name:'手术例数',
		    	            type:'gauge',
		    	            center : ['50%', '70%'],    // 默认全局居中
		    	            radius : [0, '100%'],
		    	            startAngle: 160,
		    	            endAngle : 20,
		    	            min: 0,                     // 最小值
		    	            max: 90,                   // 最大值
		    	            precision: 0,               // 小数精度，默认为0，无小数点
		    	            splitNumber: 2,             // 分割段数，默认为5
		    	            axisLine: {            // 坐标轴线
		    	                show: true,        // 默认显示，属性show控制显示与否
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: [[0.85, '#00ff00'],[0.95, '#ffd700'],[1, '#ff4500']], 
		    	                    width: 36
		    	                }
		    	            },
		    	            axisTick: {            // 坐标轴小标记
		    	                show: 0,        // 属性show控制显示与否，默认不显示
		    	                splitNumber: 5,    // 每份split细分多少段
		    	                length :8,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: '#eee',
		    	                    width: 1,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
		    	                show: true,
		    	                formatter: function(v){
		    	                    switch (v+''){
		    	                        case '0': return '0例';
		    	                        case '45': return mHalfTotals;
		    	                        case '90': return mTotals;
		    	                        default: return '';
		    	                    }
		    	                },
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333'
		    	                }
		    	            },
		    	            splitLine: {           // 分隔线
		    	                show: 0,        // 默认显示，属性show控制显示与否
		    	                length :30,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
		    	                    color: 'red',
		    	                    width: 2,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            pointer : {
		    	                length : '90%',
		    	                width : 2,
		    	                color : '#333'
		    	            },
		    	            title : {
		    	                show : true,
		    	                offsetCenter: [0, '45%'],       // x, y，单位px
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            detail : {
		    	                show : true,
		    	                backgroundColor: 'rgba(0,0,0,0)',
		    	                borderWidth: 0,
		    	                borderColor: '#ccc',
		    	                width: 100,
		    	                height: 40,
		    	                offsetCenter: [0, '7%'],       // x, y，单位px
		    	                formatter:'{value}例',
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: 'auto',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            data:[{value: mOper, name: mOperationApply}]
		    	        }
		    	    ]
		    	};
		    setInterval(function () {
		    	option3.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
		      
		    },2000);
		      myChart2.setOption(option3, true);
			    }
		    );
	}
	function mid1(leixingArr){
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		
		require(
			    [
			        'echarts',
			        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
		    var myChartM1 = ec.init(document.getElementById('midOne'));
		    optionM1 = {
		    	    title : {
		    	        text: '治疗数量'
		    	    },
		    	    tooltip : {
		    	        trigger: 'item',
		    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    	    },
		    	    legend: {
		    	        orient : 'horizontal',
		    	        x : 'left',
		    	        data:['','好转','其他','死亡','未愈','无效','治愈']
		    	    },

		    	    calculable : true,
		    	    series : [
		    	        {
		    	            name:'治疗数量',
		    	            type:'pie',
		    	            radius : '55%',
		    	            center: ['50%', '60%'],
		    	            data:eval(leixingArr)
		    	        }
		    	    ]
		    	};
		      myChartM1.setOption(optionM1, true);
			    }
		    );
	}
function mid2_1(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChart2 = ec.init(document.getElementById('midTwo1'));
				option2 = {
					    title : {
					        text: '手术死亡人数',
					    },
					    tooltip : {
					    	trigger: 'item',
			    	        formatter: "{a} : {c}"
					    },
					    calculable : true,
					    xAxis : [
					        {	
					        	splitLine : 0,
					            type : 'value',
					            boundaryGap : [0, 0.01]
					        }
					    ],
					    yAxis : [
					        {
					        	splitLine : 0,
					            axisLabel : 0,
					          	axisLine : 0,
					            type : 'category',
					            data : [5]
					        }
					    ],
					    series : [
					        {
					            name:'手术死亡人数',
					            type:'bar',
					            data:[5]
					        }
					    ]
					};
				myChart2.setOption(option2);
		    });
}
function mid2_2(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChart2_2 = ec.init(document.getElementById('midTwo2'));
				option2_2 = {
					    title : {
					        text: '手术死亡率',
					    },
					    tooltip : {
					    	trigger: 'item',
			    	        formatter: "{a} : {c}"
					    },
					    calculable : true,
					    xAxis : [
					        {
					        	splitLine : 0,
					            type : 'value',
					            boundaryGap : [0, 0.01]
					        }
					    ],
					    yAxis : [
					        {
					        	splitLine : 0,
					            axisLabel : 0,
					          	axisLine : 0,
					            type : 'category',
					            data : [0.45]
					        }
					    ],
					    series : [
					        {
					        	name:'手术死亡率',
					            type:'bar',
					            data:[0.45]
					        }
					    ]
					};
				myChart2_2.setOption(option2_2);
		    });
}
function bottom1(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChartB1 = ec.init(document.getElementById('bottomOne'));
		   		optionB1 = {
		   			   title: {
		   			       text: "住院费用",
		   			       x: "left"
		   			   },
				   	   tooltip: {
				   	   	    trigger: 'axis'
				   	   	},
		   			   legend: {
		   			       x: 'center',
		   			       data: ["住院费用"]
		   			   },
		   			   xAxis: [
		   			       {
		   			           type: "category",
		   			           name: "x",
		   			           splitLine: {show: false},
		   			           data: arr,
			   			       axisLabel:{  
			                       rotate:45,//倾斜度 -90 至 90 默认为0  
			   			       }
		   			       	}
		   			   ],
		   			   yAxis: [
		   			       {
		   			           type: "value",
		   			           name: "元"
		   			       }
		   			   ],
		   			  
		   			   calculable: true,
		   			   series: [
		   			       {
		   			           name: "住院费用",
		   			           type: "line",
		   			           data: arrDate

		   			       }
		   			   ]
		   			};
				myChartB1.setOption(optionB1);
		    });
}
function bottom2(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChartB2 = ec.init(document.getElementById('bottomTwo'));
		   		optionB2 = {
		   			    title : {
		   			        text: '疾病分类'
		   			    },
		   			    tooltip : {
		   			        trigger: 'item'
		   			    },
		   			    legend: {
		   			        data:['疾病分类']
		   			    },
		   			    calculable : true,
		   			    xAxis : [
		   			        {
		   			            type : 'value',
		   			         	data : [42,44,46,48,50,52]
		   			        }
		   			    ],
		   			    yAxis : [
		   			        {
		   			            type : 'category',
		   			            data : ['老年痴呆','小儿麻痹','心脏病','肺癌']
		   			        }
		   			    ],
		   			    series : [
		   			        {
		   			            name:'疾病分类',
		   			            type:'bar',
		   			            data:[51, 46, 45, 43]
		   			        }
		   			    ]
		   			};
				myChartB2.setOption(optionB2);
		    });
}
function clear(){
	$('#deptName').combobox('setValue','');
	$('#deptName').combobox('setText','全部');
	$('#time').val(timeAllPage);
	start();
}
function fmoney(s, n)  
{  
   n = n > 0 && n <= 20 ? n : 2;  
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
   var l = s.split(".")[0].split("").reverse(),  
   r = s.split(".")[1];  
   t = "";  
   for(i = 0; i < l.length; i ++ )  
   {  
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
   }  
   return t.split("").reverse().join("") + "." + r;  
}
</script>
</head>
<body>
	<div id="topTiltle" class="bottomLine" style=" width:100%; height:5%">
		<table style="width:100%;height:100%;">
			<tr class="monthlyTit">
				<th style="width:23%"align="left"></th>
				<th style="width:33%">
					月值
					<input id="time" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc})" />
		                              科室
		            <input class="easyui-combobox" id="deptName" name="deptName" />            
	    			<a href="javascript:clear();"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>	
				</th>
				<th style="width:22%">最后更新:<span id="show"></span></th>
				<th style="width:22%">同比：与去年同月对比；环比：与上个月对比</th>
			</tr>
		</table>	
	</div>
	<div id="fir" class="bottomLine" style=" width:100%; height:45%">
	    <div id="topOne" style="width:22%;height:100%;float: left;"></div>
	    <div style="width:11%;height:100%;float: left;background:#f5f5f5;">
	    	<table style="width:100%;height:25%;margin-bottom:40%; float: left;" class="monthlyTit1">
	    		<tr style="font-size:16;" align="left">
	    			<th colspan="2">12个月平均数</th>
	    		</tr>
	    		<tr align="left">
	    			<th></th>
	    			<th id="avg1">计算中..</th>
	    		</tr>
	    		<tr>
	    			<td width="50%">同比</td>
	    			<td width="50%" id='avgPer1'>计算中..</td>
	    		</tr>
	    		<tr>
	    			<td width="50%">环比</td>
	    			<td width="50%" id='mPer1'>计算中..</td>
	    		</tr>
	    	</table>
	    </div>
	    <div id="topTwo" style="width:22%;height:100%;float: left;"></div>
	    <div style="width:11%;height:100%;float: left;background:#f5f5f5;">
	    	<table style="width:100%;height:25%;margin-bottom:40%; float: left;" class="monthlyTit1">
	    		<tr style="font-size:16;" align="left">
	    			<th colspan="2">平均住院日(科室)</th>
	    		</tr>
	    		<tr align="left">
	    			<th></th>
	    			<th id='avg2'>计算中..</th>
	    		</tr>
	    		<tr>
	    			<td width="50%">同比</td>
	    			<td width="50%" id="avgPer2">计算中..</td>
	    		</tr>
	    		<tr>
	    			<td width="50%">环比</td>
	    			<td width="50%" id="mPer2">计算中..</td>
	    		</tr>
	    	</table>
	    </div>
	    <div id="topThr" style="width:22%;height:100%;float: left;"></div>
	    <div style="width:11%;height:100%;float: left;background:#f5f5f5;">
	    	<table style="width:100%;height:25%;margin-bottom:40%; float: left;" class="monthlyTit1">
	    		<tr style="font-size:16;" align="left">
	    			<th colspan="2">人均住院日(科室)</th>
	    		</tr>
	    		<tr align="left">
	    			<th></th>
	    			<th id="avg3">金额</th>
	    		</tr>
	    		<tr>
	    			<td width="50%">同比</td>
	    			<td width="50%" id="avgPer3">计算中..</td>
	    		</tr>
	    		<tr>
	    			<td width="50%">环比</td>
	    			<td width="50%" id="mPer3">计算中..</td>
	    		</tr>
	    	</table>
	    </div>
	</div>
	
	<div id="sec" class="bottomLine" style="width:100%;height:48%">
		<div id="midOne" class="center" style="width:22%;height:100%; float: left;"></div>
		<div style="width:11%;height:100%; float: left;">
			<table style="width:100%;height:80%; float: left;" class="monthlyTit1">
	    		<tr style="font-size:16;" align="left">
<!-- 	    			<th></th> -->
	    			<th colspan="2">治疗效果</th>
	    		</tr>
	    		<tr>
	    			<td>好转</td>
	    			<td id="betterPer">50</td>
	    		</tr>
	    		<tr bgcolor="#f5f5f5">
	    			<td>其他</td>
	    			<td id="otherPer">5</td>
	    		</tr>
	    		<tr>
	    			<td>死亡</td>
	    			<td id="deathPer">0</td>
	    		</tr>
	    		<tr bgcolor="#f5f5f5">
	    			<td>未治</td>
	    			<td id="weizhi">0</td>
	    		</tr>
	    		<tr>
	    			<td>无效</td>
	    			<td id="healedPer">0</td>
	    		</tr>
	    		<tr bgcolor="#f5f5f5">
	    			<td>治愈</td>
	    			<td id="curePer">20</td>
	    		</tr>
	    	</table>
		</div>
	    <div style="width:66.7%;height:100%;float: left;" >
	    <div id="bottomOne" style="width:82.5%;height:100%; float: left;"></div>
		<div style="width:17.5%;height:100%; float: left;background:#f5f5f5;">
	    	<table style="width:100%;height:30%; float: left;" class="monthlyTit1">
	    		<tr style="font-size:16;"  align="left">
	    			<th>本月</th>
	    			<th id="total">计算中..</th>
	    		</tr>
	    		<tr  align="left">
	    			<td>同比增长率</td>
	    			<td id="yearPer">计算中..</td>
	    		</tr>
	    		<tr  align="left">
	    			<td>环比增长率</td>
	    			<td id="monthPer">计算中..</td>
	    		</tr>
	    	</table>
		</div>
		
	    </div>
	</div>
</body>
</html>