package korzero.timing.spring.batch.repeat;

import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatOperations;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.policy.DefaultResultCompletionPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;

public class RepeatTemplateTests {
	
	public static void main(String[] args) {
		RepeatOperations repeatOperations = null;
		
		RepeatTemplate repeatTemplate = new RepeatTemplate();
		
		// CompletionPolicy 세팅
		repeatTemplate.setCompletionPolicy(getChunkCompletionPolicy());
		
		// ExceptionHandler 세팅 - RepeatCallback 에서 Exception 발생시 여기에 설정한 exceptionHandler 가 호출됨.
		repeatTemplate.setExceptionHandler(getExceptionHandler());
		
		repeatOperations = repeatTemplate;
		
		repeatOperations.iterate(new RepeatCallback() {

			// 비즈니스 로직을 여기에 추가함.
			public RepeatStatus doInIteration(RepeatContext context)
					throws Exception {
				
				// 컨텍스트를 이용한 count 전달
				Object count = context.getAttribute("count");
				
				int repeatCount = 1;
				
				if (count != null) {
					repeatCount = (Integer) count;
					repeatCount++;
				}
				
				context.setAttribute("count", repeatCount);	
				System.out.println("### 반복호출: " + repeatCount);
				
				// 여기서 Exception 이 발생하는 경우
				
				if (repeatCount == 3) {
					throw new Exception("일부러오류");
				}
				
				// 처리에 따라
				// RepeatStatus.CONTINUABLE, RepeatStatus.FINISHED 리턴
				if (repeatCount < 10) {
					return RepeatStatus.CONTINUABLE;
				} else {
					return RepeatStatus.FINISHED;
				}
			}

		});
		
		
	}

	private static ExceptionHandler getExceptionHandler() {
		ExceptionHandler handler = new ExceptionHandler() {

			public void handleException(RepeatContext context,
					Throwable throwable) throws Throwable {
				if (throwable.getMessage().equals("일부러오류")) {
					throwable.printStackTrace();
					System.out.println("일부러오류이므로 건너뛴다.");
				} else {
					throw throwable;
				}
			}
			
		};
		return handler;
	}

	private static CompletionPolicy getChunkCompletionPolicy() {
		return new DefaultResultCompletionPolicy();
	}
	
	
	
}
