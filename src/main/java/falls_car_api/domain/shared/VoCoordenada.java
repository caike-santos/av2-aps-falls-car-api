package falls_car_api.domain.shared;

public record VoCoordenada(Double latitude, Double longitude) {
    public VoCoordenada {
        if (latitude == null || longitude == null) {
            throw new DomainException("Latitude e longitude são obrigatórias para calcular a distância.");
        }
    }
}