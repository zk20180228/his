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
<title>结算患者发票对账单</title>
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

</script>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
		<div data-options="region:'north',border:false,collapsible:false" style="height:40px;">
			<table style="padding: 5px 5px 5px 5px">
	    		<tr>
	    			<td>
	    				<shiro:hasPermission name="${menuAlias}:function:query">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
	    				</shiro:hasPermission>
	    				<shiro:hasPermission name="${menuAlias}:function:readCard">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
						</shiro:hasPermission>
			        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						</shiro:hasPermission>	
						<shiro:hasPermission name="${menuAlias }:function:set">
	    				<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="" iconCls="icon-clear">清空</a>
	    				</shiro:hasPermission>	
	    				<shiro:hasPermission name="${menuAlias}:function:print">  
	    				<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="printPDf()" iconCls="icon-printer">打印</a>
   						</shiro:hasPermission>	
	    			</td>
	    		</tr>
	    	</table>
		</div>   
	    <div data-options="region:'center',border:false" style="height:90%;padding: 5px 0px 5px 0px">
	    	<table class="honry-table" style="width: 100%;">
				<tr style="height: 40px;">
					<td class="honry-lable" style="width: 10%;">病历号：</td>
					<td style="width: 10%;"><input id="medicalrecordId" style="width: 170px" data-options="prompt:'请输入病历号 ,回车查询'" class="easyui-textbox" ></td>
					<td style="width: 10%;" class="honry-lable">身份证：</td>
					<td style="width: 10%;" ><input id="idCard" style="width: 180px" data-options="prompt:'请输入身份证号 ,回车查询'" class="easyui-textbox" ></td>
					<td style="width: 10%;" class="honry-lable">患者姓名：</td>
					<td style="width: 10%;" id="patientName"></td>
					<td style="width: 10%;" class="honry-lable">合同单位：</td>
					<td style="width: 10%;" id="pactCode"></td>
				</tr>
				<tr style="height: 40px;">
					<td style="width: 10%;" class="honry-lable" >发票流水号：</td>
					<td style="width: 10%;" id="invoiceNo" ></td>
					<td style="width: 12%;" class="honry-lable" >入院时间：</td>
					<td style="width: 12%;" id="intDate"></td>
					<td style="width: 12%;" class="honry-lable" >结算时间：</td>
					<td style="width: 12%;" id="balanceDate"></td>
					<td style="width: 12%;" class="honry-lable" >预收押金：</td>
					<td style="width: 12%;" id="prepayCost"></td>
						
				</tr>
				<tr style="height: 40px;">
					<td style="width: 12%;" class="honry-lable" >结算返还：</td>
					<td style="width: 12%;" id="returnCost"></td>	
					<td style="width: 12%;" class="honry-lable" >总费用：</td>
					<td style="width: 12%;" id="totMoney" colspan="6"></td>
				</tr>
			</table>
	    	<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',pagination:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true,border:false">
				<thead>
					<tr>
						<th data-options="field:'patientName',width:'12%',align:'center' ">姓名</th>
						<th data-options="field:'statName',width:'12%',align:'center'">统计大类</th>
						<th data-options="field:'smallTot',width:'12%',align:'center'" align="right" halign="center">费用</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:10" data-options="modal:true, closed:true">   
   		<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
	<div id="diainvoiceNo" class="easyui-dialog" title="选择发票号" style="width:400;height:200;padding:10" data-options="modal:true, closed:true">   
   		<table id="invoiceNoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
	<script type="text/javascript">
	var certificatesNo;
	var inpatientNo1
	$(function(){
		
		bindEnterEvent('medicalrecordId',searchFrom,'easyui');
		bindEnterEvent('idCard',searchFrom,'easyui');
		$('#billSearchHzList').datagrid({
			onLoadSuccess: function (data) {//默认选中
				$('#billSearchHzList').datagrid("autoMergeCells", ['patientName']);
			}
		});
	})
	
	
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#medicalrecordId').textbox('setValue',data);
				searchFrom();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
					url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
					data:{idcardOrRe:card_value},
					type:'post',
					async:false,
					success: function(data) {
						if(data==null||data==''){
							$.messager.alert('提示','此卡号无效');
							return;
						}
						$('#medicalrecordId').textbox('setValue',data);
						searchFrom();
					}
			});
		};
	/*******************************结束读身份证***********************************************/
	//查询患者信息
	function searchFrom(){
		var medicalrecordId = $('#medicalrecordId').val();//病历号
		var idCard=$('#idCard').textbox('getValue');
		if(medicalrecordId == ''&&idCard==''){
			$.messager.alert('提示','请输入病历号或者身份证号！');
			return false;
		}
		else{
			$.ajax({
				url:"<%=basePath%>statistics/InvoiceChecks/queryInpatientInfolist.action",
				data:{medicalrecordId:medicalrecordId,idCard:idCard},
				type:'post',
				success:function(data){
					if(data.length==0){
						$.messager.alert('提示','未查到患者');  
						$('#medicalrecordId').textbox('setValue',"");
					}else if(data.length==1){
						var inpatientNo = data[0].inpatientNo;
						var medicalrecordId = data[0].medicalrecordId;
						certificatesNo=data[0].certificatesNo;
						queryinvoiceNo(inpatientNo,medicalrecordId);
					}else if(data.length>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							data:data,
							queryParams:{medicalrecordId:$('#medicalrecordId').val()},
							columns:[[
								{field:'idcardNo',title:'就诊卡号',width:'30%',align:'center'} ,  
								{field:'reportSex',title:'性别',width:'10%',align:'center',formatter:function(value,row,index){
									return sexMap.get(value);
								}} ,
								{field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
								{field:'certificatesNo',title:'身份证号',width:'40%',align:'center'} 
							]] ,
							onDblClickRow:function(rowIndex, rowData){
								$("#diaInpatient").window('close');
								var inpatientNo = rowData.inpatientNo;
								var medicalrecordId = rowData.medicalrecordId;
								certificatesNo=rowData.certificatesNo;
								queryinvoiceNo(inpatientNo,medicalrecordId);
							}
						});	
					}
				}
			});
		}
	}
	function queryinvoiceNo(inpatientNo,medicalrecordId){
		inpatientNo1=inpatientNo;
		$.ajax({
			url:"<%=basePath%>statistics/InvoiceChecks/queryInpatientBalanceHeadlist.action",
			data:{inpatientNo:inpatientNo},
			type:'post',
			success:function(data){
				if(data.length==0){
					$.messager.alert('提示','该患者还未进行结账！');  
					$('#medicalrecordId').textbox('setValue',"");
				}else if(data.length==1){
					var invoiceNo = data[0].invoiceNo;
					getValueInfo(invoiceNo,inpatientNo,medicalrecordId);
				}else if(data.length>1){
					$("#diainvoiceNo").window('open');
					$("#invoiceNoDatagrid").datagrid({
						url:"<%=basePath%>statistics/InvoiceChecks/queryInpatientBalanceHeadlist.action",
						queryParams:{inpatientNo:inpatientNo},
						columns:[[
							{field:'invoiceNo',title:'发票号',width:'100%',align:'center'} ,  
						]] ,
						onDblClickRow:function(rowIndex, rowData){
							$("#diainvoiceNo").window('close');
							var invoiceNo = rowData.invoiceNo;
							getValueInfo(invoiceNo,inpatientNo,medicalrecordId);
						}
					});	
				}
			}
		});
	}
	function getValueInfo(invoiceNo,inpatientNo,medicalrecordId){
		$.ajax({
			url:"<%=basePath%>statistics/InvoiceChecks/queryVinpatirntInfoBalancelist.action",
			data:{inpatientNo:inpatientNo,invoiceNo:invoiceNo},
			type:'post',
			success:function(data){
				$('#medicalrecordId').textbox('setValue',medicalrecordId)
				$('#patientName').text(data[0].patientName);
				$('#patientName').val(data[0].patientName);
				$('#pactCode').text(data[0].pactCode);
				$('#pactCode').val(data[0].pactCode);
				$('#invoiceNo').text(data[0].invoiceNo);
				$('#invoiceNo').val(data[0].invoiceNo);
				$('#intDate').text(data[0].inDate);
				$('#intDate').val(data[0].inDate);
				$('#balanceDate').text(data[0].balanceDate);
				$('#balanceDate').val(data[0].balanceDate);
				$('#prepayCost').text(data[0].prepayCost);
				$('#prepayCost').val(data[0].prepayCost);
				$('#returnCost').text(data[0].returnCost);
				$('#returnCost').val(data[0].returnCost);
				$('#totMoney').text(data[0].totCost);
				$('#totMoney').val(data[0].totCost);
				$('#idCard').textbox('setValue',certificatesNo);
			}
		});
		$('#billSearchHzList').datagrid({    
			url:"<%=basePath%>statistics/InvoiceChecks/queryVinpatirntInfoBalan.action",
			queryParams:{inpatientNo:inpatientNo,invoiceNo:invoiceNo}
		});
	}
	
	/**打印表单**/
	 function printPDf(){
		var rowsCount=$('#billSearchHzList').datagrid('getRows');
		if(''!=rowsCount&&null!=rowsCount){
			var medicalRecordId=$('#medicalrecordId').val();//就诊卡号
			var patientName=encodeURIComponent(encodeURIComponent($('#patientName').val()));//患者姓名
			var pactCode=encodeURIComponent(encodeURIComponent($('#pactCode').val()));//合同单位
			var invoiceNo=$('#invoiceNo').val();//发票流水号
			var intDate=$('#intDate').val();//入院时间
			var balanceDate=$('#balanceDate').val();//结算时间
			var prepayCost=$('#prepayCost').val();//预收押金
			var returnCost=$('#returnCost').val();//结算返还
			var totMoney=$('#totMoney').val();//总费用
			var timerStr=new Date().getTime();
		 	window.open ("<c:url value='/statistics/InvoiceChecks/queryVinpatirntInfoBalanPDF.action?randomId='/>"+timerStr+"&balance.medicalrecordId="+medicalRecordId+"&balance.patientName="+patientName+"&balance.pactCode="+
		 			pactCode+"&balance.invoiceNo="+invoiceNo+"&balance.inDate="+
		 			intDate+"&balance.balanceDate="+balanceDate+"&balance.prepayCost="+prepayCost+
		 			"&balance.returnCost="+returnCost+"&balance.totCost="+totMoney+"&inpatientNo="+inpatientNo1,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}else{
			$.messager.alert('提示','列表中没有数据，无法打印');
		}
	}
	function clear(){
		$('#medicalrecordId').textbox('setValue','');
		$('#idCard').textbox('setValue','');
		$('#patientName').text('');
		$('#pactCode').text('');
		$('#invoiceNo').text('');
		$('#intDate').text('');
		$('#balanceDate').text('');
		$('#prepayCost').text('');
		$('#returnCost').text('');
		$('#totMoney').text('');
		$('#billSearchHzList').datagrid('load', {    
			inpatientNo: '',    
			invoiceNo: ''   
		});
	}
	/**
	 * 合并单元格
	 */
	$.extend($.fn.datagrid.methods, {
		autoMergeCells: function (jq, fields) {
			return jq.each(function () {
				var target = $(this);
				if (!fields) {
					fields = target.datagrid("getColumnFields");
				}
				var rows = target.datagrid("getRows");
				var i = 0,
				j = 0,
				temp = {};
				for (i; i < rows.length; i++) {
					var row = rows[i];
					j = 0;
					for (j; j < fields.length; j++) {
						var field = fields[j];
						var tf = temp[field];
						if (!tf) {
							tf = temp[field] = {};
							tf[row[field]] = [i];
						} else {
							var tfv = tf[row[field]];
							if (tfv) {
								tfv.push(i);
							} else {
								tfv = tf[row[field]] = [i];
							}
						}
					}
				}
				$.each(temp, function (field, colunm) {
					$.each(colunm, function () {
					var group = this;
						if (group.length > 1) {
							var before,
							after,
							megerIndex = group[0];
							for (var i = 0; i < group.length; i++) {
								before = group[i];
								after = group[i + 1];
								if (after && (after - before) == 1) {
								    continue;
								}
								var rowspan = before - megerIndex + 1;
								if (rowspan > 1) {
									target.datagrid('mergeCells', {
										index: megerIndex,
										field: field,
										rowspan: rowspan
									});
								}
								if (after && (after - before) != 1) {
								    megerIndex = after;
								}
							}
						}
					});
				});
			});
		}
	});
	</script>
</body>
</html>