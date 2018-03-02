<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp" %>
<title>物资供领科室关系</title>
<script type="text/javascript">
var DeptTypeMap=new Map();
	$.ajax({
		url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=depttype",
		async:false,
		success: function(data) {
		 	var type = data;
		 	for(var i=0;i<type.length;i++){
		 		DeptTypeMap.put(type[i].encode,type[i].name);
			}
		}
	});
</script>
</head>
<body class="easyui-layout"> 
<!-- 物资供领科室关系 -->  
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
	    <div data-options="region:'north'" style="width: 100%; height: 50px; padding-top: 5px;border-top:0">
	    	<div style="text-align: left; padding: 5px 5px 5px 0px;">
	    		<a href="javascript:save()" class="easyui-linkbutton" style="margin:0 0 0 1% " data-options="iconCls:'icon-save'">保存</a>
	    		<a href="javascript:add()" class="easyui-linkbutton" style="margin:0 0 0 1% " data-options="iconCls:'icon-add'">新增科室</a>
	    		<a href="javascript:del()" class="easyui-linkbutton" style="margin:0 0 0 1% " data-options="iconCls:'icon-remove'">删除科室</a>
<!-- 	    		<a href="javascript:void(0)" class="easyui-linkbutton" style="margin:0 0 0 1% " data-options="iconCls:'icon-2012080412111',disabled:true ">重新排序</a> -->
	     	</div>
	    </div>   
		<div id="p" data-options="region:'west',tools:'#toolSMId'" title="物资供领科室管理" style="width:20%; height:80%;padding: 0px; overflow: hidden;">
<!-- 	    	<input id="searchDept" class="easyui-textbox" style="width: 150px;" data-options="prompt:'拼音,五笔,自定义,编码,名称'"  /> -->
<!-- 					<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 						 data-options="iconCls:'icon-search'">查询</a> -->
			<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'科室名'" style="width: 200px;"/>
			   <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>

	    	<div id="toolSMId">
		    	<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				
			</div>
			<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
				<ul id="tDt">数据加载中...</ul>
			</div>
	    </div>   
	    <div data-options="region:'center',fit:true,border:false" style="width: 20%; height: 70%;">
			<div id="tt" class="easyui-tabs" data-options="fit:true">
				 <div title="入库科室" data-options="fit:true">
				 	<table id="list1" style="width: 100%;" class="easyui-datagrid"
						data-options="method:'post',rownumbers:true,fit:true,
<!-- 						idField: 'id', -->
						striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,
<!-- 						pagination:true, -->
						autoSave:true,
						height:$('body').height()-78-30-27-40-3
						">
						<thead>
							<tr>
								<th data-options="field:'deptCode',checkbox:true"
									style="width: 8%; text-align: center"></th>
								<th data-options="field:'sortid',editor:{type:'numberbox',options:{min:0,max:9999}}"
									style="width: 8%; text-align: center" >排序</th>
								<th data-options="field:'objectDeptName'"
									style="width: 8%; text-align: center">科室名称</th>
								<th data-options="field:'objectDeptCode',hidden:true"
									style="width: 8%; text-align: center">供物资或领物资单位码</th>
								<th data-options="field:'objectDeptType',formatter:funDeptType"
									style="width: 8%; text-align: center">科室类型</th>
								<th data-options="field:'mark'"
									style="width: 8%; text-align: center">备注</th>
							</tr>
						</thead>
					</table>
				 </div> 
				 <div title="出库科室">
				 	<table id="list2" style="width: 100%;" class="easyui-datagrid"
						data-options="method:'post',rownumbers:true,fit:true,
<!-- 						idField: 'id', -->
						striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,
<!-- 						pagination:true, -->
						autoSave:true,
						height:$('body').height()-78-30-27-40-3
						">
						<thead>
							<tr>
								<th data-options="field:'deptCode',checkbox:true"
									style="width: 8%; text-align: center"></th>
								<th data-options="field:'sortid',editor:{type:'numberbox',options:{min:0,max:9999}}"
									style="width: 8%; text-align: center" >排序</th>
								<th data-options="field:'objectDeptName'"
									style="width: 8%; text-align: center">科室名称</th>
								<th data-options="field:'objectDeptCode',hidden:true"
									style="width: 8%; text-align: center">供物资或领物资单位码</th>
								<th data-options="field:'objectDeptType',formatter:funDeptType"
									style="width: 8%; text-align: center">科室类型</th>
								<th data-options="field:'mark'"
									style="width: 8%; text-align: center">备注</th>
							</tr>
						</thead>
					</table>
				 </div>   
			</div>
	    </div> 
	    <div id="dialog"></div>  
	     
	</div> 
</body> 
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
	//科室下拉框及时定位查询 
	  function searchTreeNodes(){
         var searchText = $('#searchTreeInpId').textbox('getValue');
         $("#tDt").tree("search", searchText);
      }
	  $(function(){
			bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
		});
// $('#searchDept').combobox({ 
// 			valueField:'id',    
// 		    textField:'deptName', 
// 		    mode:'remote',
// 		    url : "<c:url value='/baseinfo/department/findDeptsByDepartmentContact.action'/>", 
// 		    onSelect:function(rec){
// 		    	var node = $('#tDt').tree('find', rec.id);
// 		    	if(node!=null){
// 		    		var pid = node.attributes.pid;
// 		    		var pnode = $('#tDt').tree('find',pid);
// 		    		$('#tDt').tree('expand', pnode.target).tree('scrollTo', node.target).tree('select',
// 		    				node.target);
// 		    	}
// 			}
// 		});
		
	var winH=$("body").height();
	$('#p').height(winH-78-30-27-40-3);    //78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
	$('#tDt').height(winH-78-30-27-40-3);


	
	$(function(){
		trr();
		setTimeout(function(){
			tabDatagrid();
		},200);
		
	})
	
	//渲染科室类型
	function funDeptType(value,row,index){
		if(value!=null&&value!=''){
			return DeptTypeMap.get(value);
		}
	}
	
	//物资管理树
	function trr(){
		$('#tDt').tree({
				url : '<%=basePath %>material/matInoutDept/treeList.action',
				method:'get',
				lines : true,
				cache : false,
				animate : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children) {
						s += '&nbsp;<span style=\'color:blue\'>('
								+ node.children.length + ')</span>';
					}
					return s;
				},onClick : function(node) {//点击节点
					tabDatagrid();
				},onLoadSuccess : function(node, data) {//默认选中
					//$('#tDt').tree('select',$('#tDt').tree('find', '05').target);
					if(data.length>0){
						$('#tDt').tree('collapseAll');
					}
				},onBeforeCollapse:function(node){
					if(node.id=="05"){
						return false;
					}
				}
		});
	}
	
	function tabDatagrid(){
		var rows = $('#tDt').tree('getSelected');//获取选中节点 
		if(rows!=null){
			var deptCode=$('#tDt').tree('getSelected').attributes.deptCode;
			$('#list1').edatagrid({ 
				url:'<%=basePath %>material/matInoutDept/queryList.action?type=1&deptCode='+deptCode,
				onBeforeLoad:function(){
					$('#list1').datagrid('clearChecked');
					$('#list1').datagrid('clearSelections');
				},
			});
			$('#list2').edatagrid({ 
				url:'<%=basePath %>material/matInoutDept/queryList.action?type=2&deptCode='+deptCode,
				onBeforeLoad:function(){
					$('#list2').datagrid('clearChecked');
					$('#list2').datagrid('clearSelections');
				},
			});
		}
	}
	
	//添加操作
	function add(){
   		tab = $('#tt').tabs('getSelected');
		index = $('#tt').tabs('getTabIndex',tab);
		var deptCode=$('#tDt').tree('getSelected').attributes.deptCode;
		if(deptCode!=null&&deptCode!=''){
			if(index==0||index==null){
				Adddilog('请勾选入库科室','<%=basePath%>material/matInoutDept/matInoutDeptAddURL.action');
			}else{
				Adddilog('请勾选出库科室','<%=basePath%>material/matInoutDept/matInoutDeptAddURL.action');
			}
		}else{
			$.messager.alert('提示','请选择具体科室后添加');
		}
	}
	
	//删除
	function del(){
		var deptCode=$('#tDt').tree('getSelected').attributes.deptCode;
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		var stockClass;
		if(index==0){
			var obj=$('#list1').datagrid('getChecked');
			stockClass = 1;
		}else{
			var obj=$('#list2').datagrid('getChecked');
			stockClass = 2;
		}
		var arr =new Array();
		var brr =new Array();
		var a=false;
		if(obj.length>0){
			$.each(obj,function(i,n){
				brr[i]=n;
				if(n.id==null){
				}else{
					arr[i]=n.objectDeptCode;
					j=i+1;
					a=true;
				} 
			});
			$.messager.confirm('确认','您确认想要删除条'+obj.length+'终端记录吗？',function(r){    
			    if (r){
			    	//视图删除
			    	if(index==0){
			    		$.each(brr,function(i,n){
				    		$('#list1').datagrid('deleteRow',$('#list1').edatagrid('getRowIndex',n));
						});    
					}else{
						$.each(brr,function(i,n){
				    		$('#list2').datagrid('deleteRow',$('#list2').edatagrid('getRowIndex',n));
						});
					}
			    	
			    	//后台删除
			    	if(a){
			    		$.ajax({
    						url:'<%=basePath%>material/matInoutDept/matInoutDeptDel.action',
    						type:'post',
    						traditional:true, //数组提交解决方案
    						data:{'objectDeptCodes':arr,'deptCode': deptCode,'stockClass': stockClass},
    						success:function(data){
    							if(data=='success'){
    								$.messager.alert('提示','删除成功');
    							}
    						}
    					});
			    	}
			    }    
			});  
		}else{
			$.messager.alert('提示','请勾选要删除的行');
		}
	}
	
	//保存数据
	function save(){
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		$('#list1').edatagrid('acceptChanges');
		$('#list2').edatagrid('acceptChanges');
		var node = $('#tDt').tree('getSelected');
		if(node == null || node.attributes.deptid == null){
			$.messager.alert('警告','请选中要保存的科室！！');
			return false;
		}
		var deptCode = node.attributes.deptCode;
		var deptName = node.text;
		var list1Json=JSON.stringify( $('#list1').edatagrid("getRows"));
		var list2Json=JSON.stringify( $('#list2').edatagrid("getRows"));
		if(list1Json!='[]'||list2Json!='[]'){
			$.ajax({
				url:'<%=basePath%>material/matInoutDept/matInoutDeptAdd.action',
				type:'post',
				data:{'list1Json':list1Json,'list2Json':list2Json,'deptCode':deptCode,'deptName':deptName},
				success:function(obj){
					if(obj.type!='0'){
						if(obj.type=='1'){
							$.messager.alert('警告','在入库科室中'+'"'+obj.name+'"'+'已存在'); 
						}else{
							$.messager.alert('警告','在出库科室中'+'"'+obj.name+'"'+'已存在'); 
						}
					}else{
						$.messager.alert('提示','保存成功');
						//弹出提示成功的信息,1秒之后自动关闭提示框 
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						if(index == 0){
							$('#list1').edatagrid('load');
						}else{
							$('#list2').edatagrid('load');
						}
					}
				}
			});
		}
	}
	
	function refresh() {//刷新树
		$('#tDt').tree('reload');
	}
	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}
	
	//加载dialog
	function Adddilog(title, url) {
		$('#dialog').dialog({    
		    title: title,    
		    width: '40%',    
		    height:'50%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
	   });    
	}
	//打开dialog
	function openDialog() {
		$('#dialog').dialog('open'); 
	}
	//关闭dialog
	function closeDialog() {
		$('#dialog').dialog('close');  
	}
	//科室下拉定位查询 
// 	$('#deptSearch').combobox({
// 		url : "<c:url value='/outpatient/scheduleModel/getDeptByQ.action'/>",
// 		valueField : 'deptCode',
// 		textField : 'deptName',
// 		mode:'remote',
// 		onSelect:function(rec){
// 			var code=rec.deptCode;
// 			var node = $('#tDt').tree('find',code);
// 			if(node!=null){
// 				var pid= node.attributes.pid;
// 				var pnode= $('#tDt').tree('find',pid);
// 				$('#tDt').tree('expand', pnode.target).tree('scrollTo', node.target).tree('select',
// 						node.target);
// 				$('#divLayout').layout('remove','east');
// 				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
// 					$('#deptId').val(node.id);
// 					$('#deptName').val(node.text);
					
// 				}else{
// 					$('#deptId').val('');
// 				}
				
// 				$('#divLayout').layout('remove','east');
// 				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
// 					$('#deptId').val(node.id);
// 					$("#deptName").val(node.text);
// 				}else{
// 					$('#deptId').val('');
// 				}
// 				loadDatagrid(getSelected(),$('#rq').val());
// 			}
// 		}
// 	})
	
</script>  
</html>