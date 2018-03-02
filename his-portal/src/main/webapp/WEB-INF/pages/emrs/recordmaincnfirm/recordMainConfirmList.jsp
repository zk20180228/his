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
<title>病案归档</title>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var deptMap = "";
var empMap="";//医生
$(function(){
	$('#state').combobox('setValue','');
	$('#state').combobox('setText','');
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	$.ajax({//渲染科室
		url: '<%=basePath%>baseinfo/department/getDeptMap.action' ,
		type:'post',
		success: function(deptData) {
			deptMap = deptData;
		}
	});
	$('#list').datagrid({
        fit:true,
        rownumbers:true,
        pagination:true,
        pageSize:20,
		pageList:[20,30,50,80,100],
        striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,
        url: "<%=basePath%>emrs/recordmain/getConfirmList.action",
        queryParams:{startDate:startTime,endDate:endTime,deptCode:null,state:"0,1,2"}
    }); 
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#m2",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		firsturl:'<%=basePath%>emrs/emrDeptStatic/getDeptList.action?deptTypes=', //获取列表的url，必须要写
		relativeInput:".doctorInput",	//与其级联的文本框，必须要写
		relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
	});
});
function searchFrom(){
	var dept = $('#ksnew').getMenuIds();
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	var state = $('#state').combobox('getValue');
	$('#list').datagrid('reload',{startDate:startTime,endDate:endTime,deptCode:dept,state:state});
}
function submit(value){
	var recordId = $('#recordId').val();
	var totime = $('#totime').val();//送达时间
	var papaerPerson = $('#papaerPerson').textbox('getText');//送交病历人
	var signPerson = $('#signPerson').textbox('getText');//签收人
	$.ajax({
		url:"<%=basePath %>emrs/recordmain/saveSign.action",
		data:{"recordId":recordId,"totime":totime,"papaerPerson":papaerPerson,"signPerson":signPerson,"state":value},
		success:function(result){
			$.messager.alert('提示',result.resMsg,'info',function(){
				closeWin();
				window.location.href = '<%=basePath %>emrs/recordmain/toViewMaintenanceList.action';
			});
			return;
		}
		,error: function(){
			$.messager.alert('提示','网络异常,请稍后重试...');
			return;
		}		
	});
}
/**
 * 确认一个
 */
function confirmOne(){
	var row = $('#list').datagrid('getSelected');
	if(row==null||row.length<0){
		$.messager.alert('提示','请选择行!');
		return ;
	}
	$('#recordId').val(row.id);
	$('#win').window('open');
}
/*
 * 确认多个
 */
function confirmMany(){
	var row = $('#list').datagrid('getChecked');
	if(row==null||row.length<0){
		$.messager.alert('提示','请勾选要签收/回退的行!');
		return ;
	}
	var recordId = "";
	for(var i=0;i<row.length;i++){
		if(recordId!=""){
			recordId += ",";
		}
		recordId += row[i].id;
	}
	$('#recordId').val(recordId);
	$('#win').window('open');
}
/*
 * 关闭窗口
 */
function closeWin(){
	$('#win').window('close');
}
//渲染状态
function formatterState(value,row,index){
	if(value=='0'){
		return "申请";
	}else if(value=='1'){
		return "签收";
	}else{
		return "退回";
	}
}
//渲染科室
function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}
function formatterDoc(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		 <div data-options="region:'north',border:false" style="width:100%;height:40px;">
		 <table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
				<tr >
					<td style="width:55px;" align="center">科室:</td>
					<td style="width:120px; " class="newMenu">
						<div class="deptInput menuInput" style="width:200px"><input style="width:175px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
						<div id="m2" class="xmenu" style="display: none; ">
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
							<div class="select-info" style="display:none;">	
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
					<td>&nbsp;出院时间:<input id="startTime" class="Wdate" type="text" name="tecCarrier.preStoptime" value="${startDate }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					&nbsp;-&nbsp;<input id="endTime" class="Wdate" type="text" name="tecCarrier.preStoptime" value="${endDate }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					&nbsp;病案状态:<input id="state" class="easyui-combobox" data-options="valueField:'id',textField:'text',data:[{'id':0,'text':'提交'},{'id':1,'text':'签收'},{'id':2,'text':'回退'}]" />
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',border:false" >
			<table id="list" fit="true" style="width:100%;" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'code',width:'10%'">档案号</th>
						<th data-options="field:'outDept',width:'10%',formatter:functionDept">出院科室</th>
						<th data-options="field:'inpatientDoc',width:'8%',formatter:formatterDoc">住院医生</th>
						<th data-options="field:'cardId',width:'10%'">住院号</th>
						<th data-options="field:'state',width:'5%',formatter:formatterState">状态</th>
						<th data-options="field:'patientName',width:'8%'">姓名</th>
						<th data-options="field:'patientBirth',width:'10%'">出生日期</th>
						<th data-options="field:'reportAge',width:'5%'">年龄</th>
						<th data-options="field:'inDate',width:'10%'">入院日期</th>
						<th data-options="field:'outDate',width:'10%'">出院日期</th>
						<th data-options="field:'signPerson',width:'8%'">签收人</th>
					</tr>
				</thead>
			</table>
		</div>
		</div>
		<div id="win" class="easyui-window" title="签收病案" style="width:600px;height:400px" data-options="iconCls:'icon-save',modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false">   
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'center',split:true,border:false">
					<form id="addDForms" method="post" >
					<input id="recordId" type="hidden"/>
					<input id="submitType" type="hidden"/>
					<table class="honry-table">
						<tr>
							<td class="honry-lable">
								<span>纸质病历送到档案室时间:</span>
							</td>
							<td class="honry-info">
								<input id="totime" class="Wdate" type="text" name="totime"  
										onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
										style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								<span>科室送交病历人:</span>
							</td>
							<td class="honry-info">
								<input style="width: 200px;" class="easyui-textbox" id="papaerPerson"
											name="rule.code"
											data-options="required:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								<span>签收人:</span>
							</td>
							<td class="honry-info">
								<input style="width: 200px;" class="easyui-textbox" id="signPerson"
											name="rule.code"
											data-options="required:true"/>
							</td>
						</tr>
						
					</table>
				</form>
			</div>
		<div data-options="region:'south',split:false" style="height:50px;border-width:1px 0 0 0;">
			<div style="text-align: center; padding: 5px;">
				<a href="javascript:submit(1);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">签收</a>
				<a href="javascript:submit(2)void(0)" class="easyui-linkbutton" onclick="$('#win').window('close');" data-options="iconCls:'icon-cancel'">回退</a>
			</div>	
		</div> 
	</div>
	</div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="confirmOne()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick',plain:true">单个签收/回退</a>
		<a href="javascript:void(0)" onclick="confirmMany()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick',plain:true">多个签收/回退</a>	
	</div>
</body>
</html>