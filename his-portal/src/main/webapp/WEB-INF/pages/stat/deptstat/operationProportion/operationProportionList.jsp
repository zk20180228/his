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
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
	var deptMap=new Map();
	var empMap=new Map();
	var packunit= new Map();
	var dname="全部科室";
	var menuAlias = '${menuAlias}';
	var flag;
	$(function(){
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			menulines:2, //设置菜单每行显示几列（1-5），默认为2
			dropmenu:"#m3",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		});
		$('#m3 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#queryDept').getMenuIds()
		$('a[name=\'menu-confirm-clear\']').click();
		if(flag==''){
			$("#countList").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
			    $('a[name=\'menu-confirm-clear\']').click();
				var winH=$("body").height();
				var date = $('#time2').val();
				var dept = dname;
				var deptCode = $('#queryDept').getMenuIds();
				var dates = date.split("-");
				var year = dates[0];
				var month = dates[1];
				var d = new Date(year,month,0);
				var day = d.getDate();
// 				document.getElementById("bt").innerHTML = year+"-"+month+"-"+"01";
// 				document.getElementById("et").innerHTML = year+"-"+month+"-"+day;
				document.getElementById("hh").innerHTML = year+"年"+month+"月"+"手术占比统计";
				//加载数据表格
				$("#countList").datagrid({
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					idField:'id',
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:false,
					rownumbers:true,
					fit:true,
				    queryParams:{Stime:"${Etime}",dept:null},
					url:'<%=basePath %>statistics/deptstat/operationProportion/queryOperationProportion.action?menuAlias='+menuAlias, 
					onLoadSuccess:function(data){
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
		}
		
	});
	function searchFrom(){
		var deptCode = $('#queryDept').getMenuIds();
		if(deptCode==''){
			deptCode=flag;
		}
		var date = $('#time2').val();
		if(date==null||date==""){
			 $.messager.alert("提示","查询日期不能为空！");
	          return;
		}
		var dates = date.split("-");
		var year = dates[0];
		var month = dates[1];
		var d = new Date(year,month,0);
		var day = d.getDate();
// 		document.getElementById("bt").innerHTML = year+"-"+month+"-"+"01";
// 		document.getElementById("et").innerHTML = year+"-"+month+"-"+day;
		document.getElementById("hh").innerHTML = year+"年"+month+"月"+"手术占比统计";
		$('#countList').datagrid('load',{
			Stime:date,
			dept: deptCode
		});


	}
	
	//导出功能
	function exportList (){
		var start = $('#time2').val();
	    if(start==null||start==''){
	        $.messager.alert("提示","日期不能为空！");
	        return;
		}
	    var rows=$('#countList').datagrid('getRows');
		if(rows==null||rows.length==0){
			$.messager.alert("提示","没有数据，不能导出！");
			return;
		}else{
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				  //给表单的隐藏字段赋值
			    $("#rows").val(JSON.stringify(rows));
				$('#saveForm').form('submit', {
					url :'<%=basePath%>statistics/deptstat/operationProportion/exportList.action',
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
	//打印功能
	function printList(){
	 	var start = $('#time2').val();
		if(start==null||start==''){
	          $.messager.alert("提示","日期不能为空！");
	          return;
		}
		var rows=$('#countList').datagrid('getRows');
	 	var dept = $('#queryDept').val();//虚拟科室名称
		var dates = start.split("-");
		var year = dates[0];
		var month = dates[1];
		var d = new Date(year,month,0);
		var day = d.getDate();
        start =start+"-01";
        var end=year+'-'+month+'-'+day;
		if(rows!=null&&rows!=''){
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if (res) {
	 				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
				    //给表单的隐藏字段赋值
				    $("#reportToStart").val(start);
				    $("#reportToEnd").val(end);
				    $("#reportToQueryDept").val(dept);
				    $("#reportToRows").val(JSON.stringify(rows));
				    $("#reportToFileName").val("SSZBTJ");
				    //表单提交 target
				    var formTarget="hiddenFormWin";
			        var tmpPath = "<%=basePath%>statistics/deptstat/operationProportion/printList.action";
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
	//重置
	function clear(){
		$("#time2").val("${Etime}");
		var dept = $('#queryDept').val("");
		$('#queryDept').attr("name","");
		searchFrom();
	}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<body>
	<div id="divLayout"  class="easyui-layout" data-options="fit:true" >
		 <div data-options="region:'north',border:false" style="height:130px;padding: 5px 5px 1px 5px">
		    <div>
				<table style="padding-top: 8px">
				<tr>
				<td>
					日期：
					<input id="time2" class="Wdate" style="width:100px !important;height:22px";   "type="text"  value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M'})" />
				</td>
				<td style="width:55px;" align="center">科室:</td>
			<td  id = "classA" class="newMenu" style="width:120px;z-index:1;position: relative;">
				<div class="deptInput menuInput" style="margin-top:-4px;">
					<input id="queryDept" class="ksnew" readonly/><span></span></div>
					<div id="m3" class="xmenu" style="display: none;">
    	       		<div class="searchDept">
							<input type="text" name="searchByDeptName" placeholder="回车查询" />
							<span class="searchMenu"><i></i>查询</span> <a
								name="menu-confirm-cancel" href="javascript:void(0);"
								class="a-btn"> <span class="a-btn-text">取消</span>
							</a> <a name="menu-confirm-clear" href="javascript:void(0);"
								class="a-btn"> <span class="a-btn-text">清空</span>
							</a> <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
								<span class="a-btn-text">确定</span>
							</a>
						</div>
						<div class="select-info" style="display: none">
							<label class="top-label">已选部门：</label>
							<ul class="addDept">

							</ul>
						</div>
						<div class="depts-dl">
							<div class="addList"></div>
							<div class="tip" style="display: none">没有检索到数据</div>
						</div>	
				</td>
				<td>	
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 0px">查询</a>
				</shiro:hasPermission>
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
				<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:void(0)" onclick="printList()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" style="margin-left: 3px">打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'" style="margin-left: 3px">导出</a>
				</shiro:hasPermission>
				</td>
				</tr>
				</table>
		    </div>
				<div style="padding-top: 8px;">
					<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 5px"></h5>
<!-- 					<table style="padding-top: 5px"> -->
<!-- 						<tr> -->
<!-- 							<td style="width: 100px;font-size: 18"></font>统计日期：</td> -->
<!-- 							<td style="text-align:left;width: 90px;font-size: 18" id="bt"></td> -->
<!-- 							<td style="text-align: center;width: 20px;font-size: 18">至</td> -->
<!-- 							<td style="text-align: left;width: 110px;font-size: 18" id="et"></td> -->
<!-- 						</tr> -->
<!-- 					</table> -->
				</div>
		</div>
		 <div data-options="region:'center',border:false" style="height:90%;padding: 3px 0px 0px 0px;">
				<table id="countList"  data-options="fit:true,method:'post'">
					<thead>
						<tr>
							<th data-options="field:'deptCode'," width="16%" align="center">科室编码</th>
							<th data-options="field:'deptName'" width="16%" align="center">科室名称</th>
							<th data-options="field:'total'" width="15%" align="center" halign="center">出院人数</th>
							<th data-options="field:'total1'" width="15%" align="center">转出人数</th>
							<th data-options="field:'total2'" width="15%" align="center" halign="center">手术人数</th>
							<th data-options="field:'proportion'" width="15%" align="center">手术占比(%)</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post">
				  <input type="hidden" name="rows" id="rows" value=""/>
				</form>
				<form method="post" id="reportToHiddenForm">
					<input type="hidden" name="start" id="reportToStart" value=""/>
					<input type="hidden" name="end" id="reportToEnd" value=""/>
					<input type="hidden" name="dept" id="reportToQueryDept" value=""/>
					<input type="hidden" name="rows" id="reportToRows" value=""/>
				</form>
		</div>
	</div>
	</body>
</html>
