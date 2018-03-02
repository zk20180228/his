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
		<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	</head>
	<body>
		<div id="layoutId" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false" style="height:45px;">
				<table style="padding-left:5p;padding-top:5px;">
					<tr>
						<td>用户:</td>
						<td><input id="userId"></td>
						<td><a href="javascript:void(0)" onclick="selectMenuDateButton();" class="easyui-linkbutton" data-options="iconCls:'icon-confirm'">选择权限</a></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'">
				<table id="gridId">
					<thead frozen="true">
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'sign',hidden:true,width:200">标记</th>
							<th data-options="field:'userAccount',hidden:true,width:200">用户账户</th>
							<th data-options="field:'userName',hidden:true,width:200">用户名称</th>
							<th data-options="field:'menuId',hidden:true,width:200">栏目</th>
							<th data-options="field:'menuAlias',hidden:true,width:200">栏目别名</th>
							<th data-options="field:'menuName',width:200">栏目名</th>
							<th data-options="field:'jurisType',hidden:true,width:80">权限类型编码</th>
							<th data-options="field:'jurisTypeName',width:80">权限类型</th>
						</tr>
					</thead>
					<thead>
						<tr>
							<th data-options="field:'jurisCode',hidden:true,width:500">权限编码</th>
							<th data-options="field:'jurisCodeName',width:600">权限名</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias }:function:delete">
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias }:function:author">
					<a href="javascript:void(0)" onclick="juris()" class="easyui-linkbutton" data-options="iconCls:'icon-house_link',plain:true">授权</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias }:function:edit">
					<a href="javascript:void(0)" onclick="edit();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改授权</a>
				</shiro:hasPermission>
			</div>
		</div>
		<script type="text/javascript">
		var menuListMapDate=null;//页面返回选中的授权栏目数据
		var deptAndAreaDateCode=null;//选中的授权科室
		var deptAndAreaDateName=null;//选中的授权科室
		var jurisTypeName=null;//授权类型
		var jurisTypeCode=null;//授权类型
		var isMobileSyn=null;//是否同步移动端授权
		var pcAndMobileMap=null;//pc端栏目和移动端栏目对应关系
		var menuMap=null;//栏目map
		var isJuris = false;
		//加载栏目对应关系
		$.ajax({
			url:'<%=basePath %>sys/userMenuDataJuris/queryPCAndMobileMap.action',
			type:"post",
			success: function(dataMap) {
				pcAndMobileMap=dataMap;
			}
		});
		//栏目别名和id对应关系
		$.ajax({
			url:'<%=basePath %>sys/userMenuDataJuris/queryMenuAliasMap.action',
			type:"post",
			success: function(dataMap) {
				menuMap=dataMap;
			}
		});
		$(function(){
			$('#userId').combogrid({
				idField:'account',
				textField:'name',
				mode:'remote',
				panelWidth:600,
				panelHeight:560,
				striped:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,
				pageNumber:1,
				pageSize:20,
				pageList:[20,30,50,100],
				url:'<%=basePath %>sys/userMenuDataJuris/queryDataJurisUserList.action',
				columns:[[
					{field:'account',title:'账户',width:80},
					{field:'name',title:'姓名',width:90},
					{field:'nickName',title:'曾用名',width:100},
					{field:'phone',title:'手机',width:100},
					{field:'email',title:'电子邮箱',width:120},
					{field:'remark',title:'备注',width:100}
				]],
				onSelect:function(index,row){
					$('#gridId').datagrid('load',{userAcc:row.account});
				} 
			});
			$('#userId').combogrid('textbox').bind('focus',function(){
				$('#userId').combogrid('showPanel');
			});
			$('#gridId').datagrid({
				fit:true,
				rownumbers:true,
				striped:true,
				border:false,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:false,
				url:'<%=basePath %>sys/userMenuDataJuris/queryDataJuris.action',
				queryParams:{userAcc:null},
				toolbar:'#toolbarId',
				onLoadSuccess:function(data){
					isJuris = false;
				},
				onBeforeLoad:function(p){
					if(p.userAcc==null){
						return false;
					}
				}
			});
		});
		
		function confList(){
			var userAcc = $('#userId').combogrid('getValue');
			if(userAcc!=null&&userAcc!=''){
				var userName = $('#userId').combogrid('getText');
				if(menuListMapDate!=null&&menuListMapDate.length>0){
					if(jurisTypeCode!=null&&jurisTypeCode!=''){
						if(deptAndAreaDateCode!=null&&deptAndAreaDateCode!=''){
							var rows = $('#gridId').datagrid('getRows');
							var aliasArr = new Array();
							if(rows!=null&&rows.length>0){
								for(var i=0;i<rows.length;i++){
									aliasArr[aliasArr.length] = rows[i].menuAlias;
								}
							}
							for(var i=0;i<menuListMapDate.length;i++){
								if(menuListMapDate[i]!=null&&menuListMapDate[i].haveson=="1"){
									var aliasAssociated=menuListMapDate[i].alias;
									saveOrUpdateDate(i,aliasArr,userAcc,userName,aliasAssociated,menuListMapDate[i].alias,menuListMapDate[i].text);
									if(parseInt(isMobileSyn)==1){//进行App授权操作
										if(pcAndMobileMap[aliasAssociated]!=null){//如果有关联科室
											var mobleAlias=pcAndMobileMap[aliasAssociated];
											var mAlias=mobleAlias.split("&&");
											console.info(menuMap[mAlias[0]]);
											saveOrUpdateDate(i,aliasArr,userAcc,userName,menuMap[mAlias[0]],mAlias[0],mAlias[1]);
										}
									}
								}
							}
							isJuris = true;
						}else{
							$.messager.alert('提示','请选择权限名称!');
						}
					}else{
						$.messager.alert('提示','请选择权限类别!');
					}
				}else{
					$.messager.alert('提示','请选择栏目!');
				}
			}else{
				$.messager.alert('提示','请选择用户!');
			}
		}
		//页面保存或更改数据
		function saveOrUpdateDate(i,aliasArr,userAcc,userName,menuid,menuAlias,menuName){
			var index = $.inArray(menuid,aliasArr);
			if(index==-1){
				$('#gridId').datagrid('appendRow',{
					sign:1,
					userAccount:userAcc,
					userName:userName,
					menuId:menuid,
					menuAlias:menuAlias,
					menuName:menuName,
					jurisType:jurisTypeCode,
					jurisTypeName:jurisTypeName,
					jurisCode:deptAndAreaDateCode,
					jurisCodeName:deptAndAreaDateName
				});
			}else{
				$('#gridId').datagrid('updateRow',{
					index:index,
					row:{
						sign:2,
						jurisType:jurisTypeCode,
						jurisTypeName:jurisTypeName,
						jurisCode:deptAndAreaDateCode,
						jurisCodeName:deptAndAreaDateName
					}
				});
			}
		}
		function del(){
			var userAcc = $('#userId').combogrid('getValue');
			if(userAcc!=null&&userAcc!=''){
				var rows = $('#gridId').datagrid('getChecked');
				if(rows!=null&&rows.length>0){
					var menuAlias = '';
					for(var i=0;i<rows.length;i++){
						if(menuAlias!=null){
							menuAlias += ',';
						}
						menuAlias += rows[i].menuAlias;
					}
					if(menuAlias!=''){
						$.messager.progress({text:'保存中，请稍后...',modal:true});
						$.ajax({ 
							url:'<%=basePath %>sys/userMenuDataJuris/delDataJuris.action',
							type:'post',
							data:{"userAcc":userAcc,"q":menuAlias},
							success: function(dataMap) {
								$.messager.progress('close');
								if(dataMap.resMsg=="success"){
									$('#gridId').datagrid('load');
								}
								$.messager.alert('提示',dataMap.resCode);
							},
							error:function() {
								$.messager.alert('提示','请求失败!');
							}
						});
					}else{
						$.messager.alert('提示','请选择需要删除的授权信息!');
					}
				}else{
					$.messager.alert('提示','请选择需要删除的授权信息!');
				}
			}else{
				$.messager.alert('提示','请选择用户!');
			}
		}
		//保存
		function juris(){
			var userAcc = $('#userId').combogrid('getValue');
			if(userAcc!=null&&userAcc!=''){
				var rows = $('#gridId').datagrid('getRows');
				if(rows!=null&&rows.length>0&&isJuris){
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$.ajax({ 
						url:'<%=basePath %>sys/userMenuDataJuris/saveDataJuris.action',
						type:'post',
						data:{"q":JSON.stringify(rows)},
						success: function(dataMap) {
							$.messager.progress('close');
							if(dataMap.resMsg=="error"){
								$.messager.alert('提示',dataMap.resCode);
							}else{
								$('#gridId').datagrid('load');
								$.messager.alert('提示','用户['+dataMap.userName+']，'+dataMap.resCode+(dataMap.addNum!=null?'，新增栏目权限'+dataMap.addNum+'条':'')+(dataMap.updNum!=null?'，修改栏目权限'+dataMap.updNum+'条':'')+(dataMap.nooNum!=null?'，未变动栏目权限'+dataMap.nooNum+'条':'')+"。");
							}
						},
						error:function() {
							$.messager.progress('close');
							$.messager.alert('提示','请求失败!');
						}
					});
				}else{
					$.messager.alert('提示','没有需要授权的信息!');
				}
			}else{
				$.messager.alert('提示','请选择用户!');
			}
		}
		//修改授权
		function edit(){
			var row=$('#gridId').datagrid('getSelected');
			if(row!=null){
				console.info(row.menuId);
				selectMenuDate(row.menuId);
			}else{
				$.messager.alert('提示','请选择需要修改的记录');
			}
		}
		function selectMenuDateButton(){
			selectMenuDate(null);
		}
		//打开栏目选择页面
		function selectMenuDate(id){
			var userAcc = $('#userId').combogrid('getValue');
			if(userAcc!=null&&userAcc!=''){
				attWindow(id,1250,650,'<%=basePath%>sys/userMenuDataJuris/selectMenuDate.action?v='+Math.random());
			}else{
				$.messager.alert('提示','请选择用户!');
			}
		}
		//以post方式打开窗口
		function attWindow(id,width,height,url){
			var id = id;
			var url = url;
			var name = '选择权限';
			var width = width;
			var height = height;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			
			if($("#winOpenFrom").length<=0){  
				var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId' name='id'/>"+
				"<input type='hidden' id='winOpenFromInpDate' name='editMenuDate'/></form>";
						
				$("body").append(form);
			} 
			if(id!=null){
				$('#winOpenFromInpDate').val(JSON.stringify($('#gridId').datagrid('getSelected')));
			}
			$('#winOpenFromInpId').val(id);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom').prop('action',url);
			$("#winOpenFrom").submit();
		}
		//打开窗口
		function openWindow(url,name,width,height,top,left){
			var win=window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
			
		}
		</script>
	</body>
</html>