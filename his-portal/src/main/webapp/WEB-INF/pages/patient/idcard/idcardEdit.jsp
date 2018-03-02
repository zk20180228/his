<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
<div id="panelEast" class="easyui-panel"
		data-options="iconCls:'icon-form',border:false" fit='true'>
		<div style="padding: 5px;overflow-y:auto;" id="panelEasttable" data-options="border:false" >
			<form id="editForm" method="post"  >
					<table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
						border="0" style="margin-left:auto;margin-right:auto;margin-down:auto; overflow: auto;" >
						<tr>
							<td class="honry-lable">
								患者姓名：
							</td>
							<td>
								<input id="patientName" class="easyui-textbox" value="${patient.patientName }" name="patient.patientName"
									data-options="required:true,missingMessage:'请填写患者姓名!'"></input>
							</td>
							<td class="honry-lable">
								自定义码：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.patientInputcode }"
									name="patient.patientInputcode" data-options=""></input>
								<!-- required:true,missingMessage:'请填写自定义码!' -->
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								性别：
							</td>
							<td>
								<input id="patientSex" name="patient.patientSex" 
									value="${patient.patientSex }"
									data-options="required:true,missingMessage:'请选择性别!'"
									onkeydown="KeyDown(0,'CodeSex')"/>
							</td>
							<td class="honry-lable">
								出生日期：
							</td>
							<td>
									<input id="patientBirthday" value="${patient.patientBirthday }" required="required"
									name="patient.patientBirthday" class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								家庭地址：
							</td>
							<td  colspan="3">
							<div style="text-align: 25px; height: 25px;" class="familyAddressTop">
									<%--2017年2月18日 GH  岳工要求地址下拉框与上边对齐 --%>
									<input id="homeone"  style="width: 145px"  value="${oneCode }" data-options="prompt:'省/直辖市'"/>
									<input id="hometwo" style="width: 130px"  value="${twoCode }" data-options="prompt:'市'"/>
									<input id="homethree" style="width: 130px"  value="${threeCode }" data-options="prompt:'县'"/>
									<input id="homefour" style="width: 130px"  value="${fourCode }" data-options="prompt:'区'"/>
									<input type="hidden" id="patientCity" name="patient.patientCity" value="${patient.patientCity }">
								</div>
								<div style="margin-top:3px;text-align: 22px; height: 22px;">
									<input class="easyui-textbox" style="width: 514px; "
										value="${patient.patientAddress }"
										name="patient.patientAddress"
										data-options="missingMessage:'请填写详细地址!',prompt:'社区/乡镇村详细地址'"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								门牌号：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.patientDoorno }" name="patient.patientDoorno"
									data-options=""></input>
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
									data-options="required:true,missingMessage:'请选择证件类型!'"/>
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
								出生地：
							</td>
							<td>
								<input class="easyui-textbox"
									name="patient.patientBirthplace"
									value="${patient.patientBirthplace }" data-options="" />
								<!-- required:true,missingMessage:'请选择出生地!' -->
							</td>
							<td class="honry-lable">
								籍贯：
							</td>
							<td>
								<input class="easyui-textbox" 
									name="patient.patientNativeplace"
									value="${patient.patientNativeplace }" data-options="" />
								<!-- required:true,missingMessage:'请选择籍贯!' -->
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								国籍：
							</td>
							<td>
								<input id="CodeCountry" name="patient.patientNationality"
									value="${patient.patientNationality }" 
								/>
								<a href="javascript:delSelectedData('CodeCountry');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								<!-- required:true,missingMessage:'请选择国籍!' -->
							</td>
							<td class="honry-lable">
								民族：
							</td>
							<td>
								<input id="CodeNationality" name="patient.patientNation"
									value="${patient.patientNation }"  />
								<!-- required:true,missingMessage:'请选择民族!' -->
								<a href="javascript:delSelectedData('CodeNationality');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								工作单位：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.patientWorkunit }"
									name="patient.patientWorkunit" data-options=""></input>
								<!-- required:true,missingMessage:'请填写工作单位!' -->
							</td>
							<td class="honry-lable">
								单位电话：
							</td>
							<td>
								<input class="easyui-textbox" id="workphone"
									value="${patient.patientWorkphone }"
									name="patient.patientWorkphone" data-options=""></input>
								<!-- required:true,missingMessage:'请填写单位电话!' -->
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								婚姻状况：
							</td>
							<td>
								<input id="CodeMarry" name="patient.patientWarriage"
									value="${patient.patientWarriage }" data-options=""/>
								<!-- required:true,missingMessage:'请选择婚姻状态!' -->
							</td>
							<td class="honry-lable">
								职业：
							</td>
							<td>
								<input id="CodeOccupation" name="patient.patientOccupation"
									value="${patient.patientOccupation }"/>
								<a href="javascript:delSelectedData('CodeOccupation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								<!--required:true,missingMessage:'请选择职业!'  -->
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								医保号：
							</td>
							<td>
								<input id="handBook" class="easyui-textbox"
									value="${patient.patientHandbook }"
									name="patient.patientHandbook"
									data-options=""></input>
							</td>
							<td class="honry-lable">
								合同单位：
							</td>
							<td>
								<input id="businessContractunit" name="patient.unit" value="${patient.unit }" data-options=""/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								电子邮箱：
							</td>
							<td>
								<input id="email" class="easyui-textbox"
									value="${patient.patientEmail }" name="patient.patientEmail"
									data-options=""></input>
							</td>
							<td class="honry-lable">
								母亲姓名：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.patientMother }" name="patient.patientMother"
									data-options=""></input>
								<!-- required:true,missingMessage:'请填写母亲姓名!' -->
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								联系人：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.patientLinkman }"
									name="patient.patientLinkman"
									data-options=""></input>
							</td>
							<td class="honry-lable">
								联系人关系：
							</td>
							<td>
								<input id="CodeRelation" name="patient.patientLinkrelation"
									value="${patient.patientLinkrelation }"
									/>
								<a href="javascript:delSelectedData('CodeRelation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								联系人地址：
							</td>
							<td colspan="3">
								<input class="easyui-textbox"
									value="${patient.patientLinkaddress }"
									name="patient.patientLinkaddress"  style="width: 514px; "
									data-options=""></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								联系人门牌号：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.patientLinkdoorno }"
									name="patient.patientLinkdoorno" data-options=""></input>
								<!-- required:true,missingMessage:'请填写联系人门牌号!' -->
							</td>
							<td class="honry-lable">
								联系人电话：
							</td>
							<td>
								<input class="easyui-textbox" id="linkphone"
									value="${patient.patientLinkphone }"
									name="patient.patientLinkphone" 
									data-options=""></input>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病案号：
							</td>
							<td>
								<input class="easyui-textbox"
									value="${patient.caseNo }"
									name="patient.caseNo" 
									data-options=""></input>
							</td>
							<td class="honry-lable">
								结算类别：
							</td>
							<td>
								<input id="patientPaykind" name="patient.patientPaykind" value="${patient.patientPaykind }" data-options=""/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">卡类型：</td>
							<td>
								<input id="CodeIdcardType" name="idcard.idcardType"
									value="${idcard.idcardType }" data-options="required:true"
									missingMessage="请选择卡类型"/>
							</td>
							<td class="honry-lable">卡号：</td>
							<td>
								<input id="idcardNo" class="easyui-textbox" name="idcard.idcardNo" value="${idcard.idcardNo }"
									data-options="required:true" missingMessage="请输入卡号" style="width:47%"/>
								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
								<input type="hidden" id="idcardEdits" >
							</td>
						</tr>
						<tr>
							<td class="honry-lable">备注：</td>
							<td colspan="3">
								<textarea class="easyui-validatebox" rows="3" cols="50" id="idcardRemark"  style="width: 514px; "
									name="idcard.idcardRemark" data-options="multiline:true">${idcard.idcardRemark }</textarea>
							</td>
						</tr>
					</table>
				
			</form>
		
		<div style="text-align: center;margin:10px 0;">
				<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
				<c:if test="${empty idcard.id}">
					<a href="javascript:submit(1);" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:submit(0);void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		</div>
	</div>
		</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	
	<script>
	var check = "";
	var codeCertificateList="";	
	$(function(){
//		var winH=$("body").height();
//		$('#panelEasttable').height(winH-78-30);
	
		
	});
	/*******************************开始读卡***********************************************/	
	//2017-09-09读卡
	function read_card_ic(){
		var card_value=app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value=='')
		{
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		
		//填写就诊卡号
		$("#idcardNo").textbox("setValue",card_value);
		//就诊卡类型
		$("#CodeIdcardType").combobox("select","0");
	}
	
	/*******************************结束读卡***********************************************/
 	/*******************************开始读身份证***********************************************/
   ////2017-09-09读身份证
	function read_card_sfz(){
		
			var card_value=app.read_sfz().trim();//朱x             ,男,汉,1963年06月15日,重庆市沙坪坝区建工东村83号1-3                  ,510211196306151xxx,重庆市公安局沙坪坝分局    ,2008年03月05日,2028年03月05日,D:\cef\dcef3-2623\bin\Win32\510211196306151256.Bmp
			var cards = '';
			var patientCity='';//身份证前六位
			
			if(card_value=='0'||card_value==undefined||card_value=='')
			{
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}else{
				cards = card_value.split(',');
				patientCity = cards[5].trim().substring(0,6);
			}
			if(cards==undefined||cards==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			
			//patientCity="410105";测试金水区
			//2.ajax请求，把身份证前六位发送到后台(同步)
			  $.ajax({
				url: "<%=basePath%>patient/idcard/readInfoIdcard.action",
				data:"patientCity="+patientCity,
				type:'post',
				/* async:false, */
				success: function(msgData) {
					//alert("执行成功！");
					//设置其余的信息
					//姓名
					$("#patientName").textbox("setValue",cards[0].trim());
					//性别
					if(cards[1].trim()=="男"){
	  					$('#patientSex').combobox('select','1');
	  					//alert("男");
	 				}else if(cards[1].trim()=="女"){
						$('#patientSex').combobox('select','2');
	 				}else{
	 					$('#patientSex').combobox('select','3');
	 				} 
//	 				//年龄
					var bt1=cards[3].trim().replace(/(\D+)/g,"-");
					var birthday=bt1.substring(0,bt1.length-1);
					$('#patientBirthday').val(birthday);
					//alert(birthday);
//	 				//证件类型
	  				$("#CodeCertificate").combobox("select","3");
//	 				//证件号码
	 				$("#certificatesno").textbox("setValue",cards[5].trim());
	 				//alert("请求成功");
					//3.把身份证前六位代码设置为后台病人的所在城市，处理完毕返回省市区代码
					//4.请求成功得到oneCode,twoCode,threeCode,fourCode,进行设置，回显
					var cty_nodes=msgData.split(",");
					//省市区
					queryDistrictSJLDOne();
					$('#homeone').combobox('select',cty_nodes[0]);
					//alert("第一个的节点为"+cty_nodes[0]);
					queryDistrictSJLDTwo(cty_nodes[0]);
					$('#hometwo').combobox('setValue',cty_nodes[1]);
					queryDistrictSJLDThree(cty_nodes[1]);
					//alert("第二个的节点为"+cty_nodes[1]);
					$('#homethree').combobox('setValue',cty_nodes[2]);
					var flag=cty_nodes[3]==""?true:false;
					queryDistrictSJLDFour(cty_nodes[2],flag);
					//alert("第三个的节点为"+cty_nodes[2]);
					$('#homefour').combobox('setValue',cty_nodes[3]);
					//alert("第四个的节点为"+cty_nodes[3]);
					//5.请求成功，发送ajax请求把身份证上民族"字符"发送到后台得到民资代码，进行回显
					//如果是汉就不用读表了
					if(cards[2].trim()=="汉"){
						$('#CodeNationality').combobox('setValue',"01");
					}else{
						//否则读表，查民族的代码
						$.ajax({
							url: "<%=basePath%>baseinfo/pubCodeMaintain/queryPubCode.action",
							data:"dictionary.name="+cards[2].trim(),
							type:'post',
							async:false,
							success:function(msgData){
								//alert(msgData)
								//得到民族对应的object数组
								var nations=msgData.rows;
								//alert(nations[0]);
								//alert(nations[0].encode);
								//民族
								//var minority=inentitys[2];
								$('#CodeNationality').combobox('select',nations[0].encode);
								}
						});
					}
					//6.国籍,如果市不为"",说明是中国人
					if($("#hometwo").combobox("getValue")!=""){
						$('#CodeCountry').combobox('setValue',"1");
					}
				}
			  
		});
	}
	
		
	/*******************************结束读身份证***********************************************/
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
				$.messager.alert('提示',"请输入完整信息!");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}else{
				//2017-04-14加下面的代码为了解决读身份证提交表单时，省市区县数据是""的问题
				var rs =$('#homefour').combobox("getValue")==""?$('#homethree').combobox("getValue"):$('#homefour').combobox("getValue");
				$("#patientCity").val(rs);
				//alert(rs);
				submits(flg);
			}
	 }
	function submits(flg) {
		/** 证件类型列表页 显示
		 * @author  zpty
		 * @date 2015-12-23
		 * @version 1.0
		 */
		function codeCertificateFamater(value){
				for(var i=0;i<codeCertificateList.length;i++){
					if(value==codeCertificateList[i].encode){
						return codeCertificateList[i].name;
					}
				}
			}
		//证件类型 certificate
	    $.ajax({
				
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",
				//data:{"str" : "CodeCertificate"},
				type:'post',
				async:false,
				success: function(codeCertificatedata) {
					codeCertificateList =  codeCertificatedata ;
				}
		});
		$('#editForm').form('submit', {
			url : "<%=basePath%>patient/idcard/editIdcard.action",
			onSubmit : function() {
				if(!isTelphoneNum($("#cost").val())&&!isMobilephoneNum($("#cost").val())){
					$.messager.progress('close');
					$.messager.alert('提示',"电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($("#workphone").val()!="" && $("#workphone").val()!=null && !isTelphoneNum($("#workphone").val())&&!isMobilephoneNum($("#workphone").val())){
					$.messager.progress('close');
					$.messager.alert('提示',"单位电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if($("#linkphone").val()!="" && $("#linkphone").val()!=null && !isTelphoneNum($("#linkphone").val())&&!isMobilephoneNum($("#linkphone").val())){
					$.messager.progress('close');
					$.messager.alert('提示',"联系人电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
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
				if(codeCertificateFamater($("#CodeCertificate").combobox('getValue'))=="身份证" && !isIdCardNo($("#certificatesno").val())){
					$.messager.progress('close');
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
					//$('#divLayout').layout('remove', 'east');
					parent.$("#list").datagrid("reload");
					//关闭当前页面
					//selef.close();
					//实现刷新栏目中的数据
					closeLayout();
				} else if (flg == 1) {
					//清除editForm
					$('#editForm').form('reset');
// 					$('#CodeCertificate').combobox('reload');//身份证
					$('#CodeCertificate').combobox({
						url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=certificate",
						valueField : 'encode',
						textField : 'name',
						multiple : false,
						onLoadSuccess: function (data) { //加载完成后,设置选中第一项
							for(var i=0;i<data.length;i++){
				                for (var item in data[i]) {
				                    if (item == "encode" && data[i].name=="身份证") {
				                        $(this).combobox("select", data[i][item]);
				                    }
				                }
							}
			            }
					});
					$('#businessContractunit').combobox('reload');//合同单位
					//加载合同单位
					$('#businessContractunit').combobox({   
						valueField:'encode',
						textField:'name',
						url: "<%=basePath%>patient/patinent/queryUnitCombobox.action",    
					    onLoadSuccess:function(data){
					    	for(var i=0;i<data.length;i++){
				                for (var item in data[i]) {
				                    if (item == "encode" && data[i].name=="自费") {
				                        $(this).combobox("select", data[i][item]);
				                    }
				                }
							}
					    }
					});
					$('#patientPaykind').combobox('reload');//结算类别
					//加载结算类别
					$('#patientPaykind').combobox({   
						valueField:'encode',
						textField:'name',
						url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=paykind",    
					    onLoadSuccess:function(data){
					    	for(var i=0;i<data.length;i++){
				                for (var item in data[i]) {
				                    if (item == "encode" && data[i].name=="自费") {
				                        $(this).combobox("select", data[i][item]);
				                    }
				                }
							}
					    }
					});
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
		parent.$("#list").datagrid("reload");
		self.close();//关闭当前窗口
	}

	$(function() {
		
		$('#CodeCertificate').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=certificate",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
			onLoadSuccess: function (data) { //加载完成后,设置选中第一项
				for(var i=0;i<data.length;i++){
	                for (var item in data[i]) {
	                    if (item == "encode" && data[i].name=="身份证") {
	                        $(this).combobox("select", data[i][item]);
	                    }
	                }
				}
            }
		});
		$('#CodeOccupation').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=occupation",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#CodeNationality').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=nationality",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#CodeRelation').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=relation",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#CodeMarry').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=marry",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#CodeCountry').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=country",
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
				var oneCode=$('#homeone').combobox('getValue');
				queryDistrictSJLDTwo(oneCode);
				var twoCode=$('#hometwo').combobox('getValue');
				queryDistrictSJLDThree(twoCode);
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
				    },
				    onLoadSuccess:function(){
				    	var select=$('#businessContractunit').combobox('getData');
				    	for(var i=0;i<select.length;i++){
				    		if(select[i].name=="自费"){
				    			$('#businessContractunit').combobox('select',select[i].encode);
				    			return;
				    		}
				    	}
				    }
				});
				
				//加载结算类别
				$('#patientPaykind').combobox({   
					valueField:'encode',
					textField:'name',
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=paykind",    
					onSelect:function(node) {
				    	$('#patientPaykind').val(node.id);
				    },
				    onLoadSuccess:function(){
				    	var select=$('#patientPaykind').combobox('getData');
				    	for(var i=0;i<select.length;i++){
				    		if(select[i].name=="自费"){
				    			$('#patientPaykind').combobox('select',select[i].encode);
				    			return;
				    		}
				    	}
				    }
				});
				/*******************************开始读卡***********************************************/	
				//2017-09-09读卡
				function read_card_ic(){
					var card_value=app.read_ic();
					if(card_value=='0'||card_value==undefined||card_value=='')
					{
						$.messager.alert('提示','此卡号['+card_value+']无效');
						return;
					}
					
					//填写就诊卡号
					$("#idcardNo").textbox("setValue",card_value);
					//就诊卡类型
					$("#CodeIdcardType").combobox("select","0");
				}
				
				/*******************************结束读卡***********************************************/
			 	/*******************************开始读身份证***********************************************/
			   ////2017-09-09读身份证
				function read_card_sfz(){
					
						var card_value=app.read_sfz().trim();//朱x             ,男,汉,1963年06月15日,重庆市沙坪坝区建工东村83号1-3                  ,510211196306151xxx,重庆市公安局沙坪坝分局    ,2008年03月05日,2028年03月05日,D:\cef\dcef3-2623\bin\Win32\510211196306151256.Bmp
						var cards = '';
						var patientCity='';//身份证前六位
						
						if(card_value=='0'||card_value==undefined||card_value=='')
						{
							$.messager.alert('提示','此卡号['+card_value+']无效');
							return;
						}else{
							cards = card_value.split(',');
							patientCity = cards[5].trim().substring(0,6);
						}
						if(cards==undefined||cards==''){
							$.messager.alert('提示','此卡号['+card_value+']无效');
							return;
						}
						
						//patientCity="410105";测试金水区
						//2.ajax请求，把身份证前六位发送到后台(同步)
						  $.ajax({
							url: "<%=basePath%>patient/idcard/readInfoIdcard.action",
							data:"patientCity="+patientCity,
							type:'post',
							/* async:false, */
							success: function(msgData) {
								//alert("执行成功！");
								//设置其余的信息
								//姓名
								$("#patientName").textbox("setValue",cards[0].trim());
								//性别
								if(cards[1].trim()=="男"){
				  					$('#patientSex').combobox('select','1');
				  					//alert("男");
				 				}else if(cards[1].trim()=="女"){
									$('#patientSex').combobox('select','2');
				 				}else{
				 					$('#patientSex').combobox('select','3');
				 				} 
//				 				//年龄
								var bt1=cards[3].trim().replace(/(\D+)/g,"-");
								var birthday=bt1.substring(0,bt1.length-1);
								$('#patientBirthday').val(birthday);
								//alert(birthday);
//				 				//证件类型
				  				$("#CodeCertificate").combobox("select","3");
//				 				//证件号码
				 				$("#certificatesno").textbox("setValue",cards[5].trim());
				 				//alert("请求成功");
								//3.把身份证前六位代码设置为后台病人的所在城市，处理完毕返回省市区代码
								//4.请求成功得到oneCode,twoCode,threeCode,fourCode,进行设置，回显
								var cty_nodes=msgData.split(",");
								//省市区
								queryDistrictSJLDOne();
								$('#homeone').combobox('select',cty_nodes[0]);
								//alert("第一个的节点为"+cty_nodes[0]);
								queryDistrictSJLDTwo(cty_nodes[0]);
								$('#hometwo').combobox('setValue',cty_nodes[1]);
								queryDistrictSJLDThree(cty_nodes[1]);
								//alert("第二个的节点为"+cty_nodes[1]);
								$('#homethree').combobox('setValue',cty_nodes[2]);
								var flag=cty_nodes[3]==""?true:false;
								queryDistrictSJLDFour(cty_nodes[2],flag);
								//alert("第三个的节点为"+cty_nodes[2]);
								$('#homefour').combobox('setValue',cty_nodes[3]);
								//alert("第四个的节点为"+cty_nodes[3]);
								//5.请求成功，发送ajax请求把身份证上民族"字符"发送到后台得到民资代码，进行回显
								//如果是汉就不用读表了
								if(cards[2].trim()=="汉"){
									$('#CodeNationality').combobox('setValue',"01");
								}else{
									//否则读表，查民族的代码
									$.ajax({
										url: "<%=basePath%>baseinfo/pubCodeMaintain/queryPubCode.action",
										data:"dictionary.name="+cards[2].trim(),
										type:'post',
										async:false,
										success:function(msgData){
											//alert(msgData)
											//得到民族对应的object数组
											var nations=msgData.rows;
											//alert(nations[0]);
											//alert(nations[0].encode);
											//民族
											//var minority=inentitys[2];
											$('#CodeNationality').combobox('select',nations[0].encode);
											}
									});
								}
								//6.国籍,如果市不为"",说明是中国人
								if($("#hometwo").combobox("getValue")!=""){
									$('#CodeCountry').combobox('setValue',"1");
								}
							}
						  
					});
				}
				
		 		
				/*******************************结束读身份证***********************************************/
			bindEnterEvent('CodeCountry',popWinToCountry,'easyui');//绑定回车事件
			bindEnterEvent('CodeNationality',popWinToNationality,'easyui');//绑定回车事件
			bindEnterEvent('CodeOccupation',popWinToOccupation,'easyui');//绑定回车事件
			bindEnterEvent('CodeRelation',popWinToRelation,'easyui');//绑定回车事件
			bindEnterEvent('businessContractunit',popWinToUnit,'easyui');//绑定回车事件
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
			    disabled:bool,
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
			     	var bool = true;
			     	if(node.cityName=="市辖区"){
			     		bool=false;
			     		$('#homefour').combobox({
			     			required: true,
							value:'',
							disabled:bool
						});
			     	}else{
			     		$('#patientCity').val(node.cityCode);
			     		$('#homefour').combobox({
			     			required: false,
							value:'',
							disabled:true
						});
			     	}				     	
		        	queryDistrictSJLDFour(node.cityCode,bool);
		        },filter: function(q, row){
		            var keys = new Array();
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
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
			url: "<%=basePath%>baseinfo/district/queryDistrictTreeTwo.action?parId="+id,    
				valueField:'cityCode',    
			    textField:'cityName',
			    required:true,
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
		        	queryDistrictSJLDThree(node.cityCode);
		        	//2017-01-14
//		        	$('#homethree').combobox('setValue','');
	        		$('#homefour').combobox('setValue','');
		        	
		        },filter: function(q, row){
		            var keys = new Array();
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
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
					//2017-01-14
					//$('#hometwo').combobox('setValue','');
					$('#homethree').combobox('setValue','');
					$('#homefour').combobox('setValue',''); 
		        },filter: function(q, row){
		            var keys = new Array();
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
		        } 
			});
			bindEnterEvent('homeone',popWinToDistrict,'easyui');//绑定回车事件
		}

	function KeyDown(flg, tag) {
		if (flg == 1) {//回车键光标移动到下一个输入框
			if (event.keyCode == 13) {
				event.keyCode = 9;
			}
		}
		if (flg == 0) { //空格键打开弹出窗口
			if (event.keyCode == 32) {
				event.returnValue = false;
				event.cancel = true;
				if (tag == "CodeMedicaltype") {
					showWin("请医疗类型", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeMedicaltype,0", "50%", "80%");
				}
				if (tag == "CodeCertificate") {
					showWin("请写证件类型", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeCertificate,0", "50%", "80%");
				}
				if (tag == "CodeMarry") {
					showWin("请写婚姻状况", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeMarry,0", "50%", "80%");
				}
				if (tag == "CodeBloodtype") {
					showWin("请写血型", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeBloodtype,0", "50%", "80%");
				}
				if (tag == "CodeSettlement") {
					showWin("请写结算方式","<%=basePath%>ComboxOut.action?xml="
							+ "CodeSettlement,0", "50%", "80%");
				}
				if (tag == "CodeSourse") {
					showWin("请写入院来源", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeSourse,0", "50%", "80%");
				}
				if (tag == "CodeNurselevel") {
					showWin("请写护理级别", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeNurselevel,0", "50%", "80%");
				}
				if (tag == "CodeIdcardType") {
					showWin("就诊卡类型", "<%=basePath%>ComboxOut.action?xml="
							+ "CodeIdcardType,0", "50%", "80%");
				}
			}
		}
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
	 * 回车弹出国籍选择窗口
	 * @author  wanxing
	 * @date 2016-03-22 14:30 
	 * @version 1.0
	 */
	 function popWinToCountry(){
		 	$('#CodeCountry').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=country&textId=CodeCountry";
			window.open (tempWinPath,'newwindowCountry',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 
	/**
	 * 回车弹出民族选择窗口
	 * @author  wanxing
	 * @date 2016-03-22  17:29
	 * @version 1.0
	 */
	 function popWinToNationality(){
			$('#CodeNationality').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=nationality&textId=CodeNationality";
			window.open (tempWinPath,'newwindowNationality',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	 }
	/**
	 * 回车弹出职业选择窗口
	 * @author  wanxing
	 * @date 2016-03-22  15:06
	 * @version 1.0
	 */
	 function popWinToOccupation(){
			$('#CodeOccupation').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=occupation&textId=CodeOccupation";
			window.open (tempWinPath,'newwindowOccupation',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	/**
	 * 回车弹出联系人关系选择窗口
	 * @author  wanxing
	 * @date 2016-03-22  15:10
	 * @version 1.0
	 */
	 function popWinToRelation(){
			$('#CodeRelation').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=relation&textId=CodeRelation";
			window.open (tempWinPath,'newwindowRelation',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
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
		
		
		
		/*******************************开始读卡***********************************************/	
		//2017-09-09读卡
		function read_card_ic(){
			var card_value=app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value=='')
			{
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			
			//填写就诊卡号
			$("#idcardNo").textbox("setValue",card_value);
			//就诊卡类型
			$("#CodeIdcardType").combobox("select","0");
		}
		
		/*******************************结束读卡***********************************************/
	 	/*******************************开始读身份证***********************************************/
	   ////2017-09-09读身份证
		function read_card_sfz(){
			
				var card_value=app.read_sfz_all().trim();//朱x             ,男,汉,1963年06月15日,重庆市沙坪坝区建工东村83号1-3                  ,510211196306151xxx,重庆市公安局沙坪坝分局    ,2008年03月05日,2028年03月05日,D:\cef\dcef3-2623\bin\Win32\510211196306151256.Bmp
				var cards = '';
				var patientCity='';//身份证前六位
				
				if(card_value=='0'||card_value==undefined||card_value=='')
				{
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}else{
					cards = card_value.split(',');
					patientCity = cards[5].trim().substring(0,6);
				}
				if(cards==undefined||cards==''){
					$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				
				//patientCity="410105";测试金水区
				//2.ajax请求，把身份证前六位发送到后台(同步)
				  $.ajax({
					url: "<%=basePath%>patient/idcard/readInfoIdcard.action",
					data:"patientCity="+patientCity,
					type:'post',
					/* async:false, */
					success: function(msgData) {
						//alert("执行成功！");
						//设置其余的信息
						//姓名
						$("#patientName").textbox("setValue",cards[0].trim());
						//性别
						if(cards[1].trim()=="男"){
		  					$('#patientSex').combobox('select','1');
		  					//alert("男");
		 				}else if(cards[1].trim()=="女"){
							$('#patientSex').combobox('select','2');
		 				}else{
		 					$('#patientSex').combobox('select','3');
		 				} 
//		 				//年龄
						var bt1=cards[3].trim().replace(/(\D+)/g,"-");
						var birthday=bt1.substring(0,bt1.length-1);
						$('#patientBirthday').val(birthday);
						//alert(birthday);
//		 				//证件类型
		  				$("#CodeCertificate").combobox("select","3");
//		 				//证件号码
		 				$("#certificatesno").textbox("setValue",cards[5].trim());
		 				//alert("请求成功");
						//3.把身份证前六位代码设置为后台病人的所在城市，处理完毕返回省市区代码
						//4.请求成功得到oneCode,twoCode,threeCode,fourCode,进行设置，回显
						var cty_nodes=msgData.split(",");
						//省市区
						queryDistrictSJLDOne();
						$('#homeone').combobox('select',cty_nodes[0]);
						//alert("第一个的节点为"+cty_nodes[0]);
						queryDistrictSJLDTwo(cty_nodes[0]);
						$('#hometwo').combobox('setValue',cty_nodes[1]);
						queryDistrictSJLDThree(cty_nodes[1]);
						//alert("第二个的节点为"+cty_nodes[1]);
						$('#homethree').combobox('setValue',cty_nodes[2]);
						var flag=cty_nodes[3]==""?true:false;
						queryDistrictSJLDFour(cty_nodes[2],flag);
						//alert("第三个的节点为"+cty_nodes[2]);
						$('#homefour').combobox('setValue',cty_nodes[3]);
						//alert("第四个的节点为"+cty_nodes[3]);
						//5.请求成功，发送ajax请求把身份证上民族"字符"发送到后台得到民资代码，进行回显
						//如果是汉就不用读表了
						if(cards[2].trim()=="汉"){
							$('#CodeNationality').combobox('setValue',"01");
						}else{
							//否则读表，查民族的代码
							$.ajax({
								url: "<%=basePath%>baseinfo/pubCodeMaintain/queryPubCode.action",
								data:"dictionary.name="+cards[2].trim(),
								type:'post',
								async:false,
								success:function(msgData){
									//alert(msgData)
									//得到民族对应的object数组
									var nations=msgData.rows;
									//alert(nations[0]);
									//alert(nations[0].encode);
									//民族
									//var minority=inentitys[2];
									$('#CodeNationality').combobox('select',nations[0].encode);
									}
							});
						}
						//6.国籍,如果市不为"",说明是中国人
						if($("#hometwo").combobox("getValue")!=""){
							$('#CodeCountry').combobox('setValue',"1");
						}
					}
				  
			});
		}
		
 		
		/*******************************结束读身份证***********************************************/
		
		
			
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
</body>
</html>