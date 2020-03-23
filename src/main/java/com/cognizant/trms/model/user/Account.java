package com.cognizant.trms.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
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
@Document(collection = "account")
@CompoundIndexes({
        @CompoundIndex(name = "account_bu", def = "{'accountName' : 1, 'businessUnit.id': 1}")
})
public class Account {
    @Id
    private String id;

    private String accountName;
    @DBRef(lazy = true)
    private BusinessUnit businessUnit;
    @DBRef(lazy = true)
    private User accountMgr;
}

