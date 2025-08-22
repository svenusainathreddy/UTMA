package hospital.HospitalWebApp2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hospital.HospitalWebApp2.model.Appointment;
import hospital.HospitalWebApp2.service.AppointmentService;
import hospital.HospitalWebApp2.service.DoctorAppointmentService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorAppointmentService doctorService;

    @GetMapping("/new-appointment")
    public String showAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAll()); // List<Doctor>
        return "appointment";
    }

    @PostMapping("/new-appointment")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment, Model model, HttpSession session) {
        appointment.setUsername((String) session.getAttribute("username"));
        appointment.setStatus("Scheduled");
        try {
            appointmentService.bookAppointment(appointment);
            model.addAttribute("successMessage", "Appointment booked successfully!");
            return "patienthome";
        } catch (RuntimeException e) {
            model.addAttribute("doctors", doctorService.getAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "appointment";
        }
    }
    
   
}
