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
<title>设备报废管理</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'center',border:false ">
    		<table   style="padding:7px 7px 3px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
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
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
			<div  style="width:100%;height:95%;margin-top: 4px;" >
				<table id="drafts" class="easyui-datagrid"   
			        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
			        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId'">   
			   		 <thead>   
				        <tr>  
				           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
				            <th data-options="field:'officeName',width:200,align:'center'">办公用途</th>
			           		<th data-options="field:'classCode',width:150,align:'center'">类别代码</th>   
				            <th data-options="field:'className',width:150,align:'center'">设备分类</th>   
				            <th data-options="field:'deviceName',width:200,align:'center'">设备名称</th>   
				            <th data-options="field:'meterUnit',width:150,align:'center'">计量单位</th>   
				            <th data-options="field:'purchPrice',width:150,align:'center'">采购单价(元)</th>   
				            <th data-options="field:'deviceNo',width:150,align:'center'">条码编号</th>   
				            <th data-options="field:'createTime',width:150,align:'center'">报废时间</th>   
				            <th data-options="field:'manageName',width:150,align:'center'">负责人</th>   
				            <th data-options="field:'scrapReason',width:150,align:'center'">报废原因</th>   
				        </tr>   
				    </thead>   
				</table>  
			</div>
	    </div>   
   	</div>
   	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="view()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">维修记录</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="add"></div>
	<script type="text/javascript">
	$(function(){
		$("#drafts").datagrid({
			url: '<%=basePath %>assets/assetsScrap/queryAssetsScrap.action',
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	})
	function reload(){
		$('#drafts').datagrid('reload');  
	}
	function view(){
		var row = $('#drafts').datagrid('getSelected'); //获取当前选中行     
        if(row != null){
    		Adddilog("维修记录","<%=basePath %>assets/assetsRepair/repairRecord.action?deviceNo="+row.deviceNo);
           	$('#drafts').datagrid('reload');
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
	/**
	 * 查询
	 */
	function searchFrom() {
		var used = $('#used').textbox('getValue');
		var typed = $('#typed').textbox('getValue');
		var coded = $('#coded').textbox('getValue');
		var named = $('#named').textbox('getValue');
		$("#drafts").datagrid({
			url: '<%=basePath %>assets/assetsScrap/queryAssetsScrap.action',
			queryParams:{officeName:used,className:typed,deviceCode:coded,deviceName:named},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReload() {
		$('#used').textbox('setValue','');
		$('#typed').textbox('setValue','');
		$('#coded').textbox('setValue','');
		$('#named').textbox('setValue','');
		searchFrom()
	}
	</script>
</body>
</html>