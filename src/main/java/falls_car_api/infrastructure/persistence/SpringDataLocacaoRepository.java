package falls_car_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataLocacaoRepository extends JpaRepository<LocacaoEntity, UUID> {
}