package gui;

import modelo.Adocao;
import modelo.Administrador;
import modelo.Usuario;
import repositorios.AdocaoRepository;
import excecoes.RegraNegocioException;
import util.SistemaUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaAdocoes extends JFrame {

    private Usuario usuarioLogado;
    private AdocaoRepository adocaoRepo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaAdocoes(Usuario usuarioLogado, AdocaoRepository adocaoRepo) {
        this.usuarioLogado = usuarioLogado;
        this.adocaoRepo = adocaoRepo;

        setTitle(SistemaUtil.NOME_SISTEMA + " - Gerenciamento de Adoções");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tabela
        String[] colunas = {"ID", "Adotante", "Animal", "Data", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel();

        if (usuarioLogado instanceof Administrador) {
            JButton btnAprovar = new JButton("Aprovar");
            btnAprovar.setBackground(new Color(46, 204, 113));
            btnAprovar.setForeground(Color.BLACK);
            btnAprovar.addActionListener(e -> processarAcao("APROVAR"));
            painelBotoes.add(btnAprovar);

            JButton btnRejeitar = new JButton("Rejeitar");
            btnRejeitar.setBackground(new Color(231, 76, 60));
            btnRejeitar.setForeground(Color.BLACK);
            btnRejeitar.addActionListener(e -> processarAcao("REJEITAR"));
            painelBotoes.add(btnRejeitar);

            JButton btnDevolver = new JButton("Registrar Devolução");
            btnDevolver.setBackground(new Color(243, 156, 18));
            btnDevolver.setForeground(Color.BLACK);
            btnDevolver.addActionListener(e -> processarAcao("DEVOLVER"));
            painelBotoes.add(btnDevolver);
        }

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        boolean isAdmin = usuarioLogado instanceof Administrador;

        for (Adocao a : adocaoRepo.listarTodos()) {
            // Admin vê tudo e o Voluntário vê apenas as suas solicitações
            if (isAdmin || a.getAdotante().getEmail().equals(usuarioLogado.getEmail())) {
                modeloTabela.addRow(new Object[]{
                    a.getId(),
                    a.getAdotante().getNome(),
                    a.getAnimal().getNome(),
                    SistemaUtil.formatarData(a.getDataSolicitacao()),
                    a.getStatus()
                });
            }
        }
    }

    private void processarAcao(String acao) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma solicitação na tabela.");
            return;
        }

        String idAdocao = (String) modeloTabela.getValueAt(linha, 0);
        try {
            if (acao.equals("APROVAR")) {
                adocaoRepo.aprovarAdocao(idAdocao, usuarioLogado);
                JOptionPane.showMessageDialog(this, "Adoção aprovada! O animal agora consta como ADOTADO.");
            } else if (acao.equals("REJEITAR")) {
                adocaoRepo.rejeitarAdocao(idAdocao, usuarioLogado);
                JOptionPane.showMessageDialog(this, "Solicitação rejeitada.");
            } else if (acao.equals("DEVOLVER")) {
                adocaoRepo.registrarDevolucao(idAdocao, usuarioLogado);
                JOptionPane.showMessageDialog(this, "Devolução registrada! O animal voltou a ficar DISPONÍVEL.");
            }
            atualizarTabela();
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}