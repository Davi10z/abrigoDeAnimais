import modelo.Administrador;
import modelo.Usuario;
import modelo.Veterinario;
import modelo.Voluntario;
import repositorios.UsuarioRepository;
import repositorios.AnimalRepository;
import gui.TelaLogin;
import gui.TelaMenuPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        AnimalRepository animalRepo = new AnimalRepository();

        usuarioRepo.cadastrar(new Administrador("Davi - Dev", "admin@abrigo.com", "123"));
        usuarioRepo.cadastrar(new Veterinario("Dr. Davi", "vet@abrigo.com", "123", "CRMV-999"));
        usuarioRepo.cadastrar(new Voluntario("Maçã - Voluntário", "maca@abrigo.com", "123"));

        // Aplicar o visual nativo do sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Arrancar a Interface Gráfica
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            
            // Substituímos a ação do botão Entrar para usar o repositório real
            telaLogin.getBtnEntrar().addActionListener(e -> {
                String email = telaLogin.getCampoEmail().getText();
                String senha = new String(telaLogin.getCampoSenha().getPassword());
                
                Usuario usuarioLogado = usuarioRepo.autenticar(email, senha);
                
                if (usuarioLogado != null) {
                    telaLogin.dispose(); // Fecha o janela de login
                    
                    // Abre o menu principal passando o utilizador (Polimorfismo em ação)
                    TelaMenuPrincipal menu = new TelaMenuPrincipal(usuarioLogado, animalRepo);
                    menu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(telaLogin, "Credenciais inválidas.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            telaLogin.setVisible(true);
        });
    }
}
