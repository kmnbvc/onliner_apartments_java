package onliner.apartments.service;

import com.mashape.unirest.http.HttpResponse;
import onliner.apartments.binding.PageData;
import onliner.apartments.model.Apartment;
import onliner.apartments.model.Source;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.repository.SourcesRepository;
import onliner.apartments.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApartmentsLoader {

    @Autowired
    private ApartmentsRepository apartmentsRepository;

    @Autowired
    private SourcesRepository sourcesRepository;

    public Stream<CompletableFuture<Apartment>> fetchDetails(List<Apartment> apartments) {
        return loadDetails(apartments).map(future -> future.thenApply(apartmentsRepository::save));
    }

    public Stream<CompletableFuture<Apartment>> loadDetails(List<Apartment> apartments) {
        return apartments.stream().map(this::loadDetails);
    }

    public List<Apartment> loadNew() {
        List<Apartment> existed = apartmentsRepository.getAll();
        return load().filter(a -> !existed.contains(a)).collect(Collectors.toList());
    }

    private Stream<Apartment> load() {
        return sourcesRepository.getActiveSources().parallelStream()
                .flatMap(this::loadAllPages).distinct();
    }

    private Stream<Apartment> loadAllPages(Source source) {
        List<Apartment> result = new ArrayList<>();
        for (int page = 1; ; page++) {
            PageData data = loadPage(source, page);
            result.addAll(data.getApartments());
            if (data.getInfo().isLast()) break;
        }
        return result.stream();
    }

    private PageData loadPage(Source source, int page) {
        String url = source.getUrl(page);
        return HttpUtil.get(url, PageData.class).setSource(source);
    }

    private CompletableFuture<Apartment> loadDetails(Apartment apartment) {
        return CompletableFuture.supplyAsync(() -> {
            HttpResponse<String> response = HttpUtil.get(apartment.getUrl());
            if (response.getStatus() == 404) {
                apartment.setActive(Boolean.FALSE);
            } else {
                Document document = Jsoup.parse(response.getBody());
                String text = document.getElementsByClass("apartment-info__sub-line_extended-bottom").text();
                String phones = document.getElementsByClass("apartment-info__list_phones").text();
                List<String> images = document.getElementsByClass("apartment-gallery__slide").eachAttr("data-thumb");
                apartment.setDetails(text, phones, images);
            }
            return apartment;
        });
    }

}
