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
		<div data-options="region:'north',split:false,border:false" style="padding:5px 5px 5px 5px;height:38px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchDgButId" onclick="insearchDg()">查询</a>
			<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">合并</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open'" onclick="openDg()">展开</a>
		</div>
		<div data-options="region:'center',border:false" class="inpatientInfo">
			<div class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'north',split:false,border:false" style="padding:8px 5px 0px 5px;height:70px">
					<table>
						<tr>
							<td style="width:80px" class="inpatientInfoSize1">起始时间:</td>
							<td style="width:220px" class="inpatientInfoSize2">
								<input id="instartTime" class="Wdate" type="text" name="startTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<a href="javascript:void(0)" onclick="delTime('instartTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
							<td style="width:80px" class="inpatientInfoSize3">终止时间:</td>
							<td style="width:220px" class="inpatientInfoSize4">
								<input id="inendTime" class="Wdate" type="text" name="endTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<a href="javascript:void(0)"  onclick="delTime('inendTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
 							<td colspan="2"><!--查询条件选择药品时 需按空格键选择弹出药品 --></td>
						</tr>
						<tr>
							<td style="width:80px" class="inpatientInfoSize5">查询条件：</td>
							<td style="width:200px">
								<select id="inec" class="easyui-combobox" style="width:170px;">   
								    <option value="0">全部</option>   
								    <option value="1">病历号</option>   
								    <option value="2">姓名</option>   
								    <option value="3">住院流水号</option>   
								</select>
							</td>
							<td colspan="2" style="width:270px"><input id="inet" class="easyui-textbox" style="width:242px"/></td>
							<td style="width:80px"><input id="incb" type="checkbox"/>模糊查询</td>
							<td style="width:100px"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">全部合并</a></td>
							
						</tr>
					</table>
						<form id="saveForm" method="post"></form>
						<form method="post" id="reportToHiddenForm" >
						<input type="hidden" name="RECIPE_NO" id="RECIPE_NO" value=""/>
						<input type="hidden" name="fileName" id="reportToFileName" value=""/>
						</form>
				</div>
				<div data-options="region:'center',border:false">
					<table id="edv"></table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
	<script type="text/javascript">
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
	            //url: "<%=basePath%>outpatient/advice/queryInpatientInfoList.action",
	            data:[],
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
	                   	{field:'inpatientNo',title:'住院流水号',hidden:true,align:'center'},
	                   	{field:'name',title:'患者姓名',width:'22%',align:'center'},  
	                    {field:'sex',title:'性别',width:'22%',align:'center',formatter:function(value,row,index){
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
	                    {field:'age',title:'年龄',width:'22%',align:'center',formatter:function(value,row,index){
                    		if(row.age!=null&&row.age!=''){
                    			var aDate=new Date(); 
                    			var thisYear=aDate.getFullYear();
                    			var brith=row.age;
                    			brith=brith.substr(0,4);
                    			age=thisYear-brith;
                    			return age+"岁";
                    		}
	            				
                    	}},  
	                    {field:'recordNo',title:'病历号',width:'22%',align:'center'}
	                ]  
	            ],  
	            detailFormatter:function(index,row){
	                return '<div style="padding:2px"><table id="edvc-' + index + '"></table></div>';  
	            },  
	            onExpandRow:function(index,row){
	                $('#edvc-'+index).datagrid({  
	                	url: "<%=basePath%>outpatient/advice/queryInpatientAdviceInfo.action",
	                	queryParams:{inpatientNo:row.inpatientNo,tab:row.tab},
	                    rownumbers:true,
	    	            striped:true,
	    	            border:true,
	    	            checkOnSelect:true,
	    	            selectOnCheck:false,
	    	            singleSelect:true,
	                    height:'auto',  
	                    columns:[[  
	                        {field:'typeName',title:'医嘱类型',width:'6%',align:'center'},  
	                    	{field:'itemName',title:'医嘱名称',width:'15%',align:'center'},  
	                    	{field:'combNo',title:'组',width:'10%',align:'center'},  
	                    	{field:'qtyTot',title:'总量',width:'4%',align:'center'},  
	                    	{field:'priceUnit',title:'单位（总量单位）',width:'8%',align:'center'},  
	                    	{field:'drugpackagingUnit',title:'包装单位',width:'6%',align:'center'},  
	                    	{field:'doseOnces',title:'每次量',width:'6%',align:'center',formatter:function(value,row,index){
	                    		if(row.doseOnce!=null&&row.doseOnce!=''&&row.doseUnit!=null&&row.doseUnit!=''){
	                    			return parseFloat(row.doseOnce)+parseFloat(row.doseUnit);
	                    		}
	                    	}},  
	                    	{field:'doseUnit',title:'单位（剂量单位）',width:'8%',align:'center'},  
	                    	{field:'doseOnce',title:'每次剂量',width:'8%',align:'center',formatter:function(value,row,index){
	                    		if(row.doseOnce!=null && row.doseOnce!=''){
	                    			return parseFloat(row.doseOnce);
	                    		}
	                    	}},  
	                    	{field:'useDays',title:'付数',width:'6%',align:'center'},  
	                    	{field:'frequencyName',title:'频次',width:'8%',align:'center'},  
	                    	{field:'useName',title:'用法名称',width:'8%',align:'center'},  
	                    	{field:'dateBgn',title:'开始时间',width:'12%',align:'center'},  
	                    	{field:'dateEnd',title:'停止时间',width:'12%',align:'center'},  
	                    	{field:'moDate',title:'开立时间',width:'12%',align:'center'},  
	                    	{field:'docName',title:'开立医生',width:'6%',align:'center'},  
	                    	{field:'execDpnm',title:'执行科室',width:'8%',align:'center'},  
	                    	{field:'isUrgent',title:'急',width:'6%',align:'center',formatter:function(value,row,index){
		            				if (row.isUrgent==0){
		            					return '普通';
		            				} else if(row.isUrgent==1){
		            					return '加急';
		            				}else{
		            					return '未知';
		            				}
	                    	}},  
	                    	{field:'labCode',title:'样本类型',width:'6%',align:'center'},  
	                    	{field:'itemNote',title:'检查部位',width:'8%',align:'center'},  
	                    	{field:'pharmacyCode',title:'扣库科室',width:'6%',align:'center'},  
	                    	{field:'moNote2',title:'备注',width:'6%',align:'center'},  
	                    	{field:'recUsernm',title:'录入人',width:'8%',align:'center'},  
	                    	{field:'listDpcdName',title:'开立科室',width:'8%',align:'center'}, 
	                    	{field:'dcUsernm',title:'停止人',width:'6%',align:'center'},  
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
	                    	{field:'sortId',title:'顺序号',width:'6%',align:'center'}
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
			//bindEnterEvent('et',searchDg,'easyui');
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
			var startTime = $('#instartTime').val();
			var endTime = $('#inendTime').val();
			var type = $('#inec').combobox('getValue');
			var para = $('#inet').textbox('getText');
			var vague = 0;
			if($('#incb').is(':checked')){
				vague = 1;
			}
			$('#edv').datagrid({
				url: "<%=basePath%>outpatient/advice/queryInpatientInfoList.action",
				queryParams:{startTime:startTime,endTime:endTime,type:type,para:para,vague:vague}
			});
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
</body>
</html>