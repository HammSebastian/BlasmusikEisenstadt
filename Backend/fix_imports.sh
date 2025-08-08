#!/bin/bash

# Fix import statements for all moved files

# Fix imports to use new package structure
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.ApiResponse;/import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.dto\.AddressDto;/import com.sebastianhamm.Backend.shared.api.dtos.AddressDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.embedded\.Address;/import com.sebastianhamm.Backend.shared.domain.entities.Address;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.exception\.GlobalExceptionHandler;/import com.sebastianhamm.Backend.shared.domain.exceptions.GlobalExceptionHandler;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.exception\.NotFoundException;/import com.sebastianhamm.Backend.shared.domain.exceptions.NotFoundException;/g' {} \;

# Fix About module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.AboutRequest;/import com.sebastianhamm.Backend.about.api.dtos.AboutRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.AboutResponse;/import com.sebastianhamm.Backend.about.api.dtos.AboutResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.AboutEntity;/import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.AboutRepository;/import com.sebastianhamm.Backend.about.domain.repositories.AboutRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.AboutService;/import com.sebastianhamm.Backend.about.domain.services.AboutService;/g' {} \;

# Fix Event module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.EventRequest;/import com.sebastianhamm.Backend.event.api.dtos.EventRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.EventResponse;/import com.sebastianhamm.Backend.event.api.dtos.EventResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.EventEntity;/import com.sebastianhamm.Backend.event.domain.entities.EventEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.EventRepository;/import com.sebastianhamm.Backend.event.domain.repositories.EventRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.EventService;/import com.sebastianhamm.Backend.event.domain.services.EventService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.mapper\.EventMapper;/import com.sebastianhamm.Backend.event.domain.mappers.EventMapper;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.enums\.EventType;/import com.sebastianhamm.Backend.event.domain.enums.EventType;/g' {} \;

# Fix Gallery module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.GalleryRequest;/import com.sebastianhamm.Backend.gallery.api.dtos.GalleryRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.GalleryResponse;/import com.sebastianhamm.Backend.gallery.api.dtos.GalleryResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.GalleryEntity;/import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.GalleryRepository;/import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.GalleryService;/import com.sebastianhamm.Backend.gallery.domain.services.GalleryService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.mapper\.GalleryMapper;/import com.sebastianhamm.Backend.gallery.domain.mappers.GalleryMapper;/g' {} \;

# Fix Image module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.ImageRequest;/import com.sebastianhamm.Backend.image.api.dtos.ImageRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.ImageResponse;/import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.ImageEntity;/import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.ImageRepository;/import com.sebastianhamm.Backend.image.domain.repositories.ImageRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.ImageService;/import com.sebastianhamm.Backend.image.domain.services.ImageService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.mapper\.ImageMapper;/import com.sebastianhamm.Backend.image.domain.mappers.ImageMapper;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.exception\.FileStorageException;/import com.sebastianhamm.Backend.image.domain.exceptions.FileStorageException;/g' {} \;

# Fix Location module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.LocationRequest;/import com.sebastianhamm.Backend.location.api.dtos.LocationRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.LocationResponse;/import com.sebastianhamm.Backend.location.api.dtos.LocationResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.LocationEntity;/import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.LocationRepository;/import com.sebastianhamm.Backend.location.domain.repositories.LocationRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.LocationService;/import com.sebastianhamm.Backend.location.domain.services.LocationService;/g' {} \;

# Fix Member module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.MemberRequest;/import com.sebastianhamm.Backend.member.api.dtos.MemberRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.MemberResponse;/import com.sebastianhamm.Backend.member.api.dtos.MemberResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.MemberEntity;/import com.sebastianhamm.Backend.member.domain.entities.MemberEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.MemberRepository;/import com.sebastianhamm.Backend.member.domain.repositories.MemberRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.MemberService;/import com.sebastianhamm.Backend.member.domain.services.MemberService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.enums\.InstrumentEnum;/import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.enums\.SectionEnum;/import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;/g' {} \;

# Fix Welcome module imports
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.request\.WelcomeRequest;/import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.payload\.response\.WelcomeResponse;/import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeResponse;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.entity\.WelcomeEntity;/import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.repository\.WelcomeRepository;/import com.sebastianhamm.Backend.welcome.domain.repositories.WelcomeRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.service\.WelcomeService;/import com.sebastianhamm.Backend.welcome.domain.services.WelcomeService;/g' {} \;

echo "Import statements fixed!"