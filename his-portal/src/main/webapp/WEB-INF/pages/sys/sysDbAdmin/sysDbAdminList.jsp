<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<style type="text/css">
	.panel-noscroll{
		border-left:0;
	}
	</style>
	</head>
	<body  style="margin: 0px;padding: 0px;">
		<div id="divLayout"  class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'north',split:false" style="height:35px;border-top:0">
				<table id="search" style="width: 100%;">
					<tr>
						<td style="padding: 1px;">
							<input class="easyui-textbox" id="encode" name="tableName" data-options="prompt:'请输入表名称...'"/>
							<shiro:hasPermission name="${menuAlias}:function:query">
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFrom()"
									class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'" style="height:100%;border-top:0">
				<table id="list" style="width: 100%;"
					data-options="method:'post',selectOnCheck:false,fit:true,rownumbers:true,idField: 'id',striped:true,border:true,singleSelect:true,checkOnSelect:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],showRefresh:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th field="ck" checkbox="true"></th>
							<th
								data-options="field:'database', width : 100 ">
								数据库名
							</th>
							<th data-options="field:'tablespaceName', width : 100 ">
								表空间名
							</th>
							<th data-options="field:'tableName', width : 100 ">
								表名
							</th>
							<th
								data-options="field:'partitionName',width : 100 ">
								分区名
							</th>
							<th
								data-options="field:'columnName', width : 100 ">
								分区字段
							</th>
							<th
								data-options="field:'zoneType',width:100,formatter:forZoneType">
								分区类型
							</th>
							<th
								data-options="field:'highValue', width : 100 ">
								分区值
							</th>
						</tr>
					</thead>
				</table>
			</div>
		<div id="undrug"></div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-20130406015709810_easyicon_net_16',plain:true">刷新</a>
			<shiro:hasPermission name="${menuAlias}:function:initialization">
				<a href="javascript:void(0)" onclick="initDb()" class="easyui-linkbutton" data-options="iconCls:'icon-cd_edit',plain:true">初始化维护表</a>
				<a href="javascript:void(0)" onclick="initCache()" class="easyui-linkbutton" data-options="iconCls:'icon-cdr_edit',plain:true">初始化缓存</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>
			</shiro:hasPermission>
		</div>
	<script type="text/javascript">
	//加载页面
		$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			url:'<%=basePath%>baseinfo/sysDbAdmin/queryunSysDbAdmin.action?menuAlias=${menuAlias}',
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			}
	});
		bindEnterEvent('encode',searchFrom,'easyui');
	});
	 
	
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}
	//查询
	function searchFrom() {
		var encode = $('#encode').textbox('getValue');
		$('#list').datagrid('load', {
			tableNamequery: encode,
		});
	}
	
	/**  
	 *  
	 * 初始化分区维护表
	 * @Author：aizhonghua
	 * @CreateDate：2016-11-10 下午06:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-11-10 下午06:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function initDb(){
		$.ajax({
			url:"<%=basePath%>baseinfo/sysDbAdmin/initDb.action",
			type:'post',
			success:function(dataMap) {
				$.messager.alert('提示',dataMap.resCode);	
				if(dataMap.resMsg=='success'){
					$('#list').datagrid('load');
				}
			},
			error:function(){
				$.messager.alert('提示','请求失败！');	
			}
		});
	}
	
	/**  
	 *  
	 * 初始化缓存
	 * @Author：aizhonghua
	 * @CreateDate：2016-11-10 下午06:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-11-10 下午06:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function initCache(){
		$.ajax({
			url:"<%=basePath%>baseinfo/sysDbAdmin/initCache.action",
			type:'post',
			success:function(dataMap) {
				$.messager.alert('提示',dataMap.resCode);	
			},
			error:function(){
				$.messager.alert('提示','请求失败！');	
			}
		});
	}
	
	/**  
	 *  
	 * 编辑
	 * @Author：aizhonghua
	 * @CreateDate：2016-11-10 下午06:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-11-10 下午06:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function edit(){
		var row = $('#list').datagrid('getSelected');
		if(row!=null&&row!=''){
			AddOrShowEast('<%=basePath%>baseinfo/sysDbAdmin/editdbAdmin.action?id='+row.id);
		}else{
			$.messager.alert('提示','请选择需要编辑的信息');	
			close_alert();
		}
	}
	
	/**  
	 *  
	 * 分区类别渲染
	 * @Author：aizhonghua
	 * @CreateDate：2016-11-10 下午06:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-11-10 下午06:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function forZoneType(value,row,index){
		if(value!=null&&value!=''){
			return getZoneTypeMap().get(value);
		}else{
			return null;
		}
	}
	//动态添加LayOut
	function AddOrShowEast(url) {
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			//重新装载右侧面板
				$('#divLayout').layout('panel','east').panel({
					href:url 
				});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : '30%',
				split : false,
				href : url,
				closable : true
			});
		}
	}
</script>
	</body>
</html>