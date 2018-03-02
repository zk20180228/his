<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:false,border:false" style="height:40px;padding:8px 5px 0px 5px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" onclick="printDg()">打印</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">合并</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open'" onclick="openDg()">展开</a>
<!-- 			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onclick="closeTab()">退出</a> -->
			<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
			<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
			<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
			<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
			<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
			<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
			<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
		</div>
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'north',split:false,border:false" style="padding:0px 5px 5px 5px">
					<table>
						<tr>
							<td style="width:85px" class="recipelStatListSize1">起始日期:</td>
							<td style="width:160px">
<!-- 								<span style="margin-right:50"> -->
									<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:120px;border: 1px solid #95b8e7;border-radius: 5px;"/>
<!-- 									<a href="javascript:void(0)" onclick="delTime('startTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>-->
<!-- 								</span> -->
							</td>
							<td style="width:22px" align="left">至</td>
							<td style="width:140px" align="left" class="synEndTime">
							<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:120px;border: 1px solid #95b8e7;border-radius: 5px;"/>
<!-- 							<a href="javascript:void(0)"  onclick="delTime('endTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>-->
 <!--  							<td colspan="4">查询条件选择药品时 需按空格键选择弹出药品</td> -->
						</tr>
						<tr>
							<td style="width:85px" class="recipelStatListSize1">查询条件:</td>
							<td style="width:120px">
								<select id="ec" class="easyui-combobox" style="width:120px;">   
								    <option value="0">全部</option>   
								    <option value="1">病历号</option>   
								    <option value="2">发票号</option>   
								    <option value="3">姓名</option>   
								    <option value="4">处方号</option>   
								</select>
							</td>
							<td colspan="2" style="width:185px" ><input id="et" class="easyui-textbox" style="width:185px"/></td>
							<td style="width:100px">&nbsp;<input id="cb" type="checkbox" style="width:13px;height:13px;margin-top:1;"/><span style="padding:0px 0px 10px 5px;margin-top:-1;">模糊查询</span></td>
<!-- 							<td style="width:100px"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">全部合并</a></td> -->
							<td style="width:80px" class="recipelStatListSize2">&nbsp;处方数:</td>
							<td style="width:60px" id="rs"></td>
							<td>			
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchDgButId" onclick="searchDg()">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
						<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="RECIPE_NO" id="RECIPE_NO" value=""/>
						<input type="hidden" name="fileName" id="reportToFileName" value=""/>
						</form>
				</div>

<!-- 	<div data-options="region:'north',border:false" style="height:60px;padding:5px 5px 0px 5px;"> -->
<!-- 						<table id="searchTab" style="width: 100%;"> -->
<!-- 							<tr> -->
<!-- 								开始时间  -->
<!-- 								<td style="width:50px;" align="right">日期:</td> -->
<!-- 								<td style="width:110px;"> -->
<%-- 									<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:160px;border: 1px solid #95b8e7;border-radius: 5px;"/> --%>
<!-- 								</td> -->
<!-- 								<td style="width:20px;" align="center"> -->
<!-- 									<a href="javascript:void(0)" onclick="delTime('startTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
<!-- 								</td> -->
<!-- 								结束时间  -->
<!-- 								<td style="width:40px;" align="center">至</td> -->
<!-- 								<td style="width:110px;"> -->
<%-- 									<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:160px;border: 1px solid #95b8e7;border-radius: 5px;"/>  --%>
<!-- 								</td> -->
<!-- 								<td style="width:30px;" align="center"> -->
<!-- 									<a href="javascript:void(0)"  onclick="delTime('endTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>								 -->
<!-- 								</td> -->
<!-- 								<td style="width:70px;">查询条件:</td> -->
<!-- 				    			<td style="width:100px;"> -->
<!-- 					    	       	<select id="ec" class="easyui-combobox">    -->
<!-- 								    <option value="0">全部</option>    -->
<!-- 								    <option value="1">病历卡号</option>    -->
<!-- 								    <option value="2">发票号</option>    -->
<!-- 								    <option value="3">姓名</option>    -->
<!-- 								    <option value="4">处方号</option>    -->
<!-- 									</select>  -->
<!-- 					    		</td> -->
<!-- 					    		<td  style="width:120px"><input id="et" class="easyui-textbox" /></td> -->
<!-- 					    		<td><input id="cb" type="checkbox"/></td> -->
<!-- 					    		<td style="width:70px;">模糊查询</td> -->
<!-- 					    		<td style="width:100px"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">全部合并</a></td> -->
					    		
					    		
<!-- 					    		<td style="width:50px;" align="right">处方数:</td> -->
<!-- 				    			<td style="width:50px" id="rs"></td> -->
				    			
<!-- 								<td> -->
<%-- 								<shiro:hasPermission name="${menuAlias}:function:query"> --%>
<!-- 									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchDgButId" onclick="searchDg()">查询</a> -->
<!-- 									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a> -->
<!-- 									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">合并</a> -->
<!-- 									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open'" onclick="openDg()">展开</a> -->
<!-- 									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" onclick="printDg()">打印</a>								 -->
<%-- 								</shiro:hasPermission> --%>
<!-- 								</td> -->
<!-- 								<td style='text-align: right'> -->
<%-- 									<shiro:hasPermission name="${menuAlias}:function:query"> --%>
<!-- 										<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">前三天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">前七天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">前十五天</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a> -->
<!-- 										<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a> -->
<!-- 										&nbsp;&nbsp; -->
<%-- 									</shiro:hasPermission> --%>
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</table> -->
<!-- 						<form id="saveForm" method="post"></form> -->
<!-- 						<form method="post" id="reportToHiddenForm" > -->
<!-- 						<input type="hidden" name="RECIPE_NO" id="RECIPE_NO" value=""/> -->
<!-- 						<input type="hidden" name="fileName" id="reportToFileName" value=""/> -->
<!-- 						</form> -->
<!-- 					</div> -->
				<div data-options="region:'center',border:false"><!-- singleSelect:true, -->
					<table id="edv" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,selectOnCheck:false,fit:true">
						    <thead>   
						        <tr>   
						            <th data-options="field:'recipeNo',width:'190px',align:'center'" checkbox='true'>处方号</th>   
						            <th data-options="field:'name',width:'95px',align:'center'">患者姓名</th>   
						            <th data-options="field:'sex',formatter:functionSex,width:'95px',align:'center'">性别</th> 
						            <th data-options="field:'age',formatter:functionAge,width:'95px',align:'center'">年龄</th>
						            <th data-options="field:'recordNo',width:'190px',align:'center'">病历号</th> 
						            <th data-options="field:'invoiceNo',width:'190px',align:'center'">发票号</th>   
						            <th data-options="field:'disTable',width:'170px',align:'center'">配药台</th> 
						            <th data-options="field:'disUser',width:'95px',align:'center'">配药人</th> 
						            <th data-options="field:'disTime',width:'170px',align:'center'">配药时间</th> 
						            <th data-options="field:'medTable',width:'170px',align:'center'">发药台</th>
						            <th data-options="field:'medUser',width:'95px',align:'center'">发药人</th>
						            <th data-options="field:'medTime',width:'170px',align:'center'">发药时间</th>
						            <th data-options="field:'squareDoc',width:'95px',align:'center'">开方医生</th>   
						        </tr>   
						    </thead> 
					</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
	<script type="text/javascript">
		
		function functionSex(value,row,index){
			if(row.sex!=null&&row.sex!=''){
				if (row.sex==1){
					return '男';
				} else if(row.sex==2){
					return '女';
				}else{
					return '未知';
				}
			}
		}
	   function functionAge(value,row,index){
			if(row.age!=null&&row.age!=''){
				var aDate=new Date(); 
				var thisYear=aDate.getFullYear();
				var brith=row.age;
				brith=brith.substr(0,4);
				age=thisYear-brith;
				return age+"岁";
			}
				
		}
	   
		$(function(){
			var startTime="${startTime}";
			var endTime="${endTime}";
			var date = new Date(endTime);
			date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
			endTime= date.Format("yyyy-MM-dd");
			$('#edv').datagrid({
				view:detailview,
	            fit:true,
	            rownumbers:true,
	            striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true, pagination:true,
	            pageNumber:1,
	            pageSize:20,
	            pageList:[10,20,30,40,50],
	            url: "<%=basePath%>statistics/recipelStat/queryPatientInfo.action",
	            queryParams:{startTime:startTime,endTime:endTime,type:null,para:null,vague:0},
	            onLoadSuccess: function(){
	            	//分页工具栏作用提示
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
	            	var rows = $(this).datagrid('getRows');
	            	if(rows!=null){
	            		$('#rs').html(rows.length+" 张");
	            	}else{
	            		$('#rs').html("0 张");
	            	}
	            },
	                   /* 	{field:'ck',checkbox:true}, */
	            /* columns: [  
	                [  	
	                    {field:'recipeNo',title:'处方号',width:'190px',align:'center'},  
	                    {field:'name',title:'患者姓名',width:'95px',align:'center'},  
	                    {field:'sex',title:'性别',width:'95px',align:'center',formatter:function(value,row,index){
                    		if(row.sex!=null&&row.sex!=''){
	            				if (row.sex==1){
	            					return '男';
	            				} else if(row.sex==2){
	            					return '女';
	            				}else{
	            					return '未知';
	            				}
	            			}
                    	}},  
	                    {field:'age',title:'年龄',width:'95px',align:'center',formatter:function(value,row,index){
                    		if(row.age!=null&&row.age!=''){
                    			var aDate=new Date(); 
                    			var thisYear=aDate.getFullYear();
                    			var brith=row.age;
                    			brith=brith.substr(0,4);
                    			age=thisYear-brith;
                    			return age+"岁";
                    		}
	            				
                    	}},  
	                    {field:'recordNo',title:'病历号',width:'190px',align:'center'},  
	                    {field:'invoiceNo',title:'发票号',width:'190px',align:'center'},  
	                    {field:'disTable',title:'配药台',width:'170px',align:'center'},  
	                    {field:'disUser',title:'配药人',width:'95px',align:'center'},  
	                    {field:'disTime',title:'配药时间',width:'95px',align:'center'},  
	                    {field:'medTable',title:'发药台',width:'170px',align:'center'},  
	                    {field:'medUser',title:'发药人',width:'95px',align:'center'},  
	                    {field:'medTime',title:'发药时间',width:'95px',align:'center'},  
	                    {field:'squareDoc',title:'开方医生',width:'95px',align:'center'},  
	                ]  
	            ],  */ 
	            detailFormatter:function(index,row){
	                return '<div style="padding:2px"><table id="edvc-' + index + '"></table></div>';  
	            },  
	            onExpandRow:function(index,row){
					var startTime=$("#startTime").val();
					var endTime=$("#endTime").val();
					var date = new Date(endTime);
					date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
					endTime= date.Format("yyyy-MM-dd");
	                $('#edvc-'+index).datagrid({  
	                	url: "<%=basePath%>statistics/recipelStat/queryRecipelInfo.action",
	                	queryParams:{'recipeNo':row.recipeNo,'startTime':startTime,'endTime':endTime},
	                    rownumbers:true,
	    	            striped:true,
	    	            border:true,
	    	            checkOnSelect:true,
	    	            selectOnCheck:false,
	    	            singleSelect:true,
	                    height:'auto',  
	                    columns:[[  
	                        {field:'goodsName',title:'商品名',width:'270px',halign:'center',align:'right'},  
	                        {field:'oneDosage',title:'每次量',width:'90px',halign:'center',align:'right'},  
	                        {field:'usage',title:'用法',width:'200px',halign:'center',align:'right'},  
	                        {field:'frequency',title:'频次',width:'100px',halign:'center',align:'right'},  
	                        {field:'gross',title:'总量',width:'80px',halign:'center',align:'right'},  
	                        {field:'retailPrice',title:'零售价',width:'80px',halign:'center',align:'right'},  
	                        {field:'money',title:'金额',width:'80px',halign:'center',align:'right'},  
	                        {field:'dosageNum',title:'剂数',width:'80px',halign:'center',align:'right'},  
	                        {field:'validity',title:'有效性',width:'100px',halign:'center',align:'right',formatter:function(value,row,index){
			            			//有效标记（1有效，0无效，2不摆药）
	                        		if(row.validity!=null&&row.validity!=''){
			            				if (row.validity==1){
			            					return '有效';
			            				} else if(row.validity==2){
			            					return '不摆药';
			            				}else{
			            					return '无效';
			            				}
			            			}
	                        	}
	                        }
	                    ]],  
	                    onResize:function(){  
	                        $('#edv').datagrid('fixDetailRowHeight',index);  
	                    },  
	                    onLoadSuccess:function(){  
	                        setTimeout(function(){  
	                            $('#edv').datagrid('fixDetailRowHeight',index);  
	                        },0);  
	                    }  
	                });  
	                $('#edv').datagrid('fixDetailRowHeight',index);  
	            }  
	        });  
			bindEnterEvent('et',searchDg,'easyui');
		});
		
		/**  
		 *  
		 * 展开
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function openDg(){
			var rows = $('#edv').datagrid('getRows')
			if(rows!=null&&rows.length>0){
				for(var i=0;i<rows.length;i++){
					$('#edv').datagrid('expandRow',i);
				}
			}
		}
		
		/**  
		 *  
		 * 关闭
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function foldDg(){
			var rows = $('#edv').datagrid('getRows')
			if(rows!=null&&rows.length>0){
				for(var i=0;i<rows.length;i++){
					$('#edv').datagrid('collapseRow',i);
				}
			}
		}
		
		/**  
		 *  
		 * 查询
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function searchDg(){
			var startTime = $('#startTime').val();
			var endTime = $('#endTime').val();
			var date = new Date(endTime);
			date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
			var end = date.Format("yyyy-MM-dd");
			searchFinal(startTime,end);
			 $('#endTime').val(endTime);
		}
		function searchFinal(Stime,Etime){
			var startTime = $('#startTime').val();
			var endTime = $('#endTime').val();
		if(startTime!=null&&startTime!=''&&endTime!=null&&endTime!=''){
			if(Stime&&Etime){
		          if(Stime>Etime){
		            $.messager.alert("提示","开始时间不能大于结束时间！");
		            return ;
		          }
		        }
			$('#startTime').val(Stime);
			$('#endTime').val(Etime);
			var type = $('#ec').combobox('getValue');
			var para = $('#et').textbox('getText');
			var vague = 0;
			if($('#cb').is(':checked')){
				vague = 1;
			}
			$('#edv').datagrid('load',{startTime:Stime,endTime:Etime,type:type,para:para,vague:vague});
		}else{
			$.messager.alert("提示","挂号日期不能为空！");
		}
	}
		//距离当前多少天的日期
		 function GetDateStr(AddDayCount) {
			 var dd = new Date();
			 dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
			 var y = dd.getFullYear();
			 var m = dd.getMonth()+1;//获取当前月份的日期
			 if(Number(m)<10){
		         m = "0"+m;
		       }
		       var d = dd.getDate();
		       if(Number(d)<10){
		         d = "0"+d;
		       }
			 return y+"-"+m+"-"+d;
		}
		 //查询当天
		function searchOne(){
			var Stime = GetDateStr(0);
			var Etime = GetDateStr(1);
			$('#startTime').val(Stime);
			$('#endTime').val(GetDateStr(0));
			searchFinal(Stime,Etime);
			$("#endTime").val(GetDateStr(0));
		 }
		 //查询前三天
		function searchThree(){
			var Stime = GetDateStr(-3);
			var Etime = GetDateStr(0);
			$('#startTime').val(Stime);
			$('#endTime').val(GetDateStr(-1));
			searchFinal(Stime,Etime);
			$("#endTime").val(GetDateStr(-1));
		}
		//查询前七天
		function searchSeven(){
			var Stime = GetDateStr(-7);
			var Etime = GetDateStr(0);
			$('#startTime').val(Stime);
			$('#endTime').val(GetDateStr(-1));
			searchFinal(Stime,Etime);
			$("#endTime").val(GetDateStr(-1));
		}
		//查询前15天 zhangkui 2017-04-17
		function searchFifteen(){
			var Stime = GetDateStr(-15);
			var Etime = GetDateStr(0);
			$('#startTime').val(Stime);
			$('#endTime').val(GetDateStr(-1));
			searchFinal(Stime,Etime);
			$("#endTime").val(GetDateStr(-1));
		}
		
		//上月
		function beforeMonth(){
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth();
            var nowMonth = month;
            var nowYear = year;
            if(month==0)
            {
                month=12;
                nowMonth = "01";
                year=year-1;
            }
            if (month < 10) {
                nowMonth = "0" +(month+1);
                month = "0" + month;
            }
            var Stime = year + "-" + month + "-" + "01";//上个月的第一天
            var lastDate = new Date(year, month, 0);
            var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
            var Etime= nowYear+"-"+nowMonth+"-01";

            $('#startTime').val(Stime);
            $('#endTime').val(lastDay);
            searchFinal(Stime,Etime);
            $('#endTime').val(lastDay);
		}
		
		//日期格式转换
		Date.prototype.Format = function(fmt)   
		{   
		  var o = {   
		    "M+" : this.getMonth()+1,                 //月份   
		    "d+" : this.getDate(),                    //日   
		    "h+" : this.getHours(),                   //小时   
		    "m+" : this.getMinutes(),                 //分   
		    "s+" : this.getSeconds(),                 //秒   
		    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
		    "S"  : this.getMilliseconds()             //毫秒   
		  };   
		  if(/(y+)/.test(fmt))   
		    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		  for(var k in o)   
		    if(new RegExp("("+ k +")").test(fmt))   
		  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
		  return fmt;   
		} 
		//获取每月第一天
		function getCurrentMonthFirst(){
			 var date=new Date();
			 date.setDate(1);
			 return date.Format("yyyy-MM-dd");
		}
		//获取每月最后一天
		function getCurrentMonthLast(){
			 var date=new Date();
			 var currentMonth=date.getMonth();
			 var nextMonth=++currentMonth;
			 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
			 var oneDay=1000*60*60*24;
			 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
		} 
		//查询当月
		function searchMonth(){
			var Stime = getCurrentMonthFirst();
			//var Etime = getCurrentMonthLast();
			//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
			//2017-04-17新的
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Etime = GetDateStr(1);
			$('#startTime').val(Stime);
			$('#endTime').val(GetDateStr(0));
			searchFinal(Stime,Etime);
			$("#endTime").val(GetDateStr(0));
		}
		//查询当年
		function searchYear(){
			//var Etime = new Date().getFullYear()+"-12-31";
			//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
			//2017-04-17新的
// 			var date=new Date();
// 			var Etime = date.Format("yyyy-MM-dd");
			var Etime = GetDateStr(1);	
			var Stime = new Date().getFullYear()+"-01-01";
			$('#startTime').val(Stime);
			$('#endTime').val(GetDateStr(0));
			searchFinal(Stime,Etime);
			$("#endTime").val(GetDateStr(0));
		}
		
		
		
		/**
		 * 重置
		 * @author huzhenguo
		 * @date 2017-03-17
		 * @version 1.0
		 */
		function clears(){
			$('#startTime').val('${startTime}');
			$('#endTime').val('${endTime}');
			$('#ec').combobox('setValue','全部');
			$('#et').textbox('setValue','');
			$("#cb").attr("checked", false);
			searchDg();
		}
		/**  
		 *  
		 * 清空时间查询
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function delTime(id){
			$('#'+id).val('');
		}
		
		/**  
		 *  
		 * 打印
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		 function printDg(){
				var rows = $('#edv').datagrid('getChecked');
				var row_s = $('#edv').datagrid('getRows');
			if(row_s!=null&&row_s!=''){
				var ids="";  
				if(rows!=null&&rows.length>0){
				$.messager.confirm('确认', '确定要打印吗?', function(res) {  //提示是否打印假条
					if (res){
							for(var i=0;i<rows.length;i++){
								if(ids!=""){
									ids+=",";
								}
								ids+=rows[i].recipeNo;
							}
							//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
						    //给表单的隐藏字段赋值
						    $("#RECIPE_NO").val(ids);
						    $("#reportToFileName").val("newOutpatientStatAdviceList");
							var startTime = $('#startTime').val();
							var endTime = $('#endTime').val();
							var date = new Date(endTime);
							date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
							endTime= date.Format("yyyy-MM-dd");
						    //表单提交 target
						    var formTarget="hiddenFormWin";
					        var tmpPath = "<%=basePath%>statistics/recipelStat/iReportToStatAdvice.action?startTime="+startTime+"&endTime="+endTime;
					        //设置表单target
					        $("#reportToHiddenForm").attr("target",formTarget);
					        //设置表单访问路径
							$("#reportToHiddenForm").attr("action",tmpPath); 
					        //表单提交时打开一个空的窗口
						    $("#reportToHiddenForm").submit(function(e){
						    	 var timerStr = Math.random();
								 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
							});  
						    //表单提交
						    $("#reportToHiddenForm").submit();
								
								}
							});
					}else{
						$.messager.alert('提示','请选择要打印的处方！');
					}
			}else{
				$.messager.alert("提示","没有数据，不能打印！"); 	
				}
		}
		
		
		/**  
		 *  
		 * 退出
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function closeTab(){
			window.parent.$('#tabs').tabs('close','门诊处方查询');
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>