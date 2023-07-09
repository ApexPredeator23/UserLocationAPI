package com.ambula.user_location_api.controller;

import com.ambula.user_location_api.entity.UserLocation;
import com.ambula.user_location_api.service.IUserLocationService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserLocationController {

    IUserLocationService userLocationService;

    // region Admin

    //To save the user location data
    @PostMapping("/create_data")
    public ResponseEntity<String> createData(@RequestHeader("X-UserRole") String role,
                                             @RequestBody List<UserLocation> userLocationList) {
        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        userLocationService.saveData(userLocationList);
        return ResponseEntity.status(HttpStatus.CREATED).body("User Location Data Saved Successfully");
    }

    // To update the user location data
    @PatchMapping("/update_data")
    public ResponseEntity<UserLocation> updateUserLocation(@RequestHeader("X-UserRole") String role,
                                                           @RequestParam String name,
                                                           @RequestBody UserLocation userLocation) {
        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            UserLocation updatedUserLocation = userLocationService.updateUserLocation(name, userLocation);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedUserLocation);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // To delete the user location data
    @DeleteMapping("/delete_data")
    public ResponseEntity<String> deleteUserLocation(@RequestHeader("X-UserRole") String role,
                                                     String name) {
        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userLocationService.deleteUserLocation(name);
        return ResponseEntity.status(HttpStatus.OK).body(name+" Deleted Successfully");
    }

    //endregion

    //region Reader

    //To get the nearest user location data
    @GetMapping("/get_users")
    public ResponseEntity<List<UserLocation>> getUsers(@RequestHeader("X-UserRole") String role,
                                                       @RequestParam int limit) {
        if(!role.equals("READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<UserLocation> userLocationList = userLocationService.getNearestLocationUser(limit);
        if(userLocationList == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(userLocationList);
        }
    }

    //endregion
}
