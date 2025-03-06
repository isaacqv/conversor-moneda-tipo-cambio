package com.example.conversor_moneda.controller;

import com.example.conversor_moneda.dto.ConversorRequest;
import com.example.conversor_moneda.dao.MonedaDAO;
import com.example.conversor_moneda.dao.entity.MonedaEntity;
import com.example.conversor_moneda.dto.ConversorResponse;
import com.example.conversor_moneda.util.Utilitario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/conversor")
public class MonedaController {

    @Autowired
    private MonedaDAO monedaDAO;

    @PostMapping("/moneda")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrarMoneda(@RequestBody MonedaEntity moneda) {

        String nomMoneda = Utilitario.normalizarCadena(moneda.getNombreMoneda());
        MonedaEntity monedaExistente = monedaDAO.findByNombreMoneda(nomMoneda);
        if (monedaExistente != null) {
            return Utilitario.errorDTOResponse(HttpStatus.CONFLICT,"Conflicto",
                    "Ya existe una moneda registrada con ese nombre: [" + nomMoneda +"]");
        }

        try {
            MonedaEntity grabarMoneda = monedaDAO.save(moneda);
            return ResponseEntity.ok(grabarMoneda);
        } catch (Exception ex) {
            return Utilitario.errorDTOResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Problema interno",ex.getMessage());
        }

    }

    @GetMapping("/monedas")
    public ResponseEntity<?> listarMonedas(){
        List<MonedaEntity> monedas = monedaDAO.findAll();
        if(monedas.isEmpty()){
            return Utilitario.errorDTOResponse(HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "No se encontraron registros de monedas");
        }
        return ResponseEntity.ok(monedas);
    }

    @PostMapping("/calcular")
    public ResponseEntity<?> calcularCambio(@RequestBody ConversorRequest request) {

        String nomMoneda = Utilitario.normalizarCadena(request.getMonedaDestino());
        MonedaEntity monedaDestino = monedaDAO.findByNombreMoneda(nomMoneda);

        if (monedaDestino == null) {
            return Utilitario.errorDTOResponse(HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Moneda no encontrada o registrada: [" + nomMoneda +"]");
        }

        BigDecimal resultado = Utilitario.multiplicar(request.getMonto(),monedaDestino.getTipoCambio());

        ConversorResponse response = new ConversorResponse();
        response.setMontoOriginal(Utilitario.redondarDecimales(request.getMonto(),2));
        response.setMontoConvertido(Utilitario.redondarDecimales(resultado,2));
        response.setMonedaOrigen(Utilitario.normalizarCadena(request.getMonedaOrigen()));
        response.setMonedaDestino(nomMoneda);
        response.setTipoCambio(monedaDestino.getTipoCambio());

        return ResponseEntity.ok(response);
    }

    @PutMapping("moneda/{id}")
    public ResponseEntity<?> modificarMoneda(@RequestBody MonedaEntity moneda, @PathVariable(value = "id") String nombreMoneda){

        if (nombreMoneda != null) {
            nombreMoneda = Utilitario.normalizarCadena(nombreMoneda);
        }

        MonedaEntity monedaObtenida = monedaDAO.findByNombreMoneda(nombreMoneda);

        if (monedaObtenida == null) {
            return Utilitario.errorDTOResponse(HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Moneda no encontrada o registrada: [" + nombreMoneda +"]");
        }

        moneda.setIdMoneda(monedaObtenida.getIdMoneda());
        MonedaEntity monedaMod = monedaDAO.save(moneda);
        return ResponseEntity.ok(monedaMod);
    }

}
