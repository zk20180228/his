<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>在院（出院）病人医药费结算汇总表</title>
<%@ include file="/common/metas.jsp"%>
</head>
<style>
	h2,.selectAll0,.selectAll1{
		display: none !important;
	}
</style>
<body>
	<div class="easyui-layout"  style="width:100%;height: 100%">
 		<form >
	 		<table border="0" style="width: 100%;z-index: 0;height:43px;" cellspacing="0" cellpadding="0">
	 				<tr>
	 					<td class="inpatientFeeBalSumSize" nowrap="nowrap" style="width: 600px;" >
				       日期：
					<input id="Stime" name="Stime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					至
					<input id="Etime" name="Etime"  class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					&nbsp;查询类别：
					<input id="typeSerc" class="easyui-combobox" style="width:120px;" data-options="    
									       valueField: 'id',    
									       textField: 'text',    									      
									       data: [{
												id: '01',
												text: '按患者所在科室',
												'selected':true 
											},{
												id: '02',
												text: '按执行科室'
											},{
												id: '03',
												text: '医生所在科室'
											}]     
									       " />
						 科室：</td>
					<td  class="newMenu" style="width:160px;z-index:999;position: relative;">
						<div class="deptInput menuInput" style="margin-top:0px;">
							<input id="deptName" class="ksnew"  readonly/>
							<span></span>
						</div>
						<div id="m3" class="xmenu" style="display: none;">
							<div class="searchDept">
								<input type="text" name="searchByDeptName" placeholder="回车查询"/>
								<span class="searchMenu"><i></i>查询</span>
								<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">取消</span>
								</a>						
								<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">清空</span>
								</a>
								<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">确定</span>
								</a>
							</div>
							<div class="select-info" style="display:none">
								<label class="top-label">已选部门：</label>
								<ul class="addDept">
								</ul>
							</div>	
							<div class="depts-dl">
								<div class="addList"></div>
								<div class="tip" style="display:none">没有检索到数据</div>
							</div>	
						</div>
	  				</td>
				<td>
					<shiro:hasPermission name="${menuAlias}:function:query">				       			
					<a href="javascript:void(0)" onclick="searchData()" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:5px;margin-top:5px;">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:set">
					<a href="javascript:clear();void(0)" onclick="" class="easyui-linkbutton" style="margin-left:5px;margin-top:5px;" data-options="iconCls:'reset'">重置</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'" style="margin-left:5px;margin-top:5px;">导出</a>
	 				</td>
	 					<td nowrap="nowrap" style="text-align: right;" >
					<a href="javascript:void(0)" onclick="queryMidday(5)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>
					<a href="javascript:void(0)" onclick="queryMidday(4)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>
					<a href="javascript:void(0)" onclick="queryMidday(7)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>
					<a href="javascript:void(0)" onclick="queryMidday(6)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>
					<a href="javascript:void(0)" onclick="queryMidday(3)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>
					<a href="javascript:void(0)" onclick="queryMidday(2)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>
	 				<a href="javascript:void(0)" onclick="queryMidday(1)"  style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>
	 				</td>
	 			</tr>
	 		</table>
 		</form> 
		<div data-options="region:'center',split:false,border:false" style="width:100%;border:false;height: 95%;">				
			<table id="listFeeBalSum" style="width:100%;border:false;height: 100%;"  >
			</table>
		</div>
	</div>
	<form id="saveForm" method="post"/>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
 $("#m3").on('click',"input[type=checkbox]",function(){
	 $("a[name='menu-confirm-clear']").click();
 });
var columns=[];
var fieldName= new Array();
var titleName= new Array();
var menuAlias="${menuAlias}";
	$(function(){
		//科室下拉
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			menulines:2, //设置菜单每行显示几列（1-5），默认为2
			dropmenu:"#m3",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			haveThreeLevel:false,//是否有三级菜单
			para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		});
		$('#m3 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#deptName').getMenuIds();
		$('a[name=\'menu-confirm-clear\']').click();
		if(flag==''){
			$("#list").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}
	});
function getFieldName(){
	
	fieldName[0]='numberNo';
	fieldName[1]='projectName';
	fieldName[2]='cost';
	fieldName[3]='numberNos';
	fieldName[4]='projectNames';
	fieldName[5]='costs';
	
	titleName[0]='栏';
	titleName[1]='项目';
	titleName[2]='金额';
	titleName[3]='栏';
	titleName[4]='项目';
	titleName[5]='金额';

	var col=getColumns();
	columns.push(col);
	loadGrid();
}
/**
 * datagrid的列
 * @returns {String}
 */
function getColumns(){
	var columns='[';
	var j=150;
	for (var i = 0; i < fieldName.length; i++) {
		if(i==0||i==3){
			var field='{field:"'+fieldName[i]+'",title:"'+titleName[i]+'",width:"100"}';
		}else{
			var field='{field:"'+fieldName[i]+'",title:"'+titleName[i]+'",width:"'+j+'"}';
		}
		
		if(i<fieldName.length-1){
			columns+=field+',';
		}else{
			columns+=field;
		}
	}
	columns+=']';
	columns = eval("(" + columns + ")");
	return columns;
	
}
 function loadGrid(){
	 $("#listFeeBalSum").datagrid({ 
			columns:columns,
			pagination:true,	
			pageSize:20,
			pageList:[20,40,60],
			fit:true,
			method:'post',
			striped:true,
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
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
 }
    /**
	 * 查询
	 * @author  yeguanqun
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2016-6-2
	 * @version 1.0
	 */
	 function searchData(){
		var typeSerc = $('#typeSerc').combobox('getValue');
		var Stime=$("#Stime").val();
		var Etime=$("#Etime").val();
		var deptCode=$('#deptName').getMenuIds();
		if(Stime&&Etime){
		    if(Stime>Etime){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
		if(Stime=='' && Etime!=''){
			$.messager.alert('操作提示', '开始时间不能为空！');
			return;
		}else if(Etime=='' && Stime!=''){
			$.messager.alert('操作提示', '结束时间不能为空！');
			return;
		}else if(Etime!='' && Stime!=''){
			var sDate = new Date(Stime.replace(/\-/g, "\/"));  
			var eDate = new Date(Etime.replace(/\-/g, "\/"));  
			if(sDate>eDate){						
				$.messager.alert('操作提示', '开始时间不能大于结束时间！');
				return;
			}
		}
		if(deptCode==''||deptCode==null){
			$.messager.alert('操作提示', '请选择查询科室');
			return;
		}
		$("#listFeeBalSum").datagrid({ 
			pagination:true,	
			pageSize:20,
			pageList:[20,40,60],
			url:'<%=basePath%>statistics/InpatientFeeBalSum/queryInpatientFeeBalSum.action',
			queryParams: {
				typeSerc:typeSerc,
				sTime:Stime,
				eTime:Etime,
				deptCode:deptCode
			},
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
	 }
 var startTime="${sTime}";//重置开始时间
 var endTime="${eTime}";
$(function(){
    $("#Stime").val("${sTime}");
	$("#Etime").val("${eTime}");
	$('#listFeeBalSum').datagrid({
		 columns:[[    
		           {field:'projectName',title:'项目',width:150},    
		           {field:'cost',title:'金额',width:150},    
		           {field:'projectNames',title:'项目',width:150},    
		           {field:'costs',title:'金额',width:150}    
		       ]],
		pagination:true,	
		pageSize:0,
		pageList:[0,10,20,30],
		fit:true,
		method:'post',
		striped:true,
		border:true,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fitColumns:false,
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
	$("#listFeeBalSum").datagrid('loadData', { total: 0, rows: [] });
});

/**
 * 退出
 */
function out(){
	self.parent.$('#tabs').tabs('close',"在院（出院）病人医药费结算汇总表");
}

//导出列表
function exportList() {
	var typeSerc = $('#typeSerc').combobox('getValue');
	var Stime = $('#Stime').val();
	var Etime = $('#Etime').val();
	if(Stime=='' && Etime!=''){
		$.messager.alert('操作提示', '开始时间不能为空！');
		return;
	}else if(Etime=='' && Stime!=''){
		$.messager.alert('操作提示', '结束时间不能为空！');
		return;
	}else if(Etime!='' && Stime!=''){
		var sDate = new Date(Stime.replace(/\-/g, "\/"));  
		var eDate = new Date(Etime.replace(/\-/g, "\/"));  
		if(sDate>eDate){						
			$.messager.alert('操作提示', '开始时间不能大于结束时间！');
			return;
		}
	}
	var rows = $('#listFeeBalSum').datagrid('getRows');
	if(rows==null||rows==""){
		$.messager.alert("提示", "列表无数据,无法导出！");
		return;
	}
	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
		if (res) {
			$('#saveForm').form('submit', {
				url :"<%=basePath%>statistics/InpatientFeeBalSum/expFeeBalSum.action?typeSerc="+typeSerc+"&sTime="+Stime+"&eTime="+Etime,
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
		var Stime = $('#Stime').val(day);
	    var Etime = $('#Etime').val(day);
	    searchData();
	}else if(val==2){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day2=year+"-"+month+"-"+date;
		var Stime = $('#Stime').val(day2);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day2=year+"-"+month+"-"+date;
	    var Etime = $('#Etime').val(day2);
	    searchData();
	}else if(val==3){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day3=year+"-"+month+"-"+date;
		var Stime = $('#Stime').val(day3);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day3=year+"-"+month+"-"+date;
	    var Etime = $('#Etime').val(day3);
	    searchData();
	}else if(val==4){
		var myDate = new Date();
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		month=month<10?"0"+month:month;
		var day=year+"-"+month+"-"+"01";
		var Stime = $('#Stime').val(day);
		myDate = new Date();
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
		 month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day=year+"-"+month+"-"+date;
	    var Etime = $('#Etime').val(day);
	    searchData();
	}else if(val==5){
		var myDate = new Date();
		var year=myDate.getFullYear();
		var day=year+"-"+"01"+"-"+"01";
		var Stime = $('#Stime').val(day);
		year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		day=year+"-"+month+"-"+date;
	    var Etime = $('#Etime').val(day);
	    searchData();
	}else if(val==6){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day3=year+"-"+month+"-"+date;
		var Stime = $('#Stime').val(day3);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day3=year+"-"+month+"-"+date;
	    var Etime = $('#Etime').val(day3);
	    searchData();
	}else if(val==7){
		var date=new Date();
		var year=date.getFullYear();
		var month=date.getMonth();
		if(month==0){
			year=year-1;
			month=12;
		}
		var startTime=year+'-'+month+'-01';
		 $('#Stime').val(startTime);
		var date=new Date(year,month,0);
		var endTime=year+'-'+month+'-'+date.getDate();
		$('#Etime').val(endTime);
		 searchData();
	}
}
	function clear(){
		$("#Stime").val(startTime);
		$('#Etime').val(endTime);
		$('#typeSerc').combobox('setValue','01');
		$("#listFeeBalSum").datagrid('loadData', { total: 0, rows: [] });
		$("a[name='menu-confirm-clear']").click();
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>   
</body>
</html>