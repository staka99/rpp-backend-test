package rpp2022backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rpp2022backend.model.Porudzbina;

public interface PorudzbinaRepository extends JpaRepository<Porudzbina, Integer>{
	
	List<Porudzbina> findByPlacenoTrue();
		

}
