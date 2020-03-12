package com.atharva.auth.core.utils;

public class DaoConstants {

    public static final String updateCred = "update HashModel h set h.pass = :pass where h.id = :id";

    public static final String userDataWithProjectIdAndUserId = "from UserModel u where u.projectId = :projectId and u.id = :id";
}
