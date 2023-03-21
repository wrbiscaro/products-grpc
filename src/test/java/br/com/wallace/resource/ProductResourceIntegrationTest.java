package br.com.wallace.resource;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import br.com.wallace.EmptyRequest;
import br.com.wallace.ProductRequest;
import br.com.wallace.ProductResponse;
import br.com.wallace.ProductResponseList;
import br.com.wallace.ProductServiceGrpc;
import br.com.wallace.RequestById;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

// Teste integrado do gRPC Service
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext // Evita problemas como portas lockadas
public class ProductResourceIntegrationTest {

	@GrpcClient("inProcess") // Definido no application-test.properties
	private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub; // Stub do client para fazer chamadas
	
	@Autowired
	private Flyway flyway;
	
	@BeforeEach
	public void setUp() {
		// Limpa e repopula o H2 antes de cada teste usando o FlyWay
		flyway.clean();
		flyway.migrate();
	}

	@Test
	public void createProductSuccessTest() {
		ProductRequest request = ProductRequest.newBuilder()
		.setName("t")
		.setPrice(1.00)
		.setQuantityInStock(1)
		.build();
		
		ProductResponse response = serviceBlockingStub.create(request);
		
		Assertions.assertThat(request).usingRecursiveComparison()
		.comparingOnlyFields("name", "price", "quantity_in_stock").isEqualTo(response);
	}
	
	@Test
	public void createProductAlreadyExistsExceptionTest() {
		ProductRequest request = ProductRequest.newBuilder()
		.setName("Product A")
		.setPrice(1.00)
		.setQuantityInStock(1)
		.build();
		
		Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
		.isThrownBy(() -> serviceBlockingStub.create(request))
		.withMessage("ALREADY_EXISTS: Produto Product A já cadastrado no sistema");
	}
	
	@Test
	public void findByIdSuccessTest() {
		RequestById request = RequestById.newBuilder()
		.setId(1L)
		.build();
		
		ProductResponse response = serviceBlockingStub.findById(request);
		
		Assertions.assertThat(response.getId()).isEqualTo(request.getId());
	}
	
	@Test
	public void findByIdExceptionTest() {
		RequestById request = RequestById.newBuilder()
		.setId(3L)
		.build();

		Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
		.isThrownBy(() -> serviceBlockingStub.findById(request))
		.withMessage("NOT_FOUND: Produto com id 3 não encontrado");
	}
	
	@Test
	public void deleteSuccessTest() {
		RequestById request = RequestById.newBuilder()
		.setId(1L)
		.build();

		Assertions.assertThatNoException().isThrownBy(() -> serviceBlockingStub.delete(request));
	}
	
	@Test
	public void deleteExceptionTest() {
		RequestById request = RequestById.newBuilder()
		.setId(3L)
		.build();

		Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
		.isThrownBy(() -> serviceBlockingStub.delete(request))
		.withMessage("NOT_FOUND: Produto com id 3 não encontrado");
	}
	
	@Test
	public void findAllSuccessTest() {
		EmptyRequest request = EmptyRequest.newBuilder().build();
		
		ProductResponseList response = serviceBlockingStub.findAll(request);
		
		Assertions.assertThat(response).isInstanceOf(ProductResponseList.class);
		Assertions.assertThat(response.getProductsCount()).isEqualTo(2);
		Assertions.assertThat(response.getProductsList()).extracting("id", "name", "price", "quantityInStock")
		.contains(
				Assertions.tuple(1L, "Product A", 10.99, 10),
				Assertions.tuple(2L, "Product B", 10.99, 10)
			);		
	}
}