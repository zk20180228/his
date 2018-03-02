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
<title>设备维修管理</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'center',border:false" ">
    		<div id="tt" class="easyui-tabs" data-options="border:false">   
			     <div  title="待维修" >   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="used"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备分类:</span>
								<input id="typed" class="easyui-textbox" style="width: 130px;"/>
								&nbsp;&nbsp;
								<span>设备代码：</span>
								<input class="easyui-textbox" id="coded"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="named"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromDWX()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadDWX()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 5px;" >
						<table id="declared" class="easyui-datagrid"    
						        data-options="idField:'id',rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,
						        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId'">   
						    <thead>   
						        <tr>   
						           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>   
						            <th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'deviceNo',width:150,align:'center'">条码编号</th>   
						            <th data-options="field:'maintainNum',width:150,align:'center'">维修次数</th>   
						            <th data-options="field:'useDeptName',width:150,align:'center'">申请科室</th>   
						            <th data-options="field:'useName',width:150,align:'center'">申请人</th>   
						            <th data-options="field:'createTime',width:150,align:'center'">申请时间</th>   
						        </tr>   
						    </thead>   
						</table>  
					</div>
			    </div>   
			    <div title="已维修">   
					<table style="padding:7px 7px 5px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td >
								<span>办公用途：</span>
								<input class="easyui-textbox" id="using"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备分类:</span>
								<input id="typing" class="easyui-textbox"  style="width: 130px;"/>
								&nbsp;&nbsp;
								<span>设备代码：</span>
								<input class="easyui-textbox" id="coding"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<span>设备名称：</span>
								<input class="easyui-textbox" id="naming"  style="width: 150px;"/>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFromYWX()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchReloadYWX()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
					<div style="width:100%;height:91%;margin-top: 5px;" >
						<table id="declaring"  class="easyui-datagrid"   
					        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
					        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId1'">   
					   		 <thead>   
						        <tr>   
					           		<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
						            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>   
						            <th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
						            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
						            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
						            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
						            <th data-options="field:'deviceNo',width:150,align:'center'">条码编号</th>   
						            <th data-options="field:'maintainNum',width:150,align:'center'">维修次数</th>   
						            <th data-options="field:'applDate',width:150,align:'center'">维修时间</th>   
						            <th data-options="field:'applName',width:150,align:'center'">负责人</th>   
						               
						        </tr>   
					    	</thead>   
						</table>  
					</div>
			    </div>   
			    </div>   
    	</div>   
	</div>  
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="pass()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">维修</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="unratified()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">报废</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="viewDWX()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">维修记录</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloaded()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId1">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="viewYWX()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">维修记录</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloading()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="add"></div>
	<script type="text/javascript">
	$('#tt').tabs({
		onSelect: function(title,index){
			var state;
			if(title=="待维修"){
				state = 1;
				$("#declared").datagrid({
					url: '<%=basePath %>assets/deviceDossier/queryAssetsRepair.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}else if(title=="已维修"){
				state =0;
				$("#declaring").datagrid({
					url: '<%=basePath %>assets/assetsRepair/queryAssetsRepair.action?state='+state,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:true
				})
				
			}
		}
	})
	/**
	 * 查询
	 */
	function searchFromDWX() {
		var used = $('#used').textbox('getValue');
		var typed = $('#typed').textbox('getValue');
		var coded = $('#coded').textbox('getValue');
		var named = $('#named').textbox('getValue');
		$("#declared").datagrid({
			url: '<%=basePath %>assets/deviceDossier/queryAssetsRepair.action',
			queryParams:{officeName:used,className:typed,deviceCode:coded,deviceName:named,state:1},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReloadDWX() {
		$('#used').textbox('setValue','');
		$('#typed').textbox('setValue','');
		$('#coded').textbox('setValue','');
		$('#named').textbox('setValue','');
		searchFromDWX()
	}
	/**
	 * 查询
	 */
	function searchFromYWX() {
		var used = $('#using').textbox('getValue');
		var typed = $('#typing').textbox('getValue');
		var coded = $('#coding').textbox('getValue');
		var named = $('#naming').textbox('getValue');
		$("#declaring").datagrid({
			url: '<%=basePath %>assets/assetsRepair/queryAssetsRepair.action',
			queryParams:{officeName:used,className:typed,deviceCode:coded,deviceName:named,state:0},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReloadYWX() {
		$('#using').textbox('setValue','');
		$('#typing').textbox('setValue','');
		$('#coding').textbox('setValue','');
		$('#naming').textbox('setValue','');
		searchFromYWX()
	}
	function pass(){
		var row = $('#declared').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
        	Adddilog("维修内容","<%=basePath %>assets/deviceDossier/repairResonView.action?id="+row.id);
           	$('#declared').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
		
	}
	function unratified(){
		var row = $('#declared').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
        	Adddilog("报废原因","<%=basePath %>assets/deviceDossier/scrapResonView.action?id="+row.id);
           	$('#declared').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	function viewDWX(){
		var state =0;
		var row = $('#declared').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("维修记录","<%=basePath %>assets/assetsRepair/repairRecord.action?state="+state+"&deviceNo="+row.deviceNo);
           	$('#declared').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	function viewYWX(){
		var state =1;
		var row = $('#declaring').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("维修记录","<%=basePath %>assets/assetsRepair/repairRecord.action?state="+state+"&deviceNo="+row.deviceNo);
           	$('#declaring').datagrid('reload');
   		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
	function Adddilog(title, url) {
		$('#add').dialog({    
		    title: title,    
		    width: '800px',    
		    height:'500px',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
	   });    
	}
	function closeDialog(){
		$('#add').dialog('close');  
	}
	function reloaded(){
		$('#declared').datagrid('reload');
	}
	function reloading(){
		$('#declaring').datagrid('reload');
	}
	</script>
</body>
</html>