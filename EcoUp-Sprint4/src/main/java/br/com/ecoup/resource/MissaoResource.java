package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.Missao;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missoes")
public class MissaoResource {
    private final EcoUpBusiness business;
    public MissaoResource(EcoUpBusiness business) { this.business = business; }

    @GetMapping
    public List<Missao> listar() { return business.listarMissoes(); }

    @GetMapping("/{id}")
    public Missao buscar(@PathVariable int id) { return business.buscarMissao(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Missao criar(@RequestBody Missao missao) { return business.criarMissao(missao); }

    @PutMapping("/{id}")
    public Missao atualizar(@PathVariable int id, @RequestBody Missao missao) {
        return business.atualizarMissao(id, missao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) { business.excluirMissao(id); }
}
