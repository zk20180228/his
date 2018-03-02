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
<title>患者入出院信息查询</title>
<style type="text/css">
	#tt .panel>div {
		overflow:hidden; 
	} 

</style>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var flag;//科室信息
$(function(){
	//对比指定某年，某几个连续的月		
	var time1=$("#Stime").val();//开始时间
	var time2=$("#Etime").val();//结束时间
	var time=time1.substring(0,4)+"年"+time1.substring(5,7)+"~"+time2.substring(5,7)+"月";
	var timeDate = time1.replace(/-/g,"/");
	var lastDate = new Date(timeDate);
	lastDate.setFullYear(lastDate.getFullYear()-1);
	var lastTime=lastDate.getFullYear()+"年"+time1.substring(5,7)+"~"+time2.substring(5,7)+"月";
	$("#beginYear").html(lastTime);
	$("#endYear").html(time);

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
	if(flag==''||flag==null){
		$('#inPatientMsg').datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		$('#outPatientMsg').datagrid({
			columns:[[ 
			          {field:'patientno',title:'住院号',width:'6%'},
			          {field:'name',title:'姓名',width:'5%'},
			          {field:'sex',title:'性别',width:'5%',formatter:sexFormatter},
			          {field:'age',title:'年龄',width:'4%',formatter:ageFomatter},
			          {field:'bedNumber',title:'床位',width:'6%'},
			          {field:'doctor',title:'主治医师',width:'6%'},
			          {field:'nurse',title:'主管护士',width:'6%'},
			          {field:'indate',title:'入院日期',width:'10%'},
			          {field:'outdate',title:'出院日期',width:'10%'},
			          {field:'status',title:'出院情况',width:'5%'},
			          {field:'pact',title:'费别',width:'8%'},
			          {field:'clinic',title:'诊断',width:'21%'},
			]],
		});
		$('#getinPatientMsg').datagrid({
			columns:[[ 
			          {field:'patientno',title:'住院号',width:'6%'},
			          {field:'name',title:'姓名',width:'4%'},
			          {field:'sex',title:'性别',width:'4%',formatter:sexFormatter},
			          {field:'pact',title:'费别',width:'8%'},
			          {field:'beforeDept',title:'转前科室',width:'8%'},
			          {field:'beforeBedNo',title:'转前床位',width:'5%'},
			          {field:'afterDept',title:'转后科室',width:'8%'},
			          {field:'afterBedNo',title:'转后床位',width:'5%'},
			          {field:'inDate',title:'入院日期',width:'10%'},
			          {field:'turnDate',title:'转科日期',width:'10%'},
			          {field:'clinic',title:'诊断',width:'25%'},
			]],
		});
		$('#getoutPatientMsg').datagrid({
			columns:[[ 
			          {field:'patientno',title:'住院号',width:'6%'},
			          {field:'name',title:'姓名',width:'4%'},
			          {field:'sex',title:'性别',width:'4%',formatter:sexFormatter},
			          {field:'pact',title:'费别',width:'8%'},
			          {field:'beforeDept',title:'转前科室',width:'8%'},
			          {field:'beforeBedNo',title:'转前床位',width:'6%'},
			          {field:'afterDept',title:'转后科室',width:'8%'},
			          {field:'afterBedNo',title:'转后床位',width:'6%'},
			          {field:'inDate',title:'入院日期',width:'10%'},
			          {field:'turnDate',title:'转科日期',width:'10%'},
			          {field:'clinic',title:'诊断',width:'23%'},
			]],
		});

		var startTime=$('#Stime').val();
		var endTime=$('#Etime').val();
		if(startTime&&endTime){
		    if(startTime>endTime){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
		var depts = $('#deptName').getMenuIds();
		if(depts==''){
			depts=flag
		}
		$('#inPatientMsg').datagrid({
			url:'<%=basePath%>statistics/outAndInPatient/queryInPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias,
		});

		$('#tt').tabs({
		    border:false,
		    onSelect:function(title,index){
		    	var startTime=$('#Stime').val();
		    	var endTime=$('#Etime').val();
		    	var depts = $('#deptName').getMenuIds();
				if(depts==''){
					depts=flag
				}
		    	if(index == 0){
		    		$('#inPatientMsg').datagrid({
		    			url:'<%=basePath%>statistics/outAndInPatient/queryInPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias+'&timer='+new Date().getTime(),
		    		});
		    	}else if(index == 1){
		    		$('#outPatientMsg').datagrid({
		    			url:'<%=basePath%>statistics/outAndInPatient/queryOutPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias+'&timer='+new Date().getTime(),
		    		});
		    	}else if(index == 2){
		    		$('#getinPatientMsg').datagrid({
		    			url:'<%=basePath%>statistics/outAndInPatient/queryGetInPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias+'&timer='+new Date().getTime(),
		    		});
		    	}else{
		    		$('#getoutPatientMsg').datagrid({
		    			url:'<%=basePath%>statistics/outAndInPatient/queryGetOutPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias+'&timer='+new Date().getTime(),
		    		});
		    	}
		    }
		});
		
	}
	//查询性别
	$.ajax({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=sex',
		type:'post',
		success: function(data) {
			sexMap=data;
		}
	});
});
//性别渲染
function sexFormatter(value,row,index){
	if(value!=null&&value!=''){
		if(sexMap[value]){
			return sexMap[value];
		}else{
			return value;
		}
	}
}
/**
* 重置方法
*/
function clear(){
	$('#Stime').val("${startTime}");
	$('#Etime').val("${endTime}");
	$('#deptName').val('');
	$('#deptName').attr('name','');
	$('#team').textbox('setValue',"");
	$("#inPatientMsg").datagrid('loadData', { total: 0, rows: [] });
	$("#outPatientMsg").datagrid('loadData', { total: 0, rows: [] });
	$("#getinPatientMsg").datagrid('loadData', { total: 0, rows: [] });
	$("#getoutPatientMsg").datagrid('loadData', { total: 0, rows: [] });
}

function searchFrom(){
	var startTime=$('#Stime').val();
	var endTime=$('#Etime').val();
	if(startTime==''||endTime==''){
	    if(startTime>endTime){
	      $.messager.alert("提示","时间不能为空");
	      return ;
	    }
	  }
	var depts = $('#deptName').getMenuIds();
	if(depts==''){
		depts=flag;
	}
	var index ; 
	$(".tabs-wrap .tabs li").each(function(i,v){
		if($(v).hasClass("tabs-selected")){
			index = i
		}
	})
	if(index == 0){
		$('#inPatientMsg').datagrid({
			url:'<%=basePath%>statistics/outAndInPatient/queryInPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias,
			method:'post'
		});
	}else if(index == 1){
		$('#outPatientMsg').datagrid({
			url:'<%=basePath%>statistics/outAndInPatient/queryOutPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias,
			method:'post'
		});
	}else if(index == 2){
		$('#getinPatientMsg').datagrid({
			url:'<%=basePath%>statistics/outAndInPatient/queryGetInPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias,
			method:'post'
		});
	}else{
		$('#getoutPatientMsg').datagrid({
			url:'<%=basePath%>statistics/outAndInPatient/queryGetOutPatientMsg.action?startTime='+startTime+'&endTime='+endTime+'&dept='+depts+'&menuAlias='+menuAlias,
			method:'post'
		});
	}
}
//导出
function exportList(){
	var startTime=$('#Stime').val();
	var endTime=$('#Etime').val();
	var depts = $('#deptName').getMenuIds();
	if(depts==''){
		depts=flag;
	}
	var index ; 
	$(".tabs-wrap .tabs li").each(function(i,v){
		if($(v).hasClass("tabs-selected")){
			index = i
		}
	});
	var flag
	if(index == 0){//入院患者信息
		flag= flagExpor('inPatientMsg');
	}else if(index == 1){//出院患者信息
		flag= flagExpor('outPatientMsg');
	}else if(index == 2){//转入元患者信息
		flag= flagExpor('getinPatientMsg');
	}else{//转出院患者信息
		flag= flagExpor('getoutPatientMsg');
	}
	if(flag){
		$('#saveForm').form('submit', {
			url :'<%=basePath%>statistics/outAndInPatient/exprotList.action',
			queryParams:{startTime:startTime,
						endTime:endTime,
						dept:depts,
						index:index,
						menuAlias:menuAlias
			},
			onSubmit:function(param){
				return true;
			},
			success : function(data) {
				$.messager.alert("提示", "导出失败！");
			},
			error : function(data) {
				$.messager.alert("提示", "导出失败！");
			}
		});
	}else{
		$.messager.alert('提示','当前列表中无数据,无法提供打印功能!')
	}
}
function flagExpor(id){
	var row= $('#'+id).datagrid('getRows').length;
	if(row>0){
		return true;
	}else{
		return false
	}
}
function ageFomatter(value,row,index){
	if(value!=null&&value!=''){
		return row.age+row.ageUnit;
	}
	 
}
</script>
<body class="easyui-layout" data-options="fit:true">
	<div id="divLayout"  class="easyui-layout" data-options="fit:true" style="width: 100%; height: 20%;padding:0px">
			<form id="saveForm" action="" style="height:38px;padding:8px 5px 0px 5px;background-color: white;">
				<table id="searchTab" style="width: 100%;" border="0">
					<tr>
						<!-- 开始时间 --> 
						<td style="width:80px;" align="left">开始时间:</td>
						<td style="width:110px;">
							<input id="Stime" class="Wdate" type="text" name="Stime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'Etime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<!-- 结束时间 --> 
						<td style="width:80px;" align="center">结束时间:</td>
						<td style="width:110px" id="td1">
							<input id="Etime" class="Wdate" type="text" name="Etime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'Stime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td style="width: 50px">科室:</td>
							 <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
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
									<ul class="addDept"></ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
							</div>
		    		</td>
						<td>
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="margin-top:-3px">查询</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:set">
							<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset" style="margin-top:-3px">重置</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:export">
							<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" iconCls="icon-down" style="margin-top:-3px">导出</a>
						</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</form>
			
		<div data-options="region:'center',title:'患者入出转信息查询'" style="width: 100%;height: 94%;">
			<div id="tt" class="easyui-tabs" style="width: 100%;height: 100%;" data-options="fit:true,tabPosition:'bottom',border:false">   
			    <div title="入院患者信息" style="width: 100%;height: 100%;" data-options="fit:true,border:false">   
			        <p align="center" style="text-align:center;font-size:30px;">入院患者信息</p>
			        <table id="inPatientMsg" class="easyui-datagrid" style="width: 100%;height:95%;" data-options="border:false,singleSelect:true,rownumbers:true,pagination:true,pageNumber:1,pageList:[20,30,50],pageSize:20">
			        <thead>
				        <tr>
		          			<th data-options="field:'patientno',width:'6%'" >住院号</th>
		          			<th data-options="field:'name',width:'6%'" >姓名</th>
		          			<th data-options="field:'sex',width:'4%',formatter:sexFormatter" >性别</th>
		          			<th data-options="field:'age',width:'4%',formatter:ageFomatter" >年龄</th>
		          			<th data-options="field:'bedNumber',width:'5%'" >床位</th>
		          			<th data-options="field:'indate',width:'12%'" >入院时间</th>
		          			<th data-options="field:'adress',width:'26%'" >地址</th>
		          			<th data-options="field:'tel',width:'10%'" >电话</th>
		          			<th data-options="field:'pact',width:'8%'" >费别</th>
		          			<th data-options="field:'clinic',width:'18%'" >诊断</th>
		          		</tr>
		          	</thead>
					</table>
			    </div>   
			    <div title="出院患者信息" style="width: 100%;height: 100%;" data-options="fit:true,border:false">   
			       <p align="center"  style="text-align:center;font-size:30px;">出院患者信息</p>
			        <table id="outPatientMsg" class="easyui-datagrid"  style="width: 100%;height:95%;" data-options="fit:true,border:false,singleSelect:true,rownumbers:true,pagination:true,pageNumber:1,pageList:[20,30,50],pageSize:20">
			        	
					</table>
			    </div>   
			    <div title="转入患者信息" style="width: 100%;height: 100%;" data-options="fit:true,border:false">   
			       <p align="center"  style="text-align:center;font-size:30px;">转入院患者信息</p>
			        <table id="getinPatientMsg" class="easyui-datagrid"  style="width: 100%;height:95%;" data-options="fit:true,border:false,singleSelect:true,rownumbers:true,pagination:true,pageNumber:1,pageList:[20,30,50],pageSize:20" >
			        	
					</table>
			    </div>   
			    <div title="转出患者信息" style="width: 100%;height: 100%;" data-options="fit:true,border:false">   
			          <p align="center" style="text-align:center;font-size:30px;">转出院患者信息</p>
			          <table id="getoutPatientMsg" class="easyui-datagrid"  style="width: 100%;height:95%;" data-options="fit:true,border:false,singleSelect:true,rownumbers:true,pagination:true,pageNumber:1,pageList:[20,30,50],pageSize:20" >
			          	
			          </table>
			    </div>   
			</div> 
	</div>
</body>

</html>