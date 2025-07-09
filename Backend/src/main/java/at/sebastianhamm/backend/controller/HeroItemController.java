package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.payload.request.HeroItemRequest;
import at.sebastianhamm.backend.payload.response.HeroItemResponse;
import at.sebastianhamm.backend.services.HeroItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/public/hero-items")
@RequiredArgsConstructor
public class HeroItemController {

    private final HeroItemService heroItemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HeroItemResponse getAllHeroItems() {
        return heroItemService.getHeroItemById(1L);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public HeroItemResponse updateHeroItem(@RequestBody HeroItemRequest heroItemRequest) {
        return heroItemService.updateHeroItem(1L, heroItemRequest);
    }
}
