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
		$('#listMyUse').datagrid({
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
				$('#listMyUse').datagrid('clearChecked');
				$('#listMyUse').datagrid('clearSelections');
			}
		});
		
		//回车查询
        bindEnterEvent('officeMyUse',searchFromMyUse,'easyui');//查询条件
        bindEnterEvent('nameMyUse',searchFromMyUse,'easyui');//查询条件
        bindEnterEvent('classesMyUse',searchFromMyUse,'easyui');//查询条件
        bindEnterEvent('deviceMyUse',searchFromMyUse,'easyui');//查询条件
	});
	
	function reloadMyUse(){
		//实现刷新栏目中的数据
		$('#listMyUse').datagrid('reload');
	}
	
	//查询
	function searchFromMyUse() {
		var office = $.trim($('#officeMyUse').val());
		var name = $.trim($('#nameMyUse').val());
		var classes = $.trim($('#classesMyUse').val());
		var device = $.trim($('#deviceMyUse').val());
		$('#listMyUse').datagrid('load', {
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
	function searchReloadMyUse() {
		$('#officeMyUse').textbox('setValue','');
		$('#nameMyUse').textbox('setValue','');
		$('#classesMyUse').textbox('setValue','');
		$('#deviceMyUse').textbox('setValue','');
		searchFromMyUse();
	}
	
	//维修
	function repair(){
		var rows = $('#listMyUse').datagrid('getChecked');
        if (rows.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '该设备确认需要维修吗?', function(res){//提示是否删除
				if (res){
					$.messager.progress({text:'确认维修中，请稍后...',modal:true});	// 显示进度条
					var ids = '';
					for(var i=0; i<rows.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += rows[i].id;
					};
					$.ajax({
						url: '<%=basePath %>assets/deviceDossier/repairMyUse.action?id='+ids,
						type:'post',
						success: function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',data.resMsg);
							if(data.resCode=='success'){
								$('#listMyUse').datagrid('reload');
							}
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					});
				}
           });
       }else{
    	   $.messager.alert('操作提示',"请选择要维修的设备信息！");
    	   setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
       }
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
							&nbsp;办公用途：<input class="easyui-textbox" name="office" id="office" data-options="prompt:'请输入办公用途'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备分类：<input class="easyui-textbox" name="name" id="name" data-options="prompt:'请输入公司名称'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;类别代码：<input class="easyui-textbox" name="classes" id="classes" data-options="prompt:'请输入设备分类'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备名称：<input class="easyui-textbox" name="device" id="device" data-options="prompt:'请输入设备名称'" onkeydown="KeyDown()" style="width:150px"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFromMyUse()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReloadMyUse()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
			<input type="hidden" value="${id}" id="id"></input>
			<table id="listMyUse" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/assets/deviceDossier/queryAssetsDeviceMyUse.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
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
						<th data-options="field:'deviceNo', width : 80">
							条码编号
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
			<a href="javascript:void(0)" onclick="repair()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">维修</a>
			<a href="javascript:void(0)" onclick="reloadMyUse()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>