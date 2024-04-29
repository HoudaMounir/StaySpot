package com.example.houda.auth;

import com.example.houda.Config.JwtService;
import com.example.houda.Exceptions.DuplicateResource;
import com.example.houda.Exceptions.InvalidInputException;
import com.example.houda.user.*;
import com.example.houda.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final appRoleRepo appRoleRepo;
    private static String UPLOADED_FOLDER = "/images/";
    public void addNewRole(appRole role){
        appRoleRepo.save(role);
    }

    public void addRoleToUser(String email,String role){
        Optional<User> currentUser = userRepository.findByEmail(email);
        appRole RoleName = appRoleRepo.findByRoleName(role);
        currentUser.get().getRoles().add(RoleName);
    }

    public AuthenticationResponse register(registerRequest request) {

        if(
                request.getPassword().isBlank() || request.getFirstname().isBlank()
                || request.getLastname().isBlank() || !emailValidation(request.getEmail())
        )
        {
            throw new InvalidInputException("Invalid Inputs");
        }
        else {
            if (userRepository.existsUserByEmail(request.getEmail())) {
                throw new DuplicateResource("User with email " + request.getEmail() + " already exits");
            }


            Collection<appRole> rolesOfUser = new ArrayList<>();
            appRole RoleName = appRoleRepo.findByRoleName("USER");
            Collection<appRole> list = new ArrayList<>();
            list.add(RoleName);

            var user = User.builder()
                    .firstname(request.getFirstname())
                    .Lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .enabled(true)
                    .locked(false)
                    .roles(list)
                    .build();



            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        }

    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public AuthenticationResponse authenticate(authenticationRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
            );
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AuthenticationException("Invalid email/password supplied") {
            });
            var jwtToken=jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        }
        catch (AuthenticationException e){
            throw new BadCredentialsException(e.getMessage());
        }

    }

    public static boolean emailValidation(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
}
