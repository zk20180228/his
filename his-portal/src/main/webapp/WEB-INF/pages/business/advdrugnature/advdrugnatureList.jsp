<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>长期限制性药品维护</title>
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript">
		var hoslist=""; 
		//加载页面
		$(function(){
			//存储数据ID
			var id="${id}"; 
			
			//医院集合map
			$.ajax({
				url: '<%=basePath %>inpatient/advdrugnatrue/getHospitalMap.action',
				type:'post',
				success: function(data) {
					hoslist = data;
				}
			});
			
			//添加操作按钮
			$('#list').datagrid({
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				onLoadSuccess: function (data) {//默认选中
					//分页工具栏作用提示
					var pager = $(this).datagrid('getPager');
					var aArr = $(pager).find('a');
					var iArr = $(pager).find('input');
					$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
					for(var i=0;i<aArr.length;i++){
						$(aArr[i]).tooltip({
							content:toolArr[i],
							hideDelay:1
						});
						$(aArr[i]).tooltip('hide');
					}
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
				  if(value.id == id){
				     $("#list").datagrid("checkRow", index);
				  }
			    });
			    },
			    toolbar: '#advtoolbarId',
	             //双击查看
	             onDblClickRow: function (rowIndex, rowData) {
					if(getIdUtil("#list").length!=0){
						 AddOrShowEast('EditForm','${pageContext.request.contextPath}/inpatient/advdrugnatrue/viewCodeAdvdrugnature.action?id='+getIdUtil("#list"));
					}
				},
				onBeforeLoad:function(){
					$("#list").datagrid("clearChecked");
					$("#list").datagrid("clearSelections");
				}
			});
			bindEnterEvent('queryName',searchFrom,'easyui');
			bindEnterEvent('choHosp',searchFrom,'easyui');
		});
		
		/**
		* @Description 格式化可选标志复选框
		* @author    tangfeishuai
		* @version   1.0 
		* @CreateDate 2016-04-12
		* @param1 val
		* @param2 row
		* @return 是  否
		*/
		function formatCheckBox(val,row){
		  if(val==1){
			return '是';
		  }else{
			return '否';
		  }
		}
		
		/**
		* @Description 适用医院渲染
		* @author    tangfeishuai
		* @version   1.0 
		* @CreateDate 2016-04-12
		* @param1 val
		* @param2 row
		*/
		
		function funHospital(value,row,index){
			if(value!=null&&value!=''){
				return hoslist[value];
			}else{
				return value;
			}
		}
	
		
		
		/**
		* @Description 格式化可选标志复选框
		* @author    tangfeishuai
		* @version   1.0 
		* @CreateDate 2016-04-12
		* @param parameter
		* @return id
		*/	
		function getId(parameter){
			var row = $("#list").datagrid("getSelections");  
			var i = 0;   
			if (parameter=='single'){  //获得单个id
			    if (row.length<1){
				    $.messager.alert("操作提示","请选择一条记录！");
				  	return null;
				} else if (row.length>1){
					$.messager.alert("操作提示","只能选择一条记录！");
				    return null;
				} else { 
				    var id = ""; 
					for(i;i<row.length;i++){    
					  	id += row[i].id; 
					    return id;
					}
				  	} 	
			 }else if (parameter=='plurality'){  //获得多个id
				   if (row.length<1){
					   $.messager.alert("操作提示","请至少选择一条记录！");
				       return null;
				   } else {  
				       var ids = ""; 
					   for(i;i<row.length;i++){   
					  	  ids += row[i].id+","; 
					   }
					  return ids;
				  } 
			}else if (parameter=='notNull'){  //至少获得一个id
				  var id = ""; 
				  if(row.length<1){  //如果没有选择数据，默认选中第一行数据
				    $('#list').datagrid('selectRow', 0);
				    var row = $("#list").datagrid("getSelections");  
				  }
				  id += row[0].id; 
				  return id;
			} else {
				 $.messager.alert("操作提示","参数无效！");
				return null;
			}
		}
		
		/**
		* @Description 动态添加标签页
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		* @param1 title 标签名称
		* @param title 跳转路径
		* @return 新的面板 panel
		*/
		function AddOrShowEast(title,url){
			//获取右侧收缩面板
			var eastpanel=$('#panelEast'); 
			//判断右侧收缩面板是否存在
			if(eastpanel.length>0){ 
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','east').panel({
                          href:url 
                   });
			} else {
				//打开新面板
				$('#divLayout').layout('add', {
					region : 'east',
					width : 580,
					split : true,
					href : url,
					closable : true,
					border : false
				});
			}
		}
		
	   /**
		* @Description 根据文本框内容模糊查询
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		* @param queryName 文本框内容
		* @param title 跳转路径
		* @return list 数据表格 
		*/
	   	function searchFrom(){ 
	   		 var queryName = $.trim($('#queryName').textbox('getValue'));
	   		 var choHosp = $.trim($('#choHosp').combobox('getValue'));
			 $('#list').datagrid('load', {
				 name : queryName,
				 hospital:choHosp
			  });
		}
	   
	   /**
		* @Description 回车键查询该方法目前只支持IE
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-12
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		*/
		function keyDown(){  
			if (event.keyCode == 13){  
			    event.returnValue=false;  
			    event.cancel = true;  
			    searchFrom();  
			}  
		} 	
	   
		// 列表查询重置
		function searchReload() {
			$('#choHosp').combobox('setValue','');
			$('#queryName').textbox('setValue','');
			searchFrom();
		}
		
		//添加
		function btnAdd(){
			AddOrShowEast('EditForm', '${pageContext.request.contextPath}/inpatient/advdrugnatrue/addCodeAdvdrugnature.action');
		}
		
		//修改
		function btnEdit(){
			if(getIdUtil("#list")!=null){
				AddOrShowEast('EditForm', '${pageContext.request.contextPath}/inpatient/advdrugnatrue/updateCodeAdvdrugnature.action?id='+getIdUtil("#list"));
			}
		}
		
		//删除
		function btnDelete(){
			//选中要删除的行
	        var rows = $('#list').datagrid('getChecked');
	        //选中几行的话触发事件	
            if (rows.length > 0) { 
            	//提示是否删除
			   $.messager.confirm('确认', '确定要删除选中信息吗?', function(res){
				  if (res){
					var ids = '';
					  for(var i=0; i<rows.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += rows[i].id;
					 };
					$.ajax({
						url: '${pageContext.request.contextPath}/inpatient/advdrugnatrue/delCodeAdvdrugnature.action?id='+ids,
						type:'post',
						success: function() {
							$.messager.alert("操作提示","删除成功");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$("#list").datagrid("reload");
						}	
					});
				  }
              });
            }else{
         		$.messager.alert("操作提示","请选择一条或多条记录！");
         	} 
		}
		
		//刷新
		function btnReload(){
			//实现刷新
            $("#list").datagrid("reload");
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.layout-split-east {
    border-left: 0px; 
}
table.honry-table td{
	border-left:0px;
}
.panel-body-noheader{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; overflow-y: auto;">
		<div data-options="region:'north',border: false"  style="padding: 5px 5px 0px 5px;height: 40px;">
			<form id="search" method="post">
				<table
					style="width: 100%; border: false;">
					<tr>
						<td style="width: 320px;" nowrap="nowrap">
							关键字：<input class="easyui-textbox" data-options="prompt:'代码,名称,拼音,五笔'" id="queryName" name="queryName" onkeydown="KeyDown(0)"/>
							&nbsp;选择医院：<input id="choHosp" class="easyui-combobox"  style="width: 200px" data-options="valueField:'code',textField:'name',url:'<c:url value='/inpatient/advdrugnatrue/queryHospital.action'/>'" ></input>
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border: false"  style="height:90%;">		
			<table id="list" style="width:100%" data-options="url:'${pageContext.request.contextPath}/inpatient/advdrugnatrue/queryCodeAdvdrugnature.action',fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
	    				<th data-options="field:'encode'" style="width:6%">代码</th>
	    				<th data-options="field:'name'" style="width:10%">名称</th>
	    				<th data-options="field:'hospital',formatter:funHospital" style="width:20%">适用医院</th>
	    				<th data-options="field:'canselect'" formatter="formatCheckBox">可选标志</th>
						<th data-options="field:'isdefault'" formatter="formatCheckBox">默认标志</th>
	    				<th data-options="field:'pinyin'" style="width:10%">拼音码</th>
	    				<th data-options="field:'wb'" style="width:10%">五笔码</th>
	    				<th data-options="field:'order'" style="width:10%">排序</th>
						<th data-options="field:'description'" style="width:20%">说明</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="advtoolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a  href="javascript:btnAdd();" class="easyui-linkbutton" data-options="iconCls:'icon-add' ,plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a  href="javascript:btnEdit();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a  href="javascript:btnDelete();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			<a  href="javascript:btnReload();" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
	</div>
</body>
</html>
