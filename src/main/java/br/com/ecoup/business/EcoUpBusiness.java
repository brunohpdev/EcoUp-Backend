package br.com.ecoup.business;

import br.com.ecoup.dao.*;
import br.com.ecoup.model.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EcoUpBusiness {
    private final UsuarioDAO usuarios;
    private final CategoriaDAO categorias;
    private final NivelDAO niveis;
    private final MissaoDAO missoes;
    private final AvatarDAO avatares;
    private final RecomendacaoDAO recomendacoes;
    private final UsuarioMissaoDAO usuarioMissoes;
    private final UsuarioNivelDAO usuarioNiveis;
    private final BCryptPasswordEncoder senhas = new BCryptPasswordEncoder();

    public EcoUpBusiness(UsuarioDAO usuarios, CategoriaDAO categorias, NivelDAO niveis, MissaoDAO missoes,
                         AvatarDAO avatares, RecomendacaoDAO recomendacoes,
                         UsuarioMissaoDAO usuarioMissoes, UsuarioNivelDAO usuarioNiveis) {
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.niveis = niveis;
        this.missoes = missoes;
        this.avatares = avatares;
        this.recomendacoes = recomendacoes;
        this.usuarioMissoes = usuarioMissoes;
        this.usuarioNiveis = usuarioNiveis;
    }

    private void texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, campo + " obrigatorio.");
        }
    }

    private void naoEncontrado(String mensagem) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensagem);
    }

    // USUARIOS: cadastro, consulta, alteracao e exclusao.

    public Usuario buscarUsuario(int id) {
        Usuario usuario = usuarios.read(id);
        if (usuario == null) naoEncontrado("Usuario nao encontrado.");
        Nivel nivel = niveis.nivelParaPontos(usuario.getPontos());
        usuario.setNivel(nivel == null ? 1 : nivel.getNivel());
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = usuarios.readAll();
        for (Usuario usuario : lista) {
            Nivel nivel = niveis.nivelParaPontos(usuario.getPontos());
            usuario.setNivel(nivel == null ? 1 : nivel.getNivel());
        }
        return lista;
    }

    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        texto(usuario.getNome(), "Nome");
        texto(usuario.getEmail(), "Email");
        texto(usuario.getSenha(), "Senha");
        if (!usuario.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email invalido.");
        }
        // A senha nunca e gravada em texto puro no banco.
        Usuario novo = usuarios.create(usuario, senhas.encode(usuario.getSenha()));
        Nivel inicial = niveis.nivelParaPontos(0);
        if (inicial != null) usuarioNiveis.registrarSeNovo(novo.getId(), inicial.getId());
        return buscarUsuario(novo.getId());
    }

    @Transactional
    public Usuario atualizarUsuario(int id, Usuario dados) {
        buscarUsuario(id);
        texto(dados.getNome(), "Nome");
        texto(dados.getEmail(), "Email");
        if (!dados.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email invalido.");
        }
        usuarios.update(id, dados.getNome(), dados.getEmail());
        if (dados.getSenha() != null && !dados.getSenha().trim().isEmpty()) {
            usuarios.updateSenha(id, senhas.encode(dados.getSenha()));
        }
        return buscarUsuario(id);
    }

    @Transactional
    public void excluirUsuario(int id) {
        buscarUsuario(id);
        // As chaves estrangeiras do script original nao possuem ON DELETE CASCADE.
        avatares.deleteByUsuario(id);
        recomendacoes.deleteByUsuario(id);
        usuarioMissoes.deleteByUsuario(id);
        usuarioNiveis.deleteByUsuario(id);
        usuarios.delete(id);
    }

    // CATEGORIAS: organizam os tipos de missao.
    public Categoria buscarCategoria(int id) {
        Categoria categoria = categorias.read(id);
        if (categoria == null) naoEncontrado("Categoria nao encontrada.");
        return categoria;
    }

    public List<Categoria> listarCategorias() { return categorias.readAll(); }

    public Categoria criarCategoria(Categoria categoria) {
        texto(categoria.getNomeCategoria(), "Nome da categoria");
        return categorias.create(categoria);
    }

    public Categoria atualizarCategoria(int id, Categoria categoria) {
        buscarCategoria(id);
        texto(categoria.getNomeCategoria(), "Nome da categoria");
        categoria.setId(id);
        categorias.update(categoria);
        return categoria;
    }

    public void excluirCategoria(int id) {
        if (!categorias.delete(id)) naoEncontrado("Categoria nao encontrada.");
    }

    // NIVEIS: os pontos necessarios estao na tabela TB_NIVEL.
    public Nivel buscarNivel(int id) {
        Nivel nivel = niveis.read(id);
        if (nivel == null) naoEncontrado("Nivel nao encontrado.");
        return nivel;
    }

    public List<Nivel> listarNiveis() { return niveis.readAll(); }

    public Nivel criarNivel(Nivel nivel) {
        validarNivel(nivel);
        return niveis.create(nivel);
    }

    public Nivel atualizarNivel(int id, Nivel nivel) {
        buscarNivel(id);
        validarNivel(nivel);
        nivel.setId(id);
        niveis.update(nivel);
        return nivel;
    }

    public void excluirNivel(int id) {
        if (!niveis.delete(id)) naoEncontrado("Nivel nao encontrado.");
    }

    private void validarNivel(Nivel nivel) {
        if (nivel.getNivel() < 1 || nivel.getPontuacaoNecessaria() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel ou pontuacao invalida.");
        }
    }

    // MISSOES: cadastro e validacao das atividades sustentaveis.
    public Missao buscarMissao(int id) {
        Missao missao = missoes.read(id);
        if (missao == null) naoEncontrado("Missao nao encontrada.");
        return missao;
    }

    public List<Missao> listarMissoes() { return missoes.readAll(); }

    public Missao criarMissao(Missao missao) {
        validarMissao(missao);
        return missoes.create(missao);
    }

    public Missao atualizarMissao(int id, Missao missao) {
        buscarMissao(id);
        validarMissao(missao);
        missao.setId(id);
        missoes.update(missao);
        return buscarMissao(id);
    }

    @Transactional
    public void excluirMissao(int id) {
        buscarMissao(id);
        recomendacoes.deleteByMissao(id);
        usuarioMissoes.deleteByMissao(id);
        missoes.delete(id);
    }

    private void validarMissao(Missao missao) {
        texto(missao.getTitulo(), "Titulo");
        texto(missao.getDescricao(), "Descricao");
        texto(missao.getDificuldade(), "Dificuldade");
        buscarCategoria(missao.getIdCategoria());
        String dificuldade = missao.getDificuldade();
        if (!dificuldade.equals("FACIL") && !dificuldade.equals("MEDIA") && !dificuldade.equals("DIFICIL")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use FACIL, MEDIA ou DIFICIL.");
        }
        if (missao.getPontos() <= 0 || missao.getPontos() > 9999 || missao.getDataInicio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontos ou data de inicio invalida.");
        }
        if (missao.getDataFim() != null && missao.getDataFim().isBefore(missao.getDataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data final anterior a data inicial.");
        }
    }

    // AVATARES: cada usuario pode ter apenas um avatar.
    public Avatar buscarAvatar(int id) {
        Avatar avatar = avatares.read(id);
        if (avatar == null) naoEncontrado("Avatar nao encontrado.");
        return avatar;
    }

    public Avatar avatarDoUsuario(int usuarioId) {
        buscarUsuario(usuarioId);
        return avatares.readByUsuario(usuarioId);
    }

    public Avatar criarAvatar(int usuarioId, Avatar avatar) {
        buscarUsuario(usuarioId);
        texto(avatar.getNomeAvatar(), "Nome do avatar");
        texto(avatar.getTipoAvatar(), "Tipo do avatar");
        if (avatares.readByUsuario(usuarioId) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja possui avatar.");
        }
        avatar.setIdUsuario(usuarioId);
        return avatares.create(avatar);
    }

    public Avatar atualizarAvatar(int id, Avatar avatar) {
        Avatar atual = buscarAvatar(id);
        texto(avatar.getNomeAvatar(), "Nome do avatar");
        texto(avatar.getTipoAvatar(), "Tipo do avatar");
        avatar.setId(id);
        avatar.setIdUsuario(atual.getIdUsuario());
        avatares.update(avatar);
        return avatar;
    }

    public void excluirAvatar(int id) {
        if (!avatares.delete(id)) naoEncontrado("Avatar nao encontrado.");
    }

    // ANDAMENTO: o historico vem da tabela TB_USUARIO_MISSAO.
    public List<UsuarioMissao> listarMissoesUsuario(int usuarioId) {
        buscarUsuario(usuarioId);
        return usuarioMissoes.readByUsuario(usuarioId);
    }

    public List<UsuarioMissao> historico(int usuarioId) {
        buscarUsuario(usuarioId);
        return usuarioMissoes.readConcluidas(usuarioId);
    }

    public List<UsuarioNivel> historicoNiveis(int usuarioId) {
        buscarUsuario(usuarioId);
        return usuarioNiveis.readByUsuario(usuarioId);
    }

    @Transactional
    public UsuarioMissao iniciarMissao(int usuarioId, int missaoId) {
        buscarUsuario(usuarioId);
        buscarMissao(missaoId);
        if (usuarioMissoes.read(usuarioId, missaoId) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Missao ja registrada para usuario.");
        }
        usuarioMissoes.iniciar(usuarioId, missaoId);
        return usuarioMissoes.read(usuarioId, missaoId);
    }

    @Transactional
    public Usuario concluirMissao(int usuarioId, int missaoId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Missao missao = buscarMissao(missaoId);
        UsuarioMissao vinculo = usuarioMissoes.read(usuarioId, missaoId);
        if (vinculo != null && "CONCLUIDA".equals(vinculo.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Missao ja concluida.");
        }
        // Recompensa igual a pontos_recompensa; respeita a carga original do Oracle.
        int total = usuario.getPontos() + missao.getPontos();
        if (total > 999999) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pontuacao excede o limite do banco.");
        }
        usuarioMissoes.concluir(usuarioId, missaoId, vinculo != null);
        usuarios.atualizarPontos(usuarioId, total);
        Nivel nivel = niveis.nivelParaPontos(total);
        if (nivel != null) usuarioNiveis.registrarSeNovo(usuarioId, nivel.getId());
        return buscarUsuario(usuarioId);
    }

    @Transactional
    public void cancelarMissao(int usuarioId, int missaoId) {
        buscarUsuario(usuarioId);
        buscarMissao(missaoId);
        if (!usuarioMissoes.cancelar(usuarioId, missaoId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nao e possivel cancelar esta missao.");
        }
    }

    // RECOMENDACOES: cada recomendacao aponta para uma missao existente.
    public Recomendacao buscarRecomendacao(int id) {
        Recomendacao recomendacao = recomendacoes.read(id);
        if (recomendacao == null) naoEncontrado("Recomendacao nao encontrada.");
        return recomendacao;
    }

    public List<Recomendacao> listarRecomendacoes(int usuarioId) {
        buscarUsuario(usuarioId);
        return recomendacoes.readByUsuario(usuarioId);
    }

    public Recomendacao criarRecomendacao(int usuarioId, Recomendacao recomendacao) {
        buscarUsuario(usuarioId);
        buscarMissao(recomendacao.getIdMissao());
        texto(recomendacao.getMensagem(), "Mensagem");
        recomendacao.setIdUsuario(usuarioId);
        return recomendacoes.create(recomendacao);
    }

    public Recomendacao gerarRecomendacao(int usuarioId) {
        buscarUsuario(usuarioId);
        for (Missao missao : listarMissoes()) {
            UsuarioMissao vinculo = usuarioMissoes.read(usuarioId, missao.getId());
            if (vinculo == null || !"CONCLUIDA".equals(vinculo.getStatus())) {
                Recomendacao recomendacao = new Recomendacao();
                recomendacao.setIdMissao(missao.getId());
                recomendacao.setMensagem("Experimente a missao: " + missao.getTitulo());
                return criarRecomendacao(usuarioId, recomendacao);
            }
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Todas as missoes ja foram concluidas.");
    }

    public Recomendacao atualizarRecomendacao(int id, Recomendacao dados) {
        Recomendacao atual = buscarRecomendacao(id);
        buscarMissao(dados.getIdMissao());
        texto(dados.getMensagem(), "Mensagem");
        atual.setIdMissao(dados.getIdMissao());
        atual.setMensagem(dados.getMensagem());
        recomendacoes.update(atual);
        return buscarRecomendacao(id);
    }

    public void excluirRecomendacao(int id) {
        if (!recomendacoes.delete(id)) naoEncontrado("Recomendacao nao encontrada.");
    }

    public Usuario fazerLogin(String email, String senha) {

        if (email == null || senha == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha incorretos."
            );
        }

        Usuario usuario = usuarios.buscarPorEmail(email);

        String senhaBanco = usuarios.buscarSenhaPorEmail(email);

        if (usuario == null || senhaBanco == null ||
                !senhas.matches(senha, senhaBanco)) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha incorretos."
            );
        }

        return buscarUsuario(usuario.getId());
    }
}
