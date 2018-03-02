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
	<div id="divLayout">
	    		<input type="hidden" id="rid"  value="${rid }">
				<div style="padding-bottom:  15px">
			    	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="submitRoleDeptForm()" >添加</a>
				</div>
				<ul id="tDt"></ul> 
				
	</div> 
		<script type="text/javascript">
		 $(function(){
			              $('#tDt').tree({   
			 			    onlyLeafCheck:true,
						    checkbox:true,
			                  url:"<c:url value='/baseinfo/department/treeDepartmen.action'/>?param="+new Date().getTime(),   
			                  onBeforeExpand:function(node,param){
			                	  if(node.attributes.isUser=="0"){
			                		  $('#tDtRole').tree('options').url = "<c:url value='/sys/treeUserRole.action'/>?pid=" + node.id;
			                	  }else{
			                		  return false;
			                	  }
			                 } 
			             });   
			         });
			//表单提交
		   	function submitRoleDeptForm(){
		   		var nodes = $('#tDt').tree('getChecked');
                var s = '';
                if (nodes.length>0) {
	                for (var i = 0; i < nodes.length; i++) {
	                    if (s != '')
	                        s += ',';
	                    s += nodes[i].id;
	                }
			   		if(confirm("确定保存修改信息?")){
				        $.post("<c:url value='/sys/saveRoleDeptTree.action'/>",{"rId":$('#rid').val(),"dId":s},function(result){
				   			if(result=="success"){
				   				$('#dialogDivId').dialog('destroy');
				   				editedit();
				   			}else{
				   				$.messager.alert('提示','操作失败!');
				   			}
				   		});
			        }else{
			        	return;
			        }
                }else{
                	$.messager.alert('提示','请选择要添加的用户！');
                	close_alert();
                }
			}
			
           //查询树
			function searchTree(){
	   			$.ajax({
					url: "<c:url value='/baseinfo/employee/searchTree.action'/>?searcht="+encodeURI(encodeURI($('#searchTree').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
					type:'post',
					success: function(data) {
						$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
						var node = $('#tDt').tree('find',data);
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