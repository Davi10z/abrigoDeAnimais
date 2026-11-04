package modelo;

public class Administrador extends Usuario {
    public Administrador(String nome, String email, String senha){
        super(nome, email, senha);
    }

    @Override
    public String getTipoUsuario(){
        return "Adminstrador";
    }
}
