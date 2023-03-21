package br.com.wallace.resource;

import java.util.List;
import java.util.stream.Collectors;

import br.com.wallace.EmptyRequest;
import br.com.wallace.EmptyResponse;
import br.com.wallace.ProductRequest;
import br.com.wallace.ProductResponse;
import br.com.wallace.ProductResponseList;
import br.com.wallace.ProductServiceGrpc;
import br.com.wallace.RequestById;
import br.com.wallace.dto.ProductInputDTO;
import br.com.wallace.dto.ProductOutputDTO;
import br.com.wallace.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

// @GrpcService é equivalente ao REST Controller do Spring
// Extende a classe service gerada a partir do ProtoBuf 
@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {
	
	private IProductService productService;
	
	public ProductResource(IProductService productService) {
		this.productService = productService;
	}

	@Override
	public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
		ProductInputDTO inputDto = new ProductInputDTO(request.getName(), 
				request.getPrice(), 
				request.getQuantityInStock());

		ProductOutputDTO outputDto = this.productService.create(inputDto);
		
		ProductResponse response = ProductResponse.newBuilder()
		.setId(outputDto.getId())
		.setName(outputDto.getName())
		.setPrice(outputDto.getPrice()).setQuantityInStock(outputDto.getQuantityInStock())
		.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
		ProductOutputDTO outputDto = productService.findById(request.getId());
		
		ProductResponse response = ProductResponse.newBuilder()
		.setId(outputDto.getId())
		.setName(outputDto.getName())
		.setPrice(outputDto.getPrice()).setQuantityInStock(outputDto.getQuantityInStock())
		.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
		productService.delete(request.getId());
		responseObserver.onNext(EmptyResponse.newBuilder().build());
		responseObserver.onCompleted();
	}
	
	@Override
	public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
		List<ProductOutputDTO> outputDtos = productService.findAll();
		
		List<ProductResponse> productResponses = outputDtos.stream().map(outputDto -> 
				ProductResponse.newBuilder()
				.setId(outputDto.getId())
				.setName(outputDto.getName())
				.setPrice(outputDto.getPrice()).setQuantityInStock(outputDto.getQuantityInStock())
				.build()
			).collect(Collectors.toList());
		
		ProductResponseList productResponseList = ProductResponseList.newBuilder()
				.addAllProducts(productResponses).build();
		
		responseObserver.onNext(productResponseList);
		responseObserver.onCompleted();
	}
}
