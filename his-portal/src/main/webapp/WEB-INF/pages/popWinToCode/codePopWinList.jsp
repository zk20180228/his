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
										<input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" data-options="prompt:'名称,代码,拼音,五笔,自定义'"/>
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
			    				<th data-options="field:'encode'" style="width:7%">代码</th>
			    				<th data-options="field:'name'" style="width:7%">名称</th>
			    				<th data-options="field:'pinyin'" style="width:7%">拼音码</th>
			    				<th data-options="field:'wb'" style="width:7%">五笔码</th>
			    				<th data-options="field:'inputCode'" style="width:7%">自定义码</th>
			    				<th data-options="field:'description'" style="width:7%">说明</th>
			    				<th data-options="field:'order'" style="width:7%">排序</th>
			    				<th data-options="field:'hospital'" style="width:7%">适用医院</th>
			    				<th data-options="field:'nonhospital'" style="width:7%">不适用医院</th>
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
	var textId= "${textId}";
	var textName= "${textName}";
	var type="${type}";
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
				url:'queryCodePopWin.action',
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
					queryName:null,
					type:type
				},
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
					if(getIdUtil("#list").length!=0){
						 //AddOrShowEast('EditForm','viewCodeCountry.action?id='+getIdUtil("#list"));
						var tmpId ="#"+textId;
						var tmpName ="#"+textName;
						var s=rowData.name;
						if(window.opener.$(tmpId).attr("class")&&/combotree/ig.test(window.opener.$(tmpId).attr("class"))){
			    			window.opener.$(tmpId).combotree('setValue',rowData.encode);
			    		}else if(window.opener.$(tmpId).attr("class")&&/combogrid/ig.test(window.opener.$(tmpId).attr("class"))){
			    			window.opener.$(tmpId).combogrid('setValue',rowData.encode);//可触发原有js的onSelect事件 
			    		}else if(window.opener.$(tmpId).attr("class")&&/combobox/ig.test(window.opener.$(tmpId).attr("class"))){
			    			window.opener.$(tmpId).combobox('select',rowData.encode);
			    		}else if(window.opener.$(tmpId).attr("class")&&/textbox/ig.test(window.opener.$(tmpId).attr("class"))){
			    			window.opener.$(tmpName).textbox("setText",s);
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
	   		var queryName = $('#queryName').val();
	   		$('#list').datagrid('load',{queryName:queryName,type:type});
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
