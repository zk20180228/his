<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
var payMap=new Map();
	$.ajax({
		url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
		async:false,
		success: function(data) {
			var payType = data;
			for(var i=0;i<payType.length;i++){
				payMap.put(payType[i].encode,payType[i].name);
			}
		}
	});
</script>
</head>
<body>
<div id="divLayout"  class="easyui-layout" data-options="fit:true">  
	<div data-options="region:'center',border:false" style="width: 100%;height:100%;">
		<div style="text-align:center;">
			<fieldset style="width:1000px;margin-top:8px;margin-left:auto;margin-right:auto;" class="changeskin">					
				<table cellspacing="0" cellpadding="0" border="0" data-options="fit:true">
					<tr>	
					<shiro:hasPermission name="${menuAlias}:function:query">				
						<td style="padding: 10px 0px;font-size:14px;">
							发票号：
						</td>
						<td>
							<input type="text" style="width:180px;" ID="invoiceNoSerc"  class="easyui-textbox" data-options="prompt:'输入后六位回车查询'" />
						</td>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<shiro:hasPermission name="${menuAlias}:function:set">	
								<a href="javascript:void(0)" onclick="clean()" class="easyui-linkbutton" iconCls="icon-clear">清&nbsp;屏&nbsp;</a>
							</shiro:hasPermission>
						</td>
						<td>
							<shiro:hasPermission name="${menuAlias}:function:print">
								&nbsp;&nbsp;&nbsp;&nbsp;<a id="adPrintAdviceBtn" class="easyui-linkbutton" onclick="printPDf()" data-options="iconCls:'icon-2012081511202'">打&nbsp;印&nbsp;</a>
							</shiro:hasPermission>	
						</td>
					</shiro:hasPermission>											
					</tr>
				</table>
			</fieldset>	
		</div>
		<div style="text-align:center;">	
			
				<fieldset style="width:1000px;margin-top:8px;margin-left:auto;margin-right:auto;" class="changeskin">	
					<legend style="text-align:left;">患者信息</legend>		
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;" data-options="method:'post',idField:'id',split:false,fit:true" >
						<thead> 
						<tr>
							<td class="honry-lable" style="width:8%;">患者姓名  ：</td>
							<td style="width:17%;" id="inpatientName"></td>
							<td class="honry-lable" style="width:8%;">合同单位  ：</td>
							<td style="width:17%;" id="pactName"></td>
							<td class="honry-lable" style="width:8%;">住院科室  ：</td>
							<td style="width:17%;" id="deptName"></td>
							<td class="honry-lable" style="width:8%;">所属病区  ：</td>
							<td style="width:17%;" id="nurseCellName"></td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:8%;">住院医生  ：</td>
							<td id="houseDocName" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">床号  ：</td>
							<td id="bedName" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">出生日期  ：</td>
							<td id="reportBirthday" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">预交金额  ：</td>
							<td id="prepayCost" style="width:17%;"></td>
						</tr>
						</thead>
					</table>					
				</fieldset>
				<fieldset style="width:1000px;margin-top:8px;margin-left:auto;margin-right:auto;" class="changeskin">	
					<legend style="text-align:left;">发票信息</legend>		
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;"  data-options="method:'post',idField:'id',split:false,fit:true">
						<thead> 						
						<tr>
							<td class="honry-lable" style="width:8%;">结算时间  ：</td>
							<td id="balanceDate" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">结算类型  ：</td>
							<td id="paykindName" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">结算人 ：</td>
							<td id="balanceOperName" colspan="3" style="width:38%;"></td>
							
						</tr>
						<tr>
							<td class="honry-lable" style="width:8%;">费用总额  ：</td>
							<td id="totCost" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">自费金额  ：</td>
							<td id="ownCost" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">自付金额  ：</td>
							<td id="payCost" style="width:17%;"></td>
							<td class="honry-lable" style="width:8%;">公费金额  ：</td>
							<td id="pubCost" style="width:17%;"></td>
						</tr>
						</thead>
					</table>					
				</fieldset>
				<fieldset style="width:1000px;margin-top:10px;margin-left:auto;margin-right:auto;" class="changeskin">	
					<legend style="text-align:left;">发票明细</legend>	
					<div>
						<table id="detaillist" class="easyui-datagrid" style="width: 100%;min-height:100px;"  data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">
							<thead> 
							<tr>
								<th data-options="field:'statName',align:'center'" style="width: 50%;">发票类型</th>
								<th data-options="field:'totCost',align:'center'" style="width: 50%;">金额</th>
							</tr>						
							</thead>
						</table>
					</div>						
				</fieldset>				
			</div>
		</div>
</div>
<script type="text/javascript">
	var invoiceNoSave;
	var inpatientNoSave;
/**
 * 查询
 * @author  ygq
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2015-12-8
 * @version 1.0
 */
function searchFrom() {	
	var invoiceNo = $('#invoiceNoSerc').val();
	invoiceNo=invoiceNo.replace(/(^\s*)|(\s*$)/g, "");
	if(invoiceNo.length!=6){
		$.messager.alert('提示','请输入发票号后6位！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		$("#balanceDate").html('');
		$("#paykindName").html('');	
		$("#balanceOperName").html('');	
		$("#totCost").html('');	
		$("#ownCost").html('');	
		$("#payCost").html('');	
		$("#pubCost").html('');						
		$("#inpatientName").html('');
		$("#pactName").html('');	
		$("#deptName").html('');	
		$("#nurseCellName").html('');	
		$("#houseDocName").html('');	
		$("#bedName").html('');	
		$("#reportBirthday").html('');
		$("#prepayCost").html('');
		clearDgAdDgList();
		return;
	}else{
		$.messager.progress({text:"查询中,请稍等.....",model:true});
		$.ajax({
			url: '<%=basePath%>finance/invoiceRepPrint/queryInvoiceInfo.action',
			data:'invoiceRepPrintVo.invoiceNo='+invoiceNo,
			type:'post',
			datatype:'json',
			success: function(data) {
				$.messager.progress('close');
				var json=data;
				if(json==''){
					$.messager.alert('提示','没有该发票号对应的发票信息，或者该发票已作废！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$("#balanceDate").html('');
					$("#paykindName").html('');	
					$("#balanceOperName").html('');	
					$("#totCost").html('');	
					$("#ownCost").html('');	
					$("#payCost").html('');	
					$("#pubCost").html('');						
					$("#inpatientName").html('');
					$("#pactName").html('');	
					$("#deptName").html('');	
					$("#nurseCellName").html('');	
					$("#houseDocName").html('');	
					$("#bedName").html('');	
					$("#reportBirthday").html('');
					$("#prepayCost").html('');
					clearDgAdDgList();
				}else{	
					invoiceNoSave=invoiceNo;
					$("#balanceDate").html(json[0].balanceDate);				
					$("#paykindName").html(	payMap.get(json[0].paykindName));	
					$("#balanceOperName").html(json[0].balanceOperName);	
					$("#totCost").html(json[0].totCost);	
					$("#ownCost").html(json[0].ownCost);	
					$("#payCost").html(json[0].payCost);	
					$("#pubCost").html(json[0].pubCost);						
					$("#inpatientName").html(json[0].patientName);
					$("#pactName").html(json[0].pactName);	
					$("#deptName").html(json[0].deptName);	
					$("#nurseCellName").html(json[0].nurseCellName);	
					$("#houseDocName").html(json[0].houseDocName);	
					$("#bedName").html(json[0].bedName);	
					$("#reportBirthday").html(json[0].reportBirthday);
					$("#prepayCost").html(json[0].prepayCost);
					$("#detaillist").datagrid({ 
						url:'<%=basePath%>finance/invoiceRepPrint/queryBalanceList.action?menuAlias=${menuAlias}',
						queryParams: {
							'inpatientBalanceList.invoiceNo': json[0].invoiceNo,
							'inpatientBalanceList.balanceNo': json[0].balanceNo
						},
						onLoadSuccess:function(data){
							inpatientNoSave=data.rows[0].inpatientNo;
						}
					});
				}
			}
	    });
	}
}	

//清空datagrid数据
function clearDgAdDgList(){		
	var rows = $('#detaillist').datagrid('getRows');
	var len = rows.length;
	inpatientNoSave='';
	for(var i=0;i<len;i++){
		$('#detaillist').datagrid('deleteRow',0);
	}						
}
//清屏整个页面的数据
function clean(){
	$('#invoiceNoSerc').textbox('setValue','');
	$("#balanceDate").html('');
	$("#paykindName").html('');	
	$("#balanceOperName").html('');	
	$("#totCost").html('');	
	$("#ownCost").html('');	
	$("#payCost").html('');	
	$("#pubCost").html('');						
	$("#inpatientName").html('');
	$("#pactName").html('');	
	$("#deptName").html('');	
	$("#nurseCellName").html('');	
	$("#houseDocName").html('');	
	$("#bedName").html('');	
	$("#reportBirthday").html('');
	$("#prepayCost").html('');
	clearDgAdDgList();
}
/**
 * 回车键查询
 * @author  yeguanqun
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2016-4-22
 * @version 1.0
 */		
$(function(){
 	bindEnterEvent('invoiceNoSerc',searchFrom,'easyui');
});				    
	function printPDf(){
		if(null!=inpatientNoSave&&''!=inpatientNoSave){
			var timer=new Date().getTime();
			var tempWinPath = "<c:url value='/inpatient/outbalanceout/printBalace.action?randomId='/>"+timer+"&medicalrecordIdSearch="+inpatientNoSave;
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
		+',scrollbars,resizable=yes,toolbar=yes')
		}else{
			$.messager.alert('提示信息','请先查询出要打印的信息');
			return false;
		}
	}		
</script>
</body>
</html>