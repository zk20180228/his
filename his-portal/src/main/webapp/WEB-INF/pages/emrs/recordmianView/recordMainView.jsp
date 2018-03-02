<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %><%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病案查询打印</title>
<style type="text/css">
	#m2 {
	    position: fixed;
    	left: 97px !important;
	}
	
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var sexMap = new Map();
$(function(){
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	$('#patientSex').combobox({
		url:"<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		queryParams:{"type":"sex"},
		valueField:'encode',
		textField:'name',
		onSelect:function(record){
			$('#patientSexH').val(record.encode);
		}
	});
	setTimeout(function(){
		$('#list').datagrid({
			url:"<%=basePath %>emrs/recordmain/getRecord.action",
			rownumbers:true,
			pagination:true,
			pageSize:20,
			pageList:[20,40,60,80,100]
		});
	},500);
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
///性别渲染
function formatterSex(value,row,index){
	return sexMap.get(value);
}
function searchFrom(){
	var dept = $('#ksnew').getMenuIds();//科室
	var recCode = $('#recCode').textbox('getText');//病案号
	var patientName = $('#patientName').textbox('getText');//姓名
	var patientSexH = $('#patientSexH').val();
	var patientAgeS = $('#patientAgeS').textbox('getText');//年龄      起始
	var patientAgeE = $('#patientAgeE').textbox('getText');//年龄      结束
	var digNose = $('#digNose').textbox('getText');//诊断编码
	var outDateS = $('#outDateS').val();//出院日期   起始
	var outDateE = $('#outDateE').val();// 出院日期   结束
	var birthS = $('#birthS').val();//生日  起
	var birthE = $('#birthE').val();//生日  止
	$('#list').datagrid('reload',{deptCode : dept,recCode : recCode
								,patientName : patientName,sex : patientSexH
								,ageStart : patientAgeS,ageEnd : patientAgeE
								,outDateS : outDateS,outDateE : outDateE
								,birthStart : birthS,birthEnd : birthE,digNose : digNose});
}
function recordPrint(){
	var dept = $('#ksnew').getMenuIds();//科室
	var recCode = $('#recCode').textbox('getText');//病案号
	var patientName = $('#patientName').textbox('getText');//姓名
	var patientSexH = $('#patientSexH').val();
	var patientAgeS = $('#patientAgeS').textbox('getText');//年龄      起始
	var patientAgeE = $('#patientAgeE').textbox('getText');//年龄      结束
	var digNose = $('#digNose').textbox('getText');//诊断编码
	var outDateS = $('#outDateS').val();//出院日期   起始
	var outDateE = $('#outDateE').val();// 出院日期   结束
	var birthS = $('#birthS').val();//生日  起
	var birthE = $('#birthE').val();//生日  止
	var timerStr = Math.random();
	var init = "&deptCode="+dept+"&recCode="+recCode+"&patientName="+patientName+"&sex="+patientSexH+"&ageStart="+patientAgeS+"&ageEnd"+patientAgeE
			+"&outDateS="+outDateS+"&outDateE="+outDateE+"&birthStart="+birthS+"&birthEnd="+birthE+"&digNose="+digNose;
	window.open ("<%=basePath%>/iReport/iReportPrint/recordPrint.action?randomId="+timerStr+init+"&fileName=EmrRecordMain",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');
}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		<div data-options="region:'north',border:false" style="width:100%;height:100px;">
			<div id="ccc" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;">
				<div data-options="region:'west',border:true" style="width:50%;height:100%;">
					病案号：<input class="easyui-textbox" id="recCode" style="width:150px;" />
					姓名：<input class="easyui-textbox" id="patientName" style="width:150px" />
					性别：<input class="easyui-combobox" id="patientSex" style="width:150px" />
					<input id="patientSexH" type="hidden" />
					年龄：<input class="easyui-textbox" id="patientAgeS" style="width:50px" />
					-<input class="easyui-textbox" id="patientAgeE" style="width:50px" />
					<br/><br/>
					出院日期：<input id="outDateS" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					-<input id="outDateE" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					出生日期：<input id="birthS" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					-<input id="birthE" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					诊断编码：<input class="easyui-textbox" id="digNose" style="width:150px" />
					<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 99999999" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr >
							<td style="width:20px;" align="left">出院科室:</td>
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
						</tr>
					</table>			
				</div>
				<div data-options="region:'center',border:true" >
					<div text-align="center" float="left" style="margin: 0 auto">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<br/><br/>
						<a href="javascript:void(0)" onclick="recordPrint()" class="easyui-linkbutton" iconCls="icon-2012081511202">打印</a>
					</div>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false" style="width:30%;height:100%;" >
			<table id="list" fit="true" style="width:100%;" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'cardId',width:'10%'">住院号</th>
						<th data-options="field:'patientName',width:'10%'">患者姓名</th>
						<th data-options="field:'patientSex',width:'5%',formatter:formatterSex">性别</th>
						<th data-options="field:'patientBirth',width:'10%'">出生日期</th>
						<th data-options="field:'patientAge',width:'5%'">年龄</th>
						<th data-options="field:'inDate',width:'10%'">入院日期</th>
						<th data-options="field:'outDate',width:'10%'">出院日期</th>
						<th data-options="field:'outDept',width:'10%'">出院科室</th>
						<th data-options="field:'inpatientDoc',width:'10%'">住院医生</th>
						<th data-options="field:'attendingDoc',width:'10%'">主治医生</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>