package ru.andreymarkelov.test.redisdemo.delayqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnqueueController {
    private static final Logger log = LoggerFactory.getLogger(EnqueueController.class);

    private final QueueService queueService;

    public EnqueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/enqueue")
    @ResponseStatus(value = HttpStatus.OK)
    public void enqueue(@RequestParam(name="message") String message, @RequestParam(name="delay") int delay) {
        log.info("Received message:{}", message);
        queueService.queueMessage(message, 2);
    }
}
