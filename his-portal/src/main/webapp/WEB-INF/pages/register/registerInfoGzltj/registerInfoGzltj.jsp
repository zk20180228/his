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
	<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
	</style>
<script type="text/javascript">

	var deptMap = null;
	var empMap = null;
     var menuAlias = '${menuAlias}';
	$(function(){
// 		$("#roleMenuList").hide();
// 		$.messager.progress({msg:"查询中,请稍后..."});
				//科室
				$.ajax({
					url: "<%=basePath%>baseinfo/department/getDeptMap.action",
					success: function(date) {
// 						$("body").rmoveLoading("body");
						deptMap = date;
					}
				});
				//员工
				$.ajax({
					url: "<%=basePath%>baseinfo/employee/getEmplMap.action",
					success: function(date) {
						empMap = date;
						var Stime = $('#Stime').val();
						var Etime = $('#Etime').val();
						var deptCodes = $('#ksnew').getMenuIds();
						var expxrts = $('#doctornew').getMenuIds();
						$('#roleMenuList').datagrid({
							url:'<%=basePath%>/statistics/RegisterInfoGzltj/queryRegisterInfoGzltj.action',
							queryParams:{
								sTime: Stime,
								eTime: Etime,
								dept: deptCodes,
								expxrt: expxrts,
								menuAlias: menuAlias
							},
							method:'post',
							toolbar:'#toolbarIdRoleMenu',
							fit:true,
							singleSelect:true,
							remoteSort:false,
							pagination:true,
				        	pageSize:50,
				        	pageList:[50,100,150,200],
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
							      
									$('#roleMenuList').datagrid("autoMergeCells", ['dept']);
							}
						});
// 						$("#roleMenuList").show();
// 						$.messager.progress('close');
					}
				});
				
				
				
				
						$('#roleMenuList').datagrid({
							toolbar:'#toolbarIdRoleMenu',
							fit:true,
							singleSelect:true,
							remoteSort:false,
							pagination:true,
				        	pageSize:50,
				        	pageList:[50,100,150,200],
						});
				
				
				
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
						
						//选择科室
					 $(".deptInput").MenuList({
							width :530, //设置宽度，不写默认为530，不要加单位
							height :400, //设置高度，不写默认为400，不要加单位
							dropmenu:"#m2",//弹出层id，必须要写
							isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
							para:"C",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
							firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
							relativeInput:".doctorInput",	//与其级联的文本框，必须要写
							relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
						});
						//选择医生
						$(".doctorInput").MenuList({
							width :530,
							height :400,
							dropmenu:"#m3",//弹出层
							isSecond:true,	//是否是二级联动的第二级
							para:"C",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
							firsturl: "<%=basePath %>statistics/RegisterInfoGzltj/getDoctorList.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
							relativeInput:".deptInput"	//与其级联的文本框
						});
	});
	
	/**
	 * 查询
	 * @author zpty
	 * @date 2015-08-31
	 * @version 1.0
	 */
	 function searchFrom(){
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
		var dd=new Date(Etime); 
	     dd.setDate(dd.getDate()+1);//获取AddDayCount天后的日期
	     var y = dd.getFullYear();
	     var m = dd.getMonth()+1;//获取当前月份的日期
	     if(Number(m)<10){
	           m = "0"+m;
	         }
	         var d = dd.getDate();
	         if(Number(d)<10){
	           d = "0"+d;
	         }
	     Etime =  y+"-"+m+"-"+d;
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
		var expxrts = $('#doctornew').getMenuIds();
		$('#roleMenuList').datagrid('load',{
			sTime: Stime,
			eTime: Etime,
			dept: deptCodes,
			expxrt: expxrts,
			menuAlias: menuAlias
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
		var Etime = GetDateStr(1);
		searchFinal(Stime,Etime);
		$('#Etime').val(GetDateStr(0));
	 }
	 //查询前三天
	function searchThree(){
		var Stime = GetDateStr(-3);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$('#Etime').val(GetDateStr(-1));
	}
	//查询前七天
	function searchSeven(){
		var Stime = GetDateStr(-7);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$('#Etime').val(GetDateStr(-1));
	}
	//查询前15天
	function searchFifteen(){
		var Stime = GetDateStr(-15);
		var Etime = GetDateStr(0);
		searchFinal(Stime,Etime);
		$('#Etime').val(GetDateStr(-1));
	}
	
	//上月
	function beforeMonth(){
		  var date = new Date();
		  var year = date.getFullYear();
		  var month = date.getMonth();
		  if(month==0)
		  {
		  month=12;
		  year=year-1;
		  }
		  if (month < 10) {
		  month = "0" + month;
		  }
		  var Stime = year + "-" + month + "-" + "01";//上个月的第一天
		  var lastDate = new Date(year, month, 0);
		  var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
		  var Etime= year+"-"+(date.getMonth()+1)+"-"+01;
			  
		  searchFinal(Stime,Etime);
		  $('#Stime').val(Stime);
		  $('#Etime').val(lastDay);
	}
	
	
	//日期格式转换
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
		//var Etime = getCurrentMonthLast();之前的代码
		//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
		//2017-04-17新的
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Etime = GetDateStr(1);	
		searchFinal(Stime,Etime);
		$('#Etime').val(GetDateStr(0));
	}
	//查询当年,包含当天，显示当天，因后台是用小于号查询，所以往后台传值需加一天
	function searchYear(){
		//var Etime = new Date().getFullYear()+"-12-31";
		//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
// 		var date=new Date();
// 		var Etime = date.Format("yyyy-MM-dd");
		var Stime = new Date().getFullYear()+"-01-01";
		var Etime = GetDateStr(1);
		searchFinal(Stime,Etime);
		$('#Etime').val(GetDateStr(0));
	}
	
	
	
	function forDept(value,row,index){
		if(deptMap == null){
			return ""
		}
		return deptMap[value];
	}
	function forEmp(value,row,index){
		if(empMap == null){
			return ""
		}
		return empMap[value];
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#Stime').val('${sTime}');
		$('#Etime').val('${eTime}');
		$('#ksnew').val('');
		$('#doctornew').val('');
		$("a[name='menu-confirm-clear']").click();

		searchFrom();
	}

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

	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
</head>
<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%;">
			<div data-options="region:'north',border:false" style="height:38px;padding:5px 5px 0px 5px;">
						<table id="searchTab" style="width: 100%;">
							<tr>
								<!-- 开始时间 --> 
								<td style="width:40px;" align="left">日期:</td>
								<td style="width:110px;">
									<input id="Stime" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<!-- 结束时间 --> 
								<td style="width:40px;" align="center">至</td>
								<td style="width:110px" id="td1">
									<input id="Etime" class="Wdate" type="text" name="Etime" value="${eTime}" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td style="width:55px;" align="center">科室:</td>
				    			<td style="width:120px;" class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
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
										<div class="select-info" style="display:none">	
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
								<td>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="height:23px;margin-top:-3px">查询</a>
									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset" style="height:23px;margin-top:-3px">重置</a>
								</shiro:hasPermission>
								</td>
								<td style='text-align: right'>
									<shiro:hasPermission name="${menuAlias}:function:query">
								<!-- 		<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
										<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
										<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
										<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a> -->
										<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
										<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
										<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
				<div data-options="region:'center',border:false" >
					<table  style="width: 100%;" id="roleMenuList">
						<thead>
							<tr>
								<th data-options="field:'id',rowspan:2,hidden:true">主键</th>
								<th data-options="field:'dept',rowspan:2,sortable:true,formatter:forDept,align:'left'" width="10%">科室</th>
								<th data-options="field:'expxrt',rowspan:2,sortable:true,formatter:forEmp,align:'left'" width="10%">医生</th>
<!-- 								<th data-options="field:'title',rowspan:2,sortable:true,align:'left'" width="8%">医生职称</th> -->
								<th data-options="field:'mon',colspan:2,align:'center'">周一</th>
								<th data-options="field:'tue',colspan:2,align:'center'">周二</th>
								<th data-options="field:'wed',colspan:2,align:'center'">周三</th>
								<th data-options="field:'thu',colspan:2,align:'center'">周四</th>
								<th data-options="field:'fri',colspan:2,align:'center'">周五</th>
								<th data-options="field:'sat',colspan:2,align:'center'">周六</th>
								<th data-options="field:'sun',colspan:2,align:'center'">周日</th>
								<th data-options="field:'all',colspan:2,align:'center'">合计</th>
							</tr>
							<tr>
								<th data-options="field:'monNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'monCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'tueNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'tueCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'wedNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'wedCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'thuNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'thuCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'friNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'friCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'satNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'satCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'sunNum',sortable:true,halign:'center',align:'right'" width="4%">数量</th>
								<th data-options="formatter:changeCost,field:'sunCost',sortable:true,halign:'center',align:'right'" width="5%">金额</th>
								<th data-options="field:'num',sortable:true,halign:'center',align:'right'" width="5%">数量</th>
								<th data-options="formatter:changeCost,field:'cost',sortable:true,halign:'center',align:'right'" width="7%">金额</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	</body>
</html>
