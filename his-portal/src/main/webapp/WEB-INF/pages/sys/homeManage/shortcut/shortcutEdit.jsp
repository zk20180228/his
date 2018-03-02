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
    <div class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;">
		<a style="float: right;" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">确定</a>
		<ul id="tDtpt"></ul>  
		<input type="hidden" id="MenuId" >
	</div>
    <script >
	  //加载部门树
	  $(function(){
	  			var url = "";
	  				$('#tDtpt').tree({    
					    url:"<%=basePath%>sys/queryRoleMenuList.action",
					    method:'get',
					    animate:true,
					    lines:true,
					    checkbox:true,
					   	onlyLeafCheck:true,
					    formatter:function(node){//统计节点总数
							var s = node.text;
							if (node.children){
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
						$("#MenuId").val(ids);
						}
					}); 
		});
		//保存到数据库
		function save(){
	  		if($("#MenuId").val()==null || $("#MenuId").val()==""){
	  			$.messager.alert('提示','请选择要添加的栏目');
	  			close_alert();
	  		}else{
		        $.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
		  		$.ajax({
					url: "<%=basePath%>sys/shortcut/saveShortcutMenu.action?shortcutMenuId="+$("#MenuId").val(),
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert('提示','保存成功');
						$('#list').datagrid('reload');
						$('#menuWin').dialog('close');
					}
				});
	  		}
	  		
		}
		</script>
	</body>
</html>
