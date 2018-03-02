<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
    <body style="margin: 0px;padding: 0px;">
        <input type="hidden" id="itemId" name="itemCode" value="">
        <div id="el" class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%">   
            <div data-options="region:'north',split:false,border:false" style="height:80px">
                <form id="editForm" action="" method="post" style="border: none;width:100%;">
                    <table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%;border: false;" >
                  <input type="hidden" id="masterId" name="masterId" value="">
                  <input type="hidden" id="saveTemp" name="saveTemp" value="">
                  <input type="hidden" id="itemInfoJson" name="itemInfoJson" value="">
                  			<tr>
								<td colspan="10">
									    查询条件：
									<input class="easyui-textbox" id="queryName"
										style="width:250px;" />
									&nbsp;
									<a href="javascript:void(0)" onclick="searchList()"
										class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									&nbsp;
									<a href="javascript:submit(0);" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">保存</a>
									<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
									<a href="javascript:submit(1);" class="easyui-linkbutton"  data-options="iconCls:'icon-clear'">暂存</a>
									<a href="javascript:searchPlan();" class="easyui-linkbutton"  data-options="iconCls:'icon-search'">提取暂存数据</a>
								</td>
							</tr>
                        <tr>
                            <td class="honry-lable" style="font-size: 14">
             采购计划流水号:
                            </td>
                            <td>
                                <input id="procurementNo" name="procurementNo" class="easyui-textbox" data-options="required:true," missingMessage="请输入采购计划流水号">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
               采购科室:
                            </td>
                            <td>
                                <input id="procurementDept" name="procurementDept" class="easyui-combobox" data-options="required:true,">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
               供货商:
                            </td>
                            <td>
                                <input id="companyCode" name="companyCode" class="easyui-combobox">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
                采购项目:
                            </td>
                            <td>
                                <input id=tradeName name="tradeName" onkeydown="KeyDown(0)" class="easyui-combobox">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
              采购预算金额:
                            </td>
                            <td>
                                <input id="budgetMoney" class="easyui-numberbox" name="budgetMoney" data-options="min:0,precision:2">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>   
            <div data-options="region:'center',border:false" style="width:100%;height:100%">
                <table id="itemInfo" class="easyui-datagrid" data-options="fit:true,toolbar:'#toolbarId',border:false">
                </table>
            </div> 
        </div>
        <div id="selectMedical"></div>
        <script type="text/javascript">
            var manufacturer = self.parent.manufacturer;
            var reasons = '';
            var depts = ''
            var rIndex;
            var wIndex;
          //定义全局变量记录总的待出库信息条目
    		var totalRows = "";
            $(function(){
				$("#itemInfo").edatagrid({
					checkOnSelect:true,
					selectOnCheck:true,
					singleSelect:false,
					fit:true,
					autoSave:true,
					onBeforeLoad:function(){
						$('#itemInfo').edatagrid('clearChecked');
					},
					onDblClickRow:function(index, row){
						$('#itemInfo').edatagrid('beginEdit',index);
					},
					columns : [ [ {
						field : "ck",
						checkbox : "true"
					} , {
						title : 'id',
						field:'id',
						hidden:true
					} , {
						title : '采购流水号',
						field:'procurementNo',
						width:'10%'
					} , {
						title : '序号',
						field:'serialNo',
						width:'5%',
						editor : {
							type : 'numberbox',
							options : {min:0,required:true}
						}
					} , {
						title : '项目代码',
						field:'itemCode',
						width:'10%'
					} , {
						title : '项目名称',
						field:'itemName',
						width:'10%'
					} , {
						title : '物品科目编码',
						field:'kindCode',
						hidden:true
					} , {
						title : '物品科目',
						field:'kindName',
						width:'10%'
					} , {
						title : '规格',
						field:'specs',
						width:'5%'
					} , {
						title : '最小单位',
						field:'minUnit',
						width:'5%'
					} , {
						title : '大包装单位',
						field:'packUnit',
						width:'5%'
					} , {
						title : '大包装数量',
						field:'packQty',
						width:'5%'
					} , {
						title : '采购数量',
						field:'procurementNum',
						width:'5%',
						editor : {
							type : 'numberbox',
							options : {min:0,required:true}
						}
					} , {
						title : '采购价格',
						field:'procurementPrice',
						width:'5%'
					} , {
						title : '零售价格',
						field:'salePrice',
						width:'5%'
					} , {
						title : '生产商编码',
						field:'producerCode',
						hidden:true
					} , {
						title : '生产商',
						field:'producerName',
						width:'10%'
					} 
					]]
				});
                
                $('#procurementNo').textbox({
                    onChange: function(newValue, oldValue){
                    	if(newValue != null && newValue != ''){
		                   	$.ajax({
		                           url: '<%=basePath%>mat/plan/findProcurementNo.action',
		                           type:'post',
		                           data:{'procurementNo':newValue,'id':$('#masterId').val()},
		                           success: function(data) {
		                               if(data.total>0){
		                                   $.messager.alert('提示',"您输入的采购计划流水号已存在，请重新输入！");
		                                   setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
		                               	$('#procurementNo').textbox('clear');
		                               }
		                           }
		                       });
                    	}
                    }
                });
                //获取供货商的下拉框
                $("#companyCode").combobox({
                    url: "<%=basePath%>material/orderCompany/companyList.action", 
                    valueField:'companyCode',    
                    textField:'companyName',
                    multiple:false,
                    onLoadSuccess : function(data){
                   		reasons = data;
                   	}
                });
                //获取下科室的下拉框
                $("#procurementDept").combobox({
                    url:"<%=basePath%>baseinfo/department/departmentCombobox.action",
                    valueField:'deptCode',    
                    textField:'deptName',
                   	onLoadSuccess : function(data){
                   		depts = data;
                   	}
                });
                
                //获取项目的下拉框
                $("#tradeName").combobox({
                    url:"<%=basePath%>mat/plan/queryItem.action",
                    mode:'remote',
                    valueField:'itemCode',    
                    textField:'itemName',
                    onSelect: function(rec){   
                        var rows = $("#itemInfo").edatagrid('getRows');
                        for(var i = 0; i < rows.length; i++){
                            if(rows[i].itemCode == rec.itemCode){
                                $.messager.alert('提示',"你选择的药品已存在！请点击修改");
                                setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
                                return false;
                                }
                        }
                        if(!$('#editForm').form('validate')){
                        	$.messager.alert('提示',"请补全信息！");
                        	$("#tradeName").combobox('setValue','');
                            setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
                            return false;
                        }
                        $('#itemId').val(rec.itemCode);
                        var itemId=$('#itemId').val();
                        var reflag = 0;
                        var whflag = 0;
                   		rIndex = $('#itemInfo').edatagrid('appendRow',{
                   			procurementNo : $('#procurementNo').val(),
                           	itemCode : rec.itemCode,
                           	itemName: rec.itemName,
                           	kindCode : rec.kindCode,
                            specs: rec.spec,    
                            minUnit: rec.minUnit,
                            packUnit: rec.packUnit,  
                            packQty:rec.packQty,
                            procurementPrice:rec.procurementPrice,
                            producerCode:rec.producerCode,
                        }).datagrid('getRows').length-1;
                   		$('#itemInfo').edatagrid('beginEdit',rIndex);
                    }
                });
            }); 
            
            function del(){
                var item = $('#itemInfo').edatagrid('getSelections');
                if (item.length > 0) {
                    for (var i = item.length - 1; i >= 0; i--) {
                        var index = $('#itemInfo').edatagrid('getRowIndex', item[i]);
                        $('#itemInfo').edatagrid('deleteRow', index);
                    }
                }else{
                	$.messager.alert('提示',"请选中要删除的记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                	return false;
                }
            }
            
            function submit(flg){
            	var item = $('#itemInfo').edatagrid('getSelections');
                if (item.length > 0) {
	                $('#itemInfo').edatagrid('acceptChanges');
	                $('#itemInfoJson').val(JSON.stringify( $('#itemInfo').edatagrid("getRows")));
	                $('#saveTemp').val(flg);
	                
	                
	                $('#editForm').form('submit',{  
	                    url:"<%=basePath%>mat/plan/savePlan.action",
	                    onSubmit:function(){
	                    	var rows = $('#itemInfo').edatagrid("getRows");
	                    	for(var i = 0 ; i < rows.length; i++){
	                    		var index = $('#itemInfo').edatagrid("getRowIndex", rows[i]);
	                    		if(!$('#itemInfo').edatagrid("validateRow", index)){
	                    			$.messager.alert('提示',"请确认信息输入正确！");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
	                            	return false;
	                    		}
	                    	}
	                        $.messager.progress({text:'保存中，请稍后...',modal:true});    // 显示进度条
	                    },  
	                    success:function(){
	                        $.messager.progress('close');
	                        $.messager.alert('提示',"保存成功！"); 
	                        clear();
	                        var item = $('#itemInfo').edatagrid('getRows');
	                        if (item) {
	                            for (var i = item.length - 1; i >= 0; i--) {
	                                var index = $('#itemInfo').edatagrid('getRowIndex', item[i]);
	                                $('#itemInfo').edatagrid('deleteRow', index);
	                            }
	                        }
	                        $('#itemInfo').edatagrid('loadData', { total: 0, rows: [] }); 
	                        var rows = $('#itemInfo').datagrid("getRows");
	        				totalRows = cloneObject(rows);
	                    },
	                    error : function(data) {
	                        $.messager.progress('close');
	                        $.messager.alert('提示',"保存失败！"); 
	                    }   
	                }); 
                }else{
                	$.messager.alert('提示',"请选中一条信息！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                	return false;
                }
                
                
            }       
            function KeyDown(flg,tag){
                if(flg==1){//回车键光标移动到下一个输入框
                    if(event.keyCode==13){  
                        event.keyCode=9;
                    }
                }
            }
            function clear(){
                $('#editForm').form('reset');
            }
            var win;    
            function showWin (title,url, width, height) {
                var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
                var divContent = '<div id="treeDeparWin">';
                win = $('<div id="treeDeparWin"><div/>').dialog({
                    content: content,
                    width: width,
                    height: height,
                    modal: true,
                    resizable:true,
                    shadow:true,
                    center:true,
                    title: title
                });
                win.dialog('open');
            }
            
            function manufacturerFamater(value,row,index){
                if(value!=null&&value!=''){
                    return manufacturer[value];
                }
            }
            
            function searchPlan(){
            	var tempWinPath = "<%=basePath%>mat/plan/findZCPlanURL.action";
                var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
            }
        //查询暂存列表
        function searchPlanList(){
        	var queryName = $.trim($('#queryName').textbox('getValue'));
        	$.ajax({
        		url:"<%=basePath %>mat/plan/viewPlamList.action?id="+$("#masterId").val()+"&queryName="+queryName,
				type:'post',
				success: function(data) {
					var dataMasetr=data.matPlanMaster;
					$('#procurementNo').textbox('setValue',dataMasetr.procurementNo);
					$('#procurementDept').combobox('setValue',dataMasetr.procurementDept);
					$('#companyCode').combobox('setValue',dataMasetr.companyCode);
					$('#budgetMoney').numberbox('setValue',dataMasetr.budgetMoney);
					for(var i = 0; i < data.rows.length; i++){
						var dataZB=data.rows[i];
						rIndex = $('#itemInfo').edatagrid('appendRow',{
							id : dataZB.id,
	               			procurementNo : dataZB.procurementNo,
	               			serialNo : dataZB.serialNo,
	                       	itemCode : dataZB.itemCode,
	                       	itemName: dataZB.itemName,
	                       	kindCode : dataZB.kindCode,
	                        specs: dataZB.spec,    
	                        minUnit: dataZB.minUnit,
	                        packUnit: dataZB.packUnit,  
	                        packQty: dataZB.packQty,
	                        procurementNum : dataZB.procurementNum,
	                        procurementPrice: dataZB.procurementPrice,
	                        producerCode: dataZB.producerCode,
	                    }).datagrid('getRows').length-1;
	               		$('#itemInfo').edatagrid('beginEdit',rIndex);
	               		var rows = $('#itemInfo').datagrid("getRows");
	    				totalRows = cloneObject(rows);
					}
				}
			});
        }
      //js克隆对象 
		function cloneObject(obj) {
			var o = obj.constructor === Array ? [] : {};
			for ( var i in obj) {
				if (obj.hasOwnProperty(i)) {
					o[i] = typeof obj[i] === "object" ? cloneObject(obj[i])
							: obj[i];
				}
			}
			return o;
		}
     	// 列表查询重置
    	function searchReload() {
    		$('#queryName').textbox('setValue','');
    		$('#itemInfo').datagrid('loadData',[]);
    		searchPlanList();
    	}
    	//页面查询检索
		var arr=[];
		function searchList(){
			var queryName = $.trim($("#queryName").textbox('getValue'));
			if ((queryName == null || queryName == "")) {
				//显示全部
				searchReload();
			} else {
				if (queryName != null && queryName != "") {
					$('#itemInfo').datagrid('loadData',[]);
					for ( var i = 0; i < totalRows.length; i++) {
						var itemName = totalRows[i].itemName;
						var searchItem= itemName.indexOf(queryName) == -1;
						//不匹配数据，执行删除
						if (searchItem) {
							var dataZB=totalRows[i];
							rIndex = $('#itemInfo').edatagrid('appendRow',{
								id : dataZB.id,
		               			procurementNo : dataZB.procurementNo,
		               			serialNo : dataZB.serialNo,
		                       	itemCode : dataZB.itemCode,
		                       	itemName: dataZB.itemName,
		                       	kindCode : dataZB.kindCode,
		                        specs: dataZB.spec,    
		                        minUnit: dataZB.minUnit,
		                        packUnit: dataZB.packUnit,  
		                        packQty: dataZB.packQty,
		                        procurementNum : dataZB.procurementNum,
		                        procurementPrice: dataZB.procurementPrice,
		                        producerCode: dataZB.producerCode,
		                    }).datagrid('getRows').length-1;
		               		$('#itemInfo').edatagrid('beginEdit',rIndex);
						}
					}
				}
			}
		}
        </script>
        <script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
    </body>
</html>