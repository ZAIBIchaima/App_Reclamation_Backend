package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import com.backend.reclamation.entity.Arrete;
public interface ArreteCustomizedRepository {
	
	List<Arrete> getArreteWithCustomQuery(Date dateStart,Date dateEnd,String refArrete);

}
