package gui;

import modelo.Animal;
import modelo.Usuario;
import modelo.Administrador;
import repositorios.AnimalRepository;
import repositorios.AdocaoRepository;
import excecoes.RegraNegocioException;
import util.SistemaUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaListaAnimais extends JFrame {

    private AnimalRepository animalRepo;
    private AdocaoRepository adocaoRepo;
    private Usuario usuarioLogado;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField campoBusca;
    private JComboBox<String> comboEspecie;

    public TelaListaAnimais(Usuario usuarioLogado, AnimalRepository animalRepo, AdocaoRepository adocaoRepo) {
        this.usuarioLogado = usuarioLogado;
        this.animalRepo = animalRepo;
        this.adocaoRepo = adocaoRepo;

        setTitle(SistemaUtil.NOME_SISTEMA + " - Animais");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Painel Superior: Busca e Filtro por Espécie
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelFiltros.add(new JLabel("Pesquisar Nome:"));
        campoBusca = new JTextField(15);
        painelFiltros.add(campoBusca);

        painelFiltros.add(new JLabel("Espécie:"));
        comboEspecie = new JComboBox<>(new String[]{"Todas", "Cão", "Gato", "Outros"});
        painelFiltros.add(comboEspecie);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> atualizarTabela());
        painelFiltros.add(btnFiltrar);

        add(painelFiltros, BorderLayout.NORTH);

        // 2. Painel Central: Tabela de Animais
        String[] colunas = {"ID", "Nome", "Espécie", "Raça", "Idade", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta na célula
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // 3. Painel Inferior: Botões de Ação
        JPanel painelBotoes = new JPanel();

        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.addActionListener(e -> verDetalhes());
        painelBotoes.add(btnDetalhes);

        JButton btnAdotar = new JButton("Solicitar Adoção");
        btnAdotar.setBackground(new Color(46, 204, 113));
        btnAdotar.setForeground(Color.BLACK);
        btnAdotar.addActionListener(e -> solicitarAdocao());
        painelBotoes.add(btnAdotar);

        // Apenas o Administrador vê o botão de remover
        if (usuarioLogado instanceof Administrador) {
            JButton btnRemover = new JButton("Remover Animal");
            btnRemover.setBackground(new Color(231, 76, 60));
            btnRemover.setForeground(Color.BLACK);
            btnRemover.addActionListener(e -> removerAnimal());
            painelBotoes.add(btnRemover);
        }

        JButton btnFechar = new JButton("Voltar");
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);

        // Carrega os dados iniciais
        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0); // Limpa a tabela
        String termo = campoBusca.getText().trim().toLowerCase();
        String especieFiltro = (String) comboEspecie.getSelectedItem();

        List<Animal> lista = animalRepo.listarTodos();
        for (Animal a : lista) {
            boolean bateNome = termo.isEmpty() || a.getNome().toLowerCase().contains(termo);
            boolean bateEspecie = especieFiltro.equals("Todas") || a.getEspecie().equalsIgnoreCase(especieFiltro);

            if (bateNome && bateEspecie) {
                modeloTabela.addRow(new Object[]{
                    a.getId(), a.getNome(), a.getEspecie(), a.getRaca(), a.getIdade() + " anos", a.getStatus()
                });
            }
        }
    }

    private Animal getAnimalSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um animal na tabela primeiro.");
            return null;
        }
        String id = (String) modeloTabela.getValueAt(linha, 0);
        return animalRepo.buscarPorId(id);
    }

    private void verDetalhes() {
        Animal a = getAnimalSelecionado();
        if (a == null) return;

        String info = "Nome: " + a.getNome() +
                      "\nEspécie: " + a.getEspecie() +
                      "\nRaça: " + a.getRaca() +
                      "\nIdade: " + a.getIdade() + " anos" +
                      "\nPorte: " + a.getPorte() +
                      "\nEstado de Saúde: " + a.getEstadoSaude() +
                      "\nData de Resgate: " + SistemaUtil.formatarData(a.getDataResgate()) +
                      "\nStatus: " + a.getStatus();

        JOptionPane.showMessageDialog(this, info, "Detalhes do Animal", JOptionPane.INFORMATION_MESSAGE);
    }

    private void solicitarAdocao() {
        Animal a = getAnimalSelecionado();
        if (a == null) return;

        try {
            adocaoRepo.solicitarAdocao(usuarioLogado, a);
            usuarioLogado.receberNotificacao("Solicitação de adoção enviada para: " + a.getNome());
            JOptionPane.showMessageDialog(this, "Solicitação de adoção registrada com sucesso!");
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Adoção Negada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerAnimal() {
        Animal a = getAnimalSelecionado();
        if (a == null) return;

        try {
            animalRepo.remover(a.getId());
            JOptionPane.showMessageDialog(this, "Animal removido com sucesso.");
            atualizarTabela();
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Remover", JOptionPane.ERROR_MESSAGE);
        }
    }
}