package rpp2022backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rpp2022backend.model.Porudzbina;
import rpp2022backend.repository.PorudzbinaRepository;

@Service
public class PorudzbinaService {
	
	@Autowired
	private PorudzbinaRepository porudzbinaRepository;
	
	public List<Porudzbina> getAll(){
		return porudzbinaRepository.findAll();
	}
	
	public Optional<Porudzbina> findById(Integer id) {
		return porudzbinaRepository.findById(id);
	}
	
	public List<Porudzbina> findByPlacenoTrue() {
        return porudzbinaRepository.findByPlacenoTrue();
    }
	
	public Porudzbina save(Porudzbina porudzbina) {
		return porudzbinaRepository.save(porudzbina);
	}
	
	public boolean existsById(Integer id) {
		return porudzbinaRepository.existsById(id);
	}
	
	public void deleteById(Integer id) {
		porudzbinaRepository.deleteById(id);
	}

}
