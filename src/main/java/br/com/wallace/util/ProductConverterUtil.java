package br.com.wallace.util;

import br.com.wallace.domain.Product;
import br.com.wallace.dto.ProductInputDTO;
import br.com.wallace.dto.ProductOutputDTO;

public class ProductConverterUtil {
	
	public static ProductOutputDTO productToProductOutputDto(Product product) {
		return new ProductOutputDTO(product.getId(), 
				product.getName(), 
				product.getPrice(), 
				product.getQuantityInStock());
	}
	
	public static Product productInputDtoToProduct(ProductInputDTO productInputDTO) {
		return new Product(null, 
				productInputDTO.getName(), 
				productInputDTO.getPrice(), 
				productInputDTO.getQuantityInStock());
	}
}
