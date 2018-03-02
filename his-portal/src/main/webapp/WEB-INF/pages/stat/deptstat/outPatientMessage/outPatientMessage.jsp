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
<title>出院患者信息</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var drugpackagingunitMap = new Map();
// var sexMap; //性别
var typeMap=new Map();//状态不对应 
     typeMap.put('0','治愈');
     typeMap.put('1','好转');
     typeMap.put('2','未愈');
     typeMap.put('3','死亡');
     typeMap.put('4','其他');
var flag;//全局科室
$(function(){
	//查询包装单位
	$.ajax({
		url: "<%=basePath%>baseinfo/businessContractunit/getContMap.action",		
		type:'post',
		success: function(drugpackagingunitdata) {					
			drugpackagingunitMap= drugpackagingunitdata;
		}
	});
	//科室下拉
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		haveThreeLevel:false,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
	});
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
// 		var deptCode = $('#deptName').getMenuIds();
		$('#tableList').datagrid({ 
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
			data:{total:0,rows:[]}
		});
	}
});
//单位 列表页 显示	
function agingunitFamaters(value,row,index){
	if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
		return drugpackagingunitMap[value]; 
	}
	return value;
}
// //性别渲染
// function sexFormatter(value,row,index){
// 	if(value!=null&&value!=''){
// 		if(sexMap[value]){
// 			return sexMap[value];
// 		}else{
// 			return value;
// 		}
// 	}
// }
// //查询性别
// $.ajax({
<%-- 	url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=sex', --%>
// 	type:'post',
// 	success: function(data) {
// 		sexMap=data;
// 	}
// });
//出院状态渲染  数据不一致 先写死
function typeFormatter(value,row,index){
	if((value!=null&&value!='')||(value=='0')){
		if(typeMap.get(value)){
			return typeMap.get(value);
		}else{
			return value;
		}
	}
}
// //查询出院状态
// $.ajax({
<%-- 	url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=outcom', --%>
// 	type:'post',
// 	success: function(data) {
// 		typeMap=data;
// 	}
// });
//查询按钮
 function searchFrom(){
	 var deptCode = $('#deptName').getMenuIds();
	 if(deptCode==''){
		 deptCode=flag;
	 }
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 $('#tableList').datagrid({    
			url: '<%=basePath%>statistics/outPatientMessage/queryOutPatientMessage.action',
			queryParams: {
				deptCode: deptCode,
				startTime:startTime,
				endTime:endTime,
				menuAlias:menuAlias
			}
		}); 
 }
 
//重置按钮
 function clear(){
	 $('#startTime').val("${startTime }");
	 $('#endTime').val("${endTime }");
 	$('#deptName').val('');
	$('#deptName').attr('name','');
 	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
 }
 
//导出列表
 function exportList() {
 	var rows = $('#tableList').datagrid('getRows');
 	if(rows==null||rows==""){
 		$.messager.alert("提示", "列表无数据,无法导出！");
 		return;
 	}
 	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
 		if (res) {
 			rows = $('#tableList').datagrid('getRows');
			for(var i=0;i<rows.length;i++){
 				rows[i].pactCode=drugpackagingunitMap[rows[i].pactCode];
 			}
 			var valueRow=JSON.stringify(rows);
			$('#exportJson').val(valueRow);
			$('#exportForm').form('submit', {
				url :"<%=basePath%>statistics/outPatientMessage/exportOutPatientMessage.action",
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
 }
 
//报表打印
 function exportPDF() {
 	var rows = $('#tableList').datagrid('getRows');
	if(rows.length > 0){
		$('#reportJson').val(JSON.stringify(rows));
		//表单提交 target
		var formTarget="reportForm";
		var tmpPath = "<%=basePath%>statistics/outPatientMessage/reportList.action";
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
	}else{
		$.messager.alert("提示","当前统计无数据，无法打印！");
		setTimeout(function(){$(".messager-body").window('close')},1500);
		return;
	}
 }
</script>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	 	<form id="saveForm" action="" style="height:80px;width: 100%;padding-top: 8px;padding-top: 8px;padding-right: 10px;padding-left: 10px;">
	 		<table border="0" style="width: 100%;height: 10%">
	 			<tr>
 				<!-- 开始时间 --> 
					<td style="width:80px;" align="left">开始时间:</td>
					<td style="width:110px;">
						<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:80px;" align="center">结束时间:</td>
					<td style="width:110px" id="td1">
						<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
		 			<td style="width: 40px;"> 科室:</td>
				    <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
						<div class="deptInput menuInput" style="margin-top:0px;">
							<input id="deptName" class="ksnew"  readonly/>
							<span></span>
						</div>
						<div id="m3" class="xmenu" style="display: none;">
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
								<ul class="addDept"></ul>
							</div>	
							<div class="depts-dl">
								<div class="addList">
								</div>
								<div class="tip" style="display:none">没有检索到数据</div>
							</div>	
						</div>
		    		</td>
		    		<td>
		    			<shiro:hasPermission name="${menuAlias}:function:query">
			    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			 			</shiro:hasPermission>
			 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
<!-- 暂时注释掉,之后会打开注释,务删<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a> -->
		    		</td>
	    		</tr>
	    	 	<tr>
	    	 		<td align="center" colspan="8"  ><font style = "font-size: 32px !important;" class="empWorkTit" size="3">出院患者信息查询</font></td>
	    	 	</tr>
	    	 </table>
	    </form>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 89%;">
	    	<table id="tableList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'inpatientNo',width:'8%'">病历号</th>
						<th data-options="field:'patientName',width:'7%'">患者姓名</th>
<!-- 						,formatter:sexFormatter -->
						<th data-options="field:'sex',width:'4%'">性别</th>
						<th data-options="field:'ageunit',width:'5%'">年龄</th>
						<th data-options="field:'bedName',width:'8%'">床位</th>
						<th data-options="field:'docName',width:'8%'">主治医师</th>
						<th data-options="field:'nurseName',width:'8%'">主管护士</th>
						<th data-options="field:'inDate',width:'10%'">入院时间</th>
						<th data-options="field:'outDate',width:'10%'">出院时间</th>
						<th data-options="field:'outState',width:'6%',formatter:typeFormatter">出院状态</th>
						<th data-options="field:'pactCode',width:'8%',formatter:agingunitFamaters">结算方式(费别)</th>
						<th data-options="field:'diagName',width:'8%'">诊断名称(主诊断)</th>
						<th data-options="field:'deptCode',width:'8%'">科室名称</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
	<form id="reportForm" method="post">
		<input type="hidden" id="reportTime" name="reportTime"/>
		<input type="hidden" id="reportJson" name="reportJson">
		<input type="hidden" id="fileName" name="fileName" value="ZYRB">
	</form>
	<form id="exportForm" method="post">
		<input type="hidden" id="exportJson" name="exportJson">
	</form>
</body>
</html>
