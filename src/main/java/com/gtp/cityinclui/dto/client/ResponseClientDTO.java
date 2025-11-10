package com.gtp.cityinclui.dto.client;
public record ResponseClientDTO (
    Long id,
    String nomeCompleto,
    String telefone,
    String email
){}