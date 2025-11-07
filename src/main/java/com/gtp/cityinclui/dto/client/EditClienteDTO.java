package com.gtp.cityinclui.dto.client;
import jakarta.validation.constraints.Size;
public record EditClienteDTO(
        @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
        String nomeCompleto,

        String telefone
) {}
