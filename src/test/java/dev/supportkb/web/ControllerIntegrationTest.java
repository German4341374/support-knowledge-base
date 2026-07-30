package dev.supportkb.web;

import static org.assertj.core.api.Assertions.assertThat;

import dev.supportkb.AbstractPostgresIntegrationTest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerIntegrationTest extends AbstractPostgresIntegrationTest {

  @LocalServerPort private int port;

  private HttpClient client;

  @BeforeEach
  void setUp() {
    client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
  }

  @Test
  void healthEndpointReportsUp() throws Exception {
    HttpResponse<String> response = get("/actuator/health");

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body()).contains("\"status\":\"UP\"");
  }

  @Test
  void homePageRendersSeededKnowledge() throws Exception {
    HttpResponse<String> response = get("/");

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body())
        .contains("Find the fix.", "Connect to the corporate VPN", "Common support areas");
  }

  @Test
  void apiSearchReturnsRelevantArticle() throws Exception {
    HttpResponse<String> response = get("/api/articles?query=browser%20cache");

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body()).contains("clear-browser-cache-without-deleting-passwords");
  }

  @Test
  void apiRejectsInvalidArticle() throws Exception {
    HttpResponse<String> response =
        post(
            "/api/articles",
            """
                        {"title":"","summary":"","content":"","category":"","tags":[]}
                        """);

    assertThat(response.statusCode()).isEqualTo(400);
    assertThat(response.body()).contains("Request validation failed", "errors");
  }

  @Test
  void apiCreatesSanitizedDraft() throws Exception {
    HttpResponse<String> response =
        post(
            "/api/articles",
            """
                        {
                          "title":"Resolve demo proxy error",
                          "summary":"A safe integration-test article.",
                          "content":"<p>Keep this.</p><script>alert('x')</script>",
                          "category":"Network",
                          "tags":["proxy","demo"]
                        }
                        """);

    assertThat(response.statusCode()).isEqualTo(201);
    assertThat(response.headers().firstValue("location"))
        .hasValueSatisfying(value -> assertThat(value).contains("resolve-demo-proxy-error"));
    assertThat(response.body())
        .contains("\"status\":\"DRAFT\"", "<p>Keep this.</p>")
        .doesNotContain("<script>");
  }

  @Test
  void unknownArticleUsesProblemDetails() throws Exception {
    HttpResponse<String> response = get("/api/articles/does-not-exist");

    assertThat(response.statusCode()).isEqualTo(404);
    assertThat(response.headers().firstValue("content-type").orElse(""))
        .contains("application/problem+json");
    assertThat(response.body()).contains("Article not found");
  }

  private HttpResponse<String> get(String path) throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder(baseUri(path)).timeout(Duration.ofSeconds(10)).GET().build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private HttpResponse<String> post(String path, String json)
      throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder(baseUri(path))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private URI baseUri(String path) {
    return URI.create("http://127.0.0.1:" + port + path);
  }
}
