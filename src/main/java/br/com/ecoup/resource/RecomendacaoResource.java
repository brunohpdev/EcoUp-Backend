package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.Recomendacao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoResource {
    private final EcoUpBusiness business;
    public RecomendacaoResource(EcoUpBusiness business) { this.business = business; }

    @GetMapping("/{id}")
    public Recomendacao buscar(@PathVariable int id) { return business.buscarRecomendacao(id); }

    @PutMapping("/{id}")
    public Recomendacao atualizar(@PathVariable int id, @RequestBody Recomendacao item) {
        return business.atualizarRecomendacao(id, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) { business.excluirRecomendacao(id); }
}
