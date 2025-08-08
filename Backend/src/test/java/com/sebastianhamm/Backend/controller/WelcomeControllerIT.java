package com.sebastianhamm.Backend.controller;

import com.sebastianhamm.Backend.BaseIT;
import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;
import com.sebastianhamm.Backend.welcome.domain.repositories.WelcomeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for WelcomeController.
 * Tests the REST API endpoints with real Spring context and database.
 */
@DisplayName("WelcomeController Integration Tests")
class WelcomeControllerIT extends BaseIT {

    @Autowired
    private WelcomeRepository welcomeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private WelcomeEntity testWelcomeEntity;

    @BeforeEach
    void setUp() {
        // Clean up and set up test data
        welcomeRepository.deleteAll();
        
        testWelcomeEntity = new WelcomeEntity();
        testWelcomeEntity.setId(1L);
        testWelcomeEntity.setTitle("Welcome to Our Band");
        testWelcomeEntity.setSubTitle("Experience the magic of music");
        testWelcomeEntity.setButtonText("Learn More");
        testWelcomeEntity.setBackgroundImageUrl("https://example.com/background.jpg");
        testWelcomeEntity.setUpdatedAt(LocalDateTime.now());
        
        welcomeRepository.save(testWelcomeEntity);
    }

    @Test
    @DisplayName("GET /welcome should return welcome data successfully")
    void getWelcome_ShouldReturnWelcomeData() throws Exception {
        mockMvc.perform(get("/welcome")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Welcome data retrieved successfully"))
                .andExpect(jsonPath("$.data.title").value("Welcome to Our Band"))
                .andExpect(jsonPath("$.data.subTitle").value("Experience the magic of music"))
                .andExpect(jsonPath("$.data.buttonText").value("Learn More"))
                .andExpect(jsonPath("$.data.backgroundImageUrl").value("https://example.com/background.jpg"))
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @Test
    @DisplayName("GET /welcome should return 500 when no welcome data exists")
    void getWelcome_WhenNoDataExists_ShouldReturnInternalServerError() throws Exception {
        // Given - remove test data
        welcomeRepository.deleteAll();

        mockMvc.perform(get("/welcome")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Internal Server Error"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("PUT /welcome without authentication should return 401")
    void updateWelcome_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        WelcomeRequest request = new WelcomeRequest();
        request.setTitle("Updated Title");
        request.setSubTitle("Updated Subtitle");
        request.setButtonText("Updated Button");
        request.setBackgroundImageUrl("https://example.com/new-background.jpg");

        mockMvc.perform(put("/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"write:admin"})
    @DisplayName("PUT /welcome with admin authority should update welcome data")
    void updateWelcome_WithAdminAuthority_ShouldUpdateWelcomeData() throws Exception {
        WelcomeRequest request = new WelcomeRequest();
        request.setTitle("Updated Title");
        request.setSubTitle("Updated Subtitle");
        request.setButtonText("Updated Button");
        request.setBackgroundImageUrl("https://example.com/new-background.jpg");

        mockMvc.perform(put("/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Welcome data updated successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @Test
    @WithMockUser(authorities = {"write:reporter"})
    @DisplayName("PUT /welcome with reporter authority should update welcome data")
    void updateWelcome_WithReporterAuthority_ShouldUpdateWelcomeData() throws Exception {
        WelcomeRequest request = new WelcomeRequest();
        request.setTitle("Updated Title");
        request.setSubTitle("Updated Subtitle");
        request.setButtonText("Updated Button");
        request.setBackgroundImageUrl("https://example.com/new-background.jpg");

        mockMvc.perform(put("/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Welcome data updated successfully"));
    }

    @Test
    @WithMockUser(authorities = {"read:user"})
    @DisplayName("PUT /welcome with insufficient authority should return 403")
    void updateWelcome_WithInsufficientAuthority_ShouldReturnForbidden() throws Exception {
        WelcomeRequest request = new WelcomeRequest();
        request.setTitle("Updated Title");
        request.setSubTitle("Updated Subtitle");
        request.setButtonText("Updated Button");
        request.setBackgroundImageUrl("https://example.com/new-background.jpg");

        mockMvc.perform(put("/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"write:admin"})
    @DisplayName("PUT /welcome should return 400 when welcome data not found")
    void updateWelcome_WhenDataNotFound_ShouldReturnBadRequest() throws Exception {
        // Given - remove test data
        welcomeRepository.deleteAll();

        WelcomeRequest request = new WelcomeRequest();
        request.setTitle("Updated Title");
        request.setSubTitle("Updated Subtitle");
        request.setButtonText("Updated Button");
        request.setBackgroundImageUrl("https://example.com/new-background.jpg");

        mockMvc.perform(put("/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Welcome data not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}