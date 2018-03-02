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
</head>
<style>
.center {
  display: -webkit-box;
  -webkit-box-orient: horizontal;
  -webkit-box-pack: center;
  -webkit-box-align: center;
  display: -moz-box;
  -moz-box-orient: horizontal;
  -moz-box-pack: center;
  -moz-box-align: center;
  display: -o-box;
  -o-box-orient: horizontal;
  -o-box-pack: center;
  -o-box-align: center;
  display: -ms-box;
  -ms-box-orient: horizontal;
  -ms-box-pack: center;
  -ms-box-align: center;
  box-orient: horizontal;
  box-pack: center;
  box-align: center;
}
</style>
<body class="easyui-layout" data-options="fit:true" style="width:100%;height:100%" >
	<div data-options="region:'north',title:''" style="width:100%;height:100%">
		<div class="easyui-layout" data-options="fit:true" style="width:100%;height:100%" >
			<div data-options="region:'west',title:''" style="width:60%;height:100%;padding: 0px;">
				<table id="searchTab" style="width: 100%;padding:0px 0px 0px 5px;" border="0">
						<tr>
							<td style="width: 6%;">
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom(1)" iconCls="icon-search" style="height:23px;margin-top:-3px">查询</a>
								</shiro:hasPermission>
							</td>
							<td>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:clear();" onclick="" class="easyui-linkbutton" iconCls="reset" style="height:23px;margin-top:-3px">重置</a>
								</shiro:hasPermission>
							</td>
							<td style='text-align: right' colspan="6">
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom(6)" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
									<a href="javascript:void(0)" onclick="searchFrom(2)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
									<a href="javascript:void(0)" onclick="searchFrom(3)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
									<a href="javascript:void(0)" onclick="searchFrom(0)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
									<a href="javascript:void(0)" onclick="searchFrom(7)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
									<a href="javascript:void(0)" onclick="searchFrom(5)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
									<a href="javascript:void(0)" onclick="searchFrom(4)" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
								</shiro:hasPermission>
							</td>
						</tr>
						<tr>
							<!--开始时间 --> 
							<td style="width:45px;" align="left">日期:</td>
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
				    	       	<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="deptNew" readonly="readonly"/><span></span></div> 
				    	       	<div id="m2" class="xmenu" style="display: none;">
				    	       		<div class="searchDept">
				    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
				    	       			<span class="searchMenu"><i></i>查询</span>
				    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
											<span class="a-btn-text">取消</span>
										</a>						
										<a name="menu-confirm-clear" id="qx2" href="javascript:void(0);" class="a-btn">
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
			    			<td style="" class="newMenu">
				    			<div class="doctorInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="doctorNew" readonly="readonly"/><span></span></div> 
				    	       	<div id="m3" class="xmenu" style="display: none;">
				    	       		<div class="searchDept">
				    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
				    	       			<span class="searchMenu"><i></i>查询</span>
				    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" >
											<span class="a-btn-text">取消</span>
										</a>
										<a name="menu-confirm-clear" id="qx1" href="javascript:void(0);" class="a-btn">
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
			    		</tr>
						</table>
				<table id="wordList" class="easyui-datagrid" style="width:100%;height:93.9%;" data-options="singleSelect:true,striped:true,fitColumns:true,rownumbers:true">
					<thead>
						<tr>
							<th data-options="field:'deptCode',width:'24%'">科室</th>
							<th data-options="field:'doctorName',width:'24%'">医生</th>
							<th data-options="field:'openTotal',width:'24%'" >开方数量</th>
							<th data-options="field:'moneyTotal',width:'24%'">开方金额</th>
						</tr>
					</thead>
				</table>
			</div>   
			<div data-options="region:'center',title:''" style="width:40%;height:100%;padding: 5px;">
				时间维度:<select id="combo" class="easyui-combobox" style="width:60px;" >  
				    <option value="1">年</option>   
				    <option value="2">月</option>   
				    <option value="3">日</option>   
				</select>  
				<span id="dateId" ><input id="date" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker()" style="maheight:24px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;margin-left: 12px;margin-top: 8px;"/></span> 
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchTJ()" iconCls="icon-search" style="height:23px;margin-top:-3px">查询</a>
				<div id="hb" style="width: 100%;height:22.5%;"></div>
				<div id="tb" class="center" style="width: 100%;height:22.5%;"></div>
				<div id="top1"  class="center" style="width: 100%;height: 22.5%;float:left"></div>
    			<div id="top2"  class="center" style="width: 100%;height: 22.5%;float: left;"></div>
			</div>   
		</div>
	</div>   
<!--     <div data-options="region:'center',title:''" style="width:100%;height:30%"> -->
    	
<!--     </div>    -->
</body>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
	var begin="${sTime}";
	var end="${sTime}";
	var menuAlias = '${menuAlias}';
	$(function(){
		//选择科室
		 $(".deptInput").MenuList({
				width :530, //设置宽度，不写默认为530，不要加单位
				height :400, //设置高度，不写默认为400，不要加单位
				dropmenu:"#m2",//弹出层id，必须要写
				isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
				para:"I",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
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
				para:"I",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
				firsturl: "<%=basePath %>statistics/RegisterInfoGzltj/getDoctorList.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
				relativeInput:".deptInput"	//与其级联的文本框
			});
			$('#combo').combobox({
				onSelect:function(record){
					var sign=record.value;
					var daFa;//日期格式
					var date=new Date();//时间
					if(sign==1){
						daFa="onClick=\"WdatePicker({dateFmt:'yyyy',maxDate:'%y'})\"";
						date=date.getFullYear();
					}else if(sign==2){
						daFa="onClick=\"WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M'})\"";
						date=date.getFullYear()+'-'+(date.getMonth()+1);
						date=date.replace(/-(\d{1})\b/g, '-0$1')
					}else{
						daFa="onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})\"";
						date=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
						date=date.replace(/-(\d{1})\b/g, '-0$1')
					}
					var html="<input id=\"date\" class=\"Wdate\" type=\"text\"   "+daFa+" style=\"maheight:24px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;margin-left: 12px;margin-top: 8px;\"/>";
					$("#dateId").html(html);
					$('#date').val(date);
				}
			});
			$('#combo').combobox('setValue','3');
			var date=$('#date').val();
			var dateSign=$('#combo').combobox('getValue');
			//请求同环比
			AjaxSame(date,dateSign);
			//top
			AjaxTop(date,dateSign);
			$('#wordList').datagrid({
				pagination:true,
				showFooter: true,
				pagination:true,
				pageSize:10,
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
				}
			});
	});
	//同环比
	function AjaxSame(date,dateSign){
		$.ajax({
			type: "post",
	        url: "<%=basePath %>/statistics/WordLoadDoctorGZLTJ/queryTotalInpaitent.action",
	        data: {date:date,dateSign:dateSign},
	        dataType: "json",
	        success: function (data) {
	       		var tb=data.same;//同比
	       		var hb=data.sque;//环比
	       		var dateName=[];
	       		var dateList=[];
	       		for(var i=0;i<tb.length;i++){
	       			dateName.push(tb[i].workDate)
	       			dateList.push(tb[i].moneyTotal);
	       		}
	       		sameFunction(dateList,dateName);
	       		dateName=[];
	       		dateList=[];
	       		for(var i=0;i<hb.length;i++){
	       			dateName.push(hb[i].workDate)
	       			dateList.push(hb[i].moneyTotal);
	       		}
	       		squeFunction(dateList,dateName);
	        }
		});
	}
	//top
	function AjaxTop(date,dateSign){
		$.ajax({
			type: "post",
	        url: "<%=basePath %>/statistics/WordLoadDoctorGZLTJ/queryTop.action",
	        data: {date:date,dateSign:dateSign},
	        dataType: "json",
	        success: function (data) {
	        	var topDept=data.dept;//科室top
	        	var topName=[];
	        	var dateList=[];
	        	for(var i=0;i<topDept.length;i++){
	        		topName.push(topDept[i].topName);
	        		dateList.push(topDept[i].moneyTotal);
	        	}
	        	topDeptFunction(dateList,topName);
	        	topName=[];
	        	dateList=[];
	        	var topDoc=data.doc;//医生top
	        	for(var i=0;i<topDoc.length;i++){
	        		topName.push(topDoc[i].topName);
	        		dateList.push(topDoc[i].moneyTotal);
	        	}
	        	topDocFunction(dateList,topName);
	        }
		});
	}
	//科室top
	function topDeptFunction(dateList,dateName){
		if(dateList.length>0){
			eChars1('top1',dateList,dateName,'TOP(科室)','金额(万元)');
		}else{
			$('#top1').html('暂无数据');
		}
		
	}
	//医生top
	function topDocFunction(dateList,dateName){
		if(dateList.length>0){
		eChars1('top2',dateList,dateName,'TOP(医生)','金额(万元)');
		}else{
			$('#top2').html('暂无数据');
		}
	}
	//同比
	function sameFunction(dateList,dateName){
		if(dateList.length>0){
			eChars('tb',dateList,dateName,'月同比(万元)','金额(万元)');
		}else{
			$('#tb').html('暂无数据');
		}
	}
	//环比
	function squeFunction(dateList,dateName){
		eChars('hb',dateList,dateName,'月环比(万元)','金额(万元)');
	}
	//图形
	function eChars(idName,dateList,DateName,tital,tip){
		// 路径配置
	    require.config({
	        paths: {
	            echarts: '${pageContext.request.contextPath}/javascript/echarts'
	        }
	    });
	    // 使用
	    require(
	        [
	            'echarts',
	            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	        ],
	    function (echarts) { 
		var myChart = echarts.init(document.getElementById(idName));
		var option = {
				title: {
					text: tital,
					x: 'center'
				},
				tooltip: {
					trigger: 'axis'
				},
				xAxis: [{
					name:'日期',
					type: 'category',
					axisLabel:{
		                //X轴刻度配置
		                interval:0,
		                rotate:0//-30度角倾斜显示
		           },
					data: DateName
				}],
				yAxis: [{
					name:'',
					type: 'value'
				}],
				 grid: { // 控制图的大小，调整下面这些值就可以，
		             x: 40,
		             x2: 100,
		             y2: 35,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
		         },
		         label:{
			    	normal:{
			    		show: true,
			    		position: 'inside'
			    		}},
				series: [{
						name: tip,
						type: 'bar',
						barWidth : 20,//柱图宽度
				        barMaxWidth:25,//最大宽度,
						data: dateList,
						itemStyle: {
						    normal: {
						    	color:"#79C2F5"
						    }
						}
					},
					 {
		                name:'折线',
		                type:'line',
		                itemStyle : {  /*设置折线颜色*/
		                    normal : {
		                        color:'#c4cddc'
		                    }
		                },
		                data:dateList
		            }
				],
			}
			myChart.setOption(option,true);
	       }
	);
	}
	//图形
	function eChars1(idName,dateList,DateName,tital,tip){
		// 路径配置
	    require.config({
	        paths: {
	            echarts: '${pageContext.request.contextPath}/javascript/echarts'
	        }
	    });
	    // 使用
	    require(
	        [
	            'echarts',
	            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	        ],
	    function (echarts) { 
		var myChart = echarts.init(document.getElementById(idName));
		var option = {
				title: {
					text: tital,
					x: 'center'
// 					y: "top"
				},
				tooltip: {
					trigger: 'axis'
				},
				xAxis: [{
					name:'日期',
					type: 'category',
					axisLabel:{
		                //X轴刻度配置
		                interval:0,
		                rotate:0//-30度角倾斜显示
		           },
					data: DateName
				}],
				yAxis: [{
					name:'',
					type: 'value'
				}],
				 grid: { // 控制图的大小，调整下面这些值就可以，
		             x: 40,
		             x2: 100,
		             y2: 35,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
		         },
		         label:{
			    	normal:{
			    		show: true,
			    		position: 'inside'
			    		}},
				series: [{
						name: tip,
						type: 'bar',
						barWidth : 20,//柱图宽度
				        barMaxWidth:25,//最大宽度,
						data: dateList,
						itemStyle: {
						    normal: {
						    	color:"#79C2F5"
						    }
						}
					}
				]
			}
			myChart.setOption(option);
	        }
		);
	}
	//查询统计
	function searchTJ(){
		var date=$('#date').val();
		var dateSign=$('#combo').combobox('getValue');
		//请求同环比
		AjaxSame(date,dateSign);
		//top
		AjaxTop(date,dateSign);
	}
	function searchFrom(flag){
		var startTime;
		var endTime;
			if(flag==1){
				startTime = $('#Stime').val();
				endTime = $('#Etime').val();
			}else{
				var date
				if(flag<=3){
					date=new Date(new Date().getTime()-1000*3600*24);
					endTime=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
				}else{
					date=new Date();
					endTime=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
				}
				if(flag==2){
					var lon=date.getTime()-1000*3600*24*3;
					startTime=new Date(lon);
					startTime=startTime.getFullYear()+'-'+(startTime.getMonth()+1)+'-'+startTime.getDate(); 
				}else if(flag==3){
					var lon=date.getTime()-1000*3600*24*7;
					startTime=new Date(lon);
					startTime=startTime.getFullYear()+'-'+(startTime.getMonth()+1)+'-'+startTime.getDate();  
				}else if(flag==4){
					startTime=(date.getFullYear())+'-01-01';
				}else if(flag==5){
					startTime=(date.getFullYear())+'-'+(date.getMonth()+1)+'-01'
				}else if(flag==0){
					var lon=date.getTime()-1000*3600*24*15;
					startTime=new Date(lon);
					startTime=startTime.getFullYear()+'-'+(startTime.getMonth()+1)+'-'+startTime.getDate(); 
				}else if(flag==7){//上月
					var lon=date.getTime();
				    var year=date.getFullYear();
				    var month=date.getMonth();
				    if(month==0){
				    	month=12;
				    	year=year-1;
				    }
				    startTime=year+'-'+month+'-01'
					var date=new Date(year,month,0);
				    endTime=year+'-'+month+'-'+date.getDate(); 
				}else{
					startTime=endTime.split(' ')[0];
				}
				startTime = startTime.replace(/-(\d{1})\b/g,'-0$1');
				endTime = endTime.replace(/-(\d{1})\b/g,'-0$1');
				$('#Stime').val(startTime);
				$('#Etime').val(endTime);
			}
			if(new Date(($('#Stime').val()).replace(/-/g,'/')).getTime()>new Date(($('#Etime').val()).replace(/-/g,'/')).getTime()){
				$.messager.alert('提示 ','开始时间不能大于结束时间');
				return false;
			}
		$('#wordList').datagrid({
			url:'<%=basePath%>/statistics/WordLoadDoctorGZLTJ/queryWeekInpaitent.action',
			queryParams: {
				begin: $('#Stime').val(),
				end: $('#Etime').val(),
				depts:$('#deptNew').getMenuIds(),
				doctors:$('#doctorNew').getMenuIds(),
				menuAlias:menuAlias
			}
		});
	}
	function clear(){
		$('#Stime').val(begin);
		$('#Etime').val(end);
		$('#qx1').click();
		$('#qx2').click();
	}
</script>
</html>
