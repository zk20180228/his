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
	<div id="divLayout" class="layoutFix">
	    		<input type="hidden" id="uid"  value="${uid }">
				<div style="padding-bottom:  15px">
					<table>
							<tr>
								<td>查询条件：</td>
								<td>
									<input type="text" id="searchParam" class="easyui-textbox" data-options="prompt:'科室名'" style="width:145px;"/>
								</td>
								<td>
									<a id="search" href="javascript:void(0)" onClick="getTreeNode()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-top: 2px;">搜索</a>
								</td>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="submitRoleDeptForm()" >添加</a>
								</td>
							</tr>
						</table>
			    	
				</div>
				 				
	</div> 
	<div class="layoutTree" style="overflow:auto;">
		<ul id="tDt"></ul>
	</div>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
		<script type="text/javascript">
			 $(function(){
				 $(".layoutTree").height($("#dialogDivId").height()-$(".layoutFix").height()-10);
				 $('#tDt').tree({   
		 			    onlyLeafCheck:true,
					    checkbox:true,
					    fit:true,
		                url:"<%=basePath%>baseinfo/department/treeDepartmen.action",
		                onSelect:function(node){
		                	if (node.children.length == 0) {	
		                		$(this).tree('check', node.target);
		                	}else{
		                		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		                	}
		                	
						},
	             });
				 window.addEventListener("resize", function () {
		    	        setTimeout(function () {
		    	        	$(".layoutTree").height($("#dialogDivId").height()-$(".layoutFix").height()-2);
		    	        }, 300)
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
			   		$.messager.confirm('确认', "确定保存修改信息?",function(res){
			   			if(res){
					        $.post("<c:url value='/sys/saveUserDeptTree.action'/>",{"uId":$('#uid').val(),"dId":s},function(result){
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
               		 });
                }else{
                	$.messager.alert('提示','请选择要添加的科室！');
                	close_alert();
                }
			}
		    setTimeout(function(){
	        	bindEnterEvent('searchParam',getTreeNode,'easyui');
	        },200);
		    function getTreeNode(){
		    	var searchText = $('#searchParam').textbox('getValue');
		    	$("#tDt").tree("search", searchText);
		    }

		</script>
	</body>
</html>