<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
			<div style="padding:5px 5px 0px 5px;">
				<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
					<tr>
						<td style="width: 100%;">
							查询条件:&nbsp;&nbsp;<input type="text" ID="encode" name="patientName"  placeHolder="患者名称,患者病历号,医保手册号" style="width: 200px;"/>
							卡类型:&nbsp;&nbsp;<input ID="idcardTypeSerc" name="idcardType" class="easyui-combobox" data-options="valueField: 'value',textField: 'label',data: [{label: '磁卡',value: '磁卡'},{label: 'IC卡',value: 'IC卡'}]" onkeydown="KeyDown()"/>
							<a href="javascript:void(0)"  class="easyui-linkbutton" iconCls="icon-search" onclick="searchPatient()">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div style="padding: 0px 5px 5px 5px;">
				<input type="hidden" value="${id }" id="id" ></input>
				<table id="listPatient" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/patient/patientBlack/queryIdcard.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true, width : '10%'"></th>
							<th data-options="field:'patientName',formatter:function(value,row,index){
								if (row.patient){
									return row.patient.patientName;
								} else {
									return value;
								}
							}, width : '8%'" >
							患者名称</th>
							<th data-options="field:'patientHandbook',formatter:function(value,row,index){
								if (row.patient){
									return row.patient.patientHandbook;
								} else {
									return value;
								}
							}, width : '12%'">医保手册号</th>
							<th data-options="field:'patientSex',formatter:sexFamater" >性别</th>
							<th data-options="field:'patientPhone',formatter:function(value,row,index){
									if (row.patient){
										return row.patient.patientPhone;
									} else {
										return value;
									}
								} , width : '14%'" >
								电话
							</th>
							<th data-options="field:'patientMother',formatter:function(value,row,index){
									if (row.patient){
										return row.patient.patientMother;
									} else {
										return value;
									}
								} , width : '8%'">
								联系人
							</th>
							<th data-options="field:'idcardNo', width : '12%'">卡号</th>
							<th data-options="field:'medicalrecordId', width : '12%'">病历号</th>
							<th data-options="field:'idcardCreatetime', width : '10%'">建卡时间</th>
							<th data-options="field:'idcardType',formatter:idcardTypeFamater, width : '5%'">卡类型</th>
						</tr>
					</thead>
				</table>
			</div>		
		</div>
		<script type="text/javascript">
			var sexList = "";
			var countryList = "";	
			var certiList = "";
			var nationList = "";
			var marryList = "";
			var occuList = "";	
			var relationList = "";
			var idcardTypeList = "";
			//加载页面
			$(function(){
				//解析编码卡类型xml文件
				idCombobox("idcardTypeSerc","CodeIdcardType");
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeSex"},
					type:'post',
					success: function(sexdata) {
						sexList = eval("("+ sexdata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeCountry"},
					type:'post',
					success: function(countrydata) {
						countryList = eval("("+ countrydata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeCertificate"},
					type:'post',
					success: function(certidata) {
						certiList = eval("("+ certidata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeNationality"},
					type:'post',
					success: function(nationdata) {
						nationList = eval("("+ nationdata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeMarry"},
					type:'post',
					success: function(marrydata) {
						marryList = eval("("+ marrydata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeOccupation"},
					type:'post',
					success: function(occudata) {
						occuList = eval("("+ occudata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeRelation"},
					type:'post',
					success: function(relationdata) {
						relationList = eval("("+ relationdata +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeIdcardType"},
					type:'post',
					success: function(idcardTypedata) {
						idcardTypeList = eval("("+ idcardTypedata +")");
					}
				});
				//加缓冲时间1毫秒
				setTimeout(function(){
					//添加datagrid事件及分页
					$('#listPatient').datagrid({
						pagination:true,
				   		pageSize:20,
				   		pageList:[20,30,50,80,100],
						onDblClickRow: function (rowIndex, rowData) {//双击查看
							$('#medicalrecordId').val(rowData.medicalrecordId);
							$('#patientName').val(rowData.patient.patientName);
							$('#patientId').val(rowData.patient.id);
							closeDialog('patientBlackDiv');
						}    
					});
	            },1);
				
			});
	   		//查询
	   		function searchPatient(){
				var idcardTypeSerc = $('#idcardTypeSerc').combobox('getValue');
				var encode = $('#encode').val();
				$('#listPatient').datagrid('load', {
					idcardType : idcardTypeSerc,
					patientName : encode	
				});
			}	
			/**
			 * 回车键查询
			 * @author 
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-27
			 * @version 1.0
			 */
			function KeyDown()  
			{  
			    if (event.keyCode == 13)  
			    {  
			        event.returnValue=false;  
			        event.cancel = true;  
			        searchFromUser();  
			    }  
			} 
			/**
			 * 性别列表页 显示
			 */
			function sexFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<sexList.length;i++){
							if(row.patient.patientSex==sexList[i].id){
								return sexList[i].name;
							}
						}	
					}
				}
			}
			/**
			 * 国籍列表页 显示
			 */
			function countryFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<countryList.length;i++){
							if(row.patient.patientNationality==countryList[i].id){
								return countryList[i].name;
							}
						}
					}
				}	
			}
						/**
			 * 证件类型列表页 显示
			 */
			function certiFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<certiList.length;i++){
							if(row.patient.patientCertificatestype==certiList[i].id){
								return certiList[i].name;
							}
						}
					}
				}
					
			}
			/** 民族列表页 显示
			 */
			function nationFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<nationList.length;i++){
							if(row.patient.patientNation==nationList[i].id){
								return nationList[i].name;
							}
						}
					}
				}	
			}
			/** 婚姻列表页 显示
			 */
			function marryFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<marryList.length;i++){
							if(row.patient.patientWarriage==marryList[i].id){
								return marryList[i].name;
							}
						}
					}
				}	
			}
			/** 职业列表页 显示
			 */
			function occuFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<occuList.length;i++){
							if(row.patient.patientOccupation==occuList[i].id){
								return occuList[i].name;
							}
						}
					}
				}	
			}
			/** 关系列表页 显示
			 */
			function relationFamater(value,row,index){
				if(row!=null){
					if(row.patient!=null){
						for(var i=0;i<relationList.length;i++){
							if(row.patient.patientLinkrelation==relationList[i].id){
								return relationList[i].name;
							}
						}
					}
				}	
			}
			/** 卡类型列表页 显示
			 */
			function idcardTypeFamater(value,row,index){
				if(row!=null){
					for(var i=0;i<idcardTypeList.length;i++){
						if(row.idcardType==idcardTypeList[i].id){
							return idcardTypeList[i].name;
						}
					}
					
				}	
			}
			//从xml文件中解析，读到下拉框
			function idCombobox(id,name) {
				$('#' + id).combobox({
					url : "<c:url value='/comboBox.action'/>?str=" + name,
					valueField : 'id',
					textField : 'name',
					multiple : false
				});
			}
		</script>
	</body>
</html>