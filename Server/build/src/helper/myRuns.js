"use strict";
exports.__esModule = true;
exports.calculateStats = /** @class */ (function () {
    function calculateStats() {
    }
    calculateStats.prototype.cal = function (data) {
        var totalSpeed;
        console.log(typeof data);
    };
    return calculateStats;
}());
//         var numberOfLocations = data.length;
//         for(var i = 0; i < numberOfLocations; i++) {
//             console.log(data[i]["mSpeed"])  
//         }
//         for(var i = 0; i < numberOfLocations - 1; i++){
//             var mlat =data[i]["mLatitude"];
//             var mlong =data[i]["mLongitude"];
//             var mlat2 = data[i+1]["mLatitude"];
//             var mlong2 = data[i+1]["mLongitude"];
//             this.totalDistance += distance(mlat,mlong,mlat2,mlong2);
//         }
//         this.avgSpeed = totalSpeed/numberOfLocations;  
//     }
//  }
// const distance  = function(lat1,lon1,lat2,lon2){
//     var R = 6371; // Radius of the earth in km
//     var dLat = deg2rad(lat2-lat1);  // deg2rad below
//     var dLon = deg2rad(lon2-lon1); 
//     var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//     Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
//     Math.sin(dLon/2) * Math.sin(dLon/2);
//     var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
//     var d = R * c; // Distance in km
//     return d;
// }
// const deg2rad = function(deg) {
//     return deg * (Math.PI/180)
//   }
//   export default calculateStats;
