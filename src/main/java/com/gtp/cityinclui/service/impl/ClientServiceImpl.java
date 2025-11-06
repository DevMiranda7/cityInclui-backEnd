// ONDE: com/gtp/cityinclui/service/impl/ClienteServiceImpl.java
package com.gtp.cityinclui.service.impl;
import com.gtp.cityinclui.dto.client.CreateClientDTO;
import com.gtp.cityinclui.model.Cliente; // [ATENÇÃO 1]
import lombok.RequiredArgsConstructor;

import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClientDTO;
import com.gtp.cityinclui.exception.RegraNegocioException;
import com.gtp.cityinclui.model.Cliente;
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
    public Mono<ResponseClientDTO> cadastrarCliente(CreateClienteDTO dto) {
        return clientRepository.existsByEmail(dto.email())
                .flatMap(emailJaExiste -> {
                    if (emailJaExiste) {

                        return Mono.error(new RegraNegocioException("O e-mail informado já está cadastrado."));
                    }

                    // 3. LÓGICA: Cria a nova entidade
                    Cliente novoCliente = new Cliente(); // <--- Esta classe vai existir no Passo 2

                    // Os métodos abaixo vão existir no Passo 2
                    novoCliente.setNomeCompleto(dto.nomeCompleto());
                    novoCliente.setEmail(dto.email());
                    novoCliente.setTelefone(dto.telefone());
                    novoCliente.setSenha(passwordEncoder.encode(dto.senha()));

                    // 5. LÓGICA: Salva no banco
                    return clienteRepository.save(novoCliente);
                })
                // 6. LÓGICA: Converte para DTO
                .map(clienteSalvo -> new ResponseClienteDTO(
                        // Os métodos abaixo vão existir no Passo 2
                        clienteSalvo.getId(),
                        clienteSalvo.getNomeCompleto(),
                        clienteSalvo.getTelefone(),
                        clienteSalvo.getEmail()
                ));
    }
}