<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
					<div style="padding: 5px 5px 0px 5px;">
						<form id="search" method="post">
							<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
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
									 	
									 	<!-- <tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>周
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									    &nbsp;<input class='easyui-combobox' style='width:145px;' id='weekTdId1' name='week'  value=''  />
								         &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='weekTdId2' name='week'  value=''  />
									  </td>
									 	</tr> -->
									 	<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>日
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap' id='tmpTdToDay'>
									   &nbsp;<input class='easyui-combobox' style='width:145px;' id='dayTdId1'   value=''  />
									   &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='dayTdId2'   value=''  />
									 </td>
									 </tr>
									 
									 	<!-- <tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>时
									 </td>
									 <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap' >
									   &nbsp;<input class='easyui-combobox' style='width:145px;' id='hourTdId1'   value=''  />
									   &nbsp;至&nbsp;<input class='easyui-combobox' style='width:145px;' id='hourTdId2'   value=''  />
									 </td>
									 </tr> -->
									 
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>科室
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="deptTdId" class="easyui-combotree" value="" name="dept" style='width:200px;'></input>
								     </td>
									</tr>
									
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>年龄
									 </td>
									  <td style='width: 320px;' class='honry-info' nowrap='nowrap' id="tmpAgeTd">
									  <span>
									   &nbsp;<input class="easyui-textbox"   style='width:145px;' name='age' id='ageTdId1'  />
									   &nbsp;至&nbsp;<input class="easyui-textbox"   style='width:145px;' name='age' id='ageTdId2'  />
									  <a href="javascript:cloneAge();" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>
									  </span>
								     </td>
									</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>地域
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="placeTdId" class="easyui-combotree" value="" name="place" style='width:200px;'></input>
									 
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
			   if(!$("#yearTdId1").val()){//如果第一个年度没有值则不能输入第二个值
				   $("#msgTd").append("<font color='red'>请先输入第一个年度！</font>");
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
						 $("#msgTd").append("<font color='red'>第二个年度不能小于或等于第一个年度！</font>"); 
				     }
			    } 
				
			})
			
			
			$("input",$("#ageTdId1").next("span")).blur(function()  
			{  
				if($("#ageTdId1").val()){
					if(!validateNumForAge($("#ageTdId1").val())){
						 $("#ageTdId1").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>年龄只能为3位有效整数！</font>"); 
						 $("#ageTdId1").next("span").children().first().focus();
						 return;
				     }
					
					  if($("#ageTdId1").val()){
					      if(dimensionString==""){
			               	dimensionString="age,年龄";
			               }else if(dimensionString.indexOf("age")==-1){
			               	dimensionString=dimensionString+",age,年龄";
			               }
					      
				     }
				}else{
					dimensionStringToAge();
				}
		    });
			
		   $("#ageTdId1").next("span").children().first().focus(function(){
			   $("#msgTd").html("&nbsp;");
			})
		   
		   $("#ageTdId2").next("span").children().first().focus(function(){
			   $("#msgTd").html("&nbsp;");
			   if(!$("#ageTdId1").val()){//如果第一个年度没有值则不能输入第二个值
				   $("#msgTd").append("<font color='red'>请先输入第一个年龄！</font>");
				   $("#ageTdId1").next("span").children().first().focus();
				   $("#ageTdId2").val("");
			   }
			})
			$("#ageTdId2").next("span").children().first().blur(function(){
			    if($("#ageTdId2").val()){
			    	if(!validateNumForAge($("#ageTdId2").val())){
						 $("#ageTdId2").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>年龄只能为3位有效整数！</font>"); 
						 return;
					 }
				     if($("#ageTdId2").val()<=$("#ageTdId1").val()){
				    	 $("#ageTdId2").textbox('setValue','');
						 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>第二个年龄不能小于或等于第一个年龄！</font>"); 
				     }
			    }else{
			    	dimensionStringToAge();
			    } 
				
			})
			
			
			$('#seasonTdId1').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdSeason.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	if($("#yearTdId1").val()){
			    		$("#msgTd").html("&nbsp;");
			        	//选择季度时 月度值 周   日 清空
			        	clearSelectVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2'));
			        	//clearSelectVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2','hourTdId1','hourTdId2'));
			        	//clearSelectVal(new Array('monthTdId1','monthTdId2','weekTdId1','weekTdId2','dayTdId1','dayTdId2','hourTdId1','hourTdId2'));
			        	//季度  年 选中   月度 周 日取消选中
			    	}else{
			    		 $("#seasonTdId1").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
			    	}
			    	
	            }
			});
			$('#seasonTdId2').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdSeason.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	if($("#yearTdId1").val()){
				      $("#msgTd").html("&nbsp;");
				      if($("#seasonTdId1").combobox("getValue")){
					    		if(parseInt(node.id)<=$("#seasonTdId1").combobox("getValue")){
					    			 $("#seasonTdId2").combobox("setValue",null);
					    			 $("#msgTd").html("&nbsp;");
									 $("#msgTd").append("<font color='red'>第二个季度不能小于或等于第一个季度！</font>"); 
					    		}else{
					    			$("#msgTd").html("&nbsp;");
						        	//选择季度时 月度值 周   日 清空
						        	clearSelectVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2'));
						        	//clearSelectVal(new Array('monthTdId1','monthTdId2','weekTdId1','weekTdId2','dayTdId1','dayTdId2'));
					    		}
			    	  }else{
			    		     $("#seasonTdId2").combobox("setValue",null);
			    		     $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先选择第一个季度！</font>"); 
			    	  }
			    	}else{
			    		 $("#seasonTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
			    	}
			   }
			});
			
			
			$('#monthTdId1').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdMonth.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
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
						        	  //clearSelectVal(new Array('seasonTdId1','seasonTdId2','weekTdId1','weekTdId2'));
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
											 $("#msgTd").append("<font color='red'>第二个日不能小于或等于第一个日！</font>"); 
						    			}else{
						    				 $("#msgTd").html("&nbsp;");
								        	  //选择日时  季度 周 值清空
								        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
								        	 // clearSelectVal(new Array('seasonTdId1','seasonTdId2','weekTdId1','weekTdId2'));
						    			}
							    	}else{
							    		 $("#dayTdId2").combobox("setValue",null);
						        		 $("#msgTd").html("&nbsp;");
										 $("#msgTd").append("<font color='red'>请先输入第一个日！</font>"); 
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
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdMonth.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	$("#msgTd").html("&nbsp;");
			    	if($("#yearTdId1").val()){
			    		if($("#monthTdId1").combobox("getValue")){
			    			if(parseInt(node.id)<=parseInt($("#monthTdId1").combobox("getValue"))){
			    				 $("#monthTdId2").combobox("setValue",null);
				    			 $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>第二个月度不能小于或等于第一个月度！</font>"); 
			    			}else{
					        	//选择月度时 季度值清空
					        	clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
			    			}
			    		}else{
			    			 $("#monthTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入第一个月度！</font>"); 
			    		}
			    	}else{
		        		 $("#monthTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
		        	}
			    	
		        	
	            }
			});
			
			
			
			<%-- $('#weekTdId1').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdWeek.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")){
			    		$("#msgTd").html("&nbsp;");
			        	//选择周时 季度值  日 清空
			        	clearSelectVal(new Array('seasonTdId1','seasonTdId2','dayTdId1','dayTdId2'));
			    	}else{
			    		$("#weekTdId1").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
			    	}
			    	
	            }
			}); --%>
			<%-- $('#weekTdId2').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdWeek.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	$("#msgTd").html("&nbsp;");
			    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")){
			    		if($("#weekTdId1").combobox("getValue")){
				    		if(parseInt(node.id)<=parseInt($("#weekTdId1").combobox("getValue"))){
			    				 $("#weekTdId2").combobox("setValue",null);
				    			 $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>第二个周不能小于或等于第一个周！</font>"); 
			    			}else{
					        	//选择周时 季度值  日 清空
					        	clearSelectVal(new Array('seasonTdId1','seasonTdId2','dayTdId1','dayTdId2'));
			    			}
				    	}else{
				    		 $("#weekTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入第一个周！</font>"); 
				    	}
			    	}else{
			    		 $("#weekTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
			    	}
	            }
			}); --%>
			
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
				        	  //clearSelectVal(new Array('seasonTdId1','seasonTdId2','weekTdId1','weekTdId2'));
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
								 $("#msgTd").append("<font color='red'>第二个日不能小于或等于第一个日！</font>"); 
			    			}else{
			    				 $("#msgTd").html("&nbsp;");
					        	  //选择日时  季度 周 值清空
					        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
					        	  //clearSelectVal(new Array('seasonTdId1','seasonTdId2','weekTdId1','weekTdId2'));
			    			}
				    	}else{
				    		 $("#dayTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入第一个日！</font>"); 
				    	}
			    		
			    		
			    	}else{
			    		 $("#dayTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年度和月度！</font>"); 
			    	}
				}
			});
			
			<%-- $('#hourTdId1').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdHour.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
				    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")&&$("#dayTdId1").combobox("getValue")){
				    		 $("#msgTd").html("&nbsp;");
				        	  //选择日时  季度 周 值清空
				        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
				        	 // clearSelectVal(new Array('seasonTdId1','seasonTdId2','weekTdId1','weekTdId2'));
				    	}else{
				    		 $("#hourTdId1").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入年月日！</font>");
				    	}
			    	
			    	 
					}
			}); --%>
			<%-- $('#hourTdId2').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdHour.action?randomNum='+Math.random(),
			    valueField:'id',    
			    textField:'name',
			    editable:false,
			    onSelect:function(node) {
			    	if($("#yearTdId1").val()&&$("#monthTdId1").combobox("getValue")&&$("#dayTdId1").combobox("getValue")){
			    		if($("#hourTdId1").combobox("getValue")){
			    			if(parseInt(node.id)<=parseInt($("#hourTdId1").combobox("getValue"))){
				    			 $("#hourTdId2").combobox("setValue",null);
				    			 $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>第二个时间不能小于或等于第一个时间！</font>"); 
				    		}else{
				    			  $("#msgTd").html("&nbsp;");
					        	  //选择日时  季度 周 值清空
					        	  clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
					        	  //clearSelectVal(new Array('seasonTdId1','seasonTdId2','weekTdId1','weekTdId2'));
				    		}
			    		}else{
			    			 $("#hourTdId2").combobox("setValue",null);
			        		 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>请先输入第一个时间！</font>");
			    		}
			    		
			    		
			    	}else{
			    		 $("#hourTdId2").combobox("setValue",null);
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入年月日！</font>");
			    	}
				}
			}); --%>
			
			
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
	                        	dimensionString="dept,科室";
	                        }else if(dimensionString.indexOf("dept")==-1){
	                        	dimensionString=dimensionString+",dept,科室";
	                        }
	                    }else{
	                    	if(dimensionString!=""){
	                    		 if(dimensionString.indexOf("dept")!=-1){
	                    			 var tmpIndex = dimensionString.indexOf("dept");
	                    			 if(tmpIndex!=0){
	                    				 dimensionString= dimensionString.replace(",dept,科室","");
	                    			 }else{
	                    				 var tmpStr = "dept,科室";
	                    				 if(dimensionString==tmpStr){
	                    					 dimensionString = dimensionString.replace("dept,科室","");
	                    				 }else{
	                    					 dimensionString = dimensionString.replace("dept,科室,","");
	                    				 }
	                    				
	                    			 }
	                    		 }
	                    		 
	                    	}
	                    	
	                    	
	                    }
	                }
			});
			
			
			//地域下拉
			$('#placeTdId').combotree({
				url:"<%=basePath%>statistics/wdWin/treeBaseDistrict.action",
				valueField:'id',
				textField:'text',
				 multiple: true,
				 onChange:function(){
					    $("#msgTd").html("&nbsp;");
	                    var placeCodes = $('#placeTdId').combotree('getValues');
	                    if(placeCodes!=""){
	                    	if(dimensionString==""){
	                        	dimensionString="place,地域";
	                        }else if(dimensionString.indexOf("place")==-1){
	                        	dimensionString=dimensionString+",place,地域";
	                        }
	                    	
	                    	 
	                    }else{
	                    	if(dimensionString!=""){
	                    		 if(dimensionString.indexOf("place")!=-1){
	                    			 var tmpIndex = dimensionString.indexOf("place");
	                    			 if(tmpIndex!=0){
	                    				 dimensionString= dimensionString.replace(",place,地域","");
	                    			 }else{
	                    				 dimensionString = dimensionString.replace("place,地域,","");
	                    			 }
	                    		 }
	                    	}
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
			/*  $("#tmpAgeTd").append("<span><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp; <input class='easyui-textbox'   style='width:145px;' name='age' id='ageTdId1-"+globalVar+"'  />&nbsp; 至&nbsp;<input class='easyui-textbox'   style='width:145px;' name='age' id='ageTdId2-"+globalVar+"' />  <a id='"+globalVar+"' href='javascript:deleteAge("+globalVar+");' class='easyui-linkbutton' iconCls='icon-delete' plain='true'></a></span>"
			 ) */
			 var targetObj = $("<span><br/><br/>&nbsp;<input class='easyui-textbox'   style='width:145px;' name='age' id='ageTdId1-"+globalVar+"'  />&nbsp; 至&nbsp;<input class='easyui-textbox'   style='width:145px;' name='age' id='ageTdId2-"+globalVar+"' />  <a id='"+globalVar+"' href='javascript:deleteAge("+globalVar+");' class='easyui-linkbutton' iconCls='icon-delete' plain='true'></a></span>").appendTo("#tmpAgeTd");
			 //hedong 重新渲染样式		 
			// $.parser.parse(); 不能使用此渲染 若使用则影响整个页面已绑定的事件 http://www.cnblogs.com/iyangyuan/p/3358239.html
			 $.parser.parse(targetObj);//渲染追加的html
			// bindAgeEvent();
			 var ageTdId1Obj = $('#ageTdId1-'+globalVar);
			 var ageTdId2Obj = $('#ageTdId2-'+globalVar);
			 if(ageTdId1Obj){
				 $("input",ageTdId1Obj.next("span")).focus(function()  
							{  
					           $("#msgTd").html("&nbsp;");
						    });
				 $("input",ageTdId1Obj.next("span")).blur(function()  
							{  
								if(ageTdId1Obj.val()){
									if(!validateNumForAge(ageTdId1Obj.val())){
										ageTdId1Obj.textbox('setValue','');
										 $("#msgTd").html("&nbsp;");
										 $("#msgTd").append("<font color='red'>年龄只能为3位有效整数！</font>"); 
										 ageTdId1Obj.next("span").children().first().focus();
										 return;
									  }
									  
									if(ageTdId1Obj.val()){
									      if(dimensionString==""){
							               	dimensionString="age,年龄";
							               }else if(dimensionString.indexOf("age")==-1){
							               	dimensionString=dimensionString+",age,年龄";
							               }
								     }
									
								}
						    });
			 }
			 if(ageTdId2Obj){
				 ageTdId2Obj.next("span").children().first().focus(function(){
					   $("#msgTd").html("&nbsp;");
					   if(!ageTdId1Obj.val()){//如果第一个年度没有值则不能输入第二个值
						   $("#msgTd").append("<font color='red'>请先输入第一个年龄！</font>");
						   ageTdId1Obj.next("span").children().first().focus();
						   ageTdId2Obj.val("");
					   }
					})
					
			      
				ageTdId2Obj.next("span").children().first().blur(function(){
				    if(ageTdId2Obj.val()){
				    	if(!validateNumForAge(ageTdId2Obj.val())){
				    		ageTdId2Obj.textbox('setValue','');
							 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>年龄只能为3位有效整数！</font>"); 
							 return;
						 }
					     if(ageTdId2Obj.val()<=ageTdId1Obj.val()){
					    	 ageTdId2Obj.textbox('setValue','');
							 $("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>第二个年龄不能小于或等于第一个年龄！</font>"); 
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
			   var tmpVal1 = $("#ageTdId1").val();
			   var tmpVal2 = $("#ageTdId2").val();
			    var b = true;
	    	    for(var y=3;y<=globalVar;y++){
	    	    	 var ageTdId1Obj = $('#ageTdId1-'+y); 
					 var ageTdId2Obj = $('#ageTdId2-'+y);
					 if(ageTdId1Obj.length>0&&ageTdId2Obj.length>0){
						 if(ageTdId1Obj.val()==''&& ageTdId2Obj.val()==''){ 
					    	 b=false;
							 break;
						 }
					 }
	    	    }
	    	    if(tmpVal1==''&&tmpVal2==''&& b){
	    	    	if(dimensionString!=""){
	               		 if(dimensionString.indexOf("age")!=-1){
	               			 var tmpIndex = dimensionString.indexOf("age");
	               			 if(tmpIndex!=0){
	               				 dimensionString= dimensionString.replace(",age,年龄","");
	               			 }else{
	               				 dimensionString = dimensionString.replace("age,年龄,","");
	               			 }
	               		 }
               	   }
	    	    }
		 }
		 
		 var ids="year,season,month,day,dept,place,age";
		// var ids="year,season,month,day,hour,dept,place,age";
		//var ids="year,season,month,week,day,hour,dept,place,age";
		var dimensionString ="";//除时间以外的其他维度  其他维度(目前暂时有 科室 年龄 地域 )按照选择的先后顺序来排放
		
		
		function confirmWd(){
			var postFlg = true;
			var dateType = "";// 1 按年统计   2 按季度统计（年度+季度）   3 按月统计（年度+月）  4 按日统计（年度+月+日） 5 按周统计（年度+月度+周）  6 按时统计（年度+月度+日+时）
			var yearStart="";
			var yearEnd="";
			var quarterStart ="";
			var quarterEnd="";
			var monthStart="";
			var monthEnd="";
			//var weekStart="";
			//var weekEnd="";
			var dayStart="";
			var dayEnd="";
			//var hourStart="";
			//var hourEnd="";
			var deptValue=""
			var placeValue="";
			var ageValue="";
			if(ids){
				var idsArr = ids.split(",");
				for(var i=0;i<idsArr.length;i++){
					if(idsArr[i]){
						var tmpObj = document.getElementById(idsArr[i]);
						var tmpObjNameArr = document.getElementsByName(idsArr[i]);
						
						 if(idsArr[i]=='year'){//带区间 普通文本
							 var firstVal=$("#yearTdId1").val();;
					    	 var secondVal=$("#yearTdId2").val();;
						  }else  if(idsArr[i]=='season'){//下拉
					    	  quarterStart=$("#seasonTdId1").combobox("getValue");
					    	  quarterEnd =  $("#seasonTdId2").combobox("getValue");
						  }else  if(idsArr[i]=='month'){//下拉 带区间
					    	  monthStart=$("#monthTdId1").combobox("getValue");
					    	  monthEnd = $("#monthTdId2").combobox("getValue")  
						  }else  if(idsArr[i]=='day'){//下拉 带区间
							  dayStart = $("#dayTdId1").combobox("getValue");
					    	  dayEnd = $("#dayTdId2").combobox("getValue");
						  }else  if(idsArr[i]=='dept'){//下拉 科室
							  var deptVal = $("#deptTdId").combobox("getValue");
						      if(deptVal){
						    	  deptValue = deptVal;
						      }
							  
						  }else  if(idsArr[i]=='place'){//下拉 地域
							  var placeVal = $("#placeTdId").combobox("getValue");
						      
						      if(placeVal){
						    	  placeValue =placeVal;
						      }
						      $.messager.alert('提示',"placeValue>>"+placeValue)
							 
						  }else  if(idsArr[i]=='age'){//下拉 年龄
							  if($("#ageTdId1").val()||$("#ageTdId2").val()){
								  ageValue=$("#ageTdId1").val();
							      if($("#ageTdId2").val()){
							    	  ageValue=ageValue+"-"+$("#ageTdId2").val();
							      }
							  }
							  
							    var b = false;
					    	    for(var y=3;y<=globalVar;y++){
					    	    	 var ageTdId1Obj = $('#ageTdId1-'+y); 
									 var ageTdId2Obj = $('#ageTdId2-'+y);
									 if(ageTdId1Obj.length>0&&ageTdId2Obj.length>0){
										 if(ageTdId1Obj.val()==''&& ageTdId2Obj.val()==''){ 
											 $("#msgTd").html("&nbsp;");
									    	 $("#msgTd").append("<font color='red'>新添加年龄阶段中的两个年龄值不能都为空！</font>");
									    	 break;
										 }else{
											 b=true;
											 ageValue=ageValue+","+ageTdId1Obj.val();
											 if(ageTdId2Obj.val()){
												 ageValue=ageValue+"-"+ageTdId2Obj.val();
											 }
											
										 } 
									 }
					    	    }
					    	    
					    	    if(b){
					    	    	 if(!$("#ageTdId1").val()&& !$("#ageTdId2").val()){ 
										 $("#msgTd").html("&nbsp;");
								    	 $("#msgTd").append("<font color='red'>两个年龄值不能都为空！</font>");
								    	 break;
									 }
					    	    }
					    	    
							     
						  }				
							
					}
				}
			}
			
			
			
			
			// queryList(timeGroupFlg,wdTime1,wdTime2,deptArr,quarterStart,quarterEnd);
			
			
			//window.opener.document.getElementById("returnVal").value='changeValchangeVal';
			//window.opener.returnValChange("changeValchangeVal");
			//window.close();
			
			
			
			var tmpYearObj = $("#yearTdId1");
			var tmpSeasonObj = $("#seasonTdId1");//.combobox("getValue")
			var tmpMonthObj= $("#monthTdId1");
			var tmpDayObj= $("#dayTdId1");
			//var tmpWeekObj= $("#weekTdId1");
			//var tmpHourObj= $("#hourTdId1");
			
			/* else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.combobox("getValue")&&tmpWeekObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")&&!tmpHourObj.combobox("getValue")){
				dateType="5";
				$.messager.alert('提示',"按周统计");
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.combobox("getValue")&&tmpDayObj.combobox("getValue")&&tmpHourObj.combobox("getValue")){
			dateType="6";
			$.messager.alert('提示',"按时统计...");
		} */

			if(!tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>时间维度不能都为空！</font>"); 
				 postFlg=false;
				return;
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				dateType="1";
				$.messager.alert('提示',"按年统计...");
			}else if(tmpYearObj.val()&&tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				dateType="2";
				$.messager.alert('提示',"按季度统计...");
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.combobox("getValue")&&!tmpDayObj.combobox("getValue")){
				dateType="3";
				$.messager.alert('提示',"按月统计...");
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.combobox("getValue")&&tmpDayObj.combobox("getValue")){
				dateType="4";
				$.messager.alert('提示',"按日统计...");
			}
			
			
			var dimensionValue ="";//其他维度值传递
			if(dimensionString.indexOf(",")!=-1){
				var diArr = dimensionString.split(",");
				for(var i=0;i<diArr.length;i++){
					if(diArr[i]=="dept"){
						if(dimensionValue==""){
							dimensionValue="dept,"+deptValue
						}else{
							dimensionValue=dimensionValue+"?dept,"+deptValue
						}
					}else if(diArr[i]=="place"){
						if(dimensionValue==""){
							dimensionValue="place,"+placeValue
						}else{
							dimensionValue=dimensionValue+"?place,"+placeValue
						}
					}else if(diArr[i]=="age"){
						if(dimensionValue==""){
							dimensionValue="age,"+ageValue
						}else{
							dimensionValue=dimensionValue+"?age,"+ageValue
						}
					}
				}
			}
		
			var dateVo = [yearStart,yearEnd,quarterStart,quarterEnd,monthStart,monthEnd]
		}
	</script>
</html>
