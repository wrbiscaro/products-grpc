package br.com.wallace.exception.handler;

import br.com.wallace.exception.BaseBusinessException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {

	// Captura todas as exceptions da BaseBusinessException
	@GrpcExceptionHandler(BaseBusinessException.class)
	public StatusRuntimeException handleBusinessException(BaseBusinessException e) {
		return e.getStatusCode().withCause(e.getCause()).withDescription(e.getErrorMessage()).asRuntimeException();
	}
}
