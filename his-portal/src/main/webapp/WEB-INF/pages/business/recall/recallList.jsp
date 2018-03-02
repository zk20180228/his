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
<title>出院召回</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var sexMap=new Map();
	$(function() {
		var str="${medicalrecordId}";    //获得病历号
		var medicalrecordId=str.substring(6);    //截取病历号后位6位
		$('#medicalrecordIdID').textbox('setValue',medicalrecordId)
		//获取性别下拉列表  getcomboboxBystate
		$('#reportSexID').combobox({
			url :'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=sex ',
			valueField : 'encode',
			textField : 'name',
			onLoadSuccess:function(){
				var v = $('#reportSexID').combobox('getData');
			 	for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
	    	},
		});
		//根据登录科室获取病床号
		$("#newBedID").combobox({
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
			},onLoadSuccess:function(node){
				   console.info(node);
				   if(node.resCode=='error'){
					   $("body").setLoading({
							id:"body",
							isImg:false,
							text:node.resMsg
						});
				   }
			   }
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
		/**
		 * 病床号弹出事件高丽恒
		 * 2016-03-24 16:32
		 */
		bindEnterEvent('newBedID',popWinToHospitalBed,'easyui');//病床号
		bindEnterEvent('medicalrecordIdID',queryInfo,'easyui');
		
		/**
		* 绑定住院医师回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('zyys',popWinToEmployeeZYYS,'easyui');//绑定回车事件
		
		/**
		* 绑定主治医师回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('zzys',popWinToEmployeeZZYS,'easyui');//绑定回车事件
		
		/**
		* 绑定主任医师回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('zrys',popWinToEmployeeZRYS,'easyui');//绑定回车事件
		
		/**
		* 绑定责任护士回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('zrhs',popWinToEmployeeZRHS,'easyui');//绑定回车事件
	});
	//保存信息
	   function getINfooooo(){
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
		        if(inpatientNo==null||inpatientNo==""){
		        	$.messager.alert("提示","请输入要召回患者信息！");
		        	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		        	return;
		        }
		        //账户状态
		        var accountState=$("#accountState").textbox('getValue');
		        if(houseDocCode==null||chargeDocCode==null||chiefDocCode==null||dutyNurseCode==null){
		        	$.messager.alert("提示","还有信息待完善，请检查！");
		        	setTimeout(function(){
		    			$(".messager-body").window('close');
		    		},3500);
		        	return;
		        }
		        if(accountState=='1'){
		        	$.messager.alert("提示",'该用户正在进行结算操作，请稍后再试！');
		        	setTimeout(function(){
		    			$(".messager-body").window('close');
		    		},3500);
		        	return;
		        }
	        	$.ajax({
	        		type:'post',
	        		//根据患者的患者号来查询患者信息
	        		url:'<%=basePath%>inpatient/recall/getBaByInfo.action',
	        		data:{
	        			babyInpatinetNo:inpatientNo
	        		},
	        		success:function(data2){
	        			var babyInfo=eval("("+data2+")");
	        			//如果患者的妈妈不为空 
	        			if(babyInfo.id!=null){
	        				$.messager.alert('提示','该患者是婴儿');
	        				setTimeout(function(){
	    						$(".messager-body").window('close');
	    					},3500);
	        			   if(babyInfo.motherInpatientNo==null){
	        				   $.messager.alert('提示','查找母亲出错！');
	        				   setTimeout(function(){
	       							$(".messager-body").window('close');
	       						},3500);
	        			   }
	        			   else{
	        				   $.ajax({
	        					   url:'<%=basePath%>inpatient/recall/getInfoMByInpatientNo.action',
	        					   data:{
	        						   no:babyInfo.motherInpatientNo
	        					   },
	        					success:function(data){
       						   var info=eval("("+data+")");
       						   //如果查找失败
       						   if(info.id==null||info.id==""){
       							   $.messager.alert('提示','查找母亲出错!'); 
	       							setTimeout(function(){
	       								$(".messager-body").window('close');
	       							},3500);
       						   }
       						   else{
       							   //如果母亲非在院状态
       							   if(info.inState!='I'||info.inState!='R'){
       								   $.messager.alert('提示','母亲非在院状态，请先召回母亲');
	       								setTimeout(function(){
	       									$(".messager-body").window('close');
	       								},3500);   
       							   }
       							   //如果婴儿母亲是在院状态
       							   else{
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
				    							$.messager.alert('提示','召回成功');
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
	        			}
	        			//如果该患者不是婴儿
	        			else{
	        				//$.messager.alert('提示','该患者不是婴儿');
	        				var cbedis=	$('#newBedID').combobox('getValue');
	        				if(cbedis==""||cbedis==null){
	        					  $.messager.alert('提示','请选择病床号');
     								setTimeout(function(){
     									$(".messager-body").window('close');
     								},3500);  
     								return;
	        				}
		        			$.ajax({
		        				type:'post',
		        				url:'<%=basePath%>inpatient/recall/selectBedBYBedID.action?bediddd='+cbedis,
		        						success:function(data8){
		        							var bedstate=eval("("+data8+")");
		        							//如果病床没被占用
		        							if(bedstate.bedState=="7"||bedstate.bedState=="1")
		        							{
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
    			    							$.messager.alert('提示','召回成功!');
    			    							setTimeout(function(){
    			    								$(".messager-body").window('close');
    			    							},3500);
    			    							$("#form1").form('clear');
    			    						}
    			    					});
	        							}
	        							else{
	        								$.messager.alert("提示","床位已被占用,无法召回,请重新选择");
	        								setTimeout(function(){
	        									$(".messager-body").window('close');
	        								},3500);
	        								$("#newBedIDHidden").val('');
	        								return;
	        							}
		        					}
		        			    });
	        			}
	        		}
	        	})
		 };
	//查询患者住院信息
	function queryInfo(){
    	var mID=$("#medicalrecordIdID").textbox('getValue');
    	if(mID == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
    	$.ajax({
    		type:'post',
    		url:'<%=basePath%>inpatient/recall/queryInfoByMId.action',
    		data:{
    			MIdd:mID
    		},
    		success:function(data){
    			var vv=eval("("+data+")");
    			if(vv.length>1){
    				$("#diaInpatient").window('open');
					$("#infoDatagrid").datagrid({
						url:'<%=basePath%>inpatient/recall/queryInfoByMId.action?menuAlias=${menuAlias}&MIdd='+mID,    
					    columns:[[    
					        {field:'inpatientNo',title:'病历号',width:'20%',align:'center'} ,    
					        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
					        {field:'reportSex',title:'性别',width:'15%',align:'center',formatter:function(value,row,index){
					        	return sexMap.get(value);
					        }} ,
					        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
					        {field:'certificatesNo',title:'身份证号',width:'25%',align:'center'} 
					    ]] ,
					    onDblClickRow:function(rowIndex, rowData){
					    	$("#diaInpatient").window('close');
					    	if(rowData.inState=='I'||rowData.inState=='R'){
			    				$.messager.alert('提示','不可以对该患者进行出院召回,该患者是住院状态！');
			    				setTimeout(function(){
			    					$(".messager-body").window('close');
			    				},3500);
			    				return;
			    			}
					    	$.ajax({
		        				type:'post',
		        				url:'<%=basePath%>inpatient/recall/getbusinesstion.action',
								data : {bedID : rowData.bedNo },
								success : function(data) {
									$("#zyys").combobox('setValue', data[0].houseDocCode);
									$("#zzys").combobox('setValue', data[0].chargeDocCode);
									$("#zrys").combobox('setValue', data[0].chiefDocCode);
									$("#zrhs").combobox('setValue', data[0].dutyNurseCode);
								}
							});
					    	$("#inpatientId").textbox('setValue',rowData.id);
							$("#accountState").textbox('setValue',rowData.stopAcount);
							$("#inpatientNo").textbox('setValue',rowData.inpatientNo);
							$("#patientNameId").textbox('setValue',rowData.patientName);
							$("#reportSexID").combobox('setValue', rowData.reportSex);
							$("#oldBedIdID").textbox('setValue',rowData.bedName);
					    }
					});
    			}else if(vv.length=1){
    				if(vv[0].inState!='B'){
    					$.messager.alert('提示','不可以对该患者进行出院召回,该患者是不是出院登记状态！');
    					setTimeout(function(){
    						$(".messager-body").window('close');
    					},3500);
	    				return;
	    			}
    				$.ajax({
        				type:'post',
        				url:'<%=basePath%>inpatient/recall/getbusinesstion.action',
						data : {bedID : vv[0].bedNo },
						success : function(data) {
							$("#zyys").combobox('setValue', data[0].houseDocCode);
							$("#zzys").combobox('setValue', data[0].chargeDocCode);
							$("#zrys").combobox('setValue', data[0].chiefDocCode);
							$("#zrhs").combobox('setValue', data[0].dutyNurseCode);
						}
					});
    				$("#inpatientId").textbox('setValue',vv[0].id);
    				$("#accountState").textbox('setValue',vv[0].stopAcount);
    				$("#inpatientNo").textbox('setValue',vv[0].inpatientNo);
    				$("#patientNameId").textbox('setValue',vv[0].patientName);
    				$("#reportSexID").combobox('setValue', vv[0].reportSex);
    				$("#oldBedIdID").textbox('setValue',vv[0].bedName);
    					}
	   			else{
	   				$.messager.alert("提示","请检查输入!");
	   				setTimeout(function(){
	   					$(".messager-body").window('close');
	   				},3500);
	   				return;
	   			   }
    		     }
    		});
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
			* 回车弹出病床号选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToHospitalBed(){
				popWinBedCallBackFn = function(node){
					$("#newBedIDHiddden").val(node.encode);
				    	$("#newBedID").combobox('setValue',node.bedName);
						
				};
				var tempWinPath = "<%=basePath%>popWin/popWinBusinessHospitalbed/toBusinessHospitalbedPopWin.action?textId=newBedID&type=2";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
				+',scrollbars,resizable=yes,toolbar=yes')
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
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px; padding: 0px">
<div class="easyui-layout" fit=true>
	<div data-options="region:'center',border:false">
		<form id="form1" method="post">
			<div id="main" align="center">
				<h1 style="font-size: 30; padding: 15"></h1>
				<table style="padding: 10px; width: 350px" class="changeskin">
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
						<td>病历号：</td>
						<td><input id="medicalrecordIdID" data-options="prompt:'输入病历号回车查询'" class="easyui-textbox" type="text"  name="medicalrecordId" /></td>
					</tr>
					<tr style="height: 20px"></tr>
					<tr>
						<td align="left">姓&nbsp;&nbsp;名：</td>
						<td><input id="patientNameId" class="easyui-textbox" readonly type="text"  /></td>
					</tr>
					<tr style="height: 20px"></tr>
					<tr>
						<td align="left">性&nbsp;&nbsp;别：</td>
						<td><input id="reportSexID" class="easyui-combobox"  data-options="iconCls:'icon-user'"/></td>
					</tr>
					<tr style="height: 20px"></tr>
					<tr>
						<td align="left">病 床 号：</td>
						<td>
							<input id="newBedIDHidden" type="hidden"  data-options="iconCls:'icon-new_blue'"/>
							<input id="newBedID"  data-options="iconCls:'icon-new_blue',required:true"/>
							<a href="javascript:delSelectedData('newBedID');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
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
						<td><input id="zyys" class="easyui-combobox" />
							<a href="javascript:delSelectedData('zyys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr style="height: 20px"></tr>
					<tr>
						<td>主治医师：</td>
						<td><input id="zzys" class="easyui-combobox" />
							<a href="javascript:delSelectedData('zzys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr style="height: 20px"></tr>
					<tr>
						<td>主任医师：</td>
						<td><input id="zrys" class="easyui-combobox"  />
							<a href="javascript:delSelectedData('zrys');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr style="height: 20px"></tr>
					<tr>
						<td>责任护士：</td>
						<td><input id="zrhs" class="easyui-combobox" />
							<a href="javascript:delSelectedData('zrhs');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
				</table>
				<br>
				<div>
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a id="btn" onClick="getINfooooo()" data-options="iconCls:'icon-save'" class="easyui-linkbutton">保&nbsp;存&nbsp;</a>
				</shiro:hasPermission>
				</div>
		
			</div>
		</form>
	</div>
	</div>
	 <div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:500;padding:3px" data-options="modal:true, closed:true">   
		     <table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true">   
		</table>  
    </div>  
</body>

</html>