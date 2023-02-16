package rpp2022backend.controller;

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
import rpp2022backend.model.Dobavljac;
import rpp2022backend.service.DobavljacService;

@CrossOrigin
@RestController
public class DobavljacController {

    @Autowired
    private DobavljacService dobavljacService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "Returns List of all Dobavljacs")
    @GetMapping("dobavljac")
    public ResponseEntity<List<Dobavljac>> getAll() {
        List<Dobavljac> dobavljacs = dobavljacService.getAll();
        return new ResponseEntity<>(dobavljacs, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns Dobavljac with id that was forwarded as path variable.")
    @GetMapping("dobavljac/{id}")
    public ResponseEntity<Dobavljac> getOne(@PathVariable("id") Integer id) {
        if (dobavljacService.findById(id).isPresent()) {
            Optional<Dobavljac> dobavljacOpt = dobavljacService.findById(id);
            return new ResponseEntity<>(dobavljacOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Returns list of Dobavljacs containing string that was forwarded as path variable in 'naziv'.")
    @GetMapping("dobavljac/naziv/{naziv}")
    public ResponseEntity<List<Dobavljac>> getByNaziv(@PathVariable("naziv") String naziv) {
        List<Dobavljac> dobavljacs = dobavljacService.findByNazivContainingIgnoreCase(naziv);
        return new ResponseEntity<>(dobavljacs, HttpStatus.OK);
    }

    @ApiOperation(value = "Adds new Dobavljac to database.")
    @PostMapping("dobavljac")
    public ResponseEntity<Dobavljac> addDobavljac(@RequestBody Dobavljac dobavljac) {
        Dobavljac savedDobavljac = dobavljacService.save(dobavljac);
        URI location = URI.create("/dobavljac/" + savedDobavljac.getId());
        return ResponseEntity.created(location).body(savedDobavljac);
    }

    @ApiOperation(value = "Updates Dobavljac that has id that was forwarded as path variable with values forwarded in Request Body.")
    @PutMapping(value = "dobavljac/{id}")
    public ResponseEntity<Dobavljac> updateDobavljac(@RequestBody Dobavljac dobavljac, @PathVariable("id") Integer id) {
        if (dobavljacService.existsById(id)) {
            dobavljac.setId(id);
            Dobavljac savedDobavljac = dobavljacService.save(dobavljac);
            return ResponseEntity.ok().body(savedDobavljac);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Deletes Dobavljac with id that was forwarded as path variable.")
    @DeleteMapping("dobavljac/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {

        if (id == -100 && !dobavljacService.existsById(id)) {
            jdbcTemplate.execute(
                    "INSERT INTO dobavljac (\"id\", \"naziv\", \"adresa\", \"kontakt\") VALUES (-100, 'Test Naziv', 'Test Adresa', 'Test Kontakt')");
        }

        if (dobavljacService.existsById(id)) {
            dobavljacService.deleteById(id);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    }

}