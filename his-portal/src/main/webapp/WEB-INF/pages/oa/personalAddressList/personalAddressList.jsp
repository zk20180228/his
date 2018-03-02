<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
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
	<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
</style>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</head>
	<body>
		<div class="easyui-layout" fit=true
			style="width: 100%; height: 100%; overflow-y: auto;">
			<div id="p" data-options="region:'west',split:true ,tools:'#toolSMId'"" title="个人通讯录管理"
				style="width:300px;overflow: hidden;height: 35px;padding-top: 5px">
				<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
				<div style="width: 100%; height: 100%; overflow-y: auto;">
					<ul id="tDt"></ul>
				</div>
				<div id="tDtmm" class="easyui-menu" style="width:100px;">
					<shiro:hasPermission name="${menuAlias}:function:tadd"> 
						<div onclick="addGroup()" data-options="iconCls:'icon-add'" id="add">添加分组</div>
					</shiro:hasPermission>
					 <shiro:hasPermission name="${menuAlias}:function:tedit"> 
						<div onclick="editGroup()" data-options="iconCls:'icon-edit'" id="edit">修改分组</div>
					</shiro:hasPermission>
					 <shiro:hasPermission name="${menuAlias}:function:tdelete"> 
						<div onclick="delGroup()" data-options="iconCls:'icon-remove'" id="remove">移除分组</div>
					</shiro:hasPermission>
					<!-- <div onclick="viewGroup()" data-options="iconCls:'icon-search'" id="search">查看分组</div> -->
			</div>
			</div>
			<div data-options="region:'center'" style="border-top:0">
				<div id="divLayout" class="easyui-layout" fit=true
					style="width: 100%; height: 100%; ">
					<div id="divLayout" class="easyui-layout" data-options="fit:true">
						<div data-options="region:'north',split:false,border:false" style="width:100%;height: 40px;">
							<form id="search" method="post">
								<table
									style="width: 100%; border: 0px; padding: 5px 5px 0px 5px;">
									<tr>
										<td style="width: 500px;">查询条件：
										<input class="easyui-textbox" id="queryName"  name="queryName"  data-options="prompt:'姓名,拼音,五笔,自定义'"onkeydown="KeyDown(0)"style="width: 230px;"/>
											<a href="javascript:void(0)" onclick="searchFrom()"
												class="easyui-linkbutton" iconCls="icon-search">查询</a>
												<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
										<input id="parentCodes" type="hidden"></input>	
										<input id="groupNames" type="hidden"></input>		
										</td>
									</tr>
								</table>
							</form>
						</div>
						<div data-options="region:'center',split:false,iconCls:'icon-book',border:true" style="border-left:0">
							<table id="list" 
								data-options="url:'${pageContext.request.contextPath}/oa/personalAddressList/queryPersonalAddress.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true" ></th>
										<th data-options="field:'belongGroupName',align:'center', width : 120">所属分组</th>
										<th data-options="field:'perName',align:'center', width : 120" >姓名</th>
										<th data-options="field:'perSex',width :50,align:'center',formatter:sexFamater" >性别</th>
										<th data-options="field:'perInputCode' ,align:'center',width :100" >自定义码</th>
										<th data-options="field:'perBirthday' ,align:'center',width :100" >出生日期</th>
										<th data-options="field:'mobilePhone',align:'center', width : '100'" >移动电话</th>
										<th data-options="field:'workPhone',align:'center', width : 90" >办公电话</th>
										<th data-options="field:'perEmail',align:'center', width : 170" >电子邮箱</th>
										<th data-options="field:'perAddress',align:'center', width : 190" >家庭住址</th>
										<th data-options="field:'perRemark',align:'center', width : 170" >备注</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>	
		<div id="group"></div>
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
			<a href="javascript:void(0)" onclick="removeGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-change',plain:true">移至分组</a>
		</div>
		 
		 <div id="diaInpatient" class="easyui-dialog" title="分组选择" style="width:290;height:430;padding:1" data-options="modal:true, closed:true">
			<div style="height: 27px;padding: 5px 0px 5px 0px">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" onclick="confirm()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">确定</a>
			</div>
			<div id="treeDiv" style="width: 100%; height: 90%; overflow-y: auto;">
    			<ul id="tGt"></ul>
    		</div>
		</div>
		 
		 
		 
		<script type="text/javascript">
		var menuAlias="${menuAlias}";
		var deptid=null;
		var sexList=null;
		//加载页面
		$(function(){
			
			//性别渲染
			$.ajax({
				url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
				data:{"type":"sex"},
				type:'post',
				success: function(data) {
					sexList = data;
				}
			});
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination:true,
		   		pageSize:20,
		   		pageList:[10,20,30,40,50],
		   		onBeforeLoad:function (param) {
					$('#list').datagrid('clearChecked');
					$('#list').datagrid('clearSelections');
		        },
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
		        },onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(rowData.id!=null&&rowData.id!=''){
							AddOrShowEast('查看',"<%=basePath %>oa/personalAddressList/viewPersonalMes.action?id="+rowData.id);
					   	}
					}    
				});
				bindEnterEvent('queryName',searchFrom,'easyui');
		});
	
		//添加
		function add(){
			var parentCodes=$('#parentCodes').val();
			var groupNames=$('#groupNames').val();
			var node = $('#tDt').tree('getSelected');
			if(node){
				AddOrShowEast('添加',"<%=basePath %>oa/personalAddressList/addPersonalMes.action?parentCode="+parentCodes+"&groupName="+encodeURI(encodeURI(groupNames)));
			}else{
				$.messager.alert('提示','请选择分组！');	
				setTimeout(function(){
   					$(".messager-body").window('close');
   				},3500);
			}
			
		}
		//修改	
		function edit(){
			var row = $('#list').datagrid('getSelected');
			  if(row != null){
	               AddOrShowEast('编辑',"<%=basePath %>oa/personalAddressList/editPersonalMes.action?id="+row.id);
	               $('#list').datagrid('reload');
			  }else{
		   			$.messager.alert('提示','请点中要修改信息！');	
		   			setTimeout(function(){
       					$(".messager-body").window('close');
       				},3500);
	   		  }
		}
		
		function del(){
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
						$.messager.progress({text:'删除中，请稍后...',modal:true});
						$.ajax({
							url: "<c:url value='/oa/personalAddressList/delPersonalMes.action'/>?id="+ids,
							type:'post',
							success: function(data) {
								$.messager.progress('close');
								if("success"==data.resCode){
									$.messager.alert('提示',data.resMsg);
									setTimeout(function(){
				       					$(".messager-body").window('close');
				       				},3500);
									$('#list').datagrid('reload');
								}else{
									$.messager.alert('提示',data.resMsg);
									setTimeout(function(){
				       					$(".messager-body").window('close');
				       			},3500);
								}
							}
						});										
					}
		        });
	        }else{
         	    	$.messager.alert('提示信息','请选择要删除的信息！');
       	    	 	setTimeout(function(){
       					$(".messager-body").window('close');
       				},3500);
       	     }	
		}
	
		//列表刷新
		function reload(){
			//实现刷新栏目中的数据
			 $("#list").datagrid("reload");
		}
		
		//移至分组
		function removeGroup(){
			var iid = $('#list').datagrid('getChecked');
	        if (iid.length <= 0) {//选中几行的话触发事件	                        
	        	 $.messager.alert('提示信息','请选择要移动的信息！');
	       	    	setTimeout(function(){
	       					$(".messager-body").window('close');
	       			},3500);
	       	    	return false;
	        }
			$("#diaInpatient").window('open');
			/**
			 * 加载分组树
			 * @author  zxl
			 * @param 
			 * @date 2015-06-03
			 * @version 1.0
			 */		
		   	$('#tGt').tree({    
		   		url:"<c:url value='/oa/personalAddressList/treePersonalAddress.action'/>",
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if(node.children.length>0){
						if (node.children){
							s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						}
					}
					return s;
				},
				onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					$('#divLayout').layout('remove','east');
				},
				onBeforeCollapse:function(node){
					if(node.id=='root'){
						return false;
					}
				}
		         
			});
			
		}
		
		function confirm(){
			var nodes = $('#tGt').tree('getSelected');
			if(nodes){
				var parentCodes=nodes.id;
				var parentText=nodes.text;
				$('#diaInpatient').window('close');
				 //选中要删除的行
		        var iid = $('#list').datagrid('getChecked');
			 	$.messager.confirm('确认', '确定要将选中信息移动至'+parentText+'分组吗?', function(res){//提示是否删除
					if (res){
						var ids = '';
						for(var i=0; i<iid.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += iid[i].id;
						};
						$.ajax({
							url: "<c:url value='/oa/personalAddressList/movePersonalToGroup.action'/>?id="+ids+"&parentCode="+parentCodes,
							type:'post',
							success: function(data) {
								$.messager.alert('提示',data.resMsg);
								$('#list').datagrid('reload');
							}
						});										
					}
		        });
			}else{
				 $.messager.alert('提示信息','请选择分组信息！');
       	    	 setTimeout(function(){
       					$(".messager-body").window('close');
       			 },3500);
			}
		}
		
		
		/**
		 * 查询
		 */
		function searchFrom() {
			var node = $('#tDt').tree('getSelected');
			var queryName = $.trim($('#queryName').textbox('getValue'));
			var parentCodes = "";
			if(node){
				parentCodes =node.id; 
			}
			$('#list').datagrid('load', {
				queryName : queryName,
				parentCode: parentCodes,
			});
			$('#divLayout').layout('remove','east');
		}
		/**
		 * 动态添加标签页
		 * @author  zxl
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-05-21
		 * @version 1.0
		 */
		function AddOrShowEast(title, url) {
			var eastpanel = $('#panelEast'); //获取右侧收缩面板
			if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
				//重新装载右侧面板
				$('#divLayout').layout('panel', 'east').panel({
					href : url
				});
			} else {//打开新面板
				$('#divLayout').layout('add', {
					region : 'east',
					width : 580,
					title:title,
					split : true,
					maxHeight:820,
					href : url
					
				});
			}
		}

		/**
		 * 回车键查询
		 * @author  liujl
		 * @param flg 标识：0=查询；1=编辑
		 * @date 2015-05-27
		 * @version 1.0
		 */
		function KeyDown(flg)  
		{   
	    	if(flg==0){
			    if (event.keyCode == 13)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true;  
			        searchFrom();  
			    }  
		    }
		} 
   
  		/**
		 * 加载分组树
		 * @author  zxl
		 * @param 
		 * @date 2015-06-03
		 * @version 1.0
		 */		
	   	$('#tDt').tree({    
	   		url:"<c:url value='/oa/personalAddressList/treePersonalAddress.action'/>",
		    method:'get',
		    animate:false,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.children.length>0){
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},
			onSelect: function(node){//点击节点
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			    $('#groupNames').val(node.text);
			    $('#parentCodes').val(node.id);
			    var queryName=$.trim($('#queryName').textbox('getValue'));
				$('#list').datagrid('load', {
					queryName: queryName,
					parentCode: node.id
				});
				$('#divLayout').layout('remove','east');
			},
			onBeforeCollapse:function(node){
				if(node.id=='root'){
					return false;
				}
			},
	        onContextMenu: function(e,node){//添加右键菜单
					e.preventDefault();
					$(this).tree('select',node.target);
					if(node.iconCls=='icon-branch'){
						$("#add").show();
						$("#edit ").show();
						$("#remove").show();
						$('#tDtmm').menu('show',{
							left: e.pageX,
							top: e.pageY
						});
					}
				}
		}); 
	   	
	   	
	   	
		//刷新树
	   	function refresh(){
	   		$('#tDt').tree('options').url = "<%=basePath%>oa/personalAddressList/treePersonalAddress.action";
			$('#tDt').tree('reload'); 
		}
	    //展开树
	   	function expandAll(){
            //执行全部展开expandAll
			$('#tDt').tree('expandAll');
		}
	    //关闭树
	   	function collapseAll(){
			$('#tDt').tree('collapseAll');
		}
	    //添加分组
	   	function addGroup(){
	   		var id = getSelected();
	   		Adddilog("添加分组",'<%=basePath%>oa/personalAddressList/addGroup.action?menuAlias='+menuAlias+'&parentCode='+id);
		}
	   //修改分组
	   	function editGroup(){
	   		var node = $('#tDt').tree('getSelected');
	   		var id ='';
	   		var groupNames='';
	   		if (node){
				id = node.id;
				groupNames=node.text;
			}
	   		Adddilog("编辑分组",'<%=basePath%>oa/personalAddressList/editGroup.action?menuAlias='+menuAlias+'&id='+id+'&groupName='+encodeURI(encodeURI(groupNames)));
	   		
		}
	    //查看分组
		function viewGroup(){
	   		var id = getSelected();
	   		Adddilog("查看分组",'<%=basePath%>oa/personalAddressList/addGroup.action?menuAlias='+menuAlias+'&id='+id);
		}
		//移除分组
	   	function delGroup(){
	   		if($('#tDt').tree('getSelected')){
	   			$.messager.confirm('确认', '该分组下所有信息将全部删除，确定要删除选中信息吗?', function(){//提示是否删除
					$.ajax({
						url: '<%=basePath%>oa/personalAddressList/delGroup.action?id='+getSelected(),
						type:'post',
						success: function(data) {
							if("success"==data.resCode){
								$.messager.alert("提示",data.resMsg);
								refresh();
								$('#list').datagrid('reload');
							}else{
								$.messager.alert("提示",data.resMsg);
							}
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							
						}
					});										
				});
	   		}else{
	   			$.messager.alert('警告','请选择具体分组删除');  
	   			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	   		}
	   		
		}
		//获得选中节点   	
	   	function getSelected(){
			var node = $('#tDt').tree('getSelected');
			if (node){
				var id = node.id;
				return id;
			}
		}
		
		// 列表查询重置
		function searchReload() {
			$('#queryName').textbox('setValue','');
			$('#tDt').tree('reload');
			var queryName=$.trim($('#queryName').textbox('getValue'));
			$('#list').datagrid('load', {
				queryName: queryName,
			});
		}
		
		//加载dialog
		function Adddilog(title, url) {
			$('#group').dialog({    
			    title: title,    
			    width: '30%',    
			    height:'35%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		//打开dialog
		function openDialog() {
			$('#group').dialog('open'); 
		}
		//关闭dialog
		function closeDialog() {
			$('#group').dialog('close');  
		}
		
		//性别渲染
		function sexFamater(value, row, index){
			if (value != null) {
				for ( var i = 0; i < sexList.length; i++) {
					if (value == sexList[i].encode) {
						return sexList[i].name; 
					}
				}
			}
		}
		</script>
	</body>
</html>