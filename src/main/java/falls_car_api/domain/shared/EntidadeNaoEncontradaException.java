package falls_car_api.domain.shared;

public class EntidadeNaoEncontradaException extends DomainException {
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
