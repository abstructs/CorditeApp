import { LocationModel } from "./Location";

export const getDistance = (location1: LocationModel, location2: LocationModel): Number => {
    const radius = 6371.009;
    const radians = (Math.PI / 180);

    const point1 = {
        latitude: location1.mLatitude.valueOf() * radians,
        longitude: location1.mLongitude.valueOf() * radians
    }

    const point2 = {
        latitude: location1.mLatitude.valueOf() * radians,
        longitude: location2.mLongitude.valueOf() * radians
    }

    const deltaLat = (point2.latitude - point1.latitude);
    const deltaLong = (point2.longitude - point1.longitude);
    const deltaMean = (point1.latitude + point2.latitude) / 2;
    const square = (Math.pow((deltaLat), 2)) + (Math.pow(Math.cos(deltaMean) * deltaLong, 2));
   
    let distanceOfPoints = Number((radius * Math.sqrt(square)).toFixed(3));

    return distanceOfPoints;
}