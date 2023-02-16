package rpp2022backend.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import rpp2022backend.model.Porudzbina;
import rpp2022backend.model.StavkaPorudzbine;
import rpp2022backend.service.PorudzbinaService;
import rpp2022backend.service.StavkaPorudzbineService;

@CrossOrigin
@RestController
public class StavkaPorudzbineController {

    @Autowired
    private StavkaPorudzbineService stavkaPorudzbineService;

    @Autowired
    private PorudzbinaService porudzbinaService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "Returns List of all StavkaPorudzbines")
    @GetMapping("stavkaPorudzbine")
    public ResponseEntity<List<StavkaPorudzbine>> getAll() {
        List<StavkaPorudzbine> stavkaPorudzbines = stavkaPorudzbineService.getAll();
        return new ResponseEntity<>(stavkaPorudzbines, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns StavkaPorudzbine with id that was forwarded as path variable.")
    @GetMapping("stavkaPorudzbine/{id}")
    public ResponseEntity<StavkaPorudzbine> getOne(@PathVariable("id") Integer id) {
        if (stavkaPorudzbineService.findById(id).isPresent()) {
            Optional<StavkaPorudzbine> stavkaPorudzbineOpt = stavkaPorudzbineService.findById(id);
            return new ResponseEntity<>(stavkaPorudzbineOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Returns list of StavkePorudzbine for Porudzbina with id that was forwarded as path variable.")
    @GetMapping("stavkeZaPorudzbinu/{id}")
    public ResponseEntity<List<StavkaPorudzbine>> getAllForPorudzbina(@PathVariable("id") Integer id) {
        Optional<Porudzbina> porudzbinaOpt = porudzbinaService.findById(id);
        if (porudzbinaOpt.isPresent()) {
            List<StavkaPorudzbine> stavkaPorudzbines = stavkaPorudzbineService.findByPorudzbina(porudzbinaOpt.get());
            return new ResponseEntity<>(stavkaPorudzbines, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @ApiOperation(value = "Returns list of StavkePorudzbine with price that is lower then price that was forwarded as path variable.")
    @GetMapping(value = "stavkaPorudzbineCena/{cena}")
    public ResponseEntity<List<StavkaPorudzbine>> getStavkaPorudzbineCena(@PathVariable("cena") BigDecimal cena) {
        List<StavkaPorudzbine> stavkaPorudzbines = stavkaPorudzbineService.findByCenaLessThanOrderById(cena);
        return new ResponseEntity<>(stavkaPorudzbines, HttpStatus.OK);

    }

    @ApiOperation(value = "Adds new StavkaPorudzbine to database.")
    @PostMapping("stavkaPorudzbine")
    public ResponseEntity<StavkaPorudzbine> addOne(@RequestBody StavkaPorudzbine stavkaPorudzbine) {
        if (!porudzbinaService.existsById(stavkaPorudzbine.getPorudzbina().getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        stavkaPorudzbine.setRedniBroj(stavkaPorudzbineService.nextRBr(stavkaPorudzbine.getPorudzbina().getId()));
        StavkaPorudzbine savedStavkaPorudzbine = stavkaPorudzbineService.save(stavkaPorudzbine);
        URI location = URI.create("/stavkaPorudzbine/" + savedStavkaPorudzbine.getId());
        return ResponseEntity.created(location).body(savedStavkaPorudzbine);
    }

    @ApiOperation(value = "Updates StavkaPorudzbine that has id that was forwarded as path variable with values forwarded in Request Body.")
    @PutMapping("stavkaPorudzbine/{id}")
    public ResponseEntity<StavkaPorudzbine> updateOne(@RequestBody StavkaPorudzbine stavkaPorudzbine,
            @PathVariable("id") Integer id) {
        if (!stavkaPorudzbineService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        stavkaPorudzbine.setId(id);
        StavkaPorudzbine savedStavkaPorudzbine = stavkaPorudzbineService.save(stavkaPorudzbine);
        return ResponseEntity.ok().body(savedStavkaPorudzbine);
    }

    @ApiOperation(value = "Deletes StavkaPorudzbine with id that was forwarded as path variable.")
    @DeleteMapping("stavkaPorudzbine/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        if (id == -100 && !stavkaPorudzbineService.existsById(-100)) {

            jdbcTemplate.execute(
                    "INSERT INTO stavka_porudzbine (\"id\", \"redni_broj\", \"kolicina\", \"jedinica_mere\", \"cena\", \"porudzbina\", \"artikl\") "
                            + "VALUES ('-100', '100', '1', 'kom', '1', '1', '1')");
        }

        if (stavkaPorudzbineService.existsById(id)) {
            stavkaPorudzbineService.deleteById(id);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }

        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    }

}
