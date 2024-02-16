package mate.academy.bookstore.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static final String SQL_SCRIPT_BEFORE_TEST = "classpath:database/books/add-books-with-category-to-books-and-categories-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST = "classpath:database/books/delete-books-with-category-from-books-and-categories-table.sql";
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SQL_SCRIPT_AFTER_TEST));
        }
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @WithMockUser(value = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create Book - Valid Request Dto Should Be Successful")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expected = createBookDtoKobzar();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        assertNotNull(actual.getId());
        reflectionEquals(actual, expected, "id");
    }

    @Test
    @DisplayName("Get All - Valid Pageable Should Return Two Books")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAll_ValidPageable_ShouldReturnTwoBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(createBookDtoKobzar());
        expected.add(createBookDtoLisovaPisnia());
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        assertThat(actual).hasSize(2);
        assertThat(Arrays.stream(actual).toList()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get by id - Valid Pageable Should Return Books")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getBookById_ValidId_ShouldReturnBook() throws Exception {
        BookDto expected = createBookDtoKobzar();
        MvcResult result = mockMvc.perform(get("/books/" + expected.getId()))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto.class);
        Assertions.assertNotNull(actual);
        assertThat(actual).isEqualTo(expected);
    }

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("Update by id - Valid Should Return  Books")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateById_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expected = createBookDtoKobzar().setPrice(BigDecimal.valueOf(5));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/books/" + expected.getId())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertThat(actual).isEqualTo(expected);
    }

    private BookDto createBookDtoKobzar() {
        return new BookDto()
                .setId(1L)
                .setTitle("Kobzar")
                .setAuthor("Shevchenko T. G.")
                .setIsbn("0123456789")
                .setPrice(BigDecimal.valueOf(100.50))
                .setCategoryIds(Set.of(1L));
    }

    private BookDto createBookDtoLisovaPisnia() {
        return new BookDto()
                .setId(2L)
                .setTitle("Lisova pisnia")
                .setAuthor("Lesia Ykrainka")
                .setIsbn("9874563210")
                .setPrice(BigDecimal.valueOf(150.75))
                .setCategoryIds(Set.of(2L));
    }

    private CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Shevchenko T. G.")
                .setIsbn("0123456789")
                .setPrice(BigDecimal.valueOf(100.55))
                .setCategoryIds(Set.of(1L));
    }
}
