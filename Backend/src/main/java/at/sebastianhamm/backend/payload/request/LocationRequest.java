package at.sebastianhamm.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocationRequest {

    @NotBlank(message = "Zip Code cannot be empty")
    @Size(max = 10, message = "Zip Code cannot exceed 10 characters")
    private String zipCode;

    @NotBlank(message = "City cannot be empty")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "Country cannot be empty")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;

    @NotBlank(message = "Street cannot be empty")
    @Size(max = 50, message = "Street cannot exceed 50 characters")
    private String street;

    @NotBlank(message = "House Number cannot be empty")
    @Size(max = 10, message = "House Number cannot exceed 10 characters")
    private String houseNumber;

    @NotNull(message = "User ID cannot be empty")
    private Long userId;
}
