package com.ambula.user_location_api.service;

import com.ambula.user_location_api.entity.UserLocation;
import javassist.NotFoundException;

import java.util.List;

public interface IUserLocationService {
    void saveData(List<UserLocation> userLocationList);

    UserLocation updateUserLocation(String name, UserLocation userLocation) throws NotFoundException;

    List<UserLocation> getNearestLocationUser(int N);

    void deleteUserLocation(String name);
}
