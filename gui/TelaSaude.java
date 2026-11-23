package gui;

import modelo.Animal;
import modelo.StatusAdocao;
import modelo.Usuario;
import modelo.Vacina;
import repositorios.AnimalRepository;
import repositorios.SaudeRepository;
import excecoes.RegraNegocioException;
import util.SistemaUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TelaSaude extends JFrame {

    private Usuario usuarioLogado;
    private AnimalRepository animalRepo;
    private SaudeRepository saudeRepo;

    private JComboBox<Animal> comboAnimais;
    private JTextArea areaHistorico;

    public TelaSaude(Usuario usuarioLogado, AnimalRepository animalRepo, SaudeRepository saudeRepo) {
        this.usuarioLogado = usuarioLogado;
        this.animalRepo = animalRepo;
        this.saudeRepo = saudeRepo;

        setTitle(SistemaUtil.NOME_SISTEMA + " - Controle de Saúde");
        setSize(700, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Topo: Seleção do Animal
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelTopo.add(new JLabel("Selecione o Animal:"));

        comboAnimais = new JComboBox<>();
        for (Animal a : animalRepo.listarTodos()) {
            comboAnimais.addItem(a);
        }
        // Mostra apenas o nome e espécie no ComboBox
        comboAnimais.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Animal) {
                    Animal a = (Animal) value;
                    setText(a.getNome() + " (" + a.getEspecie() + " - " + a.getStatus() + ")");
                }
                return this;
            }
        });
        comboAnimais.addActionListener(e -> atualizarHistorico());
        painelTopo.add(comboAnimais);

        add(painelTopo, BorderLayout.NORTH);

        // Centro: Histórico Médico / Relatório de Saúde
        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaHistorico.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(areaHistorico), BorderLayout.CENTER);

        // Rodapé: Botões de Ação Médica
        JPanel painelBotoes = new JPanel();

        JButton btnVacina = new JButton("Registrar Vacina");
        btnVacina.setBackground(new Color(46, 204, 113));
        btnVacina.setForeground(Color.BLACK);
        btnVacina.addActionListener(e -> registrarVacina());
        painelBotoes.add(btnVacina);

        JButton btnAtendimento = new JButton("Registrar Consulta/Tratamento");
        btnAtendimento.setBackground(new Color(52, 152, 219));
        btnAtendimento.setForeground(Color.BLACK);
        btnAtendimento.addActionListener(e -> registrarAtendimento());
        painelBotoes.add(btnAtendimento);

        JButton btnStatus = new JButton("Atualizar Estado de Saúde");
        btnStatus.setBackground(new Color(243, 156, 18));
        btnStatus.setForeground(Color.BLACK);
        btnStatus.addActionListener(e -> atualizarEstadoSaude());
        painelBotoes.add(btnStatus);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarHistorico();
    }

    private void atualizarHistorico() {
        Animal a = (Animal) comboAnimais.getSelectedItem();
        if (a == null) {
            areaHistorico.setText("Nenhum animal cadastrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO MÉDICO ===\n");
        sb.append("Animal: ").append(a.getNome()).append(" | Espécie: ").append(a.getEspecie()).append("\n");
        sb.append("Estado de Saúde: ").append(a.getEstadoSaude()).append("\n");
        sb.append("Status Atual: ").append(a.getStatus()).append("\n\n");

        sb.append("--- VACINAS APLICADAS ---\n");
        List<Vacina> vacinas = saudeRepo.consultarVacinas(a.getId());
        if (vacinas.isEmpty()) {
            sb.append("Nenhuma vacina registrada.\n");
        } else {
            for (Vacina v : vacinas) {
                sb.append("- ").append(v.getNome()).append(" (").append(SistemaUtil.formatarData(v.getDataAplicacao())).append(")\n");
            }
        }

        sb.append("\n--- CONSULTAS, MEDICAMENTOS E TRATAMENTOS ---\n");
        List<String> atendimentos = saudeRepo.consultarAtendimentos(a.getId());
        if (atendimentos.isEmpty()) {
            sb.append("Nenhum atendimento registrado.\n");
        } else {
            for (String at : atendimentos) {
                sb.append("- ").append(at).append("\n");
            }
        }

        areaHistorico.setText(sb.toString());
    }

    private void registrarVacina() {
        Animal a = (Animal) comboAnimais.getSelectedItem();
        if (a == null) return;

        String nomeVacina = JOptionPane.showInputDialog(this, "Nome da vacina (ex: Antirrábica, V10):");
        if (nomeVacina == null || nomeVacina.trim().isEmpty()) return;

        try {
            Vacina novaVacina = new Vacina(nomeVacina.trim(), LocalDate.now());
            saudeRepo.registrarVacina(a.getId(), novaVacina, usuarioLogado);
            JOptionPane.showMessageDialog(this, "Vacina registrada com sucesso!");
            atualizarHistorico();
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de Negócio", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAtendimento() {
        Animal a = (Animal) comboAnimais.getSelectedItem();
        if (a == null) return;

        String desc = JOptionPane.showInputDialog(this, "Descreva a consulta, medicamento ou cirurgia:");
        if (desc == null || desc.trim().isEmpty()) return;

        try {
            String registro = "[" + SistemaUtil.formatarData(LocalDate.now()) + "] " + desc.trim();
            saudeRepo.registrarAtendimento(a.getId(), registro, usuarioLogado);
            JOptionPane.showMessageDialog(this, "Atendimento registrado!");
            atualizarHistorico();
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarEstadoSaude() {
        Animal a = (Animal) comboAnimais.getSelectedItem();
        if (a == null) return;

        String novoEstado = JOptionPane.showInputDialog(this, "Novo estado de saúde:", a.getEstadoSaude());
        if (novoEstado != null && !novoEstado.trim().isEmpty()) {
            a.setEstadoSaude(novoEstado.trim());
        }

        // Animais em tratamento crítico não podem ficar disponíveis para adoção
        int critico = JOptionPane.showConfirmDialog(this, "O animal precisa ficar EM TRATAMENTO (indisponível para adoção)?", "Status", JOptionPane.YES_NO_OPTION);
        if (critico == JOptionPane.YES_OPTION) {
            a.setStatus(StatusAdocao.EM_TRATAMENTO);
        } else if (a.getStatus() == StatusAdocao.EM_TRATAMENTO) {
            a.setStatus(StatusAdocao.DISPONIVEL);
        }

        atualizarHistorico();
        comboAnimais.repaint();
    }
}