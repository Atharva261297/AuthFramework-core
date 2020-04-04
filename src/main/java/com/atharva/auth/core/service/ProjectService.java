package com.atharva.auth.core.service;

import com.atharva.auth.core.dao.ProjectHashRepository;
import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.model.ProjectHashModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

@Service
@EnableTransactionManagement
public class ProjectService {

    @Autowired
    private ProjectHashRepository dao;

    Logger log = LoggerFactory.getLogger(ProjectService.class);

    public ErrorCodes add(String id, String pass) {
        log.debug("Adding new project - {}", id);
        if (dao.existsById(id)) {
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            dao.save(new ProjectHashModel(id, pass.getBytes()));
            return ErrorCodes.SUCCESS;
        }
    }

    public ErrorCodes verify(String id, String pass) {
        log.debug("Verifying project - {}", id);
        if (dao.existsById(id)) {
            if ( Arrays.equals(pass.getBytes(), dao.getOne(id).getPass()) ) {
                log.debug("Verifying project - {} - successful", id);
                return ErrorCodes.SUCCESS;
            } else {
                log.debug("Verifying project - {} - failed - INCORRECT_PASS", id);
                return ErrorCodes.PROJECT_PASS_INCORRECT;
            }
        } else {
            log.debug("Verifying project - {} - failed - INCORRECT_ID", id);
            return ErrorCodes.PROJECT_ID_INCORRECT;
        }
    }

    public String getKey(String id) {
        log.debug("Get project key - {}", id);
        if (dao.existsById(id)) {
            return new String(dao.getOne(id).getPass());
        } else {
            return StringUtils.EMPTY;
        }
    }

//    public ErrorCodes update(String id, String oldPass, String newPass) {
//        ErrorCodes code = verify(id, oldPass);
//        if (code == ErrorCodes.SUCCESS) {
//            dao.update(id, newPass.getBytes());
//            return ErrorCodes.SUCCESS;
//        } else {
//            return code;
//        }
//    }

    public ErrorCodes delete(String id, String pass) {
        ErrorCodes code = verify(id, pass);
        if (code == ErrorCodes.SUCCESS) {
            dao.deleteById(id);
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }
}
