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
</head>
<body>
<div id="divLayout" class="easyui-layout" fit="true">
  	<div  data-options="region:'center',split:false,title:'变更列表',iconCls:'icon-book'" style="padding:10px;">
 	  <table id="list" data-options="fit:true">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true" ></th>
				<th data-options="field:'subjectName'">主体名称</th>
				<th data-options="field:'tableName'">表名</th>
				<th data-options="field:'column'">变更属性</th>
				<th data-options="field:'columnName'">变更属性名</th>
				<th data-options="field:'oldValue'">旧值</th>
				<th data-options="field:'newValue'" >新值</th>
				<th data-options="field:'operType'">会话ID</th>
				<th data-options="field:'IP'">IP</th>
				<th data-options="field:'createUser'" >创建人</th>
				<th data-options="field:'createTime'" >创建时间</th>
				<th data-options="field:'createDept'" >创建部门</th>
			</tr>
		</thead>
	 </table>
   	</div>
</div>
 <script type="text/javascript">
 	//加载页面
	$(function(){
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad: function (param) {//加载数据
	        },
	        onDblClickRow: function (rowIndex, rowData) {//双击查看
				if(getIdUtil('#list').length!=0){
			   	    AddOrShowEast('EditForm','<%=basePath %>baseinfo/change/viewChange.action?id='+getIdUtil('#list'));
			   	}
			}
		});
	});
	//刷新
	function reload(){
		//实现刷新栏目中的数据
		 $('#list').datagrid('reload');
	}
	//动态添加LayOut
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
 </script>
</body>