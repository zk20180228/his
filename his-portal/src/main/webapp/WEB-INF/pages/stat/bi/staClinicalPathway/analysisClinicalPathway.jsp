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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	var deptMap = new Map();
	var diseaseMap = new Map();
	var outpTypeMap = new Map();
	var sexMap = new Map();
	$(function() {
		//科室渲染
		$.ajax({
			url: "<%=basePath%>baseinfo/department/getDeptMap.action",
			success: function(data) {
				deptMap = data;
				searchFrom();
			}
		});
		/**科室下拉**/
		$('.deptPD').combobox({
			url: "<%=basePath%>inpatient/info/zyDeptCombobox.action",
			valueField : 'deptCode',
			textField : 'deptName',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'deptCode';
				keys[keys.length] = 'deptName';
				keys[keys.length] = 'deptPinyin';
				keys[keys.length] = 'deptWb';
				keys[keys.length] = 'deptInputcode';
				if(filterLocalCombobox(q, row, keys)){
					row.selected=true;
				}else{
					row.selected=false;
				}
				return filterLocalCombobox(q, row, keys);
		    }
		});
	});
	/**科室渲染**/
	function formatterDept(value,row,index){
		if(value!=null&&value!=""){
			return typeof(deptMap[value])=="undefined"?value:deptMap[value];
		}
	}	
	
	function clears(){
		$('#inOrOutTopL').combobox('setValue',"");
		$('#deptCodeTopL').combobox('setValue',"");
		$('#StimeTopL').val("${sTime}");
		$('#EtimeTopL').val("${eTime}");
		searchFrom();
	}
			 /**
			 * 查询
			 * @author wujiao
			 * @date 2015-08-31
			 * @version 1.0
			 */
			function searchFrom() {
				var Stime = $('#StimeTopL').val();
				var Etime = $('#EtimeTopL').val();
				if(Stime==null || Stime=="" || Etime==null || Etime==""){
					$.messager.alert("提示","请填写正确的时间范围！");
					return ;
		  		}
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
			     searchFrom1(Stime,Etime);
			}
			function searchFrom1(Stime,Etime) {
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
				var deptCodeTopL1 = $('#deptCodeTopL').combobox('getValue');
				$('#listTopL').datagrid({
					url:'<%=basePath%>/bi/staClinicalPathwayAction/analysisClinicalList.action',
					queryParams:{
						sTime: Stime,
						eTime: Etime,
						deptCodeTopL: deptCodeTopL1
					}
				});
			}
			function searchFinal(Stime,Etime){
				$('#Stime').val(Stime);
				$('#Etime').val(Etime);
				searchFrom1(Stime,Etime);
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
				var Etime = GetDateStr(0);
				searchFinal(Stime,Etime);
			 }
			 //查询前三天
			function searchThree(){
				var Stime = GetDateStr(-3);
				var Etime = GetDateStr(-1);
				searchFinal(Stime,Etime);
			}
			//查询前七天
			function searchSeven(){
				var Stime = GetDateStr(-7);
				var Etime = GetDateStr(-1);
				searchFinal(Stime,Etime);
			}
			//查询前15天
			function searchFifteen(){
				var Stime = GetDateStr(-15);
				var Etime = GetDateStr(-1);
				searchFinal(Stime,Etime);
			}
			//上月
			function beforeMonth(){
				  var date = new Date();
				  var year = date.getFullYear();
				  var month = date.getMonth();
				  if(month==0)
				  {
				  month=12;
				  year=year-1;
				  }
				  if (month < 10) {
				  month = "0" + month;
				  }
				  var Stime = year + "-" + month + "-" + "01";//上个月的第一天
				  var lastDate = new Date(year, month, 0);
				  var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
				  var Etime= lastDay;
				  searchFinal(Stime,Etime);
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
			//查询当月,包含当天，显示当天，因后台是用小于号查询，所以往后台传值需加一天
			function searchMonth(){
				var Stime = getCurrentMonthFirst();
				//var Etime = getCurrentMonthLast();之前的代码
				//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
				//2017-04-17新的
//		 		var date=new Date();
//		 		var Etime = date.Format("yyyy-MM-dd");
				var Etime = GetDateStr(0);	
				searchFinal(Stime,Etime);
			}
			//查询当年,包含当天，显示当天，因后台是用小于号查询，所以往后台传值需加一天
			function searchYear(){
				//var Etime = new Date().getFullYear()+"-12-31";
				//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
//		 		var date=new Date();
//		 		var Etime = date.Format("yyyy-MM-dd");
				var Stime = new Date().getFullYear()+"-01-01";
				var Etime = GetDateStr(0);
				searchFinal(Stime,Etime);
			}
			
     </script>
</head>
<body style="overflow: hidden;">
	<div id="topLeft" class="easyui-layout" style="height:100%;width: 100%;float: left;border-right: 1px solid;border-color: #77d2d0">
		<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100; " data-options="border:false" cellspacing="0" cellpadding="0" border="0" >
			<tr >
				<!-- 开始时间 --> 
					<td style="width:40px;" align="left">日期:</td>
					<td style="width:110px;">
					<input id="StimeTopL" class="Wdate" type="text" name="StimeTopL" value="${sTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
					<input id="EtimeTopL" class="Wdate" type="text" name="EtimeTopL" value="${eTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			       </td>
					<td style="width:55px;" align="center">科室:</td>
					<td style="width:110px;">
						<input id="deptCodeTopL" data-options="prompt:'科室'" class="easyui-combobox deptPD" style=""/>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
			</tr>
		</table>
		<div id="tableShowRL" style="width:100%;margin-top: 7px;height:calc(100% - 40px);" >
			<table class="easyui-datagrid" id="listTopL" style="" data-options="pagination:true,pageSize:15,pageList:[15,30,45],method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fit:true,rownumbers:true">
				<thead>
					<tr>
						<th data-options="field:'deptCode',align:'center',width:'10%',formatter: formatterDept">科室</th>
						<th data-options="field:'totalNum',align:'center',width:'10%'">总人数</th>
						<th data-options="field:'inNum',align:'center',width:'10%'">入径数</th>
						<th data-options="field:'inRare',align:'center',width:'10%'">入径率</th>
						<th data-options="field:'outRare',align:'center',width:'10%'">出径率</th>
						<th data-options="field:'overRare',align:'center',width:'10%'">完成率</th>
						<th data-options="field:'vaiviationRare',align:'center',width:'10%'">变异率</th>
						<th data-options="field:'betterRare',align:'center',width:'10%'">好转率</th>
						<th data-options="field:'cureRare',align:'center',width:'10%'">治愈率</th>
				    </tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>