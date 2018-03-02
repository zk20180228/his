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
<title>广播查询</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//加载页面
		$(function(){
			//类型
			$('#queryName').combobox({
				valueField : 'id',	
				textField : 'value',
				editable : false,
				data : [{id : 0, value : '全部'},{id : 1, value : '未发送'},{id : 2, value : '已发送'}],
				onHidePanel:function(none){
				  	var val = $(this).combobox('getValue');
				  	$('#list').datagrid('load',{queryName:val});
				},
			});
			$('#list').datagrid({
				nowrap:false,    
 	            striped:true,    
 	            url:'<%=basePath%>mosys/ofBatchPush/findOfBatchPushList.action',   
 	            idField:'id',
 	            pagination:true,
				fitColumns:true,
				toolbar:'#toolbarId',
				fit:true,
				checkOnSelect:true,
				selectOnCheck:true,
				singleSelect:false,
		   		pageSize:20,
		   		pageList:[20,30,50,100],
		   		onLoadSuccess: function(data){
		   			$('#list').datagrid('clearSelections');
		   			$('#list').datagrid('clearChecked');
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
					var rows = data.rows;
					for(var i=0;i<rows.length;i++){
						var index = $(this).datagrid('getRowIndex',rows[i]);
						$(this).datagrid('updateRow',{index:index,row:{MSG_DETAIL_show:'<a class="cls" onclick="look('+index+')" href="javascript:void(0)" style="height:20"></a>'}});
					}
					$('.cls').linkbutton({text:'查看',plain:true,iconCls:'icon-application_form_magnify'}); 
		   		}
				});
				
			
		});
		
		//回车查询
		$(window).keydown(function(event) {
		      if(event.keyCode == 13) {
		    	  searchFrom();
		      }
		});	
	//实现刷新栏目中的数据
	function reload(){
		 $("#list").datagrid("reload");
	}
	//模糊查询
	function searchFrom() {
		var queryName = $('#queryName').combobox('getValue');
		$('#list').datagrid('clearSelections');
		$('#list').datagrid('clearChecked');
		$('#list').datagrid('load',{queryName:queryName});
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function searchReload(){
		$('#queryName').combobox('setValue','');
		searchFrom();
	}
	//开始处理
	function del(){
		  //选中要删除的行
        var rows = $('#list').datagrid('getChecked');
        if (rows.length > 0) {//选中几行的话触发事件	                        
		 	 $.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否处理
				if (res){
					var ids = '';
					for(var i = 0; i < rows.length; i++){
						if(ids != ''){
							ids += ','+rows[i].id;
						}else{
							ids += rows[i].id;
						}
					}
					$.ajax({
						url:"<%=basePath%>mosys/ofBatchPush/delOfBatchPush.action?ids="+ids,
						type:'post',
						success: function(data) {
							$.messager.alert("提示",data.resMsg);
							if(data.resCode=='0'){
								$('#list').datagrid('reload');
							}
							
						}
					});										
				  }
            });
         }
	}
	
	//格式化性别
	function formatData(value){
		if(!value){
			return '';
		}
		if(value=='1'){
			return '未发送';
		}else{
			return '已发送';
		}
	}
	//查看异常详细信息	
	function look(index){
		var rows = $('#list').datagrid('getRows');
		$('#msgWinId').dialog({    
		    title: '推送内容信息',    
		    width: 800,    
		    height: 400,    
		    closed: false,    
		    cache: false,    
		    modal: true   
		}); 
		$('#showDetail').val(rows[index].body);
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
</head>
<body  >
	<div id="divLayout" class="easyui-layout" data-options="fit:true"style="width:100%;height:100%">
		 <div data-options="region:'north'" style="height: 45px;">
			<table style="width:100%;border-bottom:1px solid #95b8e7;padding:6px;" class="changeskinBottom">
				<tr>
					<td  style="width: 300px;">
					广播状态：
					<input class="easyui-combobox" id="queryName"  name="queryName" style="width: 120px;" value="0" />
	   				<a href="javascript:void(0)" onclick="searchFrom()" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查询</a> 
	   				<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a> 
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" style="height: 95%;border-top:0">
			<table id="list" fit="true"  class="easyui-datagrid">
				<thead>
					<tr>
						<th field="getIdUtil" checkbox="true" ></th>
						<th data-options="field:'id',hidden:true" >id</th>
						<th data-options="field:'MSG_DETAIL_show'" style="width: 10%">推送内容</th>
						<th data-options="field:'createTime'" style="width: 10%">创建时间</th>
						<th data-options="field:'status',formatter:formatData" style="width: 10%">广播状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain=true>删除</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="msgWinId">
			<div class="easyui-layout" data-options="fit:true">  
				<textarea id='showDetail' style="width:100% ;height:100%;"></textarea>
			</div>
		</div> 
</body>
</html>