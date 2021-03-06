package com.fms.farm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.synchronoss.cloud.nio.stream.storage.FileStreamStorage;

import com.fms.farm.dao.UploadJobStatusDynamoDb;
import com.fms.farm.entities.DegreeDay;
import com.fms.farm.entities.Egg;
import com.fms.farm.entities.Hello;
import com.fms.farm.entities.Stress;
import com.fms.farm.entities.request.DegreeDayRequest;
import com.fms.farm.entities.request.FileUploadRequest;
import com.fms.farm.entities.request.RegionalDataRequest;
import com.fms.farm.entities.request.TestDataRequest;
import com.fms.farm.entities.request.UploadDegreeDay;
import com.fms.farm.entities.request.UploadTestFileRequest;
import com.fms.farm.entities.response.DegreeDayUploadStatus;
import com.fms.farm.service.EggModelHandler;
import com.fms.farm.service.FetchJobStatusDataFromDB;
import com.fms.farm.service.StressModelHandler;
import com.fms.farm.service.UploadDegreeDayHandler;
import com.fms.farm.service.UploadTestDataService;
import com.fms.farm.util.FileStreamingUtility;

@RestController
@RequestMapping(value = "api/v1")
public class FarmControllers {
	@Autowired
	public UploadTestDataService uploadTestDataService;
	@Autowired
	public FileStreamingUtility fileStreamingUtility;
	
	@Autowired
	public UploadDegreeDayHandler uploadDegreeDayHandler;
	
	@Autowired
	public FetchJobStatusDataFromDB fetchJobStatusDataFromDB;
	
	@Autowired
	public StressModelHandler stressModelHandler; 
	
	@Autowired
	public EggModelHandler eggModelHandler;

	@RequestMapping(value = ControllerLinks.HELLO_FARM, method = RequestMethod.GET)
	public Hello helloDigitalFarmer() {
		return new Hello();
	}
	
	@RequestMapping(value = ControllerLinks.ADD_TEST_DATA_VIA_API, method = RequestMethod.POST)
	public String addTestDataToDbViaAPI(@RequestBody TestDataRequest dataRequest) {
		uploadTestDataService.addDataToDynamoDB(dataRequest);
		return "Data Has Been Added to db";
	}
	
	@RequestMapping(value = ControllerLinks.UPLOAD_TEST_CSV_DATA_VIA_API, method = RequestMethod.POST)
	public String saveTestCSVFileDataToDB(@RequestBody UploadTestFileRequest request) {
//		FileStreamingUtility fileStreamingUtility = new FileStreamingUtility();
		fileStreamingUtility.streamReadingFsSyncWritingDB(request);
		return "Data Has Been Added to db";
	}
	
	@RequestMapping(value = ControllerLinks.UPLOAD_JOBS, method = RequestMethod.GET)
	public List<UploadJobStatusDynamoDb> getAllUploadJobs() {
		List<UploadJobStatusDynamoDb> list = null;
		try {
			list = fetchJobStatusDataFromDB.getAll();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@CrossOrigin(origins = { "http://localhost:3000"})
	@RequestMapping(value = ControllerLinks.UPLOAD_DEGREE_DAY, method = RequestMethod.POST)
	public EntityModel<DegreeDayUploadStatus> uploadDegreeDayFile(@RequestBody UploadDegreeDay degreeDay) {
		EntityModel<DegreeDayUploadStatus> response = EntityModel.of(uploadDegreeDayHandler.createUploadRequest(degreeDay));
		response.add(Link.of("linktobegenerated", "self"));
		return response;
	}
	@CrossOrigin(origins = { "http://localhost:3000"})
	@RequestMapping(value = ControllerLinks.GET_ALL_DEGREE_DAY_FOR_REGIION, method = RequestMethod.POST)
	public List<DegreeDay> getAllDegreeDay(@RequestBody DegreeDayRequest dayRequest) {
		return uploadDegreeDayHandler.getAllDegreeDayForRegion(dayRequest.getRegion());
	}
	

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = ControllerLinks.UPLOAD_STRESS_MODEL, method = RequestMethod.POST)
	public String uploadStressModel(@RequestBody FileUploadRequest request) {
		stressModelHandler.uploadModel(request);
		return "Your Stress Model for the Region " + request.getRegion() + "Has Been uploaded successfully";
	}


	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = ControllerLinks.GET_ALL_STRESS_MODEL, method = RequestMethod.GET)
	public List<Stress> getAllStressModel(@PathVariable("region") String region) {
		return stressModelHandler.getAllDegreeDayForRegion(region.toUpperCase());
	}


	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = ControllerLinks.UPLOAD_EGG_MODEL, method = RequestMethod.POST)
	public String uploadEggModel(@RequestBody FileUploadRequest request) {
		eggModelHandler.uploadModel(request);
		return "Your Egg Model for the Region " + request.getRegion() + "Has Been uploaded successfully";
	}


	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = ControllerLinks.GET_ALL_EGG_MODEL, method = RequestMethod.GET)
	public List<Egg> getAllEggModel(@PathVariable("region") String region) {
		return eggModelHandler.getAllDegreeDayForRegion(region.toUpperCase());
	}
}
