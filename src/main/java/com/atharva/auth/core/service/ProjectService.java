package com.atharva.auth.core.service;

import com.atharva.auth.core.dao.HashRepository;
import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.model.HashModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProjectService {

    @Autowired
    private HashRepository dao;

    public ErrorCodes add(String id, String pass) {
        if (dao.existsById(id)) {
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            dao.save(new HashModel(id, pass.getBytes()));
            return ErrorCodes.SUCCESS;
        }
    }

    public ErrorCodes verify(String id, String pass) {
        if (dao.existsById(id)) {
            if ( Arrays.equals(pass.getBytes(), dao.getOne(id).getPass()) ) {
                return ErrorCodes.SUCCESS;
            } else {
                return ErrorCodes.PASS_INCORRECT;
            }
        } else {
            return ErrorCodes.ID_INCORRECT;
        }
    }

    public String getKey(String id) {
        if (dao.existsById(id)) {
            return new String(dao.getOne(id).getPass());
        } else {
            return StringUtils.EMPTY;
        }
    }

    public ErrorCodes update(String id, String oldPass, String newPass) {
        ErrorCodes code = verify(id, oldPass);
        if (code == ErrorCodes.SUCCESS) {
            dao.update(id, newPass.getBytes());
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }

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
