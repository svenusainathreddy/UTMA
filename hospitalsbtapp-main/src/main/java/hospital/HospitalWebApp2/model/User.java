package hospital.HospitalWebApp2.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users5")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	private String firstname;
	private String lastname;
	private String email;
	private String password;
	
	
	private boolean staffMember;
	private String department;
	private String jobTitle;
	private String dateOfJoining;
	private String manager;
	private String recordPath;
	private int failedLoginAttempts = 0;
	
	private boolean accountLocked = false;
	private LocalDateTime lockTime;
}