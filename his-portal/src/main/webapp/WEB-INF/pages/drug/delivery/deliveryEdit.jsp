<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>取消发送</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
/**
 * 单位
 */
var packUnitList=null;

$(function(){

	//摆药单回车事件
	bindEnterEvent('drugNo',searchFrom,'easyui');
	/**
	 *开始时间回车事件
	 */
	$('#applyDate').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			searchFrom();
		}
	});
	/**
	 * 结束时间回车事件
	 */
	$('#applyEnd').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			searchFrom();
		}
	});
	/**
	 * 药房的下拉查询
	 */
	$('#deptName').combobox({    
		url:'<%=basePath %>inpatient/deliverySend/cancelSendDept.action',    
		valueField:'deptCode',    
		textField:'deptName',
		onSelect:function(record){
			$('#deptName').combobox('textbox').focus();
		}
	});
	//药房回车事件
	$('#deptName').combobox('textbox').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			searchFrom();
		}
	});
	$.ajax({
		url: "<%=basePath %>inpatient/deliveryDelivery/likeDrugPackagingunit.action",
		type:'post',
		success: function(packUnitdata) {
			packUnitList = packUnitdata ;
		}
	});
	
});

/**
 * 查询
 */
function searchFrom() {
	var applyDate=$('#applyDate').val();
	var applyEnd=$('#applyEnd').val();
	var deptName=$('#deptName').combobox('getValue');
	var drugNo=$('#drugNo').val();
	$('#list').datagrid({
		url:'<%=basePath %>inpatient/deliverySend/queryApplyOutList.action?menuAlias=${menuAlias}',
		method:'post',
		queryParams:{deptCode:deptName,
					drugedBill:drugNo,
					applyDate:applyDate,
					applyEnd:applyEnd
		}
	});
}

function refresh() {//刷新树
	$('#list').datagrid('reload');
}

//修改
function edit() {
	//选中要删除的行
	var rows = $('#list').datagrid('getChecked');
	if (rows.length > 0) {//选中几行的话触发事件
		$.messager.confirm('确认','确定要取消选中发送信息吗?',function(res) {//提示是否取消发送
			if (res) {
				var ids = '';
				for ( var i = 0; i < rows.length; i++) {
					if (ids != '') {
						ids += ',';
					}
					ids += rows[i].id;
				}
				$.messager.progress({text:'正在取消发送,请稍后...',modal:true});
				$.ajax({
					url: "<%=basePath %>inpatient/deliverySend/editApplyOut.action",
					data:{id:ids},
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert('提示','取消成功');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					$('#list').datagrid('reload');
					},error : function() {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');	
					}
				}); 
			}
		});
	} else {
		$.messager.alert('提示',"请选择要取消发送的记录!");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
	}
}
	
	function functionState(value,row,index){
		if(value!=null&&value!=''){
			if(value=="0"){
				return '申请';
			}else if(value=="1"){
				return '配药';
			}else if(value=="2"){
				return '核准(出库)';
			}else if(value=="3"){
				return '作废';
			}else if(value=="4"){
				return '暂不摆药';
			}else if(value=="5"){
				return '集中发送已经打印';
			}else if(value=="6"){
				return '集中发送未打印';
			}else if(value=="7"){
				return '审核';
			}else{
				return '(退药入库)';
			}
		}
	}
	/**
	 *显示单位格式化
	 */
	function packUnitFamater(value,row,index){
		if(value!=null){
			for(var i=0;i<packUnitList.length;i++){
				if(value==packUnitList[i].encode){
					return packUnitList[i].name;
				}
			}	
		}
	}
	function clear(){
		$('#applyDate').val("");
		$('#applyEnd').val("");
		$('#deptName').combobox('setValue',"");
		$('#drugNo').textbox('setValue',"");
		$("#list").datagrid('loadData', { total: 0, rows: [] });
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px; padding: 0px">  
<div class="easyui-layout" data-options="fit:true">   
	<div data-options="region:'north',border:false" style="height:50px;padding: 10px 0px 0px 10px" class="deliveryEditDateSize">
		开始时间：
            <input placeholder="选择后回车查询" id="applyDate" name="applyDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
		&nbsp;&nbsp;结束时间：
		<input placeholder="选择后回车查询" id="applyEnd" name="applyEnd" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		&nbsp;&nbsp;药房选择：<input  id="deptName" name="deptCode" data-options="prompt:'选择后回车查询'"/>
		&nbsp;&nbsp;摆药单号：<input class="easyui-textbox" id="drugNo" name="applyBillcode" data-options="prompt:'输入后回车查询'"/>
		&nbsp;
		<shiro:hasPermission name="${menuAlias}:function:query">
		<a href="javascript:searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
  				<a href="javascript:edit()" class="easyui-linkbutton" data-options="iconCls:'icon-Csend'">取消发送</a>
  		</shiro:hasPermission>
  		<shiro:hasPermission name="${menuAlias}:function:reset">
		<a href="javascript:clear()" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
		</shiro:hasPermission>
	</div>   
	<div data-options="region:'center',title:'取消发送'">
		<table id="list"  class="easyui-datagrid"  
	   			data-options="rownumbers:true,striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
		     <thead>   
		        <tr>  
		       		<th data-options="field:'ck',checkbox:true "></th> 
		            <th data-options="field:'deptName',width:'150px'">申请科室</th>   
		            <th data-options="field:'drugedBill',width:'150px'">摆药单号</th>   
		            <th data-options="field:'tradeName',width:'250px'">药品名称</th>
	 				<th data-options="field:'applyNum',width:'50px'">数量</th>     <!-- 出库数量 -->
		            <th data-options="field:'minUnit',width:'80px',formatter:packUnitFamater">单位</th>    
		            <th data-options="field:'applyDate',width:'180px'">申请时间</th>
		            <th data-options="field:'patientId',width:'180px'">病案号</th>    <!-- 根据患者编号查查病例号 -->
		            <th data-options="field:'applyState',width:'180px'" formatter="functionState">状态</th>
		        </tr>   
		    </thead>
		</table> 
	</div>
</div>
</body>
</html>