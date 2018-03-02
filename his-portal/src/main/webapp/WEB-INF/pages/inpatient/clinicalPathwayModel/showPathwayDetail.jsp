<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<body>
	<form id="treeEditForm" method="post" >
		<div title="Tab1" style="padding: 20px;">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr align="center">
	    			<td class="honry-lable">药品名称:</td>
	    			<td>
	    				<input class="easyui-textbox" readonly="readonly" id="itemName" value="${modelVsItem.itemName }" style="width:290px"/>
		    			<input type="hidden" id="itemCode" name="modelVsItem.itemCode" value="${modelVsItem.itemCode }"/>
		    			<input type="hidden" id="itemNameHidden" name="modelVsItem.itemName" value="${modelVsItem.itemName }"/>
						<input type="hidden" id="id" name="modelVsItem.id" value="${modelVsItem.id }"/>
						<input type="hidden" name="modelVsItem.modelId" value="${modelVsItem.modelId }"/>
	    			</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">模板类型:</td>
	    			<td><input  readonly="readonly" class="easyui-textbox" type="text" id="modelClass" name="modelVsItem.modelClass" value="${modelVsItem.modelClass }" data-options="required:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">医嘱类型:</td>
	    			<td><input  readonly="readonly" class="easyui-textbox" type="text" id="flag" name="modelVsItem.flag" value="${modelVsItem.flag }" data-options="required:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">数量:</td>
	    			<td><input  readonly="readonly" class="easyui-textbox" type="text" id="num" name="modelVsItem.num" value="${modelVsItem.num }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">单位:</td>
	    			<td><input  readonly="readonly" class="easyui-textbox" type="text" id="unit" name="modelVsItem.unit" value="${modelVsItem.unit }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">频次:</td>
	    			<td><input  readonly="readonly" class="easyui-textbox" type="text" id="frequencyCode" name="modelVsItem.frequencyCode" value="${modelVsItem.frequencyCode }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">用法:</td>
	    			<td><input  readonly="readonly" class="easyui-textbox" type="text" id="directionCode" name="modelVsItem.directionCode" value="${modelVsItem.directionCode }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">是否为必选项:</td>
	    			<td>
	    				<input readonly="readonly" class="easyui-combobox" type="text" id="chooseFlag" name="modelVsItem.chooseFlag" value="${modelVsItem.chooseFlag }" 
								data-options="panelHeight:'60px' ,valueField: 'value',textField: 'label',data: [{   
				                label: '否',  
				                value: '0', 
				                selected:true}, 
				                {label: '是',
				                value: '1'}]"   
							 style="width:290px"/>
	    			</td>
	    		</tr>
			</table>	
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			</div>
		</div>		
	</form>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	$(function() {
		/**模板类型下拉**/
		$('#modelClass').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=systemType",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		/**医嘱类型下拉**/
		$('#flag').combobox({
			url: "<%=basePath%>inpatient/clinicalPathwayModelAction/flagCombobox.action",
			valueField : 'typeCode',
			textField : 'typeName',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'typeCode';
				keys[keys.length] = 'typeName';
				if(filterLocalCombobox(q, row, keys)){
					row.selected=true;
				}else{
					row.selected=false;
				}
				return filterLocalCombobox(q, row, keys);
		    }
		});
	});
	
	//关闭dialog
	function closeDialog() {
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
</html>