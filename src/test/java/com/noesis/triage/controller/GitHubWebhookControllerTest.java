package com.noesis.triage.controller;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.TriageResponse;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.model.webhook.GitHubWebhookPayload;
import com.noesis.triage.service.chain.TriageProcessorFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubWebhookControllerTest {

    @Mock
    private TriageProcessorFacade triageProcessorFacade;

    @InjectMocks
    private GitHubWebhookController controller;

    private GitHubIssue mockIssue;
    private GitHubWebhookPayload mockPayload;

    @BeforeEach
    void setUp() {
        mockIssue = GitHubIssue.builder()
                .id(1001L)
                .number(42)
                .title("Connection Pool Exhaustion Bug")
                .body("Redis client running out of active connections under peak traffic.")
                .htmlUrl("https://github.com/my-org/noesis/issues/42")
                .build();

        mockPayload = GitHubWebhookPayload.builder()
                .action("opened")
                .issue(mockIssue)
                .build();
    }

    @Test
    void shouldReturnOkWithTriageResponseWhenPayloadIsValid() {
        TriageContext context = new TriageContext();
        context.setLabels(List.of("bug", "performance"));
        context.setAiSummary("User reports connection pool exhaustion under load.");

        when(triageProcessorFacade.process(any(GitHubIssue.class))).thenReturn(context);

        ResponseEntity<?> responseEntity = controller.handleIssueWebhook(mockPayload);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(TriageResponse.class);

        TriageResponse triageResponse = (TriageResponse) responseEntity.getBody();
        assertThat(triageResponse.getStatus()).isEqualTo("SUCCESS");
        assertThat(triageResponse.getIssueId()).isEqualTo(1001L);
        assertThat(triageResponse.getAssignedLabels()).containsExactly("bug", "performance");
        assertThat(triageResponse.getSummary()).contains("connection pool exhaustion");

        verify(triageProcessorFacade, times(1)).process(mockIssue);
    }

    @Test
    void shouldReturnBadRequestWhenPayloadIsNull() {
        ResponseEntity<?> response = controller.handleIssueWebhook(null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid webhook payload: issue data is required.");
        verifyNoInteractions(triageProcessorFacade);
    }

    @Test
    void shouldReturnBadRequestWhenIssueIsNull() {
        GitHubWebhookPayload payloadWithoutIssue = GitHubWebhookPayload.builder()
                .action("opened")
                .issue(null)
                .build();

        ResponseEntity<?> response = controller.handleIssueWebhook(payloadWithoutIssue);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(triageProcessorFacade);
    }
}
