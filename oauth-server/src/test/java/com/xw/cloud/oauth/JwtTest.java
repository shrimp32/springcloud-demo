package com.xw.cloud.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import java.util.Base64;

/**
 * @author : 夏玮
 * Created on 2018/9/11 10:35
 */

public class JwtTest {
    String HEADER = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
    String PAYLOAD = "{\"exp\":1510174606,\"user_name\":\"admin\",\"authorities\":[\"role_admin\"],\"jti\":\"52a8c915-1169-4c59-b42a-df8d34cd0ee4\",\"client_id\":\"app_client_zuul\",\"scope\":[\"openid\"]}";

    @Test
    public void test() {
        String encodedHeader = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9";
        String encodedPayload = "eyJleHAiOjE1MTAxNzQ2MDYsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiNTJhOGM5MTUtMTE2OS00YzU5LWI0MmEtZGY4ZDM0Y2QwZWU0IiwiY2xpZW50X2lkIjoiY2xpZW50Iiwic2NvcGUiOlsiYXBwIl19";
        String encodedSign = encodedHeader+"GecJM-FHApwznyYl-D3IjB0TpjhdhUXfYv782kfS9vdT0VZsu2HN-MGb-N-6Hf0efZ_mmz54IahJaq3KTw251v4L2O5A1r_iMuUP7GXs_qPHAGn3K1b4l-mNnpJdH5hhS5zYIRqOX2a8DXyI4zD7g8BQL-9PiR3kj9k_z9nW8vY9l2_x5Kyoc-sehxxQ5uQHM3xu6DzOwBpbbER7U_NnUwmcz5nS9YyAexSDnBbZAVpQavL2s1yYQVMJ5Dreq2asXHFbeQHXu5UqVbbTFuOgAylbFJ9K-3nsGAKT9NbzqBPRovI3s_X9HgjrzJHAuojBMeK0QMbvYSbUg2HB7MNNJw";
        String token = encodedHeader+"."+encodedPayload +"."+encodedSign;
        Jwt jwt = JwtHelper.decode(token);
        System.out.println(jwt.toString());
        System.out.println("=====================header=====================");
        System.out.println(Base64.getEncoder().encodeToString(HEADER.getBytes()));
        System.out.println("=====================payload====================");
        System.out.println(Base64.getEncoder().encodeToString(PAYLOAD.getBytes()));
    }

    @Test
    public void test2() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTAxNzQ2MDYsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiNTJhOGM5MTUtMTE2OS00YzU5LWI0MmEtZGY4ZDM0Y2QwZWU0IiwiY2xpZW50X2lkIjoiY2xpZW50Iiwic2NvcGUiOlsiYXBwIl19.GecJM-FHApwznyYl-D3IjB0TpjhdhUXfYv782kfS9vdT0VZsu2HN-MGb-N-6Hf0efZ_mmz54IahJaq3KTw251v4L2O5A1r_iMuUP7GXs_qPHAGn3K1b4l-mNnpJdH5hhS5zYIRqOX2a8DXyI4zD7g8BQL-9PiR3kj9k_z9nW8vY9l2_x5Kyoc-sehxxQ5uQHM3xu6DzOwBpbbER7U_NnUwmcz5nS9YyAexSDnBbZAVpQavL2s1yYQVMJ5Dreq2asXHFbeQHXu5UqVbbTFuOgAylbFJ9K-3nsGAKT9NbzqBPRovI3s_X9HgjrzJHAuojBMeK0QMbvYSbUg2HB7MNNJw";
        Jwt jwt = JwtHelper.decode(token);
        System.out.println(jwt.toString());
    }
}
