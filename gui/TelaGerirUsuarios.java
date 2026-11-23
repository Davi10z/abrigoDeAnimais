package gui;

import modelo.Usuario;
import repositorios.UsuarioRepository;
import util.SistemaUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaGerirUsuarios extends JFrame {

    private Usuario usuarioLogado;
    private UsuarioRepository usuarioRepo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaGerirUsuarios(Usuario usuarioLogado, UsuarioRepository usuarioRepo) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioRepo = usuarioRepo;

        setTitle(SistemaUtil.NOME_SISTEMA + " - Gerenciar Usuários");
        setSize(650, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] colunas = {"Nome", "E-mail", "Perfil"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();

        JButton btnNovo = new JButton("Novo Usuário");
        btnNovo.setBackground(new Color(46, 204, 113));
        btnNovo.setForeground(Color.BLACK);
        btnNovo.addActionListener(e -> {
            TelaCadastroUsuario telaCad = new TelaCadastroUsuario(usuarioRepo);
            telaCad.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    atualizarTabela();
                }
            });
            telaCad.setVisible(true);
        });
        painelBotoes.add(btnNovo);

        JButton btnRemover = new JButton("Remover Selecionado");
        btnRemover.setBackground(new Color(231, 76, 60));
        btnRemover.setForeground(Color.BLACK);
        btnRemover.addActionListener(e -> removerUsuario());
        painelBotoes.add(btnRemover);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Usuario u : usuarioRepo.listarTodos()) {
            modeloTabela.addRow(new Object[]{u.getNome(), u.getEmail(), u.getTipoUsuario()});
        }
    }

    private void removerUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.");
            return;
        }

        String email = (String) modeloTabela.getValueAt(linha, 1);
        if (email.equalsIgnoreCase(usuarioLogado.getEmail())) {
            JOptionPane.showMessageDialog(this, "Você não pode remover o próprio usuário logado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioRepo.removerPorEmail(email);
        JOptionPane.showMessageDialog(this, "Usuário removido com sucesso.");
        atualizarTabela();
    }
}