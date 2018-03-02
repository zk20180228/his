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
<title>Insert title here</title>
</head>
<body>
<div id="listeidt" class="easyui-panel" style="height:100%;border: none;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',split:true">
				<form id="editForm" method="post">
					<input type="hidden" name="rules.id" value="${rules.id }"/>
					<input type="hidden" name="rules.createUser" value="${rules.createUser }"/>
					<input type="hidden" name="rules.createDept" value="${rules.createDept }"/>
					<input type="hidden" name="rules.createTime" value="${rules.createTime }"/>
					<input type="hidden" name="rules.updateUser" value="${rules.updateUser }"/>
					<input type="hidden" name="rules.updateTime" value="${rules.updateTime }"/>
					<input type="hidden" name="rules.deleteUser" value="${rules.deleteUser }"/>
					<input type="hidden" name="rules.deleteTime" value="${rules.deleteTime }"/>
					<input type="hidden" name="rules.stop_flg" value="${rules.stop_flg }"/>
					<input type="hidden" name="rules.del_flg" value="${rules.del_flg }"/>
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="honry-lable">栏目名称:</td>
			    			<td class="honry-info">
			    				<input class="easyui-combobox" id="menuAliasQ" name="rules.menuAlias" value="${rules.menuAlias }" 
			    								data-options="required:true
			    									,url:'<%=basePath%>sys/pretreatment/getMenuCombobx.action'
			    									,valueField:'alias',textField:'name'"  style="width:200px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">类型:</td>
			    			<td class="honry-info">
			    				<input class="easyui-combobox" id="type" name="rules.type" value="${rules.type }" 
			    							data-options="required:true,valueField: 'id',textField: 'text',
			    								data:[{'id' : 3,'text' : '年'},{'id' : 2,'text' : '月'},{'id' : 1,'text' : '日'}]  "  style="width:200px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">状态:</td>
			    			<td class="honry-info">
			    				<input class="easyui-combobox" id="state" name="rules.state" value="${rules.state }" 
			    					data-options="required:true,valueField:'id',textField:'text', 
			    						data: [{id: '1',text: '开启'},{id: '2',text: '关闭'}]"  style="width:200px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">规则:</td>
			    			<td class="honry-info">
			    				<input class="easyui-combobox" id="rules" name="rules.rules" value="${rules.rules }" style="width:200px" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">备注：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id=remark name="rules.remark" value="${rules.remark }" style="width:220px" /></td>
		    			</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south',split:false" style="height:50px;">
				<div style="text-align: center; padding: 5px;">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitT()" data-options="iconCls:'icon-bullet_tick'">确定</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeDialog()" data-options="iconCls:'icon-cancel'">关闭</a>
				</div>	
			</div> 
		</div>
	</div>
	<script type="text/javascript">
$(function(){
	$('#rules').combobox({
		required:true,
		selectOnNavigation:true,
		valueField: 'id',
		textField: 'text',
		data:[
		      {'id' : '5m','text' : '每5分钟一次'},
		      {'id' : '10m','text' : '每10分钟一次'},
		      {'id' : '15m','text' : '每15分钟一次'},
		      {'id' : '20m','text' : '每20分钟一次'},
		      {'id' : '30m','text' : '每30分钟一次'},
		      {'id' : '1h','text' : '每1小时一次'},
		      {'id' : '2h','text' : '每2小时一次'},
		      {'id' : '3h','text' : '每3小时一次'},
		      {'id' : '4h','text' : '每4小时一次'},
		      {'id' : '6h','text' : '每6小时一次'},
		      {'id' : '8h','text' : '每8小时一次'},
		      {'id' : '12h','text' : '每12小时一次'}
		      ]
	});
});
</script>
</body>
</html>