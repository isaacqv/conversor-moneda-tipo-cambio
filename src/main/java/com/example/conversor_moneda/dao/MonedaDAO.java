package com.example.conversor_moneda.dao;

import com.example.conversor_moneda.dao.entity.MonedaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonedaDAO extends JpaRepository<MonedaEntity,Long> {
    MonedaEntity findByNombreMoneda(String nombreMoneda);
}
