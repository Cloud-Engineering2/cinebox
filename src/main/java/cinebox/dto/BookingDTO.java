package cinebox.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class BookingDTO {
    private Long bookingId;
    private Long userId;
    private LocalDateTime bookingDate;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private List<Long> seatIds;
    private List<Long> paymentIds;

    public BookingDTO(Long bookingId, Long userId, LocalDateTime bookingDate, BigDecimal totalPrice,
                      BookingStatus status, List<Long> seatIds, List<Long> paymentIds) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.seatIds = seatIds; //좌석 ID
        this.paymentIds = paymentIds; // 결제 ID리스트
    }

    
    // 엔티티 → DTO 변환 메서드
    public static BookingDTO fromEntity(Booking booking) {
        return BookingDTO.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUser().getUserId())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .seatIds(booking.getBookingSeats().stream()
                        .map(BookingSeat::getSeat) // BookingSeat에서 Seat 가져오기
                        .filter(seat -> seat != null) // Null 체크
                        .map(seat -> seat.getSeatId()) // Seat ID 가져오기
                        .collect(Collectors.toList()))
                .paymentIds(booking.getPayments().stream()
                        .map(Payment::getPaymentId) // Payment ID 가져오기
                        .collect(Collectors.toList()))
                .build();
    }
    
}
