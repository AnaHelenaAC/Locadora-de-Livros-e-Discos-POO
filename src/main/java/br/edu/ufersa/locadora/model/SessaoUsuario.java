package br.edu.ufersa.locadora.model;

import br.edu.ufersa.locadora.model.DAO.ConnectionFactory;
import br.edu.ufersa.locadora.model.DAO.DiscoDAO;
import br.edu.ufersa.locadora.model.DAO.LivroDAO;
import br.edu.ufersa.locadora.model.Service.AluguelService;
import br.edu.ufersa.locadora.model.Service.ClienteService;
import br.edu.ufersa.locadora.model.Service.DiscoService;
import br.edu.ufersa.locadora.model.Service.LivroService;
import br.edu.ufersa.locadora.model.Service.RegistroService;
import br.edu.ufersa.locadora.model.Service.UsuarioService;
import br.edu.ufersa.locadora.model.entities.Usuario;

public final class SessaoUsuario {

    private static SessaoUsuario INSTANCE;

    private Usuario usuarioLogado;
    private final ConnectionFactory connectionFactory;

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final LivroService livroService;
    private final DiscoService discoService;
    private final AluguelService aluguelService;
    private final RegistroService registroService;

    private SessaoUsuario(ConnectionFactory factory) {
        this.connectionFactory = factory;
        this.usuarioService    = new UsuarioService(connectionFactory);
        this.clienteService    = new ClienteService(connectionFactory);
        this.livroService      = new LivroService(new LivroDAO(connectionFactory));
        this.discoService      = new DiscoService(new DiscoDAO(connectionFactory));
        this.aluguelService    = new AluguelService(connectionFactory);
        this.registroService   = new RegistroService(connectionFactory);
    }

    public static void configurar(ConnectionFactory factory) {
        if (INSTANCE != null) {
            throw new IllegalStateException("SessaoUsuario já foi configurada.");
        }
        INSTANCE = new SessaoUsuario(factory);
    }

    public static SessaoUsuario getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("SessaoUsuario não foi configurada. Chame configurar() no Main.");
        }
        return INSTANCE;
    }

    public ConnectionFactory getConnectionFactory() { return connectionFactory; }

    public Usuario getUsuario()                     { return usuarioLogado; }
    public Usuario getUsuarioLogado()               { return usuarioLogado; }
    public void setUsuario(Usuario usuario)         { this.usuarioLogado = usuario; }
    public void setUsuarioLogado(Usuario usuario)   { this.usuarioLogado = usuario; }

    public void limparSessao()                      { this.usuarioLogado = null; }
    public boolean possuiUsuarioLogado()            { return usuarioLogado != null; }
    public boolean usuarioEhGerente()               { return usuarioLogado != null && usuarioLogado.isGerente(); }

    public UsuarioService getUsuarioService()       { return usuarioService; }
    public ClienteService getClienteService()       { return clienteService; }
    public LivroService getLivroService()           { return livroService; }
    public DiscoService getDiscoService()           { return discoService; }
    public AluguelService getAluguelService()       { return aluguelService; }
    public RegistroService getRegistroService()     { return registroService; }
}