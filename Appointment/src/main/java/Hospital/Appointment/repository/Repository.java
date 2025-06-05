package Hospital.Appointment.repository;

import Hospital.Appointment.entity.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface Repository extends JpaRepository<Appointment, Long> {

}
