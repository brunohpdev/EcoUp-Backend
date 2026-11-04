package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.Nivel;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/niveis")
public class NivelResource {
    private final EcoUpBusiness business;
    public NivelResource(EcoUpBusiness business) { this.business = business; }

    @GetMapping
    public List<Nivel> listar() { return business.listarNiveis(); }

    @GetMapping("/{id}")
    public Nivel buscar(@PathVariable int id) { return business.buscarNivel(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nivel criar(@RequestBody Nivel nivel) { return business.criarNivel(nivel); }

    @PutMapping("/{id}")
    public Nivel atualizar(@PathVariable int id, @RequestBody Nivel nivel) {
        return business.atualizarNivel(id, nivel);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) { business.excluirNivel(id); }
}
