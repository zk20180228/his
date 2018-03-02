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

<div id="cc" class="easyui-layout" style="width:100%;height:100%;">
	<div data-options="region:'west',title:'组套列表',split:false,border:false" style="width:15%;padding:0 10% 10% 10%">
		<ul id="stackTreeInfo"></ul> 
	</div>
	<div data-options="region:'center',split:true" style="padding:5px">
		<table id="cardStack" class="easyui-datagrid" style="width:100%;height:100%;border:0" data-options="fitColumns:true,showFooter:false,border:false">   
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>   
					<th data-options="field:'id',hidden:true">项目编号</th>   
					<th data-options="field:'name'">项目名称</th>   
					<th data-options="field:'combNo'">流水号</th>  
					<th data-options="field:'typeCode'">医嘱类型</th>   
					<th data-options="field:'frequencyCode'">服药频次</th>   
					<th data-options="field:'usageCode'">服药方法</th>  
					<th data-options="field:'onceDose'">每次服用剂量</th> 
					<th data-options="field:'ty'">类别</th>
					<th data-options="field:'doseUnit'">剂量单位</th>   
					<th data-options="field:'mainDrug',formatter:function(value,row,index){
																if(value=='1'){return '是'}
																else{return '否'}}">主药标记</th>
					<th data-options="field:'dateBgn'">医嘱开始时间</th>   
					<th data-options="field:'dateEnd'">医嘱结束时间</th>   
					<th data-options="field:'itemNote'">检查部位</th>  
					<th data-options="field:'days'">草药付数(周期)</th>   
					<th data-options="field:'intervaldays'">间隔天数</th>   
					<th data-options="field:'remark'">医嘱备注</th>  
					<th data-options="field:'remarkComb'">药品组合医嘱</th>   
					<th data-options="field:'isDrug',formatter:function(value,row,index){
																if(value=='1'){return '是'}
																else{return '否'}}">是否药品</th>
					<th data-options="field:'stackInfoNum'">开立数量</th>  
					<th data-options="field:'stackInfoUnit'">单位</th>   
					<th data-options="field:'defaultprice'">默认价</th>   
					<th data-options="field:'childrenprice'">儿童价</th>  
					<th data-options="field:'specialprice'">特诊价</th>   
				</tr>
			</thead>
		</table>
	<div>
</div>
<script type="text/javascript">
$(function(){
	$("#stackTreeInfo").tree({
		url:'<%=basePath%>outpatient/updateStack/stackAndStackInfoForTree.action',
		lines:true,
		onDblClick:function(node){
			if(node.text!='个人'||node.text!='科室'||node.text!='全院'){
				$("#cardStack").datagrid({
					url:'<%=basePath%>business/getStackInfoByInfoIdForView.action?menuAlias=${menuAlias}',
					queryParams: {infoId: node.id}
				});
			}
		}
	})
});
</script>
</body>
</html>