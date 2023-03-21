package br.com.wallace.service;

import java.util.List;

import br.com.wallace.dto.ProductInputDTO;
import br.com.wallace.dto.ProductOutputDTO;

public interface IProductService {
	ProductOutputDTO create(ProductInputDTO productInputDTO);
	ProductOutputDTO findById(Long id);
	void delete(Long id);
	List<ProductOutputDTO> findAll();
}
