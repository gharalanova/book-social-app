package com.projectgaby.book_social_app.auth;

import com.projectgaby.book_social_app.role.RoleRepository;
import com.projectgaby.book_social_app.user.Token;
import com.projectgaby.book_social_app.user.TokenRepository;
import com.projectgaby.book_social_app.user.User;
import com.projectgaby.book_social_app.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void register(RegistrationRequest request) {
        //1.need to assign role by default
        //2.create user object and save it
        //3.need to send a validation email (implement email send service) + crate the template email

        var userRole = roleRepository.findByName("USER")
                //to do -> better exception handling
                .orElseThrow(() -> new IllegalStateException("Role USER was not initialized."));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false) //by default is locked and disabled
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);
        //send email
    }

    private String generateAndSaveActivationToken(User user) {
        //1. generate token
        String generateToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now())
                .expiresAd(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
