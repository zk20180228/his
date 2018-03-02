<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>危重疑难患者人数比例统计分析</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
/**
 * 重置
 */
 var flag;//全局科室
function reset(){
	var dept = $('#deptName').val("");
	searchFrom();
}
var deptMap=new Map();
var empMap=new Map();
var packunit= new Map();
var dname="全部科室";
var menuAlias = '${menuAlias}';
function searchFrom(){
		var begin=$('#Stime').val();
		var end=$('#Etime').val();
		if(begin!=''&&end!=''){
			var deptCodes = $('#deptName').getMenuIds();
			if(deptCodes==''){
				deptCodes=flag;
			}
			$('#list').datagrid({
				url:"<%=basePath%>statistics/CriticallyPatientsAnalyseAction/queryCriticallyPatients.action",
				queryParams: {
					deptCode: deptCodes,
					firstData:begin,
					endData:end,
					menuAlias:menuAlias
				}
			});
		}else{
			$.messager.alert('提示','时间不能为空');
		}
	}
	
$(function(){
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
//		haveThreeLevel:true,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
	});
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$('#outPatientMsg').datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		$("#list").datagrid({
			fitColumns:true,
			singleSelect:true,
			border:true,
			fit:true,
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false
		});
	}
});

/**
 * 导出
 */
	function exportDown(){
		var rows=$('#list').datagrid('getRows');
		if(rows==null||rows.length==0){
			$.messager.alert("提示","没有数据，不能导出！");
			return;
		}else{
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				  //给表单的隐藏字段赋值
			    $("#rows").val(JSON.stringify(rows));
				$('#exportForm').form('submit', {
					url :'<%=basePath%>statistics/CriticallyPatientsAnalyseAction/exportCriticallyPatients.action',
					onSubmit : function() {
						return $(this).form('validate');
					},
					success : function(data) {
						$.messager.alert("提示", "导出失败！", "success");
					},
					error : function(data) {
						$.messager.alert("提示", "导出失败！", "error");
					}
				});
			}
		});
		}
	}
	/**
	 * 打印
	 */
	function experStamp() {
		var rows=$('#list').datagrid('getRows');
	 	var dept = $('#deptName').val();//虚拟科室名称
		if(rows!=null&&rows!=''){
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if (res) {
	 				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
				    //给表单的隐藏字段赋值
				    $("#reportToRows").val(JSON.stringify(rows));
				    $("#reportToQueryDept").val(dept);
				    //表单提交 target
				    var formTarget="hiddenFormWin";
				    var tmpPath = '<%=basePath%>statistics/CriticallyPatientsAnalyseAction/experCriticallyPatients.action';
			        //设置表单target
			        $("#reportToHiddenForm").attr("target",formTarget);
			        //设置表单访问路径
					$("#reportToHiddenForm").attr("action",tmpPath); 
			        //表单提交时打开一个空的窗口
				    $("#reportToHiddenForm").submit(function(e){
				    	var timerStr = Math.random();
						 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
					});  
				    //表单提交
				    $("#reportToHiddenForm").submit();
	 			}
	 		});
		}else{
			$.messager.alert("提示","没有数据，不能打印！"); 	
		}
	}
	
	/**
	* 重置方法
	*/
	function clear(){
		var begin=$('#Stime').val("${firstData}");
		var end=$('#Etime').val("${endData}");
		$('#deptName').val('');
		$('#deptName').attr('name','');
		$("#list").datagrid('loadData', { total: 0, rows: [] });
	}	
</script>
<body>
<div id="cc" class="easyui-layout" data-options="fit:true">   

			<form action="">
			<table border="0" style="width: 100%;height:40px; padding:0px">
			<tr>
				<!-- 开始时间 --> 
				<td style="width:80px;" align="left">开始时间:</td>
				<td style="width:110px;">
					<input id="Stime" class="Wdate" type="text" name="Stime" value="${firstData }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'Etime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<!-- 结束时间 --> 
				<td style="width:80px;" align="center">结束时间:</td>
				<td style="width:110px" id="td1">
					<input id="Etime" class="Wdate" type="text" name="Etime" value="${endData }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'Stime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td style="width: 50px">科室:</td>
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
					&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</shiro:hasPermission>
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
				<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:void(0)" onclick="experStamp()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportDown()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
				</shiro:hasPermission>
			</td>
		</tr>
	</table>
	</form>
	<div data-options="region:'center',border:false" style="height:95%;padding: 8px 0px 0px 0px;" align="center">
		<table id="list"  class="easyui-datagrid" >
			<thead>
				<tr>
					<th data-options="field:'deptName',width:'9%'">部门名称</th>
					<th data-options="field:'num',width:'7%'">数量</th>
					<th data-options="field:'cure',width:'6%'">治愈情况</th>
					<th data-options="field:'better',width:'6%'">好转</th>
					<th data-options="field:'nocure',width:'6%'">未治愈</th>
					<th data-options="field:'death',width:'7%'">死亡</th>
					<th data-options="field:'other',width:'7%'">其他</th>
					<th data-options="field:'curePercent',width:'7%'">治愈率百分比</th>
					<th data-options="field:'deathPercent',width:'7%'">死亡率百分比</th>
					<th data-options="field:'aveInpatient',width:'7%'">平均住院日</th>
					<th data-options="field:'aveCost',width:'7%'">平均费用</th>
					<th data-options="field:'solo',width:'7%'">并发症感染</th>
					<th data-options="field:'allPatient',width:'7%'">全院患者</th>
					<th data-options="field:'numPercent',width:'7%'">占全院比</th>
				</tr>
			</thead>
		</table>
		<form id="exportForm" method="post">
				  <input type="hidden" name="rows" id="rows" value=""/>
		</form>
		<form id="reportToHiddenForm"  method="post" >
					<input type="hidden" name="dept" id="reportToQueryDept" value=""/>
					<input type="hidden" name="rows" id="reportToRows" value=""/>
		</form>
	</div>
</div>
<div id="InpatientMes" class="easyui-dialog" title="选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
	 	<a href="javascript:void(0)" onclick="findMesIn()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	 	<br></br>
	   <table id="infoDatagridMes" class="easyui-datagrid" data-options="fitColumns:true,checkOnSelect:true,fit:true"></table>
	</div>
</body>
</html>