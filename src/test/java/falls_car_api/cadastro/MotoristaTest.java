package falls_car_api.cadastro;

import falls_car_api.domain.cadastro.Motorista;
import falls_car_api.domain.shared.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotoristaTest {

    @Test
    void alocarMotoristaMarcaComoIndisponivel() {
        Motorista motorista = new Motorista();
        motorista.setIsDisponivel(true);

        motorista.alocarMotorista();

        assertFalse(motorista.getIsDisponivel());
    }

    @Test
    void alocarMotoristaIndisponivelLancaExcecao() {
        Motorista motorista = new Motorista();
        motorista.setIsDisponivel(false);

        assertThrows(DomainException.class, motorista::alocarMotorista);
    }

    @Test
    void liberarMotoristaMarcaComoDisponivel() {
        Motorista motorista = new Motorista();
        motorista.setIsDisponivel(false);

        motorista.liberarMotorista();

        assertTrue(motorista.getIsDisponivel());
    }
}
