package gui;

import modelo.Usuario;
import modelo.Administrador;
import modelo.Veterinario;
import modelo.Voluntario;
import repositorios.AnimalRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaMenuPrincipal extends JFrame {

    private Usuario usuarioLogado;
    private AnimalRepository animalRepo;

    public TelaMenuPrincipal(Usuario usuarioLogado, AnimalRepository animalRepo) {
        this.usuarioLogado = usuarioLogado;
        this.animalRepo = animalRepo;

        setTitle("Sistema de Abrigos - Área do " + usuarioLogado.getTipoUsuario());
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(41, 128, 185));
        JLabel labelBemVindo = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome());
        labelBemVindo.setForeground(Color.WHITE);
        labelBemVindo.setFont(new Font("Arial", Font.BOLD, 20));
        painelTopo.add(labelBemVindo);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(0, 2, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Bloco dos Voluntários
        if (usuarioLogado instanceof Voluntario) {
            painelBotoes.add(criarBotao("Visualizar Animais Disponíveis"));
            painelBotoes.add(criarBotao("Solicitar Adoção"));
            painelBotoes.add(criarBotao("Consultar Minhas Solicitações"));
            painelBotoes.add(criarBotao("Participar de Eventos"));
            painelBotoes.add(criarBotao("Histórico de Participações"));
            
        // Bloco dos Veterinários
        } else if (usuarioLogado instanceof Veterinario) {
            painelBotoes.add(criarBotao("Consultar Animais"));
            painelBotoes.add(criarBotao("Registar Atendimento"));
            painelBotoes.add(criarBotao("Atualizar Tratamento"));
            painelBotoes.add(criarBotao("Registar Vacinação"));
            painelBotoes.add(criarBotao("Emitir Relatório Médico"));
            
        // Bloco dos Administradores
        } else if (usuarioLogado instanceof Administrador) {
            // Separamos este botão para lhe dar uma ação específica
            JButton btnGerirAnimais = criarBotao("Gerir Animais");
            btnGerirAnimais.addActionListener(e -> {
                // Abre o ecrã de cadastro passando o repositório que contém a lista
                TelaCadastroAnimal telaCadastro = new TelaCadastroAnimal(animalRepo);
                telaCadastro.setVisible(true);
            });
            painelBotoes.add(btnGerirAnimais);

            painelBotoes.add(criarBotao("Gerir Usuários"));
            painelBotoes.add(criarBotao("Aprovar Adoções"));
            painelBotoes.add(criarBotao("Gerir Eventos"));
            painelBotoes.add(criarBotao("Relatórios e Estatísticas"));
        }

        // Botões Comuns a todos os utilizadores
        painelBotoes.add(criarBotao("Notificações"));
        
        JButton btnSair = new JButton("Sair (Logout)");
        btnSair.setBackground(new Color(231, 76, 60)); // Vermelhão
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha o menu
                JOptionPane.showMessageDialog(null, "Sessão encerrada."); // Retornar à TelaLogin será configurado no controlador principal
            }
        });
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.CENTER);
    }

    // Método auxiliar para padronizar o design dos botões
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setBackground(new Color(236, 240, 241));
        return botao;
    }
}