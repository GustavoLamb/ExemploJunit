package br.examplo.junit.repository;

import java.util.List;
import java.util.Optional;

public interface EmailRepository {

    List<String> findAll();

    Optional<String> findByEmail(String email);
}
