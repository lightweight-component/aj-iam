package com.ajaxjs.iam.jwtv2;

import lombok.Data;

/**
 * The header of a JWT.
 */
@Data
public class Header {
    /**
     * The algorithm used to sign the JWT.
     */
    String alg;

    /**
     * The type of the JWT.
     */
    String typ;
}
