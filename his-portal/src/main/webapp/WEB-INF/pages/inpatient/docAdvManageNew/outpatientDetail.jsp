<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:false,border:false" style="padding:5px 5px 5px 5px;height:36px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchDgButId" onclick="insearchDg()">查询</a>
			<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">合并</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open'" onclick="openDg()">展开</a>
		</div>
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'north',split:false,border:false" style="padding:0px 5px 5px 5px;height:70px">
					<table>
						<tr>
							<td style="width:70px" class="inpatientInfoSize1">起始时间:</td>
							<td style="width:200px" class="inpatientInfoSize2">
							<input id="instartTime" class="Wdate" type="text" name="startTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:160px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<a href="javascript:void(0)" onclick="delTime('instartTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td style="width:70px" class="inpatientInfoSize3">-终止时间:</td>
							<td style="width:200px" class="inpatientInfoSize4">
							<input id="inendTime" class="Wdate" type="text" name="endTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:160px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<a href="javascript:void(0)"  onclick="delTime('inendTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
 							<td colspan="2"><!--查询条件选择药品时 需按空格键选择弹出药品 --></td>
						</tr>
						<tr>
							<td style="width:70px" class="inpatientInfoSize5">查询条件：</td>
							<td style="width:200px">
								<input id="inec" class="easyui-combobox" style="width:170px;"/>   
							</td>
							<td colspan="2" style="width:270px"><input id="inet" class="easyui-textbox" style="width:242px"/></td>
							<td style="width:80px"></td>
							<td style="width:100px"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">全部合并</a></td>
							
						</tr>
					</table>
						<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="RECIPE_NO" id="RECIPE_NO" value=""/>
						<input type="hidden" name="fileName" id="reportToFileName" value=""/>
						</form>
				</div>
				<div data-options="region:'center',border:false" class="outpatientDetail">
					<table id="edv"></table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
	<script type="text/javascript">
	loadAjax();//单位渲染
	
	
	var startTime="${startTime}";
	var endTime="${endTime}";	
		$(function(){
			$('#edv').datagrid({  
	            view:detailview,
	            fit:true,
	            rownumbers:true,
	            striped:true,
	            border:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
	            pagination:true,
	            pageNumber:1,
	            pageSize:20,
	            pageList:[10,20,30,40,50],
	            url: "<%=basePath%>inpatient/docAdvManage/queryoutPatient.action",
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
	            columns: [  
	                [  	{field:'ck',checkbox:true},
	                   	{field:'clinicCode',title:'门诊号',width:'10%',align:'center'},
	                    {field:'midicalrecordId',title:'病历号',width:'22%',align:'center'},
	                   	{field:'patientName',title:'患者姓名',width:'22%',align:'center'},  
	                    {field:'patientSexName',title:'性别',width:'10%',align:'center'},  
	                    {field:'regDate',title:'挂号时间',width:'22%',align:'center'}
	                ]  
	            ],  
	            detailFormatter:function(index,row){
	                return '<div style="padding:2px"><table id="edvc-' + index + '"></table></div>';  
	            },  
	            onExpandRow:function(index,row){
	                $('#edvc-'+index).datagrid({  
	                	url: "<%=basePath%>inpatient/docAdvManage/queryOutpatientRecipedetail.action",
	                	queryParams:{clinicCode:row.clinicCode,tab:row.tab,startTime:startTime,endTime:endTime},
	                    rownumbers:true,
	    	            striped:true,
	    	            border:true,
	    	            checkOnSelect:true,
	    	            selectOnCheck:false,
	    	            singleSelect:true,
	                    height:'auto',  
	                    columns:[[  
	                    	{field:'itemName',title:'医嘱名称',width:'15%',align:'center'},  
	                    	{field:'combNo',title:'组',width:'10%',align:'center'},  
	                    	{field:'qty',title:'总量',width:'4%',align:'center'},  
	                    	{field:'itemUnit',title:'单位（总量单位）',width:'8%',align:'center',formatter:function(value,row,index){
	                    		if(value!=null&&value!=''){
	                    			if(drugpackagingunitMap[value]){
	                    				return drugpackagingunitMap[value];
	                    			}else if(mindataMaps[value]){
	                    				return mindataMaps[value];
	                    			}else{
	                    				return value;
	                    			}
	                    		}
	                    	}}, 
	                    	{field:'onceDose',title:'每次量',width:'6%',align:'center',formatter:function(value,row,index){
	                    		if(value!=null&&value!=''&&row.onceUnitName!=null&&row.onceUnitName!=''){
	                    			return parseFloat(value)+row.onceUnitName;
	                    		}
	                    	}},  
	                    	{field:'onceUnitName',title:'单位（剂量单位）',width:'8%',align:'center'},  
	                    	{field:'days',title:'付数',width:'6%',align:'center'},  
	                    	{field:'frequencyName',title:'频次',width:'8%',align:'center'},  
	                    	{field:'usageName',title:'用法名称',width:'8%',align:'center'},  
	                    	{field:'operDate',title:'开立时间',width:'12%',align:'center'},  
	                    	{field:'doctDpcdName',title:'开立医生',width:'6%',align:'center'},  
	                    	{field:'execDpcdName',title:'执行科室',width:'8%',align:'center'},  
	                    	{field:'emcFlag',title:'急',width:'6%',align:'center',formatter:function(value,row,index){
		            				if (row.emcFlag==0){
		            					return '普通';
		            				} else if(row.emcFlag==1){
		            					return '加急';
		            				}else{
		            					return '未知';
		            				}
	                    	}},  
	                    	{field:'labType',title:'样本类型',width:'6%',align:'center'},  
	                    	{field:'checkBody',title:'检查部位',width:'8%',align:'center'},  
	                    	{field:'phamarcyCode',title:'扣库科室',width:'6%',align:'center',formatter:function(value,row,index){
	                    		if(value!=null&&value!=''){
	                    			return implDepartmentMap[value];
	                    		}
	                    	}},  
	                    	{field:'REMARK',title:'备注',width:'6%',align:'center'},  
	                    	{field:'doctName',title:'录入人',width:'8%',align:'center'},  
	                    	{field:'doctDpcdName',title:'开立科室',width:'8%',align:'center'}, 
	                    	{field:'hypotest',title:'皮试',width:'8%',align:'center',formatter:function(value,row,index){
		            				if (row.hypotest==1){
		            					return '不需要皮试';
		            				} else if(row.hypotest==2){
		            					return '需要皮试，未做';
		            				} else if(row.hypotest==3){
		            					return '皮试阳';
		            				} else if(row.hypotest==4){
		            					return '皮试阴';
		            				} else{
		            					return '未知';
		            				}
	                    	}},  
	                    	{field:'recipeFeeseq',title:'顺序号',width:'6%',align:'center'}
	                    ]],  
	                    onResize:function(){  
	                        $('#edv').datagrid('fixDetailRowHeight',index);  
	                    },  
	                    onLoadSuccess:function(data){
	                    	
	                        setTimeout(function(){  
	                            $('#edv').datagrid('fixDetailRowHeight',index);  
	                        },0);  
	                    }  
	                });  
	                $('#edv').datagrid('fixDetailRowHeight',index);  
	            }  
	        }); 
			 $('#inec').combobox({
				 url: "<%=basePath%>inpatient/docAdvManage/queryAllType.action",   
			     valueField:'value',    
			     textField:'text',   
			     onLoadSuccess:function(){
			    	 var data=$('#inec').combobox("getData");
			    	 for(var i=0;i<data.length;i++){
			    		 if(data[i].value=='0'){
			    			 $('#inec').combobox("select",data[i].value);
			    		 }
			    	 }
			     },
 				 onChange:function(newValue,oldValue){
					 if(newValue=='0'){
						 $('#inet').textbox('setValue',"");
						 $('#inet').textbox('textbox').attr('disabled',true);
					 }else{
						 $('#inet').textbox('textbox').attr('disabled',false);
					 }
				 }
			 });
		});
		/**  
		 *  
		 * 展开
		 * @Author：gaotiantian
		 * @CreateDate：2017-3-31 下午02:05:31  
		 * @Modifier：gaotiantian
		 * @ModifyDate：2017-3-31 下午02:05:31  
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
		 * @Author：gaotiantian
		 * @CreateDate：2017-3-31 下午02:05:31  
		 * @Modifier：gaotiantian
		 * @ModifyDate：2017-3-31 下午02:05:31  
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
		 * @Author：gaotiantian
		 * @CreateDate：2017-3-31 下午02:05:31 
		 * @Modifier：gaotiantian
		 * @ModifyDate：2017-3-31 下午02:05:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function insearchDg(){
			 startTime = $('#instartTime').val();
			 endTime = $('#inendTime').val();
			var type = $('#inec').combobox('getValue');
			var para = $('#inet').textbox('getText');
			var vague = 0;
			$('#edv').datagrid('load',{startTime:startTime,endTime:endTime,type:type,condition:para,vague:vague});
		}
		/**
		 * 重置
		 * @author gaotiantian
		 * @date 2017-3-31 下午02:05:31
		 * @version 1.0
		 */
		function clears(){
			$('#instartTime').val('${startTime}');
			$('#inendTime').val('${endTime}');
			$('#inec').combobox('setValue','全部');
			$('#inet').textbox('setValue','');
			insearchDg();
		}
		/**  
		 *  
		 * 清空时间查询
		 * @Author：gaotiantian
		 * @CreateDate：2017-3-31 下午02:05:31
		 * @Modifier：gaotiantian
		 * @ModifyDate：2017-3-31 下午02:05:31 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function delTime(id){
			$('#'+id).val('');
		}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>