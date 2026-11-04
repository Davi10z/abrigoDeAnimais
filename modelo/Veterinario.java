package modelo;

public class Veterinario extends Usuario {
    private String crmv;

    public Veterinario(String nome, String email, String senha, String crmv){
        super(nome, email, senha);
        this.crmv = crmv;
    }

    @Override
    public String getTipoUsuario(){
        return "Veterinário";
    }

    // Getter
    public String getCrmv(){
        return crmv;
    }
    // Setter
    public void setCrmv(String crmv){
        this.crmv = crmv;
    }
}
