<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>编码类别</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
			<div id="p" data-options="region:'west',tools:'#toolSMId'" title="编码类别" style="width:300px;padding:0px">
				<div id="toolSMId">
					<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
					<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
					<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
				</div>
				<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
					<ul id="tDt"></ul>  
				</div>
				<div id="tDtmm" class="easyui-menu" style="width:50px;">
					 <div onclick="" data-options="iconCls:'icon-add'" id="addDiv" >添加</div>
					 <div onclick="" data-options="iconCls:'icon-edit'" id="updateDiv" >修改</div>
					<div onclick="" data-options="iconCls:'icon-book'" id="viewDiv">查看</div>
				</div>
			</div>
			<div data-options="region:'center',border:false"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
					<div style="padding: 5px 5px 0px 5px;height: 50px" data-options="region:'north',border:false">
						<form id="search" method="post">
							<table
								style="width: 100%; border: 0; padding: 5px;">
								<tr>
									<td style="width: 320px;" nowrap="nowrap">
										关键字：<input type="text" id="queryName" name="queryName" onkeydown="KeyDown(0)"/>
										<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div data-options="region:'center',border:false">
					<table id="list" style="width:100%" data-options="url:'queryCodeType.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true" ></th>
								 <th data-options="field:'code'">代码</th>
							     <th data-options="field:'name'">名称</th>
								  <th data-options="field:'level'">允许层级</th>
							</tr>
						</thead>
					</table>
				</div>
				</div>
			</div>
			</div>
		<script type="text/javascript">
		    /**
		 	* 加载页面
		    * @author  hedong
		    * @date 2015-6-12 10:53
		    * @version 1.0
		    */
			$(function(){
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
			        },toolbar: [{
	                    id: 'btnAdd',
	                    text: '添加',
	                    iconCls: 'icon-add',
	                    handler: function () {
	                        AddOrShowEast('EditForm','addCodeType.action');
	                    }
	                }, '-', {
	                    id: 'btnEdit',
	                    text: '修改',
	                    iconCls: 'icon-edit',
	                    handler: function () {
	                        var row = $('#list').datagrid('getSelected'); //获取当前选中行                        
	                        if(row){
	                        	AddOrShowEast('EditForm','editCodeType.action?id='+row.id);
					   		}
	                    }
	                }, '-', {
	                    id: 'btnReload',
	                    text: '刷新',
	                    iconCls: 'icon-reload',
	                    handler: function () {
	                        //实现刷新栏目中的数据
	                        $('#list').datagrid('reload');
	                    }
	                }, '-', {
	                    id: 'btnInitializeXMLS',
	                    text: 'xml文件初始化',
	                    iconCls: 'icon-remove',
	                    handler: function () {
	                       //alert("功能建造中!");
	                        //选中要删除的行
		                    var rows = $('#list').datagrid('getChecked');
	                    	if (rows.length > 0) {//选中几行的话触发事件	                        
							 	$.messager.confirm('确认', '确定要初始化选中信息的xml文件吗?', function(res){//提示是否初始化
									if (res){
										var ids = '';
										for(var i=0; i<rows.length; i++){
											if(ids!=''){
												ids += ',';
											}
											ids += rows[i].id;
										};
										$.ajax({
											url: 'xmlInitial.action?selected='+ids,
											type:'post',
											success: function() {
												$.messager.alert('提示','初始化成功');
											}
										});
										
									}
	                        	});
		                    }else{
		                        $.messager.confirm('确认', '确定要初始化全部xml文件吗?', function(res){//提示是否初始化
										if (res){
											$.ajax({
												url: 'xmlInitial.action?selected=',
												type:'post',
												success: function() {
													$.messager.alert('提示','初始化成功');
												}
											});
											
										}
		                        });
		                    
		                    }
	                    }
	                }, '-', {
	                    id: 'generateCode',
	                    text: '重新生成代码',
	                    iconCls: 'icon-remove',
	                    handler: function () {
	                    	var rows = $('#list').datagrid('getChecked');
	                    	if (rows.length > 0) {//选中几行的话触发事件	                        
							 	$.messager.confirm('确认', '确定要将选中信息生成代码文件吗?', function(res){//提示是否初始化
									if (res){
										var codes = '';
										for(var i=0; i<rows.length; i++){
											if(codes!=''){
												codes += ',';
											}
											codes += rows[i].code + "/" + encodeURI(encodeURI(rows[i].name)) + "/" + rows[i].level;
										};
										$.ajax({
											url: 'generateCodeForSelected.action?selected='+codes,
											type:'post',
											success: function() {
												$.messager.alert('提示','生成代码文件成功!');
											}
										});
										
									}
	                        	});
		                    }else{
								showWin("编码类别代码生成窗口","showCodeWin.action","40%","35%");
		                    }
	                    }
	                }]	                
					,onDblClickRow: function (rowIndex, rowData) {//双击查看
							if(getIdUtil('#list').length!=0){
						   	    AddOrShowEast('EditForm','viewCodeType.action?id='+getIdUtil('#list'));
						   	}
						}    
					});
				});
				/**
				 * 格式化复选框
				 * @author  hedong
				 * @date 2015-5-26 9:25
				 * @version 1.0
				 */
				function formatCheckBox(val,row){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}	
			
				/**
				 * 查询
				 * @author  hedong
				 * @date 2015-06-26
				 * @version 1.0
				 */
				function searchFrom() {
					 var queryName = $('#queryName').val();
					 $('#list').datagrid('load', {
						 name : queryName
					  });
				}
			
				/**
				 * 动态添加标签页
				 * @author  hedong
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-05-22
				 *@modifydate 2015-06-18
				 * @version 2.0
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
				}
			
				/**
				 * 回车键查询
				 * @author  hedong
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-05-27
				 * @version 1.0
				 */
				function KeyDown()  
				{  
				    if (event.keyCode == 13)  
				    {  
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
				} 
		   
		   //树begin----------------------------------------------------------------------------------
			   /**
			 	* 加载编码树
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
			   	$('#tDt').tree({    
				    url:'showTree.action?sid='+ new Date().valueOf(),
				    method:'get',
				    animate:true,
				    lines:true,
				    onClick: function(node){//点击节点    点击节点时刷新 hd添加注释  用于刷新右侧列表页面   最终需根据节点的判断跳到哪个列表页面
					   //根据传递的节点id 
					  clickForward($('#tDt').tree('getSelected'));
					},onContextMenu: function(e,node){//添加右键菜单
						$("#addDiv").unbind( "click" );
						$("#updateDiv").unbind( "click" );
						$("#delDiv").unbind( "click" );
						$("#viewDiv").unbind( "click" );
						$("#stopDiv").unbind( "click" );
						$(this).tree('select',node.target);
					    var selectedNode = $('#tDt').tree('getSelected');
						if(node.attributes.addUrl){
						   if(node.attributes.currentLevel+1<node.attributes.level||!node.attributes.currentLevel){
						   		 $('#addDiv').css("display","block");
						   		 $("#addDiv").bind( "click",function(){
						     		if(selectedNode){
						         	var tempUrl = selectedNode.attributes.addUrl+"?nodeId="+selectedNode.id+"&nodeText="+selectedNode.text;
						         	showWin(selectedNode.attributes.sysCodeTypeName+"添加窗口",tempUrl,"55%","70%");
						      		}
						   		});
						   }else{
						         $('#addDiv').css("display","none");
						   }
						  
						}else if(!node.attributes.addUrl){
						   $('#addDiv').css("display","none");
						}
						
						if(node.attributes.editUrl){
						   $('#updateDiv').css("display","block");
						    $("#updateDiv").bind( "click",function(){
						       if(selectedNode){
						         var tempUrl = selectedNode.attributes.editUrl+"?nodeId="+selectedNode.id+"&nodeText="+selectedNode.text;
						         showWin(selectedNode.attributes.sysCodeTypeName+"修改窗口",tempUrl,"60%","70%");
						       }
						   });
						}else if(!node.attributes.editUrl){
						   $('#updateDiv').css("display","none");
						}
						
						
						
						if(node.attributes.viewUrl){
						   $('#viewDiv').css("display","block");
						   $("#viewDiv").bind( "click",function(){
						     if(selectedNode){
						          var tempUrl = selectedNode.attributes.viewUrl+"?nodeId="+selectedNode.id+"&nodeText="+selectedNode.text;
						         showWin(selectedNode.attributes.sysCodeTypeName+"查看窗口",tempUrl,"60%","70%");
						       }
						   });
						}else if(!node.attributes.viewUrl){
						   $('#viewDiv').css("display","none");
						}
						
					    if(node.attributes.addUrl||node.attributes.editUrl||node.attributes.viewUrl){
					       treeShowMenu(e);
					    }
					}
				}); 
				
				/**
			 	* 右键菜单
			 	* @param e 事件对象
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
				function treeShowMenu(e){
				  e.preventDefault();
				  e.stopPropagation();//阻止冒泡
						
				  $('#tDtmm').menu('show',{
							left: e.pageX,
							top: e.pageY
				   });
				}
			   	/**
			 	* 刷新树
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
			   	function refresh(){
			   		$('#tDt').tree('options').url = "showTree.action";
					$('#tDt').tree('reload'); 
				}
				/**
			 	* 展开树
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
				function expandAll(){
					$('#tDt').tree('expandAll');
				}
				/**
			 	* 关闭树
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
			   	function collapseAll(){
					$('#tDt').tree('collapseAll');
				}
				
			    var win;
			   	/**
			 	* 类别右键添加弹出框
			    * @param title 窗口标题
			    * @param url   路径
			    * @param width 宽度
			    * @param height 高度
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
				function showWin ( title,url, width, height) {
				    var content = '<iframe src="' + url + '" width="100%" height="99%" frameborder="0" scrolling="no"></iframe>';
				        win = $('<div id="treeDeparWin"><div/>').dialog({
				        content: content,
				        width: width,
				        height: height,
				        modal: true,
				        minimizable:false,
				        maximizable:true,
				        resizable:true,
				        shadow:true,
				        center:true,
				        title: title
				    });
	
				    win.dialog('open');
				}
	
				
				/**
			 	* 编码树点击事件  访问不同的编码类别列表
			    * @param selectedNode  被选中节点
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
				function clickForward(selectedNode){
				   if(selectedNode){
				   		var url=selectedNode.attributes.url;
				   		if(url.indexOf("listCodeType.action")!=-1){
				   		  
				   		  return;
				   		  window.location.href=url;
				   		}
				   		var content = '<iframe scrolling="auto" frameborder="0" src=" ' + url + '" style="width:100%;height:100%;"></ifrmae>';
				   		
				   		$('#treeLayOut').layout('panel','center').panel({  
	                           content:content 
	                    }); 
				   }
				  
				}
				
		        var codeWin;
		        /**
			 	* 代码生成弹出框
			    * @param title 窗口标题
			    * @param url   路径
			    * @param width 宽度
			    * @param height 高度
			    * @author  hedong
			    * @date 2015-6-12 10:53
			    * @version 1.0
			    */
				function showCodeWin ( title,url, width, height) {
				    var content = '<iframe  src="' + url + '" width="100%" height="99%" frameborder="0" scrolling="no"></iframe>';
				    codeWin = $('<div id="codeGenerateWin"><div/>').dialog({
				        content: content,
				        width: width,
				        height: height,
				        modal: true,
				        minimizable:false,
				        maximizable:true,
				        resizable:true,
				        shadow:true,
				        center:true,
				        title: title
				    });
	
				    codeWin.dialog('open');
				}
		</script>
	</body>
</html>