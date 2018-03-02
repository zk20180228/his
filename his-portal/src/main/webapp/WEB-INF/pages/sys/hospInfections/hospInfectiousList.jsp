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
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
</script>
<script type="text/javascript">
	var type='';
	$(function(){
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,100],
			onDblClickRow: function (rowIndex, rowData) {
					if(getIdUtil("#list").length!=0){
					$('#divLayout').layout('remove', 'east');
					var tempWinPath = "<c:url value='/publics/hospInfectious/viewInfectiousView.action'/>?id="+getIdUtil("#list")+"&save=true";
       				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -680) +',height='+ (screen.availHeight-370) +',scrollbars,resizable=yes,toolbar=no')
				   	}
				},
				onLoadSuccess:function(row, data){
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
					}}
			});
			bindEnterEvent('patientName',searchFrom,'easyui');
			$('#reportTy').combotree({
				data:[{"id":1,"text":"初次报告"},{"id":2,"text":"订正报告","iconCls":"icon-book","state":"open","children":[{"id":3,"text":"变更诊断"},{"id":4,"text":"死亡"},{"id":5,"text":"填卡错误"}]}],
				width:150,
				multiple:false,
				onChange: function(node){
					type=node;
				}
			});
		});
		
		function add(){
			//添加datagrid事件及分页
			var tempWinPath = "<c:url value='/publics/hospInfectious/addHospInfectious.action'/>?"+"&save=false";
			window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -680) +',height='+ (screen.availHeight-370) +',scrollbars,resizable=yes,toolbar=yes')
		}
		
		function edit(){
			if(getIdUtil("#list").length!=0){
				var tempWinPath = "<c:url value='/publics/hospInfectious/viewInfectious.action'/>?id="+getIdUtil("#list")+"&save=false";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -680) +',height='+ (screen.availHeight-370) +',scrollbars,resizable=yes,toolbar=yes')
			}
			
		}
		
		function del(){
			if(getIdUtil("#list").length!=0){
				//选中要删除的行
				var idss = $('#list').datagrid('getChecked');
				if (idss.length > 0) {//选中几行的话触发事件	                        
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
						if (res) {
							var ids = '';
							for ( var i = 0; i < idss.length; i++) {
								if (ids != '') {
									ids += ',';
								}
								ids += idss[i].id;
							};
							$.ajax({
								url : "<c:url value='/publics/hospInfectious/delInfectious.action'/>?ids="+ids,
								type : 'post',
								success : function() {
									$.messager.alert('提示','删除成功');
									setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
									$("#list").datagrid("reload");
								}
							});
							for ( var i = 0; i < idss.length; i++) {
								var index = $('#list').datagrid('getRowIndex', idss[i]);//获取某行的行号
								$('#list').datagrid('deleteRow', index); //通过行号移除该行
							};
						}
					});
				}
			}
		}
		
		function reloads(){
			$("#list").datagrid('uncheckAll');
			//实现刷新栏目中的数据
			$("#list").datagrid('reload');
		}
		
		//查询
		function searchFrom(){ 
			var patientName =$.trim($('#patientName').val());
			$('#list').datagrid('load', {
				patientName : patientName,
				reportTy : type
			});
		}
	
		function formatreportNo(val,row) {
			if (val != null) {
				if (val==1) {
					return "初次报告";
				}
				if (val==2) {
					return "订正报告";
				}
				if (val==3) {
					return "变更诊断";
				}
				if (val==4) {
					return "死亡";
				}
				if (val==5) {
					return "填卡错误";
				}
			}
		}
		//清除搜索条件
		function clearSelect(){
			$('#reportTy').combotree('setValue','');
			$('#patientName').textbox('setValue','');
			searchFrom();
		}
		//性别渲染
		function sexRend(value){
			return sexMap.get(value);
		}
		
		
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',split:false,title:'医院感染患者列表',border:false">
			<div id="p" style="width:90%;height:27px;margin-top:auto;margin-bottom:auto;padding:5px;">
				<form id="editForm" >
					类别:<input id="reportTy" name="reportTy"/>
					条件:<input style="width:150px" data-options="prompt:'请输入姓名'" class="easyui-textbox" ID="patientName" name="infectious.patientName" />
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
						<a href="javascript:void(0)" onclick="clearSelect()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</form>
			</div>
			<div id="q" style="width:100%;height:95%;">
				<table id="list" data-options="url:'${pageContext.request.contextPath}/publics/hospInfectious/querylistHospInfectious.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th  field="ck" checkbox="true" ></th>
							<th data-options="field:'report_type',formatter: formatreportNo" style="width: 20%">
								报卡类别
							</th>
							<th data-options="field:'patient_name'" style="width: 16%">
								姓名
							</th>
							<th data-options="field:'work_place'" style="width: 20%">
								工作单位
							</th>
							<th data-options="field:'report_sex',formatter: sexRend" style="width: 13%">
								性别
							</th>
						</tr>
					</thead>
				</table>
			</div>
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
		<a href="javascript:void(0)" onclick="reloads()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>