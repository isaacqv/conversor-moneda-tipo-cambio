package com.example.conversor_moneda.dao.entity;

import com.example.conversor_moneda.util.Utilitario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "moneda")
public class MonedaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMoneda;

    @NotBlank(message = "El nombre de la moneda no puede estar vacío")
    @Column(unique = true)
    private String nombreMoneda;

    @NotNull(message = "El tipo de cambio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El tipo de cambio debe ser mayor a 0.0")
    private BigDecimal tipoCambio;

    public int getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(int idMoneda) {
        this.idMoneda = idMoneda;
    }

    public String getNombreMoneda() {
        return nombreMoneda;
    }

    public void setNombreMoneda(String nombreMoneda) {
        if (nombreMoneda != null) {
            this.nombreMoneda = Utilitario.normalizarCadena(nombreMoneda);
        } else {
            this.nombreMoneda = null;
        }
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
}
