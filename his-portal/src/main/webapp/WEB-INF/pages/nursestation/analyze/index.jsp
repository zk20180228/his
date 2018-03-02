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
		<title>护士站医嘱管理</title>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
		<!-- 插件bug解决方案引入顺序不能改变 -->
		<script>
			detailview.insertRow = function(target, index, row) {
				var opts = $.data(target, 'datagrid').options;
				var dc = $.data(target, 'datagrid').dc;
				var panel = $(target).datagrid('getPanel');
				var view1 = dc.view1;
				var view2 = dc.view2;

				var isAppend = false;
				var rowLength = $(target).datagrid('getRows').length;
				if(rowLength == 0) {
					$(target).datagrid('loadData', {
						total: 1,
						rows: [row]
					});
					return;
				}

				if(index == undefined || index == null || index >= rowLength) {
					index = rowLength;
					isAppend = true;
					this.canUpdateDetail = false;
				}

				$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);

				_insert(true);
				_insert(false);

				this.canUpdateDetail = true;

				function _insert(frozen) {
					var v = frozen ? view1 : view2;
					var tr;
					v.find('tr[datagrid-row-index=' + index + ']').each(function(index, value) {
						//datagrid-row-r22-1-4
						value.id.replace(/datagrid-row-r[0-9]{1}-\d-\d/, function(res) {
							tr = $(value)
						})
					})
					//var tr = v.find('tr[datagrid-row-index='+index+']').eq(0);

					if(isAppend) {
						var newDetail = tr.next().clone();
						tr.insertAfter(tr.next());
					} else {
						var newDetail = tr.next().next().clone();
					}
					newDetail.insertAfter(tr);
					newDetail.hide();
					if(!frozen) {
						newDetail.find('div.datagrid-row-detail').html(opts.detailFormatter.call(target, index, row));
					}
				}
			}
		</script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-datagrid-method-load.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tabs-method-load.js"></script>

		<script type="text/javascript">
			var deptMap = null;
			var moStatMap = new Map();
			var minunitMap = null;
			var packunitMap = null;
			var doseUnitMap = null;
			var sampleMap = null;
			var allDataList = {}
			var adviceIndex = 0
			var analyzeIndex = 0
			var advicetmpTabsOn = {}
			var analyzetmpTabsOn = {}
			var headerTabsOn = {}
			/**  
			 *  
			 * 清除医嘱审核页签
			 * @Author：aizhonghua
			 * @CreateDate：2017-2-08 上午10:45:27  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2017-2-08 上午10:45:27  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function eliminateAuditing(no) {
				if(no != '') {
					var noArr = no.split(',');
					var audSecularEdRows = $('#audSecularEd').datagrid('getRows');
					var audInterimEdRows = $('#audInterimEd').datagrid('getRows');
					for(var i = 0; i < noArr.length; i++) {
						for(var j = audSecularEdRows.length - 1; j >= 0; j--) {
							if(noArr[i] == audSecularEdRows[j].inpatientNo) {
								$('#audSecularEd').datagrid('deleteRow', $('#audSecularEd').datagrid('getRowIndex', audSecularEdRows[j]));
							}
						}
						for(var j = audInterimEdRows.length - 1; j >= 0; j--) {
							if(noArr[i] == audInterimEdRows[j].inpatientNo) {
								$('#audInterimEd').datagrid('deleteRow', $('#audInterimEd').datagrid('getRowIndex', audInterimEdRows[j]));
							}
						}
					}
				} else {
					$('#audSecularEd').datagrid('loadData', []);
					$('#audInterimEd').datagrid('loadData', []);
				}
			}

			/**  
			 * 清除医嘱分解页签
			 * @Author：aizhonghua
			 * @CreateDate：2017-2-08 上午10:45:27  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2017-2-08 上午10:45:27  
			 * @ModifyRmk：  
			 * @version 1.0
			 */
			function eliminateAnalyze(no) {
				if(no != '') {
					var noArr = no.split(',');
					var anaAdvEdRows = $('#anaAdvEd').datagrid('getRows');
					var anaDrugEdRows = $('#anaDrugEd').datagrid('getRows');
					var anaUnDrugEdRows = $('#anaUnDrugEd').datagrid('getRows');
					for(var i = 0; i < noArr.length; i++) {
						for(var j = anaAdvEdRows.length - 1; j >= 0; j--) {
							if(noArr[i] == anaAdvEdRows[j].inpatientNo) {
								$('#anaAdvEd').datagrid('deleteRow', $('#anaAdvEd').datagrid('getRowIndex', anaAdvEdRows[j]));
							}
						}
						for(var j = anaDrugEdRows.length - 1; j >= 0; j--) {
							if(noArr[i] == anaDrugEdRows[j].inpatientNo) {
								$('#anaDrugEd').datagrid('deleteRow', $('#anaDrugEd').datagrid('getRowIndex', anaDrugEdRows[j]));
							}
						}
						for(var j = anaUnDrugEdRows.length - 1; j >= 0; j--) {
							if(noArr[i] == anaUnDrugEdRows[j].inpatientNo) {
								$('#anaUnDrugEd').datagrid('deleteRow', $('#anaUnDrugEd').datagrid('getRowIndex', anaUnDrugEdRows[j]));
							}
						}
					}

				} else {
					$('#anaAdvEd').datagrid('loadData', []);
					$('#anaDrugEd').datagrid('loadData', []);
					$('#anaUnDrugEd').datagrid('loadData', []);
				}
			}
			//删除行的方法
			//noArr 数组
			//targetTableId 元素表id
			function deleteRow(noArr, targetTableId) {
				var rows = $(targetTableId).datagrid('getRows');
				for(var i = 0; i < noArr.length; i++) {
					for(var j = rows.length - 1; j >= 0; j--) {
						if(noArr[i] == rows[j].inpatientNo) {
							$(targetTableId).datagrid('deleteRow', $(targetTableId).datagrid('getRowIndex', rows[j]));
						}
					}
				}
			}

			function setTable(targetTableId, alldata) {
				var add = [];
				var del = [];
				var rows = $(targetTableId).datagrid('getRows');
				var tmp = false
				if(rows.length == 0) return
				for(var key in alldata) {
					if(alldata[key] == true) {
						for(var i = rows.length - 1; i >= 0; i--) {
							if(rows[i].inpatientNo == key) {
								tmp = true
								break
							}
						}
						tmp ? tmp = false : add.push(key)
					} else {
						for(var j = rows.length - 1; j >= 0; i--) {
							if(rows[j].inpatientNo == key) {
								del.push(key)
								break
							}
						}
					}
				}
				return {
					add: add,
					del: del
				}
			}

			//刷新医嘱
			function refreshAuditing(no) {
				//长期医嘱
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAudSecularInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('audSecularEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求长期医嘱信息失败！', null, function() {});
					}
				});
			}

			function tmprefreshAuditing(no) {
				//临时医嘱
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAudInterimInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							//appenAudGrid('audInterimEd',data);
							appnedData('audInterimEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求临时医嘱信息失败！', null, function() {});
					}
				});
			}
			//设置节点存到对象里
			function setAllNo(node, checked) {
				if(node.id == "root") {
					var tmp = node.children
					for(var i = 0; i < tmp.length; i++) {
						allDataList[tmp[i].id] = checked
					}
				} else {
					allDataList[node.id] = checked
				}
			}
			//获取所有选中的 ID
			function getAllNo() {
				var Data = allDataList,
					arr = [];

				for(var key in Data) {
					if(Data[key]) {
						arr.push(key)
					}
				}
				return arr.join(",")
			}

			//医嘱分解方法
			//医嘱信息
			function analyzeEnjoin(no) {
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAdvInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('anaAdvEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求医嘱信息失败！', null, function() {});
					}
				});
			}
			//药品信息
			function AnalyzeDrugEd(no) {
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAnaDrugInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('anaDrugEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求药品信息失败！', null, function() {});
					}
				});
			}
			//非药平信息
			function AnalyzeUnDrugEd(no) {
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAnaUnDrugInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('anaUnDrugEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求非药品信息失败！', null, function() {});
					}
				});
			}
			//对象值为true变成数组add为需要添加
			function objectTrueKeyChangeArr(obj) {
				var add = []
				var del = [];
				for(var key in obj) {
					if(obj[key].item%2 == 0){
						if(obj[key].select == true ) {
							add.push(key)
						} else {
							del.push(key)
						}
					}
				}
				return {
					add: add,
					del: del
				}
			}
		
			function tabObjectTrueKeyChangeArr(obj) {
				var add = []
				var del = [];
				for(var key in obj) {
					if(obj[key] == true ) {
						add.push(key)
					} else {
						del.push(key)
					}
				}
				return {
					add: add,
					del: del
				}
			}
			
			
			function getData() {

			}
		</script>
		<script type="text/javascript">
			//获得科室数据

			$(function() {
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
				$('#analyzeEt').tabs({
					onSelect: function(title, index) {
						$('#auditingButId').linkbutton('disable');
						$('#postilButId').linkbutton('disable');
						$('#analyzeButId').linkbutton('disable');
						$('#sendButId').linkbutton('disable');
						if(index == 0) { //医嘱审核
							$('#auditingButId').linkbutton('enable');
							$('#postilButId').linkbutton('enable');

							setTimeout(function() {
								var selectOk = objectTrueKeyChangeArr(headerTabsOn);
								var addTmprefresh = "";
								var addrefresh = "";

								selectOk.del.length > 0 && (eliminateAuditing(selectOk.del.join(",")))
								if(selectOk.add.length > 0) {
									if(adviceIndex == 0) {
										$('#audSecularEd').datagrid('loading');
										for(var key in advicetmpTabsOn) {
											if(advicetmpTabsOn[key] == true) {
												addTmprefresh = addTmprefresh + "," + key
											}
										}
									}
									if(adviceIndex == 1) {
										$('#audInterimEd').datagrid('loading');
										for(var key in advicetmpTabsOn) {
											if(advicetmpTabsOn[key] == true) {
												addrefresh = addrefresh + "," + key
											}
										}
									}
									refreshAuditing(selectOk.add.join(",") + addrefresh)
									tmprefreshAuditing(selectOk.add.join(",") + addTmprefresh)
								}
								headerTabsOn = {}
								advicetmpTabsOn = {}
							}, 100)
						} else if(index == 1) { //医嘱分解
							$('#analyzeButId').linkbutton('enable');
							$('#sendButId').linkbutton('enable');
							setTimeout(function() {
								var analyzeSelectOk = objectTrueKeyChangeArr(headerTabsOn);
								var drug = ""
								var unDrug = ""
								analyzeSelectOk.del.length > 0 && (eliminateAnalyze(analyzeSelectOk.del.join(",")))
								if(analyzeSelectOk.add.length > 0) {
									$('#anaAdvEd').datagrid('loading');
									if(analyzeIndex == 0) {
										$('#anaDrugEd').datagrid('loading');
										for(var key in analyzetmpTabsOn) {
											if(analyzetmpTabsOn[key] == true) {
												unDrug = unDrug + "," + key
											}
										}
									}
									if(analyzeIndex == 1) {
										$('#anaUnDrugEd').datagrid('loading');
										for(var key in analyzetmpTabsOn) {
											if(analyzetmpTabsOn[key] == true) {
												drug = drug + "," + key
											}
										}
									}
									analyzeEnjoin(analyzeSelectOk.add.join(","))
									AnalyzeDrugEd(analyzeSelectOk.add.join(",") + drug)
									AnalyzeUnDrugEd(analyzeSelectOk.add.join(",") + unDrug)
								}
								analyzetmpTabsOn = {}
								headerTabsOn = {}
							}, 100)

							//							var html = $('#analyze').html();
							//							if(html == '') {
							//								$(this).tabs('getSelected').panel('refresh', '<%=basePath%>nursestation/analyze/analyze.action');
							//							}
						} else if(index == 2) {
							var html = $('#advice').html();
							if(html == '') {
								$(this).tabs('getSelected').panel('refresh', '<%=basePath%>nursestation/analyze/advice.action');
							}
						} else if(index == 3) {
							var html = $('#execute').html();
							if(html == '') {
								$(this).tabs('getSelected').panel('refresh', '<%=basePath%>nursestation/analyze/execute.action');
							}
						} else if(index == 4) {
							if($('#ttt').tabs('getSelected') != null) {
								var billNo = $('#ttt').tabs('getSelected').find("#tabBillNo").val();
								var billType = "";
								if(billNo == null || billNo == '') {} else {
									$.ajax({
										url: "<%=basePath%>inpatient/doctorAdvice/queryInpatientDrugbilldetail.action",
										data: {
											'inpatientDrugbilldetail.billNo': billNo
										},
										type: 'post',
										success: function(data) {
											if(data.total > 0) {
												billType = data.rows[0].billType;
												if(billType == 1) {
													$('#typeId').val("1");
													loadDatagrid(billNo, 1);
												} else if(billType == 2) {
													$('#typeId').val("2");
													loadDatagrid(billNo, 2);
												}
											}
										}
									});
								}
							}
							var html = $('#implementationList').html();
							if(html == '') {
								$(this).tabs('getSelected').panel('refresh', '<%=basePath%>nursestation/analyze/implementationList.action');
							}
						}
					}
				});
				$('#analyzeEt').tabs('getSelected').panel('refresh', '<%=basePath%>nursestation/analyze/auditing.action');
				setTimeout(function() {
					var html = $('#analyze').html();
					if(html == '') {
						$('#analyzeEt').tabs('getTab', 1).panel('refresh', '<%=basePath%>nursestation/analyze/analyze.action');
					}
				}, 100);
				setTimeout(function() {
					var html = $('#advice').html();
					if(html == '') {
						$('#analyzeEt').tabs('getTab', 2).panel('refresh', '<%=basePath%>nursestation/analyze/advice.action');
					}
				}, 200);
				setTimeout(function() {
					var html = $('#execute').html();
					if(html == '') {
						$('#analyzeEt').tabs('getTab', 3).panel('refresh', '<%=basePath%>nursestation/analyze/execute.action');
					}
				}, 300);
				setTimeout(function() {
					var html = $('#implementationList').html();
					if(html == '') {
						$('#analyzeEt').tabs('getTab', 4).panel('refresh', '<%=basePath%>nursestation/analyze/implementationList.action');
					}
				}, 400);
				/**  
				 *  
				 * 患者信息
				 * @Author：aizhonghua
				 * @CreateDate：2016-6-22 下午04:41:31  
				 * @Modifier：aizhonghua
				 * @ModifyDate：2016-6-22 下午04:41:31  
				 * @ModifyRmk：  
				 * @version 1.0
				 *
				 */
				$('#patientEt').tree({
					url: "<%=basePath%>nursestation/analyze/queryPatientTree.action",
					method: 'get',
					lines: true,
					checkbox: true,
					animate: true,
					formatter: function(node) { //统计节点总数
						var s = node.text;
						if(node.children != null && node.children != '' && node.children.length != 0) {
							s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						}
						return s;
					},
					onBeforeCollapse: function(node) {
						if(node.id == "root") {
							return false;
						}
					},
					onSelect: function(node) {
						if(!$(node.target).children("span.tree-checkbox").hasClass("tree-checkbox1")){
							$(this).tree('check', node.target);
						}
					},
					onCheck: function(node, checked) {
						var nodes = $(this).tree('getChecked');
						var tab = $('#analyzeEt').tabs('getSelected');
						var index = $('#analyzeEt').tabs('getTabIndex', tab);
						var no = getNo(nodes, checked);
						setAllNo(node, checked)
						if(node.id == "root" && checked) {
							var addData = no.split(",")
							for(var i = 0; i < addData.length; i++) {
								headerTabsOn[addData[i]] = {
										select : true,
										item:0
								}
								if(index == 0) {
									advicetmpTabsOn[addData[i]] = true
								}
								if(index == 1) {
									analyzetmpTabsOn[addData[i]] = true
								}
							}
						}
						if(node.id == "root" && checked == false) {
							headerTabsOn = {}
							advicetmpTabsOn = {}
							analyzetmpTabsOn = {}
							eliminateAnalyze(no)
							eliminateAuditing(no);
							return
						}
						//headerTabsOn[node.id] = checked
						if(headerTabsOn[node.id] === undefined){
							headerTabsOn[node.id] = {
									select:	checked,
									item :0
							}
						}else if(headerTabsOn[node.id].select !=checked){
							headerTabsOn[node.id].select = checked
							headerTabsOn[node.id].item+=1
						}
						if(index == 0) { //医嘱审核页签
							//$('#anaAdvEd').datagrid('loading');
							//$('#anaDrugEd').datagrid('loading');
							//$('#anaUnDrugEd').datagrid('loading');
							if(checked) { //选择患者
								advicetmpTabsOn[node.id] = true
								if(adviceIndex == 0) {
									$('#audSecularEd').datagrid('loading');
									refreshAuditing(no);
								}
								if(adviceIndex == 1) {
									$('#audInterimEd').datagrid('loading');
									tmprefreshAuditing(no);
								}
								//refreshAnalyze(no);
							} else { //取消选择患者
								advicetmpTabsOn[node.id] = false
								eliminateAuditing(no);
								//eliminateAnalyze(no);
								//$('#audSecularEd').datagrid('loaded');
								//$('#audInterimEd').datagrid('loaded');
								//$('#anaAdvEd').datagrid('loaded');
								//$('#anaDrugEd').datagrid('loaded');
								//$('#anaUnDrugEd').datagrid('loaded');
							}
						} else if(index == 1) { //医嘱分解页签
							if(checked) { //选择患者
								analyzetmpTabsOn[node.id] = true
								$('#anaAdvEd').datagrid('loading');
								analyzeEnjoin(no)
								if(analyzeIndex == 0) {
									$('#anaDrugEd').datagrid('loading');
									AnalyzeDrugEd(no)
								}
								if(analyzeIndex == 1) {
									$('#anaUnDrugEd').datagrid('loading');
									AnalyzeUnDrugEd(no)
								}
								//refreshAuditing(no);
							} else { //取消选择患者
								analyzetmpTabsOn[node.id] = false
								eliminateAnalyze(no);
								//eliminateAuditing(no);
								//$('#anaAdvEd').datagrid('loaded');
								//$('#anaDrugEd').datagrid('loaded');
								//$('#anaUnDrugEd').datagrid('loaded');
								//$('#audSecularEd').datagrid('loaded');
								//$('#audInterimEd').datagrid('loaded');
							}
						} else if(index == 4) {
							var inpatientNo = "";
							for(var i = 0; i < nodes.length; i++) {
								if(inpatientNo == "") {
									inpatientNo = nodes[i].id;
								} else {
									inpatientNo += "," + nodes[i].id;
								}
							}
							var billNo = $('#ttt').tabs('getSelected').find("#tabBillNo").val();
							var billType = "";
							if(billNo == null || billNo == '') {} else {
								$.ajax({
									url: "<%=basePath%>inpatient/doctorAdvice/queryInpatientDrugbilldetail.action",
									data: {
										'inpatientDrugbilldetail.billNo': billNo
									},
									type: 'post',
									success: function(data) {
										if(data.total > 0) {
											billType = data.rows[0].billType;
											if(billType == 1) {
												$('#typeId').val("1");
												loadDatagrid(billNo, 1, inpatientNo);
											} else if(billType == 2) {
												$('#typeId').val("2");
												loadDatagrid(billNo, 2, inpatientNo);
											}
										}
									}
								});
							}
						}
					}
				});
				moStatMap.put("0", "开立");
				moStatMap.put("1", "审核");
				moStatMap.put("2", "执行");
				moStatMap.put("3", "作废");
				moStatMap.put("4", "重整");
				moStatMap.put("-1", "需要上级审核");
				moStatMap.put("-3", "上级审核不通过");
				$.ajax({
					url: "<%=basePath%>baseinfo/department/getDeptMap.action",
					type: 'get',
					success: function(dateMap) {
						deptMap = dateMap;
					}
				});
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=doseUnit",
					type: 'get',
					success: function(dateMap) {
						doseUnitMap = dateMap;
					}
				});
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=minunit",
					type: 'get',
					success: function(dateMap) {
						minunitMap = dateMap;
					}
				});
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=packunit",
					type: 'get',
					success: function(dateMap) {
						packunitMap = dateMap;
					}
				});
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=laboratorysample",
					type: 'get',
					success: function(dateMap) {
						sampleMap = dateMap;
					}
				});
				//扩展验证
				$.extend($.fn.validatebox.defaults.rules, {
					maxLength: {
						validator: function(value, param) {
							return value.length <= param[0];
						},
						message: '请输入小于{0}位字符！'
					}
				});
				$('#analyzeButId').linkbutton('disable');
				$('#sendButId').linkbutton('disable');
				bindEnterEvent('searchTreeInpId', searchTreeNodes, 'easyui');

			});

			/**  
			 *  
			 * 添加数据到医嘱审核页签中的Datagrid
			 * @Author：aizhonghua
			 * @CreateDate：2017-2-08 上午10:45:27  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2017-2-08 上午10:45:27  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function appenAudGrid(id, data) {
				var rows = $('#' + id).datagrid('getRows');
				var allRows = jQuery.extend(true, [], rows)
				for(var i = 0; i < data.length; i++) {
					allRows[allRows.length] = data[i];
				}
				$('#' + id).datagrid('loaded');
				$('#' + id).datagrid('loadData', allRows);
			}

			function appnedData(id, data) {
				var id = '#' + id;
				$(id).datagrid('loaded');
				for(var i = 0; i < data.length; i++) {
					$(id).datagrid('appendRow', {
						ck: "",
						bedName: data[i].bedName,
						patientName: data[i].patientName,
						inpatientNo: data[i].inpatientNo
					})
				}
			}

			/**  
			 *  
			 * 刷新医嘱分解页签
			 * @Author：aizhonghua
			 * @CreateDate：2017-2-08 上午10:45:27  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2017-2-08 上午10:45:27  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function refreshAnalyze(no) {
				//医嘱信息
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAdvInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('anaAdvEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求医嘱信息失败！', null, function() {});
					}
				});
				//药品信息
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAnaDrugInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('anaDrugEd', data);
						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求药品信息失败！', null, function() {});
					}
				});
				//非药品信息
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/queryAnaUnDrugInfo.action",
					type: 'post',
					data: {
						no: no
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'success') {
							var data = dataMap.resCode;
							appnedData('anaUnDrugEd', data);

						} else {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求非药品信息失败！', null, function() {});
					}
				});
			}

			/**  
			 *  
			 * 获取当前选中的患者住院流水号
			 * @Author：aizhonghua
			 * @CreateDate：2017-2-08 上午10:45:27  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2017-2-08 上午10:45:27  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function getNo(nodes, checked) {
				if(nodes.length == 0) {
					$('#pnId').val('');
					return '';
				} else {
					//1.获取前一次操作所选患者
					var pno = $('#pnId').val();
					//2.获取本次操作所选患者
					var no = '';
					for(var i = 0; i < nodes.length; i++) {
						if(nodes[i].id != 'root') {
							if(no != '') {
								no += ',';
							}
							no += nodes[i].id
						}
					}
					if(!pno) {
						$('#pnId').val(no);
						return no;
					} else {
						//3.获得多选的患者
						var retNo1 = pno.split(',');
						var retNo2 = no.split(',');
						var retNo = ''
						for(var i = 0; i < (checked ? retNo2.length : retNo1.length); i++) {
							var sign = true;
							for(var j = 0; j < (checked ? retNo1.length : retNo2.length); j++) {
								if(checked ? retNo2[i] == retNo1[j] : retNo1[i] == retNo2[j]) {
									sign = false;
								}
							}
							if(sign) {
								if(retNo != '') {
									retNo += ',';
								}
								retNo += checked ? retNo2[i] : retNo1[i];
							}
						}
						//4.记录当前所选患者
						$('#pnId').val(no);
						//5.返回结果
						return retNo;
					}
				}
			}

			/**  
			 *  
			 * 判断该行是否被选中
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function isChecked(id, row) {
				var allRows = $('#' + id).datagrid('getChecked');
				if(allRows != null) {
					for(var i = 0; i < allRows.length; i++) {
						if(row.inpatientNo == allRows[i].inpatientNo) {
							return true;
						}
					}
				}
				return false;
			}

			/**  
			 *  
			 * 医嘱审核
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function auditingEvent() {
				//1.获取当前医嘱类型
				//1.1获得当前选中的页签
				var tab = $('#audEt').tabs('getSelected');
				//1.2获得页签的索引
				var index = $('#audEt').tabs('getTabIndex', tab);
				//1.3获得患者列表id 0为长期医嘱（audSecularEd）1为临时医嘱（audInterimEd）
				var patListId = index == 0 ? 'audSecularEd' : 'audInterimEd';
				//1.4获得医嘱列表id 0为长期医嘱（audSecularEd_）1为临时医嘱（audInterimEd_）
				var advListId = index == 0 ? 'audSecularEd_' : 'audInterimEd_';

				//2.获取选择的患者信息
				//2.1获取全部选中的患者信息
				var patDataRows = $("#" + patListId).datagrid('getChecked');
				//2.2对住院流水号进行组装
				var patNoData = ''; //存放患者住院流水号
				for(var i = 0; i < patDataRows.length; i++) {
					if(patNoData != '') {
						patNoData += ",";
					}
					patNoData += patDataRows[i].inpatientNo
				}
				//3.获取选择的医嘱信息
				//3.1获得全部患者信息
				var allRwos = $("#" + patListId).datagrid('getRows');
				//3.2对医嘱id进行组装
				var advIdData = ''; //存放医嘱id
				//3.3遍历所有患者
				for(var i = 0; i < allRwos.length; i++) {
					if(!isChecked(patListId, allRwos[i])) { //如果该患者没有被选中，则获取其对应的选择的医嘱信息
						if($('#' + advListId + i).attr('class') != null && $('#' + advListId + i).attr('class') != '') { //如果面板被展开过
							var advDataRows = $('#' + advListId + i).datagrid('getChecked');
							//3.4遍历所有选择的医嘱信息
							for(var j = 0; j < advDataRows.length; j++) {
								if(advIdData != '') {
									advIdData += ',';
								}
								advIdData += advDataRows[j].id;
							}
						}
					}
				}
				//4.判断审核信息是否为空
				if(patNoData == '' && advIdData == '') {
					$.messager.alert('提示', '请选择需要审核的医嘱信息！', null, function() {});
					return;
				}
				//5.提交审核信息
				commitAuditInfo(patNoData, advIdData, index);
			}

			/**  
			 *  
			 * 医嘱审核及分解方法
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:patNoData 患者id
			 * @param:advIdData 医嘱id
			 * @param:advType 医嘱类型  
			 *
			 */
			function commitAuditInfo(patNoData, advIdData, advType) {
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/commitAuditInfo.action",
					type: 'post',
					data: {
						patNoData: patNoData,
						advIdData: advIdData,
						advType: advType
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'error') {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						} else if(dataMap.resMsg == "exception") {
							$.messager.alert('提示', dataMap.resCode, '', function() {
								if(dataMap.resPar != 0) {
									$.messager.defaults = {
										ok: '确定',
										cancel: '取消',
										width: 500,
										collapsible: false,
										minimizable: false,
										maximizable: false,
										closable: false
									};
									jQuery.messager.confirm("提示", '成功审核' + dataMap.resSuc + '条医嘱信息，失败' + dataMap.resExc + '条，可重新审核' + dataMap.resAdv + '条信息，是否重新审核？', function(event) {
										if(event) { //继续审核
											commitAuditInfo('', dataMap.resPar, advType);
										} else {
											var listId = advType == 0 ? "audSecularEd" : "audInterimEd";
											$('#' + listId).datagrid('reload');
										}
									});
								} else {
									$.messager.alert('提示', '成功审核' + dataMap.resSuc + '条医嘱信息，失败' + dataMap.resExc + '条！', null, function() {});
									eliminateAuditing($('#pnId').val());
									eliminateAnalyze($('#pnId').val());
									refreshAuditing($('#pnId').val());
									refreshAnalyze($('#pnId').val());
								}
							});
						} else if(dataMap.resMsg == 'success') {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
							eliminateAuditing($('#pnId').val());
							eliminateAnalyze($('#pnId').val());
							refreshAuditing($('#pnId').val());
							refreshAnalyze($('#pnId').val());
						} else {
							$.messager.alert('提示', '医嘱审核异常，请刷新后重试！', null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求失败！', null, function() {});
					}
				});
			}

			/**  
			 *  
			 * 批注
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function postilEvent() {
				//1.判断当前所选页签是否为医嘱审核页面
				var analyzeEtIndex = $('#analyzeEt').tabs('getTabIndex', $('#analyzeEt').tabs('getSelected'));
				if(analyzeEtIndex != 0) {
					$.messager.alert('提示', '请切换到医嘱审核页签进行批注编辑！', null, function() {});
					return;
				}
				//2.判断为长期医嘱还是临时医嘱页签
				var audEtIndex = $('#audEt').tabs('getTabIndex', $('#audEt').tabs('getSelected'));
				var ed = 'audSecularEd'; //默认长期医嘱
				if(audEtIndex == 1) {
					ed = 'audInterimEd'; //临时医嘱
				}
				var id = '';
				var moNote1 = '';
				var dgId = '';
				var isOne = true;
				var rows = $('#' + ed).datagrid('getRows');
				if(rows.length > 0) {
					for(var i = 0; i < rows.length; i++) {
						if($('#' + ed + '_' + i).attr('class') == 'datagrid-f') { //判断ed是否已加载
							var edRows = $('#' + ed + '_' + i).datagrid('getChecked');
							if(edRows.length > 0) {
								for(var j = 0; j < edRows.length; j++) {
									if(id != '') {
										isOne = false;
									}
									dgId += ed + '_' + i;
									id += edRows[j].id;
									if(edRows[j].moNote1 != null && edRows[j].moNote1 != '') {
										moNote1 += edRows[j].moNote1;
									}
								}
							}
						}
					}
				}
				//3.判断是否有选中信息
				if(id == '') {
					$.messager.alert('提示', '请选择需要添加批注的医嘱信息！', null, function() {});
					return;
				}
				//4.判断选中信息是否仅为一条
				if(!isOne) {
					$.messager.alert('提示', '只能选择一条医嘱信息进行批注！', null, function() {});
					return;
				}
				//5.初始化模式对话框
				$('#postilWin').window({
					title: '医嘱批注',
					width: '217',
					height: '140',
					collapsible: false,
					minimizable: false,
					maximizable: false,
					closable: false,
					resizable: false,
					modal: true
				});
				//6.赋值
				$('#postilWinDgId').val(dgId);
				$('#postilWinId').val(id);
				$('#postilWinMoNote1').val(moNote1);
				//7.打开模式对话框
				$('#postilWin').window('open');
			}

			/**  
			 *  
			 * 取消批注
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function postilWinCancel() {
				$('#postilWinDgId').val('');
				$('#postilWinId').val('');
				$('#postilWinMoNote1').val('');
				$('#postilWin').window('close');
			}

			/**  
			 *  
			 * 保存批注
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function postilWinSave() {
				var dgId = $('#postilWinDgId').val();
				if(dgId == null || dgId == '') {
					$.messager.alert('提示', '所选信息有误，请重新选择！', null, function() {});
					$('#postilWinId').val('');
					$('#postilWinMoNote1').val('');
					$('#postilWin').window('close');
					return;
				}
				var id = $('#postilWinId').val();
				if(id == null || id == '') {
					$.messager.alert('提示', '所选信息有误，请重新选择！', null, function() {});
					$('#postilWinId').val('');
					$('#postilWinMoNote1').val('');
					$('#postilWin').window('close');
					return;
				}
				if(!$('#postilWinMoNote1').validatebox('isValid')) {
					return;
				}
				$('#analyzeEt').tabs('loading', '批注中，请稍候。。。');
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/postilSave.action",
					type: 'post',
					data: {
						id: id,
						name: $('#postilWinMoNote1').val()
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'error') {
							$('#analyzeEt').tabs('loaded');
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						} else {
							$('#postilWinId').val('');
							$('#postilWinMoNote1').val('');
							$('#postilWin').window('close');
							$('#' + dgId).datagrid('reload');
							$('#analyzeEt').tabs('loaded');
						}
					},
					error: function() {
						$.messager.alert('提示', '请求失败！', null, function() {});
						$('#analyzeEt').tabs('loaded');
					}
				});
			}

			/**  
			 *  
			 * 长期医嘱分解
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function analyzeEvent() {
				//1.获得分解天数
				var anaDate = $('#anaDaysNumId').numberspinner('getValue');
				//2.获得待分解的患者信息
				var checkRows = $("#anaAdvEd").datagrid('getChecked');
				var patNoData = ''; //患者住院流水号
				if(checkRows != null && checkRows.length > 0) {
					for(var i = 0; i < checkRows.length; i++) {
						if(patNoData != '') {
							patNoData += ',';
						}
						patNoData += checkRows[i].inpatientNo;
					}
				}
				var allRwos = $("#anaAdvEd").datagrid('getRows');
				//3.对医嘱id进行组装
				var advIdData = ''; //存放医嘱id
				for(var i = 0; i < allRwos.length; i++) {
					if(!isChecked('anaAdvEd', allRwos[i])) { //如果该患者没有被选中，则获取其对应的选择的医嘱信息
						if($('#anaAdvEd_' + i).attr('class') == 'datagrid-f') { //判断ed是否已加载
							var edRows = $('#anaAdvEd_' + i).datagrid('getChecked');
							if(edRows.length > 0) {
								for(var j = 0; j < edRows.length; j++) {
									if(advIdData != '') {
										advIdData += ',';
									}
									advIdData += edRows[j].id;
								}
							}
						}
					}
				}
				if(advIdData == '' && patNoData == '') {
					$.messager.alert('提示', '请选择需要分解的医嘱信息！', null, function() {});
				} else {
					commitAnalyzeInfo(patNoData, advIdData, anaDate);
				}
			}

			/**  
			 *  
			 * 长期医嘱分解方法
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 * @param:advIdData 医嘱id
			 * @param:anaDate 分解天数
			 *
			 */
			function commitAnalyzeInfo(patNoData, advIdData, anaDate) {
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/commitAnalyzeInfo.action",
					type: 'post',
					data: {
						patNoData: patNoData,
						advIdData: advIdData,
						anaDate: anaDate
					},
					success: function(dataMap) {
						if(dataMap.resMsg == 'error') {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
						} else if(dataMap.resMsg == "exception") {
							$.messager.alert('提示', dataMap.resCode, '', function() {
								if(dataMap.resPar != 0) {
									$.messager.defaults = {
										ok: '确定',
										cancel: '取消',
										width: 500,
										collapsible: false,
										minimizable: false,
										maximizable: false,
										closable: false
									};
									jQuery.messager.confirm("提示", '成功分解' + dataMap.resSuc + '条医嘱信息，失败' + dataMap.resExc + '条，停止医嘱' + dataMap.resStop + '条，可重新分解' + dataMap.resAdv + '条信息，是否重新分解？', function(event) {
										if(event) { //继续分解
											commitAnalyzeInfo(null, dataMap.resPar, anaDate);
										} else {
											eliminateAnalyze($('#pnId').val());
											refreshAnalyze($('#pnId').val());
										}
									});
								} else {
									$.messager.alert('提示', '成功分解' + dataMap.resSuc + '条医嘱信息，失败' + dataMap.resExc + '条，停止医嘱' + dataMap.resStop + '条！', null, function() {});
									eliminateAnalyze($('#pnId').val());
									refreshAnalyze($('#pnId').val());
								}
							});
						} else if(dataMap.resMsg == 'success') {
							$.messager.alert('提示', dataMap.resCode, null, function() {});
							eliminateAnalyze($('#pnId').val());
							refreshAnalyze($('#pnId').val());
						} else {
							$.messager.alert('提示', '医嘱分解异常，请刷新后重试！', null, function() {});
						}
					},
					error: function() {
						$.messager.alert('提示', '请求失败！', null, function() {});
					}
				});
			}

			/**  
			 *  
			 * 长期医嘱发送
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function sendEvent() {
				var tab = $('#anaEt').tabs('getSelected');
				var index = $('#anaEt').tabs('getTabIndex', tab);
				var dgId = index == 0 ? 'anaDrugEd' : 'anaUnDrugEd';
				var checkRows = $("#" + dgId).datagrid('getChecked');
				var patNoData = '';
				if(checkRows != null && checkRows.length > 0) {
					for(var i = 0; i < checkRows.length; i++) {
						if(patNoData != '') {
							patNoData += ',';
						}
						patNoData += checkRows[i].inpatientNo;
					}
				}
				var allRwos = $("#" + dgId).datagrid('getRows');
				//3.对医嘱id进行组装
				var advIdData = ''; //存放医嘱id
				for(var i = 0; i < allRwos.length; i++) {
					if(!isChecked(dgId, allRwos[i])) { //如果该患者没有被选中，则获取其对应的选择的医嘱信息
						if($('#' + dgId + '_' + i).attr('class') == 'datagrid-f') { //判断ed是否已加载
							var edRows = $('#' + dgId + '_' + i).datagrid('getChecked');
							if(edRows.length > 0) {
								for(var j = 0; j < edRows.length; j++) {
									if(advIdData != '') {
										advIdData += ',';
									}
									advIdData += edRows[j].id;
								}
							}
						}
					}
				}
				if(patNoData == '' && advIdData == '') {
					$.messager.alert('提示', '请选择需要发送的医嘱信息！', null, function() {});
					return;
				}
				$.ajax({
					url: "<%=basePath%>nursestation/analyze/sendEvent.action",
					type: 'post',
					data: {
						patNoData: patNoData,
						advIdData: advIdData,
						state: index
					},
					success: function(dataMap) {
						$.messager.alert('提示', dataMap.resCode, null, function() {});
						if(dataMap.resMsg == 'success') {
							eliminateAnalyze($('#pnId').val());
							refreshAnalyze($('#pnId').val());
						}
					},
					error: function() {
						$.messager.alert('提示', '请求失败！', null, function() {});
					}
				});
			}

			/**  
			 *  
			 * 打印
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function printEvent() {
				var qq = $('#analyzeEt').tabs('getSelected');
				var tab = qq.panel('options');
				if(tab.index != 4) {
					$.messager.alert('提示', '请选择医嘱执行单查询');
					setTimeout(function() {
						$(".messager-body").window('close');
					}, 3500);
					return;
				}
				var nodes = $('#patientEt').tree('getChecked');
				if(nodes == null || nodes == "") {
					$.messager.alert('提示', '请选择患者！', null, function() {});
				} else {
					var no = "";
					for(var i = 0; i < nodes.length; i++) {
						if(no == '') {
							no = nodes[i].id;
						} else {
							no += "," + nodes[i].id;
						}
					}
					$("#inpatientNo").val(no);
					//是否有效
					var c = $('input:radio[name="radiobutton"]:checked').val();
					//是否发送
					var b = $('input:radio[name="state"]:checked').val();
					var billNo = $('#ttt').tabs('getSelected').find("#tabBillNo").val();
					var endDate = $("#endDate").val();
					var beginDate = $("#beginDate").val();
					var billName = "";
					var typeId = $('#typeId').val();
					$.ajax({
						url: "<%=basePath%>nursestation/analyze/queryBillName.action",
						type: 'post',
						data: {
							billNo: billNo
						},
						async: false,
						success: function(data) {
							billName = data.billName;
							$("#billName").val(billName);
						}
					});
					var tmpPath = "";
					if(typeId == "1") {
						$.ajax({
							url: "<%=basePath%>nursestation/analyze/queryzhixingdanPdDg.action",
							type: 'post',
							async: false,
							data: {
								no: no,
								beginDate1: beginDate,
								endDate1: endDate,
								billNo: billNo,
								sfyouxiao: c,
								sffasong: b
							},
							success: function(data) {
								if(data == "0") {
									$.messager.alert('提示', '患者没有执行记录记录！', null, function() {});
									return false;
								} else {
									$("#reportToFileName").val("yizhuzhixingdan");
									tmpPath = "<%=basePath%>nursestation/analyze/queryzhixingdanPrint.action";
									$("#sfyouxiao").val(c);
									$("#sffasong").val(b);
									$("#billNo").val(billNo);
									$("#endDate1").val(endDate);
									$("#beginDate1").val(beginDate);
									$("#typeId").val(typeId);

									//表单提交 target
									var formTarget = "hiddenFormWin";
									//设置表单target
									$("#reportToHiddenForm").attr("target", formTarget);
									//设置表单访问路径
									$("#reportToHiddenForm").attr("action", tmpPath);
									//表单提交时打开一个空的窗口
									$("#reportToHiddenForm").submit(function(e) {
										var timerStr = Math.random();
										window.open('about:blank', formTarget, 'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
										window.close();
									});
									//表单提交
									$("#reportToHiddenForm").submit();
								}
							},
							error: function() {
								$.messager.alert('提示', '请求失败！', null, function() {});
							}
						});
					} else {
						$.ajax({
							url: "<%=basePath%>nursestation/analyze/queryzhixingdanPdDgUN.action",
							type: 'post',
							async: false,
							data: {
								no: no,
								beginDate1: beginDate,
								endDate1: endDate,
								billNo: billNo,
								sfyouxiao: c,
								sffasong: b
							},
							success: function(data) {
								if(data == "0") {
									$.messager.alert('提示', '患者没有执行记录记录！', null, function() {});
									return false;
								} else {
									$("#reportToFileName").val("yizhuzhixingdanUn");
									tmpPath = "<%=basePath%>nursestation/analyze/queryzhixingdanUnPrint.action";
									$("#sfyouxiao").val(c);
									$("#sffasong").val(b);
									$("#billNo").val(billNo);
									$("#endDate1").val(endDate);
									$("#beginDate1").val(beginDate);
									$("#typeId").val(typeId);

									//表单提交 target
									var formTarget = "hiddenFormWin";
									//设置表单target
									$("#reportToHiddenForm").attr("target", formTarget);
									//设置表单访问路径
									$("#reportToHiddenForm").attr("action", tmpPath);
									//表单提交时打开一个空的窗口
									$("#reportToHiddenForm").submit(function(e) {
										var timerStr = Math.random();
										window.open('about:blank', formTarget, 'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
										window.close();
									});
									//表单提交
									$("#reportToHiddenForm").submit();
								}
							},
							error: function() {
								$.messager.alert('提示', '请求失败！', null, function() {});
							}
						});
					}
				}
			}

			/**  
			 *  
			 * 全局-是与否的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forYserOrNo(value, row, index) {
				if(value == 0) {
					return '否';
				} else {
					return '是';
				}
			}

			/**  
			 *  
			 * 全局-否与是的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forNoOrYse(value, row, index) {
				if(value == 0) {
					return '是';
				} else {
					return '否';
				}
			}

			/**  
			 *  
			 * 全局-有效与作废的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forValidFlag(value, row, index) {
				if(value == 0) {
					return '作废';
				} else {
					return '有效';
				}
			}

			/**  
			 *  
			 * 全局-记账标记0待记账/1已
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forChargeFlag(value, row, index) {
				if(value == 0) {
					return '待记账';
				} else {
					return '已记账';
				}
			}

			/**  
			 *  
			 * 全局-未打印与已打印的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forPrnFlag(value, row, index) {
				if(value == 0) {
					return '未打印';
				} else {
					return '已打印';
				}
			}

			/**  
			 *  
			 * 全局-0不需发送/1集中发送/2分散发送/3已配药
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forDrugedFlag(value, row, index) {
				if(value == 0) {
					return '不需发送';
				} else if(value == 1) {
					return '集中发送';
				} else if(value == 2) {
					return '分散发送';
				} else if(value == 3) {
					return '已配药';
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-科室的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forDept(value, row, index) {
				if(value != null && value != '') {
					return deptMap[value];
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-包装单位的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forPackunitMap(value, row, index) {
				if(value != null && value != '') {
					return packunitMap[value];
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-药品或非药品包装单位的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forDrugUnDrugUnitMap(value, row, index) {
				if(value != null && value != '') {
					if(row.itemType == '1') {
						return packunitMap[value];
					} else if(row.itemType == '2') {
						return value;
					} else {
						return '';
					}
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-最小单位的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forMinunitMap(value, row, index) {
				if(value != null && value != '') {
					return minunitMap[value];
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-剂量单位的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forDoseUnitMap(value, row, index) {
				if(value != null && value != '') {
					return doseUnitMap[value];
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-剂量单位的渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forSampleMap(value, row, index) {
				if(value != null && value != '') {
					return sampleMap[value];
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-医嘱状态
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function forMoSt(value, row, index) {
				if(value != null && value != '') {
					return moStatMap.get(value);
				} else {
					return '';
				}
			}

			/**  
			 *  
			 * 全局-组合渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function functionGroup(id, value, row, index) {
				var rwos = $('#' + id).datagrid('getRows');
				if(value == null || value == '') {
					return null;
				} else {
					if(index == 0 && rwos.length > 1 && value == rwos[index + 1].combNo) {
						return "┓";
					} else if(index == 0) {
						return "";
					} else if(index == rwos.length - 1 && value == rwos[index - 1].combNo) {
						return "┛";
					} else if(index == rwos.length - 1) {
						return "";
					} else if(value != rwos[index - 1].combNo && value == rwos[index + 1].combNo) {
						return "┓";
					} else if(value == rwos[index - 1].combNo && value != rwos[index + 1].combNo) {
						return "┛";
					} else if(value == rwos[index - 1].combNo && value == rwos[index + 1].combNo) {
						return "┫";
					} else {
						return "";
					}
				}
			}

			/**  
			 *  
			 * 患者树查询
			 * @Author：aizhonghua
			 * @CreateDate：2016-5-9 下午06:56:59  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-5-9 下午06:56:59  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function searchTreeNodes() {
				var searchText = $('#searchTreeInpId').textbox('getValue');
				$("#patientEt").tree("search", searchText);
			}

			function printEventMr() {
				var nodes = $('#patientEt').tree('getChecked');
				if(nodes == null || nodes == '') {
					$.messager.alert('提示', '请选择患者！', null, function() {});
				} else {
					var no = "";
					for(var i = 0; i < nodes.length; i++) {
						if(no == '') {
							no = nodes[i].id;
						} else {
							no += "," + nodes[i].id;
						}
					}
					$("#inpatientNo").val(no);
					$("#reportToFileName").val("DailyChecklist");
					$.ajax({
						url: "<%=basePath%>nursestation/analyze/isExistCost.action",
						type: 'post',
						data: {
							no: no
						},
						success: function(data) {
							if(data == "0") {
								$.messager.alert('提示', '患者今天没有消费记录！', null, function() {});
							} else {
								//表单提交 target
								var formTarget = "hiddenFormWin";
								var tmpPath = "<%=basePath%>nursestation/analyze/querytodayCost.action";
								//设置表单target
								$("#reportToHiddenForm").attr("target", formTarget);
								//设置表单访问路径
								$("#reportToHiddenForm").attr("action", tmpPath);
								//表单提交时打开一个空的窗口
								$("#reportToHiddenForm").submit(function(e) {
									var timerStr = Math.random();
									window.open('about:blank', formTarget, 'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
									window.close();
								});
								//表单提交
								$("#reportToHiddenForm").submit();
							}
						},
						error: function() {
							$.messager.alert('提示', '请求失败！', null, function() {});
						}
					});
				}
			}
		</script>
	<style type="text/css">
		#anaCenEl .panel-header{
			border-top:0;
			border-left:0;
		}
		#anaCenEl .panel-body{
			border-left:0;
		}
	</style>
	</head>

	<body>
		<div id="analyzeEl" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west',split:false,border:false" style="width:230px;padding-left:5px;">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false,border:false">
						<input id="pnId" type="hidden">
						<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'患者姓名'" style="width:130px;" />
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
					</div>
					<div data-options="region:'center',border:false">
						<ul id="patientEt">本区患者信息加载中...</ul>
					</div>
				</div>
			</div>
			<div data-options="region:'center',border:false,collapsible:true">
				<div id="analyzeCenEl" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false,border:false" style="height:35px;padding-top:5px;padding-left:5px;border-left:1px solid #95b8e7;">
						<a id="auditingButId" onclick="auditingEvent()" data-options="iconCls:'icon-folder_go',plain:true" class="easyui-linkbutton">审核</a>
						<a id="postilButId" onClick="postilEvent()" data-options="iconCls:'icon-folder_up',plain:true" class="easyui-linkbutton">批注</a>
						<a id="analyzeButId" onclick="analyzeEvent()" data-options="iconCls:'icon-folder_link',plain:true" class="easyui-linkbutton">分解</a>
						<a id="sendButId" onclick="sendEvent()" data-options="iconCls:'icon-folder_go',plain:true" class="easyui-linkbutton">发送</a>
						<a id="printButId" onclick="printEvent()" data-options="iconCls:'icon-2012081511202',plain:true" class="easyui-linkbutton">打印</a>
						<a id="printButId" onclick="printEventMr()" data-options="iconCls:'icon-2012081511202',plain:true" class="easyui-linkbutton">打印每日清单</a>
					</div>
					<div data-options="region:'center',border:false" style="height:100%">
						<div id="analyzeEt" class="easyui-tabs" data-options="fit:true">
							<div title="医嘱审核" id="auditing"></div>
							<div title="医嘱分解" id="analyze"></div>
							<div title="医嘱查询" id="advice"></div>
							<div title="医嘱执行查询" id="execute"></div>
							<div title="医嘱执行单查询" id="implementationList"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="postilWin" class="easyui-window" align="center" data-options="modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false">
			<div style="text-align:center;">
				<input type="hidden" id="postilWinId">
				<input type="hidden" id="postilWinDgId">
				<textarea class="easyui-validatebox" id="postilWinMoNote1" rows="4" cols="26" data-options="prompt:'批注',validType:'maxLength[35]'"></textarea>
			</div>
			<div style="text-align:center;padding:5px">
				<a href="javascript:postilWinSave();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
				<a href="javascript:postilWinCancel();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>
		<div id="auxModelDivId" style="left:0;top:0"></div>
		<form method="post" id="reportToHiddenForm">
			<input type="hidden" name="no" id="inpatientNo" value="" />
			<input type="hidden" name="fileName" id="reportToFileName" value="" />
			<input type="hidden" name="billName" id="billName" value="" />
			<input type="hidden" name="sfyouxiao" id="sfyouxiao" value="" />
			<input type="hidden" name="sffasong" id="sffasong" value="" />
			<input type="hidden" name="billNo" id="billNo" value="" />
			<input type="hidden" name="endDate1" id="endDate1" value="" />
			<input type="hidden" name="beginDate1" id="beginDate1" value="" />
			<input type="hidden" name="typeId" id="typeId" value="" />
		</form>
	</body>

</html>