<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<title>采购计划查询</title>
<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
	var deptList = null;
	var companyList = null;
	var itemList = null;
	$.ajax({
		url: "<%=basePath%>material/orderCompany/companyList.action",
		type:'post',
		success: function(data) {
			companyList = data;
		}
	});
	
	$.ajax({
		url: "<%=basePath%>mat/plan/queryItem.action",
		type:'post',
		success: function(data) {
			itemList = data;
		}
	});
	//加载页面
	$(function(){
		$.ajax({
			url: "<%=basePath%>baseinfo/department/departmentCombobox.action",
			type:'post',
			success: function(data) {
				deptList = data;
				//添加datagrid事件及分页
			}
		});
		$('#list').datagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			url:"<c:url value='/mat/plan/queryMatPlanMaster.action'/>?menuAlias=${menuAlias}",
			onDblClickRow: function (rowIndex, rowData) {
				var row = $('#list').datagrid('getSelected');	                        
                if(row){
                	$('#procurementNo').val(row.procurementNo);
                	searchFrom();
   				}
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
				}
			}    
		});
		
		$('#listFrom').datagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			url:"<%=basePath%>mat/plan/queryMatPlanDetail.action",
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
				}
			}      
		});
		bindEnterEvent('queryName',searchList,'easyui');//绑定回车事件
		bindEnterEvent('searchName',searchFrom,'easyui');//绑定回车事件
	});
	
	// 主表列表查询
	function searchList() {
		var queryName = $.trim($('#queryName').textbox('getValue'));
		$('#list').datagrid('load', {
			queryName: queryName,
		});
	}
	//页面查询检索重置,下方列表
	function searchReload(){
		$("#queryName").textbox('setValue','');
		searchList();
	}
	
	//双击展示列表数据后,查询出子表数据
	function searchFrom(){
		var procurementNo=$('#procurementNo').val();
		var searchName = $.trim($('#searchName').textbox('getValue'));
		$('#listFrom').datagrid('load', {
			queryName: searchName,
			procurementNo: procurementNo
		});
	}
	
	// 子表列表查询重置
	function searchReloadNext() {
		$("#searchName").textbox('setValue','');
		searchFrom();
	}
	//显示供应商格式化
	function companyFamater(value){
		if(value!=null){
			for(var i=0;i<companyList.length;i++){
				if(value==companyList[i].companyCode){
					return companyList[i].companyName;
				}
			}	
		}
	}
	//显示科室格式化
	function deptFamater(value){
		if(value!=null){
			for(var i=0;i<deptList.length;i++){
				if(value==deptList[i].deptCode){
					return deptList[i].deptName;
				}
			}	
		}
	}
	//显示项目格式化
	function itemFamater(value){
		if(value!=null){
			for(var i=0;i<itemList.length;i++){
				if(value==itemList[i].itemCode){
					return itemList[i].itemName;
				}
			}	
		}
	}
	//审核状态格式化
	function shztFamater(value){
		if(value==0){
			return "通过";
		}else if(value==1){
			return "未通过";
		}else{
			return "";
		}
	}
	//暂存状态格式化
	function zcztFamater(value){
		if(value==0){
			return "保存";
		}else{
			return "暂存";
		}
	}
</script>

</head>
<html>
<body style="margin: 0px;padding: 0px;">
	<div id="divLayout" class="easyui-layout" fit=true>
		<div data-options="region:'north',split:false,border:false" style="height: 250px;">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'north',split:false,border:false" style="height: 35px">
					<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 5px 0px 5px;">
						<tr>
							<td nowrap="nowrap" >
								查询条件：	<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" style="width:180px" />
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
					<table id="list" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'id',hidden:true">
									id
								</th>
								<th data-options="field:'procurementNo',width : '20%'">
									采购流水号
								</th>
								<th data-options="field:'procurementDeptName',width : '10%'">
									采购科室
								</th>
								<th data-options="field:'budgetMoney',align: 'right',width : '10%'">
									预算金额
								</th>
<!-- 								<th data-options="field:'companyCode',formatter: companyFamater,width : '10%'"> -->
<!-- 									供货商 -->
<!-- 								</th> -->
								<th data-options="field:'createTime',width : '10%'">
									创建时间
								</th>
								<th data-options="field:'checkUserName',width : '10%'">
									审核人
								</th>
								<th data-options="field:'checkStatus',width : '10%',formatter: shztFamater ">
									审核状态
								</th>
								<th data-options="field:'saveTemp',width : '10%',formatter: zcztFamater ">
									暂存标志
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>	
		<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'north',split:false,border:false" style="height: 35px">
					<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 5px 0px 5px;border: none;">
						<tr>
							<td nowrap="nowrap">
								查询条件：	<input class="easyui-textbox" id="searchName" onkeydown="keyDown()" style="width:180px" />
								<input type="hidden" id="procurementNo" name="procurementNo" value="">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReloadNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,border:false" style="width: 100%;">	
					<table id="listFrom"  data-options="fit:true,url:'${pageContext.request.contextPath}/drug/outstore/queryAppOutstore.action?menuAlias=${menuAlias}',method:'post',idField: 'id',striped:true,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false" style="border-top:  1px solid #95b8e7;border-bottom: none;">
						<thead>
							<tr>
								<th data-options="field:'procurementNo', width : '10%'">
									采购流水号
								</th>
								<th data-options="field:'serialNo',width : '5%'">
									序号
								</th> 
								<th data-options="field:'itemCode',formatter: itemFamater, width : '10%'">
									物品名称
								</th>
								<th data-options="field:'specs',width : '5%'">
									规格
								</th> 
								<th data-options="field:'minUnit', width : '5%'">
									最小单位
								</th>
								<th data-options="field:'packUnit', width : '5%'">
									大包装单位
								</th>
								<th data-options="field:'packQty', width : '5%'">
									大包装数量
								</th>
								<th data-options="field:'procurementNum', width : '5%'">
									采购数量
								</th>
								<th data-options="field:'procurementPrice', width : '5%'">
									采购价格
								</th>
								<th data-options="field:'salePrice', width : '5%'">
									零售价格
								</th>
								<th data-options="field:'checkNum', width : '5%'">
									审批数量
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>