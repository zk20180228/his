<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>- 文档管理页面 -</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9,EmulateIE8,6"/>
<script src="<%=basePath%>/easyui/jquery-1.7.2.js" type="text/javascript"></script>
<style type="text/css">
	.window .panel-header .panel-tool a{
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
							<td  style="width: 300px;">
								<input class="easyui-textbox" data-options="prompt:'输入文档名称、上传人员'" id="queryName"/>
								&nbsp;所属范围：<input class="easyui-combobox" style="width:140px"  id="dType" 
    								data-options="panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '个人',text: '个人'},{value: '科室',text: '科室'}]" />
								&nbsp;上传科室：<input class="easyui-combobox" id="deptCod"  style="width:140px"/>
		    					<input type="hidden" id="deptName"  value="0"/>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clearw()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height: 89%;border-top:0">
			<table id="list" fit="true" data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'id',checkbox:true,hidden:true" ></th>
						<th data-options="field:'docName',align:'center',width:'15%',formatter:funHospital">文档名称</th>
						<th data-options="field:'docDes',align:'center',width:'20%'">文档简述</th>
						<th data-options="field:'deptType',align:'center',width:'10%'">上传类型</th>
						<th data-options="field:'createUser',align:'center',width:'13%'">上传人员</th>
						<th data-options="field:'uploadDept',align:'center',width:'15%'">上传科室 </th>
						<th data-options="field:'createDate',align:'center',width:'24%'">上传时间</th>
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
	</div>
</body>
<script type="text/javascript">
	$(function(){
		//类型
		$('#queryFlg').combobox({
			valueField : 'id',	
			textField : 'value',
			editable : false,
			data : [{id : 7, value : '全部'},{id : 1, value : '否'},{id : 2, value : '是'}]
		});
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>/oa/userPortal/getlist.action',
			/* queryParams:{"queryName":""}, */
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				AddOrShowEast('查看',"<%=basePath %>/oa/userPortal/documentView.action?id="+rowData.id);
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
		
		$('#deptCod').combobox({
			url:'<%=basePath %>/baseinfo/department/departmentCombobox.action',
			valueField:'deptCode',    
		    textField:'deptName',
		    onSelect: function(rec){
		    	console.log(rec.deptName);
		    	$('#deptName').val(rec.deptName);
		    },filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'deptCode';
				keys[keys.length] = 'deptName';
				keys[keys.length] = 'deptPinyin';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'deptInputcode';
				return filterLocalCombobox(q, row, keys);
			},onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].deptCode) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox('clear');
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
			}

		});
		
		bindEnterEvent('queryName',searchFrom,'easyui');
		bindEnterEvent('dType',searchFrom,'easyui');
		bindEnterEvent('deptCod',searchFrom,'easyui');
	});
	
	//本地下拉查询方法
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}
	
	/**
	 * 查询
	*/
	function searchFrom() {
		var queryName = $('#queryName').textbox('getValue');
		var dType = $('#dType').combo('getValue');
		var deptCode = $('#deptCod').combo('getValue');
		$('#list').datagrid('load',{queryName:queryName,dType:dType,deptCode:deptCode});
	}
	
	/**
	 * 重置
	*/
	function clearw(){
		$('#queryName').textbox('clear');
		$('#dType').combobox('clear');
		$('#deptCod').combobox('clear');
		searchFrom();
	}
	
	/**
	 * 删除
	*/
	function del() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
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
						url:"<%=basePath%>/oa/userPortal/deleteDocument.action",
						async:false,
						cache:false,
						data:{'ids':ids},
						type:"POST",
						success:function(data){
							$.messager.alert("提示","删除成功!");
							reload();
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选择要删除的信息！');	
			close_alert();
		}
	}
	
	
	//添加
		function add(){
		   AddOrShowEast('添加',"<%=basePath %>/oa/userPortal/toAddDocument.action");
	}
	//修改	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
               AddOrShowEast('编辑',"<%=basePath %>/oa/userPortal/getListDocument.action?id=" + row.id);
   		  }else{
	   			$.messager.alert('提示','请选择要修改的信息！');	
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
	
	function funHospital(value,row,index){
		 var test = new Array();
		 var name=null;
		 if(row.docDownAddr!=null){
			 var str=row.docDownAddr;
			 test = str.split(".");
			 name=row.docName+"."+test[4];
		 }
		return "<a href ="+row.docDownAddr+" download="+name+">"+ value + "</a>"; 
	}
	
</script>
</html>
