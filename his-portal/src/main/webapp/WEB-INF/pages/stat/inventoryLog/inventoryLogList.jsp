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
	var deptMap=new Map();
	var empMap=new Map();
	var packunit= new Map();
	$(function(){
		$('#STime').val("${Stime}");
		$('#ETime').val("${Etime}");
		var winH=$("body").height();
		//查询科室map
		$.ajax({
			url:'<%=basePath%>baseinfo/department/getDeptMap.action',
			async:false,
			success:function(datavalue){
				deptMap=datavalue;
			}
		});	
		//查询人员map
		$.ajax({
			url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
			async:false,
			success:function(datavalue){
				empMap=datavalue;
			}
		});	
		
		//查询单位map
		$.ajax({
			url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=packunit',
			async:false,
			success:function(datavalue){
				for(var i=0;i<datavalue.length;i++){
					packunit.put(datavalue[i].encode,datavalue[i].name);
				}
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
			queryParams:{Stime:null,Etime:null,drug:null},
			url:'<%=basePath %>statistics/InventoryLog/queryInventoryLog.action',
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

		/**
		 * 科室下拉框
		 * @author  zpty
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2016-06-22
		 * @version 1.0
		 */
		$('#dept').combobox({    
				url :"<%=basePath %>statistics/InventoryLog/departmentComboboxRegister.action",
				valueField : 'deptCode',
				textField : 'deptName',
				multiple : false,
				mode:'local',
				onSelect: function(){
					searchFrom(1);
				},
			    filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'deptCode';
					keys[keys.length] = 'deptName';
					keys[keys.length] = 'deptPinyin';
					keys[keys.length] = 'deptWb';
					keys[keys.length] = 'deptInputCode';
					return filterLocalCombobox(q, row, keys);
				}
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
			url :"<%=basePath %>statistics/InventoryLog/drugNameComboboxRegister.action",
			valueField : 'code',
			textField : 'name',
			mode:'local',
			multiple : false,
			onSelect: function(){
				searchFrom(1);
			},
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'drugNamepinyin';
				keys[keys.length] = 'drugCommonname';
				keys[keys.length] = 'drugBasicwb';
				return filterLocalCombobox(q, row, keys);
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
				if(Stime!=null&&Stime!=""){
				  Stime=Stime+" 00:00:00";
				  }
			    if(Etime!=null&&Etime!=""){
			      Etime=Etime+" 23:59:59";
			      }
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
			  if(Stime!=null&&Stime!=""){
				  Stime=Stime+" 00:00:00";
				  }
		      if(Etime!=null&&Etime!=""){
			      Etime=Etime+" 23:59:59";
			      }
    	 }
			var dept = $('#dept').combobox('getValue');
			var drug = $('#drug').combobox('getValue');
			$('#countList').datagrid('load',{
				Stime: Stime,
				Etime: Etime,
				dept: dept,
				drug: drug
			});
		}
     
		//导出列表
		function exportList() {
			//开始时间
			var Stime = $('#STime').val();
			//结束时间
			var Etime = $('#ETime').val();
			var drug = $('#drug').combobox('getValue');
			var dept = $('#dept').combobox('getValue');
			var rows = $('#countList').datagrid('getRows');
			if(rows==null||rows==""){
				$.messager.alert("提示", "列表无数据无法导出！");
				return;
			}
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {
						url :"<%=basePath%>statistics/InventoryLog/expInvLogList.action",
						queryParams:{Stime:Stime,Etime:Etime,drug:drug,dept:dept},		
						success : function(data) {
							$.messager.alert("操作提示", "导出失败！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					});
				}
			});
		}
	
		
	function deptFunction(value,row,index){
		if(value!=null&&value!=""){
			return deptMap[value];
		}
	}
	
	
	function empFunction(value,row,index){
		if(value!=null&&value!=""){
			return empMap[value];
		}
	}
	
	//渲染单位以及包装单位
	function unitFunction(value,row,index){
		if(value!=null&&value!=""){
			if(packunit.get(value)!=null&&packunit.get(value)!=""){
				return packunit.get(value);
			}else{
				return value;
			}
		}
	}
	
	
	/**  
	 *  
	 * @Description：过滤	
	 * @Author：zhangjin
	 * @CreateDate：2016-11-29
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}
	function clear(){
		$('#STime').val("${Stime}");
		$('#ETime').val("${Etime}");
		$('#dept').combobox('setValue',"${dept}");
		$('#drug').combobox('setValue',"${drug}");
		searchFrom(1);
	}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="divLayout"  class="easyui-layout" data-options="fit:true" >
		 <div data-options="region:'north',border:false" style="height:70px;padding: 5px 5px 1px 5px">
				<div>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="searchFrom(1)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 0px">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:set">
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'" style="margin-left: 3px">导出</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(4);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(5);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(8);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(7);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(3);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(2);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:searchFrom(6);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
					</shiro:hasPermission>
				</div>
				<div style="padding-top: 8px;">
					日期：
					<input id="STime" name="STime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					至
					<input id="ETime" name="ETime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			       	盘点科室：
					<input  class="easyui-combobox" id="dept" style="width: 120px;" name="dept" value="${dept}" />
					药品名称：
					<input  class="easyui-combobox" id="drug" style="width: 130px;" name="drug" value="${drug}" />
				</div>
		</div>
		 <div data-options="region:'center',border:false" style="height:90%;padding: 3px 0px 0px 0px;">
				<table id="countList"  data-options="fit:true,method:'post'">
					<thead>
						<tr>
							<th data-options="field:'deptName',formatter:deptFunction" width="12%" align="center">盘点科室</th>
							<th data-options="field:'tradeName'" width="17%" align="center">盘点药品</th>
							<th data-options="field:'batchNo'" width="5%" align="right" halign="center">批号</th>
							<th data-options="field:'specs'" width="9%" align="center">规格</th>
							<th data-options="field:'retailPrice'" width="5%"align="right" halign="center">零售价</th>
							<th data-options="field:'packUnit',formatter:unitFunction" width="5%" align="center">包装单位</th>
							<th data-options="field:'packQty'" width="5%" align="right" halign="center">包装数量</th>
							<th data-options="field:'adjustNum'" width="5%" align="right" halign="center">盘存数量</th>
							<th data-options="field:'adjustUnit',formatter:unitFunction" width="5%" align="center">单位</th> 
							<th data-options="field:'placeNo'" width="5%" align="right" halign="center">货位号</th>
							<th data-options="field:'createtime'" width="13%" align="center">操作时间</th>
							<th data-options="field:'userName',formatter:empFunction" width="10%" align="center">操作人</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/></form>
		</div>
	</div>
	</body>
</html>
