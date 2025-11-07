package com.gtp.cityinclui.service.impl;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.EditClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClientDTO;
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
    @Override
    public Mono<ResponseClientDTO> cadastrarCliente(CreateClienteDTO dto) {
        return clientRepository.existsByEmail(dto.email())
                .flatMap(emailJaExiste -> {
                    if (emailJaExiste) {
                        return Mono.error(new RegraNegocioException("O e-mail informado já está cadastrado."));
                    }
                    Client novoClient = new Client();
                    novoClient.setNomeCompleto(dto.nomeCompleto());
                    novoClient.setEmail(dto.email());
                    novoClient.setTelefone(dto.telefone());
                    novoClient.setSenha(passwordEncoder.encode(dto.senha()));
                    return clientRepository.save(novoClient);
                })
                .map(clientSalvo -> new ResponseClientDTO(
                        clientSalvo.getId(),
                        clientSalvo.getNomeCompleto(),
                        clientSalvo.getTelefone(),
                        clientSalvo.getEmail()
                ));
    }
    @Override
    public Flux<ResponseClientDTO> buscarTodosClientes() {
        return clientRepository.findAll()
                .map(client -> new ResponseClientDTO(
                        client.getId(),
                        client.getNomeCompleto(),
                        client.getTelefone(),
                        client.getEmail()
                ));
    }
    @Override
    public Mono<ResponseClientDTO> atualizarCliente(String email, EditClienteDTO dto) {
        return clientRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RegraNegocioException("Cliente não encontrado ou token inválido.")))
                .flatMap(clienteExistente -> {
                    if (dto.nomeCompleto() != null && !dto.nomeCompleto().isBlank()) {
                        clienteExistente.setNomeCompleto(dto.nomeCompleto());
                    }
                    if (dto.telefone() != null) {
                        clienteExistente.setTelefone(dto.telefone());
                    }
                    return clientRepository.save(clienteExistente);
                })
                .map(clientSalvo -> new ResponseClientDTO(
                        clientSalvo.getId(),
                        clientSalvo.getNomeCompleto(),
                        clientSalvo.getTelefone(),
                        clientSalvo.getEmail()
                ));
    }
    @Override
    public Mono<Void> deletarContaCliente(String email) {
        return clientRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        new RegraNegocioException("Cliente não encontrado ou token inválido.")
                ))
                .flatMap(clientRepository::delete);
    }
}