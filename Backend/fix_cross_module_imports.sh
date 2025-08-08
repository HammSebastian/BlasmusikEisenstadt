#!/bin/bash

# Fix cross-module dependencies and missing imports

# Fix validation imports in member module
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.validation\./import com.sebastianhamm.Backend.member.domain.validation./g' {} \;

# Fix security imports in shared config
find src/main/java -name "*.java" -exec sed -i 's/import com\.sebastianhamm\.Backend\.security\./import com.sebastianhamm.Backend.shared.security./g' {} \;

# Fix cross-module entity imports
# LocationEntity needs EventEntity
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i '/^import.*EventEntity;/d' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i '/^package/a import com.sebastianhamm.Backend.event.domain.entities.EventEntity;' {} \;

# EventEntity needs LocationEntity  
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i '/^import.*LocationEntity;/d' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i '/^package/a import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;' {} \;

# ImageEntity needs GalleryEntity
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i '/^import.*GalleryEntity;/d' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i '/^package/a import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;' {} \;

# GalleryEntity needs ImageEntity
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i '/^import.*ImageEntity;/d' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i '/^package/a import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;' {} \;

# GalleryResponse needs ImageResponse
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i '/^import.*ImageResponse;/d' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i '/^package/a import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;' {} \;

# Fix DataInitializer imports - it needs all repositories and entities
cat > temp_datainit_imports.txt << 'EOF'
import com.sebastianhamm.Backend.member.domain.repositories.MemberRepository;
import com.sebastianhamm.Backend.image.domain.repositories.ImageRepository;
import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;
import com.sebastianhamm.Backend.welcome.domain.repositories.WelcomeRepository;
import com.sebastianhamm.Backend.about.domain.repositories.AboutRepository;
import com.sebastianhamm.Backend.event.domain.repositories.EventRepository;
import com.sebastianhamm.Backend.location.domain.repositories.LocationRepository;
import com.sebastianhamm.Backend.member.domain.entities.MemberEntity;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;
import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;
import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;
import com.sebastianhamm.Backend.event.domain.entities.EventEntity;
import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;
import com.sebastianhamm.Backend.member.domain.enums.InstrumentEnum;
import com.sebastianhamm.Backend.member.domain.enums.SectionEnum;
import com.sebastianhamm.Backend.event.domain.enums.EventType;
EOF

# Remove old imports and add new ones to DataInitializer
sed -i '/^import com\.sebastianhamm\.Backend\.repository\./d' src/main/java/com/sebastianhamm/Backend/shared/init/DataInitializer.java
sed -i '/^import com\.sebastianhamm\.Backend\.entity\./d' src/main/java/com/sebastianhamm/Backend/shared/init/DataInitializer.java
sed -i '/^import com\.sebastianhamm\.Backend\.enums\./d' src/main/java/com/sebastianhamm/Backend/shared/init/DataInitializer.java
sed -i '/^package/r temp_datainit_imports.txt' src/main/java/com/sebastianhamm/Backend/shared/init/DataInitializer.java

rm temp_datainit_imports.txt

echo "Cross-module imports fixed!"