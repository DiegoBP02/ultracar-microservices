package com.example.Ultracar.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String address;
}
