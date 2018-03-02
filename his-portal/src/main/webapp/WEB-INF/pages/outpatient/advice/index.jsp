<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/metas.jsp"%>
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
				border-right:0px;
				border-top:0px;
			}
			.tableLabel {
				text-align: right;
				width: 70px;
			}
			.tableCss td {
				border-right: 1px solid #95b8e7;
				border-bottom: 1px solid #95b8e7;
				padding: 5px 15px;
				word-break: keep-all;
				white-space: nowrap;
			}
			.divLabel {
				background: #E0ECFF;
				text-align: left;
			}
			#hisEl .panel-header, #patientTabs,.pacs1 .datagrid-wrap,#hisRecordEl .panel-header{
				border-top:0px !important;
			}
			.lis .datagrid-wrap,.pacs .datagrid-wrap,.inpatientInfo .datagrid-wrap,.analysis .datagrid-wrap {
				border-left:0;
				border-right:0;
			}
		</style>
		<script type="text/javascript" src="${pageContext.request.contextPath}/themes/advice/advice.js"></script>
		<script type="text/javascript">
		$(function(){
			/**  
			 *  
			 * 就诊卡号查询
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:type 1待诊2已诊
			 *
			 */
			bindEnterEvent('idCardNo',queryPatient,'easyui');
			
			/**  
			 *  
			 * 待诊信息树
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:type 1待诊2已诊
			 *
			 */
			$('#waitEt').tree({	
				url:"<%=basePath%>outpatient/advice/queryAdviceTree.action?type=1",
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
				onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
				},
				onDblClick:function(node){
					if(node.id=="1"){
						return false;
					}else if(node.attributes.rank=="1"){
						$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
						$('#idCardNo').textbox('setText',node.id);
						var options = $('#adEndAdviceBtn').linkbutton('options');
						if(options.disabled){//非开立状态
							$('#indexTabs').tabs('select','医嘱');
							setTimeout(function(){
								queryPatient(1);
								queryHisAdvice(node.attributes.patientNo);
								emrIframeToList();
							},200);
						}else{
							$('#indexTabs').tabs('select','历史医嘱');
							setTimeout(function(){
								queryHisAdvice(node.attributes.patientNo);
								emrIframeToList();
							},200);
						}
						
					}else{
						var options = $('#adEndAdviceBtn').linkbutton('options');
						if(options.disabled){//非开立状态
							$('#indexTabs').tabs('select','医嘱');
							setTimeout(function(){
								queryPatientByclinicNo(node.id);
								queryHisAdvice(node.attributes.patientNo);
								emrIframeToList();
							},200);
						}else{
							$('#indexTabs').tabs('select','历史医嘱');
							setTimeout(function(){
								queryHisAdvice(node.attributes.patientNo);
								emrIframeToList();
							},200);
						}
					}
				}
			});
		
			/**  
			 *  
			 * 患者页签事件
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:type 1待诊2已诊
			 *
			 */
			$('#patientTabs').tabs({
				onSelect:function(title,index){
					if(index==1){
						var cla = $('#fulfilEt').attr('class');
						if(cla!='tree tree-lines'){
							/**  
							 *  
							 * 已诊信息树
							 * @Author：aizhonghua
							 * @CreateDate：2015-6-26 上午11:56:59  
							 * @Modifier：aizhonghua
							 * @ModifyDate：2015-6-26 上午11:56:59  
							 * @ModifyRmk：  
							 * @version 1.0
							 * @param:type 1待诊2已诊
							 *
							 */
							$('#fulfilEt').tree({	
								url:"<%=basePath%>outpatient/advice/queryAdviceTree.action?type=2",
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
								onBeforeCollapse:function(node){
									if(node.id=="2"){
										return false;
									}
								},
								onDblClick:function(node){
									if(node.id=="2"){
										return false;
									}else if(node.attributes.rank=="1"){
										$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
										$('#idCardNo').textbox('setText',node.id);
										var options = $('#adEndAdviceBtn').linkbutton('options');
										if(options.disabled){//非开立状态
											$('#indexTabs').tabs('select','医嘱');
											setTimeout(function(){
												queryPatient(2);
												queryHisAdvice(node.attributes.patientNo);
												emrIframeToList();
											},200);
										}else{
											$('#indexTabs').tabs('select','历史医嘱');
											setTimeout(function(){
												queryHisAdvice(node.attributes.patientNo);
												emrIframeToList();
											},200);
										}
										
									}else{
										var options = $('#adEndAdviceBtn').linkbutton('options');
										if(options.disabled){//非开立状态
											$('#indexTabs').tabs('select','医嘱');
											setTimeout(function(){
												queryPatientByclinicNo(node.id);
												queryHisAdvice(node.attributes.patientNo);
												emrIframeToList();
											},200);
										}else{
											$('#indexTabs').tabs('select','历史医嘱');
											setTimeout(function(){
												queryHisAdvice(node.attributes.patientNo);
												emrIframeToList();
											},200);
										}
									}
								}
							});
						}
					}else if(index==2){
						var cla = $('#auditEt').attr('class');
						if(cla!='tree tree-lines'){
							/**  
							 *  
							 * 待审核医嘱信息树
							 * @Author：aizhonghua
							 * @CreateDate：2015-6-26 上午11:56:59  
							 * @Modifier：aizhonghua
							 * @ModifyDate：2015-6-26 上午11:56:59  
							 * @ModifyRmk：  
							 * @version 1.0
							 * @param:type 1待诊2已诊
							 *
							 */
							$('#auditEt').tree({	
								url:"<%=basePath%>outpatient/advice/queryAuditTree.action",
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
								onBeforeCollapse:function(node){
									if(node.id=="root"){
										return false;
									}
								},
								onDblClick:function(node){
									if(node.id=="root"){
										return false;
									}else if(node.attributes.rank=="1"){
										$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
									}else{
										var options = $('#adEndAdviceBtn').linkbutton('options');
										if(options.disabled){//非开立状态
											$('#indexTabs').tabs('select','医嘱');
											setTimeout(function(){
												queryPatientByclinicNo(node.id);
												queryAdvice(node.id);
												queryRecord(node.id);
												queryHisAdvice(node.attributes.patientNo);
											},200);
										}else{
											msgShow('提示','开立状态无法审核医嘱！',3000);
										}
									}
								}
							});
						}
					}
				}
			});
			
			/**  
			 *  
			 * 所选药房
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#pharmacyCombobox').combobox({	
				url:"<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action",
				valueField:'id',
				textField:'deptName',
				multiple:false,
				editable:false,
				onLoadSuccess: function (data) {
					if (data.length > 0) {
						$('#pharmacyCombobox').combobox('select', data[0].id);
					}
				},
				onSelect:function(record){
					$.ajax({
						type:"post",
						url:"<%=basePath%>outpatient/advice/savaPharmacyInfo.action",
						data:{pharmacyId:record.id},
						success: function(dataMap) {
							if(dataMap.resMsg=="error"){
								$.messager.alert('提示',dataMap.resCode);
							}
						},
						error: function(){
							$.messager.alert('提示','请求失败！');
						}
					});
				}
			}); 
			
			/**  
			 *  
			 * 医嘱页签事件
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:type 1待诊2已诊
			 *
			 */
			$('#indexTabs').tabs({
				onSelect:function(title,index){
					if(index==1){
						var html = $('#adviceTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/advice.action');
						}
					}else if(index==2){
						var html = $('#hisAdviceTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/hisAdvice.action');
						}
					}else if(index==3){
						var html = $('#drugTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/drug.action');
						}
					}else if(index==4){
						var html = $('#unDrugTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/unDrug.action');
						}
					}else if(index==5){
						var html = $('#lisTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/lis.action');
						}
					}else if(index==6){
						var html = $('#pacsTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/pacs.action');
						}
					}else if(index==7){
						var html = $('#patientTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/patient.action');
						}
					}
// 					else if(index==8){
// 						var html = $('#analysisTabDivId').html();
// 						if(html==''){
<%-- 							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/analysis.action'); --%>
// 						}
// 					}
					else if(index==8){
						var html = $('#inpatientTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/inpatientInforList.action');
						}
					}else if(index==9){
						var html = $('#historyRecordsTabDivId').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/historyRecords.action');
						}
					}else if(index==10){
						var html = $('#emrMain').html();
						if(html==''){
							$(this).tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/emrMain.action?menuAlias=${menuAlias}');
						}
					}
				}
			});
			$('#indexTabs').tabs('getSelected').panel('refresh','<%=basePath%>outpatient/advice/record.action?menuAlias=${menuAlias}');
			setTimeout(function(){
				var html = $('#adviceTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',1).panel('refresh','<%=basePath%>outpatient/advice/advice.action');
				}
			},100);
			setTimeout(function(){
				var html = $('#hisAdviceTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',2).panel('refresh','<%=basePath%>outpatient/advice/hisAdvice.action');
				}
			},200);
			setTimeout(function(){
				var html = $('#drugTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',3).panel('refresh','<%=basePath%>outpatient/advice/drug.action');
				}
			},300);
			setTimeout(function(){
				var html = $('#unDrugTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',4).panel('refresh','<%=basePath%>outpatient/advice/unDrug.action');
				}
			},400);
			setTimeout(function(){
				var html = $('#lisTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',5).panel('refresh','<%=basePath%>outpatient/advice/lis.action');
				}
			},500);
			setTimeout(function(){
				var html = $('#pacsTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',6).panel('refresh','<%=basePath%>outpatient/advice/pacs.action');
				}
			},600);
			setTimeout(function(){
				var html = $('#patientTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',7).panel('refresh','<%=basePath%>outpatient/advice/patient.action');
				}
			},700);
// 			setTimeout(function(){
// 				var html = $('#analysisTabDivId').html();
// 				if(html==''){
<%-- 					$('#indexTabs').tabs('getTab',8).panel('refresh','<%=basePath%>outpatient/advice/analysis.action'); --%>
// 				}
// 			},800);
			//新增
			setTimeout(function(){
				var html = $('#inpatientTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',8).panel('refresh','<%=basePath%>outpatient/advice/inpatientInforList.action');
				}
			},900);
			setTimeout(function(){
				var html = $('#historyRecordsTabDivId').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',9).panel('refresh','<%=basePath%>outpatient/advice/historyRecords.action');
				}
			},1000);
			setTimeout(function(){
				var html = $('#emrMain').html();
				if(html==''){
					$('#indexTabs').tabs('getTab',10).panel('refresh','<%=basePath%>outpatient/advice/emrMain.action?menuAlias=${menuAlias}');
				}
			},1100);
			/**  
			 *  
			 * 选择患者信息
			 * @Author：aizhonghua
			 * @CreateDate：2015-6-26 上午11:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2015-6-26 上午11:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#patientList').datagrid({  
				onDblClickRow:function(rowIndex, rowData){
					voluationPatientInfo(rowData);
					$('#win').window('close');
				}
			});
			
			/**  
			 *  
			 * 键盘监听事件
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-7 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-7 下午06:56:59  
			 * @ModifyRmk： 
			 * @version 1.0
			 *
			 */
			$(document).keydown(function (event) {
				if(event.ctrlKey){
					var tab = $('#indexTabs').tabs('getSelected');
					var index = $('#indexTabs').tabs('getTabIndex',tab);
					if(index==1){//医嘱
						if(event.ctrlKey && event.keyCode === 86){ 
							if($('#focusInpId').val()==0){
								var options = $('#adEndAdviceBtn').linkbutton('options');
								if(options.disabled){//非开立状态
								}else{
									event.preventDefault();
									pasteHis();
								}
							}
						}
						if(event.ctrlKey && event.keyCode === 65){
							if($('#focusInpId').val()==0){
								var options = $('#adEndAdviceBtn').linkbutton('options');
								if(options.disabled){//非开立状态
								}else{
									event.preventDefault();
									$('#adDgList').datagrid('checkAll');
								}
							}
							
						}
						if(event.ctrlKey && event.keyCode === 67){
							if($('#focusInpId').val()==0){
								var options = $('#adEndAdviceBtn').linkbutton('options');
								if(options.disabled){//非开立状态
								}else{
									event.preventDefault();
									copyHis('adDgList');
								}
							}
						}
					}else if(index==2){//历史医嘱
						event.preventDefault();
						if(event.ctrlKey && event.keyCode === 65){
		 					$('#hisAdvice').datagrid('checkAll');
		 				}
						if(event.ctrlKey && event.keyCode === 67){
							copyHis('hisAdvice');
						}
					}
				}
			});
		});
		

		/* /his-portal/outpatient/advice/queryPatientByidCardNo.action */
		/*******************************开始读卡***********************************************/
			//定义一个事件（读卡）
			function read_card_ic(){
				var card_value = app.read_ic();
				if(card_value=='0'||card_value==undefined||card_value==''){
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				//填写就诊卡号
				$("#idCardNo").textbox("setValue",card_value);
				queryPatient('');
			};
		/*******************************结束读卡***********************************************/
		</script>
	</head>
	<body>
		<div id="wholeEl" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west',border:false" style="width:300px;">
				<div id="leftEl" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false" style="width:100%;">
						<div>
							<div style="border-left:1px solid #95b8e7;border-right:1px solid #95b8e7;padding:5px 5px 5px 5px;" class="changeskinadv">
								<input class="easyui-textbox" id="idCardNo" data-options="prompt:'请输入就诊卡号'" style="width:150px"/>
								<shiro:hasPermission name="${menuAlias}:function:readCard">
								<!-- onclick="queryPatient('')"  -->
									<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
<!-- 									<a href="javascript:void(0)" onclick="queryPatient('')" class="easyui-linkbutton adviceIndexLeft" iconCls="icon-bullet_feed" style="height:24px;">读卡</a> -->
								</shiro:hasPermission>
								
								<shiro:hasPermission name="${menuAlias}:function:diagnosis">
									<a href="javascript:void(0)" onclick="passPatient()" class="easyui-linkbutton adviceIndexLeft" iconCls="icon-forward_green" style="height:24px;">诊出</a>
								</shiro:hasPermission>
							</div>
						</div>
						<div>
							<table class="tableCss" width="100%">
								<tr>
									<td class="tableLabel"><input type="hidden" id="idCardNoInpId">姓名：</td>
									<td id="nameTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">科室：</td>
									<td id="deptTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">医生：</td>
									<td id="docTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">门诊号：</td>
									<td id="clinicNoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">合同单位：</td>
									<td id="assUnitTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">病历号：</td>
									<td id="caseNoTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">性别：</td>
									<td id="sexTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">年龄：</td>
									<td id="ageTdId"></td>
								</tr>
								<tr>
									<td class="tableLabel">顺序号：</td>
									<td id="orderNoTdId"></td>
								</tr>
							</table>
						</div>
					</div>
					<div data-options="region:'center',border:false" style="width: 100%">
						<div id="patientTabs" class="easyui-tabs" data-options="fit:true,border:false" style="width:100%;border-right:1px solid #95b8e7;border-top:0">
							<div title="待诊" style="width:100%;" data-options="tools:'#p-tools-waitEt'"> 
								<ul id="waitEt">待诊信息加载中...</ul> 
							</div>  
							<div title="已诊" style="width:100%;" data-options="tools:'#p-tools-fulfilEt'"> 
							   	<ul id="fulfilEt">已诊信息加载中...</ul> 
							</div> 
							<c:if test="${auditing }">
								<div title="医嘱审核" style="width:100%;" data-options="tools:'#p-tools-auditEt'">   
									<ul id="auditEt">待审核信息加载中...</ul> 
								</div>  
							</c:if> 
						</div>
					</div> 
				</div>
			</div>   
			<div data-options="region:'center',border:false">
				<div id="rightEl" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false" style="height:70px;padding:5px 5px 5px 5px;border-bottom:1px solid #95b8e7;" class="changeskinBottom">
						<input id="pharmacyCombobox" class="easyui-combobox" style="width:120px;height:24px;">
						<shiro:hasPermission name="${menuAlias}:function:openOrder">
							<a id="adStartAdviceBtn" href="javascript:void(0);" onclick="javascript:adStartAdvice()" class="easyui-linkbutton" data-options="iconCls:'icon-application_edit',disabled:false" style="width:120px;height:24px;margin-top:5px;">开立医嘱</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:printOrder">
							<a id="adPrintAdviceBtn" href="javascript:void(0);" onclick="javascript:adPrintAdvice()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',disabled:true" style="width:120px;height:24px;margin-top:5px;">打印医嘱</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:quitOrder">
							<a id="adEndAdviceBtn" href="javascript:void(0);" onclick="javascript:adEndAdvice()" class="easyui-linkbutton" data-options="iconCls:'icon-application_edit',disabled:true" style="width:120px;height:24px;margin-top:5px;">退出开立</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:saveOrder">
							<a id="adSaveAdviceBtn" href="javascript:void(0);" onclick="javascript:adSaveAdvice()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_add',disabled:true" style="width:120px;height:24px;margin-top:5px;">保存医嘱</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:delOrder">
							<a id="adDelAdviceBtn" href="javascript:void(0);" onclick="javascript:adDelAdvice()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_delete',disabled:true" style="width:120px;height:24px;margin-top:5px;">删除医嘱</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:grassOrder">
							<a id="adHerbMedicineBtn" href="javascript:void(0);" onclick="javascript:adHerbMedicine()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:120px;height:24px;margin-top:5px;">草药医嘱</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:group">
							<a id="adAddGroupBtn" href="javascript:void(0);" onclick="javascript:adAddGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-application_link',disabled:true" style="width:120px;height:24px;margin-top:5px;">组合</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:delGroup">
							<a id="adCancelGroupBtn" href="javascript:void(0);" onclick="javascript:adCancelGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-application_double',disabled:true" style="width:120px;height:24px;margin-top:5px;">取消组合</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:saveStack">
							<a id="adSaveGroupBtn" href="javascript:void(0);" onclick="javascript:adSaveGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_edit',disabled:true" style="width:120px;height:24px;margin-top:5px;">保存组套</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:printInspect">
							<a id="adInspectionListBtnJY" href="javascript:void(0);" onclick="javascript:adInspectionListJY()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:120px;height:24px;margin-top:5px;">检验单</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:printTest">
							<a id="adInspectionListBtn" href="javascript:void(0);" onclick="javascript:adInspectionCheckList()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:120px;height:24px;margin-top:5px;">检查单</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:queryLis">
							<a id="adLisResultBtn" href="javascript:void(0);" onclick="javascript:adLisResult()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:120px;height:24px;margin-top:5px;">LIS查询</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:queryPacs">
							<a id="adPacsResultBtn" href="javascript:void(0);" onclick="javascript:adPacsResult()" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:120px;height:24px;margin-top:5px;">PACS查询</a>
						</shiro:hasPermission>
						<input type="hidden" id="advName" value="${advName }">
						<input type="hidden" id="advUserName" value="${advUserName }">
						<input type="hidden" id="outMedideptId" value="${outMedideptId }">
						<input type="hidden" id="outMedideptName" value="${outMedideptName }">
						<input type="hidden" id="isAuditing" value="${auditing }">
					</div>
					<div data-options="region:'center',border:false">
						<div id="indexTabs" class="easyui-tabs" data-options="fit:true,border:false">   
							<div title="门诊病历" id="recordTabDivId"></div>
							<div title="医嘱" id="adviceTabDivId"></div>
							<div title="历史医嘱" id="hisAdviceTabDivId"></div>
							<div title="药品" id="drugTabDivId"></div>
							<div title="非药品" id="unDrugTabDivId"></div>
							<div title="LIS查询" id="lisTabDivId"></div>
							<div title="PACS查询" id="pacsTabDivId"></div>
							<div title="患者信息查询" id="patientTabDivId"></div>
<!-- 							<div title="患者分析" id="analysisTabDivId"></div> -->
							<div title="住院患者信息" id="inpatientTabDivId"></div>
							<div title="历史病历" id="historyRecordsTabDivId"></div>
							<div title="电子病历" id="emrMain"></div>
						</div> 
					</div>
				</div>
			</div> 
		</div>
		<div id="win" class="easyui-window" title="患者信息" style="width:650px;height:400px" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,resizable:false,center:true,closed:true">   
			<table id="patientList" class="easyui-datagrid" data-options="fit:true,rownumbers:true, striped:true, checkOnSelect:true, selectOnCheck:false, singleSelect:true, pagination:false,  border:false">
				<thead>
					<tr>
						<th data-options="field:'name', width : '100'">姓名</th>
						<th data-options="field:'regDate', width : '90'">挂号日期</th>
						<th data-options="field:'clinicNo', width : '120'">门诊号</th>
						<th data-options="field:'idCardNo',width :'90'">就诊卡号</th>
						<th data-options="field:'dept',width :'150'">挂号科室</th>
						<th data-options="field:'ynsee',width :'50'">状态</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="addun"></div>
		<div id="chinMediModleDivId" style="left:0;top:0"></div>
		<div id="p-tools-waitEt">
			<a href="javascript:void(0)" class="icon-mini_refresh" onclick="refWaitEt()"></a>
		</div>
		<div id="p-tools-fulfilEt">
			<a href="javascript:void(0)" class="icon-mini_refresh" onclick="refFulfilEt()"></a>
		</div>
		<div id="p-tools-auditEt">
			<a href="javascript:void(0)" class="icon-mini_refresh" onclick="refAuditEt()"></a>
		</div>
	</body>
</html>

