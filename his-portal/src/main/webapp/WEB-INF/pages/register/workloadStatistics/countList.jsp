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
		.addList dl:first-child ul {
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix:after{
		    content:"";
		    display:table;
		    height:0;
		    visibility:hidden;
		    clear:both;
		}
		.xmenu dl dd ul{
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
</style>
<script type="text/javascript">
var empFlag=true;
     var menuAlias = '${menuAlias}';
     var deptCodes;
		$(function(){
			$("#tableShow").height($("body").height()-38);
					//科室
					$.ajax({
						url: "<%=basePath%>baseinfo/department/getDeptMap.action",
						async:false,
						success: function(date) {
								deptMap = date;
								  //科室
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
									menulines:2, //设置菜单每行显示几列（1-5），默认为2
									dropmenu:"#m2",//弹出层id，必须要写
									isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
// 									haveThreeLevel:true,//是否有三级菜单
									para:"C",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
									firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
<%-- 									secondurl:"<%=basePath%>baseinfo/department/getDeptByauthority.action?menuAlias"+menuAlias, --%>
									relativeInput:".doctorInput",	//与其级联的文本框，必须要写
									relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
								});
								
								//如果没有数据将科室编号全部查询出来传递到后台
								$('#m2 .addList h2 input').click();
								$('a[name=\'menu-confirm\']').click();
								deptCodes=$('#ksnew').getMenuIds();
								$('a[name=\'menu-confirm-clear\']').click();		
								
							var deptCode = $('#ksnew').getMenuIds();
							 if(($('h2','.xmenu').attr('class')=='selected')||deptCode==null||deptCode==""){
								 //当全部按钮没有被选中并且没有选中科室
								 deptCode = deptCodes; 
							 }
								 var dd=new Date('${ETime}'); 
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
							     etime =  y+"-"+m+"-"+d;
					        	 	
					           $('#countList').datagrid({
					        	   url:'<%=basePath%>statistics/workloadStatistics/listDeptWorkCountByMongo.action',//listCountquery.action',
					        	   data:[],
					        	   method:'post',
					        	   queryParams:{'dept':deptCode,'STime':'${STime}','ETime':etime,'menuAlias': menuAlias},
					        	   pagination:true,
					        	   singleSelect:true,
					        	   pageSize:50,
					        	   pageList:[50,100,150,200],
					        	   onLoadSuccess: function(data){
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
					        	   }
								});
						}
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
	     
// 			var Stime = GetDateStr(0);
// 			var Etime = GetDateStr(1);
//            searchOne();
		});
		//渲染处方金额
		function changeCost(value,row,index){
			return value==null?value:value.toFixed(2);
		}
     /**
		 * 查询
		 * @author zpty
		 * @date 2015-08-31
		 * @version 1.0
		 */
		function searchFrom() {
			var Stime = $('#STime').val();
			var Etime = $('#ETime').val();
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
			searchFinal(Stime,Etime);
		}
		function searchFinal(Stime,Etime){
			var deptCode = $('#ksnew').getMenuIds();
			if(($('#m2','.xmenu').attr('class')=='selected')||deptCode==null||deptCode==""){
				 //当全部按钮没有被选中并且没有选中科室
				 deptCode = deptCodes; 
			 }
// 			if(Stime==null || Stime==""){
// 				Stime="${STime}";
//           	}
//           	if(Etime==null || Etime==""){
//           		Etime="${ETime}";
//           	}
				//url:'<%=basePath%>statistics/workloadStatistics/listDeptWorkCountByMongo.action',
// 			$('#countList').datagrid({
// 				queryParams:{
// 					STime: Stime,
// 					ETime: Etime,
// 					menuAlias:menuAlias,
// 					dept: deptCode
// 				}
// 			});
				
			$('#countList').datagrid('load',{
				STime: Stime,
				ETime: Etime,
				menuAlias:menuAlias,
				dept: deptCode
			});


		 }
		
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
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(0));
		 }
		function searchThree(){
			var Etime = GetDateStr(0);
			var Stime = GetDateStr(-3);
			searchFinal(Stime,Etime);
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(-1));
		}
		function searchSeven(){
			var Etime = GetDateStr(0);
			var Stime = GetDateStr(-7);
			searchFinal(Stime,Etime);
			$('#STime').val(Stime);
			$('#ETime').val(GetDateStr(-1));
		}
		//查询前15天
		function searchFifteen(){
			var Stime = GetDateStr(-15);
			var Etime = GetDateStr(0);
			searchFinal(Stime,Etime);
			$('#STime').val(Stime);
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
            $('#STime').val(Stime);
            $('#ETime').val(lastDay);
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
		function getCurrentMonthFirst(){
			 var date=new Date();
			 date.setDate(1);
			 return date.Format("yyyy-MM-dd");
		}
		function getCurrentMonthLast(){
			 var date=new Date();
			 var currentMonth=date.getMonth();
			 var nextMonth=++currentMonth;
			 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
			 var oneDay=1000*60*60*24;
			 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
		} 
		function searchMonth(){
			//var Etime = getCurrentMonthLast();
			//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Stime = getCurrentMonthFirst();
			var Etime = GetDateStr(1);	
			searchFinal(Stime,Etime);
			var Etime = GetDateStr(0);	
			$('#STime').val(Stime);
			$('#ETime').val(Etime);
		}
		function searchYear(){
			//var Etime = new Date().getFullYear()+"-12-31";
			//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Stime = new Date().getFullYear()+"-01-01";
			var Etime = GetDateStr(1);
			searchFinal(Stime,Etime);
			var Etime = GetDateStr(0);
			$('#STime').val(Stime);
			$('#ETime').val(Etime);
		}
		
		 /**
		 * 重置
		 * @author huzhenguo
		 * @date 2017-03-17
		 * @version 1.0
		 */
		function clears(){
			$('#STime').val('${STime}');
			$('#ETime').val('${ETime}');
			$('#ksnew').val('');
			$("a[name='menu-confirm-cancel']").click();
			$("a[name='menu-confirm-clear']").click();
			searchFrom();
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<body>
	<div id="divLayout"  class="easyui-layout" data-options="fit:true,border:false" style="width: 100%; height: 100%; position :relative">
<!-- 			<div id = "top" style="padding: 5px 5px 0px 5px; " data-options="region:'north',border:false"> -->
				<table    cellspacing="0" cellpadding="0"  data-options="border:false"  style="padding: 7px 7px 0px;width: 100%  ;position :absolute; z-index:100;"  >
					<tr style = "">
						<!-- 开始时间 --> 
							<td style="width:40px;" align="left">日期:</td>
							<td style="width:110px;">
								<input id="STime" class="Wdate" type="text" name="STime" value="${STime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<!-- 结束时间 --> 
							<td style="width:40px;" align="center">至</td>
							<td style="width:110px;">
								<input id="ETime" class="Wdate" type="text" name="ETime" value="${ETime}" onClick="WdatePicker()" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
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
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</shiro:hasPermission>
						</td>
						<td style='text-align: right'>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
								<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
								<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
								<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
								<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
								<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
								<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
<!-- 			</div> -->
			<div id="tableShow" style="width: 100%;height: 100% ; margin-top: 38px" >
				<table style="" id="countList"  class="easyui-datagrid" data-options="fit:true">
					<thead frozen="true">
						<tr>
							<th data-options="field:'deptName'" width="8%">挂号科室</th>
						</tr>
					</thead>
					<thead>
						<tr>
							<th data-options="field:'',colspan:24" >专家号</th>
							<th data-options="field:'',colspan:12" >分诊类别</th>
							<th data-options="field:'',colspan:2,rowspan:2" width="6%">合计</th>
						</tr>
						<tr>
							<th data-options="field:'',colspan:2" width="6%">国家级知名专家</th>
							<th data-options="field:'',colspan:2" width="6%">省级知名专家</th>
							<th data-options="field:'',colspan:2" width="6%">知名专家</th>
							<th data-options="field:'',colspan:2" width="6%">教授</th>
							<th data-options="field:'',colspan:2" width="6%">副教授</th>
							<th data-options="field:'',colspan:2" width="6%">简易门诊</th>
							<th data-options="field:'',colspan:2" width="6%">一般医生</th>
							<th data-options="field:'',colspan:2" width="6%">主治医生</th>
							<th data-options="field:'',colspan:2" width="6%">老年优诊</th>
							<th data-options="field:'',colspan:2" width="6%">视力诊查费</th>
							<th data-options="field:'',colspan:2" width="6%">居民健身卡</th>
							<th data-options="field:'',colspan:2" width="6%">其他</th>
							<th data-options="field:'',colspan:2" width="4%">急诊</th>
							<th data-options="field:'',colspan:2" width="4%">优诊</th>
							<th data-options="field:'',colspan:2" width="4%">预约</th>
							<th data-options="field:'',colspan:2" width="4%">过号</th>
							<th data-options="field:'',colspan:2" width="4%">复诊</th>
							<th data-options="field:'',colspan:2" width="4%">平诊</th>
						</tr>
						<tr>
							<th data-options="field:'gjjzmzjcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'gjjzmzjmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'sjzmzjcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'sjzmzjmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'zmzjcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'zmzjmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'jscount',align:'right'" width="4%">数量</th>
							<th data-options="field:'jsmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'fjscount',align:'right'" width="4%">数量</th>
							<th data-options="field:'fjsmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'jymzcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'jymzmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'ybyscount',align:'right'" width="4%">数量</th>
							<th data-options="field:'ybysmoney',align:'right',formatter:changeCost" width="6%"> 金额</th>
							<th data-options="field:'zzyscount',align:'right'" width="4%">数量</th>
							<th data-options="field:'zzysmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'lnyzcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'lnyzmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'slzcfcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'slzcfmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'jmjskcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'jmjskmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'othercount',align:'right'" width="4%">数量</th>
							<th data-options="field:'othermoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'jzcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'jzmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'yzcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'yzmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'yycount',align:'right'" width="4%">数量</th>
							<th data-options="field:'yymoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'ghcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'ghmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'fzcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'fzmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'pzcount',align:'right'" width="4%">数量</th>
							<th data-options="field:'pzmoney',align:'right',formatter:changeCost" width="6%">金额</th>
							<th data-options="field:'totalcount',align:'right'" width="5%">数量</th>
							<th data-options="field:'totalmoney',align:'right',formatter:changeCost" width="8%">金额</th>
						</tr>
					</thead>
				</table>
			</div>
	</div>
	</body>
</html>
