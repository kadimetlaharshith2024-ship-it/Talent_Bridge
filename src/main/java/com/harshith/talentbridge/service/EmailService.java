package com.harshith.talentbridge.service;

import com.harshith.talentbridge.entity.Application;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendInterviewScheduledEmail(Application app) {
        String studentEmail = app.getStudent().getUser().getEmail();
        String studentName = app.getStudent().getUser().getName();
        String jobTitle = app.getJob().getTitle();
        String companyName = app.getJob().getRecruiter().getCompanyName();

        String formattedTime = (app.getInterviewTime() != null)
                ? app.getInterviewTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a"))
                : "To be confirmed";

        String meetingLink = (app.getInterviewLink() != null && !app.getInterviewLink().isBlank())
                ? app.getInterviewLink()
                : "Link will be shared shortly";

        String subject = "Interview Scheduled: " + jobTitle + " at " + companyName;

        String htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e2e8f0; rounded: 16px;">
                <h2 style="color: #4338ca;">Interview Invitation</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Congratulations! <strong>%s</strong> has scheduled an interview session for the <strong>%s</strong> opening.</p>
                
                <div style="background-color: #f8fafc; padding: 15px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #4338ca;">
                    <p style="margin: 5px 0;"><strong>Round:</strong> %s</p>
                    <p style="margin: 5px 0;"><strong>Date & Time:</strong> %s</p>
                    <p style="margin: 5px 0;"><strong>Meeting Link:</strong> <a href="%s" style="color: #4f46e5;">%s</a></p>
                    <p style="margin: 5px 0;"><strong>Instructions:</strong> %s</p>
                </div>

                <p style="color: #64748b; font-size: 13px;">Please make sure to join 5 minutes early in a quiet environment.</p>
                <br>
                <p style="font-size: 12px; color: #94a3b8;">TalentBridge Placement Cell</p>
            </div>
            """.formatted(
                studentName,
                companyName,
                jobTitle,
                app.getInterviewRound() != null ? app.getInterviewRound() : "Technical Interview",
                formattedTime,
                meetingLink,
                meetingLink,
                app.getRecruiterFeedback() != null ? app.getRecruiterFeedback() : "Please prepare your project walkthrough."
        );

        sendHtmlEmail(studentEmail, subject, htmlContent);
    }

    @Async
    public void sendRejectionEmail(Application app) {
        String studentEmail = app.getStudent().getUser().getEmail();
        String studentName = app.getStudent().getUser().getName();
        String jobTitle = app.getJob().getTitle();
        String companyName = app.getJob().getRecruiter().getCompanyName();
        String feedback = app.getRecruiterFeedback() != null ? app.getRecruiterFeedback() : "Application evaluated. Position filled based on specific profile criteria.";

        String subject = "Application Update: " + jobTitle + " at " + companyName;

        String htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 16px;">
                <h2 style="color: #0f172a;">Application Status Update</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Thank you for taking the time to apply for the <strong>%s</strong> position at <strong>%s</strong>.</p>
                <p>After careful evaluation of candidate records, the recruitment team has decided not to move forward with your application at this stage.</p>
                
                <div style="background-color: #fff1f2; padding: 15px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #e11d48;">
                    <p style="margin: 0; color: #881337;"><strong>Recruiter Feedback:</strong> %s</p>
                </div>

                <p style="color: #64748b; font-size: 13px;">We encourage you to explore other open placement opportunities on TalentBridge.</p>
                <br>
                <p style="font-size: 12px; color: #94a3b8;">TalentBridge Placement Cell</p>
            </div>
            """.formatted(studentName, jobTitle, companyName, feedback);

        sendHtmlEmail(studentEmail, subject, htmlContent);
    }

    @Async
    public void sendShortlistEmail(Application app) {
        String studentEmail = app.getStudent().getUser().getEmail();
        String studentName = app.getStudent().getUser().getName();
        String jobTitle = app.getJob().getTitle();
        String companyName = app.getJob().getRecruiter().getCompanyName();

        String subject = "You've Been Shortlisted: " + jobTitle + " at " + companyName;

        String htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 16px;">
                <h2 style="color: #059669;">Application Shortlisted!</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your application for <strong>%s</strong> at <strong>%s</strong> has been <strong>shortlisted</strong> for further evaluation.</p>
                <p>The recruitment team is reviewing your profile and will update the next evaluation step shortly.</p>
                <br>
                <p style="font-size: 12px; color: #94a3b8;">TalentBridge Placement Cell</p>
            </div>
            """.formatted(studentName, jobTitle, companyName);

        sendHtmlEmail(studentEmail, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("Email successfully dispatched to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}