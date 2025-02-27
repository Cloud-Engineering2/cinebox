package cinebox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	// 결제 정보 조회
    Optional<Payment> findById(Long paymentId);
}