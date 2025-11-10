package com.gtp.cityinclui.service;
import reactor.core.publisher.Mono;
public interface CloudStorageService {
    Mono<String> uploadFoto(byte[] dadosFoto, String nomeArquivo, String contentTipo);
    Mono<Void> deleteFoto(String urlFoto);
}
