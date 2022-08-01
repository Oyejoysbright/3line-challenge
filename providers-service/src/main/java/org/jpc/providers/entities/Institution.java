package org.jpc.providers.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jpc.tool.enums.KycLevelEnum;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"emailAddress", "phoneNumber"})
    }
)
public class Institution {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private KycLevelEnum kyc = KycLevelEnum.LEVEL1;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String pin;
    private String password;
    private String accessToken;
    private String refreshToken;
    @Lob
    private String image;

    private boolean verified = false;
    private String resetPasswordToken;
    private String resetPasswordRef;

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<BankDetail> bankDetails = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<ContentCreator> creators = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet = new Wallet();

    @OneToOne(cascade = CascadeType.ALL)
    private SecurityQuestion securityQuestion;
}
