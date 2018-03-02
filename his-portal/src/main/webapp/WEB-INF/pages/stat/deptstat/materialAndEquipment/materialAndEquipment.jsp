<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>物资设备统计信息</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var drugpackagingunitMap = new Map();
var deptList='';//科别渲染
$(function(){
	 var itemCode=$('#itemCode').combogrid('getValue');
	 var queryStorage=$('#queryStorage').combobox('getValue');
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 $('#tableList').datagrid({    
		 	url: '<%=basePath%>statistics/materialAndEquipment/queryMaterialAndEquipment.action',
			queryParams: {
				itemCode: itemCode,
				startTime:startTime,
				endTime:endTime,
				queryStorage:queryStorage
			},
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,100],
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
	//查询包装单位
		$.ajax({
			url: "<%=basePath%>baseinfo/businessContractunit/getContMap.action",		
			type:'post',
			success: function(drugpackagingunitdata) {					
				drugpackagingunitMap= drugpackagingunitdata;
			}
		});
		//物资名称
	    $('#itemCode').combogrid({
			required : true,
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			striped : true,//数据背景颜色交替
			panelWidth : 550,//容器宽度
			fitColumns : true,//自适应列宽
			mode:'remote',
			pageSize : 20,//每页显示的记录条数，默认为20  
			pageList : [20,30,50,100],//可以设置每页记录条数的列表  
			url :"<%=basePath%>statistics/materialAndEquipment/queryItemName.action", 
			idField : 'itemCode',
			textField : 'itemName',
			columns : [ [
			{field : 'itemCode',title : '编码',hidden:true},
			{field : 'itemName',title : '名称', width: '50%'},
			{field : 'specs',title : '规格',width: '25%'},
			{field : 'minUnit',title : '单位', width: '10%'},
			] ]
		});			 
	//库存科室查询
	$('#queryStorage').combobox({
		url : '<%=basePath%>statistics/materialAndEquipment/queryStorageCode.action',
		valueField : 'code',
		textField : 'name'
	});
});
// 	$('#queryStorage').combobox({//仓库
// 		url : "<c:url value='/material/orderDepartment/getStorage.action'/>",
// 		valueField : 'deptCode',
// 		textField : 'deptName',
// 		required: true,
// 		missingMessage:'请选择仓库',
// 		onLoadSuccess:function(){
// 			var data = $('#queryStorage').combobox('getData');
// 			if (data.length > 0) {
// 				for (var i = 0; i < data.length; i++) {
// 					if(data[i].deptCode=="${deptCode}"){
// 		                $("#queryStorage").combobox('select', data[i].deptCode);
// 		                break;
// 					}else{
// 						$("#queryStorage").combobox('select', data[0].deptCode);
// 					}
//       		    }	
//      		 }		
// 		},
// 		onSelect: function(rec){
<%-- 			var url='<%=basePath%>material/order/matStockInfo/matData.action?t1.validState=1&deptId='+rec.deptCode; --%>
// 			$('#grid1').datagrid('reload', url);
// 		}
// 	});
//单位 列表页 显示	
// function agingunitFamaters(value,row,index){
// 	if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
// 		return drugpackagingunitMap[value]; 
// 	}
// 	return value;
// }
//查询按钮
 function searchFrom(){
	 var itemCode=$('#itemCode').combogrid('getValue');
	 var queryStorage=$('#queryStorage').combobox('getValue');
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 $('#tableList').datagrid('load', {
			itemCode: itemCode,
			startTime:startTime,
			endTime:endTime,
			queryStorage:queryStorage
		}
	); 
}
 
//重置按钮
 function clear(){
	$('#startTime').val("${startTime }");
	$('#endTime').val("${endTime }");
 	$('#itemCode').combogrid('setValue','');
 	$('#queryStorage').combobox('setValue','');
 	searchFrom()
 }
	$.ajax({
	url: "${pageContext.request.contextPath}/baseinfo/department/queryDepartments.action",
	type:'post',
	async:false,
	success: function(deptListData) {
		deptList = deptListData;
	}
});
//科别渲染
function deptFamater(value){
	if(value!=null){
		for(var i=0;i<deptList.length;i++){
			if(value==deptList[i].id){
				return deptList[i].deptName;
			}
		}	
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> 
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	 	<form id="saveForm" action="" style="height:80px;width: 100%;padding-top: 8px;padding-top: 8px;padding-right: 10px;padding-left: 10px;">
	 		<table border="0" style="width: 100%;height: 10%">
	 			<tr>
 				<!-- 开始时间 --> 
					<td style="width:80px;padding-left:10px">开始时间:</td>
					<td style="width:110px;">
						<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:80px;padding-left:10px">结束时间:</td>
					<td style="width:110px" id="td1">
						<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td style="padding-left:10px">
						物资名称:<input class="easyui-combogrid"id="itemCode" name="itemCode"/>
						库存科室:<input class="easyui-combobox"id="queryStorage" name="deptId"/>
		    			<shiro:hasPermission name="${menuAlias}:function:query">
			    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			 			</shiro:hasPermission>
			 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
		    		</td>
	    		</tr>
	    	 	<tr>
	    	 		<td align="center" colspan="8"  ><font style = "font-size: 32px !important;" class="empWorkTit" size="3">物资设备信息查询</font></td>
	    	 	</tr>
	    	 </table>
	    </form>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 89%;">
	    	<table id="tableList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'deptCode',width:'8%',formatter: deptFamater">科室</th>
						<th data-options="field:'itemCode',width:'7%'">物资编码</th>
						<th data-options="field:'itemName',width:'7%'">物资名称</th>
						<th data-options="field:'specs',width:'7%'">规格</th>
						<th data-options="field:'minUnit',width:'7%'">最小单位</th>
						<th data-options="field:'packQty',width:'8%'">大包装数量</th>
						<th data-options="field:'packUnit',width:'8%'">大包装单位</th>
						<th data-options="field:'inNum',width:'8%'">入库数量</th>
						<th data-options="field:'outNum',width:'10%'">出库数量</th>
						<th data-options="field:'storeCount',width:'10%'">当前库存</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
</body>
</html>
