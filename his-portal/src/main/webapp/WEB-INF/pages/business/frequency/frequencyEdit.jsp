<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>频次编辑</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
		<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',border:false,fit:true">
			<div style="padding:5px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="frequency.id" value="${frequency.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
						<tr>
							<td class="honry-lable">代码：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="encode" name="frequency.encode" value="${frequency.encode }" data-options="required:true" missingMessage="请输入代码" /></td>
		    			</tr>
		    			<tr>
			    			 <td class="honry-lable">名称：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="name" name="frequency.name" value="${frequency.name}" data-options="required:true" missingMessage="请输入名称" /></td>
			    		</tr>
			    		<c:if test="${not empty frequency.id }">
						<tr>
							<td class="honry-lable">拼音码：</td>
			    			<td class="honry-info">${frequency.pinyin }&nbsp;&nbsp;</td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">五笔码：</td>
			    			<td class="honry-info">${frequency.wb }&nbsp;&nbsp;</td>					
						</tr>
						</c:if>
						<tr>
							<td class="honry-lable">自定义码：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="inputCode" name="frequency.inputCode" value="${frequency.inputCode }" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">用法：</td>
							<td class="honry-info">
							<input style="width:95%" id="useMode" name="frequency.useMode" value="${frequency.useMode }" data-options="required:true" missingMessage="请输入用法" "/>
							<a href="javascript:delSelectedData('useMode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
		    			<tr>
							<td class="honry-lable">单位：</td>
							<td>
							<input class="easyui-combobox" style="width:90%" id="frequencyUnit" name="frequency.frequencyUnit" value="${frequency.frequencyUnit }" data-options="valueField: 'value',textField: 'text',data:[{value: 'M',text: '分钟'},{value: 'H',text: '小时'},{value: 'D',text: '天'},{value: 'W',text: '周'},{value: 'T',text: '必须时'},{value: 'ONCE',text: '仅一次'}],required:true" missingMessage="请输入单位" "/>
							</td>
						</tr>
		    			<tr id = "always" style="display:none">
							<td class="honry-lable">是否持续：</td>
							<td>
				    		<input class="easyui-combobox" style="width:90%" id="isalwaysFlags" name="frequency.alwaysFlag" value="${frequency.alwaysFlag }" data-options="valueField: 'valu',textField: 'tex',data:[{valu: '0',tex: '否'},{valu: '1',tex: '是'}]" missingMessage="请选择是否持续" "/>
		    				</td>
		    			</tr>
				    	<tr>
							<td class="honry-lable">频次数目：</td>
			    			<td class="honry-info"><input class="easyui-numberbox" style="width:95%" id="frequencyNum" name="frequency.frequencyNum" value="${frequency.frequencyNum }" /></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">频次次数：</td>
			    			<td class="honry-info"><input class="easyui-numberbox" style="width:95%" id="frequencyTime" name="frequency.frequencyTime" value="${frequency.frequencyTime }" /></td>
		    			</tr>
						<tr>					
							<td class="honry-lable">时间点：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%;height: 80px" id="period" name="frequency.period" data-options="multiline:true" value="${frequency.period }" /></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">排序：</td>
							<td class="honry-info"><input class="easyui-numberbox"  style="width:95%" id="order" name="frequency.order" value="${frequency.order }" readonly/></td>
						</tr>
		    			<tr>					
							<td class="honry-lable">适用医院：</td>
			    			<td class="honry-info">
			    				<input  style="width:65%" id="hospital" readonly/>
			    				<input type="button" value="选择医院" onclick="alertdialoghospital()"style="width: 30%">
			    				<input type="hidden" id="hospitalCode" name="hospitalCode" value="${frequency.hospital }">
			    			</td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">不适用医院：</td>
							<td class="honry-info"><input  style="width:65%" id="nonHospital"  readonly />
								<input type="button" value="选择医院"onclick="alertdialognonHospital()"style="width: 30%">
								<input type="hidden" id="nohospitalCode" name="nohospitalCode" value="${frequency.nonHospital }">
							</td>
						</tr>
						<tr>
							<td class="honry-lable">说明：</td>
			    			<td>
			    			<textarea class="easyui-textbox" style="width:95%;height: 60px" id="description"  name="frequency.description"
								data-options="multiline:true">${frequency.description }</textarea>
			    			</td>
						</tr>
						<tr>					
							<td colspan="2">可选标志：
				    			<input type="hidden" id="canSelects" name="frequency.canSelect" value="${frequency.canSelect }"/>
				    			<input type="checkBox" id="canSelect" onclick="javascript:checkBoxSelect(this,0,1)"/>
							&nbsp;&nbsp;默认标志：
				    			<input type="hidden" id="isDefaults"name="frequency.isDefault" value="${frequency.isDefault }"/>
				    			<input type="checkBox" id="isDefault" onclick="javascript:checkBoxSelect(this,0,1)"/>				    			
							&nbsp;&nbsp;停用标志：
				    			<input type="hidden" id="stop_flgs" name="frequency.stop_flg" value="${frequency.stop_flg }"/>
				    			<input type="checkBox" id="stop_flg" onclick="javascript:checkBoxSelect(this,0,1)"/>
			    			</td>
		    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			     <c:if test="${frequency.id==null }">
			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    </c:if>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
			    <div id="roleWins"></div>
	    	</form>
	    </div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
	</style>
	<script type="text/javascript">
		function hospitalName(map){
			var code=$('#hospitalCode').val();
			var noCode=$('#nohospitalCode').val();
			if(code){
				var codes=code.split(',');
				var len=codes.length;
				var name='';
				for(var a=0;a<len;a++){
					if(map.get(codes[a])){
						name=name+map.get(codes[a])+',';
					}
				}
				$('#hospital').val(name.substring(0, name.length-1));
			}
			if(noCode){
				var codes1=noCode.split(',');
				var len=codes1.length;
				var name1='';
				for(var i=0;i<len;i++){
					if(map.get(codes1[i])){
						name1=name1+map.get(codes1[i])+',';
					}
					
				}
				$('#nonHospital').val(name1.substring(0, name1.length-1));
			}
		}
		/**
		 * 页面加载
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		 var hospitalMap=new Map();
		$(function(){
			var hospitalId=$('#hospitalId').val();
			var nohospitalId=$('#nohospitalId').val();
			
			if($('#id').val()!=''){//判断是否编辑状态，编辑状态修改复选框选中状态
				
				if($('#canSelects').val()==1){
					document.getElementById('canSelect').checked=true;
				}
				if($('#isDefaults').val()==1){
					document.getElementById('isDefault').checked=true;
				}
				if($('#stop_flgs').val()==1){
					document.getElementById('stop_flg').checked=true;
				}
			}
			
			$.ajax({
			    url:  basePath+"/baseinfo/hospital/queryHospitalName.action",
				type:'post',
				asyns:false,
				success: function(data) {
					var type = data;
					for(var i=0;i<type.length;i++){
						hospitalMap.put(type[i].code,type[i].name);
					}
						hospitalName(hospitalMap);
				}
			});
			/**
			 * 用法下拉框
			 * @author liuhl
			 *参数为要解读的xml
			 */
			$('#useMode').combobox({
			    url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=useage",    
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    onHidePanel:function(none){
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
			
			$('#isalwaysFlags').combobox({
				onSelect:function () {
					   var v = $('#isalwaysFlags').combobox('getValue');
					   if(v == 1){
						   $('#period').textbox({
								value : '',
								disabled : true
							});
						   $('#frequencyTime').numberbox({
								value : '',
								disabled : true
							});
					   }else{
						   $('#period').textbox({
								value : '',
								disabled : false
							});
						   $('#frequencyTime').numberbox({
								value : '1',
								disabled : false
							});
					   }
				}
			});
			
			
			$('#frequencyUnit').combobox({
				onLoadSuccess:function(){
					var v = $('#frequencyUnit').combobox('getValue');
					if(v == 'ONCE' || v == 'T'){
						$('#period').textbox({
							value : '',
							disabled : true
						});
					}
				},
				onSelect:function () {
					   var v = $('#frequencyUnit').combobox('getValue');
						if(v == 'H' || v == 'M'){
							$('#always').show();
							$('#isalwaysFlags').combobox({
								value:''
								});
						}else{
							$('#always').hide();
						}
						if(v == 'ONCE' || v == 'T'){
							$('#period').textbox({
								value : '',
								disabled : true
							});
							$('#frequencyTime').numberbox({
								disabled : false
							});
							if(v == 'ONCE'){
								$('#frequencyTime').numberbox({
									value : '1'
								});
								$('#frequencyNum').numberbox({
									value : '1'
								});
							}else{
								$('#frequencyTime').numberbox({
									value : '0'
								});
								$('#frequencyNum').numberbox({
									value : '0'
								});
							}
						}else{
							$('#period').textbox({
								value : '',
								disabled : false
							});
							$('#frequencyTime').numberbox({
								value : '',
								disabled : false
							});
							$('#frequencyNum').numberbox({
								value : ''
							});
						}
				}
			});
			//2016年10月26日   gh  新加   如果为修改操作，初始化时需要根据单位的选项进行页面变更， 所以在这里重写一遍为了初始化页面。
			var v = $('#frequencyUnit').combobox('getValue');
			if(v == 'H' || v == 'M'){
				$('#always').show();
				$('#isalwaysFlags').combobox({
					valueField: 'id',
					textField: 'text',
					data:[{"id":0,"text":"否"},{"id":1,"text":"是"}],
					disabled:false
						});
				var sf = $('#isalwaysFlags').combobox('getValue');
				if(sf==1){
					$('#period').textbox({
						value : '',
						disabled : true
					});
					$('#frequencyTime').numberbox({
						value : '',
						disabled : true
					});
				}
			}else if(v == 'ONCE' || v == 'T'){
				$('#period').textbox({
					value : '',
					disabled : true
				});
				if(v == 'ONCE'){
					$('#frequencyTime').numberbox({
						value : '1'
					});
					$('#frequencyNum').numberbox({
						value : '1'
					});
				}else{
					$('#frequencyTime').numberbox({
						value : '0'
					});
					$('#frequencyNum').numberbox({
						value : '0'
					});
				}
				$('#always').hide();
			}else if(v){
				$('#frequencyTime').numberbox({
					value : ''
				});
				$('#frequencyNum').numberbox({
					value : ''
				});
			}
			//给用法方法绑定弹窗事件
			bindEnterEvent('useMode',popWinToUseage,'easyui');
		});	
	
		
	
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function submit(){ 
			 		var frequencyNum=$('#frequencyNum').numberbox('getValue');//数目
			 		var frequencyTime=$('#frequencyTime').numberbox('getValue');//次数
			 		if(frequencyNum!=null&&frequencyNum!=''){
			 			if(frequencyNum=='0'){
			 				$('#frequencyNum').numberbox('setValue','1');
			 				 $.messager.alert('提示信息','频次数目不能为0');
			 				setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			 				return false;
			 			}
			 		}else{
			 			$('#frequencyNum').numberbox('setValue','1');
			 		}
			 		if(frequencyTime!=null&&frequencyTime!=''){
			 			if(frequencyTime=='0'){
			 				$('#frequencyTime').numberbox('setValue','1');
			 				 $.messager.alert('提示信息','频次数目不能为0');
			 				setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
			 				return false;
			 			}
			 		}else{
			 			$('#frequencyTime').numberbox('setValue','1');
			 		}
					 var encode=$('#encode').val();//代码
					 var frequencyid=$('#id').val();//id
					 var hospital=$('#hospitalCode').val();//适用医院
					 if(''==hospital||null==hospital){
						 $.messager.alert('提示信息','可用医院不能为空');
						 setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						 return false;
					 }
					 var nonHospital=$('#nohospitalCode').val();//不适用医院
					 var order=$('#order').val();
					 var hospitalArraylist= new Array(); //定义适用医院数组 
					 var nonHospitalArraylist= new Array(); //定义不适用医院数组 
					 hospitalArraylist=hospital.split(",");
					 nonHospitalArraylist=nonHospital.split(",");
							   $('#editForm').form('submit',{  
							        url:"<c:url value='/baseinfo/frequency/saveFrequency.action'/>",
							        onSubmit:function(param){
							        	if (!$('#editForm').form('validate')) {
											$.messager.show({
												title : '提示信息',
												msg : '验证没有通过,不能提交表单!'
											});
											$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											return false;
										}
							        	param.ordert = order;    
							            param.hospitalt =hospital; 
							            param.nonHospitalt =nonHospital; 
							        },  
							        success:function(data){ 
							        	var arr=data.split(',');
							        	if(arr[0]=='Y'){
							        		$.messager.alert('提示信息',hospitalMap.get(arr[1])+'适用医院重复,请查看','warning');
							        		setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
							        		return false;
							        	}else if(arr[0]=='N'){
							        		$.messager.alert('提示信息',hospitalMap.get(arr[1])+'不适合医院重复,请检查','warning');
							        		setTimeout(function(){
												$(".messager-body").window('close');
											},3500);

							        	}else{
							        		$.messager.alert('提示信息','保存成功！','ok');
							        		$('#divLayout').layout('remove','east');
							        		//实现刷新栏目中的数据
						                     $('#list').datagrid('reload');
							        	}
							        	//
							        },
									error : function(data) {
										$.messager.alert('提示信息','保存失败！','warning');
									}							         
							    });
	    }	    
	    
		/**
		 * 连续添加
		 * @author  liujinliang
		 * @date 2015-6-1 10:58
		 * @version 1.0
		 */
		function addContinue(){
					var encode=$('#encode').val();
					var frequencyid=$('#id').val();
					var hospital=$('#hospital').val();//适用医院
					if(''==hospital||null==hospital){
						 $.messager.alert('提示信息','可用医院不能为空');
						 setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						 return false;
					 }
					var nonHospital=$('#nonHospital').val();//不适用医院
					var order=$('#order').val();
					$.ajax({
						url: "<c:url value='/baseinfo/frequency/queryFrequencyEncode.action'/>",
						type:'post',
						data:{encodet:encode,frequencyid:frequencyid},
						success: function(data) {
							if (data=="0") {
							    $('#editForm').form('submit',{  
							        url:"<c:url value='/baseinfo/frequency/saveFrequency.action'/>", 
							        onSubmit:function(param){
										if (!$('#editForm').form('validate')) {
											$.messager.show({
												title : '提示信息',
												msg : '验证没有通过,不能提交表单!'
											});
											$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											return false;
										}
										param.ordert = order;    
							            param.hospitalt =hospital; 
							            param.nonHospitalt =nonHospital; 
							        },  
							        success:function(data){ 
							        	var arr=data.split(',');
							        	if(arr[0]=='Y'){
							        		$.messager.alert('提示信息',hospitalMap.get(arr[1])+'适用医院重复,请查看','warning');
							        		setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
							        		return false;
							        	}else if(arr[0]=='N'){
							        		$.messager.alert('提示信息',hospitalMap.get(arr[1])+'不适合医院重复,请检查','warning');
							        		setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
							        		return fasle;
							        	}else{
							        		$.messager.alert('提示信息','保存成功！');
								            //实现刷新栏目中的数据
								             clear();
								           //实现刷新栏目中的数据
						                     $('#list').datagrid('reload');
						                     add();
							        	}
							        		
							        },
									error : function(data) {
										$.messager.alert('提示信息','保存失败！');
									}							         
							    }); 
							}else{
								$('#encode').textbox('setText','');
								$.messager.alert('警告！','该代码已存在请重新填写！','warning');
							}
						}
					});	
				 
	    }	    
		/**
		 * 清除页面填写信息
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function clear(){
			 var order = $('#order').val();
			$('#editForm').form('clear');
			$('#order').textbox('setText',order);
		}
		/**
		 * 关闭编辑窗口
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		/**
		 * 复选框选中
		 * @param defalVal 默认值
		 * @param selVal 选中值
		 * @author  liujinliang
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		function checkBoxSelect(obj,defalVal,selVal){
			var name = obj.id+"s";
			var element = document.getElementById(name);
			if(obj.checked==true){
				element.value=selVal;
			}else{
				element.value=defalVal;
			}
		}
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#roleWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		//适用医院选择弹窗
		function alertdialoghospital(){
			var hospitalCode=$('#hospitalCode').val();
			var nohospitalCode=$('#nohospitalCode').val();
			Adddilog("适用医院","<c:url value='/baseinfo/frequency/alertdilog.action'/>?type="+"yes"+"&hospitalCode="+hospitalCode+"&nohospitalCode="+nohospitalCode,'87%','88%');
		}
		//不适用医院选择弹窗
		function alertdialognonHospital(){
			var hospitalCode=$('#hospitalCode').val();
			var nohospitalCode=$('#nohospitalCode').val();
			Adddilog("不适用医院","<c:url value='/baseinfo/frequency/alertdilog.action'/>?type="+"no"+"&hospitalCode="+hospitalCode+"&nohospitalCode="+nohospitalCode,'87%','88%');
		}
		/**
		 * 打开用法界面弹框
		 * @author  zhuxiaolu
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		
		function popWinToUseage(){
			
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=useMode&type=useage";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>