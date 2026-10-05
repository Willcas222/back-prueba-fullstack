package com.tuempresa.soporte.domain.exception;

public class SolicitudNotFoundException extends RuntimeException {
    public SolicitudNotFoundException(Long id) {
        super("No se encontró la solicitud " + id);
    }
}
