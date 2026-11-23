# 🐾 Abrigo Seguro — Sistema de Gestão de Abrigo de Animais

Sistema desktop desenvolvido em **Java (Swing)** aplicando os pilares da **Programação Orientada a Objetos (POO)** para gerenciar o ciclo completo de um abrigo de animais: resgate, controle veterinário, adoções, eventos, notificações e relatórios estatísticos.

---

## ✨ Funcionalidades por Perfil

O sistema utiliza menus dinâmicos que se adaptam automaticamente ao perfil autenticado:

### 👤 Voluntário
- Visualizar animais disponíveis com busca por nome, filtro por espécie e visualização de detalhes.
- Solicitar adoção de animais disponíveis e acompanhar o status das próprias solicitações.
- Inscrever-se em eventos/campanhas e consultar o histórico de participações.
- Receber notificações sobre adoções e eventos.

### 🩺 Veterinário
- Consultar animais cadastrados no abrigo.
- Registrar vacinações (com trava contra vacinas duplicadas na mesma data).
- Registrar consultas, medicamentos e tratamentos, gerando relatório médico completo.
- Atualizar o estado de saúde e colocar animais `EM_TRATAMENTO` (bloqueando adoções temporariamente).

### 🛠️ Administrador
- Cadastrar, listar e remover animais.
- Gerenciar usuários (cadastrar e remover perfis, com proteção contra auto-remoção).
- Aprovar ou rejeitar solicitações de adoção e registrar devoluções (retornando o animal para `DISPONIVEL`).
- Cadastrar eventos e campanhas.
- Acessar **Relatórios e Estatísticas** (total de resgates, taxa de adoção, contagem por espécie, animais em tratamento, usuários ativos e contador de logins).

---

## 🧠 Conceitos de POO Aplicados

| Conceito | Implementação no Projeto |
| :--- | :--- |
| **Abstração e Encapsulamento** | Classe abstrata `Usuario`, atributos `private` com getters/setters e geração automática de IDs via `UUID` em `Animal`. |
| **Herança** | `Administrador`, `Veterinario` (com atributo `crmv`) e `Voluntario` estendem `Usuario`. |
| **Polimorfismo** | Armazenamento unificado em `List<Usuario>`, sobrescrita de `getTipoUsuario()` e construção dinâmica da `TelaMenuPrincipal`. |
| **Interfaces** | Interface `Notificavel` implementada por `Usuario`, garantindo o sistema de notificações para todos os perfis. |
| **Membros Estáticos** | Classe `SistemaUtil` com constantes (`NOME_SISTEMA`, `FORMATO_DATA`), atributo compartilhado (`totalLogins`) e métodos utilitários. |
| **Exceções Personalizadas** | `RegraNegocioException` lançada nos repositórios para validar regras de negócio e tratada na interface gráfica. |

---

## 📂 Arquitetura do Projeto

```text
src/
├── Main.java                 # Ponto de entrada, dados iniciais e ciclo de login/logout
├── excecoes/
│   └── RegraNegocioException.java
├── gui/
│   ├── TelaLogin.java
│   ├── TelaCadastroUsuario.java
│   ├── TelaMenuPrincipal.java
│   ├── TelaCadastroAnimal.java
│   ├── TelaListaAnimais.java
│   ├── TelaAdocoes.java
│   ├── TelaSaude.java
│   ├── TelaEventos.java
│   ├── TelaGerirUsuarios.java
│   └── TelaRelatorios.java
├── modelo/
│   ├── Usuario.java          # Classe abstrata (implements Notificavel)
│   ├── Notificavel.java      # Interface
│   ├── Administrador.java
│   ├── Veterinario.java
│   ├── Voluntario.java
│   ├── Animal.java
│   ├── Adocao.java
│   ├── Vacina.java
│   ├── Evento.java
│   ├── StatusAdocao.java     # Enum
│   └── StatusSolicitacao.java # Enum
├── repositorios/
│   ├── UsuarioRepository.java
│   ├── AnimalRepository.java
│   ├── AdocaoRepository.java
│   ├── SaudeRepository.java
│   └── EventoRepository.java
└── util/
    └── SistemaUtil.java      # Constantes e métodos estáticos
