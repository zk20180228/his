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
<body>
	<div id="divLayout"  class="easyui-layout" data-options="fit:true" >
		 <div data-options="region:'north',border:false" style="height:63px;padding: 5px 5px 1px 5px;">
			<div>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(1);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias }:function:set">
				<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 5px" >重置</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:print">  
				<a href="javascript:void(0)" onclick="exper()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">
				<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(4);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">当年</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(5);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">当月</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(8);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">上月</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(7);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">十五天</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(3);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">七天</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(2);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">三天</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:searchFrom(6);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">当天</a>
				</shiro:hasPermission>
			</div>
			<div style="padding-top: 5px;">
				日期：
				<input id="STime" name="STime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/> 
				至
				<input id="ETime"  name="ETime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		       	药品名称：
				<input  class="easyui-combobox" style="width: 150px;" id="drug" name="drug" value="${drug}" />
				生产企业：
				<input  class="easyui-combobox" style="width: 150px;" id="company" name="company" value="${company}" />
				执行人员：
				<input  class="easyui-combobox" style="width: 100px;" id="user" name="user" value="${user}" />
				
			</div>
		</div>
		 <div data-options="region:'center',border:false" style="height:90%;padding: 5px 0px 0px 0px;width: 100%">
				<table id="countList"  data-options="fit:true,method:'post'">
					<thead>
						<tr>
							<th data-options="field:'tradeName'" width="14%" align="center">药品名称</th>
							<th data-options="field:'specs'" width="6%" align="center">规格</th>
							<th data-options="field:'packUnit'" width="3%" align="center">单位</th>
							<th data-options="field:'inNum'" width="7%"align="right" halign="center">入库数</th>
							<th data-options="field:'retailPrice',formatter:formatterCost" width="7%" align="right" halign="center">零售价</th>
							<th data-options="field:'purchasePrice',formatter:formatterCost" width="5%" align="right" halign="center">购进价</th>
							<th data-options="field:'purchaseCost',formatter:formatterCost" width="10%" align="right" halign="center">购进金额</th>
							<th data-options="field:'companyCode',formatter:function(value,row,index){
								if(value!=null&&value!=''){
									return comMap[value];
								}
							}" width="15%" align="center">生产企业</th>
							<th data-options="field:'batchNo'" width="7%" align="right" halign="center">批号</th>
							<th data-options="field:'validDate',formatter:function(value,row,index){
								if(value){
									return value.split(' ')[0];
								}else{
									return '';
								}
							}" width="11%" align="center">药品效期</th>
							<th data-options="field:'invoiceNo'" width="7%" align="right" halign="center" >发票号</th>
							<th data-options="field:'deliveryNo'" width="7%" align="right" halign="center" >票单号</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/>
		</div>
	</div>
	<script type="text/javascript">
var comMap="";
	function clear(){
		/**
		 * 渲染药品生产企业
		 * @author  qh
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2017-03-28
		 */
		$('#STime').val("${Stime}");
		$('#ETime').val("${Etime}");
		$('#drug').combobox('setValue',"${drug}");
		$('#user').combobox('setValue',"${user}");
		$('#company').combobox('setValue',"${company}");
		searchFrom(1);
	}
	
	$(function(){
		$.ajax({
			url: "<c:url value='/statistics/QueryInstore/companyMap.action'/>", 
			success: function(comData) {
				comMap = comData;
			}
		});
		$('#STime').val("${Stime}");
		$('#ETime').val("${Etime}");
           var winH=$("body").height();
		$('#countList').datagrid({
		});
		
		/**
		 * 药品下拉框
		 * @author  zpty
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2016-06-22
		 * @version 1.0
		 */
		$('#drug').combobox({    
				url :"<%=basePath %>statistics/QueryInstore/drugNameComboboxRegister.action",
				valueField : 'code',
				textField : 'name',
				multiple : false,
				onSelect: function(date){
					searchFrom(1);
				}
			});
		
		
		/**
		 * 生产企业下拉框
		 * @author  zpty
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2016-06-22
		 * @version 1.0
		 */
		$('#company').combobox({    
				url :"<%=basePath %>statistics/QueryInstore/companyNameComboboxRegister.action",
				valueField : 'Id',
				textField : 'companyName',
				multiple : false,
				onSelect: function(){
					searchFrom(1);
				}
			});
		
		
		/**
		 * 人员下拉框
		 * @author  zpty
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2016-06-22
		 * @version 1.0
		 */
		$('#user').combobox({    
				url :"<%=basePath %>statistics/QueryInstore/userNameComboboxRegister.action",
				valueField : 'id',
				textField : 'name',
				multiple : false,
				onSelect: function(){
					searchFrom(1);
				}
		});
		
		//加载数据表格
		$("#countList").datagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			rownumbers:true,
			fit:true,
			queryParams:{Stime:"${Stime}",Etime:"${Etime}",drug:null},
			url:'<%=basePath %>statistics/QueryInstore/queryInstore.action',
			onLoadSuccess:function(data){
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
	
     /**
		 * 查询
		 * @author wujiao
		 * @date 2015-08-31
		 * @version 1.0
		 */
		function searchFrom(flag) {
			var startTime=$('#STime').val();
			var endTime=$('#ETime').val();
			if(startTime&&endTime){
			    if(startTime>endTime){
			      $.messager.alert("提示","开始时间不能大于结束时间！");
			      close_alert();
			      return ;
			    }
			  }
			var Stime;//开始时间
			var Etime;//结束时间
			var year;
			var month;
			var day;
		 if(flag==1){
		 		Stime = $('#STime').val();
				Etime = $('#ETime').val();	
		 }else{
			  var date=new Date();
			  year=date.getFullYear();
			  month=date.getMonth()+1;
			  day=date.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  Etime=year+'-'+month+'-'+day;
			  if(flag==2){
				  var lon=date.getTime()-1000*3600*24*3;
				  Stime=new Date(lon);
				  year=Stime.getFullYear();
				  month=Stime.getMonth()+1;
				  day=Stime.getDate();
				  month=month<10?"0"+month:month;
				  day=day<10?"0"+day:day;
				  Stime=year+'-'+month+'-'+day; 
				  lon=date.getTime()-1000*3600*24*1;
				  Etime=new Date(lon);
				  year=Etime.getFullYear();
				  month=Etime.getMonth()+1;
				  day=Etime.getDate();
				  month=month<10?"0"+month:month;
				  day=day<10?"0"+day:day;
				  Etime=year+'-'+month+'-'+day; 
			  }else if(flag==3){
				  var lon=date.getTime()-1000*3600*24*7;
				  Stime=new Date(lon);
				  year=Stime.getFullYear();
				  month=Stime.getMonth()+1;
				  day=Stime.getDate();
				  month=month<10?"0"+month:month;
				  day=day<10?"0"+day:day;
				  Stime=year+'-'+month+'-'+day; 
				  lon=date.getTime()-1000*3600*24*1;
				  Etime=new Date(lon);
				  year=Etime.getFullYear();
				  month=Etime.getMonth()+1;
				  day=Etime.getDate();
				  month=month<10?"0"+month:month;
				  day=day<10?"0"+day:day;
				  Etime=year+'-'+month+'-'+day;
			  }else if(flag==4){
				  Stime=(date.getFullYear())+'-01-01';
			  }else if(flag==5){
				  Stime=year+'-'+month+'-01'
			  }else if(flag==7){
				  var lon=date.getTime()-1000*3600*24*15;
				  Stime=new Date(lon);
				  year=Stime.getFullYear();
				  month=Stime.getMonth()+1;
				  day=Stime.getDate();
				  month=month<10?"0"+month:month;
				  day=day<10?"0"+day:day;
				  Stime=year+'-'+month+'-'+day; 
				  lon=date.getTime()-1000*3600*24*1;
				  Etime=new Date(lon);
				  year=Etime.getFullYear();
				  month=Etime.getMonth()+1;
				  day=Etime.getDate();
				  month=month<10?"0"+month:month;
				  day=day<10?"0"+day:day;
				  Etime=year+'-'+month+'-'+day;
			  }else if(flag==8){
					var date=new Date();
					var year=date.getFullYear();
					var month=date.getMonth();
					if(month==0){
						year=year-1;
						month=12;
					}
					var startTime=year+'-'+month+'-01';
					$('#STime').val(startTime);
					Stime= startTime;
					var date=new Date(year,month,0);
					var endTime=year+'-'+month+'-'+date.getDate();
					$('#ETime').val(endTime);
					Etime=endTime;
				}else{
				  Stime=Etime;
			  }
			  $('#STime').val(Stime);
			  $('#ETime').val(Etime);
    	 }
			var drug = $('#drug').combobox('getValue');
			var company = $('#company').combobox('getValue');
			var user = $('#user').combobox('getValue');
			$('#countList').datagrid('load',{
				Stime: Stime,
				Etime: Etime,
				drug: drug,
				company:company,
				user:user
			});
		}
     
		//导出列表
		function exportList() {
			var Stime = $('#STime').val();
			var Etime = $('#ETime').val();
			var drug = $('#drug').combobox('getValue');
			var company = $('#company').combobox('getValue');
			var user = $('#user').combobox('getValue');
			var rows = $('#countList').datagrid('getRows');
			if(rows==null||rows==""){
				$.messager.alert("提示", "列表无数据,无法导出！");
				return;
			}
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {
						url :"<%=basePath%>statistics/QueryInstore/expQueInstoList.action?Stime="+Stime+"&Etime="+Etime+"&drug="+drug+"&company="+company+"&user="+user,
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
		
		//打印列表
		function exper() {
			var Stime = $('#STime').val();
			var Etime = $('#ETime').val();
			var drug = $('#drug').combobox('getValue');
			var company = $('#company').combobox('getValue');
			var companyName = $('#company').combobox('getText');
			var user = $('#user').combobox('getValue');
			var rows = $('#countList').datagrid('getRows');
			if(rows==null||rows==""){
				$.messager.alert("提示", "列表无数据,无法打印！");
				return;
			}
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
				if (res) {
					window.open ("<%=basePath%>statistics/QueryInstore/printInstore.action?fileName=YPRKCXD&Stime="+Stime+"&Etime="+Etime+"&drug="+drug+"&company="+company+"&user="+user+"&companyName="+encodeURIComponent(encodeURIComponent(companyName)),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
				}
			});
		}
		function formatterCost(value,row,index){
			if(value!=null&&value!=''||value=='0'){
				return  returnFloat(value);
			}
		}
		function returnFloat(value){
			 var value=Math.round(parseFloat(value)*100)/100;
			 var xsd=value.toString().split(".");
			 if(xsd.length==1){
			 value=value.toString()+".00";
			 return value;
			 }
			 if(xsd.length>1){
			 if(xsd[1].length<2){
			  value=value.toString()+"0";
			 }
			 return value;
			 }
			}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>
