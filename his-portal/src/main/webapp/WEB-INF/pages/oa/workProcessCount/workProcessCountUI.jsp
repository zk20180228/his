<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>工作流程统计</title>
</head>

<body>
	<div id="tt" class="easyui-tabs"  data-options="fit:true,border:false,tabWidth:200,tabHeight:40,narrow:true">   
		    <div title="待办工作" data-options="iconCls:'icon-pencil'" > 
		       <div style="height:8%;weight:100%;">
		       		<div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
		       			流水号:<input  id="search_text_1" class="easyui-textbox"  style="height:30px;width:120px">
		       			工作名称/文号:<input  id="search_text_2" class="easyui-textbox"  style="height:30px;width:150px">
		       			<a id="search_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">查询</a>  
		       			<a id="newWork_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">新建工作</a> 
		       			<a id="entrust_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">委托</a> 
		       			<a id="attention_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">一键关注</a>
			       		<a id="hangUp_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">挂起</a> 
			       		<a id="postil_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">批注</a> 
			       		<a id="exportWorkList_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">导出工作列表</a> 
			       		<a id="refresh_1" class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">刷新</a> 
		      		</div>	
		       </div> 
		       
			   <div style="height:92%;weight:100%;"><table id="dg1"></table></div>  
		    </div>   
		    
		    <div title="办结工作"  data-options="iconCls:'icon-pencil'">   
		       <div style="height:8%;weight:100%;">
		       		<div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
		       			流水号:<input  id="search_text_3" class="easyui-textbox"  style="height:30px;width:120px">
		       			工作名称/文号:<input  id="search_text_4" class="easyui-textbox"  style="height:30px;width:150px">
		       			<a id="search_2"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">查询</a>  
		       			<a id="newWork_2"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">新建工作</a> 
		       			<a id="entrust_2"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">一键催办</a> 
			       		<a id="exportWorkList_2"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">导出工作列表</a> 
			       		<a id="refresh_2"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">刷新</a> 
		      		</div>	
		       </div> 
		       
			   <div style="height:92%;weight:100%;"><table id="dg2"></table></div>  
		    </div>  
		     
		    <div title="关注工作"  data-options="iconCls:'icon-pencil'"> 
			     <div style="height:8%;weight:100%;">
			       		<div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
		       				流水号:<input  id="search_text_5" class="easyui-textbox"  style="height:30px;width:120px">
		       				工作名称/文号:<input  id="search_text_6" class="easyui-textbox"  style="height:30px;width:150px">			       		
		       				<a id="search_3"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">查询</a>  
			       			<a id="newWork_3"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">新建工作</a> 
			       			<a id="entrust_3"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">一键催办</a> 
			       			<a id="attention_3"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">取消关注</a> 
				       		<a id="exportWorkList_3"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">导出工作列表</a> 
				       		<a id="refresh_3"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">刷新</a> 
			      		</div>	
			       </div> 
			       
				   <div style="height:92%;weight:100%;"><table id="dg3"></table></div>    
		    </div> 
		      
		    <div title="挂起工作"  data-options="iconCls:'icon-pencil'"> 
		    	 	<div style="height:8%;weight:100%;">
			       		<div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
		       				流水号:<input  id="search_text_7" class="easyui-textbox"  style="height:30px;width:120px">
		       				工作名称/文号:<input  id="search_text_8" class="easyui-textbox"  style="height:30px;width:150px">				       			
		       				<a id="search_4"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">查询</a>  
			       			<a id="newWork_4"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">新建工作</a> 
			       			<a id="recovery_4"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">恢复</a> 
				       		<a id="exportWorkList_4"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">导出工作列表</a> 
				       		<a id="refresh_4"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">刷新</a> 
			      		</div>	
			       </div> 
			       
				   <div style="height:92%;weight:100%;"><table id="dg4"></table></div>    	  
		   	</div>  
		   	
		    <div title="委托工作"  data-options="iconCls:'icon-pencil'">
		    	 <div style="height:8%;weight:100%;">
			       		<div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
		       				流水号:<input  id="search_text_9" class="easyui-textbox"  style="height:30px;width:120px">
		       				工作名称/文号:<input  id="search_text_10" class="easyui-textbox"  style="height:30px;width:150px">				       			
		       				<a id="search_5"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">查询</a>  
			       			<a id="newWork_5"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">新建工作</a> 
			       			<a id="entrust_5"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">一键催办</a> 
				       		<a id="exportWorkList_5"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">导出工作列表</a> 
				       		<a id="refresh_5"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">刷新</a> 
			      		</div>	
			       </div> 
			       
				   <div style="height:92%;weight:100%;"><table id="dg5"></table></div>    
		    </div>  
		    <div title="全部工作"  data-options="iconCls:'icon-pencil'">
		    	  <div style="height:8%;weight:100%;">
			       		<div style="padding-top:15px;padding-bottom:0px;padding-left:5px;text-align:left">
		       				流水号:<input  id="search_text_11" class="easyui-textbox"  style="height:30px;width:120px">
		       				工作名称/文号:<input  id="search_text_12" class="easyui-textbox"  style="height:30px;width:150px">					       			
		       				<a id="search_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">查询</a>  
			       			<a id="newWork_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">新建工作</a> 
<!-- 			       			<a id="entrust_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">委托</a>  -->
<!-- 				       		<a id="hangUp_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">挂起</a>  -->
<!-- 				       		<a id="postil_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">批注</a>  -->
				       		<a id="exportWorkList_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">导出工作列表</a> 
				       		<a id="refresh_6"  class="easyui-linkbutton buttonStyle" style="height:35px;width:120px;background-color:#00CC00;">刷新</a> 
			      		</div>	
			       </div> 
			       
				   <div style="height:92%;weight:100%;"><table id="dg6"></table></div>
		    </div>  
	</div> 

</body>
</html>
<script type="text/javascript">
	
	$(function(){
		
// 		$.messager.confirm('确认','您是连的208数据库吗？',function(r){    
// 		    if (r){    
// 		    	$.messager.alert('提示','你现在可以测试了！');    
// 		    }else{
// 		    	$.messager.alert('警告','.6可能没有测试数据哦！ 当然了,你可以去添加！');  
// 		    }    
// 		}); 
		
		/************************************************************************************待办工作************************************************************************/
		//待办工作
		$('#dg1').datagrid({    //&work_Name='+encodeURI("审批")
		    url:'${pageContext.request.contextPath}/oa/WorkProcessCount/workList.action?work_Flag=1',   
		    fitColumns:true,
		    idField:"id",
		    pagination:true,
		    striped:true,
		    fit:true,
		    pageList:[10,20,30,40,50],
		    pageSize:10,
		    columns:[[    
		        {field:'id',checkbox:true,width:100},    
		        {field:'serialNumber',title:'流水号',width:100,align:'center'},    
		        {field:'workName',title:'工作名称/文号',width:100,align:'center'},
		        {field:'processMap',title:'我经办的步骤(流程图)',width:100,align:'center'}, 
		        {field:'createPerson',title:'发起人',width:100,align:'center'}, 
		        {field:'state',title:'状态',width:100,align:'center'}, 
		        {field:'arriveTime',title:'到达时间',width:100,align:'center'}, 
		        {field:'remainTime',title:'已停留',width:100,align:'center'},
		        {field:'option',title:'操作',width:100,align:'center'}
		    ]]});  
		
		//查询
		$("#search_1").click(function(){
			var serialNumber=$("#search_text_1").val();
			var workName=$("#search_text_2").val();
			$("#dg1").datagrid("load",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		//新建工作
		
		//委托
		
		//一键关注
		$("#attention_1").click(function(){
			
			var workId = new Array();
			var rows=$("#dg1").datagrid("getSelections");
			for(var i=0;i<rows.length;i++){
				workId.push(rows[i].id);
			}
			if(workId.length>0){
				$.ajax({
					type:"post",
					url:"${pageContext.request.contextPath}/oa/WorkProcessCount/attention.action",
					data:"workId="+workId,
					success:function(backData){
						if("true"==backData){
							$.messager.alert('提示','关注成功！');
							//成功之后,刷新关注页面
							var serialNumber=$("#search_text_5").val();
							var workName=$("#search_text_6").val();
							$("#dg3").datagrid("load",{
								work_Name:workName,
								serial_Number:serialNumber
							});
							//成功之后，取消当前页所有选择的行
							$("#dg1").datagrid("clearChecked");
						}else{
							$.messager.alert('提示','关注失败！');
						}
					}
				});
			}else{
				$.messager.alert('提示','请至少选择一行！');
			}
			
		});
		
		

		//挂起 hangUpwork
		$("#hangUp_1").click(function(){
			//alert("xx");
			var workId = new Array();
			var rows=$("#dg1").datagrid("getSelections");
			for(var i=0;i<rows.length;i++){
				workId.push(rows[i].id);
			}
			
			if(workId.length>0){
				$.ajax({
					type:"post",
					url:'${pageContext.request.contextPath}/oa/WorkProcessCount/hangUpwork.action',
					data:"workId="+workId,
					success:function(backData){
						if("true"==backData){
							$.messager.alert('提示','挂起成功！');
							
							//成功之后,刷新当前页面
							var serialNumber=$("#search_text_1").val();
							var workName=$("#search_text_2").val();
							$("#dg1").datagrid("reload",{
								work_Name:workName,
								serial_Number:serialNumber
							});
							//刷新挂起页面
							var serialNumber_7=$("#search_text_7").val();
							var workName_8=$("#search_text_8").val();
							$("#dg4").datagrid("load",{
								work_Name:workName_8,
								serial_Number:serialNumber_7
							});
							
							//刷新全部任务列表-->因为要更新任务的状态
							var serialNumber_11=$("#search_text_11").val();
							var workName_12=$("#search_text_12").val();
							$("#dg6").datagrid("load",{
								work_Name:workName_12,
								serial_Number:serialNumber_11
							});
							
							//成功之后，取消当前页所有选择的行
							$("#dg1").datagrid("clearChecked");
						}else{
							$.messager.alert('提示','挂起失败！'); 
						}

					}
				});
				
			}else{
				$.messager.alert('提示','请至少选择一行！');
			}
			
		});
		

		
		//批注
		
		
		
		//刷新
		$("#refresh_1").click(function(){
			var serialNumber=$("#search_text_1").val();
			var workName=$("#search_text_2").val();
			$("#dg1").datagrid("reload",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		//导出工作列表
		
		
		
		/************************************************************************************办结工作************************************************************************/
		//办结工作
		$('#dg2').datagrid({    //&serial_Number=127501&work_Name='+encodeURI("审批")
		    url:'${pageContext.request.contextPath}/oa/WorkProcessCount/workList.action?work_Flag=2',   
		    fitColumns:true,
		    idField:"id",
		    pagination:true,
		    fit:true,
		    pageList:[10,20,30,40,50],
		    pageSize:10,
		    columns:[[    
		        {field:'id',checkbox:true,width:100},    
		        {field:'serialNumber',title:'流水号',width:100,align:'center'},    
		        {field:'workName',title:'工作名称/文号',width:100,align:'center'},
		        {field:'processMap',title:'我经办的步骤(流程图)',width:100,align:'center'}, 
		        {field:'createPerson',title:'发起人',width:100,align:'center'}, 
		        {field:'endTime',title:'办结时间',width:100,align:'center'}, 
		        {field:'processSate',title:'流程状态',width:100,align:'center'}, 
		        {field:'option',title:'操作',width:100,align:'center'}
		    ]]});  
		
		//查询
		$("#search_2").click(function(){
			var serialNumber=$("#search_text_3").val();
			var workName=$("#search_text_4").val();
			$("#dg2").datagrid("load",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		//新建工作
		
		//一键催办
		
		//导出工作列表
		
		//刷新
		$("#refresh_2").click(function(){
			var serialNumber=$("#search_text_3").val();
			var workName=$("#search_text_4").val();
			$("#dg2").datagrid("reload",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		
		/************************************************************************************关注工作************************************************************************/
		//关注工作
		$('#dg3').datagrid({    
		    url:'${pageContext.request.contextPath}/oa/WorkProcessCount/workList.action?work_Flag=3',  
		    fitColumns:true,
		    idField:"id",
		    fit:true,
		    pagination:true,
		    pageList:[10,20,30,40,50],
		    pageSize:10,
		    columns:[[    
		        {field:'id',checkbox:true,width:100},    
		        {field:'serialNumber',title:'流水号',width:100,align:'center'}, 
		        {field:'processType',title:'流程类型',width:100,align:'center'}, 
		        {field:'workName',title:'工作名称/文号',width:100,align:'center'},
		        {field:'arriveTime',title:'开始时间',width:100,align:'center'}, 
		        {field:'pbAttachment',title:'公共附件',width:100,align:'center'}, 
		        {field:'state',title:'状态',width:100,align:'center'},
		        {field:'option',title:'操作',width:100,align:'center'}
		    ]]});  
		
		//查询
		$("#search_3").click(function(){
			var serialNumber=$("#search_text_5").val();
			var workName=$("#search_text_6").val();
			$("#dg3").datagrid("load",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		//新建工作
		
		//一键催办
		
		//导出工作列表
		
		//取消关注
		$("#attention_3").click(function(){
			
			var workId = new Array();
			var rows=$("#dg3").datagrid("getSelections");
			for(var i=0;i<rows.length;i++){
				workId.push(rows[i].id);
			}
			if(workId.length>0){
				$.ajax({
					type:"post",
					url:"${pageContext.request.contextPath}/oa/WorkProcessCount/cancelAttention.action",
					data:"workId="+workId,
					success:function(backData){
						if("true"==backData){
							$.messager.alert('提示','取消关注成功！');
							//成功之后,刷新当前页面
							var serialNumber=$("#search_text_5").val();
							var workName=$("#search_text_6").val();
							$("#dg3").datagrid("reload",{
								work_Name:workName,
								serial_Number:serialNumber
							});
							//成功之后，取消当前页所有选择的行
							$("#dg3").datagrid("clearChecked");
							
						}else{
							$.messager.alert('提示','取消关注失败！');
						}
					}
				});
			}else{
				$.messager.alert('提示','请至少选择一行！');
			}
			
		});
		
		
		
		
		//刷新
		$("#refresh_3").click(function(){
			var serialNumber=$("#search_text_5").val();
			var workName=$("#search_text_6").val();
			$("#dg3").datagrid("reload",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		
		
		
		/************************************************************************************挂起工作************************************************************************/
		//挂起工作
		$('#dg4').datagrid({    
		    url:'${pageContext.request.contextPath}/oa/WorkProcessCount/workList.action?work_Flag=4',   
		    fitColumns:true,
		    idField:"id",
		    fit:true,
		    pagination:true,
		    pageList:[10,20,30,40,50],
		    pageSize:10,
		    columns:[[    
		        {field:'id',checkbox:true,width:100},    
		        {field:'serialNumber',title:'流水号',width:100,align:'center'},    
		        {field:'workName',title:'工作名称/文号',width:100,align:'center'},
		        {field:'processMap',title:'我经办的步骤(流程图)',width:100,align:'center'}, 
		        {field:'createPerson',title:'发起人',width:100,align:'center'}, 
		        {field:'arriveTime',title:'到达时间',width:100,align:'center'}, 
		        {field:'cancleTime',title:'取消挂起时间',width:100,align:'center'}, 
		        {field:'option',title:'操作',width:100,align:'center'}
		    ]]});  
		
		//查询
		$("#search_4").click(function(){
			var serialNumber=$("#search_text_7").val();
			var workName=$("#search_text_8").val();
			$("#dg4").datagrid("load",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		//新建工作
		
		//恢复到激活状态(非挂起状态)
		$("#recovery_4").click(function(){
			
			var workId = new Array();
			var rows=$("#dg4").datagrid("getSelections");
			for(var i=0;i<rows.length;i++){
				workId.push(rows[i].id);
			}
			
			if(workId.length>0){
				$.ajax({
					type:"post",
					url:"${pageContext.request.contextPath}/oa/WorkProcessCount/hangUpRecoveryWork.action",
					data:"workId="+workId,
					success:function(backData){
						if("true"==backData){
							$.messager.alert('提示','恢复成功！');
							//成功之后,刷新当前页面
							var serialNumber=$("#search_text_7").val();
							var workName=$("#search_text_8").val();
							$("#dg4").datagrid("reload",{
								work_Name:workName,
								serial_Number:serialNumber
							});
							
							//刷新待办任务列表
							var serialNumber_7=$("#search_text_1").val();
							var workName_8=$("#search_text_2").val();
							$("#dg1").datagrid("load",{
								work_Name:workName_8,
								serial_Number:serialNumber_7
							});
							
							//刷新全部任务列表-->因为要更新任务的状态
							var serialNumber_11=$("#search_text_11").val();
							var workName_12=$("#search_text_12").val();
							$("#dg6").datagrid("load",{
								work_Name:workName_12,
								serial_Number:serialNumber_11
							});
							
							//成功之后，取消当前页所有选择的行
							$("#dg4").datagrid("clearChecked");
						}else{
							$.messager.alert('提示','恢复失败！');
						}
					}
				});
			}else{
				$.messager.alert('提示','请至少选择一行！');
			}
		});
		
		//导出工作列表
		
		
		
		//刷新
		$("#refresh_4").click(function(){
			var serialNumber=$("#search_text_7").val();
			var workName=$("#search_text_8").val();
			$("#dg4").datagrid("reload",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		
		
		/************************************************************************************委托工作************************************************************************/
		//委托工作
		$('#dg5').datagrid({    
		    url:'${pageContext.request.contextPath}/oa/WorkProcessCount/workList.action?work_Flag=5',   
		    fitColumns:true,
		    idField:"id",
		    fit:true,
		    pagination:true,
		    pageList:[10,20,30,40,50],
		    pageSize:10,
		    columns:[[    
		        {field:'id',checkbox:true,width:100},    
		        {field:'serialNumber',title:'流水号',width:100,align:'center'},    
		        {field:'workName',title:'工作名称/文号',width:100,align:'center'},
		        {field:'processMap',title:'我委托的步骤(流程图)',width:100,align:'center'}, 
		        {field:'createPerson',title:'发起人',width:100,align:'center'}, 
		        {field:'optionPerson',title:'办理人',width:100,align:'center'}, 
		        {field:'state',title:'状态',width:100,align:'center'}, 
		        {field:'option',title:'操作',width:100,align:'center'}
		    ]]});  
		
		
		//查询
		$("#search_5").click(function(){
			var serialNumber=$("#search_text_9").val();
			var workName=$("#search_text_10").val();
			$("#dg5").datagrid("load",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		
		//新建工作
		
		//一键催办
		
		
		//导出工作列表
		
		
		
		//刷新
		$("#refresh_5").click(function(){
			var serialNumber=$("#search_text_10").val();
			var workName=$("#search_text_11").val();
			$("#dg5").datagrid("reload",{
				work_Name:workName,
				serial_Number:serialNumber
			});
		});
		/************************************************************************************全部工作************************************************************************/
		//全部工作
		$('#dg6').datagrid({    
		    url:'${pageContext.request.contextPath}/oa/WorkProcessCount/workList.action?work_Flag=6',   
		    fitColumns:true,
		    idField:"id",
		    fit:true,
		    pagination:true,
		    pageList:[10,20,30,40,50],
		    pageSize:10,
		    columns:[[    
		        {field:'id',checkbox:true,width:100},    
		        {field:'serialNumber',title:'流水号',width:100,align:'center'},    
		        {field:'workName',title:'工作名称/文号',width:100,align:'center'},
		        {field:'processMap',title:'我经办的步骤(流程图)',width:100,align:'center'}, 
		        {field:'createPerson',title:'发起人',width:100,align:'center'}, 
		        {field:'state',title:'状态',width:100,align:'center'}, 
		        {field:'arriveTime',title:'到达时间',width:100,align:'center'},
		        {field:'endTime',title:'办结时间',width:100,align:'center'}, 
		        {field:'remainTime',title:'已停留',width:100,align:'center'},
		        {field:'option',title:'操作',width:100,align:'center'}
		    ]]});  
	});
	
	//查询
	$("#search_6").click(function(){
		var serialNumber=$("#search_text_11").val();
		var workName=$("#search_text_12").val();
		$("#dg6").datagrid("load",{
			work_Name:workName,
			serial_Number:serialNumber
		});
	});
	
	//新建工作
	
	
	//导出工作列表
	
	
	
	//刷新
	$("#refresh_6").click(function(){
		var serialNumber=$("#search_text_11").val();
		var workName=$("#search_text_12").val();
		$("#dg6").datagrid("reload",{
			work_Name:workName,
			serial_Number:serialNumber
		});
	});
	/****************************************************************************************************************************************************************************/
</script>



