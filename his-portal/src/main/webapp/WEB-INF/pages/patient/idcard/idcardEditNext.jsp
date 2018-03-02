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
	<%@ include file="/common/metas.jsp"%>
	<title>就诊卡管理编辑界面</title>
</head>
<body>
<div id="panelEast" class="easyui-panel"
		data-options="title:'就诊卡编辑',iconCls:'icon-form',border:false">
		<div style="padding: 5px" data-options="border:false">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="idcard.id" value="${idcard.id }">
				<input type="hidden" id="pid" name="patient.id"	value="${patient.id }">
				<div style="padding: 0px 5px 5px 5px;">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;">
						<tr>
							<td class="honry-lable">
								患者姓名：
							</td>
							<td>
								<input  class="easyui-textbox" id="patientName"
									value="${patient.patientName }" name="patient.patientName"
									data-options="required:true,missingMessage:'请填写患者姓名!'"></input>
							</td>
							<td class="honry-lable">
								性别：
							</td>
							<td>
								<input id="patientSex" name="patient.patientSex" 
									value="${patient.patientSex }"
									data-options="required:true,missingMessage:'请选择性别!'"
									onkeydown="KeyDown(0,'CodeSex')" />
							</td>
						<tr>
							<td class="honry-lable">
								出生日期：
							</td>
							<td>
								<input id="patientBirthday" value="${patient.patientBirthdayView }" required="required"
									name="patient.patientBirthday" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								联系方式：
							</td>
							<td>
								<input id="cost" class="easyui-textbox"
									value="${patient.patientPhone }" name="patient.patientPhone"
									data-options="required:true,missingMessage:'请填电话!'"></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								证件类型：
							</td>
							<td>
								<input id="CodeCertificate"
									name="patient.patientCertificatestype"
									value="${patient.patientCertificatestype }"
									data-options="required:true,missingMessage:'请选择证件类型!'"
									onkeydown="KeyDown(0,'CodeCertificate')" />
							</td>
							<td class="honry-lable">
								证件号码：
							</td>
							<td>
								<input class="easyui-textbox" id="certificatesno"
									value="${patient.patientCertificatesno }"
									name="patient.patientCertificatesno"
									data-options="required:true,missingMessage:'请填写证件号码!'"></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								家庭地址：
							</td>
							<td  colspan="3">
								<div style="text-align: 25px; height: 25px;">
									<%--2017年2月18日 GH  岳工要求地址下拉框与上边对齐 --%>
									<input id="homeone"  style="width: 126px"  value="${oneCode }" data-options="prompt:'省/直辖市'"/>
									<input id="hometwo" style="width: 125px"  value="${twoCode }" data-options="prompt:'市'"/>
									<input id="homethree" style="width: 125px"  value="${threeCode }" data-options="prompt:'县'"/>
									<input id="homefour" style="width: 125px"  value="${fourCode }" data-options="prompt:'区'"/>
									<input type="hidden" id="patientCity" name="patient.patientCity" value="${patient.patientCity }">
								</div>
								<div style="margin-top:5px;text-align: 30px; height: 30px;">
									<input class="easyui-textbox" style="width: 522px; "
										value="${patient.patientAddress }"
										name="patient.patientAddress"
										data-options="missingMessage:'请填写详细地址!',prompt:'社区/乡镇村详细地址'"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								合同单位：
							</td>
							<td>
								<input id="businessContractunit" name="patient.unit" value="${patient.unit }" data-options=""/>
							</td>
							<td class="honry-lable">
								结算类别：
							</td>
							<td>
								<input id="patientPaykind" name="patient.patientPaykind" value="${patient.patientPaykind }" data-options=""/>
							</td>
						</tr>
					<%--由于不知道之前业务具体是什么,这里虽然要去掉这两项,但是没有删除20160223zpty
							<tr>
								<td class="honry-lable">
									卡号：
								</td>
								<td>
									<input class="easyui-textbox" id="idcardNo"
										name="idcard.idcardNo" value="${idcard.idcardNo }" 
										data-options="required:true" missingMessage="请输入卡号" onblur="validCheckIC()"/>
								</td>
								<td class="honry-lable">
									建卡时间：
								</td>
								<td>
									<input class="easyui-datebox" id="idcardCreatetime"
										name="idcard.idcardCreatetime"
										value="${idcard.idcardCreatetime }"
										data-options="required:true" missingMessage="请输入建卡时间" />
								</td>
							</tr>
						--%>
						<tr>
							<td class="honry-lable">
								卡类型：
							</td>
							<td>
								<input id="CodeIdcardType" name="idcard.idcardType" readonly="readonly"
									value="${idcard.idcardType }" data-options="required:true"
									 missingMessage="请选择卡类型"
									onkeydown="KeyDown(0,'CodeIdcardType')" />
							</td>
							<td class="honry-lable">
								卡号：
							</td>
							<td>
								<input class="easyui-textbox" id="idcardNo" readonly="readonly"
									name="idcard.idcardNo" value="${idcard.idcardNo }" 
									data-options="required:true" missingMessage="请输入卡号" onblur="validCheckIC()"/>
							</td>
				<%--由于不知道之前业务具体是什么,这里虽然要去掉这两项,但是没有删除20160223zpty
							<td class="honry-lable">
								操作人员：
							</td>
							<td>
								<input class="easyui-textbox" id="idcardOperator" 
									name="idcard.idcardOperator" value="${idcard.idcardOperator }"
									data-options="required:true" missingMessage="请输入操作人员" />
							</td>--%>
						</tr>
						<tr>
							<td class="honry-lable">
								备注：
							</td>
							<td colspan="3">
								<textarea class="easyui-validatebox" rows="4" cols="50" id="idcardRemark" style="width: 522px; "
									name="idcard.idcardRemark" data-options="multiline:true">${idcard.idcardRemark }</textarea>
							</td>
						</tr>
					</table>
				</div>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit(0);void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
<!--					<a href="javascript:clear();void(0)" class="easyui-linkbutton"-->
<!--						data-options="iconCls:'icon-clear'">清除</a>-->
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script>
	var check = "";
	/**
	 * 表单提交
	 * @author  lt
	 * @date 2015-6-1
	 * @version 1.0
	 */
	 function submit(flg) {
		 $.messager.progress({text:'保存中，请稍后...',modal:true});	
		 if (!$('#editForm').form('validate')) {
			    $.messager.progress('close');	
				$.messager.alert('提示',"验证没有通过,不能提交表单!");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}else{
				submits(flg);
			}
	 }
	function submits(flg) {
		$('#editForm').form('submit', {
			url : "<c:url value='/patient/idcard/editIdcard.action'/>",
			onSubmit : function() {
				if(!isTelphoneNum($("#cost").val())&&!isMobilephoneNum($("#cost").val())){
					$.messager.progress('close');
					$.messager.alert('提示',"电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($('#CodeCertificate').combobox('getText').indexOf("身份证")>=0 && !isIdCardNo($("#certificatesno").val())){
					$.messager.progress('close');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($("#email").val()!="" && $("#email").val()!=null && !checkEmail($("#email").val())){
					$.messager.progress('close');
					$.messager.alert('提示',"电子邮箱格式不正确,格式如123@163.com!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($("#idcardNo").val().length<4){
					$.messager.progress('close');
					$.messager.alert('提示',"就诊卡号长度不可以小于四位!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($("#idcardNo").val().length>50){
					$.messager.progress('close');
					$.messager.alert('提示',"就诊卡号长度不可以大于五十位!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($("#patientBirthday").val()==null || $("#patientBirthday").val()==""){
					$.messager.progress('close');
					$.messager.alert('提示',"请输入出生日期!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=="checkHandbookNo"){
					$.messager.alert('提示',"医保号已在用,不能添加就诊卡!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}else if(data=="nameNO"){
					$.messager.alert('提示',"此患者已存在,不能添加就诊卡!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}else if(data=="idcardNO"){
					$.messager.alert('提示',"就诊卡已在使用,请重新录入!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				if (flg == 0) {
					$.messager.alert('提示','保存成功');
					$('#divLayout').layout('remove', 'east');
					clearValue(1);
					isSearchFrom();
					$('#editlinkbutton').linkbutton('disable');
					$('#dellinkbutton').linkbutton('disable');
					$('#backlinkbutton').linkbutton('disable');
					$('#losslinkbutton').linkbutton('disable');
					$('#activatelinkbutton').linkbutton('disable');
					$('#filllinkbutton').linkbutton('disable');
					//实现刷新栏目中的数据
				} else if (flg == 1) {
					//清除editForm
					$('#editForm').form('reset');
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示',"保存失败！");
			}
		});
	}

	/**
	 * 清除页面填写信息
	 * @author  lt
	 * @date 2015-6-19 10:53
	 * @version 1.0
	 */
	function clear() {
		$('#editForm').form('reset');
	}
	/**
	 * 关闭编辑窗口
	 * @author  lt
	 * @date 2015-6-19 10:53
	 * @version 1.0
	 */
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}

	$(function() {
		//初始化下拉框
		$('#CodeCertificate').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=certificate",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#CodeIdcardType').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=idcardType",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#patientSex').combobox({
                url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
                valueField:'encode',    
                textField:'name',
                editable:false
            });
            <%--由于不知道之前业务具体是什么,这里虽然要去掉这两项,但是没有删除20160223zpty	
			$('#idcardCreatetime').datebox().datebox('calendar').calendar({
					validator: function(date){
						var now = new Date();
						var startTime = new Date(now.getFullYear(), now.getMonth(), now.getDate());
						return startTime>=date;
					}
				});
				--%>
				queryDistrictSJLDOne();
				queryDistrictSJLDTwo('');
				queryDistrictSJLDThree('');
				queryDistrictSJLDFour('',false);
				
				//如果是修改页面,进入的时候,让三级联动内每个输入框都可以下拉出值
				var oneCode=$('#homeone').combobox('getValue');//得到被选中的省份代码
				queryDistrictSJLDTwo(oneCode);//发送给后台，并选中指定的市
				var twoCode=$('#hometwo').combobox('getValue');//得到被选中的市的代码
				queryDistrictSJLDThree(twoCode);//传递到后台，此时身份证前六位传递完毕
				var threeCode=$('#homethree').combobox('getValue');
				var fourCode=$('#homefour').combobox('getValue');
				if(fourCode==null||fourCode==""){
					$('#homefour').combobox({
						disabled:true
					});
				}else{
					queryDistrictSJLDFour(threeCode,true);
				}
				
				//加载合同单位
				$('#businessContractunit').combobox({   
					valueField:'encode',
					textField:'name',
					url: "<%=basePath%>patient/patinent/queryUnitCombobox.action",    
					onSelect:function(node) {
				    	$('#businessContractunit').val(node.id);
				    }
				});
			bindEnterEvent('businessContractunit',popWinToUnit,'easyui');//绑定回车事件
			//加载结算类别
			$('#patientPaykind').combobox({   
				valueField:'encode',
				textField:'name',
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=paykind",    
				onSelect:function(node) {
			    	$('#patientPaykind').val(node.id);
			    }
			});
		});

				//省市区三级联动
	function queryDistrictSJLDFour(id,bool) {
			if(id===''){
				$('#homefour').combobox({
					data:[]
				})
				bindEnterEvent('homefour',popWinToDistrictFours,'easyui');
				return;
			}
			$('#homefour').combobox({  
				url: "<%=basePath%>baseinfo/district/queryDistrictTreeFour.action?parId="+id,    
				valueField:'cityCode',    
				    textField:'cityName',
				    required:bool,
				    multiple:false,
				    editable:true,
				    onSelect:function(node) {
				    	$('#patientCity').val(node.cityCode);
				    }
			});
			bindEnterEvent('homefour',popWinToDistrictFours,'easyui');
	}
	function queryDistrictSJLDThree(id) {
			if(id===''){
				$('#homethree').combobox({
					data:[]
				})
				bindEnterEvent('homethree',popWinToDistrictThrees,'easyui');
				return;
			}
			$('#homethree').combobox({  
				url: "<%=basePath%>baseinfo/district/queryDistrictTreeThree.action?parId="+id,    
					valueField:'cityCode',    
				    textField:'cityName',
				    required:true,
				    multiple:false,
				    editable:true,
				     onSelect:function(node) {
				     	var bool = false;
				     	if(node.cityName=="市辖区"){
				     		bool=true;
				     		$('#homefour').combobox({
								value:'',
								disabled:false
							});
				     	}else{
				     		$('#patientCity').val(node.cityCode);
				     		$('#homefour').combobox({
								value:'',
								disabled:true
							});
				     	}				     	
			        	queryDistrictSJLDFour(node.cityCode,bool);
			        }
			});
			bindEnterEvent('homethree',popWinToDistrictThrees,'easyui');
	}
	function queryDistrictSJLDTwo(id) {
			if(id===''){
				$('#hometwo').combobox({
					data:[]
			})
			bindEnterEvent('hometwo',popWinToDistrictTwos,'easyui');//绑定回车事件
			return;
		}
			$('#hometwo').combobox({ 
				/*cn.honry.baseinfo.district.action.DistrictAction  */
				url: "<%=basePath%>baseinfo/district/queryDistrictTreeTwo.action?parId="+id,    
					valueField:'cityCode',    
				    textField:'cityName',
				    required:true,
				    multiple:false,
				    editable:true,
				    onSelect:function(node) {
			        	queryDistrictSJLDThree(node.cityCode);
			        	$('#homethree').combobox('setValue','');
			        	$('#homefour').combobox('setValue','');
			        }
			});
			bindEnterEvent('hometwo',popWinToDistrictTwos,'easyui');//绑定回车事件
	}
		function queryDistrictSJLDOne() {
			$('#homeone').combobox({  
				url: "<%=basePath%>baseinfo/district/queryDistrictTreeOne.action?parId=",
				valueField:'cityCode',    
			    textField:'cityName',
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
					queryDistrictSJLDTwo(node.cityCode);
					$('#hometwo').combobox('setValue','');
					$('#homethree').combobox('setValue','');
					$('#homefour').combobox('setValue','');
		        }
			});
			bindEnterEvent('homeone',popWinToDistrict,'easyui');//绑定回车事件
		}

	/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-06-29
	 * @version 1.0
	 */
	var win;
	function showWin(title, url, width, height) {
		var content = '<iframe id="myiframe" src="'
				+ url
				+ '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		var divContent = '<div id="treeDeparWin">';
		win = $('<div id="treeDeparWin"><div/>').dialog({
			content : content,
			width : width,
			height : height,
			modal : true,
			minimizable : false,
			maximizable : true,
			resizable : true,
			shadow : true,
			center : true,
			title : title
		});
		win.dialog('open');
	}
	
	
	/**
	* 回车弹出合同单位选择窗口
	* @author  zpty
	* @param textId 页面上commbox的的id
	* @date 2016-04-27  
	* @version 1.0
	*/
	function popWinToUnit(){
		var tempWinPath = "<%=basePath%>popWin/popWinUnit/toUnitPopWin.action?textId=businessContractunit";
		window.open (tempWinPath,'newwindowUnit',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

	+',scrollbars,resizable=yes,toolbar=yes')
	}
	
	 /**
	* 回车弹出地址一级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrict(){
		$('#hometwo').textbox('setValue','');
		$('#homethree').textbox('setValue','');
		$('#homefour').textbox('setValue','');
		var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homeone&level=1&parentId=1";
		window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

	+',scrollbars,resizable=yes,toolbar=yes')
	}
	/**
	* 回车弹出地址二级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictTwos(){
		$('#homethree').textbox('setValue','');
		$('#homefour').textbox('setValue','');
		var parentId=$('#homeone').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择省/直辖市');  
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=hometwo&level=2&parentId="+parentId;
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
	/**
	* 回车弹出地址三级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictThrees(){
		$('#homefour').textbox('setValue','');
		var parentId=$('#hometwo').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择市');  
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homethree&level=3&parentId="+parentId;
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
	/**
	* 回车弹出地址四级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictFours(){
		var parentId=$('#homethree').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择县');  
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homefour&level=4&parentId="+parentId;
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
</script>	
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>