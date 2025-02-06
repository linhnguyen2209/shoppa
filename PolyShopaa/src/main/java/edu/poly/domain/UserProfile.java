package edu.poly.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UserProfiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;

    @Column(columnDefinition = "nvarchar(60)", nullable = false)
    private String fullName;

    @Column(columnDefinition = "nvarchar(60)")
    private String avatar;

    @Column(length = 13)
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(columnDefinition = "nvarchar(100)")
    private String shippingAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "username")
    private User user;
}
