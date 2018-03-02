//表单验证器函数
function noEmpty(v){
    if(v == "") return "不能为空";
}

function noEmptyAndNoIllegal(v){
    if(v==""){
        return "不能为空";
    }else if(contain(v,"~!@#$%^&*()=<>?/.'\";:")){
        return "不能包含非法字符“~!@#$%^&*()=<>?/.'\";:”";
    }
}

function noIllegal(v){
    if(contain(v,"~!@#$%^&*()=<>?/.'\";:")){
        return "不能包含非法字符“~!@#$%^&*()=<>?/.'\";:”";
    }
}

//****************************************************************** 
/**//* 
*JavaScript Document 
* <p>wswswsws： JS常用処理函数</p> 
* <p>说明： JS常用処理函数</p> 
* <p>著作権： Copyright (c) 2007-4-30</p> 
* <p>会社： 杭州恒生电子株式会社</p> 
* @担当者： 林颜双 
* @version 1.0 
* 由于本人日语能力有限及时间仓促没有写日文注释中文部门可能会出现乱码现象 
* 在GB2312编码格式下可正常显示此文档,代码编写过程难免有错误,错误之处欢迎指正 
* 
******函数说明****** 
* 
* isEmail(emailStr) //邮件校验,通过校验返回ture,否则返回false 
* isIp(strIp) //IP地址校验,输入正确的IP地址返回ture,否则返回false 
* isTelphoneNum(telNum) //电话号码校验,正确的电话号码（如0571-1234567[8] 010-1234567[8] ）则返回ture,否则返回false 
* isMobilephoneNum(mobileNum) //手机号码校验,正确的手机号码(如:13800571506 013800571505)则返回ture,否则返回false 
* isDigital(str) //纯数字验证输入,输入为纯数字则返回ture,否则返回false 
* card(id) //18位身份证验证,输入正确的号码返回ture,否则返回false 
* matchPattern(value,mask) //自定义规则,mask为正确的正则表达式,返回通过自定义验证的字符串 
* isEnglish(name) //判断是否为英文,正确返回ture,否则返回false 
* isChinese(name) //判断是否为中文,正确返回 ture,否则返回false 
* contain(str,charset) //非法字符判断,str中有charset则返回ture, 
* testSelect() //选中文本框或文本域文本,在input位置加上 onClick/onFocus="textSelect();" 即可 
* textOnly() //只允许输入数字 字母 下划线,在input位置加上 onkeypress="textOnly()" 即可 
* isURL(URL) //判断URL,正确的URL返回true,否则返回false 
* isDate(date) //判断是否为短日期(如:2003-(/)12-(/)05),正确返回,否则返回false 
* isTime(time) //判断是否为短时间(如:HH:MM:SS) 
* enterToTab() //在表单元素中除button外,按回车键模拟TAB功能 
* enterTOSubmit(name) //在填写表单时按回车键提交表单,name为sumbit控件名 
* isFloat(float,index) //判断是否为浮点数,并且小数点后面为index位,正确返回true 
* trimFullSpace(strIn) //返回去前后全角半角空格后的字符串 
* //校验密码复杂度,密码由数字,大小写字母,特殊字符中的任意三种组合,通过则返回true 
* checkPassWord(passWord,maxLen) //由三个函数组成checkPassWord(),charMode(),bitTotal() 
* maxLength(strin，maxLen) //判断字符最大长度,如果strin的长度不大于maxLen返回tur 
* minLength(strin，minLen) //判断字符最小长度,如果的长度不小于minLen返回ture 
* isAccount(str) //判断用户名合法性(字母 数字式下划先组成且只能以字母开头，且长度最小为6位)，合法返回true,否则返回false 
* getChineseNum(obstring) //取得字符串中中文字的个数 
* isInteger(str) //判断输入的字符是否为Integer类型，是则返回true，否则返回false 
* isDouble(str) //判断输入的字符是否为Double类型，是则返回true，否则返回false 
*/ 
//******************************************************************* 


//邮件校验 
//通过校验返回ture,否则返回false 
function isEmail(emailStr) { 
    if (emailStr.length == 0) { 
        return fasle; 
    } else { 
        var emailPat=/^(.+)@(.+)$/; 
        var specialChars="\(\)<>@,;:\\\"\.\[\]"; 
        var validChars="[^\s" + specialChars + "]"; 
        var quotedUser="(\"[^\"]*\")"; 
        var ipDomainPat=/^(d{1,3})[.](d{1,3})[.](d{1,3})[.](d{1,3})$/; 
        var atom=validChars + '+'; 
        var word="(" + atom + "|" + quotedUser + ")"; 
        var userPat=new RegExp("^" + word + "(\." + word + ")*$"); 
        var domainPat=new RegExp("^" + atom + "(\." + atom + ")*$"); 
        var matchArray=emailStr.match(emailPat); 
        if (matchArray == null) { 
            return false; 
        } 
        var user=matchArray[1]; 
        var domain=matchArray[2]; 
        if (user.match(userPat) == null) { 
            return false; 
        } 
        var IPArray = domain.match(ipDomainPat); 
        if (IPArray != null) { 
            for (var i = 1; i <= 4; i++) { 
                if (IPArray[i] > 255) { 
                    return false; 
                } 
            } 
            return true; 
        } 
        var domainArray=domain.match(domainPat); 
        if (domainArray == null) { 
            return false; 
        } 
        var atomPat=new RegExp(atom,"g"); 
        var domArr=domain.match(atomPat); 
        var len=domArr.length; 
        if ((domArr[domArr.length-1].length < 2) || (domArr[domArr.length-1].length > 3)) { 
            return false; 
        } 
        if (len < 2) { 
            return false; 
        } 
        return true; 
    } 
} 
//验证邮箱 格式正确返回true 否则返回false
function checkEmail(str){
   var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
   if(re.test(str)){
	   return true; 
   }else{
	   return false; 
   }
}

//IP地址校验 
//正确的IP地址回ture,否则返回false 
function isIp(strIp) { 
var ipDomainPat=/^((2[0-4]d|25[0-5]|[01]?dd?).){3}(2[0-4]d|25[0-5]|[01]?dd?)$/; 
var matchArray=strIp.match(ipDomainPat); 
if(matchArray!=null){ 
return true; 
} 
} 


//验证电话号码
//验证规则：区号+号码，区号以0开头，3位或4位
//号码由7位或8位数字组成
//区号与号码之间可以无连接符，也可以“-”连接
//如01088888888,010-88888888,0955-7777777 则返回ture,否则返回false 
function isTelphoneNum(telNum){ 
var telphoneNumPat=/^0\d{2,3}-?\d{7,8}$/; 
var matchArray=telNum.match(telphoneNumPat); 
if(matchArray!=null){ 
return true; 
} 
} 


//手机号码校验 
//正确的手机号码(如:13800571506 013800571505)则返回ture,否则返回false 
function isMobilephoneNum(mobileNum){ 
var mobilephoneNumPat=/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;  
var matchArray=mobileNum.match(mobilephoneNumPat); 
if(matchArray!=null){ 
return true; 
} 
} 

//纯数字验证输入,输入为纯数字则返回ture,否则返回false 
function isDigital(str){ 
	var digitalPot=/^d*$/; 
	var matchArray=str.match(digitalPot); 
	if(matchArray!=null){ 
		return true; 
	} 
} 
//18位身份证验证,输入正确的号码返回ture,否则返回false 
function card(id){ 
var Wi=new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1); 
var ai="10X98765432"; 
var sum=0 
var ssum=0; 
//alert(id.charAt(i)); 
for(var i=0;i<17;i++){ 
ssum=eval(Wi[i]*id.charAt(i)); 
sum=ssum+sum; 
} 
var modNum=sum%11; 
if(ai.charAt(modNum)==id.charAt(17)){ 
return true; 
} 
} 

//自定义规则,mask为正确的正则表达式 
//返回通过自定义验证的字符串 
function matchPattern(value,mask) { 
return mask.exec(value); 
} 

//判断是否为英文,正确返回ture,否则返回false 
function isEnglish(name) { 
    if(name.length == 0) return false; 
    for(i = 0; i < name.length; i++) { 
        if(name.charCodeAt(i) > 128) return false; 
    } 
    return true; 
} 

//判断是否为中文,正确返回 ture,否则返回false 
function isChinese(name) 
{ 
    if(name.length == 0) return false; 
    for(i = 0; i < name.length; i++) { 
        if(name.charCodeAt(i) > 128) return true; 
    } 
    return false; 
}

//非法字符判断,str中有charset则返回ture, 
function contain(str,charset){ 
    var i; 
    for(i=0;i<charset.length;i++){ 
        if(str.indexOf(charset.charAt(i))>=0){ 
            return true; 
        }        
    } 
    return false; 
}

//选中文本框或文本域文本,在input位置加上 onClick/onFocus="textSelect();" 即可 
function textSelect() { 
var obj = document.activeElement; 
if(obj.tagName == "TEXTAREA") 
{ 
obj.select(); 
} 
if(obj.tagName == "INPUT" ) { 
if(obj.type == "text") 
obj.select(); 
} 
} 

//只允许输入数字 字母 下划线 
function textOnly(){ 
var i= window.event.keyCode ; 
//8=backspace 
//9=tab 
//37=left arrow 
//39=right arrow 
//46=delete 
//48~57=0~9 
//97~122=a~z 
//65~90=A~Z 
//95=_ 
if (!((i<=57 && i>=48)||(i>=97 && i<=122)||(i>=65 && i<=90)||(i==95)||(i==8)||(i==9)||(i==37)||(i==39)||(i==46))){ 
//window.event.keyCode=27; 
event.returnValue=false; 
return false; 
} else { 
//window.event.keyCode=keycode; 
return true; 
} 
} 

//判断URL,正确的URL返回true,否则返回false 
function isURL(URL){ 
    var urlPat=/^http:\/\/[A-Za-z0-9]+.[A-Za-z0-9]+[':+!]*([^<>"])*$/; 
    var matchArray=URL.match(urlPat); 
    if(matchArray!=null){ 
        return true; 
    } else { 
        return false; 
    } 
} 


function isAllURL(str_url){ 
    var strRegex = "^((https|http|ftp|rtsp|mms)?://)"  
        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
        + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184  
        + "|" // 允许IP和DOMAIN（域名） 
        + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.  
        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名  
        + "[a-z]{2,6})" // first level domain- .com or .museum  
        + "(:[0-9]{1,4})?" // 端口- :80  
        + "((/?)|" // a slash isn't required if there is no file name  
        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
    var re=new RegExp(strRegex);  
    //re.test() 
    if (re.test(str_url)){ 
        return (true);  
    }else{  
        return (false);  
    } 
} 

//判断短日期(如2003-12-05) 
function isDate(date){ 
    var r = date.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
    if(r==null){ 
        return false; 
    } 
    if (r[1]<1 || r[3]<1 || r[3]-1>12 || r[4]<1 || r[4]>31) { 
        return false 
    } 
    var d= new Date(r[1], r[3]-1, r[4]); 
    if(d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]){ 
        return true; 
    } 
} 

//判断短时间（HH:MM:SS） 
function isTime(time){ 
var a = time.match(/^(d{1,2})(:)?(d{1,2}) (d{1,2})$/); 
if (a == null) 
{ 
return false; 
} 
if (a[1]>23 || a[1]<0 || a[3]>60 || a[3]<0 || a[4]>60 || a[4]<0){ 
return false 
} 
return true; 
} 

//在表单元素中除button外,按回车键模拟TAB功能 
function enterToTab(){ 
if (window.event.keyCode == 13 && window.event.ctrlKey == false && window.event.altKey == false){ 
if (window.event.srcElement.type != "button") 
window.event.keyCode = 9; 
} else { 
return true; 
} 
} 

//在填写表单时按回车键提交表单,name为sumbit控件名 
function enterTOSubmit(name) { 
if (window.event.keyCode == 13 && window.event.ctrlKey == false && window.event.altKey == false){ 
var objSubmit=document.getElementById(name); 
objSubmit.focus; 
} else { 
return true; 
} 
} 

//返回去前后全角半角空格后的字符串 
function trimFullSpace(strIn) { 
if (strIn == null){ 
return null; 
} else { 
var len = strIn.length; 
var start = 0; 
var end = strIn.length; 
for (var i = 0; i < len; i++){ 
if (strIn.charAt(i) == ' ' || strIn.charAt(i) == ' '){ 
start++; 
} else { 
break; 
} 
} 
for (var i = len - 1; i > -1; i--){ 
if (strIn.charAt(i) == ' '|| strIn.charAt(i) == ' '){ 
end--; 
} else { 
break; 
} 
} 
if (start >= end){ 
return ""; 
} else { 
return strIn.substring(start, end); 
} 
} 
} 

//判断是否为浮点数,并且小数点后面为index位,正确返回true 
function isFloat(float,index){ 
var floatPat=/^(d{1,})[.](d{1,})$/; 
var matchArray=float.match(floatPat); 
if(matchArray!=null) { 
if(matchArray[2].length==index){ 
return true; 
} 
} 
} 

//判断字符最大长度,如果strin的长度不大于maxLen返回ture 
function maxLength(strin,maxLen) { 
    var len=0; 
    for(var i=0;i<strin.length;i++) 
    { 
        if(strin.charCodeAt(i)>256) 
        { 
            len += 2; 
        } else { 
            len++; 
        } 
    } 
    if(len<=maxLen){ 
        return true; 
    } 
} 

//判断字符最小长度,如果的长度不小于minLen返回ture 
function minLength(strin,minLen) { 
    var len=0; 
    for(var i=0;i<strin.length;i++) 
    { 
        if(strin.charCodeAt(i)>256) 
        { 
            len += 2; 
        } else { 
            len++; 
        } 
    } 
    if(len>=maxLen){ 
        return true; 
    } 
} 

//由三个函数组成checkPassWord(),charMode(),bitTotal() 
//校验密码复杂度,密码由数字,大小写字母,特殊字符中的任意三种组合,通过则返回true 
function checkPassWord(passWord,maxLen){ 
if (passWord.length<=maxLen) 
return false; //密码太短 
Modes=0; 
for (i=0;i<passWord.length;i++){ 
//测试一个字符并判断一共有多少种模式. 
Modes|=charMode(passWord.charCodeAt(i)); 
} 
return bitTotal(Modes); 
} 

//CharMode函数 
//判断某个字符是属于哪一种类型. 
function charMode(iN){ 
if (iN>=48 && iN <=57) //数字 
return 1; 
if (iN>=65 && iN <=90) //大写字母 
return 2; 
if (iN>=97 && iN <=122) //小写 
return 4; 
else 
return 8; //特殊字符 
} 
//bitTotal函数 
//计算出当前密码当中一共有多少种模式 
function bitTotal(num){ 
modes=0; 
for (i=0;i<4;i++){ 
if (num & 1) modes++; 
num>>>=1; 
} 
if(modes==3){ 
return true 
} 
} 

//判断是否为合法的用户名，合法返回true,否则返回flase 
//用户名由字母和数字、下划线组成，且只能以字母开头，且长度最小为6位 
function isAccount(str){ 
    if(/^[a-z]w{3,}$/i.test(str)) 
    { 
        return true; 
    } else { 
        return false; 
    }
} 

//取得字符串中中文字的个数 
function getChineseNum(obstring){ 
var pattern = /^[一-龥]+$/i; 
var maxL,minL; 
maxL = obstring.length; 
obstring = obstring.replace(pattern,""); 
minL = obstring.length; 
return (maxL - minL) 
} 

//判断输入的字符是否为Integer类型，是返回true，否则返回false 
function isInteger(str){ 
	var integerPat=/^[-+]?d+$/; 
	var matchArray=str.match(integerPat); 
	if(matchArray!=null){ 
		return true; 
	} else { 
		return "输入的字符必须为整数！"; 
	} 
} 

//判断输入的字符是否为Double类型，是返回true，否则返回false 
function isDouble(str){ 
var doublePat=/^[-+]?d+(.d+)?$/; 
var matchArray=str.match(doublePat); 
if(matchArray!=null){ 
return true; 
} else { 
return false; 
} 
} 
