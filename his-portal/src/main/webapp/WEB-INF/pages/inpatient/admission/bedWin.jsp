<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
var bedStateMap=null();//病床状态
$.ajax({
    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
	type:'post',
	success: function(data) {
		var type = data;
		for(var i=0;i<type.length;i++){
			bedStateMap.put(type[i].encode,type[i].name);
		}
	}
});
</script>
</head>
<body>
     <div class="easyui-layout" style="width: 100%; height: 100%;">
		<div id="p" data-options="region:'west'" title="住院患者" style="width: 20%;height:100%; padding: 10px">
		    <ul id="tDt"></ul>
		</div>
		<div data-options="region:'center',fit:'true',url:'${pageContext.request.contextPath}/inpatient/admission/searchBedInfoWithDeptId.action'" style="width: 20%;height:100%;">
			    	<table id="list1" class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true" >   
					    <thead>   
					        <tr>   
					            <th data-options="field:'bedName'" width="20%" align="center">病床号</th>   
					            <th data-options="field:'bedLevel'" width="20%" align="center">等级</th>   
					            <th data-options="field:'inState',formatter: functionWard" width="20%" align="center">病房</th> 
					            <th data-options="field:'bedState',formatter:formatCheckbedType" width="20%" align="center">状态</th>    
					        </tr>   
					    </thead>   
					</table> 
			    </div> 
	</div>
</body>
<script type="text/javascript">
		 //加载部门树
	   	$('#tDt').tree({    
		    url:"<c:url value='/inpatient/treeDepartmen.action'/>?treeAll="+true+"&deptType="+5,
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
			},
			//双击击节点点到右边查看详细页面
			onDblClick:function(node){
				if(node.id!=""){
					var id=node.id;
					$('#list1').datagrid({
					url:"<c:url value='/inpatient/admission/searchBedInfoWithDeptId.action'/>?id="+id+"&status=7",
					method:'post'
					});
				}
				
			}
		});
	   
	// R-住院登记I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院
		function functionWard(value){
			var text = '';
			if(value=='R'){
				text='住院登记';
			}else if(value=='I'){
				text='病房接诊';
			}else if(value=='B'){
				text='出院登记 ';
			}else if(value=='O'){
				text='出院结算 ';
			}else if(value='P'){
				text='预约出院';
			}else if(value=='N'){
				text='无费退院';
			}
			return text;
		}
		
		function formatCheckbedType(value,row,index){
			if(value!=null&&value!=''){
				return bedStateMap.get(value);
			}
		}
		
		$('#list1').datagrid({
			//双击击节点赋值给父窗口
		    onDblClickRow: function (rowIndex, rowData) {
				window.opener.$("#bedName").val(rowData.bedName);
				window.opener.$("#hiddenBedId").val(rowData.bedId);
				
				window.close();
			}  
		}); 
</script>
</html>
