package at.sebastianhamm.backend.repository;

import at.sebastianhamm.backend.model.Role;
import at.sebastianhamm.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    Page<User> findByEnabled(boolean enabled, Pageable pageable);

    Page<User> findByAccountNonLocked(boolean accountNonLocked, Pageable pageable);

    List<User> findByOtpEnabled(boolean otpEnabled);

    List<User> findByEmailNotifications(boolean enabled);

    Optional<User> findByPasswordResetToken(String token);

    List<User> findByLastLoginBefore(LocalDateTime date);

    @Modifying
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :userId")
    int updateEnabledStatus(Long userId, boolean enabled);

    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = :accountNonLocked, u.lockTime = :lockTime, u.failedAttempt = :failedAttempt WHERE u.id = :userId")
    int updateAccountLock(Long userId, boolean accountNonLocked, LocalDateTime lockTime, int failedAttempt);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = :failedAttempt WHERE u.email = :email")
    void updateFailedAttempts(String email, int failedAttempt);

    @Modifying
    @Query("UPDATE User u SET u.password = :password, u.lastPasswordResetDate = :resetDate WHERE u.id = :userId")
    void updatePassword(Long userId, String password, LocalDateTime resetDate);

    @Modifying
    @Query("UPDATE User u SET u.passwordResetToken = :token, u.passwordResetTokenExpiry = :expiry WHERE u.email = :email")
    void updatePasswordResetToken(String email, String token, LocalDateTime expiry);

    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(Long userId, LocalDateTime lastLogin);

    @Modifying
    @Query("UPDATE User u SET u.otpSecret = :otpSecret, u.otpExpiry = :otpExpiry, u.otpEnabled = :otpEnabled WHERE u.id = :userId")
    void updateOtpDetails(Long userId, String otpSecret, LocalDateTime otpExpiry, boolean otpEnabled);

    @Modifying
    @Query("UPDATE User u SET u.emailNotifications = :enabled WHERE u.id = :userId")
    void updateEmailNotifications(Long userId, boolean enabled);

    @Modifying
    @Query("DELETE FROM User u WHERE u.enabled = false AND u.createdAt < :date")
    int deleteInactiveUsers(LocalDateTime date);
}
