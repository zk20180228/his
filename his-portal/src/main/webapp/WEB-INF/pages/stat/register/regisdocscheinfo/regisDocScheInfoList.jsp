<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>挂号医生排班信息查询</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
 	var titleMap=null;
 	var weekMap=null;
 	var tionMap=null;
 	var menuAlias = '${menuAlias}';
	$(function(){
		$("#tableShow").height($("body").height()-45);
		var begin='${begin}';
		//结束时间加一天
		var endTime='${end}';
		var date = new Date(endTime);
		date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
		var end = date.Format("yyyy-MM-dd");
		$.ajax({
			url:"<%=basePath%>statistics/regisdocscheinfo/getDictionary.action",
			type:"post",
			success:function(data){
				weekMap=data;
			}
		});
		$.ajax({
			url:"<%=basePath%>statistics/regisdocscheinfo/getDictionaryTionsex.action",
			type:"post",
			success:function(data){
				tionMap=data;
			}
		});
		$.ajax({
			url:"<%=basePath%>statistics/regisdocscheinfo/queryTitle.action",
			type:"post",
			success:function(data){
				titleMap=data;
			}
		});
				<%-- $.ajax({
					url: "<%=basePath%>baseinfo/department/getDeptMap.action",
					success: function(data) {
							$("#deptName").val(data[nowDept]);
					}
				}); --%>
				$.ajax({
					url: "<%=basePath%>baseinfo/employee/getEmplMap.action",
					success: function(data) {
						doctorName= $('#doctorName').getMenuIds();
						deptName= $('#deptName').getMenuIds(); 
						if(!$('h2','.xmenu').attr('class').indexOf('selected')!=-1||deptName==null||deptName==""){
							 //当全部按钮没有被选中并且没有选中科室
							 deptName = "all"; 
						 }
						//加载数据表格
						$("#operaCostListData").datagrid({
							url:'<%=basePath %>statistics/regisdocscheinfo/queryRegisDocScheInfo.action',
							idField:'id',
							border:true,
							checkOnSelect:true,
							selectOnCheck:false,
							singleSelect:true,
							fitColumns:false,
							rownumbers:true,
							pagination:true,
							pageSize:20,
				        	pageList:[20,30,50,100],
							fit:true,
							queryParams:{deptName:deptName,doctorName:doctorName,begin:begin,end:end,menuAlias:menuAlias},
							onLoadSuccess: function(data){
								$("#operaCostListData").datagrid('loaded');
								//分页工具栏作用提示
								var pager = $(this).datagrid('getPager');
								var aArr = $(pager).find('a');
								var iArr = $(pager).find('input');
								$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
								for(var i=0;i<aArr.length;i++){
									$(aArr[i]).tooltip({
										content:toolArr[i],
										hideDelay:1
									});
									$(aArr[i]).tooltip('hide');
								}
							}
						});
					}
				});
					/*******************************************************************************************************************/
					//选择科室
							$(".deptInput").MenuList({
								width :530, //设置宽度，不写默认为530，不要加单位
								height :400, //设置高度，不写默认为400，不要加单位
								dropmenu:"#m2",//弹出层id，必须要写
								isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
								para:"C",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
								firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
								relativeInput:".doctorInput",	//与其级联的文本框，必须要写
								relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
							});
							//选择医生
							$(".doctorInput").MenuList({
								width :530,
								height :400,
								dropmenu:"#m3",//弹出层
								isSecond:true,	//是否是二级联动的第二级
								para:"C",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
								firsturl: "<%=basePath %>statistics/RegisterInfoGzltj/getDoctorList.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
								relativeInput:".deptInput"	//与其级联的文本框
								//relativeDropmenu:"#m2"
							});
					/*******************************************************************************************************************/
		
// 		$('#doctorName').combobox({
<%-- 			url: '<%=basePath%>/baseinfo/employee/employeeCombobox.action', --%>
// 			valueField:'jobNo',    
// 			textField:'name'
// 		});
// 		$('#deptName').combobox({
<%-- 			url: '<%=basePath%>/baseinfo/department/departmentCombobox.action', --%>
// 			valueField:'deptCode',    
// 			textField:'deptName'
// 		});
	});	
	
	function find(){
		var begin=$("#begin").val();
		var endTime=$("#end").val();
		if(begin==null || begin=="" || endTime==null || endTime==""){
			$.messager.alert("提示","请填写正确的时间范围！");
			return ;
  		}
		if(begin&&endTime){
	          if(begin>endTime){
	            $.messager.alert("提示","开始时间不能大于结束时间！");
	            return ;
	          }
	        }
		var date = new Date(endTime);
		date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
		var end = date.Format("yyyy-MM-dd");
		searchFinal(begin,end);
		$("#end").val(endTime);
	}
	function searchFinal(Stime,Etime){
		$('#begin').val(Stime);
		$('#end').val(Etime);
		if(Stime&&Etime){
	          if(Stime>Etime){
	            $.messager.alert("提示","开始时间不能大于结束时间！");
	            return ;
	          }
	        }
		var doctorName="";
		if($.trim($('#doctorName').val())!=""){
			doctorName= $('#doctorName').getMenuIds(); 
		}
		var deptName="";
		if($.trim($('#deptName').val())!=""){
			deptName= $('#deptName').getMenuIds(); 
		}
			 //当全部按钮没有被选中并且没有选中科室
// 			 $('h2','.xmenu').attr('class').indexOf('selected')!=-1||
		if(deptName==null||deptName==""){
			 deptName = "all"; 
		 }
// 		alert(doctorName);
// 		alert(deptName);
// 		if(Stime==null || Stime==""){
// 			Stime="${begin}";
//       	}
//       	if(Etime==null || Etime==""){
//       		Etime="${end}";
//       	}
		$("#operaCostListData").datagrid('load',{deptName:deptName,doctorName:doctorName,begin:Stime,end:Etime,menuAlias:menuAlias});
	 }
	//距离当多少天的日期
	 function GetDateStr(AddDayCount) {
		 var dd = new Date();
		 dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
		 var y = dd.getFullYear();
		 var m = dd.getMonth()+1;//获取当月份的日期
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
// 		alert(Stime);
// 		alert(Etime);
		searchFinal(Stime,Etime);
		$("#end").val(GetDateStr(0));
	 }
	 //查询三天
	function searchThree(){
		var Stime = GetDateStr(-3);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$("#end").val(GetDateStr(-1));
	}
	//查询七天
	function searchSeven(){
		var Stime = GetDateStr(-7);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$("#end").val(GetDateStr(-1));
	}
	//查询15天 zhangkui 2017-04-17
	function searchFifteen(){
		var Stime = GetDateStr(-15);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$("#end").val(GetDateStr(-1));
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
        $('#end').val(lastDay);
	}
	
	
	
	//查询当月
	function searchMonth(){
		var Stime = getCurrentMonthFirst();
		//需求：统计当月时，统计1号到当时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		//var Etime = getCurrentMonthLast();
		var Etime = GetDateStr(1);	
		searchFinal(Stime,Etime);
		$("#end").val(GetDateStr(0));
	}
	//查询当年
	function searchYear(){
		//var Etime = new Date().getFullYear()+"-12-31";
		//需求：统计当年时，统计1号到当时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Etime = GetDateStr(1);	
		var Stime = new Date().getFullYear()+"-01-01";
		searchFinal(Stime,Etime);
		$("#end").val(GetDateStr(0));
		
	}
	
	
	
	

	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#begin').val('${begin}');
		$('#end').val('${end}');
			$('#doctorName').val("");
			$('#deptName').val("");
			$("a[name='menu-confirm-cancel']").click();
			$("a[name='menu-confirm-clear']").click();
		find();
	}	
	//午别
	function functionsex(value){
		if(value){
			return tionMap[value];
		}
	}
	//星期
	function functionweek(value){
		if(value!=null&&value!=""){
			return weekMap[value];
		}
		return "";
	}
	//星期
	function functiontitle(value){
		if(value!=null&&value!=""){
			return titleMap[value];
		}
		return value;
	}
	
	//导出列表
	function exportList() {
		var begin=$("#begin").val();
		var endTime=$("#end").val();
		if(begin!=null&&begin!=''&&endTime!=null&&endTime!=''){
		
			/* $('#doctorName').textbox('getValue') */
			var doctorName="";
			if($.trim($('#doctorName').val())!=""&&$('#doctorName').val()!=null){
				doctorName= $('#doctorName').getMenuIds(); 
			}
			var deptName="";
			if($.trim($('#deptName').val())!=""&&$('#deptName').val()!=null){
				deptName= $('#deptName').getMenuIds(); 
			}
	// 		var begin=$("#begin").val();
	// 		var endTime=$("#end").val();
			//结束时间加一天
			var date = new Date(endTime);
			date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
			var end = date.Format("yyyy-MM-dd");
			if(begin&&endTime){
				if(begin>endTime){
					$.messager,alert("提示","开始时间不能大于结束时间！");
					return ;
				}
			}
			var rows = $("#operaCostListData").datagrid("getRows");
			if (rows==null||rows.length==0) {
				$.messager.alert('提示','没有数据，不能导出！');
				return;
			}else{
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
	// 				  $("#reportToLogin").val(begin);
	// 				  $("#reportToEnd").val(end);
	// 				  $("#_doctor").val(doctorName);
	// 				  $("#_dept").val(deptName);
					$('#saveForm').form('submit', {/*+"&deptName"+deptName  */
						url :"<%=basePath%>statistics/regisdocscheinfo/outRegisDocScheInfo.action?doctorName="+doctorName+"&deptName="+deptName+"&begin="+begin+"&end="+end+"&menuAlias="+menuAlias,
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							$.messager.alert("提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("提示", "导出失败！", "error");
						}
					});
				}
			});
		 }
		}else{
			$.messager.alert("提示","日期不能为空！");
		}
}
	//打印
	function edit(){
		var begin=$("#begin").val();
		var endTime=$("#end").val();
		if(begin!=null&&begin!=''&&endTime!=null&&endTime!=''){
			var rows = $("#operaCostListData").datagrid("getRows");
			if (rows==null||rows.length==0) {
				$.messager.alert("提示","没有数据，不能打印!");
				return;
			}else{
			$.messager.confirm('确认', '确定要打印吗?', function(res) {  //提示是否打印假条
				if (res){
	// 				var doctorName= $('#doctorName').getMenuIds(); 
	// 				var deptName= $('#deptName').getMenuIds();
	// 				var doctorName2= $('#doctorName').val(); 
	// 				var deptName2= $('#deptName').val(); 
	// 				var begin=$("#begin").val();
	// 				var endTime=$("#end").val();
					var doctorName="";
					if($.trim($('#doctorName').val())!=""&&$('#doctorName').val()!=null){
						doctorName= $('#doctorName').getMenuIds(); 
					}
					var deptName="";
					if($.trim($('#deptName').val())!=""&&$('#deptName').val()!=null){
						deptName= $('#deptName').getMenuIds(); 
					}
					var doctorName2= $('#doctorName').val(); 
					var deptName2= $('#deptName').val(); 
// 					var begin=$("#begin").val();
// 					var endTime=$("#end").val();
					//结束时间加一天
					var date = new Date(endTime);
					date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
					var end = date.Format("yyyy-MM-dd");
					if(begin&&endTime){
						if(begin>endTime){
							$.messager,alert("提示","开始时间不能大于结束时间！");
							return ;
						}
					}
					
// 					<form id="saveForm" method="post"></form>
// 					<form method="post" id="reportToHiddenForm" >
// 					<input type="hidden" name="begin" id="reportToLogin" value=""/>
// 					<input type="hidden" name="end" id="reportToEnd" value=""/>
// 					<input type="hidden" name="deptName2" id="deptName2" value=""/>
// 					<input type="hidden" name="doctorName" id="_doctor" value=""/>
// 					<input type="hidden" name="doctorName2" id="doctorName2" value=""/>
// 					<input type="hidden" name="deptName" id="_dept" value=""/>
// 					<input type="hidden" name="fileName" id="reportToFileName" value=""/>
// 				</form>	
					
						//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
					    //给表单的隐藏字段赋值
// 					    $("#reportToLogin").val(begin);
// 					    $("#reportToEnd").val(end);
// 					    $("#_doctor").val(doctorName);
// 					    $("#_dept").val(deptName);
					    $("#deptName2").val(deptName2);//设置为post提交，防止乱码
					    $("#doctorName2").val(doctorName2);
// 					    $("#reportToFileName").val("MZGHYSGZLTJ");
						//上边的方式，总是包空指针异常，但是能打印，下面的数据量大时报io异常，能打印，无语
					    //表单提交 target
					    var formTarget="hiddenFormWin";
				        var tmpPath = "<%=basePath%>statistics/regisdocscheinfo/iReportResDocWork.action?menuAlias="+menuAlias+"&begin="+begin+"&end="+end+"&doctorName="+doctorName+"&deptName="+deptName+"&fileName="+'MZGHYSGZLTJ';
				        //设置表单target
				        $("#reportToHiddenForm").attr("target",formTarget);
				        //设置表单访问路径
						$("#reportToHiddenForm").attr("action",tmpPath); 
				        //表单提交时打开一个空的窗口
					    $("#reportToHiddenForm").submit(function(e){
					    	 var timerStr = Math.random();
							 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
						});  
					    //表单提交
					    $("#reportToHiddenForm").submit();
					}
			 });
			}
	}else{
		$.messager.alert("提示","日期不能为空！");
	}
}
	//格式化日期
    function formatDate(val,row){
    	var date = new Date(val);
		var dateFormater = date.Format("yyyy-MM-dd");
    	return dateFormater;
    }
	
	function closeM3(){
		$("a[name='menu-confirm-cancel']").click();
		$("a[name='menu-confirm-clear']").click();
	}
</script>
<style type="text/css">
.datagrid-header-rownumber,.datagrid-cell-rownumber{
   width:45px;
 }
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body style="margin: 0px;padding: 0px;">
			<div id="topShow" data-options="region:'north',border:false" style="height:38px;padding:8px 5px 0px 5px;">
						<table id="searchTab" style="width: 100%;">
							<tr>
								<!-- 开始时间 --> 
								<td style="width:40px;" align="left">日期:</td>
								<td style="width:100px;">
									<input id="begin" class="Wdate" type="text" value="${begin}" onClick="WdatePicker()"  style="height:22px; width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<!-- 结束时间 --> 
								<td style="width:15px;"><span style="margin-left:7">至</span></td> 
								<td style="width:102px;">
									<span style="margin-left:7">
									<input id="end" class="Wdate" type="text" value="${end}" onClick="WdatePicker()" style="height:22px;width:102px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									</span>
								</td>
					    		<td style="width:55px;" ><span style="margin-left:15">科室:</span></td>
				    			<td style="width:120px;" class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:120px;">	<input class="ksnew" id="deptName"  style="width:95px;" readonly="readonly"/><span ></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
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
										<div class="select-info" style="display:none; ">	
											<label class="top-label">已选科室：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl"  >
										<div class="addlist"></div>
										<div class="tip" style="display:none; ">没有检索到数据</div>		 
										</div>	
									</div>
					    		</td>
								<td style="width:50px;" ><span style="margin-left:9">医生:</span></td>
				    			<td style="width:120px;"  class="newMenu">
				    					<div class="doctorInput menuInput"  style="width:120px;"><input class="ksnew" id="doctorName"  style="width:95px;"  readonly="readonly"/></input> <span></span></div> 
					    			  	<div id="m3" class="xmenu" style="display: none;">
					    	       		<div class="searchDept" >
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" >
												<span class="a-btn-text">取消</span>
											</a>
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">确定</span>
											</a>											
					    	       		</div>
										<div class="select-info" style="display:none">	
											<label class="top-label">已选医生：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl" >
											<div class="addlist"></div>
											<div class="tip" style="display:none">没有检索到数据</div>			 
										</div>
									</div>	
					    		</td>
					    		
								<td>
									<span style="margin-left:4"><a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></span>
									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
									<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
									<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
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
					</div>
			<div  id="tableShow" onclick="closeM3()" data-options="region:'center',border:false" style="width: 100%;">
				<table id="operaCostListData"  class="easyui-datagrid" style="padding:5px 5px 5px 5px;">
					<thead>
						<tr>
						<!--2017-04-17 zhangkui 现在组长让把复选框去掉，添加一个行数列  rownumbers:true,-->
						<!--<th data-options="field:'ck',checkbox:true" ></th> -->
							<th data-options="field:'deptName',width:'12%',halign:'center'">科室</th>
							<th data-options="field:'doctorName',width:'12%',halign:'center'">医生</th>
							<th data-options="field:'reglevlName',width:'12%',halign:'center',formatter:functiontitle">职称</th> 
							<th data-options="field:'weekday',width:'12%',halign:'center',formatter:functionweek">星期</th>
							<th data-options="field:'noonName',width:'12%',halign:'center',formatter:functionsex">午别</th>
							<th data-options="field:'empRemark',width:'12%',halign:'center'">专长</th>
							<th data-options="field:'seeDate',width:'12%',halign:'center',formatter:formatDate">日期</th>
<!-- 							<th data-options="field:'empPinyin',width:'12%',halign:'center'">拼音码</th> -->
						</tr>
					</thead>
				</table>
					<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="begin" id="reportToLogin" value=""/>
						<input type="hidden" name="end" id="reportToEnd" value=""/>
						<input type="hidden" name="deptName2" id="deptName2" value=""/>
						<input type="hidden" name="doctorName" id="_doctor" value=""/>
						<input type="hidden" name="doctorName2" id="doctorName2" value=""/>
						<input type="hidden" name="deptName" id="_dept" value=""/>
						<input type="hidden" name="fileName" id="reportToFileName" value=""/>
					</form>			
			</div>
			
		</div> 
	</body>
	</html>