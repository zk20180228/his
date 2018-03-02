<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
	<title>修改页面</title>
</head>
<body  style="margin: 0px; padding:0px">
	<div id="p" class="easyui-panel" title="修改" style="padding: 5px; background: #fafafa;" data-options="fit:'true',border:true">
		<form id="hospitalForm" action="" method="post">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
<!-- 				<span>点击树节点id</span> -->
			<input type="hidden" name="did" id="did"/>
<!-- 				<span>点击树节点父id</span> -->
			<input type="hidden" name="pid" id="pid"/>
<!-- 				<span>修改数据id</span> -->
			<input type="hidden" id="hospitalbedid" name="hospitalbed.id" value="${ hospitalbed.id}"/>
			<input type="hidden" name="hospitalbed.createUser" value="${hospitalbed.createUser }"/>
			<input type="hidden" name="hospitalbed.createDept" value="${hospitalbed.createDept }"/>
			<input type="hidden" name="hospitalbed.createTime" value="${hospitalbed.createTime }"/>
				<tr style="display: none">
					<td class="honry-lable">
						<span>医院编号:</span>
					</td>
					<td class="honry-view">
						<input id="hospitalID" value="1"  style="width: 200px" name="getHospitalId">
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>病区:</span>&nbsp;&nbsp;
					</td>
					<td class="honry-view">
						<input id="nursestation" class="easyui-textbox" value="${hospitalbed.nurseCellName}" data-options="editable:false" style="width: 200px" name="nursestation1">
						<input id="nursestation1" type="hidden" value="${hospitalbed.nurseCellCode}" name="nursestation">
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>病房编号:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input id="department" name="getBedwardId" readonly ="readonly" value="${hospitalbed.businessBedward.bedwardName }" class="easyui-combobox"
<%-- 						<input id="department" name="getBedwardId" value="${hospitalbed.businessBedward.id }" class="easyui-combobox"  --%>
								missingMessage="请选择病房编号" 
								data-options="required:true"
								style="width: 200px"
								/>
						<input id="department1"  value="${hospitalbed.businessBedward.id }" type="hidden"
								/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>床位编制:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input class="easyui-combobox" id=bedOrgan
								name="hospitalbed.bedOrgan"
								missingMessage="请选择床位编制" 
								editable="false"
								value="${hospitalbed.bedOrgan }"
								data-options="required:true"
								style="width: 200px"
								/>
					</td>
				</tr>
				<tr id="cheque1" style="display: none">
					<td class="honry-lable">
						<span>数量:</span>&nbsp;&nbsp;
					</td>
					<td >
						<input  id="ss" name="wardNum" value='1' style="width:180px;">  
					</td>
				</tr>
				
				
				<tr>
					<td class="honry-lable">
						<span>床位等级:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input class="easyui-combobox" id=bedLevel
								name="hospitalbed.bedLevel"
								missingMessage="请选择床位等级" 
								data-options="required:true"
								value="${hospitalbed.bedLevel }"
								editable="false"
								style="width: 200px"
								/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>床位状态:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input class="easyui-combobox" id=bedState
								name="hospitalbed.bedState"
								missingMessage="请选择床位状态" 
								editable="false"
									value="${hospitalbed.bedState }"
								data-options="required:true"
								style="width: 200px"
								/>
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="honry-lable"> -->
<!-- 						<span style="font-size: 13">床号:</span>&nbsp;&nbsp; -->
<!-- 					</td> -->
<!-- 					<td style="text-align: left;"> -->
<%-- 						<input id="bedNO" class="easyui-textbox" value="${hospitalbed.bedName }"  --%>
<!-- 							name="hospitalbed.bedName" data-options="required:true"  style="width: 200px"></input> -->
<!-- 						<input id="serial"  style="width: 200px"></input> --> 
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
						<td class="honry-lable">
							<span>床号:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="bedNO" class="easyui-numberbox" value="${hospitalbed.bedName }" 
								name="hospitalbed.bedName" data-options="required:true"  style="width: 200px"></input>
								<span id="add1" style="display:none;">+&nbsp;<input id="bedNoAdd" class="easyui-numberbox"  style="width: 85px"></input></span>
						</td>
					</tr>
				<tr>
					<td class="honry-lable">
						<span>床位电话:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input class="easyui-textbox" type="text" value="${hospitalbed.bedPhone }" name="hospitalbed.bedPhone" style="width: 200px"></input>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>归属:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input class="easyui-textbox" type="text" value="${hospitalbed.bedBelong }" name="hospitalbed.bedBelong" style="width: 200px"></input>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>费用:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input id="cost" class="easyui-textbox" type="text" value="${hospitalbed.bedFee }" name="hospitalbed.bedFee" style="width: 200px"></input>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span>排序:</span>&nbsp;&nbsp;
					</td>
					<td style="text-align: left;">
						<input class="easyui-textbox" type="text" value="${hospitalbed.bedOrder }" name="hospitalbed.bedOrder" style="width: 200px"></input>
					</td>
				</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable"> -->
<!-- 							<span style="font-size: 13">当前病人编号:</span>&nbsp;&nbsp; -->
<!-- 						</td> -->
<!-- 						<td style="text-align: left;"> -->
<%-- 							<input class="easyui-textbox" type="text" value="${hospitalbed.patientId }" name="hospitalbed.patientId" style="width: 200px"></input> --%>
<!-- 						</td> -->
<!-- 					</tr> -->
			</table>
		</form>
		<div style="text-align: center; padding: 5px">
			<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确认修改</a>
			<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
		</div>
	</div>
	<script type="text/javascript">
	var bedNoAdd ="";
	var bedName ="";
	$(function(){
		//床位状态
		$('#bedState').combobox({    
			url:  "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
			valueField:'encode',    
			textField:'name'
		});
		//床位编制
		$('#bedOrgan').combobox({    
			url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=badorgan",
			valueField:'encode',    
			textField:'name'
		});
		if("${hospitalbed.bedOrgan }"=='2'){
			$('#bedNO').numberbox({
				width:90
			});
			$('#bedNo').numberbox('clear');
			$('#add1').show();
			$('#bedNoAdd').numberbox({
				required:true
			});
			$('#bedNoAdd').numberbox('clear');
			var data = ("${hospitalbed.bedName }" ).split("+");
			$('#bedNo').numberbox('setValue',data[0]);
			$('#bedNoAdd').numberbox('setValue',data[1]);
		}
		
		//初始化病房编号
		$('#department').combobox({ 
			url: "<%=basePath%>baseinfo/hospitalbed/queryWardlList.action?hospitalbedid="+$('#hospitalbedid').val(),
			valueField:'id',    
			textField:'bedwardName'
// 			multiple:false
		});
			
		$('#cost').numberbox({
			min : 0,
			precision : 2
		});
		
		$('#did').val($('#node').val());
		$('#pid').val(getSelected(2));	
		
		//批量添加 数量框初始化
		$('#ss').numberspinner({ 
		    min: 1,    
		    max: 50
		});
		
		//初始化床位等级
		$.ajax({
			url: "<%=basePath%>baseinfo/hospitalbed/queryBedLevelList.action",
			type:'post',
			success: function(data) {
				//床位等级（下拉框）
				$('#bedLevel').combobox({ 
					data:data,
					valueField:'encode',    
					textField:'name',
					multiple:false
				});
			}
		});
		//判断床位编制是否为加床
		$('#bedOrgan').combobox({
			onSelect:function(record){
				var bedOrgan =$('#bedOrgan').combobox('getValue');
				if(bedOrgan=='2'){
					$('#bedNO').numberbox({
						width:90
					});
					$('#bedNo').numberbox('clear');
					$('#add1').show();
					$('#bedNoAdd').numberbox({
						required:true
					});
					$('#bedNoAdd').numberbox('clear');
					
					
				}else{
					$('#bedNoAdd').numberbox('clear');
					$('#add1').hide();
					$('#bedNoAdd').numberbox({
						required:false
					});
					$('#bedNO').numberbox('clear');
					$('#bedNO').numberbox({
						width:200
					});
					
				}
			}
		});
		
	});
	
	function onClickOKbtn(){
		bedNoAdd = $('#bedNoAdd').numberbox('getValue');
		bedName = $('#bedNO').numberbox('getValue');
		var bedwardId = $('#department1').val();
		var nursestation =$('#nursestation1').val();
		if(bedNoAdd!=null&&bedNoAdd!=""){
			bedName+="+";
			bedName+=bedNoAdd;
		}
		var flag=false;
		if(bedName!="${hospitalbed.bedName }"){
			$.ajax({
				url: "<%=basePath%>baseinfo/hospitalbed/isExistBedName.action",
				type:'post',
// 				data : $('#hospitalForm').serialize(),
				data :{bedName:bedName,bedwardId:bedwardId,nursestation:nursestation},
				dataType : 'json',
				async:false,
				success: function(data) {
					if(data=="Y"){
						$.messager.alert('提示','部分病床号以存在，请重新填写病床号！');
						setTimeout(function(){
	  						$(".messager-body").window('close');
	  					},3500);
						flag = true;
					}
				}
			})	
		}
		 if(flag){
	        return false;
		 }
		$.ajax({
			url: "<%=basePath%>baseinfo/hospitalbed/queryBedName.action?id="+bedwardId,
			async:false,
			type:'post',
			dataType:'json',
			success: function(data) {
				if(data=="N"){//病房号不存在
					//设置value值
						$('#department').combobox('setValue', $('#department').combobox('getText'));
				}
				$('#hospitalForm').form('submit', {
					<%-- url : '<%=basePath%>baseinfo/hospitalbed/editHospitalbed.action?id='+$('#hospitalbedid').val(), --%>
					url : '<%=basePath%>baseinfo/hospitalbed/addHospitalbed.action?bedNoAdd='+bedNoAdd,
					data : $('#hospitalForm').serialize(),
					dataType : 'json',
					onSubmit : function() {
						if (!$('#hospitalForm').form('validate')) {
							$.messager.alert('提示','验证没有通过,不能提交表单!');  
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
					},
					success : function(data) {
						$('#list').datagrid('unselectAll');
						//fundept();
						funlevel();
						funWard();
						trr();
// 								alert("加载");
						$('#list').datagrid('load', '<%=basePath%>baseinfo/hospitalbed/queryHospitalbed.action');
						closeLayout();
						clear();
						closeLayout();
						$.messager.alert('提示','修改成功!'); 
					},
					error : function(data) {
						$.messager.alert('提示','修改失败!'); 
					}
				})
				
			}
			
		})
	}
	//清除所填信息
	function clear() {
		$('#hospitalForm').form('clear');
	}
</script>
</body>
</html>
