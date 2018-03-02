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
<title>医嘱管理药品</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">
		   <div   region="north" border="false" style="height: 50px">
				<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
					<tr>
						<td style="font-size: 14;width: 500px;">
							药品查询：&nbsp;&nbsp;
					<input class="easyui-textbox" id="drugName" name="drugName" data-options="prompt:'药品名称,拼音,五笔,自定义'" style="width: 195px;" />&nbsp;&nbsp; 
					<a href="javascript:void(0)" onclick="searchFromDrug()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div   region="center" border="false" style="height: 100%">
				<table id="listdurg" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'name'" style="width:18%">名称</th>
							<th data-options="field:'drugNamepinyin'" style="width:12%" >名称拼音码</th>
							<th data-options="field:'drugNamewb'" style="width:12%">名称五笔码</th>
							<th data-options="field:'drugNameinputcode'" >名称自定义码</th>
							<th data-options="field:'drugCommonname'" style="width:12%">通用名称</th>
							<th data-options="field:'drugCnamepinyin'" style="width:8%">通用名称拼音码</th>
							<th data-options="field:'drugCnamewb'" style="width:8%">通用名称五笔码</th>
							<th data-options="field:'drugCnameinputcode'" style="width:12%">通用名称自定义码</th>
							<th data-options="field:'drugBiddingcode'" style="width:8%" >招标识别码</th>
						</tr>
					</thead>
				</table>		
			</div>
		</div>
		<div id="addDrugsView"></div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
		<script type="text/javascript">
		//加载页面
	 $(function() {
		//添加datagrid事件及分页
			$('#listdurg').datagrid({			
				pagination:true,
				url:'<%=basePath%>drug/info/queryDrug.action',
				pageSize:20,
				pageList:[20,30,50,80,100],
				onDblClickRow : function(rowIndex, rowData) {//双击查看
			   	 var row = $('#listdurg').datagrid('getSelected'); //获取当前选中行     
	                  if(row){
	             	     AdddilogDrugs("查看药品","<%=basePath%>drug/info/DrugView.action?menuAlias=YPGL&id="+row.id);
		   	       }
		        }
			});
		bindEnterEvent('drugName',searchFromDrug,'easyui');
	 });
	 
	 
	 //加载dialog
	function AdddilogDrugs(title, url) {
		$('#addDrugsView').dialog({    
		    title: title,    
		    width: 1000,    
		    height:550,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	//打开dialog
	function openDialog() {
		$('#addDrugsView').dialog('open'); 
	}
	//关闭dialog
	function closeDialogDrugsView() {
		$('#addDrugsView').dialog('close');  
	}
	 
	 //查询
function searchFromDrug() {
	var drugName = $('#drugName').textbox('getValue');
	$('#listdurg').datagrid('load', {
		drugName : drugName
	});
}
	
</script>
	</body>
</html>