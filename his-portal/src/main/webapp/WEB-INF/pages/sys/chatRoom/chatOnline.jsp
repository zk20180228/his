<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<body style="padding:5px;margin:0px">
		<table style="width:100%;">
			
				<tr>
					<td align="center">
						账户名:<input id="name"  type="text"   />
						密码:<input id="pwd"  type="password"  />
						<a id="btn" href="javascript:void(0);conn();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">链接服务器</a>  
						<a  href="javascript:void(0);selectSend();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">发送消息</a>
						<a  href="javascript:void(0);sendRequest();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加好友</a>
						<input id="friend" class="easyui-textbox"  style="width:150px;">
					</td>
				</tr>
		</table>
		<div id="chat" class="easyui-layout" style="width:100%;height:60%;">
		    <div id="west" data-options="region:'west',title:'用户列表',split:true,collapsed:true" style="width:100px">
		    	<table id="user" style="width:100%;heigth:100%" data-options="singleSelect:true">
			    	<thead>   
		    		 <tr>   
			            <th data-options="field:'code',hidden:true"></th>   
			            <th data-options="field:'name',width:'100%'"></th>   
       				 </tr>   
		    		</thead>
		    	</table>
		    </div>
		    <div id="message" data-options="region:'center',title:'消息列表',split:true" style="width:100px;">
		    	<table id="message1" style="width:100%;height:100%" >
		    		<thead>  
		    			<th data-options="field:'message',width:100%"></th>   
		    		</thead>
		    	</table>
		    </div>   
			<div data-options="region:'south',title:'编辑消息',split:true" style="height:100px;">
				<textarea id="sendMessage" style="width:100%;height:100%;">
				
				</textarea>
			</div>   
		</div> 
	</body>
	<script type="text/javascript">
		var total=0;//计算消息存储量
		var loginState=['away','char','dnd','xa','Online'];//用户状态 离线  交谈中 希望不被打扰 离开一段时间 在线
		var user;//账户名
		var password;//密码
		var id;//openfire自动返回Id
		var connectionState = ["正在连接..", "连接已建立", "正在关闭..", "已经关闭"];
		var parser=new DOMParser(); //dom解析
		var flag=false;//判断进行绑定还是进行登录验证
      	var host = "ws://192.168.0.31:7070/ws/";
		var from;//用户标识
		var connection;
		var userMap=new Map();
		
		var onColl=false;//默认关闭状态
		$(function(){
			$('#west').panel({
				onCollapse:function(){
					onColl=false;//面板关闭状态
				},
				onExpand:function(){
					onColl=true;//
					$('.layout-button-right').each(function(index){
						$(this).html("<span style='color: red;'></span>");
					});
				}
			});
			$('#user').datagrid();
		});
		//建立连接
		function conn(){
			user=$('#name').val();
			password=$('#pwd').val();
			if (window.WebSocket != 'undefined') {
		          //OpenFire是实现了WebSocket的子协议
		          if(connection){
		        	  wsClose();
		        	  connection.close();
		          }
		          connection = new WebSocket(host, "xmpp");
		          console.log(connectionState[connection.readyState]);
		          //注册连接建立时的方法
		          connection.onopen = wsOpen;
		          //注册连接关闭时的方法
		          connection.onclose = wsClose;
		          //注册收到消息时的方法
		          connection.onmessage = wsMsg;
		          //连接发生错误的回调方法
		          connection.onerror = function(){
		        	  $.messager.alert('警告',"连接失败,请联系管理员!");  
		          };
		     }else{
		    	 $.messager.alert('提示','浏览器不支持');
		     }
		}
	 //打开连接    
	  function wsOpen(event) {
	        //打印链接状态
	        console.log(connectionState[connection.readyState]);
	        //发送建立流请求   to 域名
	        var steam = "<open to='192.168.0.30' from='"+user+"'  xmlns='urn:ietf:params:xml:ns:xmpp-framing' xml:lang='zh' version='1.0'/>";
	        connection.send(steam);
	        auth();
	 }
  	//接收消息
	function wsMsg(event) {
		//解析xml
  	    var xmlDoc=parser.parseFromString(event.data,"text/xml");
  	    console.info(event.data);
		//提取数据  
  	    if($(xmlDoc).find('open').length==1){//获取openfire 传过来的id
  	    	if(flag){//惊醒绑定
  	    		bingId(id);//绑定id
  	    		online(4);//状态变更
  	    		findGroup();//获取分组
  	    	}else{//获取id
  	    		getId(xmlDoc);
  	    		flag=true;
  	    	}
  	    }if($(xmlDoc).find('failure').length==1){//账户名密码错误
  	    	failed();
  	    }else if($(xmlDoc).find('success').length==1){//用户名密码验证成功后
  	    	nextSend();//登录成功后建立新的连接
  	    }else if($(xmlDoc).find('message').length==1){//接收消息
  	    	receiveMessage(xmlDoc);
  	    }else if($(xmlDoc).find('iq').length==1){
  	    	addGroup(xmlDoc);//获得通讯录
  	    }else if($(xmlDoc).find('presence ').length==1){
  	    	updateState(xmlDoc)//更新用户状态
  	    }
  	    
    }
  	//登录验证  登录成功后进行id绑定
	function auth() {
        //Base64编码
        var token = window.btoa(user+","+password);
        var message = "<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + token + "</auth>";
        connection.send(message);
    }
  	//关闭
  	function wsClose(){
  		var close="<close xmlns=\"urn:ietf:params:xml:ns:xmpp-framing\"/>";
  		connection.send(close);
  	}
  	//绑定ID
  	function bingId(id){
  			var bing= "<iq id=\""+id+"\" type=\"set\">"  
  			bing+="<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">"  
  			bing+="<resource>Showings</resource>";  
  			bing+="</bind>";  
  			bing+="</iq>";
  			connection.send(bing);
  	}
  	//获取id
  	function getId(xmlDoc){
  		var mySelfId = xmlDoc.getElementsByTagName('open'); 
  	    id=$(mySelfId).attr('id');
  	    from=$(mySelfId).attr('from');
  	}
  	//出台状态
  	function online(num){
 	  	var online="<presence id=\""+id+"\"><status>"+loginState[num]+"</status><priority>0</priority></presence>"
	    connection.send(online);
 	  	$.messager.alert('提示','链接成功')
  	}
  	//d登录失败操作
  	function failed(){
  		$.messager.alert('提示信息','账户名密码错误');
  	}
  	//登录成功后建立新的连接
  	function nextSend(){
  		var nextLogin="<open xmlns='jabber:client' to='192.168.0.30' version='1.0' from='"+user+"' id='"+id+"' xml:lang='zh'/>";
  		connection.send(nextLogin);
  	}
  	
  	//单点发送消息
  	function sendMessage(toId){
  		var  messBody=$.trim($('#sendMessage').val());
  		$('#sendMessage').val('');
  		$('#message1').append(returnMessage('right',user+':'+messBody));
  		var message="<message from='"+id+"'  to='"+toId+"'  type='chat'> ";
  			message+="<body>"+messBody+"</body> ";
  			message+="</message>";
  			connection.send(message);
  	}
  	
	//接收消息 获取 发送者id 消息内容 消息 用户名
  	function receiveMessage(xmlDoc){
  		var message=xmlDoc.getElementsByTagName('message');
  		var accountId=$(message).attr('id');//发送者id
  		var accountName=$(message).attr('from');//发送者账号
  		if($(message).find('body').length==1){//判断是否有发送内容
  			//获取body里内容
  			listUser(accountName,$(message).find('body').text(),false)
  		}else{
  			listUser(accountName,accountName+':'+'正在输入...',true);
  		}
  	}
	//获取分组
	function findGroup(){
		var groupFind="<iq id='"+id+"' type='get'> "; 
		groupFind+="<query xmlns='jabber:iq:roster'></query>";  
		groupFind+="</iq>";
		connection.send(groupFind);
	}
	
	//获取离线消息 详细信息
	function getAwayMessage(){
		var AwayMessage="<iq id='"+id+"' to='"+from+"' type='get' from='"+user+"@"+from+"'>";
			AwayMessage+="<query xmlns='http://jabber.org/protocol/offmsg#bif'/>";  
			AwayMessage+="</iq>"; 
			connection.send(AwayMessage);
	}
	
	//获取指定条数的离线消息
	function getNumAwayMessage(){
		var num="<iq id='"+id+"' to='"+from+"' type='get' from='"+user+"@"+from+"'>";  
			num+="<query xmlns='http://jabber.org/protocol/offmsg#start'/>"  
			num+="<pagesize>10</>"  
			num+="</iq>"
			connection.send(num);
	}
	//获取所有的当前用户
	function getAllOnline(){
			var allOnline="<iq type='get'";
			allOnline+="from='"+user+"@"+from+"' ";
			allOnline+="to='"+from+"'> ";
			allOnline+="<query xmlns='jabber:x:roster' ";
			allOnline+=" /> ";
			allOnline+="</iq> ";
			connection.send(allOnline);
// 		 var iq = $iq({type: 'get'}).c('query', {xmlns: 'jabber:iq:roster'});
// 		    connection.sendIQ(iq, function(a){
// 		        console.log('sent iq',a);
// 		        $(a).find('item').each(function(){
// 		            var jid = $(this).attr('jid'); // jid
// 		            console.log('jid',jid);
// 		        });     
// 		    });
			
	}
	//获取个人信息
	function getMyself(){
		var myself="<iq type='get' >"  
		myself+="<query xmlns='jabber:iq:roster'/>"  
		myself+="</iq>" 
		connection.send(myself);
	}
	//请求个人session
	function getSession(){
		var session="<iq xmlns='jabber:client' id='"+id+"' type='result'>";
		session+="<session xmlns='urn:ietf:params:xml:ns:xmpp-session' />";
		session+="</iq>";
		connection.send(session);
	}
	
	//查看指定用户用户状态
	function findState(searchUser){
		 var state="<presence from='"+user+"@"+from+"' to='"+searchUser+"@"+from+"' type='probe'/>";
		 connection.send(state);
	}
	//查询一个账户
	function searchOne(searchId){
		var searhc="<iq type='set'> ";
			searhc+="<query xmlns='jabber:iq:roster'> ";
			searhc+="<item jid='"+searchId+"@"+from+"' ";
			searhc+="Name='remeo'/>";
			searhc+="</query>";
			searhc+=" </iq>";
			connection.send(searhc);
	}
	//发出添加好友请求并返回对方状态
	function sendRequest(sendId){
		var row=$('#user').datagrid('getSelected')
		var friend=$('#friend').val();
		if(row!=null||friend!=null&&friend!=''||sendId!=''){
			if(row!=null){
				sendId=row.code;
			}else if(friend!=null&&friend!=''){
				sendId=friend;
			}
			var send="<presence to='"+sendId+"@"+from+"' type='subscribe'>";
			send+="<status>添加好友</status>";
			send+="</presence>";
			connection.send(send);
		}else{
			$.messager.alert('提示','请选择添加用户');
		}
		
	}
	
	
	function listUser(user,message,si){
		//判断用户列表是否展开
		if(onColl&&!si){//展开
			var index=$('#user').datagrid('getRows').length;//获取用户当前行数
			if(userMap.get(user)==undefined){
				message=user.split('@')[0]+':'+message;
					$('#message1').append(returnMessage('left',message));
				$('#user').datagrid('appendRow',{
					code: user,
					name: user.split('@')[0]
				});
				userMap.put(user,index);
			}else{
					message: user.split('@')[0]+':'+message;
			$('#message1').append(returnMessage('left',message));
			}
		}else{//关闭
			if(!si){
				total++;
				$('.layout-button-right').each(function(index){
					$(this).html("<span style='color: red;'>"+total+"</span>");
				});
				
				var index=$('#user').datagrid('getRows').length;//获取用户当前行数
				if(userMap.get(user)==undefined){
					$('#user').datagrid('appendRow',{
						code: user,
						name: user.split('@')[0]
					});
					userMap.put(user,index);
				}
			}
		}
	}
	//发送消息
	function selectSend(){
		var row=$('#user').datagrid('getSelected')
		if(row){
			sendMessage(row.code);
		}else{
			$.messager.alert('提示','请选择发送用户');
		}
	}
	function returnMessage(align,message){
		return "<tr align='"+align+"' ><td align='"+align+"' >"+message+"</td></tr>"
	}
	//分解好友
	function addGroup(xmlDoc){
		var result=xmlDoc.getElementsByTagName('iq');
		if($(result).find('item').length>0){//获得分组
			$($(result).find('item')).each(function(index){
				var index=$('#user').datagrid('getRows').length;//获取用户当前行数
				if(userMap.get($(this).attr('jid'))==undefined&&$(this).attr('subscription')=='to'){
					$('#user').datagrid('appendRow',{
						code: $(this).attr('jid'),
						name: $(this).attr('jid').split('@')[0]
					});
				userMap.put($(this).attr('jid'),index);
// 				sendRequest($(this).attr('jid').split('@')[0]);//更新用户状态
				}
				
			});
		}
	}
	//查询用户状态
	function updateState(xmlDoc){
		var state=xmlDoc.getElementsByTagName('presence');
		 $(state).attr('from');
		 if($(state).find('status').length>0){
			 $('#user').datagrid('appendRow',{
					 index:userMap.get($(state).attr('from')),
					 row:{
						code: $(state).attr('from'),
						name: $(state).attr('from').split('@')[0] +$(state).find('status').text()
			}});
		 }
	}
	</script>
</html>