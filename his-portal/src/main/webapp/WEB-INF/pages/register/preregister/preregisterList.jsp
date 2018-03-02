<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>预约挂号</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var midDayMap=new Map();
	$.ajax({
	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",
		type:'post',
		success: function(data) {
			var type = data;
			for(var i=0;i<type.length;i++){
				midDayMap.put(type[i].encode,type[i].name);
			}
		}
	});
</script>
</head>
<body>
<div id="listall" class="easyui-layout" data-options="fit:true,border:false" >   
    <div data-options="region:'west',split:true,border:false" style="width:410px;height:100%">
	    	<form id="editForm" method="post">
				<input type="hidden" id="id" name="id"value="${registerPreregister.id }">
					<table class="honry-table" style="width: 100%;border-top:0">
						<tr>
							<td style="border-top:0">
							<table id="listw" class="honry-table" style=" border: 1px solid #95b8e7;width: 100%;">
								<tr align="center">
									<td>
										<a href="javascript:void(0)" onclick="searchFromcx()"
											class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<shiro:hasPermission name="${menuAlias}:function:add">
										<a href="javascript:submit();void(0)" class="easyui-linkbutton"
											data-options="iconCls:'icon-save'">保存</a>
									</shiro:hasPermission>
										<a href="javascript:clear();void(0)" class="easyui-linkbutton"
											data-options="iconCls:'icon-clear'">清除</a>
									</td>
								</tr>
							</table>
							</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td> -->
<!-- 							<table id="listww" class="honry-table" style=" border: 1px solid #95b8e7;width: 100%"> -->
<!-- 								<tr align="center"> -->
<!-- 									<td> -->
<!-- 										预约方式： -->
<!-- 										<input id="isnetHidden" type="hidden" value="0" -->
<!-- 											name="preregisterIsnet" value="preregisterIsnet" /> -->
<!-- 										<input id="wl" type="radio" name="fs" value="0" -->
<!-- 											onclick="checkwl('wl')"> -->
<!-- 										网络 -->
<!-- 										<input id="isphoneHidden" type="hidden" value="0" -->
<!-- 											name="preregisterIsphone" value="preregisterIsphone"/> -->
<!-- 										<input id="dh" type="radio" name="fs" value="0" -->
<!-- 											onclick="checkdh('dh')"> -->
<!-- 										电话 -->
<!-- 										<input id="jzk" type="radio" name="fs" value="1" -->
<!-- 											onclick="checkjzk('jzk')"> -->
<!-- 										就诊卡 -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</table>  -->
<!-- 							</td> -->
<!-- 						</tr> -->
						<tr>
							<td>
							<table id="yyfs" class="honry-table" cellpadding="1" cellspacing="1" style=" border: 1px solid #95b8e7;width: 100%">
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										卡号：
									</td>
									<td>
									    <input type="hidden" name="scheduleId"  id="scheduleIdhidden">
									    <input type="hidden" name="midday"  id="middayhidden">
									     <input type="hidden" name="preregisterMiddayname"  id="preregisterMiddayname">
									    <input type="hidden" name="idcard" id="idCardNoHidden" >
									    <input type="hidden" name="idcardId" id="idHidden" >
										<input id="idcardNo" class="easyui-textbox" />
									</td>
								</tr>
								<tr>
									<td style="font-size: 14" class="honry-lable">
										病历号：
									</td>
									<td>
										<input id="blh" class="easyui-textbox" name="medicalrecordId">
									</td>
								<tr>
									<td style="font-size: 14" class="honry-lable">
										建卡时间：
									</td>
									<td  style="font-size: 14">
										<input id="jksj" readonly="readonly" class="Wdate" type="text" data-options="showSeconds:true ">
									</td>
								</tr>
								<tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										姓名：
									</td>
									<td  style="font-size: 14">
										<input id="xingming" class="easyui-textbox" 
											name="preregisterName" data-options="required:true"
											missingMessage="请输入拼姓名"  />
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14"class="honry-lable">
										性别：
									</td>
									<td  style="font-size: 14">
										<input type="hidden" id="sexHid" name="preregisterSex">
										<input id="preregisterSex"  class="easyui-combobox"name="sexS"  data-options="">
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										年龄：
									</td>
									<td  style="font-size: 14">
										<input style="width:95px"id="nianling" class="easyui-numberbox"
											name="preregisterAge">
										<input style="width:100px;"id="ageUnits" class="easyui-combobox"
											name="preregisterAgeunit">
<!-- 											<input id="ageUnits" type="hidden" name="preregisterAgeunit"> -->
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										联系电话：
									</td>
									<td  style="font-size: 14">
										<input id="lxdh" class="easyui-numberbox" name="preregisterPhone">
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										通讯地址：
									</td>
									<td  style="font-size: 14">
										<input id="dizhi" class="easyui-textbox"
											name="preregisterAddress">
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										证件类型：
									</td>
									<td  style="font-size: 14">
										<input id="zjlxHidden" name="preregisterCertificatesname" type="hidden">
										<input id="zjlx" class="easyui-combobox"
											name="preregisterCertificatestype">
									</td>
								</tr>
								<tr  style="font-size: 14">
									<td class="honry-lable">
										证件号码：
									</td>
									<td  style="font-size: 14">
										<input id="zjhm" class="easyui-textbox" style="width:150px"
											data-options="required:true" name="preregisterCertificatesno">
									</td>
								</tr>
								<tr>
									<td   style="font-size: 14" class="honry-lable">
										预约时间：
									</td>
									<td  style="font-size: 14">
<!-- 										<input id="time" class="Wdate" onchange="startTime()" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d+1}',maxDate:'%y-%M-{%d+7}'})"  style="width: 150px;border: 1px solid #95b8e7;border-radius: 5px;" name="preregisterDate" data-options="required:true"> -->
										<input id="time" class="Wdate"onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:startTime,minDate:'%y-%M-{%d+1}',maxDate:'%y-%M-{%d+7}'})"  style="width: 150px;border: 1px solid #95b8e7;border-radius: 5px;" name="preregisterDate" data-options="required:true">
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										挂号级别：
									</td>
									<td  style="font-size: 14">
										<input id="jbHidden" name="preregisterGradeName" type="hidden">
										<input id="jb" name="preregisterGrade" class="easyui-combobox" data-options="required:true">
										<a href="javascript:clearSelectData('jb');"  class="easyui-linkbutton" data-options=" iconCls:'icon-opera_clear',plain:true"></a>
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										挂号科室：
									</td>
									<td  style="font-size: 14">
									   
										<input id="ksHidden" name="preregisterDept" type="hidden">
										<input id="ksNameHidden" name="preregisterDeptName" type="hidden">
										<input id="ks" class="easyui-combobox"  data-options="required:true">
										<a href="javascript:clearSelectData('ks');"  class="easyui-linkbutton" data-options=" iconCls:'icon-opera_clear',plain:true"></a>
										
									</td>
								</tr>
								<tr>
									<td   style="font-size: 14"class="honry-lable">
										挂号专家：
									</td>
									<td  style="font-size: 14">
										<input id="zjHidden" type="hidden" name="preregisterExpertName">
										<input id="zj"  name="preregisterExpert"  class="easyui-combogrid"  data-options="required:true">
									</td>
								</tr>
								
								
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										开始时间：
									</td>
									<td  style="font-size: 14" >
										<input id="kssj" class="easyui-timespinner" data-options="required:true" name="preregisterStarttime"  >
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										结束时间：
									</td>
									<td  style="font-size: 14">
										<input id="jssj"  class="easyui-timespinner" data-options="required:true" name="preregisterEndtime">
									</td>
								</tr>
							</table>
							</td>
						</tr>					
					</table>
				</form>
    </div>   
    <div data-options="region:'center',border:false">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north'" style="width:100%;height:65px;border-top:0">
				<div style="width:1335px;height:20px;margin-top:17px;margin-left:5px">
				<input type="hidden" id="preregisterparameter" name="preregisterparameter" value="${preregisterparameter }">
							查询：<input style="width:130px" class="easyui-textbox" ID="preregisterNo" name="preregisterNo" data-options="prompt:'预约号，病历号，姓名，证件号'" />
							&nbsp;挂号科室：<input style="width:150px" class="easyui-combobox" id="ghks" data-options="prompt:'回车查询'"/>
							&nbsp;挂号级别：<input style="width:150px" id="ghjb" data-options="prompt:'回车查询'"><a href="javascript:delSelectedDatas('ghjb');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							&nbsp;挂号专家：<input style="width:150px" class="easyui-combogrid" id="ghzj" data-options="prompt:'输入查询'"/>
							&nbsp;是否取号：<input style="width:110px" class="easyui-combobox" id="sfqh" data-options="prompt:'回车查询'" value="2"/>
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()"
								class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</shiro:hasPermission>
				</div>
				
    		</div>   
    		<div data-options="region:'center'" style="width:72%;height:80%;">
				<table id="list" data-options="url:' ${pageContext.request.contextPath}/outpatient/preregister/queryPreregister.action?menuAlias=${menuAlias}',checkOnSelect:false,selectOnCheck:false,singleSelect:true  ,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,singleSelect:false,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th field="getIdUtil" checkbox="true"></th>
							<th data-options="field:'preregisterNo'">预约号</th>
							<th data-options="field:'preregisterName'">姓名</th>
							<th data-options="field:'preregisterAge'">年龄</th>
							<th data-options="field:'preregisterAgeunit'">年龄单位</th>
							<th data-options="field:'preregisterPhone'">联系电话</th>
							<th data-options="field:'preregisterCertificatesno'">证件号码</th>
							<th data-options="field:'preregisterDate'" formatter="formatPreregisterDate">预约时间</th>
							<th data-options="field:'preregisterMiddayname'">预约午别</th>
							<th data-options="field:'preregisterIsphone'" formatter="formatCheckBox">是否电话预约</th>
							<th data-options="field:'preregisterIsnet'" formatter="formatCheckBox">是否网络预约</th>
							<th data-options="field:'preregisterDeptName'" >挂号科室</th>
							<th data-options="field:'preregisterGradeName'" formatter="formatGrade" >挂号级别</th>
							<th data-options="field:'preregisterExpertName'"  >挂号专家 </th>
							<th data-options="field:'seeFlag' "  formatter="formatCheckBoxzt">看诊状态</th>
							<th data-options="field:'appFlag'  "  formatter="formatCheckBoxjh">是否加号</th>
							<th data-options="field:'status' "  formatter="formatCheckBoxatatus">状态</th>
							<th data-options="field:'isFirst' "  formatter="formatCheckBoxbz">初复诊标识</th>
							<th data-options="field:'sourceType'">订单来源</th>
							<th data-options="field:'orderNo'">第三方订单号</th>
						</tr>
					</thead>
				</table>
    		</div>   
    	</div>
    </div>   
</div>  
<div id="toolbarId">
<shiro:hasPermission name="${menuAlias}:function:delete">
	<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</shiro:hasPermission>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	var deptMap="";//部门
	var gradeMapg = null;//挂号级别
	var prereMap = new Map();//已预约
	var grade =new Array;//挂号级别
 	$(function(){
 		$.ajax({
			url: "<%=basePath%>outpatient/grade/getGradeMap.action",
			type:'post',
			success: function(gradeData) {
				gradeMapg = gradeData;
			}
		});	
		$.ajax({
			url: "<%=basePath%>register/newInfo/querygradeComboboxs.action",
			type:'post',
			success: function(gradeData) {
				gradeMap =gradeData;
			}
		});	
		$.ajax({
			url: "<%=basePath%>outpatient/preregister/likeTitle.action",
			type:'post',
			success: function(gradeData) {
				grade =gradeData;
			}
		});	
		
		$.ajax({
			url: "<%=basePath%>outpatient/newChangeDeptLog/querydeptComboboxs.action",
			type:'post',
			success: function(deptData) {
				deptMap = deptData;
			}
		});	
		
		$('#list').datagrid({
			pagination:true,
			fitColumns:true,
	   		pageSize:20,
	   		pageList:[20,30,50,100],
			onLoadSuccess: function (data) {//默认选中
				$(this).datagrid('clearChecked');
	           var rowData = data.rows;
	            $.each(rowData, function (index, value) {
	            	if(value.id == id){
	            		$("#list").datagrid("checkRow", index);
	            	}
	            });
	        },
	    	onLoadSuccess : function(data){
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
				for(var i=0;i<aArr.length;i++){
					$(aArr[i]).tooltip({
						content:toolArr[i],
						hideDelay:1
					});
					$(aArr[i]).tooltip('hide');
				}
			}
		});
		
 	        $('#blh').textbox({ readonly:true});
            $('#xingming').textbox({ readonly:true});
            $('#preregisterSex').combobox({ readonly:true});
            $('#nianling').textbox({ readonly:true});
            $('#ageUnits').combobox({ readonly:true});
            $('#dizhi').textbox({ readonly:true});
            $('#zjlx').combobox({ readonly:true});
            $('#zjhm').textbox({ readonly:true});
            $('#lxdh').numberbox({ readonly:true});
          
				$.extend($.fn.validatebox.defaults.rules, {     
				     xingming: {     
				         validator: function(value, param){  //姓名验证   
				             return value.length >= param[0] && value.length <= param[1];     
				         },     
				         message: '输入内容必须为2~4字符之间'    
				     },
				     lxdh: {// 验证电话号码
				        validator: function (value) {
				            return /^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/; 
				        }
				    },
				      zjhm: {
						    validator: function(value){
						    	// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
						   		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
							    if(reg.test(value))
							    {
							      return true;
							    }else{
							       return false;
							    }
						    },
						    message: '请输入正确身份证格式'
					  }
				});
				$('#jzk').attr("checked", true); 
				bindEnterEvent('idcardNo',searchFromcx,'easyui');
				bindEnterEvent('preregisterNo',searchFrom,'easyui');
				bindEnterEvent('sfqh',searchFrom,'easyui');
				
			
			/**
			 * 回车弹出科室选择窗口
			 * @author  wanxing
			 * @date 2016-03-22 14:30 
			 * @version 1.0
			 */
			 bindEnterEvent('ghks',popWinToDept,'easyui');//绑定回车事件
			 function popWinToDept(){
				 	$('#ghks').combobox('setValue','');
					var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=ghks";
					window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) +',scrollbars,resizable=yes,toolbar=yes')
			}
		});
			 
		//为复选框赋值   - 就诊卡    		
		function checkjzk(jzk){
            var check = document.getElementById(jzk);
            var preregisterSex= $('#preregisterSex').combobox('getValue');
			var ageUnits= $('#ageUnits').combobox('getValue');
			var zjlx=$('#zjlx').combobox('getValue');
           	$('#lxdh').numberbox({required:false});
 	        $('#blh').textbox({ readonly:true});
            $('#xingming').textbox({ readonly:true});
            $('#preregisterSex').combobox({readonly:true});
            $('#nianling').textbox({ readonly:true});
            $('#ageUnits').combobox({ readonly:true});
            $('#dizhi').textbox({ readonly:true});
            $('#zjlx').combobox({ readonly:true});
            $('#zjhm').textbox({ readonly:true});
            $('#lxdh').numberbox({ readonly:true});
            if(preregisterSex){
          	   $('#preregisterSex').combobox('setValue',preregisterSex);
             }
            if(ageUnits){
           	   $('#ageUnits').combobox('setValue',ageUnits);
              }
            if(zjlx){
            	   $('#zjlx').combobox('setValue',zjlx);
               }
           	$('#isnetHidden').val(0);
       	 	$('#isphoneHidden').val(0);
       	 	clearDate();
           
       	}
		//为复选框赋值   - 网络  	
       function checkwl(wl){
    	   clearDate();
             var check = document.getElementById(wl);
             if(check.checked=true){
            	 var preregisterSex= $('#preregisterSex').combobox('getValue');
     			var ageUnits= $('#ageUnits').combobox('getValue');
     			var zjlx=$('#zjlx').combobox('getValue');
            	 $('#lxdh').numberbox({required:true});
            	 $('#blh').textbox({ readonly:false});
	            $('#xingming').textbox({ readonly:false});
	            $('#preregisterSex').combobox({readonly:false});
	            $('#nianling').textbox({ readonly:false});
	            $('#ageUnits').combobox({ readonly:false});
	            $('#dizhi').textbox({ readonly:false});
	            $('#zjlx').combobox({ readonly:false});
	            $('#zjhm').textbox({ readonly:false});
	            $('#lxdh').numberbox({ readonly:false});
	            if(preregisterSex){
	           	   $('#preregisterSex').combobox('setValue',preregisterSex);
	              }
	             if(ageUnits){
	            	   $('#ageUnits').combobox('setValue',ageUnits);
	               }
	             if(zjlx){
	             	   $('#zjlx').combobox('setValue',zjlx);
	                }
            	 $('#isnetHidden').val(1);
            	 $('#isphoneHidden').val(0);
             }else{
            	 $('#lxdh').numberbox({required:false});
            	 $('#isnetHidden').val(0);
            	 $('#isphoneHidden').val(0);
             }
       }
       
       
       //为复选框赋值   - 电话    	
	   function checkdh(dh){
	        var check = document.getElementById(dh);
            if(check.checked=true){
            	clearDate();
           	 var preregisterSex= $('#preregisterSex').combobox('getValue');
  			var ageUnits= $('#ageUnits').combobox('getValue');
  			var zjlx=$('#zjlx').combobox('getValue');
            	$('#lxdh').numberbox({ readonly:false});
           	 	$('#lxdh').numberbox({required:true});
           	 	 $('#blh').textbox({ readonly:false});
	            $('#xingming').textbox({ readonly:false});
	            $('#preregisterSex').combobox({ readonly:false});
	            $('#nianling').textbox({ readonly:false});
	            $('#ageUnits').combobox({ readonly:false});
	            $('#dizhi').textbox({ readonly:false});
	            $('#zjlx').combobox({ readonly:false});
	            $('#zjhm').textbox({ readonly:false});
	            if(preregisterSex){
		           	   $('#preregisterSex').combobox('setValue',preregisterSex);
		              }
		             if(ageUnits){
		            	   $('#ageUnits').combobox('setValue',ageUnits);
		               }
		             if(zjlx){
		             	   $('#zjlx').combobox('setValue',zjlx);
		                }
           		$('#isphoneHidden').val(1);	
           		$('#isnetHidden').val(0);
            }else{
           	 	$('#lxdh').numberbox({required:false});
           	 	$('#isphoneHidden').val(0);	
           		$('#isnetHidden').val(0);
            }
	   }
       
       function clearDate(){
    	   $('#time').val('');
    	   $('#jb').combobox('setValue','');
		   $('#ks').combobox('setValue','');
    	   $('#zj').combogrid('setValue','');
    	   $('#kssj').timespinner('setValue','');
		   $('#jssj').timespinner('setValue','');
		   $('#zj').combogrid('grid').datagrid('reload',{
	    		time:$('#time').val(),
	    		gradeid:$('#jb').datebox('getValue'),
	    		deptid:$('#ks').datebox('getValue')
	    	});
       }
       
		//证件类型
		$('#zjlx') .combobox({ 
		    url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",      
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    editable:false,
		    onChange:function(post){
	    		$('#zjlx').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>");
	    	}
		    
		});
		//适用性别
		$('#preregisterSex').combobox({
			url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=sex'/>",
		    valueField:'encode',    
		    textField:'name',
		    editable:false
		});
		/*	var sexHid = $('#sexHid').val();
		 if(sexHid!=null&&sexHid!=""){
			$('#preregisterSex').combobox('setValue',sexHid);
		}  */
			
		//年龄单位
		$('#ageUnits') .combobox({  
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=ageunits",
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    editable:false,
		    width:'50px',
		    onChange:function(post){
	    		$('#ageUnits').combobox('reload');
	    	}
		    
		});
		
		setTimeout(function(){
		$('#ghzj') .combogrid({   
			 url : "<%=basePath%>outpatient/preregister/employeeComboboxQuery.action",
			    editable:true,
			    mode:'remote',
			    disabled : false,
			    queryParams:{deptId:$('#ghks').combobox('getValue'),grade:$('#ghjb').combobox('getValue')},
				rownumbers : true,//显示序号 
				pagination : true,//是否显示分页栏
				striped : true,//数据背景颜色交替
				panelWidth : 900,//容器宽度
				panelHeight:320,//高度
				fitColumns : true,//自适应列宽
				pageSize : 10,//每页显示的记录条数，默认为10  
				pageList : [ 10, 20 ],//可以设置每页记录条数的列表  
				idField : 'jobNo',
				textField : 'name',
				columns : [ [ {
					field : 'jobNo',
					title : '员工号',
					width : 200
				},{
					field : 'name',
					title : '挂号专家',
					width : 200
				}, {
					field : 'title',
					title : '挂号级别',
					width : 200
				}, {
					field : 'departmentId',
					title : '挂号科室',
					width : 200,
					formatter: function(value,row,index){
						if(value!=null&&value!=''){
							return deptMap[value];
						} 
					}
				}
				] ]
	    });
	    },1);
		//查询下拉 级别
		$('#ghjb') .combobox({    
			url: "<%=basePath%>register/newInfo/gradeCombobox.action",   
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    editable:true,
		    onSelect: function(data){
		    	$('#ghzj').combogrid('setValue','');
		    	var deptId=$('#ghks').combobox('getValue');
		    	$('#ghzj').combogrid('grid').datagrid('load', {"deptId":deptId,"grade":data.encode});
		    	
		    }
		});
		/**
		 * 挂号级别弹出事件高丽恒 原挂号级别id为ghjb
		 * 2016-03-25 09:32
		 */
		bindEnterEvent('ghjb',openBusinessJbWin,'easyui');//挂号级别
		function openBusinessJbWin(){
			popWinGradeCallBackFn = function(node){
		    	$('#ghjb').combobox('setValue',node.encode);
			};
			var tempWinPath = "<%=basePath%>popWin/popWinGrade/popWinRegisterGradeBedList.action?classNameTmp=RegisterGrade&textId=ghjb";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
		}
		//查询下拉 科室
		$('#ghks') .combobox({  
		url : "<%=basePath%>outpatient/preregister/deptComboBox.action",  
		    valueField:'deptCode',    
		    textField:'deptName',
		    multiple:false,
		    editable:true,
		    filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'deptCode';
				keys[keys.length] = 'deptName';
				keys[keys.length] = 'deptPinyin';
				keys[keys.length] = 'deptWb';
				keys[keys.length] = 'deptInputcode';
				return filterLocalCombobox(q, row, keys);
		    },
		    onSelect: function(data){
		    	$('#ghzj').combogrid('setValue','');
		    	var grade=$('#ghjb').combobox('getValue');
		    	$('#ghzj').combogrid('grid').datagrid('load', {"deptId":data.deptCode,"grade":grade});
		    	
		    }
		});
		
		//查询下拉 科室
		$('#sfqh') .combobox({  
			valueField: 'id',
       		textField: 'value',
       		data: [{id: '1',value: '是'},
       		       {id: '2',value: '否'}],
		    multiple:false
		});
		
        //为复选框赋值       
		function formatCheckBox(val,row){
			if (val == 1){
				return '是';
			} else {
				return '否';
			}
		}	
        
        //渲染预约时间
		function formatPreregisterDate(val,row){
			if(val!=null&&val!=''){
				return val.substring(0,10);
			}else{
				return val;
			}
        	
		}	
        
        
        
        //为复选框赋值       
		function formatCheckBoxzt(val,row){
			if (val == 1){
				return '已看';
			} else {
				return '未看';
			}
		}	
        //为复选框赋值       
		function formatCheckBoxjh(val,row){
			if (val == 1){
				return '是';
			} else {
				return '否';
			}
		}	
		function formatCheckBoxatatus(val,row){
			if (val == 1){
				return '有效';
			}
			else if(val==2){
			    return '无效';
			} 
			else if(val==3){
			    return '取号';
			}
			else if(val==4){
			    return '爽约';
			}  
			else {
				return '停诊';
			}
		}	
		
		function formatGrade(val,row,index){
			if(typeof(val)=='undefined'|| val==""){
				var gradeDoc=row.preregisterGrade;
				if(typeof(gradeDoc)!='undefined' && gradeDoc!=''){
					return gradeMapg[gradeDoc];
				}else{
				    return val;
				}
			}else{
				return val;
			}
		}
		
		function formatCheckBoxbz(val,row){
			if (val == 1){
				return '初';
			} else {
				return '复';
			}
		}	
		function formatCheckBoxwub(value,row,index){
			     if(value!=null&&value!=''){
						return midDayMap.get(value);
					}
		}	
		
	
		function del(){
			var rows = $('#list').datagrid('getChecked');
			if(rows!=null&&rows!=''){
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
					if(res){
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.ajax({
							url : "<%=basePath%>outpatient/preregister/delPreregister.action?id="+ids,
								type:'post',
								success: function() {
									$.messager.alert('提示','删除成功');
									setTimeout(function(){
										  $(".messager-body").window('close');  
										},3500);
									$('#list').datagrid('reload');
								}
						});	
					}
				});
			}else{
				$.messager.alert('提示',"请选择要删除的信息!");
			}
				
		}
		//提交验证>
		function submit(){
			 $.ajax({
				url : "<%=basePath %>outpatient/preregister/queryPreregisterBymid.action",
					type:'post',
					 data:{number:$('#zjhm').textbox('getValue'),dept:$('#ks').combobox('getValue'),date:$('#time').val()},
					success: function(Objstr) {
						if(Objstr=="0"){
							 if($('#idCardNoHidden').val()==$('#idcardNo').val()){
								$('#editForm').form('submit', {
									url : "<%=basePath%>outpatient/preregister/editInfoPreregister.action",
									data:$('#editForm').serialize(),
							        dataType:'json',
									onSubmit : function() {
										if(!$('#editForm').form('validate')){
											$.messager.show({  
										         title:'提示信息' ,   
										         msg:'验证没有通过,不能提交表单!'  
										    }); 
										       return false ;
										       
										}
									},
									success:function(data){
										var dataMap = eval("("+data+")");
										var msg=dataMap.resCode;
							          	$.messager.alert('提示',msg);
										if(dataMap.resMsg=="success"){
									   		//实现刷新
							          		$("#list").datagrid("reload");
							          		setTimeout(function(){
										  		$(".messager-body").window('close');  
											},3500);
							          		clear();
							         		$('#jzk').attr("checked", true);
							             	$('#isnetHidden').val(0);
						            	 	$('#isphoneHidden').val(0);
										}
									 }
								}); 
									}else{
										$.messager.alert('提示','就诊卡号已更改');
								} 
						}else if(Objstr=="1"){
							$.messager.alert('提示',"患者每天预约不能超过两次");
							 clear();
							
						}else{
							$.messager.alert('提示',"患者同一科室只能预约一次");
							$('#jb').combobox('setValue','');
						    $('#ks').combobox('setValue','');
						    $('#zj').combogrid('setValue','');
						    $('#kssj').timespinner('setValue','');
							$('#jssj').timespinner('setValue','');
							$('#time').val('');
						}
					
					}
			}); 
		
		}
		//查询
		function searchFrom(){
			var preregisterExpert =$('#ghzj').combogrid('getValue'); 
	        var preregisterNo =	$('#preregisterNo').val(); 
	        var preregisterDept =$('#ghks').combobox('getValue');  
	        var preregisterGrade =$('#ghjb').combobox('getValue'); 
	        var takeNumber =$('#sfqh').combobox('getValue'); 
			$('#list').datagrid('load', {
					pNo: preregisterNo,
					pDept: preregisterDept,
					pGrade: preregisterGrade,
					pExpert: preregisterExpert,
					takeNum:takeNumber
			});
		}	
		

		
		function clear(){
			$('#editForm').form('clear');
		}
		
		//查询卡号
		function searchFromcx(){
			var idcardNo = $('#idcardNo').val();
			if(idcardNo==null||$.trim(idcardNo)==''){
				$.messager.alert('提示',"请录入就诊卡号");
				$("#idcardNo").textbox('textbox').focus();
				return;
			}
			$.ajax({
				url : "<%=basePath%>outpatient/preregister/queryCardIdByBlack.action?idcardNo="+idcardNo,
					type:'post',
					success: function(data) {
						if(data==1){
							$.messager.alert('提示',"注意:此患者已被记录在黑名单中!");
							clear();
							return;
						}
						$.ajax({
							url : "<%=basePath%>patient/idcard/queryCcardId.action?idcardNo="+idcardNo,
								type:'post',
								success: function(idCardObj) {
									if(idCardObj.idcardCreatetime!=null&&idCardObj.idcardCreatetime!=""){
									 $('#idCardNoHidden').val(idCardObj.idcardNo);
									 $('#idHidden').val(idCardObj.idcardNo);
									 $('#blh').textbox('setValue',idCardObj.patient.medicalrecordId);
									 $('#jksj').val(idCardObj.idcardCreatetime);
									 $('#xingming').textbox('setValue',idCardObj.patient.patientName);
									 $('#preregisterSex').combobox('setValue',idCardObj.patient.patientSex);
									 $('#zjhm').textbox('setValue',idCardObj.patient.patientCertificatesno);
									 $('#lxdh').numberbox('setValue',idCardObj.patient.patientPhone);
									 $('#zjlx').combobox('setValue',idCardObj.patient.patientCertificatestype);
									 $('#dizhi').textbox('setValue',idCardObj.patient.patientAddress);
									 $('#sexHid').val($('#preregisterSex').combobox('getText'));
									 $('#zjlxHidden').val($('#zjlx').combobox('getText'));
									 //得到患者的出生年月日 age
									 var age=idCardObj.patient.patientBirthday;
									 var ages=DateOfBirth(age);
										$('#nianling').textbox('setValue',ages.get("nianling"));
									    $('#ageUnits').combobox('setValue',ages.get("ageUnits"));
										$('#ageUnit').val(ages.get("ageUnit"));
										}else{
											$.messager.alert('提示',"请输入正确的卡号");
										   clear();
										}
									 }
									}); 
					}
				}); 
			
	  		var idcardNo =	$('#idcardNo').val();  
		    $('#xx').datagrid('load', {
				idcardNo: idcardNo
			});
		}	
		 /**
		   * 回车弹出挂号科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上commbox的的id
		   * @param deptType 科室类别
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		
		function popWinToDept(){
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=ks&deptType=C";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
	}	
	
	<%--  /**
	   * 回车弹出挂号专家弹框
	   * @author  zhuxiaolu
	   * @param textId 页面上commbox的的id
	   * @param deptType 科室类别
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	
	  
	   function popWinToEmp(){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=ghzj&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
				
				
		 }
		  --%>
	 //渲染科室
//	   function functionDept(value,row,index){
// 	   	if(value!=null&&value!=''){
// 	   		return deptMap[value];
// 	   	}
// 	   }	
	   //渲染人员
// 	   function functionEmp(value,row,index){
// 	   	if(value!=null&&value!=''){
// 	   		return empMap[value];
// 	   	}
// 	   }	
	   //渲染级别
// 	   function functionGrade(value,row,index){
// 	   	if(value!=null&&value!=''){
// 	   		return gradeMap[value];
// 	   	}
// 	   }
		function delSelectedDatas(data){
			$('#ghjb').combobox('setValue','');
			$('#ghzj').combogrid('setValue','');
			var deptId=$('#ghks').combobox('getValue');
			var grade=$('#ghjb').combobox('getValue');
	    	$('#ghzj').combogrid('grid').datagrid('load', {"deptId":deptId,"grade":grade});
		}
	  function clearSelectData(data){
		  $('#'+data).combobox('setValue','');
  	      $('#zj').combogrid('setValue','');
  	      $('#kssj').timespinner('setValue','');
		  $('#jssj').timespinner('setValue','');
		  var time=$('#time').val();
	      var grade=$('#jb').combobox('getValue');
	      var deptCode=$('#ks').combobox('getValue');
	      $('#zj').combogrid('grid').datagrid('load',{
	    		time:time,
	    		gradeid:grade,
	    		deptid:deptCode
	    	});
	  }
	//重置
	  function searchReload(){
		delSelectedDatas('ghjb');
	  	delSelectedData('ghks');
	  	delSelectedData('preregisterNo');
	  	$("#sfqh").combobox('setValue','2');
	  	$('#list').datagrid('load', {});
	  }
	function startTime(){
			//预约时间预约科室预约级别预约专家
			var date = $('#time').val();
			date = eval('new Date(' + date.replace(/\d+(?=-[^-]+$)/, 
					   function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
		    var net = false;
			var pho = false;
			var no  = true;
				if($('#isnetHidden').val()==1){
					net = true;
					pho = false;
					no  = false;
				}
				if($('#isphoneHidden').val()==1){
					pho = true;
					net = false;
					no  = false;
				 }
		    $('#jb').combobox('setValue','');
		    $('#ks').combobox('setValue','');
		    $('#zj').textbox('setValue','');
		    $('#kssj').timespinner('setValue','');
			$('#jssj').timespinner('setValue','');
		    	 var ys = date.getFullYear();
		    	 var ms = (date.getMonth()+1)<10?"0"+(date.getMonth()+1):date.getMonth()+1;
		    	 var ds = (date.getDate()<10)?"0"+date.getDate():date.getDate();
		         var timechange = ys+"-"+ms+"-"+ds;
			     $('#jb').combobox({    
				 	url : "<%=basePath%>outpatient/preregister/gradeCom.action?time="+timechange,   
				    valueField:'code',    
				    textField:'name',
				    mode:'remote',
				    multiple:false,
				    editable:true,
				    onSelect:function(reJb){
				    $('#zj').combogrid('setValue','');
				    $('#zjHidden').val('');
				    $('#kssj').timespinner('setValue','');
			        $('#jssj').timespinner('setValue','');
			        $('#jbHidden').val(reJb.name);
				    $('#zj').combogrid('grid').datagrid('load',{
				    		time:timechange,
				    		gradeid:reJb.id,
				    		deptid:$('#ks').combobox('getValue')
				    	});
					}
		       	});
		       	$('#ks').combobox({ 
		    		url : "<%=basePath%>outpatient/preregister/getDeptCom.action?time="+timechange,   
				    valueField:'deptCode',    
				    textField:'deptName',
				    mode:'local',
				    multiple:false,
				    editable:true,
				    filter:function(q,row){
						var keys = new Array();
						keys[keys.length] = 'deptCode';
						keys[keys.length] = 'deptName';
						keys[keys.length] = 'deptPinyin';
						keys[keys.length] = 'deptWb';
						keys[keys.length] = 'deptInputcode';
						return filterLocalCombobox(q, row, keys);
				    },
				    onSelect:function(reKs){
				    $('#ksHidden').val(reKs.deptCode);
				    $('#ksNameHidden').val(reKs.deptName);
				    $('#zj').combogrid('setValue','');
				    $('#Hidden').val('');
				    $('#kssj').timespinner('setValue','');
	                $('#jssj').timespinner('setValue','');
				      	$('#zj').combogrid('grid').datagrid('load',{
				    		time:timechange,
				    		gradeid:$('#jb').combobox('getValue'),
				    		deptid:reKs.id
				    	});
				    }
		    		
		    	});
//			       	bindEnterEvent('ks',popWinToDept,'easyui');//绑定回车事件
				  $('#zj').combogrid({ 
						    		url : "<%=basePath%>outpatient/preregister/getEmpCom.action",
						    		queryParams:{time:timechange,gradeid:$('#jb').combobox('getValue'),deptid:$('#ks').combobox('getValue')},
						    		disabled : false,
						    		mode:'remote',
									rownumbers : true,//显示序号 
									pagination : true,//是否显示分页栏
									striped : true,//数据背景颜色交替
									panelWidth : 1200,//容器宽度
									fitColumns : true,//自适应列宽
									pageSize : 5,//每页显示的记录条数，默认为10  
									pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
									idField : 'id',
									textField : 'empName',
									onBeforeLoad:function(param){
										param.gradeid=$('#jb').combobox('getValue');
										param.deptid=$('#ks').combobox('getValue');
									},
									columns : [ [ {
										field : 'empName',
										title : '挂号专家',
										width : 200
									}, {
										field : 'gradeName',
										title : '挂号级别',
										width : 200,
										formatter: function(value,row,index){
											if(value!=null&&value!=''){
												return gradeMapg[value];
											}
										}
									}, {
										field : 'deptName',
										title : '挂号科室',
										width : 200
									}, {
										field : 'midday',
										title : '午别',
										width : 200,
										formatter: function(value,row,index){
											if(value!=null&&value!=''){
												return midDayMap.get(value);
											}
										}
									}, {
										field : 'preL',
										title : '预约总数',
										width : 200
									}, {
									    field : 'netL',
										title : '网络预约',
										width : 200,
										hidden: net?false:true
									}, {
										field : 'phoneL',
										title : '电话预约',
										width : 200,
										hidden: pho?false:true
									} , {
										field : 'nowL',
										title : '现场预约',
										width : 200,
										hidden: no?false:true
									}, {
										field : 'phoneA',
										title : '已预约电话',
										width : 200,
										hidden: pho?false:true
									} , {
										field : 'netA',  
										title : '已预约网络',
										width : 200,
										hidden: net?false:true
									}, {
										field : 'nowA',
										title : '现场已预约',
										width : 200,
										hidden: no?false:true
									}  , {
										field : 'preA',
										title : '已预约总数',
										width : 200
									} , {
										field : 'phonesy',
										title : '电话剩余',
										width : 200,
										hidden: pho?false:true
									} , {
										field : 'netsy',
										title : '网络剩余',
										width : 200,
										hidden: net?false:true
									} , {
										field : 'nowsy',
										title : '现场剩余',
										width : 200,
										hidden: no?false:true
									}, {
										field : 'scheduleId',
										hidden: true,
										formatter: function(value,row,index){

										}
									},{
										field : 'start',
										title : '状态',
										width : 200,
										formatter: function(value,row,index){
												if(net){
													if (row.netsy>0){
														return '未挂满';
													} else {
														return '已挂满';
													}
												}else if(pho){
													if (row.phonesy>0){
														return '未挂满';
													} else {
														return '已挂满';
													}
												}else if(no){
													if (row.nowsy>0){
														return '未挂满';
													} else {
														return '已挂满';
													}
												}else{
													return '';
												}
											}
										}
									] ],
									onSelect:function(rowIndex,rowData){
										if(rowData.netsy>0||rowData.phonesy>0||rowData.nowsy>0){
                                            $('#scheduleIdhidden').val(rowData.scheduleId);
											$('#middayhidden').val(rowData.midday);
											$('#preregisterMiddayname').val(midDayMap.get(rowData.midday));
											$('#middayhidden').val(rowData.midday);
											$('#kssj').timespinner('setValue',rowData.starttime);
											$('#jssj').timespinner('setValue',rowData.enttime);
											$('#ks').combobox('setValue',rowData.deptId);
											$('#jb').combobox('setValue',rowData.gradeName);
											$('#ks').combobox('setText',rowData.deptName);
											$('#ksHidden').val(rowData.deptId);
											$('#ksNameHidden').val($('#ks').combobox('getText'));
											$('#zjHidden').val(rowData.empName);
											var grd=rowData.gradeName;
											if(typeof(grd)!='undefined'&&grd!=''){
											$('#jbHidden').val(gradeMapg[grd]);
											}
										}else{
											$.messager.alert('提示',"请选择号源未满的专家进行挂号");
											 $('#zj').combogrid('setValue','');
											
										}
										
									}
									
						    	});
        
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>
