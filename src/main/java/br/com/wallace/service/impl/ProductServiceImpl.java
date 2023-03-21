package br.com.wallace.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.wallace.domain.Product;
import br.com.wallace.dto.ProductInputDTO;
import br.com.wallace.dto.ProductOutputDTO;
import br.com.wallace.exception.ProductAlreadyExistsException;
import br.com.wallace.exception.ProductNotFoundException;
import br.com.wallace.repository.ProductRepository;
import br.com.wallace.service.IProductService;
import br.com.wallace.util.ProductConverterUtil;

@Service
public class ProductServiceImpl implements IProductService {

	private final ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Override
	public ProductOutputDTO create(ProductInputDTO productInputDTO) {
		checkDuplicity(productInputDTO.getName());
		var product = ProductConverterUtil.productInputDtoToProduct(productInputDTO);
		product = this.productRepository.save(product);

		return ProductConverterUtil.productToProductOutputDto(product);
	}

	@Override
	public ProductOutputDTO findById(Long id) {
		Product product = productRepository.findById(id)
		.orElseThrow(() -> new ProductNotFoundException(id));
		
		return ProductConverterUtil.productToProductOutputDto(product);
	}

	@Override
	public void delete(Long id) {
		Product product = productRepository.findById(id)
		.orElseThrow(() -> new ProductNotFoundException(id));
		
		productRepository.delete(product);
	}

	@Override
	public List<ProductOutputDTO> findAll() {
		var products = productRepository.findAll();

		return products.stream().map(ProductConverterUtil::productToProductOutputDto)
		.collect(Collectors.toList());
	}

	private void checkDuplicity(String name) {
		this.productRepository.findByNameIgnoreCase(name)
		.ifPresent(e -> {
			throw new ProductAlreadyExistsException(name);
		});
	}
}
