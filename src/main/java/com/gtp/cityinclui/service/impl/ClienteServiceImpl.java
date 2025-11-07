package com.gtp.cityinclui.service.impl;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.EditClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClientDTO;
import com.gtp.cityinclui.dto.client.ResponseClienteDTO;
import com.gtp.cityinclui.exception.RegraNegocioException;
import com.gtp.cityinclui.entity.Client;
import com.gtp.cityinclui.repository.ClientRepository;
import com.gtp.cityinclui.service.ClientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- 3. MÉTODO 1 (estava faltando) ---
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

    // --- 4. MÉTODO 2 (que fizemos antes) ---
    @Override
    public
     Flux<ResponseClienteDTO> buscarTodosClientes() {
        return clientRepository.findAll()
                .map(client -> new ResponseClienteDTO(
                        client.getId(),
                        client.getNomeCompleto(),
                        client.getTelefone(),
                        client.getEmail()
                ));
    }

    // --- 5. MÉTODO 3 (o novo método) ---
    @Override
    public Mono<ResponseClienteDTO> atualizarCliente(String email, EditClienteDTO dto) {

        return clientRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        new RegraNegocioException("Cliente não encontrado ou token inválido.")
                ))
                .flatMap(clienteExistente -> {

                    if (dto.nomeCompleto() != null && !dto.nomeCompleto().isBlank()) {
                        clienteExistente.setNomeCompleto(dto.nomeCompleto());
                    }
                    if (dto.telefone() != null) {
                        clienteExistente.setTelefone(dto.telefone());
                    }
                    return clientRepository.save(clienteExistente);
                })
                .map(clientSalvo -> new ResponseClienteDTO(
                        clientSalvo.getId(),
                        clientSalvo.getNomeCompleto(),
                        clientSalvo.getTelefone(),
                        clientSalvo.getEmail()
                ));
    }
}