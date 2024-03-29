package com.elcom.report.controller;

import com.elcom.constant.ResourcePath;
import com.elcom.message.RequestMessage;
import com.elcom.message.ResponseMessage;
import com.elcom.report.messaging.rabbitmq.RabbitMQClient;
import com.elcom.report.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.report.model.dto.BookAuthorDTO;
import com.elcom.report.model.dto.BookCategoryDTO;
import com.elcom.report.model.dto.BookFirstLetterDTO;
import com.elcom.report.model.dto.ResponseMessageDTO;
import com.elcom.report.repository.library.BookCustomizeRepository;
import com.elcom.report.repository.log.BookLoanCustomizeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ResourcePath.VERSION)
public class ReportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private BookCustomizeRepository bookCustomizeRepository;

    @Autowired
    private BookLoanCustomizeRepository bookLoanCustomizeRepository;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Value("${user.authen.use}")
    private String authenUse;

    @Value("${user.authen.http.url}")
    private String authenHttpUrl;

    // statistic book by author, first letter, category
    @RequestMapping(value = "/report/book/statistics", method = RequestMethod.GET)
    public ResponseEntity<Object> statisticsByAuthorOrLetterOrCategory(@RequestParam("keyword") String keyword,
               @RequestHeader Map<String, String> headerMap, HttpServletRequest request) throws JsonProcessingException {
        // request path
        String requestPath = request.getRequestURI();
        if(requestPath != null && requestPath.contains(ResourcePath.VERSION)){
            requestPath = requestPath.replace(ResourcePath.VERSION, "");
        }
        // Service
        int index = requestPath.indexOf("/", "/report/".length());
        String service = null;
        if(index != -1){
            service = requestPath.substring("/report/".length(), index);
        } else {
            service = requestPath.replace("/report/", "");
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
            userRpcRequest.setRequestMethod("POST");
            userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
            userRpcRequest.setVersion(ResourcePath.VERSION);
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
            if(keyword.equals("author")){
                List<BookAuthorDTO> bookDTOList = bookCustomizeRepository.statisticsByAuthor();
                return new ResponseEntity<>(new ResponseMessageDTO(bookDTOList), HttpStatus.OK);
            }
            if(keyword.equals("firstLetter")){
                List<BookFirstLetterDTO> bookFirstLetterDTOList = bookCustomizeRepository.statisticsByFirstLetter();
                return new ResponseEntity<>(new ResponseMessageDTO(bookFirstLetterDTOList), HttpStatus.OK);
            }
            if(keyword.equals("category")){
                List<BookCategoryDTO> bookDTOList = bookCustomizeRepository.statisticsByCategory();
                return new ResponseEntity<>(new ResponseMessageDTO(bookDTOList), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessageDTO("Not found data with keyword " + keyword), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessageDTO("Token đăng nhập hết hạn"), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/report/book-loan/count-book-borrow", method = RequestMethod.GET)
    public ResponseEntity<Object> countBookBorrow(@RequestParam(value = "fromDate") String fromDate, @RequestParam("toDate") String toDate,
               @RequestHeader Map<String, String> headerMap, HttpServletRequest request) throws JsonProcessingException{
        // request path
        String requestPath = request.getRequestURI();
        if(requestPath != null && requestPath.contains(ResourcePath.VERSION)){
            requestPath = requestPath.replace(ResourcePath.VERSION, "");
        }
        // Service
        int index = requestPath.indexOf("/", "/report/".length());
        String service = null;
        if(index != -1){
            service = requestPath.substring("/report/".length(), index);
        } else {
            service = requestPath.replace("/report/", "");
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
            userRpcRequest.setRequestMethod("POST");
            userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
            userRpcRequest.setVersion(ResourcePath.VERSION);
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
            try {
                Date start = Date.valueOf(fromDate);
                Date end = Date.valueOf(toDate);
                return new ResponseEntity<>(new ResponseMessageDTO(bookLoanCustomizeRepository.countBookBorrow(start, end)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseMessageDTO(e.toString()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessageDTO("Token đăng nhập hết hạn"), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/report/book-loan/max-book-borrow", method = RequestMethod.GET)
    public ResponseEntity<Object> getBookNameBorrowMax(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
               @RequestHeader Map<String, String> headerMap, HttpServletRequest request) throws JsonProcessingException{
        // request path
        String requestPath = request.getRequestURI();
        if(requestPath != null && requestPath.contains(ResourcePath.VERSION)){
            requestPath = requestPath.replace(ResourcePath.VERSION, "");
        }
        // Service
        int index = requestPath.indexOf("/", "/report/".length());
        String service = null;
        if(index != -1){
            service = requestPath.substring("/report/".length(), index);
        } else {
            service = requestPath.replace("/report/", "");
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
            userRpcRequest.setRequestMethod("POST");
            userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
            userRpcRequest.setVersion(ResourcePath.VERSION);
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
            try {
                Date start = Date.valueOf(fromDate);
                Date end = Date.valueOf(toDate);
                return new ResponseEntity<>(new ResponseMessageDTO(bookLoanCustomizeRepository.maxBookNameBorrow(start, end)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseMessageDTO(e.toString()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessageDTO("Token đăng nhập hết hạn"), HttpStatus.FORBIDDEN);
    }
}
