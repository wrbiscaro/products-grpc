package br.com.wallace.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.wallace.domain.Product;
import br.com.wallace.dto.ProductInputDTO;

public class ProductConverterUtilTest {
	
	@Test
	public void productToProductOutputDtoTest() {
		var product = new Product(1L, "name", 10.00, 2);
		var productOutputDto = ProductConverterUtil.productToProductOutputDto(product);
		
		// Compara os campos dos dois objetos usando o AssertJ
		// Para funcionar, os nomes dos atributos devem ser iguais
		Assertions.assertThat(product).usingRecursiveComparison().isEqualTo(productOutputDto);		
	}
	
	@Test
	public void productInputDtoToProductTest() {
		var productInputDTO = new ProductInputDTO("name", 10.00, 2);
		var product = ProductConverterUtil.productInputDtoToProduct(productInputDTO);
		
		// Compara os campos dos dois objetos usando o AssertJ
		// Para funcionar, os nomes dos atributos devem ser iguais
		Assertions.assertThat(productInputDTO).usingRecursiveComparison().isEqualTo(product);		
	}
}
