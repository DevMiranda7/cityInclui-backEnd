package com.gtp.cityinclui.dto.client;

public record ResponseClientDTO (
    Long id, // Ou String, dependendo do tipo do seu ID no banco
    String nomeCompleto,
    String telefone,
    String email
){}
