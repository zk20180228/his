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
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center',fit:true" align="center">
	    	<form id="form1" method="post">
				<div id="main" style="padding-top:5%;">
					<table style="border: 1px solid; padding: 10px; width: 350px">
						<tr hidden>
							<td>主键 ：</td>
							<td><input id="inpatientId" class="easyui-textbox" /></td>
						</tr>
						<tr hidden>
							<td>病历号 ：</td>
							<td><input id="inpatientNo" class="easyui-textbox" /></td>
						</tr>
						<tr hidden>
							<td>账户状态 ：</td>
							<td><input id="accountState" class="easyui-textbox"  /></td>
						</tr>
						<tr>
							<td>病历号 ：</td>
							<td><input id="medicalrecordIdID" value="${medicalreId }" class="easyui-textbox" type="text" name="medicalrecordId" />
									<td><input id="inpatientNO" value="${inpatientNo }"  type="hidden"  /></td>
							</td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>姓 名：</td>
							<td><input id="patientNameId" class="easyui-textbox" readonly type="text"  /></td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>性 别：</td>
							<td><input id="reportSexID" class="easyui-combobox"  data-options="iconCls:'icon-user'"/></td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>病床号 ：</td>
							<td>
								<input id="newBedIDxinHidden" type="hidden"/>
								<input id="newBedIDxin" class="easyui-combobox"  data-options="prompt:'下拉或回车查询',iconCls:'icon-new_blue',required:true"/>
								<a href="javascript:delSelectedData('newBedIDxin');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>原病床号：</td>
							<td><input id="oldBedIdID" class="easyui-textbox"  type="text" readonly /></td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>住院医师：</td>
							<td><input id="zyys" class="easyui-combobox" data-options="prompt:'下拉或回车查询',iconCls:'icon-user_home'" />
								<a href="javascript:delSelectedData('zyys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>主治医师：</td>
							<td><input id="zzys" class="easyui-combobox" data-options="prompt:'下拉或回车查询',iconCls:'icon-user_gray_cool'" />
								<a href="javascript:delSelectedData('zzys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>主任医师：</td>
							<td><input id="zrys" class="easyui-combobox" data-options="prompt:'下拉或回车查询',iconCls:'icon-user_suit'" />
								<a href="javascript:delSelectedData('zrys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr style="height: 20px"></tr>
						<tr>
							<td>责任护士：</td>
							<td><input id="zrhs" class="easyui-combobox" data-options="prompt:'下拉或回车查询',iconCls:'icon-user_female'" />
								<a href="javascript:delSelectedData('zrhs');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr style="height: 50px;padding-left: 30%">
							<td></td>
							<td style="padding-left: 10%">
								<shiro:hasPermission name="${menuAlias}:function:save">
								<a id="btn" onClick="getINfooooo()" data-options="iconCls:'icon-save'" class="easyui-linkbutton">保&nbsp;存&nbsp;</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>
			</form>
	    </div>   
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//获取性别下拉列表  getcomboboxBystate
		$('#reportSexID').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			valueField : 'encode',
			textField : 'name'
		});
		
		/**
		 * 住院医生
		 */
		$('#zyys').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		/**
		 * 主治医生
		 */
		$('#zzys').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		/**
		 * 主任医生
		 */
		$('#zrys').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		
		/**
		 * 责任护士
		 */
		$('#zrhs').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=2',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		
		//根据登录科室获取病床号
		$("#newBedIDxin").combobox({
			url:'<%=basePath%>inpatient/recall/getcomboboxBystate.action',
			valueField:'id',
			textField:'bedName',
			onSelect: function(datas){
				$.ajax({
					type:'post',
					url:'<%=basePath%>inpatient/recall/getbusinesstion.action',
					data : {bedID : datas.id},
					success : function(data) {
					
						$("#zyys").combobox('setValue', data[0].houseDocCode);
						$("#zzys").combobox('setValue', data[0].chargeDocCode);
						$("#zrys").combobox('setValue', data[0].chiefDocCode);
						$("#zrhs").combobox('setValue', data[0].dutyNurseCode);
					}
				});
			}
		});
		
		bindEnterEvent('newBedIDxin',popWinToHospitalBed,'easyui');//病床号
		bindEnterEvent('zyys',popWinToEmployeeZYYS,'easyui');//绑定回车事件
		bindEnterEvent('zzys',popWinToEmployeeZZYS,'easyui');//绑定回车事件
		bindEnterEvent('zrys',popWinToEmployeeZRYS,'easyui');//绑定回车事件
		bindEnterEvent('zrhs',popWinToEmployeeZRHS,'easyui');//绑定回车事件
		$.ajax({
			type:'post',
			url:"<%=basePath%>nursestation/nurse/getInfobyId.action",
			data:{inpatientNo:$('#inpatientNO').val()},
			success:function(vv){
				if(vv.inState!='B'){
					$.messager.alert('提示','不可以对该患者进行出院召回,该患者不是出院登记状态！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			
				$("#inpatientId").textbox('setValue',vv.id);
				$("#accountState").textbox('setValue',vv.stopAcount);
				$("#inpatientNo").textbox('setValue',vv.inpatientNo);
				$("#patientNameId").textbox('setValue',vv.patientName);
				$("#reportSexID").combobox('setValue', vv.reportSex);
				$("#oldBedIdID").textbox('setValue',vv.bedName);
				}
			});
		});
	
	//保存信息
		function getINfooooo(){
				$('#newBedIDxinHidden').val($('#newBedIDxin').combobox('getValue'));
				//病床
				var bed = $('#newBedIDxin').combobox('getValue');
				//主键Id
				var inId= $("#inpatientId").textbox('getValue');
				//病历号
				var mID=$("#medicalrecordIdID").textbox('getValue');
				//住院医师
		        var houseDocCode=$("#zyys").combobox('getValue');
		        //主治医师
		        var chargeDocCode=$("#zzys").combobox('getValue');
		        //主任医师
		        var chiefDocCode=$("#zrys").combobox('getValue');
		        //责任护士
		        var dutyNurseCode=$("#zrhs").combobox('getValue');
		        //住院流水号
		        var inpatientNo=$("#inpatientNo").textbox('getValue');
		        //账户状态
		        var accountState=$("#accountState").textbox('getValue');
		        if(houseDocCode==''||chargeDocCode==''||chiefDocCode==''||dutyNurseCode==''||bed==''){
		        	$.messager.alert('提示','还有信息待完善，请检查！');
		        	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		        	return;
		        }else{
		        	if(accountState=='1'){
		        		$.messager.alert('提示','该用户正在进行结算操作，请稍后再试！');
		        		setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
			        	return;
			        }else{
			        	$.ajax({
			        		type:'post',
			        		//根据患者的患者号来查询患者信息
			        		url:'<%=basePath%>inpatient/recall/getBaByInfo.action',
			        		data:{babyInpatinetNo:inpatientNo},
			        		success:function(data2){
			        			var babyInfo=eval("("+data2+")");
			        			//如果患者的妈妈不为空 
			        			if(babyInfo.id!=null){
			        				if(babyInfo.motherInpatientNo==null){
			        					$.messager.alert('提示','查找母亲出错！');
			        					setTimeout(function(){
			        						$(".messager-body").window('close');
			        					},3500);
			        				}else{
			        					$.ajax({
			        						url:'<%=basePath%>inpatient/recall/getInfoMByInpatientNo.action',
			        						data:{no:babyInfo.motherInpatientNo},
			        						success:function(data){
		       								var info=eval("("+data+")");
		       								//如果查找失败
		       								if(info.id==null||info.id==""){
		       									$.messager.alert('提示','查找母亲出错！');
		       									setTimeout(function(){
		       										$(".messager-body").window('close');
		       									},3500);
		       								}else{
		       									//如果母亲非在院状态
		       									if(info.inState!='I'||info.inState!='R'){
		       										$.messager.alert('提示','母亲非在院状态，请先召回母亲');
		       										setTimeout(function(){
		       											$(".messager-body").window('close');
		       										},3500);
		       									}else{//如果婴儿母亲是在院状态
		       										$.ajax({
							    						type:'post',
							    						url:'<%=basePath%>inpatient/recall/saveInpatientRecall.action',
							    						data:{
							    							motherbedId:info.id,
							    							//当前婴儿主键Id
							    							infoid:inId,
							    							chargeDocCode:chargeDocCode,
							    							houseDocCode:houseDocCode,
							    							chiefDocCode:chiefDocCode,
							    							dutyNurseCode:dutyNurseCode,
							    							//bebdbebbd:bedd
							    						},
							    						success:function(){
							    							window.parent.$("#treePatientInfo").tree("reload");
							    							$.messager.alert('提示','召回成功!');
							    							setTimeout(function(){
							    								$(".messager-body").window('close');
							    							},3500);
							    							$("#form1").form('clear');
							    						}
							    					});
				      								}
				      							}
				      						}
			        					});
			        				}
			        			}else{//如果该患者不是婴儿
			        			var cbedis=	$('#newBedIDxinHidden').val();
				        			$.ajax({
				        				type:'post',
				        				url:'<%=basePath%>inpatient/recall/selectBedBYBedID.action?bediddd='+cbedis,
				        				success:function(data8){
				        					var bedstate=eval("("+data8+")");
				        					//如果病床没被占用
				        					if(bedstate.bedState=="7"||bedstate.bedState=="1"){
				        						//保存操作
				        						$.ajax({
		    			    						type:'post',
		    			    						url:'<%=basePath%>inpatient/recall/saveInpatientRecall.action',
		    			    						data:{
		    			    							infoid:inId,
		    			    							bebdbebbd:cbedis,
		    			    							chargeDocCode:chargeDocCode,
						    							houseDocCode:houseDocCode,
						    							chiefDocCode:chiefDocCode,
						    							dutyNurseCode:dutyNurseCode
		    			    						},
		    			    						success:function(){
		    			    							window.parent.$("#treePatientInfo").tree("reload");
		    			    							$.messager.alert('提示','召回成功!');
		    			    							setTimeout(function(){
		    			    								$(".messager-body").window('close');
		    			    							},3500);
		    			    							$("#form1").form('clear');
		    			    						}
		    			    					});
			        						}else{
			        							$.messager.alert('提示','床位已被占用,无法召回,请重新选择!');
			        							setTimeout(function(){
			        								$(".messager-body").window('close');
			        							},3500);
		        								$("#newBedIDxin").combobox('setValue','');
		        								return;
			        							}
				        					}
				        				});
			        			}
			        		}
			        	})
			        }
		        }
		 };

		 /**
			* 回车弹出病床号选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToHospitalBed(){
				popWinBedCallBackFn = function(node){
					$("#newBedIDxinHidden").val(node.id);
				    $("#newBedIDxin").combobox('setValue',node.id);
				};
				var tempWinPath = "<%=basePath%>popWin/popWinBusinessHospitalbed/toBusinessHospitalbedPopWin.action?textId=newBedIDxin&type=2";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
				+',scrollbars,resizable=yes,toolbar=yes')
			}
		
		/**
		   * 回车弹出住院医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployeeZYYS(){
			    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=zyys&employeeType=1";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-
				370)+',scrollbars,resizable=yes,toolbar=yes')
			
		   }
	   /**
		   * 回车弹出主治医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployeeZZYS(){
			    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=zzys&employeeType=1";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-
				370)+',scrollbars,resizable=yes,toolbar=yes')
			
		   }
	   /**
		   * 回车弹出主任医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployeeZRYS(){
			    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=zrys&employeeType=1";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-
				370)+',scrollbars,resizable=yes,toolbar=yes')
			
		   }
	   /**
		   * 回车弹出责任护士弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployeeZRHS(){
			    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=zrhs&employeeType=2";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-
				370)+',scrollbars,resizable=yes,toolbar=yes')
			
		   }
				
				
		   /**  
			 *  
			 * @Description：过滤	
			 * @Author：zhuxiaolu
			 * @CreateDate：2016-11-1
			 * @version 1.0
			 * @throws IOException 
			 *
			 */ 
		function filterLocalCombobox(q, row, keys){
			if(keys!=null && keys.length > 0){//
				for(var i=0;i<keys.length;i++){ 
					if(row[keys[i]]!=null&&row[keys[i]]!=''){
							var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
							if(istrue==true){
								return true;
							}
					}
				}
			}else{
				var opts = $(this).combobox('options');
				return row[opts.textField].indexOf(q.toUpperCase()) > -1;
			}
		}
				
</script>
</html>