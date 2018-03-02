<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工作流程管理</title>
</head>

	<body class="easyui-layout">   
	    <div data-options="region:'west',split:false,title:'栏目'" style="width:200px;">
			<div id="aa" class="easyui-accordion" data-options="fit:true,collapsible:false " style="text-align:center;">
				<c:forEach var="workProcessManageVo" items="${list}" varStatus="vs">
					<br>
						<!-- ${pageContext.request.contextPath}/oa/WorkProcessManage/spreadSonMenu.action?menuCode=${workProcessManageVo.menuCode} -->
						<a  name="${pageContext.request.contextPath}/oa/WorkProcessManage/spreadSonMenu.action?menuCode=${workProcessManageVo.menuCode}" style="font-size:16px;text-decoration:none;overflow: hidden;display: block;width: 200px;height: 20px;">${workProcessManageVo.menuName}</a>
				</c:forEach>
			</div>
	</div>  
	 
	    <div data-options="region:'center'" style="padding:5px;background:#eee;">
			<div id="tt" class="easyui-tabs" data-options="fit:true,border:false">
				
			</div>
		</div>   
	</body> 
	<div id="win" ></div>
	<div id="win2" ></div>
</html>

<script type="text/javascript">
	
	$(function(){
		
		
		var action="${pageContext.request.contextPath}/oa/WorkProcessManage/queryProcessList.action?menuCode=";
		var action2="${pageContext.request.contextPath}/oa/WorkProcessManage/queryProcessInfo.action?processId=";
		//alert(action);
		
		$("#aa a").click(function(){
// 			alert($(this).attr("name"));
			var url=$(this).attr("name");
			//window.open(url,'_self','width=200,height=100')
			var title =$(this).text();
			
			var sonNode="<div id='bb' style='text-align:left;'>";
			$.ajax({
				type:"post",
				url:url,
				data:"",
				success:function(backMsg){
					var nodes=backMsg;
					for(var i=0;i<nodes.length;i++){
// 						alert(nodes[i].menuCode);
// 						alert(nodes[i].menuName);
						if(i==0){
							sonNode+="<br>&nbsp;&nbsp;&nbsp;";
						}
						if(i%4==0&&i!=0){
							sonNode+="<br><br>";
							sonNode+="&nbsp;&nbsp;&nbsp;<a href='#' name='"+action+nodes[i].menuCode+"' style='font-size:14px;text-decoration:none;width: 100px;height: 15px;'>"+nodes[i].menuName+"</a>&nbsp;&nbsp;&nbsp;";<!--每行显示4个 -->
						}else{
							sonNode+="<a href='#' name='"+action+nodes[i].menuCode+"' style='font-size:14px;text-decoration:none;width: 100px;height: 15px;'>"+nodes[i].menuName+"</a>&nbsp;&nbsp;&nbsp;";
						}
					}
					sonNode+="</div>";
					//alert(sonNode);
					//加载窗口
					$('#win').window({
						title:title,
					    width:460,    
					    height:"100%",    
					    modal:false,
					    collapsible:false,
					    minimizable:false,
					    maximizable:false,
					    border:'thick',
					    left:200,
					    top:0,
					    draggable:false,
					    content:sonNode
					});
					
					
					$("#bb a").click(function(){
						var title2=$(this).text();
						var url2=$(this).attr("name");
						var lastIndex=url2.lastIndexOf("=")+1;//=号后边是子栏目的id,用他作为div的id，这样不不在打开其他选项卡时覆盖其余选项卡的内容
						var divId=url2.substring(lastIndex,url2.length);
						//alert(divId);
						//关闭窗口
						$("#win").window("close");
						
						//加载流程list
						$.ajax({
							url:url2,
							type:"post",
							data:"",
							success:function(backData){
								var processTitle=title+">>"+title2;
								if(!$("#tt").tabs("exists",processTitle)){//如果不存在则创建新的选项
									//创建选项卡
									$("#tt").tabs("add",{    
									    title:processTitle,    
									    content:"<div id='"+divId+"'></div>",    
									    closable:true,
									    fit:true
									});
								}else{//如果存在则选中
									$("#tt").tabs("select",processTitle);
								}
								
 								var msg=backData;
 								//alert(msg);
								var content="";
								for(var i=0;i<msg.length;i++){
									var time=msg[i].title+"("+msg[i].createTime+")";
									var createTime="";
									if(time.length>25){
										createTime=time.substring(0,26)+"...";
									}else{
										createTime=time;
									}
									
									//alert(msg[i].title);
									content+="<table id='tb' border='1px'  cellpadding='0' cellspacing='0' style='height:15%;width:100%;' rules='none'>"+
									"<tr><td style='align:left' colspan='4'>&nbsp;&nbsp;&nbsp;<a href='#' name='"+action2+msg[i].id+"' id='processTitle' style='font-size:16px;text-decoration:none;' >"+msg[i].title+"</a></td></tr>"+
									"<tr><td style='align:left' id='processTime' width=40%>&nbsp;&nbsp;&nbsp;"+createTime+"</td><td style='align:left'><a href='#' name='"+action2+msg[i].id+"' id='processDesignMap' style='font-size:14px;text-decoration:none;' >流程设计图</a></td><td style='align:left'><a href='#' name='"+action2+msg[i].id+"' id='processTable' style='font-size:14px;text-decoration:none;' >流程表单</a></td><td style='align:left'><a href='#' name='"+action2+msg[i].id+"' value='processInfo'  style='font-size:14px;text-decoration:none;' >流程说明</a></td></tr>"+
									"</table><br>"
								}
								//alert(content);
								$("#"+divId).html(content);
								
// 								alert("#"+divId+" a[value='processInfo']");
								$("#"+divId+" a[value='processInfo']").click(function(){//定位"#"+divId 下的流程说明标签
									//alert("xx");
									var url3=$(this).attr("name");
									//alert(url3);//a超链接的地址
									//请求远程数据
									$.ajax({
										url:url3,
										data:"",
										type:"post",
										success:function(msg){
											
											//加载窗口
											$('#win2').window({
												title:"流程说明",
											    width:"40%",    
											    height:"50%",    
											    modal:true,
											    collapsible:false,
											    minimizable:false,
											    maximizable:false,
											    border:'thick',
											    content:msg
											});
											
										}
									});
									
									
								});
								
								
								
							}
						});
						
						
  
						
						
					});
					
					
					
				}				
			});
			
		});
		
	});
		

	
	
</script>

