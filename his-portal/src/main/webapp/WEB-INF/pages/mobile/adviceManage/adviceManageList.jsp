<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
textarea {
resize: none;
disabled: disabled;
border:0;
height:19px; 
font-size:14px;
}
</style>
<title>意见箱管理员维护</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	$(function(){
		$('#list').datagrid({
			pagination:true,
			fitColumns:false,
			url:'<%=basePath%>mosys/adviceManage/findAdviceManageList.action',
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
		bindEnterEvent('queryName',searchFrom,'easyui');
	});
  		

	//查询
	function searchFrom(){
		$('#list').datagrid('clearChecked');
	    $('#list').datagrid('load', {
	    	queryName: $('#queryName').val(),
		});
	}	
	 
 	/**
	 * 重置
	 * @author zxl
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#queryName').textbox('setValue','');
		searchFrom();
		$("#addWindow").window('close');
	}
	
	/**
	 * 刷新
	*/
	function reload() {
		$('#list').datagrid('clearChecked');
		$('#list').datagrid('reload');
		$("#addWindow").window('close');
	}
	
	/**
	 * 删除
	*/
	function del() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			$.messager.confirm('提示','您确认要删除吗？',function(r){    
	  		    if (r){ 
					var ids = '';
					for(var i = 0; i < rows.length; i++){
						if(ids != ''){
							ids += ','+rows[i].id;
						}else{
							ids += rows[i].id;
						}
					}
					$.ajax({
						url:"<%=basePath%>mosys/adviceManage/delAdviceManage.action",
						async:false,
						cache:false,
						data:{'ids':ids},
						type:"POST",
						success:function(data){
							$.messager.alert("提示",data.resMsg);
							if(data.resCode==0){
								reload();
								setTimeout(function(){
									$(".messager-body").window('close');
								},2500);
							}
							
						}
					});
	  		    }
			})
		}else{
			$.messager.alert('提示','请勾选要删除的信息！');	
			setTimeout(function(){
				$(".messager-body").window('close');
			},2500);
		}
	}
	
	function add(){
		AdddilogModel("addWindow","用户选择","<%=basePath%>mosys/adviceManage/userManageList.action?",'750px','410px');
	}
	
	//加载标题模式窗口
	function AdddilogModel(id,title,url,width,height) {
		$('#'+id).dialog({    
		    title: title,
		    width: width,
		    height: height,
		    closed: false,
		    cache: false,
		    href: url,
		    modal: true
		});    
	}
	
</script>

</head>
	<body>
		<div id="divLayout" style="height:100%;width:100%;float:left;">
	        <div data-options="region:'north',split:false,border:false" style="height: 33px;margin-left: 5px;margin-top: 5px;">	        
				<table style="width:100%;border:false;,collapsible:false">
					<tr>
						<td  >
						<input class="easyui-textbox" id="queryName" data-options="prompt:'用户账户、姓名回车查询'" style="width: 150px;"/>
						<a href="javascript:void(0)" onclick="searchFrom()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div  id='pageShow' style="width:100%;height:95%">
				<table id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'id',hidden:true" ></th>
							<th data-options="field:'userAccount',width:170">用户账户</th>
							<th data-options="field:'empName',width:170">用户姓名</th>
							<th data-options="field:'createTime',width:170">创建时间</th>
							</tr>
					</thead>
				</table>				
			</div>	
		</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		 <div id = "addWindow"></div>
	</body>
</html>