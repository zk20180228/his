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
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
		<style type="text/css">
			.panel-body,
			.panel {
				overflow: visible;
			}
		</style>
		<script type="text/javascript">
			var empFlag = true;
			var menuAlias = '${menuAlias}';
			$(function() {
				//科室
				$.ajax({
					url: "<%=basePath%>baseinfo/department/getDeptMap.action",
					success: function(date) {
						deptMap = date;
						//科室下拉框
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
						//选择科室
						$(".deptInput").MenuList({
							width: 530, //设置宽度，不写默认为530，不要加单位
							height: 400, //设置高度，不写默认为400，不要加单位
							dropmenu: "#m2", //弹出层id，必须要写
							isSecond: false, //是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
							para: "C", //要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
							firsturl: "<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias=" + menuAlias + "&deptTypes=", //获取列表的url，必须要写
							//relativeInput:".doctorInput"	//与其级联的文本框，必须要写
							//relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
						});
						var winH = $("body").height();
						//$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
						/* $('#countList').height(winH-78-30-27-26); */
						var Stime = GetDateStr(0);
						var Etime = GetDateStr(1);
						var dept = "";
						$('#countList').datagrid({
							//url:'${pageContext.request.contextPath}/statistics/ReservationStatistics/listCountqueryReservaion.action',
// 							queryParams: {
// 								Stime: Stime,
// 								Etime: Etime,
// 								dept: dept,
// 								menuAlias: menuAlias
// 							},
							data:[],
							pagination: true,
							pageSize: 50,
							pageList: [50, 100, 150, 200],
							singleSelect: true,
							onLoadSuccess: function(data) {
								//分页工具栏作用提示
								var pager = $(this).datagrid('getPager');
								var aArr = $(pager).find('a');
								var iArr = $(pager).find('input');
								$(iArr[0]).tooltip({
									content: '回车跳转',
									showEvent: 'focus',
									hideEvent: 'blur',
									hideDelay: 1
								});
								for(var i = 0; i < aArr.length; i++) {
									$(aArr[i]).tooltip({
										content: toolArr[i],
										hideDelay: 1
									});
									$(aArr[i]).tooltip('hide');
								}
							}
						});
						$('#STime').val(GetDateStr(0));
						$('#ETime').val(GetDateStr(0));
					}
				});

// 				$('#countList').datagrid({
// 					pagination: true,
// 					pageSize: 50,
// 					pageList: [50, 100, 150, 200],
// 					singleSelect: true,
// 				});

				/*******************************************************************************************************************/
			});
			/**
			 * 下拉框联动
			 * @author  liudelin
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-06-15
			 * @version 1.0
			 */
			//科室下拉框
			// 	$('#dept').combobox({    
			<%-- 			url :"<%=basePath %>
			statistics / ReservationStatistics / departmentComboboxRegister.action ", --%>
			// 			valueField : 'id',
			// 			textField : 'deptName',
			// 			multiple : false,
			// 			filter:function(q,row){
			// 				var keys = new Array();
			// 				keys[keys.length] = 'deptCode';
			// 				keys[keys.length] = 'deptName';
			// 				keys[keys.length] = 'deptPinyin';
			// 				keys[keys.length] = 'deptWb';
			// 				keys[keys.length] = 'deptInputcode';
			// 				return filterLocalCombobox(q, row, keys);
			// 			}
			// 		});
			//过滤方法	
			function filterLocalCombobox(q, row, keys) {
				if(keys != null && keys.length > 0) { //
					for(var i = 0; i < keys.length; i++) {
						if(row[keys[i]] != null && row[keys[i]] != '') {
							var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
							if(istrue == true) {
								return true;
							}
						}
					}
				} else {
					var opts = $(this).combobox('options');
					return row[opts.textField].indexOf(q.toUpperCase()) > -1;
				}
			}
			/**
			 * 查询
			 * @author wujiao
			 * @date 2015-08-31
			 * @version 1.0
			 */
			function searchFrom() {
				var Stime = $('#STime').val();
				var Etime = $('#ETime').val();
				if(Stime == null || Stime == "" || Etime == null || Etime == "") {
					$.messager.alert("提示", "请填写正确的时间范围！");
					return;
				}
				if(Stime && Etime) {
					if(Stime > Etime) {
						$.messager.alert("提示", "开始时间不能大于结束时间！");
						close_alert();
						return;
					}
				}
				var dd = new Date(Etime);
				dd.setDate(dd.getDate() + 1); //获取AddDayCount天后的日期
				var y = dd.getFullYear();
				var m = dd.getMonth() + 1; //获取当前月份的日期
				if(Number(m) < 10) {
					m = "0" + m;
				}
				var d = dd.getDate();
				if(Number(d) < 10) {
					d = "0" + d;
				}
				Etime = y + "-" + m + "-" + d;
				searchFinal(Stime, Etime);
			}

			function searchFinal(Stime, Etime) {
				$('#STime').val(Stime);
				var dept = "";
				if($.trim($('#dept').val()) != "") {
					dept = $('#dept').getMenuIds();
				}
				if(Stime == null || Stime == "") {
					Stime = GetDateStr(0);
					$('#STime').val(Stime);
				}
				if(Etime == null || Etime == "") {
					Etime = GetDateStr(1);
					$('#ETime').val(Etime);
				}
				
				 //当全部按钮没有被选中并且没有选中科室
				if(dept==null||dept==""){
					 dept = "all"; 
				 }
// 				else if("undefined" != typeof($('h2','.xmenu').attr('class'))){
// 					if($('h2','.xmenu').attr('class').indexOf('selected')!=-1){
// 					 dept = "all"; 
// 					}
// 				}
				$('#countList').datagrid({
					url:'${pageContext.request.contextPath}/statistics/ReservationStatistics/listCountqueryReservaion.action',
					queryParams:{
						Stime: Stime,
						Etime: Etime,
						dept: dept,
						menuAlias: menuAlias
					}
				});
			}

			function GetDateStr(AddDayCount) {
				var dd = new Date();
				dd.setDate(dd.getDate() + AddDayCount); //获取AddDayCount天后的日期
				var y = dd.getFullYear();
				var m = dd.getMonth() + 1; //获取当前月份的日期
				if(Number(m) < 10) {
					m = "0" + m;
				}
				var d = dd.getDate();
				if(Number(d) < 10) {
					d = "0" + d;
				}
				return y + "-" + m + "-" + d;
			}
			//查询当天
			function searchOne() {
				var Stime = GetDateStr(0);
				var Etime = GetDateStr(1);
				searchFinal(Stime, Etime);
				$('#ETime').val(GetDateStr(0));
			}
			//查询前三天
			function searchThree() {
				var Etime = GetDateStr(0);
				var Stime = GetDateStr(-3);
				searchFinal(Stime, Etime);
				$('#ETime').val(GetDateStr(-1));
			}
			//查询前七天
			function searchSeven() {
				var Etime = GetDateStr(0);
				var Stime = GetDateStr(-7);
				searchFinal(Stime, Etime);
				$('#ETime').val(GetDateStr(-1));
			}
			//查询前15天 zhangkui 2017-04-17
			function searchFifteen() {
				var Stime = GetDateStr(-15);
				var Etime = GetDateStr(0);
				searchFinal(Stime, Etime);
				$('#ETime').val(GetDateStr(-1));
			}
			
			//上月
			function beforeMonth(){
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth();
                var nowMonth = month;
                var nowYear = year;
                if(month==0)
                {
                    month=12;
                    nowMonth = "01";
                    year=year-1;
                }
                if (month < 10) {
                    nowMonth = "0" +(month+1);
                    month = "0" + month;
                }
                var Stime = year + "-" + month + "-" + "01";//上个月的第一天
                var lastDate = new Date(year, month, 0);
                var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
                var Etime= nowYear+"-"+nowMonth+"-01";

                searchFinal(Stime,Etime);
                $('#ETime').val(lastDay);
			}
			
			
			//日期格式转换
			Date.prototype.Format = function(fmt) {
				var o = {
					"M+": this.getMonth() + 1, //月份   
					"d+": this.getDate(), //日   
					"h+": this.getHours(), //小时   
					"m+": this.getMinutes(), //分   
					"s+": this.getSeconds(), //秒   
					"q+": Math.floor((this.getMonth() + 3) / 3), //季度   
					"S": this.getMilliseconds() //毫秒   
				};
				if(/(y+)/.test(fmt))
					fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
				for(var k in o)
					if(new RegExp("(" + k + ")").test(fmt))
						fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				return fmt;
			}
			//获取每月第一天
			function getCurrentMonthFirst() {
				var date = new Date();
				date.setDate(1);
				return date.Format("yyyy-MM-dd");
			}
			//获取每月最后一天
			function getCurrentMonthLast() {
				var date = new Date();
				var currentMonth = date.getMonth();
				var nextMonth = ++currentMonth;
				var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
				var oneDay = 1000 * 60 * 60 * 24;
				return new Date(nextMonthFirstDay - oneDay).Format("yyyy-MM-dd");
			}
			//查询当月
			function searchMonth() {
				//var Etime = getCurrentMonthLast();
				//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
				//2017-04-17新的
				// 			var date=new Date();
				// 			var Etime = date.Format("yyyy-MM-dd");
				var Etime = GetDateStr(1);
				var Stime = getCurrentMonthFirst();
				searchFinal(Stime, Etime);
				$('#ETime').val(GetDateStr(0));
			}
			//查询当年
			function searchYear() {
				//var Etime = new Date().getFullYear()+"-12-31";
				//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
				//2017-04-17新的
				// 			var date=new Date();
				// 			var Etime = date.Format("yyyy-MM-dd");
				var Etime = GetDateStr(1);
				var Stime = new Date().getFullYear() + "-01-01";
				searchFinal(Stime, Etime);
				$('#ETime').val(GetDateStr(0));
			}

			/**
			 * 重置
			 * @author huzhenguo
			 * @date 2017-03-17
			 * @version 1.0
			 */
			function clears() {
				$('#ksnew').val('');
				$("a[name='menu-confirm-cancel']").click();
				$("a[name='menu-confirm-clear']").click();
				searchOne();
			}
		</script>
	</head>

	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="height:40px;padding:5px 5px 0px 5px;">
				<table style="width:100%;">
					<tr>
						<!-- 开始时间 -->
						<td style="width:40px;" align="left">日期:</td>
						<td style="width:110px;">
							<input id="STime" class="Wdate" type="text" name="STime" value="${Stime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;" />
						</td>
						<!-- 结束时间 -->
						<td style="width:40px;" align="center">至</td>
						<td style="width:110px;">
							<input id="ETime" class="Wdate" type="text" name="ETime" value="${Etime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;" />
						</td>
						<td style="width:55px;" align="center">科室:</td>
						<td width =130px class="newMenu">
							<div class="deptInput menuInput"  style="width:120px;"><input class="ksnew" id="dept" name="dept" value="${dept}" readonly="readonly"  style="width:95px" /><span></span></div>
							<div id="m2" class="xmenu" style="display: none;">
								<div class="searchDept">
									<input type="text" name="searchByDeptName" placeholder="回车查询" />
									<span class="searchMenu"><i></i>查询</span>
									<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">取消</span>
									</a>
									<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">清空</span>
									</a>
									<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">确定</span>
									</a>
								</div>
								<div class="select-info" style="display:none; ">
									<label class="top-label">已选科室：</label>
									<ul class="addDept">

									</ul>
								</div>
								<div class="depts-dl">
									<div class="addlist"></div>
									<div class="tip" style="display:none; ">没有检索到数据</div>
								</div>
							</div>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
						<td style='text-align: right'>
							<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当天</a>
							<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
							<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
							<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
							<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
							<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
							<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当年</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" style="height:90%;">
				<table id="countList" data-options="fit:true,method:'get'">
					<thead>
						<tr>
							<th data-options="field:'deptName',rowspan:3,halign:'center'" width="8%">合计数据</th>
							<th data-options="field:'',colspan:3,rowspan:2">全部号源数量</th>
							<th data-options="field:'',colspan:3,rowspan:2">就诊人次</th>
							<th data-options="field:'',colspan:9">预约挂号数量</th>
						</tr>
						<tr>
							<th data-options="field:'',colspan:2"> 挂号源</th>
							<th data-options="field:'',colspan:4"> 预约方式</th>
							<th data-options="field:'',colspan:2"> 就诊类别</th>
							<th data-options="field:'total',rowspan:2,halign:'center',align:'right'" width="8%">合计</th>
						</tr>
						<tr>
							<th data-options="field:'commonNumber',halign:'center',align:'right'" width="9%"> 普通号(含专科号)</th>
							<th data-options="field:'numberExpert',halign:'center',align:'right'" width="5%">专家号</th>
							<th data-options="field:'countAllInfo',halign:'center',align:'right'" width="5%">合计</th>
							<th data-options="field:'firstVisit',halign:'center',align:'right'" width="5%">初诊</th>
							<th data-options="field:'furtherConsultation',halign:'center',align:'right'" width="5%">复诊</th>
							<th data-options="field:'countDoctorVisits',halign:'center',align:'right'" width="5%">合计</th>
							<th data-options="field:'commonNumberRe',halign:'center',align:'right'" width="9%">普通号(含专科号)</th>
							<th data-options="field:'numberExpertRe',halign:'center',align:'right'" width="5%">专家号</th>
							<th data-options="field:'windowBooking',halign:'center',align:'right'" width="6%">窗口预约</th>
							<th data-options="field:'phoneBooking',halign:'center',align:'right'" width="6%">电话预约</th>
							<th data-options="field:'netBooking',halign:'center',align:'right'" width="6%">网络预约</th>
							<th data-options="field:'otherBooking',halign:'center',align:'right'" width="6%">其他方式</th>
							<th data-options="field:'firstVisitRe',halign:'center',align:'right'" width="6%">初诊预约</th>
							<th data-options="field:'furtherConsultationRe',halign:'center',align:'right'" width="6%">复诊预约</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>

</html>