package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.models.announcement.About;
import at.sebastianhamm.backend.payload.response.AboutResponse;
import at.sebastianhamm.backend.services.AboutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AboutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AboutService aboutService;

    @Autowired
    private ObjectMapper objectMapper;

    private AboutResponse sampleResponse;
    private About sampleAbout;

    @BeforeEach
    void setup() {
        sampleResponse = AboutResponse.builder()
                .id(1L)
                .imageUrl("http://image.url")
                .story("Test story")
                .missions(null)
                .build();

        sampleAbout = new About();
        sampleAbout.setId(1L);
        sampleAbout.setImageUrl("http://image.url");
        sampleAbout.setStory("Test story");
    }

    @Test
    void getAbout_shouldReturnOkAndResponse() throws Exception {
        when(aboutService.getAbout()).thenReturn(sampleResponse);

        mockMvc.perform(get("/public/about"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
                .andExpect(jsonPath("$.imageUrl").value(sampleResponse.getImageUrl()))
                .andExpect(jsonPath("$.story").value(sampleResponse.getStory()));

        verify(aboutService, times(1)).getAbout();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateAbout_withAdminRole_shouldReturnUpdatedAbout() throws Exception {
        when(aboutService.updateAbout(ArgumentMatchers.any(About.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/public/about")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAbout)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
                .andExpect(jsonPath("$.imageUrl").value(sampleResponse.getImageUrl()))
                .andExpect(jsonPath("$.story").value(sampleResponse.getStory()));

        verify(aboutService, times(1)).updateAbout(ArgumentMatchers.any(About.class));
    }

    @Test
    @WithMockUser(roles = {"REPORTER"})
    void updateAbout_withReporterRole_shouldReturnUpdatedAbout() throws Exception {
        when(aboutService.updateAbout(ArgumentMatchers.any(About.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/public/about")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAbout)))
                .andExpect(status().isOk());

        verify(aboutService, times(1)).updateAbout(ArgumentMatchers.any(About.class));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void updateAbout_withoutProperRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/public/about")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAbout)))
                .andExpect(status().isForbidden());

        verify(aboutService, never()).updateAbout(any());
    }

    @Test
    void updateAbout_unauthenticated_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/public/about")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAbout)))
                .andExpect(status().isUnauthorized());

        verify(aboutService, never()).updateAbout(any());
    }
}
