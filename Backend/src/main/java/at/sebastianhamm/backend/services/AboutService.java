package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.payload.response.AboutResponse;

public interface AboutService {

    AboutResponse getAbout();

    AboutResponse getOrCreateAbout();

    AboutResponse updateAbout(About about);

    AboutResponse mapToResponse(About about);
}
