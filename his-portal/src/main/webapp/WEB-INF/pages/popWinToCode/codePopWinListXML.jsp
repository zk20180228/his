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
	<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
					<div style="padding: 5px 5px 0px 5px;">
						<form id="search" method="post">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 320px;" nowrap="nowrap">
										关键字：
									          <input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" />
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div style="padding: 0px 5px 5px 5px;">
					<input id="typeId" type="hidden" value="${type}"> 
					<table id="list" style="width:100%" data-options="url:'<%=basePath%>popWin/popWinCode/queryCodePopWin.action?type=${type}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
			    				<th data-options="field:'encode'" style="width:7%">代码</th>
			    				<th data-options="field:'name'" style="width:20%">名称</th>
			    				<th data-options="field:'pinyin'" style="width:10%">拼音码</th>
			    				<th data-options="field:'wb'" style="width:7%">五笔码</th>
			    				<th data-options="field:'inputCode'" style="width:7%">自定义码</th>
			    				<th data-options="field:'description'" style="width:7%">说明</th>
			    				<th data-options="field:'order'" style="width:7%">排序</th>
			    				<th data-options="field:'hospital'" style="width:10%">适用医院</th>
			    				<th data-options="field:'nonhospital'" style="width:10%">不适用医院</th>
								<th data-options="field:'canselect'" formatter="formatCheckBox">可选标志</th>
								<th data-options="field:'isdefault'" formatter="formatCheckBox">默认标志</th>
							</tr>
						</thead>
					</table>
				</div>
				</div>
			</div>
</div>
</body>
	<script type="text/javascript">
	var type = "${type}";
	var textId= "${textId}";
	var textName= "${textName}";
	//加载页面
		$(function(){
			var winH=$("body").height();
			$('#p').height(winH-78-30-27-2);
			$('#treeDiv').height(winH-78-30-27-2);
			$('#list').height(winH-78-30-27-22);
			
			var id="${id}"; //存储数据ID
			//添加操作按钮
			$('#list').datagrid({
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
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
			    		//用于判断是否为职务。因为职务的下拉框数据不对，所以就加了个隐形框。一个用于显示，一个用于向后台传值
			    		if(textId=="post"){
			    			window.opener.$(tmpId).combobox('setValue',rowData.name);
							window.opener.$(tmpId+'hidden').val(rowData.id);
				    		window.close();
			    		}else if(window.opener.$(tmpId).attr('class')&&/combobox/ig.test(window.opener.$(tmpId).attr('class'))){
			    			var type = $('#typeId').val();
			    			if(type!=null&&type!=''){
			    				if(type=='0'){
			    					window.opener.$(tmpId).combobox('setText',rowData.name);
			    				}else if(type=='1'){
			    					window.opener.$(tmpId).combobox('select',rowData.encode);
			    				}else if(type=='2'){
			    					window.opener.$(tmpId).combobox('select',rowData.name);
			    				}else{
			    					window.opener.$(tmpId).combobox('setValue',rowData.encode);
			    				}
			    			}else{
			    				window.opener.$(tmpId).combobox('setValue',rowData.id);
			    			}
			    		}else{
							if('function' === typeof window.opener.popWinCommCallBackFn){
								window.opener.popWinCommCallBackFn(rowData);
							}else{
								window.opener.$(tmpId).val(rowData.id);
								window.opener.$(tmpName).textbox("setValue",rowData.name);
								window.opener.$(tmpId).change();
							}
						}
			    		window.close();
						
					} 
	            	 
				}    
			});
			bindEnterEvent('queryName',searchFrom,'easyui');
		});
		//格式复选框
		function formatCheckBox(val,row){
		  if(val==1){
			return '是';
		  }else{
			return '否';
		  }
		}
		/*查询
		*多个条件组合成一个条件查询 
		*
		*
		*/
		function searchFrom(){ 
	   		var queryName = $('#queryName').textbox('getValue');
	   		$('#list').datagrid({
				url:'<%=basePath%>popWin/popWinCode/queryCodePopWin.action?queryNameParam='+encodeURI(encodeURI(queryName))+"&type="+type,
				method:'post',
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
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
	             }] 
			});	
		}
		//回车键
		function keyDown(){  
			if (event.keyCode == 13){  //目前只支持IE
			    event.returnValue=false;  
			    event.cancel = true;  
			    searchFrom();  
			}  
		} 	
	</script>
</html>
