package cinebox.dto.response;

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
public class BookingResponseDTO {
    private Long bookingId;
    private Long userId;
    private LocalDateTime bookingDate;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private List<Long> seatIds; // 좌석 ID 리스트
    private List<Long> paymentIds; // 결제 ID 리스트

    public BookingResponseDTO(Long bookingId, Long userId, LocalDateTime bookingDate, BigDecimal totalPrice,
                              BookingStatus status, List<Long> seatIds, List<Long> paymentIds) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.seatIds = seatIds;
        this.paymentIds = paymentIds;
    }

    // 엔티티 → 응답 DTO 변환 메서드
    public static BookingResponseDTO fromEntity(Booking booking) {
        return BookingResponseDTO.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUser().getUserId())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .seatIds(booking.getBookingSeats().stream()
                        .map(BookingSeat::getSeat)
                        .filter(seat -> seat != null)
                        .map(seat -> seat.getSeatId())
                        .collect(Collectors.toList()))
                .paymentIds(booking.getPayments().stream()
                        .map(Payment::getPaymentId)
                        .collect(Collectors.toList()))
                .build();
    }
}

