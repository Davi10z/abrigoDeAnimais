package gui;

import modelo.Veterinario;
import modelo.Voluntario;
import repositorios.UsuarioRepository;
import util.SistemaUtil;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroUsuario extends JFrame {

    private UsuarioRepository usuarioRepo;
    private JTextField campoNome, campoEmail, campoCrmv;
    private JPasswordField campoSenha;
    private JComboBox<String> comboTipo;
    private JLabel labelCrmv;

    public TelaCadastroUsuario(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;

        setTitle(SistemaUtil.NOME_SISTEMA + " - Novo Cadastro");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        painelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelForm.add(campoNome);

        painelForm.add(new JLabel("E-mail:"));
        campoEmail = new JTextField();
        painelForm.add(campoEmail);

        painelForm.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painelForm.add(campoSenha);

        painelForm.add(new JLabel("Perfil:"));
        comboTipo = new JComboBox<>(new String[]{"Voluntário", "Veterinário"});
        painelForm.add(comboTipo);

        labelCrmv = new JLabel("CRMV:");
        campoCrmv = new JTextField();
        labelCrmv.setVisible(false);
        campoCrmv.setVisible(false);
        painelForm.add(labelCrmv);
        painelForm.add(campoCrmv);

        // Só mostra o campo CRMV se escolher Veterinário
        comboTipo.addActionListener(e -> {
            boolean isVet = comboTipo.getSelectedItem().equals("Veterinário");
            labelCrmv.setVisible(isVet);
            campoCrmv.setVisible(isVet);
        });

        add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Cadastrar");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.addActionListener(e -> salvarUsuario());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void salvarUsuario() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();
        String tipo = (String) comboTipo.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioRepo.emailExiste(email)) {
            JOptionPane.showMessageDialog(this, "Já existe um usuário cadastrado com este e-mail.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipo.equals("Veterinário")) {
            String crmv = campoCrmv.getText().trim();
            if (crmv.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o CRMV do veterinário.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            usuarioRepo.cadastrar(new Veterinario(nome, email, senha, crmv));
        } else {
            usuarioRepo.cadastrar(new Voluntario(nome, email, senha));
        }

        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso! Já pode fazer login.");
        dispose();
    }
}