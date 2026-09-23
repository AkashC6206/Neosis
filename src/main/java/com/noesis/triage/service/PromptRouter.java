package com.noesis.triage.service;

import com.noesis.triage.model.LlmRoute;
import com.noesis.triage.model.TaskContext;
import com.noesis.triage.model.TaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Intelligent router dispatching triage tasks to optimal LLMs based on task complexity,
 * token length, and cost sensitivity constraints.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PromptRouter {

    /**
     * Dynamically selects the best LLM route based on incoming task context.
     *
     * @param context The task context containing type, token count, urgency, and cost sensitivity
     * @return The routing decision with model, provider, and cost weight
     */
    public LlmRoute routeFor(TaskContext context) {
        log.debug("Routing request for task: {}, tokens: {}, urgency: {}, cost sensitivity: {}",
                context.taskType(), context.tokenCount(), context.urgency(), context.costSensitivity());

        TaskType type = context.taskType() != null ? context.taskType() : TaskType.UNKNOWN;

        LlmRoute route = switch (type) {
            case LABELING -> new LlmRoute("gpt-4", "openai", 1.0);
            case SUMMARIZATION -> {
                if (context.tokenCount() > 8000) {
                    yield new LlmRoute("us.anthropic.claude-3-5-sonnet-20240620-v1:0", "anthropic", 0.8);
                }
                yield new LlmRoute("us.anthropic.claude-3-haiku-20240307-v1:0", "anthropic", 0.2);
            }
            default -> {
                if (context.costSensitivity() < 0.3) {
                    yield new LlmRoute("gpt-3.5-turbo", "openai", 0.1);
                }
                yield new LlmRoute("gpt-4", "openai", 1.0);
            }
        };

        log.info("Routed task {} (tokens: {}) to model '{}' (provider: {})",
                context.taskType(), context.tokenCount(), route.model(), route.provider());

        return route;
    }
}
