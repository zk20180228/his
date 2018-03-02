<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院预交金管理</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
		<style type="text/css">
		.tableCss {
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #95b8e7;
			border-top: 1px solid #95b8e7;
		}
		
		.tableLabel {
			text-align: right;
			width: 150px;
		}
		
		.tableCss td {
			border-right: 1px solid #95b8e7;
			border-bottom: 1px solid #95b8e7;
			padding: 5px 15px;
			word-break: keep-all;
			white-space: nowrap;
		}
		</style>
		<script type="text/javascript">
		var codeBankMap = '';
		var payType='';
		var suretyType='';
		
		$(function(){
			//渲染银行
			$.ajax({
				url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bank',
				type:'post',
				success: function(date) {
					codeBankMap = date;
				}
			});
			//支付类型渲染
			$.ajax({
				url:  '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=payway',
				success: function(data) {
					payType = data;
				}
			});
			//担保类型
			$.ajax({
				url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=suretytype',
				success: function(data) {
					suretyType = data;
				}
			});
			
			bindEnterEvent('medicale',readerByInpatientNo,"easyui");
			bindEnterEvent('idcard',readerByIdcardNo,"easyui");
			$("#listSurety").datagrid({
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
			$("#listSurety").datagrid('loadData', { total: 0, rows: [] });
			$("#list").datagrid({
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
			$("#list").datagrid('loadData', { total: 0, rows: [] });
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
					$('#medicale').textbox('setValue',data);
					readerByInpatientNo();
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
						$('#medicale').textbox('setValue',data);
						readerByInpatientNo();
					}
				});
			};
		/*******************************结束读身份证***********************************************/
		//担保金方法 
		function suretyList(inpatientNo){
			$('#listSurety').datagrid({
				url:'<%=basePath%>inpatient/surety/querySurety.action?menuAlias=${menuAlias}',
				queryParams:{inpatientNo:inpatientNo},
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				onClickRow:function(rowIndex, rowData){
					$('#payOutpre').linkbutton('disable');
					$('#suretyOut').linkbutton('enable');
				}
			});
		}
		//预交金方法
		function prepaidList(inpatientNo){
			$('#list').datagrid({
				url:'<%=basePath%>inpatient/inprePay/queryInprePay.action?menuAlias=${menuAlias}',
				queryParams:{inpatientNo:inpatientNo},
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				onClickRow:function(rowIndex, rowData){
					$('#suretyOut').linkbutton('disable');
					$('#payOutpre').linkbutton('enable');
					if(rowData.prepayState==0){
						$('#printId').linkbutton('enable');
					}else{
						$('#printId').linkbutton('disable');
					}
				}
			});
		}
		
		/**
		 *  
		 * @Description：读卡操作根据就诊卡号获得患者信息
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function readerByIdcardNo(){
			var idcardNos = $('#idcard').textbox('getText');
			var idcardNo=Trim(idcardNos);
			if(idcardNo!=null&&idcardNo!=''){
				//获得患者信息
				$('#infoList').datagrid({
					url:'<%=basePath%>inpatient/inprePay/findPatientByIdcardNo.action?menuAlias=${menuAlias}',
					queryParams:{idcardNo:idcardNo},
					onLoadSuccess:function(data){
						if(data.rows.length==0){
							$('#medicale').textbox('setValue','');
							$('#inpatientNo').val('');
							$('#idcard').textbox('setValue','');
							$('#name').text('');
							$('#sex').text('');
							$('#certificatesType').text('');
							$('#certificatesNo').text('');
							$('#birthDay').text('');
							$('#nation').text('');
							$('#phone').text('');
							$('#handBook').text('');
							$('#dayLimit').text('');
							$('#nationAlity').text('');
							$('#payInpre').linkbutton('disable');
							$('#surety').linkbutton('disable');
							$('#printId').linkbutton('disable');
							$.messager.alert('提示',"该病历号未查询到相关患者信息，请重新输入！");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else if(data.rows.length==1){
							$('#medicale').textbox('setValue',data.rows[0].medicale);
							$('#inpatientNo').val(data.rows[0].inpatientNo);
							$('#idcard').textbox('setValue',data.rows[0].idcard);
							$('#name').text(data.rows[0].name);
							$('#sex').text(data.rows[0].sexName);
							$('#certificatesType').text(data.rows[0].certificatesType);
							$('#certificatesNo').text(data.rows[0].certificatesNo);
							$('#birthDay').text(data.rows[0].birthDay);
							$('#nation').text(data.rows[0].nation);
							$('#phone').text(data.rows[0].phone);
							$('#handBook').text(data.rows[0].handBook);
							$('#dayLimit').text(data.rows[0].dayLimit);
							$('#nationAlity').text(data.rows[0].nationAlity);
							$('#payInpre').linkbutton('enable');
							$('#surety').linkbutton('enable');
							$('#printId').linkbutton('enable');
							prepaidList(data.rows[0].inpatientNo);
							suretyList(data.rows[0].inpatientNo);
						}else{
							$('#windowInfo').window('open');
						}
					},
					onDblClickRow:function(rowIndex, rowData) {//双击查看
						$('#medicale').textbox('setValue',rowData.medicale);
						$('#inpatientNo').val(rowData.inpatientNo);
						$('#idcard').textbox('setValue',rowData.idcard);
						$('#name').text(rowData.name);
						$('#sex').text(rowData.sexName);
						$('#certificatesType').text(rowData.certificatesType);
						$('#certificatesNo').text(rowData.certificatesNo);
						$('#birthDay').text(rowData.birthDay);
						$('#nation').text(rowData.nation);
						$('#phone').text(rowData.phone);
						$('#handBook').text(rowData.handBook);
						$('#dayLimit').text(rowData.dayLimit);
						$('#nationAlity').text(rowData.nationAlity);
						$('#payInpre').linkbutton('enable');
						$('#surety').linkbutton('enable');
						$('#printId').linkbutton('enable');
						prepaidList(rowData.medicale);
						suretyList(rowData.medicale);
						
						$('#windowInfo').window('close');
					}
				});
			}else{
				$.messager.alert('提示',"请输入就诊卡号进行查询！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		function Trim(str)
		{ 
		    return str.replace(/(^\s*)|(\s*$)/g, ""); 
		}
		
		
		/**
		 *  
		 * @Description：根据病历号获得患者信息
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function readerByInpatientNo(){
			var medicales = $('#medicale').textbox('getText');
			var medicale=Trim(medicales);
			if(medicale!=null&&medicale!=''){
				$('#infoList').datagrid({
					url:'<%=basePath%>inpatient/inprePay/findPatientByInpatientNo.action?menuAlias=${menuAlias}',
					queryParams:{medicale:medicale},
					onLoadSuccess:function(data){
						if(data.rows.length==0){
							$('#medicale').textbox('setValue','');
							$('#inpatientNo').val('');
							$('#idcard').textbox('setValue','');
							$('#name').text('');
							$('#sex').text('');
							$('#certificatesType').text('');
							$('#certificatesNo').text('');
							$('#birthDay').text('');
							$('#nation').text('');
							$('#phone').text('');
							$('#handBook').text('');
							$('#dayLimit').text('');
							$('#nationAlity').text('');
							$('#payInpre').linkbutton('disable');
							$('#surety').linkbutton('disable');
							$.messager.alert('提示',"该病历号未查询到相关患者信息，请重新输入！");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else if(data.rows.length==1){
							$('#medicale').textbox('setValue',data.rows[0].medicale);
							$('#inpatientNo').val(data.rows[0].inpatientNo);
							$('#idcard').textbox('setValue',data.rows[0].idcard);
							$('#name').text(data.rows[0].name);
							$('#sex').text(data.rows[0].sexName);
							$('#certificatesType').text(data.rows[0].certificatesType);
							$('#certificatesNo').text(data.rows[0].certificatesNo);
							$('#birthDay').text(data.rows[0].birthDay);
							$('#nation').text(data.rows[0].nation);
							$('#phone').text(data.rows[0].phone);
							$('#handBook').text(data.rows[0].handBook);
							$('#dayLimit').text(data.rows[0].dayLimit);
							$('#nationAlity').text(data.rows[0].nationAlity);
							$('#payInpre').linkbutton('enable');
							$('#surety').linkbutton('enable');
							prepaidList(data.rows[0].inpatientNo);
							suretyList(data.rows[0].inpatientNo);
						}else{
							$('#windowInfo').window('open');
						}
					},
					onDblClickRow:function(rowIndex, rowData) {//双击查看
						$('#medicale').textbox('setValue',rowData.medicale);
						$('#inpatientNo').val(rowData.inpatientNo);
						$('#idcard').textbox('setValue',rowData.idcard);
						$('#name').text(rowData.name);
						$('#sex').text(rowData.sexName);
						$('#certificatesType').text(rowData.certificatesType);
						$('#certificatesNo').text(rowData.certificatesNo);
						$('#birthDay').text(rowData.birthDay);
						$('#nation').text(rowData.nation);
						$('#phone').text(rowData.phone);
						$('#handBook').text(rowData.handBook);
						$('#dayLimit').text(rowData.dayLimit);
						$('#nationAlity').text(rowData.nationAlity);
						$('#payInpre').linkbutton('enable');
						$('#surety').linkbutton('enable');
						prepaidList(rowData.medicale);
						suretyList(rowData.medicale);
						
						$('#windowInfo').window('close');
					}
				});
			}else{
				$.messager.alert('提示',"请输入病历号进行查询！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		
		/**
		 *  
		 * @Description：存预交金弹出缴纳预交金窗口
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function payInpre(){
			 var flg=$("#invoiceNoflay").val();
			 if(flg=="success"){
				 AdddilogInfoView("modlDivId","【"+$('#name').text()+"】预交金","<%=basePath%>inpatient/inprePay/payInpre.action",390,260);
			 }else{
				 if(flg=="error"){
						$.messager.alert("操作提示", "请领取发票,领取发票后请刷新页面");
						return;
				 }
			 }
			
		}
		/**
		 *  
		 * @Description：返回担保金
		 *
		 */
		 function payOutsurety(){
			 var reflag=true;
			 var inpatientNo=$('#inpatientNo').val();
			 var obj=$('#listSurety').datagrid('getChecked');
			 var arr =new Array();
			 if(obj.length>0){
				 $.each(obj,function(i,n){
					 arr[i]=n.id;
					 j=i+1;
					 if(n.state==0){
						$.messager.alert('提示',"担保金已返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;		
					}else if(n.state==2){
						$.messager.alert('提示',"担保金已进行过补打，不能再次返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;
					}
				 });
				 if(reflag){
					 $.messager.confirm('确认对话框', '您想要返回'+j+'条担保金吗？', function(r){
						 if (r){
							 $.ajax({
								 url:'<%=basePath %>inpatient/surety/removeSurety.action',
								 type:'post',
								 traditional:true,//数组提交解决方案
								 data:{'ids':arr},
								 dataType:'json',
								 success:function(data){
										$.messager.alert('提示',"返回成功！");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										$("#listSurety").datagrid("reload",{inpatientNo:inpatientNo});
								 },error : function() {
								 }
							 })
										
						 }
								 
					 })
				 }
				 
			 }
		}
		/**
		 *  
		 * @Description：返回预交金
		 * @Author：tangfeishuai
		 * @CreateDate：2016-5-26下午18:56:31  
		 * @Modifier：tangfeishuai
		 * @ModifyDate：2016-5-26 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function payOutpre(){
			var reflag=true;
			var inpatientNo=$('#inpatientNo').val();
			var obj=$('#list').datagrid('getSelections');
			var m=obj.length;
			var arr =new Array();
			if(obj.length>0){
				$.each(obj,function(i,n){
					arr[i]=n.id;
					j=i+1;
					if(n.prepayState==1){
						$.messager.alert('提示',"预交金已返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;
					}else if(n.prepayState==2){
						$.messager.alert('提示',"预交金已进行过补打，不能再次返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;
					}else if(n.balanceState==1||n.balanceState==2){
						$.messager.alert('提示',"预交金已结算或结转，不能返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;
					}else if(n.transFlag==2){
						$.messager.alert('提示',"预交金的转押金尚未打印，不能返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;
					}else if(n.daybalanceNo==1){
						$.messager.alert('提示',"票据已日结，不能返回！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						reflag=false;
						return;
					}else{
						$.ajax({
							url:'<%=basePath %>inpatient/inprePay/isStopAcountNow.action',
							async:false,
							type:'post',
							data:{'inpatientNo':n.inpatientNo},
							dataType:'json',
							success:function(data){
								if(data.stopAcount==1){
									$.messager.alert('提示',"患者处于封账状态，可能正在进行结算，请稍后操作！");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									reflag=false;
									return;
								}
							},
							error : function() {
								reflag=false;
								return;
							}
						});
					} 
				});
				if(reflag){
					$.messager.confirm('确认对话框', '您想要返回'+j+'条预交金吗？', function(r){
						if (r){
							$.ajax({
								url:'<%=basePath %>inpatient/inprePay/removeInprePay.action',
								type:'post',
								traditional:true,//数组提交解决方案
								data:{'ids':arr},
								dataType:'json',
								success:function(data){
									$.messager.alert('提示',"返回成功！");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									$("#list").datagrid("reload",{inpatientNo:inpatientNo});
								},error : function() {
								}
							});
						}
					});
				}
		
			}
		}
		
		/**
		 *  
		 * @Description：存担保金弹出缴纳担保金窗口
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function surety(){
			 var flg=$("#invoiceNoflay").val();
			 if(flg=="success"){
					AdddilogInfoView("modlDivId","【"+$('#name').text()+"】担保金","<%=basePath%>inpatient/surety/surety.action",390,320);
			 }else{
				 if(flg=="error"){
						$.messager.alert("操作提示", "请领取发票,领取发票后请刷新页面");
						return;
				 }
			 }
			 
		}
		
		/**
		 *  
		 * @Description：加载模式窗口
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function AdddilogInfoView(id,title,url,width,height) {
			$('#'+id).dialog({  
			    title: title,    
			    width: width,    
			    height: height, 
			    closed: false,    
			    cache: false,
			    top:100,
			    href: url,    
			    modal: true   
			});   
		}
		
		/**
		 *  
		 * @Description：关闭模式对话框
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param：id要关闭div的id
		 */
		function closeInfoModl(id){
			$('#'+id).dialog('close');
		}
		
		/**
		 *  
		 * @Description：支付方式渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param1：value 单元格的值
		 * @param2：row行数据
		 * @param3：index索引
		 * @return:Map中key对应值
		 */
		function funPayWay(value,row,index){
			for(var i=0;i<payType.length;i++){
				if(value==payType[i].encode){
					return payType[i].name;
				}
			}

		}
		
		/**
		 *  
		 * @Description：银行渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param1：value 单元格的值
		 * @param2：row行数据
		 * @param3：index索引
		 * @return:Map中key对应值
		 *
		 */
		function funOpenBank(value,row,index){
			for(var i=0;i<codeBankMap.length;i++){
				  if(codeBankMap[i].encode==value){
					  return codeBankMap[i].name;
				  }
			}
		}
		/**
		 *  
		 * @Description：预交金状态渲染
		 * @Author：tangfeishuai
		 * @CreateDate：2016-5-26 下午18:56:31  
		 * @Modifier：tangfeishuai
		 * @ModifyDate：2016-5-26 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param1：value 单元格的值
		 * @param2：row行数据
		 * @param3：index索引
		 * @return:Map中key对应值
		 *
		 */
		function funPrepayState(value,row,index){
			if(value==0){
				return "收取";
			}else if(value==1){
				return "返回";
			}else if(value==2){
				return "补打";
			}else if(value==3){
				return "结算召回返回";
			}
		}
		/**担保金状态渲染
		**/
		function funSuretyState(value,row,index){
			if(value==1){
				return "收取";
			}else if(value==0){
				return "返回";
			}else if(value==2){
				return "补打";
			}
		}
		
		
		/**
		 *  
		 * @Description：担保类型渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-3-09 下午18:56:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-3-09 下午18:56:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param1：value 单元格的值
		 * @param2：row行数据
		 * @param3：index索引
		 * @return:Map中key对应值
		 *
		 */
		function funSuretyType(value,row,index){
			for(var i=0;i<suretyType.length;i++){
				if(suretyType[i].encode==value){
					return suretyType[i].name;
				}
			}
		}
		function print(){
			var row = $('#list').datagrid('getSelected');
			if(row==null){
				$.messager.alert("提示", "请选择一条预交金信息进行打印！");
				return;
			}
			var mid = row.id;
			var timerStr = Math.random();
			window.open ("<c:url value='/inpatient/inprePay/iReportzyyjj.action?randomId='/>"+timerStr+"&id="+mid+"&fileName=zhuyuanyujiaojin",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}
		</script>
	</head>
	<body style="margin: 0px; padding: 0px">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 155px;">
			<div style="padding:5px 5px 5px 5px;">
				<div style="border:1px solid #95b8e7;padding:5px 5px 5px 5px;" class="changeskin">
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a id="reader" onclick="readerByInpatientNo()"  class="easyui-linkbutton readCard" data-options="iconCls:'icon-search'" type_id="inprePay_card_no" cardNo="">查询</a>
			    </shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:readCard">
					<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
				</shiro:hasPermission>
	        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
	        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:save">   
					<a id="payInpre" onclick="payInpre()" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">存预交金</a>
			    </shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">   
					<a id="payOutpre" onclick="payOutpre()" class="easyui-linkbutton" data-options="iconCls:'icon-back',disabled:'true'">返回预交金</a>
			    </shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:save">   
					<a id="surety" onclick="surety()" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:'true'">存担保金</a>
			    </shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">   
					<a id="suretyOut" onclick="payOutsurety()" class="easyui-linkbutton" data-options="iconCls:'icon-back',disabled:'true'">返回担保金</a>
			    </shiro:hasPermission>
			    <shiro:hasPermission name="${menuAlias}:function:print">   
					<a id="printId" onclick="print()" class="easyui-linkbutton" data-options="iconCls:'icon-printer',disabled:'true'">打印</a>
			    </shiro:hasPermission>
				</div>
			</div>
			<div style="padding:5px 5px 5px 5px;">
				<table id="table" class="tableCss" style="width: 100%;height: 80px;">
				<input type="hidden" id="invoiceNoflay" value="${invoiceNoflag }"/>
					<tr>
						
						<td class="tableLabel">病历号：</td>
						<td style="width: 200px;"><input  class="easyui-textbox"   id="medicale" data-options="prompt:'请输入病历号查询'" style="width:200" > 
						<td class="tableLabel">就诊卡号：</td>
						<td style="width: 200px;">
							<input class="easyui-textbox" id="idcard" data-options="prompt:'请输入就诊卡号查询'" style="width:200"/></td>
							<input id="inpatientNo" type="hidden" /></td>
						<td class="tableLabel">姓名：</td>
						<td id="name" style="width: 200px;"></td>
						<td class="tableLabel">性别：</td>
						<td id="sex" style="width: 200px;"></td>
					</tr>
					<tr>
						<td class="tableLabel">证件类型：</td>
						<td id="certificatesType" style="width: 200px;"></td>
						<td class="tableLabel">证件号：</td>
						<td id="certificatesNo" style="width: 200px;"></td>
						
						<td class="tableLabel">出生日期：</td>
						<td id="birthDay" style="width: 200px;"></td>
						<td class="tableLabel">民族：</td>
						<td id="nation" style="width: 200px;"></td>
						
					</tr>
					<tr>
						<td class="tableLabel">电话：</td>
						<td id="phone" style="width: 200px;"></td>
						<td class="tableLabel">医保号：</td>
						<td id='handBook' style="width: 200px;"></td>
						<td class="tableLabel">籍贯：</td>
						<td  colspan="3" id='nationAlity' ></td>
					</tr>
				</table>
			</div>
		</div>
		<div data-options="region:'center',border:false" >
				<div id="tt" class="easyui-tabs" data-options="fit:true">
					<div title="预交金记录">   
						<table id="list" style="height:200px" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
							<thead>
								<tr>
									<th data-options="field:'inpatientNo',hidden:true"></th>
									<th data-options="field:'payWay',formatter:funPayWay,width:200">支付方式</th>
									<th data-options="field:'prepayCost',width:200" align="right" halign="left">预交金额</th>
									<th data-options="field:'openAccounts',width:200">开户帐户</th>
									<th data-options="field:'openBank',formatter:funOpenBank,width:200">开户银行</th>
									<th data-options="field:'prepayState',formatter:funPrepayState,width:200">预交金状态</th>
									<th data-options="field:'createTime',width:200">操作日期</th>
								</tr>
							</thead>
						</table>
					</div>   
					<div title="担保金记录">   
						<table id="listSurety" class="easyui-datagrid" style="height:270px" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
							<thead>
								<tr>
									<th data-options="field:'payWay',formatter:funPayWay,width:200">支付方式</th>
									<th data-options="field:'suretyCost',width:200" align="right" halign="left">担保金额</th>
									<th data-options="field:'openAccounts',width:200">开户帐户</th>
									<th data-options="field:'openBank',formatter:funOpenBank,width:200">开户银行</th>
									<th data-options="field:'suretyType',formatter:funSuretyType,width:200">担保类型</th>
									<th data-options="field:'state',formatter:funSuretyState,width:200">担保状态</th>
									<th data-options="field:'createTime',width:200">操作日期</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
		</div>
	</div>
		<div id="modlDivId" style="left:0;top:0"></div>
		<div id="windowInfo" class="easyui-window" title="患者信息" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:500px;height:500px;">
		 	<table id="infoList" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'name'" style="width:100px">姓名</th>
						<th data-options="field:'sexName'" style="width:50px">性别</th>
						<th data-options="field:'certificatesType'" style="width:100px">证件类型</th>
						<th data-options="field:'certificatesNo'" style="width:200px">证件号</th>
					</tr>
				</thead>
			</table>
		</div>
		
	</body>
</html>