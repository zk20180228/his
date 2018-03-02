<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div class="easyui-layout" style="width:100%;height:100%;" fit=true>  
		<div id="addData-window" style="position:relative;">  
			<div style="margin:28px 30px;position:absolute;">
			<form id="editForm" method="post">			
				<table style="width: 100%;" cellSpacing=10px cellPadding=10px border=0>
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;">时间：</td>
						 <td style="border:none;">
						 	<input id="advStopTime" class="Wdate" type="text" name="startTime" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
					<tr style="padding: 8px 8px 8px 8px;">
						 <td style="border:none;">原因：</td>
						 <td style="border:none;"><input id="advStopReason" class="easyui-combobox" style="width:180px;" data-options="
						 						 valueField: 'id',    
										         textField: 'text',    									      
										         data: [{
													  id: '01',
													  text: '正常停止',
													  'selected':true
												  },{
													  id: '02',
													  text: '取消'
												  },{
													  id: '03',
													  text: '不适应'
												  }]      
										         " /></td>
					</tr>					
				</table>			
				<div style="margin:27px 63px;position:absolute;width:70%;" >		    
			    	<a id="save" href="javascript:submitStop();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>&nbsp;
			    	<a href="javascript:closeStopLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			    </div>
			</form>						
			</div>
		</div> 		

</div>
<script type="text/javascript">
		$(function(){	
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){
				$('#advStopTime').val(nowTimeStop());
			}else if(tab.title=='临时医嘱'){
				$('#advStopTime').val(nowTimeStop());
			}
		});

		//获取当前时间
		function nowTimeStop(){
		   var day=new Date();
		   var Year=0;
		   var Month=0;
		   var Day=0;
		   var Hour = 0;
		   var Minute = 0;			
		   var CurrentDate="";				
		   //初始化时间
		   Year       = day.getFullYear();
		   Month      = day.getMonth()+1;
		   Day        = day.getDate();
		   Hour       = day.getHours();
		   Minute     = day.getMinutes();			  
		   CurrentDate =  Year + "-";
		   if (Month >= 10 )
		   {
		    CurrentDate = CurrentDate + Month + "-";
		   }else{
		    CurrentDate = CurrentDate + "0" + Month + "-";
		   }
		   if (Day >= 10 )
		   {
		    CurrentDate = CurrentDate + Day ;
		   }else{
		    CurrentDate = CurrentDate + "0" + Day ;
		   }
		   if(Hour >=10)
		   {
		    CurrentDate = CurrentDate + " " + Hour ;
		   }else{
		    CurrentDate = CurrentDate + " " + "0" + Hour ;
		   }
		   if(Minute >=10)
		   {
		    CurrentDate = CurrentDate + ":" + Minute ;
		   }else{
		    CurrentDate = CurrentDate + ":0" + Minute ;
		   }      			  			  
		   return CurrentDate;
		}
		
		
		//医嘱停止
		function submitStop(){ 
			var dateTime=nowTimeStop();
			var advStopTime=$('#advStopTime').val();
			if(advStopTime==null||advStopTime==""){
				$.messager.alert('提示','请先填写停止时间!');
				return;
			}
			var advStopReasonId=$("#advStopReason").combobox('getValue');
			var advStopReason=$("#advStopReason").combobox('getText');	
			var timeFlag="";			
			if(advStopTime>=dateTime){	
				if(advStopTime==dateTime){
					timeFlag=0;
				}else{
					timeFlag=1;
				}
				$.extend($.messager.defaults,{  
			        ok:"是",  
			        cancel:"否"  
			    });
				$.messager.confirm('确认','停止时间：'+advStopTime+'，您确认操作吗？',function(r){
					if (r){
						$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
						 var qq = $('#ttta').tabs('getSelected');				
						 var tab = qq.panel('options');
						 if(tab.title=='长期医嘱'){
							var rows = $('#infolistA').datagrid('getRows');
							var ids = '';
							for ( var i = 0; i < rows.length; i++) {
								if (rows[i].id == null) {//如果id为null 则为新添加行
								} else {
									if(ids != null && ids != ""){
										ids += ",";
									}
									ids += rows[i].id;
								}
							}
							 $.ajax({ 
						    	url: '<%=basePath%>inpatient/docAdvManage/stopAdvice.action',	
						    	data:'timeFlag='+timeFlag+'&advStopTime='+advStopTime+'&advStopReasonId='+advStopReasonId+'&advStopReason='+advStopReason+'&adviceJson='+ids,
						    	type:'post',
						        success:function(data){ 
						        	if(data=='success'){
						        		$.messager.progress('close');	// 如果提交成功则隐藏进度条
						        		$('#adStopData-window').window('close');
	 									$.messager.alert('提示','停止成功!');
	 									$('#infolistA').datagrid('reload');
	 								}else{
	 									$.messager.alert('提示','停止失败!');
	 								}
						        }
						    });
						 }else if(tab.title=='临时医嘱'){
							 var rows = $('#infolistB').datagrid('getRows');
							 var ids = '';
								for ( var i = 0; i < rows.length; i++) {
									if (rows[i].id == null) {//如果id为null 则为新添加行
									} else {
										if(ids != null && ids != ""){
											ids += ",";
										}
										ids += rows[i].id;
									}
								}
							 $.ajax({ 
						    	url: '<%=basePath%>inpatient/docAdvManage/stopAdvice.action',	
						    	data:'timeFlag='+timeFlag+'&advStopTime='+advStopTime+'&advStopReasonId='+advStopReasonId+'&advStopReason='+advStopReason+'&adviceJson='+ids,
						    	type:'post',
								success:function(data){ 
						        	if(data=='success'){
						        		$.messager.progress('close');	// 如果提交成功则隐藏进度条
						        		$('#adStopData-window').window('close');
						        		$.messager.alert('提示','停止成功!'); 
	 									$('#infolistB').datagrid('reload');
	 								}else{
	 									$.messager.alert('提示','停止失败!');
	 								}
						        }										         
						    });
						 }
						 
					}
				});
			}else{
				$.messager.alert("操作提示", "停止时间不可以小于当前时间！");
			}	
		}
</script>
</body>
</html>