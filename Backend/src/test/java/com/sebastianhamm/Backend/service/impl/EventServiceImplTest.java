package com.sebastianhamm.Backend.service.impl;

import com.sebastianhamm.Backend.event.domain.entities.EventEntity;
import com.sebastianhamm.Backend.event.domain.services.EventServiceImpl;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;
import com.sebastianhamm.Backend.location.api.dtos.LocationDto;
import com.sebastianhamm.Backend.shared.domain.entities.Address;
import com.sebastianhamm.Backend.shared.api.dtos.AddressDto;
import com.sebastianhamm.Backend.event.domain.enums.EventType;
import com.sebastianhamm.Backend.event.domain.mappers.EventMapper;
import com.sebastianhamm.Backend.event.api.dtos.EventRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.event.api.dtos.EventResponse;
import com.sebastianhamm.Backend.event.domain.repositories.EventRepository;
import com.sebastianhamm.Backend.location.domain.repositories.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EventServiceImpl.
 * Tests business logic in isolation using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EventServiceImpl Tests")
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventEntity testEventEntity;
    private LocationEntity testLocationEntity;
    private LocationDto testLocationDto;
    private EventRequest testEventRequest;
    private EventResponse testEventResponse;

    @BeforeEach
    void setUp() {
        // Given - Set up test data
        Address testAddress = new Address();
        testAddress.setStreet("Test Street");
        testAddress.setStreetNumber("123");
        testAddress.setCity("Test City");
        testAddress.setPostalCode("12345");
        testAddress.setCountry("Test Country");

        testLocationEntity = new LocationEntity();
        testLocationEntity.setId(1L);
        testLocationEntity.setName("Test Venue");
        testLocationEntity.setAddress(testAddress);

        AddressDto testAddressDto = new AddressDto();
        testAddressDto.setStreet("Test Street");
        testAddressDto.setStreetNumber("123");
        testAddressDto.setPostalNumber("12345");
        testAddressDto.setCity("Test City");
        testAddressDto.setCountry("Test Country");

        testLocationDto = new LocationDto();
        testLocationDto.setId(1L);
        testLocationDto.setName("Test Venue");
        testLocationDto.setAddress(testAddressDto);

        testEventEntity = new EventEntity();
        testEventEntity.setId(1L);
        testEventEntity.setTitle("Test Concert");
        testEventEntity.setDescription("A wonderful test concert");
        testEventEntity.setDate(LocalDate.of(2025, 12, 31));
        testEventEntity.setEventImageUrl("https://example.com/event.jpg");
        testEventEntity.setEventType(EventType.CONCERT);
        testEventEntity.setLocation(testLocationEntity);

        testEventResponse = new EventResponse();
        testEventResponse.setId(1L);
        testEventResponse.setTitle("Test Concert");
        testEventResponse.setDescription("A wonderful test concert");

        testEventRequest = new EventRequest();
        testEventRequest.setTitle("Test Concert");
        testEventRequest.setDescription("A wonderful test concert");
        testEventRequest.setDate(LocalDate.of(2025, 12, 31));
        testEventRequest.setEventImageUrl("https://example.com/event.jpg");
        testEventRequest.setEventType(EventType.CONCERT);
        testEventRequest.setLocation(testLocationDto);
    }

    @Test
    @DisplayName("Should return event when found by ID")
    void getEventById_WhenEventExists_ShouldReturnSuccessResponse() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEventEntity));
        when(eventMapper.toResponse(testEventEntity)).thenReturn(testEventResponse);

        // When
        ApiResponse<EventResponse> result = eventService.getEventById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Event found");
        assertThat(result.getData()).isEqualTo(testEventResponse);

        verify(eventRepository, times(1)).findById(1L);
        verify(eventMapper, times(1)).toResponse(testEventEntity);
    }

    @Test
    @DisplayName("Should return 404 when event not found by ID")
    void getEventById_WhenEventNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<EventResponse> result = eventService.getEventById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Event not found with ID: 1");
        assertThat(result.getData()).isNull();

        verify(eventRepository, times(1)).findById(1L);
        verify(eventMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should return event when found by name")
    void findEventByName_WhenEventExists_ShouldReturnSuccessResponse() {
        // Given
        when(eventRepository.findEventEntityByTitle("Test Concert")).thenReturn(Optional.of(testEventEntity));
        when(eventMapper.toResponse(testEventEntity)).thenReturn(testEventResponse);

        // When
        ApiResponse<EventResponse> result = eventService.findEventByName("Test Concert");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Event found");
        assertThat(result.getData()).isEqualTo(testEventResponse);

        verify(eventRepository, times(1)).findEventEntityByTitle("Test Concert");
        verify(eventMapper, times(1)).toResponse(testEventEntity);
    }

    @Test
    @DisplayName("Should return 404 when event not found by name")
    void findEventByName_WhenEventNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(eventRepository.findEventEntityByTitle("Nonexistent Event")).thenReturn(Optional.empty());

        // When
        ApiResponse<EventResponse> result = eventService.findEventByName("Nonexistent Event");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Event not found with name: Nonexistent Event");
        assertThat(result.getData()).isNull();
    }

    @Test
    @DisplayName("Should return event when found by location")
    void findEventByLocation_WhenLocationAndEventExist_ShouldReturnSuccessResponse() {
        // Given
        when(locationRepository.findByName("Test Venue")).thenReturn(Optional.of(testLocationEntity));
        when(eventRepository.findEventEntityByLocation(testLocationEntity)).thenReturn(Optional.of(testEventEntity));
        when(eventMapper.toResponse(testEventEntity)).thenReturn(testEventResponse);

        // When
        ApiResponse<EventResponse> result = eventService.findEventByLocation("Test Venue");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Event found");
        assertThat(result.getData()).isEqualTo(testEventResponse);

        verify(locationRepository, times(1)).findByName("Test Venue");
        verify(eventRepository, times(1)).findEventEntityByLocation(testLocationEntity);
        verify(eventMapper, times(1)).toResponse(testEventEntity);
    }

    @Test
    @DisplayName("Should return 404 when location not found")
    void findEventByLocation_WhenLocationNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(locationRepository.findByName("Nonexistent Venue")).thenReturn(Optional.empty());

        // When
        ApiResponse<EventResponse> result = eventService.findEventByLocation("Nonexistent Venue");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Location not found with name: Nonexistent Venue");
        assertThat(result.getData()).isNull();

        verify(locationRepository, times(1)).findByName("Nonexistent Venue");
        verify(eventRepository, never()).findEventEntityByLocation(any());
    }

    @Test
    @DisplayName("Should save event successfully")
    void saveEvent_WhenLocationExists_ShouldReturnCreatedResponse() {
        // Given
        when(locationRepository.findByName("Test Venue")).thenReturn(Optional.of(testLocationEntity));
        when(eventMapper.toEntity(testEventRequest, testLocationEntity)).thenReturn(testEventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(testEventEntity);
        when(eventMapper.toResponse(testEventEntity)).thenReturn(testEventResponse);

        // When
        ApiResponse<EventResponse> result = eventService.saveEvent(testEventRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(201);
        assertThat(result.getMessage()).isEqualTo("Event created successfully");
        assertThat(result.getData()).isEqualTo(testEventResponse);

        verify(locationRepository, times(1)).findByName("Test Venue");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        verify(eventMapper, times(1)).toResponse(testEventEntity);
    }

    @Test
    @DisplayName("Should return 404 when saving event with nonexistent location")
    void saveEvent_WhenLocationNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(locationRepository.findByName("Test Venue")).thenReturn(Optional.empty());

        // When
        ApiResponse<EventResponse> result = eventService.saveEvent(testEventRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Location not found with name: Test Venue");
        assertThat(result.getData()).isNull();

        verify(locationRepository, times(1)).findByName("Test Venue");
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update event successfully")
    void updateEvent_WhenEventAndLocationExist_ShouldReturnUpdatedResponse() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEventEntity));
        when(locationRepository.findByName("Test Venue")).thenReturn(Optional.of(testLocationEntity));
        when(eventRepository.save(testEventEntity)).thenReturn(testEventEntity);
        when(eventMapper.toResponse(testEventEntity)).thenReturn(testEventResponse);

        // When
        ApiResponse<EventResponse> result = eventService.updateEvent(1L, testEventRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Event updated successfully");
        assertThat(result.getData()).isEqualTo(testEventResponse);

        verify(eventRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).findByName("Test Venue");
        verify(eventRepository, times(1)).save(testEventEntity);
        verify(eventMapper, times(1)).toResponse(testEventEntity);
    }

    @Test
    @DisplayName("Should return 404 when updating nonexistent event")
    void updateEvent_WhenEventNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<EventResponse> result = eventService.updateEvent(1L, testEventRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Event not found with ID: 1");
        assertThat(result.getData()).isNull();

        verify(eventRepository, times(1)).findById(1L);
        verify(locationRepository, never()).findByName(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete event successfully")
    void deleteEvent_WhenEventExists_ShouldReturnSuccessResponse() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEventEntity));

        // When
        ApiResponse<String> result = eventService.deleteEvent(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(204);
        assertThat(result.getMessage()).isEqualTo("Event deleted successfully");
        assertThat(result.getData()).isNull();

        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting nonexistent event")
    void deleteEvent_WhenEventNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<String> result = eventService.deleteEvent(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Event not found with ID: 1");
        assertThat(result.getData()).isNull();

        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should return all events successfully")
    void findAllEvents_WhenEventsExist_ShouldReturnAllEvents() {
        // Given
        List<EventEntity> eventEntities = List.of(testEventEntity);
        List<EventResponse> eventResponses = List.of(testEventResponse);
        
        when(eventRepository.findAll()).thenReturn(eventEntities);
        when(eventMapper.toResponse(testEventEntity)).thenReturn(testEventResponse);

        // When
        ApiResponse<List<EventResponse>> result = eventService.findAllEvents();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("All events found");
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData()).containsExactly(testEventResponse);

        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(1)).toResponse(testEventEntity);
    }

}