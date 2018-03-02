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
<title>物资厂家管理</title>

 <script type="text/javascript">
        var equipementMap="";
        var companyMap="";
        var empMap="";//渲染操作人
        //
    	$(function(){
    		 $('#fileFileName').filebox({    
 			    buttonText: '选择文件', 
 			    buttonAlign: 'right',
 			    onClickButton:function(){
 			    	//alert("aa");
 			    	 $("#BaseName input").focus();
 			    } 
 			});
 		 	$('#fileFileNameht').filebox({    
 			    buttonText: '选择文件', 
 			    buttonAlign: 'right',
 			    onClickButton:function(){
 			    	//alert("bb");
 			    	 $("#FileNameht input").focus();
 			    }
 			});
			 //加载物资供货公司生产厂家信息
    		 $("#tt").tree({    
    			    url:"<%=basePath %>material/equipmentManage/equipmentree.action",    
    			    method:'post',
    			    animate:true,  //点在展开或折叠的时候是否显示动画效果
    			    lines:true,    //是否显示树控件上的虚线
    			    state:'closed',//节点不展开
    			    formatter:function(node){//统计节点总数
    					var s = node.text;
    					  if (node.children){
    						s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
    					}  
    					return s;
    				},onSelect: function(node){//点击节点
    					var id = node.id;
    				  $("#matId").val(id);
    				  equipmentMat();
    				}
    			}); 
    		//渲染表单中器材name 
    		$.ajax({
    			 url:"<%=basePath %>material/equipmentManage/equipmentName.action",    
    			  type:'post',
    			success: function(deptData){
    				equipementMap = deptData;
    			}
    		});
    		  //渲染操作人
    		$.ajax({
    			url:"<%=basePath %>baseinfo/employee/getEmplMap.action",    
    			type:'post',
    			async:false,
    			success: function(deptData){
    				empMap = deptData;
    			}
    		});
    		$('#list').datagrid({
    			url:'<%=basePath %>material/orderCompany/listByPage.action',
    			onDblClickRow:function(index,row){
			    	if(row!=null&&row!=""){
			    		$('#companyCodez').textbox('setValue',row.companyCode);
			    		$('#companyNamez').textbox('setValue',row.companyName);
			    		$('#companyCodeh').textbox('setValue',row.companyCode);
			    		$('#companyNameh').textbox('setValue',row.companyName);
			    		$('#companyId').val(row.id);
			    		$('#icompanyId').val(row.id);
			    		$('#companyNameh').textbox('setValue',row.companyName);
				    	$("#id").val(row.id);
				    	$("#code").val(row.companyCode);
				    	$("#companyNamex").textbox("setValue",row.companyName);//公司名字
				    	$("#spellCodex").textbox("setValue",row.spellCode);//拼音编码
				    	$("#wbCodex").textbox("setValue",row.wbCode);//五笔码
				    	$("#customCodex").textbox("setValue",row.customCode);//自定义码
				    	$("#companyTypex").combobox("setValue",row.companyType);//公司类别(0:生产厂家,1:供销商)
				    	$("#coporationx").textbox("setValue",row.coporation);//公司法人
				    	$("#addressx").textbox("setValue",row.address);//公司地址
				    	$("#telCodex").textbox("setValue",row.telCode);//公司电话
				    	$("#faxCodex").textbox("setValue",row.faxCode);//传真
				    	$("#netAddressx").textbox("setValue",row.netAddress);//公司网址
				    	$("#emailx").textbox("setValue",row.email);//公司邮箱
				    	$("#linkManx").textbox("setValue",row.linkMan);//联系人
				    	$("#linkMailx").textbox("setValue",row.linkMail);//联系人邮箱
				    	$("#linkTelx").textbox("setValue",row.linkTel);//联系人电话
				    	$("#isoInfox").textbox("setValue",row.isoInfo);//ISO信息
				    	$("#openBankx").textbox("setValue",row.openBank);//开户银行
				    	$("#openAccountsx").textbox("setValue",row.openAccounts);//开户账号
				    	$("#actualRatex").textbox("setValue",row.actualRate);//政策扣率
				    	$("#memox").textbox("setValue",row.memo);//备注
				    	var validFlag=row.validFlag;
				    	if(validFlag=0){
				    		$("#validFlagx").prop("checked", true);
				    	}else{
				    	    $("#validFlagx").prop("checked", false);
				    	}
				    
					    tabsde(row);
					 	 //这句代码并没有什么意义，但是没有他，双击列表时，会出现单击不能触发文件上传功能
// 					    $("#BaseName input").click();
// 					    $("#FileNameht input").click();
			    	}
    			},
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
    			}
    		});
    		$('#photoFile').hide(); 
    		  $('#companyType').combobox({    
    			    data:[{"id":0,"text":"生产厂家"},{"id":1,"text":"供货公司"},{"id":2,"text":"全部"}],    
    			    valueField:'id',    
    			    textField:'text',
    			    required:true,    
    			    editable:false
    				}); 
    		  $('#companyTypex').combobox({    
    			    data:[{"id":0,"text":"生产厂家"},{"id":1,"text":"供货公司"}],    
    			    valueField:'id',    
    			    textField:'text',
    			    required:true,    
    			    editable:false
    				}); 
    		  
    		 $('#validFlag').combobox({    
    			    data:[{"id":2,"text":"全部"},{"id":1,"text":"有效"},{"id":0,"text":"停用"}],    
    			    valueField:'id',    
    			    textField:'text',
    			    required:true,    
    			    editable:false
    				}); 
    		 $('#contractTypeh').combobox({    
    			    data:[{"id":1,"text":"正式合同"},{"id":0,"text":"临时合同"}],    
    			    valueField:'id',    
    			    textField:'text',
    			    required:true,    
    			    editable:false
    				}); 
    		 $('#companytypez').combobox({    
    			    data:[{"id":2,"text":"经营许可证"},{"id":1,"text":"生产许可证"},{"id":0,"text":"营业执照"}],    
    			    valueField:'id',    
    			    textField:'text',
    			    required:true,    
    			    editable:false
    				}); 
    		
    	});
	//实现模仿上传file控件  吧真实file控件隐藏
		//$('#fileFileName').hide();
  
 //加载具体信息
    function equipmentMat(){
    	/* var pp = $('#xx').tabs('getSelected'); 
		var tab = pp.panel('options').title; */
			/* if(tab!="许可证"&&tab!="合同"){ */
				var id=$("#matId").val();
			 	var mfName="";
				$('#list').datagrid({    
		    	    url:"<%=basePath %>material/equipmentManage/equipmentMatCom.action?id="+id,
		    	    method:"post",
		    	    
				});   	
    }
	//查询按钮
    function searchFrom(){
    	var id=$("#matId").val();
    	var mfName=$("#mfName").val();
    	var validFlag=$("#validFlag").combobox("getValue");
    	var companyType=$("#companyType").combobox("getValue");
    	$('#list').datagrid({    
    	    url:"<%=basePath %>material/equipmentManage/equipmentMatCom.action",
    	    queryParams:{id:id,mfName:mfName,validFlag:validFlag,companyType:companyType},
    	    method:"post"
    	});  
    }
  //许可证与合同信息的加载
	function tabsde(row){
	  //器材许可证信息的加载
		$('#listxkz').datagrid({    
    	    url:"<%=basePath %>material/equipmentManage/equipmentMat.action?id="+row.companyCode,
    	    method:"post",
    	    onDblClickRow: function(rowIndex, rowData){
				$("#companyCodez").textbox("setValue",rowData.companyCode);
		    	$("#companytypez").combobox("setValue",rowData.companytype);
		    	$("#registerCodez").textbox("setValue",rowData.registerCode);
		    	$("#registerDatez").val(rowData.registerDate);
		    	$("#overDatez").val(rowData.overDate);
		    	var validFlag=rowData.validFlag;
		    	if(validFlag==0){
		    		$("#validFlagz").prop("checked",true);
		    	}else{
		    	    $("#validFlagz").attr("checked",false);
		    	}  
		    
			 }
    	    
	    }); 
		//器材合同信息的加载
		$('#listht').datagrid({    
    	    url:"<%=basePath %>material/equipmentManage/equipmentMatCon.action?id="+row.companyCode,
    	    method:"post",
    	    onDblClickRow: function(rowIndex, rowData){
		    	$("#contractTypeh").combobox("setValue",rowData.contractType);
		    	$("#contractCodeh").textbox("setValue",rowData.contractCode);
		    	$("#contractDateh").val(rowData.contractDate);
		    	$("#overDateh").val(rowData.overDate);
		    	var validFlag=ht[0].validFlag;
		    	if(validFlag==0){
		    		$("#validFlagh").prop("checked", true);
		    	}else{
		    	    $("#validFlagh").prop("checked", false);
		    	}
		    	
    	    }
	    }); 
		//许可证文件的加载
   		$('#listxkz2').datagrid({    
    	    url:"<%=basePath %>material/equipmentManage/queryxkz.action?code="+row.companyCode,
    	    method:"post"
	    }); 
		//合同文件的加载
   		$('#listht2').datagrid({    
    	    url:"<%=basePath %>material/equipmentManage/queryht.action?code="+row.companyCode,
    	    method:"post"
	    });   
	}
		//许可证上传
		function upload(){
			var fileFileName= $("#fileFileName").filebox('getValue');
			if(fileFileName!=''){
				var row2=$('#listxkz').datagrid('getSelections'); 
				var row=$("#listxkz").datagrid('getSelected');
				if(row2.length>0){
					$('#pohtoForm').form('submit', {
						url : "<%=basePath %>material/equipmentManage/equipmentUpload.action",
						success : function(data) {	
							var dataMap = eval("("+data+")");
							if(dataMap.reg=='success'){
								var fullFileName = dataMap.fileServerURL+dataMap.fileName;
								$("#listxkz2").datagrid("appendRow",{
									fileName:fileFileName,
									filePath:fullFileName,
									foreignCode:row.companyCode,
									loadStatus:'true',
								});
						 	 	 editIndex = $("#listxkz2").edatagrid('getRows').length-1;
						 	 	 $("#listxkz2").edatagrid('selectRow', editIndex).edatagrid('beginEdit', editIndex);
							}else{
								$.messager.alert('提示',"上传的文件格式不对");
							}
						},
						error : function(data) {
							$.messager.alert('提示',"上传失败");
						}
					 });
				}else{
					$.messager.alert('提示',"请选择公司");
				}
			}else{
				$.messager.alert('提示',"请选择上传的文件");
			}
		}
		//许可证查看文件
		function checkFiles(){
			var row=$('#listxkz2').datagrid('getSelected');
			if(row!=null){
	 			window.open(row.filePath); 
			}else{
				$.messager.alert('提示',"请选择查看的文件");
			}
// 			row.filePath
// 			var pathxkz=row.permitFilepath;
// 			pathx = pathxkz.replace(/\\/g,"/");
// 			var copyPathxkz = pathx.split("/upload");
		}
		
		//许可证删除文件
// 		function delFiles(){
// 			var row=$('#listxkz2').datagrid('getSelected');
// 			var pathxkz=row.permitFilepath;
// 			pathx = pathxkz.replace(/\\/g,"/")
// 			/* var copyPathxkz = pathx.split("/upload");
// 			var se="upload"+copyPathxkz[copyPathxkz.length-1]; */
// 			if(pathx!=null&&pathx!=""){ 
// 				$.ajax({
<%-- 					url:"<%=basePath %>material/equipmentManage/equipmentdel.action", --%>
// 					data:{pathxkz:pathx},
// 					type:"post",
// 					success:function(){
// 						$.messager.alert('提示',"删除成功");
// 						 var index = $('#listxkz2').datagrid('getRowIndex',$('#listxkz2').datagrid('getSelected'));
// 						$("#listxkz2").datagrid("updateRow",{
// 							index:index ,
// 							row: {
// 							permitFilename:"",
// 							permitFilepath:"",
// 							companyCode:row.companyCode
// 							}
// 						});
// 				 	 	 editIndex = $("#listxkz2").edatagrid('getRows').length-1;
// 				 	 	  $("#listxkz2").edatagrid('selectRow', editIndex).edatagrid('beginEdit', editIndex);
// 					}
// 				});
// 			}
// 		}
		//合同上传
		function uploadht(){
			var fileFileName= $("#fileFileNameht").filebox('getValue');
			if(fileFileName!=''){
				var row2=$('#listht').datagrid('getSelections');
				var row=$("#listht").datagrid('getSelected');
				if(row2.length>0){
					$('#pohtoFormht').form('submit', {
						url : "<%=basePath %>material/equipmentManage/equipmentUploadht.action",
						success : function(data) {	
					        var dataMap = eval("("+data+")");
							if(dataMap.reg=='success'){
								var fullFileName = dataMap.fileServerURL+dataMap.fileName;
								$("#listht2").datagrid("appendRow",{
									fileName:fileFileName,
									filePath:fullFileName,
									foreignCode:row.companyCode,
									loadStatus:'true',
								});
								 editIndex = $("#listht2").datagrid('getRows').length-1;
						 	 	 $("#listht2").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
					        }else{
					        	$.messager.alert('提示',"文件格式不对");
					        }
						},
						error : function(data) {
							$.messager.alert('提示',"上传失败");
						}
					});
				}else{
					$.messager.alert('提示',"请选择公司");
				}
			}else{
				$.messager.alert('提示',"请选择要上传的文件");
			}
		}
		//合同查看文件
		function checkFilesHt(){
			var row=$('#listht2').datagrid('getSelected');
			if(row!=null){
	 			window.open(row.filePath);
			}else{
				$.messager.alert('提示',"请选择要查看的文件");
			}
		}
		var xkzDelId = "";
		//删除许可证文件 
		function delFiles(){
			var row=$("#listxkz2").datagrid('getSelected');
			if(row!=null){
				xkzDelId += row.id +",";
				var index=$("#listxkz2").datagrid("getRowIndex",row);
				$("#listxkz2").datagrid("deleteRow",index);
			}else{
				$.messager.alert('提示',"请选择要删除的记录！");
			}
		}
		//删除合同文件 
		function delFilesht(){
			var row=$("#listht2").datagrid('getSelected');
			if(row!=null){
				xkzDelId += row.id +",";
				var index=$("#listht2").datagrid("getRowIndex",row);
				$("#listht2").datagrid("deleteRow",index);
			}else{
				$.messager.alert('提示',"请选择要删除的记录！");
			}
		}
		//保存后刷新页面
		function myReflush(){
			window.location.reload();
		}
		//清屏
			function clear(){
				/* setTimeout(function(){ */
				$('#megxx').form('reset');
				$('#xkz').form('reset');
				$('#ht').form('reset');
				$('#pohtoForm').form('reset');
				$('#pohtoFormht').form('reset');
				 var rows = $("#list").datagrid("getRows");
				 if(rows!=null){
					 for(var i=0;i<rows.length;i++){
						var index=$("#list").datagrid('getRowIndex',rows[i]);
						$("#list").datagrid('deleteRow',index);
					 }
				 }
				 var rowsxkz = $("#listxkz").datagrid("getRows");
				 if(rowsxkz!=null){
					 for(var i=0;i<rowsxkz.length;i++){
							var index=$("#listxkz").datagrid('getRowIndex',rowsxkz[i]);
							$("#listxkz").datagrid('deleteRow',index);
					 }
				 }
				 var rowsht = $("#listht").datagrid("getRows");
				 if(rowsht!=null){
					 for(var i=0;i<rowsht.length;i++){
							var index=$("#listht").datagrid('getRowIndex',rowsht[i]);
							$("#listht").datagrid('deleteRow',index);
					 }
				 }
				 var rowsxkz2 = $("#listxkz2").datagrid("getRows");
				 if(rowsxkz2!=null){
					 for(var i=0;i<rowsxkz2.length;i++){
							var index=$("#listxkz2").datagrid('getRowIndex',rowsxkz2[i]);
							$("#listxkz2").datagrid('deleteRow',index);
					 }
				 }
				 var rowsht2 = $("#listht2").datagrid("getRows");
				 if(rowsht2!=null){
					 for(var i=0;i<rowsht2.length;i++){
							index=$("#listht2").datagrid('getRowIndex',rowsht2[i]);
							$("#listht2").datagrid('deleteRow',index);
					 }
				 }
			}
		//过滤已经存在的数据
		function selectNewItem(allItem){
			var allItemJson = eval('('+allItem+')');
			var selectNewItemJson = [];
			for(var i = 0;i<allItemJson.length;i++){
				if(allItemJson[i].loadStatus){
					delete allItemJson[i].loadStatus;
					var json = JSON.stringify(allItemJson[i]);
					selectNewItemJson.push(eval('('+json+')'));
				}
			}
			return JSON.stringify(selectNewItemJson);
		}
		
		
		//保存
		function save(){
			var listxkz2 = $("#listxkz2").datagrid("getRows");
			for(var i=0;i<listxkz2.length;i++){
				$("#listxkz2").datagrid("endEdit",i);
			}
			var listht2 = $("#listht2").datagrid("getRows");
			for(var i=0;i<listht2.length;i++){
				$("#listht2").datagrid("endEdit",i);
			}
		    var stringstxAll= JSON.stringify($("#listxkz").edatagrid("getRows"));//许可证
		    var stringstxhAll = JSON.stringify($("#listht").edatagrid("getRows"));//合同
		    var rightStringstx2All=JSON.stringify($("#listxkz2").datagrid("getRows"));//许可证文件
		    var stringstxh2All=JSON.stringify($("#listht2").edatagrid("getRows"));//合同文件
		    //过滤已经存在的数据
		    var stringstx2 = selectNewItem(rightStringstx2All);
		    var stringstx = selectNewItem(stringstxAll);
		    var stringstxh = selectNewItem(stringstxhAll);
		    var stringstxh2 = selectNewItem(stringstxh2All);
		    
		    
		    $("#megxx").form('submit', {
				 url : "<%=basePath %>material/equipmentManage/equipmentmegxx.action", 
				onSubmit:function(){
					if (!$("#megxx").form("validate")) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
				},
				 success : function(data) {
							myReflush();
				},
				error : function(data) {
					$.messager.alert('提示',"保存失败");
				}
			 });
			    $.ajax({
			     url:"<%=basePath %>material/equipmentManage/equipmentSave.action", 
			        data:{stringstx:stringstx,stringstxh:stringstxh,stringstx2:stringstx2,stringstxh2:stringstxh2,xkzDelId:xkzDelId,xkzCMDel:xkzCMDel,htCMDelId:htCMDelId},
			        type:'post',
			       success:function(){
			       },
			         error:function(){
			        	$.messager.alert('提示',"保存失败");
			         }
		    }); 
		}
		  //复选框
		    function funcvalid(value,row,index){
		    	if(value=="1"){
					 return "<input type='checkbox' checked='checked'>";
				}else{
					return '<input type="checkbox" >';
				}
		  }
		  //渲染公司名字
		   function funccomCode(value,row,index) {
			  if(value!=null&&value!=""){
				  return equipementMap[value];  
			  }
			  
		} 
		  //渲染公司类别   data:[{"id":0,"text":"生产厂家"},{"id":1,"text":"供货公司"},{"id":2,"text":"全部"}],    
		   function funccomType(value,row,index) {
			  if(value=='0'){
				  return "生产厂家";  
			  }else if(value=='1'){
				  return "供货公司";  
			  }else if(value=='2'){
				  return "全部";  
			  }else{
				  return "";  
			  }
			  
		} 
		  //渲染许可证类型    data:[{"id":2,"text":"经营许可证"},{"id":1,"text":"生产许可证"},{"id":0,"text":"营业执照"}],       
		   function funccomTypeXK(value,row,index) {
			  if(value=='0'){
				  return "营业执照";  
			  }else if(value=='1'){
				  return "生产许可证";  
			  }else if(value=='2'){
				  return "经营许可证";  
			  }else{
				  return "";  
			  }
			  
		} 
		  //渲染合同类型     data:[{"id":1,"text":"正式合同"},{"id":0,"text":"临时合同"}],       
		   function funccomTypeHT(value,row,index) {
			  if(value=='0'){
				  return "临时合同";  
			  }else if(value=='1'){
				  return "正式合同";  
			  }else{
				  return "";  
			  }
			  
		} 
		//操作人
		function funccomTypeCZR(value,row,index){
			if(value!=null&&value!=''){
				return empMap[value];
			}else{
				return '';
			}
    	}
		  //许可证的确定
		  function savexkz(){
			  var validFlag="";
			 var companyCode=$("#companyCodez").textbox("getValue");
			 var companyName=$("#companyNamez").textbox("getValue"); 
			 var companytype=$("#companytypez").textbox("getValue");
			 var registerCode=$("#registerCodez").textbox("getValue");
			 var registerDate=$("#registerDatez").datebox("getValue");
			 var overDate=$("#overDatez").datebox("getValue");
			 if($('#validFlagz').attr("checked")){
				 validFlag=1;
			 }else{
				 validFlag=0;
			 }
			 var index = $('#listxkz').datagrid('getRowIndex',$('#listxkz').datagrid('getSelected'));
			 $("#listxkz").datagrid("updateRow",{
				 index:index,
				 row: {
					 companyCode:companyCode,
					 companyName:companyName,
					 companytype:companytype,
					 registerCode:registerCode,
					 registerDate:registerDate,
					 overDate:overDate,
					 validFlag:validFlag
					}

			 });
			 $('#xkz').form('reset');
		  }
					
		
		//合同的确定
		function saveht(){
			var validFlag="";
			 var companyCode=$("#companyCodeh").textbox("getValue");
			 var companyname=$("#companyNameh").textbox("getValue");
			 var contractType=$("#contractTypeh").textbox("getValue");
			 var contractCode=$("#contractCodeh").textbox("getValue");
			 var contractDate=$("#contractDateh").datebox("getValue");
			 var overDate=$("#overDateh").datebox("getValue");
			 if($('#validFlagz').prop("checked")){
				 validFlag=1;
			 }else{
				 validFlag=0;
			 }
			
			 var index = $('#listht').datagrid('getRowIndex',$('#listht').datagrid('getSelected'));
			 $("#listht").datagrid("updateRow",{
				 index:index,
				 row: {
					 companyCode:companyCode,
					 companyname:companyname,
					 contractType:contractType,
					 contractCode:contractCode,
					 contractDate:contractDate,
					 overDate:overDate,
					 validFlag:validFlag
					}

			 });
			 $('#ht').form('reset');
		}
		
		//合同对应公司相关信息的添加
		function addht(){
			var validFlag="";
			 var icompanyId=$("#icompanyId").val();
			 var companyCode=$("#companyCodeh").textbox("getValue");
			 var companyname=$("#companyNameh").textbox("getValue");
			 var contractType=$("#contractTypeh").combobox("getValue");
			 var contractCode=$("#contractCodeh").textbox("getValue");
			 var contractDate=$("#contractDateh").val();
			 var overDate=$("#overDateh").val();
			 if($('#validFlagz').prop("checked")){
				 validFlag=1;
			 }else{
				 validFlag=0;
			 }
			 $("#listht").datagrid("appendRow",{
				 	 icompanyId:icompanyId,
					 companyCode:companyCode,
					 companyname:companyname,
					 contractType:contractType,
					 contractCode:contractCode,
					 contractDate:contractDate,
					 overDate:overDate,
					 validFlag:validFlag,
					 loadStatus:'true',
			 });
			 $('#ht').form('reset');
		}
		var htCMDelId = ''
		//合同的删除 
		  function delht(){
				var row=$("#listht").datagrid('getSelected');
				if(row!=null){
					htCMDelId += row.id+",";
					var index=$("#listht").datagrid("getRowIndex",row);
					$("#listht").datagrid("deleteRow",index);
				}else{
					$.messager.alert('提示',"请选择选择删除的记录！");
				}
			}
		 //许可证对应公司相关信息的添加
		  function addxkz(){
			 var validFlag="";
			 var companyid=$("#companyId").val();
			 var companyCode=$("#companyCodez").textbox("getValue");
			 var companyName=$("#companyNamez").textbox("getValue"); 
			 var companytype=$("#companytypez").textbox("getValue");
			 var registerCode=$("#registerCodez").textbox("getValue");
			 var registerDate=$("#registerDatez").val();
			 var overDate=$("#overDatez").val();
			 if($('#validFlagz').attr("checked")){
				 validFlag=1;
			 }else{
				 validFlag=0;
			 }
			 $("#listxkz").datagrid("appendRow",{
				 	companyId:companyid,
					 companyCode:companyCode,
					 companyName:companyName,
					 companytype:companytype,
					 registerCode:registerCode,
					 registerDate:registerDate,
					 overDate:overDate,
					 validFlag:validFlag,
					 loadStatus:'true',

			 });
			 $('#xkz').form('reset');
		 }
		 var xkzCMDel = ''; 
		 //删除许可证的公司信息 
		  function delxkz(){
				var row=$("#listxkz").datagrid('getSelected');
				if(row!=null){
					xkzCMDel += row.id+",";
					var index=$("#listxkz").datagrid("getRowIndex",row);
					$("#listxkz").datagrid("deleteRow",index);
				}else{
					$.messager.alert('提示',"请选择选择删除的记录！");
				}
			}
			//添加按钮
				function add(){
					var validFlag="";
					 var companyCode=$("#companyCodex").textbox("getValue");
					 var companyName=$("#companyNamex").textbox("getValue");
					 var spellCode=$("#spellCodex").textbox("getValue");
					 var wbCode=$("#wbCodex").textbox("getValue");
					 var customCode=$("#customCodex").datebox("getValue");
					 var companyType=$("#companyTypex").combobox("getValue");
					 var coporation=$("#coporationx").textbox("getValue");
					 var address=$("#addressx").textbox("getValue");
					 var telCode=$("#telCodex").textbox("getValue");
					 var faxCode=$("#faxCodex").textbox("getValue");
					 var netAddress=$("#netAddressx").datebox("getValue");
					 var email=$("#emailx").datebox("getValue");
					 var linkMan=$("#linkManx").textbox("getValue");
					 var linkMail=$("#linkMailx").textbox("getValue");
					 var linkTel=$("#linkTelx").textbox("getValue");
					 var isoInfo=$("#isoInfox").textbox("getValue");
					 var openBank=$("#openBankx").datebox("getValue");
					 var openAccounts=$("#openAccountsx").datebox("getValue");
					 var actualRate=$("#linkTelx").textbox("getValue");
					 var memo=$("#isoInfox").textbox("getValue");
					 var openBank=$("#openBankx").datebox("getValue");
					 var openAccounts=$("#openAccountsx").datebox("getValue");
					 var operDate=$("#operDate").datebox("getValue");
					 if($('#validFlagx').prop("checked")){
						 validFlag=1;
					 }else{
						 validFlag=0;
					 }
					 $("#list").datagrid("appendRow",{
						     companyCode:companyCode,
							 companyName:companyName,
							 spellCode:spellCode,
							 wbCode:wbCode,
							 customCode:customCode,
							 companyType:companyType,
							 coporation:coporation,
							 address:address,
							 telCode:telCode,
							 faxCode:faxCode,
							 netAddress:netAddress,
							 email:email,
							 linkMan:linkMan,
							 linkMail:linkMail,
							 linkTel:linkTel,
							 isoInfo:isoInfo,
							 openBank:openBank,
							 openAccounts:openAccounts,
							 actualRate:actualRate,
							 validFlag:validFlag,
							 memo:memo

					 });
				}
 </script> 
 <style type="text/css">
 	#tt .tabs-header{
 		border-left:0
 	}
 	#tt #list,#tt .tabs-panels{
 		border-left:0;
 		border-top:0;
 	}
 </style>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'west'" style="width:15%;border-top:0">
	    	<input type="hidden" id="matId">
		    <ul id="tt"></ul>
	    </div>   
	    <div data-options="region:'center'" style="width: 85%;height: 100%;border-top:0">
	    	<div id="cc" class="easyui-layout" data-options="fit:true">   
			    <div data-options="region:'north'" style="height:50px;padding-top: 10px;border-left:0;border-top:0">
			    	<shiro:hasPermission name="${menuAlias}:function:save">
					         &nbsp;<a href="javascript:save();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;
					</shiro:hasPermission>
                    <input type="hidden" id="name" name="name" value="${name}"><input type="hidden" id="fileName" name="fileName" value="${fileName}">
           	     	<a href="javascript:myReflush();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清屏</a>&nbsp;&nbsp;&nbsp;
				               公司名称:&nbsp;<input class="easyui-textbox"ID="mfName" name="menufunction.mfName" data-options="prompt:'拼音,五笔,自定义码，公司法人，公司电话，公司地址'"/>&nbsp;&nbsp;
						                 公司类别：&nbsp;<input class="easyui-combobox" id="companyType" name="companyType">&nbsp;&nbsp;
						                 状&nbsp;&nbsp;态：&nbsp;<input class="easyui-combobox" id="validFlag">&nbsp;&nbsp;
					<shiro:hasPermission name="${menuAlias}:function:query">
				               <a href="javascript:searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;
				    </shiro:hasPermission>
			    </div>   
			    <div data-options="region:'center',border:false" style="width: 100%;height: 50%">
			    	<table id="list" class="easyui-edatagrid" data-options="fitColumns:true,singleSelect:true,
													         fit:true,pagination : true,pageSize : 10, rownumbers:true,
													         pageList : [ 10, 20, 30, 50, 100 ],border:false">
							<thead>
								 <tr>
<!-- 									<th data-options="field:'ck',checkbox:true,checked:false" ></th> -->
									<th data-options="field:'companyCode'"style="font: 14px;width: 5%">公司编码</th>
									<th data-options="field:'companyName'"style="font: 14px;width: 9%">公司名称</th>
									<th data-options="field:'spellCode'"style="font: 14px;width: 6%">拼音</th>
									<th data-options="field:'wbCode'"style="font: 14px;width: 6%">五笔</th>
									<th data-options="field:'customCode'"style="font: 14px;width: 5%">自定义</th>
									<th data-options="field:'companyType',formatter:funccomType" style="font: 14px;width: 4%">公司类别</th>
									<th data-options="field:'coporation'"style="font: 14px;width: 5%">公司法人</th>
									<th data-options="field:'address'"style="font: 14px;width: 5%">公司地址</th>
									<th data-options="field:'telCode'"style="font: 14px;width: 6%">公司电话</th>
									<th data-options="field:'faxCode'"style="font: 14px;width: 6%">传真</th>
									<th data-options="field:'netAddress'"style="font: 14px;width: 5%">公司网址</th>
									<th data-options="field:'email'"style="font: 14px;width: 5%">公司邮箱</th>
									<th data-options="field:'linkMan'"style="font: 14px;width: 4%">联系人</th>
									<th data-options="field:'linkMail'"style="font: 14px;width: 5%">联系人邮箱</th>
									<th data-options="field:'linkTel'"style="font: 14px;width: 5%">联系人电话</th>
									<th data-options="field:'isoInfo'"style="font: 14px;width: 5%">ISO信息</th>
									<th data-options="field:'openBank'"style="font: 14px;width: 6%">开户银行</th>
									<th data-options="field:'openAccounts'"style="font: 14px;width: 5%">开户账号</th>
									<th data-options="field:'actualRate'"style="font: 14px;width: 5%">政策扣率</th>
									<th data-options="field:'validFlag',formatter: funcvalid" style="font: 14px;width: 4%">有效标记</th>
									<th data-options="field:'operCode',formatter: funccomTypeCZR" style="font: 14px;width: 4%">操作人</th>
									<th data-options="field:'operdate'"style="font: 14px;width: 5%">操作日期</th>
								 </tr>
							</thead>
				     </table>
			    </div>   
			    <div data-options="region:'south',border:false" style="width: 100%;height: 50%">
			    	<div id="tt" class="easyui-tabs" data-options="fit:true">   
					    <div title="基本信息" data-options="fit:true">   
					        <form id="megxx" method="post">    
					           <table id="list" class="honry-table" style="width: 100%;height:90%;" align="center" data-options="fit:true,border:false" >
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0;border-top:0">公司名称：<input id="id" name="matCompany.id" type="hidden" ></td>
				                         <td width=15% style="border-top:0"><input id="companyNamex" name="matCompany.companyName" class="easyui-textbox" required="true"></td>
				                         <td class="honry-lable" width=10% align="right" style="border-top:0">拼音码：<input id="code" name="matCompany.companyCode" type="hidden"></td>
				                         <td width=15% style="border-top:0"><input id="spellCodex" name="matCompany.spellCode" class="easyui-textbox" required="true"></td>
				                         <td class="honry-lable" width=10% align="right" style="border-top:0">五笔码：</td>
				                         <td width=15% style="border-top:0"><input id="wbCodex" name="matCompany.wbCode" class="easyui-textbox" required="true"></td>
				                    </tr>
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0">自定义码：</td>
				                         <td width=15%><input id="customCodex" name="matCompany.customCode" class="easyui-textbox" required="true"></td>
				                         <td class="honry-lable" width=10% align="right">公司类别：</td>
				                         <td width=15%><input id="companyTypex" name="matCompany.companyType" class="easyui-combobox" required="true"></td>
				                         <td class="honry-lable" width=10% align="right">公司法人：</td>
				                         <td width=15%><input id="coporationx" name="matCompany.coporation" class="easyui-textbox" required="true"></td>
				                    </tr>
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0">公司电话：</td>
				                         <td><input id="telCodex" name="matCompany.telCode" class="easyui-textbox" required="true"></td>
				                         <td class="honry-lable" width=10% align="right">公司地址：</td>
				                         <td colspan="3"><input id="addressx" name="matCompany.address" class="easyui-textbox" size="90px" required="true"></td>
				                    </tr>
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0">公司传真：</td>
				                         <td width=15%><input id="faxCodex" name="matCompany.faxCode" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">公司网址：</td>
				                         <td width=15%><input id="netAddressx" name="matCompany.netAddress" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">公司邮箱：</td>
				                         <td width=15%><input id="emailx" name="matCompany.email" class="easyui-textbox"></td>
				                    </tr>
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0">联系人：</td>
				                         <td width=15%><input id="linkManx" name="matCompany.linkMan" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">联系人邮箱：</td>
				                         <td width=15%><input id="linkMailx" name="matCompany.linkMail" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">联系人电话：</td>
				                         <td width=15%><input id="linkTelx" name="matCompany.linkTel" class="easyui-textbox"></td>
				                    </tr>
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0">开户银行：</td>
				                         <td width=15% ><input id="openBankx" name="matCompany.openBank" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">开户账号：</td>
				                         <td width=15%><input id="openAccountsx" name="matCompany.openAccounts" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">政策扣率：</td>
				                         <td width=15%><input id="actualRatex" name="matCompany.actualRate" class="easyui-textbox"></td>
				                    </tr>
				                    <tr>
				                         <td class="honry-lable" width=10% align="right" style="border-left:0">ISO信息：</td>
				                         <td width=15%><input id="isoInfox" name="matCompany.isoInfo" class="easyui-textbox"></td>
				                         <td class="honry-lable" width=10% align="right">备注：</td>
				                         <td colspan="3"><input id="memox" name="matCompany.memo" class="easyui-textbox" size="90px"></td>
				                    </tr>
				             	</table>
				             	<input type="checkbox" id="validFlagx">停用<br/>
				             </form>   
					    </div>   
					    <div title="许可证" data-options="fit:true">   
					        <div id="cc" class="easyui-layout" data-options="fit:true"> 
					        	<div data-options="region:'west'" style="width:49%;border-top:0;border-left:0">
					        		<table id="listxkz" class="easyui-edatagrid" data-options="fitColumns:false,singleSelect:true,fit:true,border:false">
											<thead>
												 <tr>
<!-- 													<th data-options="field:'ck',checkbox:true,checked:false" ></th> -->
													<th data-options="field:'state',formatter: funcvalid"style="font: 14px;width: 5%">状态</th>
													<th data-options="field:'companyId',hidden:true" style="font: 14px;">公司</th>
													<th data-options="field:'loadStatus',hidden:true" style="font: 14px;"></th>
													<th data-options="field:'companyCode',formatter: funccomCode" style="font: 14px;width: 23%">公司名称</th>
													<th data-options="field:'companytype',formatter: funccomTypeXK" style="font: 14px;width: 10%">许可证类型</th>
													<th data-options="field:'registerCode'" style="font: 14px;width: 12%">注册号</th>
													<th data-options="field:'registerDate'" style="font: 14px;width: 15%">注册日期</th>
													<th data-options="field:'overDate'" style="font: 14px;width: 15%">到期日期</th>
													<th data-options="field:'validFlag',formatter: funcvalid" style="font: 14px;width: 8%">有效标记</th>
													<!-- <th data-options="field:'mark'"style="font: 14px;width: 6%">备注</th>
													<th data-options="field:'operCode',formatter: funccomTypeCZR"style="font: 14px;width: 8%">操作员</th>
													<th data-options="field:'operDate'"style="font: 14px;width: 9%">操作日期</th> -->
												 </tr>
											</thead>
								     </table>
					        	</div>
					        	<div data-options="region:'center',border:false" style="width: 21%">
							    	<form id="xkz" method="post">
							            <table id="listper" class="honry-table"  style="width: 100%;height:80%;border-top:0;border-right:0">
								             <tr>
								                 <td class="honry-lable" align="right" style="border-top:0">公司编码：</td>
								                 <td style="border-top:0;border-right:0"><input id="companyCodez" name="companycode" class="easyui-textbox"><input id="companyId" type="hidden"/></td>
								             </tr>
								             <tr>
								                 <td class="honry-lable" align="right">公司名称：</td>
								                 <td style="border-right:0"><input id="companyNamez" name="companyName" class="easyui-textbox"></td>
								             </tr>
								             <tr>
								                 <td class="honry-lable" align="right">证件类别：</td>
								                 <td style="border-right:0"><input id="companytypez" name="companytype" class="easyui-combobox" value="0"></td>
								             </tr>
								             <tr>
								                 <td class="honry-lable" align="right">注册号：</td>
								                 <td style="border-right:0"><input id="registerCodez" name="registerCode" class="easyui-textbox"></td>
								             </tr>
								             <tr>
								                 <td class="honry-lable" align="right">注册日期：</td>
								                 <td style="border-right:0">
								                 	<input id="registerDatez" name="registerDate" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})"/>
								                 </td>
								             </tr>
								             <tr>
								                 <td class="honry-lable" align="right">到期日期：</td>
								                 <td style="border-right:0">
								                 	<input id="overDatez" name="overDate" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})"/>
								                 </td>
								             </tr>
								             <tr>
								                 <td class="honry-lable" align="right">是否作废：</td>
								                 <td style="border-right:0"><input type="checkbox" id="validFlagz" name="validFlag"></td>
								             </tr>
								             <tr>
								             	<td colspan="2" align="center" style="border: 0">
								             		<a href="javascript:addxkz();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
													<shiro:hasPermission name="${menuAlias}:function:delete">
														<a href="javascript:delxkz();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
													</shiro:hasPermission>
								             	</td>
								             </tr>
									     </table>
							         </form>
							    </div>
							    <div data-options="region:'east',border:false" style="width:30%;" id="baseUp">
									<div id="cc" class="easyui-layout" data-options="fit:true">   
										<form id="pohtoForm" method="post" enctype="multipart/form-data">
									    <div data-options="region:'west'" style="width:50%;border-top:0">
									    	<table id="listxkz2" class="easyui-datagrid" style="width:100%;height: 100%;"data-options="fitColumns:false,fit:true,singleSelect:true,border:false">
												<thead>
													 <tr>
														<th data-options="field:'fileName'"style="font: 14px;width: 65%">文件名称</th>
														<th data-options="field:'memo',editor:{type:'textbox'}"style="font: 14px;width: 35%">备注</th>
														<th data-options="field:'filePath',hidden:true"style="font: 14px;width: 25%">文件路径</th>
														<th data-options="field:'foreignCode',hidden:true"style="font: 14px;width: 25%">公司编码</th>
														<th data-options="field:'loadStatus',hidden:true"style="font: 14px;width: 25%">状态</th>
													 </tr>
												</thead>
										    </table>
									    </div>   
									    <div data-options="region:'center',border:false" style="width: 50%;padding:5px 0 0 5px;" id="BaseName"> 
									  	  <!--class="easyui-filebox"  -->
									    	<tr><td><input  id="fileFileName" name="filePhoto"  style="width:200px" ></input></td></tr><br><br>
										        <shiro:hasPermission name="${menuAlias}:function:upload">
									            <tr><td><a href="javascript:void(0)" name="filePhoto" onclick="upload()" class="easyui-linkbutton" iconCls="icon-disk_upload">上传文件</a></td></tr><br><br>
										        </shiro:hasPermission>
									            <tr><td><a href="javascript:void(0)" id="qfile" onclick="checkFiles()" class="easyui-linkbutton" iconCls="icon-see">查看文件</a></td></tr><br><br>
										        <shiro:hasPermission name="${menuAlias}:function:delete">
									            <tr><td><a href="javascript:void(0)" id="removeht" onclick="delFiles()" class="easyui-linkbutton" iconCls="icon-remove">删除文件</a></td></tr> 
										        </shiro:hasPermission>
									    </div>  
								     </form>   
									</div>
							    </div>   
							</div>     
					    </div>
					    <div title="合同" data-options="fit:true">   
					        <div id="cc" class="easyui-layout" data-options="fit:true"> 
					       
							    <div data-options="region:'west'" style="width:49%;border-top:0;border-left:0">
							    	<table id="listht" class="easyui-edatagrid" data-options="fitColumns:true,singleSelect:true,fit:true,border:false">
											<thead>
												 <tr>
<!-- 													<th data-options="field:'ck',checkbox:true,checked:false" ></th> -->
													<th data-options="field:'state',formatter: funcvalid"style="font: 14px;width: 5%">状态</th>
													<th data-options="field:'icompanyId',hidden:true" style="font: 14px;width: 9%">公司名称</th>
													<th data-options="field:'loadStatus',hidden:true" style="font: 14px;"></th>
													<th data-options="field:'companyCode',formatter: funccomCode" style="font: 14px;width: 23%">公司名称</th>
													<th data-options="field:'contractType',formatter: funccomTypeHT"style="font: 14px;width: 10%">合同类型</th>
													<th data-options="field:'contractCode'"style="font: 14px;width: 12%">合同编号</th>
													<th data-options="field:'contractDate'"style="font: 14px;width: 15%">签订日期</th>
													<th data-options="field:'overDate'" style="font: 14px;width: 15%">到期日期</th>
													<th data-options="field:'validFlag',formatter: funcvalid"style="font: 14px;width: 8%">有效标记</th>
													<!-- <th data-options="field:'mark'"style="font: 14px;width: 6%">备注</th>
													<th data-options="field:'operCode',formatter: funccomTypeCZR"style="font: 14px;width: 7%">操作员</th>
													<th data-options="field:'operDate'"style="font: 14px;width: 9%">操作日期</th> -->
												 </tr>
											</thead>
								     </table>
							    </div>
							
							    <div data-options="region:'center',border:false" style="width: 21%">
							    	<form id="ht" method="post">
							            <table id="listper3" class="honry-table"  style="width: 100%;height:80%;border-top:0;border-right:0" data-options="fit:true">
									             <tr>
									                 <td class="honry-lable" align="right" style="border-top:0">公司编码：</td>
									                 <td style="border-top:0;border-right:0"><input id="companyCodeh" name="companycode" class="easyui-textbox"><input id="icompanyId" type="hidden"/></td>
									             </tr>
									             <tr>
									                 <td class="honry-lable" align="right">公司名称：</td>
									                 <td style="border-right:0"><input id="companyNameh" name="companyName" class="easyui-textbox"></td>
									             </tr>
									             <tr>
									                 <td class="honry-lable" align="right">合同类别：</td>
									                 <td style="border-right:0"><input id="contractTypeh" name="contractType" class="easyui-combobox" value="1"></td>
									             </tr>
									             <tr>
									                 <td class="honry-lable" align="right">合同编号：</td>
									                 <td style="border-right:0"><input id="contractCodeh" name="contractCode" class="easyui-textbox"></td>
									             </tr>
									             <tr>
									                 <td class="honry-lable" align="right">签订日期：</td>
									                 <td style="border-right:0">
									                 	<input id="contractDateh" name="contractDate" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})"/>
									                 </td>
									             </tr>
									             <tr>
									                 <td class="honry-lable" align="right">到期日期：</td>
									                 <td style="border-right:0">
									                 	<input id="overDateh" name="overDate" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})"/>
									                 </td>
									             </tr>
									             <tr>
									                 <td class="honry-lable" align="right">是否作废:</td>
									                 <td style="border-right:0"><input type="checkbox" id="validFlagh" name="validFlag"></td>
									             </tr>
									             <tr>
									                 <td colspan="2" align="center" style="border: 0">
									                 	<%-- <shiro:hasPermission name="${menuAlias}:function:save">
												             	<a href="javascript:saveht();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
												        </shiro:hasPermission> --%>
																<a href="javascript:addht();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
														<shiro:hasPermission name="${menuAlias}:function:delete">
																<a href="javascript:delht();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
														</shiro:hasPermission>
									                 </td>
									             </tr>
									     </table>
								     </form>
							    </div>
							    <div data-options="region:'east',border:false" style="width:30%;" id="upFile">
							    	<div id="cc" class="easyui-layout" data-options="fit:true">   
							    	 <form id="pohtoFormht" method="post" enctype="multipart/form-data">  
									    <div data-options="region:'west'" style="width:50%;border-top:0">
									    	<table id="listht2" class="easyui-datagrid" style="width:100%;height: 80%"data-options="fitColumns:false,fit:true,singleSelect:true,border:false">
												<thead>
													 <tr>
														<th data-options="field:'fileName'"style="font: 14px;width: 60%">文件名称</th>
														<th data-options="field:'memo',editor:{type:'textbox'}"style="font: 14px;width: 40%">备注</th>
														<th data-options="field:'filePath',hidden:true"style="font: 14px;width: 50%">文件路径</th>
														<th data-options="field:'foreignCode',hidden:true"style="font: 14px;width: 50%">编码</th>
														<th data-options="field:'loadStatus',hidden:true"style="font: 14px;width: 25%">状态</th>
													 </tr>
												</thead>
										    </table>
									    </div>   
									    <div data-options="region:'center',border:false" style="width:50%;padding:5px 0 0 5px;" id="FileNameht">
									    	<tr><td><input  id="fileFileNameht" name="filePhoto" class="easyui-filebox" style="width:200px"></input></td></tr><br><br>
										   <shiro:hasPermission name="${menuAlias}:function:upload">
										            <tr><td><a href="javascript:void(0)" name="filePhoto" onclick="uploadht()" class="easyui-linkbutton" iconCls="icon-disk_upload">上传文件</a></td></tr><br><br>
										   </shiro:hasPermission>
										   <shiro:hasPermission name="${menuAlias}:function:query">
										            <tr><td><a href="javascript:void(0)" id="qfileht" onclick="checkFilesHt()" class="easyui-linkbutton" iconCls="icon-see">查看文件</a></td></tr><br><br>
										   </shiro:hasPermission>
										   <shiro:hasPermission name="${menuAlias}:function:delete">
										            <tr><td><a href="javascript:void(0)" id="removeht" onclick="delFilesht()" class="easyui-linkbutton" iconCls="icon-remove">删除文件</a></td></tr>
										   </shiro:hasPermission>
									    </div> 
							        </form>  
									</div>
							    </div>
							</div>    
					    </div>   
					</div> 
			    </div>   
			</div>
	    </div>   
	</div>
</body>
</html>