package falls_car_api.infrastructure.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservaJpaRepository extends JpaRepository<ReservaEntity, UUID> {
}
