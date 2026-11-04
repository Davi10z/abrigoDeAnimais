package modelo;

import java.time.LocalDate;

public class Vacina {
    private String nome;
    private LocalDate dataAplicacao;

    public Vacina(String nome, LocalDate dataAplicacao){
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
    }

    // Getters
    public String getNome() {
        return nome;
    }
    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }
}
