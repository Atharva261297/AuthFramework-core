package com.atharva.auth.core.service;

import com.atharva.auth.core.dao.AdminHashRepository;
import com.atharva.auth.core.model.AdminHashModel;
import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.model.HashModel;
import com.atharva.auth.core.utils.hash.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@EnableTransactionManagement
public class AdminHashService {

    @Autowired
    private AdminHashRepository dao;

    Logger log = LoggerFactory.getLogger(AdminHashService.class);

    @Transactional
    public ErrorCodes add(String id, String pass) {
        log.debug("Adding new admin - {}", id);
        if (dao.existsById(id)) {
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            dao.save(new AdminHashModel(HashUtils.getHashModel(id, pass)));
            return ErrorCodes.SUCCESS;
        }
    }

    public ErrorCodes verify(String id, String pass) {
        log.debug("Verifying admin - {}", id);
        if (dao.existsById(id)) {
            AdminHashModel fromDb = dao.getOne(id);
            if (HashUtils.verifyHashModel(new HashModel(fromDb.getId(), fromDb.getPass()), pass)) {
                log.debug("Verifying admin - {} - successful", id);
                return ErrorCodes.SUCCESS;
            } else {
                log.debug("Verifying admin - {} - failed - INCORRECT_PASS", id);
                return ErrorCodes.PASS_INCORRECT;
            }
        } else {
            log.debug("Verifying admin - {} - failed - INCORRECT_ID", id);
            return ErrorCodes.ID_INCORRECT;
        }
    }

    @Transactional
    public ErrorCodes update(String oldId, String oldPass, String newId,String newPass) {
        log.debug("Updating admin - {}", oldId);
        ErrorCodes code = verify(oldId, oldPass);
        if (code == ErrorCodes.SUCCESS) {
            if (oldId.equals(newId)) {
                AdminHashModel fromDb = dao.getOne(oldId);
                fromDb.setPass(HashUtils.getHashModel(oldId, newPass).getPass());
                dao.save(fromDb);
                log.debug("Updating admin - {} - successful", oldId);
                return ErrorCodes.SUCCESS;
            } else {
                log.debug("Updating admin - {} - failed - ID_CANNOT_BE_CHANGED", oldId);
                return ErrorCodes.ID_NOT_SAME;
            }
        } else {
            log.debug("Updating admin - {} - failed", oldId);
            return code;
        }
    }

    @Transactional
    public ErrorCodes reset(String id, String pass) {
        log.debug("Resetting admin - {}", id);
        if (dao.existsById(id)) {
            AdminHashModel fromDb = dao.getOne(id);
            fromDb.setPass(HashUtils.getHashModel(id, pass).getPass());
            dao.save(fromDb);
            log.debug("Resetting admin - {} - successful", id);
            return ErrorCodes.ID_ALREADY_EXITS;
        } else {
            log.debug("Resetting admin - {} - failed - INCORRECT_ID", id);
            return ErrorCodes.ID_INCORRECT;
        }
    }

    @Transactional
    public ErrorCodes delete(String id, String pass) {
        log.debug("Deleting admin - {}", id);
        ErrorCodes code = verify(id, pass);
        if (code == ErrorCodes.SUCCESS) {
            dao.deleteById(id);
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }
}
