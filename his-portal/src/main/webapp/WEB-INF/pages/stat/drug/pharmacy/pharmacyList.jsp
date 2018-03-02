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
<title>住院部取药统计</title>
</head> 
<body style="margin: 0px;padding: 0px">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height:72px;padding-top:8px;padding-right:8px;padding-left:8px">
			<div>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:void(0)" onclick="searchFrom(1)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
 				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:set">
				<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
 				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:print">
				<a href="javascript:void(0)" onclick="experStamp()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
 				</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="exportDown()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
				<a href="javascript:searchFrom(4);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>
				<a href="javascript:searchFrom(5);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
				<a href="javascript:searchFrom(8);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>
				<a href="javascript:searchFrom(7);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>
				<a href="javascript:searchFrom(3);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>
				<a href="javascript:searchFrom(2);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>
				<a href="javascript:searchFrom(6);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
			</div>
			<div style="margin-top: 8px;">
				日期：
				<input id="startData" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				至
				<input id="endData" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>&nbsp;&nbsp;
				药品类别：<input class="easyui-combobox" id="drugCostType" style="width:100px">&nbsp;&nbsp;
				住院药房：<input class="easyui-combobox" id="drugstore" style="width:130px">
			</div>
		</div>
		<div data-options="region:'center',border:false" style="height:85%;margin-top: 0px;" align="center">
			<div id="cc" class="easyui-layout" data-options="fit:true">   
			    <div data-options="region:'north',border:false" style="height:50px;display:none" align="center">
			    	<form id="exportForm" method="post"/>
			    	<font style="font-size: 25">郑州大学第一附属医院住院部取药统计表</font>
					<br></br>
			    </div>   
			    <div data-options="region:'center'" style="">
			    	<table id="list" class="easyui-datagrid" data-options="">
						<thead>
							<tr>
								<th data-options="field:'drugDeptCode',width:'10%',formatter:deptformatter" align="center" >住院药房</th>
								<th data-options="field:'drugName',width:'10%'" align="center" >药品名称</th>
								<th data-options="field:'outState',width:'10%'" align="center" >当前状态</th>
								<th data-options="field:'outType',width:'10%'" align="center" >摆药状态</th>
								<th data-options="field:'spec',width:'10%'" align="center" >规格</th>
								<th data-options="field:'outNum',width:'10%'" align="right" halign="center">数量</th>
								<th data-options="field:'drugPackagingunit',width:'10%'" align="center" >单位</th>
								<th data-options="field:'drugRetailprice',width:'10%'" align="right" halign="center">单价</th>
								<th data-options="field:'outlCost',width:'10%'" align="right" halign="center">金额</th>
							</tr>
						</thead>
					</table>
			    </div>   
			</div> 
		</div>
	</div>
<script type="text/javascript">
	var deptMap;
		$.ajax({
			url: "<%=basePath%>publics/consultation/querydeptComboboxs.action", 
			success: function(deptData) {
				deptMap = deptData;
			}
		});
	function clear(){
		$('#startData').val("${sTime}");
		$('#endData').val("${eTime}");
		$('#drugCostType').combobox('setValue',"");
		$('#drugstore').combobox('setValue',"");
		searchFrom(1);
	}
	$(function(){
		$('#startData').val("${sTime}");
		$('#endData').val("${eTime}");
		//要费类别
		$("#drugCostType").combobox({
			mode:'remote',
			url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugType",
			valueField:'encode',
			textField:'name'
		});
		//住院药房
		$("#drugstore").combobox({
			mode:'remote',
			url:'<%=basePath%>statistics/DrugDoseCensus/queryStoreDept.action',
			valueField:'id',    
			textField:'deptName'
		});
		//列表显示
		$("#list").datagrid({
			border:false,
			fit:true,
			fitColumns:true,
			singleSelect:true,
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			pagination:true,pageSize:20,pageList:[20,40,60,80,100],
			url:"<%= basePath %>statistics/DrugPharmacy/queryDrugPharmacyNew.action",
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
		//药费类别
		var drugCostType=$("#drugCostType").combobox('getValue');
		//住院药房
		var drugstore=$("#drugstore").combobox('getValue');
		$('#list').datagrid('load',{
			startData:Stime,
			endData:Etime,
			drugCostType:drugCostType,
			drugstore:drugstore,
		})
	}
	//打印方法  
	function experStamp() {
		var rowsCount=$('#list').datagrid('getRows');
		if(''!=rowsCount&&null!=rowsCount){
		//开始时间
		var firstTime=$("#startData").val();
		//结束时间
		var endTime=$("#endData").val();
		//药费类别
		var drugCostType=$("#drugCostType").combobox('getValue');
		//住院药房
		var drugstore=$("#drugstore").combobox('getValue');
		$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
			if (res) {
				window.open ("queryDrugPharmacyPDF.action?startData="+firstTime+"&endData="+endTime+"&drugCostType="+drugCostType+"&drugstore="+drugstore,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}
		});
		}else{
			$.messager.alert('提示','列表无数据，无法打印！');
		}
	}
	//导出
	function exportDown() {
		//开始时间
		var startData=$("#startData").val();
		//结束时间
		var endData=$("#endData").val();
		//药费类别
		var drugCostType=$("#drugCostType").combobox('getValue');
		//住院药房
		var drugstore=$("#drugstore").combobox('getValue');
		var rows = $('#list').datagrid('getRows');
		if(rows==null||rows==""){
			$.messager.alert("提示", "列表无数据,无法导出！");
			return;
		}
		$.messager.confirm('提示','确定要导出吗？',function(res){
			if(res){
				$("#exportForm").form('submit',{
					url:"<%=basePath%>statistics/DrugPharmacy/expDrugPharmacy.action",
					queryParams:{startData:startData,endData:endData,
						drugCostType:drugCostType,drugstore:drugstore},
					success : function(data) {
						$.messager.alert("操作提示", "导出成功！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "导出失败！", "error");
					}
				})
			}
		});
	}
	function deptformatter(value,row,index){
		if(null!=value&&''!=value){
			return deptMap[value];
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>