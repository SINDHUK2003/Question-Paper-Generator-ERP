package com.project.erp.Service;

import com.project.erp.Entity.Profile;
import com.project.erp.Enum.ProfileType;
import com.project.erp.Repository.ProfileRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    private Map<Integer, String> loggedInUsers = new HashMap<>();
    private Map<Integer, String> adminLoggedIn = new HashMap<>();
    private Map<Integer, String> facultyLoggedIn = new HashMap<>();


    public Profile createFaculty(Profile profile) {

        if (profile.getProfileType() == ProfileType.FACULTY){

//            Profile savedProfile = profileRepository.save(profile);
//            System.out.println("Profile saved: " + savedProfile);

            profileRepository.save(profile);
            return profile;
        } else {
            throw new IllegalArgumentException("Profile creation is only allowed for ProfileType.USER");
        }
    }

    public Profile createAdmin(Profile profile) {

        if (profile.getProfileType() == ProfileType.ADMIN){

          profileRepository.save(profile);


            return profile;
        } else {
            throw new IllegalArgumentException("Profile creation is only allowed for ProfileType.USER");
        }
    }

    public String loginAdmin(String email, String password) {
        Profile profile = profileRepository.findByEmail(email);
        if (profile != null && profile.getPassword().equals(password) && profile.getProfileType() == ProfileType.ADMIN) {
            int profileId = profile.getProfileid();
            adminLoggedIn.put(profileId, email);
            loggedInUsers.put(profileId, email);
            return "Login successful!";
        } else {
            return "Only users can login , Check your data";
        }
    }

    public String otherLogin(String email, String password) {
        Profile profile = profileRepository.findByEmail(email);
        if (profile != null && profile.getPassword().equals(password)) {
            ProfileType profileType = profile.getProfileType();
            int profileId = profile.getProfileid();
            switch (profileType) {
                case FACULTY:
                    facultyLoggedIn.put(profileId, email);
                    break;
//                case EMPLOYEE:
//                    employeeLoggedInUsers.put(profileId, email);
//                    break;
//                case SELLER:
//                    sellerLoggedInUsers.put(profileId, email);
//                    break;
                default:
                    return "Invalid profile type for this login";
            }
            loggedInUsers.put(profileId, email);
            return profileType + " login successful!";
        } else {
            return "Invalid email or password for other login";
        }
    }
    public String logout(int profileId) {
        if (loggedInUsers.containsKey(profileId)) {
            loggedInUsers.remove(profileId);
            adminLoggedIn.remove(profileId);
            facultyLoggedIn.remove(profileId);
            return "Logout successful!";
        } else {
            return "User is not logged in";
        }
    }


    public void deleteProfile(int profileid) {
        if (loggedInUsers.containsKey(profileid)) {
            profileRepository.deleteById(profileid);
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }


    public Profile getProfile(int profileid)
    {
        Optional<Profile> dispProfile = profileRepository.findById(profileid);
        return dispProfile.orElse(null);
    }

    public Profile updateProfile(int profileid, Profile profile)
    {
        if (loggedInUsers.containsKey(profileid))
        {
            Profile profile1 = profileRepository.findById(profileid).orElseThrow(() -> new RuntimeException("Profile not found with profileid:" + profileid));
            profile1.setUsername(profile.getUsername());
            profile1.setEmail(profile.getEmail());
            profile1.setGender(profile.getGender());
            profile1.setDateofbirth(profile.getDateofbirth());

            return profileRepository.save(profile1);
        }
        else
        {
            System.out.println("User must be logged in to update profile");
            throw new IllegalStateException("User not logged in");
        }
    }

    public String resetPassword(String email, String newPassword) {
        Profile profile = profileRepository.findByEmail(email);

        if (profile == null) {
            throw new RuntimeException("User with email " + email + " not found!");
        }

        if (!loggedInUsers.containsKey(profile.getProfileid())) {
            throw new IllegalStateException("User not logged in");
        }

        profile.setPassword(newPassword);
        profileRepository.save(profile);
        return "Password reset successful!";
    }

    public Map<Integer, String> getLoggedInUsers()
    {
        return new HashMap<>(loggedInUsers);
    }

    public Map<Integer, String > getAdminLoggedIn()
    {
        return new HashMap<>(adminLoggedIn);
    }

    public Map<Integer, String> getFacultyLoggedIn()
    {
        return new HashMap<>(facultyLoggedIn);
    }


    public boolean isUserLoggedIn(int profileId) {
        return loggedInUsers.containsKey(profileId);
    }

    public boolean isFacultyLoggedIn(int profileId) {
        return facultyLoggedIn.containsKey(profileId);
    }



}
