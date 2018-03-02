<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>异常查询</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		var warnTypeMap=new Map();//警告类别
		var processStatusMap=new Map();//处理状态
		var warnLevelMap = new Map();//告警级别
		//加载页面
		$(function(){
			$('#list').datagrid({
				nowrap:false,    
 	            striped:true,    
 	            url:'<%=basePath%>except/HIASMongo/queryHIASException.action',   
 	            sortName: 'id',    
 	            sortOrder: 'desc',    
 	            idField:'id',
 	            pagination:true,
				fitColumns:true,
				toolbar:'#toolbarId',
				fit:true,
		   		pageSize:20,
		   		pageList:[20,30,50,100],
		   		onLoadSuccess: function(data){
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
					var rows = data.rows;
					for(var i=0;i<rows.length;i++){
						var index = $(this).datagrid('getRowIndex',rows[i]);
						$(this).datagrid('updateRow',{index:index,row:{MSG_DETAIL_show:'<a class="cls" onclick="look('+index+')" href="javascript:void(0)" style="height:20"></a>'}});
					}
					$('.cls').linkbutton({text:'查看',plain:true,iconCls:'icon-application_form_magnify'}); 
		   		}
				});
				
			//处理状态    0:未处理（默认值） ，1：处理中，2：处理完成  
			processStatusMap.put("0","未处理");
			processStatusMap.put("1","处理中");
			processStatusMap.put("2","处理完成");
			// 告警类别  0：业务告警，1：平台告警
			warnTypeMap.put("0","业务告警");
			warnTypeMap.put("1","平台告警");
			// 告警级别  1：极高，2：高，3：中，4：低，
			warnLevelMap.put("1","极高");
			warnLevelMap.put("2","高");
			warnLevelMap.put("3","中");
			warnLevelMap.put("4","低");
			
		});
		
		//回车查询
		$(window).keydown(function(event) {
		      if(event.keyCode == 13) {
		    	  searchFrom();
		      }
		});	
	//实现刷新栏目中的数据
	function reload(){
		 $("#list").datagrid("reload");
	}
	//模糊查询
	function searchFrom() {
		var codeName = $('#codeName').textbox('getValue');
		var warnLevelSelect = $('#warnLevelSelect').combobox('getValue');
		var warnTypeSelect = $('#warnTypeSelect').combobox('getValue');
		var startWarnDate = $('#startWarnDate').val();
		var endWarnDate = $('#endWarnDate').val();
		var processStatus = $('#processStatusSelect').combobox('getValue');
		if(startWarnDate!=""&&endWarnDate!=""){
			if(startWarnDate>endWarnDate){
				$.messager.alert('提示','起始时间不能大于截止时间');
				close_alert();
				return;
			}
		}
		$('#list').datagrid('clearSelections');
		$('#list').datagrid({
	   		nowrap:false,    
	        striped:true,    
	        url:'<%=basePath%>except/HIASMongo/queryHIASException.action',   
			queryParams:{"codeName":codeName,"warnLevelSelect":warnLevelSelect,"warnTypeSelect":warnTypeSelect,"startWarnDate":startWarnDate,"endWarnDate":endWarnDate,"processStatus":processStatus},
	        sortName: 'id',    
	        sortOrder: 'desc',    
	        idField:'id',
	        pagination:true,
			fitColumns:true,
			fit:true,
	   		pageSize:20,
	   		pageList:[20,30,50,100],
	   		onLoadSuccess: function(data){
				var rows = data.rows;
				for(var i=0;i<rows.length;i++){
					var index = $(this).datagrid('getRowIndex',rows[i]);
					$(this).datagrid('updateRow',{index:index,row:{MSG_DETAIL_show:'<a class="cls" onclick="look('+index+')" href="javascript:void(0)" style="height:20"></a>'}});
				}
				$('.cls').linkbutton({text:'查看',plain:true,iconCls:'icon-application_form_magnify'}); 
	   		}
		});
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function searchReload(){
		$('#codeName').textbox('setValue','');
		$('#warnLevelSelect').combobox('setValue','');
		$('#warnTypeSelect').combobox('setValue','');
		$('#warnDate').val('');
		$('#processStatusSelect').combobox('setValue','');
		searchFrom();
	}
	//查看异常详细信息	
	function look(index){
		var rows = $('#list').datagrid('getRows');
		$('#msgWinId').dialog({    
		    title: '异常详细信息',    
		    width: 800,    
		    height: 400,    
		    closed: false,    
		    cache: false,    
		    modal: true   
		}); 
		$('#showDetail').val(rows[index].MSG_DETAIL);
	}
	//渲染处理状态
	function formatProStatus(val,row){
		return processStatusMap.get(val);
	}
	//渲染告警类别
	function formatWarnType(val,row){
		return warnTypeMap.get(val);
	}
	//渲染告警级别
	function formatWarnLevel(val,row){
		return warnLevelMap.get(val);
	}
	//开始处理
	function start(){
		  //选中要删除的行
        var iid = $('#list').datagrid('getChecked');
        if (iid.length > 0) {//选中几行的话触发事件	                        
		 	 $.messager.confirm('确认', '确定要处理选中异常吗?', function(res){//提示是否处理
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						if(iid[i].PROCESS_STATUS!=0){
							$.messager.alert('提示','所选异常必须为未处理状态');						
							close_alert();
							return;
						}
						ids += iid[i].id;
					};
					$.ajax({
						url:"<%=basePath%>except/HIASMongo/startDeal.action?id="+ids,
						type:'post',
						success: function() {
						$('#list').datagrid('reload');
						}
					});										
				  }
            });
         }
	}
	//处理完成
	function end(){
		  //选中要删除的行
        var iid = $('#list').datagrid('getChecked');
        if (iid.length > 0) {//选中几行的话触发事件	                        
		 	 $.messager.confirm('确认', '异常是否处理完成?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						if(iid[i].PROCESS_STATUS==2){
							$.messager.alert('提示','所选异常已处理完成');						
							close_alert();
							return;
						}
						ids += iid[i].id;
					};
					$.ajax({
						url: "<%=basePath%>except/HIASMongo/endDeal.action?id="+ids,
						type:'post',
						success: function() {
						$('#list').datagrid('reload');
						}
					});										
				  }
            });
         }
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
</head>
<body style="margin: 0px; padding: 0px">
   <div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
   		<div  data-options="region:'north',border:false" style="height:35px;padding:5px 5px 0px 5px;">
		    <div id="searchTab">
				模块名称：
					<input  class="easyui-textbox" id="codeName" name="codeName" style="width: 120px;"/> &nbsp;
				处理状态：
					<input class="easyui-combobox" id="processStatusSelect" style="width:100px"  
		                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
		                   label: '未处理',  
		                   value: '0'},  
		                   {label: '处理中',  
		                   value: '1'},     
		                   {label: '处理完成 ',  
		                   value: '2'}]"                    
		                 /> &nbsp; 
			 	告警级别：
					<input class="easyui-combobox" id="warnLevelSelect"  style="width:50px" 
		                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
		                   label: '高',  
		                   value: '1'},  
		                   {label: '中',  
		                   value: '2'},     
		                   {label: '低',  
		                   value: '3'}]"                    
		                 /> &nbsp;
				告警类别：
					<input class="easyui-combobox" id="warnTypeSelect" style="width:100px" 
		                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
		                   label: '业务告警',  
		                   value: '0'}, 
		                   {label: '平台告警',  
		                   value: '1'}]"                    
		                 /> &nbsp;
				告警时间：
					<input id="startWarnDate"  name="warnDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:120px;border: 1px solid #95b8e7;border-radius: 5px;"/></td>&nbsp; 
				至
					<input id="endWarnDate"  name="warnDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:120px;border: 1px solid #95b8e7;border-radius: 5px;"/></td>&nbsp; 
			   	<shiro:hasPermission name="${menuAlias}:function:query">
			   		<a href="javascript:void(0)" onclick="searchFrom()" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;
			   	</shiro:hasPermission>
			   	<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a> &nbsp;
			</div>
			<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:operation">
			   	<a href="javascript:void(0)" onclick="start()" class="easyui-linkbutton" iconCls="icon-bullet_go" plain=true>开始处理</a>
			   	<a href="javascript:void(0)" onclick="end()" class="easyui-linkbutton" iconCls="icon-tick" plain=true>处理完成</a> 
			</shiro:hasPermission>
			</div>
   		</div>
   		<div data-options="region:'center',border:false" style="height:80%;width:100%;">
			<table id="list" style="width: 100%" class="easyui-datagrid">
				<thead>
					<tr>
						<th field="getIdUtil" checkbox="true" ></th>
						<th data-options="field:'CODE'" style="width: 7%">模块标识</th>
						<th data-options="field:'CODE_NAME'" style="width: 12%">模块名称</th>
						<th data-options="field:'WARN_LEVEL',formatter:formatWarnLevel" style="width: 6%">告警级别</th>
						<th data-options="field:'WARN_TYPE',formatter:formatWarnType" style="width: 7%">告警类别</th>
						<th data-options="field:'WARN_DATE'" style="width: 12%">告警时间</th>
						<th data-options="field:'PROCESS_TIME'" style="width: 12%">开始处理时间</th>
						<th data-options="field:'PROCESS_STATUS',formatter:formatProStatus" style="width: 7%">处理状态</th>
						<th data-options="field:'FINISHED_TIME'" style="width: 12%">处理完成时间</th>
						<th data-options="field:'MSG_DETAIL_show'" style="width: 10%">异常详细信息</th>
					</tr>
				</thead>
			</table>
   		</div>
	</div>
	<div id="msgWinId">
			<div class="easyui-layout" data-options="fit:true">  
				<textarea id='showDetail' style="width:100% ;height:100%;"></textarea>
			</div>
		</div> 
</body>
</html>