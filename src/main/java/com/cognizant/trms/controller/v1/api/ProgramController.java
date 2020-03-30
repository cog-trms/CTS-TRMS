/**
 * 
 */
package com.cognizant.trms.controller.v1.api;

import java.util.Collection;

import java.util.Set;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.trms.controller.v1.request.ProgramCreationRequest;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.ProgramService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Vara Kotha
 * 
 * 
 */


@RestController
@RequestMapping("/api/v1/programs")
@Api(value="trms-application", description="Operations pertaining to program management in the TRMS application")
public class ProgramController {
	
	private static final Logger log = LogManager.getLogger(ProgramController.class);
	
	@Autowired
	 private ProgramService programService;
	
	/***
	 * Retrieve all programs
	 * @return
	 */
	 @GetMapping("/all")
	    @ApiOperation(value = "API handler for getting All Program details", authorizations = {@Authorization(value = "apiKey")})
	    public Response getProgramDetails(){
	    	log.debug("Inside getAllPrograms API Method");
	    	Set<ProgramDto> programSet = programService.listPrograms();
	        return Response
	                .ok()
	                .setPayload((Set<ProgramDto>)programSet);
	    }
	
	 /***
	  * Create or update Program
	  * Input: ProgramName, AccountId and UserId
	  * @param programCreationRequest
	  * @return
	  */
	 @PostMapping("/program")
	    @ApiOperation(value = "API handler for creating new program details", authorizations = {@Authorization(value = "apiKey")})
	    public ProgramDto saveOrUpdateProgram( @Valid  @RequestBody ProgramCreationRequest programCreationRequest)  {
	    	log.debug("Inside createProgram API Method");
	   	
	    	ProgramDto programDto = new ProgramDto();
	    	programDto.setProgramName(programCreationRequest.getName());
	    	programDto.setAccountId(programCreationRequest.getAccountId());
	    	programDto.setUserId(programCreationRequest.getUserId());
	    	programDto.setProgramId(programCreationRequest.getProgramId());

	    	return programService.saveOrUpdateProgram(programDto);
	    }

        /***
         * Delete program
         * @param id
         * @return
         */
	    @DeleteMapping("/program/{id}")
	    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
	    public Response deleteProgram(@PathVariable("id") String id){
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
	        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
	        log.debug("Authorized :"+ authorized);
	        if(authorized) {
	        	return programService.deleteProgram(id);
	        }
	        return Response.accessDenied();
	    }
		 
}
