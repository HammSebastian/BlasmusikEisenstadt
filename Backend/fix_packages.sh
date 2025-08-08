#!/bin/bash

# Fix package declarations for all moved files

# Event module
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.controller;/package com.sebastianhamm.Backend.event.api.controllers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service;/package com.sebastianhamm.Backend.event.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service\.impl;/package com.sebastianhamm.Backend.event.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity;/package com.sebastianhamm.Backend.event.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.repository;/package com.sebastianhamm.Backend.event.domain.repositories;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.request;/package com.sebastianhamm.Backend.event.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.response;/package com.sebastianhamm.Backend.event.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.mapper;/package com.sebastianhamm.Backend.event.domain.mappers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/event -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.enums;/package com.sebastianhamm.Backend.event.domain.enums;/g' {} \;

# Gallery module
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.controller;/package com.sebastianhamm.Backend.gallery.api.controllers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service;/package com.sebastianhamm.Backend.gallery.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service\.impl;/package com.sebastianhamm.Backend.gallery.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity;/package com.sebastianhamm.Backend.gallery.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.repository;/package com.sebastianhamm.Backend.gallery.domain.repositories;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.request;/package com.sebastianhamm.Backend.gallery.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.response;/package com.sebastianhamm.Backend.gallery.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/gallery -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.mapper;/package com.sebastianhamm.Backend.gallery.domain.mappers;/g' {} \;

# Image module
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.controller;/package com.sebastianhamm.Backend.image.api.controllers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service;/package com.sebastianhamm.Backend.image.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service\.impl;/package com.sebastianhamm.Backend.image.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity;/package com.sebastianhamm.Backend.image.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.repository;/package com.sebastianhamm.Backend.image.domain.repositories;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.request;/package com.sebastianhamm.Backend.image.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.response;/package com.sebastianhamm.Backend.image.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.mapper;/package com.sebastianhamm.Backend.image.domain.mappers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/image -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.exception;/package com.sebastianhamm.Backend.image.domain.exceptions;/g' {} \;

# Location module
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.controller;/package com.sebastianhamm.Backend.location.api.controllers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service;/package com.sebastianhamm.Backend.location.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service\.impl;/package com.sebastianhamm.Backend.location.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity;/package com.sebastianhamm.Backend.location.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.repository;/package com.sebastianhamm.Backend.location.domain.repositories;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.request;/package com.sebastianhamm.Backend.location.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/location -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.response;/package com.sebastianhamm.Backend.location.api.dtos;/g' {} \;

# Member module
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.controller;/package com.sebastianhamm.Backend.member.api.controllers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service;/package com.sebastianhamm.Backend.member.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service\.impl;/package com.sebastianhamm.Backend.member.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity;/package com.sebastianhamm.Backend.member.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.repository;/package com.sebastianhamm.Backend.member.domain.repositories;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.request;/package com.sebastianhamm.Backend.member.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.response;/package com.sebastianhamm.Backend.member.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.enums;/package com.sebastianhamm.Backend.member.domain.enums;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/member -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.validation;/package com.sebastianhamm.Backend.member.domain.validation;/g' {} \;

# Welcome module
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.controller;/package com.sebastianhamm.Backend.welcome.api.controllers;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service;/package com.sebastianhamm.Backend.welcome.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.service\.impl;/package com.sebastianhamm.Backend.welcome.domain.services;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity;/package com.sebastianhamm.Backend.welcome.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.repository;/package com.sebastianhamm.Backend.welcome.domain.repositories;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.request;/package com.sebastianhamm.Backend.welcome.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/welcome -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.response;/package com.sebastianhamm.Backend.welcome.api.dtos;/g' {} \;

# Shared modules
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.payload\.dto;/package com.sebastianhamm.Backend.shared.api.dtos;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.entity\.embedded;/package com.sebastianhamm.Backend.shared.domain.entities;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.exception;/package com.sebastianhamm.Backend.shared.domain.exceptions;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.audit;/package com.sebastianhamm.Backend.shared.audit;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.configuration;/package com.sebastianhamm.Backend.shared.config;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.security;/package com.sebastianhamm.Backend.shared.security;/g' {} \;
find src/main/java/com/sebastianhamm/Backend/shared -name "*.java" -exec sed -i 's/package com\.sebastianhamm\.Backend\.init;/package com.sebastianhamm.Backend.shared.init;/g' {} \;

echo "Package declarations fixed!"