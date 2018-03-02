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
<title>设备采购计划申报</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true"> 
    		<table   style="padding:7px 7px 3px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td >
						<span>办公用途：</span>
						<input class="easyui-textbox" id="useCG"  data-options="prompt:'请输入办公用途'"  style="width: 150px;"/>
						&nbsp;&nbsp;
						<span>设备分类:</span>
						<input id="typeCG" class="easyui-textbox" data-options="prompt:'请输入设备分类'"  style="width: 130px;"/>
						&nbsp;&nbsp;
						<span>类别代码：</span>
						<input class="easyui-textbox" id="codeCG"  data-options="prompt:'请输入类别代码'"  style="width: 150px;"/>
						&nbsp;&nbsp;
						<span>设备名称：</span>
						<input class="easyui-textbox" id="nameCG"  data-options="prompt:'请输入设备名称'"  style="width: 150px;"/>
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
				            <th data-options="field:'planNum',width:150,align:'center'">计划总量</th>   
				        </tr>   
				    </thead>   
				</table>  
			</div>
	    </div>   
   	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#drafts").datagrid({
			url: '<%=basePath %>assets/assetsPurchase/queryAllAssetsStat.action',
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	})
	function reload(){
		$('#drafts').datagrid('reload');  
	}
	/**
	 * 查询
	 */
	function searchFrom() {
		var used = $('#useCG').textbox('getValue');
		var typed = $('#typeCG').textbox('getValue');
		var coded = $('#codeCG').textbox('getValue');
		var named = $('#nameCG').textbox('getValue');
		$("#drafts").datagrid({
			url: '<%=basePath %>assets/assetsPurchase/queryAllAssetsStat.action',
			queryParams:{officeName:used,
				className:typed,
				classCode:coded,
				deviceName:named,
				state:0},
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:true
		})
	}
	function searchReload() {
		$('#useCG').textbox('setValue','');
		$('#typCeG').textbox('setValue','');
		$('#codeCG').textbox('setValue','');
		$('#nameCG').textbox('setValue','');
		searchFrom()
	}
	</script>
</body>
</html>