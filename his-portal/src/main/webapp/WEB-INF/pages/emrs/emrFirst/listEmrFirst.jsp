<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历病案首页</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
	var idp = "";//选择患者id
	$(function(){
		tDt();
		AddOrShowEast('EditForm','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action');	
	});
	
	
	//加载患者树
	function tDt(){
		$('#tDt').tree({
			url : '<%=basePath%>emrs/emrFirst/patientTree.action?menuAlias=${menuAlias}',
			method : 'get',
			animate : true,
			lines : true,
			onlyLeafCheck:true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if(node.attributes.dateState != null && node.attributes.dateState != ''){
					s += node.attributes.dateState;
				}
				if(node.attributes.leaveFlag != null && node.attributes.leaveFlag != ''){
					s += node.attributes.leaveFlag;
				} 	
				if (node.children.length>0) {					 						
					s += '<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';					
				}					
				return s;
			},onClick : function(node){	
				if(node.id.length > 1 && (idp != node.id || idp == "")){
				}else if(node.id.length <= 1){
					$.messager.alert('提示','请选择患者进行操作！');
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}
			},onDblClick : function(node){	
				if(node.id.length > 1 && (idp != node.id || idp == "")){
					AddOrShowEast('EditForm','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?patId='+node.attributes.inpatientNo+'&cardNo='+node.attributes.inpatientNo);
				}else if(node.id.length <= 1){
					$.messager.alert('提示','请选择患者进行操作！');
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}
			}
		});
	}
	
	/**
	 * 动态添加LayOut
	 * @param title 标签名称
	 * @param url 跳转路径
	 */
		function AddOrShowEast(title, url) {
			var eastpanel=$('#listeidt'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','east').panel({
                       href:url 
                });
			}else{//打开新面板
				$('#divLayout').layout('add', {
					region : 'east',
					width : "85%",
					split : false,
					href : url,
					collapsible : false
				});
			}
		}
	
		/**
		* 刷新树
		*/
		function refresh(){
			$('#tDt').tree('reload'); 
		}
		/**
		* 展开树
		*/
		function expandAll(){
			$('#tDt').tree('expandAll');
		}
		/**
		* 关闭树
		*/
		function collapseAll(){
			$('#tDt').tree('collapseAll');
		}
</script>
</head>
<body>
<div  id="divLayout" class="easyui-layout" fit=true> 
	<div id="p" data-options="region:'west',tools:'#toolSMId',split:true" title="患者管理" style="width: 15%; padding: 10px">
    	<div id="toolSMId">
					<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
					<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
					<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
		</div> 
		<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
    		<ul id="tDt"></ul>
    	</div>
	</div>
	<div id="iframe" data-options="region:'center',border:false" >
	</div>
</div>

</body>
</html>