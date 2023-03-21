package br.com.wallace.service.impl;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wallace.domain.Product;
import br.com.wallace.dto.ProductInputDTO;
import br.com.wallace.dto.ProductOutputDTO;
import br.com.wallace.exception.ProductAlreadyExistsException;
import br.com.wallace.exception.ProductNotFoundException;
import br.com.wallace.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductServiceImpl productServiceImpl;

	@Test
	public void createProductSuccessTest() {
		Product product = new Product(1L, "name", 10.00, 2);
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
		
		ProductInputDTO inputDto = new ProductInputDTO("name", 10.00, 2);
		ProductOutputDTO outputDto = productServiceImpl.create(inputDto);
		
		Assertions.assertThat(outputDto).usingRecursiveComparison().isEqualTo(product);
	}
	
	@Test
	public void createProductExceptionTest() {
		Product product = new Product(1L, "name", 10.00, 2);
		Mockito.when(productRepository.findByNameIgnoreCase(Mockito.any())).thenReturn(Optional.of(product));
		
		ProductInputDTO inputDto = new ProductInputDTO("name", 10.00, 2);
		
		Assertions.assertThatExceptionOfType(ProductAlreadyExistsException.class)
		.isThrownBy(() -> productServiceImpl.create(inputDto));
	}
	
	@Test
	public void findByIdSuccessTest() {
		Long id = 1L;
		Product product = new Product(1L, "name", 10.00, 2);
		
		Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

		ProductOutputDTO outputDto = productServiceImpl.findById(id);
		
		Assertions.assertThat(outputDto).usingRecursiveComparison().isEqualTo(product);
	}
	
	@Test
	public void findByIdExceptionTest() {
		Long id = 1L;

		Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
		.isThrownBy(() -> productServiceImpl.findById(id));
	}
	
	@Test
	public void deleteSuccessTest() {
		Long id = 1L;
		Product product = new Product(1L, "name", 10.00, 2);
		Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

		Assertions.assertThatNoException().isThrownBy(() -> productServiceImpl.findById(id));
	}
	
	@Test
	public void deleteExceptionTest() {
		Long id = 1L;

		Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
		.isThrownBy(() -> productServiceImpl.delete(id));
	}
	
	@Test
	public void findAllSuccessTest() {
		List<Product> products = List.of(
				new Product(1L, "name 1", 10.00, 2),
				new Product(2L, "name 2", 20.00, 4)
			);
		
		Mockito.when(productRepository.findAll()).thenReturn(products);

		List<ProductOutputDTO> outputDtos = productServiceImpl.findAll();
		
		Assertions.assertThat(outputDtos).extracting("id", "name", "price", "quantityInStock")
		.contains(
				Assertions.tuple(1L, "name 1", 10.00, 2),
				Assertions.tuple(2L, "name 2", 20.00, 4)
			);
	}
}
