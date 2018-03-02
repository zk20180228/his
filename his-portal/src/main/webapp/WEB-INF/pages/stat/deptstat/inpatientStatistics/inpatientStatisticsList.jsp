<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/activiti/modeler/editor-app/libs/bootstrap_3.1.1/css/bootstrap.css" ></link>
	<head>
		<title>住院人数统计</title>
		<style type="text/css">
			* {
				box-sizing: border-box;
			}
			body,
			html {
				width: 100%;
				height: 100%;
				overflow: visible;
				min-width: 1100px;
			}
			
			.heder_box {
				padding: 10px 0;
			}
			
			.heder_box>div {
				float: left;
				margin-left: 15px;
			}
			
			.select2-selection.select2-selection--multiple {
				height: 34px;
				overflow-x: auto;
			}
			
			.cardInfo {
				width: 100%;
				/*height: 750px;*/
				border: 1px solid #FF0103;
				border-radius: 10px;
				margin-bottom: 30px;
			}
			
			.content.container-fluid {
				margin-top: 10px;
			}
			
			.cardInfo-tonTable {
				width: calc(100% - 20px);
				height: 250px;
				margin: 0 10px;
			}
			
			.cardInfo-huanTable {
				width: calc(100% - 20px);
				height: 250px;
				margin: 0 10px;
			}
			
			.radio.radio-success {
				float: left;
				width: 270px;
			}
			
			.timeTitle {
				font-weight: 900;
			}
			
			.form-group.has-feedback {
				width: 300px;
				margin-left: 20px;
				float: left;
			}
			
			.timeTitle {
				width: 100px;
				float: left;
				margin-right: 15px;
			}
			
			.radioItem {
				width: 45px;
				float: left;
			}
			
			.dayTime {
				float: left;
			}
			
			#timeInput {
				width: 100px;
			}
			
			.newMenu .menuInput {
				height: 22px;
			}
			
			#ksnew {
				padding: 0;
			}
			
			.heder_box .seachTitle {
				float: left;
				margin-left: 15px;
				margin-right: -10px;
			}
			
			.hidem2 {
				display: none !important;
			}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
		<script type="text/javascript">
		
		var menuAlias ="${menuAlias}";
		var cames;//院区
			$(function() {
				$.ajax({//查询授权院区
					url: "<c:url value='/baseinfo/department/getAuthorArea.action'/>?menuAlias="+menuAlias,
					type:"post",
					async:false,
					success: function(date){
						cames=date;
					}
				});
				//选择科室
				$(".deptInput").MenuList({
					width :530, //设置宽度，不写默认为530，不要加单位
					height :400, //设置高度，不写默认为400，不要加单位
					menulines:2, //设置菜单每行显示几列（1-5），默认为2
					dropmenu:"#m3",//弹出层id，必须要写
					isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
					para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
					firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
				});
				var sign=cames.length;
				if(sign==undefined||sign==0){//如果院区为空查看是否授权科室
					$('#m3 .addList h2 input').click();
					$('a[name=\'menu-confirm\']').click();
					$('#yuanqu').hide();
				}else{
					$('#yuanqu').show();
				}
				if(($('#ksnew').getMenuIds()=='')&&(sign==undefined||sign==0)){
					$("body").setLoading({
						id:"body",
						isImg:false,
						text:"无数据权限"
					});
				}else{
				$('a[name=\'menu-confirm-clear\']').click();
				$("#drugCostType").combobox({
					mode: 'remote',
					valueField: 'code',
					textField: 'name',
					multiple: true,
					data:cames
				});

				$.extend($.fn.datagrid.methods, {
					autoMergeCells: function(jq, fields) {
						return jq.each(function() {
							var target = $(this);
							if(!fields) {
								fields = target.datagrid("getColumnFields");
							}
							var rows = target.datagrid("getRows");
							var i = 0,
								j = 0,
								temp = {};
							for(i; i < rows.length; i++) {
								var row = rows[i];
								j = 0;
								for(j; j < fields.length; j++) {
									var field = fields[j];
									var tf = temp[field];
									if(!tf) {
										tf = temp[field] = {};
										tf[row[field]] = [i];
									} else {
										var tfv = tf[row[field]];
										if(tfv) {
											tfv.push(i);
										} else {
											tfv = tf[row[field]] = [i];
										}
									}
								}
							}
							$.each(temp, function(field, colunm) {
								$.each(colunm, function() {
									var group = this;
									if(group.length > 1) {
										var before,
											after,
											megerIndex = group[0];
										for(var i = 0; i < group.length; i++) {
											before = group[i];
											after = group[i + 1];
											if(after && (after - before) == 1) {
												continue;
											}
											var rowspan = before - megerIndex + 1;
											if(rowspan > 1) {
												target.datagrid('mergeCells', {
													index: megerIndex,
													field: field,
													rowspan: rowspan
												});
											}
											if(after && (after - before) != 1) {
												megerIndex = after;
											}
										}
									}
								});
							});
						});
					}
				});
				$("#time3").val(getNowFormatDate());
			}
		});
		</script>
		<div class=" heder_box form-horizontal clearfix">
			<div>
				时间维度 按： <label><input id = "year" name="type" class="dateType" type="radio" value="年"
				style="margin-left: 25px" /> 年</label> <label><input name="type"
				id = "mth" type="radio"  class="dateType" value="月" style='margin-left: 25px' /> 月</label> <label><input
				id = "day" name="type"  class="dateType" type="radio" value="日" checked style="margin-left: 25px" />
				日</label> 时间段： <input id="time1" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedFunc})" type="text" style="display: none;" /> <input id="time2" class="Wdate" style="display: none;" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc})" type="text" /> <input id="time3" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFunc})" type="text" />
			</div>
			<div id="yuanqu" style="display:none;">
				选择院区：<input class="easyui-combobox" id="drugCostType" style="width:120px">
			</div>
			<span class="seachTitle">选择科室：</span>
			<div class="newMenu">
				<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="ksnew" readonly="readonly" /><span></span></div>
				<div id="m3" class="xmenu" style="display: none;">
   	       			<div class="searchDept">
						<input type="text" name="searchByDeptName" placeholder = "回车查询" />
						<span class="searchMenu"><i></i>查询</span> <a
							name="menu-confirm-cancel" href="javascript:void(0);"
							class="a-btn"> <span class="a-btn-text">取消</span>
						</a> <a name="menu-confirm-clear" href="javascript:void(0);"
							class="a-btn"> <span class="a-btn-text">清空</span>
						</a> <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
							<span class="a-btn-text">确定</span>
						</a>
					</div>
					<div class="select-info" style="display: none">
						<label class="top-label">已选部门：</label>
						<ul class="addDept">

						</ul>
					</div>
					<div class="depts-dl">
						<div class="addList"></div>
						<div class="tip" style="display: none">没有检索到数据</div>
					</div>	
				</div>
			</div>
			<div class="btn-time">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="height:23px;margin-top:-3px">查询</a>
				<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset" style="height:23px;margin-top:-3px">重置</a>
			</div>
		</div>
		<div id="cardAll" class="content container-fluid"></div>
		

		<script src="<%=basePath%>javascript/echarts/echarts.min.js" type="text/javascript"></script>
		<script type="text/html" id="cardInfoScript">
			{{each data as value index}}
			<div class="cardInfoBox col-lg-3 col-md-4">
				<div class="cardInfo">
					<table id="dataTable{{index}}" class="table table-bordered table-hover">
						<thead>
							<tr>
								<th>科室/院区</th>
								<th>人数</th>
								<th>占比(%)</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>{{value.title}}</td>
								<td>{{value.dataTable.total}}</td>
								<td>{{value.dataTable.gender}}</td>
							</tr>
						</tbody>
					</table>
					{{if value.tonData}}
					<table style="height:250px" id="tonData{{index}}" class="cardInfo-tonTable"></table>
					{{/if}}
					<table style="height:250px" id="huanData{{index}}" class="cardInfo-huanTable"></table>
				</div>
			</div>
			{{/each}}
		</script>
		<script type="text/javascript">
			$('input:radio[name="type"]').change(function() {
				var newValue = $("input[name='type']:checked").val();
				var nowDate=new Date();
				if(newValue == '年') {
					
					$("#time1").show();
					$("#time1").val(nowDate.getFullYear());
					$("#time2").hide().val(null);
					$("#time3").hide().val(null);
				} else if(newValue == '月') {
					$("#time1").hide().val(null);
					$("#time2").show();
					$("#time2").val((nowDate.getFullYear()+'-'+(nowDate.getMonth()+1)).replace(/-(\d{1}\b)/,'-0$1'));
					$("#time3").hide().val(null);
				} else if(newValue == '日') {
					$("#time1").hide().val(null);
					$("#time2").hide().val(null);
					$("#time3").show();
					$("#time3").val((getNowFormatDate()).replace(/-(\d{1}\b)/,'-0$1'))
				}
			});

			function getNowFormatDate() {
				var date = new Date();
				var seperator1 = "-";
				var month = date.getMonth() + 1;
				var strDate = date.getDate();
				if(month >= 1 && month <= 9) {
					month = "0" + month;
				}
				var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
				return currentdate.replace(/-(\d{1}\b)/,'-0$1');
			}

			function clears() {
				$("#time1").val('');
				$("#day").prop("checked",true)
				$("#time2").val('');
				$("#time3").val(getNowFormatDate());
				$("#time1").hide().val(null);
				$("#time2").hide().val(null);
				$("#time3").show();
				$('#ksnew').val('');
				$('#drugCostType').combobox('clear');
			}

			function pickedFunc() {
				var type = $("input[name='type']:checked").val();
				if(type == "年") {
					var date = $("#time1").val();
				} else if(type == "月") {
					var date = $("#time2").val();
				} else if(type == "日") {
					var date = $("#time3").val();
				}
				return date
			}

			function searchFrom() {
				var ajaxData = {};
				var yuanQu = $('#drugCostType').combobox("getValues").join(",")
				var keShi = $('#ksnew').getMenuIds();
				var time = pickedFunc()
				if(!yuanQu && !keShi) {
					$.messager.alert("提示","请选择科室或者院区")
					return false
				}
				if(yuanQu!=""&&keShi==""){
					keShi=="";
				}else{
					yuanQu="";
				}
				if(!time) {
					$.messager.alert("提示","请选择查询时间")
					return false
				}
// 				 var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;     
// 			     var r = time.match(reg); 
// 			     if(r==null){
// 			    	 $.messager.alert("提示","请选择正确的时间格式")
// 						return false
// 			     }
				$("body").setLoading("body")
				$.ajax({
					type: "get",
					url: "<%=basePath%>statistics/deptstat/inpatientStatisticsAction/queryInpatientStatisticsList.action?menuAlias="+menuAlias,
					data: {
						time: time,
						ytype: yuanQu,
						ktype: keShi,
						menuAlias:menuAlias
					},
					async: true,
					success: function(data) {
						
						if(data.success == 1) {
							$("#cardAll").html(null).append(
								template("cardInfoScript", {
									data: data.data
								}))
							var tabData = data.data
							for(var i = 0; i < tabData.length; i++) {
								
								cardChunk(i, tabData[i])
							}
						}
						$("body").rmoveLoading("body")
					}
				});
			}

			function cardChunk(index, data) {
				
				if($("#tonData" + index).length > 0) {
					tonTable($("#tonData" + index), data.tonData)
				}
				huanTable($("#huanData" + index), data.huanData)
			}

function tonTable($dom, data) {
				var myChart = echarts.init($dom[0]);
				option = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross',
							crossStyle: {
								color: '#999'
							}
						}
					},
					legend: {
						data: ['住院人数','同比']
					},
					xAxis: [{
						type: 'category',
						data: data.xAxis,
						axisLabel:{
							rotate:-30,
							interval:0,
						},
						axisPointer: {
							type: 'shadow'
						}
					}],
					yAxis: [{
						type: 'value',
						name: '住院人数(人)',
						axisLabel: {
							formatter: '{value}'
						}
					},{
						type: 'value',
						name: '同比',
						min: -100,
						show:true,
						axisLabel: {
							formatter:  '{value} %'
						}
					}],
					series: [{
						name: '住院人数',
						type: 'bar',
						barWidth: 30,
						data: data.data1
					},{
						name: '同比',
						type: 'line',
						yAxisIndex: 1,
						data: data.data
					}]
				};
				myChart.setOption(option);
			}

			function huanTable($dom, data) {

				var myChart = echarts.init($dom[0]);
				option = {
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross',
							crossStyle: {
								color: '#999'
							}
						}
					},
					legend: {
						data: ['住院人数','环比']
					},
					xAxis: [{
						type: 'category',
						data: data.xAxis,
						axisLabel:{
							rotate:-30,
							interval:0
						},
						axisPointer: {
							type: 'shadow'
						}
					}],
					yAxis: [{
						type: 'value',
						name: '住院人数(人)',
						axisLabel: {
							formatter: '{value}'
						}
					},{
						type: 'value',
						name: '环比',
						min: -100,
						show:true,
						axisLabel: {
							formatter: '{value} %'
						}
					}],
					series: [{
						name: '住院人数',
						type: 'bar',
						barWidth: 30,
						data: data.data1
					},{
						name: '环比',
						type: 'line',
						barWidth: 30,
						yAxisIndex: 1,
						data: data.data
					}]
				};
				myChart.setOption(option);
			}
		</script>
	</body>

</html>