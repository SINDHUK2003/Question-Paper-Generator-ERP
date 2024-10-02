package com.project.erp.Controller;

import com.project.erp.Entity.Profile;
import com.project.erp.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/createFaculty")
    public Profile createFaculty(@RequestBody Profile profile) {
        return profileService.createFaculty(profile);
    }

    @PostMapping("/createAdmin")
    public Profile createAdmin(@RequestBody Profile profile) {
        return profileService.createAdmin(profile);
    }

    @PostMapping("/loginAdmin")
    public String loginAdmin(@RequestParam String email, @RequestParam String password) {
        return profileService.loginAdmin(email, password);
    }

    @PostMapping("/otherLogin")
    public String otherLogin(@RequestParam String email, @RequestParam String password) {
        return profileService.otherLogin(email, password);
    }

    @PostMapping("/logout")
    public String logout(@RequestParam int profileId) {
        return profileService.logout(profileId);
    }

    @DeleteMapping("/delete/{profileId}")
    public String deleteProfile(@PathVariable int profileId) {
        profileService.deleteProfile(profileId);
        return "Profile deleted successfully";
    }

    @GetMapping("/get/{profileId}")
    public Profile getProfile(@PathVariable int profileId) {
        return profileService.getProfile(profileId);
    }

    @PutMapping("/update/{profileId}")
    public Profile updateProfile(@PathVariable int profileId, @RequestBody Profile profile) {
        return profileService.updateProfile(profileId, profile);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        return profileService.resetPassword(email, newPassword);
    }

    @GetMapping("/loggedInUsers")
    public Map<Integer, String> getLoggedInUsers() {
        return profileService.getLoggedInUsers();
    }

    @GetMapping("/adminLoggedIn")
    public Map<Integer, String> getAdminLoggedIn() {
        return profileService.getAdminLoggedIn();
    }

    @GetMapping("/facultyLoggedIn")
    public Map<Integer, String> getFacultyLoggedIn() {
        return profileService.getFacultyLoggedIn();
    }
}