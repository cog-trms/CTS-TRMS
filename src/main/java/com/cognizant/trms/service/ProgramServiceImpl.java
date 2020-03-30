/**
 * 
 */
package com.cognizant.trms.service;


import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognizant.trms.dto.mapper.ProgramMapper;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.repository.user.AccountRepository;
import com.cognizant.trms.repository.user.ProgramRepository;
import com.cognizant.trms.repository.user.UserRepository;

/**
 * @author Vara Kotha
 *
 *TODO: Include created time, updated date, auditing
 *Review: Program name editable or non-editable
 */

@Component
public class ProgramServiceImpl implements ProgramService {
	
	private static final Logger log = LogManager.getLogger(ProgramServiceImpl.class);
	  
	  @Autowired
	  private ProgramRepository programRepository;
	  
	  @Autowired
	  private AccountRepository accountRepository;
	  
	  @Autowired
	  private UserRepository userRepository;
	  
	  @Autowired
	  private ModelMapper modelMapper;
	

	 /***
	  * Return all program details
	  * @return programDto
	  */
	@Override
    public Set<ProgramDto> listPrograms() {
    	log.debug("ProgramServiceImpl:listPrograms ");
    	
        return programRepository.findAll()
                .stream()
                .map(listPrograms -> ProgramMapper.toProgramDto(listPrograms,"All"))
                .collect(Collectors.toCollection(TreeSet::new));
    }
    
	
	/***
	 * Save or Update program details
	 * @param programDto
	 * @return programDto
	 */
	@Override
	public ProgramDto saveOrUpdateProgram(ProgramDto programDto) {
		Account selAccount = null;
		User selProgramMgr = null;
		Program programModel = null;
		
		//Get the account object
		Optional<Account> account = accountRepository.findById(programDto.getAccountId());
		
		if (account.isPresent()){
			 selAccount = account.get();
		}else {
			 throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, programDto.getAccountId());
		}
		
		// Get the user object
		Optional<User> programMgr = userRepository.findById(programDto.getUserId());
		
		
		if (programMgr.isPresent()) {
			selProgramMgr = programMgr.get();
		}else {
			 throw exception(EntityType.PROGRAM_MGR, ExceptionType.ENTITY_NOT_FOUND, programDto.getUserId());
		}
		
		// Build and save/update the program details
		Optional<Program> program = programRepository.findById(programDto.getProgramId());
		
		
		
		if (program.isPresent()) {
			programModel = program.get();
		}else {
			programModel = new Program();
			Optional<Program> program1 = Optional.ofNullable(programRepository.findByprogramName(programDto.getProgramName()));
			 if(program1.isPresent()) {
				 throw exception(EntityType.PROGRAM, ExceptionType.DUPLICATE_ENTITY, programDto.getProgramName());
			 }
		}
		
		programModel.setProgramName(programDto.getProgramName())
					 .setAccount(selAccount)
					 .setProgramMgr(selProgramMgr);
								
		 return ProgramMapper.toProgramDto(programRepository.save(programModel));
	
	}

	
	  /**
     * Returns a new RuntimeException
     * TODO: Need to move common utility logic
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }


    /**
     * Delete program
     * @param id
     * @return
     */
	@Override
	public Response deleteProgram(String id) {
		 Optional<Program> program = programRepository.findById(id);
         if (program.isPresent()) {
        	 programRepository.deleteById(id);
             return Response.ok();
         } else {
             return Response.notFound();
         }
	}


}
