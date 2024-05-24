package com.hana.bankai.domain.user.controller;

import com.hana.bankai.domain.user.dto.RefreshToken;
import com.hana.bankai.domain.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final TokenService tokenService;

    @PostMapping
    public RefreshToken createUser(@RequestBody RefreshToken user) {
        return tokenService.save(user);
    }

    @GetMapping("/{id}")
    public Optional<RefreshToken> getUser(@PathVariable String id) {
        return tokenService.findById(id);
    }
}
