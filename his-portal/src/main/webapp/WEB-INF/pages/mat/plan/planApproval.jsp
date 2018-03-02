<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>采购计划审批</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
	<div class="easyui-layout" data-options="fit:true">
		<form id="saveForm" method="post" style="width:100%;height:100%">
			<input type="hidden" id="procurementNo" name="procurementNo"> 
			<input type="hidden" id="checkStatus" name="checkStatus" value="">
			<input type="hidden" id="itemInfoJson" name="itemInfoJson" value="">
			<div id="p" data-options="region:'west',split:true"  style="width:15%;border-top:0" class="checkInventory">
				<ul id="tDt"></ul> 
			</div>
			<div data-options="region:'center',split:false,iconCls:'icon-book'" style="border-top:0">
				<table id="infolist" data-options="url:'${pageContext.request.contextPath}/mat/plan/queryDetail.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId',fit:true">
				</table>
			</div>	
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="pass(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">通过</a>
			<a href="javascript:void(0)" onclick="pass(1)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">不通过</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		</form>
	</div>	
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
		var drugpackagingunitMap = "";//单位Map
		//加载页面
			$(function(){
			   	$('#tDt').tree({    
			   		url:"<%=basePath %>mat/plan/planTree.action",
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
						$('#infolist').datagrid('unselectAll');
						$("#procurementNo").val(node.id);
						$('#infolist').datagrid('load', {
							procurementNo: node.id,
						});
					}
				}); 
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
					var editor = $('#infolist').datagrid('getEditor', {index:rowIndex,field:"checkNum"});
					editor.target.parent().find('span').children().focus();
				},
				onLoadSuccess:function(row, data){
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
					title : 'id',
					field:'id',
					hidden:true
				} , {
					title : '采购流水号',
					field:'procurementNo',
					width:'10%'
				} , {
					title : '序号',
					field:'serialNo',
					width:'5%',
				} , {
					title : '项目名称',
					field:'itemName',
					width:'10%'
				} , {
					title : '规格',
					field:'specs',
					width:'5%'
				} , {
					title : '最小单位',
					field:'minUnit',
					width:'5%'
				} , {
					title : '大包装单位',
					field:'packUnit',
					width:'5%'
				} , {
					title : '大包装数量',
					field:'packQty',
					width:'5%'
				} , {
					title : '采购数量',
					field:'procurementNum',
					width:'5%',
				} , {
					title : '审批数量',
					field : 'checkNum',
					editor : {
						type : 'numberbox',
						options:{min:0,required:true}
					},
					width : '7%'
					
				}, {
					title : '采购价格',
					field:'procurementPrice',
					width:'5%'
				} , {
					title : '零售价格',
					field:'salePrice',
					width:'5%'
				} , {
					title : '生产商编码',
					field:'producerCode',
					hidden:true
				} , {
					title : '生产商',
					field:'producerName',
					width:'10%'
				}
				] ]
			});
			
			function pass(flag){
				var rows = $('#infolist').edatagrid('getRows');
                $('#checkStatus').val(flag);
                $('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
				$('#itemInfoJson').val(JSON.stringify($('#infolist').edatagrid("getRows")));
				if (flag == 0) {//通过状态则要判断输入的审核数量
					for ( var i = 0; i < rows.length; i++) {
						$('#infolist').datagrid('endEdit',$('#infolist').datagrid('getRowIndex',rows[i]));
						if (rows[i].checkNum == "undifined" || isNaN(rows[i].checkNum)) {
							$.messager.alert('提示',"请输入审批数量!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
					}
				}
				if(rows.length>0){
					$('#saveForm').form('submit',{
						url : "<%=basePath %>mat/plan/passPlan.action",
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
							$.messager.alert("提示", "保存成功！", "success");
							reload();
						},
						error : function(data) {
							reload();
							$.messager.progress('close');
							$.messager.alert('提示',"保存失败！");
						}
					});
				}
			}
			//刷新
			function reload(){
				//改变科室下拉后,重新加载封账单树和清空封账药品列表
				$('#tDt').tree({    
			   		url:"<%=basePath %>mat/plan/planTree.action",
				});
				$('#infolist').datagrid('loadData',[]);
				$('#checkStatus').val('');
				$('#procurementNo').val('');
				$('#itemInfoJson').val('');
			}	
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>