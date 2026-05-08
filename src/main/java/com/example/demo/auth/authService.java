package com.example.demo.auth;

import com.example.demo.common.passwordService;
import com.example.demo.security.jwtService;
import com.example.demo.users.users;
import com.example.demo.users.usersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class authService {

    private final usersRepository usersRepository;
    private final passwordService passwordService;
    private final jwtService jwtService;

    public tokenDTO login(loginDTO dto) {
        users user = usersRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email desconocido"));

        if (!passwordService.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contrasena incorrecta");
        }

        return new tokenDTO(jwtService.generateToken(user));
    }
}
