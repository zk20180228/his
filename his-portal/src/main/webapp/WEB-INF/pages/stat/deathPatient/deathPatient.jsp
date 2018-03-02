<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>患者死亡信息统计</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var flag;//全局科室
var sexMap=new Map();
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
	flag=$('#queryDept').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$("#tableList").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		$('#tableList').datagrid();
		
	}
  $('#sex').combobox({    
	    data:[{"id":1,"text":"男"},{"id":2,"text":"女"},{"id":3,"text":"未知"}],    
	    valueField:'id',    
	    textField:'text',
	    editable:false
		});
	//性别下拉框渲染
	$('#sex').combobox({
		url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
		valueField : 'encode',
		textField : 'name',
		multiple : false
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
//查询性别
$.ajax({
	url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=sex',
	type:'post',
	success: function(data) {
		sexMap=data;
	}
});

//查询按钮
 function searchFrom(){
	 var deptCode = $('#queryDept').getMenuIds();
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 var sexId = $('#sex').textbox('getValue');
	 $('#tableList').datagrid({    
			url: '<%=basePath%>statistics/deathPatient/queryDeathPatient.action',
			queryParams: {
				pageSize:20,
				pageList:[20,40,60,100],
				deptCode: deptCode,
				sex: sexId,
				startTime:startTime,
				endTime:endTime,
				menuAlias:menuAlias
			}
		}); 
 }
 
//重置按钮
 function clear(){
	$('#startTime').val("${startTime }");
	$('#endTime').val("${endTime }");
 	$('#queryDept').val('');
	$('#queryDept').attr('name','');
	$('#sex').val('');
	$('#sex').textbox('setValue','');
 	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
 }
 
//替换字符
	function formatSex(val){
		if(val == 1 || val=='M'){
			return '男';
		}
		if(val == 2 || val=='F'){
			return '女';
		}
		if(val == 3 || val=='U'){
			return '其他';
		}
	}
	function formatInCircs(val){
		if(val == 1){
			return '危急';
		}
		if(val == 2){
			return '急';
		}
		if(val == 3){
			return '一般';
		}
	}
</script>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	 	<form id="saveForm" action="" style="height:80px;width: 100%;padding-top: 8px;padding-top: 8px;padding-right: 10px;padding-left: 10px;">
	 		<table border="0" style="width: 100%;height: 10%">
	 			<tr>
 				<!-- 开始时间 --> 
					<td style="width:80px;" align="left">出生日期:</td>
					<td style="width:110px;">
						<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:10px;" align="center">-</td>
					<td style="width:110px" id="td1">
						<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
						<td style="width: 60px;" >
					&nbsp;科室:
					</td>
					<td  class="newMenu" style="width:160px;z-index:1;position: relative;">
						<div class="deptInput menuInput" style="margin-top:0px;">
							<input id="queryDept" class="ksnew"  readonly/>
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
								<div class="addList">
								</div>
							</div>	
							<div class="tip" style="display:none">没有检索到数据</div>		
						</div>
					</td>
					<td>
						性别:<input class="easyui-combobox"id="sex" name="sex"/>
		    			<shiro:hasPermission name="${menuAlias}:function:query">
			    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			 			</shiro:hasPermission>
			 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
		    		</td>
	    		</tr>
	    	 	<tr>
	    	 		<td align="center" colspan="8"  ><font style = "font-size: 32px !important;" class="empWorkTit" size="3">患者死亡信息统计</font></td>
	    	 	</tr>
	    	 </table>
	    </form>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 87%;">
	    	<table id="tableList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'patientNo',width:'8%'">病历号</th>
						<th data-options="field:'deptName',width:'12%'">科室名称</th>
						<th data-options="field:'patientName',width:'8%'">姓名</th>
						<th data-options="field:'sex',width:'8%'" formatter="formatSex">性别</th>
						<th data-options="field:'birthday',width:'10%'">出生日期</th>
						<th data-options="field:'age',width:'8%'">年龄</th>
						<th data-options="field:'inDate',width:'10%'">入院时间</th>
						<th data-options="field:'inCircs',width:'8%'" formatter="formatInCircs">入院情况</th>
						<th data-options="field:'diagName',width:'10%'">诊断名称</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
</body>
</html>
