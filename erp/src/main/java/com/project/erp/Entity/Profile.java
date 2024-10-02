package com.project.erp.Entity;

import com.project.erp.Enum.ProfileType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProfileId", unique = true)
    private Integer profileid;

    @Column(name = "UserName")
    private String username;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "DateOfBirth")
    private Date dateofbirth;

    @Column(name = "MobileNumber")
    private Integer mobilenumber;

    @Column(name = "Address")
    private String Address;

    @Enumerated(EnumType.STRING)
    @Column(name = "profiletype")
    private ProfileType profileType;
}
