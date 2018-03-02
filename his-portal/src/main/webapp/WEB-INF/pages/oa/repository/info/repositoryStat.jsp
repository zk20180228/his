<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>知识库统计</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'west',border:false" style="width: 30%">
		<table style="padding:7px 7px 5px 7px;height:5%width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td >
					发表量排行
				</td>
				
			</tr>
		</table>
		<div style="width:100%;height:95%;margin-top: 5px;">
			<table id="dept" class="easyui-datagrid"    
			        data-options="idField:'id',rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,
			        singleSelect:true,fit:true,pagination:true,toolbar:'#toolbarId'">   
			    <thead>   
			        <tr>   
			           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
			            <th data-options="field:'deptName',width:200,align:'left'">分类/作者</th>   
			            <th data-options="field:'total',width:200,align:'left'">知识发表量</th>   
			        </tr>   
			    </thead>   
			</table>  
		</div>
	</div>  
	<div data-options="region:'center',border:true">
		<table style="padding:7px 7px 5px 7px;height:5%width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td >
					阅读量排行
				</td>
			</tr>
		</table>
		<div style="width:100%;height:95%;margin-top: 5px;" >
			<table id="ReadQuantity"  class="easyui-datagrid"   
		        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
		        singleSelect:true,fit:true,pagination:true,toolbar:'#toolbarId2'">   
		   		 <thead>   
			        <tr>   
			           	<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
			         	<th data-options="field:'name',width:200,align:'left'">标题</th>   
			            <th data-options="field:'categName',width:200,align:'left'">知识分类</th>   
			            <th data-options="field:'views',width:200,align:'left'">阅读量(次)</th>   
			            <th data-options="field:'createUser',width:200,align:'left'">作者</th>   
			            <th data-options="field:'createTime',width:200,align:'left'">发布时间</th>   
			        </tr>   
		    	</thead>   
			</table>  
		</div>
	</div>
	<div id="toolbarId">
		<input class="easyui-textbox" id="used"  style="width: 150px;" />
		<input  id="type"  style="width: 80px;" readonly="readonly"/>
		<input  id="topnum"  style="width: 80px;" readonly="readonly"/>
		<a href="javascript:void(0)" onclick="searchFormKS()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		<a href="javascript:void(0)" onclick="searchReloadKS()" class="easyui-linkbutton" iconCls="reset">重置</a>
		<a href="javascript:void(0)" onclick="reloadKS()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="toolbarId2">
		<input class="easyui-textbox" id="reading"  style="width: 150px;"/>
		<input  id="readtopnum"  style="width: 80px;" readonly="readonly"/>
		<a href="javascript:void(0)" onclick="searchFormread()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		<a href="javascript:void(0)" onclick="searchReloadRead()" class="easyui-linkbutton" iconCls="reset">重置</a>
		<a href="javascript:void(0)" onclick="reloadYDL()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
<script type="text/javascript">
	$(function(){
		$('#type').combobox({    
		    data:[{'id':'dept','text':'科室',"selected":true},{'id':'author','text':'作者'}],    
		    valueField:'id',    
		    textField:'text',
		    onSelect:function(){
		    	//刷新操作
		    	loadDate();
		    }
		}); 
		$('#topnum').combobox({    
		    data:[{'id':'10','text':'TOP 10',"selected":true},{'id':'50','text':'TOP 50'},{'id':'100','text':'TOP 100'},{'id':'500','text':'TOP 500'}],    
		    valueField:'id',    
		    textField:'text',
		    onSelect : function(data){
		    	//表单刷新操作
		    	loadDate();
		    }
		}); 
		$('#readtopnum').combobox({    
		    data:[{'id':'10','text':'TOP 10',"selected":true},{'id':'50','text':'TOP 50'},{'id':'100','text':'TOP 100'},{'id':'500','text':'TOP 500'}],    
		    valueField:'id',    
		    textField:'text',
		    onSelect : function(data){
		    	//表单刷新操作
		    	searchFormread();
		    }
		}); 
		loadDate();
		$("#ReadQuantity").datagrid({
			url: '<%=basePath %>oa/repositoryInfo/queryRepositoryStatYDL.action',
			pageSize:10,
			pageList:[10,20,30,50,80,100],
			pagination:false
		})
		
	})
	function searchFormread(){
		var reading = $('#reading').textbox('getValue');
		var readtopnum=$('#readtopnum').combobox('getValue');
		$("#ReadQuantity").datagrid('reload',{deptCode: reading,readtopnum:readtopnum });
	}
	function searchReloadRead(){
		$('#reading').textbox('setValue','');
		searchFormread();
	}
	function loadDate(){
		var used = $('#used').textbox('getValue');
		var type=$('#type').combobox('getValue');
		var topnum=$('#topnum').combobox('getValue');
		
		if("dept"==type){
			$("#dept").datagrid({
				url: '<%=basePath %>oa/repositoryInfo/queryRepositoryStatKS.action',
				pageSize:10,
				pageList:[10,20,30,50,80,100],
				pagination:false,
				queryParams:{deptCode: used,topnum: topnum},
				columns:[[    
				          {field:'id',title:'id',hidden:true},    
				          {field:'deptName',title:'分类'},
				          {field:'total',title:'知识发表量'}
				          ]] 
			});
		}else{
			$("#dept").datagrid({
				url: '<%=basePath %>oa/repositoryInfo/queryRepositoryStatZZ.action',
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					pagination:false,
					queryParams:{deptCode:used,topnum: topnum},
					columns:[[    
				          {field:'createUser',title:'作者'},
				          {field:'totalzz',title:'知识发表量'}
				          ]] 
				})
			
		}
		
	}
	
	//查询-科室
	function searchFormKS() {
		loadDate();
	}
	 //查询--阅读量
	function searchFormYDL() {
		var used = $('#reading').textbox('getValue');
		$('#ReadQuantity').datagrid('load', {
			creatUser:used,
		});
	}
	//重置--科室
	function searchReloadKS() {
		var used = $('#used').textbox('getValue');
		$('#used').textbox('setValue','');
		searchFormKS()
	}
	//重置--作者
	function searchReloadZZ() {
		$('#using').textbox('setValue','');
		searchFormZZ()
	}
	//重置--阅读量
	function searchReloadYDL() {
		$('#using').textbox('setValue','');
		searchFormYDL()
	}
	//刷新科室栏目中的数据
	function reloadKS(){
		 $("#dept").datagrid("reload");
	}
	//刷新作者栏目中的数据
	function reloadZZ(){
		 $("#author").datagrid("reload");
	}
	//刷新阅读量栏目中的数据
	function reloadYDL(){
		 $("#ReadQuantity").datagrid("reload");
	}
	
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>