<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>预交金</title>
<style type="text/css">
.panel-body{
	border-left:0;
}
</style>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div region="center" border="false" style="height: 100%">
				<div class="easyui-layout" fit="true">	
					<div data-options="region:'north',border:false" style="height:30px;">
						<table >
							<tr>
								<td style="width:50px;padding-left:5px" nowrap="nowrap">姓名：</td><td id="name" style="width:60px"></td>
								<td style="width:80px" nowrap="nowrap">入院日期：</td><td id="inDate" style="width:180px"></td>
								<td style="width:50px" nowrap="nowrap">余额：</td><td id="freeCost" style="width:50px" ></td>
								<td>
								</td>
							</tr>
						</table>
					</div>
					<div region="center" border="false" style="height:98%;">
						<table id="listDetailFeeInfo" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'" style="height:100%;">
							<thead>
								<tr>
									<th data-options="field:'feeCode'" style="width:12%"align="center" halign="center">编码</th>
									<th data-options="field:'feeName'" style="width:16%"align="center" halign="center">类别名称</th>
									<th data-options="field:'totcost',formatter:purchaseCostFormatter" style="width:10%" align="right" halign="center">金额</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>		
			</div>
		</div>
	
<script type="text/javascript">

		//保留两位小数
		function purchaseCostFormatter(value,row,index){//入库总金额
			var num=value;
			if(typeof(num)=='undefined'){
				num=0;
			}
			return num.toFixed(2);
		}
		
		/**
         * EasyUI DataGrid根据字段动态合并单元格
         * @param fldList 要合并table的id
         * @param fldList 要合并的列,用逗号分隔(例如："name,department,office");
         */
         function MergeCells(tableID, fldList) {
             var Arr = fldList.split(",");
             var dg = $('#' + tableID);
             var fldName;
             var RowCount = dg.datagrid("getRows").length;
             var span;
             var PerValue = "";
             var CurValue = "";
             var length = Arr.length - 1;
             for (i = length; i >= 0; i--) {
                 fldName = Arr[i];
                 PerValue = "";
                 span = 1;
                 for (row = 0; row <= RowCount; row++) {
                     if (row == RowCount) {
                         CurValue = "";
                     }
                     else {
                         CurValue = dg.datagrid("getRows")[row][fldName];
                     }
                     if (PerValue == CurValue) {
                        span += 1;
                     }
                     else {
                         var index = row - span;
                         dg.datagrid('mergeCells', {
                             index: index,
                             field: fldName,
                             rowspan: span,
                             colspan: null
                         });
                         span = 1;
                         PerValue = CurValue;
                     }
                 }
             }
         }
	
</script>
	</body>
</html>