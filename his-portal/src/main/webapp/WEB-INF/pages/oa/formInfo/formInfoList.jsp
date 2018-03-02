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
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false" style="height:40px;padding-top:5px;padding-left:5px;">
				<input id="search" name="search" class="easyui-textbox" data-options="prompt:'表单编码，表单名称'" style="width:240px;height:25px"/>
				<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			</div>
			<div data-options="region:'center'">
				<table id="ed">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'formCode',width:180,halign:'center'">表单编码</th>
							<th data-options="field:'formName',width:550,halign:'center'">表单名称</th>
<!-- 							<th data-options="field:'formTname',width:300,halign:'center'">表名</th> -->
<!-- 							<th data-options="field:'formState',width:150,formatter:forState,halign:'center'">表单状态</th> -->
							<th data-options="field:'stop_flg',width:150,formatter:forStopFlg,halign:'center'">是否停用</th>
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
			<shiro:hasPermission name="${menuAlias}:function:disable">
				<a href="javascript:void(0)" onclick="stop()" class="easyui-linkbutton" data-options="iconCls:'icon-lock',plain:true">停用</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:enable">
				<a href="javascript:void(0)" onclick="enable()" class="easyui-linkbutton" data-options="iconCls:'icon-lock_open',plain:true">启用</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="loadDatagrid()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<script type="text/javascript">
		$(function(){
			$('#ed').datagrid({  
	            fit:true,
	            rownumbers:true,
	            striped:true,
	            border:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
	            pagination:true,
	            pageNumber:1,
	            pageSize:20,
	            pageList:[20,30,50,100],
	            toolbar:'#toolbarId',
	            url: "<%=basePath%>oa/formInfo/queryFormInfoList.action",
	            onLoadSuccess: function(){
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
	            }
	        }); 
			bindEnterEvent('search',searchList,'easyui');//回车键查询	
		});
		
		//状态
		function forState(value,row,index){
			if(value==0){
				return '未使用';
			}else if(value==1){
				return '在用';
			}else{
				return '';
			}
		}
		
		//停用标记
		function forStopFlg(value,row,index){
			if(value==0){
				return '启用';
			}else if(value==1){
				return '停用';
			}else{
				return '';
			}
		}
		
		//查询
		function searchList(){
			var search = $('#search').textbox('getText');
			$('#ed').datagrid('load',{search:trim(search)});
		}
		
		//停用逻辑
		function stop(){
			var retMap = getIds(1);
			if('error'==retMap.get('resCode')){
				$.messager.alert('提示',(retMap.get('resSta')!=''?("表单编码：["+retMap.get('resSta')+"]的表单是[在用]状态无法停用,"):"")+retMap.get('resMsg'));
				return;
			}else{
				if(retMap.get('resSta')!=''&&retMap.get('resMsg')!=''){
					$.messager.defaults={
							ok:'确定',
							cancel:'取消',
							width:350,
							collapsible:false,
							minimizable:false,
							maximizable:false,
							closable:false
					};
					$.messager.confirm('提示',"表单编码：["+retMap.get('resSta')+"]的表单是[在用]状态无法停用,是否继续停用状态为[未使用]的表单", function(res){//提示是否删除
						if (res){
							stopFormInfo(retMap.get('resMsg'));
						}
					});
				}else{
					stopFormInfo(retMap.get('resMsg'));
				}
			}
		}
		
		//停用
		function stopFormInfo(search){
			$.ajax({ 
				url:'<%=basePath %>oa/formInfo/stopFormInfo.action',
				type:'post',
				data:{"search":search},
				success: function(dataMap) {
					if(dataMap.resCode=="success"){
						loadDatagrid();
					}
					$.messager.alert('提示',dataMap.resMsg);
				},
				error:function() {
					$.messager.alert('提示','请求失败!');
				}
			});
		}
		
		//启用
		function enable(){
			var retMap = getIds(2);
			if('error'==retMap.get('resCode')){
				$.messager.alert('提示',retMap.get('resMsg'));
				return;
			}
			$.ajax({ 
				url:'<%=basePath %>oa/formInfo/enableFormInfo.action',
				type:'post',
				data:{"search":retMap.get('resMsg')},
				success: function(dataMap) {
					if(dataMap.resCode=="success"){
						loadDatagrid();
					}
					$.messager.alert('提示',dataMap.resMsg);
				},
				error:function() {
					$.messager.alert('提示','请求失败!');
				}
			});
		}
		
		//删除逻辑
		function del(){
			var retMap = getIds(3);
			if('error'==retMap.get('resCode')){
				$.messager.alert('提示',(retMap.get('resSta')!=''?("表单编码：["+retMap.get('resSta')+"]的表单是[在用]状态无法删除,"):"")+retMap.get('resMsg'));
				return;
			}else{
				if(retMap.get('resSta')!=''&&retMap.get('resMsg')!=''){
					$.messager.defaults={
							ok:'确定',
							cancel:'取消',
							width:350,
							collapsible:false,
							minimizable:false,
							maximizable:false,
							closable:false
					};
					$.messager.confirm('提示',"表单编码：["+retMap.get('resSta')+"]的表单是[在用]状态无法删除,是否继续删除状态为[未使用]的表单", function(res){//提示是否删除
						if (res){
							delFormInfo(retMap.get('resMsg'));
						}
					});
				}else{
					delFormInfo(retMap.get('resMsg'));
				}
			}
		}
		
		//删除
		function delFormInfo(search){
			$.ajax({ 
				url:'<%=basePath %>oa/formInfo/delFormInfo.action',
				type:'post',
				data:{"search":search},
				success: function(dataMap) {
					if(dataMap.resCode=="success"){
						loadDatagrid();
					}
					$.messager.alert('提示',dataMap.resMsg);
				},
				error:function() {
					$.messager.alert('提示','请求失败!');
				}
			});
		}
		
		//获得未使用状态的信息
		function getIds(flg){
			var retMap = new Map();
			var msg = '';
			if(flg==1){
				msg = '停用';
			}else if(flg==2){
				msg = '启用';
			}else if(flg==3){
				msg = '删除';
			}else{
				retMap.put('resCode','error');
				retMap.put('resMsg','请求失败!');
				return retMap;
			}
			
			var rows = $('#ed').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				var search = '';
				var sta = '';
				for(var i=0;i<rows.length;i++){
					if(rows[i].formState==0){
						if(search!=''){
							search += ',';
						}
						search += rows[i].id;
					}else{
						if(sta!=''){
							sta += ',';
						}
						sta += rows[i].formCode;
					}
				}
				retMap.put('resSta',sta);
				if(search!=''){
					retMap.put('resCode','success');
					retMap.put('resMsg',search);
					return retMap;
				}else{
					retMap.put('resCode','error');
					retMap.put('resMsg','请选择需要'+msg+'的信息!');
					return retMap;
				}
			}else{
				retMap.put('resCode','error');
				retMap.put('resMsg','请选择需要'+msg+'的信息!');
				return retMap;
			}
		}
		
		//刷新表格
		function loadDatagrid(){
			$('#ed').datagrid('load');
		}
		
		//跳转到添加页面
		function add(){
			attWindow(null,'<%=basePath%>oa/formInfo/addFormInfo.action');
		}
		
		//跳转到编辑页面
		function edit(){
			var row = $('#ed').datagrid('getSelected');
	   		if(row==null){
	   			$.messager.alert("提示","请选择要修改的记录!");
	   			return;
	   		}
	   		if(row.formState==1){
	   			$.messager.alert("提示","只能修改未使用的表单!");
	   			return;
	   		}
	   		attWindow(row.id,'<%=basePath%>oa/formInfo/editFormInfo.action');
		}
		
		//以post方式打开窗口
		function attWindow(id,url){
			var id = id;
			var url = url;
			var name = '编辑自定义表单';
			var width = window.screen.availWidth-50;
			var height = 650;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-20-width)/2;
			if($("#winOpenFrom").length<=0){  
				var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId' name='formInfo.id'/></form>";
				$("body").append(form);
			} 
			$('#winOpenFromInpId').val(id);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom').prop('action',url);
			$("#winOpenFrom").submit();
		}
		
		//打开窗口
		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		
		</script>
	</body>
</html>