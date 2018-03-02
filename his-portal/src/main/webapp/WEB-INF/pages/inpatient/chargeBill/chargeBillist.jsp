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
<script type="text/javascript">
	//保存住院流水号
	var inpatent;
	/**
	*科室Map
	**/
	var deptMap=null;
	/**
	*员工Map
	*/
	var empMap=null;
	/**
	*结算类别Map
	*/
	var paykindMap=new Map();
	/**
	*单位Map
	*/
	var codeTypeMap=null;
	var sexMap=new Map();
	$(function(){
		bindEnterEvent('medId', searchValue, 'easyui');
		bindEnterEvent('certificatesNo', searchValue, 'easyui');
		$('#sendflag').combobox({
			data:[{"id":3,"text":"全部"},{"id":0,"text":"划价"},{"id":1,"text":"收费"},{"id":2,"text":"执行/摆药"}],    
			valueField:'id',    
			textField:'text',
			required:true,
			onLoadSuccess:function(none){
				$('#sendflag').combobox('select', 3);    
			}
		});
		//查询住院科室（渲染）
		$.ajax({
			url:'<%=basePath%>statistics/ChargeBill/queryZYDept.action',
			success:function(data){
				deptMap=data;
			}
		});
 		//查询结算类别（渲染）
 		$.ajax({
			url: basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
 			success:function(data){
 				var type = data;
 				for(var i=0;i<type.length;i++){
 					paykindMap.put(type[i].encode,type[i].name);
 				}
 			}
 		});
		//查询员工（渲染）
		$.ajax({
			url:'<%=basePath%>statistics/ChargeBill/queryEmpbill.action',
			success:function(data){
				empMap=data;
			}
		});
		//查询单位
		$.ajax({
			url:'<%=basePath%>statistics/ChargeBill/queryCodeSysType.action',
			success:function(data){
				codeTypeMap=data;
			}
		});
		//性别渲染
		$.ajax({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
			}
		});
	});
	
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#medId').textbox('setValue',data);
				searchValue();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) {
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#medId').textbox('setValue',data);
					searchValue();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	
	/**
	*通过输入的就诊卡号查询住院主表中的患者信息
	*
	**/
	function searchValue(){
		var pid=$.trim($('#medId').textbox('getValue'));
		var idCard=$('#certificatesNo').textbox('getValue');
		if(pid == ''&&idCard==''){
			$.messager.alert('提示','请输入病历号或者身份证号');
			return false;
		}
		if(pid==''){
			pid='SFZ:'+idCard;
		}
		$.ajax({
			url:'<%=basePath%>statistics/ChargeBill/queryInpatientInfobill.action',
			data:{medicalrecordId:pid},
			success:function(data){
				plist=data;
				if(plist.length>1){
					$("#InpatientMes").window('open');
					$("#infoDatagridMes").datagrid({
						data:plist,
						    columns:[[  
								{field:'ck',checkbox : true} ,  
						        {field:'medicalrecordId',title:'病历号',width:'25%',align:'center'} ,  
						        {field:'inpatientNo',title:'住院流水号',width:'25%',align:'center'},
						        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
						        {field:'inDate',title:'入院时间',width:'30%',align:'center'}
						    ]] ,
						});
				  }else if(plist.length==0){
					  $.messager.alert('提示',"该患者没有住院登记");
				  }else if(plist.length==1){
					  $("#diaInpatient").window('close');
					  	var startTime=$('#startTime').val();
						var endTime=$('#endTime').val();
						var sendflag=$('#sendflag').combobox('getValue');
						$('#name').text(plist[0].patientName);//患者姓名
						$('#deptId').text(deptMap[plist[0].deptCode]);//患者住院科室
						$('#paykind').text(paykindMap.get(plist[0].paykindCode));//患者结算类别
						$('#indate').text(plist[0].inDate);//患者入院日期
						$('#medId').textbox('setValue',plist[0].medicalrecordId);
						$('#certificatesNo').textbox('setValue',plist[0].certificatesNo)
						//通过住院流水号查询住院药品明细表和住院非药品明细表中的记录
						var io=plist[0].inpatientNo;
						findMes(io,startTime,endTime,sendflag);  
				  }
			}
		});
	}
	
	function findMesIn(){
			var startTime=$('#startTime').val();
			var endTime=$('#endTime').val();
			var sendflag=$('#sendflag').combobox('getValue');
			var data=$("#infoDatagridMes").datagrid('getSelections')
			var inpatientNo='';
			if(data.length>0){
				$("#InpatientMes").window('close');
				$('#name').text(data[0].patientName);//患者姓名
				for(var i=0;i<data.length;i++){
					if(i<data.length-1){
						inpatientNo+=""+data[i].inpatientNo+",";
					}else{
						inpatientNo+=""+data[i].inpatientNo+"";
					}
				}
			}else{
				 $.messager.alert('提示',"至少选择一条数据");
			}
			$("#diaInpatient").window('close');
			findMes(inpatientNo,startTime,endTime,sendflag);
	}
	
	function findMes(inpatientNo,startTime,endTime,sendflag){
		 if(startTime&&endTime){
			    if(startTime>endTime){
			      $.messager.alert("提示","开始时间不能大于结束时间！");
			      close_alert();
			      return ;
			    }
			  }
		inpatent=inpatientNo;
		$('#tableList').datagrid({
			url:"<%=basePath %>statistics/ChargeBill/queryDatagridinfo.action?menuAlias=${menuAlias}",
			queryParams:{inpatientNo:inpatientNo,startTime:startTime,endTime:endTime,sendFlag:sendflag},
			onLoadSuccess:function(data){
				
				var totnum=0;//费用总金额
				var ownnum=0;//自费总金额
				var paynum=0;//自付总金额
				var pubnum=0;//公费总金额
				var econum=0;//优惠总金额
				var rows=$('#tableList').datagrid('getRows');//返回当前页的所有行
				for(var i=0;i<rows.length;i++){
					totnum=eval(totnum+"+"+rows[i].totCost);
					ownnum=eval(ownnum+"+"+rows[i].ownCost);
					paynum=eval(paynum+"+"+rows[i].payCost);
					pubnum=eval(pubnum+"+"+rows[i].pubCost);
					econum=eval(econum+"+"+rows[i].ecoCost);
				}
				var a=totnum.toFixed(2);
				var b=ownnum.toFixed(2);
				var c=paynum.toFixed(2);
				var d=pubnum.toFixed(2);
				var e=econum.toFixed(2);
				$('#totcost').text(a);
				$('#owncost').text(b);
				$('#paycost').text(c);
				$('#pubcost').text(d);
				$('#ecocost').text(e);
			}
			
		});
		
	}
	/**
	*报表打印
	**/
	function printPDf(){
		var rowsCount=$('#tableList').datagrid('getRows');
		if(''!=rowsCount&&null!=rowsCount){
		var inpatientNo=inpatent;
		var Strtime=new Date().getTime();
		var startTime=$('#startTime').val();
		var endTime=$('#endTime').val();
		var sendflag=$('#sendflag').combobox('getValue');
		<!--加载PDF -->
		var medId='T_INPATIENT_INFO_NOW';
		window.open ("<c:url value='queryDatagridinfoPDF.action?inpatientNo='/>"+inpatientNo+"&startTime="+startTime+"&endTime="+endTime+"&sendflag="+sendflag+"&medId="+medId+"&time="+Strtime,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}
		else{
			$.messager.alert('提示','没有当前记录，无法提供打印功能！');
		}
		
		}
	
	/**
	*执行科室渲染
	**/
	function formatdeptId(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	/**
	*收费人渲染
	**/
	function fromaterempId(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	/**
	*单位渲染
	*/
	function formattercodetype(value,row,index){
		if(value!=null&&value!=''){
			if(codeTypeMap[value]){
				return codeTypeMap[value];
			}else{
				return value;
			}
			
		}
	}
	//渲染费用金额
	function totCostFormatter(value,row,index){
		var sum=row.totCost;
		if(typeof(sum)=='undefined'){
			sum=0;
		}
		return sum.toFixed(2);
	}
	//渲染自费金额
	function ownCostFormatter(value,row,index){
		var sum=row.ownCost;
		if(typeof(sum)=='undefined'){
			sum=0;
		}
		return sum.toFixed(2);
	}
	//渲染自付金额
	function payCostFormatter(value,row,index){
		var sum=row.payCost;
		if(typeof(sum)=='undefined'){
			sum=0;
		}
		return sum.toFixed(2);
	}
	//渲染公费金额
	function pubCostFormatter(value,row,index){
		var sum=row.pubCost;
		if(typeof(sum)=='undefined'){
			sum=0;
		}
		return sum.toFixed(2);
	}
	//渲染优惠金额
	function ecoCostFormatter(value,row,index){
		var sum=row.ecoCost;
		if(typeof(sum)=='undefined'){
			sum=0;
		}
		return sum.toFixed(2);
	}
	/**
	*药品、非药品渲染
	*/
	function functionState(value,row,index){
		if(value=='y'){
			return '<span style=\'color:black;\'>药品</span>'
		}else if(value=='f'){
			return '<span style=\'color:black;\'>非药品</span>'
		}
	}
	function clear(){
		$('#startTime').val("${startTime }");
		$('#endTime').val("${endTime }");
		$('#medId').textbox('setValue',"");
		$('#sendflag').combobox('setValue',"3");
		$('#certificatesNo').textbox('setValue',"");
		$("#tableList").datagrid('loadData', { total: 0, rows: [] });
		$('#indate').text("");//患者姓名
		$('#totcost').text("");//患者姓名
		$('#paykind').text("");//患者姓名
		$('#owncost').text("");//患者姓名
		$('#paycost').text("");//患者姓名
		$('#pubcost').text("");//患者姓名
		$('#ecocost').text("");//患者姓名
		$('#deptId').text("");//患者姓名
	}
	//按时间段查询
	function queryMidday(val){
		if(val==1){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day=year+"-"+month+"-"+date;
			var Stime = $('#startTime').val(day);
		    var Etime = $('#endTime').val(day);
		    searchValue();
		}else if(val==2){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day2=year+"-"+month+"-"+date;
			var Stime = $('#startTime').val(day2);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day3);
		    searchValue();
		}else if(val==3){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day3=year+"-"+month+"-"+date;
			var Stime = $('#startTime').val(day3);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day3);
		    searchValue();
		}else if(val==4){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			month=month<10?"0"+month:month;
			var day=year+"-"+month+"-"+"01";
			var Stime = $('#startTime').val(day);
			 myDate = new Date();
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
			 month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day);
		    searchValue();
		}else if(val==5){
			var myDate = new Date();
			var year=myDate.getFullYear();
			var day=year+"-"+"01"+"-"+"01";
			var Stime = $('#startTime').val(day);
			 myDate = new Date();
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
			 month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day);
		    searchValue();
		}else if(val==6){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day3=year+"-"+month+"-"+date;
			var Stime = $('#startTime').val(day3);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day3);
		    searchValue();
		}else if(val==7){
			var date=new Date();
			var year=date.getFullYear();
			var month=date.getMonth();
			if(month==0){
				year=year-1;
				month=12;
			}
			var startTime=year+'-'+month+'-01';
			 $('#startTime').val(startTime);
			var date=new Date(year,month,0);
			var endTime=year+'-'+month+'-'+date.getDate();
			$('#endTime').val(endTime);
			searchValue();
		}
		
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true" style="margin-left:auto;margin-right:auto;">
		<div data-options="region:'north',border:false" style="padding:5px 5px 0px 5px;width:100%;height:85px;"  >
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false" style="width:100%;">
					<table style="width: 100%" cellspacing="0" cellpadding="0">
					    <tr >
							<td>
							    <shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchValue()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:readCard">
									<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
								</shiro:hasPermission>
					        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
									<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
								</shiro:hasPermission>	
								<shiro:hasPermission name="${menuAlias}:function:reset">
								<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:print">
							    <a href="javascript:void(0)" onclick="printPDf()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
							    </shiro:hasPermission>
							    <shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>&nbsp;
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>&nbsp;
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>&nbsp;
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>&nbsp;
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>&nbsp;
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>&nbsp;
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>&nbsp;
								</shiro:hasPermission>
							</td>
					    </tr>
					    <tr style="height:5px"></tr>
						<tr>
							<td class="chargeBillistSize">日期:
						
								<input id="startTime"  name="startTime" class="Wdate" type="text" value="${startTime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							
							至
					
								<input id="endTime"  name="endTime"  class="Wdate" type="text" value="${endTime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						
							&nbsp;发送状态:
							
								<input id="sendflag"  name="sendflag" style="width:120px "  class="easyui-combobox"/>
							
							&nbsp;病历号：
					
								<input id="medId" class='easyui-textbox'>
						
							&nbsp;身份证号：
							
								<input id="certificatesNo" class='easyui-textbox'>
							</td>
						</tr>
					</table>
					<table  border="0" style="height: 10px;padding:0px 0px 0px 0px" >
						<tr>
							<td style="width: 2%;" align="left" nowrap="nowrap">姓名：</td>
							<td id="name" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">住院科室：</td>
							<td id="deptId" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">结算类别：</td>
							<td id="paykind" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">入院时间：</td>
							<td id="indate" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">费用总金额：</td>
							<td id="totcost" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">自费总金额：</td>
							<td id="owncost" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">自付总金额：</td>
							<td id="paycost" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">公费总金额：</td>
							<td id="pubcost" style="width: 5%;" align="left" nowrap="nowrap"></td>
							<td style="width: 2%;" align="right" nowrap="nowrap">优惠总金额：</td>
							<td id="ecocost" style="width: 5%;" align="left" nowrap="nowrap"></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false" style="width: 100%;padding:0px 0px 30px 0px;margin-left:auto;margin-right:auto;">
			<table id="tableList" class="easyui-datagrid" style="width:100%;height:90%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'state',width:'4%',align:'center',formatter:functionState"></th>
						<th data-options="field:'itemName',width:'10%'" align="center" halign="center">项目名称</th>
						<th data-options="field:'currentUnit',width:'10%',formatter:formattercodetype"align="center" halign="center">当前单位</th>
						<th data-options="field:'unitPrice',width:'7%'" align="center" halign="center">单价</th>
						<th data-options="field:'qty',width:'4%'" align="center" halign="center">数量</th>
						<th data-options="field:'totCost',width:'7%',formatter:totCostFormatter" align="right" halign="center">费用金额</th>
						<th data-options="field:'ownCost',width:'7%',formatter:ownCostFormatter" align="right" halign="center">自费金额</th>
						<th data-options="field:'payCost',width:'7%',formatter:payCostFormatter" align="right" halign="center">自付金额</th>
						<th data-options="field:'pubCost',width:'7%',formatter:pubCostFormatter" align="right" halign="center">公费金额</th>
						<th data-options="field:'ecoCost',width:'7%',formatter:ecoCostFormatter" align="right" halign="center">优惠金额</th>
						<th data-options="field:'executeDeptcode',width:'10%',formatter:formatdeptId" align="center" halign="center">执行科室</th>
						<th data-options="field:'chargeOpercode',width:'10%',formatter:fromaterempId" align="center" halign="center">收费人</th>
						<th data-options="field:'chargeDate',width:'10%',align:'center'" align="center" halign="center">收费日期</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
	   <table id="infoDatagrid" data-options="fitColumns:true,singleSelect:true,fit:true"></table>
	</div>
	<div id="InpatientMes" class="easyui-dialog" title="选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">  
	    <shiro:hasPermission name="${menuAlias}:function:query"> 
	 	<a href="javascript:void(0)" onclick="findMesIn()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	 	</shiro:hasPermission>
	 	<br></br>
	   <table id="infoDatagridMes" data-options="fitColumns:true,checkOnSelect:true,fit:true"></table>
	</div>
	
</body>
</html>