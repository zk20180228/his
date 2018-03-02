<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<style type="text/css">
		.tableCss {
			border-collapse: collapse;
			border-spacing: 0;
		}
		.tableLabel {
			text-align: right;
			width: 100px;
		}
		.tableCss td {
			padding: 5px 15px;
			word-break: keep-all;
			white-space: nowrap;
		}
		</style>
	</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="el" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'west',split:false,border:false" style="width:20%;">
				<ul id="etp">加载中请稍候...</ul>
			</div>   
			<div data-options="region:'center'" style="border-top:0">
				<div class="easyui-layout" data-options="fit:true">   
					<div data-options="region:'north',split:false,border:false">
						<table class="tableCss" cellspacing="5" cellpadding="5" align="center" width="100%" style="border:none !important;">
							<tbody>
								<tr>
									<td class="tableLabel" style="border-left:none !important;border-top:none !important;">就诊卡号：</td>
									<td width="15%" style="border-top:none !important;"><input id="cardNoInpId" class="easyui-textbox" width="45px;"></td>
									<td class="tableLabel" style="border-top:none !important;">门诊号：</td>
									<td id="clinicNoTdId" style="border-top:none !important;"></td>
									<td class="tableLabel" style="border-top:none !important;">患者姓名：</td>
									<td id="nameTdId" style="border-top:none !important;"></td>
									<td class="tableLabel" style="border-top:none !important;">性别：</td>
									<td id="sexTdId" style="border-top:none !important;"></td>
									<td class="tableLabel" style="border-top:none !important;">年龄：</td>
									<td id="ageTdId" style="border-top:none !important;"></td>
								</tr>
								<tr>
									<td class="tableLabel" style="border-left:none !important;">住院号：</td>
									<td id="inpatNoTdId"></td>
									<td class="tableLabel">患者类别：</td>
									<td id="patCateTdId"></td>
									<td class="tableLabel">收费类别：</td>
									<td id="costCateTdId"></td>
									<td class="tableLabel">挂号科室：</td>
									<td id="deptTdId"></td>
									<td class="tableLabel">账户余额：</td>
									<td id="balanceTdId"></td>
								</tr>
							</tbody>
						</table>
					</div>   
					<div data-options="region:'center',border:false">
						<div class="easyui-layout" data-options="fit:true">   
							<div data-options="region:'north',split:false,border:true" style="height:50%;width:100%;border-width:0 0 1px 0">
								<input type="hidden" id="itemIndex">
								<table id="itemList"></table>
							</div>   
							<div data-options="region:'center',border:false">
								<input type="hidden" id="deptId" value="${deptId}">
								<input type="hidden" id="deptName" value="${deptName}">
								<input type="hidden" id="rowIndex">
								<table id="billList"></table>
							</div> 
						</div>
					</div> 
				</div>
			</div>
		</div>
		<div id="itemListToolbarId">
			<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-affirm',plain:true">确认执行</a>
		</div>
<!-- 		该模块作为显示使用，不进行操作 -->
<!-- 		<div id="billListtoolbarId"> -->
<!-- 			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a> -->
<!-- 		</div> -->
		<div id="win" class="easyui-window" title="患者信息" style="width:600px;height:400px" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,resizable:false,center:true,closed:true">   
		    <table id="patientList" class="easyui-datagrid" data-options="fit:true,rownumbers:true, striped:true, checkOnSelect:true, selectOnCheck:false, singleSelect:true, pagination:false,  border:false">
		    	<thead>
					<tr>
						<th data-options="field:'name', width : '30%'">姓名</th>
						<th data-options="field:'clinicNo', width : '30%'">门诊号</th>
						<th data-options="field:'cardNo',width :'30%'">就诊卡号</th>
					</tr>
				</thead>
		    </table>
		</div>  
		<script type="text/javascript">
				var sexMap=new Map();
			$(function(){
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
				$('#cardNoInpId').textbox({prompt:'就诊卡号'});
				bindEnterEvent('cardNoInpId',queryPatient,'easyui');
				$('#patientList').datagrid({  
					onDblClickRow:function(rowIndex, rowData){
						queryPatientByCardNo(rowData.clinicNo);
						$('#win').window('close');
					}
				});
			});
			/**  
			 *  
			 * 患者树
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#etp').tree({    
				url:"<%=basePath%>technical/terminalApply/queryPatientTree.action",
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children!=null&&node.children!=''&&node.children.length!=0){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},
				onDblClick: function(node){
			   		if(node.id!='1'){
			   			clearPatientInfo();
			   			queryPatientByCardNo(node.id);
			   		}
			    },
			    onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
			    }
			}); 
			$.extend($.fn.validatebox.defaults.rules, {    
			    valiGtExecuteNum: {    
			        validator: function(value, param){
			        	var qty = +$.trim($(this).closest("td[field='exeQty']").siblings("td[field='qty']").children('div').html());
			        	var confirmNum = +$.trim($(this).closest("td[field='exeQty']").siblings("td[field='alExeQty']").children('div').html());
			        	var r = (+$.trim(value))>(qty-confirmNum);
			            return (!r);    
			        },    
			        message: '执行数量不能大于项目数量！'   
			    }
			});
			$.extend($.fn.validatebox.defaults.rules, {     
                maxLength: {     
                    validator: function(value, param){     
                        return param[0] >= value.length;     
                    },     
                    message: '输入信息过长，最大{0}位'    
                }     
            }); 
			/**  
			 *  
			 * 医技终端待确认项目列表
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#itemList').datagrid({  
				autoSave:true,
				fit:true,
	            rownumbers:true,
	            striped:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
	            pagination:false,
	            border:false,
	            title:'医技终端待确认项目列表',
	            toolbar:'#itemListToolbarId',
	            url: "<%=basePath%>technical/terminalApply/queryItemList.action",
	            queryParams:{clinicNo:null},
	            onBeforeLoad:function(param){
	            	if(param.clinicNo==null){
	            		return false;
	            	}
	            },
	            columns: [[  	
	                    {field:'ck',checkbox:true},  
	                    {field:'name',title:'项目名称',width:'20%',align:'center'},  
	                    {field:'price',title:'单价',width:'5%',align:'center'},  
	                    {field:'qty',title:'项目数量',width:'5%',align:'center'},  
	                    {field:'spec',title:'规格',width:'5%',align:'center'},  
	                    {field:'money',title:'金额',width:'5%',align:'center'},  
	                    {field:'exeQty',title:'执行数量',editor:{type:'numberbox',options:{required:true,validType:'valiGtExecuteNum'},min:0},width:'5%',align:'center'},  
	                    {field:'alExeQty',title:'已执行数量',width:'5%',align:'center'},  
	                    {field:'state',title:'项目状态',width:'5%',align:'center',formatter: function(value,row,index){
		        				if (value=='1'){
		        					return '收费';
		        				} else {
		        					return '划价';
		        				}
		        			}
						},  
	                    {field:'exeDept',title:'执行科室',width:'8%',align:'center'},  
	                    {field:'exeEqui',title:'执行设备',editor:{type:'textbox',options:{required:true,validType:'maxLength[10]'}},width:'8%',align:'center'},  
	                    {field:'exeUser',title:'执行人',editor:{type:'textbox',options:{required:true,validType:'maxLength[4]'}},width:'8%',align:'center'} 
  	                    
  	            ]],  
  	          	onBeforeEdit:function(rowIndex, rowData){
	            	$('#itemIndex').val(rowIndex);
	            },
	            onDblClickRow:function(index, row){
	            	var i = $('#itemIndex').val();
	            	if(i!=null&&i!=''){
	            		var vali = $(this).datagrid('validateRow',i);
	            		if(vali){
	            			$(this).datagrid('endEdit',parseInt(i));
	            		}else{
		            		$(this).datagrid('cancelEdit',parseInt(i));
	            		}
	            		$('#itemIndex').val(index);
	            	}
	            	$(this).datagrid('beginEdit',index);
	            } 
			});
			
			/**  
			 *  
			 * 医技项目所属发票明细信息
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#billList').edatagrid({  
				fit:true,
	            rownumbers:true,
	            striped:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
	            pagination:false,
	            border:false,
	            title:'医技项目所属发票明细信息',
	            toolbar:'#billListtoolbarId',
	            url: "<%=basePath%>technical/terminalApply/queryBillList.action",
	            queryParams:{clinicNo:null},
	            onBeforeLoad:function(param){
	            	if(param.clinicNo==null){
	            		return false;
	            	}
	            },
	            columns: [[  	
	                    {field:'ck',checkbox:true,hidden:true},
	                    {field:'code',title:'项目编码',width:'8%',align:'left',hidden:true},  
	                    {field:'name',title:'项目名称',width:'20%',align:'left'},  
	                    {field:'specs',title:'规格',width:'3%',align:'center',hidden:true},  
	                    {field:'unit',title:'单位',width:'5%',align:'center',hidden:true},  
	                    {field:'qty',title:'数量',width:'6%',align:'center'},  
	                    {field:'cost',title:'金额',width:'6%',align:'center'},  
	                    {field:'regDept',title:'开立科室',width:'8%',align:'center'},  
	                    {field:'exeDeptId',title:'执行科室',width:'8%',align:'center',hidden:true},  
	                    {field:'exeDept',title:'执行科室',
// 	                    	editor:{type:'combobox',
// 			        		options:{
// 						    	editable:false,
// 						    	disabled:false,
// 						    	required:true,
// 							    valueField:'id',    
// 							    textField:'name',
// 							    onSelect:function(record){
// 							    	var index = $('#rowIndex').val();
// 							    	$('#billList').edatagrid('cancelEdit',index);
// 							    	$('#billList').datagrid('updateRow',{
// 										index: parseInt(index),
// 										row: {
// 											exeDeptId:record.id,
// 											exeDept:record.name
// 										}
// 									});
// 							    }
// 				        	}
// 			        	},
							width:'8%',align:'center'},  
			        	{field:'oldExeDeptId',title:'执行科室',width:'8%',align:'center',hidden:true},  
	                    {field:'oldExeDept',title:'执行科室',width:'8%',align:'center',hidden:true},
	                    {field:'feeUser',title:'收款员',width:'8%',align:'center'},  
	                    {field:'feeTime',title:'收款时间',width:'12%',align:'center'},
	                    {field:'feeStatCodeName',title:'系统类别',width:'8%',align:'center'} 
 	            ]],
 	            onBeforeEdit:function(rowIndex, rowData){
 	            	$('#rowIndex').val(rowIndex);
 	            },
 	           	onEdit:function(index,row){
 	        	 	var ed = $('#billList').datagrid('getEditor', {index:index,field:'exeDept'});
	            	if(ed!=null){
	            		$(ed.target).combobox('loadData', getData(row.oldExeDeptId,row.oldExeDept));
	            	}
 	           	}
			});
			
			/**  
			 *  
			 * 查询患者信息
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function queryPatient(){
				var cardNo = $('#cardNoInpId').textbox('getText');
				if($.trim(cardNo)==null||$.trim(cardNo)==''){
					$.messager.alert('提示','请输入就诊卡号！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				$.messager.progress({text:'查询中，请稍后...',modal:true});
				$.ajax({
					type:"post",
					url:"<%=basePath%>technical/terminalApply/queryPatientByCardNo.action",
					data:{cardNo:$.trim(cardNo)},
					success: function(dataMap) {
						$.messager.progress('close');
						if(dataMap.resMsg=='success'){
							clearPatientInfo();
							if(dataMap.resCode.length==1){
								queryPatientByCardNo(dataMap.resCode[0].clinicNo);
							}else{
								$('#win').window('open');  
								$('#patientList').datagrid('loadData',dataMap.resCode);
							}
						}else{
							$.messager.alert('提示',dataMap.resCode);
						}
					},
			        error: function(){
			        	$.messager.alert('提示','患者信息获取失败！');
			        }
				});
			}
			
			/**  
			 *  
			 * 查询患者详细信息
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function queryPatientByCardNo(clinicNo){
				$.ajax({
					type:"post",
					url:"<%=basePath%>technical/terminalApply/queryPatientInfo.action",
					data:{clinicNo:clinicNo},
					success: function(dataMap) {
						if(dataMap.resMsg=='success'){
							$('#cardNoInpId').textbox('setText',dataMap.resCode.cardNo);
							$('#clinicNoTdId').html(dataMap.resCode.clinicNo);
							$('#nameTdId').html(dataMap.resCode.name);
							$('#sexTdId').html(sexMap.get(dataMap.resCode.sex));
							$('#ageTdId').html(dataMap.resCode.age);
							$('#inpatNoTdId').html(dataMap.resCode.inpatNo);
							$('#patCateTdId').html(dataMap.resCode.patCate);
							$('#costCateTdId').html(dataMap.resCode.costCate);
							$('#deptTdId').html(dataMap.resCode.dept);
							$('#balanceTdId').html(dataMap.resCode.balance+"元");
						}
					},
			        error: function(){
			        	$.messager.alert('提示','患者信息获取失败！');
			        }
				});
	   			$('#itemList').datagrid('load',{clinicNo:clinicNo});
	   			$('#billList').datagrid('load',{clinicNo:clinicNo});
			}
			
			/**  
			 *  
			 * 清空患者信息
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function clearPatientInfo(){
				$('#cardNoInpId').textbox('setText','');
				$('#clinicNoTdId').html('');
				$('#nameTdId').html('');
				$('#sexTdId').html('');
				$('#ageTdId').html('');
				$('#inpatNoTdId').html('');
				$('#patCateTdId').html('');
				$('#costCateTdId').html('');
				$('#deptTdId').html('');
				$('#balanceTdId').html('');
			}
			
			/**  
			 *  
			 * 获得可修改的执行科室
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function getData(oldExeDeptId,oldExeDept){
				var deptId = $('#deptId').val();
				var deptName = $('#deptName').val();
				if(deptId!=null&&deptId!=''&&deptName!=null&&deptName!=''){
					return [{'id':deptId,'name':deptName},{'id':oldExeDeptId,'name':oldExeDept}];
				}
				return [{'id':oldExeDeptId,'name':oldExeDept}];
			}
			
			/**  
			 *  
			 * 医技项目所属发票明细信息修改
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function add(){
				var rows = $('#billList').datagrid('getChecked');
				if(rows!=null&&rows.length>0){
					var record = '';
					for(var i=0;i<rows.length;i++){
						if(rows[i].exeDeptId!=rows[i].oldExeDeptId){
							if(record!=''){
								record += ",";
							}
							record += rows[i].id +"#"+ rows[i].exeDeptId +"#"+ rows[i].appId
						}
					}
					if(record!=''){
						$.ajax({
							type:"post",
							url:"<%=basePath%>technical/terminalApply/saveBillInfo.action",
							data:{record:record},
							success: function(dataMap) {
								$.messager.alert('提示',dataMap.resCode);
								if(dataMap.resMsg=='success'){
									$('#itemList').datagrid('load');
									$('#billList').datagrid('load');
								}
							},
					        error: function(){
					        	$.messager.alert('提示','请求失败！');
					        }
						});
					}else{
						$.messager.alert('提示','所选信息中没有改变的记录！');  
					}
				}else{
					$.messager.alert('提示','请选择要提交的信息！');  
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			
			/**  
			 *  
			 * 医技终端待确认项目修改
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			 function save(){
				 var itemIndex = $('#itemIndex').val();
// 				 if(itemIndex!=null&&itemIndex!=''){
// 					 var validResult = $('#itemList').datagrid('validateRow',parseInt(itemIndex));
// 					 if(!validResult){
// 						 $.messager.alert('提示',"保存信息填写错误，请重新填写！");
// 						 return;
// 					 }else{
// 						 $('#itemList').datagrid('endEdit',parseInt(itemIndex));
// 					 }
// 				 }
				 $('#itemList').edatagrid('endEdit');
				 var rows = $('#itemList').edatagrid('getChecked');
				 if(rows!=null&&rows.length>0){
					 for(var i=0;i<rows.length;i++){
						 var validResult = $('#itemList').datagrid('validateRow',parseInt(i));
						 if(!validResult){
							 $.messager.alert('提示',"保存信息填写错误，请重新填写！");
							 setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							 return;
						 }else{
							 $('#itemList').datagrid('endEdit',parseInt(itemIndex));
						 }
							 $('#itemList').datagrid('endEdit',i);
					 }
					 var result = true;
					 $.each(rows,function(i,rowData){
						 var index = $('#itemList').datagrid("getRowIndex",rowData);
						 $('#itemList').datagrid('beginEdit',index);
						 var validResult = $('#itemList').datagrid('validateRow',index);
						 if(validResult){
							 $('#itemList').datagrid('cancelEdit',index);
						 }else{
							 result = false;
							 return false;
						 }
					 });
					 if(result){
						 var record = '';
						 var pay = false;
						 $.each(rows,function(i,rowData){
							 if(rowData.state=='0'){
								 pay = true;
							 }
							 if(record!=''){
								 record += ',';
							 }
							 record += rowData.appId +"#"+ rowData.feeId +"#"+ rowData.exeQty +"#"+ rowData.exeEqui +"#"+ rowData.exeUser;
						 });
						 if(record!=''){
							 var msg = pay?'提交的信息中有未收费的项目，是否执行并从账户中扣除费用？':'是否执行所选信息？';
							 $.messager.confirm('提示',msg ,function(r){
									if (r){
										$.ajax({
											 type:"post",
											 url:"<%=basePath%>technical/terminalApply/saveItemInfo.action",
											 data:{record:record,clinicNo:$('#clinicNoTdId').text()},
											 success: function(dataMap) {
												 $.messager.alert('提示',dataMap.resCode);
												 if(dataMap.resMsg=='success'){
													 $('#etp').tree('reload');
													 clearPatientInfo();
													 $('#itemList').datagrid('loadData',[]);
													 $('#billList').datagrid('loadData',[]);
												 }
											 },
									         error: function(){
									        	 $.messager.alert('提示','请求失败！');
									         }
										 });
									}
								});
						 }else{
							 $.messager.alert('提示','所选信息中没有改变的记录！');  
						 }
					 }else{
						 $.messager.alert('提示',"保存信息填写错误，请重新填写！");
						 setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
					 }
				 }else{
					 $.messager.alert('提示','请选择要提交的信息！');  
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				 }
			 }
		</script>
	</body>
</html>