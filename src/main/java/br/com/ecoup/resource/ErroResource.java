package br.com.ecoup.resource;

import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErroResource {
    // Monta a mensagem que sera enviada ao front-end.
    private Map<String, String> mensagem(String texto) {
        Map<String, String> resposta = new HashMap<String, String>();
        resposta.put("erro", texto);
        return resposta;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> resposta(ResponseStatusException erro) {
        String descricao = erro.getReason();
        if (descricao == null) descricao = "Requisicao invalida.";
        return ResponseEntity.status(erro.getStatusCode())
                .body(mensagem(descricao));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integridade(DataIntegrityViolationException erro) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mensagem("Registro duplicado ou relacionamento invalido."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> validacao(IllegalArgumentException erro) {
        return ResponseEntity.badRequest().body(mensagem(erro.getMessage()));
    }

    // As classes DAO usam JDBC e transformam SQLException em RuntimeException.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> erroDeBanco(RuntimeException erro) {
        Throwable causa = erro;

        while (causa != null) {
            if (causa instanceof SQLException) {
                SQLException sqlErro = (SQLException) causa;
                int codigo = sqlErro.getErrorCode();

                // Oracle: registro duplicado ou chave estrangeira invalida.
                if (codigo == 1 || codigo == 2291 || codigo == 2292) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(mensagem("Registro duplicado ou relacionamento invalido."));
                }
                if (codigo == 1400) {
                    return ResponseEntity.badRequest()
                            .body(mensagem("Campo obrigatorio nao informado."));
                }
            }
            causa = causa.getCause();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mensagem("Erro ao acessar o banco de dados."));
    }
}
