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
	<div class="easyui-panel" id="panelEast" data-options="title:'栏目编辑',iconCls:'icon-form',fit:true" style="width: 100%;border:0">
		<div>
    		<form id="editForm" method="post">
				<input type="hidden" id="id" name="id" value="${menu.id }">
				<input type="hidden" id="havesonHidden" name="haveson" value="${menu.haveson }"/>
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<tr>
						<td class="honry-lable">父级名称：</td>
		    			<td class="honry-info"><input id="parent" name="parent" style="width: 300"  value="${menu.parent }" />
		    			<a href="javascript:delSelectedData('parent');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="name" name="name" style="width: 300" value="${menu.name }" data-options="required:true,missingMessage:'请输入栏目名称'"/></td>
	    			</tr>
	    			<c:if test="${menu.id==null }">
	    				<tr>
							<td class="honry-lable">栏目别名：</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="alias" name="alias" style="width: 300"  value="${menu.alias }" data-options="required:true,missingMessage:'请输入栏目别名',validType:'alias'"/></td>
		    			</tr>
	    			</c:if>
	    			<tr>
						<td class="honry-lable">栏目类型：</td>
		    			<td class="honry-info"><input id="type" name="type" style="width: 300" value="${menu.type }" /></td>
	    			</tr>
	    			<c:if test="${menu.id==null }">
	    			<tr>
						<td class="honry-lable">子菜单标志:</td>
				    	<td>
				    	<input type="checkBox" id="haveson" onclick="javascript:onclickBoxHaveson('haveson')"/>
			    		</td>
					</tr>
					</c:if>
	    			<tr>
						<td class="honry-lable">栏目所属：</td>
		    			<td class="honry-info"><input id="belong" name="belong" value="${menu.belong }"data-options="required:true" missingMessage="请选择栏目所属"  />
		    			<a href="javascript:delBelong();"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目功能：</td>
		    			<td class="honry-info"><input id="menufunctionId" name="menufunction.id" value="${menu.menufunction.id }"data-options="required:true" missingMessage="请选择栏目功能"  />
		    			<input id="menufunctionIdvalue"  type="hidden"   value="${menu.menufunction.id }"/><input id="menufunctionIdtext"  type="hidden" />
		    			<a href="javascript:delSelectedData('menufunctionId');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">图标：</td>
		    			<td class="honry-info"><input id="icon" style="width: 300"  name="icon" value="${menu.icon }"data-options="required:true" placeHolder="请按回车选择图标" missingMessage="请按回车选择图标" />
		    				<a href="javascript:delSelectedData('icon');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
	    			</tr>
	    			<c:choose>
		    			<c:when test="${menu.id!=null}">
		    				<c:if test="${menu.belong == 2||menu.belong == 3}">
				    			<tr id="menuIconPathDelId">
									<td class="honry-lable">删除图标：</td>
			    					<td class="honry-info"><input id="menuIconPathDel" style="width: 300"  name="menuIconPathDel" value="${menu.menuIconPathDel }" placeHolder="请按回车选择图标" missingMessage="请按回车选择图标" />
			    					<a href="javascript:delSelectedData('menuIconPathDel');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									</td>
								</tr>
							</c:if>
							<c:if test="${menu.belong == 1}">
				    			<tr id="menuIconPathDelId" style="display: none">
									<td class="honry-lable">删除图标：</td>
			    					<td class="honry-info"><input id="menuIconPathDel" style="width: 300"  name="menuIconPathDel" value="${menu.menuIconPathDel }"  placeHolder="请按回车选择图标" missingMessage="请按回车选择图标" />
			    					<a href="javascript:delSelectedData('menuIconPathDel');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									</td>
								</tr>
							</c:if>
						</c:when>
						<c:otherwise> 
							<tr id="menuIconPathDelId" style="display: none">
								<td class="honry-lable">删除图标：</td>
				    			<td class="honry-info"><input id="menuIconPathDel" style="width: 300"  name="menuIconPathDel" value="${menu.menuIconPathDel }" placeHolder="请按回车选择图标" missingMessage="请按回车选择图标" />
				    			<a href="javascript:delSelectedData('menuIconPathDel');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    				</td>
			    			</tr>
						</c:otherwise>
	    			</c:choose>
	    			<tr>
						<td class="honry-lable">说明：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="description" style="width: 300"  name="description" value="${menu.description }" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">打开方式：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="openmode" style="width: 300"  name="openmode" value="${menu.openmode }" /></td>
	    			</tr>
	    			<c:if test="${menu.haveson == 1||menu.id==null}">
		    			<tr>
							<td class="honry-lable">停用标志:</td>
					    	<td><input type="hidden" id="stopFlgHidden" name="stop_flg" value="${menu.stop_flg }"/>
					    	<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
				    		</td>
						</tr>
					</c:if>
					<c:choose>
		    			<c:when test="${menu.id!=null}">
		    				<c:if test="${menu.belong == 2||menu.belong == 3}">
				    			<tr id="openStateId">
									<td class="honry-lable">开放状态：</td>
			    					<td class="honry-info"><input id="openState" style="width: 300"  name="openState" value="${menu.openState }"data-options="required:true"  /></td>
								</tr>
							</c:if>
							<c:if test="${menu.belong == 1}">
				    			<tr id="openStateId" tyle="display: none">
									<td class="honry-lable">开放状态：</td>
			    					<td class="honry-info"><input id="openState" style="width: 300"  name="openState" value="${menu.openState }"  /></td>
								</tr>
							</c:if>
						</c:when>
						<c:otherwise> 
							<tr id="openStateId"  style="display: none">
								<td class="honry-lable">开放状态：</td>
			    				<td class="honry-info"><input id="openState" style="width: 300"  name="openState" value="${menu.openState }"data-options="required:true"  /></td>
							</tr>
						</c:otherwise>
	    			</c:choose>
					<c:if test="${menu.belong == 2}">
		    			<tr id="applyIsNeedTr">
							<td class="honry-lable">是否需要申请:</td>
					    	<td><input type="hidden" id="applyIsNeedHidden" name="applyIsNeed" value="${menu.applyIsNeed }"/>
					    	<input type="checkBox" id="applyIsNeed" onclick="javascript:onclickBox('applyIsNeed')"/>
				    		</td>
						</tr>
					</c:if>
					<tr id="menuId" style="display:none">
						<td class="honry-lable">栏目资源:</td>
				    	<td>
				    	   <table border="0">
							<tr>
								<td id="buttonListId">栏目资源:<br>
									<select multiple="multiple" id="selectAll" style="width:100px;height:160px;">
										<c:forEach var="list" items="${buttonList }">
											<option value="${list.mrcAlias }">${list.mrcName }</option>
										</c:forEach>
									</select>
								</td>
								<td>
								<input type="button" value="  添加选中&nbsp;&gt;&nbsp;  " id="add_this"><br>
								<input type="button" value="  移除选中&nbsp;&lt;&nbsp;  " id="remove_this"><br>
								<input type="button" value="  添加全部&gt;&gt;  " id="add_all"><br>
								<input type="button" value="  移除全部&lt;&lt;  " id="remove_all"><br>
								</td>
								<td>已拥有的资源:<br>
									<select multiple="multiple" id="selectBut" name="butName" style="width: 100px;height:160px;">
										<option id="viewButId" value="view" disabled="disabled" >访问</option>
										<c:forEach var="list" items="${mbList }">
											<c:if test="${list.alias!='view' }">
												<option  value="${list.alias }">${list.name }</option>
											</c:if>
										</c:forEach>
									</select>
								</td>
							</tr>
							</table>
			    		</td>
					</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
				    <c:if test="${menu.id==null }">
				    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a id="save" href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>    
	<div id="menuphoto"></div>
	<script type="text/javascript">
		//加载页面
		$(function(){
			if("${menu.id}"==null||"${menu.id}"==""){
				$('#viewButId').hide();  
			}else{
				if("${menu.haveson}"==0){
					$('#viewButId').hide();  
				} 
				if("${menu.haveson}"==1){
					$('#menuId').show();  
				}
			}
			//添加验证
			$.extend($.fn.validatebox.defaults.rules, {     
				alias: {
			        validator: function (value, param) {
			            if(!/^[A-Za-z]+$/.test(value)){
			                $.fn.validatebox.defaults.rules.alias.message = '请输入英文';
			                return false;
			            }
			            return true;
			        }
				}
  			});
			 //加载父节点
			 $('#parent').combotree({ 
				url: "<c:url value='/sys/queryMenuTree.action'/>?type=${type}&parId=${menu.id}&menuAlias=${menuAlias}",   
				required: true, 
				missingMessage:'请选择所属栏目',
				width:'300',
			    editable:false
			});  
			 //开放状态
			$('#openState').combobox({
				valueField: 'id',
				textField: 'name',
				data: [{id: '0',name: '开放中'},{id: '1',name: '建设中'}],
				panelHeight:'auto',
				required:true
			})
			 /**
				* 绑定父节点回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
			bindEnterEvent('parent',popWinToMenu,'easyui');//绑定回车事件
			$('#type').combobox({
				valueField: 'id',
				textField: 'name',
				data: [{id: '2',name: '一般栏目'},{id: '1',name: '系统栏目'},{id: '3',name: '统计栏目'}],
				panelHeight:'auto',
				required:true,
				missingMessage:'请选择栏目类型'
			})
			//加载栏目所属
			$('#belong').combobox({
				valueField:'id',
				textField:'text',
				data:[{'id' : 1, 'text' : '平台'},{'id' : 3, 'text' : '移动端'}],
				editable : false,
				width:'300',
				required : true,
				onSelect: function(rec){
					$('#icon').textbox('clear');
					$('#menuIconPathDel').textbox('clear');
					mfIdCom(rec.id);
				}
			});
			//加载栏目功能下拉框
			mfIdCom('${menu.belong}');
			cheHid();
			$('#icon').textbox({
				required:true,
				editable : false
			});
			$('#menuIconPathDel').textbox({
				editable : false
			});
			bindEnterAndBlackEvent('icon',menuPhoto,'easyui'); 
			bindEnterAndBlackEvent('menuIconPathDel',delMobilePhoto,'easyui'); 
			
		});
		
		//加载栏目功能
		function mfIdCom(belongs){
			if(belongs == 2 || belongs == 3){
				belongs = 2;
				$('#applyIsNeedTr').show();
				$('#openStateId').show();
				$('#menuIconPathDelId').show();
				 //开放状态
				$('#openState').combobox({
					required:true,
					editable : false
				})
				 $('#menuIconPathDel').textbox({
					editable : false
				})
				bindEnterAndBlackEvent('menuIconPathDel',delMobilePhoto,'easyui');
				
				
			}else{
				 //开放状态
				$('#openState').combobox({
					required:false,
					editable : false
				})
			 	$('#menuIconPathDel').textbox({
					editable : false
				}) 
				$('#applyIsNeedTr').hide();
				$('#openStateId').hide();
				$('#menuIconPathDelId').hide();
				
			}
			$('#menufunctionId').combobox({
				url: "<%=basePath%>sys/queryMenufunctionCombobox.action?menuAlias=${menuAlias}&belongs=" + belongs,
				valueField:'id',    
				textField:'mfName',   
				missingMessage:'请选择所属栏目',
				width:'300',
				filter: function(q, row){
					var opts = $(this).combobox('options');
					return row[opts.textField].indexOf(q) >= 0;
				},
			onChange: function (node) {
				var textss=$('#menufunctionId').combobox('getValue');
				var texts=$('#menufunctionId').combobox('getText');
				$('#menufunctionIdvalue').val(textss);
				$('#menufunctionIdtext').val(texts);
				}
			});
		}
		
		/**
		* 绑定栏目功能回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('menufunctionId',popWinToMenuFunction,'easyui');//绑定回车事件
		//表单验证别名
		function submit(){ 
			var id = "${menu.id}";
			if(!id){
				var alias = $('#alias').textbox('getText');
				$.post("<c:url value='/sys/selectMenuAlias.action'/>?alias=" + alias,function(result){
		            if(result==0){//不存在
		            	submitForm();
		            }else{
		            	$.messager.alert('提示','角色别名已存在！');
						close_alert();
						return;
		            }
			    });		
			}else{
		            	submitForm();
			}
	    }	    
	    
		//表单提交
		function submitForm(){
			var menufunctionIdvalue= $('#menufunctionIdvalue').val();
			var menufunctionIdtext=$('#menufunctionIdtext').val();
			if (menufunctionIdvalue==menufunctionIdtext) {
				$.messager.alert('提示','请选择栏目功能！');
				$('#menufunctionId').combobox({
					value:""
			});
				close_alert();
				return false;
			}
			if($('#havesonHidden').val()==1){
				$('#viewButId').prop("disabled",false);
			}
			$('#selectBut option').prop("selected",true);
			$('#editForm').form('submit',{  
				url:"<c:url value='/sys/saveMenu.action'/>",
	        	onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						$('#viewButId').prop("disabled",true);
		    			$('#selectBut option').prop("selected",false);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
	        	},  
		        success:function(result){  
		        	$.messager.progress('close');
		        	var res = eval('('+result+')');
		        	if(res.resMsg == 'success'){
		        		$.messager.alert('提示','保存成功！');
			        	$('#list').treegrid('reload');
			        	$('#divLayout').layout('remove','east');
			        	$('#menuphoto').dialog('destroy');
			        	$('#delMobilePhoto').dialog('destroy');
		        	}else{
						$.messager.alert('提示','保存失败！');	
		        	}
		        }						         
	   		}); 
		}
		//连续添加
		function addContinue(){ 
			var alias = $('#alias').textbox('getText');
			$.post("<c:url value='/sys/selectMenuAlias.action'/>?alias=" + alias,function(data){
	            if(data=="0"){//不存在
	            	var menufunctionIdvalue= $('#menufunctionIdvalue').val();
					var menufunctionIdtext=$('#menufunctionIdtext').val();
					if (menufunctionIdvalue==menufunctionIdtext) {
						$.messager.alert('提示','请选择栏目功能！');
						$('#menufunctionId').combobox({
							value:""
					});
						close_alert();
						return false;
					}
	            	if($('#havesonHidden').val()==1){
	            		$('#viewButId').prop("disabled",false);
	    			}
	    			$('#selectBut option').prop("selected",true);
	    			$('#editForm').form('submit',{  
	    		        url:"<c:url value='/sys/saveMenu.action'/>",
	    		        onSubmit:function(){
	    					if (!$('#editForm').form('validate')) {
	    						$.messager.show({
	    							title : '提示信息',
	    							msg : '验证没有通过,不能提交表单!'
	    						});
	    						$('#viewButId').prop("disabled",true);
	    		    			$('#selectBut option').prop("selected",false);
	    						return false;
	    					}
	    					$.messager.progress({text:'保存中，请稍后...',modal:true});
	    		        },  
	    		        success:function(result){  
	    		        	$.messager.progress('close');
	    		        	var res = eval('('+result+')');
	    		        	if(res.resMsg == 'success'){
		    		        	clear();
		    		        	$('#viewButId').prop("disabled",true);
		    		        	$('#selectBut option').each(function(){
		    			        	if(!$(this).prop("disabled")){
		    			        		$(this).appendTo('#selectAll');
		    			        	}
		    			        });
		    		        	$('#list').treegrid('reload');
	    		        	}else{
	    						$.messager.alert('提示','保存失败！');	
	    		        	}
	    		        }
	    		    }); 
	            }else{
	            	$.messager.alert('提示','角色别名已存在！');
					close_alert();
					return;
	            }
		    });	
	    }
	    
		//清除页面填写信息
		function clear(){
			$('#editForm').form('reset');
			cheHid();
		}
		
		//关闭编辑窗口
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		
		//复选框赋值
		function onclickBox(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		//复选框赋值
		function onclickBoxHaveson(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
				$('#viewButId').show();  
				$('#menuId').show();  
				$('#selectAll').prop("disabled",false);
			}else{
				$('#'+id+'Hidden').val(0);
				$('#viewButId').hide();  
				$('#menuId').hide();  
				$('#selectAll').prop("disabled",true);
				$('#selectBut option').each(function(){
		        	if(!$(this).prop("disabled")){
		        		$(this).appendTo('#selectAll');
		        	}
		        });
			}
		}
		
		//复选框初始化
		function cheHid(){
			//复选框赋值
			if($('#havesonHidden').val()==1){
				$('#haveson').prop("checked", true); 
			}else{
				$('#selectAll').prop("disabled",true);
			}
			if($('#stopFlgHidden').val()==1){
				$('#stopFlg').prop("checked", true); 
			}
		}
		
		 //移到右边
	    $('#add_this').click(function() {
	    //获取选中的选项，删除并追加给对方
	        $('#selectAll option:selected').appendTo('#selectBut');
	    });
	    //移到左边
	    $('#remove_this').click(function() {
	        $('#selectBut option:selected').appendTo('#selectAll');
	    });
	    //全部移到右边
	    $('#add_all').click(function() {
	        //获取全部的选项,删除并追加给对方
	        if(!$('#selectAll').prop("disabled")){
	        	 $('#selectAll option').appendTo('#selectBut');
	        }
	    });
	    //全部移到左边
	    $('#remove_all').click(function() {
	        $('#selectBut option').each(function(){
	        	if(!$(this).prop("disabled")){
	        		$(this).appendTo('#selectAll');
	        	}
	        });
	    });
	    //双击选项
	    $('#selectAll').dblclick(function(){ //绑定双击事件
	        //获取全部的选项,删除并追加给对方
	        $("option:selected",this).appendTo('#selectBut'); //追加给对方
	    });
	    //双击选项
	    $('#selectBut').dblclick(function(){
	       $("option:selected",this).appendTo('#selectAll');
	    });
	    //加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#menuphoto').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		function menuPhoto(){ 
			var icons=$("#belong").combobox('getValue');
			if(icons!=null&&icons!=""){
				if(icons=='1'){
					Adddilog("图标","<c:url value='/sys/Menuphoto.action?menuAlias=${menuAlias}'/>&time="+new Date().getTime(),'50%','80%');
				}else{
					Adddilog("图标","<c:url value='/sys/mobPhoto.action?menuAlias=${menuAlias}'/>&time="+new Date().getTime(),'50%','80%');
				}
			}else{
				$.messager.alert('提示','请先选择栏目所属！');
				close_alert();
			}
			
			
		} 
		
		function delMobilePhoto(){ 
			var icons=$("#belong").combobox('getValue');
			if(icons!=null&&icons!=""){
				Adddilog("图标","<c:url value='/sys/delMobPhoto.action?menuAlias=${menuAlias}'/>&time="+new Date().getTime(),'50%','80%');
			}else{
				$.messager.alert('提示','请先选择栏目所属！');
				close_alert();
			}
		}
		/**
		* 回车弹出父节点选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToMenu(){
			popWinMenuCallBackFn = function(node){
		    	$("#parent").combotree('setValue',node.id);
			};
			var tempWinPath = "<%=basePath%>popWin/popWinMenuTree/toMenuTreePopWin.action?textId=parentText";
			window.open (tempWinPath,'newwindow',' left=100,top=50,width='+ (screen.availWidth -200) +',height='+ (screen.availHeight-110) 

		+',scrollbars,resizable=yes,toolbar=yes')
		}
		
		/**
		* 回车弹出栏目功能选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToMenuFunction(){
			var belongs = $('#belong').combobox('getValue');
			popWinMenuFunctionBackFn = function(node){
				
		    	$("#menufunctionId").combobox('setValue',node.id);
		    	var textss=$('#menufunctionId').combobox('getValue');
				var texts=$('#menufunctionId').combobox('getText');
				$('#menufunctionIdvalue').val(textss);
				$('#menufunctionIdtext').val(texts);
			};
			var tempWinPath = "<%=basePath%>popWin/popWinMenuFunction/toMenuFunctionPopWin.action?textId=menufunctionId&belong=" + belong;
			window.open (tempWinPath,'newwindow',' left=100,top=50,width='+ (screen.availWidth-200) +',height='+ (screen.availHeight-110) 
		+',scrollbars,resizable=yes,toolbar=yes')
		}
			function delBelong(){
				delSelectedData('belong');
				mfIdCom('');
			}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>