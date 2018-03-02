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
	<div class="easyui-layout" style="width:100%;height:100%;"  >
			<div >
			<shiro:hasPermission name="${menuAlias}:function:save">
				<a href="javascript:void(0);" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="submitRoleDeptForm()">保存</a>
			</shiro:hasPermission >
			</div>
			<ul id="tDt" style="padding-top: 10px"></ul>
			<input type="hidden" id="rid" value="${rid }">
	</div>
	<script type="text/javascript">
			var deptid="";
			//加载部门树
		   	$('#tDt').tree({    
			    url:"<c:url value='/baseinfo/department/treeDepartmen.action'/>",
			    method:'get',
			    animate:true,
			    lines:true,
			    onlyLeafCheck:true,
			    checkbox:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onClick: function(node){//点击节点
			      deptid=node.id;
				}
			}); 
		
			//表单提交
		   	function submitRoleDeptForm(){
		   		var nodes = $('#tDt').tree('getChecked');
		   		if (nodes.length>0) {
	                var s = '';
	                for (var i = 0; i < nodes.length; i++) {
	                    if (s != '')
	                        s += ',';
	                    s += nodes[i].id;
	                }
			   		if(confirm("确定添加选中角色?")){
				        $.post("<c:url value='/sys/saveRoleDeptTrees.action'/>",{"rId":$('#rid').val(),"dId":s},function(result){
				   			if(result=="success"){
				   				$.messager.alert('提示','操作成功!');
				   				$('#dialogDivId').dialog('destroy'); 
				   				AddOrShowEasts('EditForm',"<c:url value='/sys/querUserRoleDept.action'/>?id="+getIdUtil("#Rolelist")); 
				   				
				   			}else{
				   				$.messager.alert('提示','操作失败!');
				   				}
				   		});
			        }else{
			        	return;
			        }
		        }else{
		        	$.messager.alert('提示','请选择要添加的科室！');
		        	close_alert();
		        }
			}
			
		</script>
</body>
</html>