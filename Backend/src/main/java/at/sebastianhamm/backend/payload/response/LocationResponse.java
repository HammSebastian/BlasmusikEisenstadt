package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.entity.Location;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationResponse {
    private Long id;
    private String zipCode;
    private String city;
    private String country;
    private String street;
    private String houseNumber;
    private Long userId; // falls Location einen User besitzt

    public static LocationResponse fromLocation(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .zipCode(location.getZipCode())
                .city(location.getCity())
                .country(location.getCountry())
                .street(location.getStreet())
                .houseNumber(location.getHouseNumber())
                .userId(location.getUser() != null ? location.getUser().getId() : null)
                .build();
    }
}


