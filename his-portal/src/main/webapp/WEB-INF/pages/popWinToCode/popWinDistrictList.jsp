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
					<div style="padding: 5px 5px 5px 5px;height: 5%;margin-bottom: 5px;" data-options="fit:true">
						<form id="search" method="post" data-options="fit:true">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 30%;margin-left: 5px;" nowrap="nowrap">
										关键字： <input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" />
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
			    				<th data-options="field:'cityCode'" style="width:10%">代码</th>
			    				<th data-options="field:'cityName'" style="width:20%">名称</th>
			    				<th data-options="field:'pinyin'" style="width:15%">拼音码</th>
			    				<th data-options="field:'wb'" style="width:15%">五笔码</th>
			    				<th data-options="field:'defined'" style="width:15%">自定义码</th>
			    				<th data-options="field:'shortname'" style="width:10%">简称</th>
			    				<th data-options="field:'ename'" style="width:15%">英文名称</th>
			    				
							</tr>
						</thead>
					</table>
				</div>
				</div>
			</div>
</div>
</body>
	<script type="text/javascript">
	var textId= "${param.textId}";
	var level= "${param.level}";
	var parentId="${param.parentId}"
	var id="${param.id}"
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
				pagination:true,
				url:'queryDistrictPopWin.action',
				method:'post',
				rownumbers:true,
				idField: 'id',
				striped:true,
				border:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				queryParams:{
					level:level,
					parentId:parentId,
					id:id
				},
				pageList:[20,30,50,80,100],pageSize:20,
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
	            	 var tmpId ="#"+textId;
	            	 if(window.opener.$(tmpId).attr("class")&&/combobox/ig.test(window.opener.$(tmpId).attr("class"))){
			    			window.opener.$(tmpId).combobox('select',rowData.cityCode);
		    		}else{
						if('function' === typeof window.opener.popWinDistrictCallBackFn){
							window.opener.popWinDistrictCallBackFn(rowData);
						}else{
							window.opener.$(tmpId).val(rowData.cityCode);
							window.opener.$(tmpName).textbox("setValue",rowData.cityName);
							window.opener.$(tmpId).change();
						}
					}
					window.close();
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
	   		var queryName = $('#queryName').val();
	   		$('#list').datagrid({
				url:'<%=basePath%>/popWin/popWinDistrict/queryDistrictPopWin.action',
				method:'post',
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				queryParams:{
					queryName:queryName,
					level:level,
					parentId:parentId,
					id:id
				},
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
