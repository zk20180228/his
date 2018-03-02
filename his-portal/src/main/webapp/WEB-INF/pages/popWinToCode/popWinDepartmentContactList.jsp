<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center',fit:true"  id="content" style="width: 100%">
					<div style="padding: 5px 5px 0px 5px;height: 5%;margin-bottom: 5px;" data-options="fit:true">
						<form id="search" method="post" data-options="fit:true">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 30%;margin-left: 5px;" nowrap="nowrap">
										关键字： <input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" />
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div style="padding: 0px 5px 5px 5px;height: 90%">
					<table id="list" style="width:100%" data-options="fit:true">
						<thead>
							<tr>
			    				<th data-options="field:'deptName'" style="width:20%">名称</th>
			    				<th data-options="field:'spellCode'" style="width:20%">拼音码</th>
			    				<th data-options="field:'wbCode'" style="width:20%">五笔码</th>
			    				<th data-options="field:'userDefinedCode'" style="width:20%">自定义码</th>
			    				<th data-options="field:'deptEnCode'" style="width:20%">英文名称</th>
							</tr>
						</thead>
					</table>
				</div>
	</div>
</div>
</body>
	<script type="text/javascript">
	
	var textId= "${param.textId}";
	var textName= "${param.textName}";
	
	//加载页面
		$(function(){
			var winH=$("body").height();
			$('#p').height(winH-78-30-27-2);
			$('#treeDiv').height(winH-78-30-27-2);
			$('#list').height(winH-78-30-27-22);
			
			var id="${id}"; //存储数据ID
			//添加操作按钮
			$('#list').datagrid({
				url:'<%=basePath%>popWin/popWinDepartmentContact/queryDepartmentContact.action',
				method:'post',
				rownumbers:true,
				idField: 'id',
				striped:true,
				border:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,
				pageSize:20,
				onLoadSuccess: function (data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
				  if(value.id == id){
				     $("#list").datagrid("checkRow", index);
				  }
			    });
			    },toolbar: ['-', {
	                  id: 'btnReload',
	                  text: '刷新',
	                  iconCls: 'icon-reload',
	                  handler: function () {
	                    //实现刷新
	                    $("#list").datagrid("reload");
	                  }
	             }],
	             onDblClickRow: function (rowIndex, rowData) {//双击查看
					if(getIdUtil("#list").length!=0){
			    		var tmpId ="#"+textId;
			    		var tmpName = "#"+textName;
						if(window.opener.$(tmpId).attr('class')&&/combobox/ig.test(window.opener.$(tmpId).attr('class'))){
							window.opener.$(tmpId).combobox('select',rowData.id);//可触发原有js的onSelect事件 
						}else if(window.opener.$(tmpId).attr('class')&&/combotree/ig.test(window.opener.$(tmpId).attr('class'))){
							window.opener.$(tmpId).combotree('setValue', rowData.id);
							window.opener.$(tmpId).combotree('hidePanel');
						}else{
							if('function' === typeof window.opener.popWinDeptCallBackFn){
								window.opener.popWinDeptCallBackFn(rowData);
							}else{
								window.opener.$(tmpId).val(rowData.encode);
								window.opener.$(tmpName).textbox("setValue",rowData.deptName);
								window.opener.$(tmpId).change();
							}
						}
			    		window.close();
					}
				}    
			});	
			bindEnterEvent('queryName',searchFrom,'easyui');
		});
		
		/*查询
		*多个条件组合成一个条件查询 
		*/
		function searchFrom(){ 
	   		var queryName = $('#queryName').val();
	   		$('#list').datagrid('load',{
					queryName:queryName
				
			});	
		}
	</script>
</html>
