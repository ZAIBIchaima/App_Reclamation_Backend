package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import com.backend.reclamation.entity.Reclamation;

public interface ReclamationCustomizedRepository {
	
	List<Reclamation> getReclamationWithCustomQuery(Date dateEntreStart,Date dateEntreEnd,String refReclamation);
	

}
