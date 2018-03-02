<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;overflow: auto;">
      <div data-options="region:'north'" style=" height:30px;border: 0px;">
    	<input type="text" id="searchTreeInpId" style="width: 200px;" onkeydown="KeyDown()"/>
   		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
		<a style="float: right;" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="appendRow()">确定</a>
	  </div>
	  <div data-options="region:'center'" style="width: 100%;border: 0px;">
		<ul id="tDtpt"></ul> 
	  </div> 
		<input type="hidden" id="tDeptId" >
		<input type="hidden" id="deptidName"  value="${deptidName}">
		<input type= "hidden" id = "flg" value="${flg}">
		<input id="inoutdeptids" type="hidden" value="${inoutdeptids}">
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
    <script >
    	function searchTreeNodes(){
        	var searchText = $('#searchTreeInpId').val();
        	$("#tDtpt").tree("search", searchText);
  		}
	  //加载部门树
	  $(function(){
		  $('#searchTreeInpId').textbox({});
		  bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
	  			var url = "";
	  				$('#tDtpt').tree({    
					    url:"<%=basePath%>baseinfo/department/treeDepartmen.action?treeAll=true",
					    method:'get',
					    animate:true,
					    lines:true,
					    checkbox:true,
					   	onlyLeafCheck:true,
					   	fit:true,
					    formatter:function(node){//统计节点总数
							var s = node.text;
							if (node.children && node.children.length > 0){
								s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
							}
							return s;
						},onCheck: function(node, checked){
							var ids='';
							var node=$('#tDtpt').tree('getChecked');
			       				for (var i = 0; i < node.length; i++) {
					            var id=node[i].id+',';
					            ids+=id;			
							}
						$("#tDeptId").val(ids);
						}
					}); 
			   	
		});
		//动态添加部门科室到datagrid
		function appendRow(){
			var depts = $("#tDtpt").tree("getChecked");
			var flg = $("#flg").val();
			var rows = '';
			var flag = 1;
			var deptId = $('#deptidName').val();
			if(!depts){
				$.messager.alert('操作提示',"请选择要添加的科室！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	       		return;
			}
			if(flg == 1){
				rows = $("#indept").datagrid('getRows');
			}else if(flg == 2){
				rows = $("#outdept").datagrid('getRows');
			}
	   		for(var i = 0; i < depts.length; i ++){
	   			if(deptId == depts[i].id){
	   				continue;
	   			}
	   			for(var j = 0 ; j < rows.length; j ++){
	   				if(depts[i].id == rows[j].deptCode){
	   					flag = 0;
	   					break;
	   				}else{
	   					flag = 1;
	   				}
	   			}
	   			if(flag == 1){
	   				if(flg == 1){
	   					$("#indept").datagrid('appendRow',{
		   					deptCode : depts[i].id,
		   					deptName : depts[i].text,
		   					objectDept : depts[i].id,
		   					deptId : depts[i].id
		   				});
	   				}else if(flg == 2){
	   					$("#outdept").datagrid('appendRow',{
		   					deptCode : depts[i].id,
		   					deptName : depts[i].text,
		   					objectDept : depts[i].id,
		   					deptId : depts[i].id
		   				});
	   				}
	   				
	   			}
			}
			$('#addDepts').dialog('close');
		}
		//保存到数据库
		function save(){
			var type = "";
	  		if($('#modelWeeks').val()=="indepts"){
	  			type = 1;
	  		}else if($('#modelWeeks').val()=="outdepts"){
	  			type = 2;
	  		}
	  		if($("#inoutdeptids").val()!=null && $("#inoutdeptids").val()!=""){
	  			
	  		}else{
		  		var deptId = getSelected();
		        if(deptId==null||deptId==""){
		        	$.messager.alert('操作提示',"请在左侧选择具体科室！");
		        	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		       		return;
		       	}
		        $.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
		  		$.ajax({
					url: "<%=basePath%>drug/inoutdept/saveInoutdept.action?deptId="+getSelected()+"&inoutDeptId="+$("#tDeptId").val()+"&type="+type,
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert('提示','保存成功');
						$('#'+$('#modelWeeks').val()).edatagrid('reload');
						$('#addDepts').dialog('close');
					}
				});
	  		}
	  		
		}
		</script>
	</body>
</html>
