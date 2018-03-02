var localObj = window.location;

var contextPath = localObj.pathname.split("/")[1];

var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;

var toolArr = new Array('首页','上一页','下一页','尾页','刷新');

/*
	* 选项卡切换时激活当前选中，禁用未选中
	*
	*
	*/
function disabletabs(tableID, arrLenth){
			var arr  = []; 
				for(i = 0; i<arrLenth;i++){
					arr[i] = i;
				}
			var tab = $(tableID).tabs('getSelected');
				var index = $(tableID).tabs('getTabIndex',tab);
				var tab = $(tableID).tabs('getSelected');
				var index = $(tableID).tabs('getTabIndex',tab);
				arr.splice($.inArray(index,arr),1);
				 $.each(arr,function(n,value) {  
				 		  $(tableID).tabs('disableTab', value);   	
				 });
				$(tableID).tabs('enableTab', index);
				console.info('需要禁用的选项卡有：'+arr);
		}

//删除选中table row 
function del(tableID){  
     var rows = $(tableID).datagrid("getSelections");   
     var copyRows = [];  
     for ( var j= 0; j < rows.length; j++) {  
     		  copyRows.push(rows[j]);  
                            }  
     for(var i =0;i<copyRows.length;i++){      
           var index = $(tableID).datagrid('getRowIndex',copyRows[i]);  
           $(tableID).datagrid('deleteRow',index);   
       }   
   } 

//获取datagrid选中row的ID(单选)     注：该ID没有拼接单引号 例如：ID = 123456
function getIdUtil(tableID){
		var row = $(tableID).datagrid("getSelections");  
	       var i = 0;    
	       var getid = ""; 
	       if(row.length!=1){
	       		$.messager.alert("操作提示", "请选择一条记录！","warning");
	       		return null;
	       }else{  
   	 for(i;i<row.length;i++){ 
          getid = row[i].id; 
		 }
   } 
   return getid;	
}
function getIdUtilsy(tableID,id){
	var row = $(tableID).datagrid("getSelections");  
       var i = 0;    
       var getid = ""; 
       if(row.length!=1){
       		$.messager.alert("操作提示", "请选择一条记录！","warning");
       		return null;
       }else{  
	 for(i;i<row.length;i++){ 
      getid = row[i].id; 
	 }
} 
return getid;	
}

//获取datagrid选中row的ID（多选）  注：该ID有拼接单引号 例如：ID = '123456'
function getbachIdUtil(id){
	 var row = $("#dg").datagrid("getSelections"); 
	 var dgID ="";
	  if(row.length<1){
	       		$.messager.alert("操作提示", "请选择一条记录！","warning");
	       } 
	       var i = 0;
	       for(i;i<row.length;i++){    
	    	   dgID += "\'"+row[i].id+"\'";    
          if(i < row.length-1){    
        	  dgID += ',';    
          }else{    
              break;    
          }  
     }  
	       return dgID;
}
//获取datagrid选中row的ID（多选）  注：该ID没有拼接单引号 例如：ID = 123456
function getbachIdUtil2(id){
	 var row = $("#dg").datagrid("getSelections"); 
	 var dgID ="";
	  if(row.length<1){
	       		$.messager.alert("操作提示", "请选择一条记录！","warning");
	       } 
	       var i = 0;
	       for(i;i<row.length;i++){    
	    	   dgID += row[i].id;    
          if(i < row.length-1){    
        	  dgID += ',';    
          }else{    
              break;    
          }  
     }  
	       return dgID;
}
//如果datagtid没有选中任何数据则返回第一条数据ID！  
function getTabNocheckedID(tableID){
	var row = $(tableID).datagrid("getSelections");  
	var id = ""; 
	       if(row.length<1){//如果没有选择数据，默认选中第一行数据
	    	  $(tableID).datagrid('selectRow', 0);
	    	  var row = $(tableID).datagrid("getSelections");  
	       }
	       id += row[0].USER_ID; 
	       return id;
}   


//获取所有部门类型的数组
var deptType="";
function getDeptTypeArray(){
	//return [{id:'C',value:'门诊'},{id:'I',value:'住院'},{id:'F',value:'财务'},{id:'PI',value:'药库'},{id:'P',value:'药房'},{id:'D',value:'行政'},{id:'T',value:'医技'},{id:'L',value:'后勤'},{id:'N',value:'护士站'},{id:'S',value:'科研'},{id:'U',value:'科室'},{id:'OP',value:'手术'},{id:'O',value:'其他'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=depttype",
//		success: function(data) {
//			deptType = data;
//			return deptType;
//		}
//	});
 }
function getDeptTypeArrayTree(){
//	return [{id:'C',text:'门诊',state:'closed'},{id:'I',text:'住院',state:'closed'},{id:'F',text:'财务',state:'closed'},{id:'PI',text:'药库',state:'closed'},{id:'P',text:'药房',state:'closed'},{id:'D',text:'行政',state:'closed'},{id:'T',text:'医技',state:'closed'},{id:'L',text:'后勤',state:'closed'},{id:'N',text:'护士站',state:'closed'},{id:'S',text:'科研',state:'closed'},{id:'U',text:'科室',state:'closed'},{id:'OP',text:'手术',state:'closed'},{id:'O',text:'其他',state:'closed'}];
}
//获取所有部门类型的Map
function getDeptTypeMap(){
//	var dTypeMap = new Map();
//	var dTypeArray = getDeptTypeArray();
//	for(var i=0;i<dTypeArray.length;i++){
//		dTypeMap.put(dTypeArray[i].id,dTypeArray[i].value);
//	}
//	return dTypeMap;
}
 


//获取所有性别类型的数组
function getSexArray(){
//	//return [{id: 1,value: '男'},{id: 2,value: '女'},{id: 3,value: '未知'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=sex",
//		success: function(data) {
//			sexType = data;
//		}
//	});
//	
//	return sexType;
}

//获取所有性别类型的Map
function getSexMap(){
//	var sexMap = new Map();
//	var sexArray = getSexArray();
//	for(var i=0;i<sexArray.length;i++){
//		sexMap.put(sexArray[i].id,sexArray[i].value);
//	}
//	return sexMap;
}

//获取门诊账户操作类型的数组
function getOperTypeArray(){
	//return [{id: 0,value: '存预存金'},{id: 1,value: '新建账户'},{id: 2,value: '停用账户'},{id: 3,value: '重启帐户'},{id: 4,value: '支付'},{id: 5,value: '退费入户'},{id: 6,value: '注销账户'},{id: 7,value: '授权支付'},{id: 8,value: '退预交金'},{id: 9,value: '修改密码'},{id: 10,value: '结清余额'},{id: 11,value: '授权'},{id: 12,value: '补打'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=opertype",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}

//获取门诊操作类型的Map
function getOperTypeMap(){
//	var operTypeMap = new Map();
//	var operTypeArray = getOperTypeArray();
//	for(var i=0;i<operTypeArray.length;i++){
//		operTypeMap.put(operTypeArray[i].id,operTypeArray[i].value);
//	}
//	return operTypeMap;
}

//获取所有工作状态类型的数组
function getWorkStateArray(){
	//return [{id: '1',value: '正式'},{id: '2',value: '退休'},{id: '3',value: '聘用制'},{id: '4',value: '编外'},{id: '5',value: '离休'},{id: '6',value: '出国'},{id: '7',value: '返聘'},{id: '8',value: '死亡'},{id: '9',value: '外出学习'},{id: 'a',value: '临时工'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=workstatec",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}

//获取所有工作状态的Map
function getWorkStateMap(){
//	var workStateMap = new Map();
//	var workStateArray = getWorkStateArray();
//	for(var i=0;i<workStateArray.length;i++){
//		workStateMap.put(workStateArray[i].id,workStateArray[i].value);
//	}
//	return workStateMap;
}

//获取所有结算类别的数组
function getPaykindArray(){
	//return [{id: '01',value: '自费'},{id: '02',value: '保险'},{id: '03',value: '公费在职'},{id: '04',value: '公费退休'},{id: '05',value: '公费干部'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=paykind",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取所有结算类别的Map
function getPaykindMap(){
//	var paykindMap = new Map();
//	var paykindArray = getPaykindArray();
//	for(var i=0;i<paykindArray.length;i++){
//		paykindMap.put(paykindArray[i].id,paykindArray[i].value);
//	}
//	return paykindMap;
}

//获取所有价格形势的数组
function getPriceFormArray(){
	//return [{id: 0,value: '三甲价'},{id: 1,value: '特诊价'},{id: 2,value: '儿童价'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=priceform",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}

//获取所有价格形势的Map
function getPriceFormMap(){
//	var priceFormMap = new Map();
//	var priceFormArray = getPriceFormArray();
//	for(var i=0;i<priceFormArray.length;i++){
//		priceFormMap.put(priceFormArray[i].id,priceFormArray[i].value);
//	}
//	return priceFormMap;
}

//获取所有标识的数组
function getFlagArray(){
	//return [{id: 0,value: '全部'},{id: 1,value: '药品'},{id: 2,value: '非药品'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=drugflag",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}

//获取所有标识的Map
function getFlagMap(){
//	var flagMap = new Map();
//	var flagArray = getFlagArray();
//	for(var i=0;i<flagArray.length;i++){
//		flagMap.put(flagArray[i].id,flagArray[i].value);
//	}
//	return flagMap;
}

//获取所有性别类型的数组
function getMidDayArray(){
	//return [{id: 1,value: '上午'},{id: 2,value: '下午'},{id: 3,value: '晚上'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=midday",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取所有性别类型的Map
function getMidDayMap(){
//	var midDayMap = new Map();
//	var midDayArray = getMidDayArray();
//	for(var i=0;i<midDayArray.length;i++){
//		midDayMap.put(midDayArray[i].id,midDayArray[i].value);
//	}
//	return midDayMap;
}
//获取星期的数组
var week='';
function getWeekArray(){
	//return [{id: 1,value: '星期一'},{id: 2,value: '星期二'},{id: 3,value: '星期三'},{id: 4,value: '星期四'},{id: 5,value: '星期五'},{id: 6,value: '星期六'},{id: 7,value: '星期日'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=week",
//		success: function(data) {
//			week = data;
//		}
//	});
//	return week;
}
//获取星期的Map
function getWeekMap(){
//	var weekMap = new Map();
//	var weekArray = getWeekArray();
//	for(var i=0;i<weekArray.length;i++){
//		weekMap.put(weekArray[i].id,weekArray[i].value);
//	}
//	return weekMap;
}
//获取担保类型E 人员担保 U 单位担保 F 财务担保
function getsuretyTypeArray(){
	//return [{id:"E",value: '人员担保'},{id:"U",value: '单位担保 '},{id:"F",value: '财务担保'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=suretytype",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取担保类型的Map
function getsuretyTypeMap(){
//	var suretyTypeMap = new Map();
//	var suretyTypeArray = getsuretyTypeArray();
//	for(var i=0;i<suretyTypeArray.length;i++){
//		suretyTypeMap.put(suretyTypeArray[i].id,suretyTypeArray[i].value);
//	}
//	return suretyTypeMap;
}
//获取英文星期的数组
function getWeekEnArray(){
	//return [{id: 1,value: 'Monday'},{id: 2,value: 'Tuesday'},{id: 3,value: 'Wednesday'},{id: 4,value: 'Thursday'},{id: 5,value: 'Friday'},{id: 6,value: 'Saturday'},{id: 7,value: 'Sunday'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=weeken",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取英文星期的Map
function getWeekEnMap(){
//	var weekEnMap = new Map();
//	var weekEnArray = getWeekEnArray();
//	for(var i=0;i<weekEnArray.length;i++){
//		weekEnMap.put(weekEnArray[i].id,weekEnArray[i].value);
//	}
//	return weekEnMap;
}
//获取分区类型的数组
function getZoneTypeArray(){
	return [{"encode":1,"name":"时间(年)"},{"encode":2,"name":"时间(月)"},{"encode":3,"name":"时间(日)"},{"encode":4,"name":"其它"}];
}
//获取分区类型的Map
function getZoneTypeMap(){
	var zoneTypeMap = new Map();
	var zoneTyperay = getZoneTypeArray();
	for(var i=0;i<zoneTyperay.length;i++){
		zoneTypeMap.put(zoneTyperay[i].encode,zoneTyperay[i].name);
	}
	return zoneTypeMap;
}
//获取星期几 参数：2015-01-01
function getWeekByString(param){
	var p = param.split("-");
	var d = new Date();
	d.setFullYear(p[0],parseInt(p[1])-1,p[2]);
	var w = d.getDay();
	if(w==0){
		return "星期日";
	}else if(w==1){
		return "星期一";
	}else if(w==2){
		return "星期二";
	}else if(w==3){
		return "星期三";
	}else if(w==4){
		return "星期四";
	}else if(w==5){
		return "星期五";
	}else if(w==6){
		return "星期六";
	}
}
//获取所有部门分类的数组
function getDeptClassArray(){
	//return [{id:1,value:'分类'},{id:2,value:'科室'},{id:3,value:'诊室'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=deptclass",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取所有部门分类的Map
function getDeptClassMap(){
//	var classMap = new Map();
//	var classArray = getDeptClassArray();
//	for(var i=0;i<classArray.length;i++){
//		classMap.put(classArray[i].id,classArray[i].value);
//	}
//	return classMap;
}

//获取所有员工类型的数组
function getEmpTypeArray(empType){
		
	/*return [{id:1,value:'医疗人员'},{id:2,value:'医技人员'},{id:3,value:'护理人员'},
           {id:4,value:'教学人员'},{id:5,value:'科研人员'},{id:6,value:'药剂人员'},
	       {id:7,value:'检验人员'},{id:8,value:'图书档案资料人员'},{id:9,value:'财会人员'},
           {id:10,value:'行政人员'},{id:11,value:'工程技术人员'},{id:12,value:'经济人员'},
	       {id:13,value:'幼教人员'},{id:14,value:'营养人员'},{id:15,value:'工勤人员'},
	       {id:16,value:'卫生检验人员'},{id:17,value:'卫生营养人员'},{id:18,value:'离休人员'},
	       {id:19,value:'退休人员'},{id:20,value:'临时工'},{id:21,value:'进修人员'},
           {id:22,value:'实习生'},{id:23,value:'研究生'},{id:24,value:'其他'}];*/
}
//获取所有员工类型的Map
function getEmpTypeMap(){
//	var empTypeMap = new Map();
//	var empTypeArray = getEmpTypeArray();
//	for(var i=0;i<empTypeArray.length;i++){
//		empTypeMap.put(empTypeArray[i].id,empTypeArray[i].value);
//	}
//	return empTypeMap;
}
//获取当前时间   bTime:true/false 是否显示时分秒    bWeek:true/false 是否显示星期
function showTime(bTime,bWeek){ 
	var date = new Date();
	this.year = date.getFullYear();
	this.month = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1):(date.getMonth()+1);
	this.date = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var retVal = this.year + "年" + this.month + "月" + this.date + "日 ";
	if(bTime){
		this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    	this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    	this.second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
		retVal += this.hour + ":" + this.minute + ":" + this.second;
	}
	if(bWeek){
		this.day = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")[date.getDay()];
		retVal += " "+this.day;
	}
	return retVal;
} 

//获取病床状态的数组
function getBedTypeArray(){
	//return [{id:'1',value:'挂床'},{id:'2',value:'包床'},{id:'3',value:'加床'},{id:'4',value:'占用'},{id:'5',value:'隔离'},{id:'6',value:'污染'},{id:'7',value:'空床'},{id:'8',value:'关闭'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=bedtype",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取病床状态的Map
function getBedTypeMap(){
//	var bedTypeMap = new Map();
//	var bedypeArray = getBedTypeArray();
//	for(var i=0;i<bedypeArray.length;i++){
//		bedTypeMap.put(bedypeArray[i].id,bedypeArray[i].value);
//	}
//	return bedTypeMap;
}
//获取病床编制的数组
function getBedOrganArray(){
	//return [{id:'1',value:'家庭病床'},{id:'2',value:'加床'},{id:'3',value:'编制外'},{id:'4',value:'编制内'}];
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=bedorgan",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取病床状态的Map
function getBedOrganMap(){
//	var bedOrganMap = new Map();
//	var bedOrganArray = getBedOrganArray();
//	for(var i=0;i<bedOrganArray.length;i++){
//		bedOrganMap.put(bedOrganArray[i].id,bedOrganArray[i].value);
//	}
//	return bedOrganMap;
}

//获取分类类型的数组
function getReferenceTypeArray(){
	//return [{id:'00',value:'组织结构'},{id:'01',value:'病区与科室间关系'},{id:'02',value:'分诊区与科室间关系'}]
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=referencetype",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取分类类型的Map
function getReferenceTyeMap(){
//	var referenceMap=new Map();
//	var referenceArray=getReferenceTypeArray();
//	for(var i=0;i<referenceArray.length;i++){
//		referenceMap.put(referenceArray[i].id,referenceArray[i].value);
//	}
//	return referenceMap;
}
//获取支付方式的数组
function getPayTypeArray(){
	//return [{id:'1',value:'现金'},{id:'2',value:'银联卡'},{id:'3',value:'支票'},{id:'4',value:'院内账户'},{id:'5',value:'转押金'},{id:'6',value:'汇票'},{id:'7',value:'保险账户'}]
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=payway",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取支付方式的Map
function getPayTypeMap(){
//	var payTypeMap=new Map();
//	var payTypeArray=getPayTypeArray();
//	for(var i=0;i<payTypeArray.length;i++){
//		payTypeMap.put(payTypeArray[i].id,payTypeArray[i].value);
//	}
//	return payTypeMap;
}
//获取年龄单位的数组
function getAgeUnitsArray(){
	//return [{id:'岁',value:'岁'},{id:'月',value:'月'},{id:'日',value:'天'}]
//	$.ajax({
//		url:  basePath+"/baseinfo/pubCodeMaintain/getNewDictionary.action?type=ageunits",
//		success: function(data) {
//			deptType = data;
//		}
//	});
//	return deptType;
}
//获取年龄单位的Map
function getAgeUnitsMap(){
//	var ageUnitsMap=new Map();
//	var ageUnitsArray=getAgeUnitsArray();
//	for(var i=0;i<ageUnitsArray.length;i++){
//		ageUnitsMap.put(ageUnitsArray[i].id,ageUnitsArray[i].value);
//	}
//	return ageUnitsMap;
}

//获取身份证信息
function idCardJson(){ 	//对象
	
	SynCardOcx1.SetPhotoPath(0,"");  //照片保存路径设置为C盘根目录
	SynCardOcx1.SetPhotoName(2);	//照片保存文件名设置为 身份证号
	SynCardOcx1.SetPhotoType(1);	//照片保存格式设置为jpg
	SynCardOcx1.FindReader();
	var str;
  	str = SynCardOcx1.FindReader();
  	if (str > 0){
  		//读取
  		var obj = null;
  		 var nRet;
  		 SynCardOcx1.SetUserLifeBType(2);		//返回有效期开始日期的格式0=YYYYMMDD,1=YYYY年MM月DD日,2=YYYY.MM.DD,3=YYYY-MM-DD,4=YYYY/MM/DD
  	 	 SynCardOcx1.SetUserLifeEType(2,0);		//返回有效期结束日期的格式0=YYYYMMDD,1=YYYY年MM月DD日,2=YYYY.M
  		 SynCardOcx1.SetBornType(3);			//返回出生日期的格式0=YYYYMMDD,1=YYYY年MM月DD日,2=YYYY.MM.DD,　　3=YYY-MM-DD,4=YYYY/MM/DD
  		 SynCardOcx1.SetNationType(2);			//返回民族的格式0=卡内存储的数据 ， 1=解释之后的数据	，2=解释之后+“族”
  		 SynCardOcx1.SetSexType(1);				//返回性别	0=代码   1 解释之后
  	  	 nRet = SynCardOcx1.ReadCardMsg();
  	  	if(nRet==0){		//读卡成功
  	  		return obj = {resCode:0,resMSg:"success",named:$.trim(SynCardOcx1.NameA),sex:$.trim(SynCardOcx1.Sex),age:getage(SynCardOcx1.CardNo)+"岁",nation:$.trim(SynCardOcx1.Nation),born:$.trim(SynCardOcx1.Born),address:$.trim(SynCardOcx1.Address),cardNo:$.trim(SynCardOcx1.CardNo),userLifeB:$.trim(SynCardOcx1.UserLifeB),userLifeE:$.trim(SynCardOcx1.UserLifeE),police:$.trim(SynCardOcx1.Police)};
  	  	}else{
  	  		return obj = {resCode:1,resMSg:"没有找到卡片"};
  	  	}
  	}else{
  		return obj = {resCode:1,resMSg:"没有找到读卡器"};
  	}
}
//身份证年龄计算
function getage(sfz){
	  var str = sfz;		// 身份证编码
	  var len = str.length;	// 身份证编码长度
	  if (len < 15) {
		  alert("不是有效身份证编码");
	  } else {
		  if ((len != 15) && (len != 18)) {	// 判断编码位数等于15或18
			  alert("不是有效身份证编码");
		  } else {
			  if (len == 15) {				// 15位身份证
				  var s = str.substr(len - 1, 1);
				  var date1 = new Date();	// 取得当前日期
				  var year1 = date1.getFullYear();	// 取得当前年份
				  var month1 = date1.getMonth();	// 取得当前月份
				  if (month1 > parseInt(str.substr(8, 2))){	// 判断当前月分与编码中的月份大小
				      var age = year1 - ("19" + str.substr(6, 2));
				  	 return age;
				  }
				  else{
					  var age = year1 - ("19" + str.substr(6, 2)) - 1;
					  return age;
				  }
			  }
			  if (len == 18) {				// 18位身份证
				  var date1 = new Date();	// 取得当前日期
				  var year1 = date1.getFullYear();	// 取得当前年份
				  var month1 = date1.getMonth();	// 取得当前月份
				  if (month1 > parseInt(str.substr(10, 2))){	// 判断当前月分与编码中的月份大小
					  var age = year1 - str.substr(6, 4);
					  return age;
				  }
				  else{
					  var age = year1 - str.substr(6, 4) - 1;
					  return age;
				  }
			  }
		  }
  	}
} 
//统计功能页面拼接的公共方法 datevo:时间对象，dimensionArray：维度种类数组，dateType：所选时间跨度级别（1-按年统计;2-按季统计;3-按月统计,4-按日统计）
function publicStatistics(datevo,dimensionArray,dateType,dimensionNameArray){
	var colspanMonth=0;//年份所占的宽度
	var colspanYear=0;//月份所占宽度
	var timeone=0;//最小时间跨度
	var	monthCha=0;//月份差(同一月需要出现的次数)
	var yearCha=0;
	var dayCha=0;//日 差(同一日需要出现的次数)
	var zbCha=0;//指标差   =年差*越差  即 指标需要重复的次数(最小时间跨度的总数)
	var num=3;//维度所占的高度，即：维度格所占的高度
	if(dateType==1){
		colspanYear=dimensionNameArray.length;
		timeone=datevo[0];
		yearCha=datevo[1]-datevo[0]+1;
		zbCha=datevo[1]-datevo[0]+1;
		num=1;
	}else if(dateType==2){
		colspanYear=(datevo[3]-datevo[2]+1)*dimensionNameArray.length;
		colspanMonth= dimensionNameArray.length;
		timeone=datevo[2];
		monthCha=datevo[5]-datevo[4]+1;
		yearCha=datevo[1]-datevo[0]+1;
		zbCha=(datevo[1]-datevo[0]+1)*(datevo[3]-datevo[2]+1);
		num=2;
	}else if(dateType==3){
		colspanYear=(datevo[5]-datevo[4]+1)*dimensionNameArray.length;
		colspanMonth= dimensionNameArray.length;
		timeone=datevo[4];
		monthCha=datevo[5]-datevo[4]+1;
		yearCha=datevo[1]-datevo[0]+1;
		zbCha=(datevo[1]-datevo[0]+1)*(datevo[5]-datevo[4]+1);
		num=2;
	}else if(dateType==4){
		colspanYear=(datevo[7]-datevo[6]+1)*dimensionNameArray.length*(datevo[5]-datevo[4]+1);
		colspanMonth= (datevo[7]-datevo[6]+1)*dimensionNameArray.length;
		timeone=datevo[6];
		monthCha=datevo[5]-datevo[4]+1;
		yearCha=datevo[1]-datevo[0]+1;
		dayCha=(datevo[1]-datevo[0]+1)*(datevo[5]-datevo[4]+1);
		zbCha=(datevo[1]-datevo[0]+1)*(datevo[5]-datevo[4]+1)*(datevo[7]-datevo[6]+1);
		num=3;
	}
	var columnsArray=new Array();
		//datagrid 的第一行表头
		var stringfield='';
		stringfield='{field:'+'\'name\',title:"维度",width:'+'\'200px\',align:'+'\'center\',rowspan:'+num+',colspan:'+dimensionArray.length*0.5+'}';
		for(var i=0;i<yearCha;i++){
			if(stringfield!=''&&stringfield!=null){
				stringfield =stringfield+',';
			}
			var time=parseInt(datevo[0])+parseInt(i);
			stringfield += '{field:"'+i+'",title:"'+time+'年'+'",width:'+'\'300px\',align:'+'\'center\',colspan:'+colspanYear+'}';
		}
		stringfield='['+stringfield+']';
		console.info("1~"+stringfield);
		stringfield =eval(stringfield);
		columnsArray.push(stringfield);
		
		//datagrid中间行的表头添加
		if(dateType==2){
			//季度
			var stringfieldQuart='';
			console.info("年差"+yearCha);
			for(var i=0;i<yearCha;i++){
				for(var q=0;q<(datevo[3]-datevo[2]+1);q++){
					var qq=datevo[2]+q;
					if(stringfieldQuart!=''&&stringfieldQuart!=null){
						stringfieldQuart =stringfieldQuart+',';
					}
					stringfieldQuart +=  '{field:"'+q+'",title:"'+qq+'季度'+'",width:'+'\'300px\',align:'+'\'center\',colspan:'+colspanMonth+'}';
				}
			}
			stringfieldQuart='['+stringfieldQuart+']';
			console.info("2~"+stringfieldQuart);
			stringfieldQuart =eval(stringfieldQuart);
			columnsArray.push(stringfieldQuart);
		}
		//月份
		if(dateType==3||dateType==4){
			var stringfieldMonth='';
			for(var mm=0;mm<yearCha;mm++){
				for(var w=0;w<monthCha;w++){
					var ww=parseInt(datevo[4])+parseInt(w);
					if(stringfieldMonth!=''&&stringfieldMonth!=null){
						stringfieldMonth =stringfieldMonth+',';
					}
					stringfieldMonth +=  '{field:"'+w+'",title:"'+ww+'月'+'",width:'+'\'300px\',align:'+'\'center\',colspan:'+colspanMonth+'}';
				}
			}
			stringfieldMonth='['+stringfieldMonth+']';
			console.info("3~"+stringfieldMonth);
			stringfieldMonth =eval(stringfieldMonth);
			columnsArray.push(stringfieldMonth);
		}
		//如果时间类型是4，则满足if判断，将日的那一行表头拼接进去
		var count=dateType-2;
		if(count>1){
			var stringfieldDay='';
			for(var dd=0;dd<dayCha;dd++){
				for(var i=0;i<(datevo[7]-datevo[6]+1);i++){
					var ii=datevo[6]+i;
					if(stringfieldDay!=''&&stringfieldDay!=null){
						stringfieldDay =stringfieldDay+',';
					}
					stringfieldDay +=  '{field:"'+i+'",title:"'+ii+'日'+'",width:'+'\'300px\',align:'+'\'center\',colspan:'+dimensionNameArray.length+'}';
				}
			}
			stringfieldDay='['+stringfieldDay+']';
			console.info("4~"+stringfieldDay);
			stringfieldDay =eval(stringfieldDay);
			columnsArray.push(stringfieldDay);
		}
		
		//datagrid的最后一行表头，即:指标 行
		var stringfield2='';
		var a;
			if(dateType==1){
				for(var i=0;i<(datevo[1]-datevo[0]+1);i++ ){
					a=Number(datevo[0])+i+""+"00000";
					for(var p=0;p<dimensionNameArray.length;p++){
						if(stringfield2!=''){
							stringfield2 +=',';
						}
						var n=p+1;
						stringfield2 += '{field:"'+a+n+'",title:"'+dimensionNameArray[p]+'",width:'+'\'85px\',align:'+'\'center\'}';
					}
				}
			}else if(dateType==2){
				for(var i=0;i<(datevo[1]-datevo[0]+1);i++ ){
					for(var j=0;j<(datevo[3]-datevo[2]+1);j++){
						a=(datevo[0]+i)+""+(datevo[2]+j)+"0000";
						for(var p=0;p<dimensionNameArray.length;p++){
							if(stringfield2!=''){
								stringfield2 +=',';
							}
							var n=p+1;
							stringfield2 += '{field:"'+a+n+'",title:"'+dimensionNameArray[p]+'",width:'+'\'85px\',align:'+'\'center\'}';
						}
					}
				}
			}else if(dateType==3){
				for(var i=0;i<(datevo[1]-datevo[0]+1);i++ ){
					for(var j=0;j<(datevo[5]-datevo[4]+1);j++){
						a=(datevo[0]+i)+"0"+""+((datevo[4]+j)>9?(datevo[4]+j):"0"+(datevo[4]+j))+"00";
						for(var p=0;p<dimensionNameArray.length;p++){
							if(stringfield2!=''){
								stringfield2 +=',';
							}
							var n=p+1;
							stringfield2 += '{field:"'+a+n+'",title:"'+dimensionNameArray[p]+'",width:'+'\'85px\',align:'+'\'center\'}';
						}
					}
				}
			}else if(dateType==4){
				for(var i=0;i<(datevo[1]-datevo[0]+1);i++ ){
					for(var j=0;j<(datevo[5]-datevo[4]+1);j++){
						for(var k=0;k<(datevo[7]-datevo[6]+1);k++){
							a=(datevo[0]+i)+"0"+""+((datevo[4]+j)<10?("0"+(datevo[4]+j)):(datevo[4]+j))+((datevo[6]+k)<10?("0"+(datevo[6]+k)):(datevo[6]+k));
							for(var p=0;p<dimensionNameArray.length;p++){
								if(stringfield2!=''){
									stringfield2 +=',';
								}
								var n=p+1;
								stringfield2 += '{field:"'+a+n+'",title:"'+dimensionNameArray[p]+'",width:'+'\'85px\',align:'+'\'center\'}';
							}
						}
					}
				}
			}
		//根据维度种类和维度名称动态循环添加所选择的维度内容
		var string ='';
		for(var i=0;i<dimensionArray.length;i+=2){
			if(string!=''){
				string +=',';
			}
			string += '{field:"'+dimensionArray[i]+'",title:"'+dimensionArray[i+1]+'",width:'+'\'120px\',align:'+'\'center\',formatter:'+dimensionArray[i]+'function'+'}';
		}
		stringfield2=string+','+stringfield2;
		stringfield2='['+stringfield2+']';
		console.info("5~"+stringfield2);
		stringfield2 =eval(stringfield2);
		columnsArray.push(stringfield2);
		return columnsArray;
}
//合并单元格
function mergeCellsOfValue(tableID, colList) {
    var ColArray = colList.split(",");
    var tTable = $('#' + tableID);
    var TableRowCnts = tTable.datagrid("getRows").length;
    var tmpA;
    var tmpB;
    var PerTxt = "";//记录上一行的字段值
    var CurTxt = "";//记录本行的字段值
    var perFie="";//记录前一列的字段值(上一行)
    var curFie="";//记录前一列的字段值(本行)
    var alertStr = "";
    for (var j = 0; j <ColArray.length - 1; j++) {
        // 当循环至某新的列时，变量复位。
        PerTxt = "";
        tmpA = 1;
        tmpB = 0;
        // 从第一行（表头为第0行）开始循环，循环至行尾(溢出一位)
        for (var i = 0; i <= TableRowCnts; i++) {
            if (i == TableRowCnts) {
                CurTxt = "";
            } else {
                CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
                curFie = tTable.datagrid("getRows")[i][ColArray[j-1]];
            }
            if (perFie == curFie && PerTxt == CurTxt) {
                tmpA += 1;
            } else {
                tmpB += tmpA;
                tTable.datagrid('mergeCells', {
                    index : i - tmpA,
                    field : ColArray[j],
                    rowspan : tmpA,
                    colspan : null
                });
                tmpA = 1;
            }
            PerTxt = CurTxt;
            perFie = curFie;
        }
    }
}

