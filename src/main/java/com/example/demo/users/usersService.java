package com.example.demo.users;

import org.springframework.stereotype.Service;

import com.example.demo.common.newIdDTO;
import com.example.demo.common.passwordService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class usersService {

    private final usersRepository repo;
    private final passwordService passwordService;

    public newIdDTO register(usersDTO dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya esta registrado");
        }

        users user = new users();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordService.hash(dto.getPassword()));

        users savedUser = repo.save(user);
        return new newIdDTO(savedUser.getId().toString());
    }
}
