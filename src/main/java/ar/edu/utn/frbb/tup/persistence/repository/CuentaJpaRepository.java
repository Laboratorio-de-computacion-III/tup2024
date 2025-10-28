package ar.edu.utn.frbb.tup.persistence.repository;

import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaJpaRepository extends JpaRepository<CuentaEntity, Long> {
    List<CuentaEntity> findByTitular_Id(Long titularId);
}
