<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
    <body style="margin: 0px;padding: 0px;">
        <input type="hidden" id="drugId" name="drugCode" value="">
        <div id="el" class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:100%">   
            <div data-options="region:'north',split:false,border:false" style="height:153px">
                <form id="editForm" action="" method="post" style="border: none;width:100%;">
                    <table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%;border: false;" >
                  <input type="hidden" id="id" name="id" value="">
                  <input type="hidden" id="drugInfoJson" name="drugInfoJson" value="">
                        <tr>
                            <td class="honry-lable" style="font-size: 14">
             调价单据号:
                            </td>
                            <td>
                                <input id="adjustBillCode" name="drugAdjustPrice.adjustBillCode" class="easyui-textbox" data-options="required:true," missingMessage="请输入调价单据号">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
              调价方式:
                            </td>
                            <td>
                                <input id="adjustMode" name="adjustModes"  data-options="valueField:'id',textField:'text',data:type,required:true"  missingMessage="请输入调价方式" >
                            </td>
                            <td class="honry-lable" style="font-size: 14">
             调价执行时间:
                            </td>
                            <td>
                           		<input id="inureTime" name="drugAdjustPrice.inureTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
                               <!--   <input id="inureTime" name="drugAdjustPrice.inureTime" class="easyui-datebox">-->
                            </td>
                            <td class="honry-lable" style="font-size: 14">
              调价依据:
                            </td>
                            <td>
                                <input id="CodeAdjustreason" name="drugAdjustPrice.adjustReason" class="easyui-combobox">
                            </td>
                        </tr>
                        <tr>
                            <td class="honry-lable" style="font-size: 14">
               库房:
                            </td>
                            <td>
                                <input id="drugDept" name="drugAdjustPrice.drugDept" class="easyui-combobox">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
                药品名:
                            </td>
                            <td>
                                <input id="tradeName" name="tradeName" onkeydown="KeyDown(0)" class="easyui-combobox">
                                <a href="javascript:delSelectedData('tradeName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
                            </td>
                            <td class="honry-lable" style="font-size: 14">
              调价后零售价格:
                            </td>
                            <td>
                                <input id="retailPrice" class="easyui-numberbox" name="retailPrice" data-options="min:0,precision:2">
                            </td>
                            <td class="honry-lable" style="font-size: 14">
              调价后批发价格:
                            </td>
                            <td>
                                <input id="wholesalePrice" class="easyui-numberbox" name="wholesalePrice" data-options="min:0,precision:2">
                            </td>
                        </tr>
                        <tr>
                            <td class="honry-lable" style="font-size: 14">
            备注:
                            </td>
                            <td colspan="7">
                                <input id="remark" name="drugAdjustPrice.remark" class="easyui-textbox" style="width: 350px;height: 49px">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>   
            <div data-options="region:'center',border:false" style="width:100%;height:100%">
                <table id="drugInfo" class="easyui-datagrid" data-options="fit:true,toolbar:'#toolbarId',border:false">
                </table>
            </div> 
        </div>
        <div id="selectMedical"></div>
        <div id="toolbarId">
	        <a href="javascript:submit();" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">保存</a>
	        <a href="javascript:clear();" class="easyui-linkbutton"  data-options="iconCls:'icon-clear'">清除</a>
	        <a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">移除</a>
        </div>
        <script type="text/javascript">
            var type = [{"id":1,"text":"零售调价"},{"id":2,"text":"批发调价"}];
            var manufacturer = self.parent.manufacturer;
            var reasons = '';
            var depts = ''
            var rIndex;
            var wIndex;
            $(function(){
				$("#drugInfo").edatagrid({
					checkOnSelect:true,
					selectOnCheck:true,
					singleSelect:false,
					fit:true,
					autoSave:true,
					onBeforeLoad:function(){
						$('#drugInfo').edatagrid('clearChecked');
					},
					onDblClickRow:function(index, row){
						$('#drugInfo').edatagrid('beginEdit',index);
					},
					columns : [ [ {
						field : "ck",
						checkbox : "true"
					} , {
						title : '药品code',
						field:'drugCode',
						hidden:true
					} , {
						title : '调价单号',
						field:'adjustBillCode',
						width:'10%'
					} , {
						title : '药品名称',
						field:'tradeName',
						width:'10%'
					} , {
						title : '调价方式',
						field:'adjustMode',
						width:'10%',
						formatter: function(value,row,index){
							if (value == 1){
								return "零售调价";
							}
							if(value == 2){
								return "批发调价";
							}
						}
					} , {
						title : '调价执行时间',
						field: 'inureTime',
						width:'10%',
						editor : {
							type : 'datebox',
							options : {required:true}
						}
					} , {
						title : '调价依据',
						field:'adjustReason',
						width:'10%',
						formatter: function(value,row,index){
							for(var i = 0; i < reasons.length; i ++){
								if(reasons[i].encode = value){
									return reasons[i].name;
								}
							}
						},
						editor : {
							type : 'combobox',
							options : {
								url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=adjustReason',
								valueField:'encode',
								textField:'name',
								required:true}
							}
					} , {
						title : '库房',
						field:'drugDept',
						width:'10%',
						formatter: function(value,row,index){
							for(var i = 0; i < depts.length; i ++){
								if(depts[i].deptCode = value){
									return depts[i].deptName;
								}
							}
						},
						editor : {
							type : 'combobox',
							options : {
								url:'<%=basePath%>drug/adjustPriceInfo/getDrugDept.action',
								valueField:'deptCode',
								textField:'deptName',
								required:true}
							}
					} , {
						title : '原零售价',
						field:'preRetailPrice',
						width:'7%'
					} , {
						title : '新零售价',
						field:'retailPrice',
						width:'7%',
						editor : {
							type : 'numberbox',
							options : {min:0,precision:2}
							}
					} , {
						title : '原批发价',
						field:'preWholesalePrice',
						width:'7%'
					} , {
						title : '新批发价',
						field:'wholesalePrice',
						width:'7%',
						editor : {
							type : 'numberbox',
							options : {min:0,precision:2}
							}
					} , {
						title : '备注',
						field:'remark',
						width:'10%',
						editor : {
							type : 'textbox'
						}
					}
					]]
				});
                //调价方式的下拉框
                $("#adjustMode").combobox({
                    multiple:true
                });
                
                $('#adjustBillCode').textbox({
                    onChange: function(newValue, oldValue){
                    	if(newValue != null && newValue != ''){
		                   	$.ajax({
		                           url: '<%=basePath%>drug/adjustPriceInfo/findByAdjustCode.action',
		                           type:'post',
		                           data:{'id':newValue},
		                           success: function(data) {
		                               if(data.total>0){
		                                   $.messager.alert('提示',"您输入的调价单号已存在，请重新输入！");
		                                   setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
		                               	$('#adjustBillCode').textbox('clear');
		                               }
		                           }
		                       });
                    	}
                    }
                });
                //从编码表获取调价依据的下拉框
                $("#CodeAdjustreason").combobox({
                    url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=adjustReason", 
                    valueField:'encode',    
                    textField:'name',
                    multiple:false,
                    onLoadSuccess : function(data){
                   		reasons = data;
                   	}
                });
                //获取药库下科室的下拉框
                $("#drugDept").combobox({
                    url:"<%=basePath%>drug/adjustPriceInfo/getDrugDept.action",
                    valueField:'deptCode',    
                    textField:'deptName',
                   	onLoadSuccess : function(data){
                   		depts = data;
                   	}
                });
                
                //获取药品的下拉框
                $("#tradeName").combobox({
                    url:"<%=basePath%>drug/adjustPriceInfo/queryDrug.action",
                    mode:'remote',
                    valueField:'code',    
                    textField:'name',
                    onSelect: function(rec){   
                        var rows = $("#drugInfo").edatagrid('getRows');
                        for(var i = 0; i < rows.length; i++){
                            if(rows[i].drugCode == rec.code){
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
                        $('#drugId').val(rec.code);
                        var drugId=$('#drugId').val();
                        var adjustModes = $('#adjustMode').combobox('getValues');
                        var reflag = 0;
                        var whflag = 0;
                        for(var i = 0; i < adjustModes.length; i ++){
                        	if(adjustModes[i] == "1"){
                        		rIndex = $('#drugInfo').edatagrid('appendRow',{
                                	adjustBillCode : $('#adjustBillCode').textbox('getText'),
                                 	adjustMode : "1",
                                 	inureTime : $('#inureTime').val(),
                                 	adjustReason : $('#CodeAdjustreason').combobox('getValue'),
                                 	drugDept : $('#drugDept').combobox('getValue'),
                                 	remark : $('#remark').textbox('getText'),
                                    tradeName: rec.name,
                                    specs: rec.spec,    
                                    producer: rec.drugManufacturer,
                                    preWholesalePrice: rec.drugWholesaleprice,  
                                    preRetailPrice:rec.drugRetailprice,
                                    drugCode:rec.code,
                                    drugType:rec.drugType,
                                    drugQuality:rec.drugNature,
                                    packUnit:rec.drugPackagingunit,
                                    packQty:rec.packagingnum,
                                }).datagrid('getRows').length-1;
                        		$('#drugInfo').edatagrid('beginEdit',rIndex);
                        		var retail = $('#drugInfo').edatagrid('getEditor', {index: rIndex,field:'retailPrice'});
                        		$(retail.target).numberbox({required:true,min:0,precision:2});
                        		var wholesale = $('#drugInfo').edatagrid('getEditor', {index: rIndex,field:'wholesalePrice'});
                        		$(wholesale.target).numberbox('destroy');
                                $('#retailPrice').numberbox('setValue',"");
                                $('#wholesalePrice').numberbox('setValue',"");
                        	}
                        	if(adjustModes[i] == "2"){
                        		wIndex = $('#drugInfo').edatagrid('appendRow',{
                        			adjustBillCode : $('#adjustBillCode').textbox('getText'),
                                 	adjustMode : "2",
                                 	inureTime : $('#inureTime').val(),
                                 	adjustReason : $('#CodeAdjustreason').combobox('getValue'),
                                 	drugDept : $('#drugDept').combobox('getValue'),
                                 	remark : $('#remark').textbox('getText'),
                                    tradeName: rec.name,
                                    specs: rec.spec,    
                                    producer: rec.drugManufacturer,
                                    preWholesalePrice: rec.drugWholesaleprice,  
                                    preRetailPrice:rec.drugRetailprice,
                                    drugCode:rec.code,
                                    drugType:rec.drugType,
                                    drugQuality:rec.drugNature,
                                    packUnit:rec.drugPackagingunit,
                                    packQty:rec.packagingnum,
                                }).datagrid('getRows').length-1;
                        		$('#drugInfo').edatagrid('beginEdit',wIndex);
                        		var wholesale = $('#drugInfo').edatagrid('getEditor', {index: wIndex,field:'wholesalePrice'});
                        		$(wholesale.target).numberbox({required:true,min:0,precision:2});
                        		var retail = $('#drugInfo').edatagrid('getEditor', {index: wIndex,field:'retailPrice'});
                        		$(retail.target).numberbox('destroy');
                                $('#retailPrice').numberbox('setValue',"");
                                $('#wholesalePrice').numberbox('setValue',"");
                        	}
                        }
                    }
                });
                /**
                 * 药品名enter弹出事件高丽恒
                 * 2016-03-23 09:32
                 */
                bindEnterEvent('tradeName',openDrugInfoWin,'easyui');//药品名enter
                function openDrugInfoWin(){
                    var tempWinPath = "<%=basePath%>popWin/popWinDrug/popWinDrugInfoList.action?classNameTmp=DrugInfo&textId=tradeName";
                    var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
                }
                //keydown事件   调用弹出窗口
                var tradeName = $('#tradeName').combobox('textbox'); 
                tradeName.keyup(function(){
                    KeyDown(0);
                });
                $('#retailPrice').numberbox('textbox').bind('keyup', function(event) {
                	var adjustModes = $('#adjustMode').combobox('getValues');
                    var tradeName=$('#tradeName').combobox('getValue');
                    if(tradeName !="" ){
                    	if(adjustModes.indexOf("1") != -1){
                    		$('#drugInfo').edatagrid('beginEdit',rIndex);
                            var retailPrice = $('#retailPrice').numberbox('getText');
                            var retail = $('#drugInfo').edatagrid('getEditor', {index: rIndex,field:'retailPrice'});
                            $(retail.target).numberbox('setValue', retailPrice);
                    	}
                    }else{
                    	$('#retailPrice').numberbox('setText','')
                        $.messager.alert('提示',"请先选择药品");
                        setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
                    }
                });
                $('#wholesalePrice').numberbox('textbox').bind('keyup', function(event) {
                	var adjustModes = $('#adjustMode').combobox('getValues');
                    var tradeName=$('#tradeName').combobox('getValue');
                    if(tradeName !=""){
                    	if(adjustModes.indexOf("2") != -1){
                    		$('#drugInfo').edatagrid('beginEdit',wIndex);
                            var wholesalePrice=$('#wholesalePrice').numberbox('getText');
                            var wholesale = $('#drugInfo').edatagrid('getEditor', {index: wIndex,field:'wholesalePrice'});
                            $(wholesale.target).numberbox('setValue', wholesalePrice);
                    	}
                    }else{
                    	$('#wholesalePrice').numberbox('setText','')
                        $.messager.alert('提示',"请先选择药品");
                        setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
                    }
                });
            }); 
            
            function del(){
                var item = $('#drugInfo').edatagrid('getSelections');
                if (item.length > 0) {
                    for (var i = item.length - 1; i >= 0; i--) {
                        var index = $('#drugInfo').edatagrid('getRowIndex', item[i]);
                        $('#drugInfo').edatagrid('deleteRow', index);
                        $("#tradeName").combobox('setValue','');
                        $('#retailPrice').numberbox('setValue',"");
                        $('#wholesalePrice').numberbox('setValue',"");
                    }
                }else{
                	$.messager.alert('提示',"请选中要删除的记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                	return false;
                }
            }
            
            function submit(){
            	var rows = $('#drugInfo').edatagrid('getSelections');
            	if(rows.length == 0){
            		$.messager.alert('提示',"请选中保存记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                	return false;
            	}
                $('#drugInfo').edatagrid('acceptChanges');
                $('#drugInfoJson').val(JSON.stringify( $('#drugInfo').edatagrid("getRows")));
                
                
                $('#editForm').form('submit',{  
                    url:"<%=basePath%>drug/adjustPriceInfo/saveAdjustPrice.action",
                    onSubmit:function(){
                    	var rows = $('#drugInfo').edatagrid("getRows");
                    	for(var i = 0 ; i < rows.length; i++){
                    		var index = $('#drugInfo').edatagrid("getRowIndex", rows[i]);
                    		if(!$('#drugInfo').edatagrid("validateRow", index)){
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
                        var item = $('#drugInfo').edatagrid('getRows');
                        if (item) {
                            for (var i = item.length - 1; i >= 0; i--) {
                                var index = $('#drugInfo').edatagrid('getRowIndex', item[i]);
                                $('#drugInfo').edatagrid('deleteRow', index);
                            }
                        }
                        $('#drugInfo').edatagrid('loadData', { total: 0, rows: [] }); 
                        checkNum=0;
                        $('#tDt').tree('reload');
                        $('#histroyList').datagrid('reload');
                        $('#searchList').datagrid('reload');
                    },
                    error : function(data) {
                        $.messager.progress('close');
                        $.messager.alert('提示',"保存失败！"); 
                    }   
                }); 
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
        </script>
        <script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
    </body>
</html>