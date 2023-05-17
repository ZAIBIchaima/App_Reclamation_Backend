package com.backend.security.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

import com.backend.security.entity.ERole;

import lombok.Data;
 
@Data
public class SignUpRequest {
	  @NotBlank
	    @Size(min = 3, max = 20)
	    private String username;
	 
	    @NotBlank
	    @Size(max = 50)
	    @Email
	    private String email;
	
	    private Set<ERole> roles;
	    
	    @NotBlank
	    @Size(min = 6, max = 40)
	    private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Set<ERole> getRole() {
			return getRole();
		}

		public void setRole(Set<ERole> roles) {
			this.roles = roles;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	    
	    
}
