/**
 * 
 */
package com.cognizant.trms.service;


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
	

	@Override
    public Set<ProgramDto> listPrograms() {
    	log.debug("ProgramServiceImpl:listPrograms ");
    	
        return programRepository.findAll()
                .stream()
                .map(listPrograms -> modelMapper.map(listPrograms, ProgramDto.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }
    
	@Override
	public ProgramDto createProgram(ProgramDto programDto) {
		
		//Get the account object using account name
		Account account = accountRepository.findByaccountName(programDto.getAccountName());
		
		if (account== null) {
			throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, programDto.getAccountName());
		}
		
		// Get the user object using  program manger email
		User programMgr = userRepository.findByEmail(programDto.getProgramMgrEmail());
		
		if (programMgr== null) {
			throw exception(EntityType.PROGRAM_MGR, ExceptionType.ENTITY_NOT_FOUND, programDto.getProgramMgrEmail());
		}
		
		// Build the Program data model
		 Program gpProgram = programRepository.findByprogramName(programDto.getProgramName());
	            if (gpProgram == null){
	                gpProgram = new Program()
	                    .setProgramName(programDto.getProgramName())
	                    .setAccount(account)
	                    .setProgramMgr(programMgr);
	                return ProgramMapper.toProgramDto(programRepository.save(gpProgram));
	            }
	       throw exception(EntityType.PROGRAM, ExceptionType.DUPLICATE_ENTITY, programDto.getProgramName());

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

}
