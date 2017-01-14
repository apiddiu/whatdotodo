angular.module('todoapp', [])
   .controller('todocontroller', ['$scope', '$filter',function($scope, $filter) {
       
    $scope.statuses = [
  {
    "name": "Closed",
    "sequence": 3
  },
  {
    "name": "In Progress",
    "sequence": 2
  },
  {
    "name": "Open",
    "sequence": 1
  }
];
    
    
    
    $scope.items = [
    {
        "id": 1,
        "title": "Add item",
        "description": "comando per aggiunta nuova carta",
        "status": "In Progress"
    },
    {
        "id": 2,
        "title": "Edit item",
        "description": "comando per modificare carta",
        "status": "Open"
    },
    {
        "id": 3,
        "title": "Change status",
        "description": "comando per cambiare stato",
        "status": "Closed"
    },
    {
        "id": 4,
        "title": "Delete item",
        "description": "comando per eliminare carta",
        "status": "Open"
    },
    {
        "id": 5,
        "title": "Back-end integration",
        "description": "integrazione con server",
        "status": "Open"
    },
        {
        "id": 6,
        "title": "Manage authentication",
        "description": "",
        "status": "Open"
    },
    ];
       
       $scope.move=function(item, status){
           console.info('eja');
           $filter('filter')($scope.items, function (i) {return i.id===item.id;})[0].status = status.name;
       }
}]);
