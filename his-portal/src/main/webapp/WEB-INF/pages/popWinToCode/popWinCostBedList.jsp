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
										关键字： <input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" />
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div style="padding: 0px 5px 5px 5px;">
						<table id="list" style="width:100%" data-options="url:'queryDrugUndrug.action?classNameTmp=${classNameTmp}',method:'get',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'id',hidden:true"></th>
									<th data-options="field:'defaultprice',hidden:true"></th>
				    				<th data-options="field:'undrugGbcode'" style="width:20%">编码</th>
				    				<th data-options="field:'name'" style="width:20%">名称</th>
				    				<th data-options="field:'undrugPinyin'" style="width:20%">拼音码</th>
				    				<th data-options="field:'undrugWb'" style="width:20%">五笔码</th>
				    				<th data-options="field:'undrugInputcode'" style="width:20%">自定义码</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var data = "${textId}".split(",");
		var textId = "";
		var undrug = "";
		var chargeUnitprice = "";
		//加载页面
		$(function(){
			var winH=$("body").height();
			$('#p').height(winH-78-30-27-2);
			$('#treeDiv').height(winH-78-30-27-2);
			$('#list').height(winH-78-30-27-22);
			
			var id="${id}"; //存储数据ID
			//添加操作按钮
			$('#list').datagrid({
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
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
	            	 for(var i = 0; i < data.length; i++){
	         			textId = data[0];
	         			undrug= data[1];
	         			chargeUnitprice= data[2];
	         		}
					if(getIdUtil("#list").length!=0){
						var tmpId = "#"+textId.trim();
						var undrug = "#"+undrug.trim();
						var chargeUnitprice = "#"+chargeUnitprice.trim();
						window.opener.$(tmpId).combogrid('setValue',rowData.name);
						window.opener.$(undrug).val(rowData.encode);
						window.opener.$(chargeUnitprice).textbox('setValue',rowData.defaultprice);
			    		window.close();
					}
				}    
			});	
			bindEnterEvent('queryName',searchFrom,'easyui');
		});
	   /*
		*查询多个条件组合成一个条件查询 
		*/
		function searchFrom(){ 
	   		var queryName = $('#queryName').val();
	   		$('#list').datagrid({
				url:'<%=basePath%>popWin/popWinCost/queryDrugUndrug.action',
				method:'post',
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				queryParams: {
					nameParam: queryName,
					nameTmp: classNameTmp
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
	</script>
</html>
