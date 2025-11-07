package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClienteDTO;
import com.gtp.cityinclui.exception.RegraNegocioException;
import com.gtp.cityinclui.entity.Client; // Import da sua pasta 'entity'
import com.gtp.cityinclui.repository.ClientRepository;
import com.gtp.cityinclui.service.ClientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ClienteServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<ResponseClienteDTO> cadastrarCliente(CreateClienteDTO dto) {

        return clientRepository.existsByEmail(dto.email())
                .flatMap(emailJaExiste -> {
                    if (emailJaExiste) {
                        return Mono.error(new RegraNegocioException("O e-mail informado já está cadastrado."));
                    }

                    Client novoCliente = new Client();
                    novoCliente.setNomeCompleto(dto.nomeCompleto());
                    novoCliente.setEmail(dto.email());
                    novoCliente.setTelefone(dto.telefone());
                    novoCliente.setSenha(passwordEncoder.encode(dto.senha()));

                    return clientRepository.save(novoCliente);
                })
                .map(clienteSalvo -> new ResponseClienteDTO(
                        clienteSalvo.getId(),
                        clienteSalvo.getNomeCompleto(),
                        clienteSalvo.getTelefone(),
                        clienteSalvo.getEmail()
                ));
    }
}