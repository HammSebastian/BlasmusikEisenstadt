package at.sebastianhamm.kapelle_eisenstadt.service;

import at.sebastianhamm.kapelle_eisenstadt.dto.AuthenticationRequest;
import at.sebastianhamm.kapelle_eisenstadt.dto.AuthenticationResponse;
import at.sebastianhamm.kapelle_eisenstadt.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
