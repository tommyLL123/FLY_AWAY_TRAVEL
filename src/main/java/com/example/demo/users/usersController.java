package com.example.demo.users;

import com.example.demo.common.newIdDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class usersController {

    private final usersService service;

    @PostMapping("/register")
    public ResponseEntity<newIdDTO> register(@Valid @RequestBody usersDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }
}
