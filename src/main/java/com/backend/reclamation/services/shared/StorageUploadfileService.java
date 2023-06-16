package com.backend.reclamation.services.shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StorageUploadfileService {
	
	
	public String convertToJson(List list ) throws JsonGenerationException, JsonMappingException, IOException {
	    final ByteArrayOutputStream outjson = new ByteArrayOutputStream();
	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(outjson, list);
	    final byte[] datajson = outjson.toByteArray();
	    return new String(datajson);
	}
	public String convertToJsonObject(Object object ) throws JsonGenerationException, JsonMappingException, IOException {
	    final ByteArrayOutputStream outjson = new ByteArrayOutputStream();
	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(outjson, object);
	    final byte[] datajson = outjson.toByteArray();
	    return new String(datajson);
	}


}
