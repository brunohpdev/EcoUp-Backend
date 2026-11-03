package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
    private final EcoUpBusiness business;
    public UsuarioResource(EcoUpBusiness business) { this.business = business; }

    @GetMapping
    public List<Usuario> listar() { return business.listarUsuarios(); }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable int id) { return business.buscarUsuario(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criar(@RequestBody Usuario usuario) { return business.criarUsuario(usuario); }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        return business.atualizarUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) { business.excluirUsuario(id); }

    @GetMapping("/{id}/nivel")
    public Map<String, Object> nivel(@PathVariable int id) {
        Usuario usuario = business.buscarUsuario(id);
        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("idUsuario", id);
        resposta.put("pontos", usuario.getPontos());
        resposta.put("nivel", usuario.getNivel());
        return resposta;
    }

    @GetMapping("/{id}/niveis")
    public List<UsuarioNivel> historicoNiveis(@PathVariable int id) { return business.historicoNiveis(id); }

    @GetMapping("/{id}/avatar")
    public Avatar avatar(@PathVariable int id) { return business.avatarDoUsuario(id); }

    @PostMapping("/{id}/avatar")
    @ResponseStatus(HttpStatus.CREATED)
    public Avatar criarAvatar(@PathVariable int id, @RequestBody Avatar avatar) {
        return business.criarAvatar(id, avatar);
    }

    @GetMapping("/{id}/missoes")
    public List<UsuarioMissao> missoes(@PathVariable int id) { return business.listarMissoesUsuario(id); }

    @GetMapping("/{id}/historico")
    public List<UsuarioMissao> historico(@PathVariable int id) { return business.historico(id); }

    @PostMapping("/{id}/missoes/{missaoId}/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioMissao iniciar(@PathVariable int id, @PathVariable int missaoId) {
        return business.iniciarMissao(id, missaoId);
    }

    @PostMapping("/{id}/missoes/{missaoId}/concluir")
    public Usuario concluir(@PathVariable int id, @PathVariable int missaoId) {
        return business.concluirMissao(id, missaoId);
    }

    @PatchMapping("/{id}/missoes/{missaoId}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable int id, @PathVariable int missaoId) {
        business.cancelarMissao(id, missaoId);
    }

    @GetMapping("/{id}/recomendacoes")
    public List<Recomendacao> recomendacoes(@PathVariable int id) { return business.listarRecomendacoes(id); }

    @PostMapping("/{id}/recomendacoes")
    @ResponseStatus(HttpStatus.CREATED)
    public Recomendacao criarRecomendacao(@PathVariable int id, @RequestBody Recomendacao item) {
        return business.criarRecomendacao(id, item);
    }

    @PostMapping("/{id}/recomendacoes/gerar")
    @ResponseStatus(HttpStatus.CREATED)
    public Recomendacao gerar(@PathVariable int id) { return business.gerarRecomendacao(id); }
}
