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
 * Assignment
 */
var KisBpmAssignmentCtrl = [ '$scope', '$modal', function($scope, $modal) {

    // Config for the modal window
    var opts = {
        template:  'editor-app/configuration/properties/assignment-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmAssignmentPopupCtrl = [ '$scope',"$http", function($scope,$http) {
    //console.log(fromInfoService)	
    //"fromInfoService"
    
    // Put json representing assignment on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.assignment !== undefined
        && $scope.property.value.assignment !== null) 
    {
        $scope.assignment = $scope.property.value.assignment;
    } else {
        $scope.assignment = {};
    }

    if ($scope.assignment.candidateUsers == undefined || $scope.assignment.candidateUsers.length == 0)
    {
    	$scope.assignment.candidateUsers = [{value: ''}];
    	$scope.assignment.candidateUserShow = [{value:"无条件"}]
    }
  
    //外加的数据回显
   
    // Click handler for + button after enum value
    var userValueIndex = 1;
//    $scope.addCandidateUserValue = function(index) {
//        $scope.assignment.candidateUsers.splice(index + 1, 0, {value: 'value ' + userValueIndex++});
//    };

    // Click handler for - button after enum value
//    $scope.removeCandidateUserValue = function(index) {
//        $scope.assignment.candidateUsers.splice(index, 1);
//    };
    
    if ($scope.assignment.candidateGroups == undefined || $scope.assignment.candidateGroups.length == 0)
    {
    	$scope.assignment.candidateGroups = [{value: ''}];
    	$scope.assignment.candidateGroupShow = [{value:"无条件"}]
    }
    var groupValueIndex = 1;
//    $scope.addCandidateGroupValue = function(index) {
//        $scope.assignment.candidateGroups.splice(index + 1, 0, {value: 'value ' + groupValueIndex++});
//    };

    // Click handler for - button after enum value
//    $scope.removeCandidateGroupValue = function(index) {
//        $scope.assignment.candidateGroups.splice(index, 1);
//    };

	$scope.userStyle = "mouldHide"
	
	$scope.deptStyle = "mouldHide"
	$scope.clearAllStyle1 = "mouldShow"
	$scope.clearAllStyle2 = "mouldShow"
	//切换
	if($scope.assignment.candidateUsers[0].value){
		$scope.assigneeTypeData = "0"
		$scope.deptStyleBox = "mouldHide"
	}
	if($scope.assignment.candidateGroups[0].value){
		$scope.assigneeTypeData = "1"
		$scope.userStyleBox = "mouldHide"
	}
	if(!$scope.assignment.candidateGroups[0].value && !$scope.assignment.candidateGroups[0].value){
		$scope.assigneeTypeData = "0"
		$scope.deptStyleBox = "mouldHide"
	}
	$scope.typeChang = function(){
		$scope.assignment.candidateUsers = [{value:""}]
		$scope.assignment.candidateUserShow = [{value:"无条件"}]
		$scope.assignment.candidateGroups = [{value: ""}]
		$scope.assignment.candidateGroupShow = [{value: "无条件"}]
		$scope.clearAllStyle1 = "mouldHide"
		$scope.clearAllStyle2 = "mouldHide"
		if($scope.assigneeTypeData === "1"){
			$scope.userStyleBox = "mouldHide"
			$scope.deptStyleBox = "mouldShow"
		}else{
			$scope.userStyleBox = "mouldShow"
			$scope.deptStyleBox = "mouldHide"
		}
	}
	
	$scope.deptCloseWin = function(){
		$scope.deptStyle = "mouldHide"
	}
	$scope.deptSave = function($event){
		var data = $event.target.parentNode.parentNode.parentNode.getElementsByTagName("iframe")[0].contentWindow.save()
		if(data){
			$scope.deptStyle = "mouldHide"
			$scope.assignment.candidateGroups = [{value: data.code}]
			$scope.assignment.candidateGroupShow = [{value: data.name}]
			$scope.clearAllStyle1 = "mouldShow"
		}
	}	
	$scope.userCloseWin = function(){
		$scope.userStyle = "mouldHide"
	}
	$scope.userSave = function($event){
		var data = $event.target.parentNode.parentNode.parentNode.getElementsByTagName("iframe")[0].contentWindow.save()
		if(data){
			$scope.userStyle = "mouldHide"
			$scope.assignment.candidateUsers = [{value: data.code}]
			$scope.assignment.candidateUserShow = [{value: data.name}]
			$scope.userTmpDa = data.resData
			$scope.resUserNode = data.resUserNode
			$scope.clearAllStyle2 = "mouldShow"
		}
	}
	var userTmp = $scope.assignment.candidateUsers[0].value
	var deptTmp = $scope.assignment.candidateGroups[0].value
 	$scope.assignment.candidateUserShow = [{value:userTmp}]
    $scope.assignment.candidateGroupShow = [{value:deptTmp}]
    
   	$scope.userValue = $scope.assignment.candidateUserShow
   	$scope.save = function() {
        $scope.property.value = {};
        handleAssignmentInput($scope);
        $scope.property.value.assignment = $scope.assignment;
        
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    // Close button handler
    $scope.close = function() {
    	handleAssignmentInput($scope);
    	$scope.property.mode = 'read';
    	$scope.$hide();
    };
    var httpData =  $scope.assignment.candidateUsers[0].value
    $scope.userTmpDa = []
    $scope.resUserNode = ""
    if(httpData.indexOf("userHand:") == -1 && httpData !=""){
	    $http({
	    	method: 'GET',
	    	url: '../../oa/empName/empNameByCode.action?code='+httpData.replace(/\(|\)|userHand:/g,""),
		}).then(
			function successCallback(response) {
				var response = response.data
				if(response.resCode == "success"){
					if(response.data.length != 0){
						var showUserData = "指派人 "
						if(httpData.indexOf("userHand") != -1){
							showUserData += "等于 "
						}else{
							showUserData += "包含 "
						}
						for(var i = 0 ;i<response.data.length;i++){
							showUserData += response.data[i]["name"] + "-"
						}
						$scope.assignment.candidateUserShow = [{value:showUserData.substr(0,showUserData.length-1)}] 
					}else{
						$scope.assignment.candidateUserShow = [{value:""}]
					}
					$scope.userTmpDa = response.data
					$scope.userFieldShowClick = function($event){
						$scope.userStyle = "mouldShow"
						document.getElementById("userMoIf").contentWindow.start({
							code:$scope.assignment.candidateUsers[0].value,
							data:$scope.userTmpDa ,
							resUserNode:$scope.resUserNode
						})
					}
				}else{
					$scope.userFieldShowClick = function($event){
						$scope.userStyle = "mouldShow"
						document.getElementById("userMoIf").contentWindow.start({
							code:$scope.assignment.candidateUsers[0].value,
							data:$scope.userTmpDa ,
							resUserNode:$scope.resUserNode
						})
					}
				}
	        }, function errorCallback(response) {
	        	console.log('请求失败！');
	        }
	    );
    }else{
    	$scope.userFieldShowClick = function($event){
			$scope.userStyle = "mouldShow"
			document.getElementById("userMoIf").contentWindow.start({
				code:$scope.assignment.candidateUsers[0].value,
				data:$scope.userTmpDa ,
				resUserNode:$scope.resUserNode
			})
		}
    }
    //候选组
    var depthttpData =  $scope.assignment.candidateGroups[0].value
    if(depthttpData){
    	depthttpData = depthttpData.substr(1,depthttpData.length-2)
    	var resStr = ""
    	var depthttpDataArr = depthttpData.split("&&")
    	for (var i = 0; i<depthttpDataArr.length;i++) {
    		if(i>0){
    			resStr += " 并且 "
    		}
    		var deptOneStr =  depthttpDataArr[i]
    		deptOneStr = deptOneStr.replace(/dept:(\w+(\-\w+)*)/g,function(a,b){
    			return "科室 包含 %"+ b +"%"
    		})
    		deptOneStr = deptOneStr.replace(/userHand:(\w+)/g,function(a,b){
    			return "科室 等于 任务"+ b +"人员所属科室"
    		})
    		deptOneStr = deptOneStr.replace(/dept:\{departmentModlue\-\w+\}/g,function(a,b){
    			return "科室 等于 所选科室"
    		})
    		deptOneStr = deptOneStr.replace(/dept:\{divisionCode\}/g,function(a,b){
    			return "科室 等于 发起人所属学部"
    		})
    		deptOneStr = deptOneStr.replace(/dept:\{generalbranchCode\}/g,function(a,b){
    			return "科室 等于 发起人所属总支"
    		})
    		deptOneStr = deptOneStr.replace(/mana:(\w+(\-\w+)*)/g,function(a,b){
    			return "决策组 包含 @"+ b +"@"
    		})
    		deptOneStr = deptOneStr.replace(/divi:\{dept\}/g,function(a,b){
    			return "分管院长 等于 主管院领导"
    		})
    		deptOneStr = deptOneStr.replace(/duty:\{dept\}/g,function(a,b){
    			return "分管负责人 等于 负责人"
    		})
    		resStr += deptOneStr
    	}
    	$scope.assignment.candidateGroupShow = [{value:resStr}]
    	resStr.replace(/%(\w+(\-\w+)*)%/,function(a,b){
    		$http({
		    	method: 'GET',
		    	url: '../../oa/empName/deptNameByCode.action?code='+b,
			}).then(
				function successCallback(response) {
					var response = response.data
					var ressre = ""
					for (var i = 0 ;i<response.data.length;i++) {
						ressre += response.data[i].name + "-"
					}
					if(response.data.length>0){
						ressre = ressre.substr(0,ressre.length-1)
					}
					resStr = resStr.replace(/(%(\w+(\-\w+)*)%)/,ressre)
					$scope.assignment.candidateGroupShow = [{value:resStr}]
				},
				function errorCallback(response) {
	        		console.log('请求失败！');
				}
	    	)	
    	})
    	resStr.replace(/@(\w+(\-\w+)*)@/,function(a,b){
    		$http({
		    	method: 'GET',
		    	url: '../../oa/empName/queryJobByCode.action?code='+b,
			}).then(
				function successCallback(response) {
					var response = response.data
					var ressre = ""
					for (var i = 0 ;i<response.data.length;i++) {
						ressre += response.data[i].name + "-"
					}
					if(response.data.length>0){
						ressre = ressre.substr(0,ressre.length-1)
					}
					resStr = resStr.replace(/(@(\w+(\-\w+)*)@)/,ressre)
					$scope.assignment.candidateGroupShow = [{value:resStr}]
				},
				function errorCallback(response) {
	        		console.log('请求失败！');
				}
	    	)	
    	})
    }
    
   	$scope.groupFieldClick = function(){
   		$scope.deptStyle = "mouldShow"
   		document.getElementById("deptMoIf").contentWindow.start({
   			name:$scope.assignment.candidateGroupShow[0].value,
   			code:$scope.assignment.candidateGroups[0].value
   		})
    }
   	$scope.clearAll = function(){
   		$scope.assignment.candidateUsers = [{value:""}]
		$scope.assignment.candidateUserShow = [{value:"无条件"}]
		$scope.assignment.candidateGroups = [{value: ""}]
		$scope.assignment.candidateGroupShow = [{value: "无条件"}]
		$scope.clearAllStyle1 = "mouldHide"
		$scope.clearAllStyle2 = "mouldHide"
    }
   	if($scope.assignment.candidateGroupShow[0].value == ""){
    	$scope.assignment.candidateGroupShow = [{value:"无条件"}]
    	$scope.clearAllStyle1 = "mouldHide"
    }
	if($scope.assignment.candidateUserShow[0].value == ""){
    	$scope.assignment.candidateUserShow = [{value:"无条件"}]
    	$scope.clearAllStyle2 = "mouldHide"
    }
   	
    var handleAssignmentInput = function($scope) {
    	if ($scope.assignment.candidateUsers)
    	{
	    	var emptyUsers = true;
	    	var toRemoveIndexes = [];
	        for (var i = 0; i < $scope.assignment.candidateUsers.length; i++)
	        {
	        	if ($scope.assignment.candidateUsers[i].value != '')
	        	{
	        		emptyUsers = false;
	        	}
	        	else
	        	{
	        		toRemoveIndexes[toRemoveIndexes.length] = i;
	        	}
	        }
	        
	        for (var i = 0; i < toRemoveIndexes.length; i++)
	        {
	        	$scope.assignment.candidateUsers.splice(toRemoveIndexes[i], 1);
	        }
	        
	        if (emptyUsers)
	        {
	        	$scope.assignment.candidateUsers = undefined;
	        }
    	}
        
    	if ($scope.assignment.candidateGroups)
    	{
	        var emptyGroups = true;
	        var toRemoveIndexes = [];
	        for (var i = 0; i < $scope.assignment.candidateGroups.length; i++)
	        {
	        	if ($scope.assignment.candidateGroups[i].value != '')
	        	{
	        		emptyGroups = false;
	        	}
	        	else
	        	{
	        		toRemoveIndexes[toRemoveIndexes.length] = i;
	        	}
	        }
	        
	        for (var i = 0; i < toRemoveIndexes.length; i++)
	        {
	        	$scope.assignment.candidateGroups.splice(toRemoveIndexes[i], 1);
	        }
	        
	        if (emptyGroups)
	        {
	        	$scope.assignment.candidateGroups = undefined;
	        }
    	}
    };
}];