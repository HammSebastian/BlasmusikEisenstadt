package at.sebastianhamm.backend.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

/**
 * Annotation to access the currently authenticated user in the controller.
 * 
 * Usage in controller methods:
 * 
 * @GetMapping("/me")
 * public ResponseEntity<UserDto> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
 *     // currentUser will be automatically injected
 * }
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
