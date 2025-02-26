package cinebox.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cinebox.dto.response.PortOneApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortOneService {

	 private static final String PORTONE_API_URL = "https://api.portone.kr/v1/merchant/cancel"; // 포트원 API URL

	    public boolean cancelPayment(String paymentId) {
	        // RestTemplate을 이용하여 포트원 API에 결제 취소 요청을 보냄
	        try {
	            // 포트원 API의 취소 요청에 필요한 파라미터 설정
	            Map<String, Object> requestParams = new HashMap<>();
	            requestParams.put("merchant_uid", paymentId); // 결제 ID (상점에서 지정한 주문번호)
	            // 기타 필요한 파라미터들 (예: 환불 금액, 사유 등)

	            // POST 요청을 포트원 API에 보내기
	            RestTemplate restTemplate = new RestTemplate();
	            ResponseEntity<String> response = restTemplate.postForEntity(PORTONE_API_URL, requestParams, String.class);

	            // API 응답 상태 확인
	            if (response.getStatusCode().is2xxSuccessful()) {
	                // 성공적인 응답이면 취소 완료
	                return true;
	            } else {
	                // 실패한 응답 처리
	                return false;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	
}
    
