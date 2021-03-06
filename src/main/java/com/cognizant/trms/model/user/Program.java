package com.cognizant.trms.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "program")
@CompoundIndexes({
        @CompoundIndex(name = "program_acc", def = "{'programName' : 1, 'account.id': 1}")
})
public class Program {
    @Id
    private String id;

    private String programName;
    @DBRef(lazy = true)
    private Account account;
    @DBRef(lazy = true)
    private User programMgr;
    //private String programMgrId;
    
	/*
	 * @CreatedBy private String createdBy;
	 * 
	 * @CreatedDate private Date createdDate;
	 * 
	 * @LastModifiedBy private String lastModifiedBy;
	 * 
	 * @LastModifiedDate private Date lastModifiedDate;
	 */
}
