package com.easypump.model.bodaboda;

import com.easypump.model.common.AuditEntity;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Setter
@Entity(name = "boda_stage")
@NoArgsConstructor
public class Stage extends AuditEntity {
    private String name;
    private String region;
    private String district;
    private String county;
    private String subCounty;
    private String village;
    private String managerName;
    private String managerPhoneNumber;
    private String managerEmail;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "region")
    public String getRegion() {
        return region;
    }

    @Column(name = "district")
    public String getDistrict() {
        return district;
    }

    @Column(name = "county")
    public String getCounty() {
        return county;
    }

    @Column(name = "sub_county")
    public String getSubCounty() {
        return subCounty;
    }

    @Column(name = "village")
    public String getVillage() {
        return village;
    }

    @Column(name = "manager_name")
    public String getManagerName() {
        return managerName;
    }

    @Column(name = "manager_phone_number")
    public String getManagerPhoneNumber() {
        return managerPhoneNumber;
    }

    @Column(name = "manager_email")
    public String getManagerEmail() {
        return managerEmail;
    }
}
