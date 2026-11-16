package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton btnEntrar;  
    private JButton btnCadastrar;  

    public TelaLogin(){
        // Criando a janela.
        setTitle("Sistema de Abrigo - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela (O mais difícil!)
        setLayout(new BorderLayout());

        // Painel Superior
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(41, 128, 185));
        JLabel labelTitulo = new JLabel("Bem vindo ao S.A.A.S");
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTitulo.add(labelTitulo);

        // Formulário
        JPanel painelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        painelForm.add(new Label("Email:"));
        campoEmail = new JTextField();
        painelForm.add(campoEmail);

        painelForm.add(new Label("Senha:"));
        campoSenha = new JPasswordField();
        painelForm.add(campoSenha);

        JPanel painelBotoes = new JPanel();
        btnEntrar = new JButton("Entrar");
        btnCadastrar = new JButton("Cadastrar-se");

        btnEntrar.setBackground(new Color(46, 204, 113));
        btnEntrar.setForeground(Color.WHITE);
        btnCadastrar.setBackground(new Color(52, 73, 94));
        btnCadastrar.setForeground(Color.WHITE);

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnCadastrar);

        add(painelTitulo, BorderLayout.NORTH);
        add(painelForm, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Chama a função responsavel pelas ações dos botões
        configurarAcoes();
    }

    private void configurarAcoes(){
        btnEntrar.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e){
                // JOptionPane.showMessageDialog(null, "Acesso liberado!"); // Tenho que vêr melhor isso depois
            }
        });
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Carregando...");
            }
        });
    }

    public JTextField getCampoEmail() { return campoEmail; }
    public JPasswordField getCampoSenha() { return campoSenha; }
    public JButton getBtnEntrar() { return btnEntrar; }
}