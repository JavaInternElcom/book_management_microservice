package com.elcom.cronjob.schedulers;

import com.elcom.cronjob.repository.BookLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class Schedulers {

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    public JavaMailSender mailSender;

    public String sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("diephang3110@gmail.com");
        message.setSubject("Bao cao so luong sach qua han phai tra.");
        message.setText("Xin chao admin, danh sach sach qua han phai tra la: \n"
                + bookLoanRepository.getBookLoanExpired() );

        mailSender.send(message);

        return "Email Sent!";
    }

    // job in ra thời gian hiện tại, set delay 2 giây giữa những lần xử lý, sync, xử lý xong thì mới bắt đầu tính delay (Sync)
    @Scheduled(fixedDelay = 2000)
    public void timePrint() throws InterruptedException {
        sendEmail();
//        System.out.println("[fixedDelay] - " + LocalDateTime.now());
    }

    // Giống fixedDelay nhưng chạy A-sync
//    @Scheduled(fixedRate = 2000)
//    public void scheduleFixedRateTask() throws InterruptedException {
//        System.out.println("[fixedRate] - " + LocalDateTime.now());
//    }

    /*
     Dùng biểu thức cron tùy biến để chạy job, list biểu thức: https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm
     vd: job này chạy vào 7h sáng mỗi ngày
    */
//    @Scheduled(cron = "0 0 7 * * ?")
//    public void scheduleTaskUsingCronExpression() throws InterruptedException {
//        sendEmail();
//        System.out.println("Successfully");
//    }
}