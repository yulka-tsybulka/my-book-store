import model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.BookService;
import java.math.BigDecimal;

@SpringBootApplication
public class MyBookStoreApplication {
	@Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(MyBookStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args ->  {
				Book kobzar = new Book();
				kobzar.setAuthor("Shevchenko");
				kobzar.setPrice(BigDecimal.valueOf(500));
				kobzar.setCoverImage("123");
				kobzar.setDescription("poetry");
				kobzar.setIsbn("9788373637290");
				kobzar.setTitle("Kobzar");

				bookService.save(kobzar);

				System.out.println(bookService.findAll());
			};
	}
}
