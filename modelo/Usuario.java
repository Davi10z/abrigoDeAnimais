package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario implements Notificavel{
    private String nome;
    private String email;
    private String senha;
    private List<String> notificacoes;

    public Usuario(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.notificacoes = new ArrayList<>();
    }

    // Metodo para forçar o polimorfismo das classes filhas
    public abstract String getTipoUsuario();

    // Métodos da interface
    @Override
    public void receberNotificacao(String mensagem) {
        this.notificacoes.add(mensagem);
    }
    @Override
    public List<String> getNotificacoes(){
        return this.notificacoes;
    }

    // ========== Getters ==========
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getSenha() {
        return senha;
    }
    // ========== Setters ==========
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

}