<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 0px solid #95b8e7;width:100%;
	}
	.tableCss td{
		border: 0px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel1{
		text-align: left;
		padding: 10px 0px;
		width:180px;
	}
</style>
<body>

<div id="cc" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="height:100px;padding:5">
    	<form id="nuiLeaveEditForm" method="post" >
					<input type="hidden"  id="leaveFlagHidden" name="leaveFlagHidden" />
					<input type="hidden"  id="leaveid" name="id" />
					<input value="${mid }"  type="hidden" id="inpatientNo" name="inpatientNo" /> 
					<table style="width:30%;" class="tableCss">
					 <tr>
						<td style="border:0px">
							<shiro:hasPermission name="${menuAlias}:function:save">
							<a  onclick="saveNuiLeave()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
							</shiro:hasPermission>
						</td>
					    <td  style="border:0px" >
							<shiro:hasPermission name="${menuAlias}:function:set">
						    <a  onclick="editNuiLeave()" class="easyui-linkbutton" data-options="iconCls:'icon-sick'">销&nbsp;假&nbsp;</a>
						    </shiro:hasPermission>
					    </td>
					</tr> 
						<tr >
							<td class="TDlabel" style="width:60px">请假天数:</td>
							<td style="width:60px"><input class="easyui-numberbox"  id="leaveDays"  name="leaveDays" data-options="required:true,min:1" /> </td>
							<td class="TDlabel" style="width:60px">请假原因:</td>
						<td >
							<!-- <input class="easyui-textbox"  id="remark"  name="remark" data-options="required:true" /> -->
							<textarea id="bz" class="easyui-validatebox" rows="1" cols="80" id="remark" name="remark"   data-options="required:true,multiline:true"></textarea>
						</td>
						</tr>
					</table>
		</form>
    </div>   
    <div data-options="region:'center'">
		<table id="nuiLeaveList" style="width:100%;height:85%" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false">
			<thead>
				<tr>
					<th data-options="field:'inpatientNo',width:'10%',align:'center'">病历号</th>
					<th data-options="field:'leaveDays',width:'10%',align:'center'">请假天数</th>
					<th data-options="field:'doctCode',width:'10%',align:'center'" >给假医生</th>
					<th data-options="field:'operCode',width:'10%',align:'center'" >请假操作人</th>
					<th data-options="field:'leaveFlag',width:'10%',align:'center',formatter:functionLeaveFlag" >假条状态</th>
					<th data-options="field:'leaveDate',width:'12%',align:'center'">请假时间</th>
					<th data-options="field:'remark',width:'20%',align:'center'">请假原因</th>
				</tr>
			</thead>
		</table>
    </div>   
</div> 
		
<script type="text/javascript">
$(function(){
	var inpatNo=$('#inpatientNo').val();
	$('#nuiLeaveList').datagrid({
		url: '<%=basePath %>nursestation/nuiLeave/getNuiLeaveByPaiName.action',
		pagination: true,
		queryParams:{inpatNo:inpatNo},
		onClickRow:function(rowIndex, rowData){
			$('#leaveFlagHidden').val(rowData.leaveFlag);
		},onLoadSuccess : function(data){
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
	
})
		


//请假的表单提交
function saveNuiLeave(){
	$('#nuiLeaveEditForm').form('submit',{ 
    	url:"<%=basePath %>/nursestation/nuiLeave/saveNuiLeave.action?a=1",  
    	onSubmit:function(){
			if (!$('#nuiLeaveEditForm').form('validate')) {
				$.messager.progress('close');
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			$.messager.progress({text:'正在处理请假，请稍等...',modal:true});
    	},
    	success:function(data){
    		$.messager.progress('close');
        	var map = eval("("+data+")");
        	if(map.mag=="success"){
        		$.messager.confirm('打印假条', '是否打印假条?', function(res) {  //提示是否打印假条
        			if (res){
        				var id=map.code;
            			var timerStr = Math.random();
            			window.open ("<c:url value='/iReport/iReportPrint/iReportToNurseView.action?randomId='/>"+timerStr+"&id2="+id+"&fileName=ZYBRQJD",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
    					};
        		 })
        		var inpatientNoi = $('#inpatientNo').val();
        		$("#nuiLeaveList").datagrid('reload',{inpatNo:inpatientNoi});
        		addNuiLeave();
        	}else if(map.mapsmall=="small"){
        		$.messager.alert('提示','婴儿不允许请假！');
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        	}else if(map.maptimein=="timein"){
        		$.messager.alert('提示','上次请假时间未到，请先销假！');
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        	}else if(map.mapwarn=="warn"){
        		$.messager.confirm('确认', '该患者已经欠费，是否允许请假?', function(res) {//提示是否允许请假
    				if (res) {
    					$.messager.progress({text:'正在处理请假，请稍等...',modal:true});
    					$('#nuiLeaveEditForm').form('submit',{
    						url:"<%=basePath %>/nursestation/nuiLeave/savewNuiLeave.action",
    						success:function(data){
    							$.messager.progress('close');
    							var mapNui = eval("("+data+")");
    							if(mapNui.mag=="success"){
    								$.messager.confirm('打印假条', '是否打印假条?', function(res) {  //提示是否打印假条
    				        			if (res){
    				        				var id=mapNui.code;
    				        				var timerStr = Math.random();
    				        				window.open ("<c:url value='/iReport/iReportPrint/iReportToNurseView.action?randomId='/>"+timerStr+"&id2="+id+"&fileName=ZYBRQJD",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
    				    					};
    				        		 })
    				        		 var inpatientNoi = $('#inpatientNo').val();
    				        		$("#nuiLeaveList").datagrid('reload',{inpatNo:inpatientNoi});
    				        		addNuiLeave();
    							}else{
    								$.messager.progress('close');
    								$.messager.alert('提示','保存失败！');
    							}
    						}
    					})
    					
    				}
    			});
        	}else{
        		$.messager.progress('close');
        		$.messager.alert('提示','保存失败！');
        	}
        },
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');
		}							         
	}); 
}


//销假
function editNuiLeave(){
 	var rows = $('#nuiLeaveList').datagrid('getSelections');
	var leaveflag = $('#leaveFlagHidden').val();
	  if (rows.length == 0) {
		  $.messager.alert('提示','请单击一行信息修改！');
		  setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	  }else if(rows.length>1){
		  $.messager.alert('提示','只能选择一条信息！');
		  setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	  }else if(leaveflag==0){
		  $.messager.confirm('确认', '确定要销假吗?', function(res){//提示是否销假
				if (res){
 					var id = rows[0].id;
					var inpatientNoi=$('#inpatientNo').val();
					$.messager.progress({text:'正在处理请假，请稍等...',modal:true});
					$.ajax({                                                        
						url: "<%=basePath %>nursestation/nuiLeave/saveNuiLeave.action?id="+id+"&a=2",
						type:'post',
						success: function(map2) {
							if(map2.mags=="success"){
								$.messager.progress('close');
								$.messager.alert('提示','销假成功!');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
				        		$("#nuiLeaveList").datagrid('reload',{inpatNo:inpatientNoi}); 
				        		addNuiLeave();
				        	}else{
				        		$.messager.progress('close');
				        		$.messager.alert('提示','销假失败!');
				        	} 
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示','销假失败!');
						}							         
						
					});
				};
		  })
     }else if(leaveflag==1){
    	 $.messager.alert('提示','该假条已销假!');
    	 setTimeout(function(){
				$(".messager-body").window('close');
		},3500);
     }else{
    	 $.messager.alert('提示','该假条已销假!');
    	 setTimeout(function(){
				$(".messager-body").window('close');
		},3500);
     }
}
//清空信息
function addNuiLeave(){
	$('#leaveid').val("");
	$('#leaveDays').textbox('clear');
	$('#bz').val('');
}

//请假的状态渲染
function functionLeaveFlag(value,row,index){
	if(value==0){
		return "请假";
	}else if(value==1){
		return "销假";
	}else{
		return "作废";		
	}
}
</script>
</body>
</html>