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
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
	var ec = null;
	var menuAlias = '${menuAlias}';
	var flag;
	$(function() {
				//科室
// 				$.ajax({
<%-- 					url: "<%=basePath%>baseinfo/department/getDeptMap.action", --%>
// 					success: function(date) {
// 						deptMap = date;
// 					}
// 				});
				//选择科室
				$(".deptInput").MenuList({
					width :530, //设置宽度，不写默认为530，不要加单位
					height :topDivH, //设置高度，不写默认为400，不要加单位
					dropmenu:"#m2",//弹出层id，必须要写
					isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
					para:"I,OP",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
					firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
					relativeInput:".doctorInput",	//与其级联的文本框，必须要写
					relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
				});
				
				$('#m2 .addList h2 input').click();
				$('a[name=\'menu-confirm\']').click();
				 flag= $('#ksnew').getMenuIds();
				 $('a[name=\'menu-confirm-clear\']').click();
					if(flag==''){
						$("body").setLoading({
							id:"body",
							isImg:false,
							text:"无数据权限"
						});
					}
				//员工
				$.ajax({
					url: "<%=basePath%>baseinfo/employee/getEmplMap.action",
					success: function(date) {
						empMap = date;
						var Stime = $('#Stime').val();
						var Etime = $('#Etime').val();
						var deptCodes = $('#ksnew').getMenuIds();
						var expxrts = $('#doctornew').getMenuIds();
						$('#list').datagrid({
							data:[],//页面刚进来不加载数据,修改为当前0条记录，总0条记录
							pageNumber:1,
							method:'post',
							toolbar:'#toolbarIdRoleMenu',
							fit:true,
							singleSelect:true,
							remoteSort:false,
							pagination:true,
				        	pageSize:100,
				        	pageList:[100,150,200,300],
							onLoadSuccess:function(data){
								//分页工具栏作用提示
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
							      
									$('#list').datagrid("autoMergeCells", ['deptName']);
							}
						});
					}
				});
					
					//科室下拉框
					 $.extend($.fn.datagrid.methods, {
					        autoMergeCells: function (jq, fields) {
					            return jq.each(function () {
					                var target = $(this);
					                if (!fields) {
					                    fields = target.datagrid("getColumnFields");
					                }
					                var rows = target.datagrid("getRows");
					                var i = 0,
					                j = 0,
					                temp = {};
					                for (i; i < rows.length; i++) {
					                    var row = rows[i];
					                    j = 0;
					                    for (j; j < fields.length; j++) {
					                        var field = fields[j];
					                        var tf = temp[field];
					                        if (!tf) {
					                            tf = temp[field] = {};
					                            tf[row[field]] = [i];
					                        } else {
					                            var tfv = tf[row[field]];
					                            if (tfv) {
					                                tfv.push(i);
					                            } else {
					                                tfv = tf[row[field]] = [i];
					                            }
					                        }
					                    }
					                }
					                $.each(temp, function (field, colunm) {
					                    $.each(colunm, function () {
					                        var group = this;
					                        if (group.length > 1) {
					                            var before,
					                            after,
					                            megerIndex = group[0];
					                            for (var i = 0; i < group.length; i++) {
					                                before = group[i];
					                                after = group[i + 1];
					                                if (after && (after - before) == 1) {
					                                    continue;
					                                }
					                                var rowspan = before - megerIndex + 1;
					                                if (rowspan > 1) {
					                                    target.datagrid('mergeCells', {
					                                        index: megerIndex,
					                                        field: field,
					                                        rowspan: rowspan
					                                    });
					                                }
					                                if (after && (after - before) != 1) {
					                                    megerIndex = after;
					                                }
					                            }
					                        }
					                    });
					                });
					            });
					        }
					    });
						var topDivH = ($("body").height()/2)-40;
						$("#tableShow")[0].style.height = ($("#topShow").height() - 40)+"px";
						//选择医生
						$(".doctorInput").MenuList({
							width :530,
							height :topDivH,
							menulines:3,
							dropmenu:"#m3",//弹出层
							isSecond:true,	//是否是二级联动的第二级
							para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
							firsturl: "<%=basePath %>statistics/RegisterInfoGzltj/getDoctorList.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
							relativeInput:".deptInput",	//与其级联的文本框
							relativeDropmenu:"#m2"
						});
// 					searchMonth();
		});
		 
		//渲染处方金额
		function changeCost(value,row,index){
			if(value!=null){
				value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(2) + ""; 
				var l = value.split(".")[0].split("").reverse(), r = value.split(".")[1]; 
				t = ""; 
				for (i = 0; i < l.length; i++) { 
				t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
				} 
				return t.split("").reverse().join("") + "." + r; 
			}else {
				return "0.00";
			}
		}
		 /**
			 * 查询
			 * @author wujiao
			 * @date 2015-08-31
			 * @version 1.0
			 */
			function searchFrom() {
				var Stime = $('#Stime').val();
				var Etime = $('#Etime').val();
				if(Stime==null || Stime=="" || Etime==null || Etime==""){
					$.messager.alert("提示","请填写正确的时间范围！");
					return ;
		  		}
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
			     searchFrom1(Stime,Etime);
			}
			function searchFrom1(Stime,Etime) {
				if(Stime&&Etime){
					if(Stime>Etime){
						$.messager.alert("提示","开始时间不能大于结束时间！");
						return ;
					}
				}
				var deptCodes = $('#ksnew').getMenuIds();
				if(deptCodes==null||deptCodes==''){
					deptCodes=flag;
				}
				var expxrts = $('#doctornew').getMenuIds();
				$('#list').datagrid({
					url:'<%=basePath%>statistics/inpatientDoctor/inpatientList.action',
					queryParams:{
						sTime: Stime,
						eTime: Etime,
						menuAlias: menuAlias,
						dept: deptCodes,
						expxrt: expxrts
					}
				});
			}
			function searchFinal(Stime,Etime){
				$('#Stime').val(Stime);
				$('#Etime').val(Etime);
				searchFrom1(Stime,Etime);
			 }
			//距离当前多少天的日期
			 function GetDateStr(AddDayCount) {
				 var dd = new Date();
				 dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
				 var y = dd.getFullYear();
				 var m = dd.getMonth()+1;//获取当前月份的日期
				 if(Number(m)<10){
			         m = "0"+m;
			       }
			       var d = dd.getDate();
			       if(Number(d)<10){
			         d = "0"+d;
			       }
				 return y+"-"+m+"-"+d;
			}
			 //查询当天
			function searchOne(){
				var Stime = GetDateStr(0);
				var Etime = GetDateStr(0);
				searchFinal(Stime,Etime);
			 }
			 //查询前三天
			function searchThree(){
				var Stime = GetDateStr(-3);
				var Etime = GetDateStr(-1);
				searchFinal(Stime,Etime);
			}
			//查询前七天
			function searchSeven(){
				var Stime = GetDateStr(-7);
				var Etime = GetDateStr(-1);
				searchFinal(Stime,Etime);
			}
			//查询前15天
			function searchFifteen(){
				var Stime = GetDateStr(-15);
				var Etime = GetDateStr(-1);
				searchFinal(Stime,Etime);
			}
// 			//上月
// 			function beforeMonth(){
// 				  var date = new Date();
// 				  var year = date.getFullYear();
// 				  var month = date.getMonth();
// 				  if(month==0)
// 				  {
// 				  month=12;
// 				  year=year-1;
// 				  }
// 				  if (month < 10) {
// 				  month = "0" + month;
// 				  }
// 				  var Stime = year + "-" + month + "-" + "01";//上个月的第一天
// 				  var lastDate = new Date(year, month, 0);
// 				  var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
// 				  var Etime= lastDay;
// 				  searchFinal(Stime,Etime);
// 			}

			//当季
			function beforeMonth(){
				  var date = new Date();
				  var year = date.getFullYear();
				  var month = date.getMonth();
				  var startMonth = getQuarterStartMonth(month)+1;
				  if(month==0)
				  {
				  month=12;
				  year=year-1;
				  }
				  var Stime = year + "-" + (startMonth<10?"0"+startMonth:startMonth) + "-" + "01";//上个月的第一天
				  var eMM = date.getMonth()+1;
				  var eDD = date.getDate();
				  var Etime= year+"-"+(eMM<10?"0"+eMM:eMM)+"-"+(eDD<10?"0"+eDD:eDD);
				  searchFinal(Stime,Etime);
			}
			//获得本季度的开始月份
			 function getQuarterStartMonth(nowMonth) {
			     if (nowMonth <= 2) { return 0; }
			     else if (nowMonth <= 5) { return 3; }
			     else if (nowMonth <= 8) { return 6; }
			     else { return 9; }
			 }
			Date.prototype.Format = function(fmt)   
			{   
			  var o = {   
			    "M+" : this.getMonth()+1,                 //月份   
			    "d+" : this.getDate(),                    //日   
			    "h+" : this.getHours(),                   //小时   
			    "m+" : this.getMinutes(),                 //分   
			    "s+" : this.getSeconds(),                 //秒   
			    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
			    "S"  : this.getMilliseconds()             //毫秒   
			  };   
			  if(/(y+)/.test(fmt))   
			    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
			  for(var k in o)   
			    if(new RegExp("("+ k +")").test(fmt))   
			  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
			  return fmt;   
			}  
			
			//获取每月第一天
			function getCurrentMonthFirst(){
				 var date=new Date();
				 date.setDate(1);
				 return date.Format("yyyy-MM-dd");
			}
			//获取每月最后一天
			function getCurrentMonthLast(){
				 var date=new Date();
				 var currentMonth=date.getMonth();
				 var nextMonth=++currentMonth;
				 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
				 var oneDay=1000*60*60*24;
				 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
			} 
			//查询当月,包含当天，显示当天，因后台是用小于号查询，所以往后台传值需加一天
			function searchMonth(){
				var Stime = getCurrentMonthFirst();
				var Etime = GetDateStr(0);	
				searchFinal(Stime,Etime);
			}
			//查询当年,包含当天，显示当天，因后台是用小于号查询，所以往后台传值需加一天
			function searchYear(){
				var Stime = new Date().getFullYear()+"-01-01";
				var Etime = GetDateStr(0);
				searchFinal(Stime,Etime);
			}
			/**
			 * 重置
			 * @author huzhenguo
			 * @date 2017-03-17
			 * @version 1.0
			 */
			function clears(){
				$('#ksnew').val('');
				$("a[name='menu-confirm-clear']").click();
				searchMonth();
			}	 

     </script>
</head>
<body style="overflow: hidden;">
		<div id="topShow" class="easyui-layout" style="height:100%">
					<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr >
							<!-- 开始时间 --> 
								<td style="width:40px;" align="left">日期:</td>
								<td style="width:110px;">
								<input id="Stime" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<!-- 结束时间 --> 
								<td style="width:40px;" align="center">至</td>
								<td style="width:110px;">
								<input id="Etime" class="Wdate" type="text" name="Etime" value="${eTime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						       </td>
								<td style="width:55px;" align="center">科室:</td>
								<td style="width:120px; " class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none; ">
					    	       		<div class="searchDept" >
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
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
										<div class="select-info" style="display:none;">	
											<label class="top-label">已选部门：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>		 
										</div>	
									</div>
								</td>
								<td style="width:55px;" align="center">医生:</td>
				    			<td style="width:120px;" class="newMenu">
					    	       	<!-- <input id="zj" name="expxrt" class="easyui-combobox" data-options="valueField:'jobNo',textField:'name',multiple:true"/>  -->
					    			<div class="doctorInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="doctornew" readonly="readonly"/><span></span></div> 
					    	       	<div id="m3" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" >
												<span class="a-btn-text">取消</span>
											</a>
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">确定</span>
											</a>											
					    	       		</div>
										<div class="select-info" style="display:none">	
											<label class="top-label">已选医生：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>	
										</div>
													 
									</div>
					    		</td>
								<td>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
							<td style='text-align: right'>
<!-- 								<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a> -->
<!-- 								<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a> -->
<!-- 								<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a> -->
<!-- 								<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a> -->
<!-- 								<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a> -->
<!-- 								<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a> -->
<!-- 								<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a> -->
									<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
									<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当季</a>
									<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
							</td>
						</tr>
					</table>
<!-- 			</div> -->
			<div id="tableShow" style="width:100%;margin-top: 7px;height:90%" >
					<table class="easyui-datagrid"  id="list" style="" data-options="fit:true">
					<thead data-options="frozen:true">
						<tr>
							<th data-options="field:'deptName',align:'center',width:'8%'">科室</th>
							<th data-options="field:'doctorName',align:'center',width:'5%'">医生</th>
					    </tr>
					</thead>
						<thead>
							<tr>
								<th data-options="field:'cfs',align:'center',rowspan:2">处方量</th>
								<th data-options="field:'ryrs',align:'center',rowspan:2">入院人次</th>
								<th data-options="field:'cyrs',align:'center',rowspan:2">出院人次</th>
								<th data-options="field:'operationNum',align:'center',rowspan:2">手术量</th>
								<th data-options="field:'totle1',align:'center',colspan:16">费别费用</th>
							</tr>
							<tr>
								<th data-options="field:'westernCost',halign:'center',align:'center',width:'5%',formatter:changeCost">西药费</th>
								<th data-options="field:'chineseCost',halign:'center',align:'center',width:'5%',formatter:changeCost">中成药费</th>
								<th data-options="field:'herbalCost',halign:'center',align:'center',width:'5%',formatter:changeCost">中草药费</th>
								<th data-options="field:'chuangweiCost',halign:'center',align:'center',width:'5%',formatter:changeCost">床位费</th>
								<th data-options="field:'treatmentCost',halign:'center',align:'center',width:'5%',formatter:changeCost">治疗费</th>
								<th data-options="field:'inspectCost',halign:'center',align:'center',width:'5%',formatter:changeCost">检查费</th>
								<th data-options="field:'radiationCost',halign:'center',align:'center',width:'5%',formatter:changeCost">放射费</th>
								<th data-options="field:'testCost',halign:'center',align:'center',width:'5%',formatter:changeCost">化验费</th>
								<th data-options="field:'shoushuCost',halign:'center',align:'center',width:'5%',formatter:changeCost">手术费</th>
								<th data-options="field:'bloodCost',halign:'center',align:'center',width:'5%',formatter:changeCost">输血费</th>
								<th data-options="field:'o2Cost',halign:'center',align:'center',width:'5%',formatter:changeCost">输氧费</th>
								<th data-options="field:'cailiaoCost',halign:'center',align:'center',width:'5%',formatter:changeCost">材料费</th>
								<th data-options="field:'otherCost',halign:'center',align:'center',width:'5%',formatter:changeCost">其他费</th>
								<th data-options="field:'huliCost',halign:'center',align:'center',width:'5%',formatter:changeCost">护理费</th>
								<th data-options="field:'zhenchaCost',halign:'center',align:'center',width:'5%',formatter:changeCost">诊察费</th>
								<th data-options="field:'totle',halign:'center',align:'center',width:'5%',formatter:changeCost">合计</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
</body>
</html>