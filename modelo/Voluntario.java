package modelo;

public class Voluntario extends Usuario {
    public Voluntario(String nome, String email, String senha){
        super(nome, email, senha);
    }

    @Override
    public String getTipoUsuario(){
        return "Voluntário";
    }
}
