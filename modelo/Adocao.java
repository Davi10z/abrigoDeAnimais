package modelo;

import java.time.LocalDate;
import java.util.UUID;

public class Adocao {
    private String id;
    private Usuario adotante;
    private Animal animal;
    private LocalDate dataSolicitacao;
    private StatusSolicitacao status;

    public Adocao(Usuario adotante, Animal animal) {
        this.id = UUID.randomUUID().toString();
        this.adotante = adotante;
        this.animal = animal;
        this.dataSolicitacao = LocalDate.now();
        this.status = StatusSolicitacao.PENDENTE;
    }

    // Getters
    public String getId() {
        return id;
    }
    public Usuario getAdotante() {
        return adotante;
    }
    public Animal getAnimal() {
        return animal;
    }
    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }
    public StatusSolicitacao getStatus() {
        return status;
    }

    // Setters
    public void setAdotante(Usuario adotante) {
        this.adotante = adotante;
    }
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }
}
