<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>频次管理</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//加载页面
			var setCodeUseagedata = "";//用法 数据源
			$(function(){
			var winH=$("body").height();
			$('#list').height(winH-78-30-27-26);
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$('#list').datagrid('checkRow', index);
			            	}
			            });
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
			        },onDblClickRow: function (rowIndex, rowData) {//双击查看
							if(getIdUtil('#list').length!=0){
						   	    AddOrShowEast('EditForm',"<c:url value='/baseinfo/frequency/viewFrequency.action'/>?id="+getIdUtil('#list'));
						   	}
					},
					onBeforeLoad:function(){
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					}
				});
					bindEnterEvent('queryName',searchFrom,'easyui');
				});
				$.ajax({
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=useage", 
					type:'post',
					success: function(CodeUseageDatas) {
						setCodeUseagedata = CodeUseageDatas;
					}
				});	
				/**
				 * 格式化复选框
				 * @author  liujinliang
				 * @date 2015-5-26 9:25
				 * @version 1.0
				 */
				function formatCheckBox(val,row,index){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}				
				function add(){
					AddOrShowEast('添加',"<c:url value='/baseinfo/frequency/addFrequency.action'/>");
					$('#roleWins').dialog('close'); 
					$('#roleWins').dialog('destroy');
				}
				function edit(){
					  if(getIdUtil("#list")!= null){
	                      	AddOrShowEast('编辑',"<c:url value='/baseinfo/frequency/editFrequency.action'/>?id="+getIdUtil("#list"));
	                    	$('#roleWins').dialog('close'); 
	            			$('#roleWins').dialog('destroy');
			   		  }
				}
				function del(){
				 //选中要删除的行
                   var iid = $('#list').datagrid('getChecked');
                  	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '您当前选中【'+iid.length+'】条数据,确定要全部删除吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							$.ajax({
								url: "<c:url value='/baseinfo/frequency/delFrequency.action'/>?id="+ids,
								type:'post',
								success: function() {
									$.messager.alert('提示信息','删除成功');
								$('#list').datagrid('reload');
								}
							});										
						}
                       });
                   }else{
                   	$.messager.alert('警告！','请选择要删除的信息！','warning');
                   	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                   }
				}
	         	function reload(){
					//实现刷新栏目中的数据
					 $("#list").datagrid("reload");
				}
	         	
				function searchReload() {
					$('#queryName').textbox('setValue','');
					searchFrom();
				}
				/**
				 * 查询
				 * @author  sunshuo
				 * @date 2015-05-21
				 * @modifier liujl
				 * @modifiedTime 2015-6-18 17:41
				 * @modifiedRmk 多个查询条件用一个输入框
				 * @version 1.0
				 */
				function searchFrom() {
					var queryName = $.trim($('#queryName').val());
					$('#list').datagrid('load', {
						name : queryName
					});
				}
				/**
				 * 动态添加LayOut
				 * @author  liujl
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-05-21
				 * @modifiedTime 2015-6-18
				 * @modifier liujl
				 * @version 1.0
				 */
				function AddOrShowEast(title, url) {
					var eastpanel=$('#panelEast'); //获取右侧收缩面板
					if(eastpanel.length>0){ //判断右侧收缩面板是否存在
						//重新装载右侧面板
				   		$('#divLayout').layout('panel','east').panel({
	                           href:url 
	                    });
					}else{//打开新面板
						$('#divLayout').layout('add', {
							region : 'east',
							width : '31%',
							split : true,
							href : url,
							closable : true
						});
					}
				}
				//用法
				function formatdescription(val,row,index){
					for(var i=0;i<setCodeUseagedata.length;i++){
						if(val==setCodeUseagedata[i].encode){
							return setCodeUseagedata[i].name;
						}
					}
				}	
				function formatunit(val,row,index){
					if(val == 'D'){
						return "天";
					}if(val == 'W'){
						return "周";
					}if(val == 'H'){
						return "小时";
					}if(val == 'ONCE'){
						return "仅一次";
					}if(val == 'T'){
						return "必须时";
					}if(val == 'M'){
						return "分钟";
					}
					
					for(var i=0;i<setCodeUseagedata.length;i++){
						if(val==setCodeUseagedata[i].encode){
							return setCodeUseagedata[i].name;
						}
					}
				}	
	</script>
</head>
<body style="margin: 0px;padding: 0px">
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px;">
		<table >
			<tr>
				<td>
					<input type="text" id="queryName" name="queryName"  class="easyui-textbox" data-options="prompt:'名称、代码、辅助码'" />
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',border:true">
		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/baseinfo/frequency/queryFrequency.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true" ></th>
					<th data-options="field:'encode'"width="9%">代码</th>
					<th data-options="field:'name'"width="9%">名称</th>
					<th data-options="field:'pinyin'"width="9%">拼音码</th>
					<th data-options="field:'wb'"width="9%">五笔码</th>
					<th data-options="field:'inputCode'"width="9%">自定义码</th>
					<th data-options="field:'useMode',formatter:formatdescription"width="9%">用法</th>
					<th data-options="field:'frequencyUnit',formatter:formatunit"width="9%">单位</th>
					<th data-options="field:'frequencyNum'"width="9%">频次数目</th>
					<th data-options="field:'frequencyTime'"width="9%">频次次数</th>
					<th data-options="field:'period'" width="9%">时间点</th>
					<th data-options="field:'description'"width="9%">说明</th>
					<th data-options="field:'order'"width="8%">排序</th>
					<th data-options="field:'alwaysFlag',formatter:formatCheckBox" width="9%">持续标志</th>
					<th data-options="field:'canSelect',formatter:formatCheckBox" width="9%">可选标志</th>
					<th data-options="field:'isDefault',formatter:formatCheckBox" width="9%">默认标志</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>