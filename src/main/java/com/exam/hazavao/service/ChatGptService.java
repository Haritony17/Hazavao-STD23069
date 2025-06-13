package com.exam.hazavao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${OPENAI.API.KEY}")
  private String apiKey;

  public String getMalagasyDefinition(String word) throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.get("application/json");

    String bodyJson =
        """
{
  "model": "gpt-3.5-turbo",
  "messages": [
    {"role": "system", "content": "Mamela hanome famaritana amin'ny teny malagasy fohy sy mazava."},
    {"role": "user", "content": "Hazavao ny teny: %s"}
  ]
}
"""
            .formatted(word);

    RequestBody body = RequestBody.create(bodyJson, mediaType);

    Request request =
        new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Erreur d'appel API ChatGPT: " + response);
      }

      String responseBody = response.body().string();
      JsonNode json = objectMapper.readTree(responseBody);
      return json.get("choices").get(0).get("message").get("content").asText();
    }
  }
}
