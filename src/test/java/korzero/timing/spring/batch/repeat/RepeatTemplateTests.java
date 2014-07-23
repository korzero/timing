package korzero.timing.spring.batch.repeat;

import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatListener;
import org.springframework.batch.repeat.RepeatOperations;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.policy.DefaultResultCompletionPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;

public class RepeatTemplateTests {

	public static void main(String[] args) {
		RepeatOperations repeatOperations = null;

		RepeatTemplate repeatTemplate = new RepeatTemplate();

		// CompletionPolicy ����
		repeatTemplate.setCompletionPolicy(getChunkCompletionPolicy());

		// ExceptionHandler ���� - RepeatCallback ���� Exception �߻��� ���⿡ ������
		// exceptionHandler �� ȣ���.
		repeatTemplate.setExceptionHandler(getExceptionHandler());

		repeatTemplate.setListeners(getRepeatListeners());

		repeatOperations = repeatTemplate;

		repeatOperations.iterate(new RepeatCallback() {

			// ����Ͻ� ������ ���⿡ �߰���.
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {

				// ���ؽ�Ʈ�� �̿��� count ����
				Object count = context.getAttribute("count");

				int repeatCount = 1;

				if (count != null) {
					repeatCount = (Integer) count;
					repeatCount++;
				}

				context.setAttribute("count", repeatCount);
				System.out.println("### �ݺ�ȣ��: " + repeatCount);

				// ���⼭ Exception �� �߻��ϴ� ���

				if (repeatCount == 3) {
					throw new Exception("�Ϻη�����");
				}

				// ó���� ����
				// RepeatStatus.CONTINUABLE, RepeatStatus.FINISHED ����
				if (repeatCount < 10) {
					return RepeatStatus.CONTINUABLE;
				}
				else {
					return RepeatStatus.FINISHED;
				}
			}

		});

	}

	private static RepeatListener[] getRepeatListeners() {

		RepeatListener listener = new RepeatListener() {

			public void open(RepeatContext arg0) {
				System.out.println("### open");
			}

			public void onError(RepeatContext arg0, Throwable arg1) {
				System.out.println("### onError");
			}

			public void close(RepeatContext arg0) {
				System.out.println("### close");

			}

			public void before(RepeatContext arg0) {
				System.out.println("### before");

			}

			public void after(RepeatContext arg0, RepeatStatus arg1) {
				System.out.println("### after");

			}
		};

		return new RepeatListener[] { listener };
	}

	private static ExceptionHandler getExceptionHandler() {
		ExceptionHandler handler = new ExceptionHandler() {

			public void handleException(RepeatContext context, Throwable throwable)
					throws Throwable {
				if (throwable.getMessage().equals("�Ϻη�����")) {
					throwable.printStackTrace();
					System.out.println("�Ϻη������̹Ƿ� �ǳʶڴ�.");
				}
				else {
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
