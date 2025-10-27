package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.exception.CloudStorageException;
import com.gtp.cityinclui.service.CloudStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    private final S3AsyncClient s3Client;
    private final String bucketName;
    private final String region;

    public CloudStorageServiceImpl(
           S3AsyncClient s3Client,
           @Value("${aws.s3.bucket-name}") String bucketName,
           @Value("${aws.s3.region}") String region)
    {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
    }

    @Override
    public Mono<String> uploadFoto(byte[] dadosFoto, String nomeArquivo, String contentTipo) {
        PutObjectRequest request= PutObjectRequest.builder()
                .bucket(bucketName)
                .key(nomeArquivo)
                .contentType(contentTipo)
                .contentLength((long) dadosFoto.length)
                .build();

        return Mono.fromFuture(
                s3Client.putObject(request, AsyncRequestBody.fromBytes(dadosFoto))
        ).map(response -> {
            if (response.sdkHttpResponse().isSuccessful()){
                return String.format("https://%s.s3.%s.amazonaws.com/%s",bucketName,region,nomeArquivo);
            }
            String errorMessage = response.sdkHttpResponse().statusText().orElse("Erro desconhecido");
            throw new CloudStorageException("Falha no upload para o S3: " + errorMessage);

        });
    }

    @Override
    public Mono<Void> deleteFoto(String urlFoto) {
        String nomeArquivo;

        try {
            URL url = new URL(urlFoto);
            nomeArquivo = url.getPath().substring(1);
        }catch (MalformedURLException e) {
            return Mono.error(new RuntimeException("URL da foto é inválida", e));
        }

        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return Mono.error(new RuntimeException("Não foi possível extrair nome do arquivo da URL: " + urlFoto));
        }

        return Mono.fromFuture(
                s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(this.bucketName)
                .key(nomeArquivo)
                .build()))
                .then();
    }
}
