package cinebox.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.domain.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}