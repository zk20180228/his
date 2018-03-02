<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>业务变更</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px;">
	<div class="easyui-layout" style="width: 100%; height: 100%;" data-options="fit:true">
		<div data-options="region:'north'" style="width: 100%; height: 35px;border-top:0">
			<form id="orderForm" style="padding: 3px">
			<a href="javascript:void(0)" onclick="queryData()" class="easyui-linkbutton" iconCls="icon-search">查看变更数据</a>&nbsp;&nbsp;
<!-- 			<a href="javascript:void(0)" onclick="closeDown()" class="easyui-linkbutton" iconCls="icon-cancel">退出</a> -->
			</form>
		</div>
		<div data-options="region:'west',title:'数据表信息',iconCls:'icon-book',split:true,tools:'#toolSMId'" style="width: 20%; height: 95%;">
			<form id="tree1"></form>
		</div>
		<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
		</div>
		<div  data-options="region:'center',title:'字段信息',iconCls:'icon-book',split:true" style="width: 80%; height: 93%;">
			<div id="div1" style="display: none;height:100%;width:100%" data-options="border:true"><table id="grid1" data-options="fit:true,border:false"></table></div>
			<div id="div2" style="display: none;height:100%;width:100%" data-options="border:true"><table id="grid2" data-options="fit:true,border:false"></table></div>
		</div>
	</div>
	<div id="tb">
		<a  onclick="saveData()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">提交</a>
		<a  onclick="uncheck()" class="easyui-linkbutton" data-options="iconCls:'icon-arrow_turn_left',plain:true">反选</a>
		
	</div>
</body>
<script type="text/javascript">
var tabName='';
$(function(){
	$('#tree1').tree({
		url:"<%=basePath%>sys/businessChange/queryTree.action",
		method : 'get',
		animate : true,
		lines : true,
		formatter : function(node) {//统计节点总数
			var s = node.text;
			if (node.children) {
				s += '&nbsp;<span style=\'color:blue\'>('
						+ node.children.length + ')</span>';
			}
			return s;
		},onClick:function(node){
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			var children= node.children;
			if(children==''){//叶子节点
			var id= node.id;
			tabName=id;
			var ul='<%=basePath%>sys/businessChange/queryColumns.action?tableName='+id;
			$('#div1').hide();
			$('#div2').show();
			$('#grid2').datagrid('load', ul);
			}
		}
	});
	
	$('#grid1').datagrid({
		url:'<%=basePath%>sys/businessChange/queryValues.action?tableName='+tabName,
		columns:[[
		          {field:'changeNo',title:'变更序号',hidden:true},
		          {field:'tableName',title:'表名称',width:'10%'},
		          {field:'tableId',title:'表中记录的id',width:'15%'},
		          {field:'columns',title:'字段名',width:'15%'},
		          {field:'oldValue',title:'原来的值',width:'15%'},
		          {field:'newValue',title:'新的值',width:'15%'},
		          {field:'updateUser',title:'修改人员',width:'15%'},
		          {field:'updateTime',title:'修改时间',width:'15%'},
		          ]]
	});
	$('#grid2').datagrid({
		columns:[[
		          {field:'-',title:'全选',width:'2%',checkbox:true},
		          {field:'columnName',title:'字段名',width:'19%'},
		          {field:'colComments',title:'字段注释',width:'75%'}
		          ]],
		toolbar:'#tb',
		onLoadSuccess:function(data){
			$.ajax({
				url:'<%=basePath%>sys/businessChange/queryTabColumns.action?tableName='+tabName,
				type:'post',
				dataType:'json',
				success:function(value){
					var rows=$('#grid2').datagrid('getRows');
					var p=[];
					for(var i=0;i<rows.length;i++){
						p.push(rows[i].columnName);
					}
					if(value.length>0){
						for (var i = 0; i < value.length; i++) {
							var n=value[i].columnName;
							var index=p.indexOf(n);
							$('#grid2').datagrid('checkRow',index);
						}
					}
				}
			});
		}
	});
});

function queryData(){
	$('#div2').hide();
	$('#div1').show();
	var ul='<%=basePath%>sys/businessChange/queryValues.action?tableName='+tabName;
	$('#grid1').datagrid('load', ul);
}
/**
 * 反选
 */
function uncheck(){
	var s_rows = $.map($('#grid2').datagrid('getSelections'),  
            function(n) {  
                return $('#grid2').datagrid('getRowIndex', n);  
            });  
    $('#grid2').datagrid('selectAll');  
    $.each(s_rows, function(i, n) {  
        $('#grid2').datagrid('unselectRow', n);  
    });  
}

/**
 * 提交数据
 */
function saveData(){
	//提取表单数据
	var formdata= getFormData('orderForm');	
	//提取表格数据
	var rows= $('#grid2').datagrid('getChecked');
	if(rows.length>0){
		$.messager.confirm('提示','确定要提交数据吗?',function(res){
			if(res){
				//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
				formdata['json']=JSON.stringify(rows);	
				$.ajax({
					url:'<%=basePath%>sys/businessChange/changeCol.action?tableName='+tabName,
					data:formdata,
					type:'post',
					dataType:'json',
					success:function(value){
						if(value){
							$.messager.alert('提示','提交成功!');
							$('#grid2').datagrid('unselectAll');
						}else{
							$.messager.alert('提示','提交失败!');
						}
					}
				});
			}
		});
	}else{
		$.messager.alert("提示", "请选择一条记录！", "warning");
		close_alert();
	}
	
}

/**
 * 退出
 */
function closeDown(){
	window.parent.$('#tabs').tabs('close',"业务变更");
}

function conveterParamsToJson(paramsAndValues) {
	var jsonObj = {};

	var param = paramsAndValues.split("&");
	for (var i = 0; param != null && i < param.length; i++) {
		var para = param[i].split("=");
		jsonObj[para[0]] = para[1];
	}

	return jsonObj;
}

/**
 * 将表单数据封装为json
 * @param form
 * @returns
 */
function getFormData(form) {
	var formValues = $("#" + form).serialize();

	//关于jquery的serialize方法转换空格为+号的解决方法  
	formValues = formValues.replace(/\+/g, " "); // g表示对整个字符串中符合条件的都进行替换  
	var temp = decodeURIComponent(JSON
			.stringify(conveterParamsToJson(formValues)));
	var queryParam = JSON.parse(temp);
	return queryParam;
}


//缓存树操作
function refresh(){//刷新树
	$('#tree1').tree('options').url = "<%=basePath%>sys/businessChange/queryTree.action";
 	$('#tree1').tree('reload'); 
}
function expandAll(){//展开树
	$('#tree1').tree('expandAll');
}
function collapseAll(){//关闭树
	$('#tree1').tree('collapseAll');
	$('#tree1').tree('expand',$('#tree1').tree('find', 0).target);
}

</script>
</html>