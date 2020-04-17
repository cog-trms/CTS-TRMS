package com.cognizant.trms.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Aravindan Dandapani
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "user")
public class User {
    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String mobileNumber;

//    @DBRef
//    private Set<Role> roles;

//    @DBRef(lazy = true)
//    private Set<UserRole> userroles;

//    @DBRef(lazy = true)
//    private Set<Role> roles;


    public String getFullName() {
        return firstName != null ? firstName.concat(" ").concat(lastName) : "";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
}
