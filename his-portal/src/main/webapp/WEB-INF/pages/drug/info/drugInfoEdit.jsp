<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
	<body>
		<div id="p" class="easyui-panel"  style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="editForm" action="" method="post">
				<input type="hidden" id="id" name="id" value="${drugInfo.id}">
				<div id="tt" class="easyui-tabs" data-options="" style="">
					<div title="药品信息" style="padding:10px" >
						<jsp:include page="firstEditTab.jsp"></jsp:include>
					</div>
					<div title="使用方法" style="padding:10px">
						<jsp:include page="secEditTab.jsp"></jsp:include>
					</div>
				</div>
			</form>		
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
			var isStopNum=0;
			$(function(){
				if($('#stopFlg').combobox('getText')=="是"){
					$("#StopReasonHidden").show(); 
				}else{
					$("#StopReasonHidden").hide();
				}
				//判断是否编辑状态，编辑状态修改复选框选中状态
				if($('#drugIscitylimit').val()==1){
				    $('#drugIscitylimitch').attr("checked", true);
				}if($('#drugIsownexpense').val()==1){
				    $('#drugIsownexpensech').attr("checked", true);
				
				}if($('#drugIsprovincelimit').val()==1){
				    $('#drugIsprovincelimitch').attr("checked", true);
				}
				if($('#drugIsnewh').val()==1){
					$('#drugIsnew').attr("checked", true); 
				}
				if($('#stopFlgHidden').val()==1){
					$('#stopFlg').attr("checked", true); 
				}
				if($('#drugIstestsensitivityh').val()==1){
					$('#drugIstestsensitivity').attr("checked", true); 
				}
				if($('#drugIsgmph').val()==1){
					$('#drugIsgmp').attr("checked", true); 
				}
				if($('#drugIsotch').val()==1){
					$('#drugIsotc').attr("checked", true); 
				}
				if($('#drugIslackh').val()==1){
					$('#drugIslack').attr("checked", true); 
				}
				if($('#drugIsterminalsubmith').val()==1){
					$('#drugIsterminalsubmit').attr("checked", true); 
				}
				if($('#drugIsscreenh').val()==1){
					$('#drugIsscreen').attr("checked", true); 
				}
				if($('#drugIsagreementprescriptionh').val()==1){
					$('#drugIsagreementprescription').attr("checked", true); 
				}
				if($('#drugIscooperativemedicalh').val()==1){
					$('#drugIscooperativemedical').attr("checked", true); 
				}
				if($('#drugIstenderh').val()==1){
					$('#drugIstender').attr("checked", true); 
				}
				if ($('#appendFlgh').val() == 1) {
	         	$('#appendFlg').attr("checked", true);
			        }
			    if ($('#supriceFlgh').val() == 1) {
			         	$('#supriceFlg').attr("checked", true);
			        }
			        //最小单位
			    $('#CodeMinimumunit').combobox({
			    	url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=minunit",
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    onLoadSuccess: function() {//请求成功后
						$(list).each(function(){
							  if ($('#CodeMinimumunit').val() == this.Id) {
								  $('#CodeMinimumunit').combobox("select",this.Id);
							   }
						});
					}
				});
				//计量单位
			    $('#drugDoseunitTmp').combobox({
			    	url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=doseUnit",
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    onLoadSuccess: function() {//请求成功后
						$(list).each(function(){
							  if ($('#doseUnit').val() == this.Id) {
								  $('#doseUnit').combobox("select",this.Id);
							   }
						});
					}
				});
				//重量单位
			    $('#weightUnit').combobox({
			    	url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugPackagingunit",
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    onLoadSuccess: function() {//请求成功后
						$(list).each(function(){
							  if ($('#weightUnit').val() == this.Id) {
								  $('#weightUnit').combobox("select",this.Id);
							   }
						});
					},onHidePanel:function(none){
					    var data = $(this).combobox('getData');
					    var val = $(this).combobox('getValue');
					    var result = true;
					    for (var i = 0; i < data.length; i++) {
					        if (val == data[i].encode) {
					            result = false;
					        }
					    }
					    if (result) {
					        $(this).combobox("clear");
					    }else{
					        $(this).combobox('unselect',val);
					        $(this).combobox('select',val);
					    }
					},
					filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               }
				});
				//体积单位
			    $('#volUnit').combobox({
			    	url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugPackagingunit",
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    onLoadSuccess: function() {//请求成功后
						$(list).each(function(){
							  if ($('#volUnit').val() == this.Id) {
								  $('#volUnit').combobox("select",this.Id);
							   }
						});
					},onHidePanel:function(none){
					    var data = $(this).combobox('getData');
					    var val = $(this).combobox('getValue');
					    var result = true;
					    for (var i = 0; i < data.length; i++) {
					        if (val == data[i].encode) {
					            result = false;
					        }
					    }
					    if (result) {
					        $(this).combobox("clear");
					    }else{
					        $(this).combobox('unselect',val);
					        $(this).combobox('select',val);
					    }
					},
					filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               }
				});
			  //获取药品频次的下拉框
				$("#drugFrequency").combobox({
				     url:"<%=basePath%>baseinfo/frequency/queryFrequencyAll.action",
				       method:'post',
				       valueField:'encode',
				       textField:'name',
				       panelWidth: 190,
				       onLoadSuccess: function (list) {
						  $(list).each(function(){
							  if ($("#drugFrequency").val() == this.Id) {
							     $("#drugFrequency").combobox("select",this.Id);
							   }
						  });
				       },onHidePanel:function(none){
				    	    var data = $(this).combobox('getData');
				    	    var val = $(this).combobox('getValue');
				    	    var result = true;
				    	    for (var i = 0; i < data.length; i++) {
				    	        if (val == data[i].encode) {
				    	            result = false;
				    	        }
				    	    }
				    	    if (result) {
				    	        $(this).combobox("clear");
				    	    }else{
				    	        $(this).combobox('unselect',val);
				    	        $(this).combobox('select',val);
				    	    }
				    	},
				       filter: function(q, row){
	                        var keys = new Array();
	                        keys[keys.length] = 'encode';
	                        keys[keys.length] = 'name';
	                        keys[keys.length] = 'pinyin';
	                        keys[keys.length] = 'wb';
	                        keys[keys.length] = 'inputCode';
	                        return filterLocalCombobox(q, row, keys);
	               }
				});
				//获取药品生产商的下拉框
				$("#drugManufacturer").combobox({
				     url:"<%=basePath%>drug/manufacturer/findAllManufacturer.action",
				       method:'post',
				       valueField:'id',
				       textField:'manufacturerName',
				       panelWidth: 190,
				       multiple:false,
				       onLoadSuccess: function (list) {	
						  $(list).each(function(){
							  if ($("#drugManufacturer").val() == this.id) {
							     $("#drugManufacturer").combobox("select",this.id);
							   }
						  });
				       },onHidePanel:function(none){
				    	    var data = $(this).combobox('getData');
				    	    var val = $(this).combobox('getValue');
				    	    var result = true;
				    	    for (var i = 0; i < data.length; i++) {
				    	        if (val == data[i].id) {
				    	            result = false;
				    	        }
				    	    }
				    	    if (result) {
				    	        $(this).combobox("clear");
				    	    }else{
				    	        $(this).combobox('unselect',val);
				    	        $(this).combobox('select',val);
				    	    }
				    	},
				       filter: function(q, row){
	                        var keys = new Array();
	                        keys[keys.length] = 'id';
	                        keys[keys.length] = 'manufacturerName';
	                        keys[keys.length] = 'manufacturerPinyin';
	                        keys[keys.length] = 'manufacturerWb';
	                        keys[keys.length] = 'manufacturerInputcode';
	                        return filterLocalCombobox(q, row, keys);
	               }
				       
				});
				bindEnterEvent('drugManufacturer',popWinToManufacturer,'easyui');//绑定回车事件
				//获取药品供应商的下拉框
				$("#drugSupplycompany").combobox({
				       url:"<%=basePath%>drug/supply/findAllSupply.action",
				       method:'post',
				       valueField:'Id',
				       textField:'companyName',
				       panelWidth: 190,
				       onLoadSuccess: function (list) {	    
						  $(list).each(function(){
							  if ($("#drugSupplycompany").val() == this.Id) {
							     $("#drugSupplycompany").combobox("select",this.Id);
							   }
						  });
				       }
				});
				
				//初始化下拉框
				idCombobox("CodeDrugtype","drugType");
				idCombobox("CodeSystemtype","systemType");
				idCombobox("CodeDrugminimumcost","drugMinimumcost");
				idCombobox("CodeDrugproperties","drugProperties");
				idCombobox("CodeDosageform","dosageForm");
				idCombobox("CodeDruggrade","drugGrade");
				idCombobox("CodeDrugpackagingunit","packunit");
				idCombobox("drugDoseunitTmp","doseUnit");
				idCombobox("CodeDrugpricetype","drugPricetype");
				idCombobox("CodeDruginfusion","druginfusion");
				idCombobox("CodeUseage","useage");
				idCombobox("CodeDrugstorage","drugStorage");
				idCombobox("CodeDrugclass","drugclass");
				idCombobox("drugPrimarypharmacology","phyFunction1");//一级药理
				idCombobox("drugTwogradepharmacology","phyFunction2");//二级药理
				idCombobox("drugThreegradepharmacology","phyFunction2");//三级药理
				//给系统类型方法绑定弹窗事件
				bindEnterEvent('CodeSystemtype',popWinToCodeSystemtype,'easyui');
				//给最小费用方法绑定弹窗事件
				bindEnterEvent('CodeDrugminimumcost',popWinToCodeDrugminimumcost,'easyui');
				//给包装单位方法绑定弹窗事件
				bindEnterEvent('CodeDrugpackagingunit',popWinToCodeDrugpackagingunit,'easyui');
				//给最小单位方法绑定弹窗事件
				bindEnterEvent('CodeMinimumunit',popWinToCodeMinimumunit,'easyui');
				//给剂量单位方法绑定弹窗事件
				bindEnterEvent('drugDoseunitTmp',popWinToCodeDoseUnit,'easyui');
				
			});
			//表单提交submit信息
		  	function submit(flg){
			  	$('#editForm').form('submit',{
			  	     url:"<%=basePath%>drug/info/saveOrupdateDrugInfo.action",
			  		 onSubmit:function(){
			  			if(isStopNum==0){
			  				$('#stopReason').textbox('disableValidation');
			  			}
			  		 	if(!$('#editForm').form('validate')){
							$.messager.alert('提示',"验证没有通过,不能提交表单!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							   return false ;
					     }
			  		 	var returnValid=valid();
			  		 	if(returnValid==1){
			  		 		$.messager.alert('提示',"包装单位和最小单位相等时，包装数量为1");
			  		 		setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			  		 		return false ;
			  		 	}
						if(returnValid==2){
							$.messager.alert('提示',"最小单位和剂量单位相等时，基本剂量为1");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			  		 		return false ;
			  		 	}
						var code=$('#code');
						if(code.val().length>50||code.val().length<2){
							$.messager.alert('提示','药品编码长度为2-50位');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
						var drugBasicdose=$('#drugBasicdose').numberbox('getValue');;
						if(drugBasicdose<=0){
							$.messager.alert('提示','药品基本剂量必须大于0');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
						if($("#drugStartdate").val()==null || $("#drugStartdate").val()==""){
							$.messager.progress('close');
							$.messager.alert('提示',"请输入起始日期!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
						if($("#drugEnddate").val()==null || $("#drugEnddate").val()==""){
							$.messager.progress('close');
							$.messager.alert('提示',"请输入终止日期!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
					 },  
					success:function(data){
						if(data=="repeat"){
							$.messager.alert('提示','药品编码重复,请重新输入');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$.messager.progress('close');
							return;
						}
						$.messager.progress('close');
						if(flg==0){
							$.messager.alert('提示',"保存成功");
							$("#list").datagrid("reload");
							$('#tDrug').tree("reload");
				          	closeDialog();
				          	
					   }else if(flg==1){
							//清除editForm
							$('#editForm').form('reset');
					  	}
					 },
					error:function(date){
						$.messager.progress('close');
						$.messager.alert('提示',"保存失败");
					}
			  	});
		  	}
			//验证包装数量和最小单位相等时，包装单位为1；最小单位和剂量单位相等时，最小剂量为1
			function valid(){
				var packNum=$("#packagingnum").textbox('getText');//包装数量
				var packMinNum=$("#CodeMinimumunit").combobox('getText');//最小单位
				var packUnit=$("#CodeDrugpackagingunit").combobox('getText');//包装单位
				var packDosUnit=$("#drugDoseunitTmp").combobox('getText');//剂量单位
				var packBaseDose=$("#drugBasicdose").numberbox('getText');//最小剂量单位
				if(packUnit==packMinNum){
					if(1!=packNum){
						return 1;
					}
				}
				if(packMinNum==packDosUnit){
					if(packDosUnit !=""){
						if(1!=packBaseDose){
							return 2;
						}
					}
				}
			}
			//从xml文件中解析，读到下拉框
			function idCombobox(param0,param1){
				$('#'+param0).combobox({
				    url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type="+param1,
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,onHidePanel:function(none){
			    	    var data = $(this).combobox('getData');
			    	    var val = $(this).combobox('getValue');
			    	    var result = true;
			    	    for (var i = 0; i < data.length; i++) {
			    	        if (val == data[i].encode) {
			    	            result = false;
			    	        }
			    	    }
			    	    if (result) {
			    	        $(this).combobox("clear");
			    	    }else{
			    	        $(this).combobox('unselect',val);
			    	        $(this).combobox('select',val);
			    	    }
			    	},
				    filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               },
				    onLoadSuccess: function() {//请求成功后
						$(list).each(function(){
							  if ($('#'+param0).val() == this.Id) {
								  $('#'+param0).combobox("select",this.Id);
							   }
						});
					}
				});
			}
			var systemtype = $('#CodeSystemtype').combobox('textbox'); 
			systemtype.keyup(function(){
				KeyDown(0,"CodeSystemtype");
			});
			var minimumCost = $('#CodeDrugminimumcost').combobox('textbox'); 
			minimumCost.keyup(function(){
				KeyDown(0,"CodeDrugminimumcost");
			});
			var drugProperties = $('#CodeDrugproperties').combobox('textbox'); 
			drugProperties.keyup(function(){
				KeyDown(0,"CodeDrugproperties");
			});
			var dosagefForm = $('#CodeDosageform').combobox('textbox'); 
			dosagefForm.keyup(function(){
				KeyDown(0,"CodeDosageform");
			});
			var drugGrade = $('#CodeDruggrade').combobox('textbox'); 
			drugGrade.keyup(function(){
				KeyDown(0,"CodeDruggrade");
			});
			var packagingunit = $('#CodeDrugpackagingunit').combobox('textbox'); 
			packagingunit.keyup(function(){
				KeyDown(0,"CodeDrugpackagingunit");
			});
			var minimumUnit = $('#CodeMinimumunit').combobox('textbox'); 
			minimumUnit.keyup(function(){
				KeyDown(0,"CodeMinimumunit");
			});
			var priceType = $('#CodeDrugpricetype').combobox('textbox'); 
			priceType.keyup(function(){
				KeyDown(0,"CodeDrugpricetype");
			});
			var useMode = $('#CodeUseage').combobox('textbox'); 
			useMode.keyup(function(){
				KeyDown(0,"CodeUseage");
			});
			function KeyDown(flg,tag){ 	    	
		    	if(flg==1){//回车键光标移动到下一个输入框
			    	if(event.keyCode==13){	
			    		event.keyCode=9;
			    	}
			    } 
			    if(flg==0){	//空格键打开弹出窗口
				    if (event.keyCode == 32)  
				    { 
				        event.returnValue=false;  
				        event.cancel = true; 
				        if(tag=="CodeDrugtype"){
				        	showWin("请写药品类别","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugType,0","50%","80%");
				        }
				        if(tag=="CodeSystemtype"){
				        	showWin("请写系统类别","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=systemType,0","50%","80%");
				        }
				        if(tag=="CodeDrugminimumcost"){
				        	showWin("请写最小费用","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugMinimumcost,0","50%","80%");
				        }
				        if(tag=="CodeDrugproperties"){
				        	showWin("请写药品性质","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugProperties,0","50%","80%");
				        }
				        if(tag=="CodeDosageform"){
				        	showWin("请写剂型","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=dosageForm,0","50%","80%");
				        }
				        if(tag=="CodeDruggrade"){
				        	showWin("请写药品等级","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugGrade,0","50%","80%");
				        }
				        if(tag=="CodeDrugpackagingunit"){
				        	showWin("请写包装单位","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=packunit,0","50%","80%");
				        }
				        if(tag=="CodeMinimumunit"){
				        	showWin("请写最小单位","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=minunit,0","50%","80%");
				        }
				        if(tag=="CodeDoseunit"){
				        	showWin("请写剂量单位","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=doseUnit,0","50%","80%");
				        }
				        if(tag=="CodeDrugpricetype"){
				        	showWin("请写价格形式","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugPricetype,0","50%","80%");
				        }
				        if(tag=="CodeUseage"){
				        	showWin("请写使用方法","<c:url value='/ComboxOut.action'/>?xml=CodeUseage,0","50%","80%");
				        }
				        if(tag=="CodeDrugstorage"){
				        	showWin("请写存储条件","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=drugStorage,0","50%","80%");
				        }
				        if(tag=="drugPrimarypharmacology"){
				        	showWin("请写一级药理","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=phyFunction1,0","50%","80%");
				        }
				        if(tag=="drugTwogradepharmacology"){
				        	showWin("请写二级药理","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=phyFunction2,0","50%","80%");
				        }
				        if(tag=="drugThreegradepharmacology"){
				        	showWin("请写三级药理","<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=phyFunction2,0","50%","80%");
				        }
				    }
			    }
			} 
			
			//清除所填信息
			function clear(flag) {
				$('#editForm').form('clear');
			}
			//复选框
			function checkBoxSelect(id,defalVal,selVal){
				var hiddenId=id+"h";
				if($('#'+id).is(':checked')){
					$('#'+hiddenId).val(selVal);
				}else{
					$('#'+hiddenId).val(defalVal);
				}
			}
			//复选框是合作医疗
			function checkboxlimit(){
				if($('#drugIscooperativemedical').is(':checked')){
					$('#drugIscooperativemedicalh').val(1);
					$('#drugIscitylimit').val(0);
					$('#drugIscitylimitch').prop('checked',false); 
					$('#selfFlg').combobox('setValue','');
				}else{
					$('#drugIscooperativemedicalh').val(0);
				}
			}
			//是否市
			function checkboxiscity(){
				if($('#drugIscitylimitch').is(':checked')){
				   $('#drugIscitylimit').val(1);
				   $('#drugIscooperativemedicalh').val(0);
				   $('#drugIscooperativemedical').prop('checked',false); 
				   $('#selfFlg').combobox('setValue','');
				}else{
					$('#drugIscitylimit').val(0);
				}
				}
				$("#selfFlg").combobox({
					onSelect:function(record){
			    	if($('#selfFlg').combobox('getValue')!=null){
	   				$('#drugIscitylimitch').prop('checked',false); 
	   				$('#drugIscooperativemedical').prop('checked',false);
	   				$('#drugIscooperativemedicalh').val(0);
	   				$('#drugIscitylimit').val(0);
				 }
				  } 
			});
			
				
				/**
				 * 打开系统类别界面弹框
				 * @author  zhuxiaolu
				 * @date 2015-5-25 10:53
				 * @version 1.0
				 */
				
				function popWinToCodeSystemtype(){
					
					var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?classNameTmp=CodeSystemtype&textId=CodeSystemtype&type=systemType";
					var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
							
				}
				/**
				 * 打开最小费用界面弹框
				 * @author  zhuxiaolu
				 * @date 2015-5-25 10:53
				 * @version 1.0
				 */
				
				function popWinToCodeDrugminimumcost(){
					
					var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?classNameTmp=CodeDrugminimumcost&textId=CodeDrugminimumcost&type=drugMinimumcost";
					var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
							
				}
				
				
				/**
				 * 打开包装单位界面弹框
				 * @author  zhuxiaolu
				 * @date 2015-5-25 10:53
				 * @version 1.0
				 */
				
				function popWinToCodeDrugpackagingunit(){
					
					var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?classNameTmp=CodeDrugpackagingunit&textId=CodeDrugpackagingunit&type=packunit";
					var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
							
				}
				/**
				 * 打开最小单位界面弹框
				 * @author  zhuxiaolu
				 * @date 2015-5-25 10:53
				 * @version 1.0
				 */
				
				function popWinToCodeMinimumunit(){
					
					var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=CodeMinimumunit&type=minunit";
					var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
							
				}
				/**
				 * 打开剂量单位界面弹框(显示的为code编码)
				 * @author  zhuxiaolu
				 * @date 2015-5-25 10:53
				 * @version 1.0
				 */
				
				function popWinToCodeDoseUnit(){
					
					var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=drugDoseunitTmp&type=drugPackagingunit";
					var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
							
				}
				 

		          /**
			   * 回车弹出生产厂家弹框
			   * @author  zhuxiaolu
			   * @param textId 页面上commbox的的id
			   * @date 2016-03-22 14:30   
			   * @version 1.0
			   */
			   function popWinToManufacturer(){
					var tempWinPath = "<%=basePath%>popWin/popWinDrugManufacturer/toDrugManufacturerPopWinDB.action?textId=drugManufacturer";
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
				}
					
			   function onSelect(d) {
			        var issd = this.id == 'drugStartdate', drugStartdate = issd ? d : new Date($('#drugStartdate').datebox('getValue')), drugEnddate = issd ? new Date($('#drugEnddate').datebox('getValue')) : d;
			            if (drugStartdate > drugEnddate) {
			            	$.messager.alert('提示',"结束日期小于开始日期"); 
			            	setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			                //只要选择了日期，不管是开始或者结束都对比一下，如果结束小于开始，则清空结束日期的值并弹出日历选择框
			                $('#drugEnddate').datebox('setValue', '').datebox('showPanel');
			            }
			        }
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>
