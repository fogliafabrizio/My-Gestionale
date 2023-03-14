package it.fogliafabrizio.mygestionale.service;

import it.fogliafabrizio.mygestionale.model.GroupRequest;
import it.fogliafabrizio.mygestionale.model.Users;
import org.apache.catalina.User;

public interface UserGroupsService {

    public String createGroup(GroupRequest request, Users user);
}
