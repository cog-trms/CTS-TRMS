/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Set;

import com.cognizant.trms.dto.model.user.TeamDto;

/**
 * @author Vara Kotha
 *
 */
public interface TeamService {
	
	Set<TeamDto> listTeams();

}
