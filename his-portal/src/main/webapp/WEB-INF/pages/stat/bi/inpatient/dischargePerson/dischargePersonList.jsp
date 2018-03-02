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
<script language="javascript" type="text/javascript" src="<%=basePath%>datePicker/WdatePicker.js"></script>
<script type="text/javascript">
	
	$(function(){
		$('#dept').combobox({
			url:'<%=basePath%>statistics/dischargePerson/queryDeptWorkload.action',
			valueField: 'deptCode',    
		    textField: 'deptName',  
		    multiple:true,
			select:'1',
			onSelect:function(data){
			},
			keyHandler:{
				enter: function(e){
					$('#diaInpatient').window('open');
				}
			}
		});
		$('#dept').combobox('select','1');
		 $('#quarter').combobox({
		        data:[{    
		            "id":1,    
		            "name":"第一季度"
		        },{    
		            "id":2,    
		            "name":"第二季度"   
		        },{    
		            "id":3,    
		            "name":"第三季度"   
		        },{    
		            "id":4,    
		            "name":"第四季度"   
		        }],
			    valueField: 'id',    
		        textField: 'name',
		        editable:false,
		        onSelect:function(node){}
		        	
			});
			
		
		$("#year").blur(function() {
			if($('#year').val()){
				var year=$('#year').val();
				  var reg = new RegExp("^([1-9][0-9]{3})$");//四位整数  
			    	if(!reg.test(year)){
			    		$('#year').val('');
			    		$.messager.alert('警告','年度只能为非0开头的4位有效整数！！');  
			    		 $("#year").next("span").children().first().focus();
			    	} 
				}
		
		} )
		 
		$('#month').focus(function (){
			  $("#msgTd").html("&nbsp;");
			 $('#quarter').combobox('setValue','');
	        	if($("#year").val()){
	        		if($("#month").val()){
			        	$('#day').combobox({    
							url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth="+$('#year').val()+"-"+$("#month").val(),
						    valueField:'id',    
						    textField:'name',
						    editable:false,
						    onSelect:function(node) {
						    	if($("#year").val()&&$("#month").val()){
						    		  $("#msgTd").html("&nbsp;");
						    	}else{
						    		 $("#day").combobox("setValue",null);
					        		 $("#msgTd").html("&nbsp;");
					        		 $.messager.alert('警告','月份不能为空！');   
						    	}
						  }
						});
	        		}
	        	}else{
	        		$.messager.alert('警告','年度不能为空！');   
	        	}
		  })
		  
		  $('#quarter').combobox().next('span').find('input').focus(function (){
			  var year=$('#year').val();
			  if(!year){
				  $.messager.alert('警告','年度不能为空！');   
			  }
			  $('#month').val('');
			  $('#day').combobox('setValue','');
		  })
		  
		   $('#day').combobox().next('span').find('input').focus(function (){
			   var month=$('#month').val();
				  if(!month){
					  $.messager.alert('警告','月份不能为空！');   
				  }
			   $('#quarter').combobox('setValue','');
		  })
		  
		  

		var stringfield='';
		var stringfield2='';
		var columnsArray=new Array();
		/* stringfield='{field:'+'\'name\',width:'+'\'300px\',align:'+'\'center\',rowspan:2}'; */
		if(stringfield!=''&&stringfield!=null){
			stringfield =stringfield+',';
		}	
		var oDate =new Date();
		var year=oDate.getFullYear();
		var month=oDate.getMonth()+1;
		var day=oDate.getDate();
		var time=year+'.'+month+'.'+day;
		stringfield += '{field:"'+1+'",title:"'+time+'",width:'+'\'350px\',align:'+'\'center\',colspan:4}';
		stringfield2 += '{field:"'+'deptName'+'",title:'+'\'科室\',width:'+'\'260px\',align:'+'\'center\'},{field:"'+'hospitalizationTime'+'",title:'+'\'住院人次\',width:'+'\'100px\',align:'+'\'center\'},{field:"'+'dischargePerson'+'",title:'+'\'出院人次\',width:'+'\'100px\',align:'+'\'center\'},{field:"'+'percentage'+'",title:'+'\'百分比\',width:'+'\'150px\',align:'+'\'center\'}';
			stringfield='['+stringfield+']';
			stringfield2='['+stringfield2+']';
			stringfield =eval(stringfield);
			stringfield2 =eval(stringfield2);
			columnsArray.push(stringfield);
			columnsArray.push(stringfield2);
		
		
		$('#table1').datagrid({
			columns:columnsArray,
			url:'<%=basePath%>statistics/dischargePerson/loadPersonList.action',
			onLoadSuccess:compute
		});
	
		bar();
		axis();
	});
	
	  function compute() {//计算函数
          var rows = $('#table1').datagrid('getRows')//获取当前的数据行
          var ptotal = 0//计算listprice的总和
          var utotal=0;//统计unitcost的总和
          var sum=0.00;
          for (var i = 0; i < rows.length; i++) {
              ptotal += rows[i]['hospitalizationTime'];
              utotal += rows[i]['dischargePerson'];
          }
          if(ptotal!=0){
        	  sum=(utotal.toFixed(2)/ptotal.toFixed(2))*100;
        	  sum=sum.toFixed(2)
          }
          //新增一行显示统计信息
          $('#table1').datagrid('appendRow', { deptName: '<b>统计：</b>', hospitalizationTime: ptotal, dischargePerson: utotal,percentage:sum+"%" });
      }
	function queryList(){
 		var nameArray=$('#dept').combobox('getValues');
 		var deptCode='';
 		var columnsArray=new Array();
		var years=$('#year').val();
		var quarters=$('#quarter').combobox('getValue');
		var months=$('#month').val();
		var days=$('#day').combobox('getValue');
		var nameString='';
		var stringfield='';
		var stringfield2='';
		
		var time='';
		if(years!=null&& years!='' ){
			time=years+'年';
		}
		if(quarters!=null&& quarters!=''){
			time=time+'第'+quarters+'季度';
		}
		if(months!=null&& months!=''){
			time=time+months+'月';
		}
		if(days!=null&& days!=''){
			time=time+days+'日';
		}
		
		stringfield += '{field:"'+1+'",title:"'+time+'",width:'+'\'350px\',align:'+'\'center\',colspan:4}';
		stringfield2 += '{field:"'+'deptName'+'",title:'+'\'科室\',width:'+'\'260px\',align:'+'\'center\'},{field:"'+'hospitalizationTime'+'",title:'+'\'住院人次\',width:'+'\'100px\',align:'+'\'center\'},{field:"'+'dischargePerson'+'",title:'+'\'出院人次\',width:'+'\'100px\',align:'+'\'center\'},{field:"'+'percentage'+'",title:'+'\'百分比\',width:'+'\'150px\',align:'+'\'center\'}';
			
			stringfield='['+stringfield+']';
			stringfield2='['+stringfield2+']';
			stringfield =eval(stringfield);
			stringfield2 =eval(stringfield2);
			columnsArray.push(stringfield);
			columnsArray.push(stringfield2);
		
			if(nameArray==''){
				$.messager.alert('提示', '请选择科室');
				return;
			}
			if(nameArray){
				//循环 将部门ID转换格式成String
				for(var i=0;i<nameArray.length;i++){
					if(nameString!=null&&nameString!=''){
						nameString +='\',\'';
					}else{
						nameString+='\'';
					}
					nameString +=nameArray[i];
					if(i==nameArray.length-1){
						nameString +='\'';
					}
				}	
			}
		$('#table1').datagrid({
			columns:columnsArray,
			url:'<%=basePath%>statistics/dischargePerson/queryDischargePersonList.action',
			queryParams:{'years':years,'quarters':quarters,'months':months,'days':days,'deptCode':nameString},
			onLoadSuccess:compute
		});
		
		bar(years,quarters,months,days,nameString);
		axis(years,quarters,months,days,nameString);
		
	}
	
</script>
</head>
<body  class="easyui-layout" style="width: 100%;">
<div data-options="region:'west',border:false" style="width: 34%;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 30%;height: 70px;padding:8px;" class="dischargePersonList">
					科室：<input class="easyui-combobox" id="dept" style="margin:15px 15px 0px 0px;">
				年度：<input style='width:145px;' name='year' id="year" class="Wdate" onFocus="var year=$dp.$('year');WdatePicker({dateFmt:'yyyy',onpicked:function(){year.focus();},maxDate:'%y',errDealMode:2})"/>
				季度：<input class="easyui-combobox" id="quarter" style="margin:15px 15px 0px 15x;"><br>
				月：<input style='width:145px;' name='month' id="month" class="Wdate" type="text"  onFocus="var month=$dp.$('month');WdatePicker({dateFmt:'M',onpicked:function(){month.focus();},maxDate:'#F{$dp.$D(\'month\')}',isShowToday:false})"/>
				&nbsp&nbsp日：<input class="easyui-combobox" id="day" style="margin:15px 15px 0px 15px;">&nbsp&nbsp&nbsp
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:5px 0px 0px 15px" >查&nbsp;询&nbsp;</a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;">
				<table id="table1" data-options="fit:true"></table>
			</div>
		</div>
	</div>

</div>
</body>

	
	<div data-options="region:'east',border:false" style="height:50%;width:60%;margin-top:10px;">
		<div id="main"  style="height:53%;width:100%;margin-top:5px;" ></div>
		<div  id="axis" style="width:100%;height:50%;margin-top:20px;" ></div>
	</div>
	
</body>
	<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
	<script type="text/javascript">
		function bar(years,quarters,months,days,deptCode){
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
	                // 基于准备好的dom，初始化echarts图表
	                var myChart = ec.init(document.getElementById('main'));
	                $.ajaxSettings.async = false; 
	                var categories = [];  
	                var values = []; 
	              	var year=[];
	              	var month=[];
	 			 // 加载数据  
	             $.getJSON('${pageContext.request.contextPath}/statistics/dischargePerson/dataAccessload.action', { years: years, quarters:quarters,months: months, days: days,deptCode: deptCode}, function (json) {  
	             	categories = json.categories;  
	                 values = json.values; 
	                 year = json.years; 
	                 month = json.months; 
	             });  
	                var option = {
	                    tooltip: {
	                        show: true
	                    },
	                    legend: {  
	                    	 x: 'left',  
	                        data: ['出院人次']
	      
	                    },  
	                    toolbox: {
					        show : true,
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					          //  magicType : {show: true, type: ['line', 'bar']},
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					    calculable : true,
	                    xAxis : [
	                        {
	                            type : 'category',
	                            data : categories
	                        }
	                    ],
	                    yAxis : [
	                        {
	                            type : 'value',
	                            	splitArea: {show: true}
	                        }
	                    ],
	                    series : [
	                              {  
	                                  name: '同比',  
	                                  type: 'bar',  
	                                  data: year  
	                                 
	                               
	                              },  
	                              {  
	                                  name: '今年',  
	                                  type: 'bar',  
	                                  data: values
	                                 
	                                 
	                              }  ,
	                              {  
	                                  name: '环比',  
	                                  type: 'bar',  
	                                  data: month 
	                              }  
	                    ]
	                };
	                // 为echarts对象加载数据 
	                myChart.setOption(option); 
	               
	            }
	        );
		}
		  
		</script>
		
		<script type="text/javascript">
		function axis(years,quarters,months,days,deptCode){
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
	                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
	            ],
	            function (ec) {
	                // 基于准备好的dom，初始化echarts图表
	                var myChart = ec.init(document.getElementById('axis')); 
	                var categories = [];  
	                var values = []; 
	                $.ajaxSettings.async = false; 
	                
	                // 加载数据  
	                $.getJSON('${pageContext.request.contextPath}/statistics/dischargePerson/axisload.action',{ years: years, quarters:quarters,months: months, days: days,deptCode: deptCode}, function (json) {  
	                	categories = json.categories;  
	                    values = json.values; 
	                });  
	                
	                var option = {
	                    tooltip: {
	                        show: true,
	                        trigger: 'axis'  
	                    },
	                    legend: {  
	                        x: 'left',  
	                        data: ['出院人次']
	      
	                    },  
	                    toolbox: {
					        show : true,
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					          //  magicType : {show: true, type: ['line', 'bar']},
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					    calculable : true,
	                    xAxis : [
	                        {
	                            type : 'category',
	                            data : categories
	                        }
	                    ],
	                    yAxis : [
	                        {
	                            type : 'value',
	                        }
	                    ],
	                    series : [
	                              {  
	                                  name: '出院人次',  
	                                  type: 'line',  
	                                  data: values
	                                 
	                                 
	                              } 
	                    ]
	                };
	                // 为echarts对象加载数据 
	                myChart.setOption(option); 
	               
	            }
	        );
		}
		   
		</script>
		
</html>