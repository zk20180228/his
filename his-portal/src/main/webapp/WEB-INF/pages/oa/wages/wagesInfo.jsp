<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工资管理</title>
<%@ include file="/common/metas.jsp"%>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
</style>
<script type="text/javascript">
	$(function(){
		//加载list数据信息
		$('#list').datagrid({
			url:'<%=basePath%>oa/Wages/listWagesQuery.action',
			async: false,
			method:'post',
			singleSelect:true,
			remoteSort:false,
			pagination:true,
        	pageSize:200,
        	pageList:[200,300,400,500],
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
		})
	})
	function searchFrom() {
		var Stime = $('#Stime').val();
		var wagesAccount = $('#wagesAccount').textbox('getValue');
		var name = $('#name').textbox('getValue');
		$('#list').datagrid('load',{
			wagesTime: Stime,
			wagesAccount:wagesAccount,
			name:name
		});
	}
	function clears(){
		$('#wagesAccount').textbox('setValue','');
		$('#name').textbox('setValue','');
		$('#Stime').val('');
		
		$('#list').datagrid('load',{
			wagesTime: '',
			wagesAccount:'',
			name:''
		});
	}
	function upload(){
		$('#uploadExcel').window('open');
	}
	function closeSaveWondowFeed(){
		$('#wagesTime').val('');
		var file=document.getElementById("fileFileName");   
		file.outerHTML=file.outerHTML   
		$('#uploadExcel').window('close');
	}
	function onCheckESIGN(filePicker) {
		var fName = document.getElementById("fileFileName").value;
		$("#copyEsign").hide();
		$("#hesign").val(fName);
		$("#hesign").show();
		if (fName != null && fName != "") {
			var ftype = fName.toLowerCase().split(".");
			var fTypeName = ftype[ftype.length - 1];
			if (!fTypeName == '') {
				if (fTypeName != "xls" && fTypeName != "xlsx") {
					$.messager.alert('提示',"上传的文件格式不正确，请选xls,xlsx格式的文件上传！");
					close_alert();
					checkFlag = "esignPatternNo";//提交时验证代表格式不对
				} else {
					if (filePicker.files[0].size > 5 * 1024 * 1024) {
						$.messager.alert('提示',"上传的文件大小不能大于5M，请重新上传！");
						close_alert();
						checkFlag = "esignSizeNo";//提交时验证代表大小不对
					}else{
						checkFlag = "";
					}
				}

			} else {
				$.messager.alert('提示',"上传的文件格式不正确，请选xls,xlsx格式的文件上传！");
				close_alert();
				checkFlag = "esignPatternNo";//提交时验证代表格式不对
			}
		}

	}
	function submit(flg) {
		var wagesTime =$('#wagesTime').val();
		if(wagesTime == null || wagesTime == ""){
			$.messager.alert('提示','请选择工资月份');
			return false;
		}
		var fName = $("#fileFileName").val();
		if(fName == null || fName == ""){
			$.messager.alert('提示','上传文件不能为空');
			return false;
		}
		$.messager.progress({text:'导入中，请稍后...',modal:true});
		$('#editForm').form('submit', {
			url : "<c:url value='/oa/Wages/saveOrUpdateEmployee.action'/>?wagesTime="+wagesTime,
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
				if(checkFlag == "esignPatternNo"){
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '上传文件格式不正确,不能提交表单!'
					});
					return false;
				}
				if(checkFlag == "esignSizeNo"){
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '上传文件大小不正确,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				var message =  eval("("+data+")");
				$.messager.alert('提示',message.resMsg);
				$('#wagesTime').val('');
				var file=document.getElementById("fileFileName");   
				file.outerHTML=file.outerHTML  
				$('#uploadExcel').window('close');
				$('#list').datagrid('reload');
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','导入失败！');
			}
		});
	}
	/**
	* 导出方法
	*/
	function exportList(){
		var times = $('#Stime').val();
		var account= $('#wagesAccount').textbox('getValue');
		var name=$('#name').textbox('getValue');
		if(times != ''){
			var rows = $('#list').datagrid('getRows');
			if(rows.length > 0){
				$('#exeReportTime').val(times);
				$('#exeReportAccount').val(account);
				$('#exeReportName').val(name);
				$('#exportJson').val(JSON.stringify(rows));
				$('#exportForm').form('submit', {
					url :"<%=basePath%>oa/Wages/exportList.action",
					onSubmit : function(param) {
					},
					success : function(data) {
						$.messager.alert("操作提示", "导出成功！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "导出失败！", "error");
					}
				});
			}else{
				$.messager.alert("提示","当前统计无数据，无法打印！");
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
		}else{
			$.messager.alert("提示","请先选择查询日期！");
			close_alert();
			return;
		}
	}
	function formatTime(value,row,index){
		if(value!=null){
			var strs= new Array();  
			strs=value.split("-"); 
			return strs[0]+"-"+strs[1];
		}else{
			return "";
		}
	}
</script>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" fit="true">
	 	 <div data-options="region:'north',border:false"style="height: 50px; padding-bottom: 5px;"class="dosageViewSize">
				<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
					<tr >
						<!-- 开始时间 --> 
						<td style="width:80px;" align="left">工资月份:</td>
						<td style="width:150px;">
							<input id="Stime" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td >
						<td style="width:250px;">&nbsp;工资号:&nbsp;<input id="wagesAccount" name="wagesAccount" class="easyui-textbox" style="width:150px"> </td>
						<td style="width:280px;">
							&nbsp;身份证号:&nbsp;<input id="name" class="easyui-textbox" style="width:150px"></td>
						<td>
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
<!-- 						<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" iconCls="icon-down">导出</a> -->
						<a href="javascript:void(0)" onclick="upload()" class="easyui-linkbutton" iconCls="icon-down">导入Excel工资数据</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false">
			<div id="tableShow" style="width: 100%; height: 100%;">
				<table class="easyui-datagrid"  id="list" style="" data-options="fit:true">
					<thead data-options="frozen:true">
							<th data-options="field:'wagesAccount',width:'8%',align:'center'">工资号</th>
							<th data-options="field:'name',width:'8%',align:'center'">姓名</th>
							<th data-options="field:'wagesTime',width:'8%',align:'center',formatter:formatTime">工资月份</th>
					</thead>
					<thead>  
						<tr>
							<th data-options="field:'deptName',width:'8%',align:'center'">部门</th>
							<th data-options="field:'category',width:'8%',align:'center'">人员类别</th>
							<th data-options="field:'postPay',width:'8%',align:'center'">岗位工资</th>
							<th data-options="field:'basePay',width:'8%',align:'center'">薪级工资</th>
							<th data-options="field:'nursinTeach',width:'8%',align:'center'">救护10</th>
							<th data-options="field:'achievements',width:'8%',align:'center'">绩效</th>
							<th data-options="field:'nursinTeaching',width:'8%',align:'center'">救护</th>
							<th data-options="field:'keepThink',width:'8%',align:'center'">保留项</th>
							<th data-options="field:'healthAllowance',width:'8%',align:'center'">卫津</th>
							<th data-options="field:'onlyChildFee',width:'8%',align:'center'">独子费</th>
							<th data-options="field:'hygieneFee',width:'8%',align:'center'">卫生费</th>
							<th data-options="field:'PHDFee',width:'8%',align:'center'">博导贴</th>
							<th data-options="field:'subsidyFee',width:'8%',align:'center'">补工资</th>
							<th data-options="field:'increased',width:'8%',align:'center'">预增资201410</th>
							<th data-options="field:'increasing',width:'8%',align:'center'">预增资20160607</th>
							<th data-options="field:'totalShould',width:'8%',align:'center'">应发合计</th>
							<th data-options="field:'deductRent',width:'8%',align:'center'">扣房费</th>
							<th data-options="field:'housingFund',width:'8%',align:'center'">公积金</th>
							<th data-options="field:'boardingFee',width:'8%',align:'center'">扣托费</th>
							<th data-options="field:'medicalInsurance',width:'8%',align:'center'">医疗保险</th>
							<th data-options="field:'overallPlanning',width:'8%',align:'center'">统筹金</th>
							<th data-options="field:'unemploymentInsurance',width:'8%',align:'center'">失业险</th>
							<th data-options="field:'deductWages',width:'8%',align:'center'">扣工资</th>
							<th data-options="field:'heatingCosts',width:'8%',align:'center'">代扣暖气费</th>
							<th data-options="field:'accountEeceivable',width:'8%',align:'center'">收账</th>
							<th data-options="field:'totalActual',width:'8%',align:'center'">实发工资</th>
							<th data-options="field:'providentFundAccount',width:'10%',align:'center'">公积金账号</th>
							<th data-options="field:'IDCard',width:'10%',align:'center'">身份证号码</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="uploadExcel" class="easyui-window" title="Excel工资数据导入" data-options="modal:true,closed:true,iconCls:'icon-save',collapsible:false,closable:false,minimizable:false,maximizable:false" style="width:500px;height:200px;">
				<form id="editForm" method="post" enctype="multipart/form-data">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px">
						<tr>
							<td class="honry-lable" style="width:80px;font-size:14px;">工资月份：</td>
							<td id = "selfPaidAmount">
								<input id="wagesTime" class="Wdate" type="text" name="Wtime" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" >选择导入文件</td>
							<td style="text-align: left;"  colspan="3">
								<input type="file" name="fileEsign" id="fileFileName"
								onChange="onCheckESIGN(this)">
								<img id="copyEsign" alt="" onclick="showEsign('${employee.esign }');"> 
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width:80px;font-size:14px;">说明：</td>
							<td>
								<p>1、请导入.xls、.xlsx格式的文件;</p>
								<p style="margin-top:10px">2、工资号和姓名不能为空，为空则不能导入;</p>
							</td>
						</tr>
					</table>
					 <div style="text-align:center;padding:5px">
				    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">导入</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeSaveWondowFeed()" data-options="iconCls:'icon-cancel'">关闭</a>
				    	
				    </div>
	    		</form>
			
			</div>
			<div id="uploadExcel" class="easyui-window" title="Excel工资数据导入" data-options="modal:true,closed:true,iconCls:'icon-save',collapsible:false,closable:false,minimizable:false,maximizable:false" style="width:700px;height:400px;">
			</div>			
			<form id="exportForm" method="post">
				<input type="hidden" id="exeReportTime" name="eReportTime" /> 
				<input type="hidden" id="exeReportAccount" name="exeReportAccount" /> 
				<input type="hidden" id="exeReportName" name="exeReportName" /> 
				<input type="hidden" id="exportJson" name="exportJson">
			</form>

		</div>
	 </div>  
</body>
</html>
