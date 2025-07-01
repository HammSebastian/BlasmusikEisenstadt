package at.sebastianhamm.backend.repository;

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
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailWithLock(@Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") User.Role role);
    
    @Query("SELECT u FROM User u WHERE u.enabled = :enabled")
    Page<User> findByEnabled(@Param("enabled") boolean enabled, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.accountNonLocked = :locked")
    Page<User> findByAccountLocked(@Param("locked") boolean locked, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.otpEnabled = :otpEnabled")
    List<User> findByOtpEnabled(@Param("otpEnabled") boolean otpEnabled);
    
    @Query("SELECT u FROM User u WHERE u.emailNotifications = :enabled")
    List<User> findByEmailNotificationsEnabled(@Param("enabled") boolean enabled);
    
    @Query("SELECT u FROM User u WHERE u.passwordResetToken = :token")
    Optional<User> findByPasswordResetToken(@Param("token") String token);
    
    @Query("SELECT u FROM User u WHERE u.lastLogin < :date")
    List<User> findInactiveSince(@Param("date") LocalDateTime date);
    
    @Modifying
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :userId")
    int updateEnabledStatus(@Param("userId") Long userId, @Param("enabled") boolean enabled);
    
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = :locked, u.lockTime = :lockTime, u.failedAttempt = :attempts WHERE u.id = :userId")
    int updateAccountLock(@Param("userId") Long userId, 
                         @Param("locked") boolean locked, 
                         @Param("lockTime") LocalDateTime lockTime, 
                         @Param("attempts") int attempts);
    
    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = :attempts WHERE u.email = :email")
    void updateFailedAttempts(@Param("email") String email, @Param("attempts") int attempts);
    
    @Modifying
    @Query("UPDATE User u SET u.password = :password, u.lastPasswordResetDate = :resetDate WHERE u.id = :userId")
    void updatePassword(@Param("userId") Long userId, 
                       @Param("password") String password, 
                       @Param("resetDate") LocalDateTime resetDate);
    
    @Modifying
    @Query("UPDATE User u SET u.passwordResetToken = :token, u.passwordResetTokenExpiry = :expiry WHERE u.email = :email")
    void updatePasswordResetToken(@Param("email") String email, 
                                @Param("token") String token, 
                                @Param("expiry") LocalDateTime expiry);
    
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);
    
    @Modifying
    @Query("UPDATE User u SET u.otpSecret = :otpSecret, u.otpExpiry = :otpExpiry, u.otpEnabled = :enabled WHERE u.id = :userId")
    void updateOtpDetails(@Param("userId") Long userId, 
                         @Param("otpSecret") String otpSecret, 
                         @Param("otpExpiry") LocalDateTime otpExpiry, 
                         @Param("enabled") boolean enabled);
    
    @Modifying
    @Query("UPDATE User u SET u.emailNotifications = :enabled WHERE u.id = :userId")
    void updateEmailNotifications(@Param("userId") Long userId, @Param("enabled") boolean enabled);
    
    @Modifying
    @Query("DELETE FROM User u WHERE u.enabled = false AND u.createdAt < :date")
    int deleteInactiveUsers(@Param("date") LocalDateTime date);
}
