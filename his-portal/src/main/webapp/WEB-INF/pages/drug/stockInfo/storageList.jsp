<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品仓库维护</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
<html>
	<body>
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center'" >
				<div id="divLayout" class="easyui-layout" fit=true>
					<div data-options="region:'center',split:false,iconCls:'icon-book'" style="padding:5px;">
						<input type="hidden" id="drugStockinfoId"  value="${drugStockinfoId}">
						<table id="storagelist" class="easyui-datagrid"  data-options="method:'post',rownumbers:true,idField: 'id'">
							<thead>
								<tr>
									<th data-options="field:'batchNo',width:'10%'" >批号</th>
									<th data-options="field:'tradeName',width:'18%'" >药品商品名</th>
									<th data-options="field:'specs',width:'10%'" >规格</th>
									<th data-options="field:'retailPrice',width:'10%'" >零售价</th>
									<th data-options="field:'validDate',width:'10%'" >有效期</th>
									<th data-options="field:'storeSum',width:'10%'" >库存数量</th>
									<th data-options="field:'minUnit',formatter:drugpackagingunitFamater,width:'10%'" >最小单位</th>
									<th data-options="field:'producerCode',formatter:drugSupplycompanyFamater,width:'10%'" >生产厂家</th>
									<th data-options="field:'remark',width:'10%'" >备注</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>	
		</div>	
		<script type="text/javascript">
			var drugpackagingunitMap = "";//单位Map
			var drugSupplycompanyMap = "";//生成厂家Map
			$(function() {
				//查询单位
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
					type:'post',
					success: function(drugpackagingunitdata) {					
						drugpackagingunitMap= drugpackagingunitdata;										
					}
				});
				//生产厂家
				$.ajax({
					url: "<%=basePath%>drug/stockinfo/queryDrugSupplycompany.action",				
					type:'post',
					success: function(drugSupplycompanydata) {
						drugSupplycompanyMap= drugSupplycompanydata;										
					}
				});
				var drugStockinfoId=$('#drugStockinfoId').val();
				$('#storagelist').datagrid({
					url:"<%=basePath%>drug/stockinfo/queryDrugStorage.action?drugStockinfoId="+drugStockinfoId + "&menuAlias=${menuAlias}"
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
			//生产厂家 列表页 显示	
			function drugSupplycompanyFamater(value,row,index){	
				if(value!=null&&value!=""){											
					if(drugSupplycompanyMap[value]!=null&&drugSupplycompanyMap[value]!=""){
						return drugSupplycompanyMap[value];
					}
					return value;												
				}	
			}
		</script>
	</body>
</html>