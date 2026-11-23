package gui;

import modelo.Animal;
import modelo.Adocao;
import modelo.StatusAdocao;
import modelo.StatusSolicitacao;
import modelo.Usuario;
import modelo.Veterinario;
import modelo.Voluntario;
import repositorios.AdocaoRepository;
import repositorios.AnimalRepository;
import repositorios.EventoRepository;
import repositorios.UsuarioRepository;
import util.SistemaUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaRelatorios extends JFrame {

    public TelaRelatorios(AnimalRepository animalRepo, AdocaoRepository adocaoRepo, UsuarioRepository usuarioRepo, EventoRepository eventoRepo) {

        setTitle(SistemaUtil.NOME_SISTEMA + " - Relatórios e Estatísticas");
        setSize(600, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JTextArea areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaRelatorio.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaRelatorio.setText(gerarTextoRelatorio(animalRepo, adocaoRepo, usuarioRepo, eventoRepo));
        add(new JScrollPane(areaRelatorio), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private String gerarTextoRelatorio(AnimalRepository animalRepo, AdocaoRepository adocaoRepo, UsuarioRepository usuarioRepo, EventoRepository eventoRepo) {

        List<Animal> animais = animalRepo.listarTodos();
        List<Adocao> adocoes = adocaoRepo.listarTodos();
        List<Usuario> usuarios = usuarioRepo.listarTodos();

        int totalAnimais = animais.size();
        int emTratamento = 0;
        Map<String, Integer> contagemEspecies = new HashMap<>();

        for (Animal a : animais) {
            if (a.getStatus() == StatusAdocao.EM_TRATAMENTO) {
                emTratamento++;
            }
            String esp = a.getEspecie();
            contagemEspecies.put(esp, contagemEspecies.getOrDefault(esp, 0) + 1);
        }

        int totalAdocoesAprovadas = 0;
        for (Adocao ad : adocoes) {
            if (ad.getStatus() == StatusSolicitacao.APROVADA) {
                totalAdocoesAprovadas++;
            }
        }

        int voluntariosAtivos = 0;
        int veterinariosAtivos = 0;
        for (Usuario u : usuarios) {
            if (u instanceof Voluntario) voluntariosAtivos++;
            else if (u instanceof Veterinario) veterinariosAtivos++;
        }

        double taxaAdocao = (totalAnimais > 0) ? ((double) totalAdocoesAprovadas / totalAnimais) * 100 : 0.0;

        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("         ESTATÍSTICAS GERAIS DO ABRIGO     \n");
        sb.append("===========================================\n\n");
        sb.append("Animais Resgatados:      ").append(totalAnimais).append("\n");
        sb.append("Adoções Realizadas:      ").append(totalAdocoesAprovadas).append("\n");
        sb.append(String.format("Taxa de Adoção:          %.1f%%\n", taxaAdocao));
        sb.append("Animais em Tratamento:   ").append(emTratamento).append("\n");
        sb.append("Voluntários Ativos:      ").append(voluntariosAtivos).append("\n");
        sb.append("Veterinários Ativos:     ").append(veterinariosAtivos).append("\n");
        sb.append("Eventos Cadastrados:     ").append(eventoRepo.listarEventos().size()).append("\n");
        sb.append("Total de Logins (Sessão):").append(SistemaUtil.getTotalLogins()).append("\n\n");

        sb.append("--- ESPÉCIES RESGATADAS ---\n");
        if (contagemEspecies.isEmpty()) {
            sb.append("Nenhum animal cadastrado.\n");
        } else {
            for (Map.Entry<String, Integer> entry : contagemEspecies.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        sb.append("\n--- ANIMAIS EM TRATAMENTO ---\n");
        if (emTratamento == 0) {
            sb.append("Nenhum animal em tratamento no momento.\n");
        } else {
            for (Animal a : animais) {
                if (a.getStatus() == StatusAdocao.EM_TRATAMENTO) {
                    sb.append("- ").append(a.getNome()).append(" (").append(a.getEstadoSaude()).append(")\n");
                }
            }
        }

        return sb.toString();
    }
}