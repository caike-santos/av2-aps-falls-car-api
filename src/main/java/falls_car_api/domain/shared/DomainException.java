package falls_car_api.domain.shared;

public class DomainException extends RuntimeException{
    public DomainException(String mensagem) {
        super(mensagem);
    }
}
