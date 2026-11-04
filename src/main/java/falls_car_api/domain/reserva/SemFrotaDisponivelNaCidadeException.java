package falls_car_api.domain.reserva;

import falls_car_api.domain.shared.DomainException;

public class SemFrotaDisponivelNaCidadeException extends DomainException {
    public SemFrotaDisponivelNaCidadeException(String mensagem) {
        super(mensagem);
    }
}
