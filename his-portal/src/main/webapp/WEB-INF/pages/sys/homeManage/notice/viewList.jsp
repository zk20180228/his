<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">

//人员map
var employeeMap=null;

$(function(){
	funformat();
	dataGrid();
});

function dataGrid(){
	$('#list1').datagrid({    
	   	url:"<%=basePath%>sys/noticeManage/queryViewList.action?info.infoPubflag=${info.infoPubflag}&info.infoType=${info.infoType}",
	   			
	    rownumbers:true,idField: 'id',striped:true,border:true,
	    checkOnSelect:false,selectOnCheck:false,singleSelect:true,
	    fitColumns:false,pagination:true,fit:true,
	    pageSize:20,
		pageList:[20,40,60,100],
	    columns:[[    
	        {field:'id',checkbox:'true',width:'5%'},    
	        {field:'infoTitle',title:'标题',width:'45%'},    
	        {field:'infoKeyword',title:'关键字',width:'25%'},
	        {field:'infoPubuser',title:'发布人',width:'8%',
	        	formatter:funEmployeeMap
	        },    
	        {field:'infoPubtime',title:'发布时间',width:'15%'},  
	    ]],
	    onDblClickRow:function(index, row){
			var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
			window.parent.addTab('信息浏览', url);
	    }
	});
}

function funformat(){
	$.ajax({
		url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
		type:'post',
		async: false,
		success: function(data) {
			employeeMap = data;
		}
	});
}

//渲染人员map
function funEmployeeMap(value,row,index){
	if(value!=null&&value!=''){
		return employeeMap[value];
	}
}
function searchFrom(){
	var codes = $('#codes').textbox('getValue');
	$('#list1').datagrid('load',{
		'info.infoBrev':codes
	});
}
setTimeout(function(){
	bindEnterEvent('codes',searchFrom,'easyui');//回车键查询	
},200);
</script>
</head>
<body style="overflow-x:auto;overflow-y:auto;">
<div id="cc" class="easyui-layout" style="width:100%;height:100%;">
    <div data-options="region:'north',border:false" style="height:35px;text-align:left;padding:1px;">
		<table style="width:100%;border:0px;padding:1px;">
			<tr>
				<td style="font-size:14px" >
					<input  id="codes" data-options="prompt:'请输入标题或关键字...'"  class="easyui-textbox"  />
					<a href="javascript:searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</td>
			</tr>
		</table>
	</div>	
    <div data-options="region:'center'" style="padding: 5">
        <table id="list1" style="width: 100%"></table>
    </div>   
</div>  
</body>
</html>