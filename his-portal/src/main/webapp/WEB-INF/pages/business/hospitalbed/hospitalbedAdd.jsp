<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">
	<title>添加页面</title>
</head>
	<body style="margin: 0px; padding:0px">
		<div id="p" class="easyui-panel" title="添加" style="padding: 5px; background: #fafafa;" data-options="fit:'true',border:true">
			<form id="hospitalForm" action="" method="post">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
				<input type="hidden" name="did" id="did"/>
				<input type="hidden" name="pid" id="pid"/>
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
							<input id="nursestation" class="easyui-textbox" data-options="editable:false" style="width: 200px" value="" name="nursestation1">
							<input id="nursestation1" type="hidden" name="nursestation">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>病房编号:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="department" name="getBedwardId" value="${hospitalbed.businessBedward.bedwardName }" class="easyui-combobox" data-options="required:true,url:'<%=basePath%>baseinfo/hospitalbed/searchBedward.action?treeId='+getSelected(3)+'&Parent='+getSelected(2)+'&node='+$('#node').val(),valueField:'id',textField:'bedwardName'" hidePanel="hidePanel" style="width: 200px"/>
							<input id="department1" name="getBedwardName" value="${hospitalbed.businessBedward.bedwardName }" type="hidden"
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
									data-options="required:true"
									style="width: 200px" />
						</td>
					</tr>
					<input id="nurseCellCode" name="hospitalbed.nurseCellCode" type="hidden">
					<input id="nurseCellName" name="hospitalbed.nurseCellName" type="hidden">
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
									editable="false"
									style="width: 200px"/>
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
									data-options="required:true"
									style="width: 200px"/>
						</td>
					</tr>
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
							<input class="easyui-textbox" type="text" id ="bedPhone" value="${hospitalbed.bedPhone }" name="hospitalbed.bedPhone" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>归属:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" id ="bedBelong" value="${hospitalbed.bedBelong }" name="hospitalbed.bedBelong" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>费用:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cost" class="easyui-numberbox" type="text"  value="${hospitalbed.bedFee }" name="hospitalbed.bedFee"  style="width: 200px"></input>
						</td>
					</tr>
					<tr  style="display: none">
						<td class="honry-lable">
							<span>排序:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox"  value="${hospitalbed.bedOrder }" name="hospitalbed.bedOrder"  style="width: 200px"></input>
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<a id="pltj" href="javascript:void(0)" style="height:26px;width:90px;" data-options="iconCls:'icon-save'" class="easyui-linkbutton"><span id="pl">批量添加</span></a>
				<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" class="easyui-linkbutton">确定</a>
				<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>
	<script type="text/javascript">
	var bedNoAdd ="";
	var bedName ="";
	var more=false;
	var nurseCellCode = $('#node').val();
	var nurseCellName = "";
    var nursestation ="";
	$(function(){
		//床位状态
		$('#bedState').combobox({    
			url:  "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
			valueField : 'encode',
			textField : 'name'
		});
		//床位编制
		$('#bedOrgan').combobox({    
			url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=badorgan",
			valueField:'encode',    
			textField:'name'
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
		
		//判断床号
		if (addAndEdit == 0) {
			$("#bedNO").attr("disabled",false);
		}else{
			$("#bedNO").attr("disabled",true);
		}
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
		
		$.ajax({
			url: "<%=basePath%>baseinfo/hospitalbed/searchursestation.action?Parent="+getSelected(2)+"&node="+$('#node').val(),
			type:'post',
			success: function(data){
				nurseCellName = data;
				$('#nursestation').textbox('setValue',data);
				if(getSelected(2)=='1'){
					nursestation = $('#did').val();
					$('#nursestation1').val(nursestation);
				}else{
					nursestation =getSelected(2);
					$('#nursestation1').val(nursestation);
				}
				
			}
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
				$('#pl').text('批量添加');
			}
		});
		
		//批量添加
		$('#pltj').click(function(){
				more = true;
				onClickOKbtn();
// 			if($("#cheque1").is(":visible")){
// 				$("#cheque1").hide();
// 				$('#pl').text('批量添加');
// 				$('#ss').numberspinner({ 
// 				   value: 1,    
// 				});
// 			}else{
// 				$("#cheque1").show();
// 				$('#pl').text('单个添加');
// 			}
			
		});
		$('#hospitalOKbtn').click(function(){
				more = false;
				onClickOKbtn();
		});
	});
	
	function onClickOKbtn() {
		bedNoAdd = $('#bedNoAdd').numberbox('getValue');
		bedName = $('#bedNO').numberbox('getValue');
		var bedwardId = $('#department').combobox('getValue');
		nursestation = $('#nursestation1').val();
		if(bedNoAdd!=null&&bedNoAdd!=""){
			bedName+="+";
			bedName+=bedNoAdd;
		}
		//先判断病房是否存在
		$.ajax({
			url: "<%=basePath%>baseinfo/hospitalbed/isExistBedwardName.action",
			type:'post',
			data :{bedwardName:$('#department').combobox('getText'),nursestation:nursestation},
			success: function(data) {
				if(data!="Y"){
					$('#department').combobox('setValue', $('#department').combobox('getText'));
					bedwardId = data;
				}
				//增加
				//为床号设置唯一验证
				$.ajax({
					url: "<%=basePath%>baseinfo/hospitalbed/isExistBedName.action",
					type:'post',
					data :{bedName:bedName,bedwardId:bedwardId,nursestation:nursestation},
					dataType : 'json',
					success: function(data) {
						if(data=='Y'){
							$.messager.alert('提示','病床号已存在，请重新填写病床号！');
							setTimeout(function(){
		  						$(".messager-body").window('close');
		  					},3500);
							$('#bedNO').numberbox('clear');
							$('#bedNoAdd').numberbox('clear');
							return;
						}else{
							$('#nurseCellName').val(nurseCellName);
							$('#nurseCellCode').val(nursestation);
							$.messager.progress({text:'保存中，请稍后...',modal:true});							//提交表单
							$('#hospitalForm').form('submit', {
								url : '<%=basePath%>baseinfo/hospitalbed/addHospitalbed.action?bedNoAdd='+bedNoAdd,
	 							data :$('#hospitalForm').serialize(),
	 							dataType : 'json',
	 							onSubmit : function() {
	 								if (!$('#hospitalForm').form('validate')) {
	 									$.messager.alert('提示','验证没有通过,不能提交表单!'); 
	 									setTimeout(function(){
	 				  						$(".messager-body").window('close');
	 				  					},3500);
	 									$.messager.progress('close');
	 									return false;
	 								}
	 							},
	 							success : function(data) {
	 								$.messager.progress('close');
	 								//fundept();
	 								funlevel();
	 								funWard();
	 								trr();
	 								if(more==true){
	 									$('#list').datagrid('load', '<%=basePath%>baseinfo/hospitalbed/queryHospitalbed.action');
	 									$('#bedNoAdd').numberbox('clear');
	 									$('#cost').numberbox('clear');
	 									$('#bedNO').numberbox('clear');
	 									$('#bedOrgan').combobox('setValue','');
	 									$('#bedLevel').combobox('setValue','');
	 									$('#bedState').combobox('setValue','');
	 									$('#bedPhone').textbox('setValue','');
	 									$('#bedBelong').textbox('setValue','');
	 								}else if(more==false){
	 									$('#list').datagrid('load', '<%=basePath%>baseinfo/hospitalbed/queryHospitalbed.action');
		 								closeLayout();
		 								$.messager.alert('提示','添加成功!');
		 								setTimeout(function(){
		 			  						$(".messager-body").window('close');
		 			  					},3500); 
		 								$('#node').val('');
		 								$('#isdept').val('');
		 							}
	 							},
	 							error : function(data) {
	 								$.messager.alert('提示','添加失败!');  
		 						 }
							})
						}
					}
				});
			}
		});
	}
	//清除所填信息
	function clear() {
		$('#hospitalForm').form('clear');
	}
</script>
	</body>
</html>
