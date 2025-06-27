package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.AuthToken;
import at.sebastianhamm.kapelle_eisenstadt.dto.LoginUser;
import at.sebastianhamm.kapelle_eisenstadt.dto.UserDto;
import at.sebastianhamm.kapelle_eisenstadt.config.JwtTokenUtil;
import at.sebastianhamm.kapelle_eisenstadt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @PostMapping(value = "/generate-token")
    public AuthToken login(@RequestBody LoginUser loginUser) throws AuthenticationException {
        logger.debug("Login User: " + loginUser.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        final UserDto user = userService.findByName(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return new AuthToken(token, user.getUsername());
    }
}
