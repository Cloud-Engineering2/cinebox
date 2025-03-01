package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}