package repositorios;

import modelo.Usuario;
import modelo.Vacina;
import modelo.Veterinario;
import excecoes.RegraNegocioException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaudeRepository {
    // Mapa que relaciona o ID do Animal com a sua lista de vacinas
    private Map<String, List<Vacina>> historicoVacinas;
    private Map<String, List<String>> historicoAtendimentos;

    public SaudeRepository(){
        this.historicoVacinas = new HashMap<>();
        this.historicoAtendimentos = new HashMap<>();
    }

    public void registrarVacina(String idAnimal, Vacina novaVacina, Usuario usuarioLogado){
        if(!(usuarioLogado instanceof Veterinario)){
            throw new RegraNegocioException("Erro de Permissão: Apenas veterinários podem registar informações médicas.");
        }

        // Garante que o animal tem uma lista instanciada no mapa
        historicoVacinas.putIfAbsent(idAnimal, new ArrayList<>());
        List<Vacina> vacinasDoAnimal = historicoVacinas.get(idAnimal);

        // Verificação de duplicidade da mesma vacinação na mesma data.
        for(Vacina v:vacinasDoAnimal){
            if(v.getNome().equalsIgnoreCase(novaVacina.getNome()) && v.getDataAplicacao().equals(novaVacina.getDataAplicacao())){
                throw new RegraNegocioException("Erro: Não é permitido registar vacinação duplicada na mesma data para o mesmo animal.");
            }
        }

        vacinasDoAnimal.add(novaVacina);
    }

    public List<Vacina> consultarVacinas(String idAnimal){
        return historicoVacinas.getOrDefault(idAnimal, new ArrayList<>());
    }

    public void registrarAtendimento(String idAnimal, String descricao, Usuario usuarioLogado) throws RegraNegocioException {
        // Apenas veterinários podem alterar informações médicas
        if(!(usuarioLogado instanceof Veterinario)){
            throw new RegraNegocioException("Erro de Permissão: Apenas veterinários podem registrar atendimentos.");
        }
        historicoAtendimentos.putIfAbsent(idAnimal, new ArrayList<>()); // Verifica se o valor não existe ou não é nulo. Então add na ArrayList
        historicoAtendimentos.get(idAnimal).add(descricao);
    }

    public List<String> consultarAtendimentos(String idAnimal){
        return historicoAtendimentos.getOrDefault(idAnimal, new ArrayList<>()); // Se existir o valor retorna o valor, se não existir retorna um valor padrão.
    }
}

