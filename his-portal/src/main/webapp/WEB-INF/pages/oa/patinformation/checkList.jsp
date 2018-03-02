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
$(function(){
	dataGrid();
});

function dataGrid(){
	$('#list').datagrid({    
		url:'<%=basePath%>oa/information/getInformationCheck.action',
	    rownumbers:true,idField: 'id',striped:true,border:true,
	    checkOnSelect:false,selectOnCheck:false,singleSelect:true,
	    fitColumns:false,pagination:true,fit:true,
	    pageSize:20,
		pageList:[20,40,60,100],
		columns:[[    
				  {field:'id',checkbox:'true'},
		          {field:'infoTitle',title:'标题',width:"20%"},    
		          {field:'infoKeyword',title:'关键词',width:"8%"},    
		          {field:'editorName',title:'作者',width:"8%"},    
		          {field:'infoPubtime',title:'发布时间',width:"10%"},    
		          {field:'writerName',title:'攥写人',width:"8%"},    
		          {field:'pubuserName',title:'发布人',width:"8%"},    
		          {field:'infoPubflag',title:'是否发布',width:"8%",
		        	  formatter:function(value,row,index){
		        	  if(value=="1"){
		        		  return "已发布";
		        	  }else if(value=="0"){
		        		  return "未发布";
		        	  }
		        	  return "草稿";
		          }},    
		          {field:'infoCheckFlag',title:'是否审核',width:"8%",
		        	  formatter:function(value,row,index){
		        	  if(value=="1"){
		        		  return "已审核";
		        	  }else if(value=="2"){
			        	  return "未通过";
		        	  }
		        	  return "未审核";
		          }},
		          {field:'check',title:'审核',width:"8%",align:"center",
		        	  formatter:function(value,row,index){
		        		  if(row.infoCheckFlag=="1"){//已审核的显示灰色
		        			  return "<button onclick=\"\" type=\"button\" class=\"btn btn-default active\">已审核</button>";
		        		  }else{
				        	  return "<button onclick=\"checkInformation('"+row.infoMenuid+"','"+row.id+"','"+row.infoPubflag+"')\" type=\"button\" class=\"btn btn-info\">审核</button>";
		        		  }
		          }},
		      ]],
	    onDblClickRow:function(index, row){
			openNav("<%=basePath%>oa/information/toAudit.action?infoid="+row.id, '文章审核', 'informationAuditid');
	    }
	});
}
function skimInformation(menuCode,infoid){
	$.ajax({
		url : '<%=basePath%>oa/information/judgeAuth.action',
		data : {menuId:menuCode,type:"3"},
		success : function(re){
			if(re.resCode=="success"){//进入浏览页面
				openNav("<%=basePath%>oa/information/view.action?infoid="+infoid, '文章浏览', 'informationviewid');
			}else{
				$.messager.alert('提示','您没有查看权限!','info');
				return ;
			}
		},
		error : function(){
			$.messager.alert('提示','网络繁忙,请稍后重试...','info');
		}
	});
}
function checkInformation(menuCode,infoid){
	$.ajax({
		url : '<%=basePath%>oa/information/judgeAuth.action',
		data : {menuId:menuCode,type:"2"},
		success : function(re){
			if(re.resCode=="success"){//进入浏览页面
				openNav("<%=basePath%>oa/information/toAudit.action?infoid="+infoid, '文章审核', 'informationAuditid');
			}else{
				$.messager.alert('提示','您没有审核权限!','info');
				return ;
			}
		},
		error : function(){
			$.messager.alert('提示','网络繁忙,请稍后重试...','info');
		}
	});
}
function searchFrom(){
	var codes = $('#codes').textbox('getValue');
	$('#list').datagrid('load',{
		'name':codes
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
        <table id="list" style="width: 100%"></table>
    </div>   
</div>  
</body>
</html>