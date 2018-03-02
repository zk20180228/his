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
	$(document).ready(function(){
		$('#accountType').combobox('setValue',2);
	    $('#accountType').combobox({  
	        onChange:function(newValue,oldValue){  
	        	$('#wagesAccount').textbox('setValue','');
	        }  
	    });  
	}); 
	function submit(flg) {
		var accountType = $('#accountType').combobox('getValue');
		var wagesAccount =$('#wagesAccount').textbox('getValue');
		if(wagesAccount == null || wagesAccount == ""){
			$.messager.alert('提示','请输入查询账号');
			return false;
		}
		var weagesPassword = $('#weagesPassword').val();
		if(weagesPassword == null || weagesPassword == ""){
			$.messager.alert('提示','密码为空,如果未设置查询密码,请前往个人中心设置工资查询密码!');
			return false;
		}
		$('#editForm').form('submit', {
			url : "<c:url value='/oa/Wages/loginWages.action'/>",
			success : function(data) {
				var message = eval("("+data+")");
				if(message.resCode=='error'){
					$.messager.alert('提示',message.resMsg);
					return false;
				}else{
					 $("#dlg" ).css("display", "none"); 
					 $("#divObj").show();
					 $('#list').datagrid({
						url:'<%=basePath%>oa/Wages/listWagesPersonQuery.action',
						queryParams:{
							wagesAccount:wagesAccount,
							accountType:accountType
						},
						fit:true,
						async: false,
						method:'post',
						singleSelect:true,
						remoteSort:false,
						pagination:true,
			        	pageSize:20,
			        	pageList:[10,20,40,50],
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
				}
			},
			error : function(data) {
			}
		});
	}
	//修改密码
	function btnModify(){
		Adddilog("修改密码","<%=basePath %>oa/Wages/updatePwdList.action");
	}
	/**
	 * 加载dialog
	 */
	function Adddilog(title, url) {
		$('#add').dialog({    
		    title: title,    
		    width: '500px',    
		    height:'190px',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
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
	function searchFrom() {
		var Stime = $('#Stime').val();
		 $('#list').datagrid({
				url:'<%=basePath%>oa/Wages/queryInfoByTime.action',
				queryParams:{
					wagesTime: Stime,
				}
		 })
	}
	function clears(){
		$('#Stime').val('');
		var Stime = $('#Stime').val();
		 $('#list').datagrid({
				url:'<%=basePath%>oa/Wages/queryInfoByTime.action',
				queryParams:{
					wagesTime: Stime,
				}
		 })
	}
</script>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" fit="true">
		 <div data-options="region:'center'">
			 <div id ="dlg" style="width:100%;text-align:center">
				 <form id="editForm" method="post">
						<center>
							<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:35%;padding:5px;margin-top:50px">
								<tr>
									<td class="honry-lable" style="width:60px;height:60px;font-size:14px;">说明：</td>
									<td style="width:80px;font-size:14px;">
										<p>1、如您为第一次登录,请前往个人中心设置查询密码</p>
										<p style="margin-top:10px">2、查询账号为个人的身份证号或工资账号</p>
									</td>
								<tr>
									<td class="honry-lable" style="width:60px;height:50px;font-size:14px;">账号：</td>
									<td >
										<input class="easyui-combobox" id="accountType" name="accountType" editable='false' style="width:80px;height:25px"
						                   data-options="valueField: 'value',textField: 'label',data: [{
						                   label: '身份证',
						                   value: '1',},
						                   {label: '工资号',
						                   value: '2',
						                   selected:true},]"                  
						                 />
										<input id="wagesAccount" name ="wagesAccount" class="easyui-textbox" style="width:180px;height:25px" prompt="请输入身份证号或工资号">
									</td>
									
								</tr>
								<tr>
									<td class="honry-lable" style="width:60px;height:50px;font-size:14px;">密码：</td>
									<td >
										<input id="weagesPassword"  name ="weagesPassword" class="easyui-textbox"  type="password"  style = "width:270px;height:25px">
									</td>
								</tr>
							</table>
						</center>
						 <div style="text-align:center;padding:5px;margin-top:5px">
					    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确&nbsp;定&nbsp;</a>
							&nbsp;&nbsp;
					    </div>
		    		</form>
			 </div>
			 <div id="divObj" style="display:none;width:100%;height: 100%">  
				 <div data-options="region:'north',border:false"style="height: 5%; padding-bottom: 5px;"class="dosageViewSize">
					<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
						<tr >
							<td style="width:80px;" align="left">工资月份:</td>
							<td style="width:150px;">
								<input id="Stime" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker({dateFmt:'yyyy-MM'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td >
							<td>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left:8px">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',border:false" style="height: 95%">
	     		 	<table class="easyui-datagrid"  id="list" style="">
						<thead data-options="frozen:true">
								<th data-options="field:'wagesAccount',width:'8%',align:'center'">工资号</th>
								<th data-options="field:'name',width:'8%',align:'center'">姓名</th>
								<th data-options="field:'wagesTime',width:'8%',align:'center',formatter:formatTime">工资月份</th>
	<!-- 							<th data-options="field:'wagesTime',width:'8%',align:'center'">工资月份</th> -->
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
								<th data-options="field:'providentFundAccount',width:'8%',align:'center'">公积金账号</th>
								<th data-options="field:'IDCard',width:'8%',align:'center'">身份证号码</th>
							</tr>
						</thead>
					</table>
				</div>
				<div id="add"></div>
     		 </div>  
		 </div>  
	</div>
</body>
</html>
