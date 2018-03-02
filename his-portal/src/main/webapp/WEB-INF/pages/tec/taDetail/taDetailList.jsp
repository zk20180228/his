<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>门诊医技终端取消</title>
		<%@ include file="/common/metas.jsp" %>
	</head>
	<body style="margin: 0px;padding: 0px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width:100%;height:40px;padding: 5px 5px;">	        
				<input class="easyui-textbox"  id="idCardId" name="" value="" data-options="prompt:'就诊卡号，回车查询'" />&nbsp;
				<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;
				<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			</div>
			<div data-options="region:'center',split:false,border:false" style="width:100%;">
				<div style="height:50%;">
					<input type="hidden" id="inpId">
					<table id="listConfirm" style="" title="已确认项目列表"
						data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
					</table>
				</div>
				<div style="height:50%;">
					<table id="listCancle" style="" title="待取消项目列表"
						data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					</table>
					<form id="editForm" method="post"><input type="hidden" id="tecTaDetailJson" name="tecTaDetailJson" ></form>
				</div>
			</div>
		</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑取消数量</a>
		</div>
		<script type="text/javascript">
			$.extend($.fn.validatebox.defaults.rules, {    
			    valiGtExecuteNum: {    
			        validator: function(value, param){
			        	var qty = +$.trim($(this).closest("td[field='extendField3']").siblings("td[field='haveConfirmNumber']").children('div').html());
			            return (+$.trim(value))<=(qty);    
			        },    
			        message: '取消数量不能大于已确认数量！'   
			    }    
			});
			$(function(){
				$('#listConfirm').datagrid({
					columns:[[
					          {field:'id',title:'终端业务子表id',hidden:true},
					          {field:'itemName',title:'项目名称', width : '10%'},
					          {field:'haveConfirmNumber',title:'已确认数量', width : '8%'},
					          {field:'confirmEmployee',title:'确认人', width : '5%'},
					          {field:'confirmDepartment',title:'确认科室Id', hidden:true},
					          {field:'confirmDeptName',title:'确认科室', width : '8%'},
					          {field:'extendField1',title:'医嘱流水号', width : '8%'},
					          {field:'execDevice',title:'执行设备号', width : '8%'},
					          {field:'execOper',title:'执行人', width : '5%'},
					          {field:'confirmDate',title:'确认时间', width : '8%'},
					          {field:'applyCode',title:'申请单流水号', hidden:true}
					  ]],onDblClickRow:function(rowIndex, rowData){
						  $('#listConfirm').datagrid('deleteRow',rowIndex);
						  $('#listCancle').edatagrid('appendRow',{
							     id: rowData.id,
							     itemName: rowData.itemName,
							     confirmEmployee:rowData.confirmEmployee,
							     confirmDepartment:rowData.confirmDepartment,
							     haveConfirmNumber:rowData.haveConfirmNumber,
							     confirmDeptName:rowData.confirmDeptName,
							     extendField1:rowData.extendField1,
							     execDevice:rowData.execDevice,
							     execOper:rowData.execOper,
							     confirmDate:rowData.confirmDate,
							     applyCode:rowData.applyCode
							});
					  }
				});
				$('#listCancle').edatagrid({
					columns:[[{field:'ck',checkbox:'false'},
					          {field:'id',title:'终端业务子表id',hidden:true},
					          {field:'itemName',title:'项目名称', width : '10%'},
					          {field:'extendField3',title:'取消数量', width : '8%',editor:{type:'numberbox',options:{required:true,validType:'valiGtExecuteNum'}}},
					          {field:'confirmEmployee',title:'确认人', width : '5%'},
					          {field:'confirmDepartment',title:'确认科室Id', hidden:true},
					          {field:'haveConfirmNumber',title:'已确认数量',width : '8%'},
					          {field:'confirmDeptName',title:'确认科室', width : '8%'},
					          {field:'extendField1',title:'医嘱流水号', width : '8%'},
					          {field:'execDevice',title:'执行设备号', width : '8%'},
					          {field:'execOper',title:'执行人', width : '5%'},
					          {field:'confirmDate',title:'确认时间', width : '8%'},
					          {field:'applyCode',title:'申请单流水号', hidden:true}
					  ]],onDblClickRow:function(rowIndex, rowData){
						  $('#listCancle').edatagrid('deleteRow',rowIndex);
						  $('#listConfirm').datagrid('appendRow',{
							  	 id: rowData.id,
							     itemName: rowData.itemName,
							     confirmEmployee:rowData.confirmEmployee,
							     confirmDepartment:rowData.confirmDepartment,
							     haveConfirmNumber:rowData.haveConfirmNumber,
							     confirmDeptName:rowData.confirmDeptName,
							     extendField1:rowData.extendField1,
							     execDevice:rowData.execDevice,
							     execOper:rowData.execOper,
							     confirmDate:rowData.confirmDate,
							     applyCode:rowData.applyCode
							});
					  }
				});
				bindEnterEvent("idCardId",searchMes,'easyui');
				
			});
			
			/*******************************开始读卡***********************************************/
			//定义一个事件（读卡）
			function read_card_ic(){
				var card_value = app.read_ic();
				if(card_value=='0'||card_value==undefined||card_value=='')
				{
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				$("#idCardId").textbox("setValue",card_value);
				searchMes();
			};
			/*******************************结束读卡***********************************************/
			//读卡操作
			function searchMes(){
				//清空列表数据
				var cancleList = $('#listCancle').edatagrid('getRows');
				if(cancleList!=null&&cancleList.length>0){
					var listR = cancleList.length;
					for(var j=0;j<listR;j++){
						$('#listCancle').datagrid('deleteRow',0);
					}
				}
				var listConfirm = $('#listConfirm').edatagrid('getRows');
				if(listConfirm!=null&&listConfirm.length>0){
					var listR = listConfirm.length;
					for(var j=0;j<listR;j++){
						$('#listConfirm').datagrid('deleteRow',0);
					}
				}
				var idCardNo = $('#idCardId').textbox('getValue');
				if(idCardNo!=null&&idCardNo!=''){
					/***
					*2017年3月14日14:43:54 去掉账户状态验证
					*/
					//先根据读卡操作判断就诊卡id的账户是否正常 
// 					$.post("<c:url value='/technical/tecTaDetail/getpatientAccountState.action'/>?idCardNo="+idCardNo,function(data){
// 						if(data.resMsg=="success"){
// 							$('#listConfirm').datagrid({
// 								url:"<c:url value='/technical/tecTaDetail/getTecTaDetail.action'/>?idCardNo="+data.resCode
// 							});
// 						}else{
// 							$.messager.alert('系统提示',data.resCode);
// 						}
// 					});	
					$('#listConfirm').datagrid({
						url:"<c:url value='/technical/tecTaDetail/getTecTaDetail.action'/>?idCardNo="+idCardNo
					});
				}else{
					$.messager.alert('系统提示',"请输入就诊卡号！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			function edit(){
				 var rows = $('#listCancle').datagrid('getChecked');
				 if($('#listCancle').datagrid('getRows').length>0){
					 if (rows.length > 0) {                        
							var ids = '';
							for(var i=0; i<rows.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += rows[i].id;
								var index=$('#listCancle').datagrid('getRowIndex',rows[i]);
								$('#listCancle').datagrid('selectRow', index).datagrid('beginEdit', index);
							};
						}else{
							$.messager.alert("系统提示","请选择编辑的终端确认的记录！");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
				 }else{
					 $.messager.alert("系统提示","待取消终端列表没有记录！");
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				 }
			}
			function save(){
				var rows = $('#listCancle').datagrid('getRows');
				if(rows!=null&&rows.length>0){
					var result = true;//校验标记
					var checkRows = $('#listCancle').datagrid("getChecked");
					if(checkRows==null||checkRows.length<=0){
						$.messager.alert('系统提示',"请选择要取消的项目！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
					$.each($('#listCancle').datagrid("getChecked"),function(i,rowData){
						var index = $('#listCancle').datagrid("getRowIndex",rowData.id);
						$('#listCancle').datagrid('endEdit',index);
						//获取当前行的行索引
						var validResult = $('#listCancle').datagrid('validateRow',index)
						if(validResult){
							$('#listCancle').datagrid('endEdit',index);
						}else{
							$('#listCancle').datagrid('selectRow',index); 
						}
						if(rowData.extendField3==null||rowData.extendField3==""){
							result = false;
						}
						result = validResult&&result;
					}) 
					$.messager.progress({text:'删除中，请稍后...',modal:true});
					if(result){
						$('#tecTaDetailJson').val(JSON.stringify( $('#listCancle').datagrid("getChecked")));
						$('#editForm').form('submit',{
					        url:"<c:url value='/technical/tecTaDetail/saveTecTaDetail.action'/>",  
					        success:function(data){
					        	$.messager.progress('close');
					        	var dataMap = eval("("+data+")");
					        	$.messager.alert('系统提示',dataMap.resCode);
					        	if(dataMap.resMsg=="success"){
					        		$('#idCardId').textbox('setValue',null);
					        		clearDgList('listConfirm');
					        		clearDgList('listCancle');
					        	}
					        },
							error : function(data) {
								$.messager.progress('close');
								$.messager.alert('系统提示',"保存失败！");
							}			         
						});
					}else{
						$.messager.alert('系统提示',"请编辑执行数量！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				}else{
					$.messager.alert('系统提示',"请添加需要取消的终端确认记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//清空datagrid数据
			function clearDgList(id){
				var rows = $('#'+id).datagrid('getRows');
				var len = rows.length;
				for(var i=0;i<len;i++){
					$('#'+id).datagrid('deleteRow',0);
				}		
			}
		</script>
	</body>
</html>