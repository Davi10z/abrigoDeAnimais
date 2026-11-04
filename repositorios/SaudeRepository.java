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

    public SaudeRepository(){
        this.historicoVacinas = new HashMap<>();
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
}
