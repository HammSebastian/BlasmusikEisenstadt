package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getProfile(String email);
}
