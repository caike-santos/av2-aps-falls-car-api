package falls_car_api.infrastructure.loja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LojaJpaRepository extends JpaRepository<LojaEntity, UUID> {
    List<LojaEntity> findByCidade(String cidade);
}