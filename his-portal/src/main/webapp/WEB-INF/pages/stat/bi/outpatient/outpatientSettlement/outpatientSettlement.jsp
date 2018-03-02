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
<script type="text/javascript">
$(function(){
	//科室维度
	$('#dept').combobox({
		url:'<%=basePath%>bi/passengers/findAllDept.action',
		valueField: 'deptCode',    
	    textField: 'deptName',  
		multiple:true,
		onSelect:function(data){
			
		}
	});
	//统计大类维度
	$('#casminfee').combobox({
		url:'<%=basePath%>bi/settlement/casminfeeCombobox.action',
		valueField: 'encode',    
	    textField: 'name',  
		multiple:true,
		onSelect:function(data){
			
		}
	});
	//时间 （年度）维度
	$('#times').combobox({
        data:[{    
            "value":1,    
            "text":"年"
        },{    
            "value":2,    
            "text":"季度"   
        },{    
            "value":3,    
            "text":"月"   
        },{    
            "value":4,    
            "text":"日"   
        }],
	    valueField: 'value',    
        textField: 'text',
        onSelect:function(data){
        	$('#s1').text(data.text);
        	$('#s2').text(data.text);
        }
	});
});

//查询
function queryLists(){
// 	var deptName = $('#dept').combobox('getText');
// 	var region = $('#region').combobox('getText');
// 	var times = $('#times').combobox('getText');
// 	var start = $('#start').textbox('getText');
// 	var end = $('#end').textbox('getText');
// 	if(start<end){
// 		$.messager.alert("操作提示","对不起，结束时间不能大于开始时间");
// 		return;
// 	}
	
	var nameArray=$('#dept').combobox('getValues');
	var nameString='';
	var columnsArray=new Array();
	var timeone=$('#start').numberbox('getValue');
	var timetwo=$('#end').numberbox('getValue');
	var minuse=timetwo-timeone+1;
	var stringfield='';
	var stringfield2='';
	var num=0;//for循环的次数，即时间维度的起始截止时间差
	if(minuse<0){
	$.messager.alert('提示','起始时间不得大于截止时间');
	return;
	}else{
		stringfield='{field:'+'\'name\',width:'+'\'200px\',align:'+'\'center\',rowspan:2}';
		for(var i=0;i<minuse;i++){
			if(stringfield!=''&&stringfield!=null){
				stringfield =stringfield+',';
			}
			var time=parseInt(timeone)+parseInt(i);
			stringfield += '{field:"'+i+'",title:"'+time+'",width:'+'\'300px\',align:'+'\'center\',colspan:3}';
			num=i+1;
		}
		for(var i=0;i<minuse;i++){
			var a=eval(timeone+"+"+i);
			if(stringfield2!=''&&stringfield2!=null){
				stringfield2 =stringfield2+',';
			}
			stringfield2 += '{field:"'+a+'1'+'",title:'+'\'总人次\',width:'+'\'100px\',align:'+'\'center\'},{field:"'+a+'2'+'",title:'+'\'急诊人次\',width:'+'\'100px\',align:'+'\'center\'},{field:"'+a+'3'+'",title:'+'\'普诊人次\',width:'+'\'100px\',align:'+'\'center\'}';
		}
		stringfield='['+stringfield+']';
		stringfield2='['+stringfield2+']';
		stringfield =eval(stringfield);
		stringfield2 =eval(stringfield2);
		columnsArray.push(stringfield);
		columnsArray.push(stringfield2);
	}
	for(var i=0;i<nameArray.length;i++){
		if(nameString!=null&&nameString!=''){
			nameString +=',';
		}
		nameString +=nameArray[i];
	}
	$('#table1').datagrid({
		columns:columnsArray,
		url:'<%=basePath%>bi/passengers/toList.action'
		//queryParams:{'timeone':timeone,'timetwo':timetwo,'nameString':nameString}
	});
}

function queryList(){
	var deptName = "神内一门诊,中医一门诊";
	var casminfee = $('#casminfee').combobox('getText');
	var times = $('#times').combobox('getText');
	var start = $('#start').textbox('getText');
	var end = $('#end').textbox('getText');
	var columnsArray=new Array();
	var stringfield = "";
	if(deptName!=""){
		stringfield+='{field:'+'\'dept\',title:'+'\'科室\',width:'+'\'100px\',align:'+'\'center\'}';
	}
	if(casminfee!=""){
		if(stringfield!=""){
			stringfield = stringfield +",";
		}
		stringfield+='{field:'+'\'codeName\',title:'+'\'费别\',width:'+'\'100px\',align:'+'\'center\'}';
	}
	if(start!=""&&end!=""){
		if(stringfield!=""){
			stringfield = stringfield +",";
		}
		stringfield+='{field:'+'\'years\',title:'+'\'年\',width:'+'\'100px\',align:'+'\'center\'}';
	}
	
	if(stringfield!=""){
		stringfield = stringfield +",";
	}
	stringfield = stringfield+'{field:'+'\'sumPeople\',title:'+'\'费用\',width:'+'\'100px\',align:'+'\'center\'},{field:'+'\'sumtotcosts\',title:'+'\'比例\',width:'+'\'100px\',align:'+'\'center\'}';
	stringfield='['+stringfield+']';
	stringfield =eval(stringfield);
	columnsArray.push(stringfield); 
	$('#table1').datagrid({
		url:'<%=basePath%>bi/settlement/toList.action',
		queryParams:{"dimensionVO.dept":deptName,"dimensionVO.start":start,"dimensionVO.end":end,"dimensionVO.codeName":casminfee,"dimensionVO.years":times},
		columns:columnsArray,
        onLoadSuccess: function (data) {//默认选中
			$('#table1').datagrid("autoMergeCells", ['dept','codeName','years']);
		},
	});
}

/**
 * 合并单元格
 */
$.extend($.fn.datagrid.methods, {
	autoMergeCells: function (jq, fields) {
		return jq.each(function () {
			var target = $(this);
			if (!fields) {
				fields = target.datagrid("getColumnFields");
			}
			var rows = target.datagrid("getRows");
			var i = 0,
			j = 0,
			temp = {};
			for (i; i < rows.length; i++) {
				var row = rows[i];
				j = 0;
				for (j; j < fields.length; j++) {
					var field = fields[j];
					var tf = temp[field];
					if (!tf) {
						tf = temp[field] = {};
						tf[row[field]] = [i];
					} else {
						var tfv = tf[row[field]];
						if (tfv) {
							tfv.push(i);
						} else {
							tfv = tf[row[field]] = [i];
						}
					}
				}
			}
			$.each(temp, function (field, colunm) {
				$.each(colunm, function () {
				var group = this;
					if (group.length > 1) {
						var before,
						after,
						megerIndex = group[0];
						for (var i = 0; i < group.length; i++) {
							before = group[i];
							after = group[i + 1];
							if (after && (after - before) == 1) {
							    continue;
							}
							var rowspan = before - megerIndex + 1;
							if (rowspan > 1) {
								target.datagrid('mergeCells', {
									index: megerIndex,
									field: field,
									rowspan: rowspan
								});
							}
							if (after && (after - before) != 1) {
							    megerIndex = after;
							}
						}
					}
				});
			});
		});
	}
});

</script>
</head>
<body>
<div  class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="height:50px;">
    	<table>
			<tr>
				<td style="font-size:14px" >科室：<input id="dept" class="easyui-combobox" />  
				&nbsp;&nbsp;&nbsp;&nbsp;收费类别：<input id="casminfee" class="easyui-combobox" />
				&nbsp;&nbsp;&nbsp;&nbsp;时间：<input id="times" class="easyui-combobox" />
				<input id="start" class="easyui-numberbox" /><span id="s1"></span>&nbsp;<span>至</span>&nbsp;
				<input id="end" class="easyui-numberbox" /><span id="s2"></span>&nbsp; 
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 50px" >查&nbsp;询&nbsp;</a>
				</td>
			</tr>
		</table>
    </div>   
    <div data-options="region:'center'">
    	<table id="table1" data-options="fti:true"></table>
    </div>   
</div> 
</body>
</html>