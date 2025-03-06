package com.example.conversor_moneda.util;

import com.example.conversor_moneda.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;

public class Utilitario {

    public static BigDecimal multiplicar(BigDecimal valorA, BigDecimal valorB) {
        return (valorA != null && valorB != null) ? valorA.multiply(valorB) : null;
    }

    public static BigDecimal redondarDecimales(BigDecimal valor, int numDecimales) {
        return (valor == null) ? valor : valor.setScale(numDecimales, RoundingMode.HALF_UP);
    }

    public static String normalizarCadena(String nombreMoneda) {
        nombreMoneda = nombreMoneda.trim();
        // Normaliza la cadena para separar los diacríticos
        String normalized = Normalizer.normalize(nombreMoneda, Normalizer.Form.NFD);
        // Remueve los diacríticos (acentos)
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.toUpperCase();
    }

    public static ResponseEntity<?> errorDTOResponse(HttpStatus status, String error, String message) {
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setStatus(status.value());
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        return new ResponseEntity<>(errorResponse, status);
    }

}
