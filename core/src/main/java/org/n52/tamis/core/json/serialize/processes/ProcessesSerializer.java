package org.n52.tamis.core.json.serialize.processes;

import java.io.IOException;
import java.util.List;

import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_forProcessList;
import org.n52.tamis.core.javarepresentations.processes.Processes_Tamis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Class to serialize and inctance of {@link Processes_Tamis}.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ProcessesSerializer extends JsonSerializer<Processes_Tamis> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessesSerializer.class);

	/**
	 * Serializes the list of processes as an array of processes!
	 */
	@Override
	public void serialize(Processes_Tamis processes, JsonGenerator jsonGenerator, SerializerProvider serProvider)
			throws IOException, JsonProcessingException {
		logger.info("Start serialization of process overview!");

		List<ProcessDescription_forProcessList> processesList = processes.getProcesses();

		ProcessDescription_forProcessList[] processesArray = new ProcessDescription_forProcessList[processesList
				.size()];
		processesArray = processesList.toArray(processesArray);

		ObjectMapper objMapper = new ObjectMapper();
		objMapper.writeValue(jsonGenerator, processesArray);

		logger.info("Serialization of processes overview ended!");

	}

}
