package com.threeht.havenhotelapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    private String email;

    @JsonIgnore
    @Nonnull
    private String password;

    @Nonnull
    private String firstName;

    @Nonnull
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @Nonnull
    @Builder.Default
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private Date updatedAt;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private Date deletedAt;

    @Nonnull
    private Collection<Role> roles;
}
