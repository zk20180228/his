<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>		<!--  类别为 1 的大量药品数据  -->
<body>
<div id="divLayout"  class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; overflow-y: auto;">
	<div data-options="region:'center',split:false,iconCls:'icon-book' " style="width: 100%;height: 50%">
	  	<div style="padding: 5px 5px 0px 5px;">
			 <table id="search"  style="width: 100%; border: 1px solid #95b8e7; ">
					<tr>
						<td style="font-size: 14">
							查询条件：<input class="easyui-textbox" name="name" id="d_name" data-options="prompt:'药品名称,拼音,五笔,自定义'" />
							药品类型：<input class="easyui-combobox" type="text" id="CodeDrugtype" style="width:140px" data-options="prompt:'请选择药品类型'"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
			</div>   
	   <div style="padding: 5px 5px 5px 5px;">
			<table id="d_unlist" style="width: 100%"></table>
			<br/>
			<table id="d_list" style="width: 100%"></table>
		</div>
		<div style='text-align:center;padding:5px'>
		 	<a href='javascript:d_save();' class='easyui-linkbutton' data-options="iconCls:'icon-save'">保存</a>
		 	<a href='javascript:closeDialog();' class='easyui-linkbutton' data-options="iconCls:'icon-cancel'">关闭</a>
		 </div>
	</div>
</div>
<script type="text/javascript">
// var it = window.opener.document.getElementById('itemType').val();
// alert(it);
<%--*** 类别:1药品;2专科;3结算类别;4特定收费窗口;5挂号级别   ***--%>
var dialog_itemType='${dialogItemType}';
$(function(){
	$('#d_name').textbox({});
	bindEnterEvent("d_name", searchFrom, 'easyui');
	d_funDrug();
	idCombobox();
})

function d_funDrug(){
	$('#d_unlist').datagrid({
		url:'<%=basePath%>drug/specialDispensingtable/queryDrugData.action',
		selectOnCheck:false,
		rownumbers:true,
		idField: 'id',pagination:true,pageSize:10,fitColumns:true,
		singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
	    columns:[[
	        {field:'name',title:'药品名称',width:'35%'},    
	        {field:'spec',title:'药品规格',width:'24%'},    
	        {field:'code',title:'药品编码',width:'25%',hidden:'true'},    
	        {field:'drugCommonname',title:'通用名称',width:'35%'}    
	    ]],onDblClickRow:function(rowIndex, rowData){
	    	 var obj=$('#d_list').datagrid('getData');
	    	if("${isupdata}"==1){
		    	 if(obj.total != 0){
		    		 $.messager.alert('友情提示','只允许选择一条数据,请双击下表记录取消');
		    		 setTimeout(function(){$(".messager-body").window('close')},1500);
		    		 return false;
		    	 }
	    	}
	    	 a=true;
    		 $.each(obj.rows,function(i,n){
 			  	if(n.id==rowData.id){
 			  		a=false;
 			  	}
 			  }); 
 			 if(a){
 				 $('#d_list').datagrid('appendRow',{
 					id:rowData.id,
 					code:rowData.code,
 					spec:rowData.spec,
 					name: rowData.name,
 					drugCommonname: rowData.drugCommonname
 		    	 });
 			  }else{
 				 $.messager.alert('提示','数据已存在，请勿重复添加!');
 				setTimeout(function(){$(".messager-body").window('close')},1500);
 			  }
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
	
	$('#d_list').datagrid({
		selectOnCheck:false,rownumbers:true,
		fitColumns:true,singleSelect:true,checkOnSelect:false,
		autoSave:true,
	    columns:[[
	        {field:'id',hidden:'true'},    
	        {field:'code',hidden:'true'},    
	        {field:'name',title:'药品名称',width:'35%'},  
	        {field:'spec',title:'药品规格',width:'25%'},    
	        {field:'drugCommonname',title:'通用名称',width:'35%'}
	    ]],onDblClickRow:function(rowIndex, rowData){
	    	$('#d_list').datagrid('deleteRow',rowIndex);
	    }
	});
	
}
<%--*** ---加载药品类别下来框  ***--%>
function idCombobox() {
	$('#CodeDrugtype').combobox({
		url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugType",
		valueField : 'encode',
		textField : 'name',
		multiple : false,
		onSelect:function(record){
			searchFrom();
		}
	});
}

/**
 * 关闭窗口
 * 
 */
function closeDialog(){
	window.close();
}
<%--*** ---判断---判断---保存  ***--%>
function d_save(){
	var obj=$('#d_list').datagrid('getData');
	 if("${isupdata}"==1){
		 if(obj.total==1){
			 $('#d_register').datagrid('acceptChanges');
			 var code = JSON.stringify( $('#d_list').datagrid("getRows"));
			 var str="";
			 $.each(obj.rows,function(i,n){
				 if(str.length>0){
					 str+=",";
				 }
				str+=n.name;
			 });
			 window.opener.setValue(code,str);
			 window.close();
		 }else{
			 $.messager.alert('友情提示','只允许选择一条数据');
			 setTimeout(function(){$(".messager-body").window('close')},1500);
		 }
	 }else{
		 if(obj.total>0){
			 $('#d_register').datagrid('acceptChanges');
			 var code = JSON.stringify( $('#d_list').datagrid("getRows"));
			 var str="";
			 $.each(obj.rows,function(i,n){
				 if(str.length>0){
					 str+=",";
				 }
				str+=n.name;
			 });
			 window.opener.setValue(code,str);
			 window.close();
		 }else{
			 $.messager.alert('友情提示','请选择要添加的数据');
			 setTimeout(function(){$(".messager-body").window('close')},1500);
		 }
	 }
}
function searchFrom(){
	var drugType = $('#CodeDrugtype').combobox('getValue');
	var name =	$('#d_name').textbox('getValue');
    $('#d_unlist').datagrid('load',{
    	name:name,
		drugType : drugType
    });
}

</script>
</body>
</html>	