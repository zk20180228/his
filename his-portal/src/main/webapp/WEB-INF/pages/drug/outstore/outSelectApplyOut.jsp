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
		<div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
			<table id="listApplyOut" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/drug/outstore/getDrugApplyout.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,toolbar: '#tbp'">
				<thead>
					<tr>
						<th data-options="field:'id',checkbox:true, width : '10%'">出库申请单id</th>
						<th data-options="field:'drugCommonname', width : '15%'">药品通用名</th>
						<th data-options="field:'drugCnamepinyin',hidden : true">拼音</th>
						<th data-options="field:'drugCnamewb',hidden : true">五笔</th>
						<th data-options="field:'drugCnameinputcode',hidden : true">自定义</th>
						<th data-options="field:'applyBillcode', width :'9%'">申请单号</th>
						<th data-options="field:'deptName', width : '13%'">申请部门</th>
						<th data-options="field:'patientName', width : '8%'">患者名称</th>
						<th data-options="field:'specs', width : '12%'">规格</th>
						<th data-options="field:'packUnit', width : '7%',formatter: packUnitFamater">包装单位</th> 
						<th data-options="field:'packQty',width : '5%'" >包装数</th>
						<th data-options="field:'retailPrice', width : '6%'">零售价</th>
						<th data-options="field:'applyNum', width : '8%'">申请出库量</th>
						<th data-options="field:'recipeNo', width : '14%'">处方号</th>
					</tr>
				</thead>
			</table>
			<div id="tbp" style="height: auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="append()">确定</a>
			</div>
		</div>
		<script type="text/javascript">
			var packUnitList = null;
			$(function(){
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeDrugpackagingunit"},
					type:'post',
					success: function(packUnitdata) {
						packUnitList = packUnitdata;
					}
				});
				setTimeout(function(){
					$('#listApplyOut').datagrid({
						pagination:true,
				   		pageSize:20,
				   		pageList:[20,30,50,80,100],
						onDblClickRow: function (rowIndex, rowData) {//双击查看
							
							
						}    
					});
				},1);
			});
			function append(){
				var row=$('#listApplyOut').datagrid('getChecked');
				if(row.length>0){
					for (var i=0;i<row.length;i++){
						$('#infolist').datagrid('appendRow',{
							id:row[i].id,
							differentId:2,
							drugCode: row[i].drugId,
							tradeName: row[i].tradeName,
							drugCommonname : row[i].drugCommonname,
							drugCnamepinyin : row[i].drugCnamepinyin,
							drugCnamewb : row[i].drugCnamewb,
							drugCnameinputcode : row[i].drugCnameinputcode,
							specs: row[i].specs,
							retailPrice:row[i].retailPrice,
							packUnit:row[i].packUnit,
							outNum:row[i].applyNum,
							outlCost:(row[i].applyNum*row[i].retailPrice).toFixed(2)
						});
					}
					closeDialog('selectApplyOut');
					totalPrice();
					$("#totalDivId").show();
				}else{
					$.messager.alert("操作提示", "请勾选申请单记录!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
				
			}
			function packUnitFamater(value){
				if(value!=null){
					for(var i=0;i<packUnitList.length;i++){
						if(value==packUnitList[i].id){
							return packUnitList[i].name;
						}
					}	
				}
			}
		</script>
	</body>
</html>