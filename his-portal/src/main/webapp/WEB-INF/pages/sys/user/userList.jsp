<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp"%>
	<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
		.addList dl:first-child ul {
			overflow:visible !important; 
			clear:both;
		}
		.clearfix:after{
		    content:"";
		    display:table;
		    height:0;
		    visibility:hidden;
		    clear:both;
		}
		.xmenu dl dd ul{
			overflow:visible !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
		#panelEast{
			overflow:auto;
		}
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
			var sexMap=new Map();
		$(function(){
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
				//添加datagrid事件及分页
				$('#list').datagrid({
					url:'<%=basePath %>sys/queryUser.action',method:'post',
					rownumbers:true,
					idField: 'id',
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:true,
					toolbar:'#usertoolbarId',
					fit:true,
					pagination:true ,	//pagination:true,
					fitColumns:true,
			   		pageSize:20,
			   		pageList:[20,30,50,100],

			   		onLoadSuccess: function(data){
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
		   			},
					onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(getIdUtil("#list").length!=0){
						   	AddOrShowEast('EditForm',"<%=basePath%>sys/viewUser.action?user.id="+getIdUtil("#list"));
						}
					},
					onBeforeLoad:function(param){
						$(this).datagrid('uncheckAll');
					},
					onClickRow: function(row){
						var eastpanel=$('#panelEast'); //获取右侧收缩面板
						if(eastpanel.length>0){ //判断右侧收缩面板是否存在
							$('#divLayout').layout('remove','east');
						}
					}
				});
				bindEnterEvent('name',searchFrom,'easyui');
			
		});
		
		//加载页面
		function btnAdd(){
		 	AddOrShowEast("EditForm","<%=basePath%>sys/addUser.action");
			}	
		function btnEdit(){
			if(getIdUtil("#list").length!=0){
			 	AddOrShowEast('EditForm',"<%=basePath%>sys/editUser.action?user.id="+getIdUtil("#list"));
			 	}
			}
			//弹框 用户--》角色
	   	function btnadduser(){
	   		var row = $('#list').datagrid('getSelected'); //获取当前选中行
	   		if(row==null||row==""){
	   			$.messager.alert('提示','请选择具体用户');
	   			close_alert();
	   			return;
	   		}  
	   		if(row.id!=""&&row.id!=null){
	   			AdddilogModel("查看角色","<%=basePath%>sys/listRoleRelations.action?userId="+row.id,'30%','70%');
	   		}
	   	}	
		function btnDelete(){
   			//选中要删除的行
              var iid = $('#list').datagrid('getChecked');
             	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							$.ajax({
								url: "<%=basePath%>sys/delUser.action",
								type:'post',
								data:{"user.id":ids},
								success: function() {
									$.messager.alert('提示','删除成功');
									$('#list').datagrid('reload');
								}
							});										
						}
                   });
               }
		}
  		//查询
  		function searchFrom(){
  		    var name =	$('#name').val();
		    $('#list').datagrid('load', {
				name: name
			});
		}
  		/**
		 * 重置
		 * @author huzhenguo
		 * @date 2017-03-17
		 * @version 1.0
		 */
		function clears(){
			$('#name').textbox('setValue','');
			searchFrom();
		}
		//适用性别
		function functionSex(value,row,index){
			if(value!=null&&value!=''){
				return sexMap.get(value);
			}
		}	
		/**
		 * 动态添加标签页
		 * @author  sunshuo
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-05-21
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
						width : 580,
						split : true,
						href : url,
						closable : true
					});
				}
				$("#panelEast").height($(".easyui-fluid .datagrid-wrap").height()-26);
				$("body").find("#panelEast").css("overflow","auto");
			}
		//加载模式窗口
		function AdddilogModel(title, url, width, height) {
			$('#dialogDivId1').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
			//查看科室
    function editedit(){
    	$('#divLayout').layout('remove','east');
		$('#dialogDivId').dialog('destroy'); 
        if(getIdUtil("#list").length!=0){
        	AddOrShowEast('EditForm',"<%=basePath%>sys/editUserDept.action?id="+getIdUtil("#list"),'35%');
		}

   }
	   	//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#roleWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		
		/**
		* 密码修改
		*/
		function btnPasModify(){
			var row = $('#list').datagrid('getSelected');
			if(row){
				AddPasDialog("重置密码","<%=basePath%>sys/toResetPas.action?userId="+row.id,'500px','200px');
			}else{
				$.messager.alert('提示信息','请选中要修改密码的用户!');
				setTimeout(function(){$(".messager-body").window('close')},3500);
			}
		}
		/**
		* 加载密码修改窗口
		*/
		function AddPasDialog(title, url, width, height) {
			$('#pasWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		
		/**
		* 启用用户APP
		*/
		function btnStartApp(){
			var iid = $('#list').datagrid('getChecked');
          	if (iid.length > 0) {//选中几行的话触发事件	                        
          		$.messager.confirm('确认', '确定要启用该用户的APP使用权限吗?', function(res){//提示是否停用
          			if (res){
						var ids = '';
						for(var i=0; i<iid.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += iid[i].id;
						};
						$.ajax({
							url: "<%=basePath%>sys/startApp.action",
							type:'post',
							data:{"user.id":ids},
							success: function() {
								$.messager.alert('提示','启用成功');
								$('#list').datagrid('reload');
							}
						});
          			}
          		});
				
            }else{
            	$.messager.alert('提示','请至少选择一个用户!');
            }
		}
		
		/**
		* 停用用户APP
		*/
		function btnStopApp(){
			var iid = $('#list').datagrid('getChecked');
          	if (iid.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要停用该用户的APP使用权限吗?', function(res){//提示是否停用
					if (res){
						var ids = '';
						for(var i=0; i<iid.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += iid[i].id;
						};
						$.ajax({
							url: "<%=basePath%>sys/stopApp.action",
							type:'post',
							data:{"user.id":ids},
							success: function() {
								$.messager.confirm('确认','是否立即停用?',function(res){
									if(res){
										$.ajax({
											url:"<%=basePath%>sys/sendStopAppMsg.action",
											type:'post',
											data:{"user.id":ids},
											success:function(){
												$.messager.alert('提示','停用成功');
												$('#list').datagrid('reload');
											}
										});
									}else{
										$.messager.alert('提示','停用成功');
										$('#list').datagrid('reload');
									}
								});
							}
						});										
					}
                });
            }else{
            	$.messager.alert('提示','请至少选择一个用户!');
            }
		}
		
		function sexRend(value){
			return sexMap.get(value);
		}
		function bindDept(){
			var list = $('#list').datagrid('getChecked');
			if(list==null||list.length<1){
				$.messager.alert('提示','请选择用户...');
				return ;
			}
			$('#win').window('open');
			var winH=$("body").height();
			$('#menulist').treegrid({
				url: "<c:url value='/sys/queryMenu.action?menuAlias=${menuAlias}'/>",
				height:winH-170,
				rownumbers: true,
				pagination: true,
				pageSize: 10,
				pageList: [10,20,30,50,80,100],
				onBeforeLoad: function(row,param){
					if (!row) {	// 加载顶级节点
						param.id = 0;	// id=0表示去加载新的一页
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
					}
					var rows = data.rows; 					//全部数据
					var options = $('#menulist').treegrid('getPager').data("pagination").options;	//获得options对象
					var pageNumber = options.pageNumber;	//当前页数
					var total = options.total; 				//总计条数
					var pageSize = options.pageSize;		//分多少页
				}
				,onCheck : function(row){
					var child = $(this).treegrid('getChildren',row.id);
			        for(var i=0;i<child.length;i++){ 
		                var childId = child[i].id;
		                $('#menulist').treegrid('select',childId);  
		            }  
			}
			});
			$(".deptInput").MenuList({
				width :530, //设置宽度，不写默认为530，不要加单位
				height :400, //设置高度，不写默认为400，不要加单位
				dropmenu:"#m2",//弹出层id，必须要写
				haveThreeLevel:true,
				isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
				para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
				firsturl:'<%=basePath%>baseinfo/department/getFicDeptMenu.action?deptTypes=', //获取列表的url，必须要写
				secondurl:"<%=basePath%>baseinfo/department/getDept.action",
				relativeInput:".doctorInput",	//与其级联的文本框，必须要写
				relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
			});
		}
		//查询栏目
		function searchMenuFrom(){
		  	var name =$('#search :input[name="menu.name"]').val();
		    $('#menulist').treegrid({
				url: "<c:url value='/sys/searchMenusByParams.action?menuAlias=${menuAlias}'/>",
				queryParams:{name:name}
		    });
		}
		function submitInit(value){
			var menulist = $('#menulist').treegrid('getChecked');
			if(menulist==null||menulist==""){
				$.messager.alert('提示','请选择栏目!');
			}
			var menu = "";
			for(var i=0;i<menulist.length;i++){
				if(menu!=""){
					menu += ",";
				}
				menu += menulist[i].alias;
			}
			var rows = $('#list').datagrid('getChecked');
			var acounts = "";
			for(var i=0;i<rows.length;i++){
				if(acounts!=""){
					acounts += ",";
				}
				acounts += rows[i].account;
			}
			$.ajax({
				url:"<%=basePath%>baseinfo/columnDept/initColumnDept.action",
				data:{"isAll":value,"mAlias":menu,"acount":acounts},
				success:function(result){
					$.messager.alert('提示',result.resMsg);
					$('#win').window('close');
					return;
				}
				,error:function(){
					$.messager.alert('提示','网络繁忙,请稍后重试!');
					return;
				}
			});
		}
		function submit(){
			var dept = $('#ksnew').getMenuIds();
			if(dept==null||dept==""){
				$.messager.alert('提示','请选择科室!');
				return ;
			}
			var menulist = $('#menulist').treegrid('getChecked');
			if(menulist==null||menulist==""){
				$.messager.alert('提示','请选择栏目!');
			}
			var menu = "";
			for(var i=0;i<menulist.length;i++){
				if(menu!=""){
					menu += ",";
				}
				menu += menulist[i].alias;
			}
			var rows = $('#list').datagrid('getChecked');
			var acounts = "";
			for(var i=0;i<rows.length;i++){
				if(acounts!=""){
					acounts += ",";
				}
				acounts += rows[i].account;
			}
			$.ajax({
				url:"<%=basePath%>baseinfo/columnDept/saveColumnDept.action",
				data:{"deptCode":dept,"mAlias":menu,"acount":acounts},
				success:function(result){
					$.messager.alert('提示',result.resMsg);
					$('#win').window('close');
					return;
				}
				,error:function(){
					$.messager.alert('提示','网络繁忙,请稍后重试!');
					return;
				}
			});
		}
		/**
		 * 关闭dialog
		 */
		function closeDialog() {
			$('#pasWins').dialog('close');
		}
		function bindDeptOpen(){
			var rows= $('#list').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				var id = '';
				var jobNos = "";
				for(var i=0;i<rows.length;i++){
					if(id!=''){
						id += ',';
					}
					id += rows[i].id;
					if(jobNos!=""){
						jobNos += ",";
					}
					jobNos += rows[i].account;
				}
				var url='<%=basePath%>baseinfo/department/userDeptAut.action?acount='+jobNos;
				var name='科室授权';
				var width=1200;
				var height=800;
				var top=(window.screen.availHeight-30-height)/2;
				var left=(window.screen.availWidth-10-width)/2;
				if($("#winOpenFrom").length<=0){  
					var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
							"<input type='hidden' id='winOpenFromInpId' name='id'/></form>";
					$("body").append(form);
				} 
				$('#winOpenFromInpId').val(id);
				openWindow(url,name,width,height,top,left);
				$('#winOpenFrom').attr('action',url);
				$("#winOpenFrom").submit();  
			}else{
				$.messager.alert('提示','请选择需要授权的员工！');
			}
		}
		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',,innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		
		//渲染授权
		function functionRight(value,row,index){
			if(value==1){
				return "授权";
			}else if(value==0){
				return "否";
			}else{
				return "";
			}
		}
		
		//渲染移动端使用状态
		function appUsageStatus(value,row,index){
			if(value==0){
				return "从未登录";
			}else if(value==1){
				return "登录过";
			}else if(value==2){
				return "已退出";
			}else{
				return "";
			}
		}
		//渲染移动端是否可用
		function appIsUse(value,row,index){
			if(value==0){
				return "可用";
			}else if(value==1){
				return "不可用";
			}else{
				return "";
			}
		}
		//格式化出生日期
		function funBirthday(value,row,index){
			if(value){
				return /\d{4}-\d{1,2}-\d{1,2}/g.exec(value);
			}else{
				return "";
			}
			
		}
		/**
		* 解锁账户 PC
		*/
		function unLockPC(){
			var iid = $('#list').datagrid('getChecked');
			if (iid.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要解锁账户吗?', function(res){
					if (res){
						var accounts = '';
						for(var i=0; i<iid.length; i++){
							if(accounts!=''){
								accounts += ',';
							}
							accounts += iid[i].account;
						};
						$.ajax({
							url: "<%=basePath%>sys/unLockPC.action",
							type:'post',
							data:{"user.account":accounts},
							success: function(data) {
								$.messager.alert('提示',data.message);
								$('#list').datagrid('reload');
							}
						});
					}
                });
            }else{
            	$.messager.alert('提示','请至少选择一个用户!');
            }
		}
			
		/**
		* 解锁账户 APP
		*/
		function unLockAPP(){
			var iid = $('#list').datagrid('getChecked');
			if (iid.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要解锁账户吗?', function(res){
					if (res){
						var accounts = '';
						for(var i=0; i<iid.length; i++){
							if(accounts!=''){
								accounts += ',';
							}
							accounts += iid[i].account;
						};
						$.ajax({
							url: "<%=basePath%>sys/unLockAPP.action",
							type:'post',
							data:{"user.account":accounts},
							success: function(data) {
								$.messager.alert('提示',data.message);
								$('#list').datagrid('reload');
							}
						});
					}
                });
            }else{
            	$.messager.alert('提示','请至少选择一个用户!');
            }
		}
			
          
</script>
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
.layout-split-east .panel-header{
	border-top:0;
	border-left:0;
}
</style>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="divLayout" class="easyui-layout" data-options = "fit:true" >
				<div data-options="region:'north',split:false,border:false" style="width: 100%;height: 35px;">
					<table style="padding-top: 2px;">
						<tr>
							<td style="width: 150px;">
								<input class="easyui-textbox" type="text" id="name" name="name" data-options="prompt:'姓名,账号,昵称'" style="width:160px;"/>
							</td>
							<td>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,fit:false,border:false" style="width: 100%;height: 95%;" >
					<input type="hidden" value="${id }" id="id" ></input>
					<table id="list" class="easyui-datagrid" style="width:100%;" 
					data-options="
						 ">
						<thead>
							<tr>
								<th field="getIdUtil" checkbox="true" width="5%"></th>
								<th data-options="field:'name',width:'9%'">姓名</th>
								<th data-options="field:'nickName',width:'7%'">昵称</th>
								<th data-options="field:'account',width:'9%'">账号</th>
								<th data-options="field:'birthday',width:'7%',formatter:funBirthday">出生日期</th> 
								<th data-options="field:'sex',formatter: sexRend,width:'4%'" >性别</th>
								<th data-options="field:'phone',width:'5%'">电话</th>
								<th data-options="field:'email',width:'10%'">电子邮箱</th>
								<th data-options="field:'createTime',width:'3%',hidden:true">创建时间</th>
<!-- 						    <th data-options="field:'canAuthorize',width:'5%',formatter:functionRight">能否授权</th> -->
								<th data-options="field:'lastLoginTime',width:'9%'">最后一次登录时间</th>
								<th data-options="field:'failedTimes',width:'6%'">登录失败次数</th>
								<th data-options="field:'deviceCode',width:'10%'">移动端设备码</th>
								<th data-options="field:'userAppUsageStatus',width:'7%',formatter:appUsageStatus">移动端使用状态</th>
								<th data-options="field:'useStatus',width:'7%',formatter:appIsUse">APP状态</th>
								<!-- <th data-options="field:'order',align:'center',hidden:true">排序</th> -->
							</tr>
						</thead>
					</table>
				</div>		
				<div id="usertoolbarId">
					<shiro:hasPermission name="${menuAlias}:function:add">
						<a  href="javascript:btnAdd();" class="easyui-linkbutton" data-options="iconCls:'icon-add' ,plain:true">新加</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:edit">
						<a  href="javascript:btnEdit();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:delete">
						<a  href="javascript:btnDelete();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-house_link',plain:true" onclick="editedit()">关联科室</a>
					<shiro:hasPermission name="${menuAlias}:function:androle">
						<a  href="javascript:btnadduser();" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511913',plain:true">查看角色</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:resetPas">
						<a href="javascript:btnPasModify();" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">重置密码</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:resetPas">
						<a href="javascript:btnStartApp();" class="easyui-linkbutton" data-options="iconCls:'icon-stop_green',plain:true">启用APP</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:resetPas">
						<a href="javascript:btnStopApp();" class="easyui-linkbutton" data-options="iconCls:'icon-stop_red',plain:true">停用APP</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:resetPas">
						<a href="javascript:unLockPC();" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">解锁平台账户</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:resetPas">
						<a href="javascript:unLockAPP();" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">解锁APP账户</a>
					</shiro:hasPermission>
				</div>
				<div id="roleWins"></div>
		</div>
		<div id="pasWins"></div>
		<div id="dialogDivId1"></div>
		<div id="win" class="easyui-window" title="授权科室" style="width:1000px;height:600px" data-options="iconCls:'icon-save',modal:true,closed:true,closable:true,shadow:true,collapsible:false,minimizable:false,maximizable:false">   
    	<div class="easyui-layout" data-options="fit:true,border:false">
    		<div data-options="region:'north',border:false" style="height:60px" data-options="fit:true">
				<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
				<tr >
					<td style="width:160px;">
						<input id="sName" name="menu.name" class="easyui-textbox" data-options="prompt:'栏目名,别名,拼音,五笔,自定义码'" style="width:150px;"/>
					</td>
					<td style="width:150px;">
						<a href="javascript:void(0)" onclick="searchMenuFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
					<td style="width:55px;" align="center">科室:</td>
					<td style="width:120px; " class="newMenu">
						<div class="deptInput menuInput" style="width:200px"><input style="width:175px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
						<div id="m2" class="xmenu" style="display: none; ">
							<div class="searchDept" >
								<input type="text" name="searchByDeptName" placeholder="回车查询"/>
								<span class="searchMenu"><i></i>查询</span>
								<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">取消</span>
								</a>						
								<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">清空</span>
								</a>
								<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">确定</span>
								</a>
							</div>
							<div class="select-info" style="display:none;">	
								<label class="top-label">已选部门：</label>																						
								<ul class="addDept">
								
								</ul>											
							</div>	
							<div class="depts-dl">
								<div class="addList"></div>
								<div class="tip" style="display:none">没有检索到数据</div>		 
							</div>	
						</div>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="submitInit(1)" class="easyui-linkbutton" data-options="iconCls:'icon-allpermission'">全部科室授权</a>
						<a href="javascript:void(0)" onclick="submitInit(0)" class="easyui-linkbutton" data-options="iconCls:'icon-initpermission'">初始化科室授权</a>
						<a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton" iconCls="icon-permission">授权</a>
					</td>
				</tr>
			</table>
			</div>   
		    <div data-options="region:'center',border:false" data-options="fit:true">
		    	<table id="menulist" style="width:100%;"class="easyui-treegrid" data-options="idField:'id',treeField:'name',animate:false,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true,height:40"></th>
								<th data-options="field:'id',hidden:true">主键</th>
								<th data-options="field:'name',width:'30%'">名称</th>
								<th data-options="field:'alias',width:'10%'">别名</th>
								<th data-options="field:'type',width:'10%'">类型</th>
								<th data-options="field:'parameter',width:'15%'">参数</th>
								<th data-options="field:'icon',hidden:true">图标</th>
								<th data-options="field:'description',width:'10%'">说明</th>
								<th data-options="field:'order',hidden:true">排序</th>
								<th data-options="field:'levelOrder',hidden:true">排序号</th>
								<th data-options="field:'move',width:120">操作</th>
							</tr>
						</thead>
					</table>
		    </div> 
    	</div>
	</div> 
	</body>
</html>