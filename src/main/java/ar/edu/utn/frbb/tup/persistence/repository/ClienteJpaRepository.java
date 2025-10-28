package ar.edu.utn.frbb.tup.persistence.repository;

import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {
}
