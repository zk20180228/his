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
<title>住院手术费用汇总</title>
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
	var deptMap=null;
	var beg="${startTime}";
	var end="${endTime}";
	var menuAliasNew = '${menuAlias}';
	$(function(){
		/**
		 * 科室
		 * @Author: zhangjin
		 * @CreateDate: 2017年2月17日
		 * @param:
		 * @return:
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		$.ajax({
			url:"<%=basePath%>statistics/OperationCost/getDeptMap.action",
			success:function(data){
				deptMap=data;
			}
		});
	
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			dropmenu:"#m2",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			para:'OP,I,C,N',
			firsturl:"<%=basePath%>baseinfo/department/queryDeptCombo.action?deptTypes=", //获取列表的url，必须要写
		});
		
		//加载数据表格
		$("#operaCostListData").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			pageSize:20,
			fit:true,
			pageList:[20,40,80],
			queryParams:{startTime:beg,endTime:end,inpatientNo:null,execDept:null,identityCard:null},
			url:'<%=basePath %>statistics/OperationCost/queryOperationCost.action?menuAlias='+menuAliasNew,
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
	
	function find(){
		var startTime= $('#startTime').val();
		var endTime= $('#endTime').val();
		var inpatientNo= $('#patientNo').textbox('getValue');
		var execDept= $('#execDept').getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		$("#operaCostListData").datagrid('load',{startTime:startTime,endTime:endTime,inpatientNo:inpatientNo,execDept:execDept,identityCard:identityCard});
	}
	
	function functioninState(value){
		if(value!=undefined){
			var v = value.substring(6,14);
			return "00"+v;
		}
	}
	
	//导出列表
	function exportList() {
		var startTime= $('#startTime').val();
		var endTime= $('#endTime').val();
		var inpatientNo= $('#patientNo').textbox('getValue');
		var execDept= $('#execDept').getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		var data=$("#operaCostListData").datagrid('getData');
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
					url :"<%=basePath%>statistics/OperationCost/outOperationCost.action",
					onSubmit : function(param) {
						param.startTime=startTime;
						param.endTime=endTime;
						param.inpatientNo=inpatientNo;
						param.execDept=execDept;
						param.identityCard=identityCard;
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
		var startTime= $('#startTime').val();
		var endTime= $('#endTime').val();
		var inpatientNo= $('#patientNo').textbox('getValue');
		var execDept= $('#execDept').getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		var data=$("#operaCostListData").datagrid('getData');
		if(data.total==0){
			$.messager.alert("友情提示", "列表无数据，无法打印");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		$.messager.confirm('打印住院手术费汇总统计', '是否打印住院手术费汇总统计信息?', function(res) {  //提示是否打印假条
			if (res){
				var timerStr = Math.random();
				window.open ("<c:url value='/statistics/OperationCost/iReportOperationCost.action?randomId='/>"+timerStr+"&startTime="+startTime+"&endTime="+endTime+"&identityCard="+identityCard+"&inpatientNo="+inpatientNo+"&execDept="+execDept+"&fileName=iReportOperationCost",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
				};
		 })
	}	
	/**
	 * @Description:清空所有查询条件
	 */
	function clearQuery(){
		$('#startTime').val(beg)
		$("#endTime").val(end);
		 $("#identityCard").textbox('setValue','');
		 $("#patientNo").textbox('setValue','');
		 $("a[name='menu-confirm-clear']").click();
		 find();

	}	
	//快速查询	
	function onclickDay(flg){
		if(flg=='1'){
			var st=beforeDay(0);
			var et=beforeDay(0);
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='2'){
			var st=beforeDay(3);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='3'){
			var st=beforeDay(7);
			var et=beforeDay(1);
			var start= st;
			var end= et;
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='4'){
			var st=beforeDay(15);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='5'){
			var myDate  = new Date();
			var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
			 var year = myDate.getFullYear();
			 var start= year+"-"+month+"-01";
			 var et=beforeDay(0);
			 $('#startTime').val(start)
				$("#endTime").val(et);
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

			$('#startTime').val(login)
			$("#endTime").val(end);
			find();
		}else{
			var myDate  = new Date();
			var start= myDate.getFullYear()+"-01-01";
			 var et=beforeDay(0);
			$('#startTime').val(start)
			$("#endTime").val(et);
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
			<div id="p" data-options="region:'north',border:false" style="width:100%;height:65px;">
				<div id="toolbarId" style="height:24px;padding: 5px 5px 5px 5px  ">
					<shiro:hasPermission name="${menuAlias}:function:query"> 
					<a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
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
				<div style="height:24px border-top: 1px;padding: 0px 5px 3px 5px ">
					<table>
						<tr>
							<td style="width: 255px">开始时间：
							<input id="startTime" name="startTime" value="${startTime}" class="Wdate" type="text"  onClick="WdatePicker({onpicked:startTimeFun})" style="width:125px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width: 255px" class="operationCostList">结束时间：
							<input id="endTime" name="endTime" value="${endTime}" class="Wdate" type="text"  onClick="WdatePicker({onpicked:endTimeFun})" style="width:125px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width: 255px">病历号：<input class="easyui-textbox" id="patientNo" name="patientNo"   style="width:150px" /></td>
							<td style="width: 80px">执行科室：
<!-- 							<input class="easyui-combobox" id="execDept" name="execDept"   style="width:150px" /> -->
							</td>
							<td style="width:150px;" class="newMenu">
					    	       	<div class="deptInput menuInput" style="margin-top:0px"><input class="ksnew" id="execDept" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
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
					        <td style="width: 255px" nowrap='true'>&nbsp;身份证号：<input class="easyui-textbox" id="identityCard" name="identityCard"   style="width:150px" /></td>
						</tr>
					</table>
				</div>
			</div>
			<div  data-options="region:'center',border:false" style="width: 100%;height: 85%;">
				<table id="operaCostListData"  class="easyui-datagrid" style="padding:5px 5px 5px 5px;width: 100%;height: 100%">
					<thead>
						<tr>
							<th data-options="field:'medicalrecordId',width:'15%',halign:'center',align:'center'">病历号</th>
							<th data-options="field:'name',width:'15%',halign:'center',align:'center'">姓名</th>
							<th data-options="field:'execDept',width:'15%',halign:'center',align:'center'" formatter="funDept" >执行科室</th> 
							<th data-options="field:'totCost',width:'15%',halign:'center',align:'right'">手术费</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center',align:'center'">收费日期</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/>
			</div>
			
		</div>
	</body>
	<script type="text/javascript">
	//hedong 20170322 日期控件变更后重新绑定事件
	function startTimeFun(){
		var stime = $("#startTime").val();
    	var etime = $("#endTime").val();
    	if(etime!=""){
    		if(stime>etime){
    			$.messager.alert('提示',"开始时间不能大于结束时间！");
    			$("#startTime").val("");
    		}
    	}
	}
	//hedong 20170322 日期控件变更后重新绑定事件
	function endTimeFun(){
		var stime = $("#startTime").val();
    	var etime = $("#endTime").val();
    	if(stime!=""){
    		if(etime<stime){
    			$.messager.alert('提示',"结束时间不能小于开始时间！");
    			$("#endTime").val("");
    		}
    	}
	}
	
	/**
	 * @Description:渲染科室患者
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月12日
	 * @param1:value:单元格的值
	 * @param2:rowData：行数据
	 * @param3:rowIndex:索引
	 * @return:科室名称
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function funDept(value,rowData,rowIndex){
			if(value!=null&&value!=""){
				return deptMap[value];
			}
	}

	</script>
</html>