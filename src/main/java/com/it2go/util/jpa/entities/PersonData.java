package com.it2go.util.jpa.entities;

import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * created by mmbarek on 10.11.2020.
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@Embeddable
public class PersonData {

  @Basic
  @Column(name = "FIRST_NAME", nullable = false, length = 100)
  private String firstName;

  @Basic
  @Column(name = "LAST_NAME", nullable = false, length = 100)
  private String lastName;

  @Basic
  @Column(name = "BIRTH_DATE", columnDefinition = "DATE")
  private LocalDate birthDate;

  @Column(name = "GENDER", length = 10)
  private String gender;

  @Column(name = "EMAIL")
  private String email;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "PUBLIC_ID", name = "ADDR_ID")
  private AddressEntity address;
}
