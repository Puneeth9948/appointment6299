package Hospital.Appointment.controller;

import Hospital.Appointment.DTO.DoctorDTO;
import Hospital.Appointment.DTO.PatientDTO;
import Hospital.Appointment.entity.Appointment;
import Hospital.Appointment.repository.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/app")
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private Repository repository;
    @Autowired private RestTemplate restTemplate;

    private final String PATIENT_SERVICE_URL = "http://localhost:3000/patient/";
    private final String DOCTOR_SERVICE_URL = "http://localhost:3001/doctor/";
    @PostMapping ("/add")
    public ResponseEntity<Appointment> create(@RequestBody Appointment req) {
        try {
            // Validate patient
            PatientDTO patient = restTemplate.getForObject(PATIENT_SERVICE_URL + req.getPatientId(), PatientDTO.class);
            // Validate doctor
            DoctorDTO doctor = restTemplate.getForObject(DOCTOR_SERVICE_URL + req.getDoctorId(), DoctorDTO.class);

            if (!"AVAILABLE".equalsIgnoreCase(doctor.getAvailabilityStatus())) {
                return ResponseEntity.badRequest().body(null);
            }

            Appointment appt = new Appointment(req.getPatientId(), req.getDoctorId(), req.getAppointmentTime(), "SCHEDULED");
            return ResponseEntity.ok(repository.save(appt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping ("/all")
    public List<Appointment> getAll() { return repository.findAll(); }
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/delete/{id}") public void delete(@PathVariable Long id) { repository.deleteById(id); }
}
