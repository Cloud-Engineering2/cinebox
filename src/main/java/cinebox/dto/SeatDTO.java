//package cinebox.dto;
//
//import lombok.Data;
//import cinebox.entity.Seat;
//
//@Data
//public class SeatDTO {
//    
//    private Long seatId;
//    private String seatNumber;
//
//    // 생성자
//    public SeatDTO(Long seatId, String seatNumber) {
//        this.seatId = seatId;
//        this.seatNumber = seatNumber;
//    }
//
//    // DTO -> 엔티티 변환
//    public Seat toEntity() {
//        return Seat.builder()
//                   .seatId(this.seatId)
//                   .seatNumber(this.seatNumber)
//                   .build();
//    }
//
//    // 엔티티 -> DTO 변환
//    public static SeatDTO from(Seat seat) {
//        return new SeatDTO(seat.getSeatId(), seat.getSeatNumber());
//    }
//}
