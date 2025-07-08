package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.payload.request.HeroItemRequest;
import at.sebastianhamm.backend.payload.response.HeroItemResponse;

public interface HeroItemService {

    HeroItemResponse getHeroItemById(Long id);
    HeroItemResponse updateHeroItem(Long id, HeroItemRequest heroItemRequest);
}
