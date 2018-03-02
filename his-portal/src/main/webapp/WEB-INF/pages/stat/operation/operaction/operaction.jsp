<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<body >
<div id="cc" class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">   
		<div data-options="region:'north',split:false" style="height:110px;width: 100%;border-top:0">
			<div id="cc" class="easyui-layout" style="width:100%;height:100%;" >   
				<div data-options="region:'north',split:false,border: false" style="height:38px;width: 100%;padding: 5px; ">
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:void();" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202'">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:exportList();" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
					</shiro:hasPermission>
					<a href="javascript:clearQuery();" class="easyui-linkbutton" iconCls="reset" ">重置</a>
					<a href="javascript:onclickDay('6')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>      
					<a href="javascript:onclickDay('5')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>  
					<a href="javascript:onclickDay('7')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>         
					<a href="javascript:onclickDay('4')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>    
					<a href="javascript:onclickDay('3')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>    
					<a href="javascript:onclickDay('2')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>  
					<a href="javascript:onclickDay('1')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
					</div>   
					<div data-options="region:'center'"  style="height:60px;width:100%;padding: 5px;border-bottom:0"  >
						<table style="width: 100%;height: 100%" >
							<tr>
								<td nowrap='true' style="width:80px;" >开始时间:</td>
								<td><input id="login" value="${login}" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td nowrap='true' style="width:80px;" >结束时间:</td>
								<td><input id="end" value="${end}" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td nowrap='true' style="width:130px;" >项目单价（>=）:</td>
								<td><input class="easyui-numberbox" id="price" ></td>
								<td  nowrap='true' style="width:80px;" >记帐编号:</td>
								<td  nowrap='true'><input class="easyui-textbox" id="repno"  data-options="prompt:'输入记帐编号后六位'" >
							</tr>
							
						</table>
					</div> 
				</div>
			</div>  
		<div data-options="region:'center'" style="width: 99%">
			<div id="cc" class="easyui-layout" style="width:100%;height:100%;" >   
				<div data-options="region:'north',split:false,border: false" style="height:90px;width: 100%;padding: 5px; ">
					<div style="text-align: center; width:100%;height: 35px">
						    <h5 style="font-size: 30;font: bold;">手术耗材统计（汇总）</h5>
					</div>
					<div style="width: 100%;">
						<table style="width:100%;height: 45px">
							<tr>
								<td style="width: 280px;font-size: 18">统计日期：
									<span style="width:200px;font-size: 18" id="log">${login }</span>
								</td>
								<td style="text-align: left;width: 260px;font-size: 18">&nbsp;至&nbsp;
									<span style="width: 200px;font-size: 18" id="en">${end }</span>
								</td>
								<td ></td>
								<td ></td>
								<td style="width:320px;text-align: right;font-size: 18" >统计科室：
									<span style="width: 180px;font-size: 18" id="deptName">${deptName} </span></td>
								<td ></td>
							</tr>
						</table>
					</div>
				</div>
				<div data-options="region:'center'" style="width: 99%;">	
					<table  id="list" class="easyui-datagrid" style="width: 100%;height:100%"   
						data-options="fitColumns:true,singleSelect:true,border:false">   
						<thead>   
							<tr>   
								<th data-options="field:'undrugGbcode',width:60,align:'center'">记帐编号</th>   
								<th data-options="field:'itemName',width:150,align:'center'">治疗项目</th>   
								<th data-options="field:'unitPrice',width:50,align:'right',halign:'center'">单价</th>
								<th data-options="field:'qty',width:50,align:'right',halign:'center'">数量</th>   
								<th data-options="field:'totCost',width:50,align:'right',halign:'center'">合计金额</th>   
							</tr>   
						</thead>   
					</table>  
						<form id="saveForm" method="post"/>
				</div>
			</div>  
		</div>	 
	</div>
	<script type="text/javascript">
	var log="";
	var en="";
		$(function(){
			log="${login }";
			en="${end }";
			$("#list").datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 100 ],
				url:"<%=basePath%>statistics/Operaction/getOperactionlist.action",
				queryParams:{login:log,end:en},
				method:"post",
				 onLoadSuccess:function(row, data){
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
						}}
			});
		});
		//查询按钮
		function query(){
			var login=$("#login").val().trim();
			var end=$("#end").val().trim();
			var price=$("#price").numberbox("getValue").trim();
			var repno=$("#repno").textbox("getValue").trim();
			if(login.trim()==""){
				$.messager.alert("提示","开始日期不能为空");
				return ;
			}
		    if(login.trim() != ""){
				var reg = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
			    var r = login.trim().match(reg);     
			    if(r==null){
				    $.messager.alert("提示",'对不起，您输入的日期格式不正确!请输入正确格式或点击日历图标选择时间'); //请将“日期”改成你需要验证的属性名称!
				    return ;
			    }    
			}
			if(repno&&repno.length!=6){
				$.messager.alert("提示","请输入记帐编号后六位");
				return;
			}
			if(login){
				$("#log").text(login);
			}
			if(end){
				$("#en").text(end);
			}
			if(login&&end){
				if(end>login){
						$("#list").datagrid({
							url:"<%=basePath%>statistics/Operaction/getOperactionlist.action",
							queryParams:{login:login,end:end,price:price,repno:repno},
							method:"post"
						});
				}else{
					$.messager.alert("提示","结束时间必须大于开始时间");
				}
			}else{
						$("#list").datagrid({
							url:"<%=basePath%>statistics/Operaction/getOperactionlist.action",
							queryParams:{login:login,end:end,price:price,repno:repno},
							method:"post"
						});
			}
			
			
		}
		//清空条件
		function clearQuery(){
			$('#login').val(log);
			$('#end').val(en);
			if(log){
				$("#log").text(log);
			}
			if(en){
				$("#en").text(en);
			}
			query();
		}
		//导出列表
		function exportList() {
			var Stime = $('#login').val().trim();
			var Etime = $('#end').val().trim();
			var drug = $('#price').numberbox('getValue').trim();
			var dept = $('#repno').textbox('getValue').trim();
			var data=$("#list").datagrid('getData');
			if(data.total==0){
				$.messager.alert("友情提示", "列表无数据，无法导出");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				return;
			}
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {              
						url :"<%=basePath%>statistics/Operaction/expOperactionlist.action?login="+Stime+"&end="+Etime+"&price="+drug+"&repno="+dept,
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							$.messager.alert("操作提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					});
				}
			});
		}
		//打印
		function edit(){
			 var timerStr = Math.random();
			 var loginTime;
			 var endTime;
			 var login=$('#login').val().trim();
			 if(login){
				 loginTime= $('#login').val().trim(); 
			 }else{
				 loginTime="${login}";
			 }
			 if($('#end').val()!=null
					 &&$('#end').val()!=""){
				 endTime= $('#end').val().trim();
			 }else{
				 endTime="${end}";
			 }
			 var data=$("#list").datagrid('getData');
				if(data.total==0){
					$.messager.alert("友情提示", "列表无数据，无法打印");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
					return;
				}
			 var price = $('#price').numberbox('getValue').trim();
			 var repno = $('#repno').textbox('getValue').trim();
			 var deptNo='${deptName}';
			 $.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
					if(res) {
                       window.open ("<%=basePath%>iReport/iReportPrint/iReportOperactionRecord.action?randomId="+timerStr+"&repno="+repno+"&price="+price+"&loginTime="+loginTime+"&endTime="+endTime+"&fileName=SSHCTJ",
		                          "newwindow","height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"); 
					};
				});
		}
		
		
		function onclickDay(flg){
			if(flg=='1'){
				var st=beforeDay(0);
				var et=beforeDay(0);
				var start= st+" 00:00:00";
				var myDate  = new Date();
				var min=myDate.getMinutes() <=9 ? '0'+myDate.getMinutes() :myDate.getMinutes();
				var sec=myDate.getSeconds()<=9 ? "0"+myDate.getSeconds():myDate.getSeconds();
				var end= et+" "+myDate.getHours()+":"+min+":"+sec;
				$('#login').val(start)
				$("#end").val(end);
				query();
			}else if(flg=='2'){
				var st=beforeDay(3);
				var et=beforeDay(1);
				var start= st+" 00:00:00";
				var end= et+" 23:59:59";
				$('#login').val(start)
				$("#end").val(end);
				query();
			}else if(flg=='3'){
				var st=beforeDay(7);
				var et=beforeDay(1);
				var start= st+" 00:00:00";
				var end= et+" 23:59:59";
				$('#login').val(start)
				$("#end").val(end);
				query();
			}else if(flg=='4'){
				var st=beforeDay(15);
				var et=beforeDay(1);
				var start= st+" 00:00:00";
				var end= et+" 23:59:59";
				$('#login').val(start)
				$("#end").val(end);
				query();
			}else if(flg=='5'){
				var myDate  = new Date();
				var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
				 var year = myDate.getFullYear();
				 var et=beforeDay(0);
				 var start= year+"-"+month+"-01 00:00:00";
				 var min=myDate.getMinutes() <=9 ? '0'+myDate.getMinutes() :myDate.getMinutes();
				 var sec=myDate.getSeconds()<=9 ? "0"+myDate.getSeconds():myDate.getSeconds();
				 var end= et+" "+myDate.getHours()+":"+min+":"+sec;
				 $('#login').val(start)
				 $("#end").val(end);
				 query();
			}else if(flg=='7'){
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
				var login = year + "-" + month + "-" + "01 00:00:00";//上个月的第一天
				var lastDate = new Date(year, month, 0);
				var end = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate()) + " 23:59:59";//上个月的最后一天
				 $('#login').val(login)
				 $("#end").val(end);
				 query();
			}else{
				var myDate  = new Date();
				var et=beforeDay(0);
				var start= myDate.getFullYear()+"-01-01 00:00:00";
				var min=myDate.getMinutes() <=9 ? '0'+myDate.getMinutes() :myDate.getMinutes();
				var sec=myDate.getSeconds()<=9 ? "0"+myDate.getSeconds():myDate.getSeconds();
				var end= et+" "+myDate.getHours()+":"+min+":"+sec;
				$('#login').val(start)
				$("#end").val(end);
				query();
			}
		}
		
		function beforeDay(beforeDayNum) {
			var d = new Date();
			var endDate = dateToString(d);
			d = d.valueOf();
			d = d - beforeDayNum * 24 * 60 * 60 * 1000;
			d = new Date(d);
			var startDate = dateToString(d);
			return startDate;
		}
		function dateToString(d) {
			var y = d.getFullYear();
			var m = d.getMonth() + 1;
			var d = d.getDate();
			if (m.toString().length == 1) m = "0" + m;
			if (d.toString().length == 1) d = "0" + d;
			return y + "-" + m + "-" + d;
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>