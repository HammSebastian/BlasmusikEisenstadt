package at.sebastianhamm.backend.model;

import at.sebastianhamm.backend.entity.UserEntity;
import at.sebastianhamm.backend.security.SecurityConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@EntityListeners(AuditingEntityListener.class)
@Slf4j
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
    
    public String getFullName() {
        if (fullName == null) {
            fullName = String.format("%s %s", firstName, lastName).trim();
        }
        return fullName;
    }
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
    
    @Transient
    private String fullName;
    
    @PostLoad
    private void setFullName() {
        this.fullName = String.format("%s %s", firstName, lastName).trim();
    }
    
    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
    
    @Builder.Default
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;
    
    @Builder.Default
    @Column(name = "failed_attempt")
    private int failedAttempt = 0;
    
    @Column(name = "lock_time")
    private LocalDateTime lockTime;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Builder.Default
    @Column(name = "email_notifications")
    private boolean emailNotifications = true;
    
    @Builder.Default
    @Column(name = "otp_enabled")
    private boolean otpEnabled = false;
    
    @JsonIgnore
    @Column(name = "otp_secret")
    private String otpSecret;
    
    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;
    
    // For compatibility with UserEntity
    public String getVerifyOtp() {
        return this.otpSecret;
    }
    
    public void setVerifyOtp(String otp) {
        this.otpSecret = otp;
    }
    
    public boolean isAccountVerified() {
        return this.enabled;
    }
    
    public void setAccountVerified(boolean verified) {
        this.enabled = verified;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() != null && getId().equals(user.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    

    
    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }
    
    @Override
    @JsonIgnore
    public String getUsername() {
        return getEmail();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    // Helper methods
    public String getOtp() {
        return this.otpSecret;
    }
    
    @Override
    public String getUserId() {
        return super.getUserId();
    }
    
    public String getName() {
        return getFullName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public String getPasswordResetToken() {
        return super.getPasswordResetToken();
    }

    @Override
    public void setPasswordResetToken(String passwordResetToken) {
        super.setPasswordResetToken(passwordResetToken);
    }

    @Override
    public LocalDateTime getPasswordResetTokenExpiry() {
        return super.getPasswordResetTokenExpiry();
    }

    @Override
    public void setPasswordResetTokenExpiry(LocalDateTime passwordResetTokenExpiry) {
        super.setPasswordResetTokenExpiry(passwordResetTokenExpiry);
    }

    @Override
    public LocalDateTime getLastPasswordResetDate() {
        return super.getLastPasswordResetDate();
    }

    @Override
    public void setLastPasswordResetDate(LocalDateTime lastPasswordResetDate) {
        super.setLastPasswordResetDate(lastPasswordResetDate);
    }

    @Override
    public LocalDateTime getLastLogin() {
        return super.getLastLogin();
    }

    @Override
    public void setLastLogin(LocalDateTime lastLogin) {
        super.setLastLogin(lastLogin);
    }

    @Override
    public String getOtpSecret() {
        return super.getOtpSecret();
    }

    @Override
    public void setOtpSecret(String otpSecret) {
        super.setOtpSecret(otpSecret);
    }

    @Override
    public LocalDateTime getOtpExpiry() {
        return super.getOtpExpiry();
    }

    @Override
    public void setOtpExpiry(LocalDateTime otpExpiry) {
        super.setOtpExpiry(otpExpiry);
    }

    @Override
    public boolean isOtpEnabled() {
        return super.isOtpEnabled();
    }

    @Override
    public void setOtpEnabled(boolean otpEnabled) {
        super.setOtpEnabled(otpEnabled);
    }

    @Override
    public boolean isEmailNotifications() {
        return super.isEmailNotifications();
    }

    @Override
    public void setEmailNotifications(boolean emailNotifications) {
        super.setEmailNotifications(emailNotifications);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public Timestamp getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public Timestamp getUpdatedAt() {
        return super.getUpdatedAt();
    }


    public void lockAccount() {
        this.accountNonLocked = false;
        this.lockTime = LocalDateTime.now();
    }

    public void unlockAccount() {
        this.accountNonLocked = true;
        this.lockTime = null;
        this.failedAttempt = 0;
    }
    
    public boolean isAccountLocked() {
        return this.lockTime != null && this.lockTime.plusMinutes(30).isAfter(LocalDateTime.now());
    }
    
    public enum Role {
        ROLE_ADMIN,
        ROLE_EDITOR,
        ROLE_USER;
        
        public String getAuthority() {
            return SecurityConstants.ROLE_PREFIX + name();
        }
    }
    
    @JsonIgnore
    public String getAuthority() {
        return role.getAuthority();
    }
}
