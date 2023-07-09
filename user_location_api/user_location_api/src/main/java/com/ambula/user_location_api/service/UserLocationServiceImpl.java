package com.ambula.user_location_api.service;

import com.ambula.user_location_api.entity.UserLocation;
import com.ambula.user_location_api.repository.UserLocationRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserLocationServiceImpl implements IUserLocationService {

    UserLocationRepository userLocationRepository;

    @Override
    public void saveData(List<UserLocation> userLocationList) {
        userLocationRepository.saveAll(userLocationList);
    }

    @Override
    public UserLocation updateUserLocation(String name, UserLocation userLocation) throws NotFoundException {
        //Get all user location data
        List<UserLocation> allUserLocations = userLocationRepository.findAll();

        // Filter out the user location data with name
        Optional<UserLocation> optionalUserLocation = allUserLocations.stream()
                .filter(ul -> ul.getName().equals(userLocation.getName()))
                .findFirst();

        // update the data
        if (optionalUserLocation.isPresent()) {
            UserLocation existingUserLocation = optionalUserLocation.get();
            existingUserLocation.setLatitude(userLocation.getLatitude());
            existingUserLocation.setLongitude(userLocation.getLongitude());
            return userLocationRepository.save(existingUserLocation);
        } else {
            throw new NotFoundException("User location not found with name: " + userLocation.getName());
        }
    }


    @Override
    public List<UserLocation> getNearestLocationUser(int limit) {
        List<UserLocation> allUserLocations = userLocationRepository.findAll();
        return allUserLocations.stream()
                .map(ul -> new UserLocation(ul.getName(), ul.getLatitude(), ul.getLongitude()))
                .sorted(Comparator.comparingDouble(ul -> calculateDistance(ul.getLatitude(), ul.getLongitude())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserLocation(String name) {
        userLocationRepository.deleteById(name);
    }

    //region private method
    private double calculateDistance(double latitude, double longitude) {
        double x = latitude - 0;  // Assuming (0, 0) as reference point
        double y = longitude - 0; // Assuming (0, 0) as reference point

        // Calculate Euclidean distance
        return Math.sqrt(x * x + y * y);
    }
    //endregion
}
