package com.elcom.search.controller;

import com.elcom.constant.ResourcePath;
import com.elcom.message.MessageContent;
import com.elcom.message.RequestMessage;
import com.elcom.message.ResponseMessage;
import com.elcom.search.elasticsearch.service.BookEsService;
import com.elcom.search.messaging.rabbitmq.RabbitMQClient;
import com.elcom.search.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.search.model.dto.AuthorizationResponseDTO;
import com.elcom.search.model.dto.ResponseMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/v1.0")
public class SearchController{

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private BookEsService bookEsService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Value("${user.authen.use}")
    private String authenUse;

    @Value("${user.authen.http.url}")
    private String authenHttpUrl;

    // find book by name using elasticsearch
    @RequestMapping(value = "/search/**", method = RequestMethod.GET)
    public ResponseEntity<Object> findBookByName(@RequestParam(value = "name") String name,
             @RequestHeader Map<String, String> headerMap, HttpServletRequest request) throws JsonProcessingException {
        // request path
        String requestPath = request.getRequestURI();
        if(requestPath != null && requestPath.contains(ResourcePath.VERSION)){
            requestPath = requestPath.replace(ResourcePath.VERSION, "/");
        }
        // Service
        int index = requestPath.indexOf("/", "/search/".length());
        String service = null;
        if(index != -1){
            service = requestPath.substring("/search/".length(), index);
        } else {
            service = requestPath.replace("/search/", "");
        }
        System.out.println("requestPath: " + requestPath + ", service: " + service);

        //Authen
        ResponseMessage response = null;
        if("http".equalsIgnoreCase(authenUse)){
            LOGGER.info("Http authen - authorization " + headerMap.get("authorization"));
            //Http -> call api authen
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", headerMap.get("authorization"));
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Dữ liệu đính kèm theo yêu cầu.
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ResponseMessage> result = restTemplate.exchange(authenHttpUrl, HttpMethod.GET, requestEntity, ResponseMessage.class);
            if (result != null && result.getStatusCode() == HttpStatus.OK) {
                response = result.getBody();
            }
            LOGGER.info("Http authen response : {}", response != null ? response.toJsonString() : null);
        } else {
            //Authen -> call rpc authen headerMap
            RequestMessage userRpcRequest = new RequestMessage();
            userRpcRequest.setRequestMethod("GET");
            userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
            userRpcRequest.setBodyParam(null);
            userRpcRequest.setUrlParam(null);
            userRpcRequest.setHeaderParam(headerMap);
            LOGGER.info("Call RPC authen - authorization " + headerMap.get("authorization"));
            LOGGER.info("RequestMessage userRpcRequest : " + userRpcRequest.toJsonString());
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                    RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
            LOGGER.info("RPC authen response : {}", result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //mapper.setDateFormat(df);
                try {
                    response = mapper.readValue(result, ResponseMessage.class);
                } catch (JsonProcessingException ex) {
                    LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                }
            }
        }

        if(response != null && response.getStatus() == HttpStatus.OK.value()){
            //process
            return new ResponseEntity<>(new ResponseMessageDTO(bookEsService.findByName(name)), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessageDTO("Token đăng nhập hết hạn"), HttpStatus.FORBIDDEN);
    }
}
