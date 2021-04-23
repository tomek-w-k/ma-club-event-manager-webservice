package com.app.em.controller.security;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.*;
import com.app.em.security.jwt.JwtUtils;
import com.app.em.security.payload.request.LoginRequest;
import com.app.em.security.payload.request.SignUpRequest;
import com.app.em.security.payload.response.JwtResponse;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController
{
    private static final long NO_USERS_REGISTERED = 0;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    BranchChiefRepository branchChiefRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public ResponseEntity registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if ( userRepository.existsByEmail(signUpRequest.getEmail()) )
            return ResponseEntity.badRequest().body(new MessageResponse("This email is already taken."));

        User user = new User();
        user.setFullName(signUpRequest.getFullName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        if ( signUpRequest.getClub() != null )
            user.setClub( clubRepository.findById(signUpRequest.getClub().getId()).orElseGet(() -> clubRepository.save(signUpRequest.getClub())) );

        user.setCountry(signUpRequest.getCountry());
        user.setRank(signUpRequest.getRank());

        if ( signUpRequest.getBranchChief() != null )
        user.setBranchChief( branchChiefRepository.findById(signUpRequest.getBranchChief().getId())
                .orElseGet(() -> branchChiefRepository.save(signUpRequest.getBranchChief())) );

        Set<Role> rolesForUser = new HashSet<>();
        rolesForUser.add( getRoleUser() );

        if ( signUpRequest.getAsTrainer() )
            rolesForUser.add( getRoleTrainer() );

        if ( userRepository.count() == NO_USERS_REGISTERED )
        {
            if ( rolesForUser.stream().noneMatch(role -> role.getRoleName().equals(RoleEnum.ROLE_TRAINER)) )
                rolesForUser.add( getRoleTrainer() );
            rolesForUser.add( getRoleAdmin() );
        }

        user.setRoles(rolesForUser);
        User registeredUser = userRepository.save(user);

        return ResponseEntity.ok(registeredUser);
    }

    // - - - PRIVATE METHODS - - -

    private Role getRoleUser()
    {
        return roleRepository.findByRoleName(RoleEnum.ROLE_USER).orElseGet(() -> new Role(RoleEnum.ROLE_USER));
    }

    private Role getRoleTrainer()
    {
        return roleRepository.findByRoleName(RoleEnum.ROLE_TRAINER).orElseGet(() -> new Role(RoleEnum.ROLE_TRAINER));
    }

    private Role getRoleAdmin()
    {
        return roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN).orElseGet(() -> new Role(RoleEnum.ROLE_ADMIN));
    }
}
