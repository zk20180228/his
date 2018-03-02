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
<title>医嘱类型与系统类别关系维护</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
    $(function(){//加载列表
		//加缓冲时间1毫秒
		setTimeout(function(){
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination:false,
		   		url:'<%=basePath %>baseinfo/advice/queryInpatientKind.action',
				onLoadSuccess: function (data) {//默认选中
					$('#list').datagrid("autoMergeCells", ['tname']);
		        },onDblClickRow: function (rowIndex, rowData) {//双击查看
						AddOrShowEast('EditForm','<%=basePath%>baseinfo/advice/viewAdviceInfo.action','post'
				 				   ,{"tname":rowData.tname,"cname":rowData.cname,"typeId":rowData.tcode});
					
				},onClickRow:function(rowIndex, rowData){
					$('#list').datagrid('unselectAll');
					var tname = rowData.tname;
					var rows = $('#list').datagrid('getRows');
					if(rows!=null&&rows.length>0){
						for(var i=0;i<rows.length;i++){
							if(rows[i].tname == tname){
								var index = $('#list').datagrid('getRowIndex',rows[i])
								$('#list').datagrid('selectRow',index);
							}
						}
					}
				}
			});
        },1);
		
	bindEnterEvent('queryName',searchFrom,'easyui');
	
	
});


	//添加
	function add(){
		AddOrShowEast('EditForm','<%=basePath%>baseinfo/advice/addAdviceInfo.action','post');
	}
	//修改
	function edit(){
		var row = $('#list').datagrid('getSelected'); //获取当前选中行   
		if(row == null){
			$.messager.alert("操作提示","请选择要修改的信息！");
			setTimeout(function(){$(".messager-body").window('close')},3500);
		}
        if(row){
           	AddOrShowEast('EditForm','<%=basePath%>baseinfo/advice/editAdviceInfo.action','post'
 				   ,{"typeId":row.tcode});
   		}
	}
	//跳转
	function AddOrShowEast(title, url,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('remove','east');
			$('#divLayout').layout('add', {
				region : 'east',
				width :580,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true,
				border : false
			});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true,
				border : false
			});
		}
	}
	function del(){
	//选中要删除的行
		var rows = $('#list').datagrid('getChecked');
	   	if (rows.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var id = '';
					for(var i=0; i<rows.length; i++){
						if(id!=''){
							id += ',';
						}
						id += rows[i].id;
					};
					$.ajax({
						url: '<%=basePath %>baseinfo/advice/delAdviceInfo.action?id='+id,
						success: function() {
							$.messager.alert("操作提示","删除成功");
							$('#list').datagrid('reload');
						}
					});
				}
	         });
	 	}else{
			$.messager.alert("操作提示","请选择要删除的信息！");
			setTimeout(function(){$(".messager-body").window('close')},3500);
		}
	}

	function reload(){
		//实现刷新栏目中的数据
 		$('#list').datagrid('reload');
	}

	//条件查询
	function searchFrom(){
		var queryName =  $.trim($('#queryName').textbox('getValue'));
	    $('#list').datagrid('load', {
	    	queryName: queryName,
		});
	}
    
    /**
	 * 回车键查询
	 * @author  liujl
	 * @param flg 标识：0=查询；1=编辑
	 * @date 2015-05-27
	 * @version 1.0
	 */
	function KeyDown(flg){   
    	if(flg==0){
		    if (event.keyCode == 13)  
		    { 
		        event.returnValue=false;  
		        event.cancel = true;  
		        searchFrom();  
		    }  
	    }
	}
    
    /*
    **id  医嘱类型
    *关闭编辑页面后再次重新打开    编辑页面医嘱类型变更后调用 
    */
    function closeAndOpenEdit(typeCode){
    	AddOrShowEast('EditForm','<%=basePath%>baseinfo/advice/editAdviceInfo.action','post'
				   ,{"typeId":typeCode});
    }
    
	$.extend($.fn.datagrid.methods, {
	    autoMergeCells: function (jq, fields) {
	        return jq.each(function () {
	            var target = $(this);
	            if (!fields) {
	                fields = target.datagrid("getColumnFields");
	            }
	            var rows = target.datagrid("getRows");
	            var i = 0,
	            j = 0,
	            temp = {};
	            for (i; i < rows.length; i++) {
	                var row = rows[i];
	                j = 0;
	                for (j; j < fields.length; j++) {
	                    var field = fields[j];
	                    var tf = temp[field];
	                    if (!tf) {
	                        tf = temp[field] = {};
	                        tf[row[field]] = [i];
	                    } else {
	                        var tfv = tf[row[field]];
	                        if (tfv) {
	                            tfv.push(i);
	                        } else {
	                            tfv = tf[row[field]] = [i];
	                        }
	                    }
	                }
	            }
	            $.each(temp, function (field, colunm) {
	                $.each(colunm, function () {
	                    var group = this;
	                    if (group.length > 1) {
	                        var before,
	                        after,
	                        megerIndex = group[0];
	                        for (var i = 0; i < group.length; i++) {
	                            before = group[i];
	                            after = group[i + 1];
	                            if (after && (after - before) == 1) {
	                                continue;
	                            }
	                            var rowspan = before - megerIndex + 1;
	                            if (rowspan > 1) {
	                                target.datagrid('mergeCells', {
	                                    index: megerIndex,
	                                    field: field,
	                                    rowspan: rowspan
	                                });
	                            }
	                            if (after && (after - before) != 1) {
	                                megerIndex = after;
	                            }
	                        }
	                    }
	                });
	            });
	        });
	    }
	});
	
	// 列表查询重置
	function searchReload() {
		$('#queryName').textbox('setValue','');
		searchFrom();
	}
</script>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:false,border:false" style="height:34px;">
			<table style="width:100%;">
				<tr>
					<td style="padding: 4px 0px 0px 5px">
						<input id="queryName" class="easyui-textbox" onkeydown="KeyDown(0)" data-options="prompt:'关键字'" />
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false,border:false" style="width: 100%;height: 100%;">
			<table id="list" style="" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:false,fitColumns:false,fit:true,toolbar:'#toolbarId',">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true,width:'5%'" ></th>
						<th data-options="field:'tname'" width="15%">医嘱类型</th>
						<th data-options="field:'cname'" width="15%">系统类别</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
 	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add"> 
 			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a> 
		</shiro:hasPermission> 
		<shiro:hasPermission name="${menuAlias}:function:edit"> 
 			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a> 
 		</shiro:hasPermission> 
 		<shiro:hasPermission name="${menuAlias}:function:delete">	
 			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a> 
 		</shiro:hasPermission> 
 		<a href="javascript:void(0)" onclick="reload()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
		
</body>
</html>