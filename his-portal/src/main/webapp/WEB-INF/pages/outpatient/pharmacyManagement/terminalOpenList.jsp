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
		<title>门诊终端模板维护界面</title>
		<script type="text/javascript">
    		var terminalMap=null; //渲染发药窗口信息
    		var propertyMap=null; //性质类型
	    	$(function(){
	    		propertyMap=getPropertyMap();
	    		
	    		$.ajax({
	    			url: '<%=basePath %>drug/pharmacyManagement/queryTerminalMap.action',
	    			type:'post',
	    			success: function(payData) {
	    				terminalMap = payData;
	    			}
	    		});
	    		
	    		//加载模版列表树
	    	   	$('#tDt').tree({    
	    		    url:'<%=basePath%>drug/pharmacyManagement/templateTree.action?pid='+window.parent.window.getSelected(2),
	    		    method:'get',
	    		    animate:true,
	    		    lines:true,
	    		    formatter:function(node){//统计节点总数
	    				var s = node.text;
	    				if (node.children.length > 0){
	    					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
	    				}
	    				return s;
	    			},onContextMenu: function(e,node){//添加右键菜单
	    				 e.preventDefault();
	    				$(this).tree('select',node.target);
	    				$('#tDtmm').menu('show',{
	    					left: e.pageX,
	    					top: e.pageY
	    				}); 
	    			},onClick: function(node){//点击节点
	    				$('#listCancle').edatagrid({
	    					url:'<%=basePath %>drug/pharmacyManagement/templateList.action',
	    					queryParams: {
	    						pid:window.parent.window.getSelected(2),
	    						nodeid:node.id,
	    						nodetext:node.text
	    					}
	    				});
	    			},onLoadSuccess : function(node, data) {//默认选中
						$(this).tree('select',$('#tDt').tree('find', '1').target);
					}
	    		});
	    		
	    		//配药台
	    		$('#listConfirm').edatagrid({
					url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=1&pid='+window.parent.window.getSelected(2),
					selectOnCheck:false,rownumbers:true,idField: 'id',pageSize:20,
		   			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1,fit:true, border:false,
					columns:[[{field:'id',checkbox:'true'},
					          {field:'name',title:'名称', width : '10%'},
					          {field:'property',title:'终端性质', width : '8%',
					        	  formatter:funPropertyMap
							  },
					          {field:'closeFlag',title:'是否关闭', width : '8%',
								  formatter: function(value,row,index){
					        		  if(value==0){
					        			  return '开放'; 
					        		  }else if(value==1){
					        			  return '关闭';
					        		  }
					        	  }  
					          },
					          {field:'alertNum',title:'警戒线（/位）',width : '8%'},
					          {field:'sendWindow',title:'发药窗口', width : '8%',
					        	  formatter: function(value,row,index){
					        		  if(value!=null&&value!=''){
					        			  return terminalMap[value];
					        		  }
					        	  }
					          },
					          {field:'replaceCode',title:'替代终端', width : '8%',
					        	  formatter: function(value,row,index){
					        		  if(value!=null&&value!=''){
					        			  return terminalMap[value];
					        		  }
					        	  }
					          },
					          {field:'refreshInterval1',title:'程序刷新间隔（/秒）', width : '10%'},
					          {field:'autopringFlag',title:'是否自动打印', width : '12%',formatter: function(value,row,index){
				        		  if(value==0){
				        			  return '否'; 
				        		  }else if(value==1){
				        			  return '是';
				        		  }
				        	  }  },
					          {field:'showNum',title:'显示人数（/位）', width : '8%'},
					          {field:'mark',title:'备注',width : '8%'}
					  ]],onDblClickRow:function(rowIndex, rowData){
						  var obj=$('#listCancle').datagrid('getData');
						  a=true;
						  if(obj.rows.length>0){
	 						  $.each(obj.rows,function(i,n){
	 						  	if(n.code==rowData.code){
	 						  		a=false;
	 						  	}
	 						  }); 
	 						 if(a){
								  $('#listCancle').edatagrid('appendRow',{
									  	 code: rowData.code,
									     name: rowData.name,
									     closeFlag:rowData.closeFlag,
									     mark:rowData.mark
									});
							  }else{
								  $.messager.alert('警告','数据已存在，请勿重复添加!');
								  setTimeout(function(){$(".messager-body").window('close')},1500);
							  }
						  }else{
							  $('#listCancle').edatagrid('appendRow',{
								 	 code: rowData.code,
								     name: rowData.name,
								     closeFlag:rowData.closeFlag,
								     mark:rowData.mark
								});
						  }
					  }
				});
	    		
	    		//终端列表
	    		$('#listCancle').edatagrid({
					selectOnCheck:false,rownumbers:true,pageSize:20,
		   			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
		   			autoSave:true,fit:true, 
					columns:[[
					          {field:'id',checkbox:'true'},
					          {field:'code',title:'名称', width : '10%',
					        	  formatter: function(value,row,index){
					        		  if(value!=null&&value!=''){
					        			  return terminalMap[value];
					        		  }
					        	  }  
					          },
					          {field:'closeFlag',title:'是否关闭', width : '10%',
								  formatter: function(value,row,index){
					        		  if(value==0){
					        			  return '开放'; 
					        		  }else if(value==1){
					        			  return '关闭';
					        		  }
					        	  },editor:{type:'combobox',options:{ valueField: 'id',
					        		  textField: 'text', 
					        		  data:[{    
					        			    "id":0,    
					        			    "text":"开放"   
					        			},{    
					        			    "id":1,    
					        			    "text":"关闭"   
					        			}],
					        			required:true,editable:false
					        	  }} 
					          },
					          {field:'mark',title:'备注',width : '50%',editor:{type:'textbox',
					        	  options:{ maxlength:30}
					          	}
					          },
					  ]],
				});
	    	});
	    	
	    	//删除终端
    		function delTerminal(){
    			var obj=$('#listCancle').edatagrid('getChecked');
				var arr =new Array();
				var brr =new Array();
				a=false;
				if(obj.length>0){
					$.each(obj,function(i,n){
						brr[i]=n;
						if(n.id==null){
						}else{
							arr[i]=n.id;
							j=i+1;
							a=true;
						} 
					});
					
					$.messager.confirm('确认','该删除不可恢复，您确认想要删除条'+obj.length+'终端记录吗？',function(r){    
	    			    if (r){
	    			    	//视图删除
	    			    	$.each(brr,function(i,n){
	    			    		$('#listCancle').datagrid('deleteRow',$('#listCancle').edatagrid('getRowIndex',n));
	    					});
	    			    	//后台删除
	    			    	if(a){
	    			    		$.ajax({
		    						url:'<%=basePath%>drug/pharmacyManagement/delTerminal.action',
		    						type:'post',
		    						traditional:true, //数组提交解决方案
		    						data:{'ids':arr},
		    						success:function(data){
		    							if(data=='success'){
		    								//$.messager.alert('警告','删除成功！');
		    							}
		    						},error : function(a,b,c) {
		    		 					//$.messager.alert('警告',"******"+a+"*****"+b+"********"+c);
		    						}
		    					});
	    			    	}
	    			    }    
	    			});  
				}
    		}
    		
	    	//添加模版
	    	function addTemplate(){
	    		Adddilog("添加模版",'<%=basePath%>drug/pharmacyManagement/addTemplateURL.action?type=0');
	    	}
	    	//修改模版名称
    		function editTemplate(){
    			node = $('#tDt').tree('getSelected');
    			$('#nodeid').val(node.id);
    			$('#nodetext').val(node.text);
    			if ($('#tDt').tree('isLeaf', node.target)&&getSelected(1)!=undefined) {
    				Adddilog("修改名称",'<%=basePath%>drug/pharmacyManagement/addTemplateURL.action?type=1');
    			}else{
    				$.messager.alert('警告','该节点不允许修改！');
    				setTimeout(function(){$(".messager-body").window('close')},1500);
    			}
    		}
	    	
	    	//删除模版
	    	function delTemplate(){
	    		if(getSelected(1)==null){
	    			$.messager.alert('提示','请选择具体模版进行操作!');
	    		}else{
	    			$.messager.confirm('确认','该删除不可恢复，您确认想要删除该模版吗？',function(r){ 
	    				if(r){
	    					$.ajax({
								url:'<%=basePath%>drug/pharmacyManagement/delTemplate.action',
								type:'post',
								traditional:true, //数组提交解决方案
								data:{'deptCode':window.parent.window.getSelected(2),'templetCode':getSelected(1),'templetName':getSelected(4)},
								success:function(data){
									if(data=='success'){
										$.messager.alert('警告','删除成功！');
										refresh();
										$('#listCancle').edatagrid('reload');
									}
								}
							});
	    				}
		    		});
	    		}
	    	}
	    	
	    	//保存
    		function saveTemplate(){
				$('#listCancle').edatagrid('acceptChanges');
				$('#terminalJSON').val(JSON.stringify( $('#listCancle').datagrid("getRows")));
				if(getSelected(1)==undefined){
	    			$.messager.alert('警告','请选择具体模版进行操作!');
	    			setTimeout(function(){$(".messager-body").window('close')},1500);
	    		}else if($('#terminalJSON').val()=='[]'){
	    			$.messager.alert('提示','终端列表为空！'); 
	    		}else{
	    			$('#editForm').form('submit',{
				        url:'<%=basePath %>drug/pharmacyManagement/saveTemplate.action',
				        queryParams:{
				        	pid:window.parent.window.getSelected(2),
				        	nodeid:getSelected(1),
				        	nodetext:getSelected(4),
				        },
				        onSubmit:function(){ 
				            return $(this).form('validate');  
				        },  
				        success:function(data){
				        	if(data=='success'){
				        		$.messager.alert('提示',"保存成功！");
				        	}
				        	$('#listConfirm').edatagrid('uncheckAll'); $('#listConfirm').edatagrid('unselectAll');
			        		$('#listCancle').edatagrid('uncheckAll'); $('#listCancle').edatagrid('unselectAll');
			        		$('#listConfirm').edatagrid('reload');
			        		$('#listCancle').edatagrid('reload');
				        },
						error : function(data) {
							$.messager.alert('提示',"保存失败！");	
						}			         
					});
	    		}
			} 
	    	
	    	
    		//渲染性质类型
    		function funPropertyMap(value,row,index){
    			if(value == 0){
    				return propertyMap.get(value);
    			}
    			if(value!=null&&value!=''){
    				return propertyMap.get(value);
    			}
    		}
    		//获取所有性质类型的数组
    		function getPropertyArray(){
    			return [{id:'0',value:'普通'},{id:'1',value:'专科'},{id:'2',value:'特殊'}];
    		}
    		
    		//获取所有性质类型的Map
    		function getPropertyMap(){
    			var PropertyMap2 = new Map();
    			var PropertyArray = getPropertyArray();
    			for(var i=0;i<PropertyArray.length;i++){
    				PropertyMap2.put(PropertyArray[i].id,PropertyArray[i].value);
    			}
    			return PropertyMap2;
    		}
	    	
	    	//执行模版
    		function executeTemplate(){
    			$('#listCancle').edatagrid('acceptChanges');
				$('#terminalJSON').val(JSON.stringify( $('#listCancle').datagrid("getRows")));
				if(getSelected(1)==null){
					$.messager.alert('提示','请选择要执行的模版');
					setTimeout(function(){$(".messager-body").window('close')},1500);
				}else if($('#terminalJSON').val()=='[]'){
					$.messager.alert('提示','请添加执行信息');
					setTimeout(function(){$(".messager-body").window('close')},1500);
				}else{
					$.messager.confirm('确认','您确认执行该模版吗？',function(r){ 
	    				if(r){
							$.ajax({
						        url:'<%=basePath %>drug/pharmacyManagement/executeTemplate.action',
						        data:{terminalJSON:$('#terminalJSON').val(),pid:window.parent.window.getSelected(2),
						        	nodeid:getSelected(1),
						        	nodetext:getSelected(4)},
						        type:'post',
						        onSubmit:function(){ 
						            return $(this).form('validate');  
						        },  
						        success:function(data){
						        	if(data == 'success'){
						        		$('#listConfirm').edatagrid('uncheckAll'); $('#listConfirm').edatagrid('unselectAll');
						        		$('#listCancle').edatagrid('uncheckAll'); $('#listCancle').edatagrid('unselectAll');
						        		$('#listConfirm').edatagrid('reload');
						        		$('#listCancle').edatagrid('reload');
						        		$.messager.alert('提示',"执行成功！");	
						        	}else if(data.state == '0'){
						        		$.messager.alert('提示',data.message);
						        	}
						        },
								error : function(data) {
									$.messager.alert('提示',"执行失败！");	
								}			         
							});
	    				}
					});
				}
	    	}
	    	
    		/**
    		 * tag=0获取nodetype
    		 * tag=1获取选中节点ID
    		 * tag = 2 父节点ID  
    		 * tag=3 判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
    		 * tag = 4 所选节点名称
    		 */
    		function getSelected(tag) {
    			var node = $('#tDt').tree('getSelected');//获取所选节点
    			if (node != null) {
    				var Pnode = $('#tDt').tree('getParent', node.target);
    				if (Pnode) {
    					if (tag == 0) {
    						var nodeType = node.nodeType;
    						return nodeType;
    					}
    					if (tag == 1) {
    						var id = node.id;
    						return id;
    					}
    					if (tag == 2) {
    						var pid = Pnode.id;
    						return pid;
    					}
    					if (tag == 3) {
    						if ($('#tDt').tree('isLeaf', node.target)) {//判断是否是叶子节点
    							var id = node.id;
    							return id;
    						} else {
    							return 1;
    						}
    					}
    					if(tag==4){
    						var text = node.text;
    						return text;
    					}
    				}
    			} else {
    				return null;
    			}
    		}
    		 
    		
    		//加载dialog
			function Adddilog(title, url) {
				$('#dd').dialog({    
				    title: title,    
				    width: '30%',    
				    height:'30%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//打开dialog
			function openDialog() {
				$('#dd').dialog('open'); 
			}
			//关闭dialog
			function closeDialog() {
				$('#dd').dialog('close');  
			}
    		
    		function refresh(){//刷新树
<%--     	   		$('#tDt').tree('options').url = '<%=basePath%>drug/pharmacyManagement/templateTree.action?pid='+window.parent.window.getSelected(2);  --%>
    			$('#tDt').tree('reload'); 
    		}
    	   	function expandAll(){//展开树
    			$('#tDt').tree('expandAll');
    		}
    	   	function collapseAll(){//关闭树
    			$('#tDt').tree('collapseAll');
    		}
	    </script>
	</head>
	<body class="easyui-layout" data-options="border:false">   
	    <div data-options="region:'north'" style="height:40px  ;">
			<table style="width:100%;border:none;padding:4px;">
				<tr> 
					<td style="font-size:14px" >
					<shiro:hasPermission name="${menuAlias}:function:add">
						<a href="javascript:addTemplate()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">新增模版</a>
					</shiro:hasPermission>	
					<shiro:hasPermission name="${menuAlias}:function:delete">		
						<a href="javascript:delTemplate()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除模版</a>
					</shiro:hasPermission>	
					<shiro:hasPermission name="${menuAlias}:function:delete">		
						<a href="javascript:delTerminal()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除终端</a>
					</shiro:hasPermission>	
						<a href="javascript:executeTemplate()" class="easyui-linkbutton" data-options="iconCls:'zhixing'">执行模版</a>
						<a href="javascript:saveTemplate()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="p" data-options="region:'west',tools:'#toolSMId',title:'模版'" style="width:10%;padding:5px">
		    <div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" d class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
				<input type="hidden" id="nodeid"/>
				<input type="hidden" id="nodetext"/>
				<ul id="tDt">数据加载中...</ul>
			</div>
				<div id="tDtmm" class="easyui-menu" style="width:100px;">
					<div onclick="addTemplate()" data-options="iconCls:'icon-add'">添加模版</div>
					<div onclick="editTemplate()" data-options="iconCls:'icon-edit'">修改名称</div>
					<div onclick="delTemplate()" data-options="iconCls:'icon-remove'">移除模版</div>
				</div>
	    </div> 
		<div data-options="region:'center',border:false" style="">
	    	<div style="width: 100%;height: 50%;">
				<table id="listConfirm" style="width: 100%;" title="配药台"></table>
			</div>
			<div id="listCancleDiv" style="width: 100%;height: 50%;" >
				<table id="listCancle"  style="width: 100%;" title="终端列表"></table>
				<form id="editForm" method="post">
					<input type="hidden" id="terminalJSON" name="terminalJSON" >
				</form>
			</div>
	    </div>
	    <div id="dd"></div>
	</body>
</html>