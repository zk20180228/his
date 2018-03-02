<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
		<div data-options="region:'north',split:false" style="height:110px;width: 100%;border-top:0">
			<div id="cc" class="easyui-layout" style="width:100%;height:100%;" >   
				<div data-options="region:'north',split:false,border: false" style="height:38px;width: 100%;padding: 5px; ">
					<shiro:hasPermission name="${menuAlias}:function:query"> 
					<a href="javascript:query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:readCard"> 
						<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					 </shiro:hasPermission>
			        <shiro:hasPermission name="${menuAlias}:function:readIdCard"> 
			        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					 </shiro:hasPermission> 
					 <shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:edit();" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202'">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:exportList();" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
					</shiro:hasPermission>
					<a href="javascript:clearQuery();" class="easyui-linkbutton" iconCls="reset">重置</a>
					<a href="javascript:onclickDay('6')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px;margin-right:2px">当年</a>      
					<a href="javascript:onclickDay('5')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
					<a href="javascript:onclickDay('7')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>             
					<a href="javascript:onclickDay('4')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>    
					<a href="javascript:onclickDay('3')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>    
					<a href="javascript:onclickDay('2')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>  
					<a href="javascript:onclickDay('1')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
				</div>
				<div data-options="region:'center'"  style="height:60px;width:100%;padding: 5px;border-bottom:0;overflow-x:auto; ">
					<table style="width: 100%;height: 100%">
						<tr>
							<td style="width: 260px"  nowrap="true" >开始时间：<input id="login" value="${login}" class="Wdate" type="text"  onClick="WdatePicker()" style="width:120px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width: 260px"  nowrap="true" >结束时间： <input id="end" value="${end}" class="Wdate" type="text"  onClick="WdatePicker()" style="width:120px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width:  270px"  nowrap="true" >&nbsp;项目单价(>=)：<input class="easyui-numberbox" id="price"  style="width:150px;"></td>
							<td style="width: 230px" nowrap='true'>身份证号：<input class="easyui-textbox" id="identityCard"    style="width:150px" /></td>
							<td style="width: 230px" nowrap="true">记帐编号：<input class="easyui-textbox" id="repno" data-options="prompt:'输入记帐编号后六位'">
							 
						</tr>
					</table>
				</div>
			</div>   
		</div>   
		
		<div data-options="region:'center'" style="width: 99%;">
			<div id="cc" class="easyui-layout" style="width:100%;height:100%;" >   
				<div data-options="region:'north',split:false,border: false" style="height:90px;width: 100%; ">
					<div style="text-align: center; width: 100%;height: 35px">
						    <h5 style="font-size: 30;font: bold;">手术耗材统计（明细）</h5>
					</div>
					<div style="width: 100%;">
						<table style="width:100%;padding: 5px;height: 45px">
							<tr>
								<td style="width: 200px;font-size: 18" nowrap='true'>统计日期：
									<span style="text-align:left;width:180px;font-size: 18" id="log">${login } </span>
								</td>
								<td style="text-align: left;width: 260px;font-size: 18" nowrap='true'>&nbsp;至&nbsp;
									<span style="text-align: left;width: 200px;font-size: 18" id="en">${end }</span> 
								</td>
								<td ></td>
								<td ></td>
								<td style="width:320px;text-align: right;font-size: 18" >统计科室：<span style="width: 180px;font-size: 18" id="deptName">${deptName}</span></td>
								<td > </td>
							</tr>
						</table>
					</div>
				</div>
				<div data-options="region:'center'" style="width: 99%;">
					<table  id="list" class="easyui-datagrid" style="width:100%;height: 100%"   
						data-options="fitColumns:true,singleSelect:true,border:false">   
						<thead>   
							<tr>
								<th data-options="field:'recipeDeptcode',width:130,align:'center'">开立科室</th> 
								<th data-options="field:'recipeDoccode',width:100,align:'center'">开立医生</th>  
								<th data-options="field:'inpatientNo',width:100,align:'center'">病历号</th>
								<th data-options="field:'name',width:100,align:'center'">患者姓名</th>   
								<th data-options="field:'undrugGbcode',width:100,align:'center'">记帐编号</th>   
								<th data-options="field:'itemName',width:220,align:'center'">项目名称</th> 
								<th data-options="field:'spec',width:70,align:'center'">规格型号</th>  
								<th data-options="field:'unitPrice',width:90,align:'right',halign:'center'">单价</th>
								<th data-options="field:'currentUnit',width:45,align:'center'">单位</th>
								<th data-options="field:'qty',width:50,align:'right',halign:'center'">数量</th>   
								<th data-options="field:'totCost',width:90,align:'right',halign:'center'">合计金额</th>   
								<th data-options="field:'feeDate',width:110,align:'center'">记帐时间</th>   
								<th data-options="field:'feeOpercode',width:100,align:'center'">操作员</th>
							</tr>   
						</thead>   
					</table>  
						<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="reportToLogin" id="reportToLogin" value=""/>
						<input type="hidden" name="reportToEnd" id="reportToEnd" value=""/>
						<input type="hidden" name="reportToPrice" id="reportToPrice" value=""/>
						<input type="hidden" name="reportToRepno" id="reportToRepno" value=""/>
						<input type="hidden" name="reportToFileName" id="reportToFileName" value=""/>
						<input type="hidden" name="identityCard" id="identityCardHidden" value=""/>
						</form>
				</div>   
			</div>
		</div>
</div>
	<script type="text/javascript">
		var log="";
		var en="";
		var deptMap="";
		var empMap="";
		$(function(){
			
			log="${login }";
			en="${end }";
			$("#list").datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 100 ],
				url:"<%=basePath%>statistics/OperationDetails/getoperationDetailsList.action",
				queryParams:{login:log,end:en,identityCard:null},
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
			var identityCard= $('#identityCard').textbox('getValue').trim();
			if(repno&&repno.length!=6){
				$.messager.alert("提示","请输入记帐编号后六位");
				return;
			}
			if(login.trim()==""){
				$.messager.alert("提示","请输入开始日期");
				return ;
			}
			
			if(login){
				$("#log").text(login);
			}
			if(end){
				$("#en").text(end);
			}
			if(end&&login){
				if(end>=login){
						$("#list").datagrid({
							url:"<%=basePath%>statistics/OperationDetails/getoperationDetailsList.action",
							queryParams:{login:login,end:end,price:price,repno:repno,identityCard:identityCard},
							method:"post"
						});
				}else{
					$.messager.alert("提示","结束时间必须大于开始时间");
				}
			}else{
					$("#list").datagrid({
						url:"<%=basePath%>statistics/OperationDetails/getoperationDetailsList.action",
						queryParams:{login:login,end:end,price:price,repno:repno,identityCard:identityCard},
						method:"post"
					});
			}
		}
		//清空条件
		function clearQuery(){
			$('#login').val(log);
			$('#end').val(en);
			query();
		}
		//导出列表
		function exportList() {
			var Stime = $('#login').val().trim();
			var Etime = $('#end').val().trim();
			var drug = $('#price').numberbox('getValue').trim();
			var dept = $('#repno').textbox('getValue').trim();
			var identityCard= $('#identityCard').textbox('getValue').trim();
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
						url :"<%=basePath%>statistics/OperationDetails/expOperactionlist.action?login="+Stime+"&identityCard="+identityCard+"&end="+Etime+"&price="+drug+"&repno="+dept,
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
			 var identityCard= $('#identityCard').textbox('getValue').trim();
			 if($('#login').val()){
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
				$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
					if(res){
						    //hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
						    //给表单的隐藏字段赋值
						    $("#reportToLogin").val(loginTime);
						    $("#reportToEnd").val(endTime);
						    $("#reportToPrice").val(price);
						    $("#reportToRepno").val(repno);
						    $("#reportToFileName").val("SSHCTJMX");
						    $("#identityCardHidden").val(identityCard);
						    
						    //表单提交 target
						    var formTarget="hiddenFormWin";
					        var tmpPath = "<%=basePath%>statistics/OperationDetails/iReportOperDetailsRecord.action";
					        //设置表单target
					        $("#reportToHiddenForm").attr("target",formTarget);
					        //设置表单访问路径
							$("#reportToHiddenForm").attr("action",tmpPath); 
					        //表单提交时打开一个空的窗口
						    $("#reportToHiddenForm").submit(function(e){
						    	 var timerStr = Math.random();
								 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
							});  
						    //表单提交
						    $("#reportToHiddenForm").submit();
				     }
				});
		}
		
		
		function onclickDay(flg){
			if(flg=='1'){
				var st=beforeDay(0);
				var et=beforeDay(0);
				$('#login').val(st)
				$("#end").val(et);
				query();
			}else if(flg=='2'){
				var st=beforeDay(3);
				var et=beforeDay(1);
				$('#login').val(st)
				$("#end").val(et);
				query();
			}else if(flg=='3'){
				var st=beforeDay(7);
				var et=beforeDay(1);
				$('#login').val(st)
				$("#end").val(et);
				query();
			}else if(flg=='4'){
				var st=beforeDay(15);
				var et=beforeDay(1);
				$('#login').val(st)
				$("#end").val(et);
				query();
			}else if(flg=='5'){
				var myDate  = new Date();
				var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
				 var year = myDate.getFullYear();
				 var start= year+"-"+month+"-01";
				 var et=beforeDay(0);
				 $('#login').val(start)
				 $("#end").val(et);
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
				var login = year + "-" + month + "-" + "01";//上个月的第一天
				var lastDate = new Date(year, month, 0);
				var end = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
				 $('#login').val(login)
				 $("#end").val(end);
				 query();
			}else{
				var myDate  = new Date();
				var start= myDate.getFullYear()+"-01-01";
				 var et=beforeDay(0);
				$('#login').val(start)
				$("#end").val(et);
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