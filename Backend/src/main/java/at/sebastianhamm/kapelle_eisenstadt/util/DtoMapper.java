package at.sebastianhamm.kapelle_eisenstadt.util;

import at.sebastianhamm.kapelle_eisenstadt.dto.*;
import at.sebastianhamm.kapelle_eisenstadt.entity.Gig;
import at.sebastianhamm.kapelle_eisenstadt.entity.Image;
import at.sebastianhamm.kapelle_eisenstadt.entity.User;

public class DtoMapper {

    // Gig Mappers
    public static Gig toGig(GigRequest gigRequest) {
        Gig gig = new Gig();
        gig.setGigTitel(gigRequest.getGigTitel());
        gig.setGigDescription(gigRequest.getGigDescription());
        gig.setGigLocation(gigRequest.getGigLocation());
        gig.setGigDate(gigRequest.getGigDate());
        return gig;
    }

    public static GigResponse toGigResponse(Gig gig) {
        GigResponse response = new GigResponse();
        response.setId(gig.getId());
        response.setGigTitel(gig.getGigTitel());
        response.setGigDescription(gig.getGigDescription());
        response.setGigLocation(gig.getGigLocation());
        response.setGigDate(gig.getGigDate());
        return response;
    }

    // Image Mappers
    public static Image toImage(ImageRequest imageRequest) {
        Image image = new Image();
        image.setImagePath(imageRequest.getImagePath());
        image.setImageCategory(imageRequest.getImageCategory());
        image.setImageAuthor(imageRequest.getImageAuthor());
        return image;
    }

    public static ImageResponse toImageResponse(Image image) {
        ImageResponse response = new ImageResponse();
        response.setId(image.getId());
        response.setImagePath(image.getImagePath());
        response.setImageCategory(image.getImageCategory());
        response.setImageAuthor(image.getImageAuthor());
        return response;
    }

    // User Mappers
    public static User toUser(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .role(userRequest.getRole())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
