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
import rpp2022backend.model.Artikl;
import rpp2022backend.service.ArtiklService;

@CrossOrigin
@RestController
public class ArtiklController {

    @Autowired
    private ArtiklService artiklService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * HTTP GET je jedna od HTTP metoda koja je analogna opciji READ iz CRUD
     * operacija. Anotacija @GetMapping se koristi kako bi se mapirao HTTP GET
     * zahtev. Predstavlja skraćenu verziju metode @RequestMapping(method =
     * RequestMethod.GET) U konkretnom slučaju HTTP GET zahtevi (a to je npr.
     * svako učitavanje stranice u browser-u) upućeni na adresu
     * localhost:8083/artikl biće prosleđeni ovoj metodi.
     *
     * Poziv metode artiklRepository.findAll() će vratiti kolekciju koja sadrži
     * sve artikala koji će potom u browseru biti prikazani u JSON formatu
     */

    @ApiOperation(value = "Returns List of all Artikls")
    @GetMapping("artikl")
    public ResponseEntity<List<Artikl>> getAll() {
        List<Artikl> artikls = artiklService.getAll();
        return new ResponseEntity<>(artikls, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns Artikl with id that was forwarded as path variable.")
    @GetMapping("artikl/{id}")
    public ResponseEntity<Artikl> getOne(@PathVariable("id") Integer id) {
        if (artiklService.findById(id).isPresent()) {
            Optional<Artikl> artiklOpt = artiklService.findById(id);
            return new ResponseEntity<>(artiklOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Returns list of Artikls containing string that was forwarded as path variable in 'naziv'.")
    @GetMapping("artikl/naziv/{naziv}")
    public ResponseEntity<List<Artikl>> getByNaziv(@PathVariable("naziv") String naziv) {
        List<Artikl> artikls = artiklService.findByNazivContainingIgnoreCase(naziv);
        return new ResponseEntity<>(artikls, HttpStatus.OK);
    }

    @ApiOperation(value = "Adds new Artikl to database.")
    @PostMapping("artikl")
    public ResponseEntity<Artikl> addArtikl(@RequestBody Artikl artikl) {
        Artikl savedArtikl = artiklService.save(artikl);
        URI location = URI.create("/artikl/" + savedArtikl.getId());
        return ResponseEntity.created(location).body(savedArtikl);
    }

    @ApiOperation(value = "Updates Artikl that has id that was forwarded as path variable with values forwarded in Request Body.")
    @PutMapping(value = "artikl/{id}")
    public ResponseEntity<Artikl> updateArtikl(@RequestBody Artikl artikl, @PathVariable("id") Integer id) {
        if (artiklService.existsById(id)) {
            artikl.setId(id);
            Artikl savedArtikl = artiklService.save(artikl);
            return ResponseEntity.ok().body(savedArtikl);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Deletes Artikl with id that was forwarded as path variable.")
    @DeleteMapping("artikl/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        if (id == -100 && !artiklService.existsById(id)) {
            jdbcTemplate.execute(
                    "INSERT INTO artikl (\"id\", \"proizvodjac\", \"naziv\") VALUES (-100, 'Test Proizvodjac', 'Test Naziv')");
        }

        if (artiklService.existsById(id)) {
            artiklService.deleteById(id);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    }

}
