/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Condition expression
 */

var KisBpmConditionExpressionCtrl = [ '$scope', '$modal', function($scope, $modal) {

    // Config for the modal window
    var opts = {
        template:  'editor-app/configuration/properties/condition-expression-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmConditionExpressionPopupCtrl = [ '$scope', '$translate', '$http', function($scope, $translate, $http) {
	// Put json representing condition on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null) {
        $scope.conditionExpression = {value: $scope.property.value};
        $scope.conValue = $scope.property.value
        if($scope.property.value){
        	$scope.addOrEgit = "修改"
        	$scope.clearAllStyle = "mouldShow"
        }else{
        	$scope.addOrEgit = "添加"
        	$scope.clearAllStyle = "mouldHide"
        }
    } else {
        $scope.conditionExpression = {value: ''};
        $scope.conValue = ""
        $scope.addOrEgit = "添加"
        $scope.clearAllStyle = "mouldHide"
    }
    $scope.conditionClass = "mouldHide"
    $scope.isShowFn = function(){
		document.getElementById("conditionIf").contentWindow.start({
			code:$scope.conditionExpression.value
		})
   		$scope.conditionClass = "mouldShow"
    }
    $scope.clearAll = function(){
    	$scope.conditionExpression = {value: ''};
        $scope.conValue = ""
        $scope.addOrEgit = "添加"
        $scope.property.value = ""
        $scope.clearAllStyle = "mouldHide"
    }

    $scope.conditionCloseWin = function(){
    	$scope.conditionClass = "mouldHide"
    }
    var numLine = {
    	"1":"一级",
    	"2":"二级",
    	"3":"三级",
    	"4":"四级"
    }
    var logic = {
    	"||":"或者",
    	"&&":"并且"
    }
    var hisLine,userLine;
    //转换 翻译
    var transition = function(){
    	var reslut = $scope.property.value
    	reslut = reslut.replace(/\$\{|\}|\s+/g,"")
    	if (reslut.indexOf("zkhonryState") != -1){
    		reslut.replace(/zkhonryState([!=]{2})'(\d+)'/g,function(a,b,c){
    			if(c == "0"){
    				if(b == "=="){
    					reslut = "审批 结果 通过"
    				}else{
    					reslut = "审批 结果 驳回"
    				}
    			}else{
    				if(b == "=="){
    					reslut = "审批 流转至 节点" + c
    				}else{
    					reslut = "审批 流转非 节点" + c
    				}
    			}
    		})
    	}else if (reslut.indexOf("lowercase") != -1){
    		reslut.replace(/lowercase([=<>]+)(\d+).00/g,function(a,b,c){
    			if(b == "<"){
    				reslut = "金额 小于 "+c+".00"
    			}else if (b == ">"){
    				reslut = "金额 大于 "+c+".00"
    			}else if (b == ">="){
    				reslut = "金额 大于等于 "+c+".00"
    			}else if (b == "<="){
    				reslut = "金额 小于等于 "+c+".00"
    			}
    		})
    	}else if (reslut.indexOf("dutiesLevel") != -1){
			var reslutDu = ""
    		reslut.replace(/dutiesLevel([!=]{2})'(\d+)'([&|]+)?/g,function(a,b,c,d){
    			if(d){
    				if(b == "=="){
    					reslutDu += "级别 等于 "+numLine[c]+" "+logic[d]+"<br/>"
    				}else{
    					reslutDu += "级别 不等于 "+numLine[c]+" "+logic[d]+"<br/>"
    				}
    			}else{
    				if(b == "=="){
    					reslutDu += "级别 等于 "+numLine[c]
    				}else{
    					reslutDu += "级别 不等于 "+numLine[c]
    				}
    			}
    		})
    		reslut = reslutDu
    	}else if (reslut.indexOf("employeeType") != -1){
    		var reslutDu = ""
    		reslut.replace(/employeeType([!=]{2})'(\w+)'([&|]+)?/g,function(a,b,c,d){
    			if(d){
    				if(b=="=="){
    					reslutDu += "人员类型 等于 "+userLine[c]+" "+logic[d]+"<br/>"
    				}else{
    					reslutDu += "人员类型 不等于 "+userLine[c]+" "+logic[d]+"<br/>"
    				}
    			}else{
    				if(b == "=="){
    					reslutDu += "人员类型 等于 "+userLine[c]
    				}else{
    					reslutDu += "人员类型 不等于 "+userLine[c]
    				}
    			}
    		})
    		reslut = reslutDu
    	}else if (reslut.indexOf("departmentCode") != -1){
    		var reslutDu = ""
    		var ajaxDataArr = []
    		reslut.replace(/departmentCode([!=]{2})'(\w+)'([&|]+)?/g,function(a,b,c,d){
    			if(c){
    				ajaxDataArr.push(c)
    			}
    			if(d){
    				if(b=="=="){
    					reslutDu += "科室 等于 %"+c+"% "+logic[d]+"<br/>"
    				}else{
    					reslutDu += "科室 不等于 %"+c+"% "+logic[d]+"<br/>"
    				}
    			}else{
    				if(b == "=="){
    					reslutDu += "科室 等于 %"+c+"%"
    				}else{
    					reslutDu += "科室 不等于  %"+c+"%"
    				}
    			}
    		})
    		reslut = reslutDu
    		$http({
		    	method: 'GET',
		    	url: '../../oa/empName/deptNameByCode.action?code='+ajaxDataArr.join("-"),
			}).then(
				function successCallback(resData) {
					var response = resData.data.data
					$scope.conValue = ""
					for (var i = 0 ;i<response.length;i++) {
						reslut = reslut.replace("%"+ response[i]["code"] +"%",response[i]["name"])
					}
					$scope.conValue = reslut
				},
				function errorCallback(response) {
	        		console.log('请求失败！');
				}
	    	)	
    	}else if (reslut.indexOf("deptAreaCode") != -1){
    		var reslutDu = ""
    		reslut.replace(/deptAreaCode([!=]{2})'(\w+)'([&|]+)?/g,function(a,b,c,d){
    			if(d){
    				if(b=="=="){
    					reslutDu += "院区 等于 "+hisLine[c]+" "+logic[d]+"<br/>"
    				}else{
    					reslutDu += "院区 不等于 "+hisLine[c]+" "+logic[d]+"<br/>"
    				}
    			}else{
    				if(b == "=="){
    					reslutDu += "院区 等于 "+hisLine[c]
    				}else{
    					reslutDu += "院区 不等于  "+hisLine[c]
    				}
    			}
    		})
    		reslut = reslutDu
    	}
    	return reslut
    }
    
	$http({
    	method: 'GET',
    	url: '../../oa/empName/hisType.action',
	}).then(
		function successCallback(resData) {
			var response = resData.data
			userLine = response.userType
			hisLine = response.hisType
			$scope.conValue =  transition()
		},
		function errorCallback(response) {
    		console.log('请求失败！');
		}
	)
    $scope.conditionSave = function($event){
   		var data = $event.target.parentNode.parentNode.parentNode.getElementsByTagName("iframe")[0].contentWindow.save()
		if(data){
			$scope.conditionExpression = {value:'${'+ data+ "}"};
			$scope.property.value = '${'+ data+ "}"
			$scope.conValue =  transition()
			$scope.conditionClass = "mouldHide"
			$scope.addOrEgit = "修改"
			$scope.clearAllStyle = "mouldShow"
		}else{
			alert("请填写必要条件信息")
		}
    }
    $scope.save = function() {
        $scope.property.value = $scope.conditionExpression.value;
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    // Close button handler
    $scope.close = function() {
    	$scope.property.mode = 'read';
    	$scope.$hide();
    };
}];