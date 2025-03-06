package com.example.conversor_moneda.controller;

import com.example.conversor_moneda.dto.ConversorRequest;
import com.example.conversor_moneda.dao.MonedaDAO;
import com.example.conversor_moneda.dao.entity.MonedaEntity;
import com.example.conversor_moneda.dto.ConversorResponse;
import com.example.conversor_moneda.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/conversor")
public class MonedaController {

    @Autowired
    private MonedaDAO monedaDAO;

    @PostMapping("/moneda")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMoneda(@RequestBody MonedaEntity moneda) {

        try {
            MonedaEntity grabarMoneda = monedaDAO.save(moneda);
            return ResponseEntity.ok(grabarMoneda);
        } catch (Exception ex) {
            ErrorDTO errorResponse = new ErrorDTO();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setError("Problema interno");
            errorResponse.setMessage(ex.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/monedas")
    List<MonedaEntity> listarMonedas(){
        return monedaDAO.findAll();
    }

    @PostMapping("/calcular")
    public ResponseEntity<?> calcularCambio(@RequestBody ConversorRequest request) {

        MonedaEntity monedaDestino = monedaDAO.findByNombreMoneda(request.getMonedaDestino().toUpperCase());

        if (monedaDestino == null) {
            ErrorDTO errorResponse = new ErrorDTO();
            errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
            errorResponse.setMessage("Moneda no encontrada o registrada: [" + request.getMonedaDestino().toUpperCase() +"]");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        BigDecimal resultado = request.getMonto().multiply(monedaDestino.getTipoCambio()).setScale(2, RoundingMode.HALF_UP);

        ConversorResponse response = new ConversorResponse();
        response.setMontoOriginal(request.getMonto().setScale(2, RoundingMode.HALF_UP));
        response.setMontoConvertido(resultado);
        response.setMonedaOrigen(request.getMonedaOrigen().toUpperCase());
        response.setMonedaDestino(request.getMonedaDestino().toUpperCase());
        response.setTipoCambio(monedaDestino.getTipoCambio());

        return ResponseEntity.ok(response);
    }

    @PutMapping("moneda/{id}")
    public ResponseEntity<?> modificarMoneda(@RequestBody MonedaEntity moneda, @PathVariable(value = "id") String nombre){

        MonedaEntity monedaObtenida = monedaDAO.findByNombreMoneda(nombre.toUpperCase());

        if (monedaObtenida == null) {
            ErrorDTO errorResponse = new ErrorDTO();
            errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
            errorResponse.setMessage("Moneda no encontrada o registrada: [" + nombre.toUpperCase() +"]");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        moneda.setIdMoneda(monedaObtenida.getIdMoneda());
        MonedaEntity modificarMoneda = monedaDAO.save(moneda);
        return ResponseEntity.ok(modificarMoneda);
    }

}
