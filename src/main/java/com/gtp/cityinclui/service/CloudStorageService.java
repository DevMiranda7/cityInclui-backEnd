package com.gtp.cityinclui.service;

import reactor.core.publisher.Mono;

public interface CloudStorageService {

    Mono<String> uploadPhoto(byte[] photoDates, String fileName, String contentType);

    Mono<Void> deletePhoto(String urlPhoto);
}
