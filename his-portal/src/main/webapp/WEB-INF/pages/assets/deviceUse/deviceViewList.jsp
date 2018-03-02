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
	<title>领用记录</title>
<body>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		var viewId = "&{viewId}";
		//添加datagrid事件及分页
		$('#listView').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad:function(){
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
				$('#listView').datagrid('clearChecked');
				$('#listView').datagrid('clearSelections');
			}
		});
		
		//回车查询
        bindEnterEvent('nameView',searchFromView,'easyui');//查询条件
	});
	
	function reloadView(){
		//实现刷新栏目中的数据
		$('#listView').datagrid('reload');
	}
	
	//查询
	function searchFromView() {
		var nameView = $.trim($('#nameView').val());
		$('#listView').datagrid('load', {
			officeName : nameView,
		});
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
	//关闭Layout
	function closeLayout(){
		$('#divLayout').layout('remove', 'east');
	}
	
	// 列表查询重置
	function searchReloadView() {
		$('#nameView').textbox('setValue','');
		searchFromView();
	}
	
	//替换字符
	function replaceState(val){
		if(val == 0){
			return '正常';
		}
		if(val == 1){
			return '维修中';
		}
	}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
		<div data-options="region:'north',split:false,border:false,iconCls:'icon-search'" style="height: 40px;">
			<table cellspacing="0" cellpadding="0" border="0" style="padding: 7px 0px 5px 0px">
				<tr>
					<td>
						&nbsp;查询条件：<input class="easyui-textbox" name="nameView" id="nameView"  onkeydown="KeyDown()" style="width:180px"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFromView()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReloadView()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
			<input type="hidden" value="${id}" id="id"></input>
			<table id="listView" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/assets/deviceDossier/queryUseViewList.action?id=${viewId}&menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
						<th data-options="field:'officeName', width : 150">
							办公用途
						</th>
						<th data-options="field:'deviceName', width : 150">
							设备名称
						</th>
						<th data-options="field:'meterUnit', width : 80">
							计量单位
						</th>
						<th data-options="field:'purchPrice', width : 100">
							采购单价(元)
						</th>
						<th data-options="field:'deviceNo', width : 80">
							条码编号
						</th>
						<th data-options="field:'useDeptName', width : 150">
							领用科室
						</th>
						<th data-options="field:'useName', width : 150">
							领用人
						</th>
						<th data-options="field:'createTime', width : 150">
							领用时间
						</th>
						<th data-options="field:'state', width : 80" formatter="replaceState">
							状态
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reloadView()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>