<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div class="easyui-layout"  fit=true>  
	<div id="addData-window" data-options="region:'center',border:false,fit:true" style="position:relative;overflow: auto;">
			<div style="margin:13px 30px;position:absolute;">
			<form id="editForm" method="post">			
				<table style="width: 100%;" cellSpacing=10px cellPadding=10px border=0>
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><span style="margin-left:29px;">频次：</span></td>
						 <td style="border:none;"><input id="freTdId" class="easyui-textbox" readonly="readonly" style="width:170px;" value="${inpatientOrder.frequencyName }"/></td>
					</tr>
				</table>
				<table style="width: 100%;" cellSpacing=10px cellPadding=10px border="0">
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><span style="margin-left:57px;">时间</span></td>
						 <td style="border:none;"><span style="margin-left:59px;">用量</span></td>
					</tr>		
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><input id="speFreTime1" name="speFreTime1" class="easyui-timespinner" style="width:140px;" value="${arytimes1 }"/></td>
						 <td style="border:none;"><input id="speFreNum1" class="easyui-textbox" data-options="editable:true" style="width:140px;" value="${arydose1 }"/></td>
					</tr>	
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><input id="speFreTime2" name="speFreTime2" class="easyui-timespinner" style="width:140px;" value="${arytimes2 }"/></td>
						 <td style="border:none;"><input id="speFreNum2" class="easyui-textbox" data-options="editable:true" style="width:140px;" value="${arydose2 }"/></td>
					</tr>
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><input id="speFreTime3" name="speFreTime3" class="easyui-timespinner" style="width:140px;" value="${arytimes3 }"/></td>
						 <td style="border:none;"><input id="speFreNum3" class="easyui-textbox" data-options="editable:true" style="width:140px;" value="${arydose3 }"/></td>
					</tr>
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><input id="speFreTime4" name="speFreTime4" class="easyui-timespinner" style="width:140px;" value="${arytimes4 }"/></td>
						 <td style="border:none;"><input id="speFreNum4" class="easyui-textbox" data-options="editable:true" style="width:140px;" value="${arydose4 }"/></td>
					</tr>
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;"><input id="speFreTime5" name="speFreTime5" class="easyui-timespinner" style="width:140px;" value="${arytimes5 }"/></td>
						 <td style="border:none;"><input id="speFreNum5" class="easyui-textbox" data-options="editable:true" style="width:140px;" value="${arydose5 }"/></td>
					</tr>		
				</table>			
				<div style="margin:21px 83px;position:absolute;width:70%;" >		    
			    	<a id="submitSpeFre" href="javascript:submitSpeFre();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>&nbsp;&nbsp;&nbsp;
			    	<a href="javascript:closeSpeFreLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			    </div>
			</form>						
			</div>
		</div> 		

</div>
<script type="text/javascript">
	$(function(){
		var rowA = $('#infolistA').datagrid('getSelected');
		var row = $('#infolistB').datagrid('getSelected');	
		if(row!=null||(rowA!=null&&rowA.itemType=='2')){
			$('#speFreNum1').attr('readonly',true);
			$('#speFreNum2').attr('readonly',true);
			$('#speFreNum3').attr('readonly',true);
			$('#speFreNum4').attr('readonly',true);
			$('#speFreNum5').attr('readonly',true);
		}
		
	});
	function submitSpeFre(){
		var speFreTime1=$('#speFreTime1').timespinner('getValue');
		var speFreTime2=$('#speFreTime2').timespinner('getValue');
		var speFreTime3=$('#speFreTime3').timespinner('getValue');
		var speFreTime4=$('#speFreTime4').timespinner('getValue');
		var speFreTime5=$('#speFreTime5').timespinner('getValue');
		var speFreNum1=$("#speFreNum1").textbox('getValue').trim();
		var speFreNum2=$("#speFreNum2").textbox('getValue').trim();
		var speFreNum3=$("#speFreNum3").textbox('getValue').trim();
		var speFreNum4=$("#speFreNum4").textbox('getValue').trim();
		var speFreNum5=$("#speFreNum5").textbox('getValue').trim();		
		var speFreTime = null;
		if(speFreTime1!=''&&speFreTime1!=null){
			speFreTime=speFreTime1;
		}
		if(speFreTime2!=''&&speFreTime2!=null){
			speFreTime=speFreTime+','+speFreTime2;
		}
		if(speFreTime3!=''&&speFreTime3!=null){
			speFreTime=speFreTime+','+speFreTime3;
		}
		if(speFreTime4!=''&&speFreTime4!=null){
			speFreTime=speFreTime+','+speFreTime4;
		}
		if(speFreTime5!=''&&speFreTime5!=null){
			speFreTime=speFreTime+','+speFreTime5;
		}
		var speFreNum = null;
		if(speFreNum1!=''&&speFreNum1!=null){
			speFreNum=speFreNum1;
		}
		if(speFreNum2!=''&&speFreNum2!=null){
			speFreNum=speFreNum+','+speFreNum2;
		}
		if(speFreNum3!=''&&speFreNum3!=null){
			speFreNum=speFreNum+','+speFreNum3;
		}
		if(speFreNum4!=''&&speFreNum4!=null){
			speFreNum=speFreNum+','+speFreNum4;
		}
		if(speFreNum5!=''&&speFreNum5!=null){
			speFreNum=speFreNum+','+speFreNum5;
		}
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='长期医嘱'){
			var row = $('#infolistA').datagrid('getSelected');
			if(row.id!=null){
				var index = getIndexForAdDgListA();
				if(index>=0){
					if(row!=null&&row.combNo!=null&&row.combNo!=''){
						var rows = $('#infolistA').datagrid('getRows');
						var id = 0;
						var nom = 0;
						for(var i=0;i<rows.length;i++){
							if(rows[i].combNo==row.combNo){
								nom+=1;
								if(rows[i].id==null||rows[i].id==''){
									id=1;
								}
								var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
								$('#infolistA').datagrid('updateRow',{
									index: indexRow,
									row: {
										execTimes:speFreTime,
										execDose:speFreNum,
										changeNo:1
									}
								});
							}
						}
						$.ajax({ 
					    	url: '<%=basePath%>inpatient/docAdvManage/saveSpeFreInfo.action',	
					    	data:'inpatientOrder.id='+row.id+'&inpatientOrder.execTimes='+speFreTime+'&inpatientOrder.execDose='+speFreNum,
					    	type:'post',
					        success:function(data){ 
					        	if(data='success'){
					        		$('#adSpeFreData-window').window('close');
					        		$.messager.alert('提示','保存成功!'); 
					        		setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$('#infolistA').datagrid('reload');
								}else{
									$.messager.alert('提示','保存失败!');									
								}
					        }										         
					    });
					}else{								
						$('#infolistA').datagrid('updateRow',{
							index: index,
							row: {
								execTimes:speFreTime,
								execDose:speFreNum,
								changeNo:1
							}
						});
						$('#adSpeFreData-window').window('close');
						$.messager.alert('提示',"特殊频次设置成功!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
				}
				
			}else{
				var index=getIndexForAdDgListA();
		    	if(index>=0){							
					$('#infolistA').datagrid('updateRow',{
					index: index,
						row: {								
							execTimes:speFreTime,
							execDose:speFreNum
						}
					});
				}
		    	$.messager.alert('提示',"特殊频次设置成功!");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
		    	$('#adSpeFreData-window').window('close');
		    	return;
			}		
		}else if(tab.title=='临时医嘱'){
			var row = $('#infolistB').datagrid('getSelected');
			if(row.id!=null){
				var index = getIndexForAdDgList();
				if(index>=0){
					if(row!=null&&row.combNo!=null&&row.combNo!=''){
						var rows = $('#infolistB').datagrid('getRows');
						var id = 0;
						var nom = 0;
						for(var i=0;i<rows.length;i++){
							if(rows[i].combNo==row.combNo){
								nom+=1;
								if(rows[i].id==null||rows[i].id==''){
									id+=1;
								}
								var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
								$('#infolistB').datagrid('updateRow',{
									index: indexRow,
									row: {
										execTimes:speFreTime,
										execDose:speFreNum,
										changeNo:1
									}
								});
							}
						}
						$.ajax({ 
					    	url: '<%=basePath%>inpatient/docAdvManage/saveSpeFreInfo.action',	
					    	data:'inpatientOrder.id='+row.id+'&inpatientOrder.execTimes='+speFreTime+'&inpatientOrder.execDose='+speFreNum,
					    	type:'post',
					        success:function(data){ 
					        	if(data='success'){
					        		$('#adSpeFreData-window').window('close');
					        		$.messager.alert('提示','保存成功!');
					        		setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$('#infolistB').datagrid('reload');
								}else{
									$.messager.alert('提示','保存失败!');									
								}
					        }										         
					    });
					}else{								
						$('#infolistB').datagrid('updateRow',{
							index: index,
							row: {
								execTimes:speFreTime,
								execDose:speFreNum,
								changeNo:1
							}
						});
						$('#adSpeFreData-window').window('close');
						
						$.messager.alert('提示',"特殊频次设置成功!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
				}
				
			}else{
				var index=getIndexForAdDgList();
		    	if(index>=0){							
					$('#infolistB').datagrid('updateRow',{
					index: index,
						row: {								
							execTimes:speFreTime,
							execDose:speFreNum
						}
					});
				}
		    	$('#adSpeFreData-window').window('close');
		    	$.messager.alert('提示',"特殊频次设置成功!");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
		    	return;
			}
		}		
	}


</script>
</body>
</html>