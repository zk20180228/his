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
		
		/**性别下拉**/
		$('#sexCode').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		//出径情况渲染
		$.ajax({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=outcom",
			success: function(data) {
				var j = data;
				for(var i=0;i<j.length;i++){
					outpTypeMap.put(j[i].encode,j[i].name);
				}
			}
		});
		//性别渲染
		$.ajax({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex",
			success: function(data) {
				var j = data;
				for(var i=0;i<j.length;i++){
					sexMap.put(j[i].encode,j[i].name);
				}
			}
		});
		//科室渲染
		$.ajax({
			url: "<%=basePath%>baseinfo/department/getDeptMap.action",
			success: function(data) {
				deptMap = data;
				searchFrom();
				searchFromTopR();
				searchFromBL();
				searchFromBR();
			}
		});
		//临床路径名称渲染
		$.ajax({
			url: "<%=basePath%>/inpatient/clinicalPathwayAction/queryCpWay.action",
			success: function(data) {
				var j = JSON.parse(data);
				for(var i=0;i<j.length;i++){
					diseaseMap.put(j[i].id,j[i].cpName);
				}
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
	/**性别渲染**/
	function formatterSex(value,row,index){
		if(value!=null&&value!=""){
			return typeof(sexMap.get(value))=="undefined"?value:sexMap.get(value);
		}
	}	
	/**科室渲染**/
	function formatterDept(value,row,index){
		if(value!=null&&value!=""){
			return typeof(deptMap[value])=="undefined"?value:deptMap[value];
		}
	}	
	/**科室渲染**/
	function formatterOutpType(value,row,index){
		if(value!=null&&value!=""){
			
		}else{
			value = "1";
		}
			return typeof(outpTypeMap.get(value))=="undefined"?value:outpTypeMap.get(value);
	}	
	/**临床路径（病种）渲染**/
	function formatterDisease(value,row,index){
		if(value!=null&&value!=""){
			return typeof(diseaseMap.get(value))=="undefined"?value:diseaseMap.get(value);
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
				var inOrOutTopL1 = $('#inOrOutTopL').combobox('getValue');
				var deptCodeTopL1 = $('#deptCodeTopL').combobox('getValue');
				$('#listTopL').datagrid({
					url:'<%=basePath%>/bi/staClinicalPathwayAction/inOutList.action',
					queryParams:{
						sTime: Stime,
						eTime: Etime,
						deptCodeTopL: deptCodeTopL1,
						inOrOutTopL: inOrOutTopL1
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
			
			function clearsBL(){
				$('#deptCodeBL').combobox('setValue',"");
				$('#StimeBL').val("${sTime}");
				$('#EtimeBL').val("${eTime}");
				searchFromBL();
			}
			/**未入径统计查询**/
			function searchFromBL() {
				var Stime = $("#StimeBL").val();
				var Etime = $("#EtimeBL").val();
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
				var deptCodeBL1 = $('#deptCodeBL').combobox('getValue');
				$('#listBottomL').datagrid({
					url:'<%=basePath%>/bi/staClinicalPathwayAction/notEntryList.action',
					queryParams:{
						sTime: Stime,
						eTime: Etime,
						deptCodeBL: deptCodeBL1,
					}
				});
			}
			function clearsTopR(){
				$('#deptCodeTR').combobox('setValue',"");
				$('#variationTopR').combobox('setValue',"");
				$('#StimeTR').val("${sTime}");
				$('#EtimeTR').val("${eTime}");
				searchFromTopR();
			}
			/**变异出径统计分析**/
			function searchFromTopR() {
				var Stime = $("#StimeTR").val();
				var Etime = $("#EtimeTR").val();
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
				var deptCodeTR1 = $('#deptCodeTR').combobox('getValue');
				var variationTopR1 = $('#variationTopR').combobox('getValue');
				$('#listTopR').datagrid({
					url:'<%=basePath%>/bi/staClinicalPathwayAction/variationOutList.action',
					queryParams:{
						sTime: Stime,
						eTime: Etime,
						deptCodeTR: deptCodeTR1,
						variationTR: variationTopR1
					}
				});
			}
			function clearsBR(){
				$('#deptCodeBR').combobox('setValue',"");
				$('#inOrOutBR').combobox('setValue',"");
				$('#sexCode').combobox('setValue',"");
				$('#StimeBR').val("${sTime}");
				$('#EtimeBR').val("${eTime}");
				searchFromBR();
			}
			/**出入径明细查询**/
			function searchFromBR() {
				var Stime = $("#StimeBR").val();
				var Etime = $("#EtimeBR").val();
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
				var deptCodeBR1 = $('#deptCodeBR').combobox('getValue');
				var inOrOutBR1 = $('#inOrOutBR').combobox('getValue');
				var sexCode1 = $('#sexCode').combobox('getValue');
				$('#listBottomR').datagrid({
					url:'<%=basePath%>/bi/staClinicalPathwayAction/inOutDetailList.action',
					queryParams:{
						sTime: Stime,
						eTime: Etime,
						deptCodeBR: deptCodeBR1,
						inOrOutBR: inOrOutBR1,
						sexCode: sexCode1,
					}
				});
			}
     </script>
</head>
<body style="overflow: hidden;">
	<div id="topLeft" class="easyui-layout" style="height:50%;width: 50%;float: left;border-right: 1px solid;border-color: #77d2d0">
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
						<input id="deptCodeTopL" data-options="prompt:'科室'" class="easyui-combobox deptPD" style="width: 120px"/>
					</td>
					<td style="width:100px;" align="center">在院/出院:</td>
					<td >
						<input id="inOrOutTopL" class="easyui-combobox" data-options="width:110,panelHeight:80,
								valueField: 'value',
								textField: 'label',
								data: [{
									label: '全部',
									value: '',
									selected: true
								},{
									label: '在院',
									value: '1'
								},{
									label: '出院',
									value: '2'
								}]">
					</td>
					<td>
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
			</tr>
		</table>
		<div id="tableShowRL" style="width:100%;margin-top: 7px;height:calc(100% - 40px);" >
			<table class="easyui-datagrid"  title="在/出院患者统计" id="listTopL" style="" data-options="pagination:true,pageSize:15,pageList:[15,30,45],method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fit:true,rownumbers:true">
				<thead>
					<tr>
						<th data-options="field:'deptCode',align:'center',width:30,formatter: formatterDept">科室</th>
						<th data-options="field:'nameCode',align:'center',width:40,formatter: formatterDisease">临床路径名称</th>
						<th data-options="field:'num',align:'center',width:25">人次</th>
				    </tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="topRight" class="easyui-layout" style="height:50%;width: 50%;float: left;">
		<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
			<tr >
				<!-- 开始时间 --> 
					<td style="width:40px;" align="left">日期:</td>
					<td style="width:110px;">
					<input id="StimeTR" class="Wdate" type="text" name="StimeTR" value="${sTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
					<input id="EtimeTR" class="Wdate" type="text" name="EtimeTR" value="${eTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			       </td>
					<td style="width:55px;" align="center">科室:</td>
					<td style="width:110px;">
						<input id="deptCodeTR" data-options="prompt:'科室'" class="easyui-combobox deptPD" style="width: 110px"/>
					</td>
					<td style="width:115px;" align="center">变异出径方向:</td>
					<td >
						<input id="variationTopR" class="easyui-combobox" data-options="width:110,panelHeight:80,
								valueField: 'value',
								textField: 'label',
								data: [{
									label: '全部',
									value: '',
									selected: true
								},{
									label: '正变异出径',
									value: '1'
								},{
									label: '负变异出径',
									value: '2'
								}]">
					</td>
					<td>
						<a href="javascript:void(0)" onclick="searchFromTopR()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
						<a href="javascript:void(0)" onclick="clearsTopR()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
			</tr>
		</table>
		<div id="tableShowTR" style="width:100%;margin-top: 7px;margin-left:-2px;height:calc(100% - 40px);" >
			<table class="easyui-datagrid" title="变异出径统计分析" id="listTopR" style="" data-options="pagination:true,pageSize:15,pageList:[15,30,45],method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fit:true,rownumbers:true">
				<thead data-options="frozen:true">
					<tr>
						<th data-options="field:'deptCode',align:'center',width:'30%',formatter: formatterDept">科室</th>
						<th data-options="field:'nameCode',align:'center',width:'40%'">变异名称</th>
						<th data-options="field:'num',align:'center',width:'25%'">变异次数</th>
				    </tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="bottomLeft" class="easyui-layout" style="float: left;height:50%;width: 50%;border-right: 1px solid;border-color: #77d2d0">
		<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
			<tr >
<!-- 				开始时间  -->
					<td style="width:40px;" align="left">日期:</td>
					<td style="width:110px;">
					<input id="StimeBL" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
<!-- 					结束时间  -->
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
					<input id="EtimeBL" class="Wdate" type="text" name="Etime" value="${eTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			       </td>
					<td style="width:55px;" align="center">科室:</td>
					<td >
						<input id="deptCodeBL" data-options="prompt:'科室'" class="easyui-combobox deptPD" style="width: 110px"/>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="searchFromBL()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
						<a href="javascript:void(0)" onclick="clearsBL()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
			</tr>
		</table>
		<div id="tableShowBL" style="width:100%;margin-top: 7px;height:calc(100% - 40px);" >
			<table class="easyui-datagrid" title="未入径统计查询" id="listBottomL" style="" data-options="pagination:true,pageSize:15,pageList:[15,30,45],method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fit:true,rownumbers:true	">
				<thead data-options="frozen:true">
					<tr>
						<th data-options="field:'deptCode',align:'center',width:'30%'">科室</th>
						<th data-options="field:'nameCode',align:'center',width:'40%',formatter: formatterDisease">临床路径名称</th>
						<th data-options="field:'num',align:'center',width:'25%'">未入径人数</th>
				    </tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="bottomRight" class="easyui-layout" style="float: left;height:50%;width: 50%;border-right: 1px solid;border-color: #77d2d0">
		<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
			<tr >
<!-- 				开始时间  -->
					<td style="width:40px;" align="left">日期:</td>
					<td style="width:110px;">
					<input id="StimeBR" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
<!-- 					结束时间  -->
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
					<input id="EtimeBR" class="Wdate" type="text" name="Etime" value="${eTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			       </td>
					<td style="width:55px;" align="center">科室:</td>
					<td style="width:88px;">
						<input id="deptCodeBR" data-options="prompt:'科室'" class="easyui-combobox deptPD" style="width: 110px"/>
					</td>
					<td style="width:80px;" align="center">出入径:</td>
					<td style="width:50px;">
						<input id="inOrOutBR" class="easyui-combobox" data-options="width:50,panelHeight:80,
								valueField: 'value',
								textField: 'label',
								data: [{
									label: '全部',
									value: '',
									selected: true
								},{
									label: '出径',
									value: '1'
								},{
									label: '入径',
									value: '2'
								}]">
					</td>
					<td style="width:66px;" align="center">性别:</td>
					<td style="width:50px;">
						<input class="easyui-combobox"  id="sexCode" data-options="width:50,panelHeight:100"/>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="searchFromBR()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
						<a href="javascript:void(0)" onclick="clearsBR()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
			</tr>
		</table>
		<div id="tableShowBR" style="width:100%;margin-top: 7px;height:calc(100% - 40px);" >
			<table class="easyui-datagrid" title="出入径明细查询" id="listBottomR" style="" data-options="pagination:true,pageSize:15,pageList:[15,30,45],method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fit:true,rownumbers:true	">
				<thead data-options="frozen:true">
					<tr>
						<th data-options="field:'deptCode',align:'center',width:'10%',formatter: formatterDept">科室</th>
						<th data-options="field:'inpatientNo',align:'center',width:'12%'">住院流水号</th>
						<th data-options="field:'inpatientName',align:'center',width:'8%'">患者姓名</th>
						<th data-options="field:'sexCode',align:'center',width:'8%',formatter: formatterSex">性别</th>
						<th data-options="field:'age',align:'center',width:'8%'">年龄</th>
						<th data-options="field:'cpId',align:'center',width:'20%',formatter: formatterDisease">临床路径名称</th>
						<th data-options="field:'inPathTime',align:'center',width:'12%'">入径日期</th>
						<th data-options="field:'outPathTime',align:'center',width:'12%'">出径日期</th>
						<th data-options="field:'outpTypeCode',align:'center',formatter: formatterOutpType,width:'10%'">出径情况</th>
				    </tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>