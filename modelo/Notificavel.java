package modelo;

import java.util.List;

public interface Notificavel {
    void receberNotificacao(String mensagem);
    List<String> getNotificacoes();
}