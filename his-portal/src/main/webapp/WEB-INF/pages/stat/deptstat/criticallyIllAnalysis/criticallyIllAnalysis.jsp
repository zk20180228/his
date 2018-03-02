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
</head>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
$(function(){
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		$('#tableList').datagrid({ 
			queryParams: {
				menuAlias:menuAlias,
				startTime:startTime,
				endTime:endTime
			},
			url: '<%=basePath%>statistics/criticallyIllAnalysis/queryCriticallyIllAnalysis.action',
			onLoadSuccess:function(data){
			}
		});
});

//查询按钮
function searchFrom(){
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 $('#tableList').datagrid({    
			url: '<%=basePath%>statistics/criticallyIllAnalysis/queryCriticallyIllAnalysis.action',
			queryParams: {
				startTime:startTime,
				endTime:endTime
			}
		}); 
}

//打印
function print(){
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==""||endTime==""){
		 $.messager.alert("提示","日期不能为空！");
	 }
	var rows=$('#tableList').datagrid('getRows');
	if(rows!=null&&rows!=''){
		$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
 			if (res) {
 				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
			    //给表单的隐藏字段赋值
			    $("#reportToStart").val(startTime);
			    $("#reportToEnd").val(endTime);
			    $("#reportToRows").val(JSON.stringify(rows));
			    //表单提交 target
			    var formTarget="hiddenFormWin";
		        var tmpPath = "<%=basePath%>statistics/criticallyIllAnalysis/printList.action";
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
	$('#startTime').val("${startTime}");
	$('#endTime').val("${endTime}");
	$("#tableList").datagrid('loadData',{total:0,rows:[]});
}
</script>
<body>
	<div class="easyui-layout" data-options="fit:true" style="margin-left:auto;margin-right:auto;">
				<form action="" style="padding:5px 5px 0px 5px;width:100%;height:35px;">
					<table style="width: 100%" cellspacing="0" cellpadding="0">
					    <tr >
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
							<td>
								&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							    <a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							    <a href="javascript:void(0)" onclick="print()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
							</td>
					    </tr>
					</table>
				</form>
		<div data-options="region:'center',border:false" style="width: 100%;height:100%;padding:0px 0px 30px 0px;margin-left:auto;margin-right:auto;">
			<table id="tableList" class="easyui-datagrid" style="width:100%;height:90%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'deptCode',width:'15%'" align="left" halign="center">科室</th>
						<th data-options="field:'outPatient',width:'15%'" align="center" halign="center">出院人数</th>
						<th data-options="field:'allPatient',width:'15%'" align="center" halign="center">全院患者</th>
						<th data-options="field:'proportion',width:'15%'" align="right" halign="center">占例</th>
						<th data-options="field:'illPatient',width:'10%'" align="right" halign="center">重症科室患者</th>
						<th data-options="field:'illPatPro',width:'10%'" align="right" halign="center">重症占比</th>
					</tr>
				</thead>
			</table>
			<form method="post" id="reportToHiddenForm">
				<input type="hidden" name="start" id="reportToStart" value=""/>
				<input type="hidden" name="end" id="reportToEnd" value=""/>
				<input type="hidden" name="rows" id="reportToRows" value=""/>
			</form>
		</div>
	</div>
</body>
</html>