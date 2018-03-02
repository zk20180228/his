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
	<title>我的设备</title>
<body>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#listReceive').datagrid({
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
				$('#listReceive').datagrid('clearChecked');
				$('#listReceive').datagrid('clearSelections');
			}
		});
		
		//回车查询
        bindEnterEvent('officeReceive',searchFromReceive,'easyui');//查询条件
        bindEnterEvent('nameReceive',searchFromReceive,'easyui');//查询条件
        bindEnterEvent('classesReceive',searchFromReceive,'easyui');//查询条件
        bindEnterEvent('deviceReceive',searchFromReceive,'easyui');//查询条件
	});
	//领用
	function addReceive(){
		var rows = $('#listReceive').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			addAndEdit = 1;
			closeLayout();
			AddOrShowEast('EditForm', '<%=basePath %>assets/deviceDossier/deviceReceiveUrl.action?id=' + getIdUtil('#listReceive'));
		}else{
			$.messager.alert('操作提示',"请选择要领用的设备信息！");
			close_alert();
		}
	}
	
	function reloadReceive(){
		//实现刷新栏目中的数据
		$('#listReceive').datagrid('reload');
	}
	
	//查询
	function searchFromReceive() {
		var office = $.trim($('#officeReceive').val());
		var name = $.trim($('#nameReceive').val());
		var classes = $.trim($('#classesReceive').val());
		var device = $.trim($('#deviceReceive').val());
		$('#listReceive').datagrid('load', {
			officeName : office,
			classCode : classes,
			className : name,
			deviceName : device,
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
	function searchReloadReceive() {
		$('#officeReceive').textbox('setValue','');
		$('#nameReceive').textbox('setValue','');
		$('#classesReceive').textbox('setValue','');
		$('#deviceReceive').textbox('setValue','');
		searchFromReceive();
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
	
	//领用记录
	function viewReceive(){
		var timerStr = Math.random();
		var rows = $('#listReceive').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			window.open ("<c:url value='/assets/deviceDossier/viewReceive.action?randomId='/>"+timerStr+"&id="+getIdUtil('#listReceive'),'newwindow','height=700,width=1300,top=50,left=100,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}else{
			$.messager.alert('操作提示',"请选择要修改的信息！");
			close_alert();
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
							&nbsp;办公用途：<input class="easyui-textbox" name="office" id="office" data-options="prompt:'请输入办公用途'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备分类：<input class="easyui-textbox" name="name" id="name" data-options="prompt:'请输入公司名称'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;类别代码：<input class="easyui-textbox" name="classes" id="classes" data-options="prompt:'请输入设备分类'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备名称：<input class="easyui-textbox" name="device" id="device" data-options="prompt:'请输入设备名称'" onkeydown="KeyDown()" style="width:150px"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFromReceive()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReloadReceive()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
			<input type="hidden" value="${id}" id="id"></input>
			<table id="listReceive" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/assets/deviceDossier/queryAssetsDeviceReceive.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
						<th data-options="field:'officeName', width : 150">
							办公用途
						</th>
						<th data-options="field:'classCode', width : 150">
							类别代码
						</th>
						<th data-options="field:'className', width : 150">
							设备分类
						</th>
						<th data-options="field:'deviceCode', width : 150">
							设备代码
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
						<th data-options="field:'restNum', width : 100">
							库存数量
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="addReceive()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">领用</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="viewReceive()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">领用记录</a>
			<a href="javascript:void(0)" onclick="reloadReceive()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>