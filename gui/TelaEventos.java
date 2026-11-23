package gui;

import modelo.Administrador;
import modelo.Evento;
import modelo.Usuario;
import modelo.Voluntario;
import repositorios.EventoRepository;
import excecoes.RegraNegocioException;
import util.SistemaUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TelaEventos extends JFrame {

    private Usuario usuarioLogado;
    private EventoRepository eventoRepo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaEventos(Usuario usuarioLogado, EventoRepository eventoRepo) {
        this.usuarioLogado = usuarioLogado;
        this.eventoRepo = eventoRepo;

        setTitle(SistemaUtil.NOME_SISTEMA + " - Eventos e Campanhas");
        setSize(750, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Tabela de Eventos
        String[] colunas = {"ID", "Título", "Tipo", "Data", "Inscritos"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // 2. Painel de Botões
        JPanel painelBotoes = new JPanel();

        if (usuarioLogado instanceof Administrador) {
            JButton btnNovo = new JButton("Cadastrar Evento");
            btnNovo.setBackground(new Color(46, 204, 113));
            btnNovo.setForeground(Color.BLACK);
            btnNovo.addActionListener(e -> cadastrarEvento());
            painelBotoes.add(btnNovo);
        }

        if (usuarioLogado instanceof Voluntario) {
            JButton btnParticipar = new JButton("Participar do Evento");
            btnParticipar.setBackground(new Color(52, 152, 219));
            btnParticipar.setForeground(Color.BLACK);
            btnParticipar.addActionListener(e -> participarEvento());
            painelBotoes.add(btnParticipar);

            JButton btnHistorico = new JButton("Meu Histórico");
            btnHistorico.setBackground(new Color(155, 89, 182));
            btnHistorico.setForeground(Color.BLACK);
            btnHistorico.addActionListener(e -> verMeuHistorico());
            painelBotoes.add(btnHistorico);
        }

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Evento e : eventoRepo.listarEventos()) {
            modeloTabela.addRow(new Object[]{
                e.getId(),
                e.getTitulo(),
                e.getTipoEvento(),
                SistemaUtil.formatarData(e.getDataEvento()),
                e.getVoluntariosInscritos().size()
            });
        }
    }

    private void cadastrarEvento() {
        String titulo = JOptionPane.showInputDialog(this, "Título do evento:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        String[] tipos = {"Feira de Adoção", "Campanha de Vacinação", "Mutirão de Limpeza", "Evento Beneficente"};
        String tipo = (String) JOptionPane.showInputDialog(
            this, "Selecione o tipo:", "Tipo de Evento",
            JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]
        );
        if (tipo == null) return;

        String dataStr = JOptionPane.showInputDialog(this, "Data do evento (DD/MM/AAAA):");
        if (dataStr == null || dataStr.trim().isEmpty()) return;

        try {
            LocalDate data = LocalDate.parse(dataStr.trim(), SistemaUtil.FORMATO_DATA);
            Evento novo = new Evento(titulo.trim(), tipo, data);
            eventoRepo.cadastrarEvento(novo);
            JOptionPane.showMessageDialog(this, "Evento cadastrado com sucesso!");
            atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void participarEvento() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um evento na tabela primeiro.");
            return;
        }

        String idEvento = (String) modeloTabela.getValueAt(linha, 0);
        String tituloEvento = (String) modeloTabela.getValueAt(linha, 1);

        try {
            eventoRepo.inscreverVoluntario(idEvento, usuarioLogado);
            usuarioLogado.receberNotificacao("Inscrição confirmada no evento: " + tituloEvento);
            JOptionPane.showMessageDialog(this, "Inscrição realizada com sucesso!");
            atualizarTabela();
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verMeuHistorico() {
        StringBuilder sb = new StringBuilder("=== MEU HISTÓRICO DE PARTICIPAÇÕES ===\n\n");
        int count = 0;

        for (Evento e : eventoRepo.listarEventos()) {
            if (e.getVoluntariosInscritos().contains(usuarioLogado)) {
                sb.append("- ").append(e.getTitulo())
                  .append(" (").append(e.getTipoEvento()).append(") - ")
                  .append(SistemaUtil.formatarData(e.getDataEvento())).append("\n");
                count++;
            }
        }

        if (count == 0) {
            sb.append("Você ainda não participou de nenhum evento.");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Histórico", JOptionPane.INFORMATION_MESSAGE);
    }
}