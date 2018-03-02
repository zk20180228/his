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
	<title>设备入库管理</title>
<body>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#listApproval').datagrid({
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
				$('#listApproval').datagrid('clearChecked');
				$('#listApproval').datagrid('clearSelections');
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#listApproval').datagrid('getSelected'); //获取当前选中行    
                if(row){
                	AddOrShowEast("查看",'<%=basePath %>assets/device/viewAssetsDevice.action?id='+rowData.id);
			   	}
			}
		});
		
		//回车查询
        bindEnterEvent('officeApproval',searchFromApproval,'easyui');//查询条件
        bindEnterEvent('nameApproval',searchFromApproval,'easyui');//查询条件
        bindEnterEvent('classesApproval',searchFromApproval,'easyui');//查询条件
        bindEnterEvent('deviceApproval',searchFromApproval,'easyui');//查询条件
	});
	
	function reloadApproval(){
		//实现刷新栏目中的数据
		$('#listApproval').datagrid('reload');
	}
	
	//查询
	function searchFromApproval() {
		var office = $.trim($('#officeApproval').val());
		var name = $.trim($('#nameApproval').val());
		var classes = $.trim($('#classesApproval').val());
		var device = $.trim($('#deviceApproval').val());
		$('#listApproval').datagrid('load', {
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
	function searchReloadApproval() {
		$('#officeApproval').textbox('setValue','');
		$('#nameApproval').textbox('setValue','');
		$('#classesApproval').textbox('setValue','');
		$('#deviceApproval').textbox('setValue','');
		searchFromApproval();
	}
	
	//替换字符
	function replaceDevice(val){
		if(val == 0){
			return '草稿';
		}
		if(val == 1){
			return '申请';
		}
		if(val == 2){
			return '未批准';
		}
		if(val == 3){
			return '已入库';
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
							&nbsp;办公用途：<input class="easyui-textbox" name="officeApproval" id="officeApproval" data-options="prompt:'请输入办公用途'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备分类：<input class="easyui-textbox" name="nameApproval" id="nameApproval" data-options="prompt:'请输入公司名称'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;类别代码：<input class="easyui-textbox" name="classesApproval" id="classesApproval" data-options="prompt:'请输入设备分类'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备名称：<input class="easyui-textbox" name="deviceApproval" id="deviceApproval" data-options="prompt:'请输入设备名称'" onkeydown="KeyDown()" style="width:150px"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFromApproval()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReloadApproval()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
			<input type="hidden" value="${id}" id="id"></input>
			<table id="listApproval" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/assets/device/queryAssetsDeviceApproval.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
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
						<th data-options="field:'deviceNum', width : 100">
							入库数量
						</th>
						<th data-options="field:'purchTotal', width : 130">
							采购总价(元)
						</th>
						<th data-options="field:'depotName', width : 120">
							所属仓库
						</th>
						<th data-options="field:'applName', width : 150">
							申报人姓名
						</th>
						<th data-options="field:'deviceDate', width : 150">
							入库时间
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reloadApproval()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>