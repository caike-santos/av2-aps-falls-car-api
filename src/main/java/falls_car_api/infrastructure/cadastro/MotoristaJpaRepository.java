package falls_car_api.infrastructure.cadastro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MotoristaJpaRepository extends JpaRepository<MotoristaEntity, UUID> {
    Optional<MotoristaEntity> findByCpf(String cpf);
}
