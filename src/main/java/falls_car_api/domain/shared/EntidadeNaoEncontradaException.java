package falls_car_api.domain.shared;

/**
 * Lançada quando um agregado não é encontrado pelo id.
 * Estende DomainException, então código existente que captura DomainException continua funcionando.
 */
public class EntidadeNaoEncontradaException extends DomainException {
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
