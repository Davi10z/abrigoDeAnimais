package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Evento {
    private String id;
    private String titulo;
    private String tipoEvento;
    private LocalDate dataEvento;
    private List<Usuario> voluntariosInscritos;

    public Evento(String titulo, String tipoEvento, LocalDate dataEvento){
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.tipoEvento = tipoEvento;
        this.dataEvento = dataEvento;
        this.voluntariosInscritos = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getTipoEvento() {
        return tipoEvento;
    }
    public LocalDate getDataEvento() {
        return dataEvento;
    }
    public List<Usuario> getVoluntariosInscritos() {
        return voluntariosInscritos;
    }

    // Apenas adiciona se o voluntário ainda não estiver na lista
    public void adicionarVoluntario(Usuario voluntario){
        if(!voluntariosInscritos.contains(voluntario)){
            voluntariosInscritos.add(voluntario);
        }
    }
}
