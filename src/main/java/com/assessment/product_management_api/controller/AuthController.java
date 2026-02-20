package com.assessment.product_management_api.controller;

import com.assessment.product_management_api.dto.AuthRequest;
import com.assessment.product_management_api.dto.AuthResponse;
import com.assessment.product_management_api.dto.RefreshTokenRequest;
import com.assessment.product_management_api.entity.RefreshToken;
import com.assessment.product_management_api.security.JwtService;
import com.assessment.product_management_api.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "JWT authentication APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and issue JWT tokens")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(principal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUsername());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken(), "Bearer"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(request.refreshToken());
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(refreshToken.getUser().getUsername())
                .password(refreshToken.getUser().getPassword())
                .authorities(refreshToken.getUser().getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();

        String accessToken = jwtService.generateAccessToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken(), "Bearer"));
    }
}

