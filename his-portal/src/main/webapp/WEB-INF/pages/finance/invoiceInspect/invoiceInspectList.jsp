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
	var intype ="";//点击查询的发票种类 
	var encode="";//点击的节点的ID
	$(function(){
		$('#tdt').tree({
			url:'<%=basePath%>finance/invoiceInspect/queryTreeList.action',
			method:'post',
			animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					if(node.children.length>0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},onClick:function(node){
				if($('#balanceOpcd').combobox('getValue')==""||$('#balanceOpcd').combobox('getValue')==null){
					$.messager.alert('操作提示', '请选择收费员！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				encode=node.attributes.encode;
				intype=encode;
				queryDatagridInfo(encode);
			}
		});
		//加载收费员下拉框
		$('#balanceOpcd').combobox({
			url:"<%=basePath%>finance/invoiceInspect/queryBalanceOpcd.action",
			valueField:'jobNo',
			textField:'name',
			mode:'remote',
			editable:true
		});
		$('#invoiceInfo').datagrid({
			onLoadSuccess:function(data){
				if(data.total==0){
					/* $.messager.alert('操作提示', '该条件下没有信息！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500); */
				}
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
		$("#invoiceInfo").datagrid('loadData', { total: 0, rows: [] });
	});
	
	//根据点击的节点查询列表信息
	function queryDatagridInfo(encode1){
		//如果encode1为空 则是从查询按钮传过来
		if(!encode1){
			if($('#balanceOpcd').combobox('getValue')==""||$('#balanceOpcd').combobox('getValue')==null){
				$.messager.alert('操作提示', '请选择收费员！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
			var node=$('#tdt').tree('getSelected');
			if(node){
				encode=node.attributes.encode;
			}else{
				$.messager.alert('操作提示', '请选择左侧树节点进行查询');
				return false;
			}
		}
		var beginTime=$('#balanceDateBegin').val();
		var endTime=$('#balanceDateEnd').val();
		var balanceOpcd=$('#balanceOpcd').combobox('getValue');
		if(beginTime!="" && endTime!=""){
			var sDate = new Date(beginTime.replace(/\-/g, "\/"));  
			var eDate = new Date(endTime.replace(/\-/g, "\/"));  
			if(sDate>eDate){
				$.messager.alert('操作提示', '开始时间不能大于结束时间！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
		}
		$('#invoiceInfo').datagrid({
			url:"<%=basePath%>finance/invoiceInspect/queryInvoiceInfoList.action",
			queryParams:{"beginTime":beginTime,"endTime":endTime,"balanceOpcd":balanceOpcd,"encode":encode,"menuAlias":"${menuAlias}"}
		});
	}
	
	function clear(){
		$('#balanceOpcd').combobox('setValue','');
		$('#balanceDateBegin').val('');
		$('#balanceDateEnd').val('');
		$("#invoiceInfo").datagrid('loadData', { total: 0, rows: [] });	
	}
	function functionCancel(value,row,index){
		if(intype=="01"){
// 			if(value!=null&&value!=""){
				if(value == 0){
					return "退费";
				}else if(value==1){
					return "有效";
				}else if(value==2){
					return "重打";
				}else if(value==3){
					return "注销";
				}
// 			}
		}else if(intype=="03"){
// 			if(value!=null&&value!=""){
				if(value==1){
					return "有效";
				}else if(value==2){
					return "结算召回";
				}
// 			}
		}
	}
	function functioncheck(value,row,index){
		if(value==undefined||value==0){
			return "未核查";
		}else if(value==1){
			return "已核查";
		}
	}
	function saveT(){
		var rows=$('#invoiceInfo').datagrid('getChecked');
		
		if(rows==""||rows== undefined||rows==null){
			$.messager.alert('操作提示', '请选择要进行保存的数据！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			var mainId="";//Id字符串
			for(var i = 0;i<rows.length;i++){
				if(mainId!=''){
					mainId+=',';
				}
				mainId+=rows[i].mainId;
			}
			$.ajax({
				url:'<%=basePath%>finance/invoiceInspect/saveDatagridInfo.action',
				data:{"rowdate":mainId,"intype":intype},
				type:'post',
				success:function(){
					$.messager.alert('操作提示', '保存成功！'); 	
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					queryDatagridInfo();
				}
			});
		}
	}
	
	
	 /**
	   * 回车弹出申请医师弹框
	   * @author  zhuxiaolu
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToEmployee(){
		   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=balanceOpcd&employeeType=3";
		   window.open (tempWinPath,'newwindow',' left=200,top=75,width='+ (screen.availWidth -300) +',height='
		   + (screen.availHeight-150)+',scrollbars,resizable=yes,toolbar=yes')
				
	   }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',split:true" style="width:10%;height: 94%;padding:5px 5px 5px 5px;border-top:0">
			<ul id="tdt"></ul>
		</div>
		<div data-options="region:'center',border:false" style="width:90%;height: 94%;">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north'" style="height: 45px;border-top:0">
				<div style="height:8px">
				</div>
					<table id="invoice" >
							<tr class="invoiceInspectListSize">
								<td nowrap="nowrap">开始时间：</td>
								<td >
<!-- 									<input id="balanceDateBegin" class="easyui-datetimebox" /> -->
									<input id="balanceDateBegin" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td nowrap="nowrap">&nbsp;结束时间：</td>
								<td>
<!-- 									<input id="balanceDateEnd" class="easyui-datetimebox" /> -->
									<input id="balanceDateEnd" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td nowrap="nowrap">&nbsp;收费员：</td>
								<td >
<!-- 									<input id="balanceOpcd" class="easyui-combobox" data-options="prompt:'回车查询收费员'"/> -->
									<input id="balanceOpcd" class="easyui-combobox" />
									<a href="javascript:delSelectedData('balanceOpcd');"  class="easyui-linkbutton" 
									   data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
								<td>
									<shiro:hasPermission name="${menuAlias}:function:query">
									&nbsp;<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryDatagridInfo()" data-options="iconCls:'icon-search'" >查&nbsp;询&nbsp;</a>
									</shiro:hasPermission>
								</td>
								<td>
									<shiro:hasPermission name="${menuAlias}:function:save">
									&nbsp;<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="saveT()" data-options="iconCls:'icon-save'" >保&nbsp;存&nbsp;</a>
									</shiro:hasPermission>
								</td>
								<td>
								<shiro:hasPermission name="${menuAlias}:function:set">
									&nbsp;<a href="javascript:clear();void(0)"  class="easyui-linkbutton"  data-options="iconCls:'reset'" >重&nbsp;置&nbsp;</a>
								</shiro:hasPermission>
								</td>
							</tr>
					</table>
				</div>
				<div data-options="region:'center',border:false"  style="height: 90%;">
					<table id="invoiceInfo" class="easyui-datagrid" data-options="striped:true,border:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true,rownumbers:true,pagination:true,pageSize:20,pageList:[20,30,50,80,100],fitColumns:false,fit:true">
						<thead>
							<tr>
								<th data-options="field:'checkBoxName',checkbox:'ture',align:'center'">
								<th data-options="field:'mainId',hidden:true">
								<th data-options="field:'checkFlag',width:'16%',align:'center',formatter:functioncheck">是否核销</th>
								<th data-options="field:'invoiceNo',width:'16%',align:'center'">发票号</th>
								<th data-options="field:'cancelFlag',width:'16%',align:'center',formatter:functionCancel">发票状态</th>
								<th data-options="field:'totCost',width:'16%',align:'center'">金额</th>
								<th data-options="field:'operDate',width:'16%',align:'center'">结算时间</th>
								<th data-options="field:'operDate',width:'16%',align:'center'">缴款时间</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>