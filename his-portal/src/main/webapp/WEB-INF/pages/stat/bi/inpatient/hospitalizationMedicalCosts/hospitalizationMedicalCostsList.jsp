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
<script type="text/javascript">
	$(function(){
		 var d = new Date(); 
			var yyyy = d.getFullYear()+''; 
		 $('#tableYearStart').numberbox('setValue',yyyy);
		    
		$('#dept').combobox({
			url:'<%=basePath%>statistics/biHospitalizationMedicalCosts/queryDeptWorkload.action',
			valueField: 'deptCode',    
		    textField: 'deptName',  
		    select:'1',
			multiple:true,
			onSelect:function(data){
			},
			keyHandler:{
				enter: function(e){
					$('#diaInpatient').window('open');
				}
			}
		});
		$('#dept').combobox('select','1');
		
		$("input",$("#tableYearStart").next("span")).blur(function() {
			if($("#tableYearStart").textbox('getValue')){
				  var year=$('#tableYearStart').textbox('getValue');
				  var reg = new RegExp("^([1-9][0-9]{3})$");//四位整数  
			    	if(!reg.test(year)){
			    		$('#tableYearStart').textbox('setValue','');
			    		$.messager.alert('警告','年度只能为非0开头的4位有效整数！！');  
			    		 $("#tableYearStart").next("span").children().first().focus();
			    	} 
				}
		
		} )
	});
	function queryList(){
		var nameArray=$('#dept').combobox('getValues');
		var nameString='';
 		var columnsArray=new Array();
 		var years=$('#tableYearStart').numberbox('getValue');
		var timeone;
		var stringfield2='';
		var num=0;//for循环的次数，即时间维度的起始截止时间差
		if(nameArray==''){
			$.messager.alert('提示', '请选择科室');
			return;
		}
		if(!years){
			$.messager.alert('提示', '请输入查询年度');
			return;
		}else{
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
			stringfield2 +=	'{field:"'+'deptname'+'",title:'+'\'科室\',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'totcost'+'",title:'+'\'总金额(元)\',width:'+'\'100px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'xy'+'",title:'+'\'西药费(元) \',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'xypro'+'",title:'+'\'西药费金额占比 \',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'zchengy'+'",title:'+'\'中成药(元) \',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'zchengypro'+'",title:'+'\'中成药金额占比 \',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'zcaoy'+'",title:'+'\'中草药(元) \',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'zcaoypro'+'",title:'+'\'中草药金额占比 \',width:'+'\'150px\',align:'+'\'center\'},';
			stringfield2 +=	'{field:"'+'coun'+'",title:'+'\'人次\',width:'+'\'100px\',align:'+'\'center\'},';
			stringfield2 += '{field:"'+'totAvg'+'",title:'+'\'平均每次缴费 \',width:'+'\'150px\',align:'+'\'center\'}';
			stringfield2='['+stringfield2+']';
			stringfield2 =eval(stringfield2);
			columnsArray.push(stringfield2);
		}
		
		$('#table1').datagrid({
			columns:columnsArray,
			url:'<%=basePath%>statistics/biHospitalizationMedicalCosts/querytDatagrid.action',
			queryParams:{'timeone':years,'nameString':nameString}
		});
		
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true" onload="queryList()">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%;height: 70px;padding:8px;">
				科室：<input class="easyui-combobox" id="dept" style="margin:0px 30px 0px 0px;">
				年度：<input class="easyui-numberbox" id="tableYearStart" style="margin:0px 30px 0px 50px;">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 50px" >查&nbsp;询&nbsp;</a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;">
				<table id="table1" data-options="fit:true"></table>
			</div>
	</div>
		
		
		
</body>
	<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
	<script type="text/javascript">
	function echarts(){
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
                var myChart = ec.init(document.getElementById('main')); 
                var categories = new Array;  
                var values =new Array; 
                var old =new Array; 
                var mom =new Array; 
                $.ajaxSettings.async = false; 
                
                // 加载数据  
                $.getJSON('${pageContext.request.contextPath}/statistics/biHospitalizationInformation/queryStatDate.action', function (json) {  
                	categories = json.categories;  
                    values = json.values;  
                });  
                
                var option = {
              		 title : {
              		        text: '今年同比去年住院情况'
              		    },
                    tooltip: {
                        show: true
                    },
                    
                    legend: {  
                        x: 'center',  
                        data: ['同比','当前数据','环比']
      
                    },  
                    toolbox: {
				        show : true,
				        feature : {
				            mark : {show: true},
				            dataView : {show: true, readOnly: false},
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
                            type : 'value'
                        }
                    ],
                    series : [
                        {
                            "name":"同比",
                            "type":"bar",
                            "data":old
                        },
                        {
                            "name":"当前数据",
                            "type":"bar",
                            "data":values
                        },
                        {
                            "name":"环比",
                            "type":"bar",
                            "data":mom
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