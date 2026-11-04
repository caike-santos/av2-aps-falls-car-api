package falls_car_api.infrastructure.frota;

import falls_car_api.domain.shared.StatusAutomovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AutomovelJpaRepository extends JpaRepository<AutomovelEntity, UUID> {
    boolean existsByPlaca(String placa);
    List<AutomovelEntity> findByLojaAtualIdAndStatusAutomovel(UUID lojaAtualId, StatusAutomovel statusAutomovel);
}
