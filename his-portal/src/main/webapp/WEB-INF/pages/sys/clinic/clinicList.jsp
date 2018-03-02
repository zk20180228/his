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
<style type="text/css">
	.panel-header{
		border-top:0;
		border-left:0;
	}
	.clinicView{
		border-left:0
	}
</style>
</head>
	<body>
		<div class="easyui-layout" class="easyui-layout" fit=true
			style="width: 100%; height: 100%; overflow-y: auto;">
			<div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="部门科室管理" style="width: 300px; padding: 0px; overflow: hidden;">
				<div id="toolSMId">
					<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
					<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
					<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
				<div>
					<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'科室名,拼音,五笔,自定义码'" style="width: 200px;"/>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
				</div>
				<div style="width: 100%; height: 100%; overflow-y: auto;">
					<ul id="tDt"></ul>
				</div>
			</div>
			<div data-options="region:'center'" style="border-top:0">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; ">
					<div data-options="region:'north',border:false" style="height:40px;padding: 5px 5px 0px 5px;">
						<form id="search" method="post">
							<table style="width: 100%; border: 0px;">
								<tr>
									<td style="width:300px;">查询条件：<input class="easyui-textbox" id="queryName" name="queryName" onkeydown="KeyDown(0)" data-options="prompt:'诊室名称,拼音,五笔,科室,自定义'"  style="width: 220px;"/></td>
									<td>
										<a href="javascript:void(0)" onclick="searchFrom()"
											class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div data-options="region:'center',border:true" style="border-down:none;border-left:none;">
						<input type="hidden" name="deptNames" id="deptNames"/>
						<table id="list" 
							data-options="url:'${pageContext.request.contextPath}/baseinfo/clinic/queryClinic.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true,
			onLoadSuccess:function(row, data){
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
				}}">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
									<th data-options="field:'clinicName', width : '15%'">诊室名称</th>
									<th data-options="field:'clinicPiyin', width : '8%'" >诊室拼音</th>
									<th data-options="field:'clinicWb',width :'8%'" >诊室五笔</th>
									<th data-options="field:'clinicInputcode', width : '9%'" >诊室自定义码</th>
									<th data-options="field:'clinicDeptId', width : '10%',formatter:deptFamater" >所属科室</th>
									<th data-options="field:'clinicRemark', width : '30%'" >备注</th>
								</tr>
							</thead>
						</table>
					</div>
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
				<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
		<script type="text/javascript">
			var delptList=null;//用于显示科室集合
			var deptname=null;//用于放回显科室id
			var deptnames=null;//用于放回显示科室的name
			//加载页面
			$(function(){
// 			    var winH=$("body").height();
// 				$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
// 				$('#list').height(winH-78-30-27-26);
				var id="${id}"; //存储数据ID
				$.ajax({
					url: "<c:url value='/baseinfo/department/queryDepartments.action'/>",
					type:'post',
					success: function(deptListData) {
						delptList =deptListData;
					}
				});
				//加缓冲时间1毫秒
				setTimeout(function(){
					//添加datagrid事件及分页
					$('#list').datagrid({
						pagination:true,
				   		pageSize:20,
				   		pageList:[20,30,50,80,100],
				   		onBeforeLoad:function (param) {
				   		//GH 2017年2月17日 翻页时清空前页的选中项
							$('#list').datagrid('clearChecked');
							$('#list').datagrid('clearSelections');
				        },
						onLoadSuccess: function (data) {//默认选中
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
				        },onDblClickRow: function (rowIndex, rowData) {//双击查看
								if(getIdUtil("#list").length!=0){
									AddOrShowEast('查看',"<c:url value='/baseinfo/clinic/viewClinic.action'/>?id="+getIdUtil("#list"),'560');
							   	}
							}    
					});
	            },500);
			bindEnterEvent('queryName',searchFrom,'easyui');
			bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
			});
			function add(){
				var deptnames = $("#deptnames").val();
				   AddOrShowEast('添加','<%=basePath%>baseinfo/clinic/addClinic.action','560','post'
						   ,{"deptid":deptname,"deptNames":encodeURI(encodeURI($('#deptNames').val()))});
			}
			function edit(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行     
	            if(row != null){
                   	AddOrShowEast('编辑',"<c:url value='/baseinfo/clinic/editClinic.action'/>?id="+row.id,'560');
		   		}else{
		   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
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
							$.ajax({
								url: "<c:url value='/baseinfo/clinic/delClinic.action'/>?id="+ids,
								type:'post',
								success: function(data) {
									$.messager.alert('提示',data.resMsg);
									if(data.resCode=='success'){
										$('#list').datagrid('reload');
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
	
			function reload(){
				//实现刷新栏目中的数据
				 $("#list").datagrid("reload");
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
					var dept = $("#tDt").tree('getSelected');
				    var deptID='';
					if(dept != null){
						deptID=dept.id;
					}
					var queryName = $.trim($('#queryName').textbox('getValue'));
					$('#list').datagrid('load', {
						deptId : deptID,
						name : queryName
					});
				}
			
				/**
				 * 动态添加LayOut
				 * @author  liujl
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-05-21
				 * @version 1.0
				 */
				function AddOrShowEast(title, url,width,method,params) {
					if(!method){
						method="get";
					}
					if(!params){
						params={};
					}
					var eastpanel=$('#panelEast'); //获取右侧收缩面板
					if(eastpanel.length>0){ //判断右侧收缩面板是否存在
						$('#divLayout').layout('remove','east');
						$('#divLayout').layout('add', {
							region : 'east',
							width :width ,
							split : true,
							href : url,
							method:method,
							queryParams:params,
							closable : true
						});
					}else{//打开新面板
						$('#divLayout').layout('add', {
							region : 'east',
							width :width ,
							split : true,
							href : url,
							method:method,
							queryParams:params,
							closable : true
						});
					}
					
				}
			//显示科室格式化
			function deptFamater(value){
				var retVal = "";
				if(value!=null){
					for(var i=0;i<delptList.length;i++){
						if(value==delptList[i].id){
							retVal = delptList[i].deptName;
							break;
						}
					}	
				}
				return retVal;
			}	
			/**
			 * 回车键查询
			 * @author  liujl
			 * @param flg 标识：0=查询；1=编辑
			 * @date 2015-05-27
			 * @version 1.0
			 */
			function KeyDown(flg){   
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
			 * 关闭编辑窗口
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			/**
			 * 在列别页面插入科室树
			 * @author  zpty
			 * @param 
			 * @date 2015-06-03
			 * @version 1.0
			 */		
			//加载部门树
		   	$('#tDt').tree({    
			    url:"<c:url value='/baseinfo/department/treeDepartmen.action'/>?treeAll="+false,
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children){
						if(node.children.length!=0)
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					$('#list').datagrid('uncheckAll');
					$('#list').datagrid('unselectAll')
					deptname=node.id;
					$('#list').datagrid('load', {
						deptId : node.id
					});
					$('#deptNames').val(node.text);
				},
				onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
				},
				onLoadSuccess:function(){
					$('#tDt').tree('collapseAll');
				}
			}); 
		   	
		   	//科室部门树操作
		   	function refresh(){//刷新树
		   		
				$('#tDt').tree('reload'); 
			}
		   	function expandAll(){//展开树
				$('#tDt').tree('expandAll');
			}
		   	function collapseAll(){//关闭树
				$('#tDt').tree('collapseAll');
			}
		   	function getSelected(){//获得选中节点
				var node = $('#tDt').tree('getSelected');
				if (node){
					var id = node.id;
					return id;
				}
			}

			/**
			 * 树查询的方法
			 * @author  zpty
			 * @param 
			 * @date 2015-06-03
			 * @version 1.0
			 */
			function searchTree(){//刷新树
	   			$.ajax({
						url: "<c:url value='/baseinfo/employee/searchTree.action'/>?searcht="+encodeURI(encodeURI($('#searchTree').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
						type:'post',
						success: function(data) {
							$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
							var node = $('#tDt').tree('find',data);
							$('#tDt').tree('expandTo', node.target).tree('select', node.target); //展开指定id的节点
							$("#list").datagrid("uncheckAll");
							$('#list').datagrid('load', {
								deptName: data
							});
						}
					});					
			}
	
			/**
			 * 为树查询的方法加入回车查询方式
			 * @author  zpty
			 * @param 
			 * @date 2015-06-10
			 * @version 1.0
			 */
			function KeyDownTree(){
			    if (event.keyCode==13){  
			    	event.returnValue=false;  
			        event.cancel = true;  
			        searchTree(); 
			    }
			}
			function searchTreeNodes(){
		    	var searchText = $('#searchTreeInpId').textbox('getValue');
		    	$("#tDt").tree("search",[searchText,true]);
		    }
			
			// 列表查询重置
			function searchReload() {
				$('#queryName').textbox('setValue','');
				searchFrom();
			}
		</script>
	</body>
</html>