package com.papp.skyline.dto;

public class UserDTO {
    private String name;
    private String cpf;

    public UserDTO(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }
}
