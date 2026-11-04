package br.com.ecoup.resource;

import br.com.ecoup.business.EcoUpBusiness;
import br.com.ecoup.model.Login;
import br.com.ecoup.model.Usuario;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    private final EcoUpBusiness business;

    public AuthResource(EcoUpBusiness business) {
        this.business = business;
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Login dados) {

        return business.fazerLogin(
                dados.getEmail(),
                dados.getSenha()
        );
    }
}