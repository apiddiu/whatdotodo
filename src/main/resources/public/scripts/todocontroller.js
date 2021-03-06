angular.module('todoapp', [])
   .controller('todocontroller', ['$scope', '$filter', '$http',function($scope, $filter, $http) {

    loadPrincipal();
    loadStatuses();
    loadItems();

    function loadPrincipal(){
            $http({
                    method: "get",
                    url: "api/principal",
                    params: {
                        action: "get"
                    }
                }).then(function successCallback(response) {
                       $scope.user = response.data;
                     }, function errorCallback(response) {
                       console.error("Error loading Principal");
                     });

               }

    function loadStatuses(){
            $http({
                method: "get",
                url: "api/statuses",
                params: {
                    action: "get"
                }
            }).then(function successCallback(response) {
                   $scope.statuses = response.data;
                 }, function errorCallback(response) {
                   console.error("Error loading Statuses");
                 });

           }

    function loadItems(){
        $http({
            method: "get",
            url: "api/items",
            params: {
                action: "get"
            }
        }).then(function successCallback(response) {
               $scope.items = response.data;
             }, function errorCallback(response) {
               console.error("Error loading Items");
             });

       }

       function doAdd(item){
            $http({
                    method: "post",
                    url: "api/items",
                    params: {
                        action: "add"
                    },
                    data: item

                }).then(function successCallback(response) {
                       $scope.items.push(response.data);
                     }, function errorCallback(response) {
                       console.error("Error adding Item");
                     });

               }

        function doEdit(item){
                    $http({
                            method: "put",
                            url: "api/items/"+item.id,
                            params: {
                                action: "edit"
                            },
                            data: item

                        }).then(function successCallback(response) {
                               $scope.items[findItem(item)]=response.data;
                             }, function errorCallback(response) {
                               console.error("Error updating Item");
                             });

                       }

       function doMove(item, status){
           $http({
                   method: "put",
                   url: "api/items/"+item.id+"/status",
                   params: {
                       action: "edit"
                   },
                   data: status

               }).then(function successCallback(response) {
               console.info("Received: "+response.data)
                      $scope.items[findItem(item)].status=response.data.name;
                    }, function errorCallback(response) {
                      console.error("Error updating Item status");
                    });

              }

       function doDelete(item){
                   $http({
                           method: "delete",
                           url: "api/items/"+item.id,
                           params: {
                               action: "delete"
                           },
                           data: item

                       }).then(function successCallback(response) {
                              $scope.items.splice(findItem(response.data),1);
                            }, function errorCallback(response) {
                              console.error("Error deleting Item");
                            });

                      }

       function findItem(item){
            return $scope.items.indexOf($filter('filter')($scope.items, function (i) {return i.id===item.id;})[0]);
       }


       $scope.move=function(item, status){
           doMove(item, status);
       }
       
       $scope.prepareAdd=function(status){
           $scope.newItem={
                title: null,
                description: null,
                status: status.name    
           }
       }
       
       $scope.confirmAdd=function(){
           doAdd($scope.newItem);
       }
       
       $scope.prepareEdit=function(item){
           $scope.editItem={
                id: item.id,
                title: item.title,
                description: item.description,
                status: item.status
           }
       }

       $scope.confirmEdit=function(){
           doEdit($scope.editItem);
       }

       $scope.prepareDelete=function(item){
                  $scope.deleteItem=item;
       }

       $scope.confirmDelete=function(){
                  doDelete($scope.deleteItem);
       }
}]);
