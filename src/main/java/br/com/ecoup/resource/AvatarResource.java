package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.Avatar;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avatares")
public class AvatarResource {
    private final EcoUpBusiness business;
    public AvatarResource(EcoUpBusiness business) { this.business = business; }

    @GetMapping("/{id}")
    public Avatar buscar(@PathVariable int id) { return business.buscarAvatar(id); }

    @PutMapping("/{id}")
    public Avatar atualizar(@PathVariable int id, @RequestBody Avatar avatar) {
        return business.atualizarAvatar(id, avatar);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable int id) { business.excluirAvatar(id); }
}
