package com.elcom.library.messaging.rabbitmq;

import com.elcom.constant.ResourcePath;
import com.elcom.library.controller.AuthorController;
import com.elcom.library.controller.BookController;
import com.elcom.library.controller.CategoryController;
import com.elcom.library.exception.ValidationException;
import com.elcom.message.RequestMessage;
import com.elcom.message.ResponseMessage;
import com.elcom.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private AuthorController authorController;

    @Autowired
    private BookController bookController;

    @RabbitListener(queues = "${library.rpc.queue}")
    public String processService(String json) throws ValidationException {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);

            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                //GatewayDebugUtil.debug(requestPath, urlParam, pathParam, bodyParam, headerParam);
                LOGGER.info(" [-->] Server received requestPath =========>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + requestPath);

                switch (request.getRequestMethod()) {
                    case "GET":
                        if("/library/category".equalsIgnoreCase(requestPath)){
                            response = categoryController.getAllCategories(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        } else if("/library/author".equalsIgnoreCase(requestPath)){
                            response = authorController.getAllAuthors(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        } else if("/library/book".equalsIgnoreCase(requestPath)){
                            response = bookController.getAllBooks(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }
                        break;
                    case "POST":
                        if("/library/category".equalsIgnoreCase(requestPath)){
                            response = categoryController.createCategory(requestPath, request.getRequestMethod(), headerParam, bodyParam);
                        } else if("/library/author".equalsIgnoreCase(requestPath)){
                            response = authorController.createAuthor(requestPath, request.getRequestMethod(), headerParam, bodyParam);
                        } else if("/library/book".equalsIgnoreCase(requestPath)){
                            response = bookController.createBook(requestPath, request.getRequestMethod(), headerParam, bodyParam);
                        }
                        break;
                    case "PUT":
                        if("/library/category".equalsIgnoreCase(requestPath)){
                            response = categoryController.updateCategoryById(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        } else if("/library/author".equalsIgnoreCase(requestPath)){
                            response = authorController.updateAuthorById(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        } else if("/library/book".equalsIgnoreCase(requestPath)){
                            response = bookController.updateBookById(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        }
                        break;
                    case "DELETE":
                        if("/library/category".equalsIgnoreCase(requestPath)){
                            response = categoryController.deleteCategoryById(requestPath, headerParam, pathParam, request.getRequestMethod());
                        } else if("/library/author".equalsIgnoreCase(requestPath)){
                            response = authorController.deleteAuthorById(requestPath, headerParam, pathParam, request.getRequestMethod());
                        } else if("/library/book".equalsIgnoreCase(requestPath)){
                            response = bookController.deleteBookById(requestPath, headerParam, pathParam, request.getRequestMethod());
                        }
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response != null ? response.toJsonString() : "null");
            return response != null ? response.toJsonString() : null;
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> " + StringUtil.printException(ex));
            ex.printStackTrace();
        }
        return null;
    }
}
