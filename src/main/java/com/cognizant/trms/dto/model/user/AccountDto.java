package com.cognizant.trms.dto.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto implements Comparable {

    private String id;
    private String accountName;
    private UserDto hiringManger;
    //private String userId;
    private BusinessUnitDto businessUnit;



    @Override
    public int compareTo(Object o) {
        {
            return this.getAccountName().compareTo(((AccountDto) o).getAccountName());
        }
    }
}
