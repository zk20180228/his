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
<title>结账处冲单明细日报</title>
<style type="text/css">
.datagrid-header-rownumber,.datagrid-cell-rownumber{
   width:50px;
 }
</style>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',border:false" style="height:40px;width: 100%;padding-top: 8px;padding-top: 8px;padding-right: 10px;padding-left: 10px;">
 			日期：
 			<input id="beginDate" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
 			 至
 			  <input id="endDate" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
 			<shiro:hasPermission name="${menuAlias}:function:query">
 			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom(1)" iconCls="icon-search">查询</a>
 			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:set">
 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
			</shiro:hasPermission>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
			<shiro:hasPermission name="${menuAlias}:function:set">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a>
	    	</shiro:hasPermission>
	    	<shiro:hasPermission name="${menuAlias}:function:query">
	    	<a href="javascript:searchFrom(4);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>
	    	</shiro:hasPermission>
	    	<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:searchFrom(5);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:searchFrom(8);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:searchFrom(7);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:searchFrom(3);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:searchFrom(2);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:searchFrom(6);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
			</shiro:hasPermission>
	    </div>   
	    <div data-options="region:'center',border:false" style="width: 100%;height: 95%;">
	    	<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',pagination:true,striped:true,fitColumns:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'deptName',width:'17%'">科室</th>
						<th data-options="field:'inpatientNo',width:'13%'">住院流水号</th>
						<th data-options="field:'name',width:'17%'">姓名</th>
						<th data-options="field:'statName',width:'17%'">统计大类</th>
						<th data-options="field:'totCost',width:'17%',formatter:totCostFormatter">冲单费用</th>
						<th data-options="field:'balanceOpername',width:'18%'">操作员</th>
					</tr>
				</thead>
			</table>
			<form id="saveForm" method="post"></form>
	    </div>   
	</div>
	<script type="text/javascript">
	function totCostFormatter(value,row,index){//库存数量
		var sum=value;
		if(typeof(sum)=='undefined'){
			sum=0;
		}
		return sum.toFixed(2);
	}
		function clear(){
			$('#beginDate').val("${stime}");
			$('#endDate').val("${etime}");
			$("#billSearchHzList").datagrid('loadData', { total: 0, rows: [] });
		}
		$(function(){
			$('#beginDate').val("${stime}");
			$('#endDate').val("${etime}");
			$('#billSearchHzList').datagrid({
				url:'<%=basePath%>statistics/DetailedDaily/queryDetailedDailylist.action',
				onLoadSuccess: function (data) {//默认选中
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
					$('#billSearchHzList').datagrid("autoMergeCells", ['deptName','inpatientNo','name','operName']);
				}
			});
			$("#billSearchHzList").datagrid('loadData', { total: 0, rows: [] });
		})
	//查询
	function searchFrom(flag){
		var startTime=$('#beginDate').val();
		var endTime=$('#endDate').val();
		if(startTime&&endTime){
		    if(startTime>endTime){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
		var Stime;//开始时间
		var Etime;//结束时间
		var year;
		var month;
		var day;
	 if(flag==1){
	 		Stime = $('#beginDate').val();
			Etime = $('#endData').val();
			 if(Stime!=null&&Stime!=""){
			  Stime=Stime+" 00:00:00";
			  }
			  if(Etime!=null&&Etime!=""){
			  Etime=Etime+" 23:59:59";
			  }
	 }else{
		  var date=new Date();
		  year=date.getFullYear();
		  month=date.getMonth()+1;
		  day=date.getDate();
		  month=month<10?"0"+month:month;
		  day=day<10?"0"+day:day;
		  Etime=year+'-'+month+'-'+day;
		  if(flag==2){
			  var lon=date.getTime()-1000*3600*24*3;
			  Stime=new Date(lon);
			  year=Stime.getFullYear();
			  month=Stime.getMonth()+1;
			  day=Stime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Stime=year+'-'+month+'-'+day; 
			  lon=date.getTime()-1000*3600*24*1;
			  Etime=new Date(lon);
			  year=Etime.getFullYear();
			  month=Etime.getMonth()+1;
			  day=Etime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Etime=year+'-'+month+'-'+day; 
		  }else if(flag==3){
			  var lon=date.getTime()-1000*3600*24*7;
			  Stime=new Date(lon);
			  year=Stime.getFullYear();
			  month=Stime.getMonth()+1;
			  day=Stime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Stime=year+'-'+month+'-'+day; 
			  lon=date.getTime()-1000*3600*24*1;
			  Etime=new Date(lon);
			  year=Etime.getFullYear();
			  month=Etime.getMonth()+1;
			  day=Etime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Etime=year+'-'+month+'-'+day;
		  }else if(flag==4){
			  Stime=(date.getFullYear())+'-01-01';
		  }else if(flag==5){
			  Stime=year+'-'+month+'-01'
		  }else if(flag==7){
			  var lon=date.getTime()-1000*3600*24*15;
			  Stime=new Date(lon);
			  year=Stime.getFullYear();
			  month=Stime.getMonth()+1;
			  day=Stime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Stime=year+'-'+month+'-'+day; 
			  lon=date.getTime()-1000*3600*24*1;
			  Etime=new Date(lon);
			  year=Etime.getFullYear();
			  month=Etime.getMonth()+1;
			  day=Etime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Etime=year+'-'+month+'-'+day;
		  }else if(flag==8){
				var date=new Date();
				var year=date.getFullYear();
				var month=date.getMonth();
				if(month==0){
					year=year-1;
					month=12;
				}
				var startTime=year+'-'+month+'-01';
				$('#beginDate').val(startTime);
				Stime= startTime;
				var date=new Date(year,month,0);
				var endTime=year+'-'+month+'-'+date.getDate();
				$('#endDate').val(endTime);
				Etime=endTime;
			}else{
			  Stime=Etime;
		  }
		  $('#beginDate').val(Stime);
		  $('#endDate').val(Etime);
		  if(Stime!=null&&Stime!=""){
		  Stime=Stime+" 00:00:00";
		  }
		  if(Etime!=null&&Etime!=""){
		  Etime=Etime+" 23:59:59";
		  }
	 }
		$('#billSearchHzList').datagrid('reload',{
			stime:Stime,
			etime:Etime,
		})
	}
		//导出列表
		function exportList() {
			var endDate = $('#endDate').val();
			var beginDate = $('#beginDate').val();
			var rows = $('#billSearchHzList').datagrid('getRows');
			if(rows==null||rows==""){
				$.messager.alert("提示", "列表无数据,无法导出！");
				return;
			}
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {
						url :"<%=basePath%>statistics/DetailedDaily/expDetailedDailylist.action",
						queryParams:{stime:beginDate,etime:endDate},
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							$.messager.alert("操作提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					});
				}
			});
		}
		//报表打印
		function exportPDF() {
			var rowsCount=$('#billSearchHzList').datagrid('getRows')
			if(''!=rowsCount&&null!=rowsCount){
			var endDate = $('#endDate').val();
			var beginDate = $('#beginDate').val();
			window.open ("<c:url value='queryDetailedDailylistPDF.action?stime='/>"+beginDate+"&etime="+endDate,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}else{
				$.messager.alert('提示','列表无数据,无法打印！');
			}	
		}
		/**
		 * 合并单元格
		 */
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
	</script>
</body>
</html>