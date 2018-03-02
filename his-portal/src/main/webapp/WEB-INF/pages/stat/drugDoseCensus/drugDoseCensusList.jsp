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
<title>住院发药工作量统计</title>
</head>
<body style="margin: 0px;padding: 0px">
<div id="cc" class="easyui-layout" data-options="fit:true">
	
	<div data-options="region:'center',border:false">
		<div id="cc" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="height:38px;width: 100%">
				<table style="padding: 5px;width:100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td style="width: 160px;">日期：
						<input id="startData" class="Wdate" type="text" onClick="WdatePicker()" value="${startData}" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td style="width: 160px;">至
						<input id="endData" class="Wdate" type="text" onClick="WdatePicker()" value="${endData }" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						 </td>
						<td style="width: 240px;" class="middleSize">住院药房：<input class="easyui-combobox" id="drugstore" style="width:150px;"></td>
						<td style="width: 272px;" class="drugDoseCensusListSize">
						    <shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom(1)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:reset">
							<a href="javascript:clear();void(0)" onclick="" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:print">
							<a href="javascript:void(0)" onclick="experStamp()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:export">
							<a href="javascript:void(0)" onclick="exportDown()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
							</shiro:hasPermission>
						</td>
						<td  align="right">
						    <shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(4);void(0)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">当年</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(5);void(0)"  style="float: right;margin-left: 10px"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">当月</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(8);void(0)"  style="float: right;margin-left: 10px"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">上月</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(7);void(0)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">十五天</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(3);void(0)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">七天</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(2);void(0)"  style="float: right;margin-left: 10px"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">三天</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:searchFrom(6);void(0)"  style="float: right;margin-left: 10px"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="margin-left: 12px">当天</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" style="height:93%;">
				<table id="list" class="easyui-datagrid" style="width:100%;height:90%;" >   
					<thead>
						<tr>
							<th data-options="field:'bedName',width:'7%'">床号</th>
							<th data-options="field:'name',width:'7%'">姓名</th>
							<th data-options="field:'tradeName',width:'7%'">药品名称</th>
							<th data-options="field:'specs',width:'7%'">规格</th>
							<th data-options="field:'dfqFreq',width:'7%'">频次</th>
							<th data-options="field:'useName',width:'7%'">用法</th>
							<th data-options="field:'num',width:'7%'">总量</th>
							<th data-options="field:'minUnit',width:'7%'">最小单位</th>
							<th data-options="field:'drugDeptCode',width:'7%'">取药药房</th>
							<th data-options="field:'sendType',width:'7%'">发送类型</th>
							<th data-options="field:'billclassCode',width:'7%'">摆药单</th>
							<th data-options="field:'applyOpercode',width:'7%'">发送人</th>
							<th data-options="field:'applyDate',width:'7%'">发送时间</th>
							<th data-options="field:'printEmpl',width:'7%'">发药人</th>
							<th data-options="field:'applyDate',width:'7%'">发药时间</th>
							<th data-options="field:'validState',width:'7%'">有效性</th>
							<th data-options="field:'namePinyin',width:'7%'">拼音码</th>
							<th data-options="field:'nameWb',width:'7%'">五笔码</th>
							<th data-options="field:'notPrintDate',width:'7%'">是否摆药</th>
						</tr>
					</thead>
				</table>
				<form id="reportForm" method="post">
					<input type="hidden" id="reportJson" name="reportJson">
					<input type="hidden" id="startData1" name="startData">
					<input type="hidden" id="endData1" name="endData" > 
					<input type="hidden" id="deptName1" name="drugstore" > 
				</form>
			<form id="exportForm" method="post">
				<input type="hidden" id="exportJson" name="exportJson">
				
			</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var deptMap="";
 var startTime=$('#startData').val();
 var endTime=$('#endData').val();
	$(function(){
		//住院科室
		$("#drugstore").combobox({
			mode:'remote',
			url:'<%=basePath%>statistics/DrugDoseCensus/queryStoreDept.action',
			valueField:'id',    
			textField:'deptName'
		});
		
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			fitColumns:true,
			singleSelect:true,
			border:true,
			fit:true,
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			url:'<%=basePath%>statistics/DrugDoseCensus/queryDrugDoseCen.action',
			onLoadSuccess : function(data){
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
	});
	function printDateFormatter(value,row,index){
		var flag = "";
		if(value==null||value==''){
			flag = "未摆";
		}else{
			flag = "已摆";
		}
		return flag;
	}
	//查询
	function searchFrom(flag){
		var startTime=$('#startData').val();
		var endTime=$('#endData').val();
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
	 		Stime = $('#startData').val();
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
				$('#startData').val(startTime);
				Stime= startTime;
				var date=new Date(year,month,0);
				var endTime=year+'-'+month+'-'+date.getDate();
				$('#endData').val(endTime);
				Etime=endTime;
			}else{
			  Stime=Etime;
		  }
		  $('#startData').val(Stime);
		  $('#endData').val(Etime);
		  if(Stime!=null&&Stime!=""){
		  Stime=Stime+" 00:00:00";
		  }
		  if(Etime!=null&&Etime!=""){
		  Etime=Etime+" 23:59:59";
		  }
	 }
		//住院药房
		var drugstore=$("#drugstore").combobox('getValue');
		$('#list').datagrid('load',{
			startData:Stime,
			endData:Etime,
			drugstore:drugstore
		});
	}
	//导出
	function exportDown() {
		var rows = $('#list').datagrid('getRows');
		if(rows.length > 0){
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
		 		if (res) {
			$('#exportJson').val(JSON.stringify(rows));
			$('#exportForm').form('submit', {
				url :"<%=basePath%>statistics/DrugDoseCensus/expDrugDoseCensus.action",
				onSubmit : function(param) {
				
				},
				success : function(data) {
					$.messager.alert("操作提示", "导出失败！", "success");
				},
				error : function(data) {
					$.messager.alert("操作提示", "导出失败！", "error");
				}
			});
		 		}
			});
		}else{
			$.messager.alert("提示","当前统计无数据，无法打印！");
			setTimeout(function(){$(".messager-body").window('close')},1500);
			return;
		}
	}
	//打印方法  
	function experStamp() {
		var rows = $('#list').datagrid('getRows');
		if(rows!=null&&rows.length > 0){
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
		 		if (res) {
		 		$("#startData1").val($('#startData').val());
		 		$('#endData1').val($('#endData').val());
				$('#reportJson').val(JSON.stringify(rows));
				$('#deptName1').val($("#drugstore").combobox('getText'))
				//表单提交 target
				var formTarget="reportForm";
				var tmpPath = "<%=basePath%>statistics/DrugDoseCensus/printDrugDoseCensus.action";
				//设置表单target
				$("#reportForm").attr("target",formTarget);
				//设置表单访问路径
				$("#reportForm").attr("action",tmpPath); 
				//表单提交时打开一个空的窗口
				$("#reportForm").submit(function(e){
					var timerStr = Math.random();
					window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
					window.close();
				});
				$("#reportForm").submit();
		 		}
			});
		}else{
			$.messager.alert("提示","当前统计无数据，无法打印！");
			setTimeout(function(){$(".messager-body").window('close')},1500);
			return;
		}
	}
	function clear(){
		$('#startData').val(startTime);
		$('#endData').val(endTime);
		$('#drugstore').combobox('setValue','');
		searchFrom(1);
	}
	//科室
	$.ajax({
		url: "<%=basePath%>publics/consultation/querydeptComboboxs.action", 
		success: function(deptData) {
			deptMap = deptData;
		}
	});
	/**
	*科室渲染
	*/
	function functiondept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>