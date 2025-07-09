package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.models.announcement.HeroItem;
import at.sebastianhamm.backend.payload.request.HeroItemRequest;
import at.sebastianhamm.backend.payload.response.HeroItemResponse;
import at.sebastianhamm.backend.repository.HeroItemRepository;
import at.sebastianhamm.backend.services.HeroItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroItemServiceImpl implements HeroItemService {

    private final HeroItemRepository heroItemRepository;

    @Override
    public HeroItemResponse getHeroItemById(Long id) {
        HeroItem heroItem = heroItemRepository.findHeroItemById(id);
        if (heroItem == null) {
            throw new RuntimeException("HeroItem not found");
        }
        return mapToHeroResponse(heroItem);
    }


    @Override
    public HeroItemResponse updateHeroItem(Long id, HeroItemRequest heroItemRequest) {
        if (!heroItemRepository.existsById(id)) {
            throw new RuntimeException("HeroItem not found");
        }
        HeroItem heroItem = heroItemRepository.findHeroItemById(id);
        heroItem.setTitle(heroItemRequest.getTitle());
        heroItem.setDescription(heroItemRequest.getDescription());
        heroItem.setImageUrl(heroItemRequest.getImageUrl());
        HeroItem updatedHeroItem = heroItemRepository.save(heroItem);
        
        return mapToHeroResponse(updatedHeroItem);
    }

    private HeroItemResponse mapToHeroResponse(HeroItem heroItem) {
        return HeroItemResponse.builder()
                .title(heroItem.getTitle())
                .description(heroItem.getDescription())
                .imageUrl(heroItem.getImageUrl())
                .build();
    }
}
