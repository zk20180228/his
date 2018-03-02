<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>设备盘点管理</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
</style>
</head>
	<body style="margin: 0px;padding: 0px;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;">
					<div data-options="region:'north',split:false,border:false,iconCls:'icon-search'" >
						<table style="padding:5px 5px">
								<tr>
								<td align="right">
									办公用途：
								</td>
								<td style="width:210px;">
									<input class="easyui-textbox" name="officeName" id="officeName" data-options="prompt:'请输入办公用途'"  style="width:200px"/>
								</td>
								<td>
									设备分类：
								</td>
								<td style="width:210px;">
									<input class="easyui-textbox" name="className" id="className" data-options="prompt:'请输入设备分类'" style="width:200px"/>
								</td>
								<td>
									类别代码：
								</td>
								<td style="width:210px;">
									<input class="easyui-textbox" name="classCode" id="classCode" data-options="prompt:'请输入类别代码'" style="width:200px"/>
								</td>
								<td>
									设备名称：
								</td>
								<td style="width:210px;">
									<input class="easyui-textbox" name="deviceName" id="deviceName" data-options="prompt:'请输入设备名称'" style="width:200px"/>
								</td>
								<td>
									仓库：
								</td>
								<td style="width:210px;">
									<input class="easyui-textbox" name="depotName" id="depotName" data-options="prompt:'请输入仓库名称'" style="width:200px"/>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,border:false" style=" width: 100%;height: 95%;">
									 <table id="assetTpye" class="easyui-datagrid" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true,toolbar:'#toolbarId'">
							<thead>
								<tr>
									<th style="width: 10%;" data-options="field:'officeName'" align="center">办公用途</th>
									<th style="width: 10%;" data-options="field:'classCode'" align="center">类别代码</th>
									<th style="width: 10%;" data-options="field:'className'" align="center">设备分类</th>
									<th style="width: 10%;" data-options="field:'deviceName'" align="center">设备名称</th>
									<th style="width: 8%;" data-options="field:'meterUnit'" align="center">计量单位</th>
									<th style="width: 10%;" data-options="field:'reperNum'" align="center">库存数量</th>
									<th style="width: 10%;" data-options="field:'checkNum'" align="center">实盘量</th>
									<th style="width: 10%;" data-options="field:'depotName'" align="center">仓库名称</th>
									<th style="width: 10%;" data-options="field:'manageName'" align="center">负责人</th>
									<th style="width: 10%;" data-options="field:'checkDate'" align="center">盘点日期 </th>
								</tr>
							</thead>
						</table> 
					</div>
			</div>
<div id="toolbarId">
<shiro:hasPermission name="${menuAlias}:function:edit">
	<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">盘点</a>
	<a href="javascript:void(0)" onclick="correcting()" class="easyui-linkbutton" data-options="iconCls:'icon-cut',plain:true">校正</a>
</shiro:hasPermission>
	<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
</div>
<script type="text/javascript">
var type=1;
//加载页面
$(function(){
	 $("#assetTpye").datagrid({
			url:'<%=basePath %>assets/assetsDeviceCheck/queryAssetsDeviceCheck.action',
			queryParams:{
				officeName:$('#officeName').textbox("getText"),
				className:$('#className').textbox("getText"),
				classCode:$('#classCode').textbox("getText"),
				deviceName:$('#deviceName').textbox("getText"),
				depotName:$('#depotName').textbox("getText")
				},
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,100],
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
})	

	function query(){
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		 $("#assetTpye").datagrid('load',{
			 	officeName:$('#officeName').textbox("getText"),
				className:$('#className').textbox("getText"),
				classCode:$('#classCode').textbox("getText"),
				deviceName:$('#deviceName').textbox("getText"),
				depotName:$('#depotName').textbox("getText")
				}
		 );
		 $.messager.progress('close');
	}

	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-21
	 * @version 1.0
	 */
	function clears(){
		$("#officeName").textbox("setValue","");
		$("#className").textbox("setValue","");
		$("#classCode").textbox("setValue","");
		$("#deviceName").textbox("setValue","");
		$("#depotName").textbox("setValue","");
		query();
	}
  	//盘点
	function edit(){
		var rows = $('#assetTpye').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			closeLayout();
			AddOrShowEast('EditForm', '<%=basePath %>assets/assetsDeviceCheck/updateAssetsDeviceCheckUrl.action?id=' + rows.id);
		}else{
			$.messager.alert('操作提示',"请选择需要盘点的信息！");
			close_alert();
		}
	}
  	//校正
	function correcting(){
		var rows = $('#assetTpye').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			closeLayout();
	  		$.messager.confirm('确认', '确定要校正选中信息吗?', function(res){//提示是否删除
	  			if (res){
					$.ajax({
						url: '<%=basePath%>assets/assetsDeviceCheck/Correcting.action?id='+rows.id,
						type:'post',
						success: function() {
							$.messager.alert("提示",'校正成功!');
							query();
						}
					});										
				}
			});
		}else{
			$.messager.alert('操作提示',"请选择需要校正的信息！");
			close_alert();
		}
	}
	//关闭Layout
	function closeLayout(){
		$('#divLayout').layout('remove', 'east');
	}
   	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		//获取右侧收缩面板
		var eastpanel=$('#panelEast'); 
		//判断右侧收缩面板是否存在
		if(eastpanel.length>0){
			//重新装载右侧面板
	   		$('#divLayout').layout('panel','east').panel({
                      href:url 
               });
		}else{
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				split : true,
				border : false,
				href : url,
				closable : true
			});
		}
			
	}
	//刷新
	function reload(){
		$('#assetTpye').datagrid('reload');
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>