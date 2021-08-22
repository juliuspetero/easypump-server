package com.easypump.model.bodaboda;

import com.easypump.model.aws.S3Document;
import com.easypump.model.common.AuditEntity;
import com.easypump.model.common.GenderEnum;
import com.easypump.model.common.IdTypeEnum;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Entity(name = "boda_boda")
@NoArgsConstructor
public class BodaBoda extends AuditEntity {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private GenderEnum gender;
    private String idNumber;
    private IdTypeEnum idType;
    private S3Document IdPhotograph;
    private S3Document headshot;
    private String email;
    private String phoneNumber;
    private String numberPlate;
    private OwnershipEnum ownership;
    private Stage stage;


    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "date_of_birth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Column(name = "id_number")
    public String getIdNumber() {
        return idNumber;
    }

    @Column(name = "id_type_enum")
    @Enumerated(EnumType.STRING)
    public IdTypeEnum getIdType() {
        return idType;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stage_id")
    public Stage getStage() {
        return stage;
    }

    @Column(name = "gender_enum")
    @Enumerated(EnumType.STRING)
    public GenderEnum getGender() {
        return gender;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_photograph_id")
    public S3Document getIdPhotograph() {
        return IdPhotograph;
    }

    @Column(name = "number_plate")
    public String getNumberPlate() {
        return numberPlate;
    }

    @Column(name = "ownership_enum")
    @Enumerated(EnumType.STRING)
    public OwnershipEnum getOwnership() {
        return ownership;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "headshot_id")
    public S3Document getHeadshot() {
        return headshot;
    }
}
