package at.sebastianhamm.backend.service;
import at.sebastianhamm.backend.entity.User;
import at.sebastianhamm.backend.payload.request.LoginRequest;
import at.sebastianhamm.backend.payload.request.RegisterRequest;
import at.sebastianhamm.backend.payload.response.ApiResponse;
import at.sebastianhamm.backend.payload.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    ApiResponse<?> signUp(RegisterRequest registerRequest);
    ApiResponse<?> login(LoginRequest loginRequest, HttpServletResponse response);
    ApiResponse<User> getCurrentLoggedInUser();
    ApiResponse<UserResponse> currentLoggedInUser();

    ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response);

    ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response);
}
