package com.samagra.transformer.odk.utilities;

import android.webkit.MimeTypeMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samagra.transformer.odk.FormDownloader;
import com.samagra.transformer.odk.model.FormDetails;
import com.samagra.transformer.odk.openrosa.CollectThenSystemContentTypeMapper;
import com.samagra.transformer.odk.openrosa.OpenRosaAPIClient;
import com.samagra.transformer.odk.openrosa.OpenRosaHttpInterface;
import com.samagra.transformer.odk.openrosa.okhttp.OkHttpConnection;
import com.samagra.transformer.odk.openrosa.okhttp.OkHttpOpenRosaServerClientProvider;
import io.fusionauth.domain.User;
import messagerosa.core.model.XMessage;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FormListDownloaderTest {

    RestTemplate restTemplate = new RestTemplate();

    private void updateStatusForApproval(String employeeName, String teamName, String startDateString) {
        String baseURL = "http://139.59.93.172:3000/samagra-internal-workflow?filter=";
        String filters = String.format("{\"where\":{\"data.member_name\": \"%s\", \"data.team_name\": \"%s\", \"data.start_date_leave\": \"%s\"}}"
                , employeeName, teamName, startDateString);
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(baseURL + filters)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONArray jsonArray = new JSONArray(jsonData);
            JSONObject data = (JSONObject) jsonArray.get(0);

            ((JSONObject) data.getJSONArray("data").get(0)).put("manager_approval", true);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, data.toString());

            Request request2 = new Request.Builder()
                    .url("http://139.59.93.172:3000/samagra-internal-workflow/" + data.get("id"))
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response2 = client.newCall(request2).execute();
            String updateResponse = response2.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void formListDownloadTest() {
        updateStatusForApproval("Chakshu Gautam", "CTT", "10-10-2020");
    }


}