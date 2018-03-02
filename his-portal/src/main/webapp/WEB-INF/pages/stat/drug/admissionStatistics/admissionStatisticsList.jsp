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
<title>住院用药统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
//全局变量

$(function(){
	//开始时间，结束时间
	$('#startTime').datebox({
	    onSelect: function(date){
	    	$("#startTime2").textbox('setValue',$("#startTime").textbox('getValue'));
	    	var stime = $("#startTime").textbox('getValue');
	    	var etime = $("#endTime").textbox('getValue');
	    	if(etime!=""){
	    		if(stime>etime){
	    			$.messager.alert('提示',"开始时间不能大于结束时间！");
	    			$("#startTime").datebox('setValue',"");
	    			$("#startTime2").textbox('setValue',"");
	    		}else{
	    			$("#startTime2").textbox('setValue',stime);
	    		}
	    	}else{
	    		$("#startTime2").textbox('setValue',stime);
	    	}
	    }
	});
	$('#endTime').datebox({
	    onSelect: function(date){
	    	var stime = $("#startTime").textbox('getValue');
	    	var etime = $("#endTime").textbox('getValue');
	    	if(stime!=""){
	    		if(etime<stime){
	    			$.messager.alert('提示',"结束时间不能小于开始时间！");
	    			$("#endTime").datebox('setValue',"");
	    			$("#endTime2").textbox('setValue',"");
	    		}else{
	    			$("#endTime2").textbox('setValue',$("#endTime").textbox('getValue'));
	    		}
	    	}else{
	    		$("#endTime2").textbox('setValue',$("#endTime").textbox('getValue'));
	    	}
	    }
	});
	setTimeout(function(){
		//加载数据表格
		$("#operaArrangeListData").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{startTime:null,endTime:null,deptCode:null,storageCode:null,drugType:null,outType:null},
			url:'<%=basePath %>statistics/AdmissionStatistics/queryAdmissionStatistics.action'
			
		});
	},300)
	
	
	 //药品类型
	 $('#drugType').combobox({
	 	url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugType",
	 	valueField : 'encode',
	 	textField : 'name',
	 	multiple : false,
	 	editable : false
	 });
	 //药房查询
	 $('#deptCode').combobox({
	 	url : '<%=basePath %>/statistics/AdmissionStatistics/querySysYaoFang.action',
	 	valueField : 'deptCode',
	 	textField : 'deptName',
	 	onSelect: function(date){
			$("#deptCode2").textbox('setValue',date.deptName);
		}
	 });
	//入库科室
	$("#storageCode").combobox({
		url : '<%=basePath %>/statistics/AdmissionStatistics/querySysYaoFang.action',
		valueField:'deptCode',    
	    textField:'deptName',
	   
	});
	//出库类型
	$('#outType').combobox({
		valueField: 'id',    
        textField: 'text',
		data:[{
					id:'10',
					text:'内部入库'
				},{
					id:'1',
					text:'一般出库'
				},{
					id:'2',
					text:'出库退库申请'
				},{
					id:'3',
					text:'出库退库审核'
				},{
					id:'4',
					text:'出库审批'
				},{
					id:'5',
					text:'报损'
				},{
					id:'6',
					text:'价让出库'
				},{
					id:'7',
					text:'特殊'
				},{
					id:'8',
					text:'各科室领药'
				},{
					id:'9',
					text:'特殊出库'
			}]
	});
	
})	
	
	function query(){
		var startTime= $('#startTime').datebox('getValue');
		var endTime= $('#endTime').datebox('getValue');
 		var drugType= $('#drugType').combobox('getValue');
		var outType= $('#outType').combobox('getValue');
		var deptCode= $('#deptCode').combobox('getValue');
		var storageCode= $('#storageCode').combobox('getValue');
		$("#operaArrangeListData").datagrid('load',{beginTime:startTime,endTime:endTime,
			drugType:drugType,deptCode:deptCode,storageCode:storageCode,outType:outType});
	}
	//导出列表
	function exportList() {
		var startTime= $('#startTime').datebox('getValue');
		var endTime= $('#endTime').datebox('getValue');
 		var drugType= $('#drugType').combobox('getValue');
		var outType= $('#outType').combobox('getValue');
		var deptCode= $('#deptCode').combobox('getValue');
		var storageCode= $('#storageCode').combobox('getValue'); 
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				$('#saveForm').form('submit', {
					url :"<%=basePath%>statistics/AdmissionStatistics/outAdmissionStatisticsVo.action",
					onSubmit : function(param) {
						param.startTime=startTime;
						param.endTime=endTime;
						param.drugType=drugType;
						param.outType=outType;
						param.deptCode=deptCode;
						param.storageCode=storageCode;
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
	
	function edit(){
		$.messager.confirm('住院部用药统计', '是否打印住院部用药统计?', function(res) {  //提示是否打印假条
			if (res){
				var startTime= $('#startTime').datebox('getValue');
				var endTime= $('#endTime').datebox('getValue');
		 		var drugType= $('#drugType').combobox('getValue');
				var outType= $('#outType').combobox('getValue');
				var deptCode= $('#deptCode').combobox('getValue');
				var storageCode= $('#storageCode').combobox('getValue'); 
				var timerStr = Math.random();
				window.open ("<c:url value='/iReport/iReportPrint/iReportAdmissionStatistics.action?randomId='/>"+timerStr+"&startTime="+startTime+"&endTime="+endTime+"&drugType="+drugType+"&outType="+outType+"&deptCode="+deptCode+"&storageCode="+storageCode+"&fileName=ZYBYYCXTJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
				};
		 })
	}
</script>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="cc" class="easyui-layout" data-options="fit:true"> 
			<div id="p" data-options="region:'north',border:false" style="width:100%;height:10%;">
				<div id="toolbarId" style="padding:5px 5px 5px 5px;">
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:print">   
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">导出</a> 
					</shiro:hasPermission>
				</div>
				<div style="padding:5px 5px 5px 5px;">
					开始时间：<input class="easyui-datebox" id="startTime" name="startTime"   style="width:200px" />
					结束时间：<input class="easyui-datebox" id="endTime" name="endTime"   style="width:200px" />
					药品类别：<input class="easyui-combobox" id="drugType" name="drugType"   style="width:200px" />
					<a href="javascript:delSelectedData('drugType');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
					出库类型：<input class="easyui-combobox" id="outType" name="outType"   style="width:200px" />
					<a href="javascript:delSelectedData('outType');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
					出库药房：<input class="easyui-combobox" id="deptCode" name="deptCode"   style="width:200px" />
					<a href="javascript:delSelectedData('deptCode');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
					入库科室：<input class="easyui-combobox" id="storageCode" name="storageCode"   style="width:200px" />
					<a href="javascript:delSelectedData('storageCode');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;height: 90%;">
				<div id="c" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false" style="width: 100%;height: 35px;padding:5px">
						药房名称：<input class="easyui-textbox" id="deptCode2" style="width:200px" />
						统计日期：<input class="easyui-textbox" id="startTime2" style="width:200px" />至<input class="easyui-datebox" id="endTime2"  style="width:200px" />
						
					</div>
					<div data-options="region:'center',border:false" style="width: 100%;height: 94%;">
						<table id="operaArrangeListData" class="easyui-datagrid" style="padding:5px 5px 5px 5px;">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th>
									<th data-options="field:'drugBasicCode',width:'9%',halign:'center'">基本药物码</th>
									<th data-options="field:'drugBiddingCode',width:'6%',halign:'center'">招标流水码</th>
									<th data-options="field:'drugId',width:'12%',halign:'center'">药品编码</th>
									<th data-options="field:'drugName',width:'9%',halign:'center'">药品名称</th>
									<th data-options="field:'outState',width:'9%',halign:'center'">当前状态</th>
									<th data-options="field:'optype',width:'9%',halign:'center'">摆药状态</th>
									<th data-options="field:'drugSpec',width:'9%',halign:'center'">规格</th>
									<th data-options="field:'num',width:'9%',halign:'center'">数量</th>
									<th data-options="field:'drugPackgingUnit',width:'9%',halign:'center'">单位</th>
									<th data-options="field:'retailPrice',width:'9%',halign:'center'">单价</th>
									<th data-options="field:'sum',width:'9%',halign:'center'">金额</th>
								</tr>
							</thead>
						</table>
						<form id="saveForm" method="post"/>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>