<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>		<!--  类别为3  5的少量数据表格  -->
<body>
	<div>
		<div style="padding:10px;">
			<input type="hidden" id="d_isupdata" value="${isupdata}"/>
    		<div id='d_item' style="width: 100%;padding: 5">
    			<div>
					<table id='d_unsettlement' title='双击选择结算类别'></table>
				</div>
				<br>
				<div>
					<table id='d_settlement' title='已选择结算类别' style='padding:5px 0 0 0'></table>
				</div>
			</div>
			<div style='text-align:center;padding:5px'>
				<a href='javascript:d_save();' class='easyui-linkbutton' data-options="iconCls:'icon-save'">保存</a>
				<a href='javascript:void(0)' onclick='closeDialog()' class='easyui-linkbutton' data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
	    </div>
	</div>

<script type="text/javascript">

<%--*** 类别:1药品;2专科;3结算类别;4特定收费窗口;5挂号级别   ***--%>
var dialog_itemType='${dialogItemType}';
<%--*** 类别:1药品;2专科;3结算类别(settlement);4特定收费窗口(charge);5挂号级别(register)   ***--%>
$(function(){
	if(dialog_itemType==3){
		d_funsettlement();
	}else if(dialog_itemType==5){
		document.getElementById('d_unsettlement').attributes.title.value = '双击选择挂号级别';
		document.getElementById('d_settlement').attributes.title.value = '已选择挂号级别';
		d_funregister();
	}
	
})

// drug dept settlement charge register

<%--*** 3结算类别;添加数据  *****************************************************************************************************--%>
function d_funsettlement(){
	$('#d_unsettlement').datagrid({
		selectOnCheck:false,
		rownumbers:true,
		idField: 'id',pagination:true,pageSize:10,fitColumns:true,
		singleSelect:true,checkOnSelect:false,pageNumber: 1, 
		url:"<%=basePath%>baseinfo/pubCodeMaintain/queryPubCode.action?dictionary.type=paykind",
	    columns:[[
	        {field:'encode',title:'结算编码',width:'40%'},    
	        {field:'name',title:'结算类别',width:'40%'}    
	    ]],onDblClickRow:function(rowIndex, rowData){
	    	 var obj=$('#d_settlement').datagrid('getData');
	    	 if("${isupdata}"==1){
		    	 if(obj.total != 0){
		    		 $.messager.alert('友情提示','只允许选择一条数据,请双击下表记录取消');
		    		 setTimeout(function(){$(".messager-body").window('close')},1500);
		    		 return false;
		    	 }
	    	 }
	    	 a=true;
    		 $.each(obj.rows,function(i,n){
 			  	if(n.encode==rowData.encode){
 			  		a=false;
 			  	}
 			  }); 
 			 if(a){
 				 $('#d_settlement').datagrid('appendRow',{
 					id: rowData.encode,
 					name: rowData.name
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
	
	$('#d_settlement').datagrid({
		selectOnCheck:false,rownumbers:true,pageSize:20,
		fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
		autoSave:true,
	    columns:[[
	        {field:'id',title:'结算编码',width:'40%'},    
	        {field:'name',title:'结算类别',width:'40%'}   
	    ]],onDblClickRow:function(rowIndex, rowData){
	    	$('#d_settlement').datagrid('deleteRow',rowIndex);
	    }
	    
	});
}
<%--*** 3结算类别;添加数据  *****************************************************************************************************--%>



<%--*** 5挂号级别;添加数据  *****************************************************************************************************--%>

function d_funregister(){
	$('#d_unsettlement').datagrid({
		selectOnCheck:false,
		rownumbers:true,
		idField: 'id',pagination:true,pageSize:10,fitColumns:true,
		singleSelect:true,checkOnSelect:false,pageNumber: 1, 
		url:'<%=basePath%>drug/specialDispensingtable/queryRegisterData.action',
	    columns:[[
	        {field:'code',title:'级别代码',width:'15%'},    
	        {field:'name',title:'级别名称',width:'15%'},   
	        {field:'expertno',title:'是否使用专家号',width:'15%',
	        	formatter:formatCheckBox	
	        },   
	        {field:'specialistno',title:'是否使用专科',width:'15%',
	        	formatter:formatCheckBox	
	        },  
	        {field:'specialdiagnosisno',title:'是否使用特诊',width:'15%',
	        	formatter:formatCheckBox	
	        }    
	    ]],onDblClickRow:function(rowIndex, rowData){
	    	 var obj=$('#d_settlement').datagrid('getData');
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
 				 $('#d_settlement').datagrid('appendRow',{
 		    		id: rowData.code,
 		    		name: rowData.name,
 		    		expertno: rowData.expertno,
 		    		specialistno: rowData.specialistno,
 		    		specialdiagnosisno: rowData.specialdiagnosisno
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
	
	$('#d_settlement').datagrid({
		selectOnCheck:false,rownumbers:true,pageSize:20,
		fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
		autoSave:true,
	    columns:[[
			{field:'id',title:'级别代码',width:'15%'},    
			{field:'name',title:'级别名称',width:'15%'},   
			{field:'expertno',title:'是否使用专家号',width:'15%',
				formatter:formatCheckBox	
			},{field:'specialistno',title:'是否使用专科',width:'15%',
				formatter:formatCheckBox	
			},{field:'specialdiagnosisno',title:'是否使用特诊',width:'15%',
				formatter:formatCheckBox	
			}
	    ]],onDblClickRow:function(rowIndex, rowData){
	    	$('#d_settlement').datagrid('deleteRow',rowIndex);
	    }
	});
}

function formatCheckBox(val,row){
	if (val == 1){
		return '是';
	} else {
		return '否';
	}
}
<%--*** 5挂号级别;添加数据end  *****************************************************************************************************--%>
/**
 * 关闭窗口
 * 
 */
function closeDialog(){
	window.close();
}
<%--*** ---判断---判断---保存  ***--%>
function d_save(){
	if(dialog_itemType==3){
		d_saveSettlement();
	}else if(dialog_itemType==4){
		
	}else if(dialog_itemType==5){
		d_saveRegister();
	}
}
<%--***	---判断---保存		将选择结算类别添加到项目名称和项目编码  ********************************************************--%>
//3结算类别
function d_saveSettlement(){
	 var obj=$('#d_settlement').datagrid('getData');
	 if("${isupdata}"==1){
		 if(obj.total==1){
			 $('#d_settlement').datagrid('acceptChanges');
			 var code = JSON.stringify( $('#d_settlement').datagrid("getRows"));
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
			 $('#d_settlement').datagrid('acceptChanges');
			 var code = JSON.stringify( $('#d_settlement').datagrid("getRows"));
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

//5挂号级别
function d_saveRegister(){
	var obj=$('#d_settlement').datagrid('getData');
	
	 if("${isupdata}"==1){
		 if(obj.total==1){
			 $('#d_settlement').datagrid('acceptChanges');
			 var code = JSON.stringify( $('#d_settlement').datagrid("getRows"));
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
			 $('#d_settlement').datagrid('acceptChanges');
			 var code = JSON.stringify( $('#d_settlement').datagrid("getRows"));
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

</script>
</body>
</html>	