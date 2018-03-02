<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>信息列表</title>
</head>
<body>
<div id="tt" class="easyui-tabs"  data-options="fit:true,border:false,tabWidth:100,tabHeight:30,narrow:true">   
	   
	<div title="信息列表"  > 
		  <div style="height:5%;weight:100%;">
		       		<div style="padding-top:8px;padding-bottom:0px;padding-right:5px;text-align:right">
		       			<input  id="search_text_1" class="easyui-textbox" data-options="prompt:'请输入要查询信息标题'" style="height:35px;width:200px">
		       			<a id="search_1"  class="easyui-linkbutton buttonStyle" style="height:35px;width:80px;background-color:#00CC00;">查询</a>  
		      		</div>	
		  </div> 
		       
		<div style="height:95%;weight:100%;"><table id="dg1"></table></div>  
	</div>   
		          
 </div>
</body>
</html>

<script type="text/javascript">
		
		$(function(){
			
			var searchText=$("#search_text_1").val();
			if(searchText==""){
				searchText="${infoTitle}";
			}
			var menu_code="${menuCode}";
			//加载信息列表
			$('#dg1').datagrid({    
			    url:"${pageContext.request.contextPath}/oa/OAHome/infoList.action",   
			    fitColumns:true,
			    idField:"id",
			    pagination:true,
			    striped:true,
			    fit:true,
			    pageList:[10,20,30,40,50],
			    pageSize:10,
			    rownumbers:true,
			    singleSelect:true,
			    columns:[[    
			        {field:'infoId',checkbox:true,width:100},    
			        {field:'infoTitle',title:'标题',width:100,align:'center'},    
			        {field:'infoPubTime',title:'发布时间',width:100,align:'center'},
			        {field:'infoKeyWord',title:'关键字',width:100,align:'center'}, 
			        {field:'infoEditor',title:'作者',width:100,align:'center'}, 
			        {field:'updateUser',title:'修改人',width:100,align:'center'}, 
			        {field:'updateTime',title:'修改时间',width:100,align:'center'}, 
			        {field:'outTime',title:'过期时间',width:100,align:'center'},
			    ]],
			    queryParams: {
			    	infoTitle: searchText,
			    	menuCode: menu_code
				}
			});  
			
			$("#search_1").click(function(){
				searchText=$("#search_text_1").val();
				menu_code="${menuCode}";
				$('#dg1').datagrid('load',{
					infoTitle: searchText,
					menuCode: menu_code
				});
			});
			
		});
	

</script>
