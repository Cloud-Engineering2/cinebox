// 예매하기 버튼 클릭 시 이벤트 처리
document.getElementById('confirm-reservation').addEventListener('click', function() {
    const selectedSeats = getSelectedSeats(); // 선택된 좌석 정보를 가져오는 함수

    if (selectedSeats.length === 0) {
        alert('좌석을 선택해주세요!');
        return;
    }

    // 예매 데이터 객체 생성
    const reservationData = {
        seatNumbers: selectedSeats,
        screenId: '12345', // 예시로 상영회차 ID
        userId: 'user123' // 예시로 사용자 ID
    };

    // 예매 요청 보내기
    fetch('/api/reserve', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(reservationData),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('예약이 성공적으로 완료되었습니다. 결제 처리를 진행합니다.');
            // 예약이 완료되면 결제 처리 함수 호출
            mypayment(reservationData);
        } else {
            alert('예약 처리에 실패했습니다: ' + data.message);
        }
    })
    .catch((error) => {
        alert('예약 요청 중 오류 발생: ' + error.message);
    });
});

// 선택된 좌석을 가져오는 함수 (예시)
function getSelectedSeats() {
    const selectedSeats = [];
    document.querySelectorAll('.seat.selected').forEach(seat => {
        selectedSeats.push(seat.dataset.seat);
    });
    return selectedSeats;
}
