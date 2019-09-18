package com.pancarte.architecte.controller;

import com.pancarte.architecte.model.*;
import com.pancarte.architecte.repository.*;
import com.pancarte.architecte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public ArchitectController(@Qualifier("roleRepository") RoleRepository roleRepository, @Qualifier("userRepository") UserRepository userRepository, MeetingRepository meetingRepository, @Qualifier("materialRepository") MaterialRepository materialRepository, @Qualifier("blockedMailRepository") BlockedMailRepository blockedMailRepository, ProjectRepository projectRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.meetingRepository = meetingRepository;
        this.materialRepository = materialRepository;
        this.blockedMailRepository = blockedMailRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @RequestMapping(value = {"/createUser"}, method = RequestMethod.GET)
    public void createUser(@RequestParam("last_name") String lastName, @RequestParam("first_name") String firstName, @RequestParam("email") String email, @RequestParam("password") String password) {
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

    @RequestMapping(value = {"/queryUserById"}, method = RequestMethod.GET)
    public String queryUserById(@RequestParam("id_user") int idUser) {
        User user = userRepository.findById(idUser);
        return "";
    }

    @RequestMapping(value = {"/deleteUserById"}, method = RequestMethod.GET)
    public void deleteUserById(@RequestParam("id_user") int idUser) {
        User user = userRepository.findById(idUser);
        userRepository.delete(user);
    }

    //todo:CRUD

    @RequestMapping(value = {"/addMaterial"}, method = RequestMethod.GET)
    public void addMaterial(@RequestParam("name") String name) {
        Material material = new Material();
        material.setName(name);
        materialRepository.save(material);
    }

    @RequestMapping(value = {"/addProject"}, method = RequestMethod.GET)
    public void addProject(@RequestParam("description") String description, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("material") String material, @RequestParam("surface") int surface) {

        Project project = new Project();
        project.setHidden(false);
        project.setDescription(description);
        project.setProjectName(name);
        project.setSurface(surface);
        project.setType(type);
        Material materials = materialRepository.findByName(material);
        project.setMaterials(new HashSet<Material>(Arrays.asList(materials)));
        projectRepository.save(project);
    }

    @RequestMapping(value = {"/updateProject"}, method = RequestMethod.GET)
    public void updateProject(@RequestParam("id_project") int idProject, @RequestParam("description") String description, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("material") String material, @RequestParam("surface") int surface) {

        Project projectUpdate = new Project();
        projectUpdate.setHidden(false);
        projectUpdate.setDescription(description);
        projectUpdate.setId(idProject);
        projectUpdate.setProjectName(name);
        projectUpdate.setSurface(surface);
        projectUpdate.setType(type);

        projectRepository.save(projectUpdate);
    }

    @RequestMapping(value = {"/deleteProject"}, method = RequestMethod.GET)
    public String deleteProject(@RequestParam("id_user") int idUser) {
        Project project = new Project();
        project.setHidden(true);

        return "";
    }

    @RequestMapping(value = {"/blockEmail"}, method = RequestMethod.GET)
    public void blockEmail(@RequestParam("email") String email, @RequestParam("cause") String cause) {
        BlockedMail blockedMail = new BlockedMail();
        blockedMail.setCause(cause);
        blockedMail.setEmail(email);
        blockedMailRepository.save(blockedMail);


    }

    @RequestMapping(value = {"/quoteMaking"}, method = RequestMethod.GET)
    public String quoteMaking(@RequestParam("id_user") int idUser) {

        return "";
    }

    //todo: send email to architeck ton notify a meeting and ask if he validate
    @RequestMapping(value = {"/sendMeeting"}, method = RequestMethod.GET)
    public void sendMeeting(@RequestParam("email") String email, @RequestParam("dateSended") Timestamp dateSended, @RequestParam("purpose") String purpose) {
        List<BlockedMail> blockedMails = blockedMailRepository.findAll();

        if (!Arrays.toString(blockedMails.toArray()).contains(email)) {
            //todo: send email =rdv en attente si dans 1
            //todo:send email to notify ac
            Meeting meeting = new Meeting();
            meeting.setClosed(false);
            Date now = Date.valueOf(LocalDate.now());
            meeting.setDateSended(new Timestamp(now.getTime()));
            meeting.setDateMeeting(dateSended);
            meeting.setInvitationSended(true);
            meeting.setPurpose(purpose);
            meeting.setMeetingValidate(false);
            meetingRepository.save(meeting);
        } else {
            //todo:send email = rdv annuler
        }
    }

    //todo: he validate the meeting or cancel it

    @RequestMapping(value = {"/verifyMeeting"}, method = RequestMethod.GET)
    public String verifyMeeting(@RequestParam("meeting") boolean meeting, @RequestParam("id_meeting") int idMeeting) {
        Meeting checkMeeting = meetingRepository.queryMeetingById(idMeeting);
        checkMeeting.setMeetingValidate(meeting);
        checkMeeting.setClosed(true);
        meetingRepository.save(checkMeeting);
        //todo:send email if ok or not

        return "";
    }

    //todo:check if 1 week passed and clean unvalidate meeting
    @RequestMapping(value = {"/cleanMeeting"}, method = RequestMethod.GET)
    public void cleanMeeting() {
        List<Meeting> meetings = meetingRepository.findAll();
        for (Meeting meeting : meetings
        ) {
            LocalDate sendedDate = meeting.getDateSended().toLocalDateTime().toLocalDate();

            LocalDate nextWeek = sendedDate.plus(1, ChronoUnit.WEEKS);

            if (sendedDate.compareTo(nextWeek) > 0) {
                meeting.setClosed(true);
                meetingRepository.save(meeting);
            }
        }


    }
}
