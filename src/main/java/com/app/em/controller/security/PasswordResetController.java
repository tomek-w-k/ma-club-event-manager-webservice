package com.app.em.controller.security;

import com.app.em.persistence.entity.security.PasswordResetToken;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.security.PasswordResetTokenRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.request.NewPasswordRequest;
import com.app.em.security.payload.request.PasswordResetRequest;
import com.app.em.security.service.PasswordResetMailSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/reset_password")
public class PasswordResetController
{
    private static final int TOKEN_EXPIRED_HTTP_STATUS = 498;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetMailSenderService mailSender;
    private final PasswordEncoder passwordEncoder;


    public PasswordResetController( UserRepository userRepository,
                                    PasswordResetTokenRepository passwordResetTokenRepository,
                                    PasswordResetMailSenderService passwordResetMailSenderService,
                                    PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailSender = passwordResetMailSenderService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/generate_token")
    public ResponseEntity generateAndSendResetPasswordToken(@RequestBody PasswordResetRequest passwordResetRequest, HttpServletRequest request)
    {
        return userRepository.findByEmail(passwordResetRequest.getEmail())
                .map(user -> {
                    LocalDateTime expiryLocalDate = LocalDateTime.of(LocalDate.now(), LocalTime.now()).plusHours(1);
                    Date expiryDate = Timestamp.valueOf(expiryLocalDate);

                    PasswordResetToken newToken = new PasswordResetToken();
                    newToken.setToken(UUID.randomUUID().toString());
                    newToken.setUser(user);
                    newToken.setExpiryDate(expiryDate);

                    passwordResetTokenRepository.findByUser(user)
                        .ifPresentOrElse(oldToken -> {
                            passwordResetTokenRepository.delete(oldToken);
                            passwordResetTokenRepository.save(newToken);
                        }, () -> passwordResetTokenRepository.save(newToken) );

                    Message resetPasswordMail = mailSender.prepareMessage(user.getEmail(), newToken.getToken(), request);
                    mailSender.sendMail(resetPasswordMail);

                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate_token/{token}")
    public ResponseEntity validateToken(@PathVariable String token)
    {
        return passwordResetTokenRepository.findByToken(token)
                .map(passwordResetToken -> {
                    LocalDateTime currentLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
                    Date currentTimestamp = Timestamp.valueOf(currentLocalDateTime);

                    if ( currentTimestamp.after(passwordResetToken.getExpiryDate()) )
                        return ResponseEntity.status(TOKEN_EXPIRED_HTTP_STATUS).build();
                    else return ResponseEntity.ok().build();
                }).orElseGet( () -> ResponseEntity.status(HttpStatus.NOT_FOUND).build() );
    }

    @PutMapping("/reset_password")
    public ResponseEntity resetPassword(@RequestBody NewPasswordRequest newPasswordRequest)
    {
        return passwordResetTokenRepository.findByToken(newPasswordRequest.getToken())
                .map(token -> {
                    User user = token.getUser();
                    user.setPassword(passwordEncoder.encode(newPasswordRequest.getPassword()));
                    userRepository.save(user);

                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
