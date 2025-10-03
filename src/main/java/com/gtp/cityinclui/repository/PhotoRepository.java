package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.PhotoRegister;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PhotoRepository extends ReactiveCrudRepository<PhotoRegister, Long > {
}
