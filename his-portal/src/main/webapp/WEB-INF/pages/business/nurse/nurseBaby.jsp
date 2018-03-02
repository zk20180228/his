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
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: right;
		width:10%;
		height:35;
	}
	
	.tableCss .TDTextbox{
		width:10%;
	}
</style>
<body>
		<div id="tool">
			<a  onclick="register()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">登记
			<input type="hidden" id="blh" value="${medicalreId }">
			<input type="hidden" id="inpatientNo" value="${inpatientNo }">
			</a>
			<shiro:hasPermission name="${menuAlias}:function:set">
			<a  onclick="cancelRegister()" class="easyui-linkbutton" data-options="iconCls:'icon-2012080412301',plain:true">取消登记</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:print">
			<a  onclick="printCSZMB()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',plain:true">打印出生证</a>
			</shiro:hasPermission>
		</div>
		<div id="cc" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north'" style="width: 100%;height: 30%">
		    	<table id="babyList"  class="easyui-datagrid" data-options="fit:true,idField:'id',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,toolbar:'#tool'">
					<thead>
						<tr>
							<th style="width: 10%;" data-options="field:'name'">姓名</th>
							<th style="width: 10%;" data-options="field:'idcardNo'">就诊卡号</th>
							<th style="width: 10%;" data-options="field:'medicalrecordId'">病历号</th>
							<th style="width: 5%;" data-options="field:'sexName'">性别</th>
							<th style="width: 10%;" data-options="field:'birthDay'" >出生日期</th>
							<th style="width: 10%;" data-options="field:'gestation'">出生孕周</th>
							<th style="width: 10%;" data-options="field:'weight'">体重/g</th>
							<th style="width: 10%;" data-options="field:'height'">身长/cm</th>
							<th style="width: 10%;" data-options="field:'headSize'">头围</th>
							<th style="width: 9%;" data-options="field:'bloodCode'">血型</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center'" style="width: 100%;height: 70%">
				<form id="babyInfoForm" method="post">
					<input id="babyId" name="id" type="hidden"/>
					<input id="babyInpatientNo" name="babyInpatientNo" type="hidden"/>
					<table class="tableCss" style="height: 43%;width: 95%;">
						<tr>
							<td class="TDlabel">新生儿姓名：</td>
							<td class="TDTextbox">
							    <input id="name" name="name" class="easyui-textbox" style="width: 165px;" data-options="required:true"/></td>
							<td class="TDlabel">性别：</td>
							<td class="TDTextbox">
							    <input id="sexCode"  class="easyui-combobox" name="sexCode" style="width: 165px;">
							    <input id="sexName"    name="sexName" type="hidden"  >
							</td>
							<td class="TDlabel">出生日期：</td>
							<td colspan="3">
								<input id="birthDay" name="birthDay" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width:165px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr rowspan="2">
							<td class="TDlabel">出生地：</td>
							<td colspan="3"><div>
									<input  id="homeone" name="home1" style="width: 165px"  value="${oneName }" data-options="prompt:'省/直辖市',required:true"/>
									<input  id="hometwo" name="home2" style="width: 165px"  value="${twoName }" data-options="prompt:'市',required:true"/>
									<input  id="homethree" name="home3" style="width: 165px"  value="${threeName }" data-options="prompt:'县',required:true"/>
									<input  id="homefour" name="home4" style="width: 165px"  value="${fourName }" data-options="prompt:'区',required:true"/>
								</div>
								<div style="margin-top:5px;">
									<input class="easyui-textbox" style="width: 681px;"
										value="${patient.patientAddress }"
										name="home5"
										id="homefive"
										data-options="prompt:'社区/乡镇村详细地址',required:true"/>
								</div>
							</td>
							<td class="TDlabel">出生孕周：</td>
							<td colspan="3"><input style="width: 165px;" id="gestation" name="gestation" class="easyui-numberbox" data-options="validType:'gestationIsNumber[100]',required:true" />&nbsp;周</td>
						</tr>
						<tr>
							<td class="TDlabel">健康状况：</td>
							<td>
								<input type="radio"  name="healthStatus" value="0" />良好
								<input type="radio"  name="healthStatus" value="1" />一般
								<input type="radio"  name="healthStatus" value="2" />差
							</td>
							<td class="TDlabel">体重：</td>
							<td><input style="width: 165px;" id="weight" name="weight" class="easyui-numberbox" data-options="min:0,precision:2,validType:'isNumber[1000]',required:true" />&nbsp;克</td>
							<td class="TDlabel">身长：</td>
							<td colspan="5"><input style="width: 165px;" id="height" name="height" class="easyui-numberbox" data-options="precision:2,validType:'isNumber[1000]',required:true" />&nbsp;公分</td>
						</tr>
						<tr>
							<td class="TDlabel">头围：</td>
							<td><input style="width: 165px;" id="headSize" name="headSize" class="easyui-numberbox" data-options="precision:2,validType:'isNumber[1000]',required:true"  /></td>
							<td class="TDlabel">血型：</td>
							<td colspan="5">
								<select id="bloodCode" class="easyui-combobox" name="bloodCode" style="width:165px;" >
									<option value="A">A型</option>
									<option value="B">B型</option>
									<option value="O">O型</option>
									<option value="AB">AB型</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="TDlabel">母亲姓名：</td>
							<td><input style="width: 165px;" id="motherName" name="motherName"  value="${info.patientName }" class="easyui-textbox" /></td>
							<td class="TDlabel">年龄：</td>
							<td><input style="width: 165px;" id="motherAge" name="motherAge"  value="${info.reportAge }" class="easyui-numberbox" data-options="validType:'ageVaild[1000]'" />&nbsp;${info.reportAgeunit }
							</td>
							<td class="TDlabel">国籍：</td>
							<td style="width: 111px;">
								<select id="motherNationality" class="easyui-combobox"  value="${info.counCode }" name="motherNationality" style="width:110px;" >   
								</select>
							</td>
						</tr>
						<tr>
						 <td class="TDlabel">民族：</td>
							<td style="width: 111px;"><select id="motherNation" class="easyui-combobox"  value="${info.nationCode }" name="motherNation" style="width:110px;" >   
								</select>
							</td>
							<td class="TDlabel">身份证号：</td>
							<td ><input id="motherCardNo" name="motherCardNo" class="easyui-textbox"  value="${info.certificatesNo }" data-options="validType:'idCardCheck[1]'" style="width: 165px;" /></td>
						  
						</tr>
						<tr>
							<td class="TDlabel">父亲姓名：</td>
							<td><input id="fatherName" name="fatherName" class="easyui-textbox" value="${infoFather.patientName }"style="width: 165px;" /></td>
							<td class="TDlabel">年龄：</td>
							<td><input id="fatherAge" name="fatherAge" class="easyui-numberbox" value="${infoFather.reportAge }" data-options="validType:'ageVaild[1000]'" style="width: 165px;" />&nbsp;岁</td>
							<td class="TDlabel">国籍：</td>
							<td style="width: 111px;">
								<select id="fatherNationality" class="easyui-combobox" value="${infoFather.counCode }" name="fatherNationality" style="width:110px;" >   
								</select>
							</td>
							
					</tr>
					<tr>
					 <td class="TDlabel">民族：</td>
							<td>
								<select id="fatherNation" class="easyui-combobox" value="${infoFather.nationCode }" name="fatherNation" style="width:110px;" >   
								</select>
							</td>
						<td class="TDlabel">身份证号：</td>
						<td ><input id="fatherCardnNo" name="fatherCardnNo"  value="${infoFather.certificatesNo }"  class="easyui-textbox" data-options="validType:'idCardCheck[1]'" style="width: 165px;" /></td>
			 		</tr>
					<tr>
						<td class="TDlabel">出生地点分类：</td>
						<td colspan="7">
							<input name="placeType" value="0" type="radio" />医院
							<input name="placeType" value="1" type="radio" />妇幼保健院
							<input name="placeType" value="2" type="radio" />家庭
							<input name="placeType" value="3" type="radio" />其他
						</td>
					</tr>
					<tr>
						<td class="TDlabel">接生机构名称：</td>
						<td colspan="7"><input id="facility" name="facility" class="easyui-textbox" style="width: 477px;" /></td>
					</tr>
					<tr>
						<td class="TDlabel">出生证编号：</td>
						<td><input id="birthCertificateNo" name="birthCertificateNo" class="easyui-textbox" style="width: 165px;" data-options="required:true"/></td>
						<td class="TDlabel">签发日期：</td>
						<td colspan="5">
						<input id="issueDate" name="issueDate" class="Wdate" type="text" value="${now }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width:165px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						<input id="ss"  type="hidden" value="${inpatientInfo }" style="width: 165px;" />
						</td>
					</tr>
				</table>
				</form>
		    </div>   
		</div>
		<script type="text/javascript">
		//打印出生证明
		function printCSZMB(){
			var row = $("#babyList").datagrid("getSelected");
			if(row){
				var ID = row.id;
				
				var timerStr = Math.random();
			 	window.open ("<c:url value='/iReport/iReportPrint/iReportToCSZMB.action?randomId='/>"+timerStr+"&ID="+ID+"&fileName=CSZMB",'newwindow'+ID,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
			} 
		}
		$(function(){
			//地址
			queryDistrictSJLDOne();
			$('#homefour').combobox({
				
			});
			$('#homethree').combobox({
				
			});
			$('#hometwo').combobox({
				
			});
			$('#sexCode').combobox({
				prompt:'请选择性别',
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
				valueField:'encode',
				textField:'name',
				required:true
			});
			
			
			/**
			 * 婴儿展示列表
			 * @author  huangbiao
			 * @date 2016-3-21
			 * @version 1.0
			 */
			$('#babyList').datagrid({
				url:'<%=basePath %>nursestation/babyinfo/getBabyList.action',
				pageSize:20,
				pageList:[20,30,50,80,100],
				pagination:true,
				queryParams:{
					medicalrecordId:$('#blh').val()
					},
				onSelect:function(rowIndex,rowData){
					$('#babyId').val(rowData.id);//姓名
					$('#babyInpatientNo').val(rowData.inpatientNo);//姓名
					$('#name').textbox('setValue',rowData.name);//姓名
					$('#sexCode').combobox('setValue',rowData.sexCode);//性别
					$('#birthDay').val(rowData.birthDay);//出生日期
					var homeArr = rowData.birthAddress.split(",");
					 $('#homeone').combobox('setValue',homeArr[0]);
					 
					 queryDistrictSJLDTwo(homeArr[0]);
					 queryDistrictSJLDThree(homeArr[1]);
					 queryDistrictSJLDFour(homeArr[2],false);
					 $('#hometwo').combobox('setValue',homeArr[1]);
					 $('#homethree').combobox('setValue',homeArr[2]);
					 
					 if(homeArr[3]!=null&&homeArr[3]!=""){
						 $('#homefour').combobox({
								value:homeArr[3],
								disabled:false
							});
					 }else{
						 $('#homefour').combobox({
								value:'',
								disabled:true
							});
					 }
					
					 $('#homefive').textbox('setValue',homeArr[4]);
					$('#gestation').textbox('setText',rowData.gestation);
					$('#gestation').textbox('setValue',rowData.gestation);
					
					var opt1 = document.getElementsByName("healthStatus");
					for(var i=0;i<opt1.length;i++){
						if(opt1[i].value==rowData.healthStatus){
							opt1[i].checked=true;
							opt1[i].value=rowData.healthStatus;
						}
					}
					
					$('#weight').textbox('setValue',rowData.weight);
					$('#height').textbox('setValue',rowData.height);
					$('#headSize').textbox('setValue',rowData.headSize);
					$('#bloodCode').combobox('setValue',rowData.bloodCode);
					$('#motherName').textbox('setValue',rowData.motherName);
					$('#motherAge').textbox('setValue',rowData.motherAge);
					$('#motherNationality').combobox('setValue',rowData.motherNationality);
					$('#motherNation').combobox('setValue',rowData.motherNation);
					$('#motherCardNo').textbox('setValue',rowData.motherCardNo);
					$('#fatherName').textbox('setValue',rowData.fatherName);
					$('#fatherAge').textbox('setValue',rowData.fatherAge);
						$('#fatherNationality').combobox('setValue',rowData.fatherNationality);
						$('#fatherNation').combobox('setValue',rowData.fatherNation);
					$('#fatherNation').combobox('setValue',rowData.fatherNation);
					$('#fatherCardnNo').textbox('setValue',rowData.fatherCardnNo);
					
					var opt2 = document.getElementsByName("placeType");
					for(var i=0;i<opt2.length;i++){
						if(opt2[i].value==rowData.placeType){
							opt2[i].checked=true;
							opt2[i].value=rowData.placeType;
						}
					}
					$('#facility').textbox('setValue',rowData.facility);
					$('#birthCertificateNo').textbox('setValue',rowData.birthCertificateNo);
					$('#issueDate').val(rowData.issueDate);
					
					var motherNation=$('#motherNationality').combobox('getValue');
					var fatherNation=$('#fatherNationality').combobox('getValue');
					if(motherNation!='1'){
						$('#motherNation').combobox({
							disabled:true
						});		
					}
					if(fatherNation!='1'){
						$('#fatherNation').combobox({
							disabled:true
						});		
					}
					
				}
			});
			

			/**
			 * 父亲国籍，民族联动
			 * @author  huangbiao
			 * @date 2016-3-16
			 * @version 1.0
			 */
			$('#fatherNation').combobox({
				valueField:'encode',
				textField:'name',
				editable:true,
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
				queryParams:{type:'nationality'}
			})
			$('#fatherNationality').combobox({
				valueField:'encode',
				textField:'name',
				editable:true,
				mode:'local',
				filter: function(q, row){
					var opts = $(this).combobox('options');
					if(opts.textField.indexOf(q)==0){
						return row[opts.textField];
					}
					if(opts.valueFiled.indexOf(q)==0){
						return row[opts.valueFiled];
					}
				},
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=country'/>", 
				onSelect: function(rec){
					$('#fatherNation').combobox('clear');
					if(rec.encode!="1"){
						$('#fatherNation').combobox({
							disabled:true
					});
					}else{
						$('#fatherNation').combobox({
							disabled:false
						});
						$('#fatherNation').combobox('setValue','01');
					}
				}
			})
			/**
			 * 母亲的国籍民族
			 * @author  huangbiao
			 * @date 2016-3-20
			 * @version 1.0
			 */
			$('#motherNation').combobox({
				valueField:'encode',
				textField:'name',
				editable:true,
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
				queryParams:{type:'nationality'} 
			})
			$('#motherNationality').combobox({
				valueField:'encode',
				textField:'name',
				editable:true,
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=country'/>", 
				onSelect: function(rec){
					$('#motherNation').combobox('clear');
					if(rec.encode!="1"){
						$('#motherNation').combobox({
							disabled:true
						});
					}else{
						$('#motherNation').combobox({
							disabled:false
						});
						$('#motherNation').combobox('setValue','01');
					}
				}
			})
			var motherNationality="${info.counCode }";
			var fatherNationality="${infoFather.counCode }";
			if(motherNationality==""){
				$('#motherNationality').combobox('setValue','1');
				var motherNation="${info.nationCode }";
				if(motherNation==""||motherNation==null){
					$('#motherNation').combobox('setValue','01');
				}else{
					$('#motherNation').combobox('setValue',motherNation);
				}
			}else{
				if(motherNationality!='1'){
					$('#motherNation').combobox({
						disabled:true
					});		
				}
				$('#motherNationality').combobox('setValue',motherNationality);
			}
			if(fatherNationality==""){
				$('#fatherNationality').combobox('setValue','1');
				var fatherNation="${infoFather.nationCode }";
				if(fatherNation==""||fatherNation==null){
					$('#fatherNation').combobox('setValue','01');
				}
			}else{
				if(fatherNationality!='1'){
					$('#fatherNation').combobox({
						disabled:true
					});		
				}
			}
			

		})
		

		     /**
			 * 登记
			 * @author  huangbiao
			 * @date 2016-3-17
			 * @version 1.0
			 */
			function register(){
				var blh = $('#blh').val();
				var inpatientNo = $('#inpatientNo').val();
				var motherCardNo = $('#motherCardNo').val();//母亲身份证号
				var fatherCardNo = $('#fatherCardnNo').val();//父亲身份证号
				if(blh==null||blh==""){
					$.messager.alert("提示","请选择待接诊患者！","info");
					setTimeout(function(){
	   					$(".messager-body").window('close');
	   				},3500);
					return false;
				}
				jQuery.post("<%=basePath%>nursestation/babyinfo/getInpantientInfo.action",{"blh":blh},function(result){
					if(result=="no"){
						$.messager.alert("提示","只有母亲才可以进行婴儿登记！","info");
						setTimeout(function(){
		   					$(".messager-body").window('close');
		   				},3500);
						return false;
					}
					submit(inpatientNo);
				});
			}
			//表单提交
			function submit(inpatientNo){ 
				$('#sexName').val($('#sexCode').combobox('getText'));
				var blh=$('#blh').val();
				$('#babyInfoForm').form('submit',{  
			    	url: '<%=basePath%>nursestation/babyinfo/babyRegister.action?inpatientNo='+inpatientNo,
			        onSubmit:function(){
						if (!$('#babyInfoForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
						$.messager.progress({text:'正在登记,清稍等...',modal:true});
			        },  
			        success:function(data){ 
			        	var dataMap = eval("("+data+")");
			        	$.messager.progress('close');
			        	if(dataMap.resMsg=="success"){
			        		$.messager.alert('提示',dataMap.resCode);
				        	setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$('#babyList').datagrid('reload',{medicalrecordId:blh});
							$("#babyInfoForm").form('clear');
							$('#motherName').textbox('setValue',"${info.patientName }");
							$('#motherAge').textbox('setValue',"${info.reportAge  }");
							$('#motherNationality').combobox('setValue',"${info.counCode}");
							$('#motherNation').combobox('setValue',"${info.nationCode}");
							$('#motherCardNo').textbox('setValue',"${info.certificatesNo }");
			        	}else{
			        		$.messager.alert('提示',dataMap.resCode);
			        	}
			        		
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败!');	
					}							         
			    }); 
			}	    
			/**
			 * 取消登记
			 * @author  huangbiao
			 * @date 2016-3-17
			 * @version 1.0
			 */
			function cancelRegister(){
				var selected = $('#babyList').datagrid('getSelections');
				var idValue = "";
				var blh = $('#blh').val();
				if(selected!=null&&selected.length>0){
					for(var i=0;i<selected.length;i++){
							idValue=selected[i].id;
							inpatientNo = selected[i].inpatientNo;
							nameValue=selected[i].name;
							jQuery.post("<%=basePath%>nursestation/babyinfo/cancelRegister.action",{"blh":blh,"id":idValue,"nameValue":nameValue,"inpatientNo":inpatientNo},function(result){ 
								if(result=="no"){
									$.messager.alert("提示","未结算的费用总额+已结算的费用>0，不允许取消登记！","info");
									setTimeout(function(){
					   					$(".messager-body").window('close');
					   				},3500);
									return false;
								}else{
									$.messager.alert("提示","取消登记成功！","info");
									setTimeout(function(){
					   					$(".messager-body").window('close');
					   				},3500);
									$('#babyList').datagrid('reload',{medicalrecordId:blh});
									window.parent.$("#treePatientInfo").tree('reload');
									$("#babyInfoForm").form('clear');
								}
							})
					}
				}else{
					$.messager.alert("提示","请选择一条数据！","info");
					setTimeout(function(){
	   					$(".messager-body").window('close');
	   				},3500);
				}
			}
			
			/**
			 * 省市区三级联动      省市区三级联动没有业务关联，不将id改成code了
			 * @author  huangbiao
			 * @date 2016-3-21
			 * @version 1.0
			 */
			function queryDistrictSJLDFour(id,bool) {
					$('#homefour').combobox({  
						url: "<c:url value='/baseinfo/district/queryDistrictTreeFour.action'/>?parId="+id,    
						valueField:'cityCode',    
						    textField:'cityName',
						    multiple:false,
						    editable:true,
						    onSelect:function(node) {
						    }
					});
			}
			function queryDistrictSJLDThree(id) {
					$('#homethree').combobox({  
						url: "<c:url value='/baseinfo/district/queryDistrictTreeThree.action'/>?parId="+id,    
							valueField:'cityCode',    
						    textField:'cityName',
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
						     		$('#homefour').combobox({
										required: false,
										value:'',
										disabled:true
									});
						     	}
					        	queryDistrictSJLDFour(node.cityCode,bool);
					        }
					});
			}
			function queryDistrictSJLDTwo(id) {
					$('#hometwo').combobox({  
						url: "<c:url value='/baseinfo/district/queryDistrictTreeTwo.action'/>?parId="+id,    
							valueField:'cityCode',    
						    textField:'cityName',
						    multiple:false,
						    editable:true,
						    onSelect:function(node) {
					        	 $('#homefour').combobox({
									value:''
								});
								$('#homethree').combobox({
									value:''
								}); 
								queryDistrictSJLDThree(node.cityCode);
					        }
					});
			}
			function queryDistrictSJLDOne() {
					$('#homeone').combobox({  
						url: "<c:url value='/baseinfo/district/queryDistrictTreeOne.action'/>?parId=",
						valueField:'cityCode',    
					    textField:'cityName',
					    multiple:false,
					    editable:true,
					    onSelect:function(node) {
							$('#homefour').combobox({
									value:''
							});
							$('#homethree').combobox({
								value:''
							});
							$('#hometwo').combobox({
								value:''
							});
							queryDistrictSJLDTwo(node.cityCode);
				        }
					});
				}
		</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>