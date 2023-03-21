package br.com.wallace.exception;

import io.grpc.Status;

public abstract class BaseBusinessException extends RuntimeException {
	
	public BaseBusinessException(String message) {
		super(message);
	}
	
	public abstract Status getStatusCode(); // gRPC Status
	public abstract String getErrorMessage();
}
