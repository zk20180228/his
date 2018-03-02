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
		<div class="easyui-layout" fit="true">
		<div data-options="region:'west'" title="组套管理" style="width:200px;">
			<ul id="DHtree" class="easyui-tree" data-options="url:'departmentAction_tree.do?dhtree=dhtree',method:'post',animate:true,lines:true"></ul>
		</div>
		<div data-options="region:'center',iconCls:'icon-ok'">
		<div id="tabsList" class="easyui-tabs"> 
		 	<input type="hidden" value="${step}" id="step"></input>
		 	<input type="hidden" value="${id }" id="id" ></input>
	     	<div title="列表" >
	     		<div id="accordionList" class="easyui-accordion" >   
				    <div title="查询" data-options="iconCls:'icon-search'">   
					 	<input type="hidden" value="${step}" id="step" ></input>
					 	<input type="hidden" value="${id }" id="id" ></input>
				     	<div id="searchPanel" style="padding:2px 5px;">
							名称:<input id="u_name" class="easyui-textbox" style="height:23px" name="sqr">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
						</div>
	    			</div>   
				</div>  
		   			
		   		<table id="list" title="列表" 
					data-options="rownumbers:true,singleSelect:false,pagination:true,pageList: [2,3,4,5],total:2,pageSize:2,url:'queryStack.action',method:'post',fitColumns:'true'">
					<thead>
						<tr>
							<th field="ck" checkbox="true" ></th>
							<th data-options="field:'stackName'" style="width:7%">名称</th>
						</tr>
					</thead>
				</table>
			</div>
			<div title="编辑" data-options="closable:false,cache:false">
				<jsp:include page="${request.contextPath}/editStack.action'">
			</div>
			<div title="查看" data-options="closable:false">
				<jsp:include page="${request.contextPath }/viewStack.action"></jsp:include>
			</div>
		</div>
		</div>
		</div>
	</body>
	<script type="text/javascript">
	//加载页面
		$(function(){
			var addtag;
			//disableTabs("#tabsList",4);
			//添加事件
			$('#list').datagrid({
				onLoadSuccess: function (data) {//默认选中
		            var rowData = data.rows;
		            $.each(rowData, function (index, value) {
		            	if(value.id == $('#id').val()){
		            		$("#list").datagrid("checkRow", index);
		            	}
		            });
		        },onDblClickRow: function (rowIndex, rowData) {//双击查看
					window.location="<%=basePath%>/viewStack.action?id="+rowData.id;
			    }
			});
			
			
		
			//添加页签切换操作
			$('#ui').tabs({
				border: false,
				onSelect: function(title){
					if(title=='查看'){
						var id = getId("notNull");
						window.location="<%=basePath%>/viewStack.action?id="+id;
					}
					if(title=='编辑'){
						var id = getId("notNull");
						window.location="<%=basePath%>/editStack.action?id="+id;
					}
				}
		   	});
			
			//添加操作按钮
			var pager = $('#list').datagrid().datagrid('getPager');	
			pager.pagination({
				idField:'id',
				buttons:[{
					text:'查看',
					iconCls:'icon-search',
					handler:function(){
						look();
						disabletabs("#tabsList",4);
					}
				},'-',{
				    text:'添加',
					iconCls:'icon-add',
					handler:function(){
					 $('#myform_add').form('clear');   
					 $("#tt").tabs('select', "编辑");
					 disabletabs("#tabsList",4);
					 addtag= 1; //用来判断是否是天价操作
					}
				},'-',{
					text:'修改',	
					iconCls:'icon-edit',
					handler:function(){
					 getUserID();
					 disabletabs("#tabsList",4);
					 addtag = 0;
					}
				},'-',{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						clickbox();
					}
				},'-',{
					text:'权限',
					iconCls:'icon-redo', // //获取用户id进行该用户权限查询
					handler:function(){
						  var row = $("#dg").datagrid("getSelections");  
				   		  if(row.length!=1){
				   		  	$.messager.alert("操作提示", "请选择一条记录！","warning");
				   		 setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
				   		  	return null;
				   		  }else{
				  			   $("#tt").tabs('select', "权限");
							   disabletabs("#tt",4);
					   		   $("#tgg").datagrid('options').url = "userAction_getUserPermission.do?id="+getIdUtil("#dg");
					   		   $("#tgg").datagrid('reload');
				  		  }
					}
				}]
			});	
			$("#dg").datagrid('options').url = "userAction_userlist.do?tag=1";
			$("#dg").datagrid('reload');
			
		});		
		
		//获得选中id	
		function getId(parameter){
			var row = $("#lb").datagrid("getSelections");  
			var i = 0;   
			if(parameter=='single'){//获得单个id
			    if(row.length<1){
			    	$.messager.alert('提示','请选择一条记录！');
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			       	return null;
			    }else if(row.length>1){
			    	$.messager.alert('提示','只能选择一条记录！');
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			    	return null;
			    }else{ 
			    	var id = ""; 
				  	for(i;i<row.length;i++){    
				  		id += row[i].id; 
				      	return id;
					}
			  	} 	
			}else if(parameter=='plurality'){//获得多个id
			    if(row.length<1){
			    	$.messager.alert('提示','请至少选择一条记录！');
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			       	return null;
			    }else{  
			    	var ids = ""; 
				  	for(i;i<row.length;i++){   
				  		ids += row[i].id+","; 
					}
				  	return ids;
			  	} 
			}else if(parameter=='notNull'){//至少获得一个id
				var id = ""; 
			    if(row.length<1){//如果没有选择数据，默认选中第一行数据
			    	$('#lb').datagrid('selectRow', 0);
			    	var row = $("#lb").datagrid("getSelections");  
			    }
			    id += row[0].id; 
			    return id;
			}else{
				$.messager.alert('提示','参数无效！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return null;
			}
		}
	
		//时间处理
		function formattime(val) {  
		    var year=parseInt(val.year)+1900;  
		    var month=(parseInt(val.month)+1);  
		    month=month>9?month:('0'+month);  
		    var date=parseInt(val.date);  
		    date=date>9?date:('0'+date);  
		    var hours=parseInt(val.hours);  
		    hours=hours>9?hours:('0'+hours);  
		    var minutes=parseInt(val.minutes);  
		    minutes=minutes>9?minutes:('0'+minutes);  
		    var seconds=parseInt(val.seconds);  
		    seconds=seconds>9?seconds:('0'+seconds);  
		    var time=year+'-'+month+'-'+date+' '+hours+':'+minutes+':'+seconds;  
		        return time;  
		}  
		
		/*
		* 选项卡切换时激活当前选中，禁用未选中
		*
		*
		*/
		function disableTabs(tableID, arrLenth){
			var arr  = []; 
			for(i = 0; i<arrLenth;i++){
				arr[i] = i;
			}
			var tab = $(tableID).tabs('getSelected');
			var index = $(tableID).tabs('getTabIndex',tab);
			var tab = $(tableID).tabs('getSelected');
			var index = $(tableID).tabs('getTabIndex',tab);
			arr.splice($.inArray(index,arr),1);
			$.each(arr,function(n,value) {  
			 	$(tableID).tabs('disableTab', value);   	
			});
			$(tableID).tabs('enableTab', index);
		}
		
		function searchView(){
			$("#select").slideDown("slow");
		}
				
		//查询
   		function searchFrom(){
   		    var u_name =	$('#u_name').val();
   		    var u_depar =$('#u_depar').val();
   		     
		    $('#lb').datagrid('load', {
				name: u_name,
				address:u_depar 
			});
		}
		$(function(){
        	$("#accordingList").accordion('getSelected').panel('collapse');
		});
		//点击树获取该节点ID	   
		$('#DHtree').tree({   		   
	        onClick: function(node){
	         $("#tt").tabs('select', "列表");
	         disabletabs("#tt",4);
				//重新赋值url
		       $("#dg").datagrid('options').url = "<%=path%>/userAction_userlist.do?fatherId=" + node.id;   
		       //重新加载 	 
			   $("#dg").datagrid('reload');					
			}
		});
	</script>
</html>