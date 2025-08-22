package hospital.HospitalWebApp2.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "appointments5")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String doctor;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String patientEmail;

    @Column(nullable = false)
    private boolean isSlotAvailable;
    
    @Column(nullable=false)
    private String username;
    
    private String status;
   
}
