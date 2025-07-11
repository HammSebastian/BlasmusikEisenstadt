package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.payload.request.LoginRequest;
import at.sebastianhamm.backend.payload.request.RegisterRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.UserResponse;
import at.sebastianhamm.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<?>> signUp(@Valid @RequestBody RegisterRequest registerRequest){
        logger.info("Registering new user");
        return ResponseEntity.ok(userService.signUp(registerRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){
        logger.info("Logging in user");
        return ResponseEntity.ok(userService.login(loginRequest, response));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Logging out user");
        return ResponseEntity.ok(userService.logout(request, response));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Refreshing token");
        return ResponseEntity.ok(userService.refreshToken(request, response));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentLoggedInUser(){
        logger.info("Fetching current logged in user");

        if (userService.getCurrentLoggedInUser().getData() == null) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(404, "User not found", null));
        }
        UserResponse userResponse = userService.currentLoggedInUser().getData();

        if (userResponse == null) {
            return ResponseEntity.status(404)
                    .body(new ApiResponse<>(404, "User not found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "User fetched successfully", userResponse));
    }




}
