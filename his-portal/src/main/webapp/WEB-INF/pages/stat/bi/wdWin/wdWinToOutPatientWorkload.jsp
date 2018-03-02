<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>书写病历界面</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script language="javascript" type="text/javascript" src="<%=basePath%>datePicker/WdatePicker.js"></script>
	
</head>
<body>
	<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: auto;">
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
									&nbsp;<input style='width:145px;' name='month' id="yearTdId1" class="Wdate" onFocus="var yearTdId2=$dp.$('yearTdId2');WdatePicker({dateFmt:'yyyy',onpicked:function(){yearTdId2.focus();},maxDate:'%y',errDealMode:2})"/>
									&nbsp;至&nbsp;
									<input style='width:145px;' name='year' id="yearTdId2" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy',minDate:'#F{$dp.$D(\'yearTdId1\',{y:1})}',isShowToday:false,errDealMode:1})"/>
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
									 &nbsp;<input style='width:145px;' name='month' id="monthTdId1" class="Wdate" type="text"  onFocus="var monthTdId2=$dp.$('monthTdId2');WdatePicker({dateFmt:'M',onpicked:function(){monthTdId2.focus();},maxDate:'#F{$dp.$D(\'monthTdId2\')}',isShowToday:false})"/>
								        &nbsp;至&nbsp;<input style='width:145px;' name='month' id="monthTdId2" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'M',minDate:'#F{$dp.$D(\'monthTdId1\',{M:+1})}',isShowToday:false})"/>
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
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>医生
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="docTdId" class="easyui-combotree" value="" name="docTdId" style='width:200px;'></input>
								     </td>
									</tr>
									<tr>
									 <td style='width: 320px;height: 40px;' class='honry-lable' style='width: 5%;' nowrap='nowrap'>挂号级别
									 </td>
									  <td style='width: 320px;height: 40px;' class='honry-info' nowrap='nowrap'>
									  &nbsp;<input id="doclevel" class="easyui-combobox" value="" name="doclevel" style='width:200px;'></input>
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
	var yearStart=0;
	var yearEnd=0;
	var quarterStart =0;
	var quarterEnd=0;
	var monthStart=0;
	var monthEnd=0;
	var dayStart=0;
	var dayEnd=0;
	var dateType = 4;// 1 按年统计   2 按季度统计（年度+季度）   3 按月统计（年度+月）  4 按日统计（年度+月+日） 5 按周统计（年度+月度+周）  6 按时统计（年度+月度+日+时）
	var dimensionString =""; //除时间以外的其他维度  其他维度(目前暂时有 科室 年龄 地域 )按照选择的先后顺序来排放
	var deptCodeMap = new Map();//20160817 hedong 用于存放后台获取的deptCode 000001=子部门 
	var dateArray = window.opener.tmpDateArray;
	var tmpDimensionValue = window.opener.tmpDimensionValue;
	    //hedong  20160811 绑定相关事件及下拉数据源
		$(function(){
			
		   $("#yearTdId2").focus(function(){
			   $("#msgTd").html("&nbsp;");
			   if(!$("#yearTdId1").val()){//如果起始年度没有值则不能输入截止值
				   $("#msgTd").append("<font color='red'>请先输入起始年度！</font>");
				   $("#yearTdId1").focus();
				   $("#yearTdId2").val("");
			   }
			})
			
			
		   $("#monthTdId1").focus(function(){
			   $("#msgTd").html("&nbsp;");
	        	if($("#yearTdId1").val()){
	        		if($("#monthTdId1").val()){
			        	//选择月度时 季度值清空
			        	clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
			           //重新加载日
					   clearVal(new Array('dayTdId1','dayTdId2'));
			        	$('#dayTdId1').combobox({    
							url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth="+$('#yearTdId1').val()+"-"+$("#monthTdId1").val(),
						    valueField:'id',    
						    textField:'name',
						    editable:false,
						    onSelect:function(node) {
						    	if($("#yearTdId1").val()&&$("#monthTdId1").val()){
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
							url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth="+$('#yearTdId1').val()+"-"+$("#monthTdId1").val(),
						    valueField:'id',    
						    textField:'name',
						    editable:false,
						    onSelect:function(node) {
						    	if($("#yearTdId1").val()&&$("#monthTdId1").val()){
						    		
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
	        		}
	        	}else{
	        		 $("#monthTdId1").val('');
	        		 $("#msgTd").html("&nbsp;");
					 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
	        	}
			})
		   
			$("#monthTdId2").focus(function(){
				$("#msgTd").html("&nbsp;");
		    	if($("#yearTdId1").val()){
		    		if($("#monthTdId1").val()){
		    			if($("#monthTdId1").val() == 12){
		    				$("#msgTd").html("&nbsp;");
							 $("#msgTd").append("<font color='red'>截止月度不能等于起始月度！</font>"); 
		    				$("#monthTdId2").val('')
		    			}
			    			if($("#monthTdId2").val() <= $("#monthTdId1").val()){
			    				 $("#monthTdId2").val('');
				    			 $("#msgTd").html("&nbsp;");
								 $("#msgTd").append("<font color='red'>截止月度不能小于或等于起始月度！</font>"); 
			    			}else{
					        	//选择月度时 季度值清空
					        	clearSelectVal(new Array('seasonTdId1','seasonTdId2'));
			    			}
		    		}else{
		    			 $("#monthTdId2").val('');
		        		 $("#msgTd").html("&nbsp;");
						 $("#msgTd").append("<font color='red'>请先输入起始月度！</font>"); 
		    		}
		    	}else{
	        		 $("#monthTdId2").val('');
	        		 $("#msgTd").html("&nbsp;");
					 $("#msgTd").append("<font color='red'>请先输入年度！</font>"); 
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
			        	clearVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2'));
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
							        	clearVal(new Array('monthTdId1','monthTdId2','dayTdId1','dayTdId2'));
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
			
		   
		   
			
			
			//日 下拉
			$('#dayTdId1').combobox({    
				url: '<%=basePath %>statistics/wdWin/ajaxTogetWdDay.action?randomNum='+Math.random()+"&selectMonth=curMonth",
			    valueField:'id',   
			    editable:false,
			    textField:'name',
			    onSelect:function(node) {
				    	if($("#yearTdId1").val()&&$("#monthTdId1").val()){
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
			    	if($("#yearTdId1").val()&&$("#monthTdId1").val()){
			    		
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
			//挂号级别
			$('#doclevel').combobox({
				url:'<%=basePath%>statistics/wdWin/queryDocLevelForBiPublic.action',
				valueField:'code',
				textField:'name',
				multiple: true,
				onChange:function(){
					$("#msgTd").html("&nbsp;");
                    var doclevelCodes = $('#doclevel').combobox('getValues');
                    if(doclevelCodes!=""){ 
                        if(dimensionString==""){
                        	dimensionString="reglevl_code,挂号级别";
                        }else if(dimensionString.indexOf("reglevl_code")==-1){
                        	dimensionString=dimensionString+",reglevl_code,挂号级别";
                        }
                    }else{
                    	if(dimensionString!=""){
                    		 if(dimensionString.indexOf("reglevl_code")!=-1){
                    			 var tmpIndex = dimensionString.indexOf("reglevl_code");
                    			 if(tmpIndex!=0){
                    				 dimensionString= dimensionString.replace(",reglevl_code,挂号级别","");
                    			 }else{
                    				 var tmpStr = "reglevl_code,挂号级别";
                    				 if(dimensionString==tmpStr){
                    					 dimensionString = dimensionString.replace("reglevl_code,挂号级别","");
                    				 }else{
                    					 dimensionString = dimensionString.replace("reglevl_code,挂号级别,","");
                    				 }
                    			 }
                    		 }
                    	}
                    }
				}
			});
			//医生下拉树 
			<%--科室可根据部门类型来获取不同类型的下拉科室  url:"<%=basePath%>statistics/wdWin/treeBaseOrg.action?deptType=C,D",--%>
			$('#docTdId').combotree({ 
				url:"<%=basePath%>statistics/wdWin/deptEmpTree.action", 
				valueField:'id',
				textField:'text',
				 multiple: true,
				 onChange:function(){
					 $("#msgTd").html("&nbsp;");
	                    var docCodes = $('#docTdId').combotree('getValues');
	                    
	                    if(docCodes!=""){ 
	                        if(dimensionString==""){
	                        	dimensionString="doct_code,医生";
	                        }else if(dimensionString.indexOf("doct_code")==-1){
	                        	dimensionString=dimensionString+",doct_code,医生";
	                        }
	                    }else{
	                    	if(dimensionString!=""){
	                    		 if(dimensionString.indexOf("doct_code")!=-1){
	                    			 var tmpIndex = dimensionString.indexOf("doct_code");
	                    			 if(tmpIndex!=0){
	                    				 dimensionString= dimensionString.replace(",doct_code,医生","");
	                    			 }else{
	                    				 var tmpStr = "doct_code,医生";
	                    				 if(dimensionString==tmpStr){
	                    					 dimensionString = dimensionString.replace("doct_code,医生","");
	                    				 }else{
	                    					 dimensionString = dimensionString.replace("doct_code,医生,","");
	                    				 }
	                    				
	                    			 }
	                    		 }
	                    	}
	                    }
	                }
			});
			
			//hedong  deptCodeMap初始化 begin
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
			//hedong  deptCodeMap初始化 end
			if(dateArray){
				$('#yearTdId1').val(dateArray[0]);
				if(dateArray[1] != 0 && dateArray[1] != dateArray[0]){
					$('#yearTdId2').val(dateArray[1]);
				}
				if(dateArray[2] != 0){
					$('#seasonTdId1').combobox('setValue',dateArray[2]);
				}
				if(dateArray[3] != 0 && dateArray[3] != dateArray[2]){
					$('#seasonTdId2').combobox('setValue',dateArray[3]);
				}
				if(dateArray[4] != 0){
					$('#monthTdId1').val(dateArray[4]);
				}
				if(dateArray[5] != 0 && dateArray[5] != dateArray[4]){
					$('#monthTdId2').val(dateArray[5]);
				}
				if(dateArray[6] != 0){
					var day = '00'+dateArray[6];
					$('#dayTdId1').combobox('setValue',day.substring(day.length-2,day.length));
				}
				if(dateArray[7] != 0 && dateArray[7] != dateArray[6]){
					var day = '00'+dateArray[7];
					$('#dayTdId2').combobox('setValue',day.substring(day.length-2,day.length));
				}
			}
			if(tmpDimensionValue){
				var dimensionValue = tmpDimensionValue.split('?');
				for(var i =0; i < dimensionValue.length; i++ ){
					var dimensionArray = dimensionValue[i].split(',');
					if(dimensionArray[0] == 'reglevl_code'){
						dimensionArray.shift();
						$('#doclevel').combobox('setValues', dimensionArray);
					}
				}
			}
			
			
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
		//20160817 hedong 将数组中数据进行','拼接 begin
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
	   //20160817 hedong  将数组中数据进行','拼接 end
	    //hedong 维度确认
		function confirmWd(){
			
			yearStart=Number($("#yearTdId1").val());
			yearEnd=Number($("#yearTdId2").val());
			if(yearEnd==0){
				yearEnd = yearStart;
			}
			quarterStart =Number($("#seasonTdId1").combobox("getValue"));
			quarterEnd=Number($("#seasonTdId2").combobox("getValue"));
			if(quarterEnd==0){
				quarterEnd = quarterStart;
			}
			monthStart=Number($("#monthTdId1").val());
			monthEnd=Number($("#monthTdId2").val());
			if(monthEnd==0){
				monthEnd = monthStart;
			}
			dayStart=Number($("#dayTdId1").combobox("getValue"));
			dayEnd=Number($("#dayTdId2").combobox("getValue"));
			if(dayEnd==0){
				dayEnd = dayStart;
			}
			
			 //hedong 20160817 (若用户选择了科室分类如)处理科室begin....
			var deptValue="";
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
			//hedong 20160817 (若用户选择了科室分类如)处理科室end....
				
		      var docValue ="";
		      var docVal = $("#docTdId").combotree("getValues");//用户选择全部 doct_code='1'
		      if(docVal){
		    	  docValue = docVal;
		      }
		      var docLevelValue ="";
		      var docLevelVal = $("#doclevel").combobox("getValues");//用户选择全部 doct_code='1'
		      if(docLevelVal){
		    	  docLevelValue = docLevelVal;
		      }
			
			
			var tmpYearObj = $("#yearTdId1");
			var tmpSeasonObj = $("#seasonTdId1");//.combobox("getValue")
			var tmpMonthObj= $("#monthTdId1");
			var tmpDayObj= $("#dayTdId1");

			if(!tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.val()&&!tmpDayObj.combobox("getValue")){
				 $("#msgTd").html("&nbsp;");
				 $("#msgTd").append("<font color='red'>时间维度不能都为空！</font>"); 
				return;
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.val()&&!tmpDayObj.combobox("getValue")){
				dateType="1";
			}else if(tmpYearObj.val()&&tmpSeasonObj.combobox("getValue")&&!tmpMonthObj.val()&&!tmpDayObj.combobox("getValue")){
				dateType="2";
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.val()&&!tmpDayObj.combobox("getValue")){
				dateType="3";
			}else if(tmpYearObj.val()&&!tmpSeasonObj.combobox("getValue")&&tmpMonthObj.val()&&tmpDayObj.combobox("getValue")){
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
					}
					if(diArr[i]=="doct_code"){
						if(dimensionValue==""){
							dimensionValue="doct_code,"+docValue
						}else{
							dimensionValue=dimensionValue+"?doct_code,"+docValue
						}
					}
					if(diArr[i]=="reglevl_code"){
						if(dimensionValue==""){
							dimensionValue="reglevl_code,"+docLevelValue
						}else{
							dimensionValue=dimensionValue+"?reglevl_code,"+docLevelValue
						}
					}
				}
			}
			var dateArray = [Number(yearStart),Number(yearEnd),Number(quarterStart),Number(quarterEnd),Number(monthStart),Number(monthEnd),Number(dayStart),Number(dayEnd)];
			window.opener.queryList(dateArray,dateType,dimensionString,dimensionValue);
			window.close();
		}
		
		
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
	    
	    //hedong  复写 数组js数组的indexOf  用于处理数据的删除  begin
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
    	
    	// 清空值 
		function clearVal(selectArr){
			for(var x=0;x<selectArr.length;x++){
				$('#'+selectArr[x]).val('');
			}
		}
    	//hedong 20160817  仿java的Map集合 用于存放科室数据 key=000001.... value=子科室   end
	</script>
</html>
