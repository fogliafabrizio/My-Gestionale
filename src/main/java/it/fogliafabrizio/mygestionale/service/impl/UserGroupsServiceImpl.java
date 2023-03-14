package it.fogliafabrizio.mygestionale.service.impl;

import it.fogliafabrizio.mygestionale.model.GroupRequest;
import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import it.fogliafabrizio.mygestionale.service.UserGroupsService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupsServiceImpl implements UserGroupsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserGroupsRepository groupsRepository;

    @Override
    public String createGroup(GroupRequest request, Users user) {
        UserGroups group = new UserGroups();
        group.setName(request.getGroupName());
        group.setDescription(request.getGroupDescription());
        group.setUserAdmin(user);
        group.setVisibility(request.getGroupVisibility());
        List<Users> userMembers = new ArrayList<>();
        for(Long id : request.getUserIds()){
          userMembers.add(usersRepository.findById(id).orElseThrow());
        }
        userMembers.add(user);
        group.setUserMembers(userMembers);
        groupsRepository.save(group);
        return "OK";
    }
}
