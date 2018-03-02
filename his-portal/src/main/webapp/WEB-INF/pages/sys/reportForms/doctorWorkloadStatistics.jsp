﻿﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
		.addList dl:first-child ul {
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix:after{
		    content:"";
		    display:table;
		    height:0;
		    visibility:hidden;
		    clear:both;
		}
		.xmenu dl dd ul{
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
		

</style>
</style>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<title>医生工作量统计报表</title>
 <script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script> 
	    <script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
		<script type="text/javascript">
		var empFlag=true;
		var ec = null;
		var persent = null;
	     var menuAlias = '${menuAlias}';
	     var flag;//全局科室
		$(function() {
// 			$.messager.progress({msg:"查询中,请稍后..."});
			var topDivH = ($("body").height()/2)-40;
			var Stime = GetDateStr(0);
			var Etime = GetDateStr(1);
						//科室
						$.ajax({
							url: "<%=basePath%>baseinfo/department/getDeptMap.action",
							success: function(date) {
								deptMap = date;
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
									//选择科室
										$(".deptInput").MenuList({
											width :530, //设置宽度，不写默认为530，不要加单位
											height :topDivH, //设置高度，不写默认为400，不要加单位
											dropmenu:"#m2",//弹出层id，必须要写
											isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
											para:"C",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
											firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
											relativeInput:".doctorInput",	//与其级联的文本框，必须要写
											relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
										});
									//判断是否有科室权限
										$('#m2 .addList h2 input').click();
										$('a[name=\'menu-confirm\']').click();
										flag=$('#ksnew').getMenuIds();
										$('a[name=\'menu-confirm-clear\']').click();
										if(flag==''||flag==null){
											$("body").setLoading({
												id:"body",
												isImg:false,
												text:"无数据权限"
											});
										}
										console.info(flag);
								$("#topShow").height(topDivH);
								$("#tableShow").height(topDivH);
								$('#list').datagrid({
									url:"<%=basePath %>statistics/ReportForms/listCountquerywrok.action",
									queryParams:{dept:flag,sTime:Stime,eTime:Etime,menuAlias: menuAlias},
									singleSelect:true,
									collapsible:true,
									rownumbers: true,
									border:false,
									fit:true,
									onLoadSuccess:function(data){
										ec = data.rows;
										inntView(flag);
// 										$.messager.progress('close');
									}
								});
				}
			});
		
// 			$("#tableShow")[0].style.height = ($("#topShow").height() - 40)+"px";
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(0));
		});
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
			 if(Stime&&Etime){
			    if(Stime>Etime){
			      $.messager.alert("提示","开始时间不能大于结束时间！");
			      close_alert();
			      return ;
			    }
			  }
			 var dept = $('#ksnew').getMenuIds();
// 			 $('h2','.xmenu').attr('class').indexOf('selected')!=-1||
			 if(dept==null||dept==""){
				 //当全部按钮没有被选中并且没有选中科室
				 dept = flag;
			 }
			 $('#list').datagrid('load',{
				sTime: Stime,
				eTime: Etime,
				menuAlias:menuAlias,
				dept: dept
			});
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
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(0));
		 }
		function searchThree(){
			var Etime = GetDateStr(0);
			var Stime = GetDateStr(-3);
			searchFinal(Stime,Etime);
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(-1));
		}
		function searchSeven(){
			var Etime = GetDateStr(0);
			var Stime = GetDateStr(-7);
			searchFinal(Stime,Etime);
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(-1));
		}
		//查询前15天
		function searchFifteen(){
			var Stime = GetDateStr(-15);
			var Etime = GetDateStr(0);
			searchFinal(Stime,Etime);
			$('#STime').val(Stime);
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
				$('#STime').val(Stime);
				$('#ETime').val(lastDay);

		}
		
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
		function getCurrentMonthFirst(){
			 var date=new Date();
			 date.setDate(1);
			 return date.Format("yyyy-MM-dd");
		}
		function getCurrentMonthLast(){
			 var date=new Date();
			 var currentMonth=date.getMonth();
			 var nextMonth=++currentMonth;
			 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
			 var oneDay=1000*60*60*24;
			 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
		} 
		function searchMonth(){
			//var Etime = getCurrentMonthLast();
			//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Stime = getCurrentMonthFirst();
			var Etime = GetDateStr(1);
			searchFinal(Stime,Etime);
			var Etime = GetDateStr(0);
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(0));
		}
		function searchYear(){
			//var Etime = new Date().getFullYear()+"-12-31";
			//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Etime = GetDateStr(1);
			var Stime = new Date().getFullYear()+"-01-01";
			searchFinal(Stime,Etime);
			var Etime = GetDateStr(0);
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(0));
		}
		
		
		 function inntView(dept){
			 require.config({
		            paths: {
		                echarts: '${pageContext.request.contextPath}/javascript/echarts'
		            }
		        });
		        var arrayName = new Array();
		        var arrayValidTot = new Array();
		        var arraybookTot = new Array();
		        var arrayregTot = new Array();
		        var arrayarrTot = new Array();
		        var option=null;
		        // 使用
		        require(
		            [
		                'echarts',
		                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		            ],
		            function (ecdom) {
		                // 基于准备好的dom，初始化echarts图表
		                var myChart = ecdom.init(document.getElementById('main')); 
						var dNum = 0;
						if(ec.length==0){
    						arrayName.push('');
    						arrayValidTot.push(0);
    						arraybookTot.push(0);
    						arrayregTot.push(0);
    						arrayarrTot.push(0);
    						persent = 100;
						}else{
	    					for(i=0;i<ec.length;i++){
	    						arrayName.push((ec[i].name==null||ec[i].name=='')?'':ec[i].name);
	    						arrayValidTot.push(ec[i].validTot);
	    						arraybookTot.push(ec[i].bookTot);
	    						arrayregTot.push(ec[i].regTot);
	    						arrayarrTot.push(ec[i].arrTot);
	    						dNum++;
	    					}
	    					persent = 10/(dNum<10?10:dNum)*100;
						}
  						    option = {
  								    title : {
  								        text: '医生工作量统计',
  								        subtext: '展示数据'
  								    },
  								    tooltip : {
  								        trigger: 'axis'
  								    },
  								    legend: {
  								        data:['咨询总数','预约总数','挂号总数','就诊总数']
  								    },
	  								dataZoom : {
	  							      show : true,
	  							      realtime : true,
	  							      start : 0,
	  							      end : persent,
	  							      fillerColor : 'rgba(0,102,153,0.2)' 
	  							    },
  								    calculable : true,
  								    xAxis : [
  								        {
  								            type : 'category',
  								            data : arrayName,
  								        }
  								    ],
  								    yAxis : [
  								        {
  								            type : 'value'
  								        }
  								    ],
  								    series : [
  								        {
  								            name:'咨询总数',
  								            type:'bar',
  								            data:arrayValidTot,
	  								        barWidth : 30,
  								            markPoint : {
  								                data : [
  								                    {type : 'max', name: '最大值'},
  								                    {type : 'min', name: '最小值'}
  								                ]
  								            },
  								            markLine : {
  								                data : [
  								                    {type : 'average', name: '平均值'}
  								                ]
  								            }
  								        },
  								        {
  								            name:'预约总数',
  								            type:'bar',
  								            data:arraybookTot,
	  								        barWidth : 30,
  								            markPoint : {
  								                data : [
  								                    {type : 'max', name: '最大值'},
  								                    {type : 'min', name: '最小值'}
  								                ]
  								            },
  								            markLine : {
  								                data : [
  								                    {type : 'average', name : '平均值'}
  								                ]
  								            }
  								        },
  								        {
  								            name:'挂号总数',
  								            type:'bar',
  								            data:arrayregTot,
	  								        barWidth : 30,
  								            markPoint : {
  								                data : [
  								                    {type : 'max', name: '最大值'},
  								                    {type : 'min', name: '最小值'}
  								                ]
  								            },
  								            markLine : {
  								                data : [
  								                    {type : 'average', name: '平均值'}
  								                ]
  								            }
  								        },
  								        {
  								            name:'就诊总数',
  								            type:'bar',
  								            data:arrayarrTot,
	  								        barWidth : 30,
  								            markPoint : {
  								                data : [
  								                    {type : 'max', name: '最大值'},
  								                    {type : 'min', name: '最小值'}
  								                ]
  								            },
  								            markLine : {
  								                data : [
  								                    {type : 'average', name: '平均值'}
  								                ]
  								            }
  								        },
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
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px;padding: 0px;">
		<div id="cc" class="easyui-layout"  data-options="fit:true" style="margin: -16px;margin-left: 0px;">
			 <div data-options="region:'north',collapsed:false,border:false" style="width: 100%;height: 50%;overflow: hidden;"> 
			 <table  title="医生工作量统计"  cellspacing="0" cellpadding="0" border="0" style="padding: 7px 7px 0px 7px;;width: 100%;position :absolute; z-index:100;">
							<tr>
								<!-- 开始时间 --> 
								<td style="width:40px;" align="left">日期:</td>
								<td style="width:110px;">
									<input id="STime" class="Wdate" type="text" name="STime" value="${Stime}" onClick="WdatePicker()" style="margin-top:0px;height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<!-- 结束时间 --> 
								<td style="width:40px;" align="center">至</td>
								<td style="width:110px;">
									<input id="ETime" class="Wdate" type="text" name="ETime" value="${Etime}" onClick="WdatePicker()" style="margin-top:0px;height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td style="width:55px;" align="center">科室:</td>
<!-- 							       	&nbsp;科室部门: -->
								<td style="width:120px; " class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
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
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-top:0px">查询</a>
									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset" style="margin-top:0px">重置</a>
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
				 <div id="topShow" class="easyui-layout" style="margin-top: 40px">
					<div id="tableShow"  data-options="region:'center'"  style="padding: 0px 0px 5px 0px;width:100%;height:100%" >
						<table id="list"  class="easyui-datagrid" style="width: 100%;">
							<thead>
								<tr>
									<th colspan="21" data-options="width:'100%',align:'center'">医生工作量统计</th>
								</tr>
								<tr>
									<th rowspan="2" data-options="field:'name',width:'6%',align:'center'">医生姓名</th>
									<th rowspan="2" data-options="field:'dept',width:'6%',align:'center'">科室</th>
									<th colspan="3" data-options="width:'15%',align:'center'">门诊</th>
									<th colspan="5" data-options="width:'25%',align:'center'">电话</th>
									<th colspan="7" data-options="width:'35%',align:'center'">网络</th>
									<th colspan="4" data-options="width:'20%',align:'center'">汇总</th>
								</tr>
								<tr>
									<th data-options="field:'bookNo',width:'5%',align:'right'">预约数</th>
									<th data-options="field:'regNo',width:'5%',align:'right'">挂号数</th>
									<th data-options="field:'visNo',width:'5%',align:'right'">就诊数</th>
									<th data-options="field:'helpLoc',width:'7%',align:'right'">咨询（本地）</th>
									<th data-options="field:'helpNet',width:'7%',align:'right'">咨询（外地）</th>
									<th data-options="field:'helpNo',width:'6%',align:'right'">咨询总数</th>
									<th data-options="field:'telBook',width:'6%',align:'right'">电话预约</th>
									<th data-options="field:'telReg',width:'6%',align:'right'">电话挂号</th>
									<th data-options="field:'validHelpLoc',width:'8%',align:'right'">有效咨询(本地)</th>
									<th data-options="field:'validHelpNet',width:'8%',align:'right'">有效咨询(外地)</th>
									<th data-options="field:'validNo',width:'5%',align:'right'">咨询总数</th>
									<th data-options="field:'netBook',width:'5%',align:'right'">网络预约</th>
									<th data-options="field:'bookRat',width:'5%',align:'right'">预约率</th>
									<th data-options="field:'netReg',width:'5%',align:'right'">网络挂号</th>
									<th data-options="field:'arrRat',width:'5%',align:'right'">到诊率</th>
									<th data-options="field:'validTot',width:'6%',align:'right'">咨询总数</th>
									<th data-options="field:'bookTot',width:'6%',align:'right'">预约总数</th>
									<th data-options="field:'regTot',width:'6%',align:'right'">挂号总数</th>
									<th data-options="field:'arrTot',width:'6%',align:'right'">就诊总数</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>	
			</div>	
			<div data-options="region:'center',collapsible:false" id="main" style="height:50%;width: 100%;"></div>
		</div>
	</body>
</html>

<!--修改datagrid组件的行宽  -->
<style type="text/css">
	.datagrid-header-rownumber,.datagrid-cell-rownumber{
	   width:50px;
	 }
</style>

