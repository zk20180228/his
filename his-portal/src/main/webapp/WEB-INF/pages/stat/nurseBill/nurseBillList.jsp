<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>护士站领药查询 </title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
var billMap=new Map(); //摆药单
var deptMap="";
var startTime="${beginTime}";//重置开始时间
var endTime="${endTime}";//重置结束时间
var minunitMap = "";//药品单位Map
var doseunitMap = "";//计量 单位Map
//计量单位
$.ajax({
	url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=doseUnit", 
	type:'post',
	success: function(data) {
		doseunitMap = data;
	}
});
//计量单位 列表页 显示	
function doseUnitFamaters(value,row,index){	
	if(value!=null&&value!=""){					
		if(doseunitMap[value]!=null&&doseunitMap[value]!=""){
			return doseunitMap[value];
		}
		return value;				
	}			
}
//渲染表单中的最小单位
	$.ajax({
		url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=minunit", 
		type:'post',
		success: function(data) {
			minunitMap = data;
		}
	});
//单位 列表页 显示	
function drugUnitFamaters(value,row,index){	
	if(value!=null&&value!=""){					
		if(minunitMap[value]!=null&&minunitMap[value]!=""){
			return minunitMap[value];
		}
		return value;				
	}			
}
    //科室
	$.ajax({
		url: "<%=basePath%>publics/consultation/querydeptComboboxs.action", 
		success: function(deptData) {
			deptMap = deptData;
		}
	});
	/**
	*科室渲染
	*/
	function functiondept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//查询摆药单
	$.ajax({
		url: "<%=basePath%>statistics/NurseDrugDispens/queryBillNameList.action",				
		type:'post',
		success: function(data) {					
			if(data!=null&&data!=""){
				billMap=data;
			}										
		}
	});
	//摆药单的渲染
	function billClassFormatter(value,row,index){
		if(value!=null&&value!=""){	
			if(billMap[value]!=null&&billMap[value]!=""){
				return billMap[value];
			}
			return value;
		}
	}
	$(function(){
		$("#beginTime").val("${beginTime}");
		$("#endTime").val("${endTime}");
		 // 默认选择汇总，显示汇总table，隐藏明细table
		 $('#hz').prop("checked", true); 
		$('#billSearchHzdiv').show();
		$('#billSearchMxdiv').hide();  
		
		//加载手术摆药单树
		tree();
		bindEnterEvent('bname',query,'easyui');
		//加载数据表格
		$("#billSearchHzList").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{deptCode:null,billClassCode:null,beginTime:null,endTime:null,applyState:null,bname:null},
			url:'<%=basePath %>statistics/nursebill/queryNurseBillHz.action',
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
		//申请状态
		$('#applyState').combobox({
			valueField: 'id',    
	        textField: 'text',
			data:[{
						id:'0',
						text:'申请'
					},{
						id:'1',
						text:'配药'
					},{
						id:'2',
						text:'核准出库'
					},{
						id:'3',
						text:'作废'
					},{
						id:'4',
						text:'暂不摆药'
					},{
						id:'5',
						text:'集中发送已打印'
					},{
						id:'6',
						text:'集中发送未打印'
					},{
						id:'7',
						text:'审核'
				}]
		});
	})
	function onclickhz(){
		$('#hz').prop("checked", true); 
		$('#billSearchHzdiv').show();
		$('#billSearchMxdiv').hide();
		var beginTime=$("#beginTime").val();
		var endTime=$("#endTime").val();
		var applyState=$("#applyState").combobox('getValue');
		var bname=$("#bname").textbox("getValue");
		var billClassCode=$("#billClass").val();
		var deptCode=$("#deptCode").val();
		$("#billSearchHzList").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{deptCode:deptCode,billClassCode:billClassCode,beginTime:beginTime,endTime:endTime,applyState:applyState,bname:bname},
			url:'<%=basePath %>statistics/nursebill/queryNurseBillHz.action'
			
		});  
		 
	}
	function onclickmx(){
		$('#mx').prop("checked", true); 
		$('#billSearchHzdiv').hide();
		$('#billSearchMxdiv').show();
		var beginTime=$("#beginTime").val();
		var endTime=$("#endTime").val();
		var applyState=$("#applyState").combobox('getValue');
		var bname=$("#bname").textbox("getValue");
		var billClassCode=$("#billClass").val();
		var deptCode=$("#deptCode").val();
		$("#billSearchMxList").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
			queryParams:{deptCode:deptCode,billClassCode:billClassCode,beginTime:beginTime,endTime:endTime,applyState:applyState,bname:bname},
			url:'<%=basePath %>statistics/nursebill/queryNurseBillMx.action',
			onLoadSuccess : function(data){
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
	}
	
	/**
	 * @Description:加载摆药单树
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月12日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	function tree(){
		$('#tDt').tree({ 
			url:"<%=basePath%>statistics/nursebill/getNurseBillTree.action",
		    method:'post',
		    animate:true,  //点在展开或折叠的时候是否显示动画效果
		    lines:true,    //是否显示树控件上的虚线
		    state:'closed',//节点不展开
		    formatter:function(node){//统计节点总数
				var s = node.text;
				  if (node.children){
					  if(node.children.length>0){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					  }
				}  
				return s;
			},
			onBeforeLoad:function(node, param){
				if(node!=null){
					return false;
				}
			},
			onClick:function(node){
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);	
			},
			onSelect: function(node){//点击节点
				if(node.id!=1){
					var beginTime=$("#beginTime").val();
					var endTime=$("#endTime").val();
					var applyState=$("#applyState").combobox('getValue');
					var bname=$("#bname").textbox("getValue");
					if(node.attributes.bid=='1'){
					}else if(node.attributes.bid=='2'){
						$("#deptCode").val(node.attributes.did);
						$("#billClass").val(node.attributes.cid);
						if ($("#hz").prop("checked")==true){
							 $("#billSearchHzList").datagrid('load',{deptCode:$("#deptCode").val(),billClassCode:$("#billClass").val(),beginTime:beginTime,endTime:endTime,applyState:applyState,bname:bname});
						}else{
							 $("#billSearchMxList").datagrid('load',{deptCode:$("#deptCode").val(),billClassCode:$("#billClass").val(),beginTime:beginTime,endTime:endTime,applyState:applyState,bname:bname});
						}
					}
				}
			},onLoadSuccess:function(node, data){
				   console.info(data);
				   if(data.resCode=='error'){
					   $("body").setLoading({
							id:"body",
							isImg:false,
							text:data.resMsg
						});
				   }
			   }
		}); 
	}
	
	
	/**
	 * 查询
	 * @author  tangfeishuai
	 * @date 2016-06-13
	 * @version 1.0
	 */
	function query() {
		var beginTime=$("#beginTime").val();
		var endTime=$("#endTime").val();
		if(beginTime&&endTime){
		    if(beginTime>endTime){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
		var applyState=$("#applyState").combobox('getValue');
		var bname=$("#bname").textbox("getValue");
		var billClassCode=$("#billClass").val();
		var deptCode=$("#deptCode").val();
		if ($("#hz").prop("checked")==true){
			 $("#billSearchHzList").datagrid('load',{deptCode:deptCode,billClassCode:billClassCode,beginTime:beginTime,endTime:endTime,applyState:applyState,bname:bname});
		}else{
			 $("#billSearchMxList").datagrid('load',{deptCode:deptCode,billClassCode:billClassCode,beginTime:beginTime,endTime:endTime,applyState:applyState,bname:bname});
		}
		
	}
	function edit(){
		var beginTime=$("#beginTime").val();
		var endTime=$("#endTime").val();
		var applyState=$("#applyState").combobox('getValue');
		var bname=$("#bname").textbox("getValue");
		var billClassCode=$("#billClass").val();
		var deptCode=$("#deptCode").val();
		if ($("#hz").prop("checked")==true){
			var rows = $("#billSearchHzList").datagrid('getRows');
			if(rows==null||rows==''){
				$.messager.alert('提示',"汇总信息无数据!");
				return;
			}
			$.messager.confirm('打印护士站领药汇总信息', '是否打印护士站领药汇总信息?', function(res) {  //提示是否打印假条
				if (res){
					var timerStr = Math.random();
					window.open ("<c:url value='/statistics/nursebill/printNurseBillHz.action?randomId='/>"+timerStr+"&beginTime="+beginTime+"&endTime="+endTime+"&applyState="+applyState+"&bname="+bname+"&billClassCode="+billClassCode+"&deptCode="+deptCode+"&fileName=HSZLYYPHZTJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
					};
			 })
		}else{
			var rows = $("#billSearchMxList").datagrid('getRows');
			if(rows==null||rows==''){
				$.messager.alert('提示',"明细信息无数据!");
				return;
			}
			$.messager.confirm('打印护士站领药明细信息', '是否打印护士站领药明细信息?', function(res) {  //提示是否打印假条
				if (res){
					var timerStr = Math.random();
					window.open ("<c:url value='/statistics/nursebill/printNurseBillMx.action?randomId='/>"+timerStr+"&beginTime="+beginTime+"&endTime="+endTime+"&applyState="+applyState+"&bname="+bname+"&billClassCode="+billClassCode+"&deptCode="+deptCode+"&fileName=HSZLYYPMXTJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
					};
			 })
		}
		
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
					var Stime = $('#beginTime').val(day);
				    var Etime = $('#endTime').val(day);
				    query();
				}else if(val==2){
					var nowd = new Date();
					var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
					var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					var date=myDate.getDate();
					month=month<10?"0"+month:month;
					date=date<10?"0"+date:date;
					var day2=year+"-"+month+"-"+date;
					var Stime = $('#beginTime').val(day2);
					 nowd = new Date();
					 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
					 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					 date=myDate.getDate();
				  	month=month<10?"0"+month:month;
					 date=date<10?"0"+date:date;
					 day3=year+"-"+month+"-"+date;
				    var Etime = $('#endTime').val(day3);
				    query();
				}else if(val==3){
					var nowd = new Date();
					var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
					var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					var date=myDate.getDate();
					month=month<10?"0"+month:month;
					date=date<10?"0"+date:date;
					var day3=year+"-"+month+"-"+date;
					var Stime = $('#beginTime').val(day3);
					 nowd = new Date();
					 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
					 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					 date=myDate.getDate();
				  	month=month<10?"0"+month:month;
					 date=date<10?"0"+date:date;
					 day3=year+"-"+month+"-"+date;
				    var Etime = $('#endTime').val(day3);
				    query();
				}else if(val==4){
					var myDate = new Date();
					var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					month=month<10?"0"+month:month;
					var day=year+"-"+month+"-"+"01";
					var Stime = $('#beginTime').val(day);
					year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					date=myDate.getDate();
					month=month<10?"0"+month:month;
					date=date<10?"0"+date:date;
					day=year+"-"+month+"-"+date;
				    var Etime = $('#endTime').val(day);
				    query();
				}else if(val==5){
					var myDate = new Date();
					var year=myDate.getFullYear();
					var day=year+"-"+"01"+"-"+"01";
					var Stime = $('#beginTime').val(day);
					year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					date=myDate.getDate();
					month=month<10?"0"+month:month;
					date=date<10?"0"+date:date;
					day=year+"-"+month+"-"+date;
				    var Etime = $('#endTime').val(day);
				    query();
				}else if(val==6){
					var nowd = new Date();
					var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
					var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					var date=myDate.getDate();
					month=month<10?"0"+month:month;
					date=date<10?"0"+date:date;
					var day3=year+"-"+month+"-"+date;
					var Stime = $('#beginTime').val(day3);
					 nowd = new Date();
					 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
					 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
					 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
					 date=myDate.getDate();
				  	month=month<10?"0"+month:month;
					 date=date<10?"0"+date:date;
					 day3=year+"-"+month+"-"+date;
				    var Etime = $('#endTime').val(day3);
				    query();
				}else if(val==7){//上个月
					var date=new Date();
					var year=date.getFullYear();
					var month=date.getMonth();
					if(month==0){
						year=year-1;
						month=12;
					}
					var startTime=year+'-'+month+'-01';
					startTime=startTime.replace(/-(\d)\b/g,'-0$1');
					 $('#beginTime').val(startTime);
					var date=new Date(year,month,0);
					var endTime=year+'-'+month+'-'+date.getDate();
					endTime=endTime.replace(/-(\d)\b/g,'-0$1');
					$('#endTime').val(endTime);
					var Stime = $('#beginTime').val();
				    var Etime = $('#endTime').val();
				    query();
				}
				
			}
	 //重置
	function clear(){
		$("#beginTime").val(startTime);
		$("#endTime").val(endTime);
		 $('#hz').prop("checked", true); 
		$('#billSearchHzdiv').show();
		$('#billSearchMxdiv').hide();
		$('#applyState').combobox('setValue','');
		$('#bname').textbox('setValue','')
		query();
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="c" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west',split:true,border:false" style="width: 18%;padding:5px">
				<div id="treeDiv" >
					<ul id="tDt">加载中，请稍等...</ul>
				</div>
			</div>
			<div data-options="region:'center',split:true,border:false" style="width: 82%;height:100%">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false" style="height: 70px;border-top:0">
						<div style="padding: 5px 5px 0px 5px;">
							<input type="hidden" id="deptCode" />
							<input type="hidden" id="billClass" />
									<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="${menuAlias}:function:print">
									<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="${menuAlias}:function:set">
									<a href="javascript:clear();void(0)" onclick="" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>&nbsp;
									</shiro:hasPermission> 
									汇总&nbsp;<input type="radio" onclick="javascript:onclickhz()"  id="hz" name="drug">&nbsp; 
									明细&nbsp;<input type="radio" id="mx" onclick="javascript:onclickmx()"  name="drug">
									<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>&nbsp;
									<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>&nbsp;
									<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>&nbsp;
									<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>&nbsp;
									<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>&nbsp;
									<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>&nbsp;
									<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>&nbsp;
			    		</div>
						<div id="toolbarId" style="padding:5px 5px 5px 5px;">
							<table style="width: 100%" cellspacing="0" cellpadding="0">
							 <tr>
								<td>
									日期：
									<input id="beginTime"  name=beginTime class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				    				至
				    				<input id="endTime" name="endTime"  class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				    				&nbsp;摆药状态：<input class="easyui-combobox" id="applyState" style="width:150px" />
				    				<a href="javascript:delSelectedData('applyState');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
									药品过滤：<input class="easyui-textbox" id="bname" style="width:150px">
								</td>
							    <td>
							   </td>
							   </tr>
							</table>
						</div>
					</div>
					<div data-options="region:'center',split:false,border:false"  style="height:95%">
						<div id="billSearchHzdiv" style="height:100%">
							<table id="billSearchHzList" class="easyui-datagrid" data-options="border:false,rownumbers:true,pageSize:20,pageList:[20,30,50,80,100]"  style="padding:5px 5px 5px 5px;">
								<thead>
									<tr>
										<th data-options="field:'drugName',width:'9%',align:'center' ">药品名称</th>
										<th data-options="field:'specs',width:'9%',align:'center'">规格</th>
										<th data-options="field:'applySum',width:'9%',align:'center'">总量</th>
										<th data-options="field:'minUnit',width:'9%',align:'center',formatter:drugUnitFamaters">单位</th>
										<th data-options="field:'applyDept',formatter:functiondept,width:'9%',align:'center'">申请科室</th>
										<th data-options="field:'drugDept',formatter:functiondept,width:'9%',align:'center'">发药药房</th>
										<th data-options="field:'billClassName',formatter:billClassFormatter,width:'9%',align:'center'">摆药单</th>
										<th data-options="field:'validState',width:'9%',align:'center'">有效性</th>
										<th data-options="field:'states',width:'9%',align:'center'">状态</th>
									</tr>
								</thead>
							</table>
						</div>
						<div id="billSearchMxdiv" style="height:100%">
							<table id="billSearchMxList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;"  data-options="rownumbers:true,pageSize:20,pageList:[20,30,50,80,100]" >
								<thead>
									<tr>
										<th data-options="field:'bedNo',width:'4%',halign:'center' ">床号</th>
										<th data-options="field:'patientName',width:'4%',halign:'center'">姓名</th>
										<th data-options="field:'medicalRecordID',width:'7%',halign:'center'">病历号</th>
										<th data-options="field:'drugName',width:'11%',halign:'center'">药品名称</th>
										<th data-options="field:'specs',width:'7%',halign:'center'">规格</th>
										<th data-options="field:'doseOnce',width:'3%',halign:'center'">每次量</th>
										<th data-options="field:'doseUnit',width:'5%',halign:'center',formatter:doseUnitFamaters">剂量单位</th>
										<th data-options="field:'dfqFreq',width:'5%',halign:'center' ">频次</th>
										<th data-options="field:'useName',width:'5%',halign:'center'">用法</th>
										<th data-options="field:'applyNum',width:'3%',halign:'center'">总量</th>
										<th data-options="field:'minUnit',width:'5%',halign:'center',formatter:drugUnitFamaters">单位</th>
										<th data-options="field:'applyDept',width:'6%',halign:'center'">申请科室</th>
										<th data-options="field:'drugDept',width:'6%',halign:'center'">发药药房</th>
										<th data-options="field:'billClassName',formatter:billClassFormatter,width:'9%',halign:'center'">摆药单</th>
										<th data-options="field:'validState',width:'5%',halign:'center'">有效性</th>
										<th data-options="field:'states',width:'5%',halign:'center'">状态</th>
										<th data-options="field:'drugedBill',width:'5%',halign:'center'">摆药单号</th>
										<th data-options="field:'printDate',width:'5%',halign:'center'">发药时间</th>
									</tr>
								</thead>
							</table>
						</div> 
					</div>
				</div>
			</div>
		</div>
	</body>
</html>