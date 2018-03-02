<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>护理管理</title>
	
	<script type="text/javascript">
	var menuAlias = '${menuAlias}';
	$(function(){
		$("#tabsWe").tabs({
			onSelect:function(title,index){
			$("#showTabsTitle").val(title);	
			var qql = $('#tabsWe').tabs('getSelected');				
			var tabl = qql.panel('options');
				if(tabl.title!='床头卡'){
					$('#printReport').linkbutton('disable');
				}else{
					var aa= $("#queryBedInfoTabs").tabs('getSelected');
					var ta = aa.panel('options');
					if(ta.index==0){
						$('#printReport').linkbutton('disable');
					}else{
						 $('#printReport').linkbutton('enable');
					}
					
				}
			}
		
		});
		/**
		*患者树 
		*/
		$("#treePatientInfo").tree({
			url:'<%=basePath%>nursestation/nurse/getInpatientTree.action',
			lines:true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children.length>0) {					 						
					s += '<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';					
				}					
				return s;
			},
			onContextMenu: function(e,node){//添加右键菜单
				e.preventDefault();
				// 查找节点
				$(this).tree('select',node.target);
				$.ajax({  //根据患者住院流水号查询患者信息
					url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
					data:{inpatientNo:node.attributes.inpatientNo},
					success:function(info){ //
						$("#blh").val(info.medicalrecordId);
						$("#xm").val(info.patientName);
						var ages=DateOfBirth(info.reportBirthday);
						 //年龄
						$("#nl").val(ages.get("nianling")+ages.get('ageUnits'));
						$("#hljb").val(info.tend);

						$("#zyys").val(info.houseDocName);
						$("#zzys").val(info.chargeDocName);
						$("#zrys").val(info.chiefDocName);
						$("#zrhs").val(info.dutyNurseName);
						$("#zd").val(info.diagName);
						$("#cw").val(info.bedName);
						if(info.anaphyFlag=="1"){
							$("#gm").val('有');
						}
						else{
							$("#gm").val('无');
						}
					}
				});
				
				//本区患者 (出院登记，换医师，婴儿登记操作)
				if(node.attributes.pid=='1'){
					var rows = $('#treePatientInfo').tree('getData',node.target);
					if(rows.attributes.babyFlag==1){
						$('#packbed').css("display","none");
						$('#huanbed').css("display","none");
					}else{
						$('#packbed').css("display","block");
						$('#huanbed').css("display","block");
					}
					if(rows.attributes.haveBabyFlag=='0'){
						$('#yinger').css("display","none");
					}else{
 						if(rows.attributes.sex=="2"){
							$('#yinger').css("display","block");
 						}else{
 							$('#yinger').css("display","none");
 						}
					}
					$('#jiezhen').css("display","none");
					$('#zhaohui').css("display","none");
					$('#qingjia').css("display","block");
					$('#chuyuan').css("display","block");
					$('#huanyis').css("display","block");
					$('#zhuanke').css("display","block");
					$('#quxiao').css("display","none");
					$('#confirm').css("display","none");
					$('#rightClick').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}
				//转入患者 (接诊操作)
				 if(node.attributes.pid=='2'){
					$('#jiezhen').css("display","block");
					$('#qingjia').css("display","none");
					$('#zhaohui').css("display","none");
					$('#chuyuan').css("display","none");
					$('#huanyis').css("display","none");
					$('#yinger').css("display","none");
					$('#zhuanke').css("display","none");
					$('#quxiao').css("display","none");
					$('#confirm').css("display","block");
					$('#huanbed').css("display","none");
					$('#packbed').css("display","none");
					$('#rightClick').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}
				// 转出患者
				if(node.attributes.pid=='3'){
					$('#jiezhen').css("display","none");
					$('#zhaohui').css("display","none");
					$('#qingjia').css("display","none");
					$('#chuyuan').css("display","none");
					$('#huanyis').css("display","none");
					$('#yinger').css("display","none");
					$('#zhuanke').css("display","block");
					$('#quxiao').css("display","block");
					$('#confirm').css("display","none");
					$('#huanbed').css("display","none");
					$('#packbed').css("display","none");
					$('#rightClick').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}
				
				// 出院登记患者()
			    if(node.attributes.pid=='4'){
			    	$('#jiezhen').css("display","none");
			    	$('#qingjia').css("display","none");
					$('#chuyuan').css("display","none");
					$('#huanyis').css("display","none");
					$('#yinger').css("display","none");
					$('#zhaohui').css("display","block");
					$('#zhuanke').css("display","none");
					$('#quxiao').css("display","none");
					$('#confirm').css("display","none");
					$('#huanbed').css("display","none");
					$('#packbed').css("display","none");
					$('#rightClick').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}  
				
			},onClick:function(node){
				if(node.id=='1'||node.id=='2'||node.id=='3'||node.id=='4'){
					$.messager.alert('提示','请选择患者点击！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					var tree=node.text;
					addNuiLeave();
					if($("#showTabsTitle").val()=="请假"){
						if(node.attributes.extFlag2=="1"){
							 	var url = "<%=basePath%>nursestation/nurse/nurseLeave.action?mid="+node.attributes.inpatientNo;
								if(url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','请假'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
					    }else{
					    	$.messager.alert('提示','请假只能对本区患者进行操作！');
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
					    }
					 }else if($("#showTabsTitle").val()=="换医师"){
						  if(node.attributes.extFlag2=="1"){
							  	var url="<%=basePath%>nursestation/nurse/replaceDoctorView.action?inpatientNo="+node.attributes.inpatientNo+"&mid="+node.attributes.medicalrecordId;
								if(url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','换医师'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
						    }else{
						    	$.messager.alert('提示','换医师只能对本区患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }else if($("#showTabsTitle").val()=="婴儿登记"){
						  	if(node.attributes.extFlag2=="1"){
						  		var rows = $('#treePatientInfo').tree('getData',node.target);
								if(rows.attributes.haveBabyFlag=='1'&&rows.attributes.sex=="2"){
									var medicalrecordId=node.attributes.medicalrecordId;
									var inpatientNo=node.attributes.inpatientNo;
									var url="<%=basePath%>nursestation/nurse/babyRegistered.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId;
									if(url != undefined) {
										$('#tabsWe').tabs('update',{
											tab:$('#tabsWe').tabs('getSelected','婴儿登记'),
											options:{
												content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
											}
										});
									}
								}else{
									$.messager.alert('提示','该患者不是母亲，不能进行登记！');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}
						  		
						  		
							  	
						    }else{
						    	$.messager.alert('提示','婴儿登记只能对本区患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }else if($("#showTabsTitle").val()=="出院登记"){
						    if(node.attributes.extFlag2=="1"){
						    	var medicalrecordId=node.attributes.medicalrecordId;
						    	var inpatientNo=node.attributes.inpatientNo;
								var url="<%=basePath%>nursestation/nurse/leaveHospital.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId;
								if(url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','出院登记'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
						    }else{
						    	$.messager.alert('提示','出院登记只能对本区患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }else if($("#showTabsTitle").val()=="召回"){
						  	if(node.attributes.extFlag2=="4"){
						  		var inpatientNo=node.attributes.inpatientNo;
						  		var medicalrecordId=node.attributes.medicalrecordId;
								var url="<%=basePath%>nursestation/nurse/recall.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId;
								if(url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','召回'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
						    }else{
						    	$.messager.alert('提示','召回只能对出院登记患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }else if($("#showTabsTitle").val()=="转科申请"){
						  	if(node.attributes.extFlag2=="1"||node.attributes.extFlag2=="3"){
						  		var medicalrecordId=node.attributes.medicalrecordId;
						  		var inpatientNo=node.attributes.inpatientNo;
								var url="<%=basePath%>nursestation/nurse/transfer.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
								if(url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','转科申请'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
						    }else{
						    	$.messager.alert('提示','转科申请只能对本区患者和转出患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }else if($("#showTabsTitle").val()=="取消转科"){
						  if(node.attributes.extFlag2=="3"){
							  	var medicalrecordId=node.attributes.medicalrecordId;
							  	var inpatientNo=node.attributes.inpatientNo;
								var url="<%=basePath%>nursestation/nurse/updateShiftApply.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId;
								if (url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','取消转科'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
						    }else{
						    	$.messager.alert('提示','取消转科只能对转出患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }else if($("#showTabsTitle").val()=="确认转科"){
						  if(node.attributes.extFlag2=="2"){
							  if(node.attributes.extFlag1=="2"){
								  $.messager.alert('提示','该患者已经进行了确认转科操作！');
								  setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
							  }else{
							  	var medicalrecordId=node.attributes.medicalrecordId;
								var inpatientNo=node.attributes.inpatientNo;
								var url="<%=basePath%>nursestation/nurse/confirmShiftApply.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId;
								if (url != undefined) {
									$('#tabsWe').tabs('update',{
										tab:$('#tabsWe').tabs('getSelected','确认转科'),
										options:{
											content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
										}
									});
								}
							  }
						    }else{
						    	$.messager.alert('提示','确认转科只能对转入患者进行操作！');
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						    }
					  }
					 
					$("#nuiLeaveList").datagrid('reload',{inpatNo:node.attributes.inpatientNo});
					$("#nuiLeaveList").datagrid('clearChecked');
					$("#babyList").datagrid('reload',{medicalrecordId:node.attributes.medicalrecordId});
					
						 if(tree=="转入患者"){
							$("#infoLayOut").layout('collapse','west');
							$('#cardView').datagrid('reload',{
								state: 'inn'
							});
							$("#listView").datagrid('reload',{
								state: 'inn'
							});
						}else if(tree=="转出患者"){
							$("#infoLayOut").layout('collapse','west');
							$('#cardView').datagrid('reload',{
									state: 'out'
							});
							$("#listView").datagrid('reload',{
								state: 'out'
							});
						}else if(tree=="出院登记患者"){
							$("#infoLayOut").layout('collapse','west');
							$('#cardView').datagrid('reload',{
									state: 'B'
							});
							$("#listView").datagrid('reload',{
								state: 'B'
							});
						}else if(tree=="本区患者"){
							$("#infoLayOut").layout('collapse','west');
							$('#cardView').datagrid('reload',{
									state: 'I'
							});
							$("#listView").datagrid('reload',{
								state: 'I'
							});
						}else{
							$("#infoLayOut").layout('expand','west');
							$("#inpatientHiddenNo").textbox('setValue',node.id); //赋值
							$.ajax({  //根据患者住院流水号查询患者信息
								url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
								data:{inpatientNo:node.attributes.inpatientNo},
								success:function(info){ //
									$("#blh").val(info.medicalrecordId);
									$("#xm").val(info.patientName);
									var ages=DateOfBirth(info.reportBirthday);
									 //年龄
									$("#nl").val(ages.get("nianling")+ages.get('ageUnits'));
									$("#hljb").val(info.tend);

									$("#zyys").val(info.houseDocName);
									$("#zzys").val(info.chargeDocName);
									$("#zrys").val(info.chiefDocName);
									$("#zrhs").val(info.dutyNurseName);
									$("#zd").val(info.diagName);
									$("#cw").val(info.bedName);
									if(info.anaphyFlag=="1"){
										$("#gm").val('有');
									}
									else{
										$("#gm").val('无');
									}
								}
							});
						}
				}
			},
			onDblClick:function(node){
				//转出患者
				if(node.attributes.pid=='4'){
					openApply();
				}
			},onLoadSuccess : function(node, data) {
				if(data.resCode=='error'){
					   $("body").setLoading({
							id:"body",
							isImg:false,
							text:data.resMsg
						});
				   }
			}
		});
			
	/**
	 * 关于numberbox的默认规则，参数都是根据表里长度
	 * @author  huangbiao
	 * @date 2016-3-22
	 * @version 1.0
	 */
	$.extend($.fn.numberbox.defaults.rules, {    
		gestationIsNumber: {    
	        validator: function(value, param){ 
	            return value>0&&value<param[0];    
	        },    
	        message: '请输入正确的值！'
	    },
	
	    isNumber:{
			validator: function(value, param){    
	            return value>0.00&&value<param[0];    
	        },    
	        message: '请输入正确的值！'
		},
		
		ageVaild:{
			validator: function(value, param){    
	            return value>0&&value<param[0];    
	        },    
	        message: '请输入正确的年龄！'
		},
		
		idCardCheck:{
			validator: function(value, param){    
	            return checkIdCardNo(value);    
	        },    
	        message: '请输入正确的身份证号！'
		}
	}); 
	$.ajax({  //查询护士站病床信息
		url:'<%=basePath%>nursestation/nurse/listNurseView.action',
		success:function(info){
			$('#bedNum').html(info.bedC);
			$('#bedC1').html(info.bedC1);
			$('#bedC2').html(info.bedC2);
			$('#bili').html(info.bili);
			$('#infoC1').html(info.infoC1);
			$('#infoC2').html(info.infoC2);
			$('#infoC').html(info.infoC);
			$('#deptName1').html(info.deptName1);
			$('#deptName2').html(info.deptName1);
		}
	});
});
//包床
function packbed(){
	var row =  $('#treePatientInfo').tree('getSelected');
	var inpatientNo=row.attributes.inpatientNo;
	var tempWinPath = "<%=basePath%>nursestation/nurse/bedInfoKList.action?inpatientNo="+inpatientNo;
	var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
}
//换床
function huanbed(){
	var row =  $('#treePatientInfo').tree('getSelected');
	var inpatientNo=row.attributes.inpatientNo;
	var tempWinPath = "<%=basePath%>nursestation/nurse/huanbedInfoKList.action?inpatientNo="+inpatientNo;
	var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
}
//弹出接诊选项卡
function acceptsName(){
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.medicalrecordId;
	if (window.parent.$('#tabs').tabs('exists','住院接诊')){
		window.parent.$('#tabs').tabs('close', '住院接诊');
		var url="<%=basePath%>inpatient/admission/listAdmission.action?menuAlias=ZYZYJZ&medicalId="+medicalrecordId;
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		window.parent.$('#tabs').tabs('add',{
				title:'住院接诊',
				content:content,
				closable:true
		});

	}  else {
		var url="<%=basePath%>inpatient/admission/listAdmission.action?menuAlias=ZYZYJZ&medicalId="+medicalrecordId;
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		window.parent.$('#tabs').tabs('add',{
				title:'住院接诊',
				content:content,
				closable:true
		});
	}
	fuzhi(row.attributes.inpatientNo);
	$('#printReport').linkbutton('disable');
};
//确认转科
function confirm(){
	$("#himid").val('确认转科');
	var row =  $('#treePatientInfo').tree('getSelected');
	if(row.attributes.extFlag1=="2"){
		  $.messager.alert('提示','该患者已经进行了确认转科操作！');
		  setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	}else{
		var medicalrecordId=row.attributes.medicalrecordId;
		var inpatientNo=row.attributes.inpatientNo;
		var url="<%=basePath%>nursestation/nurse/confirmShiftApply.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
		if ($('#tabsWe').tabs('exists', '确认转科')){
			fuzhi(inpatientNo);
				$('#tabsWe').tabs('select', '确认转科');
				$('#tabsWe').tabs('update',{
					tab:$('#tabsWe').tabs('getSelected','确认转科'),
					options:{
						content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
					}
				});
				return;
			}
		$('#tabsWe').tabs('add',{
			title:'确认转科',
			// 新内容的URL
			content:"<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",
			closable:true
		});
	}
	fuzhi(inpatientNo);
}
//取消转科
function transferqx(){
	$("#himid").val('取消转科');
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.medicalrecordId;
	var inpatientNo=row.attributes.inpatientNo;
	var url="<%=basePath%>nursestation/nurse/updateShiftApply.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
	if ($('#tabsWe').tabs('exists', '取消转科')){
		fuzhi(row.attributes.inpatientNo);
			$('#tabsWe').tabs('select', '取消转科');
			$('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','取消转科'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
			return;
		}
	$('#tabsWe').tabs('add',{  
		title:'取消转科',
		// 新内容的URL
		content:"<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",
		closable:true  
	});
	fuzhi(row.attributes.inpatientNo);
}
//转科申请
function transfer(){
	$("#himid").val('转科申请');
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.medicalrecordId;
	var inpatientNo=row.attributes.inpatientNo;
	if(row.attributes.babyFlag==1){
		$.messager.alert('提示','婴儿不能进行转科申请，请对母亲进行转科!');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return;
	}
	var url="<%=basePath%>nursestation/nurse/transfer.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
	if ($('#tabsWe').tabs('exists', '转科申请')){
		fuzhi(row.attributes.inpatientNo);
			$('#tabsWe').tabs('select', '转科申请');
			$('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','转科申请'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
			return;
		}
	$('#tabsWe').tabs('add',{  
		title:'转科申请',
		// 新内容的URL
		content:"<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",
		closable:true  
	});
	fuzhi(row.attributes.inpatientNo);
} 

//召回
function recall(){
	$("#himid").val('召回');
	var row =  $('#treePatientInfo').tree('getSelected');
	var inpatientNo=row.attributes.inpatientNo;
	var medicalrecordId=row.attributes.medicalrecordId;
	var url="<%=basePath%>nursestation/nurse/recall.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
	if ($('#tabsWe').tabs('exists', '召回')){
		  fuzhi(row.attributes.inpatientNo);
			$('#tabsWe').tabs('select', '召回');
			$('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','召回'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
			return;
		}
	$('#tabsWe').tabs('add',{  
		title:'召回',
		// 新内容的URL
		content:"<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",
		closable:true  
	});
	fuzhi(row.attributes.inpatientNo);
} 

//换医师 
function replaceDoctor(){
	$("#himid").val('换医师');
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.medicalrecordId;
	var inpatientNo=row.attributes.inpatientNo;
	var url="<%=basePath%>nursestation/nurse/replaceDoctorView.action?inpatientNo="+inpatientNo+"&mid="+medicalrecordId+"&menuAlias="+menuAlias;
	if ($('#tabsWe').tabs('exists', '换医师')){  
		   fuzhi(row.attributes.inpatientNo);
	        $('#tabsWe').tabs('select', '换医师');  
	        $('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','换医师'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
	        return;
	    }
	$('#tabsWe').tabs('add',{  
			title:'换医师',  
			 // 新内容的URL
		    content:"<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",    
		    closable:true  
	    
	}); 
	fuzhi(row.attributes.inpatientNo);
}


//出院登记
function leaveHospitalFun(){
	$("#himid").val('出院登记');
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.medicalrecordId;
	var inpatientNo=row.attributes.inpatientNo;
	var url="<%=basePath%>nursestation/nurse/leaveHospital.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
	if ($('#tabsWe').tabs('exists', '出院登记')){  
		fuzhi(row.attributes.inpatientNo);
	        $('#tabsWe').tabs('select', '出院登记');  
	        $('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','出院登记'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
	        return;
	    }
	$('#tabsWe').tabs('add',{    
	    title:'出院登记',    
	    content: "<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",    
	    closable:true    
	});  
	fuzhi(row.attributes.inpatientNo);
}
//婴儿登记
function babyRegistered(){
	$("#himid").val('婴儿登记');
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.medicalrecordId;
	var inpatientNo=row.attributes.inpatientNo;
	var url="<%=basePath%>nursestation/nurse/babyRegistered.action?inpatientNo="+inpatientNo+"&medicalreId="+medicalrecordId+"&menuAlias="+menuAlias;
	if ($('#tabsWe').tabs('exists', '婴儿登记')){
		fuzhi(row.attributes.inpatientNo);
	        $('#tabsWe').tabs('select', '婴儿登记'); 
	        $('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','婴儿登记'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
	        return;
	}
	$('#tabsWe').tabs('add',{    
	    title:'婴儿登记',    
	    content: "<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",    
	    closable:true    
	});  
	fuzhi(row.attributes.inpatientNo);
}

//欠费报警
function openAlertMoney(){
	$("#himid").val('欠费报警');
	 if ($('#tabsWe').tabs('exists', '欠费报警')){    
	        $('#tabsWe').tabs('select', '欠费报警');  
	        return;
	    }
	$('#tabsWe').tabs('add',{    
	    title:'欠费报警',    
	    content:'<iframe scrolling="auto" frameborder="0"  src="<%=basePath%>nursestation/nurse/moneyLineAlert.action"  style="width:100%;height:100%;"></iframe>',    
	    closable:true    
	});  
	$("#alertView").datagrid('reload');
	$('#printReport').linkbutton('disable');
}

//请假
function nurseLeave(){
	$("#himid").val('请假');
	var row =  $('#treePatientInfo').tree('getSelected');
	var medicalrecordId=row.attributes.inpatientNo;
	var url="<%=basePath%>nursestation/nurse/nurseLeave.action?mid="+medicalrecordId+"&menuAlias="+menuAlias;
	 if ($('#tabsWe').tabs('exists', '请假')){    
		 fuzhi(row.attributes.inpatientNo);
	        $('#tabsWe').tabs('select', '请假');  
	        $('#tabsWe').tabs('update',{
				tab:$('#tabsWe').tabs('getSelected','请假'),
				options:{
					content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
				}
			});
	        return;}
	$('#tabsWe').tabs('add',{    
	    title:'请假',    
	    content: "<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",    
	    closable:true 
	});  
	fuzhi(row.attributes.inpatientNo);
};

//病床维护 nurseLeave
function openBed(){
	$("#himid").val('病床维护');
	 if ($('#tabsWe').tabs('exists', '病床维护')){    
	        $('#tabsWe').tabs('select', '病床维护');  
	        return;}
	$('#tabsWe').tabs('add',{    
	    title:'病床维护',    
	    content:'<iframe scrolling="auto" frameborder="0"  src="<%=basePath%>nursestation/nurse/nurseBedInfo.action"  style="width:100%;height:100%;"></iframe>',    
	    closable:true   
	});  
	$('#printReport').linkbutton('disable');
};





function fuzhi(inpatientNo){
	$.ajax({  //根据患者住院流水号查询患者信息
		url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
		data:{inpatientNo:inpatientNo},
		success:function(info){ //
			$("#blh").val(info.medicalrecordId);
			$("#xm").val(info.patientName);
			var ages=DateOfBirth(info.reportBirthday);
			 //年龄
			$("#nl").val(ages.get("nianling")+ages.get('ageUnits'));
			$("#hljb").val(info.tend);
			
			$("#zyys").val(info.houseDocName);
			$("#zzys").val(info.chargeDocName);
			$("#zrys").val(info.chiefDocName);
			$("#zrhs").val(info.dutyNurseName);
			$("#zd").val(info.diagName);
			$("#cw").val(info.bedName);
			if(info.anaphyFlag=="1"){
				$("#gm").val('有');
			}
			else{
				$("#gm").val('无');
			}
		}
	});
	$('#printReport').linkbutton('disable');
	
}

//打开病床收费时间设置
function openDialog(){
	$('#printReport').linkbutton('disable');
	$("#bedTimeDia").window('open'); 
};

function openApply(){
	$("#cancelApply").window('open');
};

//保存按钮
function saveTime(){
	var timeOut=$("#bedTime").timespinner('getText');
	$.ajax({
		type:'post',
		url:'<%=basePath%>nursestation/nurse/saveTimeOfFee.action',
		data:{
			charGTime:timeOut
		}
	});
	$("#bedTime").timespinner('setValue',timeOut);
	$.messager.alert('提示','保存成功！');
	setTimeout(function(){
		$(".messager-body").window('close');
	},3500);
};
function closeTheDia(){
	$("#bedTimeDia").window('close');
};

//取消+更换转往科室
function ApplyCancel(state){
	//住院主表Id
	var zyid=$("#zyid").textbox('getValue');
	//获得住院流水号
	var cc=$("#inpatientHiddenNo").textbox('getValue');
	var appNew=$("#goToDept").combobox('getValue');
	if(zyid==null||zyid==""){
		$.messager.alert('提示','没选中患者');  
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return;
	}
	$.ajax({
		url:'<%=basePath%>nursestation/nurse/saveApply.action',
		data:{
			//住院流水号
			inpatientNo:cc,
			//判断是取消转科还是更换科室
			state:state,
			//新科室
			newDept:appNew
		},
		success:function(){
			$.messager.alert('提示','操作成功!'); 
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		},
		error:function(){
			$.messager.alert('提示','失败，请联系管理员!');
		}
	});
	
};

function windowLoad(){
	$('#printReport').linkbutton('disable');
	$("#treePatientInfo").tree("reload");
	$("#vdForm").form('clear');
};

function loadEvent(data){
	$("#showView").show();
	$("#hideView").hide();
	$("#stact").val("hide");
};

function openPanel(title,url,width) {
	var eastpanel=$('#nurList'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		$('#divout').layout('remove','east');
		$('#divout').layout('add', {
			region : 'east',
			width :width ,
			split : true,
			href : url,
			closable : true
		});
	}else{//打开新面板
		$('#divout').layout('add', {
			region : 'east',
			width :width ,
			split : true,
			href : url,
			closable : true
		});
	}
};

//加载dialog
function Adddilog(title, url) {
	$('#dialog').dialog({    
	    title: title,    
	    width: '60%',    
	    height:'80%',    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true   
   });    
};
//打开dialog
function openDialog() {
	$('#printReport').linkbutton('disable');
	$('#dialog').dialog('open'); 
};
//关闭dialog
function closeDialog() {
	$('#dialog').dialog('close');  
};
function addNuiLeave(){
	$('#leaveid').val("");
	$('#leaveDays').textbox('clear');
	$('#leaveDate').textbox('clear');
	$('#remark').textbox('clear');
};



//打印病危病重通知单
function printBWZTZD(){
	
	if($("#showTabsTitle").val()=="床头卡"){
		var row = $("#listView").datagrid("getSelected");//unselectAll
		if(row){
			if(row.inpatientNoLS){
				 var timerStr = Math.random();
				if(row.patientStatus=='01'){
				 	 window.open ("<c:url value='/iReport/iReportPrint/iReportToNurseViewPrint.action?randomId='/>"+timerStr+"&inpatientNo="+row.inpatientNoLS+"&fileName=BZTZD",'newwindow1','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
				}else if(row.patientStatus=='02'){
				 	 window.open ("<c:url value='/iReport/iReportPrint/iReportToNurseViewPrint.action?randomId='/>"+timerStr+"&inpatientNo="+row.inpatientNoLS+"&fileName=BWTZD",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
				}else{
					$.messager.alert('系统消息','只有病危或者病重的患者才能打印通知单!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}else{
				$.messager.alert('系统消息','必须有病历号才能打印通知单!');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}else{
			$.messager.alert('系统消息','请选择你所要打印通知单的患者!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
		$("#listView").datagrid("unselectAll")
	}else{
		$('#printReport').linkbutton('disable');
	}
	
};


//打印出生证明
function printCSZMB(){
	var row = $("#babyList").datagrid("getSelected");
	if(row){
		var ID = row.itemid;
		
		var timerStr = Math.random();
	 	window.open ("<c:url value='/iReport/iReportPrint/iReportToCSZMB.action?randomId='/>"+timerStr+"&ID="+ID+"&fileName=CSZMB",'newwindow'+ID,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
	} 
};

//打印按钮显示
 	function show(){
 		var row = $("#babyList").datagrid("getSelected");
 		var ID = row.itemid;
 		if (!ID) {
			 $('#printCSZMB').linkbutton('disable');
 		}else{
			$('#printCSZMB').linkbutton('enable');
 		}
 	};


//验证身份证格式是否正确
function checkIdCardNo(idCard)
{
    var id=idCard;
    var id_length=id.length;

    if (id_length==0){
        return false;
    }

    if (id_length!=15 && id_length!=18){
        return false;
    }

    if (id_length==15){
        yyyy="19"+id.substring(6,8);
        mm=id.substring(8,10);
        dd=id.substring(10,12);

        if (mm>12 || mm<=0){
            return false;
        }

        if (dd>31 || dd<=0){
            return false;
        }

        birthday=yyyy+ "-" +mm+ "-" +dd;

        if ("13579".indexOf(id.substring(14,15))!=-1){
            sex="1";
        }else{
            sex="2";
        }
    }else if (id_length==18){
        if (id.indexOf("X") > 0 && id.indexOf("X")!=17 || id.indexOf("x")>0 && id.indexOf("x")!=17){
            return false;
        }

        yyyy=id.substring(6,10);
        if (yyyy>2200 || yyyy<1900){
            return false;
        }

        mm=id.substring(10,12);
        if (mm>12 || mm<=0){
            return false;
        }

        dd=id.substring(12,14);
        if (dd>31 || dd<=0){
            return false;
        }

        if (id.charAt(17)=="x" || id.charAt(17)=="X")
        {
            if ("x"!=GetVerifyBit(id) && "X"!=GetVerifyBit(id)){
                return false;
            }

        }else{
            if (id.charAt(17)!=GetVerifyBit(id)){
                return false;
            }
        }

        birthday=id.substring(6,10) + "-" + id.substring(10,12) + "-" + id.substring(12,14);
        if ("13579".indexOf(id.substring(16,17)) > -1){
            sex="1";
        }else{
            sex="2";
        }
    }

    return true;
}


//15位转18位身份证中,计算校验位即最后一位
function GetVerifyBit(id){
    var result;
    var nNum=eval(id.charAt(0)*7+id.charAt(1)*9+id.charAt(2)*10+id.charAt(3)*5+id.charAt(4)*8+id.charAt(5)*4+id.charAt(6)*2+id.charAt(7)*1+id.charAt(8)*6+id.charAt(9)*3+id.charAt(10)*7+id.charAt(11)*9+id.charAt(12)*10+id.charAt(13)*5+id.charAt(14)*8+id.charAt(15)*4+id.charAt(16)*2);
    nNum=nNum%11;
    switch (nNum) {
       case 0 :
          result="1";
          break;
       case 1 :
          result="0";
          break;
       case 2 :
          result="X";
          break;
       case 3 :
          result="9";
          break;
       case 4 :
          result="8";
          break;
       case 5 :
          result="7";
          break;
       case 6 :
          result="6";
          break;
       case 7 :
          result="5";
          break;
       case 8 :
          result="4";
          break;
       case 9 :
          result="3";
          break;
       case 10 :
          result="2";
          break;
    }
    return result;
}
//15位转18位身份证
function Get18(idCard){
 if (CheckValue(idCard)){
  var id = idCard;
  var id18=id;
  if (id.length==0){
   $.messager.alert('提示','请输入15位身份证号！');    
   setTimeout(function(){
		$(".messager-body").window('close');
	},3500);
   return false;
  }
  if (id.length==15){
   if (id.substring(6,8)>20){
    id18=id.substring(0,6)+"19"+id.substring(6,15);
   }else{
    id18=id.substring(0,6)+"20"+id.substring(6,15);
   }

   id18=id18+GetVerifyBit(id18);
  }

  return id18;
 }else{
  return false;
 }
}

</script>
</head>
<body>
<div id="cc" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north'" style="height:77px;padding:16px;border-top:0">
        <shiro:hasPermission name="${menuAlias }:function:bed">
 		    <a class="easyui-linkbutton" onclick="openBed()" data-options="iconCls:'icon-chart_pie',plain:true">床位维护</a> 
 	    </shiro:hasPermission>
 	    <a class="easyui-linkbutton" onClick="openAlertMoney()" data-options="iconCls:'icon-exclamation',plain:true">欠费报警</a> 
		<a class="easyui-linkbutton" id="printReport" onclick="printBWZTZD()" data-options="iconCls:'icon-2012081511202',disabled:true,plain:true">打印</a><span style="color:red;font-size:12px;" class="nurseViewFont">切换视图可打印</span>
	    	<div style="float:right">
			<p style="font-weight:bold;">该科共有 <span id='bedNum' style="font-weight: bold;"></span> 张床，其中空床 <span id='bedC1' style="font-weight: bold;"></span> 张，编制加床<span id='bedC2' style="font-weight: bold;"></span>张，床位占用率是 <span id='bili' style="font-weight: bold;"></span>%,<br>一级护理<span id='infoC1' style="font-weight: bold;"></span>人，病危<span id='infoC2' style="font-weight: bold;"></span>人，病人总数<span id='infoC' style="font-weight: bold;"></span>人,科室：<span id='deptName1' style="font-weight: bold;"></span>  病房：<span id='deptName2' style="font-weight: bold;"></span></p>
			<p><input type="text" id="showTabsTitle" hidden value="床头卡"/></p>
		</div>
	</div>
	<div data-options="region:'west',split:true" style="width:15%;padding-left: 20px;border-width:0 1px 0 0;">
		<ul id="treePatientInfo"></ul>
		<div id="rightClick" class="easyui-menu" data-options="" style="width: 120px;">
			<div id="jiezhen"  onclick="acceptsName()"   data-options="iconCls:'icon-mini_edit'">接诊</div>
			<div id="qingjia"  onclick="nurseLeave()"         data-options="iconCls:'icon-mini_edit'">请假</div>
			<div id="zhaohui"  onclick="recall()"        data-options="iconCls:'icon-mini_edit'">召回</div>
			<div id="chuyuan"  onclick="leaveHospitalFun()" data-options="iconCls:'icon-mini_edit'">出院登记</div>
			<div id="huanyis"  onclick="replaceDoctor()" data-options="iconCls:'icon-mini_edit'">换医师</div>
			<div id="yinger"   onclick="babyRegistered()" data-options="iconCls:'icon-mini_edit'">婴儿登记</div>
			<div id="zhuanke"   onclick="transfer()" data-options="iconCls:'icon-mini_edit'">转科申请</div>
			<div id="quxiao"   onclick="transferqx()" data-options="iconCls:'icon-mini_edit'">取消转科</div>
			<div id="confirm"   onclick="confirm()" data-options="iconCls:'icon-mini_edit'">确认转科</div>
			<div id="packbed"   onclick="packbed()" data-options="iconCls:'icon-mini_edit'">包床</div>
			<div id="huanbed"   onclick="huanbed()" data-options="iconCls:'icon-mini_edit'">换床</div>
		</div>
	</div>  
	<div data-options="region:'center'" style="width: 85%;">
			<div class="easyui-layout" id="infoLayOut" data-options="fit:true">
				<div
					data-options="region:'west',title:'患者信息',collapsed:false,border:false"
					style="width: 225px;">
					<form id="vdForm">
						<table
							style="width: 225px; height: 60%; padding-top: 5px; padding-left: 5px;">
							<tr>
								<td>病 历 号：<input id="blh" readonly type="text"
									style="border: 0; width: 100" /></td>
								<th></th>
							</tr>
							<tr>
								<td>姓&nbsp;&nbsp;名：<input id="xm" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>年&nbsp;&nbsp;龄：<input id="nl" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>床&nbsp;&nbsp;位：<input id="cw" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>护理级别：<input id="hljb" readonly type="text"
									style="border: 0; width: 100" /></td>
								<th></th>
							</tr>
							<tr>
								<td>住院医生：<input id="zyys" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>主治医生：<input id="zzys" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>主任医生：<input id="zrys" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>责任护士：<input id="zrhs" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
							<tr>
								<td>入院诊断：<input id="zd" readonly type="text"
									style="border: 0; width: 120" /></td>
								<th></th>
							</tr>
							<tr>
								<td>过 敏 史：<input id="gm" readonly type="text"
									style="border: 0; width: 80" /></td>
								<th></th>
							</tr>
						</table>
					</form>
					<div
						style="background: #EBEBEB; width: 220px; height: 200px; padding-top: 5px; padding-left: 5px;">
						<p>提示：</p>
						<br>
						<p style="width: 100%">卡片右上角的★代表护理级别</p>
						<br>
						<p>
							<strong style="color: red; font-size: 18">★</strong>特级护理 <strong
								style="color: yellow; font-size: 18">★</strong>一级护理
						</p>
						<br>
						<p>
							<strong style="color: blue; font-size: 18">★</strong>二级护理 <strong
								style="color: purple; font-size: 18">★</strong>三级护理
						</p>
						<br>
					</div>
				</div>
				<div data-options="region:'center'" style="border-top:0">
					<div id="tabsWe" class="easyui-tabs" style="width: 100%; height: 100%;" data-options="border:false,tabHeight:'24',fit:true">
						<div title="床头卡"><jsp:include page="nurseBedView.jsp"></jsp:include></div>
					</div>
				</div>
			</div>
		</div>
	<div id="dialog"></div>
</div>
<div id="openMajor" class="easyui-window" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" title="取消转科" style="width:100%;height:100%;">
	<iframe scrolling="auto" id='openMajorIframe' frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
</div>
<div id="openRecall" class="easyui-window" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" title="召回" style="width:100%;height:100%;">
	<iframe scrolling="auto" id='openRecallIframe' frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
</div>
<input type="text" id="himid" hidden >
</body>
</html>