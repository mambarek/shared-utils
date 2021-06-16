package com.it2go.util.jpa.entities;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADDRESS")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addr_seq_gen")
    @SequenceGenerator(name = "addr_seq_gen", sequenceName = "addr_seq", allocationSize = 50)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "PUBLIC_ID", unique = true, nullable = false)
    private UUID publicId;

    @Basic
    @Column(name = "STREET_ONE", length = 100,nullable = false)
    private String streetOne;

    @Basic
    @Column(name = "BUILDING_NR", length = 5, nullable = false)
    private String buildingNr;

    @Basic
    @Column(name = "STREET_TWO", length = 100)
    private String streetTwo;

    @Basic
    @Column(name = "ZIP_CODE", length = 10, nullable = false)
    private String zipCode;

    @Basic
    @Column(name = "CITY", length = 100, nullable = false)
    private String city;

    @Basic
    @Column(name = "COUNTRY_CODE", length = 3, nullable = false)
    private String countryCode;
}
