package onliner.apartments.controller;

import onliner.apartments.model.Apartment;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.service.ApartmentsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

@RestController
public class ApartmentsSseController {

    @Autowired
    private ApartmentsLoader apartmentsLoader;
    @Autowired
    private ApartmentsRepository apartmentsRepository;


    @RequestMapping(path = "/api/apartments/details", method = RequestMethod.GET)
    public SseEmitter loadDetails() {
        SseEmitter emitter = new SseEmitter();
        List<Apartment> active = apartmentsRepository.getActive();
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture.runAsync(sender(emitter, "total", active.size()), executor)
                .runAfterBothAsync(startLoading(active, emitter, executor), sender(emitter, "finish", "finish"), executor)
                .whenCompleteAsync((empty, e) -> emitter.complete(), executor);
        return emitter;
    }

    private CompletableFuture<Void> startLoading(List<Apartment> apartments, SseEmitter emitter, ExecutorService senderExecutor) {
        return CompletableFuture.allOf(apartmentsLoader.fetchDetails(apartments)
                .map(future -> future.thenAcceptAsync(sender(emitter), senderExecutor).exceptionally(errorSender(emitter)))
                .toArray(CompletableFuture[]::new));
    }

    private Consumer<Apartment> sender(SseEmitter emitter) {
        return (apartment) -> send(emitter, apartment);
    }

    private Runnable sender(SseEmitter emitter, String event, Object data) {
        return () -> send(emitter, SseEmitter.event().name(event).data(data));
    }

    private Function<Throwable, Void> errorSender(SseEmitter emitter) {
        return (e) -> {
            send(emitter, SseEmitter.event().name("error").data(e.getMessage()));
            return null;
        };
    }

    private void send(SseEmitter emitter, Object object) {
        try {
            if (object instanceof SseEmitter.SseEventBuilder)
                emitter.send((SseEmitter.SseEventBuilder) object);
            else emitter.send(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
