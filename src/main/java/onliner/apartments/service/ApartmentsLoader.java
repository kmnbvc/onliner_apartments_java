package onliner.apartments.service;

import onliner.apartments.binding.PageData;
import onliner.apartments.model.Apartment;
import onliner.apartments.model.Source;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.repository.SourcesRepository;
import onliner.apartments.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApartmentsLoader {

    @Autowired
    private ApartmentsRepository apartmentsRepository;

    @Autowired
    private SourcesRepository sourcesRepository;

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

    private PageData loadPage(Source source, int page) throws RuntimeException {
        String url = source.getUrl().replace("page=1", "page=" + page);
        return HttpUtil.get(url, PageData.class).setSource(source);
    }

}


/*

    const client = require('flashheart').createClient({
        name: 'apartments_loader',
        logger: console,
        retries: 10,
        retryTimeout: 500,
        rateLimitLimit: 5,
        rateLimitInterval: 1000
    });

    const loadApartments = () => {
        return sources_db.getActiveSources()
            .then(sources => concatResults(sources.map(source => loadAllPages(source))))
            .then(apartments => unique(apartments));
    };

    const loadAllPages = (source) => {
        return loadPage(source, 1).then(data => {
            const total_pages = data.page.last;
            const results = [extract_results(source, data)];

            for (let page = 2; page <= total_pages; page++) {
                results.push(loadPage(source, page, extract_results));
            }

            return concatResults(results);
        });
    };

    const loadPage = (source, page, mapper = (source, data) => data) => {
        const current_page = source.url.replace('page=1', 'page=' + page);

        return new Promise((resolve, reject) =>
            client.get(current_page, (err, body) => err ? reject(err) : resolve(mapper(source, body)))
        );
    };

    const extract_results = (source, data) => {
        const apartments = data.apartments;
        return apartments.map(ap => Object.assign(ap, {source}));
    };

    const concatResults = (results) => {
        return Promise.all(results).then(values => [].concat(...values));
    };

    const unique = (apartments) => {
        const seen = new Set();
        return apartments.filter(ap => seen.has(ap.id) ? false : seen.add(ap.id));
    };

 */
