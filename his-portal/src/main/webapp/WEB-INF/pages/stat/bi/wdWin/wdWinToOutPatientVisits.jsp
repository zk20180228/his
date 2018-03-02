<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body >
	<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: auto;">
					<div style="padding: 5px 5px 0px 5px;">
						<form id="search" method="post">
							<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style='overflow:scroll;'>
								<tr>
								    <td align="left" style="border-right: none;width: 30%;" id="msgTd">
										&nbsp;&nbsp;
									</td>
									<td align="right" style="border-left: none;">
										&nbsp;&nbsp; <a href="javascript:confirmWd();" class="easyui-linkbutton" >确定</a>
										&nbsp;&nbsp; <a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
									</td>
								</tr>
								<tr>
								 <td style="width: 320px;height: 40px;" class="honry-lable" style="width: 5%;" nowrap="nowrap">年度
								 </td>
								<td style="width: 320px;height: 40px;" class="honry-info" nowrap="nowrap">
										&nbsp;<input class='easyui-textbox' style='width:145px;' name='year' id="yearTdId1" />
										&nbsp;至&nbsp;<input class='easyui-textbox' style='width:145px;' name='year' id="yearTdId2" />
								</td>
								</tr>
								<tr>
									<td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>季度
									</td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									    &nbsp;<input class='easyui-combobox' style='width:145px;' id='seasonTdId1' name='season'  value=''  />
									 &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='seasonTdId2' name='season'  value=''  />
										</td>
									 	</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>月度
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									 &nbsp;<input class='easyui-combobox' style='width:145px;' id='monthTdId1' name='month'  value=''  />
								        &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='monthTdId2' name='month'  value=''  />
									  </td>
									 	</tr>
									 	<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>日
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap' id='tmpTdToDay'>
									   &nbsp;<input class='easyui-combobox' style='width:145px;' id='dayTdId1'   value=''  />
									   &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='dayTdId2'   value=''  />
									 </td>
									 </tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>科室
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="deptTdId" class="easyui-combotree" value="" name="dept_code" style='width:200px;'></input>
								     </td>
									</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>地域
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="placeTdId" class="easyui-combotree" value="" name="patient_birthplace" style='width:200px;'></input>
								     </td>
									</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>患者年龄
									 </td>
									  <td style='width: 320px;' class='honry-info' nowrap='nowrap' id="tmpAgeTd">
									  <span>
									   &nbsp;<input class="easyui-textbox"   style='width:145px;' name='patient_age' id='patient_ageTdId1'  />
									   &nbsp;至&nbsp;<input class="easyui-textbox"   style='width:145px;' name='patient_age' id='patient_ageTdId2'  />&nbsp;<select id="patient_ageComboxbox" class="easyui-combobox"  style="width:50px;">   
    																																<option value="岁">岁</option>   
   												 																					<option value="月">月</option>  
   												 																					<option value="天">天</option> 
									 																								</select>
									  <a href="javascript:cloneAge();" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>
									  </span>
								     </td>
									</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>性别
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="sexTdId" class="easyui-combobox" value="" name="patient_sex" style='width:200px;'></input>
								     </td>
									</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
</div>
</body>
	<script type="text/javascript">
	var deptCodeMap = new Map();
	    function validateNum(tmpVal){//非零开头的4位有效数字  "^([1-9][0-9]{0,3})$  1-4位
	    	var reg = new RegExp("^([1-9][0-9]{3})$");//四位整数  
	    	return reg.test(tmpVal)
	    }
	    function validateNumForAge(tmpVal){//非零开头的3位有效数字  ^([0-9][0-9]{0,2}[\.]?[0-9]{1})$  100.5
	    	var reg = new RegExp("^([1-9][0-9]{0,2})$");//1-3为有效数字
	    	return reg.test(tmpVal)
	    }
	    function isNullStr( str ){
	    	if ( str == "" ) return true;
	    	var regu = "^[ ]+$";
	    	var re = new RegExp(regu);
	    	return re.test(str);
	    	}
	    //hedong  20160811 绑定相关事件及下拉数据源
		$(function(){
			// 文本框 失去焦点事件
			$("input",$("#yearTdId1").next("span")).blur(function()  
			{  
				if($("#yearTdId1").val()){
					if(!validateNum($("#yearTdId1").val())){
						 $("#yearTdId1").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>年度只能为非0开头的4位有效整数！</font>"); 
						 $("#yearTdId1").next("span").children().first().focus();
						 return;
					  }
				}
		    });
			
		   $("#yearTdId2").next("span").children().first().focus(function(){
			   $("#msgTd").html("&nbsp;");
			   if(!$("#yearTdId1").val()){//如果起始年度没有值则不能输入截止值
				   $("#msgTd").append("<font color='red'>请先输入起始年度！</font>");
				   $("#yearTdId1").next("span").children().first().focus();
				   $("#yearTdId2").val("");
			   }
			})
			$("#yearTdId2").next("span").children().first().blur(function(){
			    if($("#yearTdId2").val()){
			    	if(!validateNum($("#yearTdId2").val())){
						 $("#yearTdId2").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>年度只能为非0开头的4位有效整数！</font>"); 
						 return;
					 }
				     if($("#yearTdId2").val()<=$("#yearTdId1").val()){
				    	 $("#yearTdId2").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>截止年度不能小于或等于起始年度！</font>"); 
				     }
			    } 
				
			})
			
			$("input",$("#patient_ageTdId1").next("span")).blur(function()  
			{  
				if($("#patient_ageTdId1").val()){
					if(!validateNumForAge($("#patient_ageTdId1").val())){
						 $("#patient_ageTdId1").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>患者年龄只能为3位有效整数！</font>"); 
						 $("#patient_ageTdId1").next("span").children().first().focus();
						 return;
				     }
					
					  if($("#patient_ageTdId1").val()){
					      if(dimensionString==""){
			               	dimensionString="patient_age,患者年龄";
			               }else if(dimensionString.indexOf("patient_age")==-1){
			               	dimensionString=dimensionString+",patient_age,患者年龄";
			               }
				     }
				}else{
					dimensionStringToAge();
				}
		    });
			
		   $("#patient_ageTdId1").next("span").children().first().focus(function(){
			   $("#msgTd").html("&nbsp;");
			})
		   
		   $("#patient_ageTdId2").next("span").children().first().focus(function(){
			   $("#msgTd").html("&nbsp;");
			   if(!$("#patient_ageTdId1").val()){//如果起始年度没有值则不能输入截止值
				   $("#msgTd").append("<font color='red'>请先输入起始患者年龄！</font>");
				   $("#patient_ageTdId1").next("span").children().first().focus();
				   $("#patient_ageTdId2").val("");
			   }
			})
			$("#patient_ageTdId2").next("span").children().first().blur(function(){
			    if($("#patient_ageTdId2").val()){
			    	if(!validateNumForAge($("#patient_ageTdId2").val())){
						 $("#patient_ageTdId2").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>患者年龄只能为3位有效整数！</font>"); 
						 return;
					 }
				     if($("#patient_ageTdId2").val()<=$("#patient_ageTdId1").val()){
				    	 $("#patient_ageTdId2").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>截止患者年龄不能小于或等于起始患者年龄！</font>"); 
				     }
			    }else{
			    	dimensionStringToAge();
			    } 
				
			})
			
		   
		   $('#seasonTdId1').combobox({
		        data:[{    
		            "id":1,    
		            "name":"第一季度"
		        },{    
		            "id":2,    
		            "name":"第二季度"   
		        },{    
		            "id":3,    
		            "name":"第三季度"   
		        },{    
		            "id":4,    
		            "name":"第四季度"   
		        }],
			    valueField: 'id',    
		        textField: 'name',
		        editable:false,
		        onSelect:function(node){
		        	if($("#yearTdId1").val()){
			    		$("#msgTd").html("&nbsp;");
			        	//选择季度时 月度值 周   日 清空
			        	clearSelectVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2'));
			    	}else{
			    		 $("#seasonTdId1").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
			    	}
		        }
			});
		   
		   $('#seasonTdId2').combobox({
		        data:[{    
		            "id":1,    
		            "name":"第一季度"
		        },{    
		            "id":2,    
		            "name":"第二季度"   
		        },{    
		            "id":3,    
		            "name":"第三季度"   
		        },{    
		            "id":4,    
		            "name":"第四季度"   
		        }],
			    valueField: 'id',    
		        textField: 'name',
		        editable:false,
		        onSelect:function(node){
		        	if($("#yearTdId1").val()){
					      $("#msgTd").html("&nbsp;");
					      if($("#seasonTdId1").combobox("getValue")){
						    		if(parseInt(node.id)<=$("#seasonTdId1").combobox("getValue")){
						    			 $("#seasonTdId2").combobox("setValue",null);
						    			 $("#msgTd").html("&nbsp;");
										 $("#msgTd").append("<font color='red'>截止季度不能小于或等于起始季度！</font>"); 
						    		}else{
						    			$("#msgTd").html("&nbsp;");
							        	//选择季度时 月度值 周   日 清空
							        	clearSelectVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2'));
						    		}
				    	  }else{
				    		     $("#seasonTdId2").combobox("setValue",null);
				    		     $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>请先选择起始季度！</font>"); 
				    	  }
				    	}else{
				    		 $("#seasonTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
				    	}
		        }
			});
			
		   
		   $('#monthTdId1').combobox({
		        data:[{    
		            "id":1,    
		            "name":"1"
		        },{    
		            "id":2,    
		            "name":"2"   
		        },{    
		            "id":3,    
		            "name":"3"   
		        },{    
		            "id":4,    
		            "name":"4"   
		        },{    
		            "id":5,    
		            "name":"5"   
		        },{    
		            "id":6,    
		            "name":"6"   
		        },{    
		            "id":7,    
		            "name":"7"   
		        },{    
		            "id":8,    
		            "name":"8"   
		        },{    
		            "id":9,    
		            "name":"9"   
		        },{    
		            "id":10,    
		            "name":"10"   
		        },{    
		            "id":11,    
		            "name":"11"   
		        },{    
		            "id":12,    
		            "name":"12"   
		        }],
			    valueField: 'id',    
		        textField: 'name',
		        editable:false,
		        onSelect:function(node){
                  $("#msgTd").html("&nbsp;");
		        	
		        	if($("#yearTdId1").val()){
			        	//选择月度时 季度值清空
			        	clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
			           //重新加载日
					   clearSelectVal(new Array('dayTdId1','dayTdId2'));
			        	$('#dayTdId1').combobox({    
							url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth="+$('#yearTdId1').val()+"-"+node.id,
						    valueField:'id',    
						    textField:'name',
						    editable:false,
						    onSelect:function(node) {
						    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")){
						    		  $("#msgTd").html("&nbsp;");
						        	  //选择日时  季度 周 值清空
						        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
						    	}else{
						    		 $("#dayTdId1").combobox("setValue",null);
					        		 $("#msgTd").html("&nbsp;");
									 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
						    	}
						  }
						});
						$('#dayTdId2').combobox({    
							url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth="+$('#yearTdId1').val()+"-"+node.id,
						    valueField:'id',    
						    textField:'name',
						    editable:false,
						    onSelect:function(node) {
						    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")){
						    		
						    		if($("#dayTdId1").combobox("getValue")){
							    		if(parseInt(node.id)<=parseInt($("#dayTdId1").combobox("getValue"))){
						    				 $("#dayTdId2").combobox("setValue",null);
							    			 $("#msgTd").html("&nbsp;");
											 $("#msgTd").append("<font color='red'>截止日不能小于或等于起始日！</font>"); 
						    			}else{
						    				 $("#msgTd").html("&nbsp;");
								        	  //选择日时  季度 周 值清空
								        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
						    			}
							    	}else{
							    		 $("#dayTdId2").combobox("setValue",null);
						        		 $("#msgTd").html("&nbsp;");
										 $("#msgTd").append("<font color='red'>请先输入起始日！</font>"); 
							    	}
						    		
						    		
						    	}else{
						    		 $("#dayTdId2").combobox("setValue",null);
					        		 $("#msgTd").html("&nbsp;");
									 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
						    	} 
							}
						});
						
		        	}else{
		        		 $("#monthTdId1").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
		        	}
		        }
			});
		   
			$('#monthTdId2').combobox({
		        data:[{    
		            "id":1,    
		            "name":"1"
		        },{    
		            "id":2,    
		            "name":"2"   
		        },{    
		            "id":3,    
		            "name":"3"   
		        },{    
		            "id":4,    
		            "name":"4"   
		        },{    
		            "id":5,    
		            "name":"5"   
		        },{    
		            "id":6,    
		            "name":"6"   
		        },{    
		            "id":7,    
		            "name":"7"   
		        },{    
		            "id":8,    
		            "name":"8"   
		        },{    
		            "id":9,    
		            "name":"9"   
		        },{    
		            "id":10,    
		            "name":"10"   
		        },{    
		            "id":11,    
		            "name":"11"   
		        },{    
		            "id":12,    
		            "name":"12"   
		        }],
			    valueField: 'id',    
		        textField: 'name',
		        editable:false,
		        onSelect:function(node){
		        	$("#msgTd").html("&nbsp;");
			    	if($("#yearTdId1").val()){
			    		if($("#monthTdId1").combobox("getValue")){
			    			if(parseInt(node.id)<=parseInt($("#monthTdId1").combobox("getValue"))){
			    				 $("#monthTdId2").combobox("setValue",null);
				    			 $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>截止月度不能小于或等于起始月度！</font>"); 
			    			}else{
					        	//选择月度时 季度值清空
					        	clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
			    			}
			    		}else{
			    			 $("#monthTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入起始月度！</font>"); 
			    		}
			    	}else{
		        		 $("#monthTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
		        	}
			    	
		        }
			});
			
			
			//日 下拉
			$('#dayTdId1').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth=curMonth",
			    valueField:'id',   
			    editable:false,
			    textField:'name',
			    onSelect:function(node) {
				    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")){
				    		  $("#msgTd").html("&nbsp;");
				        	  //选择日时  季度 周 值清空
				        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
				    	}else{
				    		 $("#dayTdId1").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
				    	}
			    	
			    	
			    	
					}
			});
			$('#dayTdId2').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth=curMonth",
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")){
			    		
			    		if($("#dayTdId1").combobox("getValue")){
				    		if(parseInt(node.id)<=parseInt($("#dayTdId1").combobox("getValue"))){
			    				 $("#dayTdId2").combobox("setValue",null);
				    			 $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>截止日不能小于或等于起始日！</font>"); 
			    			}else{
			    				 $("#msgTd").html("&nbsp;");
					        	  //选择日时  季度 周 值清空
					        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
			    			}
				    	}else{
				    		 $("#dayTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入起始日！</font>"); 
				    	}
			    		
			    		
			    	}else{
			    		 $("#dayTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
			    	}
				}
			});
			
			//科室下拉 
			<%--科室可根据部门类型来获取不同类型的下拉科室  url:"<%=basePath%>statistics/wdWin/treeBaseOrg.action?deptType=C,D",--%>
			$('#deptTdId').combotree({
				url:"<%=basePath%>statistics/wdWin/treeBaseOrg.action", 
				valueField:'id',
				textField:'text',
				 multiple: true,
				 onChange:function(){
					 $("#msgTd").html("&nbsp;");
	                    var deptCodes = $('#deptTdId').combotree('getValues');
	                    if(deptCodes!=""){ 
	                        if(dimensionString==""){
	                        	dimensionString="dept_code,科室";
	                        }else if(dimensionString.indexOf("dept_code")==-1){
	                        	dimensionString=dimensionString+",dept_code,科室";
	                        }
	                    }else{
	                    	if(dimensionString!=""){
	                    		 if(dimensionString.indexOf("dept_code")!=-1){
	                    			 var tmpIndex = dimensionString.indexOf("dept_code");
	                    			 if(tmpIndex!=0){
	                    				 dimensionString= dimensionString.replace(",dept_code,科室","");
	                    			 }else{
	                    				 var tmpStr = "dept_code,科室";
	                    				 if(dimensionString==tmpStr){
	                    					 dimensionString = dimensionString.replace("dept_code,科室","");
	                    				 }else{
	                    					 dimensionString = dimensionString.replace("dept_code,科室,","");
	                    				 }
	                    				
	                    			 }
	                    		 }
	                    		 
	                    	}
	                    	
	                    	
	                    }
	                }
			});
			
			
			//地域下拉
			$('#placeTdId').combotree({
				url:"<%=basePath%>statistics/wdWin/treeBaseDistrict.action?provinceOnly=0",
				valueField:'id',
				textField:'text',
				 multiple: true,
				 onChange:function(){
					    $("#msgTd").html("&nbsp;");
	                    var placeCodes = $('#placeTdId').combotree('getValues');
	                    if(placeCodes!=""){
	                    	if(dimensionString==""){
	                        	dimensionString="patient_birthplace,地域";
	                        }else if(dimensionString.indexOf("patient_birthplace")==-1){
	                        	dimensionString=dimensionString+",patient_birthplace,地域";
	                        }
	                    }else{
	                    	if(dimensionString!=""){
	                    		 if(dimensionString.indexOf("patient_birthplace")!=-1){
	                    			 var tmpIndex = dimensionString.indexOf("patient_birthplace");
	                    			 if(tmpIndex!=0){
	                    				 dimensionString= dimensionString.replace(",patient_birthplace,地域","");
	                    			 }else{
	                    				 dimensionString = dimensionString.replace("patient_birthplace,地域,","");
	                    			 }
	                    		 }
	                    	}
	                    }
	                }
			});
			//性别
			$('#sexTdId').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
                valueField:'encode',    
                textField:'name',
			    editable:false,
			    multiple:true,
			    onSelect:function(node){
			    	 $("#msgTd").html("&nbsp;");
	                    var sexCodes = $('#sexTdId').combobox('getValues');
	                    if(sexCodes!=""){
	                    	if(dimensionString==""){
	                        	dimensionString="patient_sex,性别";
	                        }else if(dimensionString.indexOf("patient_sex")==-1){
	                        	dimensionString=dimensionString+",patient_sex,性别";
	                        }
	                    }else{
	                    	if(dimensionString!=""){
	                    		 if(dimensionString.indexOf("patient_sex")!=-1){
	                    			 var tmpIndex = dimensionString.indexOf("patient_sex");
	                    			 if(tmpIndex!=0){
	                    				 dimensionString= dimensionString.replace(",patient_sex,性别","");
	                    			 }else{
	                    				 dimensionString = dimensionString.replace("patient_sex,性别,","");
	                    			 }
	                    		 }
	                    	}
	                    }
		        }
			});
			
			$.ajax({
				 url:"<%=basePath%>statistics/wdWin/ajaxTogetDeptMap.action", 
				type:'post',
				success: function(data) {
				   var tmpArr = eval(data);
				   for(var x=0;x<tmpArr.length;x++){
					   deptCodeMap.put(tmpArr[x].id,tmpArr[x].name);
				   }
				}
			});
			
		})
		//hedong 清空下拉值
		function clearSelectVal(selectArr){
			for(var x=0;x<selectArr.length;x++){
				$('#'+selectArr[x]).combobox('clear');
			}
		}
		 //清除信息
	  	function clear(){
	  		 window.location.reload();
	  	}
		 
	  	 var globalVar = 2;
		 function cloneAge(){
			 globalVar = globalVar+1;
			/*  $("#tmpAgeTd").append("<span><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp; <input class='easyui-textbox'   style='width:145px;' name='patient_age' id='patient_ageTdId1-"+globalVar+"'  />&nbsp; 至&nbsp;<input class='easyui-textbox'   style='width:145px;' name='patient_age' id='patient_ageTdId2-"+globalVar+"' />  <a id='"+globalVar+"' href='javascript:deleteAge("+globalVar+");' class='easyui-linkbutton' iconCls='icon-delete' plain='true'></a></span>"
			 ) */
			 var targetObj = $("<span><br/><br/>&nbsp;<input class='easyui-textbox'   style='width:145px;' name='patient_age' id='patient_ageTdId1-"+globalVar+"'  />&nbsp; 至&nbsp;<input class='easyui-textbox'   style='width:145px;' name='patient_age' id='patient_ageTdId2-"+globalVar+"' />&nbsp;<select id='patient_ageComboxbox-"+globalVar+"' class='easyui-combobox'  style='width:50px;'><option value='岁'>岁</option><option value='月'>月</option>  <option value='天'>天</option> </select> <a id='"+globalVar+"' href='javascript:deleteAge("+globalVar+");' class='easyui-linkbutton' iconCls='icon-delete' plain='true'></a></span>").appendTo("#tmpAgeTd");
			 //hedong 重新渲染样式		 
			// $.parser.parse(); 不能使用此渲染 若使用则影响整个页面已绑定的事件 http://www.cnblogs.com/iyangyuan/p/3358239.html
			 $.parser.parse(targetObj);//渲染追加的html
			// bindAgeEvent();
			 var patient_ageTdId1Obj = $('#patient_ageTdId1-'+globalVar);
			 var patient_ageTdId2Obj = $('#patient_ageTdId2-'+globalVar);
			 if(patient_ageTdId1Obj){
				 $("input",patient_ageTdId1Obj.next("span")).focus(function()  
							{  
					           $("#msgTd").html("&nbsp;");
						    });
				 $("input",patient_ageTdId1Obj.next("span")).blur(function()  
							{  
								if(patient_ageTdId1Obj.val()){
									if(!validateNumForAge(patient_ageTdId1Obj.val())){
										patient_ageTdId1Obj.textbox('setValue','');
										 $("#msgTd").html("&nbsp;");
										 $("#msgTd").append("<font color='red'>患者年龄只能为3位有效整数！</font>"); 
										 patient_ageTdId1Obj.next("span").children().first().focus();
										 return;
									  }
									  
									if(patient_ageTdId1Obj.val()){
									      if(dimensionString==""){
							               	dimensionString="patient_age,患者年龄";
							               }else if(dimensionString.indexOf("patient_age")==-1){
							               	dimensionString=dimensionString+",patient_age,患者年龄";
							               }
								     }
									
								}
						    });
			 }
			 if(patient_ageTdId2Obj){
				 patient_ageTdId2Obj.next("span").children().first().focus(function(){
					   $("#msgTd").html("&nbsp;");
					   if(!patient_ageTdId1Obj.val()){//如果起始年度没有值则不能输入截止值
						   $("#msgTd").append("<font color='red'>请先输入起始患者年龄！</font>");
						   patient_ageTdId1Obj.next("span").children().first().focus();
						   patient_ageTdId2Obj.val("");
					   }
					})
					
			      
				patient_ageTdId2Obj.next("span").children().first().blur(function(){
				    if(patient_ageTdId2Obj.val()){
				    	if(!validateNumForAge(patient_ageTdId2Obj.val())){
				    		patient_ageTdId2Obj.textbox('setValue','');
							 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>患者年龄只能为3位有效整数！</font>"); 
							 return;
						 }
					     if(patient_ageTdId2Obj.val()<=patient_ageTdId1Obj.val()){
					    	 patient_ageTdId2Obj.textbox('setValue','');
							 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>截止患者年龄不能小于或等于起始患者年龄！</font>"); 
					     }
				    } 
					
				 })
				
			 }
		 }
		 function deleteAge(tmpVar){
			 $("#msgTd").html("&nbsp;");
			 $('#'+tmpVar).parent().empty();
		 }
		 
		 function dimensionStringToAge(){
			   var tmpVal1 = $("#patient_ageTdId1").val();
			   var tmpVal2 = $("#patient_ageTdId2").val();
			    var b = true;
	    	    for(var y=3;y<=globalVar;y++){
	    	    	 var patient_ageTdId1Obj = $('#patient_ageTdId1-'+y); 
					 var patient_ageTdId2Obj = $('#patient_ageTdId2-'+y);
					 if(patient_ageTdId1Obj.length>0&&patient_ageTdId2Obj.length>0){
						 if(patient_ageTdId1Obj.val()==''&& patient_ageTdId2Obj.val()==''){ 
					    	 b=false;
							 break;
						 }
					 }
	    	    }
	    	    if(tmpVal1==''&&tmpVal2==''&& b){
	    	    	if(dimensionString!=""){
	               		 if(dimensionString.indexOf("patient_age")!=-1){
	               			 var tmpIndex = dimensionString.indexOf("patient_age");
	               			 if(tmpIndex!=0){
	               				 dimensionString= dimensionString.replace(",patient_age,患者年龄","");
	               			 }else{
	               				 dimensionString = dimensionString.replace("patient_age,患者年龄,","");
	               			 }
	               		 }
               	   }
	    	    }
		 }
		 
		 
		 
		var dimensionString =""; //除时间以外的其他维度  其他维度(目前暂时有 科室 患者年龄 地域 )按照选择的先后顺序来排放
		function confirmWd(){
			var dateType = "";// 1 按年统计   2 按季度统计（年度+季度）   3 按月统计（年度+月）  4 按日统计（年度+月+日） 5 按周统计（年度+月度+周）  6 按时统计（年度+月度+日+时）
			var yearStart=Number($("#yearTdId1").val());
			var yearEnd=Number($("#yearTdId2").val());
			if(yearEnd==0){
				yearEnd = yearStart;
			}
			var quarterStart =Number($("#seasonTdId1").combobox("getValue"));
			var quarterEnd=Number($("#seasonTdId2").combobox("getValue"));
			if(quarterEnd==0){
				quarterEnd = quarterStart;
			}
			var monthStart=Number($("#monthTdId1").combobox("getValue"));
			var monthEnd=Number($("#monthTdId2").combobox("getValue"));
			if(monthEnd==0){
				monthEnd = monthStart;
			}
			var dayStart=Number($("#dayTdId1").combobox("getValue"));
			var dayEnd=Number($("#dayTdId2").combobox("getValue"));
			if(dayEnd==0){
				dayEnd = dayStart;
			}
			
			var deptValue ="";
			var deptVal = $("#deptTdId").combobox("getValues");//若选择的是多个直接返回的就是可执行的数组
		    if(deptVal){
		    	 if(deptVal[0]=='1'){//若选择了等于1(全部)的则删除
			    			deptVal.remove('1');
			    			deptValue=arrToStr(deptVal);//将所选择的数据都转成普通的字符串
			    	}else{
			    		deptValue=arrToStr(deptVal);//将所选择的数据都转成普通的字符串
			    	}
		    }
			if(deptValue!=""){
				//遍历 deptCodeMap 
				var array = deptCodeMap.keySet();
		    	for(var i=0;i<array.length;i++) {
		    		if(deptValue.indexOf(array[i])!=-1){
		    			//得到子部门信息
		    			var tmpChildStr = deptCodeMap.get(array[i]);
		    			var tmpChildArr =[];
		    			if(tmpChildStr.indexOf(',')!=-1){
		    				tmpChildArr=tmpChildStr.split(',');
		    			}else{
		    				tmpChildArr.push(tmpChildStr);
		    			}
		    			var tmpFlg = false;
		    			loop:
		    			for(var x=0;x<tmpChildArr.length;x++){
				    		   if(deptValue.indexOf(tmpChildArr[x])!=-1){//用户展开了全部
				    		    		tmpFlg=true;
				    		    	    break loop;
				    		    }
				    	}
		    			if(tmpFlg){//用户展开了全部
		    				 deptValue = deptValue.replace(array[i]+","+deptCodeMap.get(array[i]),deptCodeMap.get(array[i]));
		    			}else{//用户没有展开全部
		    				deptValue = deptValue.replace(array[i],deptCodeMap.get(array[i]));
		    			}
		    		}
		   		}
			}
			
		      var placeVaue ="";
		      var placeVal = $("#placeTdId").combobox("getValues");//用户选择全部 patient_birthplace='1'
		     
		      if(placeVal){
		    	if(placeVal[0]=='1'){//若选择了等于1(全部)的则删除
		    		placeVal.remove('1');
		    		placeValue=arrToStr(placeVal);//将所选择的数据都转成普通的字符串
		    	}else{
		    		placeValue=arrToStr(placeVal);//将所选择的数据都转成普通的字符串
		    	}
		      }
		     
		      
		      var patient_ageValue="";
	          if($("#patient_ageTdId1").val()||$("#patient_ageTdId2").val()){
	        	     patient_ageValue=patient_ageValue+$("#patient_ageTdId1").val();
			      if($("#patient_ageTdId2").val()){
			    	  patient_ageValue=patient_ageValue+"-"+$("#patient_ageTdId2").val();
			      }
			      patient_ageValue=patient_ageValue+ $("#patient_ageComboxbox").combobox("getValue");
	          }
		      
		    var b = false;
		    var patient_ageNew = true;
    	    for(var y=3;y<=globalVar;y++){
    	    	 var patient_ageTdId1Obj = $('#patient_ageTdId1-'+y); 
				 var patient_ageTdId2Obj = $('#patient_ageTdId2-'+y);
				 var patient_ageUitObj = $('#patient_ageComboxbox-'+y);
				 
				 if(patient_ageTdId1Obj.length>0&&patient_ageTdId2Obj.length>0){
					 if(patient_ageTdId1Obj.val()==''&& patient_ageTdId2Obj.val()==''){ 
				    	 patient_ageNew = false;
				    	 break;
					 }else{
						 b=true;
						 patient_ageValue=patient_ageValue+","+patient_ageTdId1Obj.val();
						 if(patient_ageTdId2Obj.val()){
							 patient_ageValue=patient_ageValue+"-"+patient_ageTdId2Obj.val();
						 }
						 patient_ageValue=patient_ageValue+patient_ageUitObj.combobox("getValue");
					 } 
				 }
    	    }
    	    
    	    if(b){//有新添加patient_age行
    	    	 if(!$("#patient_ageTdId1").val()&& !$("#patient_ageTdId2").val()){ 
					 $("#msgTd").html("&nbsp;");
			    	 $("#msgTd").append("<font color='red'>两个患者年龄值不能都为空！</font>");
			    	 return;
				 }
    	    }
    	    
    	    if(!patient_ageNew){
    	    	 $("#msgTd").html("&nbsp;");
		    	 $("#msgTd").append("<font color='red'>新添加患者年龄阶段中的两个患者年龄值不能都为空！</font>");
		    	 return;
    	    }
    	    
    	    var sexVaue ="";
		      var sexVal = $("#sexTdId").combobox("getValues");//用户选择全部 patient_birthplace='1'
		      if(sexVal){
		    	  sexValue = sexVal;
		      }
			
			var tmpYearObj = $("#yearTdId1");
			var tmpSeasonObj = $("#seasonTdId1");//.combobox("getValue")
			var tmpMonthObj= $("#monthTdId1");
			var tmpDayObj= $("#dayTdId1");

			if(!tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>时间维度不能都为空！</font>"); 
				return;
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				dateType="1";
			}else if(tmpYearObj.val()&&tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				dateType="2";
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				dateType="3";
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.combobox("getValue")&&tmpDayObj.combobox("getValue")){
				dateType="4";
			}
			
			if(!deptValue){
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>科室维度不能为空！</font>"); 
				 return;
			}
			
			
			var dimensionValue ="";//其他维度值传递
			if(dimensionString.indexOf(",")!=-1){
				var diArr = dimensionString.split(",");
				for(var i=0;i<diArr.length;i++){
					if(diArr[i]=="dept_code"){
						if(dimensionValue==""){
							dimensionValue="dept_code,"+deptValue
						}else{
							dimensionValue=dimensionValue+"?dept_code,"+deptValue
						}
					}if(diArr[i]=="patient_birthplace"){
						if(dimensionValue==""){
							dimensionValue="patient_birthplace,"+placeValue
						}else{
							dimensionValue=dimensionValue+"?patient_birthplace,"+placeValue
						}
					}if(diArr[i]=="patient_age"){
						if(dimensionValue==""){
							dimensionValue="patient_age,"+patient_ageValue
						}else{
							dimensionValue=dimensionValue+"?patient_age,"+patient_ageValue
						}
					}if(diArr[i]=="patient_sex"){
						if(dimensionValue==""){
							dimensionValue="patient_sex,"+sexValue
						}else{
							dimensionValue=dimensionValue+"?patient_sex,"+sexValue
						}
					}
				}
			}
			
			var dateVo = [yearStart,yearEnd,quarterStart,quarterEnd,monthStart,monthEnd];
			window.opener.queryList(dateVo,Number(dateType),dimensionString,dimensionValue);
			window.close();
		}
		function arrToStr(deptVal){
			 var tmpDeptValue="";
			//hedong 重新拼接部门字符串
			for(var i=0;i<deptVal.length;i++){
				if(i!=deptVal.length-1){
					tmpDeptValue=tmpDeptValue+deptVal[i]+",";
				}else{
					tmpDeptValue = tmpDeptValue+deptVal[i];
				}
			}
			return tmpDeptValue;
		 }
		
		 Array.prototype.indexOf = function(val) {
				for (var i = 0; i < this.length; i++) {
				     if (this[i] == val) return i;
				}
				return -1;
			};
		    Array.prototype.remove = function(val) {
				var index = this.indexOf(val);
				if (index > -1) {
				this.splice(index, 1);
				}
		    };
		    //hedong  复写 数组js数组的indexOf  用于处理数据的删除  end
		    
	        //hedong 20160817  仿java的Map集合 用于存放科室数据 key=000001.... value=子科室   begin
	        function Map(){
	    	this.container = new Object();
	    	}
	    	Map.prototype.put = function(key, value){
	    	this.container[key] = value;
	    	}
	    	Map.prototype.get = function(key){
	    	return this.container[key];
	    	}
	    	Map.prototype.keySet = function() {
	    	var keyset = new Array();
	    	var count = 0;
	    	for (var key in this.container) {
	    	// 跳过object的extend函数
	    	if (key == 'extend') {
	    	continue;
	    	}
	    	keyset[count] = key;
	    	count++;
	    	}
	    	return keyset;
	    	}
	    	Map.prototype.size = function() {
	    	var count = 0;
	    	for (var key in this.container) {
	    	// 跳过object的extend函数
	    	if (key == 'extend'){
	    	continue;
	    	}
	    	count++;
	    	}
	    	return count;
	    	}
	    	Map.prototype.remove = function(key) {
	    	delete this.container[key];
	    	}
	    	Map.prototype.toString = function(){
	    	var str = "";
	    	for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
	    	str = str + keys[i] + "=" + this.container[keys[i]] + ";\n";
	    	}
	    	return str;
	    	}
	</script>
</html>
