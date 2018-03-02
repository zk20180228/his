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
<title>- 手机号黑名单管理 -</title>
	<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close a{
			background-color: red;	
		}
		#panelEast{
			border:0
		}
	</style>
</head>
<body >
	<div id="divLayout" class="easyui-layout" data-options="fit:true"style="width:100%;height:100%">
		 <div data-options="region:'north'" style="height: 45px;">
				<form id="search" method="post">
					<table style="width:100%;border-bottom:1px solid #95b8e7;padding:6px;" class="changeskinBottom">
						<tr>
							<td  >
								<input class="easyui-textbox" id="queryName" data-options="prompt:'输入手机号回车查询'" style="width: 150px;"/>
								&nbsp;类型：<input class="easyui-combobox" id="type"  />
								<a href="javascript:void(0)" onclick="searchFrom()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height: 89%;border-top:0">
				<table id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					 <thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'id',hidden:true" ></th>
							<th data-options="field:'mobileNum',width:250">手机号</th>
							<th data-options="field:'type',formatter:formatType,width:170">类型</th>
							<th data-options="field:'mobileRemark',width:350">备注</th>
							<th data-options="field:'createTime',width:170">创建时间</th>
						</tr>
					</thead> 
				</table>				
		</div>
	</div>
	<div id="temWins">
	</div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a href="javascript:void(0)" onclick="synCach()" class="easyui-linkbutton" data-options="iconCls:'reset',plain:true">同步至缓存</a>
	</div>
	 <div id="diaInpatient" class="easyui-dialog" title="同步至缓存前，请先进行类型选择" style="width:330;height:120;padding:1" data-options="modal:true, closed:true">
			<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
    			<table style="width:100%;border-bottom:1px solid #95b8e7;padding:6px;" class="changeskinBottom">
						<tr>
							<td>
								<input type="checkbox" name="mType" value="YWSP"   />业务审批
                				<input type="checkbox" name="mType" value="RCAP"  />日程安排
                				<input type="checkbox" name="mType" value="WZGL" />文章管理
							</td>
						</tr>
						<tr style="height: 20px;">
						</tr>
						<tr >
							<td colspan="3" align="center">
							<a href="javascript:void(0)" onclick="confirmType()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">确定</a>
							</td>
						</tr>
				</table>
    		</div>
	</div>
	
</body>
<script type="text/javascript">
	$(function(){
		//类型
		$('#type').combobox({
			valueField : 'id',	
			textField : 'value',
			editable : false,
			data : [{id : 1, value : '业务审批'},{id : 2, value : '日程安排'},{id : 3, value : '文章管理'}],
			onHidePanel:function(none){
				searchFrom()
			}
		});
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>mosys/cellPhoneBlackList/findCellPhoneBlack.action',
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				AddOrShowEast('查看',"<%=basePath %>mosys/cellPhoneBlackList/toViewCellPhoneBlack.action?id="+rowData.id);
			},
			onLoadSuccess:function(row, data){
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
				}}
		});	
		
		bindEnterEvent('queryName',searchFrom,'easyui');
	});
	
	/**
	 * 查询
	*/
	function searchFrom() {
		var queryName = $('#queryName').textbox('getValue');
		var type = $('#type').combobox('getValue');
		$('#list').datagrid('load',{mobileNum:queryName,type:type});
	}
	
	/**
	 * 重置
	*/
	function clearw(){
		$('#queryName').textbox('clear');
		$('#type').combobox('clear');
		searchFrom();
	}
	
	/**
	 * 重置
	 * @author zxl
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#queryName').textbox('setValue','');
		$('#type').combobox('setValue','');
		searchFrom();
		$('#divLayout').layout('remove','east');
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
	  					url:"<%=basePath%>mosys/cellPhoneBlackList/delCellPhoneBlack.action",
	  					async:false,
	  					cache:false,
	  					data:{'ids':ids},
	  					type:"POST",
	  					success:function(data){
	  						$.messager.alert("提示",data.resMsg);
	  						if(data.resCode=="0"){
	  							reload();
	  						}
	  						
	  					}
	  				});
	  		    }
			})
		}else{
			$.messager.alert('提示','请勾选要删除的信息！');	
			close_alert();
		}
	}
	
	
	//添加
		function add(){
		   AddOrShowEast('添加',"<%=basePath %>mosys/cellPhoneBlackList/cellPhoneBlackAdd.action");
	}
	//修改	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
               AddOrShowEast('编辑',"<%=basePath %>mosys/cellPhoneBlackList/toEditCellPhoneBlack.action?id=" + row.id);
   		  }else{
	   			$.messager.alert('提示','请点中要修改的信息！');	
				close_alert();
   		  }
	}

	/**
	 * 刷新
	*/
	function reload() {
		$('#divLayout').layout('remove','east');
		$('#list').datagrid('reload');
	}
	/**
	 * 动态添加标签页
	 * @author  zxl
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		var eastpanel = $('#panelEast'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
				href : url
			});
		} else {//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				title:title,
				split : true,
				maxHeight:820,
				href : url
				
			});
		}
	}
	
	/* 
	* 关闭界面
	*/
	function closeLayout(flag){
		$('#temWins').layout('remove','east');
		if(flag == 'edit'){
			$('#list').datagrid('reload');
		}
	}
	
	/**
	 * 格式化
	*/
	function formatType(value){
		if(value==1){
			return '业务审批';
		}else if(value==2){
			return '日程安排';
		}else if(value==3){
			return '文章管理';
		}else{
			return '';
		}
	}
	
	
	//同步至缓存
	function synCach(){
		$("#diaInpatient").window('open');
		
	}
	
	function confirmType(){
		 var checks = document.getElementsByName("mType");
		 var ids = ''; 
		 $('input[name="mType"]:checked').each(function(){ 
			 if(ids!=''){
					ids += ',';
				}
				ids += $(this).val();
		 }); 
		if(ids!=null&&ids!=''){
			$('#diaInpatient').window('close');
		 	$.messager.confirm('确认', '同步至缓存中吗？', function(res){//提示是否删除
				if (res){
					$.ajax({
						url: "<c:url value='/mosys/cellPhoneBlackList/synCach.action'/>?id="+ids,
						type:'post',
						success: function(data) {
							$.messager.alert('提示',data.resMsg);
							$('#list').datagrid('reload');
						}
					});										
				}
	        });
		}else{
			 $.messager.alert('提示信息','请选择要同步的类型！');
   	    	 setTimeout(function(){
   					$(".messager-body").window('close');
   			 },3500);
		}
	}
</script>
</html>
