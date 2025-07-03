package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.dto.ChangePasswordRequest;
import at.sebastianhamm.backend.dto.UserDto;
import at.sebastianhamm.backend.security.CurrentUser;
import at.sebastianhamm.backend.security.UserPrincipal;
import at.sebastianhamm.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing user accounts")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(createdUser.getId()).toUri();
        logger.info("User registered with id {}", createdUser.getId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserDto> getCurrentUser(
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(userService.getUserById(currentUser.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<UserDto> updateCurrentUser(
            @Valid @RequestBody UserDto userDto,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(userService.updateUser(currentUser.getId(), userDto));
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change current user's password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest passwordRequest,
            @Parameter(hidden = true) @CurrentUser UserPrincipal currentUser) {
        userService.changePassword(currentUser.getId(), passwordRequest.getCurrentPassword(), passwordRequest.getNewPassword());
        logger.info("Password changed for user id {}", currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/reset-request")
    @Operation(summary = "Request password reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/password/reset")
    @Operation(summary = "Reset password with token")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @Operation(summary = "Update user by ID")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        logger.info("User with id {} deleted", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Enable a user account (Admin only)")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        logger.info("User with id {} enabled", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Disable a user account (Admin only)")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        logger.info("User with id {} disabled", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Unlock a locked user account (Admin only)")
    public ResponseEntity<Void> unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        logger.info("User with id {} unlocked", id);
        return ResponseEntity.noContent().build();
    }
}
