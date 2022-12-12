package com.baidu.acg.det.finance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RestTemplate restTemplate;

    @GetMapping("index")
    public String hello(String code, Model model){

        if (code!=null) {
            MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
            map.add("code",code);
            map.add("client_id","clientDemo");
            map.add("client_secret","123");
            map.add("redirect_uri","http://localhost:8082/index");
            map.add("grant_type", "authorization_code");
            // 获取token
            Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
            String access_token = resp.get("access_token");
            System.out.println("access_token = "+access_token);

            // 访问资源服务器获取受保护资源,请求头中添加token
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer " +access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> entity =
                restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
            String msg = entity.getBody();
            model.addAttribute("msg",msg);
        }

        return "index";
    }

    @GetMapping("implicit")
    public String implicit(){

        return "implicit";
    }

    @GetMapping("/password")
    public String passwordDemo(){
        return "password";
    }

    @PostMapping("/login")
    public String login(String username, String password,Model model) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("client_secret", "123");
        map.add("client_id", "clientDemo");
        map.add("grant_type", "password");
        Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
        System.out.println(resp);
        String access_token = resp.get("access_token");
        System.out.println(access_token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
        model.addAttribute("msg", entity.getBody());
        return "password";
    }

    @GetMapping("credential")
    public String credential(Model model){
        LinkedMultiValueMap<String, String > map = new LinkedMultiValueMap<>();
        map.add("client_secret", "123");
        map.add("client_id", "clientDemo");
        map.add("grant_type", "client_credentials");
        Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
        System.out.println(resp);
        String access_token = resp.get("access_token");
        System.out.println(access_token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
        model.addAttribute("msg", entity.getBody());
        return "client_credential";
    }
}
