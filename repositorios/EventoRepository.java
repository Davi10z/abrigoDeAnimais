package repositorios;

import modelo.Evento;
import modelo.Usuario;
import modelo.Voluntario;
import excecoes.RegraNegocioException;

import java.util.ArrayList;
import java.util.List;

public class EventoRepository {
    private List<Evento> eventos;
    
    public EventoRepository(){
        this.eventos = new ArrayList<>();
    }

    public void cadastrarEvento(Evento evento){
        eventos.add(evento);
    }

    public List<Evento> listarEventos(){
        return eventos;
    } 

    public void inscreverVoluntario(String idEvento, Usuario usuarioLogado) throws RegraNegocioException {
        if(!(usuarioLogado instanceof Voluntario)){
            throw new RegraNegocioException("Erro: Apenas voluntários podem inscrever-se nas atividades de apoio.");
        }
        for(Evento evento : eventos){
            if(evento.getId().equals(idEvento)){
                evento.adicionarVoluntario(usuarioLogado);
                return;
            }
        }
        throw new RegraNegocioException("Evento não encontrado.");
    }
}
