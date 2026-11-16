package gui;

import modelo.Animal;
import modelo.StatusAdocao;
import repositorios.AnimalRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaCadastroAnimal extends JFrame {

    private JTextField campoNome, campoEspecie, campoRaca, campoIdade, campoPorte, campoSaude, campoData;
    private JComboBox<StatusAdocao> comboStatus;
    private AnimalRepository animalRepo;

    public TelaCadastroAnimal(AnimalRepository animalRepo) {
        this.animalRepo = animalRepo;

        setTitle("Cadastro de Animal Resgatado");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Título
        JLabel labelTitulo = new JLabel("Novo Registo de Animal", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(labelTitulo, BorderLayout.NORTH);

        // Formulário com os campos exigidos
        JPanel painelForm = new JPanel(new GridLayout(8, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        painelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelForm.add(campoNome);

        painelForm.add(new JLabel("Espécie (Ex: Cão, Gato):"));
        campoEspecie = new JTextField();
        painelForm.add(campoEspecie);

        painelForm.add(new JLabel("Raça:"));
        campoRaca = new JTextField();
        painelForm.add(campoRaca);

        painelForm.add(new JLabel("Idade (anos):"));
        campoIdade = new JTextField();
        painelForm.add(campoIdade);

        painelForm.add(new JLabel("Porte:"));
        campoPorte = new JTextField();
        painelForm.add(campoPorte);

        painelForm.add(new JLabel("Estado de Saúde:"));
        campoSaude = new JTextField();
        painelForm.add(campoSaude);

        painelForm.add(new JLabel("Data de Resgate (DD/MM/AAAA):"));
        campoData = new JTextField();
        painelForm.add(campoData);

        painelForm.add(new JLabel("Status de Adoção:"));
        comboStatus = new JComboBox<>(StatusAdocao.values()); // Usa o Enum diretamente
        painelForm.add(comboStatus);

        add(painelForm, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar Registo");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAnimal();
            }
        });

        btnCancelar.addActionListener(e -> dispose()); // Fecha apenas esta janela

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void salvarAnimal() {
        try {
            // Captura e converte os dados da interface
            String nome = campoNome.getText();
            String especie = campoEspecie.getText();
            String raca = campoRaca.getText();
            int idade = Integer.parseInt(campoIdade.getText());
            String porte = campoPorte.getText();
            String saude = campoSaude.getText();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataResgate = LocalDate.parse(campoData.getText(), formatter);
            
            StatusAdocao status = (StatusAdocao) comboStatus.getSelectedItem();

            // Instancia o objeto Animal e guarda no repositório
            Animal novoAnimal = new Animal(nome, especie, raca, idade, porte, saude, dataResgate, status);
            animalRepo.cadastrar(novoAnimal);

            JOptionPane.showMessageDialog(this, "Animal cadastrado com sucesso!");
            dispose(); // Fecha o formulário após salvar

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar: Verifique se a idade é um número e a data está no formato correto (DD/MM/AAAA).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }
}