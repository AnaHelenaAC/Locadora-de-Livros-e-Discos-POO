package br.edu.ufersa.locadora.model;

import br.edu.ufersa.locadora.model.Service.AluguelService;
import br.edu.ufersa.locadora.model.Service.ClienteService;
import br.edu.ufersa.locadora.model.Service.DiscoService;
import br.edu.ufersa.locadora.model.Service.LivroService;
import br.edu.ufersa.locadora.model.Service.RegistroService;
import br.edu.ufersa.locadora.model.Service.UsuarioService;
import br.edu.ufersa.locadora.model.entities.Usuario;

public final class SessaoUsuario {

    private static final SessaoUsuario INSTANCE = new SessaoUsuario();

    private Usuario usuarioLogado;

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final LivroService livroService;
    private final DiscoService discoService;
    private final AluguelService aluguelService;
    private final RegistroService registroService;

    private SessaoUsuario() {
        this.usuarioService = new UsuarioService();
        this.clienteService = new ClienteService();
        this.livroService = new LivroService(new br.edu.ufersa.locadora.model.DAO.LivroDAO());
        this.discoService = new DiscoService(new br.edu.ufersa.locadora.model.DAO.DiscoDAO());
        this.aluguelService = new AluguelService();
        this.registroService = new RegistroService();
    }

    public static SessaoUsuario getInstance() {
        return INSTANCE;
    }

    public Usuario getUsuario() {
        return usuarioLogado;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void limparSessao() {
        this.usuarioLogado = null;
    }

    public boolean possuiUsuarioLogado() {
        return usuarioLogado != null;
    }

    public boolean usuarioEhGerente() {
        return usuarioLogado != null && usuarioLogado.isGerente();
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public LivroService getLivroService() {
        return livroService;
    }

    public DiscoService getDiscoService() {
        return discoService;
    }

    public AluguelService getAluguelService() {
        return aluguelService;
    }

    public RegistroService getRegistroService() {
        return registroService;
    }
}