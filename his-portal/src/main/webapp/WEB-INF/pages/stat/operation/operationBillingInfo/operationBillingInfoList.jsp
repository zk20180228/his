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
<title>手术计费信息汇总</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
//全局变量
var deptMap=null;	   //科室信息map
var opDocMap = new Map();
var doctorMap=null;
var beg="${beginTime}";
var end="${endTime}";
var inStates="";
var opStates="";
var feeStates="";
$(function(){
	//选择科室
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#m2",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		firsturl:"<%=basePath%>statistics/OperationBillingInfo/getDeptList.action?deptTypes=", //获取列表的url，必须要写
		relativeInput:".doctorInput",	//与其级联的文本框，必须要写
		relativeDropmenu:"#m3"	//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
	});
	$(".deptInput1").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#mm",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		firsturl:"<%=basePath%>statistics/OperationBillingInfo/getDeptList.action?deptTypes=", //获取列表的url，必须要写
		relativeInput:".doctorInput",	//与其级联的文本框，必须要写
		relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
	});
	//选择医生
	$(".doctorInput").MenuList({
		width :530,
		height :400,
		dropmenu:"#m3",//弹出层
		isSecond:true,	//是否是二级联动的第二级
		firsturl: "<%=basePath %>statistics/OperationBillingInfo/getDoctorList.action?deptTypes=", //获取列表的url，必须要写
		relativeInput:".deptInput",	//与其级联的文本框
		relativeDropmenu:"#m2"
	});
	$('#makeSure').click(function(){
		var texts=new Array();
		var iflag = 0;
		$("#m2 .select-info li").each(function(key,value){					
			if($(this).attr("rel") != "none"){
				texts[iflag] = $(this).html();
				iflag++;
			}
		}); 
		$("#execDept2").text(texts);
	});
	
	$('#clearId').click(function(){
		$("#execDept2").text("");
	});
	$.ajax({
		url: "<c:url value='/statistics/OperationCost/getDeptMap.action'/>",
		type:'post',
		success: function(date) {
			deptMap = date;
		}
	});

	$.ajax({//渲染医生
		url : '<%=basePath %>statistics/OperationBillingInfo/getEmpMap.action',
		type:"post",
		success : function(data){
			doctorMap=data;
		}
	});
	

	//计费状态
	$('#feeState').combobox({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=operationFeeState',
		valueField: 'encode',    
        textField: 'name',
        onLoadSuccess:function(data){
        	feeStates=data;
        }

	});
	
	//手术状态
	$('#opState').combobox({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=operationState',
		valueField: 'encode',    
        textField: 'name',
        onLoadSuccess:function(data){
        	opStates=data;
        }
	});
	
	//在院状态
	$('#inState').combobox({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=inState',
		valueField: 'encode',    
        textField: 'name',
        onLoadSuccess:function(data){
        	inStates=data;
        },
        onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].encode) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox("clear");
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
		},
        filter:function(q,row){//hedong 20170309 增加科室过滤
			 var keys = new Array();
			 keys[keys.length] = 'encode';//code
			 keys[keys.length] = 'name';//名称
			 keys[keys.length] = 'pinyin';//拼音
			 keys[keys.length] = 'wb';//五笔
		     keys[keys.length] = 'inputCode';//自定义码
			return filterLocalCombobox(q, row, keys);
		}
        
	});
	
	//加载数据表格
	$("#operaBillingListData").datagrid({
		url:'<%=basePath %>statistics/OperationBillingInfo/queryOperationBillingInfo.action',
		queryParams:{beginTime:null,endTime:null,feeBegTime:beg,feeEndTime:end,opStatus:null,
			feeState:null,inState:null,opDoctor:null,opDoctordept:null,execDept:null,identityCard:null},
		pageSize:20,
		pageList:[20,30,50,80,100],
		pagination:true,
		idField:'id',
		border:true,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fitColumns:false,
		pagination:true,
		fit:true,
		onLoadSuccess:function(row, data){
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
			}}
	});
	

	/**
	 * 下拉框过滤
	 * @param q
	 * @param row
	 * @param keys Array型
	 * @return
	 */
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}

})	
	
	
	//医生
	function functiondoc(value,row,index){
		if(value!=null&&value!=''){
			return doctorMap[value];
		}
	}
	//渲染科室		
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value]; 
		}
	}
	//在院状态
	function functioninState(value){
		for(var i=0;i<inStates.length;i++){
			if(inStates[i].encode==value){
				return inStates[i].name;
			}
		}
	}
	//手术状态
	function functionopState(value){
		for(var i=0;i<opStates.length;i++){
			if(opStates[i].encode==value){
				return opStates[i].name;
			}
		}
	}
	//计费状态
	function functionfeeState(value){
		for(var i=0;i<feeStates.length;i++){
			if(feeStates[i].encode==value){
				return feeStates[i].name;
			}
		}
		
	}
	//格式化诊断名称
	function funcdiagName(value,row,index){
		if(value){
			return value;
		}
	}
	
	//查询方法
	function find(){
		var beginTime= $('#beginTime').val();
		var endTime= $('#endTime').val();
		var feeBeginTime= $('#feeBeginTime').val();
		var feeEndTime= $('#feeEndTime').val();
		var execDept= $('#execDept').getMenuIds();
		var opDoctor= $('#opDoctor').getMenuIds();
		var opDoctordept= $('#opDoctorDept').getMenuIds();
		var inState= $('#inState').combobox('getValue');
		var opStatus= $('#opState').combobox('getValue');
		var feeState= $('#feeState').combobox('getValue');
		var identityCard= $('#identityCard').textbox('getValue').trim();
		if(beginTime&&endTime){
			if(beginTime>endTime){
				$.messager.alert("提示","预约开始时间不能大于结束时间！");
				return ;
			}
		}
		if(feeBeginTime&&feeEndTime){
			if(feeBeginTime>feeEndTime){
				$.messager.alert("提示","批费的开始时间不能大于结束时间！");
				return ;
			}
		}
		 $("#operaBillingListData").datagrid('load',{beginTime:beginTime,endTime:endTime,feeBegTime:feeBeginTime,feeEndTime:feeEndTime,opStatus:opStatus,
			feeState:feeState,inState:inState,opDoctor:opDoctor,opDoctordept:opDoctordept,execDept:execDept,identityCard:identityCard});
	}
	//导出列表
	function exportList() {
		 var beginTime= $('#beginTime').val();
		var endTime= $('#endTime').val();
		var feeBeginTime= $('#feeBeginTime').val();
		var feeEndTime= $('#feeEndTime').val();
		var execDept= $('#execDept').getMenuIds();
		var opDoctor= $('#opDoctor').getMenuIds();
		var opDoctordept= $('#opDoctorDept').getMenuIds();
		var inState= $('#inState').combobox('getValue');
		var opStatus= $('#opState').combobox('getValue');
		var feeState= $('#feeState').combobox('getValue');
		var identityCard= $('#identityCard').textbox('getValue').trim();
		var data=$("#operaBillingListData").datagrid('getData');
		if(data.total==0){
			$.messager.alert("友情提示", "列表无数据，无法导出");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				$('#saveForm').form('submit', {
					url :"<%=basePath%>statistics/OperationBillingInfo/outOperationBillingInfoVo.action",
					onSubmit : function(param) {
						param.beginTime=beginTime;
						param.endTime=endTime;
						param.feeBegTime=feeBeginTime;
						param.feeEndTime=feeEndTime;
						param.opStatus=opStatus;
						param.feeState=feeState;
						param.inState=inState;
						param.opDoctor=opDoctor;
						param.opDoctordept=opDoctordept;
						param.execDept=execDept;
						param.identityCard=identityCard;
					},
					success : function(data) {
						$.messager.alert("操作提示", "导出成功！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "导出失败！", "error");
					}
				})
			}o
		});  
	}
	//打印
	function edit(){
		var beginTime= $('#beginTime').val();
		var endTime= $('#endTime').val();
		var feeBeginTime= $('#feeBeginTime').val();
		var feeEndTime= $('#feeEndTime').val();
		var execDept= $('#execDept').getMenuIds();
		var opDoctor= $('#opDoctor').getMenuIds();
		var opDoctordept= $('#opDoctorDept').getMenuIds();
		var inState= $('#inState').combobox('getValue');
		var opStatus= $('#opState').combobox('getValue');
		var feeState= $('#feeState').combobox('getValue');
 		var identityCard= $('#identityCard').textbox('getValue').trim();
 		var data=$("#operaBillingListData").datagrid('getData');
		if(data.total==0){
			$.messager.alert("友情提示", "列表无数据，无法打印");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		$.messager.confirm('打印住院手术费计费信息汇总', '是否打印手术计费信息汇总信息?', function(res) {  //提示是否打印假条
			if (res){
				var timerStr = Math.random();
				window.open ("<c:url value='/iReport/iReportPrint/iReportOperationMillingInfo.action?randomId='/>"+timerStr+"&beginTime="+beginTime+"&endTime="+endTime+"&identityCard="+identityCard+"&feeBegTime="+feeBeginTime+"&feeEndTime="+feeEndTime+"&execDept="+execDept+"&opDoctor="+opDoctor+"&opDoctordept="+opDoctordept+"&inState="+inState+"&opStatus="+opStatus+"&feeState="+feeState+"&fileName=SSJFXXHZ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
				};
		 })
	}
	
	/**
	 * @Description:清空所有查询条件
	 */
	function clearQuery(){
		$("a[name='menu-confirm-clear']").click();
		 $('#beginTime').val('');
		 $('#endTime').val('');
		 $("#feeEndTime2").text(end);
		 $("#feeBeginTime2").text(beg);
		 $("#identityCard").textbox('setValue','');
		 $('#feeBeginTime').val(beg);
		 $('#feeEndTime').val(end);
		 $('#inState').combobox('setValue','');
		 $('#opState').combobox('setValue','');
		 $('#feeState').combobox('setValue','');
		 $('#identityCard').textbox('setValue','');
		 $("#execDept2").text('');
		 find();
	}
		
	//快速查询	
	function onclickDay(flg){
		if(flg=='1'){
			var st=beforeDay(0);
			var et=beforeDay(0);
			 $('#feeBeginTime').val(st)
			 $("#feeEndTime").val(et);
			 $("#feeEndTime2").text(et);
			 $("#feeBeginTime2").text(st);
			find();
		}else if(flg=='2'){
			var st=beforeDay(3);
			var et=beforeDay(1);
			 $('#feeBeginTime').val(st)
			 $("#feeEndTime").val(et);
			 $("#feeEndTime2").text(et);
			 $("#feeBeginTime2").text(st);
			find();
		}else if(flg=='3'){
			var st=beforeDay(7);
			var et=beforeDay(1);
			var start= st;
			var end= et;
			 $('#feeBeginTime').val(st)
			 $("#feeEndTime").val(et);
			 $("#feeEndTime2").text(et);
			 $("#feeBeginTime2").text(st);
			find();
		}else if(flg=='4'){
			var st=beforeDay(15);
			var et=beforeDay(1);
			 $('#feeBeginTime').val(st)
			 $("#feeEndTime").val(et);
			 $("#feeEndTime2").text(et);
			 $("#feeBeginTime2").text(st);
			find();
		}else if(flg=='5'){
			var myDate  = new Date();
			var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
			 var year = myDate.getFullYear();
			 var et=beforeDay(0);
			 var start= year+"-"+month+"-01";
			 $('#feeBeginTime').val(start)
			 $("#feeEndTime").val(et);
			 $("#feeEndTime2").text(et);
			 $("#feeBeginTime2").text(start);
			 find();
		}else if(flg=='7'){
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
			var login = year + "-" + month + "-" + "01";//上个月的第一天
			var lastDate = new Date(year, month, 0);
			var end = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天

			 $('#feeBeginTime').val(login)
			 $("#feeEndTime").val(end);
			 $("#feeEndTime2").text(end);
			 $("#feeBeginTime2").text(login);
			 find();
		}else{
			var myDate  = new Date();
			var start= myDate.getFullYear()+"-01-01";
			 var et=beforeDay(0);
			 $('#feeBeginTime').val(start)
			 $("#feeEndTime").val(et);
			 $("#feeEndTime2").text(et);
			 $("#feeBeginTime2").text(start);
			find();
		}
	}
	
	function beforeDay(beforeDayNum) {
		var d = new Date();
		var endDate = dateToString(d);
		d = d.valueOf();
		d = d - beforeDayNum * 24 * 60 * 60 * 1000;
		d = new Date(d);
		var startDate = dateToString(d);
		return startDate;
	}
	function dateToString(d) {
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var d = d.getDate();
		if (m.toString().length == 1) m = "0" + m;
		if (d.toString().length == 1) d = "0" + d;
		return y + "-" + m + "-" + d;
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="cc" class="easyui-layout" data-options="fit:true"> 
			<div id="p" data-options="region:'north',border:false" style="width: 100%;height:93px;">
				<div id="toolbarId" style="height:24px;padding: 5px 5px 5px 5px  ">
				<shiro:hasPermission name="${menuAlias}:function:query"> 
					<a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false">查询</a>
				</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard"> 
						<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					 </shiro:hasPermission>
			        <shiro:hasPermission name="${menuAlias}:function:readIdCard"> 
			        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					 </shiro:hasPermission> 
					 <shiro:hasPermission name="${menuAlias}:function:print"> 
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',plain:false">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:false">导出</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="clearQuery()" class="easyui-linkbutton" iconCls="reset">重置</a> 
					<a href="javascript:onclickDay('6')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>      
					<a href="javascript:onclickDay('5')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
					<a href="javascript:onclickDay('7')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>     
					<a href="javascript:onclickDay('4')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>    
					<a href="javascript:onclickDay('3')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>    
					<a href="javascript:onclickDay('2')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>  
					<a href="javascript:onclickDay('1')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
				</div>
				<div style="height:45px border-top: 1px;padding: 2px 5px 3px 5px; ">
					<table>
						<tr class="operationBillingInfoList">
							<td  style="text-align: right;" nowrap='true'>预约时间：</td>
							<td colspan="3" nowrap='true'>
							<input id="beginTime" class="Wdate" type="text"  onClick="WdatePicker()" style="width:120px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/> 
							 至 
							<input id="endTime" name="endTime" class="Wdate" type="text"  onClick="WdatePicker()" style="width:120px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="text-align: right;" nowrap='true'>&nbsp;在院状态：</td>
							<td><input class="easyui-combobox" id="inState"   style="width:120px" /></td>
							
<!-- 							<td><input class="easyui-combobox" id="execDept"   style="width:120px" /></td> -->
							
<!-- 							<td><input class="easyui-combobox" id="opDoctorDept"    style="width:120px" /></td> -->
							<td style="text-align: right;" nowrap='true'>&nbsp;&nbsp;执行科室：</td>
							<td style="width:150px;" class="newMenu">
					    	       	<div class="deptInput menuInput" style="margin-top:0px"><input class="ksnew" id="execDept" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">取消</span>
											</a>						
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn" id="clearId">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text" id="makeSure">确定</span>
											</a>
					    	       		</div>
										<div class="select-info" style="display:none">	
											<label class="top-label">已选部门：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											 <div class="addList" ></div>
											<div class="tip" style="display:none">没有检索到数据</div>	
										</div>	
									</div>
							</td>
							<td style="text-align: right;" nowrap='true'>&nbsp;&nbsp;医生科室：</td>
							<td style="width:150px;" class="newMenu">
					    	       	<div class="deptInput1 menuInput" style="margin-top:0px"><input class="ksnew" id="opDoctorDept" readonly="readonly"/><span></span></div> 
					    	       	<div id="mm" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
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
										<div class="select-info" style="display:none">	
											<label class="top-label">已选部门：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											 <div class="addList" ></div>
											<div class="tip" style="display:none">没有检索到数据</div>	
										</div>	
									</div>
							</td>
						</tr>
						<tr style="height: 5px;">
							<td></td><td></td><td></td> <td></td> <td></td> <td></td> <td></td> <td></td> <td></td><td></td><td></td><td></td>
						</tr>
						<tr>
							<td></td><td></td><td></td> <td></td> <td></td> <td></td> <td></td> <td></td> <td></td><td></td><td></td><td></td>
						</tr>
						<tr>
							<td style="text-align: right;" nowrap='true'>批费时间：</td>
							<td colspan="3" nowrap='true'>
							<input id="feeBeginTime" value="${beginTime}" class="Wdate" type="text"  onClick="WdatePicker({onpicked:feeBeginTimeFun})" style="width:120px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							至
							<input id="feeEndTime"  name="endTime" value="${endTime}" class="Wdate" type="text"  onClick="WdatePicker({onpicked:feeEndTimeFun})" style="width:120px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="text-align: right;" nowrap='true'>&nbsp;&nbsp;手术状态：</td>
							<td><input class="easyui-combobox" id="opState"    style="width:120px" /></td>
							<td style="text-align: right;" nowrap='true' >&nbsp;&nbsp;计费状态：</td>
							<td><input class="easyui-combobox" id="feeState"    style="width:150px" /></td>
							<td style="text-align: right;" nowrap='true'>&nbsp;&nbsp;手术医生：</td>
							<td style="width:120px;" class="newMenu">
					    	       	<!-- <input id="zj" name="expxrt" class="easyui-combobox" data-options="valueField:'jobNo',textField:'name',multiple:true"/>  -->
					    			<div class="doctorInput menuInput" style="margin-top:0px"><input class="ksnew" id="opDoctor" readonly="readonly"/><span></span></div> 
					    	       	<div id="m3" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName1" placeholder="回车查询"/>
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
											<label class="top-label">已选部门：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
										    <div class="addList" ></div>
											<div class="tip" style="display:none">没有检索到数据</div>	
										</div>
												 
									</div>
					    		</td>
<!-- 							<td><input class="easyui-combobox" id="opDoctor"    style="width:120px" /></td> -->
							<td style="text-align: right;" nowrap='true'>&nbsp;身份证号：</td>
							<td  nowrap='true'>
							<input class="easyui-textbox" id="identityCard" name="identityCard"   style="width:120px" />
							</td>
							
						</tr>
						
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:true" style="width: 100%;height: 85%;">
				<div id="c" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false" style="width: 100%;height:92px;padding: 3px">
						<div style="text-align: center; width: 85%;">
				    	<h5 style="font-size: 30;font: bold;">手术计费信息汇总</h5>
						</div>
						<div style="width: 100%;overflow-x:auto;">
							<table style="width: 99%;padding: 3px">
								<tr>
									<td style="width:214px;font-size: 18;padding-left: 10px" class="operbilling" nowrap='true'>批费时间： <span id="feeBeginTime2" style="font-size: 18">${beginTime}</span></td>
									<td style="width: 214px;font-size: 18" class="operbilling" nowrap='true'>至&nbsp; <span style="text-align:left;width: 150px;font-size: 18" id="feeEndTime2">${endTime}</span></td>
									<td style="text-align: left;width: 270px;font-size: 18" nowrap='true'></td>
									<td style="text-align: left;width: 270px;font-size: 18" nowrap='true'></td>
									<td style="width: 180px;text-align: right;font-size: 18" class="operbilling" nowrap='true'>统计科室：</td>
									<td nowrap='true'>
										<span style="font-size: 18" id="execDept2" class="operbilling" ></span ></td>
									<td ></td>
								</tr>
							</table>
						</div>
					</div>
					<div data-options="region:'center',border:false" style="width: 100%;height: 94%;padding-top: 9px;">
						<table id="operaBillingListData" class="easyui-datagrid" style="padding:5px 5px 5px 5px;">
							<thead>
								<tr>
									<th data-options="field:'patientNo',width:'10%',halign:'center',align:'center'">病历号</th>
									<th data-options="field:'diagName',width:'10%',halign:'center',align:'center',formatter:funcdiagName" >诊断</th>
									<th data-options="field:'name',width:'10%',halign:'center',align:'center'">患者姓名</th>
									<th data-options="field:'preDate',width:'10%',halign:'center',align:'center',formatter:functionPreDate">预约手术日期</th>
									<th data-options="field:'opDoctor',width:'10%',halign:'center',align:'center'">手术医生</th>
									<th data-options="field:'opDoctordept',width:'10%',halign:'center',align:'center'">医生科室</th>
									<th data-options="field:'inState',width:'10%',halign:'center',align:'center',formatter:functioninState">在院状态</th>
									<th data-options="field:'opStatus',width:'10%',halign:'center',align:'center',formatter:functionopState">手术状态</th>
									<th data-options="field:'feeState',width:'10%',halign:'center',align:'center',formatter:functionfeeState">计费状态</th>
									<th data-options="field:'feeDate',width:'10%',halign:'center',align:'center'">收费时间</th>
								</tr>
							</thead>
						</table>
						<form id="saveForm" method="post"/>
					</div>
				</div>
			</div>
		</div>
	</body>
<script type="text/javascript">
//hedong 20170322 手术批费时间   更换控件后重新绑定事件
function feeBeginTimeFun(){
	var fbtime = $("#feeBeginTime").val();
	var fetime = $("#feeEndTime").val();
	if(fetime!=""){
		if(fbtime>fetime){
			$.messager.alert('提示',"手术批费开始时间不能大于结束时间！");
			$("#feeBeginTime").val("");
			$("#feeBeginTime2").text("");
		}else{
			$("#feeBeginTime2").text(fbtime);
		}
	}else{
		$("#feeBeginTime2").text(fbtime);
	}
}
//hedong 20170322 手术批费时间 更换控件后重新绑定事件
function feeEndTimeFun(){
	var fbtime = $("#feeBeginTime").val();
	var fetime = $("#feeEndTime").val();
	if(fbtime!=""){
		if(fetime<fbtime){
			$.messager.alert('提示',"手术批费结束时间不能小于开始时间！");
			$("#feeEndTime").val("");
			$("#feeEndTime2").text("");
		}else{
			$("#feeEndTime2").text(fetime);
		}
	}else{
		$("#feeEndTime2").text(fetime);
	}
}

//格式化预约时间
function functionPreDate(value,row,index){
	if(value!=null&&value!=''){
		var newDate=/\d{4}-\d{1,2}-\d{1,2} \d{1,2}:\d{1,2}/g.exec(value);
		return newDate;
	}
}
</script>
</html>