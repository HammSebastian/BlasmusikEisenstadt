package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.RegisterRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest registerRequest);
}
