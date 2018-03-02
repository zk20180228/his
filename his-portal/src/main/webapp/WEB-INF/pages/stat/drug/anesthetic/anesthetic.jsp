<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>麻醉精神药品统计</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
	$(function(){
		var login = "${login}";
		var end = "${end}";
		var deptId = $("#deptId").combobox("getValue");
		var drug = $("#drug").combobox("getValue");
		$("#list").datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:"<%=basePath%>statistics/Anesthetic/anestheList.action",
			queryParams:{login:login,end:end,drug:drug,deptId:deptId},
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
				}
			}
		});
		$("#drug").combobox({
			url:"<%=basePath%>statistics/Anesthetic/anestheDrug.action",
			valueField:'encode',
			textField:'name',
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i ++) {
					if (val == data[i].encode) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		$("#deptId").combobox({
			url:"<%=basePath%>statistics/Anesthetic/deptCombobox.action",
			valueField:'deptCode',
			textField:'deptName',
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i ++) {
					if (val == data[i].deptCode) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'deptCode';
				keys[keys.length] = 'deptName';
				keys[keys.length] = 'deptPinyin';
				keys[keys.length] = 'deptWb';
				keys[keys.length] = 'deptInputcode';
				return filterLocalCombobox(q, row, keys);
			}
		});
	});
	
	
	//查询按钮
	function query(flag){
		var login;
		var end;
		if(flag == 0){
			login = $('#login').val();
			end = $('#end').val();
		}else {
			var date=new Date();
			var sort = date.getTime() - 1000 * 3600 * 24;
			sort = new Date(sort);
			if(flag == 1){
				login = date.getFullYear() + '-' + ((date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : (date.getMonth()+1)) + '-' + (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + ' 00:00:00';
				end = currentTime();
			}else if(flag == 2){
				var lon=date.getTime()-1000*3600*24*3;
				var log = new Date(lon);
				login = log.getFullYear() + '-' + ((log.getMonth()+1) < 10 ? "0"+(log.getMonth()+1) : (log.getMonth()+1)) + '-' + (log.getDate() < 10 ? "0" + log.getDate() : log.getDate()) + ' 00:00:00'; 
				end = sort.getFullYear() + '-' + ((sort.getMonth()+1) < 10 ? "0"+(sort.getMonth()+1) : (sort.getMonth()+1)) + '-' + (sort.getDate() < 10 ? "0" + sort.getDate() : sort.getDate()) + ' 23:59:59';
			}else if(flag == 3){
				var lon=date.getTime()-1000*3600*24*7;
				var log = new Date(lon);
				login = log.getFullYear() + '-' + ((log.getMonth()+1) < 10 ? "0"+(log.getMonth()+1) : (log.getMonth()+1)) + '-' + (log.getDate() < 10 ? "0" + log.getDate() : log.getDate()) + ' 00:00:00'; 
				end = sort.getFullYear() + '-' + ((sort.getMonth()+1) < 10 ? "0"+(sort.getMonth()+1) : (sort.getMonth()+1)) + '-' + (sort.getDate() < 10 ? "0" + sort.getDate() : sort.getDate()) + ' 23:59:59';
			}else if(flag == 4){
				var lon=date.getTime()-1000*3600*24*15;
				var log = new Date(lon);
				login = log.getFullYear() + '-' + ((log.getMonth()+1) < 10 ? "0"+(log.getMonth()+1) : (log.getMonth()+1)) + '-' + (log.getDate() < 10 ? "0" + log.getDate() : log.getDate()) + ' 00:00:00'; 
				end = sort.getFullYear() + '-' + ((sort.getMonth()+1) < 10 ? "0"+(sort.getMonth()+1) : (sort.getMonth()+1)) + '-' + (sort.getDate() < 10 ? "0" + sort.getDate() : sort.getDate()) + ' 23:59:59';
			}else if(flag == 5){
				login = date.getFullYear() + '-' + ((date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : (date.getMonth()+1)) + '-01 00:00:00'; 
				end = currentTime();
			}else if(flag == 6){
				login = date.getFullYear() + '-01-01 00:00:00'; 
				end = currentTime();
			}else if(flag == 7){
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
				login = year + "-" + month + "-" + "01 00:00:00";//上个月的第一天
				var lastDate = new Date(year, month, 0);
				end = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate()) + " 23:59:59";//上个月的最后一天
			}
			$('#login').val(login);
			$('#end').val(end);
		}
		var deptId = $("#deptId").combobox("getValue");
		var drug = $("#drug").combobox("getValue");
		if(login == null || login == ""){
			$.messager.alert("提示","请选择开始时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}else if(end == null || end== ""){
			$.messager.alert("提示","请选择结束时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
		if(end > login){
			if(drug != "" && drug != null){
				if(deptId != "" && deptId != null){
					var drugtype = $("#drug").combobox("getText");
					$("#drugType").text(drugtype);
					var deptName = $("#deptId").combobox("getText");
					$("#deptName").text(deptName);
					$("#log").text(login);
					$("#en").text(end);
					$("#list").datagrid('load',{login:login,end:end,drug:drug,deptId:deptId});
				}else{
					$.messager.alert("提示","请选择科室");
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return;
				}
			}else{
				$.messager.alert("提示","请选择药品性质");
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return;
			}
		}else{
			$.messager.alert("提示","结束时间必须大于开始时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
		}
	}
	//导出列表
	function exportList() {
		var login = $("#login").val();
		var end = $("#end").val();
		var deptId = $("#deptId").combobox("getValue");
		var drug = $("#drug").combobox("getValue");
		if(login == null || login == ""){
			$.messager.alert("提示","请选择开始时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}else if(end == null || end== ""){
			$.messager.alert("提示","请选择结束时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}if(end > login){
			if(drug != "" && drug != null){
				if(deptId != "" && deptId != null){
					var rows = $('#list').datagrid('getRows');
					if(rows.length > 0){
						$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
							if (res) {
								$('#saveForm').form('submit', {
									url :"<%=basePath%>statistics/Anesthetic/expOperactionlist.action",
									onSubmit : function(param) {
										param.login = login;
										param.end = end;
										param.drug = drug;
										param.deptId = deptId;
										if(!$(this).form('validate')){
											return ;
										}
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
					}else{
						$.messager.alert("提示","当前统计无数据，无法导出！");
						setTimeout(function(){$(".messager-body").window('close')},3500);
						return;
					}
				}else{
					$.messager.alert("提示","请选择科室");
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return;
				}
			}else{
				$.messager.alert("提示","请选择药品性质");
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return;
			}
		}else{
			$.messager.alert("提示","结束时间必须大于开始时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
	}
	// 药品列表查询重置
	function report() {
		var login = $("#login").val();
		var end = $("#end").val();
		var deptId = $("#deptId").combobox("getValue");
		var drug = $("#drug").combobox("getValue");
		var deptName = $("#deptId").combobox("getText");
		var drugType = $("#drug").combobox("getText");
		$('#loginT').val(login);
		$('#endT').val(end);
		$('#deptCode').val(deptId);
		$('#deptN').val(deptName);
		$('#drugTypeId').val(drug);
		$('#drugTypeName').val(drugType);
		if(login == null || login == ""){
			$.messager.alert("提示","请选择开始时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}else if(end == null || end== ""){
			$.messager.alert("提示","请选择结束时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}if(end > login){
			if(drug != "" && drug != null){
				if(deptId != "" && deptId != null){
					var rows = $('#list').datagrid('getRows');
					if(rows.length > 0){
						//表单提交 target
						var formTarget="reportForm";
						var tmpPath = "<%=basePath%>statistics/Anesthetic/iReportMZJSYP.action";
						//设置表单target
						$("#reportForm").attr("target",formTarget);
						//设置表单访问路径
						$("#reportForm").attr("action",tmpPath); 
						//表单提交时打开一个空的窗口
						$("#reportForm").submit(function(e){
							var timerStr = Math.random();
							window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
							window.close();
						});
						$("#reportForm").submit();
					}else{
						$.messager.alert("提示","当前统计无数据，无法打印！");
						setTimeout(function(){$(".messager-body").window('close')},3500);
						return;
					}
				}else{
					$.messager.alert("提示","请选择科室");
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return;
				}
			}else{
				$.messager.alert("提示","请选择药品性质");
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return;
			}
		}else{
			$.messager.alert("提示","结束时间必须大于开始时间");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
	}
	/**
	 *得到当前时间
	 */
	function currentTime() {
			var date = new Date();
			this.year = date.getFullYear();
			this.month = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1):(date.getMonth()+1);
			this.date = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
			var retVal = this.year + "-" + this.month + "-" + this.date + " ";
			this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
			this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
			this.second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
			retVal += this.hour + ":" + this.minute + ":" + this.second;
			return retVal;
	}	
	// 药品列表查询重置
	function searchReload() {
		$('#login').val('');
		$('#end').val('');
		$('#drug').combobox('setValue','${drug}');
		$('#deptId').combobox('setValue','${deptId}');
		$('#list').datagrid('loadData',[]);
	}	
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
		<div data-options="region:'north',split:false" style="height:40px;width: 100%;padding: 4px 5px 4px 5px;">
			<table style="width: 100%;height:100%;">
				<tr>
					<td nowrap="nowrap">
						<a href="javascript:query(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						<a href="javascript:report()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
						<a href="javascript:exportList();" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
					</td>
					<td nowrap="nowrap" align="right">
						<a href="javascript:query(1)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当天</a>
						<a href="javascript:query(2)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
						<a href="javascript:query(3)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
						<a href="javascript:query(4)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
						<a href="javascript:query(7)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
						<a href="javascript:query(5)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
						<a href="javascript:query(6)" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当年</a>
						
					</td>
				</tr>
			</table>
			<form id="reportForm" method="post">
				<input type="hidden" id="loginT" name="login" value="${login}">
				<input type="hidden" id="endT" name="end" value="${end}">
				<input type="hidden" id="deptCode" name="deptId" value="${deptId}">
				<input type="hidden" id="deptN" name="deptName" value="${deptName}">
				<input type="hidden" id="drugTypeId" name="drug" value="${drug}">
				<input type="hidden" id="drugTypeName" name="drugType" value="${drugType}">
				<input type="hidden" id="fileName" name="fileName" value="MZJSYPTJ">
			</form>
		</div>   
		<div data-options="region:'center',border:false" style="width: 100%;">
			<div id="c" class="easyui-layout" style="width:100%;height:100%;">
				<div data-options="region:'north'" style="height:40px;width: 100%;">
					<table style="width: 100%;height:100%;">
						<input id="dept" value="${deptId}" type="hidden"/>
						<tr >
							<td nowrap="nowrap">日期：
								<input id="login" value="${login}" style="padding-bottom: 6px;" name="login" class="Wdate" type="text" onFocus="var endDate=$dp.$('end');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'end\')}'})"/>
								至
								<input id="end" value="${end}" style="padding-bottom: 6px" name="end" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'login\')}'})"/>
								药品类别 ：
								<input class="easyui-combobox" id="drug" value="${drug}">
								科室：
								<input class="easyui-combobox" id="deptId" value="${deptId}"/>
							</td>
						</tr>
					</table>
				</div>   
				<div data-options="region:'center',border:false" style="height:90%;width: 100%">
					<div id="z" class="easyui-layout" fit="true">
						<div data-options="region:'north'" style="width: 100%;height: 107px">
							<h5 style="font-size: 30;font: bold;text-align: center;padding-top: 5px">麻醉精神药品统计</h5>
							<table style="width: 100%;padding-top: 5px">
								<tr>
									<td style="width: 7%;font-size: 18"></font>统计日期：</td>
									<td style="text-align:left;width: 15%;font-size: 18" id="log">${login } </td>
									<td style="text-align: left;width: 2%;font-size: 18">至</td>
									<td style="text-align: left;width: 16%;font-size: 18" id="en">${end }</td>
									<td style="width: 8%;text-align: right;font-size: 18" >科室：</td>
									<td style="width: 15%;font-size: 18" id="deptName">${deptName }</td>
									<td style="width: 8%;font-size: 18; text-align: right;">类别：</td>
									<td style="width: 15%;font-size: 18" id="drugType">全部</td>
								</tr>
							</table>
						</div>
						<div data-options="region:'center',boeder:false" style="width: 100%;height: 80%">
							<table  id="list"
								data-options="fit:true,fitColumns:false,singleSelect:true,border:false">   
								<thead>   
									<tr>   
										<th data-options="field:'deptName',width:100">科室</th>   
										<th data-options="field:'pno',width:120">住院号</th>   
										<th data-options="field:'patientName',width:100,">患者姓名</th>
										<th data-options="field:'doctName',width:100">医生姓名</th>   
										<th data-options="field:'drugName',width:300">药品名称</th>   
										<th data-options="field:'drugSpec',width:100">规格</th>
										<th data-options="field:'drugPack',width:100">单位</th>   
										<th data-options="field:'num',width:100">数量</th>   
										<th data-options="field:'drugedDate',width:200,">取药日期</th>
<!-- 										<th data-options="field:'code',width:100">回收情况</th>    -->
										<th data-options="field:'name',width:100">经手人</th>
										<th data-options="field:'meark',width:200">备注</th>	
									</tr>   
								</thead>   
							</table>  
						</div>
					</div>
					<form id="saveForm" method="post"/>
				</div>
			</div>
		</div>
	</div>
</body>
</html>