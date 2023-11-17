package repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import model.Book;
import org.springframework.stereotype.Component;
import repository.SpecificationProvider;
import repository.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find correct specification provider for key" + key));
    }
}
