<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>麻醉登记</title>
	<%@ include file="/common/metas.jsp" %>
<script type="text/javascript">
var menuAlias="${menuAlias}";
var bedNoMap ="";
var typeMap ="";
var deptMap = "";//科室名称
var opTypeMap;//手术类型
var zshs=null;//加载临时助手
var aneWayMap = "";//麻醉方式
var userMap = null;
var sexMap=new Map();

$(function(){
	//性别渲染
	$.ajax({
		url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	$.ajax({//渲染麻醉方式
		url:"<%=basePath %>operation/anesthesia/anesthesaneWay.action",
		type:"post",
		success : function(data){
			if(data!=null&&data!=""){
				aneWayMap=data;
			}
		}
	});
	$.ajax({//麻醉医生，麻醉助手
		url:"<%=basePath %>operation/anaerecord/findAnaEmpCombobox.action",
		type:"post",
		success : function(data){
			if(data!=null&&data!=""){
				userMap=data;
			}
		}
	});
	$.ajax({//渲染手术类型
		url:"<%=basePath %>operation/anesthesia/anesthesopTypeMap.action",
		type:"post",
		success : function(data){
			if(data!=null&&data!=""){
				opTypeMap=data;
			}
		}
	});

	bindEnterEvent('no',query,'easyui');
	$.ajax({ //渲染科室名称
		url: "<%=basePath%>outpatient/changeDeptLog/querydeptComboboxs.action", 
		type:"post",
		success : function(data){
			if(data!=null&&data!=""){
				deptMap=data;
			}
		}
	}); 
	

	
/**
 * @Description:手术类型
 * @Author: zhangjin
 * @CreateDate: 2017年2月10日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
	$('#arrangeType').combobox({    
		url:"<%=basePath%>operation/operationList/queryCodeOperatetype.action",
	    valueField:'encode',    
	    textField:'name',
	});
	setTimeout(function(){
		//加载列表
		$('#list').datagrid({
			url: '<%=basePath %>operation/anaerecord/getOperationApplyVoList.action?menuAlias=${menuAlias}',
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 100 ],
			onDblClickRow: function(rowIndex, rowData){
				
				$("#rowId").val(rowData.id);//获取id
				AddOrShowEast('EditForm','<%=basePath %>operation/anaerecord/getOperationApplyById.action?menuAlias='+menuAlias+'&id='+rowData.id);
			}, onLoadSuccess:function(row, data){
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
			},
			onBeforeLoad:function(param){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			}
		});
	},380);
});
/**
 * @Description:提示框自动消失
 * @Author: zhangjin
 * @CreateDate: 2017年2月10日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function alert_autoClose(title,msg,icon){  
	 var interval;  
	 var time=3500;  
	 var x=1;    //设置时间2s
	$.messager.alert(title,msg,icon,function(){});  
	 interval=setInterval(fun,time);  
	        function fun(){  
	      --x;  
	      if(x==0){  
	          clearInterval(interval);  
	  $(".messager-body").window('close');    
	       }  
	}; 
	}

function query(){
	var no = $('#no').textbox('getValue').trim();
	var beganTime = $('#beganTime').val().trim();
	var endTime = $('#endTime').val().trim();
	var arrangeType = $('#arrangeType').combobox('getValue').trim();
	
    $('#list').datagrid('load', {
    	no: no,
    	beganTime:beganTime,
    	arrangeType:arrangeType,
    	endTime:endTime
	});
}
/**
 * @Description:渲染科室患者
 * @Author: huangbiao
 * @CreateDate: 2016年4月12日
 * @param1:value:单元格的值
 * @param2:rowData：行数据
 * @param3:rowIndex:索引
 * @return:科室名称
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function funcindept(value,rowData,rowIndex){
		if(value!=null&&value!=""){
			return deptMap[value];
		}
}

//添加颜色标识
function functionColour(value,row,index){
	if(row.isane=="1"){
		return 'background-color:#ffee01;color:black;';
	}
}
//渲染类别
function functionType(value,rowData,index){
	if(value!=null&&value!=''){
		return opTypeMap[value];
	}
}
//渲染年龄
function funcage(value,rowData,index){
	if(value!=null&&value!=''){
		if(rowData.ageUnit){
			return value+rowData.ageUnit;
		}
		return value;
	}else{
		return "";
	}
}
//渲染性别
function functionSex(value,row,index){
	return sexMap.get(value);
}
//关闭
function close(){
	$('#divLayout').layout('remove','east');
}




function AddOrShowEast(title, url) {
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		$('#divLayout').layout('remove','east');
		$('#divLayout').layout('add', {
			region : 'east',
			width :  "50%",
			height:"100%",
			split : false,
			href : url,
			closable : true
		});
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : "50%",
			height:"100%",
			split : false,
			href : url,
			closable : true
		});
	}
}
/**
 * 重置
 * @author  hedong
 * @date 2017-3-18
 * @version 1.0
 */
 function clearQuery(){
	 $('#no').textbox('setValue',"");
		$('#beganTime').val('');
		$('#endTime').val('');
		$('#arrangeType').combobox('setValue',"");
		$('#list').datagrid('load', {
		});
}
</script>
<style type="text/css">
.textbox {
    padding: 0;
    margin-top: -3px;
}
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> 
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="divLayout" class="easyui-layout" data-options="fit:true">   
		<div data-options="region:'north',split:false" style="height:50px;border-top:0">
			<table style="width:100%;height: 100%">
				<tr>
					<td class="anaerecordList">
						&nbsp;病历号：<input class="easyui-textbox" id="no" name="no" style="margin-top: -3px"/>
						&nbsp;开始时间：<input id="beganTime" name="beganTime" class="Wdate" type="text"  onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}'})" style="width:120px;height:22px ;border: 1px solid #95b8e7;border-radius: 5px;margin-top: 7px"/>
						&nbsp;结束时间：<input id="endTime" name="endTime" class="Wdate" type="text"  onClick="WdatePicker({minDate:'#F{$dp.$D(\'beganTime\')}'})" style="width:120px;height:22px ;border: 1px solid #95b8e7;border-radius: 5px;margin-top: 7px"/>
						&nbsp;手术类型：<input class="easyui-combobox" id="arrangeType" name="arrangeType"/>
						<shiro:hasPermission name="${menuAlias}:function:query">
						  &nbsp;<a href="javascript:query();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-top: -3px">查询</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard">
							<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'" style="margin-top: -3px">读卡</a>
						</shiro:hasPermission>
        				<shiro:hasPermission name="${menuAlias}:function:readIdCard">
        					<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'" style="margin-top: -3px">读身份证</a>
						</shiro:hasPermission>
						<a href="javascript:void(0)" onclick="clearQuery()" class="easyui-linkbutton" iconCls="reset" style="margin-top: -3px">重置</a>
						<input type='hidden' id='rowId'>
						&nbsp;
						<span style="height:14px;line-height:11px;display:inline-block;background-color:#ffee01">&nbsp;&nbsp;</span>
					<span style="font-size:14px" class="tip">表示已麻醉</span>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" style="height:80%;width:100%;">
			<table id="list" style="width:100%;" class="easyui-datagrid" data-options="idField:'id',border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,fit:true">
				<thead>
					<tr>
					    <th data-options="field:'orderId',width:24,align:'center',styler:functionColour" ></th>
						<th data-options="field:'id',hidden:'true',align:'center'" >id</th>
						<th data-options="field:'patientNo',width:'15%',align:'center'"  >病历号</th>
						<th data-options="field:'name',width:'20%',align:'center'">患者姓名</th>
						<th data-options="field:'sex',width:'10%',align:'center'" formatter="functionSex" >性别</th>
						<th data-options="field:'age',width:'10%',align:'center'" formatter="funcage">年龄</th>
						<th data-options="field:'inDept',width:'20%',align:'center'" formatter="funcindept" >住院科室</th>
						<th data-options="field:'bedNo',width:'8%',align:'center'" >床号</th>
						<th data-options="field:'opType',width:'15%',align:'center'" formatter="functionType" >手术类型</th>
					</tr>
				</thead>
			</table>
		</div>
		
	</div>  
</body>
</html>