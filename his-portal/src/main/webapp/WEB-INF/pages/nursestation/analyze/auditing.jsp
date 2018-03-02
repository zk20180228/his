<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<head>
	</head>

	<body style="margin:0px;padding:0px">
		<div id="audEl" class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',split:false,border:false" style="height:30px;padding-left:5px;padding-top:5px;border-bottom:1px solid #95b8e7;">
				&nbsp;<span style="font-size:12px">新开立：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#00FF00">&nbsp;&nbsp;</span> &nbsp;
				<span style="font-size:12px">已审核：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#4A4AFF">&nbsp;&nbsp;</span> &nbsp;
				<span style="font-size:12px">已执行：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#EEEE00">&nbsp;&nbsp;</span> &nbsp;
				<span style="font-size:12px">已作废：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#FF0000">&nbsp;&nbsp;</span> &nbsp;
				<span style="font-size:12px">重整：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#000000">&nbsp;&nbsp;</span> &nbsp;
				<span style="font-size:12px">医嘱存在批注：</span><span style="color:red">*</span> &nbsp;
				<span style="font-size:12px">知情同意书：</span><span style="font-size:12px">√</span> &nbsp;
				<span style="font-size:12px">存在附材：</span><span style="font-size:12px">@</span> &nbsp;
				<span style="font-size:12px">需审核药品：</span><span style="display:inline-block;width:12px;height:12px;background-image:url(${pageContext.request.contextPath}/themes/system/images/button/shen1.png)"></span>
			</div>
			<div data-options="region:'center',border:false">
				<div id="audEt" class="easyui-tabs" data-options="tabPosition:'bottom',fit:true,border:false" style="width:100%;height:100%;">
					<div title="长期医嘱">
						<table id="audSecularEd"></table>
					</div>
					<div title="临时医嘱">
						<table id="audInterimEd"></table>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			$(function() {

				/**  
				 *  
				 * 审核信息-长期医嘱列表
				 * @Author：aizhonghua
				 * @CreateDate：2016-6-22 下午04:41:31  
				 * @Modifier：aizhonghua
				 * @ModifyDate：2016-6-22 下午04:41:31  
				 * @ModifyRmk：  
				 * @version 1.0
				 *
				 */
				$('#audSecularEd').datagrid({
					view: detailview,
					method: 'post',
					rownumbers: true,
					striped: true,
					border: false,
					checkOnSelect: true,
					selectOnCheck: false,
					singleSelect: true,
					fitColumns: false,
					pagination: false,
					fit: true,
					columns: [
						[{
								field: 'ck',
								checkbox: true
							},
							{
								field: 'bedName',
								title: '床号',
								width: '5%',
								align: 'center'
							},
							{
								field: 'patientName',
								title: '姓名',
								width: '10%',
								align: 'center'
							},
							{
								field: 'inpatientNo',
								title: "住院流水号",
								width: '75%'
							}
						]
					],
					detailFormatter: function(index, row) {
						return '<div><table id="audSecularEd_' + index + '"></table></div>';
					},
					onCheck: function(index, row) {
						if($('#audSecularEd_' + index).attr('class') == 'datagrid-f') {
							var ckArr = $('#audSecularEd_' + index).datagrid('getPanel').find('input');
							for(var i = 1; i < ckArr.length; i++) {
								if(!$(ckArr[i]).is(":checked")) {
									$('#audSecularEd_' + index).datagrid('checkRow', i - 1);
								}
							}
						}
					},
					onUncheck: function(index, row) {
						if($('#audSecularEd_' + index).attr('class') == 'datagrid-f') {
							var ckArr = $('#audSecularEd_' + index).datagrid('getPanel').find('input');
							for(var i = 1; i < ckArr.length; i++) {
								if($(ckArr[i]).is(":checked")) {
									$('#audSecularEd_' + index).datagrid('uncheckRow', i - 1);
								}
							}
						}
					},
					onCheckAll: function(rows) {
						var rows = $(this).datagrid('getRows');
						for(var i = 0; i < rows.length; i++) {
							$(this).datagrid('checkRow', i);
						}
					},
					onUncheckAll: function(rows) {
						var rows = $(this).datagrid('getRows');
						for(var i = 0; i < rows.length; i++) {
							$(this).datagrid('uncheckRow', i);
						}
					},
					onExpandRow: function(index, row) {
						$('#audSecularEd_' + index).datagrid({
							url: "<%=basePath%>nursestation/analyze/queryAudSecularDetails.action",
							queryParams: {
								no: row.inpatientNo
							},
							rownumbers: true,
							striped: true,
							border: true,
							checkOnSelect: true,
							selectOnCheck: false,
							singleSelect: true,
							height: 'auto',
							columns: [
								[{
										field: 'ck',
										checkbox: true
									},
									{
										field: 'permission',
										title: '用法',
										align: 'center',
										hidden: true
									},
									{
										field: 'itemName',
										title: '医嘱名称',
										align: 'left',
										formatter: forAuditingName
									},
									{
										field: 'combNo',
										title: '组',
										align: 'center'
									},
									{
										field: 'specs',
										title: '规格',
										align: 'center'
									},
									{
										field: 'doseOnce',
										title: '每次量',
										align: 'center'
									},
									{
										field: 'frequencyName',
										title: '频次',
										align: 'center'
									},
									{
										field: 'qtyTot',
										title: '数量',
										align: 'center'
									},
									{
										field: 'useDays',
										title: '付数',
										align: 'center'
									},
									{
										field: 'emcFlag',
										title: '急',
										align: 'center'
									},
									{
										field: 'typeName',
										title: '医嘱类型',
										align: 'center'
									},
									{
										field: 'docName',
										title: '开立医生',
										align: 'center'
									},
									{
										field: 'execDpnm',
										title: '执行科室',
										align: 'center'
									},
									{
										field: 'dcDate',
										title: '开立时间',
										align: 'center'
									},
									{
										field: 'dcUsernm',
										title: '停止医生',
										align: 'center'
									},
									// 							{ field: 'moNote1', title: '批注',align:'center'}, 
									{
										field: 'sortId',
										title: '顺序',
										align: 'center'
									},
									{
										field: 'pharmacyCode',
										title: '取药药房',
										align: 'center',
										hidden: true
									},
									{
										field: 'hypotest',
										title: '皮试',
										align: 'center'
									},
									// 							{ field: 'itemNote', title: '检查',align:'center'},
									// 							{ field: 'labCode', title: '化验',align:'center'},
									{
										field: 'setSubtbl',
										title: '附材',
										align: 'center',
										formatter: forYserOrNo
									}
								]
							],
							onResize: function() {
								$('#audSecularEd').datagrid('fixDetailRowHeight', index);
							},
							onLoadSuccess: function() {
								$(".note").tooltip({
									onShow: function() {
										$(this).tooltip('tip').css({
											boxShadow: '1px 1px 3px #292929'
										});
									}
								});
								if(isChecked('audSecularEd', row)) {
									$('#audSecularEd_' + index).datagrid('checkAll');
								}
								setTimeout(function() {
									$('#audSecularEd').datagrid('fixDetailRowHeight', index);
								}, 0);
							},
							onDblClickRow: function(index, row) {
								if(row.id != null && row.id != '') {
									dynamicAddingLayOut('AuxDivId', 'audEl', 'east', '60%', 'post', '<%=basePath%>nursestation/analyze/queryAuxiliaryInfo.action', {
										id: row.id,
										name: '[' + row.itemName + ']'
									});
								}
							},
							onCheck: function(index, row) {
								var rows = $(this).datagrid('getRows');
								var checkRows = $(this).datagrid('getChecked');
								if(checkRows.length == rows.length) {
									var id = $(this).prop('id');
									var i = id.split('_')[1];
									$('#audSecularEd').datagrid('checkRow', parseInt(i));
								}
							},
							onUncheck: function(index, row) {
								var rows = $(this).datagrid('getRows');
								var checkRows = $(this).datagrid('getChecked');
								if(checkRows.length != rows.length) {
									var id = $(this).prop('id');
									var i = id.split('_')[1];
									var ckArr = $(this).datagrid('getPanel').find('input');
									var nckArr = new Array();
									for(var j = 1; j <= ckArr.length; j++) {
										nckArr[nckArr.length] = $(ckArr[j]).is(":checked");
									}
									$('#audSecularEd').datagrid('uncheckRow', parseInt(i));
									for(var x = 0; x < nckArr.length; x++) {
										if(nckArr[x]) {
											$(this).datagrid('checkRow', x);
										}
									}
								}
							},
							onCheckAll: function(rows) {
								var id = $(this).prop('id');
								var i = id.split('_')[1];
								$('#audSecularEd').datagrid('checkRow', parseInt(i));
							},
							onUncheckAll: function(rows) {
								var id = $(this).prop('id');
								var i = id.split('_')[1];
								$('#audSecularEd').datagrid('uncheckRow', parseInt(i));
							}
						});
						$('#audSecularEd').datagrid('fixDetailRowHeight', index);
					}
				});

				/**  
				 *  
				 * 审核信息-临时医嘱列表
				 * @Author：aizhonghua
				 * @CreateDate：2016-6-22 下午04:41:31  
				 * @Modifier：aizhonghua
				 * @ModifyDate：2016-6-22 下午04:41:31  
				 * @ModifyRmk：  
				 * @version 1.0
				 *
				 */
				$('#audInterimEd').datagrid({
					view: detailview,
					method: 'post',
					rownumbers: true,
					striped: true,
					border: false,
					checkOnSelect: true,
					selectOnCheck: false,
					singleSelect: true,
					fitColumns: true,
					pagination: false,
					fit: true,
					columns: [
						[{
								field: 'ck',
								checkbox: true
							},
							{
								field: 'bedName',
								title: '床号',
								width: '5%',
								align: 'center'
							},
							{
								field: 'patientName',
								title: '姓名',
								width: '10%',
								align: 'center'
							},
							{
								field: 'inpatientNo',
								title: "住院流水号",
								width: '75%'
							}
						]
					],
					detailFormatter: function(index, row) {
						return '<div><table id="audInterimEd_' + index + '"></table></div>';
					},
					onCheck: function(index, row) {
						if($('#audInterimEd_' + index).attr('class') == 'datagrid-f') {
							var ckArr = $('#audInterimEd_' + index).datagrid('getPanel').find('input');
							for(var i = 1; i < ckArr.length; i++) {
								if(!$(ckArr[i]).is(":checked")) {
									$('#audInterimEd_' + index).datagrid('checkRow', i - 1);
								}
							}
						}
					},
					onUncheck: function(index, row) {
						if($('#audInterimEd_' + index).attr('class') == 'datagrid-f') {
							var ckArr = $('#audInterimEd_' + index).datagrid('getPanel').find('input');
							for(var i = 1; i < ckArr.length; i++) {
								if($(ckArr[i]).is(":checked")) {
									$('#audInterimEd_' + index).datagrid('uncheckRow', i - 1);
								}
							}
						}
					},
					onCheckAll: function(rows) {
						var rows = $(this).datagrid('getRows');
						for(var i = 0; i < rows.length; i++) {
							$(this).datagrid('checkRow', i);
						}
					},
					onUncheckAll: function(rows) {
						var rows = $(this).datagrid('getRows');
						for(var i = 0; i < rows.length; i++) {
							$(this).datagrid('uncheckRow', i);
						}
					},
					onExpandRow: function(index, row) {
						$('#audInterimEd_' + index).datagrid({
							url: "<%=basePath%>nursestation/analyze/queryAudInterimDetails.action",
							queryParams: {
								no: row.inpatientNo
							},
							rownumbers: true,
							striped: true,
							border: true,
							checkOnSelect: true,
							selectOnCheck: false,
							singleSelect: true,
							height: 'auto',
							columns: [
								[{
										field: 'ck',
										checkbox: true
									},
									{
										field: 'permission',
										title: '用法',
										align: 'center',
										hidden: true
									},
									{
										field: 'itemName',
										title: '医嘱名称',
										align: 'left',
										formatter: forAuditingName
									},
									{
										field: 'combNo',
										title: '组',
										align: 'center'
									},
									{
										field: 'specs',
										title: '规格',
										align: 'center'
									},
									{
										field: 'doseOnce',
										title: '每次量',
										align: 'center'
									},
									{
										field: 'qtyTot',
										title: '数量',
										align: 'center'
									},
									{
										field: 'useDays',
										title: '付数',
										align: 'center'
									},
									{
										field: 'emcFlag',
										title: '急',
										align: 'center'
									},
									{
										field: 'typeName',
										title: '医嘱类型',
										align: 'center'
									},
									{
										field: 'docName',
										title: '开立医生',
										align: 'center'
									},
									{
										field: 'execDpnm',
										title: '执行科室',
										align: 'center'
									},
									{
										field: 'dcDate',
										title: '开立时间',
										align: 'center'
									},
									{
										field: 'dcUsernm',
										title: '停止医生',
										align: 'center'
									},
									// 							{ field: 'moNote1', title: '批注',align:'center'}, 
									{
										field: 'sortId',
										title: '顺序',
										align: 'center'
									},
									{
										field: 'hypotest',
										title: '皮试',
										align: 'center'
									},
									// 							{ field: 'itemNote', title: '检查',align:'center'},
									// 							{ field: 'labCode', title: '化验',align:'center'},
									{
										field: 'pharmacyCode',
										title: '取药药房',
										align: 'center',
										hidden: true
									},
									{
										field: 'setSubtbl',
										title: '附材',
										align: 'center',
										formatter: forYserOrNo
									}
								]
							],
							onResize: function() {
								$('#audInterimEd').datagrid('fixDetailRowHeight', index);
							},
							onLoadSuccess: function() {
								$(".note").tooltip({
									onShow: function() {
										$(this).tooltip('tip').css({
											boxShadow: '1px 1px 3px #292929'
										});
									}
								});
								if(isChecked('audInterimEd', row)) {
									$('#audInterimEd_' + index).datagrid('checkAll');
								}
								setTimeout(function() {
									$('#audInterimEd').datagrid('fixDetailRowHeight', index);
								}, 0);
							},
							onDblClickRow: function(index, row) {
								if(row.id != null && row.id != '') {
									dynamicAddingLayOut('AuxDivId', 'audEl', 'east', '60%', 'post', '<%=basePath%>nursestation/analyze/queryAuxiliaryInfo.action', {
										id: row.id,
										name: '[' + row.itemName + ']'
									});
								}
							},
							onCheck: function(index, row) {
								var rows = $(this).datagrid('getRows');
								var checkRows = $(this).datagrid('getChecked');
								if(checkRows.length == rows.length) {
									var id = $(this).prop('id');
									var i = id.split('_')[1];
									$('#audInterimEd').datagrid('checkRow', parseInt(i));
								}
							},
							onUncheck: function(index, row) {
								var rows = $(this).datagrid('getRows');
								var checkRows = $(this).datagrid('getChecked');
								if(checkRows.length != rows.length) {
									var id = $(this).prop('id');
									var i = id.split('_')[1];
									var ckArr = $(this).datagrid('getPanel').find('input');
									var nckArr = new Array();
									for(var j = 1; j <= ckArr.length; j++) {
										nckArr[nckArr.length] = $(ckArr[j]).is(":checked");
									}
									$('#audInterimEd').datagrid('uncheckRow', parseInt(i));
									for(var x = 0; x < nckArr.length; x++) {
										if(nckArr[x]) {
											$(this).datagrid('checkRow', x);
										}
									}
								}
							},
							onCheckAll: function(rows) {
								var id = $(this).prop('id');
								var i = id.split('_')[1];
								$('#audInterimEd').datagrid('checkRow', parseInt(i));
							},
							onUncheckAll: function(rows) {
								var id = $(this).prop('id');
								var i = id.split('_')[1];
								$('#audInterimEd').datagrid('uncheckRow', parseInt(i));
							}
						});
						$('#audInterimEd').datagrid('fixDetailRowHeight', index);
					}
				});
			});

			/**  
			 *  
			 * 医嘱审核-医嘱名称渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forAuditingName(value, row, index) {
				if(row.permission == "1") {
					value = "√" + value;
				}
				if(row.setSubtbl == "1") {
					value = "@" + value;
				}
				if(row.moNote1 != null) {
					value = '<a href="javascript:void(0)" title="<span style=&quot;color:red&quot;>批注</span>：<span style=&quot;color:#0000A0&quot;>' + row.moNote1 + '</span>" class="note">' + "<span style='color:red'>*</span>" + value + '</a>';
				}
				return value;
			}

			/**  
			 *  
			 * 医嘱审核-动态添加LayOut
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function dynamicAddingLayOut(divId, layOutId, region, width, method, url, params) {
				if(!method) {
					method = "get";
				}
				if(!params) {
					params = {};
				}
				var eastpanel = $('#' + divId); //获取右侧收缩面板
				if(eastpanel.length > 0) { //判断右侧收缩面板是否存在
					$('#' + layOutId).layout('remove', region);
					$('#' + layOutId).layout('add', {
						region: region,
						width: width,
						border: false,
						split: false,
						href: url,
						method: method,
						queryParams: params,
						closable: true
					});
				} else { //打开新面板
					$('#' + layOutId).layout('add', {
						region: region,
						width: width,
						border: false,
						split: false,
						href: url,
						method: method,
						queryParams: params,
						closable: true
					});
				}
			}

			// 获取选项卡

			$('#audEt').tabs({
				onSelect: function(title, index) {
					adviceIndex = index
					var selectOk = tabObjectTrueKeyChangeArr(advicetmpTabsOn);
					if(index == 0) {
						selectOk.del.length > 0 && (eliminateAuditing(selectOk.del.join(",")))
						if(selectOk.add.length > 0){
							$('#audSecularEd').datagrid('loading');
							refreshAuditing(selectOk.add.join(","))
						}
					}
					if(index == 1) {
						selectOk.del.length > 0 && (eliminateAuditing(selectOk.del.join(",")))
						if(selectOk.add.length > 0){
							$('#audInterimEd').datagrid('loading');
							tmprefreshAuditing(selectOk.add.join(","))
						}
					}
					advicetmpTabsOn = {};
				}
			});
		</script>
	</body>

</html>