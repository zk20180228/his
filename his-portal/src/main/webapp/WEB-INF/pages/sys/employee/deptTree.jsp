<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
    <div class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;" >
		<input type="text" id="searchParam" class="easyui-textbox" data-options="prompt:'科室名'" style="width: 150px;"/>
   		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="getTreeNode()" style="margin-top: 2px;">搜索</a>
		<ul id="tDtpt"></ul>  
	</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
		  //加载部门树
		 $(function(){
			$('#tDtpt').tree({    
			    url:"<c:url value='/baseinfo/department/treeDepartmen.action'/>?treeAll=true",
			    method:'get',
			    animate:true,
			    lines:true,
			   // checkbox:true,
			   	onlyLeafCheck:true,
			   	height:'100',
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					if(node.children.length==0){
						$('#deptSelectName').textbox('setValue',node.text);
				    	$('#deptId').val(node.id);
				    	closeDialog('selectDept');
					}
				}
			}); 
			setTimeout(function(){
	        	bindEnterEvent('searchParam',getTreeNode,'easyui');
	        },200);
		});
		//科室树查询方法
		function getTreeNode(){
	         var searchText = $('#searchParam').val();
	         $("#tDtpt").tree("search", searchText);
	   }
	</script>
	</body>
</html>
