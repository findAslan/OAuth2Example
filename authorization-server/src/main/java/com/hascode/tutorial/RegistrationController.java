package com.hascode.tutorial;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /*@Autowired
    private IUserService userService;

    @Autowired
    private ISecurityUserService securityUserService;

    @Autowired
    private ICaptchaService captchaService;*/

    @Autowired
    private MessageSource messages;

 /*   @Autowired
    private JavaMailSender mailSender;*/
    
    @Autowired
	private AccountRepository repo;

    /*@Autowired
    private ApplicationEventPublisher eventPublisher;*/
    
   /* @Autowired
    private MailSender mailSender;

    private SimpleMailMessage templateMessage;
    */
    
   /* @Autowired
	private JavaMailSender mailSender;*/
    
    /*@Value("${send.from.email}")
    private String fromEmail;

    @Value("${send.to.email}")
    private String toEmail;*/


    @Autowired
    private Environment env;

    public RegistrationController() {
        super();
    }

    // Registration

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registerUserAccount( final HttpServletRequest request ,@RequestBody Account account) {
    	//@Valid final UserDto accountDto,
        LOGGER.debug("Registering user account with information: {}",account);//, accountDto
        //repo.save(account);
       /* final User registered = userService.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));*/
        
        System.out.println("Starting Send...");
        String[] to = { "lohiyarohitmau@gmail.com" };
       // sendMail("navin.oracle@gmail.com", to, "abcd", "dadsdfdsfsdfd");
        System.out.println("Finished Send...");
        
        return new GenericResponse("success");
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse confirmRegistration(final Locale locale, @RequestParam("token") final String token) throws UnsupportedEncodingException {
    	
            final Account user = repo.findByToken(token);
            
            if(user != null){
            	return new GenericResponse("success");
            } else{
            	return new GenericResponse("fail","failed validation");
            }
            
            
            /*System.out.println(user);
            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();*/
    }

    // user activation - verification

    @RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        /*final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));*/
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    // Reset password
    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        /*final User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }
        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));*/
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

    @RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        /*final String result = securityUserService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }*/
        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public GenericResponse savePassword(final Locale locale) {
    	//, @Valid PasswordDto passwordDto
       /* final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeUserPassword(user, passwordDto.getNewPassword());*/
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    // change user password
    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public GenericResponse changeUserPassword(final Locale locale) {
    	//, @Valid PasswordDto passwordDto
        /*final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());*/
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

   /* @RequestMapping(value = "/user/update/2fa", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA) throws UnsupportedEncodingException {
        final User user = userService.updateUser2FA(use2FA);
        if (use2FA) {
            return new GenericResponse(userService.generateQRUrl(user));
        }
        return null;
    }*/

    // ============== NON-API ============

    /*private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final Account user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + user.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final Account user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, Account user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }*/
    
/*public void sendMail(String from, String[] to, String subject, String msg) {
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(msg, "text/html");
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(mimeMessage);

	}
*/
}
