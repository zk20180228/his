<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>退号</title>
<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
		//加载退号列表
		$(function(){
			//回车事件
			bindEnterEvent('idcardId',searchFrom,'easyui');
		});
		//根据就诊卡号查询列表
		function searchFrom(){
			var idcardId = $('#idcardId').textbox('getValue');
			var no = $('#no').textbox('getText');
			if((idcardId==null||idcardId=="")&&(no==null||no=="")){
				$.messager.alert("操作提示", "请输入就诊卡号或门诊号");
				return false;
			}
			
			$('#list').datagrid({
				url: "<%=basePath%>register/newInfo/queryBackNo.action",
				queryParams:{"ationInfo.cardNo":idcardId,"ationInfo.clinicCode":no},
				onLoadSuccess:function(rowIndex,rowData){
					$('#list').datagrid('selectRow',0);
					var data=$('#list').datagrid("getSelected");
					if(typeof(data)!="undefined"){
						$('#backName').text(data.patientName);
						$('#backFee').text(data.sumCost);
					}
				},
				onClickRow: function (rowIndex, rowData) {
					$('#backName').text(rowData.patientName);
					$('#backFee').text(rowData.sumCost);
				}
			});
		}
		//退号
		function delFee(){
			var infoId = getIdUtil('#list');
			if(infoId==""||infoId==null){
				return;
			}
			var list = $('#list').datagrid('getSelected');
			var clinicCode = list.clinicCode;
			$.ajax({
				url:'<%=basePath%>register/newInfo/checkISsee.action',
				data:{"clinicCode":clinicCode},
				success:function(dataMap){
					
					if(dataMap.resMsg=="error"){
						$.messager.alert("操作提示",dataMap.resCode);
						return ;
					}else{
						$('#infoId').val(infoId);
						$('#windowOpen').window('open');
					}
				},
				error:function(){
					$.messager.alert("操作提示","请求发送失败，请检查网络！");
				}
			});
			
		}
		//提交
		function updateInfo(){
			var payType = $('#payType').combobox('getValue');
			if(payType==null||payType==""){
				$.messager.alert("操作提示","请选择支付方式");
				return
			}
			var infoId = $('#infoId').val(); 
			var quitreason = $('#quitreason').val(); 
			var payType = $('#payType').combobox('getValue');
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			$.post("<%=basePath%>register/newInfo/updateInfo.action",
		        {"ationInfo.id":infoId,"ationInfo.backnumberReason":quitreason,"ationInfo.payType":payType},
		        function(data){
		        	$.messager.progress('close');
		        	if(data.resMsg=="error"){
		        		$.messager.alert("操作提示",data.resCode);
		        	}else{
		        		$.messager.alert("操作提示",data.resCode);
		        		$('#windowOpen').window('close');
						$('#list').datagrid('load');
		        	}
		        	
		   	});
		}
		function searchReload(){
			delSelectedData('idcardId');
			delSelectedData('no');
			$('#list').datagrid('loadData', { total: 0, rows: [] });
		}
		/*******************************开始读卡***********************************************/
		//定义一个事件（读卡）
		function read_card_ic(){
			var card_value = app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$("#idcardId").textbox("setValue",card_value);
			searchFrom();
		};
		/*******************************结束读卡***********************************************/
	</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout"  data-options="fit:true">
	   <div data-options="region:'north'" style="width: 100%;padding:3px 5px;height: 50px;border-top:0">
		<div style="padding:5px 5px 5px 5px;">	        
			<table style="width:100%;">
				<tr>
					<td>
						&nbsp;就诊卡号：<input name="idcardId" id="idcardId" class="easyui-textbox">
						&nbsp;门诊号：<input class="easyui-textbox" id="no"/>
						&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="margin-left:-11px">查询</a>
						&nbsp;<!-- <a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-bullet_feed">读卡</a> -->
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'" style="margin-left:-21px">读卡</a>
						&nbsp;<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset" style="margin-left:-14px">重置</a>
					</td>
				</tr>
			</table>
		</div>
		</div>
		<div data-options="region:'center'" style="width: 100%;">
		<div >
			<table id="list" style="width:100%;" class="easyui-datagrid" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'clinicCode',width :'10%'">门诊号</th>
						<th data-options="field:'patientName',width :'10%'">姓名</th>
						<th data-options="field:'relaPhone',width :'10%'">联系方式</th>
						<th data-options="field:'doctName',width :'10%'" >挂号专家</th>
						<th data-options="field:'reglevlName',width :'10%'" >挂号级别</th>
						<th data-options="field:'deptName',width :'10%'"  >挂号科室</th>
						<th data-options="field:'pactName',width:'10%'">合同单位</th>
						<th data-options="field:'sumCost',width :'10%'">挂号费</th>
					</tr>
				</thead>
			</table>
		</div>
	  </div>
	</div>
	<div id="toolbarId">
	<div id="windowOpen" class="easyui-window" title="退号操作" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:800px;height:400px;">
			<input type="hidden" id="infoId">
			<table  class="honry-table" style="width: 100%">
				<tr>
					<td class="honry-lable">姓名：</td>
					<td id="backName" style="font-size:14px;width:50px" ></td>
					<td style="font-size:14px;background: #E0ECFF;width:50px">挂号费：</td>
					<td id="backFee"  style="font-size:14px;width:50px"></td>
					<td style="font-size:14px;background: #E0ECFF;width:50px">退费方式：</td>
					<td  style="font-size:14px;width:50px"><input id="payType" class="easyui-combobox" data-options="required:true,valueField: 'id', textField: 'value',data: [{ id: '1', value: '现金'},{ id: '2', value: '院内账户'}]"></td>
				</tr>
				<tr>
					<td class="honry-lable">退号原因：</td>
					<td colspan="5"><textarea class="easyui-validatebox" rows="2" cols="50" id="quitreason" name="quitreason" data-options="multiline:true"></textarea></td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<a href="javascript:updateInfo();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">提交</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#windowOpen').window('close')" data-options="iconCls:'icon-cancel'">关闭</a>
					</td>
				</tr>
			</table>		
		</div>
		&nbsp;<a href="javascript:void(0)" onclick="delFee()" class="easyui-linkbutton" data-options="iconCls:'icon-door_in',plain:true">退号</a>
	</div>
</body>
</html>