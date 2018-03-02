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
<style type="text/css">
.layout-split-east {
    border-left: 0px;
}
.panel-body-noheader{
	border-left: 0px;
}
.layout-split-east .panel-header{
	border-top:0;
	border-left:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;" data-options="fit:true">
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
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false" style="width: 100%; height: 30px; border-top:0">
					<table id="list" style="width:100%;"class="easyui-datagrid" data-options="idField:'id',treeField:'name',striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true,height:40"></th>
								<th data-options="field:'id',hidden:true">id</th>
								<th data-options="field:'officeName',width:'7%'">办公用途</th>
								<th data-options="field:'classCode',width:'7%'">类别代码</th>
								<th data-options="field:'className',width:'7%'">设备分类</th>
								<th data-options="field:'deviceName',width:'7%'">设备名称</th>
								<th data-options="field:'meterUnit',width:'7%'">计量单位</th>
								<th data-options="field:'purchPrice',width:'7%'">采购单价(元)</th>
								<th data-options="field:'purchNum',width:'7%'">采购数量</th>
								<th data-options="field:'freight',width:'7%'">运费(元)</th>
								<th data-options="field:'installationFee',width:'7%'">安装费(元)</th>
								<th data-options="field:'purchTotal',width:'7%'">采购总价(元)</th>
								<th data-options="field:'purchName',width:'7%'">采购员</th>
								<th data-options="field:'suppName',width:'7%'">供应商</th>
								<th data-options="field:'attach',width:'7%'">合同路径</th>
								<th data-options="field:'state',width:'7%',formatter:formatterState">状态</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	<div id="menuWin"></div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias }:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true">上传合同</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:edit">
		<a href="javascript:void(0)" onclick="checkBase();" class="easyui-linkbutton" iconCls="icon-see">预览合同</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<a id="down" href=""></a>
	<script type="text/javascript">
	var fileServerURL ="${fileServerURL}";
	console.info(fileServerURL);
	var menuAlias="${menuAlias}";
	var stateMap={'0':'未上传','1':'已上传','2':'全部'};
	$(function(){
		searchFrom();
		//回车查询
        bindEnterEvent('office',searchFrom,'easyui');//查询条件
        bindEnterEvent('name',searchFrom,'easyui');//查询条件
        bindEnterEvent('classes',searchFrom,'easyui');//查询条件
        bindEnterEvent('device',searchFrom,'easyui');//查询条件
	});
	//条件查询
	function searchFrom(){
		$('#list').datagrid({
			rownumbers: true,
			pageSize: "20",
			fit:true,
			singleSelect:true,
			checkOnSelect:false,
			selectOnCheck:false,
			pageList: [10, 20, 30, 50, 80, 100],
			pagination: true,
			method: "post",
			url: '<%=basePath%>/assets/deviceContract/queryDeviceContract.action',
			queryParams:{menuAlias:menuAlias,
				officeName:$('#office').textbox('getValue'),
				className:$('#name').textbox('getValue'),
				classCode:$('#classes').textbox('getValue'),
				deviceName:$('#device').textbox('getValue'),
				},
			onLoadSuccess:function(data){    
	        	 $("a[name='opera']").linkbutton({});
	        	 $('#list').datagrid('unselectAll');
	           	 $('#list').datagrid('uncheckAll');
			}
		});
	}
	 //重置
	function searchReload(){
		$('#office').textbox('setValue','');
		$('#name').textbox('setValue','');
		$('#classes').textbox('setValue','');
		$('#device').textbox('setValue','');
		searchFrom();
	}
	
	//渲染状态   1:已上传,0:未上传,2:全部 
	function formatterState(value,row,index){
		if(value=='1'){
			return "已上传";
		}else{
			return "未上传";
		}
	}
	
	//刷新列表
	function reload(){
		$('#list').datagrid('reload');
	}
	
	//跳转到上传合同页面
	function add(){
		var rows = $('#list').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			addAndEdit = 1;
			closeDialog();
			AddOrShowEast('EditForm',"<%=basePath %>assets/deviceContract/addDeviceContract.action?id=" + getIdUtil('#list'),"30%");
		}else{
			$.messager.alert('操作提示',"请选择要上传文件的信息！");
			close_alert();
		}
	}
	//查看合同信息文件
	function checkBase(){
		var row = $('#list').datagrid('getSelected');
		if(row!=null){
			var filepath=fileServerURL+row.attach;//文件路径
			var a = document.getElementById("down");  
            a.href=filepath;  
            a.download="哈哈";
            a.click();
		}else{
			$.messager.alert('提示',"请选择要查看的文件！");
		}
	}
	
	
	//动态添加LayOut
	function AddOrShowEast(title, url,width,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			$('#divLayout').layout('remove','east');
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}
	}
    //加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#menuWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
    
	//打开模式窗口
	function openDialog() {
		$('#roleWin').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#roleWin').dialog('close');  
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>