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
<script type="text/javascript">
	$(function(){
		var today = new Date();
		$('#searchTime').val(today.Format("yyyy-MM-dd"));
		querylist();
	});
	/***
	 * 导出功能
	 */
	function queryBottenWeek(){
			var date=$('#searchTime').val();
			if(date==null||date==''){
		          $.messager.alert("提示","日期不能为空！");
		          return;
			}
			var rows=$('#table1').datagrid('getRows');
			if(rows==null||rows.length==0){
				$.messager.alert("提示","没有数据，不能导出！");
				return;
			}else{
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {/*+"&deptName"+deptName  */
						url :'<%=basePath%>statistics/InpatientUDB/outpatientUDInfo.action?date='+date,
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							
							$.messager.alert("提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("提示", "导出失败！", "error");
						}
					});
				}
			});
		
		}	
	}
	
	//格式化日期
    function formatDate(val,row){
		if(val!=null&&val!=''){
			return val.toFixed(2);
		}else{
			return '0.00';
		}
    
    }

	//日期格式转换
	Date.prototype.Format = function(fmt)   
	{   
	  var o = {   
	    "M+" : this.getMonth()+1,                 //月份   
	    "d+" : this.getDate(),                    //日   
	    "h+" : this.getHours(),                   //小时   
	    "m+" : this.getMinutes(),                 //分   
	    "s+" : this.getSeconds(),                 //秒   
	    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
	    "S"  : this.getMilliseconds()             //毫秒   
	  };   
	  if(/(y+)/.test(fmt))   
	    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	  for(var k in o)   
	    if(new RegExp("("+ k +")").test(fmt))   
	  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	  return fmt;   
	} 
	function querylist(){
		var date=$('#searchTime').val();
		if(date!=null&&date!=''&&date!='undefind'){
			$('#table1').datagrid({
				url:'<%=basePath%>statistics/InpatientUDB/querylist.action',
				queryParams:{'date':date}
			});
		}else{
			$.messager.alert('提示','请先选择时间，再进行查询');
		}
	}
	<%-- function printOut(){
		var rows=$('#table1').datagrid('getRows');
		var rows2 = jQuery.extend(true, [],rows);
		rows2[0].name = "111111111111";
		if(rows!=null&&rows!=''){
			var searchTimeUDBalance=$('#searchTime').datebox('getValue');
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if (res) {
					window.open ("<%=basePath%>statistics/InpatientUDB/iReportInpatientUDB.action?rows="+JSON.stringify(rows)+"&rows2="+JSON.stringify(rows2)+"&date="+searchTimeUDBalance+"&fileName=NewSKYJKD",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	 			}
	 		});
		}else{
			$.messager.alert("提示","没有需要打印的信息！"); 
		}
	} --%>
	/***
	 * 打印功能
	 */
	function printOut(){
		var rows=$('#table1').datagrid('getRows');
		var searchTimeUDBalance=$('#searchTime').val();
		if(searchTimeUDBalance==null||searchTimeUDBalance==''){
	          $.messager.alert("提示","日期不能为空！");
	          return;
		}
		if(rows!=null&&rows!=''){
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if (res) {
	 				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
				    //给表单的隐藏字段赋值
				    $("#reportToLogin").val(searchTimeUDBalance);
				    $("#reportToRows").val(JSON.stringify(rows));
				    //alert(JSON.stringify(rows));
				    $("#reportToFileName").val("NewSKYJKD");
				
				    //表单提交 target
				    var formTarget="hiddenFormWin";
			        var tmpPath = "<%=basePath%>statistics/InpatientUDB/iReportInpatientUDB.action";
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


</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height:40px;padding:8px 5px 5px 5px">
		<span style="padding:5px 2px 0px 5px;">查询时间:</span>
		<input id="searchTime" class="Wdate" type="text" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;margin-left:-1;"/>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="querylist()" data-options="iconCls:'icon-search'">查询</a>
		<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="printOut()" data-options="iconCls:'icon-printer'">打印</a>
		<a href="javascript:queryBottenWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
	</div>
	<div data-options="region:'center',border:false" style="width: 100%;">
		<table id="table1" class="easyui-datagrid" data-options="fit:true,fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'name',rowspan:2,width:'10%',halign:'center',align:'right'">收款员</th>
					<th data-options="field:'q',colspan:3,align:'center'">收入</th>
					<th data-options="field:'w',colspan:3,align:'center'">支出</th>
					<th data-options="field:'e',colspan:3,align:'center'">实收</th>
					<th data-options="field:'time',rowspan:2,width:'10%',halign:'center',align:'right'">日结时间</th>
				</tr>
				<tr>
					<th data-options="field:'icash',width:'8%',halign:'center',align:'right',formatter:formatDate">现金</th>
					<th data-options="field:'icheck',width:'8%',halign:'center',align:'right',formatter:formatDate">支票</th>
					<th data-options="field:'iother',width:'8%',halign:'center',align:'right',formatter:formatDate">其他</th>
					<th data-options="field:'ocash',width:'8%',halign:'center',align:'right',formatter:formatDate">现金</th>
					<th data-options="field:'ocheck',width:'8%',halign:'center',align:'right',formatter:formatDate">支票</th>
					<th data-options="field:'oother',width:'8%',halign:'center',align:'right',formatter:formatDate">其他</th>
					<th data-options="field:'scash',width:'8%',halign:'center',align:'right',formatter:formatDate">现金</th>
					<th data-options="field:'scheck',width:'8%',halign:'center',align:'right',formatter:formatDate">支票</th>
					<th data-options="field:'sother',width:'8%',halign:'center',align:'right',formatter:formatDate">其他</th>
				
				</tr>
			</thead>
		</table>
						<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="date" id="reportToLogin" value=""/>
						<input type="hidden" name="rows" id="reportToRows" value=""/>
						<input type="hidden" name="fileName" id="reportToFileName" value=""/>
						</form>
	</div>

</body>
</html>