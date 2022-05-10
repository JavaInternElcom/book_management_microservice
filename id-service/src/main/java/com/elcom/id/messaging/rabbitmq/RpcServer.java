package com.elcom.id.messaging.rabbitmq;

import com.elcom.constant.ResourcePath;
import com.elcom.id.controller.AuthenController;
import com.elcom.id.controller.StudentController;
import com.elcom.id.exception.ValidationException;
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
    private AuthenController authenController;

    @Autowired
    private StudentController studentController;

    @RabbitListener(queues = "${user.rpc.queue}")
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
                        if ("/user/student".equalsIgnoreCase(requestPath)) {
                            response = studentController.getAllStudents(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                        }
                        break;
                    case "POST":
                        if("/user/login".equalsIgnoreCase(requestPath)){
                            response = authenController.userLogin(requestPath, headerParam, bodyParam);
<<<<<<< HEAD
                        } else if("/user/student".equalsIgnoreCase(requestPath)){
                            response = studentController.createStudent(request.getRequestPath(), request.getRequestMethod(), headerParam, bodyParam);
                        } else if("/user/authentication".equalsIgnoreCase(requestPath)){
                            response = authenController.authorized(requestPath, headerParam);
=======
>>>>>>> 1bd077859e474795f299e4022af917512a689d80
                        }
                        break;
                    case "PUT":
                        if("/user/student".equalsIgnoreCase(requestPath)){
                            response = studentController.updateStudent(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        }
                        break;
                    case "DELETE":
                        if("/user/student".equalsIgnoreCase(requestPath)){
                            response = studentController.deleteStudent(requestPath, headerParam, pathParam, request.getRequestMethod());
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