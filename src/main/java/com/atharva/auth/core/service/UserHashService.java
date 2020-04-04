package com.atharva.auth.core.service;

import com.atharva.auth.core.dao.UserHashRepository;
import com.atharva.auth.core.model.*;
import com.atharva.auth.core.utils.hash.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
//@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@EnableTransactionManagement
public class UserHashService {

    @Autowired private UserHashRepository dao;

    Logger log = LoggerFactory.getLogger(UserHashService.class);

    @Transactional
    public Response add(String userId, String projectId, String pass) {
        UserId id = new UserId(userId, projectId);
        if (dao.existsById(id)) {
            return new Response(ErrorCodes.ID_ALREADY_EXITS, null);
        } else {
            UserHashModel s = new UserHashModel(HashUtils.getHashModel(userId, pass), projectId);
            dao.save(s);
            return new Response(ErrorCodes.SUCCESS, Base64.getEncoder().encodeToString(s.getPass()));
        }
    }

    public Response verify(String userId, String projectId, String pass) {
        UserId id = new UserId(userId, projectId);
        log.debug("Login 2 : " + id);
        if (dao.existsById(id)) {
            UserHashModel fromDb = dao.getOne(id);
            if (HashUtils.verifyHashModel(new HashModel(fromDb.getId(), fromDb.getPass()), pass)) {
                return new Response(ErrorCodes.SUCCESS, Base64.getEncoder().encodeToString(fromDb.getPass()));
            } else {
                return new Response(ErrorCodes.PASS_INCORRECT, null);
            }
        } else {
            return new Response(ErrorCodes.ID_INCORRECT, null);
        }
    }

    @Transactional
    public ErrorCodes update(String oldUserId, String oldPass, String newUserId, String newPass, String projectId) {
        Response response = verify(oldUserId, projectId, oldPass);
        if (response.getCode() == ErrorCodes.SUCCESS) {
            if (oldUserId.equals(newUserId)) {
                UserHashModel fromDb = dao.getOne(new UserId(oldUserId, projectId));
                fromDb.setPass(HashUtils.getHashModel(oldUserId, newPass).getPass());
                dao.save(fromDb);
                return ErrorCodes.SUCCESS;
            } else {
                return ErrorCodes.ID_NOT_SAME;
            }
        } else {
            return response.getCode();
        }
    }

    @Transactional
    public ErrorCodes reset(String userId, String projectId, String pass) {
        UserId id = new UserId(userId, projectId);
        if (dao.existsById(id)) {
            UserHashModel fromDb = dao.getOne(id);
            fromDb.setPass(HashUtils.getHashModel(userId, pass).getPass());
            dao.save(fromDb);
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            return ErrorCodes.ID_INCORRECT;
        }
    }

    @Transactional
    public ErrorCodes delete(String userId, String projectId, String pass) {
        UserId id = new UserId(userId, projectId);
        Response response = verify(userId, projectId, pass);
        if (response.getCode() == ErrorCodes.SUCCESS) {
            dao.deleteById(id);
            return ErrorCodes.SUCCESS;
        } else {
            return response.getCode();
        }
    }
}
