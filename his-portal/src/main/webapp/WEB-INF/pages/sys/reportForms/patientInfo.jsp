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
<title>门诊住院情况统计</title>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var empFlag=true;
	var sexMap=new Map();
	 var menuAlias = '${menuAlias}';
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

		var ec = null;
			$(function() {
				//科室下拉框
				 $.extend($.fn.datagrid.methods, {
				        autoMergeCells: function (jq, fields) {
				            return jq.each(function () {
				                var target = $(this);
				                if (!fields) {
				                    fields = target.datagrid("getColumnFields");
				                }
				                var rows = target.datagrid("getRows");
				                var i = 0,
				                j = 0,
				                temp = {};
				                for (i; i < rows.length; i++) {
				                    var row = rows[i];
				                    j = 0;
				                    for (j; j < fields.length; j++) {
				                        var field = fields[j];
				                        var tf = temp[field];
				                        if (!tf) {
				                            tf = temp[field] = {};
				                            tf[row[field]] = [i];
				                        } else {
				                            var tfv = tf[row[field]];
				                            if (tfv) {
				                                tfv.push(i);
				                            } else {
				                                tfv = tf[row[field]] = [i];
				                            }
				                        }
				                    }
				                }
				                $.each(temp, function (field, colunm) {
				                    $.each(colunm, function () {
				                        var group = this;
				                        if (group.length > 1) {
				                            var before,
				                            after,
				                            megerIndex = group[0];
				                            for (var i = 0; i < group.length; i++) {
				                                before = group[i];
				                                after = group[i + 1];
				                                if (after && (after - before) == 1) {
				                                    continue;
				                                }
				                                var rowspan = before - megerIndex + 1;
				                                if (rowspan > 1) {
				                                    target.datagrid('mergeCells', {
				                                        index: megerIndex,
				                                        field: field,
				                                        rowspan: rowspan
				                                    });
				                                }
				                                if (after && (after - before) != 1) {
				                                    megerIndex = after;
				                                }
				                            }
				                        }
				                    });
				                });
				            });
				        }
				    });
					var topDivH = ($("body").height()/2)-40;
					$("#tableShow")[0].style.height = ($("#topShow").height() - 40)+"px";
					//选择科室
				$(".deptInput").MenuList({
					width :530, //设置宽度，不写默认为530，不要加单位
					height :topDivH, //设置高度，不写默认为400，不要加单位
					dropmenu:"#m2",//弹出层id，必须要写
					isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
					para:"C,I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
					firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=",
					relativeInput:".doctorInput",	//与其级联的文本框，必须要写
					relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
				});
				var Stime = GetDateStr(0);
				var Etime = GetDateStr(1);
				var dept = $('#ksnew').getMenuIds();
				if($('h2','.xmenu').attr('class')){
					if(!$('h2','.xmenu').attr('class').indexOf('selected')!=-1||dept==null||dept==""){
						 //当全部按钮没有被选中并且没有选中科室
						 dept = "all"; 
					}
				}
				$('#list').datagrid({
					url:"<%=basePath %>statistics/ReportForms/listPatientQuery.action?menuAlias=${menuAlias}",
					queryParams:{dept:dept,sTime:Stime,eTime:Etime},
					singleSelect:true,
					collapsible:true,
					rownumbers: true,
					onLoadSuccess:function(data){
						ec = data.rows;
						var merges = [{
							index: 20,
							colspan: 9
						}];
						for(var i=0; i<merges.length; i++){
							$(this).datagrid('mergeCells',{
								index: merges[i].index,
								field: 'order',
								colspan: merges[i].colspan
							});
						}
					inntView(null,null,dept);
					}
				});
				$('#ETime').val(GetDateStr(0));
				$('#STime').val(GetDateStr(0));
			});
		
			//渲染处方金额
			function changeCost(value,row,index){
				return value==null?value:value.toFixed(2);
			}
			//渲染年龄
			function ageFormatter(value,row,index){
				if("undefined"!=typeof(value)){
			        var arr=value.split("-");
					var y =arr[0];
					var m =arr[1];
					var d =arr[2];
					var agedate=DateOfBirth(y+"-"+m+"-"+d);
					if(agedate.get("nianling")=="0"){
						return 0;	
					}else{
						return agedate.get("nianling")+agedate.get("ageUnits");	
					}
				}
				return value;
			}
   
        /**
		 * 查询
		 * @author wujiao
		 * @date 2015-08-31
		 * @version 1.0
		 */
		function searchFrom() {
			var Stime = $('#STime').val();
			var Etime = $('#ETime').val();
			if(Stime==null || Stime=="" || Etime==null || Etime==""){
				$.messager.alert("提示","请填写正确的时间范围！");
				return ;
	  		}
			if(Stime&&Etime){
	          if(Stime>Etime){
	            $.messager.alert("提示","开始时间不能大于结束时间！");
	            close_alert();
	            return ;
	          }
	        }
			var dd=new Date(Etime); 
		     dd.setDate(dd.getDate()+1);//获取AddDayCount天后的日期
		     var y = dd.getFullYear();
		     var m = dd.getMonth()+1;//获取当前月份的日期
		     if(Number(m)<10){
		           m = "0"+m;
		         }
		         var d = dd.getDate();
		         if(Number(d)<10){
		           d = "0"+d;
		         }
		     Etime =  y+"-"+m+"-"+d;
			searchFinal(Stime,Etime);
		}
		function searchFinal(Stime,Etime){
			$('#STime').val(Stime);
// 			$('#ETime').val(Etime);
			var dept = $('#ksnew').getMenuIds();
			if(dept==null||dept==""){
				dept = "all"; 
			 }else if("undefined" != typeof($('h2','.xmenu').attr('class'))){
					if($('h2','.xmenu').attr('class').indexOf('selected')==-1){
					 dept = "all"; 
					}
				}
			if(Stime==null || Stime==""){
				Stime="${Stime}";
          	}
          	if(Etime==null || Etime==""){
          		Etime="${Etime}";
          	}
			$('#list').datagrid('reload',{
				sTime: Stime,
				eTime: Etime,
				dept: dept
			});
			inntView(Stime,Etime,dept);
		 }
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
			$('#ETime').val(GetDateStr(0));
		 }
		 //查询前三天
		function searchThree(){
			var Etime = GetDateStr(0);
			var Stime = GetDateStr(-3);
			searchFinal(Stime,Etime);
			$('#ETime').val(GetDateStr(-1));
		}
		//查询前七天
		function searchSeven(){
			var Etime = GetDateStr(0);
			var Stime = GetDateStr(-7);
			searchFinal(Stime,Etime);
			$('#ETime').val(GetDateStr(-1));
		}
		
		
		//查询前15天 zhangkui 2017-04-17
		function searchFifteen(){
			var Stime = GetDateStr(-15);
			var Etime = GetDateStr(0);
			searchFinal(Stime,Etime);
			$('#ETime').val(GetDateStr(-1));
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
            $('#ETime').val(lastDay);
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
			//var Etime = getCurrentMonthLast();
			//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Etime = GetDateStr(1);
			var Stime = getCurrentMonthFirst();
			searchFinal(Stime,Etime);
			$('#ETime').val(GetDateStr(0));
		}
		//查询当年
		function searchYear(){
			//var Etime = new Date().getFullYear()+"-12-31";
			//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Stime = new Date().getFullYear()+"-01-01";
			var Etime = GetDateStr(1);
			searchFinal(Stime,Etime);
			$('#ETime').val(GetDateStr(0));
		}
		
		
		
		
        
		function inntView(Stime,Etime,dept){
			 require.config({
		            paths: {
		                echarts: '${pageContext.request.contextPath}/javascript/echarts'
		            }
		        });
// 		        var arrayName = new Array();
// 		        var arrayValidTot = new Array();
// 		        var arraybookTot = new Array();
// 		        var arrayregTot = new Array();
// 		        var arrayarrTot = new Array();
				
				var arrayName = 0.0;
				var arrayValidTot = 0.0; 
				var arraybookTot = 0.0;
				var arrayregTot = 0.0;
				var arrayarrTot = 0.0;
				
		        var option=null;
		        // 使用
		        require(
		            [
		                'echarts',
		                'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
		            ],
		            function (ecdom) {
		            	var Stime = $('#STime').val();
		    			var Etime = $('#ETime').val();
		    			if(Stime==null || Stime==""){
		    				Stime="${Stime}";
		              	}
		              	if(Etime==null || Etime==""){
		              		Etime="${Etime}";
		              	}
		                // 基于准备好的dom，初始化echarts图表
		                var myChart = ecdom.init(document.getElementById('main')); 
// 		    					for(i=0;i<ec.length;i++){
									var i = ec.length-1;
		    						arrayName =(arrayName+ec[i].medicalExpense).toFixed(2);
		    						arrayValidTot=(arrayValidTot+ec[i].drugCost).toFixed(2);
		    						arraybookTot = (arraybookTot+ec[i].medicalExpensez).toFixed(2);
		    						arrayregTot = (arrayregTot+ec[i].drugCostz).toFixed(2);
		    						arrayarrTot = (arrayarrTot+ec[i].otherExpensesz).toFixed(2);
// 		    						arrayName.push(ec[i].medicalExpense);
// 		    						arrayValidTot.push(ec[i].drugCost);
// 		    						arraybookTot.push(ec[i].medicalExpensez);
// 		    						arrayregTot.push(ec[i].drugCostz);
// 		    						arrayarrTot.push(ec[i].otherExpensesz);
// 		    					}
		    					option = {
		    						    title : {
		    						        text: '患者门诊及住院情况统计',
		    						        subtext: '数据展示',
		    						        x:'center'
		    						    },
		    						    tooltip : {
		    						        trigger: 'item',
		    						        formatter: "{a} <br/>{b}:{c} ({d}%)"
		    						    },
		    						    legend: {
		    						        orient : 'vertical',
		    						        x : 'left',
		    						        data:['门诊医疗费用','门诊药品费用','住院医疗费用',
		    						              '住院药品费用','住院其他费用']
		    						    },
		    						    calculable : true,
		    						    series : [
		    						        {
		    						            name:'费用统计',
		    						            type:'pie',
		    						            radius : '55%',
		    						            center: ['50%', '55%'],
		    						            data:[
		    						                {value:arrayName, name:'门诊医疗费用'},
		    						                {value:arrayValidTot, name:'门诊药品费用'},
		    						                {value:arraybookTot, name:'住院医疗费用'},
		    						                {value:arrayregTot, name:'住院药品费用'},
		    						                {value:arrayarrTot, name:'住院其他费用'}
		    						            ],
			    						        itemStyle:{ 
				    	                            normal:{ 
				    	                                label:{ 
				    	                                   show: true, 
				    	                                   formatter: '{b} : {c} ({d}%)' 
				    	                                }, 
				    	                                labelLine :{show:true}
				    	                            } 
				    	                        },
		    						        }
		    						    ]
		    						}; 
		    					
		    					
		    						    // 为echarts对象加载数据 
		    					 myChart.setOption(option); 
		            }
		        );
		 }
		/**
		 * 重置
		 * @author huzhenguo
		 * @date 2017-03-17
		 * @version 1.0
		 */
		function clears(){
			$('#ksnew').val('');
			$("a[name='menu-confirm-cancel']").click();
			$("a[name='menu-confirm-clear']").click();
			searchOne();
		}
</script>
</head>
<body style="margin: 0px; padding: 0px;"> 
<div id="layouteasy" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width:100%;height:50%;">
	<div id="topShow"  class="easyui-layout" style="height:100%" data-options="fit:true">
<!-- 	<div data-options="region:'north',border:false" style="width:100%;height:40px;padding:5px 5px 0px 5px;"> -->
		<table  style="padding:1px 3px 0px 3px;width:100%;position: relative;z-index:100" title="患者门诊及住院情况统计">
			<tr>
				<tr>
					<!-- 开始时间 --> 
					<td style="width:50px;" align="left">日期:</td>
					<td style="width:110px;">
					<input id="STime" class="Wdate" type="text" name="STime" value="${Stime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
					<input id="ETime" class="Wdate" type="text" name="ETime" value="${Etime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td style="width:55px;" align="right">科室:</td>
	    			<td width =130px class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:120px;" ><input class="ksnew" id="ksnew" style="width:95px" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none; ">
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
										<div class="select-info" style="display:none;">	
											<label class="top-label">已选部门：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>		 
										</div>	
									</div>
								</td>
					<td>
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-top:2px">查询</a>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
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
<!-- 	</div> -->
	<div id="tableShow" style="width:100%;margin-top: 3px" >
		<table id="list" class="easyui-datagrid" style="" data-options="fit:true">
			<thead>
				<tr>
					<th data-options="field:'name',width:140,align:'left',rowspan:2">患者姓名</th>
					<th data-options="field:'sex',width:100,align:'left',rowspan:2, formatter: function(value,row,index){return sexMap.get(value);}">性别</th>
					<th data-options="field:'age',width:140,align:'left',rowspan:2,formatter: ageFormatter">年龄</th>
					<th data-options="field:'address',align:'left',width:140,rowspan:2">地域</th>
					<th data-options="field:'tyepPatient',width:140,align:'left',rowspan:2">患者类型</th>
					<th data-options="field:'tyep',width:140,align:'left',rowspan:2">疾病类型</th>
					<th data-options="field:'dept',width:140,align:'left',rowspan:2">所在科室</th>
					<th data-options="field:'doctor',width:140,align:'left',rowspan:2">责任医生</th>
					<th data-options="field:'menzhen',width:140,align:'center',colspan:3">门诊</th>
					<th data-options="field:'zhuyuan',width:140,align:'center',colspan:5">住院</th>
					<th data-options="field:'totalall',width:140,halign:'center',align:'right',rowspan:2,formatter:changeCost">费用总计</th>
				</tr>
				<tr>
				<th data-options="field:'medicalExpense',align:'right',formatter:changeCost">医疗费用</th>
				<th data-options="field:'drugCost',align:'right',formatter:changeCost">药品费用</th>
				<th data-options="field:'total',align:'right',formatter:changeCost">费用总计</th>
				<th data-options="field:'days',align:'right'">住院天数</th>
				<th data-options="field:'medicalExpensez',align:'right',formatter:changeCost">医疗费用</th>
				<th data-options="field:'drugCostz',align:'right',formatter:changeCost">药品费用</th>
				<th data-options="field:'otherExpensesz',align:'right',formatter:changeCost">其他费用</th>
				<th data-options="field:'totalz',align:'right',formatter:changeCost">费用总计</th>
				</tr>
			</thead>
		</table>
	</div>
	</div>
	</div>
   	<div data-options="region:'center',border:false" id="main" style="height:50%"></div>
    </div>  
	</body>
</html>