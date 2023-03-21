package br.com.wallace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wallace.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	Optional<Product> findByNameIgnoreCase(String name);
}
