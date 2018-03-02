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
	<title>住院次数维护</title>
	<script type="text/javascript">
var deptMapq="";//科室
var payMap=new Map();//结算类别
var bedMap="";//床号
/**  
 *  
 * @Description：渲染科室	
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
$.ajax({
	url: "<%=basePath%>outpatient/changeDeptLog/querydeptComboboxs.action", 
	type:'post',
	async:true,
	success: function(deptData) {
		deptMapq = deptData;
	}
});
$(function(){
	bindEnterEvent('medicalrecordId',searchFrom,'easyui');
	/**  
	 *  
	 * @Description：获取床号	
	 * @Author：zhangjin
	 * @CreateDate：2017-2-17
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	$.ajax({
		url:"<%=basePath%>inpatient/InpatientNumber/getBedinfoId.action ",
		type:"post",
		success:function(data1){
		bedMap=data1;
		}
	});
	/**  
	 *  
	 * @Description：结算类别	
	 * @Author：zhangjin
	 * @CreateDate：2017-2-17
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	$.ajax({
	    url:  "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
		type:'post',
		success: function(data) {
			var type = data;
			for(var i=0;i<type.length;i++){
				payMap.put(type[i].encode,type[i].name);
			}
		}
	});
	
/**  
	 *  
	 * @Description：数据加载
	 * @Author：zhangjin
	 * @CreateDate：2017-2-17
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	$("#numberList").datagrid({
		pageSize:25,
		pageList:[25,30,50,100],
		pagination:true,
		pagePosition:'bottom',
		fitColumns:true,
		url:"<%=basePath%>inpatient/InpatientNumber/getNumberList.action",
		method:"post",
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
			if(data.total=='0'){
				if($("#medicalrecordId").textbox("getValue")!=null&&$("#medicalrecordId").textbox("getValue")!=""){
					$.messager.alert('提示','请核查病历号!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return ;
				}
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
				$('#medicalrecordId').textbox('setValue',data);
				searchFrom();
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
				$('#medicalrecordId').textbox('setValue',data);
				searchFrom();
			}
		});
	};
	/*******************************结束读身份证***********************************************/

/**  
 *  
 * @Description：初始化	
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
 function popWinToOpCombine() {
	 $.messager.confirm('提示','初始化时需要先删除表中的数据再进行初始化，且初始化时间比较长。确定初始化吗?',function(r){    
		    if (r){ 
		    	$.messager.progress({text:'初始化中，请稍后...',modal:true});
		    	 $.ajax({
		    		 url:"<%=basePath%>inpatient/InpatientNumber/getInpatientNumber.action",
		    		 type:"post",
		    		 success:function(data){
		    			 if(data=="ok"){
		    				 $.messager.progress("close");
		    				 $.messager.alert("提示","初始化成功！");
		    			 }else{
		    				 $.messager.progress("close");
		    				 $.messager.alert("提示","初始化成失败！");
		    			 }
		    		 }
		    	 });
		    }    
		});  
 }
 /**  
 *  
 * @Description：渲染科室	
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
 function funcdept(value,row,index){
	 if(value){
		 return deptMapq[value];
	 }
	 return "";
 }
 /**  
 *  
 * @Description：渲染结算类别	
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
 function funcpayking(value,row,index){
	 if(value!=null&&value!=""){
		 return payMap.get(value);
	 }
	 return "";
 }
 
 //比较时间
	function compareTime(sTime,endTime){
		var d1 = new Date(sTime.replace(/\-/g, "\/")); 
		var d2 = new Date(endTime.replace(/\-/g, "\/"));
		if(d1>d2){
			return false;
		}else{
			return true;
		}
	}
 /**  
 *  
 * @Description：查询	
 * @Author：zhangjin
 * @CreateDate：2017-2-17
 * @version 1.0
 * @throws IOException 
 *
 */ 
function searchFrom(){
	var medicalrecordId=$("#medicalrecordId").textbox("getValue");
	var beganTime=$("#beganTime").val();
	var endTime=$("#endTime").val();
	if(!compareTime(beganTime,endTime)){
		$.messager.alert('操作提示', '住院时间晚于出院时间！'); 
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
       return false; 
	}
	
	$("#numberList").datagrid('load',{
		medicalrecordId:medicalrecordId,
		beganTime:beganTime,
		endTime:endTime
	});
}
</script>
</head>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
		<div data-options="region:'north',split:false,border:false" style="height:43px;width: 60%">
			<table style="width:100%;height:30px;padding: 8px 5px 0px">
    			<tr class="inpatientNumberSize">
    				<td style="width: 300px">病历号：<input id="medicalrecordId" class="easyui-textbox" />
    				入院时间：
    				<input id="beganTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
    				出院时间：
    				<input id="endTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
    				<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
    				<shiro:hasPermission name="${menuAlias}:function:readCard">
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					</shiro:hasPermission>
    				<shiro:hasPermission name="${menuAlias}:function:readIdCard">
		        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					</shiro:hasPermission>
    				</td>
    			</tr>
    		</table>
		</div>
		<div data-options="region:'center',border:false"style="width: 99%;height:99%;" >
			<table class="easyui-datagrid" id="numberList" data-options="fit:true,toolbar:'#toolbarId'">   
				<thead>   
					<tr>   
						<th data-options="field:'name',width:100" align="center">姓名</th>
						<th data-options="field:'deptCode',align:'right',formatter:funcdept"align="center" style="width: 10%">住院科室</th> 
						<th data-options="field:'medicalrecordId',width:100" align="center" style="width: 10%">病历号</th>   
						<th data-options="field:'idcardNo',width:100" align="center" style="width: 10%">就诊卡号</th> 
						<th data-options="field:'inpatientNo',width:100" align="center" style="width: 10%">住院流水号</th>   
						<th data-options="field:'caseNo',width:100,align:'right'" align="center" style="width: 10%">病案号</th>
						<th data-options="field:'inDate',width:100,align:'right'" align="center" style="width: 10%">入院时间</th>
						<th data-options="field:'outDate',width:100,align:'right'" align="center" style="width: 10%">出院时间</th>
						<th data-options="field:'paykindCode',width:100,align:'right',formatter:funcpayking" align="center">结算类别</th>
					</tr>   
				</thead>   
			</table>
		</div>   
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:initialization">
			<a href="javascript:void(0)" onclick="popWinToOpCombine()" class="easyui-linkbutton" data-options="iconCls:'icon-init'">初始化</a>
		</shiro:hasPermission>
	</div>
</body>
</html>