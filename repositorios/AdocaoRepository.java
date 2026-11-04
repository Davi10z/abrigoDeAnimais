package repositorios;

// Importando classes e exceções
import modelo.Adocao;
import modelo.Animal;
import modelo.StatusAdocao;
import modelo.StatusSolicitacao;
import modelo.Usuario;
import modelo.Administrador;
import excecoes.RegraNegocioException;

// Importando bibliotecas
import java.util.ArrayList;
import java.util.List;

public class AdocaoRepository {
    private List<Adocao> adocoes;

    public AdocaoRepository(){
        this.adocoes = new ArrayList<>();
    }

    public void solicitarAdocao(Usuario adotante, Animal animal) throws RegraNegocioException {
        if(animal.getStatus() != StatusAdocao.DISPONIVEL){
            throw new RegraNegocioException("Erro: Solicitação inválida. O animal não está disponível para adoção.");
        }

        Adocao novaAdocao = new Adocao(adotante, animal);
        adocoes.add(novaAdocao);
    }
    
    public Adocao buscarPorId(String id){
        for(Adocao adocao:adocoes){
            if(adocao.getId().equals(id)){
                return adocao;
            }
        }
        return null;
    }

    public void aprovarAdocao(String idAdocao, Usuario usuarioLogado) throws RegraNegocioException{
        // Vê se não é adminstrador, porque apenas adminstradores podem aprovar adoções
        if(!(usuarioLogado instanceof Administrador)){ 
            throw new RegraNegocioException("Erro de Permissão: Apenas administradores podem aprovar adoções.");
        }
        Adocao adocaoEncontrada = buscarPorId(idAdocao);
        if(adocaoEncontrada == null){
            throw new RegraNegocioException("Solicitação de adoção não encontrada.");
        }
        if(adocaoEncontrada.getStatus() != StatusSolicitacao.PENDENTE){
            throw new RegraNegocioException("Esta solicitação já foi processada.");
        }

        // Atualiza a solicitação e o status do animal
        adocaoEncontrada.setStatus(StatusSolicitacao.APROVADA);
        adocaoEncontrada.getAnimal().setStatus(StatusAdocao.ADOTADO);
    }

    public List<Adocao> listarTodos(){
        return adocoes;
    }
}
