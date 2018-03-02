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
		$('#listDraft').datagrid({
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
				$('#listDraft').datagrid('clearChecked');
				$('#listDraft').datagrid('clearSelections');
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#listDraft').datagrid('getSelected'); //获取当前选中行    
                if(row){
                	AddOrShowEast("查看",'<%=basePath %>assets/device/viewAssetsDevice.action?id='+rowData.id);
			   	}
			}
		});
		
		//回车查询
        bindEnterEvent('officeDraft',searchFromDraft,'easyui');//查询条件
        bindEnterEvent('nameDraft',searchFromDraft,'easyui');//查询条件
        bindEnterEvent('classesDraft',searchFromDraft,'easyui');//查询条件
        bindEnterEvent('deviceDraft',searchFromDraft,'easyui');//查询条件
	});
	//修改
	function editDraft(){
		var rows = $('#listDraft').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			addAndEdit = 1;
			closeLayout();
			AddOrShowEast('EditForm', '<%=basePath %>assets/device/updateDeviceUrl.action?id=' + getIdUtil('#listDraft'));
		}else{
			$.messager.alert('操作提示',"请选择要修改的信息！");
			close_alert();
		}
	}
	
	function delDraft(){
			var rows = $('#listDraft').datagrid('getChecked');
            if (rows.length > 0) {//选中几行的话触发事件	                        
			 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
					if (res){
						$.messager.progress({text:'删除中，请稍后...',modal:true});	// 显示进度条
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.ajax({
							url: '<%=basePath %>assets/device/deleteDevice.action?id='+ids,
							type:'post',
							success: function(data) {
								$.messager.progress('close');
								$.messager.alert('提示',data.resMsg);
								if(data.resCode=='success'){
									$('#listDraft').datagrid('reload');
								}
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
						});
					}
	           });
           }else{
        	   $.messager.alert('操作提示',"请选择要删除的信息！");
        	   setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
           }
	}
	
	function reloadDraft(){
		//实现刷新栏目中的数据
		$('#listDraft').datagrid('reload');
	}
	
	//查询
	function searchFromDraft() {
		var office = $.trim($('#officeDraft').val());
		var name = $.trim($('#nameDraft').val());
		var classes = $.trim($('#classesDraft').val());
		var device = $.trim($('#deviceDraft').val());
		$('#listDraft').datagrid('load', {
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
	function searchReloadDraft() {
		$('#officeDraft').textbox('setValue','');
		$('#nameDraft').textbox('setValue','');
		$('#classesDraft').textbox('setValue','');
		$('#deviceDraft').textbox('setValue','');
		searchFromDraft();
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
							&nbsp;办公用途：<input class="easyui-textbox" name="officeDraft" id="officeDraft" data-options="prompt:'请输入办公用途'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备分类：<input class="easyui-textbox" name="nameDraft" id="nameDraft" data-options="prompt:'请输入公司名称'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;类别代码：<input class="easyui-textbox" name="classesDraft" id="classesDraft" data-options="prompt:'请输入设备分类'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备名称：<input class="easyui-textbox" name="deviceDraft" id="deviceDraft" data-options="prompt:'请输入设备名称'" onkeydown="KeyDown()" style="width:150px"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFromDraft()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReloadDraft()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
			<input type="hidden" value="${id}" id="id"></input>
			<table id="listDraft" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/assets/device/queryAssetsDeviceDraft.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
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
						<th data-options="field:'applDate', width : 150">
							申报时间
						</th>
						<th data-options="field:'deviceState', width : 80" formatter="replaceDevice">
							入库状态
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="editDraft()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>	
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="delDraft()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reloadDraft()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>