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
		<base href="<%=basePath%>">
		<title>添加页面</title>
	</head>
	<body>
	<div>
		<div id="p" class="easyui-panel" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:false">
			<form id="addDForm" method="post">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;">
				<input type="hidden" id="stoTerminalid" name="stoTerminal.id" value="${stoTerminal.id }"/>
				<input type="hidden" id="stoTerminalType" name="stoTerminal.type" value="${stoTerminal.type }"/>
				<input type="hidden" id="oldname" value="${stoTerminal.name }"/>
					 <tr>
						<td class="honry-lable">
							<span>打印类型:</span>
						</td>
						<td>
							<input  id=printType
									name="stoTerminal.printType"
									value="${stoTerminal.printType }"
									missingMessage="请选择打印类型" 
									data-options="required:true"
									style="width:150px;"
									editable="false"/>
						</td>
					</tr>
					<tr style="display: none">
						<td class="honry-lable">所属药房编码:</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="deptCode" name="stoTerminal.deptCode" 
				    				data-options="required:true" value="${stoTerminal.deptCode}"  
				    				style="width:150px" readonly="readonly"/>
		    			</td>
	    			</tr>
					<tr>
						<td class="honry-lable">
							<span>类别:</span>
						</td>
						<td>
							<input class="easyui-textbox" id="stotype" name="stotype" 
									data-options="required:true" style="width:150px" 
									readonly="readonly"/>
							<input id="stotypeHidden" type="hidden"
									data-options="required:true" style="width:150px" 
									readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>名称:</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id=name
									name="stoTerminal.name"
									value="${stoTerminal.name }"
									missingMessage="请填写名称" 
									data-options="required:true"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>编码:</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id='code'
									name="stoTerminal.code"
									value="${stoTerminal.code }"
									missingMessage="请填写编码" data-options="required:true"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>性质:</span>
						</td>
						<td style="text-align: left;">
							<input  id=property
									name="stoTerminal.property"
									value="${stoTerminal.property }"
									missingMessage="请填写性质" 
									data-options="required:true"
									editable="false"
									style="width:150px;"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>程序刷新时间间隔(秒):</span>
						</td>
						<td style="text-align: left;">
							<input id="refreshinterval1" class="easyui-numberspinner" 
											name="stoTerminal.refreshInterval1"
											value="${stoTerminal.refreshInterval1}"
											style="width:150px;"   
       										data-options="required:true,min:1,max:9999"/>  
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>警戒线:</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberspinner" id=alertNum
									name="stoTerminal.alertNum"
									value="${stoTerminal.alertNum }"
									missingMessage="请填写警戒线" 
									data-options="required:true,min:1,max:9999"
									style="width:150px;"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>是否关闭:</span>
						</td>
						<td style="text-align: left;">
							<input  id=closeFlag
									name="stoTerminal.closeFlag"
									value="${stoTerminal.closeFlag }"
									missingMessage="请选择是否关闭" 
									data-options="required:true"
									editable="false"
									style="width:150px;"/>
						</td>
					</tr>
					<tr id="stoTerminalAuto">
						<td class="honry-lable">
							<span>是否自动打印:</span>
						</td>
						<td style="text-align: left;">
							<input  id='autopringFlag'
									name="stoTerminal.autopringFlag"
									value="${stoTerminal.autopringFlag }"
									data-options="required:true"
									editable="false"
									style="width:150px;"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>替代终端:</span>
						</td>
						<td style="text-align: left;">
							<input  id=replaceCode
									name="stoTerminal.replaceCode"
									value="${stoTerminal.replaceCode }"
									editable="false"
									missingMessage="请填写替代终端"/>
									<a href="javascript:delSelectedData('replaceCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>显示人数:</span>
						</td>
						<td style="text-align: left;">
							<input id="showNum" class="easyui-numberspinner" 
									name="stoTerminal.showNum"
									value="${stoTerminal.showNum }"
									style="width:150px;"   
								 data-options="required:true,min:1,max:99"/>  
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>显示刷新间隔(秒):</span>
						</td>
						<td style="text-align: left;">
							<input id="refreshinterval2" class="easyui-numberspinner" 
								name="stoTerminal.refreshInterval2"
								value="${stoTerminal.refreshInterval2}"
								style="width:150px;"   
								data-options="required:true,min:1,max:9999"/>    
						</td>
					</tr>
					<input type="hidden" id="sendWindowName" name = "stoTerminal.sendWindowName" value="${stoTerminal.sendWindowName}"/>
					<tr id="stoTerminalProperty" style="display: none">
						<td class="honry-lable">
							<span>发药窗口:</span>
						</td>
						<td style="text-align: left;">
							<input  id=sendWindow
									name="stoTerminal.sendWindow"
									value="${stoTerminal.sendWindow}"
									missingMessage="请填写发药窗口" 
									editable="false"
									style="width:150px;"/>
							<a href="javascript:delSelectedData('sendWindow','sendWindowName');"  class="easyui-linkbutton" 
							data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>指定IP:</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id='loginIp'
									name="stoTerminal.loginIp"
									value="${stoTerminal.loginIp }"
									missingMessage="请填写指定IP" data-options="required:true,validType:'checkIp'"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							备注:
						</td>
						<td class="honry-info">
							<textarea class="easyui-validatebox" rows="2" cols="32" id="mark" name="stoTerminal.mark"
								data-options="multiline:true" maxlength="30" style="width:220;height:80">${stoTerminal.mark}</textarea>
						</td>
					</tr>
				</table>
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">保存</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
				</shiro:hasPermission>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	
	$(function(){
		$.extend($.fn.validatebox.defaults.rules, {           
	        checkIp : {// 验证IP地址  
	            validator : function(value) {  
	                var reg = /^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/ ;  
	                return reg.test(value);  
	            },  
	            message : 'IP地址格式不正确'  
	    }  
	});
		var stoTerminalType=$('#stoTerminalType').val();
		//打印类型
		$('#printType').combobox({    
			data:[{id:'0',value:'清单'},{id:'1',value:'标签'},{id:'2',value:'扩展'}],
			valueField:'id',    
			textField:'value'
		});
		
		/* $('#code').numberbox({
		    min:1,
		    max:999,
		    required:true,
		    formatter:function(num){
		    	if(num !='' && num != null){
		    		var fill=2;
			    	var len = ('' + num).length;
		    	    return (Array(fill > len ? fill - len + 1 || 0 : 0).join(0) + num);
		    	}
		    }
		});  */
		
		//性质
		$('#property').combobox({    
			//data:[{id:'1',value:'药品'},{id:'2',value:'专科'},{id:'3',value:'结算类别'},{id:'4',value:'特定收费窗口'},{id:'5',value:'挂号级别'}],
			data:[{id:'0',value:'普通'},{id:'1',value:'专科'},{id:'2',value:'特殊'}],
			valueField:'id',    
			textField:'value'
		});
		//是否自动打印
		$('#autopringFlag').combobox({    
			data:[{id:'0',value:'不自动打印配药标签'},{id:'1',value:'自动打印配药标签'}],
			valueField:'id',    
			textField:'value'
		});
		//是否关闭		
		$('#closeFlag').combobox({    
			data:[{id:'0',value:'开放'},{id:'1',value:'关闭'}],
			valueField:'id',    
			textField:'value'
		});
		//替代终端
		$('#replaceCode').combobox({    
		    url:'<%=basePath%>drug/pharmacyManagement/queryReplaceCode.action?type='+stoTerminalType+'&pid='+window.parent.window.getSelected(2),    
		    valueField:'code',    
		    textField:'name'
		}); 
		bindEnterEvent('replaceCode',popWinToReplaceCode,'easyui');//绑定替代终端回车事件
		//判断当前所选则的tab   0-发药窗口   1-配药台
		if(stoTerminalType==1){
			$('#sendWindow').combobox({    
			    url:'<%=basePath%>drug/pharmacyManagement/queryReplaceCode.action?type=0&pid='+window.parent.window.getSelected(2),    
			    valueField:'code',    
			    textField:'name',
			    required:true,
			    onSelect: function(record){
			    	$('#sendWindowName').val(record.name);
			    }
			}); 
			bindEnterEvent('sendWindow',popWinToSendWindow,'easyui');//绑定替代终端回车事件
			$('#stoTerminalProperty').show();
			$('#stotype').val('配药台');
		}else{
			$('#stotype').val('发药窗口');
			$('#stoTerminalAuto').hide();
			$('#autopringFlag').combobox({    
				required:false
			});
		}
		
		$('#deptCode').val(window.parent.window.getSelected(2));
// 		$('#deptCode').combobox.('setValue',window.parent.window.getSelected(1));
	});
	
	//确定添加
	function onClickOKbtn() {
		$('#addDForm').form('submit', {
				url : "<%=basePath%>drug/pharmacyManagement/terminalAdd.action",
				onSubmit : function() {
					if(!$(this).form('validate')){
						$.messager.alert('友情提示','请确认信息完整!','warning');
						setTimeout(function(){$(".messager-body").window('close')},1500);
						return false;
					}
				},
				success : function(data) {
					data = $.parseJSON(data);
					if(data.mesCode == 1){
						if($('#stoTerminalid').val()==''){
							$.messager.alert('友情提示','添加成功！');
						}else{
							$.messager.alert('友情提示','修改成功！');
						}
						
						//添加成功后更新 渲染map
						$.ajax({
							url: '<%=basePath %>drug/pharmacyManagement/queryTerminalMap.action',
							type:'post',
							success: function(payData) {
								terminalMap = payData;
							}
						});
						
						listReload($('#stoTerminalType').val());
						closeLayout();
					}else if(data.mesCode == 0){
						$.messager.alert('友情提示',data.mesMesg,'warning');
					}else if(data.mesCode == 2){
						$.messager.alert('友情提示','终端名称已存在!','warning');
					}else if(data.mesCode == 3){
						$.messager.alert('友情提示','终端编码已存在!','warning');
					}else{
						$.messager.alert('友情提示','无法关闭该发药台，请先将该发药窗口下属配药台全部关闭或关联其他发药窗口！','warning');
					}
					setTimeout(function(){$(".messager-body").window('close')},1500);
				},
				error : function(data) {
					$.messager.show({
						title : '提示信息',
						msg : '添加失败!'
					});
				}
		});
	}
	//清除
	function clear(){
		$('#addDForm').form('clear');
	}
	
	/**
	* 回车弹出替代终端选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToReplaceCode(){
		var type=$("#stoTerminalType").val();
		var deptCode=$("#deptCode").combobox('getValue');
		var tempWinPath = "<%=basePath%>popWin/popWinTerminal/toTerminalPopWin.action?textId=replaceCode&deptCode="+deptCode+"&type="+type;
		window.open (tempWinPath,'newwindow',' left=150,top=100,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-210) 
	+',scrollbars,resizable=yes,toolbar=yes')
	}
	/**
	* 回车弹出发药选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToSendWindow(){
		var type=$("#stoTerminalType").val();
		if(type==1){
			var deptCode=$("#deptCode").combobox('getValue');
			var tempWinPath = "<%=basePath%>popWin/popWinTerminal/toTerminalPopWin.action?textId=sendWindow&deptCode="+deptCode+"&type="+0;
			window.open (tempWinPath,'newwindow',' left=150,top=100,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-210) 
		+',scrollbars,resizable=yes,toolbar=yes')
		}
		
	}
</script>
</body>
</html>
