package rpp2022backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rpp2022backend.model.Dobavljac;

public interface DobavljacRepository extends JpaRepository<Dobavljac, Integer>{
	
	List<Dobavljac> findByNazivContainingIgnoreCase(String naziv);

}
