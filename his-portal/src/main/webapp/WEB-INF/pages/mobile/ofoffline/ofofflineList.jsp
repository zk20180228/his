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
<title>异常查询</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//加载页面
		$(function(){
			$('#queryName').combogrid({
				idField:'account',
				textField:'name',
				mode:'remote',
				panelWidth:600,
				panelHeight:560,
				striped:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,
				pageNumber:1,
				pageSize:20,
				pageList:[20,30,50,100],
				url:'<%=basePath %>sys/userMenuFunJuris/queryFunJurisUserList.action',
				columns:[[
					{field:'account',title:'账户',width:80},
					{field:'name',title:'姓名',width:90},
					{field:'nickName',title:'曾用名',width:100},
					{field:'phone',title:'手机',width:100},
					{field:'email',title:'电子邮箱',width:120},
					{field:'remark',title:'备注',width:100}
				]],
				onSelect:function(index,row){
					$('#list').datagrid('load',{queryName:row.account});
				} 
			});
			$('#queryName').combogrid('textbox').bind('focus',function(){
				$('#queryName').combogrid('showPanel');
			});
			$('#list').datagrid({
				nowrap:false,    
 	            striped:true,    
 	            url:'<%=basePath%>mosys/ofoffline/findOfofflineList.action',   
 	            idField:'messageId',
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
		var queryName = $('#queryName').textbox('getValue');
		$('#list').datagrid('clearSelections');
		$('#list').datagrid('clearChecked');
		$('#list').datagrid({
	   		nowrap:false,    
	        url:'<%=basePath%>mosys/ofoffline/findOfofflineList.action',   
			queryParams:{"queryName":queryName},
	        idField:'messageId',
	        pagination:true,
			fitColumns:true,
			striped:true,
			checkOnSelect:true,
			selectOnCheck:true,
			singleSelect:false,
			fit:true,
	   		pageSize:20,
	   		pageList:[20,30,50,100],
	   		onLoadSuccess: function(data){
				var rows = data.rows;
				for(var i=0;i<rows.length;i++){
					var index = $(this).datagrid('getRowIndex',rows[i]);
					$(this).datagrid('updateRow',{index:index,row:{MSG_DETAIL_show:'<a class="cls" onclick="look('+index+')" href="javascript:void(0)" style="height:20"></a>'}});
				}
				$('.cls').linkbutton({text:'查看',plain:true,iconCls:'icon-application_form_magnify'}); 
	   		}
		});
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function searchReload(){
		$('#queryName').textbox('setValue','');
		searchFrom();
	}
	//查看异常详细信息	
	function look(index){
		var rows = $('#list').datagrid('getRows');
		$('#msgWinId').dialog({    
		    title: '消息内容详细信息',    
		    width: 800,    
		    height: 400,    
		    closed: false,    
		    cache: false,    
		    modal: true   
		}); 
		$('#showDetail').val(rows[index].stanza);
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
							ids += ','+rows[i].messageId;
						}else{
							ids += rows[i].messageId;
						}
					}
					$.ajax({
						url:"<%=basePath%>mosys/ofoffline/delOfoffline.action?ids="+ids,
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
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
</head>
<body  >
	<div id="divLayout" class="easyui-layout" data-options="fit:true"style="width:100%;height:100%">
		 <div data-options="region:'north'" style="height: 45px;">
			<table style="width:100%;border-bottom:1px solid #95b8e7;padding:6px;" class="changeskinBottom">
				<tr>
					<td  style="width: 300px;">
					用户账户：
					<input  id="queryName" name="queryName" style="width: 120px;"/> 
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
						<th data-options="field:'userName'" style="width: 10%">用户账户</th>
						<th data-options="field:'uname'" style="width: 10%">用户姓名</th>
						<th data-options="field:'messageId'" style="width: 12%">消息ID</th>
						<th data-options="field:'creationDate'" style="width: 12%">创建日期</th>
						<th data-options="field:'messageSize'" style="width: 7%">消息长度</th>
						<th data-options="field:'MSG_DETAIL_show'" style="width: 12%">消息内容</th>
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