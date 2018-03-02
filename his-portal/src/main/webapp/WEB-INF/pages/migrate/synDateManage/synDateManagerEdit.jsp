<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>同步数据添加</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<form id="SynDateFrom" method="post"  >
		<input id="id" name="id" type=hidden value="${dataSynch.id }" >
		<input id="code" name="code" type="hidden" value="${dataSynch.code }" >
		<div data-options="region:'center',border:false" style="width: 100%;height: 100%;padding:5px" align="center">
			<table class="honry-table"  cellpadding="1" cellspacing="1"	border="1px solid black" style="width:100%;padding:5px">
				<tr>
					<td class="honry-lable">
						相关表：
					</td>
					<td >
						<input id="tableName" name="tableName" class="easyui-textbox" data-options="required:true" value="${dataSynch.tableName}" >
					</td>
					<td class="honry-lable">
						所属用户：
					</td>
					<td >
						<input id="tableFromUser" name="tableFromUser" class="easyui-textbox"  value="${dataSynch.tableFromUser}" >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						表名：
					</td>
					<td>
						<input id="tableZhName" name="tableZhName" class="easyui-textbox"  value="${dataSynch.tableZhName}" >
					</td>
					<td class="honry-lable">
						主键：
					</td>
					<td>
						<input id="primaryColum" name="primaryColum" class="easyui-textbox"  value="${dataSynch.primaryColum}" >
					</td>
				</tr>
				<tr style="height:100px;">
					<td class="honry-lable">
						所在视图：
					</td>
					<td>
						<input id="viewName" name="viewName" style="height:90px;width:400px;" data-options="prompt:'输入视图名\',\'分割',multiline:true" value="${dataSynch.viewName}" class="easyui-textbox">
					</td>
					<td class="honry-lable">
						视图名称：
					</td>
					<td>
						<input id="viewZhName" name="viewZhName" style="height:90px;width:400px;" class="easyui-textbox"  data-options="multiline:true" value="${dataSynch.viewZhName}" ></input>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
							同步方式：
					</td>
					<td>
						<input id="synchSign" name="synchSign" class="easyui-combobox" value="${dataSynch.synchSign}" data-options="required:true,
							editable:false,
							valueField: 'id',
							textField: 'value',
							data:[
							{id:'0',value:'增量'},
							{id:'1',value:'全量'}]" >
					</td>
					<td class="honry-lable">
						增量字段：
					</td>
					<td>
						<input id="synchCond" name="synchCond" class="easyui-textbox" value="${dataSynch.synchCond}" >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开启线程数：
					</td>
					<td colspan="3">
						<input id="threadNum" name="threadNum" class="easyui-numberbox" value="${dataSynch.threadNum}" data-options="required:true">
					</td>
					
				</tr>
				<tr>
					<td class="honry-lable">
							同步间隔：
					</td>
					<td>
						<input id="timeSpace" name="timeSpace" class="easyui-numberbox" style="width: 115px;" value="${dataSynch.timeSpace }" data-options="required:true">
						<input class="easyui-combobox" id="timeUnit" name="timeUnit" value="${dataSynch.timeUnit}" style="width: 50px;" data-options="required:true,
							editable:false,
							valueField: 'id',
							textField: 'value',
							data:[
							{id:'S',value:'秒'},
							{id:'M',value:'分'},
							{id:'H',value:'时'},
							{id:'D',value:'天'},
							{id:'W',value:'周'}]" >
					</td>
						　　
					<td  class="honry-lable">
						同步时长：
					</td>
					<td>
						<input id="synchLength" name="synchLength" class="easyui-numberbox" style="width: 115px;" value="${ dataSynch.synchLength}"  data-options="required:true">
						<input id="synchUnit" name="synchUnit" class="easyui-combobox" style="width: 50px;" value="${dataSynch.synchUnit }" 
						data-options="required:true,
							editable:false,
							valueField: 'id',
							textField: 'value',
							data:[
							{id:'S',value:'秒'},
							{id:'M',value:'分'},
							{id:'H',value:'时'},
							{id:'D',value:'天'},
							{id:'W',value:'周'}]"  >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						schema(账户)：
					</td>
					<td >
						<input id="schema"  name="schema" class="easyui-textbox" value="${dataSynch.schema}" date-options="required:true" >	
					</td>
					<td class="honry-lable">
						分区字段：
					</td>
					<td >
						<input id="tablePartition"  name="tablePartition" class="easyui-textbox" value="${dataSynch.tablePartition}"  >	
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						查询字段：
					</td>
					<td>
						<input id="queryField"  name="queryField" class="easyui-textbox"  value="${dataSynch.queryField}" />
					</td>
					<td class="honry-lable">
						查询条件：
					</td>
					<td>
						<input id="queryCond" name="queryCond" value="${dataSynch.queryCond}" class="easyui-textbox" >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						分组字段：
					</td>
					<td>
						<input id="groupFiled" name="groupFiled" class="easyui-textbox" value="${dataSynch.groupFiled}" >
					</td>
					<td class="honry-lable">
						排序字段：
					</td>
					<td>
						<input id="orderFiled" name="orderFiled" class="easyui-textbox"  value="${dataSynch.orderFiled}" >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						视图排序：
					</td>
					<td >
						<input id="viewOrder" name="viewOrder" class="easyui-numberbox" value="${dataSynch.viewOrder}" >
					</td>
					<td class="honry-lable">
						排序条件：
					</td>
					<td>
						<input id="orderCond" name="orderCond" class="easyui-textbox"  value="${dataSynch.orderCond}" >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						表排序：
					</td>
					<td colspan="3">
						<input id="tableOrder" name="tableOrder" class="easyui-numberbox" value="${dataSynch.tableOrder}" >
					</td>
					
				</tr>
				<tr>
					<td class="honry-lable">
						执行服务分类(主)：
					</td>
					<td>
						<input id="serveCode" name="serveCode"   value="${dataSynch.serveCode }" readonly="readonly">
					</td>
					<td class="honry-lable">
					        执行服务分类(备)：
					</td>
					<td>
						<input id="serveCodeprepare" name="serveCodeprepare"   value="${dataSynch.serveCodeprepare }" readonly="readonly" >
					</td>
				</tr>
				<tr>
					<td  class="honry-lable">
						默认同步时间：
					</td>
					<td>
						<input id="defaTime" name="defaTime" type="text" class="Wdate"  value="${dataSynch.defaTimeStr }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"   />  
					</td>
					<td  class="honry-lable">
						最新同步时间：
					</td>
					<td>
						<input id="newestTime" name="newestTime" type="text" class="Wdate"  value="${dataSynch.newesTimeStr }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"   />
					</td>
				</tr>
				<tr>
					<td  class="honry-lable">
						状态：
					</td>
					<td>
						<input id="state" name="state" class="easyui-combobox"  value="${dataSynch.state }" 
							data-options="required:true,editable:false,
								valueField: 'id',
								textField: 'value',
								data:[
								{id:'0',value:'启用'},
								{id:'1',value:'停用'}]">
					</td>
					<td class="honry-lable">
							是否业务关联：
						</td>
						<td>
							<input id="workSign" class="easyui-combobox" name="workSign"   value="${dataSynch.workSign }" 
								data-options="required:true,editable:false,
									valueField: 'id',
									textField: 'value',
									data:[
									{id:'0',value:'是'},
									{id:'1',value:'否'}]">
						</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注：
					</td>
					<td colspan="3">
						<input  id="remarks" name=remarks class="easyui-textbox" style="width:600px;" value="${dataSynch.remarks }">
					</td>
				</tr>
			</table>
			<table style="width: 100%;">
				<tr >
						<shiro:hasPermission name="${menuAlias}:function:save"> 
						<a href="javascript:void(0);" onclick="submit()" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="text-align: right ;">保&nbsp;存&nbsp;</a>
						</shiro:hasPermission> 
						<shiro:hasPermission name="${menuAlias}:function:cancel">
						&nbsp;<a href="javascript:void(0);" onclick="closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" style="text-align: right ;">取&nbsp;消&nbsp;</a>
						</shiro:hasPermission>
				</tr>
			</table>
		</div>
		<div id="proc"></div>
	</form>
</div>
<script type="text/javascript">
$(function(){
	//加载服务列表
	 $('#serveCode').combobox({ 
		url: '<%=basePath%>migrate/outInterfaceManager/findAllServer.action',
		queryParams:{queryCode:1},
		valueField:'code',    
	    textField:'name',   
		required: true, 
		missingMessage:'请选择所属栏目',
		
	});
	 $('#serveCodeprepare').combobox({ 
			url: '<%=basePath%>migrate/outInterfaceManager/findAllServer.action',
			queryParams:{queryCode:2},
			valueField:'code',    
		    textField:'name',   
			required: true, 
			missingMessage:'请选择所属栏目',
			
		});
});
	function submit(){
		$('#SynDateFrom').form('submit',{    
		    url:'<%=basePath%>/migrate/SynDateManager/editSynDate.action',
		    onSubmit: function(){//保存和更改  
		    	 if($(this).form('validate')){
		    		 $.messager.progress({text:'保存中，请稍后...',modal:true});
		    		 return true;
		    	 }else{
		    		 $.messager.alert('提示','请填写完整信息');
		    		 return false;
		    	 }
		    },    
		    success:function(date){   
		    	$.messager.progress('close'); 
		    	var data = eval('(' + date + ')');
		    	$.messager.alert('提示',data.resMsg);
		    	closeLayout();
		    }    
		}); 
	}
	function closeLayout(){
    window.opener.document.getElementById("searchFrom").click();
	window.close();
	}
</script>	
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>