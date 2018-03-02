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
<title>手术安排统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
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

<script type="text/javascript">
//全局变量
var deptMap="";	   //科室信息map
var bedWardMap="";    //病床名称Map对象 
var anyWayList=null;//麻醉方式s
var mingchengMap=new Map();//手术名称
var opjianMap=new Map();//手术间
var doctorMap=new Map();
var roomMap=new Map();
var sexMap=new Map();
var aneList = "";
var startTime="${startTime}";
var endTime="${endTime}";
var menuAliasNew = '${menuAlias}';
$(function(){

	$('#makeSure').click(function(){
			var texts=new Array();
			var iflag = 0;
			$("#m2 .select-info li").each(function(key,value){					
				if($(this).attr("rel") != "none"){
					texts[iflag] = $(this).html();
					iflag++;
				}
			}); 
			$("#execdept2").text(texts);
		});
	
	$('#clearId').click(function(){
		$("#execdept2").text("");
	});
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{type:"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	
	//渲染科室
			$.ajax({
				url:"<%=basePath%>operation/arrangement/querydeptComboboxs.action",
				success:function(data){
					deptMap=data;
				}
			});
	//渲染人员
	$.ajax({
		url : '<%=basePath %>operation/operationList/ssComboboxList.action',
		type:'post',
		success: function(payData) {
			var emp =payData;
			for(var i=0;i<emp.length;i++){
				doctorMap.put(emp[i].jobNo,emp[i].name);
			}
		}
	}); 
	//手术间
	$.ajax({
		url:'<%=basePath%>operation/arrangement/queryOproom.action',
		success:function(data){
			var opjianlist = data;
			for(var i=0;i<opjianlist.length;i++){
				opjianMap.put(opjianlist[i].id,opjianlist[i].roomName);
			}
		}
	});
	
	//麻醉方式
	$.ajax({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=aneWay',
		success: function(aneData) {
			aneList = aneData;
		}
	});
	
	//选择科室
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#m2",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		para:'OP',
		firsturl:"<%=basePath%>baseinfo/department/queryDeptCombo.action?deptTypes=", //获取列表的url，必须要写
	});
	
	//手术状态
	$('#operaState').combobox({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=operationState',
		valueField: 'encode',    
        textField: 'name',
	});
	
	//渲染手术室
	$.ajax({
		url : '<%=basePath %>operation/operationList/querysysDeptmentShi.action',
		type:"post",
		success:function(data){
			if(data!=null&&data!=""){
				for(var i=0;i<data.length;i++){
					roomMap.put(data[i].deptCode,data[i].deptName);
				}
			}
		}
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
			align:'right',
			fit:true,
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 100 ],
			queryParams:{startTime:startTime,endTime:endTime,status:null,execDept:null,identityCard:null},
			url:'<%=basePath %>statistics/OperationArrange/queryOperationArrange.action',
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
	},300)
})	
	

	
	//医生
	function functiondoc(value,row,index){
		if(value!=null&&value!=''){
			return doctorMap.get(value);
		}
		return "";
	}
	
	//麻醉方式为空的标识及渲染
	function functionaneway(value,row,index){
		if(value==null||value==""){
			return '<span style=\'position: relative; right:-60px;top:-6px;color:red;\'></span>';
		}else{
			for(var i=0;i<aneList.length;i++){
				if(value==aneList[i].encode){
					return aneList[i].name;
				}
			}
		}
	}
	 //科室empList
	function functioninDept(value){
		var emp = "";
		if(value!=null&&value!=undefined){
			emp = deptMap[value];
		} 
		return emp;
	} 
	//病床
	function functionbedNo(value){
		var emp = "";
		if(value!=null&&value!=undefined){
			emp = bedWardMap[value];
		} 
		return emp;
	}

	//性别
	function functionsex(value){
		return sexMap.get(value);
	}
	function query(){
		var startTime= $('#startTime').val().trim();
		var endTime= $('#endTime').val().trim();
 		var status= $('#operaState').combobox('getValue').trim();
 		var identityCard= $('#identityCard').textbox('getValue').trim();
		var execDept = $('#execDept').getMenuIds();
		if(startTime&&endTime){
			if(endTime<startTime){
				$.messager.alert("提示","开始时间不能大于结束时间！");
				return ;
			}
			$("#startTime2").text(startTime);
			$("#endtime2").text(endTime);
		}
		if(startTime){
			$("#startTime2").text(startTime);
		}else{
			$("#startTime2").text("");
		}
		if(endTime){
			$("#endtime2").text(endTime);
		}else{
			$("#endtime2").text("");
		}
		
		$("#operaArrangeListData").datagrid('load',{startTime:startTime,endTime:endTime,status:status,execDept:execDept,identityCard:identityCard});
	}
	//导出列表
	function exportList() {
		var startTime= $('#startTime').val().trim();
		var endTime= $('#endTime').val().trim();
 		var status= $('#operaState').textbox('getValue').trim();
		var execDept= $('#execDept').getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		var data=$("#operaArrangeListData").datagrid('getData');
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
					url :"<%=basePath%>statistics/OperationArrange/outOperationArrangeVo.action",
					onSubmit : function(param) {
						param.startTime=startTime;
						param.endTime=endTime;
						param.status=status;
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
			}
		});  
	}
	function edit(){
		var beg= $('#startTime').val().trim();
		var end= $('#endTime').val().trim();
 		var status= $('#operaState').combobox('getValue').trim();
		var execDept= $('#execDept').getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		var data=$("#operaArrangeListData").datagrid('getData');
		if(data.total==0){
			$.messager.alert("友情提示", "列表无数据，无法打印");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		$.messager.confirm('打印手术安排统计', '是否打印手术安排统计信息?', function(res) {
			if (res){
				var timerStr = Math.random();
				window.open ("<c:url value='/statistics/OperationArrange/iReportOperationArrange.action?randomId='/>"+timerStr+"&startTime="+beg+"&endTime="+end+"&status="+status+"&identityCard="+identityCard+"&execDept="+execDept+"&fileName=SSAPTJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
				};
		 })
	}
	/**
	 * @Description:渲染手术室
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月20日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
 function functionexecDept(value,row,index){
	 if(value!=null&&value!=""){
		 return roomMap.get(value);
	 }
 }
	/**
	 * @Description:渲染洗手
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:valueList-单元格的值
	 * @param:row-行数据
	 * @param:index-索引
	 * @return:渲染的效果
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function functionXiShou(valueList,row,index){
		if(valueList!=null){
			var result = "";
			$.each(valueList,function(index,value){
				if(index == valueList.length-1){
					result+=value.emplName||"";
				}else{
					result1=value.emplName||"";
					result+=result1+",";
				}
			});
			return result;
		}
	}
	function funcssmc(value,row,index){
		if(value!=null){
			var mc="";
			$.each(value,function(index,value){
				if(index==value.length-1){
					mc+=value.itemName||"";
				}else{
					result1=value.itemName||"";
					mc+=result1+",";
				}
			});

			if(mc!=""){
				return mc.substring(0,mc.length-1);
			}
			return mc;
		}
	}
	/**
	 * @Description:渲染临时助手
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月15日
	 * @param:valueList-单元格的值
	 * @param:row-行数据
	 * @param:index-索引
	 * @return:渲染的效果
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function functionLinShi(valueList,row,index){
		if(valueList!=null){
			var result = "";
			$.each(valueList,function(index,value){
				if(index == valueList.length-1){
					result+=value.emplName||"";
				}else{
					result1=value.emplName||"";
					result+=result1+",";
				}
			});
			return result;
		}
	}
	
	/**
	 * @Description:清空所有查询条件
	 */
	function clearQuery(){
		 $('#startTime').val(startTime);
		 $("#endTime").val(endTime);
		 $("#identityCard").textbox('setValue','');
		 $("a[name='menu-confirm-clear']").click();
		 $("#operaState").combobox('setValue','');
		query();


	 }
	//格式化预约时间
	function functionPreDate(value,row,index){
		if(value!=null&&value!=''){
			var newDate=/\d{4}-\d{1,2}-\d{1,2} \d{1,2}:\d{1,2}/g.exec(value);
			return newDate;
		}
	}

	function onclickDay(flg){
		if(flg=='1'){
			var st=beforeDay(0);
			var et=beforeDay(0);
			$('#startTime').val(st)
			$("#endTime").val(et);
			query();
		}else if(flg=='2'){
			var st=beforeDay(3);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			query();
		}else if(flg=='3'){
			var st=beforeDay(7);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			query();
		}else if(flg=='4'){
			var st=beforeDay(15);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			query();
		}else if(flg=='5'){
			var myDate  = new Date();
			var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
			 var year = myDate.getFullYear();
			 var start= year+"-"+month+"-01";
			 var et=beforeDay(0);
			 $('#startTime').val(start)
				$("#endTime").val(et);
			 query();
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
			$('#startTime').val(login)
			$("#endTime").val(end);
			query();
		}else{
			var myDate  = new Date();
			var start= myDate.getFullYear()+"-01-01";
			 var et=beforeDay(0);
			$('#startTime').val(start)
			$("#endTime").val(et);
			query();
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
		<div id="cc" class="easyui-layout" data-options="fit:true" style="z-index: 0" > 
			<div id="p" data-options="region:'north',border:false" style="width:100%;height:85px;z-index: 1">
				<div id="toolbarId" style="height:24px;padding: 6px 5px 5px 5px  ">
					<shiro:hasPermission name="${menuAlias}:function:query"> 
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard"> 
						<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					 </shiro:hasPermission>
			        <shiro:hasPermission name="${menuAlias}:function:readIdCard"> 
			        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					 </shiro:hasPermission> 
					 <shiro:hasPermission name="${menuAlias}:function:print"> 
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202'">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export"> 
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a> 
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
				<div style="height:55x;padding: 13px 5px 3px 5px;z-index: 5 ">
					<table style="width: 1350px;">
						<tr>
							<td style="width: 70px" nowrap='true'>开始时间：</td>
							<td style="width:125px;"><input id="startTime" name="startTime" value="${startTime}" class="Wdate" type="text"  onClick="WdatePicker()" style="width:125px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width: 70px" nowrap='true'>结束时间：</td>
							<td style="width:125px;"><input id="endTime" name="endTime" value="${endTime}" class="Wdate" type="text"  onClick="WdatePicker()" style="width:125px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width: 70px" nowrap='true'>手术状态：</td>
							<td style="width:125px;"><input class="easyui-combobox" id="operaState" name="operaState"   style="width:150px" /></td>
							<td style="width: 70px" nowrap='true' >执行科室：</td>
						    <td  style="width: 150px;z-index:9999" class="newMenu" >		     
								   <div class="deptInput menuInput" style="margin-top:0px;"><input class="ksnew" id="execDept" readonly="readonly"/><span></span></div> 
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
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn" id="makeSure">
												<span class="a-btn-text">确定</span>
											</a>
					    	       		</div>
										<div class="select-info" style="display:none">	
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
							<td style="width: 70px;text-align: right" nowrap='true'>身份证号：</td>
							<td style="width: 150px" nowrap='true'><input class="easyui-textbox" id="identityCard" name="identityCard"   style="width:150px" /></td>
						</tr>
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:true" style="width: 100%;height: 90%;">
				<div   class="easyui-layout" data-options="fit:true"> 
					<div  data-options="region:'north',border:false" style="height:90px;width: 100%;padding: 5px;">
						<div style="text-align: center; width:100%;padding-top: 3px" >
							    <h5 style="font-size: 30;font: bold;">手术安排统计</h5>
						</div>
						<div style="width: 100%;height:37px;overflow-x:auto;">
							<table  data-options="fit:true" style="width: 100%; ">
								<tr>
									<td style="width: 214px;font-size: 18;padding-left: 10px" nowrap='true'>统计日期：
										<span style="width: 150px;font-size: 18" id="startTime2">${startTime}</span>
									</td>
									<td style="text-align: left;width: 260px;font-size: 18" nowrap='true'>至&nbsp;
										<span style="width: 150px;font-size: 18" id="endtime2">${endTime }</span>
									</td>
									<td style="text-align: left;width: 270px;font-size: 18" nowrap='true'></td>
									<td style="text-align: left;width: 270px;font-size: 18" nowrap='true'></td>
									<td style="width: 180px;text-align: right;font-size: 18;" nowrap='true'>统计科室：</td>
									<td nowrap='true'><span style="font-size: 18" id="execdept2"></span></td>
									<td > </td>
								</tr>
							</table>
						</div>
					</div>
					<div data-options="region:'center',border:false" style="width: 98%;height: 96%;padding-top: 9px;">
						<table id="operaArrangeListData" class="easyui-datagrid" style="padding:5px 5px 5px 5px;width:98%;height: 100%" 
						data-options="fitColumns:true,singleSelect:true,border:true">
							<thead>
								<tr>
									<th data-options="field:'inDept',width:'7%',halign:'center',align:'center',formatter:functioninDept">患者所在科室</th>
									<th data-options="field:'preDate',width:'8%',halign:'center',align:'center',formatter:functionPreDate">手术开始时间</th>
									<th data-options="field:'patientNo',width:'7%',halign:'center',align:'center'">病历号</th>
									<th data-options="field:'bedNo',width:'6%',halign:'center',align:'center'">床号</th>
									<th data-options="field:'name',width:'6%',halign:'center',align:'center'">姓名</th>
									<th data-options="field:'sex',width:'3%',halign:'center',align:'center',formatter:functionsex">性别</th>
									<th data-options="field:'age',width:'4%',halign:'center',align:'center'">年龄</th>
									<th data-options="field:'mclist',width:'10%',halign:'center',align:'center'"formatter="funcssmc">手术名称</th>
									<th data-options="field:'execDept',width:'6%',halign:'center',align:'center'" formatter="functionexecDept">手术室</th>
									<th data-options="field:'opDoctor',width:'6%',halign:'center',align:'center'" formatter="functiondoc">施手术者</th>
									<th data-options="field:'zsDocList',width:'6%',halign:'center',align:'center'" formatter="functionLinShi">助手</th>
									<th data-options="field:'xhlist',width:'7%',halign:'center',align:'center'" formatter="functionXiShou">巡回护士</th>
									<th data-options="field:'xslist',width:'7%',halign:'center',align:'center'" formatter="functionXiShou">洗手护士</th>
									<th data-options="field:'aneType',width:'6%',halign:'center',align:'center',formatter:functionaneway">麻醉方法</th>
									<th data-options="field:'anaeDocd',width:'6%',halign:'center',align:'center'" formatter="functiondoc">麻醉医生</th>
									<th data-options="field:'anaeHelper',width:'6%',halign:'center',align:'center'" formatter="functiondoc">麻醉助手</th>
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