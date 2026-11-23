package gui;

import modelo.Usuario;
import modelo.Administrador;
import modelo.Veterinario;
import modelo.Voluntario;
import repositorios.AnimalRepository;
import repositorios.EventoRepository;
import repositorios.AdocaoRepository;
import repositorios.SaudeRepository;
import repositorios.UsuarioRepository;

import javax.swing.*;
import java.awt.*;

public class TelaMenuPrincipal extends JFrame {

    private Usuario usuarioLogado;
    private AnimalRepository animalRepo;
    private AdocaoRepository adocaoRepo;
    private SaudeRepository saudeRepo;
    private EventoRepository eventoRepo;
    private UsuarioRepository usuarioRepo;
    private Runnable aoDeslogar;

    // 3. O construtor agora recebe o adocaoRepo também
    public TelaMenuPrincipal(Usuario usuarioLogado, AnimalRepository animalRepo, AdocaoRepository adocaoRepo, SaudeRepository saudeRepo, EventoRepository eventoRepo, UsuarioRepository usuarioRepo, Runnable aoDeslogar) {
        this.usuarioLogado = usuarioLogado;
        this.animalRepo = animalRepo;
        this.adocaoRepo = adocaoRepo;
        this.saudeRepo = saudeRepo;
        this.eventoRepo = eventoRepo;
        this.usuarioRepo = usuarioRepo;
        this.aoDeslogar = aoDeslogar;

        setTitle("Sistema de Abrigos - Área do " + usuarioLogado.getTipoUsuario());
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel Superior
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(41, 128, 185));
        JLabel labelBemVindo = new JLabel("Bem-vindo(a), " + usuarioLogado.getNome());
        labelBemVindo.setForeground(Color.WHITE);
        labelBemVindo.setFont(new Font("Arial", Font.BOLD, 20));
        painelTopo.add(labelBemVindo);
        add(painelTopo, BorderLayout.NORTH);

        // Painel Central
        JPanel painelBotoes = new JPanel(new GridLayout(0, 2, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // --- BLOCO DO VOLUNTÁRIO ---
        if (usuarioLogado instanceof Voluntario) {
            // 4. Botão separado para ligar na TelaListaAnimais
            JButton btnVerAnimais = criarBotao("Visualizar Animais Disponíveis");
            btnVerAnimais.addActionListener(e -> {
                new TelaListaAnimais(usuarioLogado, animalRepo, adocaoRepo).setVisible(true);
            });
            painelBotoes.add(btnVerAnimais);

            JButton btnMinhasSolicitacoes = criarBotao("Consultar Minhas Solicitações");
            btnMinhasSolicitacoes.addActionListener(e -> {
                new TelaAdocoes(usuarioLogado, adocaoRepo).setVisible(true);
            });
            painelBotoes.add(btnMinhasSolicitacoes);
            JButton btnParticiparDeEventos = criarBotao("Participar de Eventos");
            btnParticiparDeEventos.addActionListener(e -> {
                new TelaEventos(usuarioLogado, eventoRepo).setVisible(true);
            });
            painelBotoes.add(btnParticiparDeEventos);
            JButton btnHistoricoParticipacoes = criarBotao("Historico de Participações");
            btnHistoricoParticipacoes.addActionListener(e -> {
                new TelaEventos(usuarioLogado, eventoRepo).setVisible(true);
            });
            painelBotoes.add(btnHistoricoParticipacoes);

        // --- BLOCO DO VETERINÁRIO ---
        } else if (usuarioLogado instanceof Veterinario) {
            // 5. Botão separado para ligar na TelaListaAnimais
            JButton btnConsultarAnimais = criarBotao("Consultar Animais");
            btnConsultarAnimais.addActionListener(e -> {
                new TelaListaAnimais(usuarioLogado, animalRepo, adocaoRepo).setVisible(true);
            });
            painelBotoes.add(btnConsultarAnimais);

            // ===== Botões Tela Saúde =====
            JButton btnRegistrarAtendimento = criarBotao("Registar Atendimento");
            btnRegistrarAtendimento.addActionListener(e -> {
                new TelaSaude(usuarioLogado, animalRepo, saudeRepo).setVisible(true);;
            });
            painelBotoes.add(btnRegistrarAtendimento);

            JButton btnAtualizarTratamento = criarBotao("Atualizar Atendimento");
            btnAtualizarTratamento.addActionListener(e -> {
                new TelaSaude(usuarioLogado, animalRepo, saudeRepo).setVisible(true);;
            });
            painelBotoes.add(btnAtualizarTratamento);

            JButton btnRegistrarVacinacao = criarBotao("Registrar vacinação");
            btnRegistrarVacinacao.addActionListener(e -> {
                new TelaSaude(usuarioLogado, animalRepo, saudeRepo).setVisible(true);;
            });
            painelBotoes.add(btnRegistrarVacinacao);

            JButton btnEmitirRelatorioMedico = criarBotao("Emitir Relatório Médico");
            btnEmitirRelatorioMedico.addActionListener(e -> {
                new TelaSaude(usuarioLogado, animalRepo, saudeRepo).setVisible(true);;
            });
            painelBotoes.add(btnEmitirRelatorioMedico);

        // --- BLOCO DO ADMINISTRADOR ---
        } else if (usuarioLogado instanceof Administrador) {
            JButton btnCadastrarAnimal = criarBotao("Cadastrar Animal");
            btnCadastrarAnimal.addActionListener(e -> {
                new TelaCadastroAnimal(animalRepo).setVisible(true);
            });
            painelBotoes.add(btnCadastrarAnimal);

            JButton btnListarAnimais = criarBotao("Listar / Remover Animais");
            btnListarAnimais.addActionListener(e -> {
                new TelaListaAnimais(usuarioLogado, animalRepo, adocaoRepo).setVisible(true);
            });
            painelBotoes.add(btnListarAnimais);

            JButton btnGerirUsuarios = criarBotao("Gerir Usuários");
            btnGerirUsuarios.addActionListener(e -> {
                new TelaGerirUsuarios(usuarioLogado, usuarioRepo).setVisible(true);
            });
            painelBotoes.add(btnGerirUsuarios);

            JButton btnAprovarAdocoes = criarBotao("Aprovar Adoções");
            btnAprovarAdocoes.addActionListener(e -> {
                new TelaAdocoes(usuarioLogado, adocaoRepo).setVisible(true);
            });
            painelBotoes.add(btnAprovarAdocoes);

            JButton btnGerirEventos = criarBotao("Gerir Eventos");
            btnGerirEventos.addActionListener(e -> {
                new TelaEventos(usuarioLogado, eventoRepo).setVisible(true);
            });
            painelBotoes.add(btnGerirEventos);

            JButton btnRelatoriosEstatisticas = criarBotao("Relatórios e Estatísticas");
            btnRelatoriosEstatisticas.addActionListener(e -> {
                new TelaRelatorios(animalRepo, adocaoRepo, usuarioRepo, eventoRepo).setVisible(true);
            });
            painelBotoes.add(btnRelatoriosEstatisticas);
        }

        // Botões Comuns
        JButton btnNotificacoes = criarBotao("Notificações");
        btnNotificacoes.addActionListener(e -> {
            java.util.List<String> lista = usuarioLogado.getNotificacoes();
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Você não possui novas notificações.", "Notificações", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder sb = new StringBuilder("=== SUAS NOTIFICAÇÕES ===\n\n");
                for (String msg : lista) {
                    sb.append("[!] ").append(msg).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Notificações", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        painelBotoes.add(btnNotificacoes);

        JButton btnSair = new JButton("Sair (Logout)");
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.BLACK);
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Sessão encerrada.");
            if(aoDeslogar != null){
                aoDeslogar.run(); // Exxecuta o abrirLogin dentro do Main
            }
        });
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.CENTER);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setBackground(new Color(236, 240, 241));
        return botao;
    }
}