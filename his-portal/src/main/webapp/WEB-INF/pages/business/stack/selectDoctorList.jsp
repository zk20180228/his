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
						<td style="width: 260px;">
							查询条件:&nbsp;&nbsp;<input class="easyui-textbox" id="queryDoc"  data-options="prompt:'姓名,拼音,五笔,科室,自定义,工作号'"  style="width: 230px;"/>
							<a href="javascript:void(0)"  onclick="searchDoc()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div style="padding: 0px 5px 5px 5px;">
				<input type="hidden" value="${id }" id="id" ></input>
				<input type="hidden" value="${flag}" id="flag" ></input>
				<table id="listDoctor" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/stack/getDoctor.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
							<th data-options="field:'id', width : '5%',hidden:true">id</th>
							<th data-options="field:'jobNo', width : '5%'">工作号</th>
							<th data-options="field:'name', width : '8%',align:'center'" >姓名</th>
							<th data-options="field:'deptId', width : '10%',formatter: function(value,row,index){
																						if(row.deptId){
																							return row.deptId.deptName;
																						}else{
																							return '';
																						}
																					},align:'center'" >科室</th>
							<th data-options="field:'sex' ,width :'3%',formatter:sexRend" >性别</th>
							<th data-options="field:'family', width : '8%',formatter:familyFamater,align:'center'" >民族</th>
							<th data-options="field:'birthday',formatter:formatDatebox, width : '8%',align:'center'" >出生日期</th>
							<th data-options="field:'education', width : '14%',formatter:degreeFamater,align:'center'" >学历</th>
							<th data-options="field:'post', width : '8%',formatter:dutiesFamater,align:'center'" >职务</th>
							<th data-options="field:'title', width : '8%',formatter:titleFamater,align:'center'" >职称</th>
							<th data-options="field:'mobile', width : '10%' ,align:'center'">电话</th> 
							<th data-options="field:'isExpert' ,formatter:formatCheckBox, width : '8%',align:'center'" >是否是专家</th>
							</tr>
					</thead>
				</table>
			</div>		
		</div>
		<script type="text/javascript">
			var nationalityList=null;
			var dutiesList=null;
			var drgreeList=null;
			var titleList=null;
				var sexMap=new Map();
			//加载页面
			$(function(){
				//性别渲染
					$.ajax({
						url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
						data:{"type":"sex"},
						type:'post',
						success: function(data) {
							var v = data;
							for(var i=0;i<v.length;i++){
								sexMap.put(v[i].encode,v[i].name);
							}
						}
					});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeNationality"},
					type:'post',
					success: function(nationalityData) {
						nationalityList = eval("("+ nationalityData +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeDuties"},
					type:'post',
					success: function(dutiesData) {
						dutiesList = eval("("+ dutiesData +")");
					}
				});
				$.ajax({
					url: "<c:url value='/comboBox.action'/>",
					data:{"str" : "CodeDegree"},
					type:'post',
					success: function(drgreeData) {
						drgreeList = eval("("+ drgreeData +")");
					}
				});
				$.ajax({
					url: "<c:url value='/queryTitle.action'/>",
					type:'post',
					success: function(titleData) {
						titleList = eval("("+ titleData +")");
					}
				});
				setTimeout(function(){
					$('#listDoctor').datagrid({
						pagination:true,
				   		pageSize:20,
				   		pageList:[20,30,50,80,100],
						onDblClickRow: function (rowIndex, rowData) {//双击查看
							$('#hiddenDoc').val(rowData.id);
							$('#showDoc').val(rowData.name);
							if($('#flag').val()==0){
								closeDialogUser('selectDoctor');
							}else{
								closeDialogUser('selectDoctorUpdate');
							}
						}    
					});
	            },1);
				
			});
			function sexRend(value,row,index){
				return sexMap.get(value);
	   		//查询
	   		function searchDoc(){
	   		    var queryName =$('#queryDoc').textbox('getValue');
			    $('#listDoctor').datagrid('load', {
			    	name: queryName
				});
			}	
			/**
			 * 格式化复选框
			 */
			function formatCheckBox(val,row){
				if (val == 0){
					return '否';
				} else {
					return '是';
				}
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
			//显示民族格式化
			function familyFamater(value){
				if(value!=null){
					for(var i=0;i<nationalityList.length;i++){
						if(value==nationalityList[i].id){
							return nationalityList[i].name;
						}
					}	
				}
			}
			//显示职位格式化
			function dutiesFamater(value){
				if(value!=null){
					for(var i=0;i<dutiesList.length;i++){
						if(value==dutiesList[i].id){
							return dutiesList[i].name;
						}
					}	
				}
			}
			//显示学历格式化
			function degreeFamater(value){
				if(value!=null){
					for(var i=0;i<drgreeList.length;i++){
						if(value==drgreeList[i].id){
							return drgreeList[i].name;
						}
					}	
				}
			}
			//显示职称格式化
			function titleFamater(value){
				var retVal = "";
				if(value!=null){
					for(var i=0;i<titleList.length;i++){
						if(value==titleList[i].id){
							retVal = titleList[i].name;
							break;
						}
					}	
				}
				return retVal;
			}
			/**
			 * 格式化生日
			 * @author  zpty
			 * @date 2015-6-19 9:25
			 * @version 1.0
			 */
			function formatDatebox(value) {
	            if (value == null || value == '') {
	                return '';
	            }
	            var dt;
	            if (value instanceof Date) {
	                dt = value;
	            } else {

	                dt = new Date(value);

	            }
	            return dt.format("yyyy-MM-dd"); //扩展的Date的format方法
	        }
			 //扩展的Date的format方法
			Date.prototype.format = function(format) {
		        var o = {
		            "M+": this.getMonth() + 1, // month
		            "d+": this.getDate(), // day
		            "h+": this.getHours(), // hour
		            "m+": this.getMinutes(), // minute
		            "s+": this.getSeconds(), // second
		            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
		            "S": this.getMilliseconds()
		        };
		        if (/(y+)/.test(format))
		            format = format.replace(RegExp.$1, (this.getFullYear() + "")
		                .substr(4 - RegExp.$1.length));
		        for (var k in o)
		            if (new RegExp("(" + k + ")").test(format))
		                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		        return format;
		    };
		</script>
	</body>
</html>