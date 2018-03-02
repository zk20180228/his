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
 var menuAlias = '${menuAlias}';
 var flag;//全局科室
 $(function(){
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

//查询按钮
 function searchFrom(){
 	 var startTime = $('#startTime').val();
 	 var endTime = $('#Etime').val();
 	 if(startTime==''||endTime==''){
 		 $.messager.alert('提示','时间不能为空');
 		 return false;
 	 }
 	var deptCode = $('#deptName').getMenuIds();
 	if(deptCode==''){
 		deptCode=flag;
 	}
 	 $('#tableList').datagrid({    
			url: '<%=basePath%>deptstat/dischargeStatistics/queryDischargeStat.action',
 			queryParams: {
 				deptCode: deptCode,
 				startTime:startTime,
 				endTime:endTime
 			}
 		}); 
 	}
 //打印
 function printReport(){
		var rows=$('#tableList').datagrid('getRows');
	 	if(rows.length > 0){
				$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
			 		if (res) {
			 			rows=$('#tableList').datagrid('getRows');
						$('#reportJson').val(JSON.stringify(rows));
						//表单提交 target
						var formTarget="reportForm";
						var tmpPath = "<%=basePath%>deptstat/dischargeStatistics/reportList.action";
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
	 		$.messager.alert('提示','列表无数据,无法打印！');
	 	}	
 }
function changeTime(){
	var startTime = $("#startTime").val();
	var endTime = $("#Etime").val();
	$("#startTime").val(startTime);
	$("#Etime").val(endTime);
}
//小数点前是0 渲染
function functionRate(value,row,index){
	if(value!=null){
		if(value.substr(0,1)=='.'){
			return '0'+value;
		}else{
			return value;
		}
	}
}
/**
* 重置方法
*/
function clear(){
	$('#startTime').val("${startTime}");
	$('#Etime').val("${endTime}");
	$('#name').textbox('setValue',"");
	$('a[name=\'menu-confirm-clear\']').click();
	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height:85px;padding:8px 5px 5px 5px">
	<table id="searchTab" style="width: 100%;">
		<tr>
			<td style="width:40px;" align="left">日期:</td>
			<td style="width:110px;">
				<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'Etime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			</td>
			<!-- 结束时间 --> 
			<td style="width:40px;" align="center">至</td>
			<td style="width:110px" id="td1">
				<input id="Etime" class="Wdate" type="text" name="Etime" value="${EndTime}"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
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
			<td style='text-align: left'>
			<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="searchFrom()" data-options="iconCls:'icon-search'">查询</a>
			</shiro:hasPermission>	
				<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
			<shiro:hasPermission name="${menuAlias}:function:print">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="printReport()" data-options="iconCls:'icon-printer'">打印</a>
			</shiro:hasPermission>	
<%-- 			<shiro:hasPermission name="${menuAlias}:function:export"> --%>
<!-- 				<a href="javascript:queryBottenWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a> -->
<%-- 			</shiro:hasPermission>	 --%>
			</td>
		</tr>
	</table>
	<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 5px">出院病种统计查询</h5>
	</div>
	<div data-options="region:'center',border:false" style="width: 100%;">
		<table id="tableList" class="easyui-datagrid" data-options="fit:true,fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'diseaseKind',rowspan:3,width:'10%',halign:'center',align:'left'">疾病名称</th>
					<th data-options="field:'icd_code',rowspan:3,width:'10%',halign:'center',align:'center'">ICD-10</th>
					<th data-options="field:'2',colspan:7,align:'center',width:'21%'">出院病人数</th>
					<th data-options="field:'cureRate',rowspan:3,align:'center',width:'6%',formatter:functionRate">治愈率（%）</th>
					<th data-options="field:'betterRate',rowspan:3,align:'center',width:'6%',formatter:functionRate">好转率（%）</th>
					<th data-options="field:'deathRate',rowspan:3,align:'center',width:'6%',formatter:functionRate">死亡率（%）</th>
					<th data-options="field:'avgInpatDay',rowspan:3,align:'center',width:'6%'">人均住院日数</th>
					<th data-options="field:'avgBeforeOperDay',rowspan:3,align:'center',width:'6%'">术前人均住院日数</th>
					<th data-options="field:'q',colspan:5,align:'center',width:'20%'">平均每人住院医疗费</th>
					<th data-options="field:'drugProp',rowspan:3,align:'center',width:'6%'">药品比例</th>
				</tr>
				<tr>
					<th data-options="field:'outPatient',rowspan:2,align:'center',width:'3%'">合计</th>
					<th data-options="field:'e',colspan:5,align:'center',width:'15%'">其中</th>
					<th data-options="field:'other',rowspan:2,align:'center',width:'3%'">其他</th>
					<th data-options="field:'cost',rowspan:2,align:'center',width:'4%'">总费用</th>
					<th data-options="field:'e',colspan:4,align:'center',width:'16%'">其中</th>
				</tr>
				<tr>
					<th data-options="field:'subTotal',align:'center',width:'3%'">小计</th>
					<th data-options="field:'cure',align:'center',width:'3%'">治愈</th>
					<th data-options="field:'better',align:'center',width:'3%'">好转</th>
					<th data-options="field:'notCure',align:'center',width:'3%'">未愈</th>
					<th data-options="field:'death',align:'center',width:'3%'">死亡</th>
					<th data-options="field:'avgBedFee',align:'center',width:'4%'">床位</th>
					<th data-options="field:'avgDrugFee',align:'center',width:'4%'">药费</th>
					<th data-options="field:'avgOperFee',align:'center',width:'4%'">手术</th>
					<th data-options="field:'avgCheckTreat',align:'center',width:'4%'">检治</th>
				</tr>
			</thead>
		</table>
		<form id="reportForm" method="post">
			<input type="hidden" id="reportJson" name="reportJson">
		</form>
	</div>
	
</body>
</html>