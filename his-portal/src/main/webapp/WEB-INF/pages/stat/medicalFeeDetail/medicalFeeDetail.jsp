<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
在院患者在院医药费用明细
</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="height:40px; line-height: 40px;padding-top: 8px;padding-left: 10px;padding-right: 10px;">
		<table border="0" style="width: 100%" cellspacing="0" cellpadding="0">
			<tr>
				<td style="width: 85px;">
					<input id="queryType" style="width: 80px;" readonly="readonly">
				</td>
				<td id="date"  style="width: 350px;">住院日期：
					<input id="startTime" name="startTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
				至  <input id="endTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
				&nbsp;
<!-- 					<input id="inpatientNoId" type="checkbox" onclick="onClickIsUrgent('inpatientNoId')" /> -->
				</td>
				<td id="medicalrecordId" style="display: none;width: 350px;">
					病历号：<input type="text"  id="medicalrecordIdSerc"  style="width:150px;" class="easyui-textbox" />
				</td>
				<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="searchGrid()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:readCard">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" id="read_medical_cardID" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					</shiro:hasPermission>
		        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" id="read_identityID" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:reset">
					<a href="javascript:clear();void(0)" onclick="" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
					</shiro:hasPermission>
				</td>
				<td style="text-align:center;">
				   <shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false" style="height:95%;">
		<table id="listMedicalFeeDetail">					
		</table>
	</div>
</div>
<input id="deptId" type="hidden" value="${deptId}">
<script type="text/javascript">
 var menuAlias = '${menuAlias}';
 var startTime;//重置开始时间
 var endTime;//重置结束时间
	$(function(){
		$('#queryType').combobox({
			valueField: 'id',    
	        textField: 'text',
	        data:[{'id':'date','text':'住院时间','selected':true},
	              {'id':'medicalrecordId','text':'病历号'}],
	        onSelect:function(record){
	        	console.info(record);
	        		$('#'+record.id).show();
	        		if(record.id=='date'){
	        			$('#medicalrecordId').hide();
	        			clearCls();
	        			disableReadCard();
	        		}else{
	        			$('#date').hide();
	        			$('#startTime').val('');
	        			$('#endTime').val('');
	        			$('#read_medical_cardID').linkbutton('enable');
	        			$('#read_identityID').linkbutton('enable');
	        		}
	        }
		});
		$('#medicalrecordIdSerc').textbox({});
		bindEnterEvent('medicalrecordIdSerc',searchGrid,'easyui');
		var myDate = new Date();
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		month=month<10?"0"+month:month;
		var day=year+"-"+month+"-"+"01";
		var Stime = $('#startTime').val(day);
		startTime=day;//重置按钮
		year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		day=year+"-"+month+"-"+date;
	    var Etime = $('#endTime').val(day);
	    endTime=day;//重置结束时间
	    disableReadCard();
	});
	function clearCls(){
		$('#medicalrecordIdSerc').textbox('setValue','');
		$('#startTime').val(startTime);
		$('#endTime').val(endTime);
	}
	function disableReadCard(){
		$('#read_medical_cardID').linkbutton('disable');
	 	$('#read_identityID').linkbutton('disable');
	}
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
				$('#medicalrecordIdSerc').textbox('setValue',data);
				searchGrid();
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
					$('#medicalrecordIdSerc').textbox('setValue',data);
					searchGrid();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	var column = [];//列标题数组
	var fields = [];
	//查询列表标题
	$.ajax({
		url: "<%=basePath%>statistics/MedicalFeeDetail/queryMedicalFeeDetailTitle.action",				
		type:'post',
		success: function(data) {		
			columns='[';			
			for (var i = 0; i < data.length; i++) {
                if (i == 0) {
                    var field = '{field:"' + data[i].feeStatCode + '",title:"' + data[i].feeStatName + '",width:"120"}';
                } else if (i == 1) {
                    var field = '{field:"' + data[i].feeStatCode + '",title:"' + data[i].feeStatName + '",width:"90"}';
                } else if (i == data.length - 2 || i == data.length - 1) {
                    var field='{field:"'+data[i].feeStatCode+'",title:"'+data[i].feeStatName+'",width:"110",align:"right",formatter:formatterNum}';
                } else {
                    var field='{field:"'+data[i].feeStatCode+'",title:"'+data[i].feeStatName+'",width:"90",align:"right",formatter:formatterNum}';
				}
				
				
				if(i<data.length-1){
					columns+=field+',';
					fields.push(data[i].feeStatCode);
				}else{
					fields.push(data[i].feeStatCode);
					columns+=field;
				}
			}
			columns+=']';
			columns = eval("(" + columns + ")");	
			column.push(columns); 
			listInfo();
		}
	});
	function listInfo(){
        var medicalrecordId = $('#medicalrecordIdSerc').val();
		$("#listMedicalFeeDetail").datagrid({ 
			columns:column,
			fit:true,
			method:'post',
            url:'<%=basePath%>statistics/MedicalFeeDetail/queryMedicalFeeDetailsByES.action?menuAlias='+menuAlias,
            queryParams: {
                'inpatientInfo.medicalrecordId':medicalrecordId,
                'startTime':startTime,
                'endTime':endTime
            },
			rownumbers:true,
			striped:true,
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
            pagination:true,
            pageSize:50,
            pageList:[50,100,200,300]
		});
	}
	/**
	 * 查询
	 * @author  yeguanqun
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2016-6-2
	 * @version 1.0
	 */
	 function searchGrid(){
		var medicalrecordId = $('#medicalrecordIdSerc').val();
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		
         var searchSign=$('#queryType').combobox('getValue');
         if(searchSign=='medicalrecordId'){
        	 if(medicalrecordId=='' || medicalrecordId==null){
     			$.messager.alert('操作提示', '请输入病历号！');
     			return;
     		}
         }else{
        	 if(startTime=='' || startTime==null){
     			$.messager.alert('操作提示', '开始时间不能为空！');
     			return;
     		 }else if(endTime=='' || endTime==null){
     			$.messager.alert('操作提示', '结束时间不能为空！');
     			return;
     		}else if(endTime!='' && startTime!=''){
     			var sDate = new Date(startTime.replace(/\-/g, "\/"));  
     			var eDate = new Date(endTime.replace(/\-/g, "\/"));  
     			if(sDate>eDate){						
     				$.messager.alert('操作提示', '开始时间不能大于结束时间！');
     				return;
     			}
     		}
         }
		$("#listMedicalFeeDetail").datagrid('load', {
            'inpatientInfo.medicalrecordId':medicalrecordId,
            'startTime':startTime,
            'endTime':endTime
        });
	 }
	 /**
	  * 计算行统计
	  */
	 function getRowSum(){
	 	 var colName='';
	 	 var sum=0;
	 	 var field='{inpatientNo:'+"'合计:'"+','+'patientName:'+"''";
	 	 var rows = $('#listMedicalFeeDetail').datagrid('getRows')//获取当前的数据行
	 	 if(rows==null||rows==""){
	 		 return ;
	 	 }
	     for (var i = 2; i < fields.length;i++) {
	     	 var totSum=0;
	 		 colName=fields[i];
	 	     for (var j = 0; j < rows.length; j++) {
	 	    	 if(rows[j][colName]==''||rows[j][colName]==null){
	 	    		rows[j][colName]=0;
	 	    		
	 	    	 }
	 	         totSum += rows[j][colName];
	 	     }
//	 	     sum+=totSum;
	 	     field=field+',"'+colName+'":'+totSum.toFixed(2);
	 	    
	 	}
	 	field+='}';
	 	field = eval("(" + field + ")");
	      //新增一行显示统计信息
	    $('#listMedicalFeeDetail').datagrid('appendRow',field);
	 }

	function onClickIsUrgent(id){
		if($('#'+id).is(':checked')){	
			$('#medicalrecordIdSerc').textbox('enable');
			$('#read_medical_cardID').linkbutton('enable');
			$('#read_identityID').linkbutton('enable');
		}else{
			$('#medicalrecordIdSerc').textbox('setValue','');
			$('#medicalrecordIdSerc').textbox('disable');
			$('#read_medical_cardID').linkbutton('disable');
			$('#read_identityID').linkbutton('disable');
		}
	}
	
	//按时间段查询
	function queryMidday(val){
		$('#queryType').combobox('select','date');
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
		    searchGrid();
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
		    searchGrid();
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
		    searchGrid();
		}else if(val==4){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			month=month<10?"0"+month:month;
			var day=year+"-"+month+"-"+"01";
			var Stime = $('#startTime').val(day);
			year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			day=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day);
		    searchGrid();
		}else if(val==5){
			var myDate = new Date();
			var year=myDate.getFullYear();
			var day=year+"-"+"01"+"-"+"01";
			var Stime = $('#startTime').val(day);
			year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			day=year+"-"+month+"-"+date;
		    var Etime = $('#endTime').val(day);
			searchGrid();
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
		    searchGrid();
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
			 searchGrid();
		}
		
	}
function clear(){
	$('#queryType').combobox('unselect','date');
	$('#queryType').combobox('select','date');
// 	$('#startTime').val(startTime);
// 	$('#endTime').val(endTime);
// 	$('#inpatientNoId').attr('checked',false);
// 	$('#medicalrecordIdSerc').textbox('setValue','');
// 	$('#medicalrecordIdSerc').textbox('disable');
// 	$('#read_medical_cardID').linkbutton('disable');
// 	$('#read_identityID').linkbutton('disable');
 	$("#listMedicalFeeDetail").datagrid('loadData',{total: 0, rows: [] });
	 if ($('#listMedicalFeeDetail').datagrid('getRows').length != 0){
 		$("#listMedicalFeeDetail").datagrid('deleteRow',0); 
	 }
}
 function formatterNum(value,row,index){
     if(value!=null){
         value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
         var l = value.split(".")[0].split("").reverse(), r = value.split(".")[1];
         t = "";
         for (i = 0; i < l.length; i++) {
             t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
         }
		 t = t.split("").reverse().join("") + "." + r;
         //去掉负数负号之后的逗号，例如-,125.00改为-125.00
         if(t.indexOf("-") === 0 && t.indexOf(",") === 1){
			 t = t.substring(0, 1) +t.substring(2, t.length);
		 }
         return t
     }else {
         return "0.00";
     }
 }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>