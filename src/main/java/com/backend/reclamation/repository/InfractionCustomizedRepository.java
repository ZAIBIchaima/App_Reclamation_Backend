package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import com.backend.reclamation.entity.Infraction;


public interface InfractionCustomizedRepository {
	List<Infraction> getInfractionWithCustomQuery(Date dateStart,Date dateEnd,Long numInfraction, String source);

}
