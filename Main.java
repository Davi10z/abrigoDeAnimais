import modelo.Administrador;
import modelo.Animal;
import modelo.Evento;
import modelo.StatusAdocao;
import modelo.Usuario;
import modelo.Veterinario;
import modelo.Voluntario;
import repositorios.AdocaoRepository;
import repositorios.AnimalRepository;
import repositorios.EventoRepository;
import repositorios.SaudeRepository;
import repositorios.UsuarioRepository;
import gui.TelaCadastroUsuario;
import gui.TelaLogin;
import gui.TelaMenuPrincipal;
import util.SistemaUtil;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Instanciar todos os repositórios (é o nosso banco de dados temporario)
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        AnimalRepository animalRepo = new AnimalRepository();
        AdocaoRepository adocaoRepo = new AdocaoRepository();
        SaudeRepository saudeRepo = new SaudeRepository();
        EventoRepository eventoRepo = new EventoRepository();

        // Dados iniciais de teste
        usuarioRepo.cadastrar(new Administrador("Davi Admin", "admin@abrigo.com", "123"));
        usuarioRepo.cadastrar(new Veterinario("Dr. Carlos", "vet@abrigo.com", "123", "CRMV-999"));
        usuarioRepo.cadastrar(new Voluntario("Ana Voluntária", "ana@abrigo.com", "123"));

        animalRepo.cadastrar(new Animal("Thor", "Cão", "Labrador", 4, "Grande", "Saudável", LocalDate.now(), StatusAdocao.DISPONIVEL));
        animalRepo.cadastrar(new Animal("Mel", "Gato", "SRD", 2, "Pequeno", "Vacinada", LocalDate.now(), StatusAdocao.DISPONIVEL));

        eventoRepo.cadastrarEvento(new Evento("Feira de Domingo", "Feira de Adoção", LocalDate.now().plusDays(7)));

        // Visual nativo do sistema operacional (Tô no windows, no linux fica diferente, testei lá também)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Arranca a interface chamando o método auxiliar
        SwingUtilities.invokeLater(() -> {
            abrirLogin(usuarioRepo, animalRepo, adocaoRepo, saudeRepo, eventoRepo);
        });
    }

    private static void abrirLogin(UsuarioRepository usuarioRepo, AnimalRepository animalRepo, AdocaoRepository adocaoRepo, SaudeRepository saudeRepo, EventoRepository eventoRepo) {
        TelaLogin telaLogin = new TelaLogin();

        telaLogin.getBtnCadastrar().addActionListener(e -> {
            new TelaCadastroUsuario(usuarioRepo).setVisible(true);
        });

        telaLogin.getBtnEntrar().addActionListener(e -> {
            String email = telaLogin.getCampoEmail().getText();
            String senha = new String(telaLogin.getCampoSenha().getPassword());

            Usuario usuarioLogado = usuarioRepo.autenticar(email, senha);
            if (usuarioLogado != null) {
                SistemaUtil.registrarLogin();
                telaLogin.dispose();

                Runnable aoDeslogar = () -> abrirLogin(usuarioRepo, animalRepo, adocaoRepo, saudeRepo, eventoRepo);
                new TelaMenuPrincipal(
                    usuarioLogado, animalRepo, adocaoRepo, saudeRepo, eventoRepo, usuarioRepo, aoDeslogar
                ).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaLogin, "Credenciais inválidas.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        telaLogin.setVisible(true);
    }
}