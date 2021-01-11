package com.app.em.controller.security;

import com.app.em.persistence.entity.user.Role;
import com.app.em.persistence.entity.user.RoleEnum;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.user.RoleRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.jwt.JwtUtils;
import com.app.em.security.payload.request.LoginRequest;
import com.app.em.security.payload.request.SignUpRequest;
import com.app.em.security.payload.response.JwtResponse;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.security.service.UserDetailsImpl;
import com.app.em.security.service.UserDetailsServiceImpl;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()) );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateAccessToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String customSessionId = UUID.randomUUID().toString();

        return ResponseEntity.ok(new JwtResponse(
               accessToken,
               customSessionId,
               userDetails.getId(),
               userDetails.getEmail(),
               roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if ( userRepository.existsByEmail(signUpRequest.getEmail()) )
            return ResponseEntity.badRequest().body(new MessageResponse("Error: This email is already taken."));

        User user = new User();
        user.setFullName("empty");
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> rolesStrings = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if ( rolesStrings == null )
        {
            Role userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER)
                    .orElseThrow( () -> new RuntimeException("Error: Role is not found.") );

            roles.add(userRole);
        }
        else
        {
            rolesStrings.forEach( role -> {
                    switch ( role )
                    {
                        case "ROLE_ADMIN":
                            Role adminRole = roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)
                                    .orElseThrow( () -> new RuntimeException("Error: Role is not found.") );
                            roles.add(adminRole);
                            break;
                        default:
                            Role userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER)
                                    .orElseThrow( () -> new RuntimeException("Error: Role is not found.") );
                            roles.add(userRole);
                    }
                }
            );
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully."));
    }
}
