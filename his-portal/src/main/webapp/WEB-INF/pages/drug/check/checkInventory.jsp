<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品盘点</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
	<div class="easyui-layout" data-options="fit:true">
		<form id="saveForm" method="post" style="width:100%;height:100%">
			<input type="hidden" name="infoJson" id="infoJson">
			<div id="p" data-options="region:'west',split:true"  style="width:15%;border-top:0" class="checkInventory">
				盘点科室：<input ID="deptList"  style="width:130px;"/>
				<a href="javascript:deleData()"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
				<ul id="tDt"></ul> 
				<input type="hidden" id="id"> 
			</div>
			<div data-options="region:'center',split:false,iconCls:'icon-book'" style="border-top:0">
				<table id="infolist" data-options="url:'${pageContext.request.contextPath}/drug/check/queryCheckDetail.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId',fit:true">
				</table>
			</div>	
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:checkAccount">
			<a href="javascript:void(0)" onclick="update(1)" class="easyui-linkbutton" data-options="iconCls:'icon-pandian'">盘点</a>
		</shiro:hasPermission>
		</div>
		</form>
	</div>	
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
		var drugpackagingunitMap = "";//单位Map
		//加载页面
			$(function(){
				//查询包装单位
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
					type:'post',
					success: function(drugpackagingunitdata) {		
						drugpackagingunitMap = drugpackagingunitdata;							
					}
				});
				
			   	$('#tDt').tree({    
				    url:"<%=basePath %>drug/check/queryBalanceTree.action",
				    method:'post',
				    animate:true,
				    lines:true,
				    formatter:function(node){//统计节点总数
						var s = node.text;
						if (node.children){
						}
						return s;
					},onClick: function(node){//点击节点
						$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
						var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
						if(drugStorageCode){
							$('#infolist').datagrid('unselectAll');
							$("#id").val(node.id);
							$('#infolist').datagrid('load', {
								pid: node.id,
								deptCode: drugStorageCode
							});
						}else{
							$.messager.alert("操作提示", "请选择要盘点的科室！", "warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					}
				}); 
			});
			//单位 列表页 显示	
			function drugpackagingunitFamater(value,row,index){				
				if(value!=null&&value!=""){										
					if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
						return drugpackagingunitMap[value];
					}
					return value;					
				}			
			}
			//初始化盘点科室下拉
			$('#deptList').combobox({
				url : "<%=basePath%>drug/check/findDeptByYFYK.action",
				valueField : 'deptCode',
				textField : 'deptName',
				width : 130,
				onSelect : function(record){
					deptchange();
				},onHidePanel:function(none){
				    var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].deptCode) {
				            result = false;
				        }
				    }
				    if (result) {
				        $(this).combobox("clear");
				    }else{
				        $(this).combobox('unselect',val);
				        $(this).combobox('select',val);
				    }
				},
				filter: function(q, row){
				    var keys = new Array();
				    keys[keys.length] = 'deptCode';
				    keys[keys.length] = 'deptName';
				    keys[keys.length] = 'deptPinyin';
				    keys[keys.length] = 'deptWb';
				    keys[keys.length] = 'deptInputcode';
				    return filterLocalCombobox(q, row, keys);
				}
			});
			$.extend($.fn.validatebox.defaults.rules, {    
				valiAdjustNum: {    
			        validator: function(value, param){
			        	var fstoreNum = +$.trim($(this).closest("td[field='adjustNum']").siblings("td[field='fstoreNum']").children('div').html());
						var retailPrice = +$.trim($(this).closest("td[field='adjustNum']").siblings("td[field='retailPrice']").children('div').html());
						var costall = ((value==null?0:value) - (fstoreNum==null?0:fstoreNum))*(retailPrice==null?0:retailPrice);
						$(this).closest("td[field='adjustNum']").siblings("td[field='costall']").children('div').text(((costall==null)?0:costall).toFixed(2));
			            return true;    
			        }  
			    }    
			});
			$('#infolist').datagrid({
				striped : true,
				selectOnCheck : true,
				singleSelect : true,
				fitColumns : false,
				pagination : true,
				rownumbers : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 100 ],
				onBeforeLoad: function(param){
					var node = $('#tDt').tree('getSelected');
					if(node == null || node.id == 0){
						return false;
					}
				},
				onDblClickRow:function(rowIndex, rowData){
					var rows = $('#infolist').datagrid('getRows');
					if(rows!=null&&rows.length>0){
						for(var i=0;i<rows.length;i++){
							$('#infolist').datagrid('endEdit',$('#infolist').datagrid('getRowIndex',rows[i]));
						}
					}
					$('#infolist').datagrid('beginEdit',rowIndex);
					var editor = $('#infolist').datagrid('getEditor', {index:rowIndex,field:"adjustNum"});
					editor.target.parent().find('span').children().focus();
				},
				onAfterEdit:function(rowIndex, rowData, changes){
					var fstoreNum = rowData.fstoreNum;
					var retailPrice = rowData.retailPrice;
					var costall = (changes.adjustNum - fstoreNum)*retailPrice;
					$('#infolist').datagrid('updateRow',{
						index: rowIndex,
						row: {
							adjustNum:changes.adjustNum,
							costall:costall.toFixed(2)
						}
					});
				},onLoadSuccess:function(row, data){
					//分页工具栏作用提示
					var pager = $(this).datagrid('getPager');
					var aArr = $(pager).find('a');
					var iArr = $(pager).find('input');
					$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
					for(var i=0;i<aArr.length;i++){
						$(aArr[i]).tooltip({
							content:toolArr[i],
							hideDelay:1
						});
						$(aArr[i]).tooltip('hide');
					}},
				columns : [ [ {
					field : 'ck',
					checkbox : true
				},  {
					field : 'tradeName',
					title : '药品名称',
					width : '7%'
				}, {
					field : 'specs',
					title : '规格',
					width : '7%'
				}, {
					field : 'retailPrice',
					title : '零售价',
					width : '7%',
					formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
				}, {
					field : 'wholesalePrice',
					title : '批发价',
					width : '7%',
					formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
				}, {
					field : 'purchasePrice',
					title : '购入价',
					width : '7%',
					formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
				}, {
					field : 'fstoreNum',
					title : '库存数量',
					width : '7%'
				}, {
					field : 'adjustNum',
					title : '实际盘点数量',
					editor : {
						type : 'numberbox',
						options:{validType:'valiAdjustNum'}
					},
					width : '7%'
					
				}, {
					field : 'costall',
					title : '盈亏总金额',
					width : '7%',
					formatter : function (value,row,index){		
						   if(row.adjustNum!=null){
							   return((row.adjustNum-row.fstoreNum)*row.retailPrice).toFixed(2);
						   }
					}
				} ] ]
			});
			
			function update(flag){
				var rows = $('#infolist').edatagrid('getSelections');
				if (rows.length > 0) {//选中几行的话触发事件	 
					for ( var i = 0; i < rows.length; i++) {
						$('#infolist').datagrid('endEdit',$('#infolist').datagrid('getRowIndex',rows[i]));
						if (rows[i].adjustNum == "undifined" || isNaN(rows[i].adjustNum)) {
							$.messager.alert('提示',"请输入药品【"+rows[i].tradeName+"】的实际盘点数量!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
					}
					$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getSelections")));
					$('#saveForm').form(
							'submit',
							{url : "<%=basePath %>drug/check/saveCheckStatic.action?flag="+flag,
								type:'post',
								onSubmit : function() {
									if (!$(this).form('validate')) {
										$.messager.alert('提示',"验证没有通过,不能提交表单!");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										return false;
									}
									$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
								},
								success : function(data) {
									$.messager.progress('close');
									$.messager.alert("操作提示", "操作成功！", "success");
									//清空科室下拉
									$('#deptList').combobox('setValue','');
									deptchange();
								},
								error : function(data) {
									$('#deptList').combobox('setValue','');
									deptchange();
									$.messager.progress('close');
									$.messager.alert('提示',"保存失败！");
								}
							});
				} else {
					$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			function deleData(){
				delSelectedData('deptList');
				deptchange();
			}
			//科室下拉框改变的时候重新加载封账单
			function deptchange(){
				var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
				//改变科室下拉后,重新加载封账单树和清空封账药品列表
				$('#tDt').tree({    
			   		url:"<%=basePath %>drug/check/queryBalanceTree.action?deptCode="+drugStorageCode,
				});
				$('#infolist').datagrid('loadData',[]);
			}	
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>