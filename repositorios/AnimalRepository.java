package repositorios;

import modelo.Animal;
import modelo.StatusAdocao;
import excecoes.RegraNegocioException;

import java.util.ArrayList;
import java.util.List;

public class AnimalRepository {
    private List<Animal> animais;

    public AnimalRepository(){
        this.animais = new ArrayList<>();
    }

    public void cadastrar(Animal animal){
        if(animal != null){
            animais.add(animal);
        }
    }

    public List<Animal> listarTodos(){
        return animais;
    }

    public Animal buscarPorId(String id){
        for(Animal animal:animais){
            if (animal.getId().equals(id)) { // Vê se o ID pesquisa está dentro do ArrayList
                return animal; // Retorna o animal
            }
        }
        return null; // Caso não ache nenhum animal
    }

    public void remover(String id) throws RegraNegocioException{
        Animal animal = buscarPorId(id);

        if(animal == null){
            throw new RegraNegocioException("Animal não encontrado!");
        }
        if(animal.getStatus() == StatusAdocao.ADOTADO){
            throw new RegraNegocioException("Erro: Um animal adotado não pode ser removido.");
        }
    }
}
