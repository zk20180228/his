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
<title>门诊账户管理</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel .panel-header .panel-tool a{
    background-color: red;	
}

.tableCss {
	border-collapse: collapse;
	border-spacing: 0;
	border-left: 1px solid #95b8e7;
	border-top: 1px solid #95b8e7;
}

.tableLabel {
	text-align: right;
	width: 150px;
}

.tableCss td {
	border-right: 1px solid #95b8e7;
	border-bottom: 1px solid #95b8e7;
	padding: 5px 15px;
	word-break: keep-all;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
		var payTypeMap=new Map();
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=payway",
			type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					payTypeMap.put(type[i].encode,type[i].name);
				}
			}
		});
		var operTypeMap=new Map();
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=opertype",
			type:'post',
			success: function(data) {
				var otype = data;
				for(var i=0;i<otype.length;i++){
					operTypeMap.put(otype[i].encode,otype[i].name);
				}
			}
		});
	</script>
</head>
<body>

	<div class="easyui-layout" style="width: 100%; height: 100%;">
		<div data-options="region:'north',border:false" style="height: 130;">
			<div style="text-align: left; padding: 5px;">
				<shiro:hasPermission name="${menuAlias}:function:query">
					<span style="margin: 0 15 0 5">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
						<input type="hidden" id="accountView_card_no">
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:receivables">
					<span style="margin: 0 15 0 0"> <a id="save"
						onclick="save()" class="easyui-linkbutton"
						data-options="iconCls:'icon-cunyujiaojin',disabled:'true'">存预交金</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:disable">
					<span style="margin: 0 15 0 0"> <a id="stop"
						onclick="stop()" class="easyui-linkbutton"
						data-options="iconCls:'icon-02',disabled:'true'">停用账户</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:enable">
					<span style="margin: 0 15 0 0"> <a id="start"
						onclick="start()" class="easyui-linkbutton"
						data-options="iconCls:'icon-2012080412263',disabled:'true'">重启账户</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:cancellation">
					<span style="margin: 0 15 0 0"> <a id="zhuxiao"
						onclick="zhuxiao()" class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',disabled:'true'">注销账号</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:clean">
					<span style="margin: 0 15 0 0"> <a id="jieqing"
						onclick="jieqing()" class="easyui-linkbutton"
						data-options="iconCls:'icon-bin_empty',disabled:'true'">结清余额</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:return">
					<span style="margin: 0 15 0 0"> <a id="return"
						onclick="funreturn()" class="easyui-linkbutton"
						data-options="iconCls:'icon-back',disabled:'true'">退预交金</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:make">
					<span style="margin: 0 15 0 0"> <a id="make"
						onclick="make()" class="easyui-linkbutton"
						data-options="iconCls:'icon-reprint',disabled:'true'">补打</a>
					</span>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:add">
					<span style="margin: 0 15 0 0"> <a id="create"
						onclick="create()" class="easyui-linkbutton"
						data-options="iconCls:'icon-add',disabled:'true'">创建账户</a>
					</span>
				</shiro:hasPermission>
				<span style="margin: 0 15 0 0"> <a id="pwd" onclick="pwd()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',disabled:'true'">修改密码</a>
				</span> <span style="margin: 0 15 0 0"> <a id="dayLimit"
					onclick="updayLimit()" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',disabled:'true'">修改当日消费限额</a>
				</span>
			</div>
			<!--患者信息************************************************************************************************ -->
			<div style="height: 90;">
				<table id="table" class="tableCss" style="width: 100%; height: 100%">
					<tr style="display: none">
						<td class="tableLabel">就诊卡号：</td>
						<td style="width: 200px;"><input class="easyui-textbox"
							id="idcardNo" name="idcardNo" style="width: 200" /></td>
						<td class="tableLabel">病历号：</td>
						<td><input id="blh" name="blhString" /></td>
					</tr>
					<tr>
						<td class="tableLabel">就诊卡号：</td>
						<td style="width: 200px;"><input class="easyui-textbox"
							id="patientCard" name="" data-options="required:true"
							missingMessage="请输入卡号" style="width: 200" /></td>
						<td class="tableLabel">姓名：</td>
						<td id="patientName"></td>
						<td class="tableLabel">性别：</td>
						<td id="patientSex"></td>
						<td class="tableLabel">证件类型：</td>
						<td id="patientCertificatestype"></td>
					</tr>
					<tr>
						<td class="tableLabel">电话：</td>
						<td id="patientPhone"></td>
						<td class="tableLabel">出生日期：</td>
						<td id="patientBirthday"></td>
						<td class="tableLabel">民族：</td>
						<td id="patientNation"></td>
						<td class="tableLabel">证件号：</td>
						<td id="patientCertificatesno"></td>
					</tr>
					<tr>
						<td class="tableLabel">医保号：</td>
						<td id='patientHandbook'></td>
						<td class="tableLabel">单日消费限额（/元）：</td>
						<td id="accountDaylimit" style="text-align: right;"></td>
						<td class="tableLabel"><span style="font-weight: bold;">当前余额（/元）：</span>
						</td>
						<td id="accountBalance" style="text-align: right;"></td>
						<td class="tableLabel">籍贯：</td>
						<td id='patientNativeplace'></td>
					</tr>
				</table>
			</div>
		</div>

		<!--账户信息stop************************************************************************************************ -->
		<div data-options="region:'center',border:false" style="height: 80%;">
			<div id="tt" class="easyui-tabs" data-options="fit:true">
				<div title="预存金">
					<table id="data_prestore"></table>
				</div>
				<div title="历史预存金">
					<table id="data_history"></table>
				</div>
				<div title="患者账户明细">
					<table id="data_iled"></table>
				</div>
				<div title="就诊卡信息">
					<table id="data_idcard"></table>
				</div>
			</div>
<!--患者信息************************************************************************************************ -->
		<div style="height: 90;">
			<table id="table" class="tableCss" style="width: 100%;height: 100%">
				<tr style="display: none">
					<td class="tableLabel">就诊卡号：</td>
					<td style="width: 200px;"><input class="easyui-textbox"
						id="idcardNo" name="idcardNo"
						style="width:200"/></td>
					<td class="tableLabel">病历号：</td>
					<td><input id="blh" name="blhString"/></td>
				</tr>
				<tr>
					<td class="tableLabel">就诊卡号：</td>
					<td style="width: 200px;"><input class="easyui-textbox"
						id="patientCard" name="" data-options="required:true"
						missingMessage="请输入卡号" style="width:200"/></td>
					<td class="tableLabel">姓名：</td>
					<td id="patientName"></td>
					<td class="tableLabel">性别：</td>
					<td id="patientSex"></td>
					<td class="tableLabel">证件类型：</td>
					<td id="patientCertificatestype"></td>
				</tr>
				<tr>
					<td class="tableLabel">电话：</td>
					<td id="patientPhone"></td>
					<td class="tableLabel">出生日期：</td>
					<td id="patientBirthday"></td>
					<td class="tableLabel">民族：</td>
					<td id="patientNation"></td>
					<td class="tableLabel">证件号：</td>
					<td id="patientCertificatesno"></td>
				</tr>
				<tr>
					<td class="tableLabel">医保号：</td>
					<td id='patientHandbook'></td>
					<td class="tableLabel">单日消费限额（/元）：</td>
					<td id="accountDaylimit" style="text-align:right;"></td>
					<td class="tableLabel">
						<span style="font-weight: bold;">当前余额（/元）：</span>
					</td>
					<td id="accountBalance" style="text-align:right;"></td>
					<td class="tableLabel">籍贯：</td>
					<td id='patientNativeplace'></td>
				</tr>
			</table>	
		</div>
    </div>
		<!-- 	dialog弹框 -->
		<div id="dd"></div>

		<!--修改当日消费限额弹框 -->
		<div id="windayLimit" class="easyui-window" title="当日消费限额"
			style="width: 500; height: 200; padding: 5"
			data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true">
			<form id="dayLimitForm" method="post">
				<table class="honry-table" cellpadding="0" cellspacing="0"
					border="0">
					<tr style="display: none">
						<td class="honry-lable">menuAlias：</td>
						<td class="honry-info"><input type="text" name="menuAlias"
							value="${menuAlias}" /></td>
					</tr>
					<tr style="display: none">
						<td class="honry-lable">就诊卡号：</td>
						<td class="honry-info"><input class="easyui-textbox"
							id="w_idcardNo" name="idcardNo" data-options="readonly:'true' "
							style="width: 200" /></td>
					</tr>
					<tr>
						<td class="honry-lable">单日消费限额：</td>
						<td class="honry-info"><input class="easyui-numberbox"
							id="w_daylimit" name="account.accountDaylimit"
							data-options="precision:2,required:true"
							missingMessage="请填写消费限额！" style="width: 200" /></td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:dayLimitSub();" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a> <a
						href="javascript:closeWin();" class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel'">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
//民族
var nationList = "";
//证件类型
var certiList="";
//预存金开户银行
var bankList="";
//就诊卡类型
var idcardTypeList="";
//科室map
var deptMap=null;
//人员map
var employeeMap=null;
//全局变量 用于标识账户的查询方式 1 通过就诊卡   0 通过病历号
var idcardQuery;
//性别
var sexMap=new Map();

	/*******************************开始读卡***********************************************/
	function read_card_ic()
	{
		var card_value=app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value=='')
		{
			alert_autoClose('友情提示','此卡号['+card_value+']无效','info');
			return;
		}
		$("#idcardNo").textbox("setValue",card_value);
		$("#patientCard").textbox("setValue",card_value);
		reader();
	}
	/*******************************结束读卡***********************************************/

$(function(){
	
	funformat();
	funDataGrid();	
	fundisableTab();
	
	bindEnterEvent('patientCard',reader,"easyui");
});

/** 读卡 获取患者信息 **/
function reader(){
	var card=$.trim($('#patientCard').textbox('getValue'));
	var blh=$('#blh').val();
	clearData();
	clearDataGrid();
	fundisabled();
	fundisableTab();
	if(card){
		$.ajax({
			url:'<%=basePath%>finance/outAccount/queryPatient.action',
			type:'post',
			data:{'idcardNo':card,'blhString':blh},
			success:function(data){
				//判断就诊卡状态   失效
				if(data=="invalid"){
					alert_autoClose('友情提示','就诊卡已失效','info');
					return;
				}
				//判断就诊卡状态   停用
				if(data=="stop"){
					alert_autoClose('友情提示','就诊卡已停用或挂失','info');
					return;
				}
				if($.isEmptyObject(data.patient)){
					alert_autoClose('友情提示','请输入正确的就诊卡号','info');
				}else{
					$('#idcardNo').textbox('setValue',card);
					idcardQuery=data.idcardQuery;
					funaccount(data);
				}
			}
		});
	}else{
		alert_autoClose('友情提示','请输入就诊卡号','info');
	}
}

/**根据患者账户状态，响应不同事件***/
function funaccount(data){
	showData(data.patient,data.account);
	//判断是否存在账户信息，并且判断账状态；0停用 1正常 2注销 ；默认：1
	if(data.account){
		//患者信息存在，但是是通过病历号查询所得，即患者就诊卡发生过更改
		if(data.account.accountState==0){
			alert_autoClose('友情提示','该账户已停用！','info');
			//TODO
			$('#start').linkbutton('enable');
		}else if(data.account.accountState==2){
				alert_autoClose('友情提示','该账户已注销！','info');
				//TODO
				$('#start').linkbutton('enable');
			}else if(data.account.accountState==0){
				alert_autoClose('友情提示','该账户已停用！','info');
				//TODO
				$('#start').linkbutton('enable');
			}else{
				funenable();
				funenableTab();
				tabSelected(data.account);
			}
	}else{
		$.messager.confirm('确认','该患者还没有账户，是否为该患者创建账户？',function(r){    
		    if (r){ 
		    	$('#create').linkbutton('enable');
		    	create();
		    }
		    $('#create').linkbutton('enable');
		}); 
	}
}

//为tab面板绑定事件
function tabSelected(account){
	var tab = $('#tt').tabs('getSelected');
	var index = $('#tt').tabs('getTabIndex',tab);
	funtabData(account,index);
	
	$('#tt').tabs({    
	    border:false,
	    onSelect:function(title,index){
	    	funtabData(account,index);
	    }
	}); 	
}

/**根据面板，加载相应信息**/
function funtabData(account,index){
	if(index==0){
		$('#data_prestore').datagrid({
			url:'<%=basePath%>finance/outAccount/queryPrestore.action?menuAlias=${menuAlias}',
			queryParams:{'account.id':account.id,'ishistory':0}
		});	
	}else if(index==1){
		$('#data_history').datagrid({
			url:'<%=basePath%>finance/outAccount/queryPrestore.action?menuAlias=${menuAlias}',
			queryParams:{'account.id':account.id,'ishistory':1 }
		});	
	}else if(index==2){
		$('#data_iled').datagrid({
			url:'<%=basePath%>finance/outAccount/queryDatailed.action?menuAlias=${menuAlias}',
			queryParams:{'account.id':account.id }
		});	
	}else{
		var card=$('#idcardNo').textbox('getValue');
		$('#data_idcard').datagrid({
			border:false,
			url:'<%=basePath%>finance/outAccount/queryidcard.action?menuAlias=${menuAlias}',
			queryParams:{'idcardNo':card }
		});	
	}
}

/**初始化患者账户信息**/
function funDataGrid(){
	
	//初始化预存金
	$('#data_prestore').datagrid({
		rownumbers:true,idField: 'id',striped:true,pageSize:20,
			checkOnSelect:false,selectOnCheck:false,singleSelect:true,pagination:true,
			border:false,fit:true,
		columns:[[{field:'id',title:'ck',hidden:"true"},
		          {field:'receiptNo',title:'收据号',width:'10%'},
		          {field:'prepayCost',title:'充值金额（/元）',width:'10%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(2);}}},
		          {field:'prepayType',title:'支付方式',width:'10%',
		        	formatter:funPayTypeMap
		          },
		          {field:'openAccounts',title:'银行账号',width:'10%'},
		          {field:'openBank',title:'开户银行',width:'10%',
		        	formatter:bankFamater  
		          },
		          {field:'workName',title:'开户单位',width:'10%'},
		          {field:'prepayState',title:'预交金状态',width:'10%',
		        	formatter:function(value,row){
	  					if(value==0){
	  						return '返还';
		  				}else if(value==1){
		  					return '收取';
		  				}else {
		  					return '重打';
		  				}
		        	}
		          },
		          {field:'updateTime',title:'操作时间',width:'10%'}
		]],
		onBeforeLoad:function (param) {
			var card=$.trim($('#patientCard').textbox('getValue'));
			if(card==""){
				$('#data_prestore').datagrid('loadData',{total:0,rows:[]});
			}
        },
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
	
	//历史预存金
	$('#data_history').datagrid({
		rownumbers:true,idField: 'id',striped:true,border:true,pageSize:20,
		checkOnSelect:false,selectOnCheck:false,singleSelect:true,pagination:true,
		border:false,fit:true,
		columns:[[{field:'id',title:'ck',hidden:"true"},
		          {field:'receiptNo',title:'收据号',width:'10%'},
		          {field:'prepayCost',title:'充值金额（/元）',width:'10%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(2);}}},
		          {field:'prepayType',title:'支付方式',width:'10%',
		        	  formatter:funPayTypeMap  
		          },
		          {field:'openAccounts',title:'银行账号',width:'15%'},
		          {field:'openBank',title:'开户银行',width:'10%',
		        	  formatter:bankFamater
		          },
		          {field:'workName',title:'开户单位',width:'20%'},
		          {field:'updateTime',title:'操作时间',width:'10%'}
		]],
		onBeforeLoad:function (param) {
			var card=$.trim($('#patientCard').textbox('getValue'));
			if(card==""){
				$('#data_history').datagrid('loadData',{total:0,rows:[]});
			}
        },
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
	
	//患者账户明细
	$('#data_iled').datagrid({
		rownumbers:true,idField: 'id',striped:true,border:true,pageSize:20,pageNumber: 1,
		checkOnSelect:false,selectOnCheck:false,singleSelect:true,pagination:true,
		border:false,fit:true,
		columns:[[{field:'id',title:'ck',hidden:"true"},
		          {field:'opertype',title:'操作类型',width:'10%',
					formatter:funOperTypeMap
		          },
		          {field:'money',title:'交易金额（/元）',width:'10%',align:'right',formatter: function (value, row, index) {if (row != null && value!=null) { return parseFloat(value).toFixed(2);}}},
		          {field:'accountBalance',title:'交易后余额（/元）',width:'10%',align:'right',formatter: function (value, row, index) {if (row != null && value!=null) { return parseFloat(value).toFixed(2);}}},
		          {field:'deptCode',title:'相关科室',width:'10%',
		        	formatter:funDeptMap
		          },
		          {field:'operCode',title:'操作人员',width:'10%',
 		        	formatter:funEmployeeMap  
		          },
		          {field:'operDate',title:'操作时间',width:'10%'}
		]],
		onBeforeLoad:function (param) {
			var card=$.trim($('#patientCard').textbox('getValue'));
			if(card==""){
				$('#data_iled').datagrid('loadData',{total:0,rows:[]});
			}
        },
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
			if(data.total > 0){
				$('#accountBalance').text(data.rows[0].accountBalance.toFixed(2));	//当前余额
			}
		}
	});
	
	//就诊卡信息
	$('#data_idcard').datagrid({
		rownumbers:true,idField: 'id',striped:true,
		checkOnSelect:false,selectOnCheck:false,singleSelect:true,
 		border:false,//fit:true,
		columns:[[
		          {field:'patientName',title:'患者姓名',width:'15%',
		        	  formatter: function(value,row,index){
		  				if (row.patient){
		  					return row.patient.patientName;
		  				}
		  			}
		          },
		          {field:'idcardNo',title:'就诊卡号',width:'15%'},
		          {field:'medicalrecordId',title:'病历号',width:'15%',
		        	  formatter: function(value,row,index){
			  				if (row.patient){
			  					return row.patient.medicalrecordId;
			  				}
			  			}
		          },
		          {field:'idcardType',title:'卡类型',width:'15%',
		        	formatter:funidcardTypeList  
		          },
		          {field:'createTime',title:'建卡时间',width:'15%'},
		          {field:'createUser',title:'操作人员',width:'15%',
		        	  formatter:funEmployeeMap  
		          }
		]],
		onBeforeLoad:function (param) {
			var card=$.trim($('#patientCard').textbox('getValue'));
			if(card==""){
				$('#data_idcard').datagrid('loadData',{total:0,rows:[]});
			}
        },
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
}

//预交金列表刷新//其他列表也一起刷新
function prestoreReload(){
	$('#data_prestore').datagrid('reload');
	$('#data_history').datagrid('reload');
	$('#data_iled').datagrid('reload');
	$('#data_idcard').datagrid('reload');
}

/**创建用户**/
function create(){
	Adddilog('新建患者账户','<%=basePath%>finance/outAccount/accountAdd.action');    
}

/**	存预交金	***/
function save(){
	var card=$('#idcardNo').textbox('getValue');
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		success:function(data){
			if(data != 0){
				if($("#panelEast")){
					$("#panelEast").remove();
				}					
				Adddilog('收纳预交金','<%=basePath%>finance/outAccount/checkout.action');    
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
}

/***
 * 停用账户
 * 账户状态：0停用 1正常 2注销 ；
 * 默认：1 
 */
function stop(){
	$.messager.confirm('确认','您确定要停用此账户吗？',function(r){    
	    if (r){    
	    	var card=$('#idcardNo').textbox('getValue');
	    	$.ajax({
				url:'<%=basePath%>finance/outAccount/updateAccount.action',
				type:'post',
				data:{'account.accountState':0,'idcardNo':card},
				success:function(data){
					if(data=='success'){
						alert_autoClose('友情提示','该账户已停用！','info');
						window.location.reload();
					}else{
						alert_autoClose('警告','发生未知错误','warning');
					}
				}
			});
	    }    
	});
}

/** 重启账户**/
function start(){
	$.messager.confirm('确认','您确定要重启此账户吗？',function(r){    
	    if (r){    
	    	var card=$('#idcardNo').textbox('getValue');
	    	var blh=$('#blh').val();
	    	$.ajax({
				url:'<%=basePath%>finance/outAccount/updateAccount.action',
				data:{'idcardQuery':idcardQuery},
				type:'post',
				data:{'account.accountState':1,'idcardQuery':idcardQuery,'idcardNo':card,'blhString':blh},
				success:function(data){
					if(data=='success'){
						alert_autoClose('友情提示','该账户已启用！','info');
						reader();
					}else{
						alert_autoClose('警告','发生未知错误','warning');
					}
				}
			});
	    }    
	});
}

/**结清账户**/
function jieqing(){
	var card=$('#idcardNo').textbox('getValue');
	
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		success:function(data){
			//账户状态：0停用 1正常 2注销
			if(data != 0){
				$.ajax({
					url:'<%=basePath%>finance/outAccount/getAccountForcardNo.action',
					type:'post',
					data:{'idcardNo':card},
					success:function(data){
						$.messager.confirm('确认','该账户现有余额'+data.accountBalance.toFixed(2)+'元，您将要结清此账户吗？',function(r){   
						    if (r){   
						    	$.messager.progress({text:'结清中，请稍后...',modal:true});
						    	$.ajax({
						    		url:'<%=basePath%>finance/outAccount/settleAccount.action',
						    		type:'post',
						    		data:{'idcardNo':card},
						    		success:function(data){
						    			$.messager.progress('close');	
						    			if(data=='success'){
						    				$('#accountBalance').text(0.0);	//账户余额
						    				alert_autoClose('友情提示','该账户已结清！','info');
						    				prestoreReload();
						    			}
						    		}
						    	});
						    }    
						});
					}
				});
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
}
 
/**注销账户**/
function zhuxiao(){
	var card=$('#idcardNo').textbox('getValue');
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		success:function(data){
			//账户状态：0停用 1正常 2注销
			if(data != 0){
				$.ajax({
					url:'<%=basePath%>finance/outAccount/getAccountForcardNo.action',
					type:'post',
					data:{'idcardNo':card},
					success:function(data){
						$.messager.confirm('确认','该账户现有余额'+data.accountBalance.toFixed(2)+'元，您将要注销此账户吗？',function(r){   
						    if (r){   
						    	$.messager.progress({text:'注销中，请稍后...',modal:true});
						    	$.ajax({
						    		url:'<%=basePath%>finance/outAccount/logoutAccount.action',
						    		type:'post',
						    		data:{'account.accountState':2,'idcardNo':card},
						    		success:function(data){
						    			$.messager.progress('close');
						    			if(data=='success'){
						    				alert_autoClose('友情提示','该账户已注销,剩余预交金已返还','info');
						    			}
						    		}
						    	});
						    }    
						});
					}
				});
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
}

/**退预交金**/
function funreturn(){
	
	var card=$('#idcardNo').textbox('getValue');
	var pd = false;
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		async: false,
		success:function(data){
		//账户状态：0停用 1正常 2注销
			if(data != 0){
				pd = true;
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
	
	if(pd){
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		if(index==0){
			var row = $('#data_prestore').datagrid('getSelected');
			if(row){
				if(row.prepayState==1){
					$.ajax({
			    		url:'<%=basePath%>finance/outAccount/judgeMoney.action',
		 	    		type:'post',
		 	    		data:{'outprepay.id':row.id},
		 	    		success:function(data){
		 	    			if(data.resMsg=='success'){
		 	    				//退款方式
		 	    				var resType=data.resType;
	 	    					$.messager.confirm('确认',data.resCode,function(r){    
		 	    				    if (r){    
		 	    				    	$.ajax({
			    				    		url:'<%=basePath%>finance/outAccount/returnOutprepay.action',
		 	    				    		type:'post',
		 	    				    		data:{'outprepay.id':row.id,'outprepay.accountId':row.accountId,'resType':resType},
		 	    				    		success:function(data){
		 	    				    			if(data.resMsg == 'success'){
		 	    				    				$('#accountBalance').text(data.accountBalance.toFixed(2));
		 	    				    				$.messager.alert('友情提示','退款成功！'); 
		 	    				    				prestoreReload();
		 	    				    			}
		 	    				    		}
		 	    				    	});
		 	    				    }    
		 	    				});
		 	    			}
		 	    		}
		 	    	});
				}else{
					alert_autoClose('友情提示','只有收款状态的预交金可以进行退款！','info');
				}
			}else{
				alert_autoClose('友情提示','请在预存金列表中选择一条收款记录进行返还操作！','info');
			}
		}else{
			alert_autoClose('友情提示','请在预存金列表中选择一条收款记录进行返还操作！','info');
		}
	}
}
 
/**补打账户**/
function make(){
	var card=$('#idcardNo').textbox('getValue');
	var pd = false;
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		async: false,
		success:function(data){
		//账户状态：0停用 1正常 2注销
			if(data != 0){
				pd = true;
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
	if(pd){
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		if(index==0){
			var row = $('#data_prestore').datagrid('getSelected');
			if(row){
				if(row.prepayState!=0){
					$.messager.confirm('确认','确认补打预交金收据？',function(r){    
					    if (r){    
					    	$.ajax({
					    		url:'<%=basePath%>finance/outAccount/makeOutprepay.action',
					    		type:'post',
					    		data:{'outprepay.id':row.id},
					    		success:function(data){
					    			if(data=='success'){
					    				$.messager.confirm('确认','操作成功，是否打印预交金收据？',function(r){    
					            		    if (r){    
					            		    	  var timerStr = Math.random();
					    	        	          //门诊预交金票据
					    	        	  		  window.open ("<c:url value='/iReport/iReportPrint/iReportForOutpatientPrepaid.action?randomId='/>"+timerStr+"&tid="+$('#idcardNo').textbox('getValue')+"&fileName=outpatient_account",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
					            		    }    
					            		});
					    				prestoreReload();
					    			}
					    		}
					    	});
					    }    
					});
				}else{
					alert_autoClose('友情提示','只有收款状态的预交金可以进行补打操作！','info');
				}
			}else{
				alert_autoClose('友情提示','请在预存金列表中选择一条收款记录进行补打操作！','info');
			}
		}else{
			alert_autoClose('友情提示','请在预存金列表中选择一条收款记录进行补打操作！','info');
		}
	}
}

/**修改密码**/
function pwd(){
	var card=$('#idcardNo').textbox('getValue');
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		success:function(data){
		//账户状态：0停用 1正常 2注销
			if(data.resCode != 0){
				Adddilog('修改密码','<%=basePath%>finance/outAccount/editpwd.action?menuAlias='+'${menuAlias}');  
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
}

/** 显示患者信息（患者信息，账户信息）**/<%-- 当患者账户信息不存在是会 --%>
function showData(patient,account){
	$('#patientName').text(patient.patientName);	//姓名
	$('#patientSex').text(sexMap.get(patient.patientSex));		//性别
	$('#patientCertificatestype').text(certiFamater(patient.patientCertificatestype));	//证件类型
	$('#patientPhone').text(patient.patientPhone);	//电话
	$('#patientBirthday').text(patient.patientBirthday);	//出生日期
	$('#patientNation').text(nationFamater(patient.patientNation));	//民族
	$('#patientCertificatesno').text(patient.patientCertificatesno);	//证件号
	$('#patientHandbook').text(patient.patientHandbook);	//医保号
	if(account){
		$('#accountDaylimit').text(account.accountDaylimit.toFixed(2));	//单日消费限额
		$('#accountBalance').text(account.accountBalance.toFixed(2));	//账户余额
	}
	$('#patientNativeplace').text(patient.patientNativeplace);	//籍贯
}

/** 清除患者信息   ***/
function clearData(){
	$('#patientName').text('');		//姓名
	$('#patientSex').text('');		//性别
	$('#patientCertificatestype').text('');	//证件类型
	$('#patientPhone').text('');	//电话
	$('#patientBirthday').text('');	//出生日期
	$('#patientNation').text('');	//民族
	$('#patientCertificatesno').text('');	//证件号
	$('#patientHandbook').text('');	//医保号
	$('#accountDaylimit').text('');	//单日消费限额
	$('#patientNativeplace').text('');	//籍贯
	$('#accountBalance').text('');	//当前余额
}

/**启用全部按钮**/
function funenable(){
	$('#save').linkbutton('enable');
	$('#stop').linkbutton('enable');
	$('#start').linkbutton('disable');
	$('#zhuxiao').linkbutton('enable');
	$('#jieqing').linkbutton('enable');
	$('#return').linkbutton('enable');
	$('#make').linkbutton('enable');
	$('#pwd').linkbutton('enable');
	$('#dayLimit').linkbutton('enable');
}
	
/**禁用全部按钮***/
function fundisabled(){
	$('#save').linkbutton('disable');
	$('#stop').linkbutton('disable');
	$('#start').linkbutton('disable');
	$('#zhuxiao').linkbutton('disable');
	$('#jieqing').linkbutton('disable');
	$('#return').linkbutton('disable');
	$('#make').linkbutton('disable');
	$('#pwd').linkbutton('disable');
	$('#dayLimit').linkbutton('disable');
	$('#create').linkbutton('disable');
}

/**禁用全部选项卡***/
function fundisableTab(){
	$('#tt').tabs('disableTab', 0);
	$('#tt').tabs('disableTab', 1);
	$('#tt').tabs('disableTab', 2);
	$('#tt').tabs('disableTab', 3);
}

/**启用全部选项卡***/
function funenableTab(){
	$('#tt').tabs('enableTab', 0);            
	$('#tt').tabs('enableTab', 1);            
	$('#tt').tabs('enableTab', 2);            
	$('#tt').tabs('enableTab', 3);            
}

/** 清除DataGrid数据**/
function clearDataGrid(){
	$('#data_prestore').datagrid('load',{
		'account.id':'0',
		'ishistory':'0'
	});	
	$('#data_history').datagrid('load',{
		'account.id':'0',
		'ishistory':'0'
	});	
	$('#data_iled').datagrid('load',{
		'account.id':'0'
	});	
	$('#data_idcard').datagrid('load',{
		'idcardNo':'0'
	});	
}


<%----------------------------------------  渲染        -------------------------------------------------------------------------------------------------------%>
function funformat(){
	$.ajax({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		data:{"type" : "nationality"},
		type:'post',
		success: function(nationdata) {
			nationList = nationdata ;
		}
	});	
	$.ajax({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		data:{"type" : "certificate"},
		type:'post',
		success: function(certidata) {
			certiList = certidata ;
		}
	});
	$.ajax({
		url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		data:{"type" : "bank"},
		type:'post',
		success: function(codeBank) {
			bankList =  codeBank ;
		}
	});
	
	$.ajax({
		url:'<%=basePath%>baseinfo/department/getDeptMap.action',
		type:'post',
		success: function(data) {
			deptMap = data;
		}
	});
	
	$.ajax({
		url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
		type:'post',
		success: function(data) {
			employeeMap = data;
		}
	});
	 //卡类型
    $.ajax({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		data:{"type" : "idcardType"},
		type:'post',
		success: function(idcardTypedata) {
			idcardTypeList =  idcardTypedata ;
		}
	});
    
  ///性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

}

//渲染支付方式
function funPayTypeMap(value,row,index){
	if(value){
		return payTypeMap.get(value);
	}
}

//渲染操作类型
function funOperTypeMap(value,row,index){
	return operTypeMap.get(value);
}
	
// 民族列表页 显示
function nationFamater(value){
	if(value!=null){
		for(var i=0;i<nationList.length;i++){
			if(value==nationList[i].encode){
				return nationList[i].name;
			}
		}
	}	
}

// 证件类型列表页 显示
function certiFamater(value){
	if(value!=null){
		for(var i=0;i<certiList.length;i++){
			if(value==certiList[i].encode){
				return certiList[i].name;
			}
		}
	}
}	

//银行
function bankFamater(value){
	if(value!=null){
		for(var i=0;i<bankList.length;i++){
			if(value==bankList[i].encode){
				return bankList[i].name;
			}
		}
	}
}

//就诊卡类型
function funidcardTypeList(value){
	if(value!=null){
		for(var i=0;i<idcardTypeList.length;i++){
			if(value==idcardTypeList[i].encode){
				return idcardTypeList[i].name;
			}
		}
	}
}
	
//渲染科室Map
function funDeptMap(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}

//渲染人员map
function funEmployeeMap(value,row,index){
	if(value!=null&&value!=''){
		return employeeMap[value];
	}
}

function Adddilog(title, url) {
	$('#dd').dialog({    
	    title: title,    
	    width: '40%',    
	    height:'40%',
	    closed:false,
	    cache: false,    
	    href: url,    
	    modal: true   
   });    
}

//打开dialog
function openDialog() {
	$('#dd').dialog('open'); 
}
//关闭dialog
function closeDialog() {
	$('#dd').dialog('close');  
}


<%-------------------------------------------------------------修改当日消费限额-----------------------------------------------------------------------------%>

//开启windayLimit
function updayLimit(){
	var card=$('#idcardNo').textbox('getValue');
	$.ajax({
		url:'<%=basePath%>finance/outAccount/accounState.action',
		type:'post',
		data:{'idcardNo':card},
		success:function(data){
			if(data != 0){
				$('#windayLimit').window('open');
				$('#windayLimit').window('center');
				$('#w_idcardNo').textbox('setValue',card);
			}else{
				alert_autoClose('友情提示','该账户已停用，此操作不可进行！','info');
				fundisabled();
				$('#start').linkbutton('enable');
			}
		}
	});
}

//关闭windayLimit
function closeWin(){
	$('#dayLimitForm').form('clear');
	$('#windayLimit').window('close');
}

//提交修改限额信息
function dayLimitSub(){
	$('#dayLimitForm').form('submit',{  
		url:'<%=basePath%>finance/outAccount/updayLimit.action',
        onSubmit:function(){
			if (!$('#dayLimitForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交信息!'
				});
				return false;
			}
			$.messager.progress({text:'修改中，请稍后...',modal:true});
        },success:function(data){
        	$.messager.progress('close');	
        	data = $.parseJSON(data);
        	if(data.resMsg == "success"){
        		$.messager.alert('提示','修改成功!');	
        		$('#accountDaylimit').text(data.accountDaylimit.toFixed(2));
        		closeWin();
        	}
        },error : function(data) {
        	$.messager.progress('close');	
			$.messager.alert('提示','修改失败!');	
		}							         
    }); 
}
//设置$.messager.alert()的关闭时间
function alert_autoClose(title,msg,icon){ 
	 var interval; 
	 var time=1500; //设置时间
	 $.messager.alert(title,msg,icon,function(){}); 
	 interval=setInterval(fun,time); 
	    function fun(){ 
		    clearInterval(interval); 
		 	$(".messager-body").window('close');  
		}; 
	}
</script>
</body>
</html>