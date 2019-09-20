package com.pancarte.architecte.controller;

import com.pancarte.architecte.model.*;
import com.pancarte.architecte.repository.*;
import com.pancarte.architecte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
public class ArchitectController {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final RoleRepository roleRepository;
    private final MaterialRepository materialRepository;
    private final BlockedMailRepository blockedMailRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private static final String SUBJECT = "Rendez vous avec M.architect ";

    @Autowired
    public ArchitectController(@Qualifier("roleRepository") RoleRepository roleRepository, @Qualifier("userRepository") UserRepository userRepository, MeetingRepository meetingRepository, @Qualifier("materialRepository") MaterialRepository materialRepository, @Qualifier("blockedMailRepository") BlockedMailRepository blockedMailRepository,@Qualifier("projectRepository") ProjectRepository projectRepository, UserService userService, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.meetingRepository = meetingRepository;
        this.materialRepository = materialRepository;
        this.blockedMailRepository = blockedMailRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    void sendEmail(String email, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
    }

    @RequestMapping(value = {"/createUser"}, method = RequestMethod.GET)
    public void createUser(@RequestParam("last_name") String lastName,
                           @RequestParam("first_name") String firstName,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password) {
        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setEmail(email);
        user.setPassword(password);

        userService.saveUser(user);
    }

    @RequestMapping(value = {"/queryUsers"}, method = RequestMethod.GET)
    public List<User> queryAllUser() {

        return userRepository.findAll();
    }

    @RequestMapping(value = {"/queryUserByName"}, method = RequestMethod.GET)
    public User queryUserByName(@RequestParam("email") String email) {
        return userRepository.queryUser(email);
    }

    @RequestMapping(value = {"/queryUserById"}, method = RequestMethod.GET)
    public User queryUserById(@RequestParam("id_user") int idUser) {
        User user = userRepository.findById(idUser);
        return user;
    }

    @RequestMapping(value = {"/deleteUserById"}, method = RequestMethod.GET)
    public void deleteUserById(@RequestParam("id_user") int idUser) {
        User user = userRepository.findById(idUser);
        user.setHidden(true);
        userRepository.save(user);
    }

    //todo:CRUD

    @RequestMapping(value = {"/addMaterial"}, method = RequestMethod.GET)
    public void addMaterial(@RequestParam("name") String name,
                            @RequestParam("thickness") int thickness,
                            @RequestParam("opaque") boolean opaque) {
        Material material = new Material();
        material.setOpaque(opaque);
        material.setThickness(thickness);
        material.setName(name);
        materialRepository.save(material);
    }

    @RequestMapping(value = {"/addProject"}, method = RequestMethod.GET)
    public void addProject(@RequestParam("description") String description, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("material") String material, @RequestParam("surface") int surface) {

        Project project = new Project();
        project.setHidden(false);
        project.setProjectName(name);
        project.setDescription(description);

        project.setSurface(surface);
        project.setType(type);
        Material materials = materialRepository.findByName(material);
        project.setMaterials(new HashSet<Material>(Arrays.asList(materials)));
        projectRepository.save(project);
    }

    @RequestMapping(value = {"/updateProject"}, method = RequestMethod.GET)
    public void updateProject(@RequestParam("id_project") int idProject, @RequestParam("description") String description, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("material") String material, @RequestParam("surface") int surface) {

        Project projectUpdate = projectRepository.queryProjectById(idProject);

        projectUpdate.setDescription(description);
        projectUpdate.setProjectName(name);
        projectUpdate.setSurface(surface);
        projectUpdate.setType(type);

        projectRepository.save(projectUpdate);
    }

    @RequestMapping(value = {"/deleteProject"}, method = RequestMethod.GET)
    public void deleteProject(@RequestParam("id_project") int idProject) {
        Project project = projectRepository.queryProjectById(idProject);
        project.setHidden(true);

        projectRepository.save(project);
    }

    @RequestMapping(value = {"/blockEmail"}, method = RequestMethod.GET)
    public void blockEmail(@RequestParam("email") String email, @RequestParam("cause") String cause) {
        BlockedMail blockedMail = new BlockedMail();
        blockedMail.setCause(cause);
        blockedMail.setEmail(email);
        blockedMailRepository.save(blockedMail);
    }

    @RequestMapping(value = {"/queryBlockedEmail"}, method = RequestMethod.GET)
    public List<BlockedMail> queryblockedEmail(){

        return blockedMailRepository.findAll();
    }
    @RequestMapping(value = {"/quoteMaking"}, method = RequestMethod.GET)
    public String quoteMaking(@RequestParam("id_user") int idUser) {

        return "";
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    //todo: send email to architeck ton notify a meeting and ask if he validate
    @RequestMapping(value = {"/sendMeeting"}, method = RequestMethod.GET)
    public void sendMeeting(@RequestParam("email") String email, @RequestParam("dateSended") Timestamp dateSended, @RequestParam("purpose") String purpose) {
        List<BlockedMail> blockedMails = blockedMailRepository.findAll();

        if (!Arrays.toString(blockedMails.toArray()).contains(email) && isValidEmailAddress(email)) {
            User userArchitect = userRepository.queryUser("marj12@live.fr");
            System.out.println(email);
            Meeting meeting = new Meeting();
            meeting.setClosed(false);
            Date now = Date.valueOf(LocalDate.now());
            meeting.setDateSended(new Timestamp(now.getTime()));
            meeting.setDateMeeting(dateSended);
            meeting.setInvitationSended(true);
            meeting.setPurpose(purpose);
            meeting.setMeetingValidate(false);
            meeting.setEmail(email);
            meetingRepository.save(meeting);
            List<Meeting> allMeeting = meetingRepository.findAll();
            Meeting lastmeeting = allMeeting.get(allMeeting.size() - 1);

            sendEmail(userArchitect.getEmail(),
                    "Prise de rendez vous avec :" + email + " le :" + dateSended,
                    purpose +
                            "\n Rendez vous avec :" + email + " le :" + dateSended
                            + "\n pour valider le rendez vous cliquer sur : http://localhost:9090/verifyMeeting?id_meeting=" + lastmeeting.getId() + "&meeting=true"
                            + "\n pour annuler le rendez vous cliquer sur : http://localhost:9090/verifyMeeting?id_meeting=" + lastmeeting.getId() + "&meeting=false"
            );
        } else {
            // possible mise en place d'envoie email pour les compte bloqué.

        }
    }


    @RequestMapping(value = {"/queryMeeting"}, method = RequestMethod.GET)
    public Meeting queryMeeting(@RequestParam("id_meeting") int idMeeting){

        return meetingRepository.queryMeetingById(idMeeting);
    }   @RequestMapping(value = {"/queryAllMeeting"}, method = RequestMethod.GET)
    public List<Meeting> queryAllMeeting(){

        return meetingRepository.findAll();
    }

    @RequestMapping(value = {"/verifyMeeting"}, method = RequestMethod.GET)
    public void verifyMeeting(@RequestParam("meeting") boolean meeting, @RequestParam("id_meeting") int idMeeting) {
        Meeting checkMeeting = meetingRepository.queryMeetingById(idMeeting);

        User userArchitect = userRepository.queryUser("marj12@live.fr");

        //todo:send email if ok or not
        if (meeting) {
            sendEmail(checkMeeting.getEmail(),
                    SUBJECT,
                    "Votre rendez vous avec Mr Architect est confirmé pour le :" + checkMeeting.getDateMeeting());
        } else {
            sendEmail(checkMeeting.getEmail(),
                    SUBJECT,
                    "Votre rendez vous avec Mr Architect est annulé pour le :" + checkMeeting.getDateMeeting());
        }
        checkMeeting.setMeetingValidate(meeting);
        checkMeeting.setClosed(true);

        meetingRepository.save(checkMeeting);
    }

    //todo:check if 1 week passed and clean unvalidate meeting
    @Scheduled(cron = "	0 0 0 1/1 * ? ")// tout les jours à 0h00
    @RequestMapping(value = {"/cleanMeeting"}, method = RequestMethod.GET)
    public void cleanMeeting() {
        List<Meeting> meetings = meetingRepository.findAll();
        User userArchitect = userRepository.queryUser("marj12@live.fr");
        for (Meeting meeting : meetings
        ) {
            LocalDate sendedDate = meeting.getDateSended().toLocalDateTime().toLocalDate();

            LocalDate nextWeek = sendedDate.plus(1, ChronoUnit.WEEKS);

            if (sendedDate.compareTo(nextWeek) > 0 && !meeting.isMeetingValidate() && !meeting.isClosed()) {
                sendEmail(meeting.getEmail(),
                        SUBJECT,
                        "Votre rendez vous avec Mr Architect est annulé pour le :" + meeting.getDateMeeting()
                );
                sendEmail(userArchitect.getEmail(),
                        SUBJECT,
                        "Annulation automatique du rendez vous  le :" + meeting.getDateMeeting()+" avec :"+meeting.getEmail()
                );
                meeting.setClosed(true);
                meetingRepository.save(meeting);
            }
        }
    }
}
