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
		<meta charset="utf-8" />
		<title></title>
		<style type="text/css">
			html,body{
				width: 100%;
				height: 100%;
			}
			*{
				padding: 0;
				margin: 0;
				box-sizing:border-box;
			}
			li{
				list-style: none;
				width: 33.3%;
				height: 100%;
				float: left;
				overflow: hidden;
			}
			ul:nth-child(1){
				width: 100%;
				height: 15%;
			}
			ul:nth-child(2){
				width: 100%;
				height: 40%;
			}
			ul:nth-child(3){
				width: 100%;
				height: 45%;
			}
		</style>
		<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
	</head>
	<body>
		<ul>
			<li></li>
			<li>
				<table style="padding-top: 10px;padding-left: 80px;">
					<tr>
						<td ><input class="easyui-combobox" id="timeType"   style="width:50px;padding-top: -2px !important;" value="3" />
						<span id="yearFlg" style="display: none"><input id="time1" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:queryDay})"  type="text" style="width:95px !important;height:23px;" /></span>
						<span id="monthFlg" style="display: none"><input id="time2" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M',onpicked:queryDay})" style="width:110px !important;height:23px;" /></span>
						<span id="dayFlg"><input id="time3" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:queryDay})" style="width:110px !important;height:23px;"/></span>
						</td>
					</tr>
					<tr style="height: 50px;">
						<td >&nbsp;&nbsp;&nbsp;合计：<span id="totalSum" ></span></td>
					</tr>
				</table>
			</li>
			<li></li>
		</ul>
		<ul>
			<li><div id="outOrInBar" style="width: 100%;height: 100%"></div> </li>
			<li><div  id='deptAxis' style="width: 100%;padding-left: 20px;height: 100%"></div></li>
			<li><div  id='ratioAxis' style="width: 100%;padding-left: 20px;height: 100%"></div></li>
		</ul>
		<ul>
			<li><div id="opTypeBar" style="width: 100%;height: 100%" ></div></li>
			<li><div  id='docAxis' style="width: 100%;padding-left: 20px;height: 100%"></div></li>
			<li><div  id='yoyAxis' style="width: 100%;padding-left: 20px;height: 100%"></div></li>
		</ul>
	</body>
</html>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
var todayTime="${currDate}";
$(function(){
	//格式化查询日期
	$("#time3").val(/\d{4}-\d{1,2}-\d{1,2}/g.exec(todayTime));
	//日期下拉框初始化
	$('#timeType').combobox({
		 data:[{    
	         "id":1,    
	         "name":"年"
	     },{    
	         "id":2,    
	         "name":"月"   
	     },{    
	         "id":3,    
	         "name":"日"   
	     }],
		 valueField: 'id',    
	     textField: 'name',
	     editable:false,
	     onSelect:function(node){
	    	 if(node.id==1){
	    		//按天查询
	    		 $("#time1").val(/\d{4}/g.exec(todayTime));
	    		 $("#monthFlg").hide();
	    		 $("#dayFlg").hide();
	    		 $("#yearFlg").show();
	    		 queryDay();
	    	 }else if(node.id==2){
	    		//按月查询
	    		 $("#time2").val(/\d{4}-\d{1,2}/g.exec(todayTime));
	    		 $("#yearFlg").hide();
	    		 $("#dayFlg").hide();
	    		 $("#monthFlg").show();
	    		 queryDay();
	    	 }else{
	    		//按年查询
	    		 $("#time3").val(/\d{4}-\d{1,2}-\d{1,2}/g.exec(todayTime));
	    		 $("#yearFlg").hide();
	    		 $("#monthFlg").hide();
	    		 $("#dayFlg").show();
	    		 queryDay();
	    	 }
	     }
	});
	queryDay();
})
//格式化金额
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
//查询方法
function queryDay(){
	var timeType= $("#timeType").combobox('getValue');
	var searchTime="";
	if(timeType=="1"){
		searchTime=$("#time1").val();
	}else if(timeType=="2"){
		searchTime=$("#time2").val();
	}else{
		searchTime=$("#time3").val();
	}
    $.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationIncome/queryOperationNums.action",
        data: {searchTime:searchTime,dateSign:timeType},
        dataType: "json",
        success: function (data) {
        	var sums=data.count;
        	if(sums>10000){
        		$("#totalSum").text(fmoney(data.count/10000,2)+"万元");
        	}else{
        		$("#totalSum").text(data.count+"元");
        	}
        	 
             //门诊住院
        	 var mzList=data.sum;
             var arrNums = [];  
             if(mzList.length>0){
            	 for (var i = 0; i < mzList.length; i++) {
            		 arrNums.push({value: mzList[i].totalAmount,name:mzList[i].name});
                 }
            	setData1(arrNums);
 			}else{
 				 $('#outOrInBar').html('&nbsp;&nbsp;暂无数据');
 			} 
          
        }
    });
    //手术分类
    $.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationIncome/queryOperationOpType.action",
        data: {searchTime:searchTime,dateSign:timeType},
        dataType: "json",
        success: function (data) {
             //门诊住院
        	 var mzList=data;
             var arrNums = [];  
             if(mzList.length>0){
            	 for (var i = 0; i < mzList.length; i++) {
            		 arrNums.push({value: mzList[i].totalAmount,name:mzList[i].name});
                 }
            	setData2(arrNums);
 			}else{
 				 $('#opTypeBar').html('&nbsp;&nbsp;暂无数据');
 			} 
          
        }
    });
    //科室前五
    $.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationIncome/queryOperationTopFiveDept.action",
        data: {searchTime:searchTime,dateSign:timeType},
        dataType: "json",
        success: function (data) {
        	var list=data;
	     	var deptName=[];    //类别数组（实际用来盛放X轴坐标值）
            var nums=[];
        	if(list.length>0){
        		for(var i=0;i<list.length;i++){       
             	   deptName.push(list[i].name);    
                 }
                for(var i=0;i<list.length;i++){       
                    nums.push(list[i].totalAmount);    
                  }
 				setData3(deptName,nums);
			} else {
				$('#deptAxis').html('&nbsp;暂无数据');
			}
          
        }
    });
    //同比
    if(timeType!="1"){
    	$.ajax({
            type: "post",
            url: "<%=basePath %>statistics/operationIncome/queryYoyCount.action",
            data: {searchTime:searchTime,dateSign:timeType},
            dataType: "json",
            success: function (data) {
            	var list=data;
    	     	var yoyName=[];    //类别数组（实际用来盛放X轴坐标值）
                var nums=[];
            	if(list.length>0){
            		for(var i=0;i<list.length;i++){       
            			yoyName.push(list[i].name);    
                     }
                    for(var i=0;i<list.length;i++){       
                        nums.push(list[i].totalAmount);    
                      }
     				setData6(yoyName,nums);
    			} else {
    				$('#yoyAxis').html('&nbsp;暂无数据');
    			}
              
            }
        });
    }else{
    	$('#yoyAxis').html('&nbsp;');
    }
  //医生前五
    $.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationIncome/queryOperationTopFiveDoc.action",
        data: {searchTime:searchTime,dateSign:timeType},
        dataType: "json",
        success: function (data) {
        	var list=data;
	     	var docName=[];    //类别数组（实际用来盛放X轴坐标值）
            var nums=[];
        	if(list.length>0){
        		for(var i=0;i<list.length;i++){       
        			docName.push(list[i].name);    
                 }
                for(var i=0;i<list.length;i++){       
                    nums.push(list[i].totalAmount);    
                  }
 				setData4(docName,nums);
			} else {
				$('#docAxis').html('&nbsp;暂无数据');
			}
          
        }
    });
  //环比
    $.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationIncome/queryRatioCount.action",
        data: {searchTime:searchTime,dateSign:timeType},
        dataType: "json",
        success: function (data) {
        	var list=data;
	     	var ratioName=[];    //类别数组（实际用来盛放X轴坐标值）
            var nums=[];
        	if(list.length>0){
        		for(var i=0;i<list.length;i++){       
        			ratioName.push(list[i].name);    
                 }
                for(var i=0;i<list.length;i++){       
                    nums.push(list[i].totalAmount);    
                  }
 				setData5(ratioName,nums);
			} else {
				$('#ratioAxis').html('&nbsp;暂无数据');
			}
          
        }
    });
    
}
//门诊住院饼图
function setData1(arrNums){
    // 路径配置
    require.config({
        paths: {
            echarts: '${pageContext.request.contextPath}/javascript/echarts'
        }
    });
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
        	var myChart1 = echarts.init(document.getElementById('outOrInBar'));
        	outOrInBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
        		        orient : 'vertical',
        		        x : 'left',
        		        data:['门诊','住院']
        		    },
        		    toolbox: {
        		        show : true,
        		        feature : {
        		            mark : {show: true},
        		            dataView : {show: true, readOnly: false},
        		            magicType : {
        		                show: true, 
        		                type: ['pie', 'funnel'],
        		                option: {
        		                    funnel: {
        		                        x: '25%',
        		                        width: '50%',
        		                        funnelAlign: 'left',
        		                        max: 1548
        		                    }
        		                }
        		            },
        		            restore : {show: true},
        		        }
        		    },
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '50%',
        		            center: ['50%', '50%'],
        		            data:arrNums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        		    color:['#FF9190', '#79C2F5'],  
        		};
        	  myChart1.setOption(outOrInBar); 
        	}
    );
}
//手术分类饼图
function setData2(nums){
    // 路径配置
    require.config({
        paths: {
            echarts: '${pageContext.request.contextPath}/javascript/echarts'
        }
    });
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
        	var myChart2 = echarts.init(document.getElementById('opTypeBar'));
        	opTypeBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
        		        orient : 'vertical',
        		        x : 'left',
        		        data:['普通','急诊','择期','感染']
        		    },
        		    toolbox: {
        		        show : true,
        		        feature : {
        		            mark : {show: true},
        		            dataView : {show: true, readOnly: false},
        		            magicType : {
        		                show: true, 
        		                type: ['pie', 'funnel'],
        		                option: {
        		                    funnel: {
        		                        x: '25%',
        		                        width: '50%',
        		                        funnelAlign: 'left',
        		                        max: 1548
        		                    }
        		                }
        		            },
        		            restore : {show: true},
        		        }
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '50%',
        		            center: ['50%', '50%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ['#FDB12A','#FF9190', '#79C2F5', '#A3E874']
        		};
        	  
        	  
        	  myChart2.setOption(opTypeBar); 
        	}
    );
}
//科室前五
function setData3(deptName,nums){
	 // 路径配置
    require.config({
        paths: {
            echarts: '${pageContext.request.contextPath}/javascript/echarts'
        }
    });
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
        	var myChart3 = echarts.init(document.getElementById('deptAxis')); 
		      var deptAxis = {
		    			title: {
		    				text: '科室收入TOP5',
		    				x: 'center',
		    				y: "top"
		    			},
		    			tooltip: {
		    				trigger: 'axis'
		    			},
		    			calculable : true,
		    			xAxis: [{
		    				name:'科室',
		    				type : 'category',
		    	            data : deptName,
		    	            axisLabel:{
		    	                 //X轴刻度配置
		    	                 interval:0,
		    	                 rotate:-30//-30度角倾斜显示
		    	            }
		    			}],
		    			grid: { // 控制图的大小，调整下面这些值就可以，
		    	             x: 85,
		    	             x2: 100,
		    	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
		    	        },
		    			yAxis: [{
		    				name:'收入(元)',
		    				type: 'value'
		    			}],label:{
		    		    	normal:{
		    		    		show: true,
		    		    		position: 'top'
		    		    		}},
		    			series: [{
		    					name: '科室收入TOP5',
		    					type: 'bar',
		    					barWidth : 20,//柱图宽度
		    			        barMaxWidth:25,//最大宽度,
		    					data: nums,
		    					itemStyle: {
		    					    normal: {
		    					    	color:"#79C2F5"
		    					    }
		    					}
		    				}
		    			],
		    		};
        	  
        	  // 为echarts对象加载数据 
        	  myChart3.setOption(deptAxis); 
        	}
    );
}
//医生前五
function setData4(docName,nums){
	 // 路径配置
   require.config({
       paths: {
           echarts: '${pageContext.request.contextPath}/javascript/echarts'
       }
   });
   // 使用
   require(
       [
           'echarts',
           'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
       ],
       function (ec) {
       	var myChart4 = echarts.init(document.getElementById('docAxis')); 
		      var docAxis = {
		    			title: {
		    				text: '医生收入TOP5',
		    				x: 'center',
		    				y: "top"
		    			},
		    			tooltip: {
		    				trigger: 'axis'
		    			},
		    			calculable : true,
		    			xAxis: [{
		    				name:'医生',
		    				type : 'category',
		    	            data : docName,
		    	            axisLabel:{
		    	                 //X轴刻度配置
		    	                 interval:0,
		    	                 rotate:-30//-30度角倾斜显示
		    	            }
		    			}],
		    			grid: { // 控制图的大小，调整下面这些值就可以，
		    	             x: 85,
		    	             x2: 100,
		    	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
		    	        },
		    			yAxis: [{
		    				name:'收入(元)',
		    				type: 'value'
		    			}],label:{
		    		    	normal:{
		    		    		show: true,
		    		    		position: 'top',
		    		    		}},
		    			series: [{
		    					name: '医生收入TOP5',
		    					type: 'bar',
		    					barWidth : 20,//柱图宽度
		    			        barMaxWidth:25,//最大宽度,
		    					data: nums,
		    					itemStyle: {
		    					    normal: {
		    					    	color:"#79C2F5"
		    					    }
		    					}
		    				}
		    			],
		    		};
       	  
       	  // 为echarts对象加载数据 
       	  myChart4.setOption(docAxis); 
       	}
   );
}
//环比
function setData5(ratioName,nums){
	 // 路径配置
  require.config({
      paths: {
          echarts: '${pageContext.request.contextPath}/javascript/echarts'
      }
  });
  // 使用
  require(
      [
          'echarts',
          'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
      ],
      function (ec) {
      	var myChart5 = echarts.init(document.getElementById('ratioAxis')); 
      	var ratioAxis = {
      			title: {
      				text: '手术收入环比',
      				x: 'center',
      				y: "top"
      			},
      			tooltip: {
      				trigger: 'axis'
      			},
      			xAxis: [{
      				name:'日期',
      				type: 'category',
      				axisLabel:{
      	                //X轴刻度配置
      	                interval:0,
      	                rotate:-30//-30度角倾斜显示
      	           },
      				data: ratioName
      			}],
      			 grid: { // 控制图的大小，调整下面这些值就可以，
      	             x: 85,
      	             x2: 100,
      	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
      	         },
      			yAxis: [{
      				name:'收入(元)',
      				type: 'value'
      			}],
      			series: [{
      					name: '手术收入环比',
      					type: 'bar',
      					barWidth : 20,//柱图宽度
      			        barMaxWidth:25,//最大宽度,
      					data: nums,
      					itemStyle: {
      					    normal: {
      					    	color:"#79C2F5",
      					    	label: {
                                    show: true,
                                    position: 'top',
                                }
      					    }
      					  	
      					}
      				},
      				 {
      	                name:'折线',
      	                type:'line',
      	                itemStyle : {  /*设置折线颜色*/
      	                    normal : {
      	                         color:'#FF9190' 
      	                    }
      	                },
      	                data:nums
      	            }
      			],
      		}
      	  // 为echarts对象加载数据 
      	  myChart5.setOption(ratioAxis); 
      	}
  );
}
//同比
function setData6(yoyName,nums){
	 // 路径配置
  require.config({
      paths: {
          echarts: '${pageContext.request.contextPath}/javascript/echarts'
      }
  });
  // 使用
  require(
      [
          'echarts',
          'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
      ],
      function (ec) {
      	var myChart6 = echarts.init(document.getElementById('yoyAxis')); 
      	var yoyAxis = {
      			title: {
      				text: '手术收入同比',
      				x: 'center',
      				y: "top"
      			},
      			tooltip: {
      				trigger: 'axis'
      			},
      			xAxis: [{
      				name:'日期',
      				type: 'category',
      				axisLabel:{
      	                //X轴刻度配置
      	                interval:0,
      	                rotate:-30//-30度角倾斜显示
      	           },
      				data: yoyName
      			}],
      			yAxis: [{
      				name:'收入(元)',
      				type: 'value'
      			}],
      			 grid: { // 控制图的大小，调整下面这些值就可以，
      	             x: 85,
      	             x2: 100,
      	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
      	         },
      			series: [{
      					name: '手术收入同比',
      					type: 'bar',
      					barWidth : 20,//柱图宽度
      			        barMaxWidth:25,//最大宽度,
      					data: nums,
      					itemStyle: {
      					    normal: {
      					    	color:"#79C2F5",
      					    	label: {
                                    show: true,
                                    position: 'top',
                                }
      					    }
      					  	
      					}
      				},
      				 {
      	                name:'折线',
      	                type:'line',
      	                itemStyle : {  /*设置折线颜色*/
      	                    normal : {
      	                         color:'#FF9190' 
      	                    }
      	                },
      	                data:nums
      	            }
      			],
      		}
      	  
      	  // 为echarts对象加载数据 
      	  myChart6.setOption(yoyAxis); 
      	}
  );
}
</script>
	
