package repositorios;

import modelo.Usuario;
import java.util.List;
import java.util.ArrayList;

public class UsuarioRepository {
    private List<Usuario> usuarios;

    public UsuarioRepository(){
        this.usuarios = new ArrayList<>();
    }

    public void cadastrar(Usuario usuario){
        usuarios.add(usuario);
    }

    public Usuario autenticar(String email, String senha){
        for (Usuario u : usuarios){
            if(u.getEmail().equals(email) && u.getSenha().equals(senha)){
                return u; // Retorna se o usuario tiver digitado email e senha de forma correta.
            }
        }
        return null; // Retorna nada.
    }

    public List<Usuario> listarTodos() {
        return usuarios;
    }

    public boolean emailExiste(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public void removerPorEmail(String email) {
        usuarios.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
