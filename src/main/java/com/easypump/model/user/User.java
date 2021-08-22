package com.easypump.model.user;

import com.easypump.model.common.AuditEntity;
import com.easypump.model.common.IdTypeEnum;
import com.easypump.model.role.Role;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Entity(name = "user")
@NoArgsConstructor
public class User extends AuditEntity {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private IdTypeEnum idType;
    private String idNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Column(name = "id_type_enum")
    @Enumerated(EnumType.STRING)
    public IdTypeEnum getIdType() {
        return idType;
    }

    @Column(name = "id_number")
    public String getIdNumber() {
        return idNumber;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return role;
    }
}
