package at.sebastianhamm.backend.model;

import at.sebastianhamm.backend.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id")),
        @AttributeOverride(name = "email", column = @Column(name = "email", unique = true, nullable = false, length = 100)),
        @AttributeOverride(name = "password", column = @Column(name = "password", nullable = false, length = 100)),
        @AttributeOverride(name = "otpSecret", column = @Column(name = "otp_secret")),
        @AttributeOverride(name = "otpExpiry", column = @Column(name = "otp_expiry")),
        @AttributeOverride(name = "otpEnabled", column = @Column(name = "otp_enabled")),
        @AttributeOverride(name = "accountNonLocked", column = @Column(name = "account_non_locked")),
        @AttributeOverride(name = "enabled", column = @Column(name = "enabled")),
        @AttributeOverride(name = "failedAttempt", column = @Column(name = "failed_attempt")),
        @AttributeOverride(name = "lockTime", column = @Column(name = "lock_time")),
        @AttributeOverride(name = "passwordResetToken", column = @Column(name = "password_reset_token")),
        @AttributeOverride(name = "passwordResetTokenExpiry", column = @Column(name = "password_reset_token_expiry")),
        @AttributeOverride(name = "lastPasswordResetDate", column = @Column(name = "last_password_reset_date")),
        @AttributeOverride(name = "lastLogin", column = @Column(name = "last_login")),
        @AttributeOverride(name = "emailNotifications", column = @Column(name = "email_notifications")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false, updatable = false)),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
public class User extends UserEntity implements UserDetails {

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @NotBlank
    private boolean accountVerified;

    @Transient
    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

    // UserDetails methods

    @Override
    @JsonIgnore
    public String getUsername() {
        return getEmail();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    // Account lock management

    public void lockAccount() {
        setAccountNonLocked(false);
        setLockTime(LocalDateTime.now());
    }

    public void unlockAccount() {
        setAccountNonLocked(true);
        setLockTime(null);
        setFailedAttempt(0);
    }

    public boolean isAccountLocked() {
        return getLockTime() != null && getLockTime().plusMinutes(30).isAfter(LocalDateTime.now());
    }
}
