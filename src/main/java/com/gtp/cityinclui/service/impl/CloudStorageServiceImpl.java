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
    public Mono<String> uploadPhoto(byte[] photoDates, String fileName, String contentType) {
        PutObjectRequest request= PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .contentLength((long) photoDates.length)
                .build();

        return Mono.fromFuture(
                s3Client.putObject(request, AsyncRequestBody.fromBytes(photoDates))
        ).map(response -> {
            if (response.sdkHttpResponse().isSuccessful()){
                return String.format("https://%s.s3.%s.amazonaws.com/%s",bucketName,region, fileName);
            }
            String errorMessage = response.sdkHttpResponse().statusText().orElse("Erro desconhecido");
            throw new CloudStorageException("Falha no upload para o S3: " + errorMessage);

        }).onErrorMap(throwable ->
                new CloudStorageException("Erro ao tentar fazer upload para o S3: "+throwable.getMessage() , throwable));
    }

    @Override
    public Mono<Void> deletePhoto(String urlPhoto) {
        String nomeArquivo;

        try {
            URL url = new URL(urlPhoto);
            nomeArquivo = url.getPath().substring(1);
        }catch (MalformedURLException e) {
            return Mono.error(new CloudStorageException("URL da foto é inválida ", e));
        }

        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return Mono.error(new CloudStorageException("Não foi possível extrair nome do arquivo da URL: " + urlPhoto));
        }

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(this.bucketName)
                .key(nomeArquivo)
                .build();

        return Mono.fromFuture(s3Client.deleteObject(deleteObjectRequest))
                .onErrorMap(throwable ->
                        new CloudStorageException("Erro ao tentar deletar foto do S3: " + throwable.getMessage() , throwable)).then();

    }
}
