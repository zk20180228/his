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
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-layout-method-load.js"></script>   
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:95%;" fit=true>  
			<div>							
					<table id="patientinfo" class="honry-table" style="width: 100%;align:center;">
						<thead> 
						<tr>						
							<td class="honry-lable" width="50%">姓名 ：</td>
							<td id="hzpatientName" width="50%">${inpatientInfo.patientName}</td>
						</tr>	
						<tr>						
							<td class="honry-lable" width="50%">警戒线 ：</td>
							<td id="hzmoneyAlert" width="50%"></td>
						</tr>				
						<tr>
							<td class="honry-lable" width="50%">预交金 ：</td>
							<td id="hzprepayCost" width="50%"></td>
						</tr>					
						<tr>
							<td class="honry-lable" width="50%">自费总额 ：</td>
							<td id="hzownCost" width="50%"></td>
						</tr>
						<tr>
							<td class="honry-lable" width="50%">费用总额 ：</td>
							<td id="hztotCost" width="50%"></td>
						</tr>					
						<tr>
							<td class="honry-lable" width="50%">余额 ：</td>
							<td id="hzfreeCost" width="50%"></td>
						</tr>	
						<tr>
							<td class="honry-lable" width="50%">本次费用 ：</td>
							<td id="hzthisCost" width="50%"></td>
						</tr>					
						</thead>
					</table>								
			</div>
			<div style="margin:18px 55px;position:absolute;width:80%;" >	
				<div style="margin-left:-8px;">该患者余额不足，不能进行收费！</div>
				<div style="margin-top:5px;">请补交<span id="arrearageCost"></span>元</div>	    
			    <div style="margin-top:17px;">
			    	<a id="submitAudit" href="javascript:submitAudit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
			    	<a href="javascript:closeAuditLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			    </div>
			</div>
			<input id="userId" value="${user.id }" type="hidden"/>
			
			<input id="idbobob" value="${idbobob }" type="hidden"/>
			<input id="orderInfoId" value="${orderInfoId }" type="hidden"/>
		   <input id="qtyCount" value="${qty}" hidden/>
		   <input id="opId" value="${operationId}" hidden/>
	</div>
	<div id="confirmPassword-window" style="width: 350px;height:300px"> 
		<div style="margin:28px 50px;width: 180px;height: 30px">
			<input type="text" style="width:97%;" id="passwordSerc"  class="easyui-textbox" data-options="prompt:'请输入登录密码'" />
		</div> 
		<div style="margin:0px 50px;width: 180px;height: 50px">
			<a id="submitPassword" href="javascript:submitPassword();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>&nbsp;&nbsp;
			<a href="javascript:void(0);" onclick="closePasswordLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		 </div>
	</div>
	<input id="hiddenDays" type="text" value="" hidden/>
<script type="text/javascript">
     //将金额格式为两位小数
	var hzmoneyAlert=Number("${inpatientInfo.moneyAlert}");
	$("#hzmoneyAlert").html(hzmoneyAlert.toFixed(2));
	var hzprepayCost=Number("${inpatientInfo.prepayCost}");
	$("#hzprepayCost").html(hzprepayCost.toFixed(2));
	var hzownCost=Number("${inpatientInfo.ownCost}");
	$("#hzownCost").html(hzownCost.toFixed(2));
	var hztotCost=Number("${inpatientInfo.totCost}");
	$("#hztotCost").html(hztotCost);
	var hzfreeCost=Number("${inpatientInfo.freeCost}");
	$("#hzfreeCost").html(hzfreeCost.toFixed(2));
	var hzthisCost=Number("${thisTotCost}");
	$("#hzthisCost").html(hzthisCost.toFixed(2));
	var arrearageCost=Number("${arrearageCost}");
	$("#arrearageCost").html(arrearageCost.toFixed(2));
			
	
	/**
	 * 校验密码弹出框
	 * @author  yeguanqun
	 * @date 2016-4-15
	 * @version 1.0
	 */	
	function submitAudit(){
		AdddilogModel("confirmPassword-window","密码校验","",'280px','180px');
		/**
		 * 校验密码、密码校验成功则再次进行费用处理回车键查询
		 * @author  yeguanqun
		 * @date 2016-4-15
		 * @version 1.0
		 */	
	 	bindEnterEvent('passwordSerc',submitPassword,'easyui');
	}
	/**
	 * 校验密码、密码校验成功则再次进行费用处理
	 * @author  yeguanqun
	 * @date 2016-4-15
	 * @version 1.0
	 */	
	function submitPassword(){
		var userId = $('#userId').val();
		var passwordSerc = $('#passwordSerc').val();
		if(passwordSerc==''||passwordSerc==null){
			$.messager.alert('提示',"密码不能为空！");	
			return;
		}
		var days=$("#idbobob").val();
		var orderInfoId=$("#orderInfoId").val();
		var qty=$("#hhidden").val();
		 var opId= $("#opId").val();//手术序号
		$.ajax({
				url: '<%=basePath%>inpatient/exitNofee/confirmPassword.action',
				data:'user.id='+userId+'&user.password='+passwordSerc,
				type:'post',
				success: function(data) {
					if(data.resMsg=='error'){
						$.messager.alert('提示',data.resCode);
					}
					else if(data.resMsg=='success'){
						var arrearageId = $('#arrearageId').val();
						if(arrearageId=='hszsf'){							
							var stringNurseCharge =$("#stringNurseCharge").val();
							$("#ym").layout('loading');
							$.ajax({
								url:"<%=basePath%>nursestation/nurseCharge/nurseSaveOrUpdate.action",
 			 			        data:{stringNurseCharge:stringNurseCharge,goon:1},
			 					type:'post',
			 					success: function(data) {
			 						var dataMap = data;
						   			if(dataMap.resCode =="error"){
						   				$("#ym").layout('loaded');
						   				$.messager.alert('提示',dataMap.resMsg);			   				
						        		return;
						        	}else if(dataMap.resCode=="success"){
						        		$("#ym").layout('loaded');
						        		$.messager.alert('提示',dataMap.resMsg);	
						        		var nono=$("#inpatientNo").val();
						        		var recipeNo=dataMap.recipeNo3;//处方号
						        		var recipeNo2=dataMap.recipeNo2;//处方号（不重复）
						        		var unName=dataMap.name;//库存不足的项目
						        		var drugname=dataMap.drugname;//药品库存不足项目
 	 					        		var strs= new Array();
 	 					        		strs=unName.split(",");//非药品name
 	 					        		var strname= new Array();
 	 					        		strname=drugname.split(",");
 	 					        		if(unName!=null&&unName!=""){
 	 					        			for(i=0;i<strname.length;i++){
 	 					        				$.messager.alert("提示",strname[i],'info');
 	 					        			}
 	 					        		}
 	 					        		var itemId=dataMap.itemId;//非药品id
 	 					        		var medId=dataMap.medId;//药品id
		 		 	 					/**收费后刷新页面
 	 					        		*/
 	 					        		$("#listzj").datagrid({//已收费信息
 	 			  	   				    	url:"<%=basePath%>nursestation/nurseCharge/dayCharge.action",
 	 			  	   				        queryParams:{inpatientNo:nono,recipeNo2:recipeNo2,itemId:itemId,medId:medId},
 	 			  	   				        method:"post"
 	 			  	   				    });
 	 					        		if(itemId!=""||medId!=""){
 	 					        			 var itemIds= new Array();
 	 							        		itemIds=itemId.split(",");//非药品name
 	 							        		var medIds= new Array();
 	 							        		medIds=medId.split(",");//药品name
 	 						        			//删除收费信息
 	 							        		var dataArr = new Array();
 	 							        		var en=$("#listyz").datagrid("getRows");
 	 							        		var len = en.length;
 	 									    	for(var i=0;i<en.length;i++){
 	 									    		for(var a=0;a<itemIds.length;a++){
 	 									    			if(en[i].id==itemIds[a]){
 	 										    			dataArr[dataArr.length] = en[i];
 	 									    			}
 	 									    		}
 	 									    		$("#listyz").datagrid("deleteRow",0);
 	 											}
 	 									    	for(var i=0;i<en.length;i++){
 	 									    		for(var a=0;a<medIds.length;a++){
 	 									    			if(en[i].id==medIds[a]){
 	 										    			dataArr[dataArr.length] = en[i];
 	 									    			}
 	 									    		}
 	 									    		$("#listyz").datagrid("deleteRow",0);
 	 											}
 	 					        		}
 	 					        		
						        		//删除收费信息
						        		var dataArr = new Array();
						        		var en=$("#listyz").datagrid("getRows");
						        		var len = en.length;
								    	for(var i=0;i<len;i++){
								    		for(var a=0;a<strs.length;a++){
								    			if(en[0].undrugName==strs[a]){
									    			dataArr[dataArr.length] = en[0];
								    			}
								    		}
								    		$("#listyz").datagrid("deleteRow",0);
										}
								    	if(dataArr.length>0){
								    		for(var i=0;i<dataArr.length;i++){
								    			$('#listyz').datagrid('appendRow',dataArr[i]);
								    		}
								    	}
		 		 	 				    var inpatientNo=$("#admNo1").val();//病历号
		 		 	 				    var dept=$("#deptId").val();//当前科室
		 		 	 				    $.ajax({//刷新患者信息
		 		 	 				    	url:"<%=basePath%>nursestation/nurseCharge/queryNurseChargeInpinfo.action",
		 		 	 				    	data:{inpatientNo:inpatientNo,dept:dept},
		 		 	 				    	type:"post",
		 		 	 				    	success: function(data) {
		 		 	 				    		$("#freeCost").text((data[0].freeCost).toFixed(2));
		 		 	 				    	}
		 		 	 				    });
			 		 	 				  $.ajax({//药品出库申请
			 		 	 					 url:"<%=basePath%>nursestation/nurseCharge/nurseDrug.action",
							 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
							 			        type:'post',
			 		 	 				  });
			 		 	 			     $.ajax({//物资出库申请
						 			        url:"<%=basePath%>nursestation/nurseCharge/nursewz.action",
						 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
						 			        type:'post',
						 			        success:function(data){
						 			        	
						 			        }
			 		 	 				}); 
						        		closePasswordLayout();
						        		closeAuditLayout();
						        		return;
						        	}
						        	else{
						        		$("#ym").layout('loaded');
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        		return;
						        	}	
			 					}
							});
						}
						if(arrearageId=='hszyzgl'){
							$.ajax({
			 					url: '<%=basePath%>business/resolveOrder.action',
			 					data:{
			 						goon:1,
			 						orderInfoId:orderInfoId,
									idbobob:days,
									countQty:qty
			 					},
			 					type:'post',
			 					success: function(data) {
			 						var dataMap = data;
			 						if(dataMap.noExec!=null){
			 							$.messager.alert('提示',dataMap.noExec);
			 							closePasswordLayout();
						        		closeAuditLayout();
			 							return;
			 						}
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);
						   				closePasswordLayout();
						        		closeAuditLayout();
						        		return;
						        	}else if(dataMap.resMsg=="success"){
						        		$.messager.alert('提示',dataMap.resCode);	
						        		closePasswordLayout();
						        		closeAuditLayout();
						        		return;
						        	}
						        	else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        		return;
						        	}	
			 					}
							});
						}
						if(arrearageId=='zysspf'){
							var stringNurseCharge =$("#stringNurseCharge").val(); 
							var opId= $("#opId").val();//手术序号
							$.ajax({
								url:"<%=basePath%>operation/arrangelist/zysspf.action",
 			 			        data:{stringNurseCharge:stringNurseCharge,goon:1,opId:opId},
			 					type:'post',
			 					success: function(data) {
			 						var dataMap = data;
						   			if(dataMap.resCode=="error"){
						   				$.messager.alert('提示',dataMap.resMsg);			   				
						        		return;
						        	}else if(dataMap.resCode=="success"){
						        		$.messager.alert('提示',dataMap.resMsg);	
						        		var recipeNo=dataMap.recipeNo2;//处方号
						        		var unName=dataMap.name;//库存不足的项目
						        		var drugname=dataMap.drugname;//药品库存不足项目
	 					        		var strs= new Array();
	 					        		strs=unName.split(",");
	 					        		var strname= new Array();
	 					        		strname=drugname.split(",");
	 					        		$("#number").html("");
	 					        		if(opId!=null&&opId!=""){
			 		 	 					if(unName!=null&&unName!=""){
				 			        			$.messager.alert("提示",strname[i],'info');
				 			        			//删除收费信息
				 				        		var dataArr = new Array();
				 				        		var en=$("#list").datagrid("getRows");
				 				        		var len = en.length;
				 			        			for(var i=0;i<len;i++){
				 						    		for(var a=0;a<strs.length;a++){
				 						    			if(en[0].undrugName==strs[a]){
				 							    			dataArr[dataArr.length] = en[0];
				 						    			}
				 						    		}
				 						    		$("#list").datagrid("deleteRow",0);
				 								}
				 						    	if(dataArr.length>0){
				 						    		for(var i=0;i<dataArr.length;i++){
				 						    			$('#list').datagrid('appendRow',dataArr[i]);
				 						    		}
				 						    	}
				 			        		}else{
				 			        			//删除所有行
				 			        			var no=$("#list").datagrid("getRows");
				 			        			var row=no.length;
				 			        			for(var i=0;i<row;i++){
				 			        				$("#list").datagrid("getRowIndex",row[i]);
				 			        				$("#list").datagrid("deleteRow",0);
				 			        			}
				 			        		}
			 		 	 					
			 		 	 				 /**
				 			 				 * 回写手术序号和并且改为收费标记
				 			 				 * @Author: zhangjin
				 			 				 * @CreateDate: 2017年2月17日
				 			 				 * @param:recipeNo住院流水号 opId 手术序号
				 			 				 * @return:
				 			 				 * @version: 1.0
				 			 				 */
				 			 			   $.ajax({
				 			 			        url:"<%=basePath%>operation/arrangelist/writeOperationId.action",
				 			 			        data:{recipeNo:recipeNo,opId:opId},
				 			 			        type:'post',
				 			 				});
				 			        		
				 			 			 $.messager.confirm('提示','是否打印手术室收费通知单？',function(r){ 
							 				  if(r){
							 					 dayin(recipeNo);
							 				  }
							 			   });
				 			        		
			 		 	 				}else{
			 		 	 					if(unName!=""&&unName!=null){
		 					        			$.messager.alert("提示",strname[i],'info');
		 					        			//删除收费信息
								        		var dataArr = new Array();
								        		var en=$("#listyz").datagrid("getRows");
								        		var len = en.length;
										    	for(var i=0;i<len;i++){
										    		for(var a=0;a<strs.length;a++){
										    			if(en[0].undrugName==strs[a]){
											    			dataArr[dataArr.length] = en[0];
										    			}
										    		}
										    		$("#listyz").datagrid("deleteRow",0);
												}
										    	if(dataArr.length>0){
										    		for(var i=0;i<dataArr.length;i++){
										    			$('#listyz').datagrid('appendRow',dataArr[i]);
										    		}
										    	}
		 					        		}else{
		 					        			del();
		 					        		}
			 		 	 				}
	 					        		
	 					        		$.ajax({//药品出库申请
	 				 	 					 url:"<%=basePath%>operation/arrangelist/nurseDrug.action",
	 						 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
	 						 			        type:'post',
	 				 	 				  });
			 		 	 			     $.ajax({//物资出库申请
						 			        url:"<%=basePath%>nursestation/nurseCharge/nursewz.action",
						 			        data:{stringNurseCharge:stringNurseCharge,recipeNo:recipeNo},
						 			        type:'post',
						 			        success:function(data){
						 			        	
						 			        }
			 		 	 				}); 
						        		closePasswordLayout();
						        		closeAuditLayout();
						        		return;
						        	}
						        	else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        		return;
						        	}	
			 					}
							});
						}
						if(arrearageId=='hszyzsh'){  //临时医嘱审核的时候分解
							$.ajax({
								url:"<%=basePath%>business/saveOrder.action",
								data:{
									orderId:orderInfoId,
									goon:1
								},
			 					type:'post',
			 					success: function(data) {
			 						var dataMap = data;
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);
						   				closePasswordLayout();
						        		closeAuditLayout();
						        		return;
						        	}else if(dataMap.resMsg=="success"){
						        		$.messager.alert('提示',dataMap.resCode);	
						        		closePasswordLayout();
						        		closeAuditLayout();
						        		return;
						        	}
						        	else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        		return;
						        	}	
			 					}
							});
						}
					}
				}
    	});
	}
	function closePasswordLayout(){
		$('#confirmPassword-window').window('destroy');		
	}			
	//关闭模式窗口
	function closeAuditLayout(){
		$('#arrearageInfo-window').window('close');
	}
	
</script>
</body>
</html>