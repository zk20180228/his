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
<title>医嘱管理非药品</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">
			<div   region="north" border="false" style="height: 50px">
				<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
					<tr>
						<td style="font-size: 14;width: 500px;">非药品查询：&nbsp;&nbsp;
					 <input class="easyui-textbox" id="undrugName" name="undrugName"  data-options="prompt:'名称,拼音,五笔,自定义'" style="width: 200px;" />
				&nbsp;&nbsp; <a href="javascript:void(0)" onclick="searchFromNondrug()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div   region="center" border="false" style="height: 100%">
				<table id="listnon" class="easyui-datagrid" style="width: 100%;" data-options=" fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
					<th data-options="field:'ck',checkbox:true" ></th>
					<th data-options="field:'name', width : 230">项目名称</th>
					<th data-options="field:'undrugPinyin', width : 100">拼音码</th>
					<th data-options="field:'undrugWb', width : 100">五笔码</th>
					<th data-options="field:'undrugInputcode', width : 100">自定义码</th>
					<th data-options="field:'undrugDefaultprice', width : 100">默认价</th>
					<th data-options="field:'undrugState', width : 100,formatter: function(value,row,index){
																						if (value=='1'){
																							return '在用';
																						}if (value=='2'){
																							return '停用';
																						} if (value=='3'){
																							return '废弃';
																						} 
																					}">状态</th>
					<th data-options="field:'undrugIsownexpense', width : 100 ,formatter:replaceTrueOrFalse">自费</th>
					<th data-options="field:'undrugIsc', width : 100,formatter:replaceTrueOrFalse">组套</th>
					<th data-options="field:'undrugDiseaseclassification', width : 100,formatter:diseasetypeFamater">疾病分类</th>
					<th data-options="field:'undrugRemark', width : 140">备注</th>
				</tr>
					</thead>
				</table>
			</div>
			<div id="addun"></div>
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
		<script type="text/javascript">
		var diseaseclassificationList = null;
		//加载页面
	 	$(function() {
			//添加datagrid事件及分页
			$('#listnon').datagrid({
				pagination:true,
				url:'<%=basePath%>drug/undrug/queryunDrug.action',
				pageSize:20,
				pageList:[20,30,50,80,100],
				onDblClickRow : function(rowIndex, rowData) {//双击查看
			    var row = $('#listnon').datagrid('getSelected'); //获取当前选中行     
	                  if(row){
	             	     Adddilog("查看非药品","<%=basePath%>/drug/undrug/viewDrugUndrug.action?id="+row.id);
		   	       }
		         }
			});
			$.ajax({
				url: "<c:url value='/drug/undrug/queryunDrug.action'/>",
				data:{"type" : "diseasetype"},
				type:'post',
				success: function(diseasetypeData) {
					diseaseclassificationList = diseasetypeData;
				}
			});
			bindEnterEvent('undrugName',searchFromNondrug,'easyui');
	 	});
	 	 
	 //加载dialog
	function Adddilog(title, url) {
		$('#addun').dialog({    
		    title: title,    
		    width: 800,    
		    height:600,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	//打开dialog
	function openDialog() {
		$('#addun').dialog('open'); 
	}
	//关闭dialog
	function closeDialogun() {
		$('#addun').dialog('close');  
	}
	 
	 //查询
	function searchFromNondrug() {
		var undrugName = $('#undrugName').textbox('getValue');
		$('#listnon').datagrid('load', {
			undrugName : undrugName
		});
	}
	
	//显示疾病类别格式化 
	function diseasetypeFamater(value){
		if(value!=null){
			for(var i=0;i<diseaseclassificationList.length;i++){
				if(value==diseaseclassificationList[i].id){
					return diseaseclassificationList[i].name;
				}
			}	
		}
	}
	//替换字符
	function replaceTrueOrFalse(val){
		if(val == 1){
			return '是';
		}if(val == 0){
			return '否';
		}if(val == ""){
			return '否';
		}
		
	}
</script>
	</body>
</html>