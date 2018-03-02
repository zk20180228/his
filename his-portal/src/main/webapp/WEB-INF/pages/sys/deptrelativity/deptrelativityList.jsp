<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>部门相关性管理</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div class="easyui-layout" data-options="fit:true">
			<div id="p" data-options="region:'west',tools:'#toolSMId'" title="部门相关性" style="width:20%;height:100%;">
				<div class="easyui-layout" data-options="fit:true,border:false" >
					<div  style="padding: 5px 0px 5px 5px; width: 90%;" data-options="region:'north',border:false">
						<input type="text" ID="searchTree" name="searchTree" />
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchTree()" data-options="iconCls:'icon-search',border:false">查询</a>
						</shiro:hasPermission>
					</div>
					<div data-options="region:'center',border:false" style="height: 94%">
						<div style="width: 100%;height: 100%" data-options="border:false">
							<div id="toolSMId" >
								<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
								<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
								<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
							</div>
							<ul id="tDt" data-options="border:false"></ul>  
							<div id="tDtmm" class="easyui-menu" style="width:100px;">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div data-options="region:'center'" >
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; ">
			        <div data-options="region:'north',split:false" style="height:80px;">	        
						<div class="easyui-panel" data-options="title:'信息查询',iconCls:'icon-search',border:false">
							<form id="search" method="post">
								<table cellspacing="0" cellpadding="0" border="0px">
								<tr>
									<td style="padding: 15px 15px;">相关性分类：</td>
									<td>
										<input id="refdeptType" name="refdeptType" onkeydown="KeyDown()"/>
										<input type="hidden" id="deptId" name="id" />
									</td>
									<td>
									<shiro:hasPermission name="${menuAlias}:function:query">
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</shiro:hasPermission>
									</td>
									</tr>
								</table>
							</form>
						</div>
					</div>			
					<div data-options="region:'center',split:false,title:'部门列表',iconCls:'icon-book',border:false" style="padding:5px;">
						<table id="list"  data-options="url:'${pageContext.request.contextPath}/baseinfo/department/queryDepartmentById.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
							<thead>
								<tr>
									<th field="ck" checkbox="true" ></th>
									<th data-options="field:'dept',width:'15%',formatter: functionDept" >部门</th>
									<th data-options="field:'deptType',width:'15%'" >部门分类</th>
									<th data-options="field:'refdept',width:'15%',formatter: functionDept" >相关部门</th>
									<th data-options="field:'refdeptType',width:'15%'" >相关部门分类</th>
									<th data-options="field:'remark',width:'35%'" >备注</th>
									
									
								</tr>
							</thead>
						</table>				
					</div>
				</div>
			</div>	
		</div>	
		<div id="dept"></div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<script type="text/javascript">
			var deptList = "";
			$('#refdeptType').combobox({
			url:"<%=basePath %>baseinfo/deptRelativity/refdeptTypeBox.action?",
			valueField:'refdeptType',
			textField:'refdeptType',
			multiple:false,
			editable:false
			});
			$(function(){
				var winH=$("body").height();
				$('#list').height(winH-78-30-27-26-45);
				$.ajax({
					url: "<%=basePath %>baseinfo/department/queryDepartments.action",
					type:'post',
					success: function(deptData) {
						deptList = eval("("+deptData+")");
					}
				});	
			
			//加缓冲时间1毫秒
			setTimeout(function(){
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					fitColumns:true,
			   		pageSize:20,
			   		pageList:[20,30,50,80,100],
			   		onBeforeLoad: function (param) {//加载数据
			        },
					onDblClickRow: function (rowIndex, rowData) {//双击查看
							if(getIdUtil("#list").length!=0){
						   	    AddOrShowEast('EditForm',"<%=basePath %>baseinfo/deptRelativity/viewDeptrelativity.action?id="+getIdUtil("#list"));
						   	}
						}    
					});
	            },10);
			});
				
				
				function reload(){
					  //实现刷新栏目中的数据
	                  $("#list").datagrid("reload");
	                  
				}
					//科室部门树操作
		   	function refresh(){//刷新树
		   		$('#tDt').tree('options').url = "<%=basePath %>baseinfo/department/treeDepartmen.action?treeAll="+false;
				$('#tDt').tree('reload'); 
			}
		   	function expandAll(){//展开树
				$('#tDt').tree('expandAll');
			}
		   	function collapseAll(){//关闭树
				$('#tDt').tree('collapseAll');
			}
	   		
			//查询
	   		function searchFrom(){
	   		    var refdeptType = $('#refdeptType').combobox('getValue');
			    $('#list').datagrid('load', {
			    	refdeptType : refdeptType,
			    	id:$('#deptId').val()
				});
			}	
			
			//回车事件
	   		function KeyDown()  
			{  
			    if (event.keyCode == 13)  
			    {  
			        event.returnValue=false;  
			        event.cancel = true;  
			        searchFrom();  
			    }  
			} 
			
	   		//获得选中id	
			function getId(tableID,str){
				var row = $(tableID).datagrid("getChecked");  
				var dgID = "";
				if(row.length < 1){
					$.messager.alert("操作提示","请选择一条记录！","warning");
					close_alert();
				}
				var i = 0;
				for(i;i < row.length;i++){
					if(str = 0){
						dgID += "\'" + row[i].BED_ID + "\'";
					}else{
						dgID += row[i].BED_ID;
					}
					if(i < row.length - 1){
						dhID += ',';
					}else{
						break;
					}
				}
				return dgID;
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
				}
			
			//加载部门树
		   	$('#tDt').tree({    
			    url:"<%=basePath %>baseinfo/department/treeDepartmen.action?treeAll="+true,
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
				},onClick: function(node){//点击节点
					$('#deptId').val(node.id);
					$('#list').datagrid('load', {
						id: node.id
					});
				//	relativity(node.id);
				}
			}); 
		   	
		   	//科室部门树操作
		   	function refresh(){//刷新树
		   		$('#tDt').tree('options').url = "<%=basePath %>baseinfo/department/treeDepartmen.action?treeAll="+true;
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
				}else{
					return "";
				}
			}
			//部门名字
				function functionDept(value,row,index){
					for(var i=0;i<deptList.length;i++){
						if(value==deptList[i].id){
							return deptList[i].deptName;
						}
					}
				}	
				//查询树
			function searchTree(){
	   			$.ajax({
					url: "<%=basePath %>baseinfo/employee/searchTree.action?searcht="+encodeURI(encodeURI($('#searchTree').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
					type:'post',
					success: function(data) {
						$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
						var node = $('#tDt').tree('find',data);
						$.messager.alert('提示',data+"-"+node);
						if(node!=null){
							$('#tDt').tree('expandTo', node.target).tree('select', node.target); //展开指定id的节点
							$("#list").datagrid("uncheckAll");
							$('#list').datagrid('load', {
								deptName: data
							});
						}	
					}
				});					
			}
		</script>
	</body>
</html>