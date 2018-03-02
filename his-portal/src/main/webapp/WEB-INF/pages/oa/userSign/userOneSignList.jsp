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
	</head>
	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'north',split:false" style="height:40px;padding-top:5px;padding-left:5px;">
				<input id="search" name="search" class="easyui-textbox" data-options="prompt:'名称，拼音，笔码，自定义码'" style="width:220px;"/>
				<shiro:hasPermission name="${menuAlias}:function:query"> 
  					<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
  				</shiro:hasPermission>
			</div>   
			<div data-options="region:'center'">
				<table id="ed">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'signType',width:'6%',formatter:functionsignType">类型</th>
							<th data-options="field:'signCategory',width:'6%',formatter:functionsignCategory">分类</th>
							<th data-options="field:'userAccName',width:'15%'">分类范围</th>
							<th data-options="field:'signName',width:'8%'">名称</th>
							<th data-options="field:'signPinYin',width:'6%'">拼音码</th>
							<th data-options="field:'signWb',width:'6%'">五笔码</th>
							<th data-options="field:'signInputcode',width:'6%'">自定义码</th>
							<th data-options="field:'signDesc',width:'10%'">描述</th>
							<th data-options="field:'stop_flg',width:'10%',formatter:functionstop_flg">是否停用</th>
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
  				<a href="javascript:void(0)" onclick="stop()" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">停用</a>
  			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete"> 
  				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
  			</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<div id="confirmPassword" style="width: 350px;height:300px"> 
			<div style="margin:28px 50px;width: 180px;height: 30px">
				<input style="width:97%;" id="passwordSerc"  class="easyui-textbox" data-options="prompt:'请输入登录密码'" />
			</div>
			<div style="margin:0px 50px;width: 180px;height: 50px">
				<a id="submitPassword" href="javascript:submitPassword();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>&nbsp;&nbsp;
				<a href="javascript:void(0);" onclick="closePasswordLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			 </div>
		</div>
		<script type="text/javascript">
		var userAccount = "${userAccount}";
		$(function(){
			$('#ed').datagrid({  
	            fit:true,
	            rownumbers:true,
	            striped:true,
	            border:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
	            toolbar:'#toolbarId',
	            url: "<%=basePath%>oa/userSign/queryOneUserSignList.action",
	            queryParams:{userAccount:userAccount},
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
		
		function searchList(){
			var search = $('#search').textbox('getText');
			$('#ed').datagrid('load',{search:trim(search)});
		}
		//加载模式窗口
		function AdddilogModel(id,title,url,width,height) {
			$('#confirmPassword').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,
			    href: url,    
			    modal: true   
			});    
		}
		function add(){
			AddOrShowEast('<%=basePath %>oa/userSign/addOneUserSign.action');
		}
		
		function edit(){
			var row = $('#ed').datagrid('getSelected');
	   		if(row==null){
	   			$.messager.alert("提示","请选择要修改的记录!");
	   			return;
	   		}
	   		$('#passwordSerc').textbox('setValue','')
			AdddilogModel("confirmPassword","密码校验","",'280px','180px');
			/**
			 * 校验密码、密码校验成功则再次进行费用处理回车键查询
			 * @author  yeguanqun
			 * @date 2016-4-15
			 * @version 1.0
			 */	
		 	bindEnterEvent('passwordSerc',submitPassword,'easyui');
		}
		function submitPassword(){
			var row = $('#ed').datagrid('getSelected');
			var passwordSerc = $('#passwordSerc').val();
			if(passwordSerc==''||passwordSerc==null){
				$.messager.alert('提示',"密码不能为空！");	
				return;
			}
			$.ajax({
				url: '<%=basePath%>oa/userSign/getSignRow.action',
				data:{password:passwordSerc,signid:row.id},
				type:'post',
				success: function(data) {
					if(data.signPassword!=passwordSerc){
						$.messager.alert('提示',"密码输入错误,请重新输入！");	
						return;
					}else{
						closePasswordLayout();
				   		AddOrShowEast('<%=basePath %>oa/userSign/editOneUserSign.action?search='+row.id);
					}
				}
			});	
		}	
		function AddOrShowEast(url) {
			var eastpanel=$('#panelEast');
			if(eastpanel.length>0){
		   		$('#divLayout').layout('panel','east').panel({
                    href:url 
                });
			}else{
				$('#divLayout').layout('add', {
					region:'east',
					width:580,
					split:false,
					href:url,
					closable:true
				});
			}
		}
		function closePasswordLayout(){
			$('#confirmPassword').dialog('close');
		}
		function reload(){
			$('#ed').datagrid('reload');
		}
		function stop(){
			var rows = $('#ed').datagrid('getChecked');
			if(rows.length>0){
				var ids = "";
				for (var i = 0; i < rows.length; i++) {
					if(rows[i].stop_flg==1){
						continue;
					}
					if(ids==""){
						ids = rows[i].id;
					}else{
						ids += ","+rows[i].id;
					}
				}
				if(ids == ""){
					$.messager.alert('提示',"请选择的记录都已停用！");
					return;
				}
				$.ajax({
					url: '<%=basePath%>oa/userSign/stopSign.action',
					data:{signid:ids},
					type:'post',
					success: function(data) {
						$.messager.alert('提示',data.resMsg);	
						$('#ed').datagrid('reload');
					}
				});	
			}else{
				$.messager.alert('提示',"请选择停用的记录！");	
			}
		}
		function del(){
			var rows = $('#ed').datagrid('getChecked');
			if(rows.length>0){
				var ids = "";
				for (var i = 0; i < rows.length; i++) {
					if(ids==""){
						ids = rows[i].id;
					}else{
						ids += ","+rows[i].id;
					}
				}
				$.ajax({
					url: '<%=basePath%>oa/userSign/deleteSign.action',
					data:{signid:ids},
					type:'post',
					success: function(data) {
						$.messager.alert('提示',data.resMsg);	
						$('#ed').datagrid('reload');
					}
				});	
			}else{
				$.messager.alert('提示',"请选择删除的记录！");	
			}
		}
		function functionstop_flg(value,row,index){
			if(value==0){
				return "在用";
			}else if(value==1){
				return "停用";
			}
		}
		function functionsignType(value,row,index){
			if(value==1){
				return "电子签名";
			}else if(value==2){
				return "电子签章";
			}
		}
		function functionsignCategory(value,row,index){
			if(value==1){
				return "用户";
			}else if(value==2){
				return "角色";
			}else if(value==3){
				return "职务";
			}else if(value==4){
				return "科室";
			}
		}
		</script>
	</body>
</html>