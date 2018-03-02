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
<title>会议签到信息列表</title>
<style type="text/css">
	*{
		box-sizing: border-box;
	}
</style>
</head>
<body>
	
	<div style="position: relative;">
		<div style="width:100%;height:35px;position: absolute;top: 0;left: 0;padding-left: 15px;">
				<div style="padding-top:4px">
				<input class="easyui-textbox" style="width:150px"  id="search" data-options="prompt:'姓名/员工号/科室(会议室)'">
				<a href="javascript:void(0)"  id="query" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
				<div style="float: right;margin-right: 5px;">
					总计:${total}人,准时签到:${onTimeNum}人,迟到:${isLateNum}人,未到:${noComeNum}人,临时参加:${tempComeNum}人
				</div>
			</div>
		</div>
		<div style="width:100%;height:100%; padding-top: 35px;" >
			<div id="tt" class="easyui-tabs" data-options="fit:true,border:false,tabWidth:130,tabHeight:35,narrow:true">
			    <div title="准时签到" >
			    	<table  id ="dg1" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/meeting/meetingSigned/onTimeList.action?id=${id}', fitColumns:true, pagination:true, striped:true, fit:true,pageList:[10,20,30,40,50],pageSize:20,rownumbers:true,border:false">
						<thead>   
					        <tr>   
					            <th data-options="field:'pName',width:80,align:'center'">姓名</th>   
					            <th data-options="field:'pAccount',width:80,align:'center'">员工号</th>   
					            <th data-options="field:'deptName',width:80,align:'center'">科室名</th>   
					            <th data-options="field:'signedTime',width:150,align:'center'">签到时间</th> 
					        </tr>   
					    </thead>   
					</table> 
			    </div>   
			    <div title="迟到">   
			       <table  id ="dg2" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/meeting/meetingSigned/isLateList.action?id=${id}', fitColumns:true, pagination:true, striped:true, fit:true,pageList:[10,20,30,40,50],pageSize:20,rownumbers:true,border:false">
						<thead>   
					        <tr>   
					            <th data-options="field:'pName',width:80,align:'center'">姓名</th>   
					            <th data-options="field:'pAccount',width:80,align:'center'">员工号</th>   
					            <th data-options="field:'deptName',width:80,align:'center'">科室名</th>   
					            <th data-options="field:'signedTime',width:150,align:'center'">签到时间</th> 
					        </tr>   
					    </thead>   
				  </table> 
			    </div>   
			     <div title="未到">
			    	<table  id ="dg3" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/meeting/meetingSigned/noComeList.action?id=${id}', fitColumns:true, pagination:true, striped:true, fit:true,pageList:[10,20,30,40,50],pageSize:20,rownumbers:true,border:false">
						<thead>   
					        <tr>   
					            <th data-options="field:'pName',width:80,align:'center'">姓名</th>   
					            <th data-options="field:'pAccount',width:80,align:'center'">员工号</th>   
					            <th data-options="field:'deptName',width:80,align:'center'">科室名</th>   
					            <th data-options="field:'signedTime',width:150,align:'center'">签到时间</th> 
					        </tr>   
					    </thead>   
				  </table> 
			    </div>   
			    <div title="临时参加">   
			        <table  id ="dg4" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/meeting/meetingSigned/tempComeList.action?id=${id}', fitColumns:true, pagination:true, striped:true, fit:true,pageList:[10,20,30,40,50],pageSize:20,rownumbers:true,border:false">
						<thead>   
					        <tr>   
					            <th data-options="field:'pName',width:80,align:'center'">姓名</th>   
					            <th data-options="field:'pAccount',width:80,align:'center'">员工号</th>   
					            <th data-options="field:'deptName',width:80,align:'center'">科室名</th>   
					            <th data-options="field:'signedTime',width:150,align:'center'">签到时间</th> 
					        </tr>   
					    </thead>   
				  </table> 
			    </div> 
			</div>
		</div>
	</div>	
</body>
<script type="text/javascript">

	


	//查询：
	$(function(){
		
		//添加分页提示
		$('#dg1').datagrid({
			onLoadSuccess: function(data){
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
				}
 	  		 }
		});
		
		$('#dg2').datagrid({
			onLoadSuccess: function(data){
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
				}
 	  		 }
		});
		$('#dg3').datagrid({
			onLoadSuccess: function(data){
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
				}
 	  		 }
		});
		$('#dg4').datagrid({
			onLoadSuccess: function(data){
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
				}
 	  		 }
		});
 	   
		
		
		$("#query").click(function(){
			var inputText=$('#search').textbox("getText");			
			//四个选项卡都加载
			$('#dg1').datagrid({
				queryParams: {//datagrid的请求参数中已经含有id了，所以不需要加id参数了
					searchField: inputText
				}
			});
			
			$('#dg2').datagrid({
				queryParams: {
					searchField: inputText
				}
			});
			
			$('#dg3').datagrid({
				queryParams: {
					searchField: inputText
				}
			});	
			$('#dg4').datagrid({
				queryParams: {
					searchField: inputText
				}
			});
			
		});	
		
	});
	
	


</script>	
</html>
