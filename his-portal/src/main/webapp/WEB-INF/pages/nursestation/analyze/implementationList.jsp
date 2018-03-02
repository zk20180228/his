<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <%@ include file="/common/metas.jsp"%> --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body style="margin: 0px;padding: 0px"> 
	<div id="cc" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false" class="nurseImplementDateSize">
			<div id="ttt" class="easyui-tabs" data-options="fit:true,tabPosition:'bottom'">
			<input type="hidden" id="typeId"> 
				<c:forEach items="${jsson}" var="bean"  varStatus="varStatus">		
						<div title="${bean.billName}" id="${bean.id}" class="easyui-layout">	
							<input type="hidden" value="${bean.billNo}" id="tabBillNo">
							<div style="height: 35px;padding-top: 12px;border-bottom:1px solid #95b8e7;">
								&nbsp;查询时间：
								<input id="beginDate" class="Wdate" type="text" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								到 
								<input id="endDate" class="Wdate" type="text" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								&nbsp;&nbsp;
								<input id="r1" type="radio" name="radiobutton" value="1" onclick="javascript:onclickBoxtai1()">有效
								<input id="r2" type="radio" name="radiobutton" value="0" onclick="javascript:onclickBoxtai2()">无效&nbsp;&nbsp;
								<input id="r3" type="radio" name="state" value="1" onclick="javascript:onclickBoxtai3()"><span id="r33">发送</span>
								<input id="r4" type="radio" name="state" value="0" onclick="javascript:onclickBoxtai4()"><span id="r44">未发送</span>
								&nbsp;&nbsp;&nbsp; <a  onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</div>
							<table id="infolist_${bean.id}" class="easyui-datagrid" style="height: 93%">
							</table>
						</div>
				</c:forEach>											
			</div>
		</div>   
	</div>
	<script type="text/javascript">
	var packunit= new Map();
	$(function(){
		var deptFlg = "${deptFlg}"
		console.log(deptFlg)
		
		//查询单位map
		$.ajax({
			url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=packunit',
			async:false,
			success:function(datavalue){
				for(var i=0;i<datavalue.length;i++){
					packunit.put(datavalue[i].encode,datavalue[i].name);
				}
			}
		});	
	})
	//渲染单位以及包装单位
	function unitFunction(value,row,index){
		if(value!=null&&value!=""){
			if(packunit.get(value)!=null&&packunit.get(value)!=""){
				return packunit.get(value);
			}else{
				return value;
			}
		}
	}
	$('#ttt').tabs({
		border:false,
		onSelect:function(title,index){
			if($('#ttt').tabs('getSelected')!=null){
				var billNo=$('#ttt').tabs('getSelected').find("#tabBillNo").val();
				if(billNo==null||billNo==''){
				}else{
					$.ajax({
						url: "<%=basePath%>inpatient/doctorAdvice/queryInpatientDrugbilldetail.action",
						data:{'inpatientDrugbilldetail.billNo':billNo},	
						type:'post',
						success: function(data) {
							var nodes = $('#patientEt').tree('getChecked');
							var inpatientNo = "";
							for (var i = 0; i < nodes.length; i++) {
								if(inpatientNo==""){
									inpatientNo=nodes[i].id;
								}else{
									inpatientNo+=","+nodes[i].id;
								}
							}
							if(data.total>0){
								billType = data.rows[0].billType;
								if(billType==1){
									loadDatagrid(billNo,1,inpatientNo);
								}else if(billType==2){
									loadDatagrid(billNo,2,inpatientNo);
								}
							}else{
								$('#typeId').val("");
								loadDatagrid(billNo,2);
							}
						}
					});
				}	
			}
		}
	});
	if($('#ttt').tabs('getSelected')!=null){
		var billNo=$('#ttt').tabs('getSelected').find("#tabBillNo").val();
		var billType="";
		if(billNo==null||billNo==''){
		}else{
			
			$.ajax({
				url: "<%=basePath%>inpatient/doctorAdvice/queryInpatientDrugbilldetail.action",
				data:{'inpatientDrugbilldetail.billNo':billNo},	
				type:'post',
				success: function(data) {
					if(data.total>0){
						billType = data.rows[0].billType;
						if(billType==1){
							$('#typeId').val("1");
							loadDatagrid(billNo,1);
						}else if(billType==2){
							$('#typeId').val("2");
							loadDatagrid(billNo,2);
						}
					}
				}
			});
		}
	}
		//复选按钮赋值
		function onclickBoxtai1(){
			searchFrom();
		}
		//复选按钮赋值
		function onclickBoxtai2(){
			searchFrom();
		}
		//复选按钮赋值
		function onclickBoxtai3(){
			searchFrom();
		}
		//复选按钮赋值
		function onclickBoxtai4(){
			searchFrom();
		}
		//查询
		function searchFrom(){
			var typeId = $('#typeId').val();
			//是否有效
			var c= $('input:radio[name="radiobutton"]:checked').val();
			//是否发送
			var b= $('input:radio[name="state"]:checked').val();
			var billNo=$('#ttt').tabs('getSelected').find("#tabBillNo").val();
			var endDate=$("#endDate").val();
			var beginDate=$("#beginDate").val();
			if(typeId==1){
				$('#'+getTabId()).datagrid('reload',{
					billNo:billNo,
					endDate:endDate,
					beginDate:beginDate,
					drugedFlag:b,
					validFlag:c
				});
			}else if(typeId==2){
				$('#'+getTabId()).datagrid('reload',{
					billNo:billNo,
					endDate:endDate,
					beginDate:beginDate,
					drugedFlag:b,
					validFlag:c
				});
			}
		}
		function getTabId(){
			var ww = $('#ttt').tabs('getSelected');
			var wwtab = ww.panel('options');				 
			var wwid = wwtab.id;
			var infolist = "infolist_"+wwid;
			return infolist;
		}
		function loadDatagrid(billNo,billType,inpatientNo){
			if(billType==1){
				$('#typeId').val("1");
				$('#'+getTabId()).datagrid({
					striped:true,
					pagination:true,
					border:false,
					rownumbers:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					url:'<%=basePath%>nursestation/analyze/queryExecdrugList.action?menuAlias=${menuAlias}',
					queryParams:{billNo:billNo,patNoData:inpatientNo},
					columns: [  
								[
								   	{field:'id',title:'Id',hidden:true},
									{field:'drugName',title:'名称',width:200},  
									{field:'specs',title:'规格/样品类型',width:100},
									{field:'qtyTot',title:'用量',width:80},
									{field:'priceUnit',title:'单位',width:100,formatter:unitFunction},
									{field:'docName',title:'开立医生',width:100},
									{field:'validFlag',title:'有效',width:100,formatter:functionvalidFlag},
									{field:'frequencyName',title:'频次',width:100},
									{field:'moDate',title:'医嘱时间',width:150},
									{field:'useTime',title:'应执行时间',width:150},
									{field:'decoDate',title:'分解时间',width:150},
									{field:'execDpcd',title:'执行科室',width:100,formatter:forDept},
									{field:'execFlag',title:'执行',width:50,formatter:functionDid},
									{field:'prnFlag',title:'打印',width:50,formatter:functionPrint}
								]  
							] 
				});
			}else{
				$('#typeId').val("2");
				$('#'+getTabId()).datagrid({
					striped:true,
					pagination:true,
					border:false,
					rownumbers:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					columns: [  
								[
									{field:'id',title:'Id',hidden:true},
									{field:'drugName',title:'名称',width:200},  
									{field:'specs',title:'规格/样品类型',width:100},
									{field:'qtyTot',title:'用量',width:80},
									{field:'priceUnit',title:'单位',width:100,formatter:unitFunction},
									{field:'docName',title:'开立医生',width:100},
									{field:'validFlag',title:'有效',width:100,formatter:functionvalidFlag},
									{field:'frequencyName',title:'频次',width:100},
									{field:'moDate',title:'医嘱时间',width:150},
									{field:'useTime',title:'应执行时间',width:150},
									{field:'decoDate',title:'分解时间',width:150},
									{field:'execDpcd',title:'执行科室',width:100,formatter:forDept},
									{field:'execFlag',title:'执行',width:50,formatter:functionDid},
									{field:'execPrnflag',title:'打印',width:50,align:'left',formatter:functionPrint}
								]  
							] ,
					url:'<%=basePath%>nursestation/analyze/queryunExecdrugList.action?menuAlias=${menuAlias}',
					queryParams:{billNo:billNo}				 
				});
			}
		}	
			//有效显示渲染
	function functionvalidFlag(value,row,index){
		if(value==1){
			return "有效";
		}else if(value==0){
			return "作废";
		}else{
			return "";
		}
	}
			//是否打印渲染
	function functionPrint(value,row,index){
		if(value==1){
			return "已打印";
		}else if(value==0){
			return "未打印";
		}else{
			return "";
		}
	}
			//是否执行显示渲染
	function functionDid(value,row,index){
		if(value==1){
			return "已执行";
		}else if(value==0){
			return "未执行";
		}else{
			return "";
		}
	}
	</script> 
</body>
</html>