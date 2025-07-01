package at.sebastianhamm.backend.dto;

import at.sebastianhamm.backend.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User information.
 * Used for transferring user data between client and server.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User data transfer object")
public class UserDto {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "First name contains invalid characters")
    @Schema(description = "User's first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Last name contains invalid characters")
    @Schema(description = "User's last name", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "User's role", example = "ROLE_USER")
    private User.Role role;

    @Schema(description = "Whether the user account is enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Whether the user account is locked", example = "false")
    private boolean accountNonLocked;

    @Schema(description = "Whether OTP is enabled for the user", example = "true")
    private boolean otpEnabled;

    @Schema(description = "Whether email notifications are enabled", example = "true")
    private boolean emailNotifications;

    @Schema(description = "Number of failed login attempts", example = "0")
    private Integer failedAttempt;

    @Schema(description = "Date when the account was locked", example = "2023-01-01T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lockTime;

    @Schema(description = "Date when the user last logged in", example = "2023-01-01T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastLogin;

    @Schema(description = "Date when the password was last reset", example = "2023-01-01T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastPasswordResetDate;

    @Schema(description = "Timestamp when the user was created", example = "2023-01-01T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the user was last updated", example = "2023-01-01T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    @Schema(description = "User's password (write-only)", example = "P@ssw0rd!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase, one uppercase, one special character and no whitespace"
    )
    private String password;

    @Schema(description = "Confirmation password (write-only)", example = "P@ssw0rd!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity to convert
     * @return the converted UserDto
     */
    public static UserDto fromUser(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .otpEnabled(user.isOtpEnabled())
                .emailNotifications(user.isEmailNotifications())
                .failedAttempt(user.getFailedAttempt())
                .lockTime(user.getLockTime())
                .lastLogin(user.getLastLogin())
                .lastPasswordResetDate(user.getLastPasswordResetDate())
                .createdAt(user.getCreatedAt().toLocalDateTime())
                .updatedAt(user.getUpdatedAt().toLocalDateTime())
                .build();
    }

    /**
     * Converts this DTO to a User entity.
     *
     * @return the converted User entity
     */
    public User toUser() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password) // Should be encoded before saving
                .firstName(this.firstName)
                .lastName(this.lastName)
                .role(this.role != null ? this.role : User.Role.ROLE_USER)
                .enabled(this.enabled)
                .accountNonLocked(this.accountNonLocked)
                .otpEnabled(this.otpEnabled)
                .emailNotifications(this.emailNotifications)
                .build();
    }

    /**
     * Updates the given User entity with the values from this DTO.
     * Does not update sensitive fields like password.
     *
     * @param user the User entity to update
     */
    public void updateUser(User user) {
        if (this.email != null) user.setEmail(this.email);
        if (this.firstName != null) user.setFirstName(this.firstName);
        if (this.lastName != null) user.setLastName(this.lastName);
        if (this.role != null) user.setRole(this.role);
        user.setEnabled(this.enabled);
        user.setAccountNonLocked(this.accountNonLocked);
        user.setOtpEnabled(this.otpEnabled);
        user.setEmailNotifications(this.emailNotifications);
    }

    /**
     * Validates that the password and confirmPassword fields match.
     *
     * @return true if passwords match or both are null/empty, false otherwise
     */
    @JsonIgnore
    public boolean isPasswordMatching() {
        if (password == null && confirmPassword == null) {
            return true;
        }
        return password != null && password.equals(confirmPassword);
    }
}
