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
<script type="text/javascript">
//定义一个map
var map;
function toUp(rowId,index){//根据索引获取上一行ID
	editOrder(rowId,map.get(index-1));
}
function toDown(rowId,index){//根据索引获取下一行ID
	editOrder(rowId,map.get(index+1));
}
function editOrder(currentId,otherId){//发请求到后台 把 值传到后台
	$.post("<c:url value='/outpatient/registerDeptManage/editOrders.action'/>",{"currentId":currentId,"otherId":otherId},function(result){
		if(result>0){
			reload();
		}else{
			$.messager.alert('提示',"error");
		}
	});
}
//加载页面
$(function(){
	var id='${id}'; //存储数据ID
	//添加datagrid事件及分页
	$('#list').datagrid({
		pagination:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		onLoadSuccess: function(data){
	        	var grid = $('#list');  
				var options = grid.datagrid('getPager').data("pagination").options;  
				var curr = options.pageNumber;  
				var total = options.total; 
				var pageSize = options.pageSize; 
				var rows = data.rows;
				map=null;
				map=new Map();
	            var rowData = data.rows;
	            $.each(rowData, function (index, value) {
	            	map.put(index,value.id);
	            	if(value.id == id){
	            		$('#list').datagrid('checkRow', index);
	            	}
	            });
				for(var i=0;i<rows.length;i++){
					var index = $('#list').datagrid('getRowIndex',rows[i]);
					var a = "";
					if(curr==1&&index==0){//第一行
						a='<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toDown(\''+rows[i].id+'\','+index+')">下移</a>';
					}else if((index+1)+((curr-1)*pageSize)==total){//最后一行
						a='<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toUp(\''+rows[i].id+'\','+index+')">上移</a>';
					}else{
						a= '<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toUp(\''+rows[i].id+'\','+index+')">上移</a>';
						a+='&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toDown(\''+rows[i].id+'\','+index+')">下移</a>';
					}
					$('#list').datagrid('updateRow',{
						index: index,
						row: {
							mfOrderOpr : a
						}
					});
				}
        },  
        onDblClickRow: function (rowIndex, rowData) {//双击查看
			if(getIdUtil('#list').length!=0){
		   	    AddOrShowEast('EditForm',"<c:url value='/outpatient/registerDeptManage/viewSysDepartment.action'/>?id="+getIdUtil('#list'));
		   	}
		}    
		});
		bindEnterEvent('deptName',searchFrom);
});
//刷新
function reload(){
	//实现刷新栏目中的数据
	$('#list').datagrid('reload');
}
//获得选中id	
function getId(parameter){
	var row = $("#list").datagrid("getSelections");  
	var i = 0;   
	if(parameter=='single'){//获得单个id
	    if(row.length<1){
	    	$.messager.alert('提示',"请选择一条记录！");
	       	return null;
	    }else if(row.length>1){
	    	$.messager.alert('提示',"只能选择一条记录！");
	    	return null;
	    }else{ 
	    	var id = ""; 
		  	for(i;i<row.length;i++){    
		  		id += row[i].id; 
		      	return id;
			}
	  	} 	
	}else if(parameter=='plurality'){//获得多个id
	    if(row.length<1){
	    	$.messager.alert('提示',"请至少选择一条记录！");
	       	return null;
	    }else{  
	    	var ids = ""; 
		  	for(i;i<row.length;i++){   
		  		ids += row[i].id+","; 
			}
		  	return id;
	  	} 
	}else if(parameter=='notNull'){//至少获得一个id
		var id = ""; 
	    if(row.length<1){//如果没有选择数据，默认选中第一行数据
	    	$('#list').datagrid('selectRow', 0);
	    	var row = $("#list").datagrid("getSelections");  
	    }
	    id += row[0].id; 
	    return id;
	}else{
		$.messager.alert('提示',"参数无效！");
		return null;
	}
}
/**
 * 动态添加LayOut
 * @author  zpty
 * @param title 标签名称
 * @param url 跳转路径
 * @date 2015-11-17
 * @version 1.0
 */
function AddOrShowEast(title, url) {
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		//重新装载右侧面板
   		$('#divLayout').layout('panel','east').panel({
                        href:url 
                 });
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			href : url,
			closable : true
		});
	}
}	
//条件查询
function searchFrom(){
    var deptName =  $('#deptName').val();
    $('#list').datagrid('load', {
		deptName: deptName 
	});
}	
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		<div style="padding:5px 5px 5px 5px;">	        
			<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
				<tr>
					<td style="width: 300px;">过滤条件：<input type="text" ID="deptName" name="sysDepartment.deptName" placeHolder="名称,简称,五笔,拼音,自定义码" style="width:200px;"/></td>
					<td><shiro:hasPermission name="${menuAlias}:function:query"><a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a></shiro:hasPermission></td>
				</tr>
			</table>
		</div>
		<div style="padding: 0px 5px 5px 5px;">
			<table id="list"style="width:100%;" data-options="url:'${pageContext.request.contextPath}/outpatient/registerDeptManage/querySysDepartment.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true, width :'5%'" ></th>
						<th data-options="field:'deptName' , width :'10%'">名称</th>
						<th data-options="field:'deptBrev', width :'10%'">简称</th>
						<th data-options="field:'deptInputcode', width :'10%'">自定义码</th>
						<th data-options="field:'deptRegisterOrder', width :'10%'">排序</th>
						<th data-options="field:'mfOrderOpr', width :'10%'">排序操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>