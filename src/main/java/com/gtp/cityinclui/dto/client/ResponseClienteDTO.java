package com.gtp.cityinclui.dto.client;

public record ResponseClienteDTO(
        Long id,
        String nomeCompleto,
        String telefone,
        String email) {
}
