package rpp2022backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rpp2022backend.model.Artikl;
import rpp2022backend.repository.ArtiklRepository;

@Service
public class ArtiklService {
	
	/*
	* Anotacija @Autowired se može primeniti nad varijablama instace, setter metodama i
	* konstruktorima. Označava da je neophodno injektovati zavisni objekat. Prilikom
	* pokretanja aplikacije IoC kontejner prolazi kroz kompletan kod tražeči anotacije
	* koje označavaju da je potrebno kreirati objekte. Upotrebom @Autowired anotacije
	* stavljeno je do znanja da je potrebno kreirati objekta klase koja će implementirati
	* repozitorijum AriklRepository i proslediti klasi ArtiklRestController referencu
	* na taj objekat. 
	*/
	
	@Autowired
	private ArtiklRepository artiklRepository;
	
	public List<Artikl> getAll(){
		return artiklRepository.findAll();
	}
	
	public Optional<Artikl> findById(Integer id) {
		return artiklRepository.findById(id);
	}
	
	public List<Artikl> findByNazivContainingIgnoreCase(String naziv) {
        return artiklRepository.findByNazivContainingIgnoreCase(naziv);
    }
	
	public Artikl save(Artikl artikl) {
		return artiklRepository.save(artikl);
	}
	
	public boolean existsById(Integer id) {
		return artiklRepository.existsById(id);
	}
	
	public void deleteById(Integer id) {
		artiklRepository.deleteById(id);
	}

}
